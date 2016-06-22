package com.kuailework;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.kuailework.util.ObjectTool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by lufei on 16/6/3.
 */
public class PipleLineMain {

    private static Log log = LogFactory.getLog(PipleLineMain.class);
    private CanalConnector connector;
    private DefaultMQProducer producer;
    private static String mqGroup = "group";
    private String mqTopic;

    public PipleLineMain(String cannalIp, String mqNameIp, String mqTopic) {
        this.mqTopic = mqTopic;
        connector = CanalConnectors.newSingleConnector(new InetSocketAddress(cannalIp,
                11111), "group", "", "");
        producer = new DefaultMQProducer(mqGroup);
        producer.setNamesrvAddr(mqNameIp);
//        start();
    }

    public void start() {
        int batchSize = 1;

        try {
            producer.start();
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                com.alibaba.otter.canal.protocol.Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    log.info("empty data !");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    log.info("data size " + size);
                    sendMsg(message);
                }
                connector.ack(batchId); // 提交确认
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }


    private void sendMsg(com.alibaba.otter.canal.protocol.Message message) {
        byte[] msg = ObjectTool.toByteArray(message);
        List<Entry> entrys = message.getEntries();
        log.info(entrys);
        try {
            com.alibaba.rocketmq.common.message.Message mqMessage = new com.alibaba.rocketmq.common.message.Message(mqTopic, msg);
            SendResult sendResult = producer.send(mqMessage);
            log.info(sendResult);
        } catch (Exception e) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e1) {
            }
            log.error("sendMsg producer.send(mqMessage) " + e.getMessage(), e);
        }
    }

    private void close() {
        connector.disconnect();
        producer.shutdown();
    }


}
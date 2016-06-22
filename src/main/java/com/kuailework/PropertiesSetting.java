package com.kuailework;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lufei on 16/6/22.
 */
//@ConfigurationProperties
public class PropertiesSetting {

    private String mqTopic;
    private String mqNameIp;
    private String canalIp;

    public String getMqTopic() {
        return mqTopic;
    }

    public void setMqTopic(String mqTopic) {
        this.mqTopic = mqTopic;
    }

    public String getMqNameIp() {
        return mqNameIp;
    }

    public void setMqNameIp(String mqNameIp) {
        this.mqNameIp = mqNameIp;
    }

    public String getCanalIp() {
        return canalIp;
    }

    public void setCanalIp(String canalIp) {
        this.canalIp = canalIp;
    }
}

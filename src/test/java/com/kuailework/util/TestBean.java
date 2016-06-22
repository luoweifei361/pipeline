package com.kuailework.util;

import java.io.Serializable;

/**
 * Created by lufei on 16/6/3.
 */
public class TestBean implements Serializable {


    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static void main(String[] args) {
        TestBean tb = new TestBean();
        tb.setName("daqing");
        tb.setValue("1234567890");

        byte[] b = ObjectTool.toByteArray(tb);
        System.out.println(new String(b));

        System.out.println("=======================================");

        TestBean teb = (TestBean) ObjectTool.toObject(b);
        System.out.println(teb.getName());
        System.out.println(teb.getValue());
    }

}

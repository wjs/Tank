package com.pj.Tank.network.testData;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 13-12-24
 * Time: 下午1:15
 * To change this template use File | Settings | File Templates.
 */
public class TestClass implements Serializable {
    private String name;
    private String ip;

    public TestClass(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}

package com.pj.Tank.network.socket;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;

public class Data implements Serializable {
    private static final long serialVersionUID = 732001046714137404L;
	private long serialNumber;
    private long sendTime;
    private String sender_ip;
    private String receiver_ip;
    private Serializable data;

	public Data(Serializable data) {
		super();
		this.data = data;
        this.sendTime = Calendar.getInstance().getTimeInMillis();
	}
    public Data(Serializable data, long serialNumber, String sender_ip, String receiver_ip) {
       this(data);
       this.serialNumber = serialNumber;
       this.sender_ip = sender_ip;
       this.receiver_ip = receiver_ip;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }
	public long getSerialNumber() {
		return this.serialNumber;
	}
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
    public void setSendTime() {
        this.sendTime = Calendar.getInstance().getTimeInMillis();
    }
    public long getSendTime() {
        return this.sendTime;
    }
    public void setSender_ip(String sender_ip) {
        this.sender_ip = sender_ip;
    }
    public String getSender_ip() {
        return this.sender_ip;
    }
    public void setReceiver_ip(String receiver_ip) {
        this.receiver_ip = receiver_ip;
    }
    public String getReceiver_ip() {
        return this.receiver_ip;
    }
    public void setData(Serializable data) {
        this.data = data;
    }
    public Serializable getData() {
        return this.data;
    }

    public static String getLocalAddress() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch(Exception e) {

        }
        return "127.0.0.1";
    }
}

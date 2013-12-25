package com.pj.Tank.network.socket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import java.net.InetAddress;
/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-12-15
 * Time: 下午5:38
 * To change this template use File | Settings | File Templates.
 */
public interface Connectable {
    public void send(Serializable data);
    public HashMap<InetAddress,Serializable> getLatest();
    public Vector<InetAddress> getPlayers();
}

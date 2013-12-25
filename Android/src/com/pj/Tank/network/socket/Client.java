package com.pj.Tank.network.socket;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.*;
import com.pj.Tank.network.socket.constants.Constants;

public class Client extends CSFather implements Runnable, Connectable {
    private static long serialNumber = 0;
	private DatagramSocket socket;
    private InetSocketAddress server;
    private final Semaphore ready = new Semaphore(1);
    private Serializable data;
    private boolean stop;

    public Client(String ip) {
        try {
            ready.acquire();
            stop = false;
            this.socket =  new DatagramSocket(Constants.CLIENTPORT);
            socket.setSoTimeout(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        server = new InetSocketAddress(ip,Constants.SERVERPORT);
    }

    @Override
    public void run() {
        try {
            System.out.println(socket);
//          System.out.println(server);
            this.makeConnection();
            System.out.println("client connection done");
            while(stop == false) {
                listen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("client stopped!");

    }

    private void makeConnection() {
        try {
            naiveSend("join");
            while(true) {
                Data data = naiveReceive();
                if(data.getData() instanceof String) {
                    String ans = (String) data.getData();
                    if(ans.equalsIgnoreCase("accept"))  {
                        System.out.println("got feedback");
                        ready.release();
                        return;
                    }
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void send(Serializable data) {
        testLock();
        naiveSend(data);
    }
    public void naiveSend(Serializable data) {
        Data dat = new Data(data);
        byte[] dataBytes = new byte[Constants.SIZE];
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            ObjectOutputStream bo = new ObjectOutputStream(bs);
            bo.writeObject(dat);
            dataBytes = bs.toByteArray();
            DatagramPacket dataPacket = new DatagramPacket(dataBytes, dataBytes.length, server);
            socket.send(dataPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws Exception {
        testLock();
        naiveListen();
    }
    public void naiveListen() throws Exception {
        DatagramPacket dataPacket = constructDatagramPacket();
        try {
            socket.receive(dataPacket);
            Data data = getDataFromPacket(dataPacket);

            if(this.data != null &&
                    (data.getSendTime() < data.getSendTime() ||
                            data.getSerialNumber() < data.getSerialNumber())) {
                System.out.println("Receive disordered data that should be discarded!");
                return;
            }

            if (data.getData() instanceof String) {
                System.out.println((String) data.getData());
            }
            else {
                System.out.println(data.getData());
                this.data = data.getData();
            }
        } catch (SocketTimeoutException e) {
            //e.printStackTrace();
        }
    }

    public Data naiveReceive() throws Exception {
        DatagramPacket dataPacket = constructDatagramPacket();
        try {
            socket.receive(dataPacket);
            return getDataFromPacket(dataPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stop() {
        System.err.println("client stopped!!");
        stop = true;
    }

    @Override
    public HashMap<InetAddress, Serializable> getLatest() {
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
        if(data != null) {
            HashMap<InetAddress,Serializable> v;
            v = new HashMap<InetAddress,Serializable>();
            v.put(server.getAddress(),data);
            data = null;
            return (HashMap<InetAddress, Serializable>)v.clone();
        }
        return new HashMap<InetAddress, Serializable>();
    }

    @Override
    public Vector<InetAddress> getPlayers() {
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
        Vector<InetAddress> ipVector = new Vector<InetAddress>();
        try {
            ipVector.add(InetAddress.getLocalHost());
        }
        catch (Exception e) {
            // e.printStackTrace();
        }
        return ipVector;
    }

    public void testLock() {
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
    }

    public static DatagramPacket constructDatagramPacket() {
        byte[] receiveBytes = new byte[Constants.SIZE];
        return new DatagramPacket(receiveBytes, receiveBytes.length);
    }

    public static Data getDataFromPacket(DatagramPacket packet) {
        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(packet.getData());
            ObjectInputStream os = new ObjectInputStream(bs);
            return (Data)os.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public InetSocketAddress getServer() {
        return server;
    }

    public void setServer(InetSocketAddress server) {
        this.server = server;
    }

    public Semaphore getReady() {
        return ready;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}

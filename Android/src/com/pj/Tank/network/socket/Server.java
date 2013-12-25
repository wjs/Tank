package com.pj.Tank.network.socket;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import com.pj.Tank.logic.GlobalEnvironment;
import com.pj.Tank.network.socket.constants.Constants;

public class Server extends CSFather implements Runnable,Connectable{
	private DatagramSocket socket;
    private HashMap<InetAddress,Serializable> clientsData;
    private HashMap<InetAddress,Long> clientsSendTime;
    private HashMap<InetAddress,Long> clientsSerialNumber;
    private final Semaphore ready = new Semaphore(1);
    private boolean stop;

    public Server() {
        try {
            ready.acquire();
            stop = false;
            clientsData = new HashMap<InetAddress,Serializable>();
            clientsSendTime = new HashMap<InetAddress,Long>();
            clientsSerialNumber = new HashMap<InetAddress,Long>();
            socket = new DatagramSocket(Constants.SERVERPORT);
            socket.setSoTimeout(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClient(InetAddress client_ip) {
        clientsData.put(client_ip, null);
        clientsSendTime.put(client_ip,new Long(-1));
        clientsSerialNumber.put(client_ip,new Long(-1));
    }

	private void listen() throws Exception{
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
        byte[] receiveBytes = new byte[Constants.SIZE];
        DatagramPacket dataPacket = new DatagramPacket(receiveBytes, receiveBytes.length);
        try {
            socket.receive(dataPacket);
            ByteArrayInputStream bs = new ByteArrayInputStream(dataPacket.getData());
            ObjectInputStream os = new ObjectInputStream(bs);
            Data data = (Data)os.readObject();
            if(data.getData() instanceof String) {
//                System.out.println((String) data.getData());
                String ans = (String) data.getData();
                if(ans.equalsIgnoreCase("join"))  {
					InetAddress addr = dataPacket.getAddress();
                    addClient(addr);
					// ---------------------------------------------------------------
					// 我在这里自己设了一下 GlobalEnvironment的全局变量 otherIP
					// ---------------------------------------------------------------
					System.out.println(addr.toString() + " ####################################");
					GlobalEnvironment.otherIP = addr.toString();

                    send("accept",dataPacket.getAddress());
                }
            }
            else {
//                System.out.println(data.getData());

            if(clientsData.containsKey(dataPacket.getAddress())) {
                if(clientsSendTime.get(dataPacket.getAddress()) > data.getSendTime() &&
                        clientsSerialNumber.get(dataPacket.getAddress()) > data.getSerialNumber()) {
                    System.out.println("Receive disordered data that should be discarded!");
                    return;
                }
                else {
                    clientsData.put(dataPacket.getAddress(),data.getData());
                    clientsSendTime.put(dataPacket.getAddress(), data.getSendTime());
                    clientsSerialNumber.put(dataPacket.getAddress(), data.getSendTime());
                }

            }
            }
        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
        }
	}

    public void send(Serializable data,InetAddress ip) {
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
        Data dat = new Data(data);
        byte[] dataBytes = new byte[Constants.SIZE];
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            ObjectOutputStream bo = new ObjectOutputStream(bs);
            bo.writeObject(dat);
            dataBytes = bs.toByteArray();
            DatagramPacket dataPacket = new DatagramPacket(dataBytes, dataBytes.length, new InetSocketAddress(ip,Constants.CLIENTPORT));
            socket.send(dataPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(Data data) {
        try{
            ready.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready.release();
        byte[] dataBytes = new byte[Constants.SIZE];
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            ObjectOutputStream bo = new ObjectOutputStream(bs);
            bo.writeObject(data);
            dataBytes = bs.toByteArray();
//            System.err.println("sending");
            for(InetAddress client: clientsData.keySet()) {
//                System.err.println(client.getPort());
                DatagramPacket dataPacket = new DatagramPacket(dataBytes, dataBytes.length, new InetSocketAddress(client,Constants.CLIENTPORT));
                socket.send(dataPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            System.out.println(socket);
            ready.release();
            while(stop == false ) {
                listen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("server stopped!");

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stop() {
        System.err.println("server stopped!!");
        stop = true;
    }
    @Override
    public void send(Serializable data) {
        this.sendToAll(new Data(data));
    }

    @Override
    public HashMap<InetAddress, Serializable> getLatest() {
        HashMap<InetAddress, Serializable> retData = (HashMap<InetAddress, Serializable>)clientsData.clone();
        for(InetAddress addr: clientsData.keySet()) {
            clientsData.put(addr, null);
        }
        return retData;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Vector<InetAddress> getPlayers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public HashMap<InetAddress, Serializable> getClientsData() {
        return clientsData;
    }

    public void setClientsData(HashMap<InetAddress, Serializable> clientsData) {
        this.clientsData = clientsData;
    }

    public HashMap<InetAddress, Long> getClientsSendTime() {
        return clientsSendTime;
    }

    public void setClientsSendTime(HashMap<InetAddress, Long> clientsSendTime) {
        this.clientsSendTime = clientsSendTime;
    }

    public HashMap<InetAddress, Long> getClientsSerialNumber() {
        return clientsSerialNumber;
    }

    public void setClientsSerialNumber(HashMap<InetAddress, Long> clientsSerialNumber) {
        this.clientsSerialNumber = clientsSerialNumber;
    }

    public Semaphore getReady() {
        return ready;
    }
}
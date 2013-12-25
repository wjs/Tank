package com.pj.Tank.network.socket;


import com.pj.Tank.entity.NetPackage;
import com.pj.Tank.entity.Point;
import com.pj.Tank.entity.Tank;
import com.pj.Tank.logic.GlobalEnvironment;
import com.pj.Tank.network.testData.TestClass;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void main(String args[]) throws InterruptedException {
        Server server = new Server();
        Thread st = new Thread(server);
        st.start();

		System.out.println("Start -------------------");

		HashMap<InetAddress, Serializable> tmpReceiveMap = server.getLatest();
		try {
			NetPackage tmpNP = (NetPackage) tmpReceiveMap.get(InetAddress.getByName("10.147.136.42"));
			while (true) {
				while (tmpNP == null) {
					tmpReceiveMap = server.getLatest();
					tmpNP = (NetPackage) tmpReceiveMap.get(InetAddress.getByName("10.147.136.42"));
				}
				System.out.println(tmpReceiveMap);
				server.send(tmpNP);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
//		for (int i = 0; i < 1000; i ++) {
//			try {
//				while (tmpReceiveMap == null || tmpReceiveMap.size() == 0) {
//					Thread.sleep(100);
//					tmpReceiveMap = client.getLatest();
//					System.out.print(i);
//				}
//				System.out.println("\n" + i + " ");
//				tmpNP = (NetPackage) tmpReceiveMap.get(InetAddress.getByName("10.131.226.223"));
//				client.send(tmpNP);
//
//			} catch (UnknownHostException e) {
//				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//			}
//		}
//		System.out.println("\n end ------------------- " + (System.currentTimeMillis() - t1));

//		try {
//			System.out.println("Latest 1 is: " + ((TestClass) client.getLatest().get(InetAddress.getByName("127.0.0.1"))).getName());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//		}
//		st.join();
//        ct.join();
	}
}

package com.github.jusm.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AddressUtil {

	private final static String NEED_CLEAR_IP = "127.0.0.1";
 
	public static String getHostAddress() {
		Enumeration<NetworkInterface> allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return null;
		}
		if (allNetInterfaces == null) {
			return null;
		}
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> address = networkInterface.getInetAddresses();
			InetAddress ip = null;
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					String hostAddress = ip.getHostAddress();
					if (!NEED_CLEAR_IP.equals(hostAddress)) {
						return hostAddress;
					}
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getHostAddress());
	}
}
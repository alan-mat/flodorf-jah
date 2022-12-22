package com.floridsdorf.jah.networkTest;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class NetworkDiscovery {
    //get IPv4 address of the host
    public static String getCurrentIp4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    //get IPv4 in hexadecimal format
    public static String getEncodeIp4Address() {
        String ip = getCurrentIp4Address();
        String[] parts = ip.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            result.append(Integer.toHexString(Integer.parseInt(part)));
        }
        return result.toString();
    }

    //decode
    public static String getDecodedIp4Address(String encodedIp) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encodedIp.length(); i += 2) {
            String part = encodedIp.substring(i, i + 2);
            result.append(Integer.parseInt(part, 16));
            result.append(".");
        }
        return result.substring(0, result.length() - 1);
    }



    public static void main(String[] args) {
        System.out.println(getCurrentIp4Address());
        System.out.println(getEncodeIp4Address());
        System.out.println(getDecodedIp4Address(getEncodeIp4Address()));
    }

}
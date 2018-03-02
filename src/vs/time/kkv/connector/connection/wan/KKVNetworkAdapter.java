/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.wan;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author kyo
 */
public class KKVNetworkAdapter {

  public String sid;
  public String ipAddress;
  public String multiHostAddress;

  public static String getWlanID(NetworkInterface n) {
    String result = n.getName();// + " [" + n.getDisplayName() + "]";

    try {
      Enumeration ee = n.getInetAddresses();
      while (ee.hasMoreElements()) {
        InetAddress i = (InetAddress) ee.nextElement();
        String ip = i.getHostAddress();
        String ip_host = "";
        int pos = ip.lastIndexOf(".");
        if (pos > 0) {
          String net = ip.substring(0, pos);
          ip_host =  net + ".255";          
          result+=" ["+net+".xxx]";
        }
        break;
      }
    } catch (Exception e) {
    }
    return result;
  }

  public static String[] getNetworkAddress() {
    List<String> list = new ArrayList<String>();
    try {
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        if (n.isLoopback()) {
          continue;
        }
        //if (!n.supportsMulticast()) continue;
        if (!n.isUp()) {
          continue;
        }
        //if (n.isVirtual()) continue;
        Enumeration ee = n.getInetAddresses();
        String sid = getWlanID(n);
        list.add(sid);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    String[] result = new String[list.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = list.get(i);
    }
    return result;
  }

  public static KKVNetworkAdapter getAddress(String network_sid) {
    KKVNetworkAdapter first_adapter = null;
    try {
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        if (!n.isUp()) {
          continue;
        }
        Enumeration ee = n.getInetAddresses();
        String sid = getWlanID(n);
        KKVNetworkAdapter na_ip = null;
        while (ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();
          String ip = i.getHostAddress();
          String ip_host = "";
          int pos = ip.lastIndexOf(".");
          if (pos > 0) {
            ip_host = ip.substring(0, pos) + ".255";
            na_ip = new KKVNetworkAdapter();
            na_ip.sid = sid;
            na_ip.multiHostAddress = ip_host;
            na_ip.ipAddress = ip;
            if (first_adapter == null) {
              first_adapter = na_ip;
            }
          }
          break;
        }
        if (sid.equalsIgnoreCase(network_sid) && na_ip != null) {
          return na_ip;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return first_adapter;
  }
}

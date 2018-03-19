/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.wan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.connection.ConnectionVSTimeMachine;
import vs.time.kkv.connector.connection.VSTimeConnector;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;

/**
 *
 * @author kyo
 */
public class ConnectionSocket extends Thread implements ConnectionVSTimeMachine {

  //public DatagramSocket socketOut = null;
  public boolean finish = false;
  VSTimeMachineReciver receiver = null;
  public VSTimeConnector timeConnector = null;
  public String network_sid = "";

  public int port_for_listining = 8888;
  public int port_for_sending = 8889;
  InetAddress ipAddress = null;
  public String staticIP = null;
  //InetAddress host = InetAddress.getAllByName(null);

  public ConnectionSocket(VSTimeConnector timeConnector, VSTimeMachineReciver receiver, String network_sid, String staticIP, int portForListing, int portForSending) throws IOException {
    super();
    if (portForListing != 0) {
      this.port_for_listining = portForListing;
      this.port_for_sending = portForSending;
    }
    this.network_sid = network_sid;
    this.staticIP = staticIP;
    
    this.receiver = receiver;
    this.timeConnector = timeConnector;

    String host = "localhost";
    //String host = "255.255.255.255";
    //String host = "192.168.197.255";
    //String host = "192.168.197.100";
    
    if (staticIP==null){
      KKVNetworkAdapter ka = KKVNetworkAdapter.getAddress(network_sid);
      if (ka!=null) host = ka.multiHostAddress;    
      ipAddress = InetAddress.getByName(host);
    }else{
      ipAddress = InetAddress.getByName(staticIP);
    }

    start();
  }

  DatagramSocket sock = null;

  @Override
  public void run() {

    try {
      sock = new DatagramSocket(port_for_listining);

      while (!finish) {
        byte[] buffer = new byte[65536];

        try {

          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          sock.receive(packet);

          byte[] data_b = packet.getData();
          ipAddress = packet.getAddress();
          String data = new String(data_b, 0, packet.getLength());          
          data = data.trim();

          // try{
          /*int len = data == null ? 0 : data.length();
        if (len >= 2 && data.substring(len - 2, len).equalsIgnoreCase("\r\n")) {
          data = data.substring(0, len - 2);
        };
        if (len >= 1 && data.substring(len - 2, len).equalsIgnoreCase("\n")) {
          data = data.substring(0, len - 1);
        };*/
          if (data != null && !data.equals("")) {
            String[] commands = data.split(":");
            String[] params = null;
            long crc8 = 0;
            int lpos = data.lastIndexOf(",");
            if (lpos > 0) {
              String nd = data.substring(0, lpos);
              crc8 = VSTimeConnector.crc8(nd.getBytes());
            }
            VSTM_LapInfo lap = null;
            if (commands.length >= 2) {
              params = commands[1].split(",");
              lap = timeConnector.handleRequest(commands, params, crc8);
            };
            if (receiver != null) {
              receiver.receiveData(data, commands, params, lap);
            }
          }
        } catch (SocketException se) {
          //se.printStackTrace();
          try {
            sock.close();
          } catch (Exception e) {
          }
          try {
            sock = new DatagramSocket(port_for_listining);
          } catch (Exception e) {
          }
        } catch (Exception ein) {
          ein.printStackTrace();  
          try {
            sock.close();
          } catch (Exception e) {
          }
          try {
            sock = new DatagramSocket(port_for_listining);
          } catch (Exception e) {
          }
        }
      }

    } catch (IOException e) {
      System.err.println("IOException " + e);
    }
    try {
      sock.close();
    } catch (Exception e) {
    }
  }
  
  String prev_data = null;

  @Override
  public void sendData(String data) {
    
    if (prev_data!=null && prev_data.equalsIgnoreCase(data)){
      try{        
        String host = "localhost";
        if (staticIP==null){
          KKVNetworkAdapter ka = KKVNetworkAdapter.getAddress(network_sid);
          if (ka!=null) host = ka.multiHostAddress;    
          ipAddress = InetAddress.getByName(host);
        }else{
         ipAddress = InetAddress.getByName(staticIP);
        }
      }catch(Exception e){}      
    }
    
    //data = data.trim();//+"\r";
    byte[] b = data.getBytes();
    DatagramPacket dp = new DatagramPacket(b, b.length, ipAddress, this.port_for_sending);
    //DatagramPacket dp = new DatagramPacket(b, b.length, this.port_for_sending);

    try {
      DatagramSocket ds = new DatagramSocket();
      ds.setBroadcast(true);
      ds.send(dp);
      ds.close();
      //System.out.println("send to :'" + ipAddress + "' port:" + this.port_for_sending + " data='" + data + "'");
    } catch (IOException ex) {
      ex.printStackTrace();
      Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
    }
 
    prev_data = data;
  }

  @Override
  public void disconnect() {
    System.out.println("disconnect");
    finish = true;
    try {
      if (sock != null) {
        sock.close();
      }
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) {
    try {
      /*InetAddress[] all_ipAddress = InetAddress.getAllByName("localhost");

      for (InetAddress ia : all_ipAddress) {
        System.out.println(ia + " hostName:" + ia.getHostAddress());
      }*/

      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        if (n.isLoopback()) continue;        
        //if (n.isPointToPoint()) continue;
        if (!n.supportsMulticast()) continue;
        if (!n.isUp()) continue;
        if (n.isVirtual()) continue;
        Enumeration ee = n.getInetAddresses();        
        System.out.println(n.getName()+" ["+n.getDisplayName()+"]");
        
          while (ee.hasMoreElements()) {
            InetAddress i = (InetAddress) ee.nextElement();
            String ip = i.getHostAddress();
            String ip_host = "";
            int pos = ip.lastIndexOf(".");
            if (pos>0){
              ip_host = ip.substring(0,pos)+".255";
            }
            System.out.println("ip:"+ip+" brodcast:"+ip_host);
            break;
          }
       
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}

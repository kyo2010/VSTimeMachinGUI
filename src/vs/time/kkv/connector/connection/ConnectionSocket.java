/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;

/**
 *
 * @author kyo
 */
public class ConnectionSocket extends Thread implements ConnectionVSTimeMachine {

  public DatagramSocket socketOut = null;

  public boolean finish = false;
  VSTimeMachineReciver receiver = null;
  public VSTimeConnector timeConnector = null;

  public int port_for_listining = 8888;
  public int port_for_sending = 8889;
  InetAddress ipAddress = null;
  //InetAddress host = InetAddress.getAllByName(null);

  public ConnectionSocket(VSTimeConnector timeConnector, VSTimeMachineReciver receiver, int portForListing, int portForSending) throws IOException {
    super();
    if (portForListing != 0) {
      this.port_for_listining = portForListing;
      this.port_for_sending = portForSending;
    }
    //ipAddress = InetAddress.getByName("localhost");
    //ipAddress = InetAddress.getLocalHost();
    //"192.168.197.100";
    //InetAddress[] all_ipAddress = Inet4Address.get;
    
    ipAddress = Inet4Address.getLocalHost();
    //System.out.println("IP for WAN : "+ipAddress);
    
    //ipAddress = Inet4Address.getLocalHost();
    
    /*if (all_ipAddress!=null && all_ipAddress.length>0) {
      ipAddress = all_ipAddress[1];
    }*/
    
    /*for (InetAddress ia: all_ipAddress){
      System.out.println(ia+" hostName:"+ia.getHostAddress());
    }*/
    
    this.receiver = receiver;
    this.timeConnector = timeConnector;
    socketOut = new DatagramSocket();
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
            if (receiver!=null) receiver.receiveData(data,commands,params,lap);
          }
        } catch (Exception ein) {
          ein.printStackTrace();
          System.err.println("IOException " + ein);
          try{
            sock.close();
          }catch(Exception e){}
          try{
            sock = new DatagramSocket(port_for_listining);
          }catch(Exception e){}
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

  @Override
  public void sendData(String data) {    
    //data = data.trim();//+"\r";
    if (socketOut != null) {
      byte[] b = data.getBytes();
      DatagramPacket dp = new DatagramPacket(b, b.length, ipAddress, this.port_for_sending );
      try {
        socketOut.send(dp);
      } catch (IOException ex) {
        Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void disconnect() {
    System.out.println("disconnect");
    finish = true;
    try {
      if (socketOut != null) {
        socketOut.close();
      }
    } catch (Exception e) {
    }
    
    try {
      if (sock != null) {
        sock.close();
      }
    } catch (Exception e) {
    }
  }
}

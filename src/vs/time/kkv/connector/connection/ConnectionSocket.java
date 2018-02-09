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
    if (portForListing!=0){
      port_for_listining = portForListing;
      port_for_sending = portForSending;
    }  
    ipAddress = InetAddress.getByName("localhost");
    this.receiver = receiver;
    this.timeConnector = timeConnector;
    socketOut = new DatagramSocket();        
    start();    
  }

  @Override
  public void run() {

    DatagramSocket sock = null;

    try {
      sock = new DatagramSocket(port_for_listining);

      while (!finish) {         
        byte[] buffer = new byte[65536];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        sock.receive(packet);

        byte[] data_b = packet.getData();
        String data = new String(data_b, 0, packet.getLength());
        int len = data==null?0:data.length();
        if (len>=2 && data.substring(len-2,len).equalsIgnoreCase("\r\n")){
          data = data.substring(0,len-2);
        };
        if (data != null && !data.equals("")) {
          String[] commands = data.split(":");
          String[] params = null;
          
          long crc8 = 0;
          int lpos = data.lastIndexOf(",");
          String nd = data.substring(0,lpos);
          crc8 = VSTimeConnector.crc8(nd.getBytes());
          VSTM_LapInfo lap = null;
          
          if (commands.length >= 2) {           
            try {
              if ( commands!=null && commands.length>=1 ) params = commands[1].split(",");
              lap = timeConnector.handleRequest(commands, params,crc8);
            } catch (SerialPortException ex) {
              Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
          if (receiver != null) {
            data += "\r\n";
            receiver.receiveData(data, commands, params, lap);
          }
        }
      }
      sock.close();
    } catch (IOException e) {
      System.err.println("IOException " + e);
    }
  }

  @Override
  public void sendData(String data) {
    if (socketOut != null) {      
      byte[] b = data.getBytes();                 
      DatagramPacket  dp = new DatagramPacket(b , b.length , ipAddress , port_for_sending);
      try {
        socketOut.send(dp);      
      } catch (IOException ex) {
        Logger.getLogger(ConnectionSocket.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void disconnect() {
    finish = true;
    try {
      if (socketOut != null) {
        socketOut.close();
      }
    } catch (Exception e) {
    }
  }
}

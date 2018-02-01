/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_ESPInfo;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.connection.ConnectionCOMPort;
import vs.time.kkv.connector.connection.ConnectionSocket;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.connector.connection.ConnectionVSTimeMachine;

/**
 *
 * @author kyo
 */
public class VSTimeConnector {
    
  public String comPort;
  public boolean connected = false;
  public String baseStationID = null;
  public int sensitivityIndex = 0;
  public String firmWareVersion = "";
  public ConnectionVSTimeMachine transport = null;
  public int lastTransponderID = -1;
  public boolean WIFI = false;
  public String last_error = "";
  
  public VSTimeConnector(String port) {
    comPort = port;
    //connect();
  }

  public void disconnect() {
    connected = false;
    if (transport!=null) transport.disconnect();
  } 

  public void connect(VSTimeMachineReciver reciver, int portForListing, int portForSending) throws InterruptedException, SerialPortException, IOException {
    if (comPort.equalsIgnoreCase("WLAN")) {
      transport = new ConnectionSocket(this, reciver, portForListing, portForSending);
      connected = true;
      WIFI = true;
    } else {
      transport = new ConnectionCOMPort(this, reciver, comPort);
      connected = true;
      WIFI = false;
    }    
    hello();
    setTime();
  }

  /**
   * Type - "Personal", ip1=192, ip2=169, ip3=197, port receiver 8888, port send
   * 8889
   */
  public void setVSTMParams(String baseID, VSTM_ESPInfo i) throws SerialPortException {
    sentMessage("espsetinfo:" + baseID + "," + i.sensitivity + "," + i.connectionType + "," + i.SSID + "," + i.SSPWD + "," + i.ip1 + "," + i.ip2 + "," + i.ip3 + "," + i.port_receiver + "," + i.port_send + "\r\n");
  }

  public void setPower(int powerIndex) throws SerialPortException {
    sentMessage("sendpwr:" + powerIndex + "\r\n");
  }

  public void getPower() throws SerialPortException {
    sentMessage("rcvpwr\r\n");
  }

  Boolean waitingVSTMParams = false;
  VSTM_ESPInfo current_info = null;

  public VSTM_ESPInfo getVSTMParams(String baseID) throws SerialPortException {
    current_info = null;
    //sentMessage("espgetinfo:"+baseID+"\r\n");
    sentMessage("espgetinfo\r\n");
    VSTM_ESPInfo info = null;
    int retry = 0;
    //synchronized (waitingVSTMParams){
    waitingVSTMParams = true;
    while (waitingVSTMParams && info == null && retry < 30) {
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
      if (!waitingVSTMParams && info == null) {
        return info = current_info;
      }
      retry++;
    }
    if (!waitingVSTMParams && info == null) {
      return info = current_info;
    }
    //}
    if (info != null) {
      info.baseID = baseID;
      info.sensitivity = sensitivityIndex;
    }
    return info;
  }

  public void setTime() throws SerialPortException, InterruptedException {
    sentMessage("settime:" + Calendar.getInstance().getTime().getTime() + "\r\n");
  }

  public void clear() throws SerialPortException {
    sentMessage("clear\r\n");
  }

  public void hello() throws SerialPortException {
    sentMessage("hello\r\n");
  }

  public void getInfo(String baseID) throws SerialPortException {
    sentMessage("getinfo:" + baseID + "\r\n");
  }

  public void sentMessage(String st) throws SerialPortException {
    System.out.print("send:" + st);
    if (transport!=null){
      transport.sendData(st);
    }  
     
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException, SerialPortException {
    //������� � ����������� ��� �����    
    VSTimeConnector connector = new VSTimeConnector("WLAN");
    try {
      connector.connect(new VSTimeMachineReciver() {
        @Override
        public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap) {
          //if (data.indexOf("ping")!=0)
            System.out.print("receive:"+data);
        }
      },0,0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Thread.sleep(10000);
    connector.disconnect();
  }

  public VSTM_LapInfo handleRequest(String[] commands, String[] params, long crc8) throws SerialPortException {
    if (commands[0].equalsIgnoreCase("ping")) {
      if (baseStationID == null) {
        baseStationID = params[1];
      }          
    }else if (commands[0].equalsIgnoreCase("timesynchok")) {
      baseStationID = params[0];
      sentMessage("timesynchreceived:" + params[0] + "\r\n");
    } else if (commands[0].equalsIgnoreCase("systime")) {
      sensitivityIndex = Integer.parseInt(params[1]);
      baseStationID = params[2];
      firmWareVersion = params[3];
    } else if (commands[0].equalsIgnoreCase("espinfo")) {
      //synchronized(waitingVSTMParams){        
      // espinfo:0,VS Time Machine,vs123456,192,168,197,8888,8889,0.2.1
      current_info = new VSTM_ESPInfo();
      try {
        current_info.connectionType = Integer.parseInt(params[0]);
      } catch (Exception e) {
      }
      current_info.SSID = params[1];
      current_info.SSPWD = params[2];
      current_info.ip1 = params[3];
      current_info.ip2 = params[4];
      current_info.ip3 = params[5];
      try {
        current_info.port_receiver = "" + Integer.parseInt(params[6]);
      } catch (Exception e) {
      }
      try {
        current_info.port_send = "" + Integer.parseInt(params[7]);
      } catch (Exception e) {
      }
      current_info.isError = false;
      waitingVSTMParams = false;
      //};
    }else if (commands[0].equalsIgnoreCase("lap")) {
      try{
        VSTM_LapInfo lap = new VSTM_LapInfo();
        lap.numberOfPacket = Integer.parseInt(params[0]);
        lap.baseStationID = Integer.parseInt(params[1]);
        lap.transponderID = Integer.parseInt(params[2]);
        lap.time = Integer.parseInt(params[3]);
        lap.transpnderCounter = Integer.parseInt(params[4]);        
        long crc8_f = Long.parseLong(params[5]);
        if (crc8!=crc8_f){
          last_error = "lap receive is error. crc8 is error "+crc8_f+"<>"+crc8;
          System.out.print(last_error);
          return null;  
        }else{
          sentMessage("lapreceived:" + lap.numberOfPacket + "," + lap.baseStationID + "\r\n");        
          return lap;
        }  
        //int pos1
        
      }catch(Exception e){}  
      
      //lap:<Number	packet>,<ID	Base	station>,<ID	transponder>,<Time	in milliseconds>,<Transponder	start	number>,<CRC	8>\r\n
    
      //synchronized(waitingVSTMParams){        
      // espinfo:0,VS Time Machine,vs123456,192,168,197,8888,8889,0.2.1
     /* current_info = new VSTM_ESPInfo();
      try {
        current_info.connectionType = Integer.parseInt(params[0]);
      } catch (Exception e) {
      }
      current_info.SSID = params[1];
      current_info.SSPWD = params[2];
      current_info.ip1 = params[3];
      current_info.ip2 = params[4];
      current_info.ip3 = params[5];
      try {
        current_info.port_receiver = "" + Integer.parseInt(params[6]);
      } catch (Exception e) {
      }
      try {
        current_info.port_send = "" + Integer.parseInt(params[7]);
      } catch (Exception e) {
      }
      current_info.isError = false;
      waitingVSTMParams = false;*/            
      //};
    }
            
    return null;
  }
 

  public static long crc8(byte[] buffer) {
    int crc = 0;
    for (byte b : buffer) {
      for (int j = 0; j < 8; j++) {
        int mix = (crc ^ b) & 0x01;
        crc >>= 1;
        if (mix == 1) {
          crc ^= 0x8C;
        }
        b >>= 1;
      }
      crc &= 0xFF;
    }
    return crc & 0xFF;
  }
}
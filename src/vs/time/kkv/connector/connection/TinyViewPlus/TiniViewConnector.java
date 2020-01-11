/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.TinyViewPlus;

import vs.time.kkv.connector.connection.VSTimeConnection.*;
import vs.time.kkv.connector.connection.*;
import KKV.Utils.Tools;
import KKV.Utils.UserException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import vs.time.kkv.connector.MainForm;
import vs.time.kkv.connector.TimeMachine.VSColor;
import vs.time.kkv.connector.TimeMachine.VSTM_ESPInfo;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.connection.com.ConnectionCOMPort;
import vs.time.kkv.connector.connection.wan.ConnectionSocket;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.models.VS_SETTING;
import vs.time.kkv.connector.connection.DroneConection;

/**
 * @author kyo
 */
public class TiniViewConnector extends DroneConnector {
        
  public TiniViewConnector(DroneConection transport){
    super(transport);
  }
  
 public void checkConnection() {
    if (transport != null) {
      long time = Calendar.getInstance().getTimeInMillis();
      if ((lastPingTime + 1000 * transport.getTimeOutForReconnect()) <= time) {
        lastPingTime = Calendar.getInstance().getTimeInMillis();
        if (transport.needToAutoReconect()){
          try {
            transport.disconnect();
          } catch (Exception e) {
          }
          try {
            connect();
          } catch (Exception e) {
          }
        }
      }
    }
  }
  
  public void disconnect() {
    connected = false;
    if (transport != null) {
      try{
        transport.disconnect();
      }catch(Exception e){}
    }  
  } 

  public void connect() throws InterruptedException, SerialPortException, IOException, UserException {       
    try{
      if (connected)disconnect();        
    }catch(Exception e){}
    
    try{
      if (transport!=null) transport.connect();    
      lastPingTime = Calendar.getInstance().getTimeInMillis();
      new AfterConnectionCommsnds(this);   
      connected = true;
    }catch(Exception e){
      connected = false;      
      disconnect();        
    }
  }

  @Override
  public void sentMessage(String st) throws SerialPortException {
    System.out.print("send:" + st);
    if (transport != null) {
      transport.sendData(st);
    }
    if (sendListener != null) {
      try {
        sendListener.sendVSText(st);
      } catch (Exception e) {
      }
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException, SerialPortException {
    //������� � ����������� ��� �����    
    String param = "pilot5";
    String num = param.substring(5);
    System.out.println("pilot num :"+num);
    
    if (1==1) return;
    
    TiniViewConnector connector = new TiniViewConnector(
     new ConnectionCOMPort(
      new VSTimeMachineReciver() {
          public void receiveDataForLog(String data) {
          };
        @Override
        public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap) {
          //if (data.indexOf("ping")!=0)
          System.out.print("receive:" + data);
        };
       }, "COM5"));
    try {
      connector.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Thread.sleep(10000);
    connector.disconnect();
  }

  @Override
  public VSTM_LapInfo handleRequest(String[] commands, String[] params, long crc8) throws SerialPortException {
    try {
      if (commands[0].equalsIgnoreCase("ping")) {
        lastPingTime = Calendar.getInstance().getTimeInMillis();
        if (baseStationID == null) {
          baseStationID = params[1];
        }
        waitFirstPing = false;
      } else if (commands[0].equalsIgnoreCase("lap")) {
        try {
          VSTM_LapInfo lap = new VSTM_LapInfo();                    
          if (params[0] != null &&  params[0].indexOf("pilot")== 0) {
             lap.isPilotNumber = true;
             lap.baseStationID = 0;
             String num = params[0].substring(5);
             lap.pilotNumber = Integer.parseInt(num);
             //System.out.print("Arduino push pilot numner : "+num);
             sentMessage("lapreceived:pilot" + num +"\r\n");
             return lap;
          } else if (params[0] != null &&  params[0].indexOf("channel")== 0) {
             lap.isPilotNumber = true;
             lap.isPilotChannel = true;
             lap.baseStationID = 0;
             lap.pilotChannel = params[0].substring(7);
             //System.out.print("Arduino push pilot numner : "+num);
             //sentMessage("lapreceived:channel" + lap.pilotChannel +"\r\n");
             
             // params[1] baseID
             // params[2] cameraIndex
             // params[3] package id
             // params[4] channel
             
             sentMessage("lapreceived:" + params[3] +"\r\n");
             return lap;
          } else {
            lap.numberOfPacket = Integer.parseInt(params[0]);
            lap.baseStationID = Integer.parseInt(params[1]);
            lap.transponderID = Integer.parseInt(params[2]);
            lap.time = Long.parseLong(params[3]);
            lap.transpnderCounter = Integer.parseInt(params[4]);
            long crc8_f = Long.parseLong(params[5]);
            if (crc8 != crc8_f) {
              last_error = "lap receive is error. crc8 is error " + crc8_f + "<>" + crc8;
              System.out.print(last_error);
              return null;
            } else {
              sentMessage("lapreceived:" + lap.numberOfPacket + "," + lap.baseStationID + "\r\n");
              return lap;
            }
          }
          //int pos1

        } catch (Exception e) {
        }

       }
    } catch (Exception eot) {
      MainForm._toLog(eot);
    }

    return null;
  }

  @Override
  public void clearTransponderSearchQueue() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isTransponderSeached(int trans_id) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void rfidLock(int key) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void rfidUnlock() throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setVSTMParams(String baseID, VSTM_ESPInfo i) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setPower(int powerIndex) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getPower() throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void seachTransponder(int transponderID, int color) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setSensitivity(int sensivity) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setColor(int transponderID, int color) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setColorForTraficLight(int transponderID, VSColor color) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public VSTM_ESPInfo getVSTMParams(String baseID) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void addBlinkTransponder(int transID, int color, VSColor gateColor, boolean isBlink) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setTime() throws SerialPortException, InterruptedException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void clear() throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void hello() throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getInfo(String baseID) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void sendflash(int TransID, String data) throws SerialPortException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public boolean supportSetTime(){ return false; };
   
   public boolean supportVSTimeMachineExtendMenu() {
    return false;
  }

  public boolean supportSearch() {
    return false;
  }

  ;
   public boolean supportRFIDMode() {
    return false;
  }
}

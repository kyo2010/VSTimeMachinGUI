/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.VSTimeConnection;

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
public class VSTimeConnector extends DroneConnector {
        
  public VSTimeConnector(DroneConection transport){
    super(transport);
  }
  
  public Map<Integer, VS_EchoTrans> transpondersIsAlive = new HashMap<Integer, VS_EchoTrans>();  
  public void clearTransponderSearchQueue() {
    transpondersIsAlive.clear();
  }
  public boolean isTransponderSeached(int trans_id) {
    if (transpondersIsAlive.get(trans_id) != null) {
      return true;
    }
    return false;
  }
  @Override
  public void rfidLock(int key) throws SerialPortException {
    sentMessage("rfidlock:" + key + "\r\n");
  }
  @Override
  public void rfidUnlock() throws SerialPortException {
    sentMessage("rfidunlock\r\n");
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
    blinkingTimer.stop();
  } 

  public void connect() throws InterruptedException, SerialPortException, IOException, UserException {       
    try{
      if (connected)disconnect();        
    }catch(Exception e){}
    
    try{
      if (transport!=null) transport.connect();    
      lastPingTime = Calendar.getInstance().getTimeInMillis();
      new AfterConnectionCommsnds(this);
      blinkingTimer.start();    
      connected = true;
    }catch(Exception e){
      connected = false;      
      disconnect();        
    }
  }

  /**
   * Type - "Personal", ip1=192, ip2=169, ip3=197, port receiver 8888, port send
   * 8889
   * @param baseID
   * @param i
   * @throws jssc.SerialPortException
   */
  @Override
  public void setVSTMParams(String baseID, VSTM_ESPInfo i) throws SerialPortException {
    sentMessage("espsetinfo:" + baseID + "," + i.sensitivity + "," + i.connectionType + "," + i.SSID + "," + i.SSPWD + "," + i.ip1 + "," + i.ip2 + "," + i.ip3 + "," + i.port_receiver + "," + i.port_send + "\r\n");
  }

  public void setPower(int powerIndex) throws SerialPortException {
    sentMessage("sendpwr:" + powerIndex + "\r\n");
  }

  /* public void setPowerMax() throws SerialPortException {
    setPower(11);
  }*/
  public void getPower() throws SerialPortException {
    sentMessage("rcvpwr\r\n");
  }

  public void seachTransponder(int transponderID, int color) throws SerialPortException {
    sentMessage("searchtrans:" + transponderID + "," + color + ";\r\n");
  }

  public void setSensitivity(int sensivity) throws SerialPortException {
    sentMessage("setsensitivity:" + sensivity + "\r\n");
  }

  /*public void setSensitivityMax() throws SerialPortException {
    setSensitivity(12);
  }*/
  public void setColor(int transponderID, int color) throws SerialPortException {
    //sentMessage("sendcolor:" + transponderID + "," + color + "\r\n");
    sentMessage("sendcolor:" + color + "," + transponderID + "\r\n");
  }
  
  @Override
  public void setColorForTraficLight(int transponderID, VSColor color) throws SerialPortException {
    //sentMessage("sendcolor:" + transponderID + "," + color + "\r\n");
    for (int i=0; i<3; i++){
      try{
      Thread.currentThread().sleep(50);
      }catch(Exception e){}
      
    if (transponderID==0){
      //sendgatecolor:<color>,<gate>\r\n
      
      sentMessage("sendgatecolor:" + color.getOnlyColor() + "," + transponderID + "\r\n");    
      ///sentMessage("sendcolor:" + color + "," + transponderID + "\r\n");    
    }else{
      sentMessage("sendcolor:" + color.vscolor + "," + transponderID + "\r\n");
    }
    }
    
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
    if (info != null) {
      info.baseID = baseID;
      info.sensitivity = sensitivityIndex;
    }
    return info;
  }

  class BlinkigQuee {
    public int transID;
    public int color;
    public int delay = 2500;
    public long milisecond_start = 0;
    public int state = 0; // 0 - setColor, 1 - delay, 2 - off;
    VSColor gateColor = null;
    boolean isBlink = false;
    public BlinkigQuee(int transID, int color, VSColor gateColor, boolean isBlink) {
      this.transID = transID;
      this.color = color;
      this.gateColor = gateColor;
      this.isBlink = isBlink;
    }
  }
  Vector<BlinkigQuee> blinkigQuee = new Vector<BlinkigQuee>();

  public void addBlinkTransponder(int transID, int color, VSColor gateColor, boolean isBlink) {
    this.gateColor = gateColor;
    gateTransId = transID;
    if (transID != -1) {
      blinkigQuee.add(new BlinkigQuee(transID, color, gateColor, isBlink));
    }
    System.out.println("fire gate");
    blinkingTimer.setDelay(1);
    blinkingTimer.restart();
  }

  VSColor gateColor = null;
  int gateTransId = -1;
  int gateChecker = 0;
  public Timer blinkingTimer = new Timer(100, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (connected && blinkigQuee.size() > 0) {
        blinkingTimer.setDelay(500);
        BlinkigQuee blink = blinkigQuee.get(0);
        gateChecker = 0;
        if (blink.state == 0) {
          try {
            if (blink.isBlink) {
              setColor(blink.transID, VSColor.blinkColor(blink.color));
            } else {
              setColor(blink.transID, blink.color);
            }
          } catch (Exception ein) {
          }
          blink.state = 1;
          blink.milisecond_start = Calendar.getInstance().getTimeInMillis();
        } else if (blink.state == 1) {
          if (Calendar.getInstance().getTimeInMillis() - blink.milisecond_start > blink.delay) {
            try {
              if (blink.gateColor == null) {
                setColor(blink.transID, 0);
              } else {
                setColor(blink.transID, blink.gateColor.vscolor);
              }
            } catch (Exception ein) {
            }
            blink.state = 2;
            blinkigQuee.remove(0);
          } else {
            try {
              if (blink.isBlink) {
                setColor(blink.transID, VSColor.blinkColor(blink.color));
              } else {
                setColor(blink.transID, blink.color);
              }
            } catch (Exception ein) {
            }
          }
        } else {
          // Remove from queue          
          blinkigQuee.remove(0);
          try {
            if (blink.gateColor == null) {
              setColor(blink.transID, 0);
            } else {
              setColor(blink.transID, blink.gateColor.vscolor);
            }
          } catch (Exception ein) {
          }
        }
      } else {
        gateChecker++;
        if (gateChecker <= 5) {
          //gateChecker=0;
          if (gateColor != null) {
            try {
              setColor(gateTransId, gateColor.vscolor);
            } catch (Exception ein) {
            }
          }
        }

      }
    }
  });

  public void setTime() throws SerialPortException, InterruptedException {
    if (transport!=null && transport.supportSetTime()){
      sentMessage("settime:" + Calendar.getInstance().getTime().getTime() + "\r\n");
    }
  }

  /*public void setTimeWithDeklay() throws SerialPortException, InterruptedException {
    setTime();
    try {
      sleep(300);
    } catch (Exception e) {
    }
    setTime();
  }*/
  public void clear() throws SerialPortException {
    sentMessage("clear\r\n");
  }

  public void hello() throws SerialPortException {
    sentMessage("hello\r\n");

  }

  public void getInfo(String baseID) throws SerialPortException {
    sentMessage("getinfo:" + baseID + "\r\n");    
  }

  public void sendflash(int TransID, String data) throws SerialPortException {
    flashResponse.put(TransID, null);
    sentMessage("sendflash:" + TransID + data + "\r\n");
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
    
    VSTimeConnector connector = new VSTimeConnector(
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
      } else if (commands[0].equalsIgnoreCase("bootflashok")) {
        int trans_id = Integer.parseInt(params[0]);
        flashResponse.put(trans_id, "OK");
        lastFlashTransponderID = trans_id;
      } else if (commands[0].equalsIgnoreCase("echotrans")) {
        //echotrans:<ID>,<CRC>\r\n  
        if (params.length >= 2) {
          int last_index = params.length - 1;
          long crc8_f = Long.parseLong(params[last_index]);
          String firmWareVersion = "not detected";
          if (params.length > 2) {
            firmWareVersion = params[1];
          }
          int trans_id = Integer.parseInt(params[0]);
          if (crc8 == crc8_f) {
            transpondersIsAlive.put(trans_id, new VS_EchoTrans(trans_id, firmWareVersion));
            sentMessage("echook:" + trans_id + "\r\n");
          }
        }
      } else if (commands[0].equalsIgnoreCase("timesynchok")) {
        baseStationID = params[0];
        boolean time_is_ok = false;
        try {
          sentMessage("timesynchreceived:" + params[0] + "\r\n");
          /*long timeBase = Long.parseLong(params[1]);
          long current_time = Calendar.getInstance().getTimeInMillis();
          if (Math.abs(timeBase - current_time) < 10000) {
            time_is_ok = true;
          }
          if (!time_is_ok) {
            setTime();
          }*/
        } catch (Exception e) {
        }
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
             sentMessage("lapreceived:channel" + lap.pilotChannel +"\r\n");
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
    } catch (Exception eot) {
      MainForm._toLog(eot);
    }

    return null;
  }
}

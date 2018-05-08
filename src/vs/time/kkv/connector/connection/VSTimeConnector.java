/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

import KKV.Utils.Tools;
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
import vs.time.kkv.connector.connection.ConnectionVSTimeMachine;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class VSTimeConnector {

  public String comPort;
  int portForListing;
  int portForSending;
  public String network_sid;
  public String staticIP = null;
  VSTimeMachineReciver reciver;
  public boolean connected = false;
  public String baseStationID = null;
  public int sensitivityIndex = 0;
  public String firmWareVersion = "";
  public ConnectionVSTimeMachine transport = null;
  public int lastTransponderID = -1;
  public boolean WIFI = false;
  public String last_error = "";
  public VSSendListener sendListener = null;
  public Map<Integer, String> flashResponse = new HashMap<Integer, String>();
  public boolean waitFirstPing = false;
  public Connection connForParams = null;

  public VSSendListener getSendListener() {
    return sendListener;
  }

  public VSTimeConnector setSendListener(VSSendListener sendListener) {
    this.sendListener = sendListener;
    return this;
  }

  public interface VSSendListener {

    public void sendVSText(String text);
  }

  public Map<Integer, VS_EchoTrans> transpondersIsAlive = new HashMap<Integer, VS_EchoTrans>();
  long lastPingTime = 0;

  public void clearTransponderSearchQueue() {
    transpondersIsAlive.clear();
  }

  public boolean isTransponderSeached(int trans_id) {
    if (transpondersIsAlive.get(trans_id) != null) {
      return true;
    }
    return false;
  }

  public void checkConnection() {
    if (transport != null) {
      long time = Calendar.getInstance().getTimeInMillis();
      if ((lastPingTime + 1000 * transport.getTimeOutForReconnect()) <= time) {
        lastPingTime = Calendar.getInstance().getTimeInMillis();
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

  public VSTimeConnector(VSTimeMachineReciver reciver, String port, String network_sid, String staticIP, int portForListing, int portForSending, Connection connForParams) {
    comPort = port;
    lastPingTime = Calendar.getInstance().getTimeInMillis();
    this.portForListing = portForListing;
    this.portForSending = portForSending;
    this.reciver = reciver;
    this.network_sid = network_sid;
    this.staticIP = staticIP;
    this.connForParams = connForParams;
    //connect();
  }

  public void disconnect() {
    connected = false;
    if (transport != null) {
      transport.disconnect();
    }
    blinkingTimer.stop();
  }

  public class AfterConnectionCommsnds extends Thread {

    VSTimeConnector conector = null;

    public AfterConnectionCommsnds(VSTimeConnector conector) {
      this.conector = conector;
      start();
    }

    @Override
    public void run() {
      if (comPort.equalsIgnoreCase("WLAN")) {
        long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("waiting first ping...");
        waitFirstPing = true;
        int countRepeats = 0;
        while (waitFirstPing && countRepeats < 100) {
          countRepeats++;
          try {
            sleep(100);
          } catch (Exception e) {
          }
        }
        long time2 = Calendar.getInstance().getTimeInMillis();
        System.out.println("first ping is " + (waitFirstPing == false ? "ok" : "not found.") + " Waiting time is " + ((time2 - time) / 1000) + " sec.");
      }

      try {
        //conector.hello();
        // for whoop
        // conector.setSensitivityMax();
        if (connForParams==null){
          conector.setSensitivityMax();
        }else{
          int sens = VS_SETTING.getParam(connForParams, "VS_BASE_SENS", 12);
          conector.setSensitivity(sens);
        }  
        
        
        try {
          sleep(200);
        } catch (Exception e) {
        }
        conector.setTime();
        try {
          sleep(300);
        } catch (Exception e) {
        }
        //conector.setSensitivityMax();
        //try {
        //  sleep(300);
        //} catch (Exception e) {
        //}
        //conector.setTime();
      } catch (Exception e) {
      }
    }

  }

  public void connect() throws InterruptedException, SerialPortException, IOException {
    if (comPort.equalsIgnoreCase("WLAN")) {
      transport = new ConnectionSocket(this, reciver, network_sid, staticIP, portForListing, portForSending);
      connected = true;
      WIFI = true;
    } else {
      transport = new ConnectionCOMPort(this, reciver, comPort);
      connected = true;
      WIFI = false;
    }
    lastPingTime = Calendar.getInstance().getTimeInMillis();
    new AfterConnectionCommsnds(this);
    blinkingTimer.start();
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

  public void setPowerMax() throws SerialPortException {
    setPower(12);
  }

  public void getPower() throws SerialPortException {
    sentMessage("rcvpwr\r\n");
  }

  public void seachTransponder(int transponderID, int color) throws SerialPortException {
    sentMessage("searchtrans:" + transponderID + "," + color + ";\r\n");
  }

  public void setSensitivity(int sensivity) throws SerialPortException {
    sentMessage("setsensitivity:" + sensivity + "\r\n");
  }

  public void setSensitivityMax() throws SerialPortException {
    setSensitivity(12);
  }

  public void setColor(int transponderID, int color) throws SerialPortException {
    //sentMessage("sendcolor:" + transponderID + "," + color + "\r\n");
    sentMessage("sendcolor:" + color + "," + transponderID + "\r\n");

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
    blinkigQuee.add(new BlinkigQuee(transID, color, gateColor, isBlink));
    System.out.println("fire gate");
    blinkingTimer.setDelay(1);
    blinkingTimer.restart();
  }
  
  VSColor gateColor = null;
  int gateTransId = -1;
  int gateChecker= 0 ;
  public Timer blinkingTimer = new Timer(500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (connected && blinkigQuee.size() > 0) {
        blinkingTimer.setDelay(500);
        BlinkigQuee blink = blinkigQuee.get(0);
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
      }else{
        gateChecker++;
        if (gateChecker>=2){
          gateChecker=0;
          if (gateColor!=null){
            try{
              setColor(gateTransId, gateColor.vscolor);
            }catch(Exception ein){}              
          }
        }        
        
      }
    }
  });

  public void setTime() throws SerialPortException, InterruptedException {
    sentMessage("settime:" + Calendar.getInstance().getTime().getTime() + "\r\n");
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
    VSTimeConnector connector = new VSTimeConnector(new VSTimeMachineReciver() {
      @Override
      public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap) {
        //if (data.indexOf("ping")!=0)
        System.out.print("receive:" + data);
      }
    }, "WLAN", "", null, 0, 0,null);
    try {
      connector.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Thread.sleep(10000);
    connector.disconnect();
  }

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

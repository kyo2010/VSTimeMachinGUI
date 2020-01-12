/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

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
import vs.time.kkv.connector.connection.VSTimeConnection.VSTimeConnector;
import vs.time.kkv.models.VS_RACE_LAP;
import vs.time.kkv.models.VS_STAGE_GROUP;
import vs.time.kkv.models.VS_STAGE_GROUPS;

/**
 *
 * @author kyo
 */
public abstract class DroneConnector {
  
  public DroneConnector(DroneConection transport ) {  
    this.transport = transport;
    transport.setVSTimeConnector(this);   
  }
  
  public long lastPingTime = 0;
  public boolean connected = false;
  public String baseStationID = null;
  public int sensitivityIndex = 0;
  public String firmWareVersion = "";
  public DroneConection transport = null;
  public int lastTransponderID = -1;
  public boolean WIFI = false;
  public String last_error = "";
  public VSSendListener sendListener = null;
  public Integer lastFlashTransponderID = null;
  public Map<Integer, String> flashResponse = new HashMap<Integer, String>();
  public boolean waitFirstPing = false;
    
  public interface VSSendListener {
    public void sendVSText(String text);
  }

  public abstract void clearTransponderSearchQueue();

  public abstract boolean isTransponderSeached(int trans_id);

  public abstract void rfidLock(int key) throws SerialPortException;
  public abstract void rfidUnlock() throws SerialPortException;
  public abstract void checkConnection();
  public abstract void disconnect();

  public class AfterConnectionCommsnds extends Thread {
    DroneConnector conector = null;
    
    public AfterConnectionCommsnds(DroneConnector conector) {
      this.conector = conector;
      start();
    }
    @Override
    public void run() {
      if (conector!=null && conector.getDroneConnection()!=null) {
        conector.getDroneConnection().preStart();
      }      
    }
  }

  public abstract void connect() throws InterruptedException, SerialPortException, IOException, UserException ;
  public abstract void setVSTMParams(String baseID, VSTM_ESPInfo i) throws SerialPortException;
  public abstract void setPower(int powerIndex) throws SerialPortException;
  public abstract void getPower() throws SerialPortException;
  public abstract void seachTransponder(int transponderID, int color) throws SerialPortException;
  public abstract void setSensitivity(int sensivity) throws SerialPortException;
  public abstract void setColor(int transponderID, int color) throws SerialPortException;
  public abstract void setColorForTraficLight(int transponderID, VSColor color)  throws SerialPortException;
  public abstract VSTM_ESPInfo getVSTMParams(String baseID) throws SerialPortException;
  public abstract void addBlinkTransponder(int transID, int color, VSColor gateColor, boolean isBlink);
  public abstract void setTime() throws SerialPortException, InterruptedException;
  public abstract void clear() throws SerialPortException;
  public abstract void hello() throws SerialPortException;
  public abstract void getInfo(String baseID) throws SerialPortException;
  public abstract void sendflash(int TransID, String data) throws SerialPortException ;
  public abstract void sentMessage(String st) throws SerialPortException; 
  public abstract VSTM_LapInfo handleRequest(String[] commands, String[] params, long crc8) throws SerialPortException;
  public DroneConection getDroneConnection(){ return transport; };
  
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
  
   public VSSendListener getSendListener() {
    return sendListener;
  }

  public DroneConnector setSendListener(VSSendListener sendListener) {
    this.sendListener = sendListener;
    return this;
  }
  
  public void invate(VS_STAGE_GROUP invate_group){
    
  }
  
  public void runRace(VS_STAGE_GROUP invate_group){
    
  }
  
  public void startRace(VS_STAGE_GROUP group){
  }
  
  public void finishRace(VS_STAGE_GROUP invate_group){
    
  }
  
  public void showMessage(String message){}
  
  public void lapRace(VS_STAGE_GROUPS pilot, VS_RACE_LAP lap){}
}

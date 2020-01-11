/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.buttonsConnector;

import KKV.Utils.UserException;
import java.util.ArrayList;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.connection.DroneConnector;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;
import vs.time.kkv.connector.connection.DroneConection;

/**
 *
 * @author kyo
 */
public class ButtonComPortConnector implements SerialPortEventListener, DroneConection {

  public DroneConnector timeConnector = null;
  VSTimeMachineReciver receiver = null;
  public SerialPort serialPort = null;
  String comPort;
  int portSpeed;
  String channels;

  public void setVSTimeConnector(DroneConnector timeConnector) {
    this.timeConnector = timeConnector;
  }

  public void connect() throws UserException {
    try {
      serialPort = new SerialPort(comPort);
      serialPort.openPort();

      serialPort.setParams(portSpeed,
              SerialPort.DATABITS_8,
              SerialPort.STOPBITS_1,
              SerialPort.PARITY_NONE);
      serialPort.addEventListener(this);
    } catch (Exception e) {
      throw new UserException("Connection is error", e.toString());
    }
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
 
   public void preStart() {     
     sendData("init:"+channels);
   }
  
   public SerialCasheControl casheData = new SerialCasheControl();

  // SerialPort.BAUDRATE_115200
  public ButtonComPortConnector(VSTimeMachineReciver receiver, String comPort, int portSpeed, String channels) {
    this.receiver = receiver;
    this.comPort = comPort;
    this.portSpeed = portSpeed;
    this.channels = channels;
    //System.err.println("channels:"+channels);
  }

  public static class SerialCasheControl {

    String cashe = "";

    public SerialCasheControl() {
    }

    public void addData(String data) {
      cashe = cashe + data;
    }

    public List<String> getDatum() {
      List<String> res = new ArrayList();
      int pos = cashe.indexOf("\r\n");
      while (pos >= 0) {
        String data = cashe.substring(0, pos);
        res.add(data);
        cashe=cashe.substring(pos+2);
        pos = cashe.indexOf("\r\n");
      }
      return res;
    }
  }

  @Override
  public void serialEvent(SerialPortEvent event) {
    if (event.isRXCHAR() && event.getEventValue() > 0) {
      if (event.getEventValue() > 4) {//Check bytes count in the input buffer

        try {
          String datum = serialPort.readString(event.getEventValue());
          
          System.out.println("COM : "+datum);
          
          casheData.addData(datum);

          for (String data : casheData.getDatum()) {

            data = data.trim();
            if (receiver != null) {
              receiver.receiveDataForLog(data);
            }
            if (data.indexOf("ping") == -1) {
              int y = 0;
            }
            /* int len = data==null?0:data.length();
        if (len>=2 && data.substring(len-2,len).equalsIgnoreCase("\r\n")){
          data = data.substring(0,len-2);
        };*/
            String[] commands = data.split(":");
            String[] params = null;
            long crc8 = 0;
            int lpos = data.lastIndexOf(",");
            if (lpos > 0) {
              String nd = data.substring(0, lpos);
              crc8 = DroneConnector.crc8(nd.getBytes());
            }
            VSTM_LapInfo lap = null;
            if (commands.length >= 2) {
              params = commands[1].split(",");
              lap = timeConnector.handleRequest(commands, params, crc8);
            }
            if (receiver != null) {
              receiver.receiveData(data, commands, params, lap);
            }
          }
        } catch (SerialPortException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  @Override
  public void sendData(String data) {

  }

  @Override
  public void disconnect() {
    if (serialPort != null) {
      try {
        serialPort.closePort();
      } catch (SerialPortException ex) {
        //Logger.getLogger(ConnectionCOMPort.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public int getTimeOutForReconnect() {
    return 5;
  }
  
  public static void printMas(String info, List<String> list){
    System.out.print(info + " Result : ");
    for (String l : list){
      System.out.print(l+";");
    }
    System.out.println();
  }
  
  public static void main (String[] args){
    SerialCasheControl cc = new SerialCasheControl();
    printMas("expected ''",cc.getDatum());
    cc.addData("test");    
    printMas("expected ''",cc.getDatum());
    cc.addData("1\r\ntest2");            
    printMas("expected 'test1'",cc.getDatum());
    cc.addData("\r\ntest3\r\ntest4\r");  
    cc.addData("\ntest5");     
    printMas("expected 'test2;test3;test4'",cc.getDatum());
    cc.addData("\r\n");        
    printMas("expected 'test5'",cc.getDatum());    
  }
  
  public boolean needToAutoReconect() { return true; };

}

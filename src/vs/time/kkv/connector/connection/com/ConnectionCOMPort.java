/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection.com;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;
import vs.time.kkv.connector.connection.ConnectionVSTimeMachine;
import vs.time.kkv.connector.connection.VSTimeConnector;
import vs.time.kkv.connector.connection.VSTimeMachineReciver;

/**
 *
 * @author kyo
 */
public class ConnectionCOMPort implements SerialPortEventListener, ConnectionVSTimeMachine {

  public VSTimeConnector timeConnector = null;
  VSTimeMachineReciver receiver = null;
  public SerialPort serialPort = null;

  public ConnectionCOMPort(VSTimeConnector timeConnector, VSTimeMachineReciver receiver, String comPort) throws SerialPortException {
    this.receiver = receiver;
    this.timeConnector = timeConnector;
    serialPort = new SerialPort(comPort);
    serialPort.openPort();

    serialPort.setParams(SerialPort.BAUDRATE_115200,
              SerialPort.DATABITS_8,
              SerialPort.STOPBITS_1,
              SerialPort.PARITY_NONE);
    serialPort.addEventListener(this);
  }

  @Override
  public void serialEvent(SerialPortEvent event) {
    if (event.isRXCHAR() && event.getEventValue() > 0) {
      String data = "";
      try {
        data = serialPort.readString(event.getEventValue());
        data = data.trim();
        if (data.indexOf("ping")==-1){
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
        if (lpos>0){
          String nd = data.substring(0,lpos);
          crc8 = VSTimeConnector.crc8(nd.getBytes());
        }
        VSTM_LapInfo lap = null;
        if (commands.length >= 2) {
          params = commands[1].split(",");
          lap = timeConnector.handleRequest(commands, params,crc8);
        }
        if (receiver!=null) receiver.receiveData(data,commands,params,lap);
      } catch (SerialPortException ex) {
        ex.printStackTrace();
      }      
    }
  }

  @Override
  public void sendData(String data) {
    if (serialPort != null) {
      try {
        serialPort.writeString(data);
      } catch (SerialPortException ex) {
        Logger.getLogger(ConnectionCOMPort.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
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

}

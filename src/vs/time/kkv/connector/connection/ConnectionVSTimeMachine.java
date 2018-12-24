/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

import KKV.Utils.UserException;

/**
 *
 * @author kyo
 */
public interface ConnectionVSTimeMachine {
  public void sendData(String data);
  public void disconnect() throws UserException;
  public void connect() throws UserException;
  public int getTimeOutForReconnect();
  public void setVSTimeConnector (DroneConnector timeConnector);
}

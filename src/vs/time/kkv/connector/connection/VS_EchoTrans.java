/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

/**
 *
 * @author kyo
 */
public class VS_EchoTrans {
  public int transID;
  public String firmwareVersion;

  public VS_EchoTrans(int transID, String firmwareVersion) {
    this.transID = transID;
    this.firmwareVersion = firmwareVersion;
  }
  
}

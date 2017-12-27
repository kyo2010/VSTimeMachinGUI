/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

/**
 *
 * @author kyo
 */
public class VSTM_ESPInfo {
    public boolean isError = true;
    public String baseID = "";
    public int sensitivity = 0;
    public int connectionType = 0; //0- is normall, 1- for router
    public String SSID = "kkv";
    public String SSPWD = "vs123456";
    public String ip1 = "192";
    public String ip2 = "168";
    public String ip3 = "197";
    public String port_receiver = "8888";
    public String port_send = "8889";
    public String ver_esp = "";   

    public String toString() {
      return ("espsetinfo:" + baseID + "," + sensitivity + "," + connectionType + "," + SSID + "," + SSPWD + "," + ip1 + "," + ip2 + "," + ip3 + "," + port_receiver + "," + port_send);
    }

    public VSTM_ESPInfo() {
    }
}

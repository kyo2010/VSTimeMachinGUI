/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.connection;

import vs.time.kkv.connector.TimeMachine.VSTM_LapInfo;

/**
 *
 * @author kyo
 */
public interface VSTimeMachineReciver {
    public void receiveData(String data, String[] commands, String[] params, VSTM_LapInfo lap);
    public void receiveDataForLog(String data);
}

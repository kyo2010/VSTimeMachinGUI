/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import vs.time.kkv.connector.Utils.TTS.LANG.ISpeachMessages;

/**
 *
 * @author kyo
 */
public interface IKKVSpeek {
  public void say(String text);
  public String getTTSName();
  public boolean useTranslit();
  public ISpeachMessages returnSpeachMessages();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS.LANG;

/**
 *
 * @author kyo
 */
public class SpeekText {

    public String textToSpeach;
    public long time = 1500; 
    public long wait = 0;    

    public SpeekText(String textToSpeach) {
      this.textToSpeach = textToSpeach;
    }
    public SpeekText(String textToSpeach, long delay) {
      this.textToSpeach = textToSpeach;
      this.time = delay;
    }
    public SpeekText(String textToSpeach, long delay, long wait) {
      this.textToSpeach = textToSpeach;
      this.time = delay;
      this.wait = wait;
    }
  }
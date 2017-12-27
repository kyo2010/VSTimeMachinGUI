/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.util.Scanner;

/**
 *
 * @author kyo
 */
public class SpeekUtil {

  // Some available voices are (kevin, kevin16, alan) 
  public static final String VOICE_ALAN = "alan";
  public static final String VOICE_KEVIN = "kevin";
  public static final String VOICE_KEVIN16 = "kevin16";
  
  private final Voice voice;

  public SpeekUtil() {
    VoiceManager vm = VoiceManager.getInstance();
    voice = vm.getVoice(VOICE_KEVIN16);
    voice.allocate();
  }

  public void speak(String inputText) {
    if (inputText != null && !inputText.isEmpty()) {
      voice.speak(inputText);
    }
  }

  public static void main(String[] args) {
    SpeekUtil speaker = new SpeekUtil();
    speaker.speak("Kolpakov, finish");

  }

}

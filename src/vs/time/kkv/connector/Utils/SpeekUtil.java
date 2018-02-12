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

  private static String alpha = new String("абвгдеЄжзиыйклмнопрстуфхцчшщьэю€");
  private static String[] _alpha = {"a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
    "k", "l", "m", "n", "o", "p", "r", "s", "t", /*"u"*/ "oo",
    "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya"};
  

  private final Voice voice;
  
  TextToSpeech[] tts_mas = new TextToSpeech[]{
    new TextToSpeech(),
    //new TextToSpeech(),
    //new TextToSpeech(),
  };

  public int current = 0;
  public synchronized TextToSpeech getTTS(){            
    TextToSpeech tts = tts_mas[current];
    current++;
    if (current>=tts_mas.length) current = 0;
    return tts;
  }
  
  public SpeekUtil() {
    
    VoiceManager vm = VoiceManager.getInstance();
    voice = vm.getVoice(VOICE_KEVIN16);
    voice.allocate();
  }
  
  

  public void speak(final String inputText) {
    //if (inputText != null && !inputText.isEmpty()) {
    //  voice.speak(inputText);
    //}
    
    new Thread(){          
      @Override
      public void run() { 
        TextToSpeech tts = getTTS();
        while (tts.tts!=null && tts.tts.isPlaying()){
          try{
            Thread.currentThread().wait(100);
          }catch(Exception ein){}
        }
        getTTS().speak(translit(inputText), 2.0f, false, true);
      }                 
    }.start();
  }

  public static void main(String[] args) {
    SpeekUtil speaker = new SpeekUtil();
    speaker.speak("Kolpakov, finish");
    speaker.speak("Konstantin второй круг");
    speaker.speak("Konstantin третий круг");
    speaker.speak("Konstantin finish");

  }

  public String translit(String text) {
    StringBuffer textOut = new StringBuffer();
    char[] chs = text.toLowerCase().toCharArray();
    for (int i = 0; i < chs.length; i++) {
      int k = alpha.indexOf(chs[i]);
      if (k != -1) {
        textOut.append(_alpha[k]);
      } else {
        textOut.append(chs[i]);
      }
    }
    return textOut.toString();
  }
  
  public String krugNumber(int lap){
    if (lap==1) return "первый";
    if (lap==2) return "второй";
    if (lap==3) return "третий";
    if (lap==4) return "четвертый";
    if (lap==5) return "п€тый";
    if (lap==6) return "шестой";
    if (lap==7) return "седьмой";
    if (lap==8) return "восьмой";
    if (lap==9) return "дев€тый";
    return ""+lap;
  }

}

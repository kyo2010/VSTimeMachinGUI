/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import vs.time.kkv.connector.MainForm;
import static vs.time.kkv.connector.MainForm._mainForm;
import vs.time.kkv.connector.Utils.TTS.LANG.ISpeachMessages;
import vs.time.kkv.connector.Utils.TTS.LANG.SpeekText;
import vs.time.kkv.models.VS_SETTING;

/**
 *
 * @author kyo
 */
public class SpeekUtil extends Thread {

  // Some available voices are (kevin, kevin16, alan) 
  public static final String VOICE_ALAN = "alan";
  public static final String VOICE_KEVIN = "kevin";
  public static final String VOICE_KEVIN16 = "kevin16";

  private static String alpha = new String("абвгдеёжзиыйклмнопрстуфхцчшщьэюя");
  private static String[] _alpha = {"a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
    "k", "l", "m", "n", "o", "p", "r", "s", "t", /*"u"*/ "oo",
    "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya"};


  private final Voice voice;
  public IKKVSpeek defaultSpeaker = null;

  TextToSpeech[] tts_mas = new TextToSpeech[]{
    new TextToSpeech(), //new TextToSpeech(),
  //new TextToSpeech(),
  };

  public int current = 0;

  public synchronized TextToSpeech getTTS() {
    /*TextToSpeech tts = tts_mas[current];
    current++;
    if (current>=tts_mas.length) current = 0;
    return tts;*/
    return tts_mas[0];
  }

  MainForm mainForm;
  public SpeekUtil(MainForm mainForm) {

    this.mainForm = mainForm;
    VoiceManager vm = VoiceManager.getInstance();
    voice = vm.getVoice(VOICE_KEVIN16);
    voice.allocate();

    reset();
    start();
  }
 

  @Override
  public void run() {
    while (!MainForm.PLEASE_CLOSE_ALL_THREAD) {
      if (stack.size() > 0) {
        try {
          SpeekText st = stack.get(0);
          String txt =st.textToSpeach;
          stack.remove(0);
          if (defaultSpeaker.useTranslit()) {
            txt = translit(txt);
          }
          if (defaultSpeaker != null) {
            defaultSpeaker.say(txt);
          }
          sleep(st.time);
        } catch (Exception e) {
        }
      } else {
        try {
          sleep(100);
        } catch (Exception e) {
        }
      }
    }
  }
  
  public ISpeachMessages getSpeachMessages(){
    if (defaultSpeaker!=null) return defaultSpeaker.returnSpeachMessages();
    return ISpeachMessages.EN;
  }

  Vector<SpeekText> stack = new Vector<SpeekText>();

  public void reset() {
    defaultSpeaker = TextToSpeachFactory.getByNameTTS(VS_SETTING.getParam(mainForm.con, "TTS_API", ""));
  }

  public void speak(SpeekText st) {
    //if (inputText != null && !inputText.isEmpty()) {
    //  voice.speak(inputText);
    //}

    /*new Thread(){          
      @Override
      public void run() { 
        TextToSpeech tts = getTTS();
        tts.speak(translit(inputText), 1.0f, true, true);
      }                 
    }.start();*/
    stack.add(st);
    if (st.wait>0){
      try{
        //wait(st.wait);
        sleep(st.wait);
      }catch(Exception e){}
    }
  }

  public static void main(String[] args) throws MaryConfigurationException {

    MaryInterface marytts = new LocalMaryInterface();
    Set<String> voices = marytts.getAvailableVoices();
    for (String voice : voices) {
      System.out.println(voice);
    }

    //SpeekUtil speaker = new SpeekUtil();
    //speaker.speak("Kolpakov, finish");
    //speaker.speak("Konstantin ������ ����");
    //speaker.speak("Konstantin ������ ����");
    //speaker.speak("Konstantin finish");
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

  

}

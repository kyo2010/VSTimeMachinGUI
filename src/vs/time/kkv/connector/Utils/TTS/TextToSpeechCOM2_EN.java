/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Variant;
import vs.time.kkv.connector.Utils.TTS.LANG.ISpeachMessages;

/**
 *
 * @author kyo
 */
public class TextToSpeechCOM2_EN implements IKKVSpeek {

  ActiveXComponent  speak = null;

  public TextToSpeechCOM2_EN() {    
    if (speak == null) {
      try {
        //speak = new ActiveXComponent("SPEECH.SpVoice");
        speak = new ActiveXComponent("SAPI.SpVoice");       
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void say(String text) {
    // set s = CreateObject("SAPI.SpVoice")
    // s.Speak Wscript.Arguments(0), 3
    try {
      // !!!!!! Please load audio forma notify.wav
      speak.invoke("Speak", new Variant[]{new Variant("   "+text),new Variant(1)});
    } catch (RuntimeException rt){    
    } catch (UnsupportedClassVersionError rt1){   
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void pause() {
    try {
      //Thread.currentThread().wait(3000);
      Thread.sleep(1500);
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) {
    TextToSpeechCOM2_EN tts = new TextToSpeechCOM2_EN();
    tts.say("тест");
    pause();
    tts.say("Hello тест");
    pause();
    tts.say("Hi world");
    try {
      //Thread.currentThread().wait(3000);
      Thread.sleep(3000);
    } catch (Exception e) {
    }
    System.out.println("exit");

  }

  public String getTTSName() {
    return "Windows TTS2 [ENG]";
  }

  ;
  
  public boolean useTranslit() {
    return false;
  }

  ;
  
  public ISpeachMessages returnSpeachMessages() {
    return ISpeachMessages.EN;
  }

  @Override
  protected void finalize() throws Throwable {
    ComThread.Release();
  }
  
  

}

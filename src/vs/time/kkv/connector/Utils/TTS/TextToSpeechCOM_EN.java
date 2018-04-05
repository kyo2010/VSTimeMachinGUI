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
import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;
import vs.time.kkv.connector.Utils.TTS.LANG.ISpeachMessages;

/**
 *
 * @author kyo
 */
public class TextToSpeechCOM_EN implements IKKVSpeek{

  ReleaseManager rm = new ReleaseManager();
  IDispatch speak = null;
  
  public TextToSpeechCOM_EN() {
    try{    
      speak = new IDispatch(rm, "SAPI.SpVoice");                
    }catch(Exception e){
      e.printStackTrace();
    }
	}
  
  public void say(String text){
    // set s = CreateObject("SAPI.SpVoice")
    // s.Speak Wscript.Arguments(0), 3
    try{
      // !!!!!! Please load audio forma notify.wav
      speak.method("Speak",new Object[]{text,3});
    } catch(Exception e){
      e.printStackTrace();
    }    
  }
  
  public static void pause(){
  try{
      //Thread.currentThread().wait(3000);
      Thread.sleep(1500);
    }catch(Exception e){}
  }
  
  public static void main(String[] args){
    TextToSpeechCOM_EN tts = new TextToSpeechCOM_EN();
    tts.say("тест");
    pause();
    tts.say("Hello тест");
    pause();
    tts.say("Hi world");
    try{
      //Thread.currentThread().wait(3000);
      Thread.sleep(3000);
    }catch(Exception e){}
    System.out.println("exit");
    
  }
  
  public String getTTSName(){
    return "Windows TTS [ENG]"; 
  };
  
  public boolean useTranslit(){
    return false;
  };
  
  public ISpeachMessages returnSpeachMessages(){
    return ISpeachMessages.EN;
  };
  
}

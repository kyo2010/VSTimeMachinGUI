/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;
import vs.time.kkv.connector.Utils.TTS.LANG.ISpeachMessages;

/**
 *
 * @author kyo
 */
public class TextToSpeechCOM implements IKKVSpeek{

  ReleaseManager rm = new ReleaseManager();
  IDispatch speak = null;
  
  public TextToSpeechCOM() {
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
    TextToSpeechCOM tts = new TextToSpeechCOM();
    tts.say("������ �����");
    pause();
    tts.say("Hello ������ ����");
    pause();
    tts.say("Hi world");
    try{
      //Thread.currentThread().wait(3000);
      Thread.sleep(3000);
    }catch(Exception e){}
    System.out.println("exit");
    
  }
  
  public String getTTSName(){
    return "Windows TTS"; 
  };
  
  public boolean useTranslit(){
    return false;
  };
  
  public ISpeachMessages returnSpeachMessages(){
    return ISpeachMessages.RU;
  };
  
}

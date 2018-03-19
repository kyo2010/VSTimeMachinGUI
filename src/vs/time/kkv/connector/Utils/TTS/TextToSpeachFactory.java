/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kyo
 */
public class TextToSpeachFactory {
  public static List<IKKVSpeek> TTS_LIST = new ArrayList<IKKVSpeek>();
  
  static{
    try{
      IKKVSpeek api_en = new TextToSpeechCOM_EN();
      TTS_LIST.add(api_en);
    }catch(java.lang.UnsatisfiedLinkError | Exception e){      
    }
    try{
      TTS_LIST.add(new TextToSpeech());
    }catch(RuntimeException re){
    }catch(Exception e){}
    try{
      TTS_LIST.add(new TextToSpeechCOM());
    }catch(java.lang.UnsatisfiedLinkError | Exception e){      
    }    
  }
  
  public static String[] getTTSNames(){
    String[] result = new String[TTS_LIST.size()];
    int index = 0;
    for (IKKVSpeek tts: TTS_LIST){
      result[index] = "TTS"+index;
      try{
        result[index] = tts.getTTSName();
      }catch(Exception e){}
      index++;
    }
    return result;
  }
  
  public static IKKVSpeek getTTS(int index){
    if (index>=TTS_LIST.size()){
      if (TTS_LIST.size()>0) return TTS_LIST.get(0);
      return null;
    }
    return TTS_LIST.get(index);
  }
  
  public static IKKVSpeek getByNameTTS(String name){
    IKKVSpeek result = null;
    for (IKKVSpeek tts: TTS_LIST){      
      if (result==null) result = tts;
      try{
        if( tts.getTTSName().equalsIgnoreCase(name)) return tts;
      }catch(Exception e){}      
    }
    return result;
  }
}

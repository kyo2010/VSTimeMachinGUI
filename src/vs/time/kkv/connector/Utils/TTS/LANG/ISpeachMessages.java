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
public class ISpeachMessages {
  
  public static ISpeachMessages EN = new EngSpeachMessages();
  public static ISpeachMessages RU = new RusSpeachMessages();
  
  public String groupFinished(long groupIndex){
    return "Group " + groupIndex + " has been finshed";
  };
  
  public String pilotIsChecked(String pilot){
    return pilot+" is checked";
  }
  
  public String raceIsOverIn10sec(){
   return "The Race will be finished in ten seconds";
  }
  
  public String connected(){
    return "connected";
  }
  
  public String disconnected(){
    return "disconnected";
  }
  
  public String pilot(String name){
    return "Pilot "+name;
  }
  
}

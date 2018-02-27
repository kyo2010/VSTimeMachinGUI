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
public class RusSpeachMessages extends ISpeachMessages{
  public String groupFinished(long groupIndex){
    return "Гонка группы " + groupIndex + " закончена";
  };
  
  public String pilotIsChecked(String pilot){
    return pilot+" найден";
  }
  
  public String raceIsOverIn10sec(){
   return "Гонка закончится через десять секунд";
  }
  
  public String connected(){
    return "соединено";
  }
  
  public String disconnected(){
    return "разъединено";
  }
  
  public String pilot(String name){
    return "Пилот "+name;
  }
}

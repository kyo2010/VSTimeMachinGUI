/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS.LANG;

import java.util.List;

/**
 *
 * @author kyo
 */
public class RusSpeachMessages extends ISpeachMessages{
  public SpeekText groupFinished(long groupIndex){
    return new SpeekText("Гонка группы " + groupIndex + " закончена",2000);
  };
  
  public SpeekText pilotIsChecked(String pilot){
    return new SpeekText(pilot+" найден");
  }
  
  public SpeekText raceIsOverIn10sec(){
   return new SpeekText("Гонка закончится через десять секунд",2000);
  }
  
  public SpeekText connected(){
    return new SpeekText("соединено");
  }
  
  public SpeekText disconnected(){
    return new SpeekText("разъединено");
  }
  
  public SpeekText pilot(String name){
    return new SpeekText("Пилот "+name,3000);
  }
  
  public SpeekText startMessage(String id){
    if (id.equalsIgnoreCase("one")) return new SpeekText("Один",350);
    if (id.equalsIgnoreCase("two")) return new SpeekText("Два",350);
    if (id.equalsIgnoreCase("three")) return new SpeekText("Три",350);
    if (id.equalsIgnoreCase("attention")) return new SpeekText("Внимание, старт после звукового сигнала",5000,5000);
    return new SpeekText(id);      
  }
  
   public SpeekText lapTime(String pilot, int lap, int countLaps) {
    String krug = "";
    if (lap == countLaps) {
      krug = " финиш";
    } else {
      krug =  " круг "+lap;
    }
    /*return new SpeekText(pilot +" "+krug);*/
    if (lap==0) return new SpeekText(pilot + " старт");
    if (lap>countLaps) return new SpeekText(pilot);
    return new SpeekText(pilot +krug);
  }
  
  public String krugNumber(int lap) {
    if (lap == 1) {
      return "первый";
    }
    if (lap == 2) {
      return "второй";
    }
    if (lap == 3) {
      return "третий";
    }
    if (lap == 4) {
      return "четвертый";
    }
    if (lap == 5) {
      return "пятый";
    }
    if (lap == 6) {
      return "шестой";
    }
    if (lap == 7) {
      return "седьмой";
    }
    if (lap == 8) {
      return "восьмой";
    }
    if (lap == 9) {
      return "девятый";
    }
    return "" + lap;
  }
  
   public SpeekText invatieGroup(long num, List<String> pilots){
    String text = "Приглашается группа "+num+". ";
    for (String pilot :  pilots){
      text += pilot+"...";      
    }
    return new SpeekText(text, 1000+1000*pilots.size());
  }
   
   public SpeekText findTransponders(long num){
    String text = "Внимание, Группа "+num+". Идет поиск транспондеров. Проверьте питание ваших дронов.";    
    return new SpeekText(text, 2000);
  }
}

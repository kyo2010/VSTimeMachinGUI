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
    return new SpeekText("гонка группы " + groupIndex + " завершена",2000);
  };
  
  public SpeekText pilotIsChecked(String pilot){
    return new SpeekText(pilot+" найден");
  }
  
  public SpeekText raceIsOverIn10sec(){
   return new SpeekText("гонка закончится через десять секунд",2000);
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
  
  public SpeekText gate() {
    return new SpeekText("транспондер для ворот", 1000);
  }
  
  public SpeekText startMessage(String id){
    if (id.equalsIgnoreCase("one")) return new SpeekText("Один",350);
    if (id.equalsIgnoreCase("two")) return new SpeekText("Два",350);
    if (id.equalsIgnoreCase("three")) return new SpeekText("Три",350);
    if (id.equalsIgnoreCase("attention")) return new SpeekText("внимание, старт после звукового сигнала",5000,5000);
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
    if (lap==0) return new SpeekText(pilot + " старт",1000);
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
  
  public SpeekText raceWillBeStarted(long miliseconds) {    
    long value = miliseconds/1000/60;
    String value_st = null;
    String mesure = "минуты";
    if (value==1) {
      value_st = "одну";
      mesure = "минуту";
    } 
    if (value==1) {
      value_st = "пять";
      mesure = "минут";
    } 
    if (value==0) {
      mesure = "секунд";
      value = miliseconds/1000; 
    }
    if (value_st==null) value_st = ""+value;
    return new SpeekText("Гонка начнется через "+value_st+" "+mesure, 3000);
  }
  
   public SpeekText invatieGroup(long num, List<String> pilots){
    String text = "приглашается группа "+num+". ";
    int count_of_chars = 0;
    for (String pilot :  pilots){
      text += pilot+"...";   
      count_of_chars += pilot.length();
    }
    return new SpeekText(text, 1500+2000*pilots.size() );
  }
   
   public SpeekText waitingAdmin() {
    return new SpeekText("Администраторы проверьте засечку", 1200);
  }  
   
   public SpeekText stageFinished() {
    return new SpeekText("Этап завершен", 500);
  } 
   
   public SpeekText findTransponders(long num){
    String text = "поиск транспондеров. Группа "+num;    
    return new SpeekText(text, 2000);
  }
}

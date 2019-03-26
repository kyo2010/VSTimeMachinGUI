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
public class ISpeachMessages {

  public static ISpeachMessages EN = new EngSpeachMessages();
  public static ISpeachMessages RU = new RusSpeachMessages();

  public SpeekText groupFinished(long groupIndex) {
    return new SpeekText("Group " + groupIndex + " has been finshed", 2000);
  }
  
  public SpeekText pilotIsChecked(String pilot) {
    return new SpeekText(pilot + " is checked");
  }

  public SpeekText raceIsOverIn10sec() {
    return new SpeekText("The Race will be finished in ten seconds", 3000);
  }
  
  public SpeekText raceTimeIsOver() {
    return new SpeekText("Main Race Time is Over!", 2000);
  }
  
  public SpeekText raceWillBeStarted(long miliseconds) {    
    long value = miliseconds/1000/60;
    String mesure = "minutes";
    if (value==1) mesure = "minute"; 
    if (value==0) {
      mesure = "seconds";
      value = miliseconds/1000; 
    }
    return new SpeekText("The Race will be started in "+value+" "+mesure, 3000);
  }
  
  public SpeekText waitingAdmin() {
    return new SpeekText("Administrator please check transponder hub", 1200);
  }  
  
  public SpeekText stageFinished() {
    return new SpeekText("The stage has been finished", 1000);
  }  

  public SpeekText connected() {
    return new SpeekText("connected", 800);
  }
  
  public SpeekText gate() {
    return new SpeekText("Transonder of gate", 800);
  }

  public SpeekText disconnected() {
    return new SpeekText("disconnected", 800);
  }

  public SpeekText pilot(String name) {
    return new SpeekText("Pilot " + name,1500);
  }
  
  public SpeekText msg(String name) {
    return new SpeekText(name);
  }

  public SpeekText startMessage(String id) {
    if (id.equalsIgnoreCase("one")) {
      return new SpeekText("One", 450);
    }
    if (id.equalsIgnoreCase("two")) {
      return new SpeekText("two", 450);
    }
    if (id.equalsIgnoreCase("three")) {
      return new SpeekText("three", 450);
    }
    if (id.equalsIgnoreCase("attention")) {
      return new SpeekText("attention", 1000,1000);
    }
    return new SpeekText(id);
  }
  
  public SpeekText saySecunds(int seconds) {
    return new SpeekText(""+seconds, 1000);
  }

  public SpeekText lapTime(String pilot, int lap, int countLaps, int HALF_LAPS_CALCL) {
    String krug = "";        
    
    int lap_real = lap;
    if (HALF_LAPS_CALCL==1) lap_real = lap/2;
    
     if (lap_real == countLaps) {
      krug = " finish";
    } else {      
      String addon = "";
      if (lap%2==1 && HALF_LAPS_CALCL==1) addon = " and half";
      if (lap_real==0){
        krug =  " half lap";
      }else{
        krug =  " lap "+lap_real+addon;
      }  
    }
    /*return new SpeekText(pilot +" "+krug);*/
    if (lap==0) return new SpeekText("");
    if (lap_real>countLaps || (HALF_LAPS_CALCL==1 && lap%2==1 && lap_real==countLaps)) return new SpeekText(pilot);
    return new SpeekText(pilot +krug);
  }

  public String krugNumber(int lap_db, int half_lap) {
    int lap = lap_db;
    if (half_lap==1){
      lap = Math.round(lap_db/2);
    }    
    String addon = "";
    if (lap!=lap_db/2){
      addon = " and half";
    }    
    if (lap == 0) {
      return "half";
    }            
    if (lap == 1) {
      return "first"+addon;
    }
    if (lap == 2) {
      return "second"+addon;
    }
    if (lap == 3) {
      return "third"+addon;
    }
    if (lap == 4) {
      return "fourth"+addon;
    }
    if (lap == 5) {
      return "fifth"+addon;
    }
    if (lap == 6) {
      return "sixth"+addon;
    }
    if (lap == 7) {
      return "seventh"+addon;
    }
    if (lap == 8) {
      return "eighth"+addon;
    }
    if (lap == 9) {
      return "ninth"+addon;
    }
    return "" + lap+addon;
  }
  
  public SpeekText invatieGroup(long num, List<String> pilots){
    String text = "Invate Group "+num+". ";
    for (String pilot :  pilots){
      if (!text.equalsIgnoreCase("")) text+=", ";
      text += pilot;      
    }
    return new SpeekText(text, 1500+2000*pilots.size());
  }
  
  public SpeekText findTransponders(long num){
    String text = "Finding transponders. Group "+num+".";    
    return new SpeekText(text, 2000);
  }

}

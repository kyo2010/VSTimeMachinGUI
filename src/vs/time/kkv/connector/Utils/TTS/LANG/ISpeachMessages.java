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

  ;
  
  public SpeekText pilotIsChecked(String pilot) {
    return new SpeekText(pilot + " is checked");
  }

  public SpeekText raceIsOverIn10sec() {
    return new SpeekText("The Race will be finished in ten seconds", 3000);
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
      return new SpeekText("One", 400);
    }
    if (id.equalsIgnoreCase("two")) {
      return new SpeekText("two", 400);
    }
    if (id.equalsIgnoreCase("three")) {
      return new SpeekText("three", 400);
    }
    if (id.equalsIgnoreCase("attention")) {
      return new SpeekText("attention", 1000,1000);
    }
    return new SpeekText(id);
  }

  public SpeekText lapTime(String pilot, int lap, int countLaps) {
    String krug = "";
    if (lap == countLaps) {
      krug = " finish";
    } else {
      krug =  " lap "+lap;
    }
    /*return new SpeekText(pilot +" "+krug);*/
    if (lap==0) return new SpeekText("");
    if (lap>countLaps) return new SpeekText(pilot);
    return new SpeekText(pilot +krug);
  }

  public String krugNumber(int lap) {
    if (lap == 1) {
      return "first";
    }
    if (lap == 2) {
      return "second";
    }
    if (lap == 3) {
      return "third";
    }
    if (lap == 4) {
      return "fourth";
    }
    if (lap == 5) {
      return "fifth";
    }
    if (lap == 6) {
      return "sixth";
    }
    if (lap == 7) {
      return "seventh";
    }
    if (lap == 8) {
      return "eighth";
    }
    if (lap == 9) {
      return "ninth";
    }
    return "" + lap;
  }
  
  public SpeekText invatieGroup(long num, List<String> pilots){
    String text = "Invate Group "+num+". Pilots : ";
    for (String pilot :  pilots){
      text += pilot+"...";      
    }
    return new SpeekText(text, 1500+2000*pilots.size());
  }
  
  public SpeekText findTransponders(long num){
    String text = "Finding transponders. Group "+num+".";    
    return new SpeekText(text, 2000);
  }

}

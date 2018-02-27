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

  public SpeekText groupFinished(long groupIndex) {
    return new SpeekText("Group " + groupIndex + " has been finshed", 2000);
  }

  ;
  
  public SpeekText pilotIsChecked(String pilot) {
    return new SpeekText(pilot + " is checked");
  }

  public SpeekText raceIsOverIn10sec() {
    return new SpeekText("The Race will be finished in ten seconds", 2000);
  }

  public SpeekText connected() {
    return new SpeekText("connected", 800);
  }

  public SpeekText disconnected() {
    return new SpeekText("disconnected", 800);
  }

  public SpeekText pilot(String name) {
    return new SpeekText("Pilot " + name);
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
      krug = "finish";
    } else {
      krug = krugNumber(lap + 1) + " lap";
    }
    return new SpeekText(pilot +" "+krug);
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

}

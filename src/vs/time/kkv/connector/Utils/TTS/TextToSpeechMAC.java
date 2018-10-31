/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
public class TextToSpeechMAC implements IKKVSpeek {

 // MacSpeaker speak = null;
    
  public TextToSpeechMAC() {
    try {
   //   speak = new MacSpeaker(); 
      selectVoice();
    } catch (Exception e) {
      e.printStackTrace();
    }    
  }

  public void say(String text) {
    try {
      //speak.speak(text);
        speak(text);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void pause() {
    try {
      //Thread.currentThread().wait(3000);
      Thread.sleep(1500);
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) {
    TextToSpeechMAC tts = new TextToSpeechMAC();
    tts.say("тест");
    pause();
    tts.say("Hello тест");
    pause();
    tts.say("Hi world");
    try {
      //Thread.currentThread().wait(3000);
      Thread.sleep(3000);
    } catch (Exception e) {
    }
    System.out.println("exit");

  }
  
  void selectVoice(){
   /*try {
      //Process p = Runtime.getRuntime().exec("say -v Bruce");
      Process p = Runtime.getRuntime().exec("say -v Miliena");
    } catch (IOException ex) {
    }*/    
  }
  
  void speak(String text){
    String msg = "say \""+text+"\"";
    try {  //  say "Privet mir"      
      Process p = Runtime.getRuntime().exec(msg);
      System.out.println("msg : "+msg);
    } catch (IOException ex) {
    }    
    //System.out.println("msg : "+msg);
  }
  

  public String getTTSName() {
    return "Mac OS TTS";
  } 
  
  public boolean useTranslit() {
    return false;
  }

  public ISpeachMessages returnSpeachMessages() {
    return ISpeachMessages.RU;
  }
  
  
  
public class MacSpeaker {
 
  protected File scriptFile;
  protected Process process;
  protected final String[] voices;
  protected int voiceIndex = 0;
  //protected int pitch = 0; // -10 to +10
  protected int volume = 50; // 0 to 100
  protected int rate = 100; // 80 to 400
 
  
 
  private void test() {
    int numVoices = voices.length;
    if (numVoices == 0) {
      return;
    }
    Random random = new Random();
    for (int i = 0; i < 10 && i < numVoices; i++) {
      voiceIndex = random.nextInt(numVoices);
      volume = random.nextInt(100);
      System.out.println("Voice: " + voices[voiceIndex]
              + "  Volume: " + volume);
      speakAndWait("Hello World");
      try {
        Thread.sleep(500);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }
 
  public MacSpeaker() {
    voices = populateVoiceArray();
  }
 
  protected String[] populateVoiceArray() {
    Scanner input = null;
    try {
      Process p = Runtime.getRuntime().exec("say -v '?'");
      new ProcessReader(p, true); // consume the error stream
      input = new Scanner(p.getInputStream());
      List<String> voiceList = new ArrayList<String>();
      while (input.hasNextLine()) {
        voiceList.add(input.nextLine());
      }
      return voiceList.toArray(new String[0]);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return new String[]{};
  }
 
  protected void makeScriptFile() {
    String speakScript = ""
            + "on run argv\n"
            + "  set theCall to (item 1 of argv)\n"
            + "  set theVoice to (item 2 of argv)\n"
            + "  set theVolume to (item 3 of argv)\n"
            + "  set theRate to (item 4 of argv)\n"
            + "  set oldVolume to output volume of (get volume settings)\n"
            + "  set volume output volume ((oldVolume * theVolume) / 100)\n"
            + "  say -v theVoice  -r theRate theCall with waiting until completion\n"
            + "  set volume output volume (oldVolume)\n"
            + "end run\n";
    FileWriter fw = null;
    try {
      scriptFile = File.createTempFile("speech", ".scpt");
      scriptFile.deleteOnExit();
      fw = new java.io.FileWriter(scriptFile);
      fw.write(speakScript);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
 
  public void speakAndWait(String call) {
    process = speak(call);
    if (process != null) {
      try {
        process.waitFor();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }
 
  protected Process speak(String call) {
    if (voices.length==0) return null;
    process = null;
    try {
      process = Runtime.getRuntime().exec(new String[]{"osascript",
                scriptFile.getAbsolutePath(),
                "\"" + call + "\"",
                "\"" + voices[voiceIndex] + "\"",
                "" + volume,
                "" + rate
              });
      new ProcessReader(process, false); // consume the output stream
      new ProcessReader(process, true); // consume the error stream
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return process;
  }
}
 
class ProcessReader {
 
  public ProcessReader(Process process, final boolean errorStream) {
    final InputStream processStream = errorStream
            ? process.getErrorStream()
            : process.getInputStream();
    new Thread() {
 
      @Override
      public void run() {
        try {
          BufferedReader br = new BufferedReader(new InputStreamReader(processStream));
          String line;
          while ((line = br.readLine()) != null) {
            if (errorStream) {
              System.err.println(line);
            } else {
              System.out.println(line);
            }
          }
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }.start();
  }
}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import vs.time.kkv.connector.Utils.TTS.SpeekUtil;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import marytts.util.io.FileUtils;
import vs.time.kkv.connector.MainForm;

/**
 *
 * @author kyo
 */
public class Beep {

  Map<String, PreLoadSound> sounds = new HashMap<String, PreLoadSound>();

  //SpeekUtil speaker = null;
  MainForm mainForm;

  public final static String SOUND_ID_START = "START";
  public final static String SOUND_ID_FINISH = "FINISH";

  public Beep(MainForm mainForm) {
    this.mainForm = mainForm;

    sounds.put("notify", new PreLoadSound("notify.wav"));  //0
    sounds.put("beep", new PreLoadSound("beep.wav"));
    sounds.put("one", new PreLoadSound("_1.wav"));
    sounds.put("two", new PreLoadSound("_2.wav"));
    sounds.put("three", new PreLoadSound("_3.wav"));
    sounds.put("attention", new PreLoadSound("_attention.wav")); // 5
    sounds.put(SOUND_ID_START, new PreLoadSound("start.wav"));          // 6
    sounds.put(SOUND_ID_FINISH, new PreLoadSound("finish.wav"));        // 7
  }

  public void paly(String sound_id) {
    PreLoadSound sound = sounds.get(sound_id);
    if (sound == null || !sound.isLoaded) {
      if (sound_id.equalsIgnoreCase("beep")) {
        Beep.beep();
      } else {
        mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().startMessage(sound_id));
      }
    } else {      
      try{
        sound.stop();
        sound.interrupt();
      }catch(Exception e){}  
      sound.setVolume(1f);
      sound.play();
      //sound.start();
    }
  }

  public void palyAndWait(String sound_id) {
    PreLoadSound sound = sounds.get(sound_id);
    if (sound == null || !sound.isLoaded) {
      if (sound_id.equalsIgnoreCase("beep")) {
        Beep.beep();
      } else {
        mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().startMessage(sound_id));
      }
    } else {
      sound.playAndWait();
    }
  }

  public void palyInThread(String sound_id) {
    PreLoadSound sound = sounds.get(sound_id);
    if (sound == null || !sound.isLoaded) {
      if (sound_id.equalsIgnoreCase("beep")) {
        Beep.beep();
      } else {
        mainForm.speaker.speak(mainForm.speaker.getSpeachMessages().startMessage(sound_id));
      }
    } else {
      sound.playInThread();
    }
  }

  public class PreLoadSound extends Thread {

    public String file = "";
    public Clip clip = null;
    public boolean isLoaded = false;

    public PreLoadSound(String file) {
      try {
        String dir = new File("sounds").getAbsolutePath();
        File tadaSound = new File(dir + File.separator + file);
        if (FileUtils.exists(tadaSound.getAbsolutePath())) {

          FileInputStream fis = new FileInputStream(tadaSound);

          InputStream bufferedIn = new BufferedInputStream(fis);

          AudioInputStream audioInputStream = AudioSystem
                  .getAudioInputStream(bufferedIn);
          AudioFormat audioFormat = audioInputStream
                  .getFormat();
          DataLine.Info dataLineInfo = new DataLine.Info(
                  Clip.class, audioFormat);
          clip = (Clip) AudioSystem
                  .getLine(dataLineInfo);
          clip.open(audioInputStream);

          //clip.start();
          isLoaded = true;
        }
      } catch (Exception e) {
        e.printStackTrace();
        Beep.this.mainForm.toLog("reading file is error:" + file);
      }

      try {
        //Получаем контроллер громкости
        FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //Устанавливаем значение
        //Оно должно быть в пределах от vc.getMinimum() до vc.getMaximum()
        vc.setValue(5); //Громче обычного
      } catch (Exception e) {
      }
    }

    public void setVolume(float volume) {
      // if (volume < 0f || volume > 1f)
      //   throw new IllegalArgumentException("Volume not valid: " + volume);
      try {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
      } catch (Exception e) {
      }
    }

    public void play() {
      setVolume(1f);
      clip.setMicrosecondPosition(0);
      if (clip != null) {
        clip.start();
      }
    }

    public void playAndWait() {
      clip.setMicrosecondPosition(0);
      if (clip != null) {
        clip.start();
      }
      while (clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {
      }
    }

    public void playInThread() {
      this.start();
    }

    @Override
    public void run() {
      clip.setMicrosecondPosition(0);
      if (clip != null) {
        clip.start();
      }
    }
  }

  public static void beep(int notePitch, long duration, int velocity, int numInstr, int numChannel) {
    Synthesizer mySynthesizer = null;
    try {
      mySynthesizer = MidiSystem.getSynthesizer();
      if (mySynthesizer == null) {
        System.out.println("getSynthesizer() failed!");
        return;
      }
      mySynthesizer.open();
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
    Instrument[] instruments = mySynthesizer.getAvailableInstruments();
    if (numInstr < 0) {
      numInstr = 0;
    } else if (numInstr >= instruments.length) {
      numInstr = instruments.length - 1;
    }
    System.out.println(numInstr + ":" + instruments.length);
    Instrument myInstrument = instruments[numInstr];
    mySynthesizer.loadInstrument(myInstrument);
    MidiChannel[] channels = mySynthesizer.getChannels();
    if (numChannel < 0) {
      numChannel = 0;
    } else if (numChannel >= channels.length) {
      numChannel = channels.length - 1;
    }
    MidiChannel channel = channels[numChannel];
    int prog = myInstrument.getPatch().getProgram();
    channel.programChange(prog);
    channel.noteOn(notePitch, velocity);
    try {
      Thread.currentThread().sleep(duration);
    } catch (InterruptedException ie) {
    }
    channel.allNotesOff();
    if (mySynthesizer.isOpen()) {
      mySynthesizer.close();
    }
    mySynthesizer = null;
  }

  public static void beep(double frequency, int duration) throws Exception {
    int nChannel = 1; // number of channel : 1 or 2       
    // samples per second     
    float sampleRate = 16000;  // valid:8000,11025,16000,22050,44100     
    int nBit = 16;             // 8 bit or 16 bit sample       
    int bytesPerSample = nChannel * nBit / 8;
    double durationInSecond = duration / 1000;
    int bufferSize = (int) (nChannel * sampleRate * durationInSecond * bytesPerSample);
    byte[] audioData = new byte[bufferSize];       // "type cast" to ShortBuffer    
    java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(audioData);
    java.nio.ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
    int sampleLength = audioData.length / bytesPerSample;       // generate the sine wave     
    double volume = 8192;   // 0-32767    
    double PI = Math.PI;
    for (int i = 0; i < sampleLength; i++) {
      double time = i / sampleRate;
      double freq = frequency;
      double angle = 2 * PI * freq * time;
      double sinValue = Math.sin(angle);
      short amplitude = (short) (volume * sinValue);
      for (int c = 0; c < nChannel; c++) {
        shortBuffer.put(amplitude);
      }
    }//end generating sound wave sample         
    boolean isSigned = true;
    boolean isBigEndian = true;       // Define audio format     
    javax.sound.sampled.AudioFormat audioFormat
            = new javax.sound.sampled.AudioFormat(sampleRate, nBit, nChannel, isSigned, isBigEndian);
    javax.sound.sampled.DataLine.Info dataLineInfo
            = new javax.sound.sampled.DataLine.Info(javax.sound.sampled.SourceDataLine.class, audioFormat);       // get the SourceDataLine object     
    javax.sound.sampled.SourceDataLine sourceDataLine = (javax.sound.sampled.SourceDataLine) javax.sound.sampled.AudioSystem.getLine(dataLineInfo);
    sourceDataLine.open(audioFormat);
    sourceDataLine.start();       // actually play the sound     
    sourceDataLine.write(audioData, 0, audioData.length);       // "flush",  wait until the sound is completed    
    sourceDataLine.drain();
  }

  public static void beep() {
    try {
      //int frequency = 300; // hz     
      //int duration = 1200;  // milliseconds    

      int frequency = 300; // hz     
      int duration = 1200;  // milliseconds    

      beep(frequency, duration);

       //beep(notePitch duration velocity numInstr numChannel)
       //beep (5,1000,100,i,128);
       
    } catch (Exception e) {
      e.printStackTrace();
    }
    //Toolkit.getDefaultToolkit().beep();

    //System.out.print("\007");
    // System.out.flush();
  }

  public static void main(String[] args) throws Exception {
    beep();
    //playStartupSound();
    Thread.sleep(3000);
  }

  public static void playStartupSound() {
    Runnable soundPlayer = new Runnable() {
      public void run() {
        try {
          // use one of the WAV of Windows installation
          File tadaSound = new File(System.getenv("windir") + "/"
                  + "media/tada.wav");
          AudioInputStream audioInputStream = AudioSystem
                  .getAudioInputStream(new FileInputStream(tadaSound));
          AudioFormat audioFormat = audioInputStream
                  .getFormat();
          DataLine.Info dataLineInfo = new DataLine.Info(
                  Clip.class, audioFormat);
          Clip clip = (Clip) AudioSystem
                  .getLine(dataLineInfo);
          clip.open(audioInputStream);
          clip.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    Thread soundPlayingThread = new Thread(soundPlayer);
    soundPlayingThread.start();
  }

}

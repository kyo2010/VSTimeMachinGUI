/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

/**
 *
 * @author kyo
 */
public class Beep {

  public static void beep(double frequency, int duration) throws Exception  {
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

  public static void beep() throws Exception  {
    int frequency = 500; // hz     
    int duration = 3000+(int)(Math.random()*3000);  // milliseconds    
    beep(frequency, duration);
  }
  
  public static void main(String[] args) throws Exception {
    beep();
  }
}

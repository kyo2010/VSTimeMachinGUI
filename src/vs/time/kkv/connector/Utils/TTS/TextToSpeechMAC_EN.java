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
public class TextToSpeechMAC_EN extends TextToSpeechMAC {

  public String getTTSName() {
    return "Mac OS TTS [EN]";
  } 
  
  public boolean useTranslit() {
    return true;
  }

  public ISpeachMessages returnSpeachMessages() {
    return ISpeachMessages.EN;
  }
  
}

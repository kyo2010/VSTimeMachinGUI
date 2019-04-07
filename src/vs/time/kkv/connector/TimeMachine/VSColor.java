/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author kyo
 */
public class VSColor {

  final static byte BYTE_1 = 1;
  final static byte BYTE_0 = 0;

  public static VSColor RED = new VSColor("RED",BYTE_0, BYTE_0, BYTE_1, Color.RED);
  public static VSColor OFF = new VSColor("BLACK",BYTE_0, BYTE_0, BYTE_0, Color.BLACK);
  public static VSColor GREEN = new VSColor("GREEN",BYTE_0, BYTE_1, BYTE_0, Color.GREEN);
  public static VSColor BLUE = new VSColor("BLUE",BYTE_1, BYTE_0, BYTE_0, Color.BLUE);
  public static VSColor WHITE = new VSColor("WHITE",BYTE_1, BYTE_1, BYTE_1, Color.WHITE);
  public static VSColor YELLOW = new VSColor("YELLOW",BYTE_0, BYTE_1, BYTE_1, Color.YELLOW);
  public static VSColor AQUA = new VSColor("AQUA",BYTE_1, BYTE_1, BYTE_0, new Color(140, 255, 255));
  
  public static VSColor[] CHANNEL_COLORS_FIXED = new VSColor[]{
    new VSColor("RED",BYTE_0, BYTE_0, BYTE_1, new Color(255, 140, 140)).setW3css("w3-red"), // 1 channel
    new VSColor("GREEN",BYTE_0, BYTE_1, BYTE_0, new Color(140, 255, 140)).setW3css("w3-green"), // 2 channel
    new VSColor("YELLOW",BYTE_0, BYTE_1, BYTE_1, new Color(255, 255, 140)).setW3css("w3-yellow"), // 3 channel
    new VSColor("BLUE",BYTE_1, BYTE_0, BYTE_0, new Color(140, 140, 255)).setW3css("w3-blue"), // 4 channel
    new VSColor("PURPURE",BYTE_1, BYTE_0, BYTE_1, new Color(255, 140, 255)).setW3css("w3-pink"), // 5 channel
    new VSColor("AQUA",BYTE_1, BYTE_1, BYTE_0, new Color(140, 255, 255)), // 6 channel
    new VSColor("WHITE",BYTE_1, BYTE_1, BYTE_1, new Color(240, 240, 255)), // 7 channel
    new VSColor("BLACK",BYTE_0, BYTE_0, BYTE_0, new Color(00, 00, 00)), // 7 channel
    //new VSColor("WHITE",BYTE_1, BYTE_1, BYTE_1, Color.WHITE), // 8 channel
  }; 
  
  public static HashMap<String, VSColor> COLORS_MAP = new HashMap<String,VSColor> ();
  
  static{
    for (VSColor color : CHANNEL_COLORS_FIXED){
      COLORS_MAP.put(color.colorname, color);
    }
  }
  
  public static VSColor getColor(String colorNAme){
    for (VSColor color : CHANNEL_COLORS_FIXED){
      if (color.colorname.equalsIgnoreCase(colorNAme)) return color;     
    }
    return CHANNEL_COLORS_FIXED[0];
  }
  
   
  public static String[] getColors(){
    String[] result = new String[CHANNEL_COLORS_FIXED.length];    
    int index = 0;
    for (VSColor color : CHANNEL_COLORS_FIXED){
      result[index] = color.colorname;
      index++;
    }
    return result;
  }

  public Color color;
  public int vscolor = 0;
  public String colorname = "";
  public String w3css = "";
  
  public VSColor setW3css(String w3css) {
    this.w3css = w3css;
    return this;
  }
  
  public String getW3css() {
    return w3css;
  }
  
  public String getHTMLColorString(){
    return  getHTMLColorString(this.color);
  }

  public static String getHTMLColorString(Color color) {
    String red = Integer.toHexString(color.getRed());
    String green = Integer.toHexString(color.getGreen());
    String blue = Integer.toHexString(color.getBlue());

    return "#"
            + (red.length() == 1 ? "0" + red : red)
            + (green.length() == 1 ? "0" + green : green)
            + (blue.length() == 1 ? "0" + blue : blue);
  }
  
  public static int blinkColor(int vscolor){
    vscolor |= (1 << 3); // blink
    vscolor |= (1 << 4); // fast blink
    return vscolor;
  }
  
  // LockChangeColor = gateColor & (1 << 4);
  // isSendGate = gateColor & (1 << 5);
  // mainGateColor = gateColor & (1 << 6); 
  // lockColor = gateColor & (1 << 7);
  
  /*
  lockChangeColor: это зафиксировать цвет тоесть пролетающие дроны не смогут его менять.
  isSendGate: переключить в режим обмена с базовой станцией
  mainGateColor: Установить этот цвет как основной
  lockColor: Блокирует смену цвета в цвет по умолчанию(через 1 сек после пролета)
  */

  /***
    6 bit - blinking in the gate
    5 bit - light brightnes : 1 - max, 0 - normal
    4 bit - speed blinking : 1 - fast, 0 - low
    3 bit - blinking, 0 - off, 1 - blink
   */
  
  public VSColor(String colorname, byte blue, byte green, byte red, Color color) {
    /*vscolor = (blue & 0xFF) | ((green & 0xFF) << 1) | ((red & 0xFF) << 2) | 
              ((1 & 0xFF) << 6); // blinking*/
    this.colorname = colorname;
    vscolor = 0;
    if (blue == 1) {
      vscolor |= 1;
    }
    if (green == 1) {
      vscolor |= (1 << 1);
    }
    if (red == 1) {
      vscolor |= (1 << 2);
    }
    vscolor |= (1 << 5);
    vscolor |= (1 << 6);
    this.color = color;
  }
  
  public int getOnlyColor(){
    int c = vscolor;
    c &= ~(1<<4);
    c &= ~(1<<5);
    c &= ~(1<<6);
    c &= ~(1<<7);
    return c;
  }

  public int getVSColor() {
    return vscolor;
  }

  public Color getColor() {
    return color;
  }

   /** R5  ;   R1;R5;R8   RED;GREEN;YELLOW => R5  */
  public static VSColor getColorForChannel(String channel, String channels, String colors) {
    try {
      String[] cos = colors.split(";");
      String[] chs = channels.split(";");
      int color_index = 0;
      for (String ch : chs){
        if (ch.equalsIgnoreCase(channel)){
          String color = cos[color_index];
          if (COLORS_MAP.get(color)!=null) return COLORS_MAP.get(color);
        }
        color_index++;
      }
    } catch (Exception ein) {
    }
    return RED;
  }

  public static void main(String[] args) {
    VSColor color = getColorForChannel("R8", "BLUE;GREEN;WHITE", "R8;R8;R8");
    System.out.println("color:"+color.colorname+" code:"+color.vscolor);    
  }
}

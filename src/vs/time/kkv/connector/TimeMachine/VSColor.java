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
  
  public static VSColor[] CHANNEL_COLORS_FIXED = new VSColor[]{
    new VSColor("RED",BYTE_0, BYTE_0, BYTE_1, Color.RED), // 1 channel
    new VSColor("GREEN",BYTE_0, BYTE_1, BYTE_0, Color.GREEN), // 2 channel
    new VSColor("C1",BYTE_0, BYTE_1, BYTE_1, new Color(255, 255, 0)), // 3 channel
    new VSColor("BLUE",BYTE_1, BYTE_0, BYTE_0, Color.BLUE), // 4 channel
    new VSColor("C2",BYTE_1, BYTE_0, BYTE_1, new Color(255, 0, 255)), // 5 channel
    new VSColor("C3",BYTE_1, BYTE_1, BYTE_0, new Color(0, 255, 255)), // 6 channel
    new VSColor("WHITE",BYTE_1, BYTE_1, BYTE_1, Color.WHITE), // 7 channel
    //new VSColor("WHITE",BYTE_1, BYTE_1, BYTE_1, Color.WHITE), // 8 channel
  }; 
  
  public static HashMap<String, VSColor> COLORS_MAP = new HashMap<String,VSColor> ();
  
  static{
    for (VSColor color : CHANNEL_COLORS_FIXED){
      COLORS_MAP.put(color.colorname, color);
    }
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

  public static String getHTMLColorString(Color color) {
    String red = Integer.toHexString(color.getRed());
    String green = Integer.toHexString(color.getGreen());
    String blue = Integer.toHexString(color.getBlue());

    return "#"
            + (red.length() == 1 ? "0" + red : red)
            + (green.length() == 1 ? "0" + green : green)
            + (blue.length() == 1 ? "0" + blue : blue);
  }

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
    vscolor |= (1 << 6);
    this.color = color;
  }

  public int getVSColor() {
    return vscolor;
  }

  public Color getColor() {
    return color;
  }

   /** R5  ;   R1;R5;R8   RED;GREEN;YELLOW => R5  */
  public static VSColor getColorForChannel(String channel, String colors, String channels) {
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

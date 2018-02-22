/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.TimeMachine;

import java.awt.Color;

/**
 *
 * @author kyo
 */
public class VSColor {

  final static byte BYTE_1 = 1;
  final static byte BYTE_0 = 0;

  public static VSColor[] CHANNEL_COLORS = new VSColor[]{
    new VSColor(BYTE_0, BYTE_0, BYTE_1, Color.RED), // 1 channel
    new VSColor(BYTE_0, BYTE_1, BYTE_0, Color.GREEN), // 2 channel
    new VSColor(BYTE_0, BYTE_1, BYTE_1, new Color(255, 255, 0)), // 3 channel
    new VSColor(BYTE_1, BYTE_0, BYTE_0, Color.BLUE), // 4 channel
    new VSColor(BYTE_1, BYTE_0, BYTE_1, new Color(255, 0, 255)), // 5 channel
    new VSColor(BYTE_1, BYTE_1, BYTE_0, new Color(0, 255, 255)), // 6 channel
    new VSColor(BYTE_1, BYTE_1, BYTE_1, Color.WHITE), // 7 channel
    new VSColor(BYTE_1, BYTE_1, BYTE_1, Color.WHITE), // 8 channel
  };

  public Color color;
  public int vscolor = 0;

  public VSColor(byte blue, byte green, byte red, Color color) {
    /*vscolor = (blue & 0xFF) | ((green & 0xFF) << 1) | ((red & 0xFF) << 2) | 
              ((1 & 0xFF) << 6); // blinking*/
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

  public static VSColor getColorForChannel(String channel) {
    try {
      int color_index = Integer.parseInt(channel.substring(1)) - 1;
      if (color_index < VSColor.CHANNEL_COLORS.length) {
        return VSColor.CHANNEL_COLORS[color_index];
      }
    } catch (Exception ein) {
    }
    return null;
  }

  public static void main(String[] args) {
    for (VSColor c : CHANNEL_COLORS) {
      System.out.println(c.vscolor);
    }
  }
}

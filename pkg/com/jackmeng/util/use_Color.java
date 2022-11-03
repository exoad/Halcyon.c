package com.jackmeng.util;

import com.jackmeng.halcyon.use_HalcyonProperties;

import java.awt.*;

public final class use_Color {
  private use_Color() {
  }

  /**
   * @param hex
   * @return Color
   */
  /*---------------------------- /
  / color theory magic bs stuffs /
  /-----------------------------*/

  public static Color hexToRGB(String hex) {
    if (!hex.startsWith("#")) {
      /*--------------------------------- /
      / this part is so hardcoded and bad /
      /----------------------------------*/
      hex = "#" + hex;
    }
    return new Color(
        Integer.valueOf(hex.substring(1, 3), 16),
        Integer.valueOf(hex.substring(3, 5), 16),
        Integer.valueOf(hex.substring(5, 7), 16));
  }

  /**
   * @return Color
   */
  public static Color rndColor() {
    return new Color(use_HalcyonProperties.rng.nextInt(255), use_HalcyonProperties.rng.nextInt(255),
        use_HalcyonProperties.rng.nextInt(255));
  }

  /**
   * @return Color
   */
  public static Color nullColor() {
    return new Color(0, 0, 0, 0);
  }

  public static String rgbToHex(int[] rgb) {
    return "#" + Integer.toHexString(rgb[0]) + Integer.toHexString(rgb[1]) + Integer.toHexString(rgb[2]);
  }

  public static boolean is_gray(int[] rgba, int tolerance) {
    return (rgba[1] - rgba[2] > tolerance || rgba[1]
        - rgba[3] < -tolerance)
        && (rgba[1]
            - rgba[3] > tolerance || rgba[1] - rgba[2] < -tolerance) ? false : true;
  }

  public static int[] parse_RGB(int rgb) {
    return new int[] { (rgb >> 24) & 0xFF, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
    /*---------------------------------- /
    / returns in alpha, red, green, blue /
    /-----------------------------------*/
  }
}

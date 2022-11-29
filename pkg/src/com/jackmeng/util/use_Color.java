package com.jackmeng.util;

import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.util.use_Struct.struct_Trio;

import java.awt.*;

public final class use_Color
{
  private use_Color()
  {
  }

  /**
   * @param hex
   * @return Color
   */
  /*---------------------------- /
  / color theory magic bs stuffs /
  /-----------------------------*/

  public static Color hexToRGB(String hex)
  {
    if (!hex.startsWith("#"))
    {
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

  public static Color make(struct_Trio< Integer, Integer, Integer > p)
  {
    return new Color(p.first, p.second, p.first);
  }

  public static Color alpha_set(Color r, int alpha)
  {
    return new Color(r.getRed(), r.getGreen(), r.getBlue(), alpha);
  }

  public static Color darker(Color clr, double factor)
  {
    return new Color(Math.max((int) (clr.getRed() * factor), 0),
        Math.max((int) (clr.getGreen() * factor), 0),
        Math.max((int) (clr.getBlue() * factor), 0),
        clr.getAlpha());
  }

  public static Color lighter(Color clr, double factor)
  {
    int r = clr.getRed(), g = clr.getGreen(), b = clr.getBlue(), a = clr.getAlpha();
    int i = (int) (1.0D / (1.0D - factor));
    if (r == 0 && g == 0 && b == 0)
      return new Color(i, i, i, a);
    if (r > 0 && r < i)
      r = i;
    if (g > 0 && g < i)
      g = i;
    if (b > 0 && b < i)
      b = i;
    return new Color(Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255),
        Math.min((int) (b / factor), 255), a);
  }

  /**
   * @return Color
   */
  public static Color rndColor()
  {
    return new Color(use_Halcyon.rng.nextInt(255), use_Halcyon.rng.nextInt(255),
        use_Halcyon.rng.nextInt(255));
  }

  /**
   * @return Color
   */
  public static Color nullColor()
  {
    return new Color(0, 0, 0, 0);
  }

  public static String rgbToHex(int[] rgb)
  {
    return "#" + Integer.toHexString(rgb[0]) + Integer.toHexString(rgb[1]) + Integer.toHexString(rgb[2]);
  }

  public static String colorToHex(Color r)
  {
    return String.format("#%02x%02x%02x", r.getRed(), r.getGreen(), r.getBlue());
  }

  public static boolean is_gray(int[] rgba, int tolerance)
  {
    return (rgba[1] - rgba[2] > tolerance || rgba[1]
        - rgba[3] < -tolerance)
        && (rgba[1]
            - rgba[3] > tolerance || rgba[1] - rgba[2] < -tolerance) ? false : true;
  }

  public static int[] parse_RGB(int rgb)
  {
    return new int[] { (rgb >> 24) & 0xFF, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
    /*---------------------------------- /
    / returns in alpha, red, green, blue /
    /-----------------------------------*/
  }
}

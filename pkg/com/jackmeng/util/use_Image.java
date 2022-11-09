package com.jackmeng.util;

import javax.swing.*;

import com.jackmeng.util.use_Struct.struct_Trio;

import java.awt.*;
import java.awt.image.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public final class use_Image
{
  private use_Image()
  {
  }

  public static struct_Trio< Integer, Integer, Integer > accurate_accent_color_1(BufferedImage img)
  {
    Map< Integer, Integer > m = new HashMap<>();
    for (int i = 0; i < img.getWidth(); i++)
    {
      for (int j = 0; j < img.getHeight(); j++)
      {
        int rgb = img.getRGB(i, j);
        int[] arr = use_Color.parse_RGB(rgb);
        if (!use_Color.is_gray(arr, 10))
        {
          Integer color = m.get(rgb);
          if (color == null)
          {
            color = 0;
          }
          color++;
          m.put(rgb, color);
        }
      }
    }
    java.util.List< Entry< Integer, Integer > > list = new LinkedList<>(m.entrySet());
    Collections.sort(list, (x, y) -> {
      return ((Comparable< Integer >) x.getValue())
          .compareTo(y.getValue());
    });
    Map.Entry< Integer, Integer > cum = list.get(list.size() - 1);
    return new struct_Trio<>(use_Color.parse_RGB(cum.getKey())[1], use_Color.parse_RGB(cum.getKey())[2],
        use_Color.parse_RGB(cum.getKey())[3]);
  }

  public static int[] pixels(BufferedImage img, int x, int y, int width, int height, int[] pixels)
  {
    if (width == 0 || height == 0)
      return new int[0];
    if (pixels == null)
    {
      pixels = new int[width * height];
    }
    assert pixels.length >= width * height;
    if (img.getType() == BufferedImage.TYPE_INT_ARGB || img.getType() == BufferedImage.TYPE_INT_RGB)
    {
      Raster rst = img.getRaster();
      return (int[]) rst.getDataElements(x, y, width, height, pixels);
    }
    return img.getRGB(x, y, width, height, pixels, 0, width);
  }

  /**
   * @param n_width
   * @param h_height
   * @param i
   * @return ImageIcon
   */
  public static ImageIcon resize_fast_1(int n_width, int h_height, ImageIcon i)
  {
    i.setImage(i.getImage().getScaledInstance(n_width, h_height, Image.SCALE_AREA_AVERAGING));
    return i;
  }

  public static ImageIcon resize_2(int w, int h, ImageIcon i)
  {
    i.setImage(i.getImage().getScaledInstance(w, h, Image.SCALE_REPLICATE));
    return i;
  }
}

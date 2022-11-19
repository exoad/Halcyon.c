package com.jackmeng.util;

import javax.swing.*;

import com.jackmeng.sys.use_Program;
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

  public static BufferedImage image_to_bi(Image i, int w, int h)
  {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = img.getGraphics();
    g.drawImage(i, 0, 0, null);
    g.dispose();
    return img;
  }

  public static BufferedImage compat_img(BufferedImage img)
  {
    BufferedImage dst = use_Program.graphics_conf().createCompatibleImage(img.getWidth(), img.getHeight(),
        img.getTransparency());
    Graphics2D g2 = dst.createGraphics();
    g2.drawImage(img, 0, 0, null);
    g2.dispose();
    return dst;
  }

  public static BufferedImage mask(BufferedImage img, BufferedImage mask, int method)
  {
    BufferedImage target = null;
    if (img != null)
    {
      int w = mask.getWidth();
      int h = mask.getHeight();
      target = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = target.createGraphics();
      int x = (w - img.getWidth()) / 2;
      int y = (h - img.getHeight()) / 2;
      g2.drawImage(img, x, y, null);
      g2.setComposite(AlphaComposite.getInstance(method));
      g2.drawImage(mask, 0, 0, null);
      g2.dispose();
    }
    return target;
  }

  public static struct_Trio< Integer, Integer, Integer > accurate_accent_color_1(BufferedImage img)
  {
    if (img.getWidth() > 512 || img.getHeight() > 512)
      img = image_to_bi(img.getScaledInstance(512, 512, Image.SCALE_FAST), 512, 512);
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
            color = 0;
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

  public static void pixels(BufferedImage img, int x, int y, int width, int height, int[] pixels)
  {
    if (width == 0 || height == 0 || pixels == null)
      return;
    assert pixels.length >= width * height;
    if (img.getType() == BufferedImage.TYPE_INT_ARGB || img.getType() == BufferedImage.TYPE_INT_RGB)
    {
      WritableRaster rst = img.getRaster();
      rst.getDataElements(x, y, width, height, pixels);
    }
    else
      img.getRGB(x, y, width, height, pixels, 0, width);
  }

  public static BufferedImage compat_Img(BufferedImage e)
  {
    BufferedImage r = compat_Img(e.getWidth(), e.getHeight(), e);
    Graphics2D g2 = r.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
    g2.drawImage(e, 0, 0, null);
    g2.dispose();
    return r;
  }

  public static GraphicsConfiguration g_conf()
  {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  }

  public static BufferedImage compat_Img(int w, int h, BufferedImage r)
  {
    return g_conf().createCompatibleImage(w, h, r.getTransparency());
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

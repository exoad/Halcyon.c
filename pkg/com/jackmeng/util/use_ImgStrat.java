package com.jackmeng.util;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.util.Collections;

public final class use_ImgStrat
{
  private use_ImgStrat()
  {
  }

  public static class imgstrat_BlurhashBlur
      implements BufferedImageOp
  {
    private int x, y;
    private double punch;

    public imgstrat_BlurhashBlur(int xRatio, int yRatio, double strength)
    {
      this.x = xRatio;
      this.y = yRatio;
      this.punch = strength < 0 ? 1.2D : strength;
      /*------------------------------------------------------------------------ /
      / 1.2D is like the standard punch value but may vary for different systems /
      /-------------------------------------------------------------------------*/
    }

    public int x_ratio()
    {
      return x;
    }

    public double strength()
    {
      return punch;
    }

    public void set_strength(double e)
    {
      this.punch = e;
    }

    public int y_ratio()
    {
      return y;
    }

    public void set_y_ratio(int y)
    {
      this.y = y;
    }

    public void set_x_ratio(int x)
    {
      this.x = x;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
      {
        cumCM = srcum.getColorModel();
      }
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override
    public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

    @Override
    public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();
      if (cum == null)
      {
        cum = createCompatibleDestImage(srcum, null);
      }

      cum.setRGB(0, 0, width, height,
          use_Blurhash.dec(
              use_Blurhash.enc(srcum.getRGB(0, 0, width, height, null, 0, width), width, height, x_ratio(), y_ratio()),
              width, height, punch),
          0, width);
      return cum;
    }

  }

  public static class imgstrat_TransposedGaussianBlur
      implements BufferedImageOp
  {
    private float[] matrix;

    /*------------------------------------------------------------------- /
    / you can call the tranpose twice to untranspose it with effects kept /
    /--------------------------------------------------------------------*/

    public imgstrat_TransposedGaussianBlur(float[] matrix)
    {
      /*-------------------------------------------------------------- /
      / the matrix determines the amount of artifacts kept and removed /
      /---------------------------------------------------------------*/

      this.matrix = matrix;
    }

    public imgstrat_TransposedGaussianBlur()
    {
      this(new float[] { 0.00599F, 0.060628F, 0.241844F, 0.383113F, 0.241823F, 0.060636F, 0.00528F });
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
      {
        cumCM = srcum.getColorModel();
      }
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override
    public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

    @Override
    public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();

      if (cum == null)
      {
        cum = createCompatibleDestImage(srcum, null);
      }

      for (int i = 0; i < height; i++)
      {
        for (int j = 3; j < width - 3; j++)
        {
          float r = 0F, g = 0F, b = 0F;
          for (int d = 0; d < matrix.length; d++)
          {
            int px = srcum.getRGB(j + d - 3, i);
            b += (px & 0xFF) * matrix[d];
            g += ((px >> 8) & 0xFF) * matrix[d];
            r += ((px >> 16) & 0xFF) * matrix[d];
          }
          int rgb = (int) b + ((int) g << 8) + ((int) r << 16);
          cum.setRGB(i, j, rgb);
        }
      }
      return cum;
    }
  }

  public static class imgstrat_StackBlur implements BufferedImageOp
  {
    private int radius, iterations;

    public imgstrat_StackBlur(int radius, int itr)
    {
      this.radius = radius;
      this.iterations = itr;
    }

    @Override
    public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();

      if (cum == null)
      {
        cum = createCompatibleDestImage(srcum, null);
      }

      int[] srcum_px = new int[width * height];
      int[] cum_px = new int[width * height];

      use_Image.pixels(srcum, 0, 0, width, height, srcum_px);
      for (int i = 0; i < iterations; i++)
      {
        imgstrat_FastBlur.blur(srcum_px, cum_px, width, height, radius);
        imgstrat_FastBlur.blur(cum_px, srcum_px, height, width, radius);
      }
      use_Image.pixels(cum, 0, 0, width, height, srcum_px);
      return cum;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
      {
        cumCM = srcum.getColorModel();
      }
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override
    public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }
  }

  public static class imgstrat_FastBlur
      implements BufferedImageOp
  {
    private int radius;

    public imgstrat_FastBlur(int radius)
    {
      assert radius > 0;
      this.radius = radius;
    }

    public static void blur(int[] srcum, int[] cum, int width, int height, int radius)
    {
      final int windowSize = radius * 2 + 1;
      final int r_1 = radius + 1;

      int alpha_, red_, green_, bleu_, srcumIndex = 0, dstIndex, px_1;

      int[] sumLookupTable = new int[256 * windowSize];
      for (int i = 0; i < sumLookupTable.length; i++)
      {
        sumLookupTable[i] = i / windowSize;
      }

      int[] indexLookupTable = new int[r_1];
      if (radius < width)
      {
        for (int i = 0; i < indexLookupTable.length; i++)
        {
          indexLookupTable[i] = i;
        }
      }
      else
      {
        for (int i = 0; i < width; i++)
        {
          indexLookupTable[i] = i;
        }
        for (int i = width; i < indexLookupTable.length; i++)
        {
          indexLookupTable[i] = width - 1;
        }
      }

      for (int y = 0; y < height; y++)
      {
        alpha_ = red_ = green_ = bleu_ = 0;
        dstIndex = y;

        px_1 = srcum[srcumIndex];
        alpha_ += r_1 * ((px_1 >> 24) & 0xFF);
        red_ += r_1 * ((px_1 >> 16) & 0xFF);
        green_ += r_1 * ((px_1 >> 8) & 0xFF);
        bleu_ += r_1 * (px_1 & 0xFF);

        for (int i = 1; i <= radius; i++)
        {
          px_1 = srcum[srcumIndex + indexLookupTable[i]];
          alpha_ += (px_1 >> 24) & 0xFF;
          red_ += (px_1 >> 16) & 0xFF;
          green_ += (px_1 >> 8) & 0xFF;
          bleu_ += px_1 & 0xFF;
        }

        for (int x = 0; x < width; x++)
        {
          cum[dstIndex] = sumLookupTable[alpha_] << 24
              | sumLookupTable[red_] << 16
              | sumLookupTable[green_] << 8
              | sumLookupTable[bleu_];
          dstIndex += height;

          int nextPixelIndex = x + r_1;
          if (nextPixelIndex >= width)
          {
            nextPixelIndex = width - 1;
          }

          int previousPixelIndex = x - radius;
          if (previousPixelIndex < 0)
          {
            previousPixelIndex = 0;
          }

          int nextPixel = srcum[srcumIndex + nextPixelIndex];
          int previousPixel = srcum[srcumIndex + previousPixelIndex];

          alpha_ += (nextPixel >> 24) & 0xFF;
          alpha_ -= (previousPixel >> 24) & 0xFF;

          red_ += (nextPixel >> 16) & 0xFF;
          red_ -= (previousPixel >> 16) & 0xFF;

          green_ += (nextPixel >> 8) & 0xFF;
          green_ -= (previousPixel >> 8) & 0xFF;

          bleu_ += nextPixel & 0xFF;
          bleu_ -= previousPixel & 0xFF;
        }

        srcumIndex += width;
      }
    }

    @Override
    public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();
      if (cum == null)
      {
        cum = createCompatibleDestImage(srcum, null);
      }
      int[] srcum_px = new int[width * height];
      int[] cum_px = new int[width * height];

      use_Image.pixels(srcum, 0, 0, width, height, srcum_px);
      blur(srcum_px, cum_px, width, height, radius);
      blur(cum_px, srcum_px, height, width, radius);
      use_Image.pixels(cum, 0, 0, width, height, srcum_px);
      return cum;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
      {
        cumCM = srcum.getColorModel();
      }
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override
    public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

  }
}

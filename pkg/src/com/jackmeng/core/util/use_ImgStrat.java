package com.jackmeng.core.util;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

import java.awt.MultipleGradientPaint.CycleMethod;

/**
 * A set of BufferedImageOp for GUI or other general purpose
 * filtering and layering processings.
 *
 * @author Jack Meng
 */
public final class use_ImgStrat
{
  private use_ImgStrat()
  {
  }

  public static JLayer< Component > acquireOpLayer(BufferedImageOp e, Component over)
  {
    return new JLayer<>(over, new LayerUI<>() {

      @Override public void paint(Graphics g, JComponent comp)
      {
        if (comp.getWidth() == 0 || comp.getHeight() == 0)
          return;
        BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = img.createGraphics();
        ig2.setClip(g.getClip());
        super.paint(ig2, comp);
        ig2.dispose();
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, e, 0, 0);
        g2.dispose();
      }
    });
  }

  public static BufferedImageOp convolutionLayer(int w, int h, RenderingHints e)
  {
    assert w > 0 && h > 0;
    float[] matrix = new float[w * h];
    float d = 1.0F / (w * h);
    for (int i = 0; i < (w * h); i++)
      matrix[i] = d;
    return new ConvolveOp(new Kernel(w, h, matrix), ConvolveOp.EDGE_ZERO_FILL, e);
  }

  public static class imgstrat_4_CornerGradient
      implements
      BufferedImageOp
  {

    public double e;
    public int a, b;

    public imgstrat_4_CornerGradient(double strength, int startAlpha, int endAlpha)
    {
      this.e = strength;
      this.a = startAlpha;
      this.b = endAlpha;
    }

    @Override public BufferedImage filter(BufferedImage src, BufferedImage dest)
    {
      int w = src.getWidth();
      int h = src.getHeight();
      dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = dest.createGraphics();
      g.drawImage(src, 0, 0, null);
      g.setComposite(AlphaComposite.DstOut);

      Color c0 = new Color(0, 0, 0, a);
      Color c1 = new Color(0, 0, 0, b);

      double cy = e, cx = e;

      g.setPaint(new GradientPaint(
          new Point2D.Double(0, cy), c0,
          new Point2D.Double(cx, cy), c1));
      g.fill(new Rectangle2D.Double(
          0, cy, cx, h - cy - cy));

      g.setPaint(new GradientPaint(
          new Point2D.Double(w - cx, cy), c1,
          new Point2D.Double(w, cy), c0));
      g.fill(new Rectangle2D.Double(
          w - cx, cy, cx, h - cy - cy));

      g.setPaint(new GradientPaint(
          new Point2D.Double(cx, 0), c0,
          new Point2D.Double(cx, cy), c1));
      g.fill(new Rectangle2D.Double(
          cx, 0, w - cx - cx, cy));

      g.setPaint(new GradientPaint(
          new Point2D.Double(cx, h - cy), c1,
          new Point2D.Double(cx, h), c0));
      g.fill(new Rectangle2D.Double(
          cx, h - cy, w - cx - cx, cy));

      g.setPaint(new RadialGradientPaint(
          new Rectangle2D.Double(0, 0, cx + cx, cy + cy),
          new float[] { 0, 1 }, new Color[] { c1, c0 }, CycleMethod.NO_CYCLE));
      g.fill(new Rectangle2D.Double(0, 0, cx, cy));

      g.setPaint(new RadialGradientPaint(
          new Rectangle2D.Double(w - cx - cx, 0, cx + cx, cy + cy),
          new float[] { 0, 1 }, new Color[] { c1, c0 }, CycleMethod.NO_CYCLE));
      g.fill(new Rectangle2D.Double(w - cx, 0, cx, cy));

      g.setPaint(new RadialGradientPaint(
          new Rectangle2D.Double(0, h - cy - cy, cx + cx, cy + cy),
          new float[] { 0, 1 }, new Color[] { c1, c0 }, CycleMethod.NO_CYCLE));
      g.fill(new Rectangle2D.Double(0, h - cy, cx, cy));

      g.setPaint(new RadialGradientPaint(
          new Rectangle2D.Double(w - cx - cx, h - cy - cy, cx + cx, cy + cy),
          new float[] { 0, 1 }, new Color[] { c1, c0 }, CycleMethod.NO_CYCLE));
      g.fill(new Rectangle2D.Double(w - cx, h - cy, cx, cy));

      g.dispose();
      return dest;
    }

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

  }

  public static class imgstrat_Y_ImageGradient
      implements
      BufferedImageOp
  {

    public enum imagegradient_GradientType {
      BOTTOM, TOP // which side is the side to be gradiented (the one to have transparency reduced
                   // on)
    }

    public imagegradient_GradientType type;
    public int a, b;

    public imgstrat_Y_ImageGradient(imagegradient_GradientType type, int startAlpha, int endAlpha)
    {
      this.a = startAlpha;
      this.b = endAlpha;
      this.type = type;
    }

    @Override public BufferedImage filter(BufferedImage src, BufferedImage dest)
    {
      if (type == imagegradient_GradientType.TOP)
      {
        dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        Graphics2D g2 = dest.createGraphics();
        LinearGradientPaint lgp = new LinearGradientPaint(new Point(0, 0), new Point(0, src.getHeight()),
            new float[] { 0F, 1F }, new Color[] { new Color(0, 0, 0, a), new Color(0, 0, 0, b) });
        g2.setPaint(lgp);
        g2.fillRect(0, 0, src.getWidth(), src.getHeight());
        g2.dispose();
        return use_Image.mask(src, dest, AlphaComposite.DST_IN);
      }
      else if (type == imagegradient_GradientType.BOTTOM)
      {
        dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        Graphics2D g2 = dest.createGraphics();
        LinearGradientPaint lgp = new LinearGradientPaint(new Point(0, src.getHeight()), new Point(0, 0),
            new float[] { 0F, 1F }, new Color[] { new Color(0, 0, 0, a), new Color(0, 0, 0, b) });
        g2.setPaint(lgp);
        g2.fillRect(0, 0, src.getWidth(), src.getHeight());
        g2.dispose();
        return use_Image.mask(src, dest, AlphaComposite.DST_IN);
      }
      return src;
    }

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

  }

  public static class imgstrat_BlurhashBlur
      implements
      BufferedImageOp
  {
    private int x, y;
    private double punch;

    public imgstrat_BlurhashBlur(int xRatio, int yRatio, double strength)
    {
      this.x = xRatio;
      this.y = yRatio;
      this.punch = strength < 0 ? 1.0D : strength;
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

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

    @Override public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();
      if (cum == null)
        cum = createCompatibleDestImage(srcum, null);

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
    private final float[] matrix;

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

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

    @Override public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();

      if (cum == null)
        cum = createCompatibleDestImage(srcum, null);

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
    private final int radius;
    private final int iterations;

    public imgstrat_StackBlur(int radius, int itr)
    {
      this.radius = radius;
      this.iterations = itr;
    }

    @Override public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();

      if (cum == null)
        cum = createCompatibleDestImage(srcum, null);

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

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }
  }

  public static class imgstrat_FastBlur
      implements
      BufferedImageOp
  {
    private final int radius;

    public imgstrat_FastBlur(int radius)
    {
      assert radius > 0;
      this.radius = radius;
    }

    public static void blur(int[] srcPixels, int[] dstPixels,
        int width, int height, int radius)
    {
      final int windowSize = radius * 2 + 1;
      final int radiusPlusOne = radius + 1;

      int sumAlpha;
      int sumRed;
      int sumGreen;
      int sumBlue;

      int srcIndex = 0;
      int dstIndex;
      int pixel;

      int[] sumLookupTable = new int[256 * windowSize];
      for (int i = 0; i < sumLookupTable.length; i++)
      {
        sumLookupTable[i] = i / windowSize;
      }

      int[] indexLookupTable = new int[radiusPlusOne];
      if (radius < width)
        for (int i = 0; i < indexLookupTable.length; i++)
          indexLookupTable[i] = i;
      else
      {
        for (int i = 0; i < width; i++)
          indexLookupTable[i] = i;
        for (int i = width; i < indexLookupTable.length; i++)
          indexLookupTable[i] = width - 1;
      }

      for (int y = 0; y < height; y++)
      {
        sumAlpha = sumRed = sumGreen = sumBlue = 0;
        dstIndex = y;

        pixel = srcPixels[srcIndex];
        sumAlpha += radiusPlusOne * ((pixel >> 24) & 0xFF);
        sumRed += radiusPlusOne * ((pixel >> 16) & 0xFF);
        sumGreen += radiusPlusOne * ((pixel >> 8) & 0xFF);
        sumBlue += radiusPlusOne * (pixel & 0xFF);

        for (int i = 1; i <= radius; i++)
        {
          pixel = srcPixels[srcIndex + indexLookupTable[i]];
          sumAlpha += (pixel >> 24) & 0xFF;
          sumRed += (pixel >> 16) & 0xFF;
          sumGreen += (pixel >> 8) & 0xFF;
          sumBlue += pixel & 0xFF;
        }

        for (int x = 0; x < width; x++)
        {
          dstPixels[dstIndex] = sumLookupTable[sumAlpha] << 24
              | sumLookupTable[sumRed] << 16
              | sumLookupTable[sumGreen] << 8
              | sumLookupTable[sumBlue];
          dstIndex += height;

          int nextPixelIndex = x + radiusPlusOne;
          if (nextPixelIndex >= width)
            nextPixelIndex = width - 1;

          int previousPixelIndex = x - radius;
          if (previousPixelIndex < 0)
            previousPixelIndex = 0;

          int nextPixel = srcPixels[srcIndex + nextPixelIndex];
          int previousPixel = srcPixels[srcIndex + previousPixelIndex];

          sumAlpha += (nextPixel >> 24) & 0xFF;
          sumAlpha -= (previousPixel >> 24) & 0xFF;

          sumRed += (nextPixel >> 16) & 0xFF;
          sumRed -= (previousPixel >> 16) & 0xFF;

          sumGreen += (nextPixel >> 8) & 0xFF;
          sumGreen -= (previousPixel >> 8) & 0xFF;

          sumBlue += nextPixel & 0xFF;
          sumBlue -= previousPixel & 0xFF;
        }

        srcIndex += width;
      }
    }

    @Override public BufferedImage filter(BufferedImage srcum, BufferedImage cum)
    {
      int width = srcum.getWidth();
      int height = srcum.getHeight();
      if (cum == null)
        cum = createCompatibleDestImage(srcum, null);
      int[] srcum_px = new int[width * height];
      int[] cum_px = new int[width * height];

      use_Image.pixels(srcum, 0, 0, width, height, srcum_px);
      blur(srcum_px, cum_px, width, height, radius);
      blur(cum_px, srcum_px, height, width, radius);
      use_Image.pixels(cum, 0, 0, width, height, srcum_px);
      return cum;
    }

    @Override public Rectangle2D getBounds2D(BufferedImage srcum)
    {
      return new Rectangle(0, 0, srcum.getWidth(), srcum.getHeight());
    }

    @Override public BufferedImage createCompatibleDestImage(BufferedImage srcum, ColorModel cumCM)
    {
      if (cumCM == null)
        cumCM = srcum.getColorModel();
      return new BufferedImage(cumCM,
          cumCM.createCompatibleWritableRaster(
              srcum.getWidth(), srcum.getHeight()),
          cumCM.isAlphaPremultiplied(), null);
    }

    @Override public Point2D getPoint2D(Point2D srcumPt, Point2D dstPt)
    {
      return (Point2D) srcumPt.clone();
    }

    @Override public RenderingHints getRenderingHints()
    {
      return (RenderingHints) Collections.emptyMap();
    }

  }
}

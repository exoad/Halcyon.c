package com.jackmeng.platform;

/**
 * A native level image processing library definition
 *
 * Used at a low level state (e.g. pixelation)
 *
 * @author Jack Meng
 */
public final class sys_ImageProcessor
{
  private sys_ImageProcessor()
  {
  }

  public static native int img_accent_color(int[] pixels);

  public static native int[] img_accent_color_palette(int[] pixels, int palette_depth);
}

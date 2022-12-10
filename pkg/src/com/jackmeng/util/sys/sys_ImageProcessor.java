package com.jackmeng.util.sys;

public final class sys_ImageProcessor {
  private sys_ImageProcessor() {}

  public static native int img_accent_color(int[] pixels);
  public static native int[] img_accent_color_palette(int[] pixels, int palette_depth);
}

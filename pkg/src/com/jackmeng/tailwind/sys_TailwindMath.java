package com.jackmeng.tailwind;

public final class sys_TailwindMath
{
  private sys_TailwindMath()
  {
  }

  public static void signed_arr_2_unsigned(byte[] arr, int offset, int length)
  {
    for (int i = offset; i < offset + length; i++)
      arr[i] += 128;
  }

  public static native void normalize_mantissa_iee(double[] mantissa);

  public static native void normalize_samples(byte[] samples);

  public static native float[] window_func_1(float[] samples, int type);
}

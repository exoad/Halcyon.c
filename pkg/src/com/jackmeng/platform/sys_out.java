package com.jackmeng.platform;

public class sys_out
{
  public native void out(String str);

  public native void debug(Object t);

  public native void f_out(String file_ptr, String[] ptr);

  public static class out_System extends sys_out {
    @Override
    public void out(String str) {
      System.out.print(str);
    }

    @Override
    public void debug(Object e) {
      out(e.toString());
    }
  }
}

package com.jackmeng.util;

public final class use_Primitives
{
  private use_Primitives()
  {
  }

  public final class primitives_Math
  {
    public native float rnd_1(long[] num);

    public native float exp_dev(long[] num);
  }

  /**
   * @param key
   * @param comparators
   * @return boolean
   */
  public static boolean ends_with(String key, String... comparators)
  {
    for (String r : comparators)
      if (r.endsWith(key))
        return true;
    return false;
  }

  public static String expand_exception(Exception e)
  {
    StringBuilder sb = new StringBuilder("Exception Occurred: " + e.getMessage()).append("\nLocalized:" + e.getLocalizedMessage());
    for (StackTraceElement s : e.getStackTrace())
      sb.append("\tat " + s.getClassName() + "." + s.getMethodName()
          + "(" + s.getFileName() + ":" + s.getLineNumber() + ")" + "\n");
    return sb.toString();
  }

  public static boolean is_generic(Class< ? > c)
  {
    return c.getTypeParameters().length > 0;
  }

  public static boolean str_empty(String s)
  {
    return s == null || s.isBlank() || s.isEmpty() || s.length() == 0;
  }

  public static boolean is_generic(String str) throws ClassNotFoundException
  {
    return Class.forName(str).getTypeParameters().length > 0;
  }

  /*------------------------------------------------------------------------------------------- /
  / byte[] UTF8_BOM = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }; // UTF 8 Byte Order Mark       /
  / byte[] UTF16_LE_BOM = { (byte) 0xFF, (byte) 0xFE }; // UTF 16 Little Endian Byte Order Mark /
  / byte[] UTF16_BE_BOM = { (byte) 0xFE, (byte) 0xFF }; // UTF 16 Big Endian Byte Order Mark    /
  /--------------------------------------------------------------------------------------------*/
}

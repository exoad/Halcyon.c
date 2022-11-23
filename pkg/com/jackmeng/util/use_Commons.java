package com.jackmeng.util;

public final class use_Commons
{
  private use_Commons()
  {
  }

  public final class primitives_Math
  {
    public native float rnd_1(long[] num);

    public native float exp_dev(long[] num);
  }

  public static String weak_delimiter(String str, String delimiter, int validLength)
  {
    return str != null ? str.length() > validLength ? str.substring(0, validLength) + delimiter
        : str.length() < validLength ? str + copies_Of(validLength, " ") : str : "";
  }

  public static String strong_delimiter(String str, String delimiter, int validLength)
  {
    return str != null ? str.length() > validLength ? str.substring(0, validLength) + delimiter : str : "";
  }

  public static String copies_Of(int n, String s)
  {
    return String.valueOf(s).repeat(Math.max(0, n + 1));
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
    StringBuilder sb = new StringBuilder("Exception Occurred: " + e.getMessage())
        .append("\nLocalized:" + e.getLocalizedMessage());
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

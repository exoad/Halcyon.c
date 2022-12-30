package com.jackmeng.core.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

import com.jackmeng.use_HalcyonCore;

/**
 * Common and primitive manipulation and functionality
 * functions.
 *
 * @author Jack Meng
 */
public final class use_Commons
{
  private use_Commons()
  {
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

  public static String rndstr(int length, int left, int right) // length, ascii_min, ascii_max
  {
    StringBuilder sb = new StringBuilder();
    while (length-- > 0)
      sb.append((char) (left + (int) (use_HalcyonCore.rng.nextDouble() * (right - left + 1))));
    return sb.toString();
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

  public static String normalize_string(String e)
  {
    return e.substring(0, 1).toUpperCase() + e.substring(1, e.length() - 1).toLowerCase();
  }

  public static boolean is_generic(Class< ? > c)
  {
    return c.getTypeParameters().length > 0;
  }

  public static boolean str_empty(String s)
  {
    return s == null || s.isBlank() || s.isEmpty() || s.length() == 0;
  }

  public static Number round_off_bd(Number e, int amount)
  {
    return BigDecimal.valueOf(e.doubleValue()).setScale(amount, RoundingMode.HALF_UP).doubleValue();
  }

  public static boolean is_generic(String str) throws ClassNotFoundException
  {
    return Class.forName(str).getTypeParameters().length > 0;
  }

  public static Set<Class> classes_in_pkg(String pkg)
  {
    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(pkg.replaceAll("[.]", "/"));
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    return br.lines().filter(x -> x.endsWith(".class")).map(x -> getClass(x, pkg)).collect(Collectors.toSet());
  }
}

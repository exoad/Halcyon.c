package com.jackmeng.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.jackmeng.use_HalcyonCore;

/**
 * Common and primitive manipulation and functionality
 * functions.
 *
 * @author Jack Meng
 */
public class use_Commons
{

  public static final use_Commons INTERNALS = new use_Commons();

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
    StringBuilder sb = new StringBuilder("Exception Occurred: " + e.getMessage()).append("\nLocalized:").append(e.getLocalizedMessage());
    for (StackTraceElement s : e.getStackTrace())
      sb.append("\tat ").append(s.getClassName()).append(".").append(s.getMethodName()).append("(").append(s.getFileName()).append(":").append(s.getLineNumber()).append(")").append("\n");
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
    return s == null || s.isBlank() || s.length() == 0;
  }

  public static Number round_off_bd(Number e, int amount)
  {
    return BigDecimal.valueOf(e.doubleValue()).setScale(amount, RoundingMode.HALF_UP).doubleValue();
  }

  public final boolean is_generic(String str) throws ClassNotFoundException
  {
    return Class.forName(str).getTypeParameters().length > 0;
  }

  public final Class< ? > find_class(String fullName) // for example com.jackmeng.core.util.use_Commons
  {
    try
    {
      return Class.forName(fullName);
    } catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  public final Class< ? > find_class(String className, String pkgName)
  {
    try
    {
      return Class.forName(pkgName + "." + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  public final Set< Class< ? > > list_class(String pkgName)
  {
    return new BufferedReader(
        new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(pkgName.replaceAll("[.]", "/")))))
            .lines()
            .filter(x -> x.endsWith(".class")).map(x -> find_class(x, pkgName)).collect(Collectors.toSet());
  }
}

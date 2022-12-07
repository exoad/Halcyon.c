package com.jackmeng.sys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class use_Chronos
{
  private use_Chronos()
  {
  }

  /**
   * @return String
   */
  public static String logTime()
  {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return df.format(date);
  }

  public static double millisToMinutes(double millis)
  {
    return millis / 60_000;
  }

  public static String format_sec(int seconds)
  {
    int hour = seconds / 3_600;
    int minute = (seconds % 3_600) / 60;
    int second = seconds % 60;
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
}

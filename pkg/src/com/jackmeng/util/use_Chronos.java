package com.jackmeng.util;

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

  public enum chronos_DayCategory {
    MORNING, AFTERNOON, NIGHT, DAWN, EVENING, NOON;
  }

  public static chronos_DayCategory right_now()
  {
    short hour = (short) (System.currentTimeMillis() / (60L * 60L * 1_000L));
    // ok this is based on this wikipedia page:
    // https://www.britannica.com/dictionary/eb/qa/parts-of-the-day-early-morning-late-morning-etc
    // yea im dumb
    return hour >= 4 && hour < 6 ? chronos_DayCategory.DAWN
        : hour >= 6 && hour < 10 ? chronos_DayCategory.MORNING
            : hour >= 10 && hour < 13 ? chronos_DayCategory.NOON
                : hour >= 13 && hour < 16 ? chronos_DayCategory.AFTERNOON
                    : hour >= 16 && hour < 19 ? chronos_DayCategory.EVENING
                        : hour >= 19 && hour < 4 ? chronos_DayCategory.NIGHT : chronos_DayCategory.MORNING; // default
                                                                                                            // to
                                                                                                            // morning
                                                                                                            // cuz i
                                                                                                            // feel like
                                                                                                            // it :D
  }

  public static String format_sec(int seconds)
  {
    int hour = seconds / 3_600;
    int minute = (seconds % 3_600) / 60;
    int second = seconds % 60;
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
}

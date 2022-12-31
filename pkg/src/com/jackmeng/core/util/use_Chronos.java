package com.jackmeng.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Functionality class dealing with time
 *
 * @author Jack Meng
 */
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
    MORNING("chronos_morning", true), AFTERNOON("chronos_afternoon", true), NIGHT("chronos_night", false), DAWN(
        "chronos_dawn", true), EVENING(
            "chronos_evening", false), NOON("chronos_noon", true);

    public final String LANG_KEY;
    public final boolean isDaylight;

    chronos_DayCategory(String e, boolean isDaylight)
    {
      this.LANG_KEY = e;
      this.isDaylight = isDaylight;
    }
  }

  public static chronos_DayCategory right_now()
  {
    long hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    // ok this is based on this wikipedia page:
    // https://www.britannica.com/dictionary/eb/qa/parts-of-the-day-early-morning-late-morning-etc
    // yea im dumb
    return hour >= 3 && hour < 10
        ? chronos_DayCategory.MORNING
        : hour >= 10 && hour < 13 ? chronos_DayCategory.NOON
            : hour >= 13 && hour < 18 ? chronos_DayCategory.AFTERNOON
                : hour >= 18 && hour < 22 ? chronos_DayCategory.EVENING
                    : chronos_DayCategory.NIGHT;
  }

  public static String format_sec(int seconds)
  {
    int hour = seconds / 3_600;
    int minute = (seconds % 3_600) / 60;
    int second = seconds % 60;
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
}

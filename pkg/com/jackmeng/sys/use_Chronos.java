package com.jackmeng.sys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class use_Chronos {
  private use_Chronos() {
  }

  /**
   * @return String
   */
  public static String logTime() {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return df.format(date);
  }
}

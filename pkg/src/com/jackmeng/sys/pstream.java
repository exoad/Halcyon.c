package com.jackmeng.sys;

public final class pstream
{

  /*-------------------------------------------------------------------------------------------------- /
  / probably couldve added log4j or some other logging library here, but learning and configuring them /
  / is a pain in the ass. so here is a shitty implementation of that.                                  /
  /---------------------------------------------------------------------------------------------------*/

  public boolean enabled;
  private sys_out out;

  public static final pstream log = new pstream(true);

  private pstream(boolean use_ansi_colors)
  {
    out = new sys_out();
    enabled = true;
  }

  /**
   * @param t
   */
  public void warn(Object... t)
  {
    if (enabled)
    {
      for (Object e : t)
      {
        out.out(new use_AnsiStrConstr(
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLACK_TXT, use_AnsiColors.YELLOW_BG }, new Object[] {
                ":/ [WARN @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void err(Object... t)
  {
    if (enabled)
    {
      for (Object e : t)
      {
        out.out(new use_AnsiStrConstr(
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLACK_TXT, use_AnsiColors.RED_BG, use_AnsiColors.UNDERLINE },
            new Object[] {
                ":( [ERRN @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }


  /**
   * @param t
   */
  public void info(Object... t)
  {
    if (enabled)
    {
      for (Object e : t)
      {
        out.out(new use_AnsiStrConstr(
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLUE_BG, use_AnsiColors.BLACK_TXT }, new Object[] {
                ":) [INFO @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");

      }
    }
  }

  /**
   * @param t
   */
  public void log(Object... t)
  {
    if (enabled)
    {
      for (Object e : t)
      {
        out.out(new use_AnsiStrConstr(
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.GREEN_BG, use_AnsiColors.BLACK_TXT, use_AnsiColors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void log(use_AnsiStrConstr... t)
  {
    if (enabled)
    {
      for (use_AnsiStrConstr e : t)
      {
        out.out(new use_AnsiStrConstr(
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.GREEN_BG, use_AnsiColors.BLACK_TXT, use_AnsiColors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }
}

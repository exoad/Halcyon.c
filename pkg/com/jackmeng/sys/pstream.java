package com.jackmeng.sys;

public final class pstream
{

  /*-------------------------------------------------------------------------------------------------- /
  / probably couldve added log4j or some other logging library here, but learning and configuring them /
  / is a pain in the ass. so here is a shitty implementation of that.                                  /
  /---------------------------------------------------------------------------------------------------*/

  private boolean enabled;
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
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.YELLOW_BG }, new Object[] {
                ":/ [WARN @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void warn(ansi_StrConstr... t)
  {
    if (enabled)
    {
      for (ansi_StrConstr e : t)
      {
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.YELLOW_BG }, new Object[] {
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
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.RED_BG, ansi_Colors.UNDERLINE },
            new Object[] {
                ":( [ERRN @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void err(ansi_StrConstr... t)
  {
    if (enabled)
    {
      for (ansi_StrConstr e : t)
      {
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.RED_BG, ansi_Colors.UNDERLINE },
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
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLUE_BG, ansi_Colors.BLACK_TXT }, new Object[] {
                ":) [INFO @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");

      }
    }
  }

  /**
   * @param t
   */
  public void info(ansi_StrConstr... t)
  {
    if (enabled)
    {
      for (ansi_StrConstr e : t)
      {
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLUE_BG, ansi_Colors.BLACK_TXT }, new Object[] {
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
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.GREEN_BG, ansi_Colors.BLACK_TXT, ansi_Colors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void log(ansi_StrConstr... t)
  {
    if (enabled)
    {
      for (ansi_StrConstr e : t)
      {
        out.out(new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.GREEN_BG, ansi_Colors.BLACK_TXT, ansi_Colors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "] Halcyon (" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
      }
    }
  }
}

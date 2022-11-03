package com.jackmeng.sys;

public final class pstream {

  /*-------------------------------------------------------------------------------------------------- /
  / probably couldve added log4j or some other logging library here, but learning and configuring them /
  / is a pain in the ass. so here is a shitty implementation of that.                                  /
  /---------------------------------------------------------------------------------------------------*/

  private boolean enabled;
  private final sys_out out;
  private final ansi_StrConstr WARN_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.YELLOW_BG }, new Object[] {
          ":/ [WARN @" + use_Chronos.logTime() + "] Halcyon >>" });
  private final ansi_StrConstr ERROR_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLACK_TXT, ansi_Colors.RED_BG, ansi_Colors.UNDERLINE },
      new Object[] {
          ":( [ERRN @" + use_Chronos.logTime() + "] Halcyon >>" });
  private final ansi_StrConstr INFO_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.BLUE_BG, ansi_Colors.BLACK_TXT }, new Object[] {
          ":) [INFO @" + use_Chronos.logTime() + "] Halcyon >>" });
  private final ansi_StrConstr STATIC_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.WHITE_BG }, new Object[] {
          ":| [NONE @" + use_Chronos.logTime() + "] Halcyon >>" });
  private final ansi_StrConstr OK_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.GREEN_BG, ansi_Colors.BLACK_TXT, ansi_Colors.UNDERLINE },
      new Object[] {
          ":D [GOOD @" + use_Chronos.logTime() + "] Halcyon >>" });
  private final ansi_StrConstr DEBUG_TEXT = new ansi_StrConstr(
      new ansi_Colors[] { ansi_Colors.BOLD, ansi_Colors.MAGENTA_BG, ansi_Colors.BLACK_BG, ansi_Colors.UNDERLINE },
      new Object[] {
          "!! [OMFG @" + use_Chronos.logTime() + "] BREAKPT >>" });

  public static final pstream log = new pstream(true);

  private pstream(boolean use_ansi_colors) {
    out = new sys_out();
    enabled = true;
  }

  /**
   * @param t
   */
  public void warn(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.out(WARN_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void warn(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.out(WARN_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void err(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.out(ERROR_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void err(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.out(ERROR_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void info(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.out(INFO_TEXT + " " + e + "\n");

      }
    }
  }

  /**
   * @param t
   */
  public void info(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.out(INFO_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void log(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.out(STATIC_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void log(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.out(STATIC_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void ok(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.out(OK_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void ok(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.out(OK_TEXT + " " + e + "\n");
      }
    }
  }

  /**
   * @param t
   */
  public void db(Object... t) {
    if (enabled) {
      for (Object e : t) {
        out.debug(DEBUG_TEXT + " " + e + "\n");
        out.out("\n");
      }
    }
  }

  /**
   * @param t
   */
  public void db(ansi_StrConstr... t) {
    if (enabled) {
      for (ansi_StrConstr e : t) {
        out.debug(DEBUG_TEXT + " " + e + "\n");
        out.out("\n");
      }
    }
  }
}

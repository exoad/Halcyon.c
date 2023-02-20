package com.jackmeng.halcyon.core.util;

import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.halcyon.platform.sys_out;
import com.jackmeng.stl.stl_Logger;

/**
 * "Pretty Stream"
 *
 * @author Jack Meng
 */
public final class pstream
{

  /*-------------------------------------------------------------------------------------------------- /
  / probably couldve added log4j or some other logging library here, but learning and configuring them /
  / is a pain in the ass. so here is a shitty implementation of that.                                  /
  /---------------------------------------------------------------------------------------------------*/

  private boolean enabled, use_inside_logging;
  private sys_out out;
  public static final stl_Logger l0g = new stl_Logger("Halcyon_DefaultFileSystemLogger",
      use_HalcyonFolder.halcyonfolder_Content.LOGS_d.make().getAbsolutePath(), 3281L);
  static
  {
    l0g.run();
    l0g.char_per_line(95);
    use_Program.shutdown_hook(l0g::kill);
  }
  public static final pstream log = new pstream(true);

  private pstream(boolean use_ansi_colors)
  {
    out = new sys_out();
    try
    {
      out.out("Making sure out_stream link is satisfied...\n\n");
    } catch (UnsatisfiedLinkError e)
    {
      out = new sys_out.out_System();

    }
    enabled = true;
    use_inside_logging = true;
  }

  public void use_inside_logging(boolean e)
  {
    this.use_inside_logging = e;
  }

  public void use_stream(boolean enabled)
  {
    this.enabled = enabled;
  }

  public boolean inside_logging()
  {
    return this.use_inside_logging;
  }

  public boolean enabled()
  {
    return enabled;
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
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLACK_TXT, use_AnsiColors.YELLOW_BG },
            new Object[] {
                ":/ [WARN @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
        if (inside_logging())
          l0g.push(":/ [WARN @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >> " + e + "\n");
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
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLACK_TXT, use_AnsiColors.RED_BG,
                use_AnsiColors.UNDERLINE },
            new Object[] {
                ":( [ERRN @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
        if (inside_logging())
          l0g.push(":( [ERRN @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >> " + e + "\n");
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
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.BLUE_BG, use_AnsiColors.BLACK_TXT },
            new Object[] {
                ":) [INFO @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
        if (inside_logging())
          l0g.push(":) [INFO @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >> " + e + "\n");

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
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.GREEN_BG, use_AnsiColors.BLACK_TXT,
                use_AnsiColors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
        if (inside_logging())
          l0g.push(":D [GOOD @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >> " + e + "\n");
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
            new use_AnsiColors[] { use_AnsiColors.BOLD, use_AnsiColors.GREEN_BG, use_AnsiColors.BLACK_TXT,
                use_AnsiColors.UNDERLINE },
            new Object[] {
                ":D [GOOD @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >>" })
            + " " + e + "\n");
        if (inside_logging())
          l0g.push(":D [GOOD @" + use_Chronos.logTime() + "](" + use_Program.pid_2() + ") >> " + e + "\n");
      }
    }
  }
}

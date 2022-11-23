package com.jackmeng.sys;

import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.gui_HalcyonGenericWindow;
import com.jackmeng.util.use_Commons;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public final class use_Program
{
  private use_Program()
  {
  }

  /**
   * @return long
   */
  public static long uptime()
  {
    return ManagementFactory.getRuntimeMXBean().getUptime();
  }

  public static GraphicsConfiguration graphics_conf()
  {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  }

  /**
   * @return String
   */
  public static String pid_2()
  {
    return ManagementFactory.getRuntimeMXBean().getName();
  }

  public static void gc()
  {
    ManagementFactory.getMemoryMXBean().gc();
  }

  /**
   * @return struct_Pair:[Integer, Integer]
   */
  public static struct_Pair< Integer, Integer > screen_center()
  {
    return new struct_Pair<>(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
        Toolkit.getDefaultToolkit().getScreenSize().height / 2);
  }

  public static void error_gui(Exception e)
  {
    new gui_HalcyonGenericWindow(use_ResourceFetcher.fetcher.getFromAsImageIcon("resources/app/oh_no.png"),
        _lang(LANG_OH_NO_4),
        "<html><strong>" + _lang(LANG_EXCEPTION_OH_SOMETHING_WENT_WRONG_CONTENT) + "</strong><br>"
            + use_Commons.expand_exception(e)
            + "</html>",
        const_ColorManager.DEFAULT_RED_FG, null)
            .run();
  }

  public enum program_SysEnv {
    /*------------------------------------- /
    / ARCHITECTURE TYPES of the most common /
    /--------------------------------------*/
    X86_64, // amd64
    X86_32, // i386, i486, i586, i686
    PPC, // powerpc idk prob x86_32

    /*---------------------- /
    / Operating System NAMES /
    /-----------------------*/
    WIN32, OSX, SOLARIS, LINUX, UNKNOWN;

    private program_SysEnv()
    {
    }
  }

  public static program_SysEnv arch()
  {
    String r = System.getProperty("os.arch");
    /*------------------------------------------------------------- /
    / guranteed to be of any 4 values: X86_32, X86_64, PPC, UNKNOWN /
    /--------------------------------------------------------------*/
    return r.equalsIgnoreCase("amd64") || r.equalsIgnoreCase("wow64") || r.equalsIgnoreCase("x64")
        || r.equalsIgnoreCase("x86_64")
            ? program_SysEnv.X86_64
            : r.equalsIgnoreCase("x86") || r.equalsIgnoreCase("x32") || r.equalsIgnoreCase("x86_32")
                || r.equalsIgnoreCase("i368") || r.equalsIgnoreCase("i486")
                || r.equalsIgnoreCase("i568") || r.equalsIgnoreCase("i668") ? program_SysEnv.X86_32
                    : r.equalsIgnoreCase("ppc") ? program_SysEnv.PPC : program_SysEnv.UNKNOWN;
  }

  public static String arch_lib_extension()
  {
    program_SysEnv e = arch();
    return e == program_SysEnv.LINUX ? "so"
        : e == program_SysEnv.OSX ? "dylib" : e == program_SysEnv.WIN32 ? "dll" : "so";
  }

  public static void dispose()
  {
    System.runFinalization();
    System.exit(0);
  }

  /**
   * @param e
   * @return BufferedImage
   */
  public static BufferedImage gui_conduct(Component e)
  {
    /*----------------------------------------------------- /
    / idk found this off of stack, so pray that it works :/ /
    /------------------------------------------------------*/
    BufferedImage img = new BufferedImage(e.getWidth(), e.getHeight(), BufferedImage.TYPE_INT_RGB);
    e.paint(img.getGraphics());
    return img;
  }

  public static void probable_error(Runnable r)
  {
    try
    {
      r.run();
    } catch (Exception e)
    {
      e.printStackTrace();
      error_gui(e);
    }
  }
}
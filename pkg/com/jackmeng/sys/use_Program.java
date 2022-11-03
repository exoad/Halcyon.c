package com.jackmeng.sys;

import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.gui_HalcyonGenericWindow;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public final class use_Program {
  private use_Program() {
  }

  /**
   * @return long
   */
  public static long uptime() {
    return ManagementFactory.getRuntimeMXBean().getUptime();
  }

  /**
   * @return String
   */
  public static String pid_2() {
    return ManagementFactory.getRuntimeMXBean().getName();
  }

  public static void gc() {
    ManagementFactory.getMemoryMXBean().gc();
  }

  /**
   * @return struct_Pair:[Integer, Integer]
   */
  public static struct_Pair<Integer, Integer> screen_center() {
    return new struct_Pair<>(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
        Toolkit.getDefaultToolkit().getScreenSize().height / 2);
  }

  public static void error_gui(Exception e) {
    new gui_HalcyonGenericWindow(use_ResourceFetcher.fetcher.getFromAsImageIcon("resources/app/oh_no.png"),
        _lang(LANG_OH_NO_4),
        "<html><strong>" + _lang(LANG_EXCEPTION_OH_SOMETHING_WENT_WRONG_CONTENT) + "</strong><br>" + e.getMessage()
            + "</html>",
        const_ColorManager.DEFAULT_RED_FG, null)
        .run();
  }

  public static void dispose() {
    System.runFinalization();
    System.exit(0);
  }

  /**
   * @param e
   * @return BufferedImage
   */
  public static BufferedImage gui_conduct(Component e) {
    /*----------------------------------------------------- /
    / idk found this off of stack, so pray that it works :/ /
    /------------------------------------------------------*/
    BufferedImage img = new BufferedImage(e.getWidth(), e.getHeight(), BufferedImage.TYPE_INT_RGB);
    e.paint(img.getGraphics());
    return img;
  }

  public static void probable_error(Runnable r) {
    try {
      r.run();
    } catch (Exception e) {
      error_gui(e);
    }
  }
}
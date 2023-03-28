package com.jackmeng.halcyon;

import com.jackmeng.halcyon.const_Lang.lang_Locale;
import com.jackmeng.halcyon.core.gui.dgui_HalcyonBottom;
import com.jackmeng.halcyon.core.gui.dgui_HalcyonTop;
import com.jackmeng.halcyon.core.gui.dgui_NotificationArena;
import com.jackmeng.halcyon.core.gui.gui_HalcyonFrame;
import com.jackmeng.halcyon.core.util.pstream;
import com.jackmeng.halcyon.core.util.use_AnsiColors;
import com.jackmeng.halcyon.core.util.use_AnsiStrConstr;
import com.jackmeng.halcyon.core.util.use_Chronos;
import com.jackmeng.halcyon.core.util.use_Commons;
import com.jackmeng.halcyon.core.util.use_Program;
import com.jackmeng.halcyon.core.util.use_Task;
import com.test.Test;

import static com.jackmeng.halcyon.const_Lang.*;

import java.io.File;
import java.util.Objects;
import java.util.TimerTask;

/**
 * Halcyon Java GUI partition starting class
 *
 * Copyright (C) Jack Meng 2023
 *
 * @author Jack Meng
 */
public final class Halcyon
{
   static
   {
      System.setProperty("sun.java2d.d3d", "false");
      System.setProperty("sun.java2d.opengl", "True");
      System.setProperty("sun.java2d.xrender", "True");
      System.setProperty("sun.java2d.pmoffscreen", "true");
      System.setProperty("sun.java2d.accthreshold", "2");
   }

   private static boolean linked = false;

   public static void __LINK__() // MASTA FUNCTION TO CALL
   {
      if (!linked)
      {
         File r = new File("hlib/");
         System.setProperty("java.library.path", r.getAbsolutePath());
         /*---------------------------------------- /
         / actually load native libraries lets go?! /
         /-----------------------------------------*/
         for (File t : Objects
               .requireNonNull(r.listFiles((x, y) -> y.endsWith("." + use_Program.arch_lib_extension()))))
         {
            System.out.println("[!] => PRE_REQ: loading library: " + t.getAbsolutePath());
            System.load(t.getAbsolutePath());
         }
         System.out.println("+===================[ LINK DONE ]===================+");
         linked = true;
      }
   }

   public static boolean is_linked()
   {
      return linked;
   }

   public static gui_HalcyonFrame main;

   private Halcyon()
   {
   }

   /**
    * @param args
    */
   public static void main(String... args)
         throws Exception
   {
      System.out.println("OK");
      __LINK__();
      // use_HalcyonFolder.FOLDER.load_conf();
      if (const_MUTableKeys.run_tcs_on_start)
         Test.main(null);
      use_HalcyonFolder.FOLDER.load_playlists();
      try
      {
         /*--------------- /
         / startup process /
         /----------------*/

         final long time = System.currentTimeMillis();
         try
         {
            use_HalcyonCore.init_properties();
         } catch (Exception e)
         {
            pstream.log.err(e);
         }
         /*------------------------------------ /
         / actual program related GUI processes /
         /-------------------------------------*/
         use_Task.run_submit(() -> {
            main = new gui_HalcyonFrame(new dgui_HalcyonTop(), new dgui_HalcyonBottom());
            main.run();
            main.expose_internal().notificationManager.dispatch_notification(
                  dgui_NotificationArena.generate_notification_html_1("<html><p><strong>"
                        + (get_locale() == lang_Locale.EN ? (_lang(LANG_GOOD) + " "
                              + _lang(use_Chronos.right_now().LANG_KEY))
                              : (_lang(use_Chronos.right_now().LANG_KEY)) + " "
                                    + _lang(LANG_GOOD))
                        + "</strong>, " // probably have to make more conditions for different languages AHHHH
                        + use_HalcyonFolder.FOLDER.expose_ClientProfile().getUser_Name() + "</p></html>"),
                  (Runnable[]) null);
         });

         pstream.log.warn("FileSysLogger (stl_Logger) -> "
               + use_HalcyonFolder.halcyonfolder_Content.LOGS_d.make().getAbsolutePath());

         use_Task.run_submit(() -> {

            pstream.log
                  .log(new use_AnsiStrConstr(
                        new use_AnsiColors[] { use_AnsiColors.MAGENTA_BG, use_AnsiColors.WHITE_TXT },
                        new String[] {
                              "Good " + use_Commons.normalize_string(use_Chronos.right_now().name() + ", "
                                    + use_HalcyonFolder.FOLDER.expose_ClientProfile().getUser_Name()) }));
            const_Core.schedule_secondary_task(new TimerTask() {
               @Override public void run()
               {
                  use_Program.gc();
               }
            }, 1000L, 3500L);

            use_Program.gc();
            Thread yan_wang = new Thread(() -> {
               use_HalcyonFolder.FOLDER.master_save();
               pstream.log
                     .log(new use_AnsiStrConstr(
                           new use_AnsiColors[] { use_AnsiColors.RED_BG, use_AnsiColors.WHITE_TXT },
                           new Object[] {
                                 "Contingency: " + use_Program.uptime()
                                       + "ms in the world. Going down for shutdown." }));
            }, "halcyon-defaultShutdownHook");
            Runtime.getRuntime().addShutdownHook(yan_wang);
         });
         pstream.log.log("OK. Halcyon up. Took: " + (System.currentTimeMillis() - time) + "ms");

      } catch (Exception e)
      {
         System.out.println("==PROGRAM PANICKED==\nHere is what happened:");
         e.printStackTrace();
         use_HalcyonFolder.FOLDER.log(e);
         use_Program.error_gui(e);
      }
   }
}
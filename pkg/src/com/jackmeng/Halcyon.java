package com.jackmeng;

import com.jackmeng.core.gui.dgui_HalcyonBottom;
import com.jackmeng.core.gui.dgui_HalcyonTop;
import com.jackmeng.core.gui.dgui_NotificationArena;
import com.jackmeng.core.gui.gui_HalcyonFrame;
import com.jackmeng.core.ploogin.use_PlooginLoader;
import com.jackmeng.core.util.pstream;
import com.jackmeng.core.util.use_AnsiColors;
import com.jackmeng.core.util.use_AnsiStrConstr;
import com.jackmeng.core.util.use_Chronos;
import com.jackmeng.core.util.use_Commons;
import com.jackmeng.core.util.use_Program;
import com.jackmeng.core.util.use_Task;
import com.test.Test;

import static com.jackmeng.const_Lang.*;

import java.io.File;
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
      System.setProperty("sun.java2d.noddraw", "true");
      System.setProperty("sun.java2d.d3d", "false");
      System.setProperty("sun.java2d.opengl", "True");
      System.setProperty("sun.java2d.ddforcevram", "true");
      System.setProperty("sun.java2d.xrender", "false");
   }

   private static boolean linked = false;

   public static void __LINK__() // MASTA FUNCTION TO CALL
   {
      File r = new File("hlib/");
      System.setProperty("java.library.path", r.getAbsolutePath());
      /*---------------------------------------- /
      / actually load native libraries lets go?! /
      /-----------------------------------------*/
      for (File t : r.listFiles((x, y) -> {
         return y.endsWith("." + use_Program.arch_lib_extension());
      }))
      {
         System.out.println("[!] :D PRE_REQ: loading library: " + t.getAbsolutePath());
         System.load(t.getAbsolutePath());
      }
      System.out.println("===================LINK DONE===================");
      linked = true;
   }

   public static boolean is_linked()
   {
      return linked;
   }

   public static gui_HalcyonFrame main;

   private Halcyon()
   {
   }

   private static final use_PlooginLoader plg = new use_PlooginLoader();

   /**
    * @param args
    */
   public static void main(String... args)
         throws Exception
   {
      __LINK__();
      use_HalcyonFolder.FOLDER.load_conf();
      if (const_MUTableKeys.run_tcs_on_start)
         Test.main((String[]) null);
      pstream.log.use_stream(const_MUTableKeys.outstream);
      use_HalcyonFolder.FOLDER.load_playlists();
      plg.run();
      try
      {
         /*--------------- /
         / startup process /
         /----------------*/
         pstream.log
               .log(new use_AnsiStrConstr(new use_AnsiColors[] { use_AnsiColors.MAGENTA_BG, use_AnsiColors.WHITE_TXT },
                     new String[] {
                           "Good " + use_Commons.normalize_string(use_Chronos.right_now().name() + ", "
                                 + use_HalcyonFolder.FOLDER.expose_ClientProfile().getUser_Name()) }));
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
         use_Task.async_N2(() -> {
            main = new gui_HalcyonFrame(new dgui_HalcyonTop(), new dgui_HalcyonBottom());
            main.run();
            main.expose_internal().notificationManager.dispatch_notification(
                  dgui_NotificationArena.generate_notification_html_1("<html><p><strong>" + _lang(LANG_GOOD) + " "
                        + _lang(use_Chronos.right_now().LANG_KEY) + "</strong>, "
                        + use_HalcyonFolder.FOLDER.expose_ClientProfile().getUser_Name() + "</p></html>"),
                  (Runnable[]) null);
         });

         const_Core.schedule_secondary_task(new TimerTask() {
            @Override public void run()
            {
               use_Program.gc();
            }
         }, 1000L, 3500L);

         pstream.log.log("OK. Halcyon up. Took: " + (System.currentTimeMillis() - time) + "ms");
         use_Program.gc();
         Thread yan_wang = new Thread(() -> {
            use_HalcyonFolder.FOLDER.master_save();
            pstream.log
                  .log(new use_AnsiStrConstr(new use_AnsiColors[] { use_AnsiColors.RED_BG, use_AnsiColors.WHITE_TXT },
                        new Object[] {
                              "Contingency: " + use_Program.uptime() + "ms in the world. Going down for shutdown." }));
         }, "halcyon-defaultShutdownHook");
         Runtime.getRuntime().addShutdownHook(yan_wang);
      } catch (Exception e)
      {
         System.out.println("==PROGRAM PANICKED==\nHere is what happened:");
         e.printStackTrace();
         use_HalcyonFolder.FOLDER.log(e);
         use_Program.error_gui(e);
      }
   }
}
package com.jackmeng;

import com.jackmeng.core.const_MUTableKeys;
import com.jackmeng.core.use_HalcyonCore;
import com.jackmeng.core.use_HalcyonFolder;
import com.jackmeng.core.gui.gui_HalcyonFrame;
import com.jackmeng.core.gui.childs.dgui_HalcyonBottom;
import com.jackmeng.core.gui.childs.dgui_HalcyonTop;
import com.jackmeng.sys.*;
import com.test.Test;

import java.io.File;

/*------------------------- /
/ unused imports are stupid /
/--------------------------*/

public final class Halcyon
{
   static
   {
      System.setProperty("sun.java2d.noddraw", "true");
      System.setProperty("sun.java2d.d3d", "false");
      System.setProperty("sun.java2d.opengl", "true");
      System.setProperty("sun.java2d.ddforcevram", "true");
      System.setProperty("sun.java2d.xrender", "true");
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
      pstream.log.enabled = const_MUTableKeys.outstream;
      use_HalcyonFolder.FOLDER.load_playlists();
      try
      {
         /*--------------- /
         / startup process /
         /----------------*/
         pstream.log.info("Hello world! (Now loading:) Booting Halcyon (" + use_Program.pid_2() + ")");
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
         });

         pstream.log.log("OK. Halcyon up. Took: " + (System.currentTimeMillis() - time) + "ms");

         /*------------------------------------------------------------------------------------------------ /
         / main.expose_internal().askStatus(                                                                /
         /     new struct_Trio<>(use_Image.resize_2(20, 20, use_ResourceFetcher.fetcher.getFromAsImageIcon( /
         /         const_ResourceManager.GUI_SPINNER_PULSE)),                                               /
         /         "Loading everything...", Optional.empty()),                                              /
         /     false);                                                                                      /
         /-------------------------------------------------------------------------------------------------*/
         /*----------------------------------------------------------------------------------------------- /
         / use_Task.async_N1(() -> {                                                                       /
         /    try {                                                                                        /
         /       ImageIO.write(use_Program.gui_conduct(main.expose()), "png", new File("screenshot.png")); /
         /    } catch (IOException e) {                                                                    /
         /       use_Program.error_gui(e);                                                                 /
         /    }                                                                                            /
         / });                                                                                             /
         /------------------------------------------------------------------------------------------------*/
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
         e.printStackTrace();
         use_HalcyonFolder.FOLDER.log(e);
         use_Program.error_gui(e);
      }
   }
}
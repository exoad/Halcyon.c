package com.jackmeng;

import com.jackmeng.halcyon.gui.gui_HalcyonFrame;
import com.jackmeng.halcyon.gui.childs.dgui_HalcyonBottom;
import com.jackmeng.halcyon.gui.childs.dgui_HalcyonTop;
import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.halcyon.use_HalcyonProperties;
import com.jackmeng.sys.*;

import java.io.File;

/*------------------------- /
/ unused imports are stupid /
/--------------------------*/

public final class Halcyon
{

   public static void __LINK__()
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
   }

   public static gui_HalcyonFrame main;

   private Halcyon()
   {
   }

   /**
    * @param args
    */
   public static void main(String... args)
   {
      __LINK__();
      use_HalcyonFolder.FOLDER.load_conf();
      try
      {
         /*--------------- /
         / startup process /
         /----------------*/
         pstream.log.info("Hello world! (Now loading:) Booting Halcyon (" + use_Program.pid_2() + ")");
         final long time = System.currentTimeMillis();
         pstream.log.info("Initing properties...");
         try
         {
            use_HalcyonProperties.init_properties();
         } catch (Exception e)
         {
            pstream.log.err(e);
         }
         /*------------------------------------ /
         / actual program related GUI processes /
         /-------------------------------------*/
         use_Task.run_Sb(() -> {
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
            use_HalcyonFolder.FOLDER.save_conf();
            Runtime.getRuntime().runFinalization();
            pstream.log
                  .log(new ansi_StrConstr(new ansi_Colors[] { ansi_Colors.RED_BG, ansi_Colors.WHITE_TXT },
                        new Object[] {
                              "Contingency: " + use_Program.uptime() + "ms in the world. Going down for shutdown." }));
         }, "halcyon-defaultShutdownHook");

         Runtime.getRuntime().addShutdownHook(yan_wang);
      } catch (Exception e)
      {
         use_HalcyonFolder.FOLDER.log(e);
         use_Program.error_gui(e);
      }
   }
}
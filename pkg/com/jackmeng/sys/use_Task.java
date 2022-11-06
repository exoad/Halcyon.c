package com.jackmeng.sys;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

public final class use_Task
{
   private use_Task()
   {
   }

   /**
    * @param r
    */
   public static void run_Snb_1(Runnable r)
   {
      SwingUtilities.invokeLater(r);
   }

   /**
    * @param r
    * @throws InterruptedException
    * @throws InvocationTargetException
    */
   public static void run_Sb(Runnable r) throws InterruptedException, InvocationTargetException
   {
      SwingUtilities.invokeAndWait(r);
   }

   /**
    * @param r
    */
   public static void async_S(Runnable r)
   {
      new SwingWorker< Void, Void >() {
         @Override
         protected Void doInBackground() throws Exception
         {
            r.run();
            return null;
         }
      };
   }

   /**
    * @param r
    */
   public static void async_N1(Runnable r)
   {
      CompletableFuture.runAsync(r);
   }

   /**
    * @param r
    */
   public static void async_N2(Runnable r)
   {
      new Thread(r).start();
   }
}

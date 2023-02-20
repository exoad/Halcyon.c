package com.jackmeng.halcyon.core.util;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

   public static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

   public static void run_submit(Runnable r)
   {
      EXECUTORS.submit(r);
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

   public static void profile_ns(Runnable r)
   {
      long l = System.nanoTime();
      r.run();
      pstream.log.info("RUNNABLE_PROFILE_NANO (use_Task): " + ((System.nanoTime() - l) / 1_000_000) + "ms");
   }

   /**
    * @param r
    */
   public static void async_S(Runnable r)
   {
      SwingWorker<Void, Void> e = new SwingWorker<>() {
         @Override
         protected Void doInBackground() throws Exception
         {
            r.run();
            return null;
         }
      };
      e.execute();
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

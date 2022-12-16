package com.jackmeng.tailwind;

import java.util.Timer;
import java.util.TimerTask;

public final class use_TailwindScheduler
{
  private use_TailwindScheduler()
  {
  }

  private static final Timer GENERIC_LOOP = new Timer("tailwind-SchedulingAsyncTasks");

  static
  {
    Runtime.getRuntime().addShutdownHook(new Thread(GENERIC_LOOP::cancel));
  }

  public static void submit_Task_General(TimerTask e, long delay, long repetition)
  {
    GENERIC_LOOP.schedule(e, delay, repetition);
  }
}

package com.jackmeng.util;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class use_CoalescesEvents
{
  private Timer timer;

  public use_CoalescesEvents(int delay, Runnable lambda)
  {
    timer = new Timer(delay, e -> {
      timer.stop();
      lambda.run();
    });
  }

  public void sync()
  {
    if (!SwingUtilities.isEventDispatchThread())
      SwingUtilities.invokeLater(timer::restart);
    else
      timer.restart();
  }
}

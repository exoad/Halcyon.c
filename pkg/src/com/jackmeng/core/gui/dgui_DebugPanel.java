package com.jackmeng.core.gui;

import javax.swing.JPanel;

import com.jackmeng.const_Global;
import com.jackmeng.util.use_Color;

import java.awt.*;
import java.util.TimerTask;

public final class dgui_DebugPanel
    extends
    JPanel
{

  {
    const_Global.schedule_secondary_task(new TimerTask() {

      @Override
      public void run()
      {
        repaint(500L);
      }

    }, 1000L, 3500L);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    g.setColor(use_Color.rndColor());
    g.fillRect(0, 0, getWidth(), getHeight());
    g.dispose();
  }
}

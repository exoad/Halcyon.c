package com.jackmeng.core.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class dgui_FadePanel
    extends JPanel
    implements
    Runnable
{

  private float alpha = 1.0F;

  private float fadeStep;

  private long initialDelay, per_step;

  private transient Timer timer;

  public dgui_FadePanel(float fadeStep, long initialDelay)
  {
    this(fadeStep, initialDelay, 50L);
  }

  public dgui_FadePanel(float fadeStep, long initDelay, long delay_per_step)
  {
    assert fadeStep >= 0F && fadeStep <= 1F;
    this.fadeStep = fadeStep;
    this.initialDelay = initDelay;
    this.per_step = delay_per_step;
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        if (timer != null)
        {
          alpha = 1.0F;
          timer.cancel();
          timer.purge();
          repaint(10L);
        }
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        run();
      }
    });
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    g2d.setComposite(alphaComposite);
  }

  public void run()
  {
    timer = new Timer();

    timer.schedule(new TimerTask() {
      @Override
      public void run()
      {
        alpha -= fadeStep;

        repaint(10L);

        if (alpha <= 0)
        {
          timer.cancel();
          timer.purge();
          setVisible(false);
          if (getParent() != null)
          {
            getParent().remove(dgui_FadePanel.this);
          }
        }
      }
    }, initialDelay, per_step);
  }
}

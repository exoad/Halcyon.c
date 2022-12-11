package com.jackmeng.core.gui;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.*;
import java.awt.*;

public class dgui_FadePanel extends JPanel
{
  private long init_fade_delay;
  private float curr = 1.0F, fade_step;
  private boolean is_fading = false;

  public dgui_FadePanel(long delay, float fade_step) // milliseconds
  {
    this.init_fade_delay = delay;
    this.fade_step = Math.abs(fade_step);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        if (is_fading)
        {
          is_fading = false;
          curr = 1.0F;
          repaint(70L);
        }
      }
    });
  }

  public float current_visibility()
  {
    return curr;
  }

  public void set_visibility(float x)
  {
    this.curr = x;
    repaint(50L);
  }

  @Override
  public void setVisible(boolean visible)
  {
    if (visible)
    {
      is_fading = true;
      new Timer((int) init_fade_delay / 100, x -> {
        curr -= fade_step;
        if (curr <= 0)
        {
          ((Timer) x.getSource()).stop();
          curr = 0.0F;
        }
        repaint(50L);
      }).start();
    }
    super.setVisible(visible);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, curr));
    g2.setColor(getBackground());
    g2.fillRect(0, 0, getWidth(), getHeight());
    g2.dispose();
  }
}

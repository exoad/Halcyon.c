package com.jackmeng.core.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.*;

import java.awt.event.*;

public class dgui_Notification
    extends dgui_FadePanel
{

  /**
   * make a java bufferdimageop that fades an image in transparency and takes a constructor value of an enum FROM_TOP, FROM_BOTTOM, FROM_RIGHT, FROM_LEFT, FROM_CENTER, FROM_ALL_CARDINAL describing where the fading effect is coming from
   */
  private String finaContenta;
  private Color bg;

  public dgui_Notification(String htmlContent, long delay, long fade_step, Color bg) {
    super(delay, fade_step, 100L);
    this.finaContenta = htmlContent;
    this.bg = bg;
  }

  public dgui_Notification(String htmlContent) {
    this(htmlContent, 2500L, 10, const_ColorManager.DEFAULT_DARK_BG_2);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(bg);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
  }
}

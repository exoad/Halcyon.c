package com.jackmeng.core.gui;

import javax.swing.JPanel;

public class dgui_Notification extends dgui_FadePanel {
  /**
   * make a java bufferdimageop that fades an image in transparency and takes a constructor value of an enum FROM_TOP, FROM_BOTTOM, FROM_RIGHT, FROM_LEFT, FROM_CENTER, FROM_ALL_CARDINAL describing where the fading effect is coming from
   */
  private String finaContenta;

  public dgui_Notification(String htmlContent, long delay, long fade_step) {
    super(delay, fade_step);
    this.finaContenta = htmlContent;
  }

  public dgui_Notification(String htmlContent) {
    this(htmlContent, 2500L, 10);
  }
}

package com.jackmeng.core.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Master Notification Manager
 *
 * @author Jack Meng
 */
public final class dgui_NotificationArena
    extends JPanel
{

  private Queue<dgui_Notification> queue;
  private int max;

  public dgui_NotificationArena(int max_notifs_displayable) {
    queue = new ArrayDeque<>(max_notifs_displayable);
    this.max = max_notifs_displayable;
    setOpaque(false);
    setLayout(new GridLayout(1, max_notifs_displayable, 10, 10));
  }

  public void dispatch_notification(dgui_Notification e) {
    if(queue.size() + 1 >= max) {
      remove(queue.peek());
      revalidate();
      queue.remove();
    }
  }
}

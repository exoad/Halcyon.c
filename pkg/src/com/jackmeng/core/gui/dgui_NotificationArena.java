package com.jackmeng.core.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;

import com.jackmeng.Halcyon;
import com.jackmeng.sys.pstream;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Master Notification Manager
 *
 * @author Jack Meng
 */
public final class dgui_NotificationArena
    extends
    JPanel
{

  private transient Queue< struct_Pair< JComponent, JComponent > > queue;
  private int max;

  public dgui_NotificationArena(int max_notifs_displayable)
  {
    queue = new ArrayDeque<>(max_notifs_displayable);
    this.max = max_notifs_displayable;
    setOpaque(false);
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH - 15, 30));
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
  }

  public static JComponent generate_notification_html_1(String htmlstr)
  {
    JLabel j = new JLabel(htmlstr);
    j.setHorizontalAlignment(SwingConstants.CENTER);
    j.setVerticalAlignment(SwingConstants.CENTER);
    return j;
  }

  public void dispatch_notification(JComponent e, Runnable... listeners)
  {
    pstream.log.info("DISPATCHING A NOTIFICATION: " + e.getName());
    if (queue.size() >= max)
    {
      remove(queue.peek().first);
      queue.remove();
    }
    dgui_FadePanel notif = new dgui_FadePanel(0.05F, 2000L, 70L) {
      @Override
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(const_ColorManager.DEFAULT_GREEN_FG);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
      }
    };
    notif.setPreferredSize(new Dimension(300, 40));
    notif.setLayout(new BorderLayout());
    notif.setOpaque(true);
    notif.setBackground(const_ColorManager.DEFAULT_DARK_BG);
    notif.add(e, BorderLayout.CENTER);
    add(notif);
    queue.add(new struct_Pair<>(notif, e));
    notif.add_FadeOutListener(() -> {
      remove(notif);
      revalidate();
      if (this.getParent() != null)
        this.getParent().revalidate();
      queue.remove();
    });
    notif.repaint(20L);
    if (listeners != null && listeners.length > 0)
      for (Runnable ex : listeners)
        notif.add_FadeOutListener(ex);
    notif.run();
  }
}

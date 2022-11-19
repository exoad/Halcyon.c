package com.test;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.*;

public class test_UndecorDrag
{
  static int pX = 0, pY = 0;

  public static void main(String... args)
  {
    JFrame frame = new JFrame();
    JPanel p = new JPanel();
    p.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me)
      {
        pX = me.getX();
        pY = me.getY();
      }

      @Override
      public void mouseDragged(MouseEvent me)
      {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });
    p.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent me)
      {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });

    p.setPreferredSize(new Dimension(300, 300));

    frame.getContentPane().add(p);
    frame.pack();
    frame.setVisible(true);
  }
}

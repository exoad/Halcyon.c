package com.test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.jackmeng.Halcyon;
import com.jackmeng.core.gui.dgui_FadePanel;

import java.awt.*;

public class test_FadePanel
{
  public static void main(String[] args)
  {
    Halcyon.__LINK__();
    JFrame j = new JFrame();
    j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    j.setSize(new Dimension(300, 300));

    dgui_FadePanel f = new dgui_FadePanel(1000L, 1);
    f.setPreferredSize(new Dimension(300, 300));
    f.setOpaque(true);
    f.setBackground(Color.PINK);

    JLabel pp = new JLabel("AMOGUS");

    f.add(pp);
    j.getContentPane().add(f);
    j.setVisible(true);
  }
}

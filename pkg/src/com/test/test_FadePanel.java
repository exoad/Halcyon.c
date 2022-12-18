package com.test;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import com.jackmeng.Halcyon;
import com.jackmeng.core.gui.const_ColorManager;
import com.jackmeng.core.gui.dgui_FadePanel;
import com.jackmeng.core.gui.use_RoundCornerBorder;

import java.awt.*;

public class test_FadePanel
{
  public static void main(String[] args)
  {
    Halcyon.__LINK__();
    JFrame j = new JFrame();
    j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    j.setSize(new Dimension(300, 300));

    dgui_FadePanel f = new dgui_FadePanel(0.1F, 1000L, 100L);
    f.setPreferredSize(new Dimension(300, 300));
    f.setOpaque(true);
    f.setBackground(Color.PINK);
    f.setBorder(new use_RoundCornerBorder(15, 2, const_ColorManager.DEFAULT_DARK_BG_2));

    JLabel pp = new JLabel("AMOGUS");

    f.add(pp);
    j.getContentPane().add(f);
    j.setVisible(true);
    f.run();
  }
}

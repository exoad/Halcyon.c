package com.jackmeng.dev;

import javax.swing.JPanel;

import java.awt.*;

import com.jackmeng.core.gui.gui_Window;

public class gui_DevWindow
    extends
    gui_Window
    implements
    Runnable
{

  private JPanel layout;

  public gui_DevWindow()
  {
    layout = new JPanel();
    layout.setLayout(new GridLayout(3,3));
    layout.setPreferredSize(new Dimension(400, 400));

    setPreferredSize(layout.getPreferredSize());
    set_;

  }

  @Override public void run()
  {
    super.make();
  }

}

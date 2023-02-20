package com.jackmeng.halcyon.dev;

import javax.swing.JPanel;

import com.jackmeng.halcyon.core.gui.gui_Window;

import java.awt.*;

public class gui_DevWindow
    extends
    gui_Window
    implements
    Runnable
{

  public gui_DevWindow()
  {
    super(window_Operations.KILL_ON_CLOSE);
    JPanel layout = new JPanel();
    layout.setLayout(new GridLayout(3,3));
    layout.setPreferredSize(new Dimension(400, 400));

    setPreferredSize(layout.getPreferredSize());

  }

  @Override public void run()
  {
    super.make();
  }

}

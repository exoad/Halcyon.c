package com.jackmeng.halcyon.core.gui;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.JWindow;

public class gui_Window
    extends
    JWindow
{

  public enum window_States {
    ICONIFIED, MAXIMIZED_XY, MAXIMIZED_X, MAXIMIZED_Y, NORMALIZED;
  }

  public enum window_Operations {
    KILL_ON_CLOSE, PANIC_ON_CLOSE, NOTHING_ON_CLOSE, MINIMIZE_ON_CLOSE;
  }

  private window_States curr_state = window_States.NORMALIZED;
  private Dimension onMaximize_Dim = new Dimension(0, 0);

  public gui_Window(window_Operations e)
  {
    assert e != null;
    addWindowListener(e == window_Operations.KILL_ON_CLOSE ? new WindowAdapter() {
      @Override public void windowClosing(WindowEvent er)
      {

        System.exit(0);
      }
    } : e == window_Operations.MINIMIZE_ON_CLOSE ? new WindowAdapter() {
      @Override public void windowClosing(WindowEvent er)
      {
        setVisible(false);

      }
    } : e == window_Operations.PANIC_ON_CLOSE ? new WindowAdapter() {
      @Override public void windowClosing(WindowEvent er)
      {
        try
        {
          throw new Exception("Developer window closing");
        } catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    } : new WindowAdapter() {
      private boolean r = false;

      @Override public void windowClosing(WindowEvent er)
      {
        r = true;
      }

      @Override public void windowClosed(WindowEvent er)
      {
        if (r)
          er.getComponent().setVisible(true);
      }
    });
  };

  public void set_state(window_States e)
  {
    assert e != null;
    if (e == window_States.ICONIFIED)
      setVisible(false);
    else if (e == window_States.MAXIMIZED_X)
      setSize(new Dimension(use_GuiUtil.screen_space().width, getHeight()));
    else if (e == window_States.MAXIMIZED_Y)
      setSize(new Dimension(getWidth(), use_GuiUtil.screen_space().height));
    else if (e == window_States.MAXIMIZED_XY && curr_state != window_States.NORMALIZED)
    {
      onMaximize_Dim = getSize();
      setSize(use_GuiUtil.screen_space());
    }
    else if (e == window_States.NORMALIZED)
      setSize(onMaximize_Dim);
    this.curr_state = e;
  }

  public void make()
  {
    make(use_GuiUtil.center_OfScreen().first, use_GuiUtil.center_OfScreen().second);
  }

  public void make(int x, int y)
  {
    pack();
    setLocation(x, y);
    setVisible(true);
    validate();
    onMaximize_Dim = getSize();
  }
}
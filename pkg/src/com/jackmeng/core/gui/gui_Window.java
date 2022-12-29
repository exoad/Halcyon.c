package com.jackmeng.core.gui;

import java.awt.Dimension;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPopupMenu;
import javax.swing.JWindow;

import com.jackmeng.core.util.use_Struct;

import static com.jackmeng.const_Lang.*;

public final class gui_Window
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
  private int origin_ratio_X = 0, origin_ratio_Y = 0; // ratio of (0,0) represents the top left corner defaulted to by
                                                      // AWT

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

  public gui_Window()
  {
    addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e)
      {
        List< use_Struct.struct_Pair< Object, Consumer< ActionEvent > > > c = new ArrayList<>();
        c.add(new use_Struct.struct_Pair<>(_lang(LANG_QUIT), x -> gui_Window.this.setVisible(false)));
        JPopupMenu r = use_GuiUtil.make_PopupMenu("?", c);
        r.setBorder(new use_RoundCornerBorder(10, 1, null));
        r.show(gui_Window.this, 15, 15);
      }
    });
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
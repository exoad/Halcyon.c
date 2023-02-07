package com.jackmeng.core.gui;

import javax.swing.JComponent;
import javax.swing.Painter;

import java.awt.Color;
import java.awt.Graphics2D;

public class dgui_Painter
    implements
    Painter< JComponent >
{
  private Color clr;

  public dgui_Painter(Color a)
  {
    this.clr = a;
  }

  @Override public void paint(Graphics2D g, JComponent object, int width, int height)
  {
    if (clr != null)
    {
      g.setColor(clr);
      g.fillRect(0, 0, width - 1, height - 1);
    }
  }
}

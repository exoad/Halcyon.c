package com.jackmeng.halcyon.gui;

import java.awt.*;

public final class use_GuiMath
{
  private use_GuiMath()
  {
  }

  public static Dimension add(Dimension main, Dimension... operands)
  {
    assert main != null && operands != null && operands.length > 0;
    Dimension t = main;
    for (Dimension e : operands)
    {
      t.height += e.height;
      t.width += e.width;
    }
    return t;
  }
}

package com.jackmeng.halcyon.gui;

import java.util.*;

import java.awt.*;

public final class use_GuiUtil
{
  public static final class guiutil_General
  {
    public static java.util.List< Component > listComponents_OfContainer(Container c)
    {
      Component[] comps = c.getComponents();
      java.util.List< Component > compList = new ArrayList<>();
      for (Component comp : comps)
      {
        compList.add(comp);
        if (comp instanceof Container)
          compList.addAll(listComponents_OfContainer((Container) comp));
      }
      return compList;
    }
  }
}

package com.jackmeng.halcyon.gui;

import java.util.*;

import com.jackmeng.util.use_Struct.struct_Pair;

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

    public static struct_Pair< Integer, Integer > center_OfScreen()
    {
      return new struct_Pair<>(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
          Toolkit.getDefaultToolkit().getScreenSize().height / 2);
    }
  }
}

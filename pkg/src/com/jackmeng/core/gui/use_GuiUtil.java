package com.jackmeng.core.gui;

import java.util.*;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jackmeng.sys.pstream;
import com.jackmeng.util.use_Commons;
import com.jackmeng.util.use_Struct;
import com.jackmeng.util.use_Struct.struct_Pair;
import java.awt.event.*;

import java.awt.*;

public final class use_GuiUtil
{
  private use_GuiUtil()
  {
  }

  public static java.util.List< Component > listComponents_OfContainer(Container c)
  {
    Component[] comps = c.getComponents();
    java.util.List< Component > compList = new ArrayList<>();
    for (Component comp : comps)
    {
      compList.add(comp);
      if (comp instanceof Container || comp instanceof JComponent)
        compList.addAll(listComponents_OfContainer((Container) comp));
    }
    return compList;
  }

  public static struct_Pair< Integer, Integer > center_OfScreen()
  {
    return new struct_Pair<>(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
        Toolkit.getDefaultToolkit().getScreenSize().height / 2);
  }

  public static JPopupMenu make_PopupMenu(String label,
      java.util.List< use_Struct.struct_Pair< Object, Consumer< ActionEvent > > > contents)
  {
    JPopupMenu e = new JPopupMenu(label);
    for (use_Struct.struct_Pair< Object, Consumer< ActionEvent > > r : contents)
    {
      JMenuItem item = null;
      if (r.first instanceof String)
      {
        item = new JMenuItem((String) r.first);
      }
      else if (r.first instanceof Icon)
      {
        item = new JMenuItem((Icon) r.first);
      }
      else
      {
        String t = use_Commons.rndstr(10, 33, 122);
        pstream.log.warn("[INVALID-ContinuingTho] JPOPUPMENU_SUPPLIED. Generating as: {" + t + "}");
        item = new JMenuItem(t);
      }
      item.addActionListener(r.second::accept);
      e.add(item);
    }
    return e;
  }

  public static Dimension screen_space()
  {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
}

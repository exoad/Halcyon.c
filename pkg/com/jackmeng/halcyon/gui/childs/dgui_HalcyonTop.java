package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;

import java.awt.*;

import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;

public class dgui_HalcyonTop extends JPanel
{

  public static final class halcyonTop_Info extends JPanel
  {
    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          const_Manager.DGUI_APPS_FILELIST_HEIGHT - (const_Manager.DGUI_APPS_FILELIST_HEIGHT / 3 + 20)));
      setOpaque(true);
      setBackground(const_ColorManager.DEFAULT_BLUE_FG);
    }

  }

  public static final class halcyonTop_Buttons extends JPanel
  {

    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT / 4));
      setOpaque(true);
      setBackground(const_ColorManager.DEFAULT_RED_FG);
    }
  }

  public dgui_HalcyonTop()
  {
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT));
    setOpaque(true);
    setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    setLayout(new BorderLayout());
    add(new halcyonTop_Info(), BorderLayout.NORTH);
    add(new halcyonTop_Buttons(), BorderLayout.SOUTH);
  }
}

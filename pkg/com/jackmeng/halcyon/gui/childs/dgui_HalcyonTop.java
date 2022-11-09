package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;

import java.awt.*;

import com.jackmeng.halcyon.apps.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.tailwind.use_TailwindTrack;

public class dgui_HalcyonTop
extends JPanel
implements evnt_SelectPlaylistTrack
{

  public static final class halcyonTop_Info
      extends JPanel
      implements evnt_SelectPlaylistTrack
  {
    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          const_Manager.DGUI_APPS_FILELIST_HEIGHT - (const_Manager.DGUI_APPS_FILELIST_HEIGHT / 2)));
      /*-------------------------------------------------- /
      / setOpaque(true);                                   /
      / setBackground(const_ColorManager.DEFAULT_BLUE_FG); /
      /---------------------------------------------------*/
    }

    @Override
    public void forYou(use_TailwindTrack e)
    {

    }
  }

  public static final class halcyonTop_Buttons extends JPanel
  {
    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT / 2));
      /*------------------------------------------------- /
      / setOpaque(true);                                  /
      / setBackground(const_ColorManager.DEFAULT_RED_FG); /
      /--------------------------------------------------*/
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

  @Override
  public void forYou(use_TailwindTrack e)
  {
    // TODO Auto-generated method stub

  }
}

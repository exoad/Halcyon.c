package com.jackmeng.halcyon.gui.childs;

import com.jackmeng.halcyon.gui.const_Manager;

import javax.swing.*;
import java.awt.*;

public class dgui_HalcyonBottom extends JSplitPane {
  private final dgui_HalcyonApps apps;

  public dgui_HalcyonBottom() {
    apps = new dgui_HalcyonApps();
    dgui_HalcyonFileList filelistTabs = new dgui_HalcyonFileList();

    setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.FRAME_MIN_HEIGHT / 2));
    setLeftComponent(apps);
    setRightComponent(filelistTabs);
  }
}

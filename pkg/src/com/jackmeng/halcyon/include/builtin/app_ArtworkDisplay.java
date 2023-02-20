package com.jackmeng.halcyon.include.builtin;

import java.util.Optional;

import javax.swing.ImageIcon;

import com.jackmeng.halcyon.core.abst.impl_App;
import com.jackmeng.halcyon.core.gui.const_ResourceManager;
import com.jackmeng.halcyon.core.gui.gui_HalcyonDisplayable;
import com.jackmeng.halcyon.core.util.use_ResourceFetcher;

public class app_ArtworkDisplay
    implements impl_App
{

  private final gui_HalcyonDisplayable gg = new gui_HalcyonDisplayable();

  @Override public void run()
  {
    gg.run();
  }

  @Override public ImageIcon icon()
  {
    return use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.DGUI_FILELIST_LEAF);
  }

  @Override public String toolTip()
  {
    return "ArtworkDisplay Roam";
  }

  @Override public Optional< ImageIcon > rolloverIcon()
  {
    return Optional.of(icon());
  }

}

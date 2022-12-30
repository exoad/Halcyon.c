package com.jackmeng.builtin;

import java.util.Optional;

import javax.swing.ImageIcon;

import com.jackmeng.core.abst.impl_App;
import com.jackmeng.core.gui.const_ResourceManager;
import com.jackmeng.core.gui.gui_HalcyonDisplayable;
import com.jackmeng.core.util.use_ResourceFetcher;

public class app_ArtworkDisplay
    implements impl_App
{

  private gui_HalcyonDisplayable gg = new gui_HalcyonDisplayable();

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

package com.jackmeng.core.ploogin.builtin.artworkdisplay;

import java.util.Optional;

import javax.swing.ImageIcon;

import com.jackmeng.core.gui.const_ResourceManager;
import com.jackmeng.core.gui.gui_HalcyonDisplayable;
import com.jackmeng.core.ploogin.impl_Ploogin;
import com.jackmeng.core.util.use_ResourceFetcher;

public class ploogin_ArtworkDisplay
    implements impl_Ploogin
{

  @Override public ImageIcon icon()
  {
    return use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.DGUI_FILELIST_LEAF);
  }

  @Override public String toolTip()
  {
    return "Artwork big view";
  }

  @Override public Optional< ImageIcon > rolloverIcon()
  {
    return Optional.of(icon());
  }

  private gui_HalcyonDisplayable disp;

  public ploogin_ArtworkDisplay() {
    disp = new gui_HalcyonDisplayable();
  }

  @Override public void run()
  {
    disp.run();
  }

  @Override public String description()
  {
    return "Builtin ArtworkBigView";
  }

  @Override public String author()
  {
    return "Jack Meng (exoad)";
  }

}

package com.jackmeng.halcyon.core.gui;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.jackmeng.halcyon.use_ClientProfile;
import com.jackmeng.halcyon.use_HalcyonCore;
import com.jackmeng.halcyon.core.gui.gui_HalcyonFrame.TitleBarConfig;
import com.jackmeng.halcyon.core.gui.gui_HalcyonFrame.TitledFrame;
import com.jackmeng.halcyon.core.util.use_ResourceFetcher;

import static com.jackmeng.halcyon.const_Lang.*;

import java.awt.*;

public class gui_UserProfile
    implements
    Runnable
{
  private final TitledFrame frame;
  private use_ClientProfile cl;

  public gui_UserProfile(use_ClientProfile cl)
  {
    this.cl = cl;
    JLabel e = new JLabel("Total time: " + Float.NaN  + " hours");
    JLabel f = new JLabel("Current session: " + Float.NaN + " minutes");

    cl.add_listener(x -> {
      e.setText("Total time: " + x.first + " hours");
      f.setText("Current Session: " + x.second + " minutes");
      return (Void) null;
    });

    JScrollPane jsp = new JScrollPane();

    jsp.add(e);
    jsp.add(f);

    frame = new TitledFrame(new TitleBarConfig(_lang(LANG_PROFILE) + cl.getUser_Name(),
        use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO),
        use_HalcyonCore.boldFont().deriveFont(const_Manager.PROGRAM_DEFAULT_FONT_SIZE),
        const_ColorManager.DEFAULT_RED_FG, const_ColorManager.DEFAULT_SOFT_GREEN_FG, const_ColorManager.DEFAULT_RED_FG,
        const_ColorManager.DEFAULT_YELLOW_FG, const_ColorManager.DEFAULT_GREEN_FG,
        const_ColorManager.DEFAULT_PINK_FG, const_ColorManager.DEFAULT_SOFT_GREEN_FG),
        const_Manager.FRAME_TITLEBAR_HEIGHT, jsp);
    frame.expose().setSize(new Dimension(400, 300));
  }

  @Override public void run()
  {
    frame.run();
  }
}

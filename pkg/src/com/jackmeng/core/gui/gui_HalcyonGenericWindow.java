package com.jackmeng.core.gui;

import javax.swing.*;

import com.jackmeng.use_HalcyonCore;
import com.jackmeng.core.gui.gui_HalcyonFrame.TitleBarConfig;
import com.jackmeng.core.gui.gui_HalcyonFrame.TitledFrame;

import java.awt.*;

public class gui_HalcyonGenericWindow
    implements
    Runnable
{
  protected final TitledFrame frame;
  protected JEditorPane jep;

  public gui_HalcyonGenericWindow(ImageIcon icon, String title, String content_HTML, Color r, JFrame parent)
  {
    JScrollPane jsp = new JScrollPane();

    jep = new JEditorPane("text/html", content_HTML);
    jep.setEditable(false);
    jep.setDragEnabled(false);
    jep.setPreferredSize(new Dimension(600, 400));
    jsp.getViewport().add(jep);

    frame = new TitledFrame(
        new TitleBarConfig(title, icon,
            use_HalcyonCore.boldFont().deriveFont(const_Manager.PROGRAM_DEFAULT_FONT_SIZE),
            const_ColorManager.DEFAULT_RED_FG, r, const_ColorManager.DEFAULT_RED_FG,
            const_ColorManager.DEFAULT_YELLOW_FG, const_ColorManager.DEFAULT_GREEN_FG,
            const_ColorManager.DEFAULT_PINK_FG, r),
        const_Manager.FRAME_TITLEBAR_HEIGHT, jsp);
    frame.expose().setLocationRelativeTo(parent);
    frame.expose().setAlwaysOnTop(true);
  }


  @Override public void run()
  {
    SwingUtilities.invokeLater(frame);
  }
}

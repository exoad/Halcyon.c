package com.jackmeng.halcyon.gui;

import com.jackmeng.halcyon.gui.gui_HalcyonFrame.TitleBarConfig;
import com.jackmeng.halcyon.gui.gui_HalcyonFrame.TitledFrame;
import com.jackmeng.halcyon.use_Halcyon;

import javax.swing.*;
import java.awt.*;

public class gui_HalcyonGenericWindow
    implements Runnable
{
  protected final TitledFrame frame;

  public gui_HalcyonGenericWindow(ImageIcon icon, String title, String content_HTML, Color r, JFrame parent)
  {
    JScrollPane jsp = new JScrollPane();

    JEditorPane jep = new JEditorPane("text/html", content_HTML);
    jep.setEditable(false);
    jep.setDragEnabled(false);
    jep.setPreferredSize(new Dimension(600, 400));
    jsp.getViewport().add(jep);

    frame = new TitledFrame(
        new TitleBarConfig(title, icon,
            use_Halcyon.boldFont().deriveFont(const_Manager.PROGRAM_DEFAULT_FONT_SIZE),
            const_ColorManager.DEFAULT_DARK_BG, r, const_ColorManager.DEFAULT_BG,
            const_ColorManager.DEFAULT_BG, const_ColorManager.DEFAULT_BG, const_ColorManager.DEFAULT_BG, r),
        const_Manager.FRAME_TITLEBAR_HEIGHT, jsp);
    frame.expose().setLocationRelativeTo(parent);
    frame.expose().setAlwaysOnTop(true);
  }

  @Override
  public void run()
  {
    SwingUtilities.invokeLater(frame);
  }
}

package com.jackmeng.halcyon.gui.childs;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.gui_HalcyonGenericWindow;
import com.jackmeng.util.use_ResourceFetcher;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public class dgui_HalcyonConfirmWindow extends gui_HalcyonGenericWindow
{

  public dgui_HalcyonConfirmWindow(String title, String content_HTML, Runnable onConfirm, Runnable onDeny,
      JFrame parent)
  {
    super(use_ResourceFetcher.fetcher.getFromAsImageIcon("resources/app/oh_no.png"), title, content_HTML,
        const_ColorManager.DEFAULT_YELLOW_FG, parent);

    JButton confirm = new JButton(_lang(LANG_CONFIRM_WINDOW_CONFIRM_BUTTON_OK));
    confirm.setOpaque(true);
    confirm.setBackground(const_ColorManager.DEFAULT_SOFT_GREEN_FG);
    confirm.setForeground(const_ColorManager.DEFAULT_DARK_BG_2);

    JButton deny = new JButton(_lang(LANG_CONFIRM_WINDOW_CONFIRM_BUTTON_DENY));
    deny.setOpaque(true);
    deny.setBackground(const_ColorManager.DEFAULT_RED_FG);
    deny.setForeground(const_ColorManager.DEFAULT_DARK_BG_2);

  }
}

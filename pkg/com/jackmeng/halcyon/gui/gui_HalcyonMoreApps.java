package com.jackmeng.halcyon.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

import com.jackmeng.Halcyon;
import com.jackmeng.halcyon.const_Global;
import com.jackmeng.halcyon.apps.evnt_WindowFocusAdapter;
import com.jackmeng.halcyon.apps.impl_App;
import com.jackmeng.halcyon.apps.impl_HalcyonRefreshable;
import com.jackmeng.ploogin.impl_Ploogin;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public class gui_HalcyonMoreApps
    implements Runnable, impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<impl_App>>> {

  private JFrame frame;
  private JScrollPane jsp;
  private JPanel panel;
  private int pX, pY;

  public gui_HalcyonMoreApps() {
    frame = new JFrame(_lang(LANG_MOREAPPS_TITLE));
    frame.setIconImage(
        use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO).getImage());
    frame.setPreferredSize(new Dimension(const_Manager.GUI_MOREAPPS_WIDTH, const_Manager.GUI_MOREAPPS_HEIGHT));
    frame.setUndecorated(true);
    frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, const_ColorManager.DEFAULT_DARK_BG));
    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {
        pX = me.getX();
        pY = me.getY();
      }

      @Override
      public void mouseDragged(MouseEvent me) {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });
    frame.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent me) {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });
    frame.addWindowFocusListener(new evnt_WindowFocusAdapter() {
      @Override
      public void windowLostFocus(WindowEvent e) {
        frame.dispose();
      }
    });
    gui_HalcyonFrame.TitledFrame.cr.registerComponent(frame);
    frame.addMouseListener(gui_HalcyonFrame.TitledFrame.cr);
    const_Global.APPS_POOL.addRefreshable(this);

    panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder());

    panel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {
        pX = me.getX();
        pY = me.getY();
      }

      @Override
      public void mouseDragged(MouseEvent me) {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });
    panel.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent me) {
        frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
      }
    });
    panel.setLayout(new GridLayout());

    jsp = new JScrollPane(panel);
    jsp.getViewport().setLayout(new FlowLayout(FlowLayout.CENTER));
    jsp.setBorder(BorderFactory.createEmptyBorder());

    frame.getContentPane().add(jsp);
  }

  private JPanel get_PlooginEntry(impl_App x) {
    JPanel main = new JPanel();
    main.setLayout(new FlowLayout(FlowLayout.CENTER));
    JButton action = new JButton();
    action.addActionListener(e -> x.run());
    action.setRolloverEnabled(false);
    action.setContentAreaFilled(false);
    if (x.icon() == null)
      action.setText(x.id());
    else
      action.setIcon(x.icon());
    if (x instanceof impl_Ploogin) {
      impl_Ploogin x2 = (impl_Ploogin) x;
      action.setToolTipText("<html><strong>Ploogin: " + x2.id() + "</strong><br>Author: " + x2.author()
          + "<br>Description: " + x2.description() + "</html>");
    } else {
      action.setToolTipText("<html><strong>App: " + x.id() + "</strong><br>hash: " + x.hashCode() + "</html>");
    }
    main.add(action);
    return main;
  }

  @Override
  public void run() {
    frame.pack();
    frame.setLocationRelativeTo(Halcyon.main.expose());
    frame.setVisible(true);
  }

  @Override
  public void refresh(struct_Pair<Optional<String>, Optional<impl_App>> refreshed) {
    refreshed.second.ifPresent(x -> panel.add(get_PlooginEntry(x)));
  }

  @Override
  public void dry_refresh() {
    // TODO Auto-generated method stub

  }
}

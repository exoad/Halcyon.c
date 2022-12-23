package com.jackmeng.core.gui;

import javax.swing.*;

import com.jackmeng.core.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.core.util.use_Struct.*;
import com.jackmeng.tailwind.use_TailwindTrack;

import java.awt.image.*;

import static com.jackmeng.const_Lang.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class gui_HalcyonDisplayable
    extends
    JWindow
    implements
    Runnable
{
  private static final class halcyondisplayable_PanelMain
      extends
      JPanel
      implements
      evnt_SelectPlaylistTrack
  {
    private transient BufferedImage current;
    private boolean x = true;

    public void draw_(boolean e)
    {
      this.x = e;
    }

    public boolean drawing()
    {
      return this.x;
    }

    @Override public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      if (x)
      {
        Graphics2D g2 = (Graphics2D) g;

        g2.dispose();
      }
    }

    @Override public void forYou(use_TailwindTrack e)
    {

    }

  }

  private halcyondisplayable_PanelMain pane;

  public gui_HalcyonDisplayable()
  {
    pane = new halcyondisplayable_PanelMain();
    pane.setPreferredSize(use_GuiUtil.screen_space());
    setLayout(new BorderLayout());
    setSize(use_GuiUtil.screen_space());
    add(pane, BorderLayout.CENTER);
    addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e)
      {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
          List< struct_Pair< Object, Consumer< ActionEvent > > > rr = new ArrayList<>();
          rr.add(new struct_Pair<>(_lang(LANG_QUIT),
              x -> {
                gui_HalcyonDisplayable.this.setVisible(false);
                pane.draw_(false);
              }));
          use_GuiUtil.make_PopupMenu("Displayability", rr).show(gui_HalcyonDisplayable.this, e.getX() + 10,
              e.getY() + 10);
        }
      }
    });
  }

  public halcyondisplayable_PanelMain expose_internal()
  {
    return pane;
  }

  @Override public void run()
  {
    pack();
    setVisible(true);
  }
}

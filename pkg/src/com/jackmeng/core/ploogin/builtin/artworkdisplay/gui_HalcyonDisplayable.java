package com.jackmeng.core.ploogin.builtin.artworkdisplay;

import javax.swing.*;

import com.jackmeng.const_Core;
import com.jackmeng.core.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.core.abst.evnt_WindowFocusAdapter;
import com.jackmeng.core.gui.use_GuiUtil;
import com.jackmeng.core.util.use_Image;
import com.jackmeng.core.util.use_ImgStrat;
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

    public halcyondisplayable_PanelMain()
    {
      setOpaque(false);
      const_Core.SELECTION_LISTENERS.add_listener(this);
    }

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
      if (x && current != null)
      {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
        g2.drawImage(use_Image.subimage_resizing(getWidth(), getHeight(), current), null, null);
        g2.dispose();
      }
    }

    @Override public void forYou(use_TailwindTrack e)
    {
      current = e.get_artwork();
    }
  }

  private halcyondisplayable_PanelMain pane;

  public gui_HalcyonDisplayable()
  {

    pane = new halcyondisplayable_PanelMain();
    pane.setPreferredSize(use_GuiUtil.screen_space());

    JPanel wrapperPane = new JPanel();
    wrapperPane.setLayout(new BorderLayout());
    wrapperPane.setOpaque(false);

    setLayout(new OverlayLayout(getContentPane()));
    setSize(use_GuiUtil.screen_space());

    wrapperPane.add(pane, BorderLayout.CENTER);

    add(wrapperPane);
    add(use_ImgStrat.acquireOpLayer(new use_ImgStrat.imgstrat_BlurhashBlur(3, 4, 1.0D), wrapperPane));

    addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e)
      {
        if (e.getButton() == MouseEvent.BUTTON3)
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
    addWindowFocusListener(new evnt_WindowFocusAdapter() {
      @Override public void windowLostFocus(WindowEvent e)
      {
        setVisible(false);
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

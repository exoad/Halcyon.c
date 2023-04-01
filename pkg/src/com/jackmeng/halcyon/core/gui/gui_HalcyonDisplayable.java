package com.jackmeng.halcyon.core.gui;

import javax.swing.*;

import com.jackmeng.halcyon.const_Core;
import com.jackmeng.halcyon.core.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.core.abst.evnt_WindowFocusAdapter;
import com.jackmeng.halcyon.core.util.use_Image;
import com.jackmeng.halcyon.core.util.use_ImgStrat;
import com.jackmeng.halcyon.core.util.use_ResourceFetcher;
import com.jackmeng.halcyon.core.util.use_Struct.*;
import com.jackmeng.halcyon.tailwind.use_TailwindTrack;

import java.awt.image.*;

import static com.jackmeng.halcyon.const_Lang.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class gui_HalcyonDisplayable
    extends
    JWindow
    implements
    Runnable,
    evnt_SelectPlaylistTrack
{
  private static final class halcyondisplayable_PanelMain
      extends
      Canvas
      implements
      evnt_SelectPlaylistTrack
  {
    private transient BufferedImage current;
    private boolean r = true;

    public halcyondisplayable_PanelMain()
    {
      const_Core.SELECTION_LISTENERS.add_listener(this);
      createBufferStrategy(3);
    }

    public void draw_(boolean e)
    {
      this.r = e;
    }

    /*------------------------ /
    / public boolean drawing() /
    / {                        /
    /   return this.r;         /
    / }                        /
    /                          /
    /-------------------------*/
    
    @Override public void paint(Graphics g)
    {
      super.paint(g);
      if (r && current != null)
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
      SwingUtilities.invokeLater(() -> repaint(100L));
    }
  }

  private final halcyondisplayable_PanelMain pane;
  private final JLabel smallArtworkHolder;

  public gui_HalcyonDisplayable()
  {

    pane = new halcyondisplayable_PanelMain();
    pane.setPreferredSize(use_GuiUtil.screen_space());

    setLayout(new OverlayLayout(getContentPane()));
    setSize(use_GuiUtil.screen_space());

    JPanel smallerLabelling = new JPanel();
    smallerLabelling.setPreferredSize(getSize());
    smallerLabelling.setLayout(new BorderLayout());
    smallerLabelling.setOpaque(false);

    smallArtworkHolder = new JLabel();
    smallArtworkHolder.setIcon(use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_DISK_ICON));
    smallArtworkHolder.setPreferredSize(new Dimension(500, 500));
    smallArtworkHolder.setOpaque(false);

    smallerLabelling.add(smallArtworkHolder, BorderLayout.CENTER);

    add(smallerLabelling);
    add(use_ImgStrat.acquireOpLayer(new use_ImgStrat.imgstrat_BlurhashBlur(3, 4, 1.0D), pane));

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

  @Override public void forYou(use_TailwindTrack e)
  {
    SwingUtilities.invokeLater(() -> {
      smallArtworkHolder.setIcon(new ImageIcon(use_Image.subimage_resizing(500, 500, e.get_artwork())));
    });
  }
}

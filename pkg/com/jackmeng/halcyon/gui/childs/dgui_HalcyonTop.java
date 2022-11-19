package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;
import javax.swing.plaf.LayerUI;

import java.awt.*;
import java.awt.image.*;
import com.jackmeng.halcyon.const_Global;
import com.jackmeng.halcyon.const_MUTableKeys;
import com.jackmeng.halcyon.apps.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.halcyon.gui.dgui_ImgLabel;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Chronos;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_ImgStrat.*;

public class dgui_HalcyonTop
    extends JPanel
    implements evnt_SelectPlaylistTrack
{

  public static final class halcyonTop_Info
      extends JPanel
      implements evnt_SelectPlaylistTrack
  {
    private dgui_ImgLabel artwork;
    private JPanel infoDisplayer;
    private JLabel mainTitle, miscTitle, otherTitle;

    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          (const_Manager.DGUI_TOP) / 2));
      setLayout(new GridBagLayout());

      infoDisplayer = new JPanel();
      infoDisplayer.setLayout(new BoxLayout(infoDisplayer, BoxLayout.Y_AXIS));
      infoDisplayer.setPreferredSize(new Dimension());
      infoDisplayer.setBorder(BorderFactory.createEmptyBorder());

      artwork = new dgui_ImgLabel(null, false);
      mainTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_TITLE.value);

      setOpaque(true);
      setBackground(const_ColorManager.DEFAULT_BLUE_FG);
      const_Global.SELECTION_LISTENERS.add_listener(this);
    }

    @Override
    public void forYou(use_TailwindTrack e)
    {
      setToolTipText(use_ResourceFetcher.fetcher.load_n_parse_hll(const_ResourceManager.HLL_HALCYONTOP_TOOLTIP,
          use_Color.colorToHex(const_ColorManager.DEFAULT_GREEN_FG),
          e.get(tailwindtrack_Tags.MEDIA_TITLE),
          use_Color.colorToHex(const_ColorManager.DEFAULT_PINK_FG),
          e.get(tailwindtrack_Tags.MEDIA_ARTIST),
          e.get(tailwindtrack_Tags.MEDIA_BITRATE),
          e.get(tailwindtrack_Tags.MEDIA_SAMPLERATE),
          use_Chronos.format_sec((Integer) e.get(tailwindtrack_Tags.MEDIA_DURATION))));
      /*------------------------------------------------------------------------------------------------------------- /
      / infoDisplayer.setToolTipText("<html><body><p style=\"text-align: left;\"><span style=\"color: "               /
      /     + use_Color.colorToHex(const_ColorManager.DEFAULT_GREEN_FG)                                               /
      /     + ";font-size: 14px;\"><nobr><strong>" + e.get(tailwindtrack_Tags.MEDIA_TITLE)                            /
      /     + "</strong></nobr></span></p><p style=\"text-align: left;\"><span style=\"color: "                       /
      /     + use_Color.colorToHex(const_ColorManager.DEFAULT_PINK_FG) + ";font-size: 11px\">"                        /
      /     + e.get(tailwindtrack_Tags.MEDIA_ARTIST)                                                                  /
      /     + "</span></p><p style=\"text-align: left;\"><span style=\"color: #ffffffff;font-size:10px\">"            /
      /     + e.get(tailwindtrack_Tags.MEDIA_BITRATE) + "kbps " + e.get(tailwindtrack_Tags.MEDIA_SAMPLERATE) + "kHz " /
      /     + use_Chronos.format_sec((Integer) e.get(tailwindtrack_Tags.MEDIA_DURATION))                              /
      /     + "</span></p></body></html>");                                                                           /
      /--------------------------------------------------------------------------------------------------------------*/
    }
  }

  public static final class halcyonTop_Buttons
      extends JPanel
  {
    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, (const_Manager.DGUI_TOP) / 2));
      setOpaque(true);
      setBackground(const_ColorManager.DEFAULT_RED_FG);
    }
  }

  private JLayer< Component > blurBp;
  private transient BufferedImage bi;
  private JPanel bgPanel;

  public dgui_HalcyonTop()
  {
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_TOP));
    setMinimumSize(getPreferredSize());
    setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    setLayout(new OverlayLayout(this));

    JPanel copy = new JPanel();
    copy.setPreferredSize(getPreferredSize());
    copy.setOpaque(false);
    copy.setLayout(new BorderLayout());
    copy.add(new halcyonTop_Info(), BorderLayout.NORTH);
    copy.add(new halcyonTop_Buttons(), BorderLayout.SOUTH);
    copy.setAlignmentX(Component.CENTER_ALIGNMENT);

    bgPanel = new JPanel();
    bgPanel.setPreferredSize(getPreferredSize());

    blurBp = new JLayer<>(bgPanel, new LayerUI<>() {
      private transient imgstrat_BlurhashBlur blur = new imgstrat_BlurhashBlur(2, 3, 1.0D);

      @Override
      public void paint(Graphics g, JComponent comp)
      {
        if (bi != null)
        {
          if (comp.getWidth() == 0 || comp.getHeight() == 0)
            return;

          BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
          Graphics2D ig2 = img.createGraphics();
          ig2.setClip(g.getClip());
          super.paint(ig2, comp);
          ig2.dispose();
          Graphics2D g2 = (Graphics2D) g;
          g2.drawImage(img, blur, 0, 0);
          g2.dispose();
        }
        g.dispose();
      }
    });

    add(copy);
    add(blurBp);

    const_Global.SELECTION_LISTENERS.add_listener(this);
  }

  @Override
  public void forYou(use_TailwindTrack e)
  {
    pstream.log.warn("TOP_REFFED: " + e.hashCode());
    bi = e.has_artwork() ? e.get_artwork() : null;
    blurBp.repaint(15L);
  }

}

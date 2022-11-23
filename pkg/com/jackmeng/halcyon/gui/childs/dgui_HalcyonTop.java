package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;
import java.awt.*;

import com.jackmeng.const_Global;
import com.jackmeng.halcyon.const_MUTableKeys;
import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.sys.use_Chronos;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.awt.image.*;

public class dgui_HalcyonTop
    extends JPanel
    implements evnt_SelectPlaylistTrack
{

  public static final class halcyonTop_Info
      extends JPanel
      implements evnt_SelectPlaylistTrack
  {
    private static class halcyonTop_Info_Artworklabel extends JPanel
    {
      private transient BufferedImage img;
      private boolean retain;
      private int x_width, x_height;

      public halcyonTop_Info_Artworklabel(boolean retainOnNull, struct_Pair< Integer, Integer > resizeSize)
      {
        this.retain = retainOnNull;
        this.x_width = Math.abs(resizeSize.first);
        this.x_height = Math.abs(resizeSize.second);
      }

      public void setIMG(BufferedImage i)
      {
        this.img = i;
      }

      @Override
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        if (img != null)
        {
          Graphics2D g2 = (Graphics2D) g;
          g2.drawImage((img.getWidth() > img.getHeight()
              ? img.getSubimage(img.getWidth() / 2 - img.getHeight() / 2, 0, img.getHeight(), img.getHeight())
              : img.getSubimage(0, img.getHeight() / 2 - img.getWidth() / 2, img.getWidth(), img.getWidth()))
                  .getScaledInstance(x_width, x_height,
                      Image.SCALE_SMOOTH),
              null, null);
          g2.dispose();
        }
        else if (!retain)
          g.clearRect(0, 0, getSize().width, getSize().height);
        g.dispose();
      }

    }

    private halcyonTop_Info_Artworklabel artwork;
    private JPanel infoDisplayer;
    private JLabel mainTitle, miscTitle, otherTitle;

    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          (const_Manager.DGUI_TOP + 50) / 2));
      setLayout(new GridLayout(1, 3, 15, ((const_Manager.DGUI_TOP + 50) / 2) / 2));
      setOpaque(false);

      infoDisplayer = new JPanel();
      infoDisplayer.setLayout(new BoxLayout(infoDisplayer, BoxLayout.Y_AXIS));
      infoDisplayer.setPreferredSize(new Dimension());

      mainTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_TITLE.value);
      mainTitle.setFont(use_Halcyon.boldFont().deriveFont(22F));
      mainTitle.setForeground(const_ColorManager.DEFAULT_GREEN_FG);
      mainTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      mainTitle.setOpaque(false);

      miscTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_ARTIST.value);
      miscTitle.setFont(use_Halcyon.regularFont().deriveFont(14.5F));
      miscTitle.setForeground(Color.WHITE);
      miscTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      miscTitle.setOpaque(false);

      otherTitle = new JLabel("0kpbs | 0kHz | 00:00:00");
      otherTitle.setFont(use_Halcyon.regularFont().deriveFont(12F));
      otherTitle.setForeground(const_ColorManager.DEFAULT_GRAY_FG);
      otherTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      otherTitle.setOpaque(false);

      infoDisplayer.add(mainTitle);
      infoDisplayer.add(miscTitle);
      infoDisplayer.add(otherTitle);
      infoDisplayer.setOpaque(false);

      artwork = new halcyonTop_Info_Artworklabel(false, const_MUTableKeys.top_artwork_wxh);
      artwork.setIMG((BufferedImage) tailwindtrack_Tags.MEDIA_ART.value);
      artwork.setOpaque(false);

      add(artwork);
      add(infoDisplayer);

      setOpaque(true);
      setBackground(const_ColorManager.DEFAULT_BLUE_FG);

      const_Global.SELECTION_LISTENERS.add_listener(this);
    }

    @Override
    public void forYou(use_TailwindTrack e)
    {
      SwingUtilities.invokeLater(() -> {
        infoDisplayer
            .setToolTipText(use_ResourceFetcher.fetcher.load_n_parse_hll(const_ResourceManager.HLL_HALCYONTOP_TOOLTIP,
                use_Color.colorToHex(const_ColorManager.DEFAULT_GREEN_FG),
                e.get(tailwindtrack_Tags.MEDIA_TITLE),
                use_Color.colorToHex(const_ColorManager.DEFAULT_PINK_FG),
                e.get(tailwindtrack_Tags.MEDIA_ARTIST),
                e.get(tailwindtrack_Tags.MEDIA_BITRATE),
                e.get(tailwindtrack_Tags.MEDIA_SAMPLERATE),
                use_Chronos.format_sec((Integer) e.get(tailwindtrack_Tags.MEDIA_DURATION))));
        mainTitle.setText((String) e.get(tailwindtrack_Tags.MEDIA_TITLE));
        miscTitle.setText((String) e.get(tailwindtrack_Tags.MEDIA_ARTIST));
        otherTitle.setText((String) e.get(tailwindtrack_Tags.MEDIA_BITRATE) + "kpbs | "
            + e.get(tailwindtrack_Tags.MEDIA_SAMPLERATE) + "kHz | "
            + use_Chronos.format_sec((Integer) e.get(tailwindtrack_Tags.MEDIA_DURATION)));
        artwork.setIMG(e.get_artwork());
        artwork.repaint(30L);
      });

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
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, (const_Manager.DGUI_TOP - 50) / 2));
      setOpaque(false);
      /*------------------------------------------------- /
      / setOpaque(true);                                  /
      / setBackground(const_ColorManager.DEFAULT_RED_FG); /
      /--------------------------------------------------*/
    }
  }

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

    bgPanel = new JPanel() {
      @Override
      public void paintComponent(Graphics g)
      {
        g.dispose();
      }
    };
    bgPanel.setPreferredSize(getPreferredSize());

    add(copy);
    add(bgPanel);

    const_Global.SELECTION_LISTENERS.add_listener(this);
  }

  @Override
  public void forYou(use_TailwindTrack e)
  {
  }
}

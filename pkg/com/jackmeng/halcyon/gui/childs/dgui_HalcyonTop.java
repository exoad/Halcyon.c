package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;
import javax.swing.plaf.LayerUI;

import java.awt.*;
import java.awt.image.*;
import java.util.WeakHashMap;

import com.jackmeng.halcyon.const_Global;
import com.jackmeng.halcyon.apps.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.halcyon.gui.dgui_ImgLabel;
import com.jackmeng.sys.use_Chronos;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;
import com.jackmeng.util.use_ImgStrat;
import com.jackmeng.util.use_ResourceFetcher;

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
      setOpaque(false);

      infoDisplayer = new JPanel();
      infoDisplayer.setLayout(new BoxLayout(infoDisplayer, BoxLayout.Y_AXIS));
      infoDisplayer.setPreferredSize(new Dimension());
      infoDisplayer.setBorder(BorderFactory.createEmptyBorder());

      artwork = new dgui_ImgLabel(null, false);
      mainTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_TITLE.value);

      /*-------------------------------------------------- /
      / setOpaque(true);                                   /
      / setBackground(const_ColorManager.DEFAULT_BLUE_FG); /
      /---------------------------------------------------*/
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
      setOpaque(false);
      /*------------------------------------------------- /
      / setOpaque(true);                                  /
      / setBackground(const_ColorManager.DEFAULT_RED_FG); /
      /--------------------------------------------------*/
    }
  }

  private JLayer< Component > blurBp;
  private transient BufferedImage bi;
  private JPanel bgPanel;
  private transient Color accentColor;

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
        if (accentColor != null)
        {
          g.setColor(accentColor);
          g.fillRect(0, 0, getSize().width, getSize().height);
        }
        else
        {
          g.clearRect(0, 0, getSize().width, getSize().height);
        }
        g.dispose();
      }
    };
    bgPanel.setPreferredSize(getPreferredSize());

    blurBp = new JLayer<>(bgPanel, new LayerUI<>() {
      /*------------------------------------------------------------------------------------------------- /
      / private transient BufferedImageOp op = new use_ImageStrat;                                        /
      / private transient WeakHashMap<JComponent, BufferedImage> lazyImage_Cache = new WeakHashMap<>(10); /
      /                                                                                                   /
      / @Override                                                                                         /
      / public void paint(Graphics g, JComponent comp)                                                    /
      / {                                                                                                 /
      /   if (bi != null)                                                                                 /
      /   {                                                                                               /
      /     if (comp.getWidth() == 0 || comp.getHeight() == 0)                                            /
      /       return;                                                                                     /
      /     BufferedImage blur = lazyImage_Cache.get(comp);                                               /
      /     if(blur == null || blur.getWidth() != bi.getWidth() || blur.getHeight() != bi.getHeight())    /
      /     {                                                                                             /
      /       blur = use_Image.compat_Img(bi);                                                            /
      /       lazyImage_Cache.put(comp, blur);                                                            /
      /     }                                                                                             /
      /     Graphics2D blr = blur.createGraphics();                                                       /
      /     blr.drawImage(bi, op, 0, 0);                                                                  /
      /     blr.dispose();                                                                                /
      /   }                                                                                               /
      /   g.dispose();                                                                                    /
      / }                                                                                                 /
      /--------------------------------------------------------------------------------------------------*/

      @Override
      public void paint(Graphics g, JComponent comp)
      {

      }
    });

    add(copy);
    add(bgPanel);

    const_Global.SELECTION_LISTENERS.add_listener(this);
  }

  @Override
  public void forYou(use_TailwindTrack e)
  {
    bi = (BufferedImage) e.get(tailwindtrack_Tags.MEDIA_ART);
    accentColor = (Color) e.get(tailwindtrack_Tags.MEDIA_ART_COLOR_PRIMA);
    bgPanel.repaint(15L);
  }
}

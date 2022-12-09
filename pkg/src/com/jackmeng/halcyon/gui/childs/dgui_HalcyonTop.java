package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

import com.jackmeng.const_Global;
import com.jackmeng.halcyon.const_MUTableKeys;
import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.abst.use_MastaTemp;
import com.jackmeng.halcyon.abst.impl_Callback.callback_Specific;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.halcyon.gui.gui_LazyLoadingPanel;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Chronos;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;
import com.jackmeng.util.use_ResourceFetcher;

public class dgui_HalcyonTop
    extends JPanel
    implements evnt_SelectPlaylistTrack
{

  public static final class halcyonTop_Info
      extends gui_LazyLoadingPanel
      implements evnt_SelectPlaylistTrack
  {

    private JPanel infoDisplayer;
    private JLabel mainTitle, miscTitle, otherTitle, artwork;

    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          (const_Manager.DGUI_TOP + 50) / 2));
      setLayout(new GridLayout(1, 2, 15, ((const_Manager.DGUI_TOP + 50) / 2) / 2));
      setOpaque(false);

      infoDisplayer = new JPanel();
      infoDisplayer.setLayout(new BoxLayout(infoDisplayer, BoxLayout.Y_AXIS));

      mainTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_TITLE.value);
      mainTitle.setFont(use_Halcyon.boldFont().deriveFont(20F));
      mainTitle.setForeground(const_ColorManager.DEFAULT_GREEN_FG);
      mainTitle.setOpaque(false);
      mainTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      mainTitle.setVerticalAlignment(SwingConstants.CENTER);

      miscTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_ARTIST.value);
      miscTitle.setFont(use_Halcyon.regularFont().deriveFont(15.5F));
      miscTitle.setForeground(Color.WHITE);
      miscTitle.setOpaque(false);
      miscTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      miscTitle.setVerticalAlignment(SwingConstants.CENTER);

      otherTitle = new JLabel("0kpbs | 0kHz | 00:00:00");
      otherTitle.setFont(use_Halcyon.regularFont().deriveFont(13F));
      otherTitle.setForeground(Color.WHITE);
      otherTitle.setOpaque(false);
      otherTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      otherTitle.setVerticalAlignment(SwingConstants.CENTER);

      infoDisplayer.add(Box.createVerticalGlue());
      infoDisplayer.add(mainTitle);
      infoDisplayer.add(miscTitle);
      infoDisplayer.add(otherTitle);
      infoDisplayer.add(Box.createVerticalGlue());
      infoDisplayer.setOpaque(false);

      artwork = new JLabel(new ImageIcon((BufferedImage) tailwindtrack_Tags.MEDIA_ART.value));
      artwork.setHorizontalAlignment(SwingConstants.CENTER);
      artwork.setVerticalAlignment(SwingConstants.CENTER);

      add(artwork);
      add(infoDisplayer);

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
        final BufferedImage img = use_Image.compat_Img(e.get_artwork());
        artwork.setIcon(new ImageIcon((img.getWidth() > img.getHeight()
            ? img.getSubimage(img.getWidth() / 2 - img.getHeight() / 2, 0, img.getHeight(), img.getHeight())
            : img.getSubimage(0, img.getHeight() / 2 - img.getWidth() / 2, img.getWidth(), img.getWidth()))
                .getScaledInstance(const_MUTableKeys.top_artwork_wxh.first, const_MUTableKeys.top_artwork_wxh.second,
                    Image.SCALE_AREA_AVERAGING)));
        pstream.log.warn(use_Color.colorToHex(use_Image.accents_color_1(img).get(0)));
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

    @Override
    protected void constr()
    {

    }
  }

  public static final class halcyonTop_Buttons
      extends
      JPanel
  {

    private static class buttons_Funcs
        extends
        JButton
    {

      private ImageIcon normal, rollover;
      private boolean rolled = false;

      public buttons_Funcs(ImageIcon normal, ImageIcon rollover, callback_Specific< Boolean > rolloverGuard)
      {
        assert normal != null;
        this.normal = normal;
        this.rollover = rollover;
        setBorder(null); // REST ASSURED YESSIR! o7
        setContentAreaFilled(false);
        setRolloverEnabled(false);
        setBorderPainted(false);
        setBackground(null);
        if (rollover != null)
        {
          addActionListener(x -> {
            if (rolloverGuard != null && rolloverGuard.call(rolled))
            {
              rolled = !rolled;
              repaint(100L);
            }
          });
        }
        repaint();
      }

      @Override
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(rolled && rollover != null ? rollover.getImage() : normal.getImage(), null, this);
        g2.dispose();
      }
    }

    private final buttons_Funcs playPause_Button, trackInfo_Button, nextTrack_Button, lastTrack_Button,
        loopTrack_Button,
        shufflePlaystyle_Button, likeTrack_Button;

    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, (const_Manager.DGUI_TOP - 50) / 2));
      setOpaque(true);

      setLayout(new FlowLayout(FlowLayout.LEFT));
      playPause_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_PLAY_TRACK),
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_PAUSE_TRACK),
          use_MastaTemp::returnTrue);
      trackInfo_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_TRACK_INFORMATION), null,
          use_MastaTemp::returnTrue);
      nextTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_NEXT_TRACK), null,
          use_MastaTemp::returnTrue);
      lastTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_PREVIOUS_TRACK), null,
          use_MastaTemp::returnTrue);
      loopTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_LOOP_PLAYSTYLE), null,
          use_MastaTemp::returnTrue);
      shufflePlaystyle_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_SHUFFLE_PLAYSTYLE), null,
          use_MastaTemp::returnTrue);
      likeTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_UNLIKE_TRACK),
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.CTRL_BUTTON_LIKE_TRACK),
          use_MastaTemp::returnTrue);

      add(trackInfo_Button);
      add(likeTrack_Button);
      add(lastTrack_Button);
      add(playPause_Button);
      add(nextTrack_Button);
      add(shufflePlaystyle_Button);
      add(loopTrack_Button);
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

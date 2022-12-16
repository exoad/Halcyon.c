package com.jackmeng.core.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicProgressBarUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import com.jackmeng.const_Core;
import com.jackmeng.core.const_MUTableKeys;
import com.jackmeng.core.use_HalcyonCore;
import com.jackmeng.core.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.core.abst.use_MastaTemp;
import com.jackmeng.core.abst.impl_Callback.callback_Specific;
import com.jackmeng.sys.pstream;
import com.jackmeng.tailwind.evnt_TailwindStatus;
import com.jackmeng.tailwind.use_Tailwind.tailwind_Status;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.use_Chronos;
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
      mainTitle.setFont(use_HalcyonCore.boldFont().deriveFont(20F));
      mainTitle.setForeground(const_ColorManager.DEFAULT_GREEN_FG);
      mainTitle.setOpaque(false);
      mainTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      mainTitle.setVerticalAlignment(SwingConstants.CENTER);

      miscTitle = new JLabel((String) tailwindtrack_Tags.MEDIA_ARTIST.value);
      miscTitle.setFont(use_HalcyonCore.regularFont().deriveFont(15.5F));
      miscTitle.setForeground(Color.WHITE);
      miscTitle.setOpaque(false);
      miscTitle.setAlignmentY(Component.CENTER_ALIGNMENT);
      miscTitle.setVerticalAlignment(SwingConstants.CENTER);

      otherTitle = new JLabel("0kpbs | 0kHz | 00:00:00");
      otherTitle.setFont(use_HalcyonCore.regularFont().deriveFont(13F));
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

      const_Core.SELECTION_LISTENERS.add_listener(this);
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
      implements
      evnt_TailwindStatus
  {

    private static class buttons_Funcs
        extends
        JButton
    {
      private ImageIcon a, b;
      private boolean rolled = false;

      public buttons_Funcs(ImageIcon normal, ImageIcon rollover, callback_Specific< Boolean > rolloverGuard,
          ActionListener e)
      {
        assert normal != null;
        this.a = normal;
        this.b = rollover;
        setBorder(null); // REST ASSURED YESSIR! o7
        setContentAreaFilled(false);
        setRolloverEnabled(false);
        setBorderPainted(false);
        setIcon(normal);
        if (rollover != null)
        {
          addActionListener(x -> {
            if (rolloverGuard != null && rolloverGuard.call(rolled))
            {
              rolled = !rolled;
              setIcon(rolled && rollover != null ? rollover : normal);
            }
          });
        }
      }

      public void set_rolled(boolean isRolled)
      {
        rolled = isRolled;
        setIcon(isRolled ? b : a);
      }

      /*------------------------------------------------------------------------------------------------- /
      / @Override                                                                                         /
      / public void paintComponent(Graphics g)                                                            /
      / {                                                                                                 /
      /   super.paintComponent(g);                                                                        /
      /   Graphics2D g2 = (Graphics2D) g;                                                                 /
      /   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        /
      /   g2.drawImage(rolled && rollover != null ? rollover.getImage() : normal.getImage(), null, this); /
      /   g2.dispose();                                                                                   /
      / }                                                                                                 /
      /--------------------------------------------------------------------------------------------------*/
    }

    private final buttons_Funcs playPause_Button, trackInfo_Button, nextTrack_Button, lastTrack_Button,
        loopTrack_Button,
        shufflePlaystyle_Button, likeTrack_Button;
    protected final JSlider timeSlider, volumeSlider;
    protected FlowLayout mastaJP_layout = new FlowLayout(FlowLayout.CENTER, 15, 0);

    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, (const_Manager.DGUI_TOP - 50) / 2));
      setMinimumSize(getPreferredSize());
      setLayout(new GridLayout(2, 1));

      JPanel mastaJP = new JPanel();
      mastaJP.setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height / 2));
      mastaJP.setMinimumSize(mastaJP.getPreferredSize());
      mastaJP.setLayout(mastaJP_layout);

      playPause_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_PLAY_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH + 15, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH + 15),
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_PAUSE_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH + 15, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH + 15),
          x -> {
            return x && const_Core.PLAYER.expose().state() == tailwind_Status.PLAYING;
          }, use_MastaTemp::doNothing);

      trackInfo_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_TRACK_INFORMATION,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          null,
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);
      nextTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_NEXT_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          null,
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);
      lastTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_PREVIOUS_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          null,
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);
      loopTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_LOOP_PLAYSTYLE,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          null,
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);
      shufflePlaystyle_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_SHUFFLE_PLAYSTYLE,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          null,
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);
      likeTrack_Button = new buttons_Funcs(
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_UNLIKE_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          use_ResourceFetcher.fetcher.rz_fromImageIcon(const_ResourceManager.CTRL_BUTTON_LIKE_TRACK,
              const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH, const_Manager.DGUI_TOP_CTRL_BUTTONS_DEF_WXH),
          use_MastaTemp::returnTrue, use_MastaTemp::doNothing);

      volumeSlider = new JSlider(0, 100);
      volumeSlider.setPaintTicks(true);
      volumeSlider.setPreferredSize(new Dimension(110, 15));
      volumeSlider.setMinimumSize(volumeSlider.getPreferredSize());
      volumeSlider.setFocusable(false);
      volumeSlider.setForeground(const_ColorManager.DEFAULT_GREEN_FG);

      JProgressBar pb = new JProgressBar();
      pb.setStringPainted(true);
      pb.setPreferredSize(new Dimension(100, 15));
      pb.setForeground(const_ColorManager.DEFAULT_GREEN_FG);
      pb.setUI(new BasicProgressBarUI() {
        @Override
        protected Color getSelectionBackground()
        {
          return const_ColorManager.DEFAULT_GREEN_FG;
        }
      });
      pb.setModel(volumeSlider.getModel());

      JPanel volumeSlider_alignment = new JPanel();
      volumeSlider_alignment.setLayout(new BorderLayout());
      volumeSlider_alignment.setPreferredSize(new Dimension(110, 30));
      volumeSlider_alignment.add(volumeSlider, BorderLayout.NORTH);
      volumeSlider_alignment.add(pb, BorderLayout.SOUTH);

      mastaJP.add(volumeSlider_alignment);
      mastaJP.add(trackInfo_Button);
      mastaJP.add(likeTrack_Button);
      mastaJP.add(lastTrack_Button);
      mastaJP.add(playPause_Button);
      mastaJP.add(nextTrack_Button);
      mastaJP.add(shufflePlaystyle_Button);
      mastaJP.add(loopTrack_Button);

      timeSlider = new JSlider(0, 100);
      timeSlider.setValue(0);
      timeSlider.setFocusable(false);
      timeSlider.setForeground(const_ColorManager.DEFAULT_GREEN_FG);

      addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e)
        {
          SwingUtilities.invokeLater(() -> {
            timeSlider.setMaximum(Math.min(100, getSize().width)); // expanding -> more smooth ticking
          });
        }
      });

      const_Core.PLAYER.expose().add_status_listener(this);

      add(mastaJP);
      add(timeSlider);
    }

    @Override
    public void tailwind_status(tailwind_Status e)
    {
      playPause_Button.set_rolled(e == tailwind_Status.PLAYING);
    }
  }

  private JPanel bgPanel;

  public dgui_HalcyonTop()
  {
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_TOP));
    setMinimumSize(getPreferredSize());
    setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    setLayout(new OverlayLayout(this));

    JComponent e = new halcyonTop_Info(), x = new halcyonTop_Buttons();

    e.setAlignmentX(Component.CENTER_ALIGNMENT);
    x.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel copy = new JPanel();
    copy.setPreferredSize(getPreferredSize());
    copy.setOpaque(false);
    copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
    copy.add(e);
    copy.add(x);

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

    const_Core.SELECTION_LISTENERS.add_listener(this);
  }

  @Override
  public void forYou(use_TailwindTrack e)
  {

  }
}

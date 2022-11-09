package com.jackmeng.halcyon.gui.childs;

import javax.swing.*;

import java.awt.*;

import com.jackmeng.halcyon.const_Global;
import com.jackmeng.halcyon.apps.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.sys.pstream;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;

public class dgui_HalcyonTop
    extends JPanel
    implements evnt_SelectPlaylistTrack
{

  public static final class halcyonTop_Info
      extends JPanel
      implements evnt_SelectPlaylistTrack
  {
    public halcyonTop_Info()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH,
          const_Manager.DGUI_APPS_FILELIST_HEIGHT - (const_Manager.DGUI_APPS_FILELIST_HEIGHT / 2)));
      setOpaque(false);
      /*-------------------------------------------------- /
      / setOpaque(true);                                   /
      / setBackground(const_ColorManager.DEFAULT_BLUE_FG); /
      /---------------------------------------------------*/
    }

    @Override
    public void forYou(use_TailwindTrack e)
    {

    }
  }

  public static final class halcyonTop_Buttons extends JPanel
  {
    public halcyonTop_Buttons()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT / 2));
      setOpaque(false);
      /*------------------------------------------------- /
      / setOpaque(true);                                  /
      / setBackground(const_ColorManager.DEFAULT_RED_FG); /
      /--------------------------------------------------*/
    }
  }

  /*----------------------------------- /
  / private JLayer< Component > blurBp; /
  /------------------------------------*/
  /*----------------------------------- /
  / private transient BufferedImage bi; /
  / private JPanel bgPanel;             /
  /------------------------------------*/

  public dgui_HalcyonTop()
  {
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT));
    setOpaque(true);
    setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    setLayout(new OverlayLayout(this));

    /*--------------------------------------------------------------------------- /
    / bgPanel = new JPanel() {                                                    /
    /                                                                             /
    /   @Override                                                                 /
    /   public void paintComponent(Graphics g)                                    /
    /   {                                                                         /
    /     super.paintComponent(g);                                                /
    /     if (bi != null)                                                         /
    /     {                                                                       /
    /       g.setColor(use_Color.make(use_Image.accurate_accent_color_1(bi)));    /
    /       g.fillRect(0, 0, bgPanel.getSize().width, bgPanel.getSize().height);  /
    /     }                                                                       /
    /     else                                                                    /
    /       g.clearRect(0, 0, bgPanel.getSize().width, bgPanel.getSize().height); /
    /     g.dispose();                                                            /
    /   }                                                                         /
    / };                                                                          /
    / bgPanel.setPreferredSize(getPreferredSize());                               /
    / bgPanel.setOpaque(false);                                                   /
    /                                                                             /
    /----------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------ /
    / blurBp = new JLayer<>(bgPanel, new LayerUI<>() {                                                             /
    /   private transient imgstrat_BlurhashBlur blur = const_MUTableKeys.top_bg_panel_use_blur                     /
    /       ? new imgstrat_BlurhashBlur(3, 4, 1.0D)                                                                /
    /       : new imgstrat_BlurhashBlur(1, 1, 0.5D);                                                               /
    /                                                                                                              /
    /   @Override                                                                                                  /
    /   public void paint(Graphics g, JComponent comp)                                                             /
    /   {                                                                                                          /
    /     if (comp.getWidth() == 0 || comp.getHeight() == 0)                                                       /
    /       return;                                                                                                /
    /     if (bi != null)                                                                                          /
    /     {                                                                                                        /
    /       BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB); /
    /       Graphics2D ig2 = img.createGraphics();                                                                 /
    /       ig2.setClip(g.getClip());                                                                              /
    /       super.paint(ig2, comp);                                                                                /
    /       ig2.dispose();                                                                                         /
    /       Graphics2D g2 = (Graphics2D) g;                                                                        /
    /       g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);                  /
    /       g2.drawImage(img, blur, 0, 0);                                                                         /
    /       g2.dispose();                                                                                          /
    /     }                                                                                                        /
    /     else                                                                                                     /
    /       g.clearRect(0, 0, 0, 0);                                                                               /
    /     g.dispose();                                                                                             /
    /   }                                                                                                          /
    / });                                                                                                          /
    /-------------------------------------------------------------------------------------------------------------*/

    JPanel containerPane = new JPanel();
    containerPane.setPreferredSize(getPreferredSize());
    containerPane.setOpaque(false);
    containerPane.setLayout(new BorderLayout());
    containerPane.add(new halcyonTop_Info(), BorderLayout.NORTH);
    containerPane.add(new halcyonTop_Buttons(), BorderLayout.SOUTH);

    add(containerPane);
    /*------------ /
    / add(blurBp); /
    /-------------*/

    const_Global.SELECTION_LISTENERS.add_listener_top(this);
  }

  @Override
  public void forYou(use_TailwindTrack e)
  {
    pstream.log.warn("AWAITED TRACK: " + e.get(tailwindtrack_Tags.MEDIA_ART));
    /*-------------------- /
    / blurBp.repaint(10L); /
    /---------------------*/
  }
}

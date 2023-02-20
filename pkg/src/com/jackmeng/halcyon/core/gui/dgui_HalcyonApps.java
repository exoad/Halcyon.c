package com.jackmeng.halcyon.core.gui;

import com.jackmeng.halcyon.const_Core;
import com.jackmeng.halcyon.use_HalcyonCore;
import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.halcyon.core.abst.impl_App;
import com.jackmeng.halcyon.core.abst.impl_HalcyonRefreshable;
import com.jackmeng.halcyon.core.ploogin.impl_Ploogin;
import com.jackmeng.halcyon.core.util.const_GeneralStatus;
import com.jackmeng.halcyon.core.util.pstream;
import com.jackmeng.halcyon.core.util.use_Color;
import com.jackmeng.halcyon.core.util.use_Image;
import com.jackmeng.halcyon.core.util.use_ResourceFetcher;
import com.jackmeng.halcyon.core.util.use_Task;
import com.jackmeng.halcyon.core.util.use_Struct.struct_Pair;
import com.jackmeng.halcyon.use_HalcyonFolder.halcyonfolder_Content;

import javax.swing.*;

import static com.jackmeng.halcyon.const_Lang.*;

import java.awt.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

public class dgui_HalcyonApps
    extends
    JPanel
    implements
    impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< impl_App > > >
{

  private transient gui_HalcyonPlaylistSelect fileChooser;
  private final transient Map< String, JButton > appMap; // Key: AppID Value: GUIComponent

  public dgui_HalcyonApps()
  {
    appMap = new Hashtable<>();
    /*----------------------------------------------------------------------------- /
    / !!: make the current dir be able to dynamic or where the user last selected /
    /------------------------------------------------------------------------------*/
    use_Task.async_N1(() -> {
      use_HalcyonFolder.FOLDER.deserialize(halcyonfolder_Content.PLAYLIST_SELECT_FOLDER_CACHE_f.val,
          gui_HalcyonPlaylistSelect.class, x -> { // on error
            use_HalcyonFolder.FOLDER.log(x);
            fileChooser = new gui_HalcyonPlaylistSelect(use_HalcyonCore.getInheritableFrame(), // dont use
                                                                                               // Halcyon.main.expose()
                                                                                               // because you cant call
                                                                                               // when u are still
                                                                                               // constructing that obj
                ".");
            fileChooser.setListener(const_Core::append_to_Playlist);
          }, x -> { // default promise
            fileChooser = x == null ? new gui_HalcyonPlaylistSelect(use_HalcyonCore.getInheritableFrame(),
                ".") : x;
            fileChooser.setListener(const_Core::append_to_Playlist);
          });

      Runtime.getRuntime()
          .addShutdownHook(new Thread(() -> use_HalcyonFolder.FOLDER
              .serialize(halcyonfolder_Content.PLAYLIST_SELECT_FOLDER_CACHE_f.val,
                  fileChooser)));

    });

    gui_HalcyonMoreApps apps = new gui_HalcyonMoreApps();

    setPreferredSize(new Dimension(const_Manager.DGUI_APPS_WIDTH, const_Manager.FRAME_MIN_HEIGHT / 2));
    setMinimumSize(getPreferredSize());
    setMaximumSize(new Dimension(const_Manager.DGUI_APPS_WIDTH + 20, const_Manager.FRAME_MIN_HEIGHT / 2));
    const_Core.APPS_POOL.addRefreshable(this);
    setPreferredSize(new Dimension(const_Manager.DGUI_APPS_WIDTH, const_Manager.FRAME_MIN_HEIGHT / 2));
    setMinimumSize(getPreferredSize());
    setMaximumSize(new Dimension(const_Manager.DGUI_APPS_WIDTH + 20, const_Manager.FRAME_MIN_HEIGHT / 2));
    setLayout(new FlowLayout(FlowLayout.CENTER, const_Manager.DGUI_APPS_APPS_ICON_HGAP,
        const_Manager.DGUI_APPS_APPS_ICON_VGAP));
    setFont(use_HalcyonCore.regularFont());
    setOpaque(false);

    if (const_Manager.DEBUG_GRAPHICS)
    {
      setOpaque(true);
      setBackground(use_Color.rndColor());
    }

    // =========================================================================
    // This part designates the default created pool of apps to be added
    // =========================================================================
    use_Task.run_submit(() -> {
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(_lang(LANG_APPS_ADD_PLAYLIST_TOOLTIP), fileChooser,
              const_ResourceManager.DGUI_APPS_ADD_PLAYLIST));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(_lang(LANG_APPS_OPEN_LIKED_LIST), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_PLAYER_LIKED_MUSIC));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(_lang(LANG_APPS_AUDIO_CTRLERS), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_AUDIO_CTRLER));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(_lang(LANG_APPS_OPEN_MINI_PLAYER), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_MINI_PLAYER));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(
              _lang(LANG_APPS_OPEN_SETTINGS), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_PLAYER_SETTINGS));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(
              _lang(LANG_APPS_PLAYLIST_VIEWER), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_PLAYER_LISTVIEW));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(
              _lang(LANG_APPS_REFRESH_PLAYLISTS), use_HalcyonCore::do_nothing,
              const_ResourceManager.DGUI_APPS_PLAYER_REFRESH));
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(
              _lang(LANG_APPS_INFO), () -> new gui_UserProfile(use_HalcyonFolder.FOLDER.expose_ClientProfile()).run(),
              const_ResourceManager.DGUI_APPS_PLAYER_INFO));
      /*----------------------------------------------------------------------------------------------------------- /
      / const_Core.APPS_POOL.addPoolObject(                                                                         /
      /     make_DefaultApp("ArtworkDisplay", new app_ArtworkDisplay(), const_ResourceManager.DGUI_FILELIST_LEAF)); /
      /------------------------------------------------------------------------------------------------------------*/
      const_Core.APPS_POOL.addPoolObject(
          make_DefaultApp(_lang(LANG_APPS_VIEW_MORE), apps,
              const_ResourceManager.DGUI_APPS_PLAYER_MOREAPPS));
      /*---------------------------------------------- /
      / this part forces everything else to be ignored /
      /-----------------------------------------------*/
    });

  }

  public static final Insets APPS_BTN_BORDER_INSETS = new Insets(3, 3, 3, 3);
  public static final use_RoundCornerBorder APPS_BTN_ROUND_BORDER = new use_RoundCornerBorder(15, 4,
      const_ColorManager.DEFAULT_DARK_BG_2.darker().darker(), APPS_BTN_BORDER_INSETS);

  /**
   * @param r
   */
  private void addApp(impl_App r)
  {
    if (!(r instanceof impl_Ploogin) && !appMap.containsKey(r.id()))
    {
      use_Task.run_Snb_1(() -> {
        JButton btn = !const_Manager.DEBUG_GRAPHICS ? new JButton() {
          @Override public void paintComponent(Graphics g)
          {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setColor(APPS_BTN_ROUND_BORDER.color);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);
            super.paintComponent(g2);
          }
        } : new JButton();
        btn.setToolTipText(r.toolTip());
        btn.setIcon(r.icon());
        btn.setOpaque(true);
        btn.setBorder(APPS_BTN_ROUND_BORDER);
        btn.addActionListener(e -> r.run());
        if (!const_Manager.DEBUG_GRAPHICS)
        {
          btn.setBackground(null);
          btn.setRolloverEnabled(false);
          btn.setContentAreaFilled(false);
        }
        else
        {
          btn.setBackground(use_Color.rndColor());
        }
        r.rolloverIcon().ifPresent(rol -> {
          btn.setRolloverEnabled(true);
          btn.setRolloverIcon(rol);
        });
        appMap.put(r.id(), btn);
        add(btn);
        revalidate();
      });
    }
    else
      pstream.log.warn("Failed to add an instance of impl_App.\nRequirements checked: \n\t1. impl_Ploogin "
          + (r instanceof impl_Ploogin) + "\n\t2. appMap containment " + (appMap.containsKey(r.id())));
  }

  private void removeApp(impl_App r)
  {
    if (appMap.containsKey(r.id()))
    {
      JButton e = appMap.get(r.id());
      remove(e);
      revalidate();
    }
  }

  /**
   * @param tooltip
   * @param run
   * @param iconLocale
   * @return impl_App
   */
  public static impl_App make_DefaultApp(String tooltip, Runnable run, String iconLocale)
  {
    return new impl_App() {
      @Override public void run()
      {
        run.run();
      }

      @Override public ImageIcon icon()
      {
        return use_Image.resize_fast_1(const_Manager.DGUI_APPS_ICON_BTN_WIDTH, const_Manager.DGUI_APPS_ICON_BTN_WIDTH,
            use_ResourceFetcher.fetcher.getFromAsImageIcon(iconLocale));
      }

      @Override public String toolTip()
      {
        return tooltip;
      }

      @Override public Optional< ImageIcon > rolloverIcon()
      {
        return Optional.of(icon());
      }

      @Override public String id()
      {
        return new File(iconLocale).getName();
      }
    };
  }

  /**
   * @param refreshed
   */
  @Override public void refresh(const_GeneralStatus type,
      struct_Pair< Optional< String >, Optional< impl_App > > refreshed)
  {
    refreshed.second.ifPresent(type == const_GeneralStatus.ADDITION ? this::addApp : this::removeApp);
  }

  @Override public void dry_refresh()
  {
    const_Core.APPS_POOL.objs().forEach(x -> addApp(const_Core.APPS_POOL.get(x)));
  }
}

package com.jackmeng.halcyon.gui.childs;

import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.halcyon.gui.gui_HalcyonMoreApps;
import com.jackmeng.halcyon.gui.gui_HalcyonPlaylistSelect;
import com.jackmeng.halcyon.ploogin.impl_Ploogin;
import com.jackmeng.const_Global;
import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.halcyon.abst.impl_App;
import com.jackmeng.halcyon.abst.impl_HalcyonRefreshable;
import com.jackmeng.sys.use_Task;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public class dgui_HalcyonApps
    extends JPanel
    implements impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< impl_App > > >
{

  private transient gui_HalcyonMoreApps apps;
  private transient gui_HalcyonPlaylistSelect fileChooser;

  public dgui_HalcyonApps()
  {
    /*----------------------------------------------------------------------------- /
    / !!: make the current dir be able to dynamic or where the user last selected /
    /------------------------------------------------------------------------------*/
    use_HalcyonFolder.FOLDER.deserialize("PLAYLIST_SELECT_FOLDER.x",
        gui_HalcyonPlaylistSelect.class, x -> {
          use_HalcyonFolder.FOLDER.log(x);
          fileChooser = new gui_HalcyonPlaylistSelect(use_Halcyon.getInheritableFrame(),
              ".");
        }, x -> fileChooser = x != null ? new gui_HalcyonPlaylistSelect(use_Halcyon.getInheritableFrame(),
            ".") : x);
    fileChooser.setListener(const_Global::append_to_Playlist);

    Runtime.getRuntime()
        .addShutdownHook(new Thread(() -> use_HalcyonFolder.FOLDER.serialize("PLAYLIST_SELECT_FOLDER.x", fileChooser)));

    apps = new gui_HalcyonMoreApps();

    setPreferredSize(new Dimension(const_Manager.DGUI_APPS_WIDTH, const_Manager.FRAME_MIN_HEIGHT / 2));
    setMinimumSize(getPreferredSize());
    setMaximumSize(new Dimension(const_Manager.DGUI_APPS_WIDTH + 20, const_Manager.FRAME_MIN_HEIGHT / 2));
    const_Global.APPS_POOL.addRefreshable(this);
    setPreferredSize(new Dimension(const_Manager.DGUI_APPS_WIDTH, const_Manager.FRAME_MIN_HEIGHT / 2));
    setMinimumSize(getPreferredSize());
    setMaximumSize(new Dimension(const_Manager.DGUI_APPS_WIDTH + 20, const_Manager.FRAME_MIN_HEIGHT / 2));
    setLayout(new FlowLayout(FlowLayout.CENTER, const_Manager.DGUI_APPS_APPS_ICON_HGAP,
        const_Manager.DGUI_APPS_APPS_ICON_VGAP));
    setFont(use_Halcyon.regularFont());

    if (const_Manager.DEBUG_GRAPHICS)
    {
      setOpaque(true);
      setBackground(use_Color.rndColor());
    }

    // =========================================================================
    // This part designates the default created pool of apps to be added
    // =========================================================================
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(_lang(LANG_APPS_ADD_PLAYLIST_TOOLTIP), fileChooser,
            const_ResourceManager.DGUI_APPS_ADD_PLAYLIST));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(_lang(LANG_APPS_OPEN_LIKED_LIST), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_PLAYER_LIKED_MUSIC));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(_lang(LANG_APPS_AUDIO_CTRLERS), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_AUDIO_CTRLER));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(_lang(LANG_APPS_OPEN_MINI_PLAYER), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_MINI_PLAYER));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(
            _lang(LANG_APPS_OPEN_SETTINGS), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_PLAYER_SETTINGS));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(
            _lang(LANG_APPS_PLAYLIST_VIEWER), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_PLAYER_LISTVIEW));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(
            _lang(LANG_APPS_REFRESH_PLAYLISTS), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_PLAYER_REFRESH));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(
            _lang(LANG_APPS_INFO), use_Halcyon::do_nothing,
            const_ResourceManager.DGUI_APPS_PLAYER_INFO));
    const_Global.APPS_POOL.addPoolObject(
        make_DefaultApp(_lang(LANG_APPS_VIEW_MORE), apps,
            const_ResourceManager.DGUI_APPS_PLAYER_MOREAPPS));
    /*---------------------------------------------- /
    / this part forces everything else to be ignored /
    /-----------------------------------------------*/
  }

  /**
   * @param r
   */
  private void addApp(impl_App r)
  {
    if (!(r instanceof impl_Ploogin))
    {
      use_Task.run_Snb_1(() -> {
        JButton btn = new JButton();
        btn.setToolTipText(r.toolTip());
        btn.setIcon(r.icon());
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder());
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
        add(btn);
        revalidate();
      });
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
      @Override
      public void run()
      {
        run.run();
      }

      @Override
      public ImageIcon icon()
      {
        return use_Image.resize_fast_1(const_Manager.DGUI_APPS_ICON_BTN_WIDTH, const_Manager.DGUI_APPS_ICON_BTN_WIDTH,
            use_ResourceFetcher.fetcher.getFromAsImageIcon(iconLocale));
      }

      @Override
      public String toolTip()
      {
        return tooltip;
      }

      @Override
      public Optional< ImageIcon > rolloverIcon()
      {
        return Optional.of(icon());
      }

      @Override
      public String id()
      {
        return new File(iconLocale).getName();
      }
    };
  }

  /**
   * @param refreshed
   */
  @Override
  public void refresh(struct_Pair< Optional< String >, Optional< impl_App > > refreshed)
  {
    refreshed.second.ifPresent(this::addApp);
  }

  @Override
  public void dry_refresh()
  {
    const_Global.APPS_POOL.objs().forEach(x -> addApp(const_Global.APPS_POOL.get(x)));
  }
}

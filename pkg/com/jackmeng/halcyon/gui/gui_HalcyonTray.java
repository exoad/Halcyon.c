package com.jackmeng.halcyon.gui;

import java.util.Optional;

import javax.swing.ImageIcon;

import com.jackmeng.halcyon.const_Global;
import com.jackmeng.halcyon.use_HalcyonProperties;
import com.jackmeng.halcyon.apps.impl_App;
import com.jackmeng.halcyon.apps.impl_HalcyonRefreshable;
import com.jackmeng.halcyon.apps.impl_TrayApp;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Program;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.awt.*;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public class gui_HalcyonTray
    implements Runnable, impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<impl_App>>> {

  /*-------------------- /
  / DOES NOT WORK AT ALL /
  /---------------------*/

  private PopupMenu main;
  private SystemTray tray;
  private TrayIcon ico;

  public gui_HalcyonTray() {
    if (SystemTray.isSupported()) {
      const_Global.APPS_POOL.addRefreshable(this);
      main = new PopupMenu("Halcyon");
      tray = SystemTray.getSystemTray();
      ico = new TrayIcon(
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO).getImage());
      ico.setPopupMenu(main);
      ico.setToolTip("Halcyon");

      /*-------------------------------------------- /
      / default components are not added to the pool /
      /---------------------------------------------*/
      MenuItem about = new MenuItem(_lang(LANG_APPS_INFO));
      about.addActionListener(x -> use_HalcyonProperties.do_nothing());
      MenuItem quit = new MenuItem(_lang(LANG_QUIT));
      quit.addActionListener(x -> {
        tray.remove(ico);
        use_Program.dispose();
      });

      main.add(_lang(LANG_TRAY_DEFAULT_APPS));
      main.add(about);
      main.add(quit);
      main.addSeparator();
      main.add(_lang(LANG_TRAY_DEFAULT_APPS));
    } else {
      pstream.log.err("FAILED TO CREATE A SYSTEM TRAY INSTANCE! REASON: NOT SUPPORTED");
    }
  }

  public static impl_TrayApp make_DefaultTrayApp(String toolTip, Runnable run, String iconLocale,
      struct_Pair<String, Runnable> items) {
    return new impl_TrayApp() {

      @Override
      public ImageIcon icon() {
        return use_ResourceFetcher.fetcher.getFromAsImageIcon(iconLocale);
      }

      @Override
      public String toolTip() {
        return toolTip;
      }

      @Override
      public Optional<ImageIcon> rolloverIcon() {
        return Optional.empty();
      }

      @Override
      public void run() {
        use_HalcyonProperties.do_nothing();
      }

      @Override
      public struct_Pair<String, Runnable> actions() {
        return items;
      }

    };
  }

  @Override
  public void run() {
    if (SystemTray.isSupported()) {
      try {
        tray.add(ico);
      } catch (AWTException e) {
        pstream.log.err("FAILED TO LAUNCH A TRAY INSTANCE...\n" + e.getLocalizedMessage());
      }
    }
  }

  @Override
  public void refresh(struct_Pair<Optional<String>, Optional<impl_App>> refreshed) {
    if (SystemTray.isSupported()) {
      /*-------------------------------------------------------------------------------------------------------- /
      / All impl_App children are able to be added as an APP module, but cannot be used in subinstances as APPS. /
      /---------------------------------------------------------------------------------------------------------*/
      refreshed.second.ifPresent(x -> {
        /*--------------------------------------------------------------------------------- /
        / only specific trayApps can be added as they have a specific function that is used /
        / to create a popupmenu                                                             /
        /----------------------------------------------------------------------------------*/
        if (x instanceof impl_TrayApp) {
          /*---------------------------------- /
          / impl_TrayApp t = (impl_TrayApp) x; /
          /-----------------------------------*/
          /*----------------------- /
          / to be implemented later /
          /------------------------*/
        }
      });
    }
  }

  @Override
  public void dry_refresh() {
    use_HalcyonProperties.do_nothing();
  }
}

package com.jackmeng.halcyon;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.jackmeng.const_Global;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_ResourceFetcher;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.LogManager;

public final class use_Halcyon
{
  private use_Halcyon()
  {
  }

  public static use_MUTableDefinition[] DEFS = {
      new use_MUTableDefinition("Program Language", "halcyon.language", "en_US", new String[] { "en_US", "zh_CN" },
          x -> {
            const_MUTableKeys.lang_locale.forYou(x);
            language = ResourceBundle.getBundle("com.jackmeng.include.locale.HalcyonLang",
                new Locale(const_MUTableKeys.lang_locale.first, const_MUTableKeys.lang_locale.second));
          }, x -> {
            return const_MUTableKeys.lang_locale.first + "_" + const_MUTableKeys.lang_locale.second;
          }),
      new use_MUTableDefinition("Enable Logging", "halcyon.logging", "yes", new String[] { "yes", "no" },
          x -> const_MUTableKeys.outstream = x.equalsIgnoreCase("yes"), x -> {
            return const_MUTableKeys.outstream ? "yes" : "no";
          }),
      new use_MUTableDefinition("Enable Startup Testcase Check", "halcyon.tc_eval", "no", new String[] { "yes", "no" },
          x -> const_MUTableKeys.run_tcs_on_start = !x.equalsIgnoreCase("no"), x -> {
            return const_MUTableKeys.run_tcs_on_start ? "yes" : "no";
          }),
      new use_MUTableDefinition("TitleBar Styling", "halcyon.gui.title_bar_styling", "custom",
          new String[] { "custom", "native" },
          x -> const_MUTableKeys.title_frame_styling = !x.equalsIgnoreCase("custom"),
          x -> {
            return const_MUTableKeys.title_frame_styling ? "native" : "custom";
          }),
      new use_MUTableDefinition("Use blurring for the back cover art", "halcyon.gui.top_bg_panel_use_blur", "no",
          new String[] { "yes", "no" },
          x -> const_MUTableKeys.top_bg_panel_use_blur = !x.equalsIgnoreCase("no"), x -> {
            return const_MUTableKeys.top_bg_panel_use_blur ? "yes" : "no";
          }),
      new use_MUTableDefinition("File Listing use a titled border", "halcyon.gui.filelist_titled_border", "no",
          new String[] { "yes", "no" },
          x -> const_MUTableKeys.use_filelist_titled_border = !x.equalsIgnoreCase("no"), x -> {
            return const_MUTableKeys.use_filelist_titled_border ? "yes" : "no";
          }),
      new use_MUTableDefinition("Width & Height of Artwork in Top Pane.", "halcyon.gui.top_artwork_wxh", "132x132",
          new String[0],
          x -> {
            const_MUTableKeys.top_artwork_wxh.first = Integer.parseInt(x.toLowerCase().split("x")[0]);
            const_MUTableKeys.top_artwork_wxh.second = Integer.parseInt(x.toLowerCase().split("x")[1]);
          }, x -> {
            return const_MUTableKeys.top_artwork_wxh.first + "x" + const_MUTableKeys.top_artwork_wxh.second;
          }),
      new use_MUTableDefinition("Turn on debugging layouts for the GUI", "halcyon.gui.debug_layout", "no",
          new String[] { "yes", "no" },
          x -> const_MUTableKeys.gui_use_debug = !x.equalsIgnoreCase("no"), x -> {
            return const_MUTableKeys.gui_use_debug ? "yes" : "no";
          }),
  };

  public static final Random rng = new Random();
  public static ResourceBundle language = ResourceBundle.getBundle("com.jackmeng.include.locale.HalcyonLang",
      new Locale(const_MUTableKeys.lang_locale.first, const_MUTableKeys.lang_locale.second));

  /**
   * @return String
   */
  public static String getFileSeparator()
  {
    return System.getProperty("file.separator") == null ? "/" : System.getProperty("file.separator");
  }

  public static Border getDebugBorder()
  {
    return BorderFactory.createLineBorder(use_Color.rndColor(), 2);
  }

  /**
   * @param key
   * @return String
   */
  public static String lang(String key)
  {
    return language.getString(key);
  }

  /**
   * @return String[]
   */
  public static String[] acceptedEndings()
  {
    return new String[] { ".mp3", ".wav", ".ogg", ".oga", ".aiff", ".aifc", ".au", ".opus", ".flac" };
  }

  /**
   * @return Font
   */
  public static Font regularFont()
  {
    return getFont(use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Regular.ttf"));
  }

  /**
   * @return Font
   */
  public static Font boldFont()
  {
    return getFont(use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Bold.ttf"));
  }

  /**
   * @param rsc
   * @return Font
   */
  private static Font getFont(File rsc)
  {
    try
    {
      return Font.createFont(Font.TRUETYPE_FONT,
          rsc);
    } catch (FontFormatException | IOException e)
    {
      e.printStackTrace();
    }
    return new Font(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0], Font.PLAIN, 12);
  }

  /**
   * @param f
   */
  public static void setUIFont(javax.swing.plaf.FontUIResource f)
  {
    Enumeration< Object > keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put(key, f);
    }
  }

  /**
   * @return JFrame
   */
  public static JFrame getInheritableFrame()
  {
    JFrame e = new JFrame();
    e.setIconImage(use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO).getImage());
    return e;
  }

  /**
   * @throws Exception
   */
  public static void init_properties() throws Exception
  {
    System.setProperty("file.encoding", "UTF-8");

    /*----------------------------------- /
    / Set all UI element based properties /
    /------------------------------------*/

    final ColorUIResource colorUI_Green = new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG);
    UIManager.setLookAndFeel(FlatAtomOneDarkIJTheme.class.getName());
    UIManager.put("JScrollPane.smoothScrolling", true);
    UIManager.put("ScrollBar.thumbArc", 999);
    UIManager.put("ScrollBar.trackArc", 999);
    UIManager.put("ScrollBar.background", null);
    UIManager.put("ScrollBar.thumb", colorUI_Green);
    UIManager.put("Scrollbar.pressedThumbColor", colorUI_Green);
    UIManager.put("ScrollBar.hoverThumbColor", colorUI_Green);
    UIManager.put("TabbedPane.underlineColor", colorUI_Green);
    UIManager.put("TabbedPane.showTabSeparators", true);
    UIManager.put("ScrollBar.showButtons", false);
    UIManager.put("Component.focusColor", colorUI_Green);
    UIManager.put("Component.focusedBorderColor", colorUI_Green);
    UIManager.put("TextArea.caretForeground", colorUI_Green);
    UIManager.put("TextField.caretForeground", colorUI_Green);
    UIManager.put("TitlePane.closeHoverBackground", colorUI_Green);
    UIManager.put("TitlePane.closePressedBackground", colorUI_Green);
    UIManager.put("TitlePane.buttonHoverBackground", colorUI_Green);
    UIManager.put("TitlePane.buttonPressedBackground", colorUI_Green);
    UIManager.put("TitlePane.closeHoverForeground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG.darker()));
    UIManager.put("TitlePane.closePressedForeground",
        new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG.darker()));
    UIManager.put("TitlePane.buttonHoverForeground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG.darker()));
    UIManager.put("TitlePane.buttonPressedForeground",
        new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG.darker()));
    UIManager.put("Component.focusedBorderColor", use_Color.nullColor());
    UIManager.put("Component.focusColor", use_Color.nullColor());
    UIManager.put("TitlePane.centerTitle", true);
    UIManager.put("TitlePane.buttonSize", new java.awt.Dimension(25, 20));
    UIManager.put("TitlePane.unifiedBackground", true);
    UIManager.put("SplitPaneDivider.gripDotCount", 0);
    UIManager.put("FileChooser.readOnly", false);
    System.setProperty("flatlaf.useWindowDecorations", "false");
    System.setProperty("flatlaf.useJetBrainsCustomDecorations", "false");
    System.setProperty("flatlaf.animation", "true");
    GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();

    g.registerFont(
        Font.createFont(Font.TRUETYPE_FONT,
            use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Regular.ttf")));
    g.registerFont(
        Font.createFont(Font.TRUETYPE_FONT,
            use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Bold.ttf")));
    g.registerFont(
        Font.createFont(Font.TRUETYPE_FONT,
            use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Italic.ttf")));

    setUIFont(new FontUIResource(regularFont().getFontName(), Font.TRUETYPE_FONT, 13));

    /*-------------------- /
    / Perma genned locales /
    /---------------------*/
    UIManager.put("FileChooser.acceptAllFileFilterText", "?");
    /*------------------------------ /
    / below is all for locale stuffs /
    /-------------------------------*/
    UIManager.getDefaults().setDefaultLocale(
        language.getLocale());
    /*------------------------------------------------------------------------------------------- /
    / addResourceBundle doesnt automatically find by locale and instead needs to be provided with /
    / the EXACT bundle base name                                                                  /
    /--------------------------------------------------------------------------------------------*/
    UIManager.getDefaults()
        .addResourceBundle("com.jackmeng.include.locale.HalcyonLang_" + language.getLocale());

    const_Global.GENERAL_LOOP.schedule(new TimerTask() {

      @Override
      public void run()
      {
        use_HalcyonFolder.FOLDER.master_save();
      }

    }, 1000L, 3500L);

    LogManager.getLogManager().reset();
  }

  public static void do_nothing()
  {
    // DO NOTHING AND TO BE IMPLEMENTED WITH STUFF LATER
  }
}

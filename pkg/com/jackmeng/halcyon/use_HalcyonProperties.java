package com.jackmeng.halcyon;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_MutableManager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_ResourceFetcher;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

public final class use_HalcyonProperties {
  private use_HalcyonProperties() {
  }

  public static final Random rng = new Random();
  public static final ResourceBundle language = ResourceBundle.getBundle("com.jackmeng.include.locale.HalcyonLang",
      new Locale(const_MutableManager.lang_locale.first, const_MutableManager.lang_locale.second));

  /**
   * @return String
   */
  public static String getFileSeparator() {
    return System.getProperty("file.separator");
  }

  /**
   * @param key
   * @return String
   */
  public static String lang(String key) {
    return language.getString(key);
  }

  /**
   * @return String[]
   */
  public static String[] acceptedEndings() {
    return new String[] { ".mp3", ".wav", ".ogg", ".oga", ".aiff", ".aifc", ".au", ".opus", ".flac" };
  }

  /**
   * @return Font
   */
  public static Font regularFont() {
    return getFont(use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Regular.ttf"));
  }

  /**
   * @return Font
   */
  public static Font boldFont() {
    return getFont(use_ResourceFetcher.fetcher.getFromAsFile("resources/font/HalcyonFont-Bold.ttf"));
  }

  /**
   * @param rsc
   * @return Font
   */
  private static Font getFont(File rsc) {
    try {
      return Font.createFont(Font.TRUETYPE_FONT,
          rsc);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
    return new Font(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0], Font.PLAIN, 12);
  }

  /**
   * @param f
   */
  public static void setUIFont(javax.swing.plaf.FontUIResource f) {
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put(key, f);
    }
  }

  /**
   * @return JFrame
   */
  public static JFrame getInheritableFrame() {
    JFrame e = new JFrame();
    e.setIconImage(use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO).getImage());
    return e;
  }

  /**
   * @throws Exception
   */
  public static void init_properties() throws Exception {
    System.setProperty("file.encoding", "UTF-8");

    /*----------------------------------- /
    / Set all UI element based properties /
    /------------------------------------*/
    UIManager.setLookAndFeel(FlatAtomOneDarkIJTheme.class.getName());
    UIManager.put("ScrollBar.thumbArc", 999);
    UIManager.put("ScrollBar.trackArc", 999);
    UIManager.put("ScrollBar.background", null);
    UIManager.put("ScrollBar.thumb", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("Scrollbar.pressedThumbColor", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("ScrollBar.hoverThumbColor", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("TabbedPane.underlineColor", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("TabbedPane.showTabSeparators", true);
    UIManager.put("ScrollBar.showButtons", false);
    UIManager.put("TitlePane.closeHoverBackground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("TitlePane.closePressedBackground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("TitlePane.buttonHoverBackground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
    UIManager.put("TitlePane.buttonPressedBackground", new ColorUIResource(const_ColorManager.DEFAULT_GREEN_FG));
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

    LogManager.getLogManager().reset();
  }

  public static void do_nothing() {
    // DO NOTHING AND TO BE IMPLEMENTED WITH STUFF LATER
  }
}
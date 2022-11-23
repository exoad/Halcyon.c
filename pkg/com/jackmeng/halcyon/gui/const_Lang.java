package com.jackmeng.halcyon.gui;

import com.jackmeng.halcyon.use_Halcyon;

public final class const_Lang
{
  private const_Lang()
  {
  }

  public static final String LANG_EXCEPTION_OH_SOMETHING_WENT_WRONG_CONTENT = "exception_oh_something_went_wrong_content";
  public static final String LANG_OH_NO_1 = "oh_no_1";
  public static final String LANG_OH_NO_2 = "oh_no_2";
  public static final String LANG_OH_NO_3 = "oh_no_3";
  public static final String LANG_OH_NO_4 = "oh_no_4";
  public static final String LANG_QUIT = "quit";
  public static final String LANG_TRAY_DEFAULT_APPS = "tray_default_apps";
  public static final String LANG_TRAY_OUTER_APPS = "tray_outer_apps";
  public static final String LANG_PLAYLIST_SELECT_APPROVE_BUTTON_TOOLTIP = "playlist_select_approve_button_tooltip";
  public static final String LANG_PLAYLIST_SELECT_DIALOG_TITLE_SUB = "playlist_select_dialog_title_sub";
  public static final String LANG_PLAYLIST_SELECT_APPROVE_BUTTON = "playlist_select_approve_button";
  public static final String LANG_PLAYLIST_DEFAULT_LIKED_TITLE = "playlist_default_liked_title";
  public static final String LANG_APPS_ADD_PLAYLIST_TOOLTIP = "apps_add_playlist_tooltip";
  public static final String LANG_APPS_AUDIO_CTRLERS = "apps_audio_ctrlers";
  public static final String LANG_APPS_OPEN_MINI_PLAYER = "apps_open_mini_player";
  public static final String LANG_APPS_OPEN_SETTINGS = "apps_open_settings";
  public static final String LANG_APPS_PLAYLIST_VIEWER = "apps_playlist_viewer";
  public static final String LANG_APPS_REFRESH_PLAYLISTS = "apps_refresh_playlists";
  public static final String LANG_APPS_INFO = "apps_info";
  public static final String LANG_APPS_VIEW_MORE = "apps_view_more";
  public static final String LANG_FILELIST_BORDER_TITLE = "filelist_border_title";
  public static final String LANG_APPS_OPEN_LIKED_LIST = "apps_open_liked_list";
  public static final String LANG_FILELIST_CANCEL = "filelist_cancel";
  public static final String LANG_MOREAPPS_TITLE = "moreapps_title";
  public static final String LANG_UNKNOWN = "unknown";
  public static final String LANG_CONFIRM_WINDOW_CONFIRM_BUTTON_OK = "confirm_window_button_ok";
  public static final String LANG_CONFIRM_WINDOW_CONFIRM_BUTTON_DENY = "confirm_window_button_deny";

  /**
   * @param key
   * @return String
   */
  public static String _lang(String key)
  {
    return use_Halcyon.lang(key);
  }
}

package com.jackmeng.halcyon;

import com.jackmeng.util.use_Struct.struct_Pair;

public final class const_MUTableKeys
{
  private const_MUTableKeys()
  {
  }

  /*--------------------------------------------------------------------------- /
  / A mutable constant manager is basically a manager but instead of having all /
  / of its constants being actual immutable constants, they can be reassigned.  /
  /----------------------------------------------------------------------------*/
  public static struct_Pair< String, String > lang_locale = new struct_Pair<>("en", "");
  public static struct_Pair< Integer, Integer > top_artwork_wxh = new struct_Pair<>(132, 132);
  public static boolean outstream = true;
  public static int playlist_select_icon_w_h = 16;
  public static boolean run_tcs_on_start = false;
  public static boolean title_frame_styling = false;
  public static boolean top_bg_panel_use_blur = false;
  public static boolean use_filelist_titled_border = true;
  public static boolean gui_use_debug = false;
}

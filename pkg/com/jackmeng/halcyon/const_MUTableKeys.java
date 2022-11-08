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
  public static boolean outstream = true;
  public static int playlist_select_icon_w_h = 16;
  public static boolean run_tcs_on_start = false;
}

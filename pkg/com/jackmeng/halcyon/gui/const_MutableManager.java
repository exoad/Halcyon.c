package com.jackmeng.halcyon.gui;

import com.jackmeng.util.use_Struct.struct_Pair;

public final class const_MutableManager {
  private const_MutableManager() {
  }

  /*--------------------------------------------------------------------------- /
  / A mutable constant manager is basically a manager but instead of having all /
  / of its constants being actual immutable constants, they can be reassigned.  /
  /----------------------------------------------------------------------------*/
  public static struct_Pair<String, String> lang_locale = new struct_Pair<>("en", "");
}

package com.jackmeng.sys;

import com.jackmeng.halcyon.use_Halcyon;

public abstract interface const_FetcherBuiltin
{

  /*-------------------------------------------------------------------------------------------------- /
  / This constants struct holds information regarding things that are builtin at the boilerplate level /
  /---------------------------------------------------------------------------------------------------*/

  String BUILTIN_LOCALE = "builtin" + use_Halcyon.getFileSeparator();

  /*------------------------------------------------------ /
  / The following represents locations to default PLOOGINS /
  /-------------------------------------------------------*/
  String PLOOGINS = BUILTIN_LOCALE + "ploogins" + use_Halcyon.getFileSeparator();
  String PLOOGIN_DEFAULT_LUA = PLOOGINS + "lua" + use_Halcyon.getFileSeparator();
  String PLOOGIN_LUA_VERIFIER = PLOOGIN_DEFAULT_LUA + "_halcyon_lua_verifier.lua";
}

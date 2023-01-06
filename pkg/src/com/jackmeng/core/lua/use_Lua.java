package com.jackmeng.core.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.jackmeng.core.util.has_Unsafe;

public final class use_Lua
{
  private use_Lua()
  {
  }

  @has_Unsafe(reason = "This method executes unknown Lua snippeted code which can be unsafe to execute without proper prehand checks!") public static LuaValue exec_Lua(
      String luaCode)
  {
    return get_globals().load(luaCode).call();
  }


  public static Globals get_globals()
  {
    return JsePlatform.standardGlobals();
  }

  public static Globals get_debug_globals()
  {
    return JsePlatform.debugGlobals();
  }
}

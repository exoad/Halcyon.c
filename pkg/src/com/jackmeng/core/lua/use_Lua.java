package com.jackmeng.core.lua;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.jackmeng.core.util.has_Unsafe;
import com.jackmeng.core.util.use_Struct.struct_Trio;

/**
 * Some high level and simplifications for LuaJ's awesome library.
 *
 * @author Jack Meng
 */
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

  public static Hashtable< String, ? > make_coerce_map(String[] identifiers,
      List< struct_Trio< Class< ? >, Class< ? >[], Object[] > > args)
  {
    assert identifiers.length == args.size();
    Map<String, ?> t = new HashMap<>();
    for(int i = 0; i < identifiers.length; i++)
    {
      
    }
  }

  public static LuaValue exec_coerce_l2j(String[] identifiers, String luaCode, Class< ? > type,
      Class< ? >[] constructorSignature,
      Object... args)
  {

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

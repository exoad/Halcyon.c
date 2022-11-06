package com.jackmeng.halcyon.apps;

import com.jackmeng.util.use_Struct.struct_Pair;

public interface impl_TrayApp extends impl_App
{

  public struct_Pair< String, Runnable > actions();
}

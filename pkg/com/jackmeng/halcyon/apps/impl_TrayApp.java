package com.jackmeng.halcyon.apps;

import com.jackmeng.halcyon.impl_App;
import com.jackmeng.util.use_Struct.struct_Pair;

public abstract interface impl_TrayApp
    extends impl_App
{

  public struct_Pair< String, Runnable > actions();
}

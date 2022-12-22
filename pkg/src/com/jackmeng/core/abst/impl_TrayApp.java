package com.jackmeng.core.abst;

import com.jackmeng.core.util.use_Struct.struct_Pair;

public abstract interface impl_TrayApp
    extends
    impl_App
{

  public struct_Pair< String, Runnable > actions();
}

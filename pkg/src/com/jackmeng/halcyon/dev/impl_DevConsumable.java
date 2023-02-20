package com.jackmeng.halcyon.dev;

import com.jackmeng.halcyon.core.abst.impl_Identifiable;

public interface impl_DevConsumable
    extends
    impl_Identifiable
{
  public String name();

  public void payload();
}
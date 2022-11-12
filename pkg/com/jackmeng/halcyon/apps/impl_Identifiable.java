package com.jackmeng.halcyon.apps;

public abstract interface impl_Identifiable
{
  public default String id()
  {
    return getClass().getName() + getClass().hashCode() + hashCode();
  }
}

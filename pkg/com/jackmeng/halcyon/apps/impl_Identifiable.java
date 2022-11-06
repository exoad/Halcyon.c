package com.jackmeng.halcyon.apps;

public interface impl_Identifiable
{
  public default String id()
  {
    return getClass().getName() + getClass().hashCode() + hashCode();
  }
}

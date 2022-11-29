package com.jackmeng.halcyon.abst;

public abstract interface impl_Identifiable
{
  public default String id()
  {
    return getClass().getName() + getClass().hashCode() + hashCode();
  }
}

package com.jackmeng.core.abst;

public abstract interface impl_Identifiable
{
  default String id()
  {
    return getClass().getName() + getClass().hashCode() + hashCode();
  }
}

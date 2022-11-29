package com.jackmeng.halcyon.abst;

@FunctionalInterface
public abstract interface impl_Callback< T >
{
  public T call(Object... params);
}

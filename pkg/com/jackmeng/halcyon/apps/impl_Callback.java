package com.jackmeng.halcyon.apps;

@FunctionalInterface
public abstract interface impl_Callback< T >
{
  public T call(Object... params);
}

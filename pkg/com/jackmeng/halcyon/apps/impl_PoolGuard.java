package com.jackmeng.halcyon.apps;

public interface impl_PoolGuard<T> {
  /*-------------------------------------------------------------------------------------- /
  / an optional pool guard to make sure an element can be added or manipulated in the pool /
  /---------------------------------------------------------------------------------------*/

  public boolean check(T e);
}

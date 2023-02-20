package com.jackmeng.halcyon.core.abst;

public interface impl_Task< T >
    extends Runnable
{
  @Override default void run()
  {
    // DO NOTHING
  }
}

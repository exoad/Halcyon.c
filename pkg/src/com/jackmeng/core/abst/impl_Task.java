package com.jackmeng.core.abst;

public interface impl_Task< T >
    extends Runnable
{
  @Override default void run()
  {
    // DO NOTHING
  }
}

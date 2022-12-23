package com.jackmeng.core.abst;

public interface impl_Task< T, E >
    extends Runnable
{
  public E run(T[] args);

  @Override public default void run()
  {
    // DO NOTHING
  }
}

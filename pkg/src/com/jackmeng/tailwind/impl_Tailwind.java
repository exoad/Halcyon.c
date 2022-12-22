package com.jackmeng.tailwind;

/**
 * Lightweight components and specifications for a Tailwind Clone
 *
 * @author Jack Meng
 */
public abstract interface impl_Tailwind
{
  public void play();

  public void pause();

  public void close();

  public long time_ms();

  public default void stop()
  {
    close();
  }
}

package com.jackmeng.tailwind;

import com.jackmeng.core.abst.impl_Identifiable;

public final class use_TailwindFeeder
    implements
    impl_Identifiable,
    impl_Tailwind
{
  private use_Tailwind player;

  public use_TailwindFeeder(use_Tailwind e)
  {
    this.player = e;
  }


  public use_TailwindFeeder()
  {
    this(new use_Tailwind());
  }

  public final synchronized use_Tailwind expose()
  {
    return player;
  }

  @Override
  public final String id()
  {
    return this.getClass().getCanonicalName();
  }

  @Override
  public final synchronized void play()
  {
    player.play();
  }

  @Override
  public final synchronized void pause()
  {
    player.pause();
  }

  @Override
  public final synchronized void close()
  {
    player.close();
  }

  @Override
  public final synchronized long time_ms()
  {
    return player.time_ms();
  }
}

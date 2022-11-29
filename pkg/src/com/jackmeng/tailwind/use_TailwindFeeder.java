package com.jackmeng.tailwind;

import com.jackmeng.halcyon.abst.impl_Identifiable;

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

  @Override
  public String id()
  {
    return this.getClass().getCanonicalName();
  }

  @Override
  public void play()
  {
    player.play();
  }

  @Override
  public void play(use_TailwindTrack e)
  {
    player.play(e);
  }

  @Override
  public void pause()
  {
    player.pause();
  }

  @Override
  public void close()
  {
    player.close();
  }

  @Override
  public long time_ms()
  {
    return player.time_ms();
  }
}

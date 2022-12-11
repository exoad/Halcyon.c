package com.jackmeng.tailwind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.util.use_LooseList;

public final class use_TailwindFeeder
    implements
    impl_Identifiable,
    impl_Tailwind,
    Iterable< String >
{
  public enum tailwindfeeder_FeederStates {
    TRACK_PREVIOUS, TRACK_NEXT;
  }

  private int ptr;
  private use_Tailwind player;
  private use_LooseList< String > history;
  private List< evnt_TailwindFeederStatus > stateListeners;

  public use_TailwindFeeder(use_Tailwind e)
  {
    this.player = e;
    this.history = new use_LooseList<>();
    this.ptr = 0;
    this.stateListeners = new ArrayList<>();
  }

  private void run_ping(tailwindfeeder_FeederStates e)
  {
    stateListeners.forEach(r -> r.feeder_state(e));
  }

  public void add_feeder_state_listener(evnt_TailwindFeederStatus e)
  {
    stateListeners.add(e);
  }

  public synchronized next()
  {

  }

  public synchronized void previous()
  {
    player.close();
    if (ptr > 1)
    {
      player.open(history.get(ptr - 1));
      player.play();
      ptr--;
    }
  }

  public use_TailwindFeeder()
  {
    this(new use_Tailwind());
  }

  public synchronized use_Tailwind expose()
  {
    return player;
  }

  @Override
  public String id()
  {
    return this.getClass().getCanonicalName();
  }

  @Override
  public synchronized void play()
  {
    player.play();
  }

  @Override
  public synchronized void pause()
  {
    player.pause();
  }

  @Override
  public synchronized void close()
  {
    player.close();
  }

  @Override
  public synchronized long time_ms()
  {
    return player.time_ms();
  }

  @Override
  public Iterator< String > iterator()
  {
    return history.iterator();
  }
}

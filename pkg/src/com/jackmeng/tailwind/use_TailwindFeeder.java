package com.jackmeng.tailwind;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;

import com.jackmeng.const_Core;
import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.tailwind.use_Tailwind.tailwind_Status;
import com.jackmeng.util.use_LooseList;

public final class use_TailwindFeeder
    implements
    impl_Identifiable,
    impl_Tailwind,
    evnt_TailwindStatus,
    Iterable< String >
{
  public enum tailwindfeeder_FeederStates {
    FEEDER_SHUFFLE_PLAYLIST, FEEDER_LOOP_TRACK, FEEDER_STOPLOOP_TRACK, FEEDER_STOPSHUFFLE_PLAYLIST, TRACK_PREVIOUS, TRACK_NEXT;
  }

  private int ptr;
  private boolean isShuffle, isLoop;
  private use_Tailwind player;
  private use_LooseList< String > memory_bloc;
  private Queue< use_TailwindTrack > toPlay; // FIFO
  private Set< evnt_TailwindFeederStatus > stateListeners;

  public use_TailwindFeeder(use_Tailwind e)
  {
    this.player = e;
    this.memory_bloc = new use_LooseList<>();
    this.ptr = 0;
    this.stateListeners = new HashSet<>();
    this.toPlay = new ArrayDeque<>();

    isShuffle = false;
    isLoop = false;

    this.player.add_status_listener(this);

    const_Core.schedule_secondary_task(new TimerTask() {

      @Override
      public void run()
      {
        List< Integer > toRemove = new ArrayList<>();
        for (int i = 0; i < memory_bloc.size(); i++)
        {
          File f = new File(memory_bloc.get(i));
          if (!f.exists() || !f.isFile() || !f.canRead())
            toRemove.add(i);
        }
        toRemove.forEach(x -> memory_bloc.remove(x));
      }

    }, 1000L, 3300L);
  }

  public synchronized void bump(use_TailwindTrack... tracks)
  {
    for (use_TailwindTrack e : tracks)
      toPlay.add(e);
  }

  public synchronized void bump(use_TailwindPlaylist e)
  {
    e.forEach(x -> toPlay.add(new use_TailwindTrack(x)));
  }

  public synchronized void bump_as_next(use_TailwindTrack e)
  {
    toPl
  }

  private void run_ping(tailwindfeeder_FeederStates e)
  {
    stateListeners.forEach(r -> r.feeder_state(e));
  }

  public void add_feeder_state_listener(evnt_TailwindFeederStatus e)
  {
    stateListeners.add(e);
  }

  public synchronized void next()
  {
    player.close();
    if(ptr == memory_bloc.size() - 1) // if a "PREVIOUS_TRACK" initiate was called then we are still in the location of the memory_bloc section
      player.open(memory_bloc.get(ptr+1));
    else {
      player.open()
    }
    player.play();
    ptr++;
    run_ping(tailwindfeeder_FeederStates.TRACK_NEXT);
  }

  public synchronized void previous()
  {
    player.close();
    if (ptr > 1)
    {
      player.open(memory_bloc.get(ptr - 1));
      player.play();
      ptr--;
      run_ping(tailwindfeeder_FeederStates.TRACK_PREVIOUS);
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
    return memory_bloc.iterator();
  }

  @Override
  public void tailwind_status(tailwind_Status e)
  {
    if(e == tailwind_Status.CLOSED)
    {
      if()
    }
  }
}

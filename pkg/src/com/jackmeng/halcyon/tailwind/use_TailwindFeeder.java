package com.jackmeng.halcyon.tailwind;

import java.io.File;
import java.util.*;

import com.jackmeng.halcyon.core.abst.impl_Identifiable;
import com.jackmeng.halcyon.core.util.use_LooseList;
import com.jackmeng.halcyon.tailwind.use_Tailwind.tailwind_Status;

/*---------------------------------------------------------------------------------- /
/ a feeder uses a LIFO style for its history compression model, however there are no /
/ modification values only initial write and initial read and read after             /
/-----------------------------------------------------------------------------------*/

/**
 * LIFO Write once Read Many Data Processing Class
 * targetted specifically for quick audio management on a large scale
 *
 * @author Jack Meng
 */
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
  private final use_Tailwind player;
  private final use_LooseList< String > memory_bloc;
  private final Queue< use_TailwindTrack > toPlay; // FIFO
  private final Set< evnt_TailwindFeederStatus > stateListeners;

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

    use_TailwindScheduler.submit_Task_General(new TimerTask() {

      @Override public void run()
      {
        List< Integer > toRemove = new ArrayList<>();
        for (int i = 0; i < memory_bloc.size(); i++)
        {
          File f = new File(memory_bloc.get(i));
          if (!f.exists() || !f.isFile() || !f.canRead())
            toRemove.add(i);
        }
        toRemove.forEach(memory_bloc::remove);
      }

    }, 1000L, 3300L);
  }

  public synchronized void bump(use_TailwindTrack... tracks)
  {
    Collections.addAll(toPlay, tracks);
  }

  public synchronized void bump(use_TailwindPlaylist e)
  {
    e.forEach(x -> toPlay.add(new use_TailwindTrack(x)));
  }

  public synchronized void bump_as_next(use_TailwindTrack e)
  {
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
    if (ptr == memory_bloc.size() - 1) // if a "PREVIOUS_TRACK" initiate was called then we are still in the location
                                       // of the memory_bloc section
      player.open(memory_bloc.get(ptr + 1));
    else
    {
      // player.open();
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

  @Override public String id()
  {
    return this.getClass().getCanonicalName();
  }

  @Override public synchronized void play()
  {
    player.play();
  }

  @Override public synchronized void pause()
  {
    player.pause();
  }

  @Override public synchronized void close()
  {
    player.close();
  }

  @Override public synchronized long time_ms()
  {
    return player.time_ms();
  }

  @Override public Iterator< String > iterator()
  {
    return memory_bloc.iterator();
  }

  @Override public void tailwind_status(tailwind_Status e)
  {
    if (e == tailwind_Status.CLOSED)
    {
    }
  }
}

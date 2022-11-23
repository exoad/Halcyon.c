package com.jackmeng.tailwind;

import java.util.ArrayList;
import java.util.List;

import com.jackmeng.halcyon.abst.impl_Identifiable;

public class use_Tailwind
    implements
    impl_Identifiable,
    impl_Tailwind,
    Cloneable
{
  /*----------------------------------------------------- /
  / master class for the tailwind audio processor and the /
  / "frontman" for the library                            /
  /------------------------------------------------------*/

  public enum tailwind_Status {
    PAUSED, PLAYING, CLOSED, STOPPED;
  }

  private List< evnt_TailwindStatus > statusListener;
  private use_TailwindTrack currentTrack; // AKA the starting track, can be NULL

  private final Object lock = new Object();

  public use_Tailwind(use_TailwindTrack e)
  {
    this.currentTrack = e;
    statusListener = new ArrayList<>();
  }

  public use_Tailwind()
  {
    this(null);
  }

  private void run_ping(tailwind_Status e)
  {
    statusListener.forEach(x -> x.forYou(e));
  }

  public use_TailwindTrack current_track()
  {
    return currentTrack;
  }

  public void set_curr_track(use_TailwindTrack e)
  {
    synchronized (lock)
    {
      currentTrack = e;
    }
  }

  public void add_status_listener(evnt_TailwindStatus e)
  {
    statusListener.add(e);
  }

  public void rm_status_listener(evnt_TailwindStatus e)
  {
    statusListener.remove(e);
  }

  @Override
  public void play()
  {
    if (currentTrack != null)
    {
      play(currentTrack);
    }
  }

  @Override
  public void play(use_TailwindTrack e)
  {
    run_ping(tailwind_Status.PLAYING);
  }

  @Override
  public void pause()
  {
    run_ping(tailwind_Status.PAUSED);

  }

  @Override
  public void close()
  {
    run_ping(tailwind_Status.CLOSED);
  }

  @Override
  public long time_ms()
  {
    return 0;
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}

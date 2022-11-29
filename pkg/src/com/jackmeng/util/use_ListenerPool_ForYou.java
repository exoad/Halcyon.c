package com.jackmeng.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jackmeng.halcyon.abst.impl_ForYou;

public class use_ListenerPool_ForYou< E, T extends impl_ForYou< E > >
    implements Iterable< T >
{
  private List< T > listeners;

  public use_ListenerPool_ForYou()
  {
    listeners = new ArrayList<>();
  }

  public void add_listener(T e)
  {
    listeners.add(e);
  }

  public void add_listener(T e, int priority)
  {
    assert priority <= listeners.size() - 1 && priority >= 0;
    listeners.add(priority, e);
  }

  public void add_listener_top(T e)
  {
    listeners.add(0, e);
  }

  public void rmf_listener(T e)
  {
    listeners.remove(e);
  }

  public void rmf_listener(int priority)
  {
    listeners.remove(priority);
  }

  public void run(E optionals)
  {
    listeners.forEach(x -> x.forYou(optionals));
  }

  @Override
  public Iterator< T > iterator()
  {
    return listeners.iterator();
  }
}

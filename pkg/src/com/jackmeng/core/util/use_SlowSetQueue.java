package com.jackmeng.core.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class use_SlowSetQueue< T > implements Queue< T >
{

  private final Queue< T > queue = new LinkedList<>();
  private final Set< T > set = new HashSet<>();

  @Override
  public boolean add(T t)
  {
    if (set.add(t))
      queue.add(t);
    return true;
  }

  @Override
  public boolean addAll(Collection< ? extends T > arg0)
  {
    boolean ret = false;
    for (T t : arg0)
      if (set.add(t))
      {
        queue.add(t);
        ret = true;
      }
    return ret;
  }

  @Override
  public T remove() throws NoSuchElementException
  {
    T ret = queue.remove();
    set.remove(ret);
    return ret;
  }

  @Override
  public boolean remove(Object arg0)
  {
    boolean ret = queue.remove(arg0);
    set.remove(arg0);
    return ret;
  }

  @Override
  public boolean removeAll(Collection< ? > arg0)
  {
    boolean ret = queue.removeAll(arg0);
    set.removeAll(arg0);
    return ret;
  }

  @Override
  public void clear()
  {
    set.clear();
    queue.clear();
  }

  @Override
  public boolean contains(Object arg0)
  {
    return set.contains(arg0);
  }

  @Override
  public boolean containsAll(Collection< ? > arg0)
  {
    return set.containsAll(arg0);
  }

  @Override
  public boolean isEmpty()
  {
    return set.isEmpty();
  }

  @Override
  public Iterator< T > iterator()
  {
    return queue.iterator();
  }

  @Override
  public boolean retainAll(Collection< ? > arg0)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size()
  {
    return queue.size();
  }

  @Override
  public Object[] toArray()
  {
    return queue.toArray();
  }

  @Override
  @SuppressWarnings("hiding")
  public < T > T[] toArray(T[] arg0)
  {
    return queue.toArray(arg0);
  }

  @Override
  public T element()
  {
    return queue.element();
  }

  @Override
  public boolean offer(T e)
  {
    return queue.offer(e);
  }

  @Override
  public T peek()
  {
    return queue.peek();
  }

  @Override
  public T poll()
  {
    return queue.poll();
  }

}
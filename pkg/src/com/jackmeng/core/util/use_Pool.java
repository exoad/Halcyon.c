package com.jackmeng.core.util;

import com.jackmeng.core.abst.impl_Guard;
import com.jackmeng.core.abst.impl_HalcyonRefreshable;
import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.core.util.use_Struct.struct_Pair;

import java.util.*;

public class use_Pool< T extends impl_Identifiable >
    implements
    Iterable< T >
{
  private Map< String, T > poolObjects;
  private List< impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > > refreshables;
  private impl_Guard< impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > > guards;
  private impl_Guard< T > guards2;

  public enum pool_RefreshStatus {
    ADDED, REMOVED;
  }

  public use_Pool()
  {
    refreshables = new ArrayList<>();
    poolObjects = new HashMap<>();
    guards = new impl_Guard< impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > >() {
      /*-------------------------------------------------------------------- /
      / by default the pool will always return true for all entered elements /
      /---------------------------------------------------------------------*/
      @Override
      public boolean check(impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > e)
      {
        return true;
      }

    };
    guards2 = new impl_Guard<>() {

      @Override
      public boolean check(T e)
      {
        return true;
      }

    };
  }

  /**
   * @param content
   */
  public void removeRefreshable(impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > content)
  {
    refreshables.remove(content);
  }

  /**
   * @param content
   */
  public void addRefreshable(impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > content)
  {
    if (guards != null && guards.check(content))
    {
      refreshables.add(content);
      content.dry_refresh();
    }
  }

  public void setRefreshableGuard(
      impl_Guard< impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< T > > > > e)
  {
    this.guards = e;
  }

  public void setGuard(impl_Guard< T > e)
  {
    this.guards2 = e;
  }

  /**
   * @param object
   */
  public boolean addPoolObject(T object)
  {
    if (guards2.check(object))
    {
      poolObjects.put(object.id(), object);
      notifyRefreshers(false, const_GeneralStatus.ADDITION,
          new struct_Pair<>(Optional.of(object.id()), Optional.of(object)));
      return true;
    }
    else
      return false;

  }

  /**
   * @param id
   */
  public void removePoolObject_ID(String id)
  {
    poolObjects.remove(id);
    notifyRefreshers(false, const_GeneralStatus.DELETION, new struct_Pair<>(Optional.of(id), Optional.empty()));
  }

  public void removePoolObj(T e)
  {
    poolObjects.remove(e.id());
    notifyRefreshers(false, const_GeneralStatus.DELETION, new struct_Pair<>(Optional.of(e.id()), Optional.of(e)));
  }

  public T objOf_T(Class< ? extends T > e)
  {
    for (String r : objs())
      if (poolObjects.get(r).getClass().getCanonicalName().equals(e.getCanonicalName()))
        return poolObjects.get(r);
    return null;
  }

  public boolean hasObj(T e)
  {
    return get(e.id()) != null;
  }

  public boolean contains_objOf_T(Class< ? extends T > e)
  {
    for (String r : objs())
      if (poolObjects.get(r).getClass().getCanonicalName().equals(e.getCanonicalName()))
        return true;
    return false;
  }

  /**
   * @return Set:String
   */
  public Set< String > objs()
  {
    return poolObjects.keySet();
  }

  /**
   * @return Map:[String, T]
   */
  public Map< String, T > expose()
  {
    return poolObjects;
  }

  /**
   * @param id
   * @return T
   */
  public T get(String id)
  {
    return poolObjects.get(id);
  }

  /**
   * @param removed
   */
  private void notifyRefreshers(boolean dry, const_GeneralStatus type,
      struct_Pair< Optional< String >, Optional< T > > removed)
  {
    use_Task.run_Snb_1(dry ? () -> refreshables.forEach(impl_HalcyonRefreshable::dry_refresh)
        : () -> refreshables.forEach(x -> x.refresh(type, removed)));
  }

  /**
   * @return String
   */
  public String toString()
  {
    return "OBJECT_POOL{\n" + refreshables.toString() + "\n" + poolObjects.toString() + "\n}";
  }

  @Override
  public Iterator< T > iterator()
  {
    return poolObjects.values().iterator();
  }
}

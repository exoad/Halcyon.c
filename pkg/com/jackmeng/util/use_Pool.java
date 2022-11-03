package com.jackmeng.util;

import com.jackmeng.halcyon.apps.impl_HalcyonRefreshable;
import com.jackmeng.halcyon.apps.impl_Identifiable;
import com.jackmeng.halcyon.apps.impl_PoolGuard;
import com.jackmeng.sys.use_Task;
import com.jackmeng.util.use_Struct.struct_Pair;

import java.util.*;

public class use_Pool<T extends impl_Identifiable>
    implements impl_HalcyonRefreshable<impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>>> {
  private Map<String, T> poolObjects;
  private List<impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>>> refreshables;
  private impl_PoolGuard<impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>>> guards;

  public use_Pool() {
    refreshables = new ArrayList<>();
    poolObjects = new HashMap<>();
    guards = new impl_PoolGuard<impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>>>() {
      /*-------------------------------------------------------------------- /
      / by default the pool will always return true for all entered elements /
      /---------------------------------------------------------------------*/
      @Override
      public boolean check(impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>> e) {
        return true;
      }

    };
  }

  /**
   * @param content
   */
  public void removeRefreshable(impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>> content) {
    refreshables.remove(content);
  }

  /**
   * @param content
   */
  public void addRefreshable(impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>> content) {
    if (guards != null && guards.check(content)) {
      refreshables.add(content);
      content.dry_refresh();
    }
  }

  public void setGuard(impl_PoolGuard<impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>>> e) {
    this.guards = e;
  }

  /**
   * @param object
   */
  public void addPoolObject(T object) {
    poolObjects.put(object.id(), object);
    notifyRefreshers(false, new struct_Pair<>(Optional.of(object.id()), Optional.of(object)));
  }

  /**
   * @param id
   */
  public void removePoolObject_ID(String id) {
    poolObjects.remove(id);
    notifyRefreshers(false, new struct_Pair<>(Optional.of(id), Optional.empty()));
  }

  public T objOf_T(Class<? extends T> e) {
    for (String r : objs()) {
      if (poolObjects.get(r).getClass().getCanonicalName().equals(e.getCanonicalName())) {
        return poolObjects.get(r);
      }
    }
    return null;
  }

  public boolean contains_objOf_T(Class<? extends T> e) {
    for (String r : objs()) {
      if (poolObjects.get(r).getClass().getCanonicalName().equals(e.getCanonicalName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return Set:String
   */
  public Set<String> objs() {
    return poolObjects.keySet();
  }

  /**
   * @return Map:[String, T]
   */
  public Map<String, T> expose() {
    return poolObjects;
  }

  /**
   * @param id
   * @return T
   */
  public T get(String id) {
    return poolObjects.get(id);
  }

  /**
   * @param removed
   */
  public void notifyRefreshers(boolean dry, struct_Pair<Optional<String>, Optional<T>> removed) {
    if (dry)
      use_Task.run_Snb_1(() -> refreshables.forEach(impl_HalcyonRefreshable::dry_refresh));
    else
      use_Task.run_Snb_1(() -> refreshables.forEach(x -> x.refresh(removed)));
  }

  /**
   * @return String
   */
  public String toString() {
    return "OBJECT_POOL{\n" + refreshables.toString() + "\n" + poolObjects.toString() + "\n}";
  }

  /**
   * @param refreshed
   */
  @Override
  public void refresh(impl_HalcyonRefreshable<struct_Pair<Optional<String>, Optional<T>>> refreshed) {
  }

  @Override
  public void dry_refresh() {

  }
}

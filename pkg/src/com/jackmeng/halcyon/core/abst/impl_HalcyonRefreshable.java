package com.jackmeng.halcyon.core.abst;

import com.jackmeng.halcyon.core.util.const_GeneralStatus;

public abstract interface impl_HalcyonRefreshable< T >
{

  void refresh(const_GeneralStatus classification, T refreshed);

  void dry_refresh();
}

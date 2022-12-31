package com.jackmeng.core.abst;

import com.jackmeng.core.util.const_GeneralStatus;

public abstract interface impl_HalcyonRefreshable< T >
{

  void refresh(const_GeneralStatus classification, T refreshed);

  void dry_refresh();
}

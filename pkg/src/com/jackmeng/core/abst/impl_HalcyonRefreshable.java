package com.jackmeng.core.abst;

import com.jackmeng.core.util.const_GeneralStatus;

public abstract interface impl_HalcyonRefreshable< T >
{

  public void refresh(const_GeneralStatus classification, T refreshed);

  public void dry_refresh();
}

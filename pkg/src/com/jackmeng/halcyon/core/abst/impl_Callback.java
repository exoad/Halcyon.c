package com.jackmeng.halcyon.core.abst;

@FunctionalInterface public abstract interface impl_Callback< T >
{
  T call(Object... params);

  abstract interface callback_Specific< E >
  {
    E call(E params);
  }
}

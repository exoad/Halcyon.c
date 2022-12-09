package com.jackmeng.core.abst;

@FunctionalInterface
public abstract interface impl_Callback< T >
{
  public T call(Object... params);

  public abstract interface callback_Specific< E >
  {
    public E call(E params);
  }
}

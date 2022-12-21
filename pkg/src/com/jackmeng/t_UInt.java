package com.jackmeng;

public final class t_UInt extends Number implements Comparable< t_UInt >
{

  private final int val;

  private t_UInt(int v)
  {
    val = v & 0xFFFFFFFF;
  }

  public static final t_UInt ZERO = new t_UInt(0);
  public static final t_UInt ONE = new t_UInt(1);
  public static final t_UInt MAX_VALUE = new t_UInt(-1);

  public static t_UInt make(long t)
  {
    return new t_UInt((int) t);
  }

  @Override
  public int compareTo(t_UInt o)
  {
    assert o != null;
    return (o.val ^ Integer.MAX_VALUE) < (this.val ^ Integer.MAX_VALUE) ? -1
        : (o.val ^ Integer.MAX_VALUE) > (this.val ^ Integer.MAX_VALUE) ? 1 : 0;
  }

  @Override
  public boolean equals(Object e)
  {
    return e instanceof t_UInt ? val == ((t_UInt) e).val : false;
  }

  @Override
  public int intValue()
  {
    return val;
  }

  @Override
  public long longValue()
  {
    return val & 0xFFFFFFFFL;
  }

  @Override
  public float floatValue()
  {
    return longValue();
  }

  @Override
  public double doubleValue()
  {
    return longValue();
  }

}

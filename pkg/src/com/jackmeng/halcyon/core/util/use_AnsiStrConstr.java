package com.jackmeng.halcyon.core.util;

public class use_AnsiStrConstr
{
  private final Object[] payload;
  private final use_AnsiColors[] colors;

  public use_AnsiStrConstr(use_AnsiColors[] start, Object[] payload)
  {
    this.colors = start;
    this.payload = payload;
  }

  public use_AnsiStrConstr(use_AnsiColors color, Object payload)
  {
    this.colors = new use_AnsiColors[] { color };
    this.payload = new Object[] { payload };
  }

  /**
   * @return String
   */
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (use_AnsiColors e : colors)
      sb.append(e.color());
    for (Object r : payload)
      sb.append(r);
    sb.append(use_AnsiColors.RESET.color());
    return sb.toString();
  }
}

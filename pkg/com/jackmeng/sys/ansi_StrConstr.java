package com.jackmeng.sys;

public class ansi_StrConstr
{
  private final Object[] payload;
  private final ansi_Colors[] colors;

  public ansi_StrConstr(ansi_Colors[] start, Object[] payload)
  {
    this.colors = start;
    this.payload = payload;
  }

  public ansi_StrConstr(ansi_Colors color, Object payload)
  {
    this.colors = new ansi_Colors[] { color };
    this.payload = new Object[] { payload };
  }

  /**
   * @return String
   */
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (ansi_Colors e : colors)
    {
      sb.append(e.color());
    }
    for (Object r : payload)
    {
      sb.append(r);
    }
    sb.append(ansi_Colors.RESET.color());
    return sb.toString();
  }
}

package com.jackmeng.core.util;

import java.math.BigInteger;
import java.util.Random;

public final class use_Math
{
  private use_Math()
  {
  }

  /*---------------------------------- /
  / 0_1 Specifiers indicate BigInteger /
  /-----------------------------------*/

  public static boolean is_prime0_1(BigInteger n, int k) // TODO
  {
    if (n.compareTo(BigInteger.TWO) < 0)
      return false;
    if (n.equals(BigInteger.TWO))
      return true;
    if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO))
      return false;

    BigInteger d = n.subtract(BigInteger.ONE);
    int s = 0;
    while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO))
    {
      s++;
      d = d.divide(BigInteger.TWO);
    }

    for (int i = 0; i < k; i++)
    {
      BigInteger o = rnd0_1(BigInteger.TWO, n.subtract(BigInteger.ONE));
      BigInteger x = o.modPow(d, n);
      if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
      {
        for (int r = 0; r < s; r++)
        {
          x = x.modPow(BigInteger.TWO, n);
          if (x.equals(BigInteger.ONE))
            return false;
          if (x.equals(n.subtract(BigInteger.ONE)))
            break;
        }
        if (!x.equals(n.subtract(BigInteger.ONE)))
          return false;
      }

    }
    return true;

  }

  public static BigInteger rnd0_1(BigInteger min, BigInteger max)
  {
    BigInteger range = max.subtract(min).add(BigInteger.ONE);
    int bits = range.bitLength();
    BigInteger res;
    do
    {
      res = new BigInteger(bits, RNG_01);
    }
    while (res.compareTo(range) >= 0);
    return res.add(min);
  }

  private static final Random RNG_01 = new Random(System.currentTimeMillis());
}

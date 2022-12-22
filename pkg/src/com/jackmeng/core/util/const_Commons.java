package com.jackmeng.core.util;

public interface const_Commons
{
  int EOF = -1;

  byte[] UTF8_BOM = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
  byte[] UTF16_LE_BOM = { (byte) 0xFF, (byte) 0xFE };
  byte[] UTF16_BE_BOM = { (byte) 0xFE, (byte) 0xFF };
}

package com.jackmeng;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import com.jackmeng.halcyon.Halcyon;

final class __JackMeng
{
  static Reference< __JackMeng > REF = new SoftReference<>(new __JackMeng());

  static void $$routine_method_no__extcall(Runnable follow_through)
  {
    System.out.println("==JVM_Information==\n\t" + System.getProperty("java.vendor") + "\n\t"
        + System.getProperty("os.specification.version") + " -> " + System.getProperty("java.specification.vendor")
        + "\n\t" + System.getProperty("java.runtime.name") + " -> " + System.getProperty("java.runtime.version")
        + "\n\n==Programs_Args==\n\t" + System.identityHashCode(REF.get()) + "\n\t" + System.getProperty("os.arch")
        + "\n\t" + System.getProperty("os.name") + "\n\tLine_Sep.:" + System.lineSeparator());

    /*--------------------------------------- /
    / Runtime.getRuntime().runFinalization(); /
    /----------------------------------------*/

    follow_through.run();
  }

  public static void main(String... args)
  {
    $$routine_method_no__extcall(() -> {
      try
      {
        Halcyon.main();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    });
  }
}

package com.test;

import com.jackmeng.halcyon.apps.impl_Callback;
import com.jackmeng.sys.ansi_Colors;
import com.jackmeng.sys.ansi_StrConstr;

public class TestCase implements Runnable {
  private String name;
  private impl_Callback<?> action;
  private Object expects;

  public TestCase(String name, impl_Callback<?> action) {
    this.name = name;
    this.action = action;
  }

  public TestCase(String name, impl_Callback<?> action, Object expect) {
    this(name, action);
    expects = expect;
  }

  @Override
  public void run() {
    System.out.println("Testcase: " + name + "\n" + action.hashCode());
    long s = Runtime.getRuntime().maxMemory()
        - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024,
        l = System.currentTimeMillis();
    Object returns = null;
    try {
      returns = action.call();
    } catch (Exception e) {
      System.out.println(new ansi_StrConstr(new ansi_Colors[] { ansi_Colors.GREEN_BG, ansi_Colors.BLACK_TXT },
                    new Object[] { "FAILED ON EXCEPTIOn" }));
      e.printStackTrace();
      return;
    }
    long t = System.currentTimeMillis() - l;
    System.out.println("Finished in: " + t + "ms\nMemory used: "
        + (Math.abs((Runtime.getRuntime().maxMemory()
            - ((double) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024
                / 1024)
            - s))
        + " Mb"
        + (expects == null ? "\nCase Status: " + new ansi_StrConstr(
            new ansi_Colors[] { ansi_Colors.BLUE_BG, ansi_Colors.BLACK_TXT },
            new Object[] { "NONE" })
            : "\nCase status: " + ((returns.equals(expects)
                ? new ansi_StrConstr(new ansi_Colors[] { ansi_Colors.GREEN_BG, ansi_Colors.BLACK_TXT },
                    new Object[] { "PASSED" })
                : new ansi_StrConstr(new ansi_Colors[] { ansi_Colors.RED_BG, ansi_Colors.BLACK_TXT },
                    new Object[] { "FAILED" }))
                + "\nExpected: " + expects + "\nReturned: " + returns)));
  }
}

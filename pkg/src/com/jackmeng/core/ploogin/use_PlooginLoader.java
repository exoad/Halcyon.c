package com.jackmeng.core.ploogin;

import java.util.jar.JarFile;

public class use_PlooginLoader
    extends ClassLoader
{
  public void require(String file) {}
  public void require(JarFile file) {}
  public void shutdown() {}
  public void start() {}
}

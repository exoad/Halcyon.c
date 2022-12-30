package com.jackmeng.core.ploogin;

import java.util.jar.JarFile;

public class use_PlooginLoader
    extends ClassLoader
    implements
    Runnable
{

  @Override
  public void run() // boots all builtin plugins
  {
    
  }
  public void require(String file) {}
  public void require(JarFile file) {}
  public void shutdown() {}
  public void start() {}
}

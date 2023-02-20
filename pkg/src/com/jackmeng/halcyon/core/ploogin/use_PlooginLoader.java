package com.jackmeng.halcyon.core.ploogin;

import java.util.jar.JarFile;

import com.jackmeng.halcyon.core.util.pstream;

public class use_PlooginLoader
    extends ClassLoader
    implements
    Runnable
{

  @Override public void run() // boots all builtin plugins
  {
    pstream.log.warn("Loading all builtin ploogins... Hang tight!");
  }

  public void require(String file)
  {
  }

  public void require(JarFile file)
  {
  }

  public void shutdown()
  {
  }

  public void start()
  {
  }
}

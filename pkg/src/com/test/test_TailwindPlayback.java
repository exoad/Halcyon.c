package com.test;

import java.io.File;

import com.jackmeng.Halcyon;
import com.jackmeng.tailwind.use_Tailwind;

public class test_TailwindPlayback {
  public static void main(String[] args)
  {
    Halcyon.__LINK__();
    use_Tailwind t = new use_Tailwind();
    t.open(new File("/home/jackm/Code/halcyon-gui-overhaul/pkg/src/com/test/mazie - dumb dumb - sped up.mp3"));
    t.play();
  }

  
}

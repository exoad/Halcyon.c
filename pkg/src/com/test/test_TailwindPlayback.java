package com.test;

import java.awt.Dimension;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.jackmeng.halcyon.Halcyon;
import com.jackmeng.halcyon.tailwind.use_Tailwind;
import com.jackmeng.halcyon.tailwind.use_Tailwind.tailwind_Status;

public class test_TailwindPlayback
{
  public static void main(String[] args)
  {
    Halcyon.__LINK__();
    use_Tailwind t = new use_Tailwind();
    t.open(new File("/home/jackm/Code/halcyon-gui-overhaul/pkg/src/com/test/piano2.wav"));

    AtomicBoolean playing = new AtomicBoolean(false);
    JFrame f = new JFrame();
    f.setPreferredSize(new Dimension(50, 50));
    JButton b = new JButton();
    b.setPreferredSize(new Dimension(50, 50));
    b.setText("Play");
    b.addActionListener(x -> {
      if (!playing.get())
        t.resume();
      else t.pause();
      playing.set(!playing.get());
      b.setText(playing.get() ? "Pause" : "Play");
    });

    t.add_status_listener(x -> {
      if (x == tailwind_Status.EOF)
      {
        t.open(new File("/home/jackm/Code/halcyon-gui-overhaul/pkg/src/com/test/piano2.wav"));
        b.setText("Play");
        playing.set(false);
      }
    });

    f.add(b);

    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

}

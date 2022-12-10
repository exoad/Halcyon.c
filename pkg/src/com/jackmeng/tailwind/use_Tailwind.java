package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.jackmeng.const_Global;
import com.jackmeng.core.abst.impl_ForYou;
import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.core.abst.use_MastaTemp;
import com.jackmeng.sys.pstream;
import com.jackmeng.util.const_Commons;
import com.jackmeng.util.use_Struct.struct_Pair;

public class use_Tailwind
    implements
    impl_Identifiable,
    impl_Tailwind,
    Cloneable,
    Serializable
{
  /*----------------------------------------------------- /
  / master class for the tailwind audio processor and the /
  / "frontman" for the library                            /
  /------------------------------------------------------*/

  public enum tailwind_Status {
    PAUSED, PLAYING, CLOSED, STOPPED, ARMED, SEEK, FAILED_SEEK, FAILED_PLAY, FAIL_CLOSED, FAIL_STOPPING, FAIL_ARMING, FAIL_PAUSING, FAIL_UNKNOWN;
  }

  private transient List< evnt_TailwindStatus > statusListener;
  private transient impl_ForYou< struct_Pair< tailwind_Status, Exception > > error_callback;
  private use_TailwindTrack currentTrack; // AKA the starting track, can be NULL
  private transient AudioInputStream ais;
  private transient AudioFormat af;
  private transient SourceDataLine sourceLine;
  private transient Thread bruh = new Thread();
  private transient tailwind_Status compareStatus = null;
  private volatile boolean isPlaying = false;
  private volatile boolean isPaused = false;
  private AtomicBoolean isClosed = new AtomicBoolean(true);
  private volatile long pauseTime = 0;
  private volatile long resumeTime = 0;
  private volatile long totalPausedTime = 0;
  private volatile int currentFrame = 0;

  public use_Tailwind(use_TailwindTrack e, impl_ForYou< struct_Pair< tailwind_Status, Exception > > errorCallback)
  {
    this.currentTrack = e;
    this.error_callback = errorCallback;
    statusListener = new ArrayList<>();
    add_status_listener(x -> {
      pstream.log.warn("TAILWIND_PLAYER#" + this.hashCode() + ": IS_" + x.name());
      compareStatus = x.name().startsWith("FAILED") ? null : calculate_status();
    });
    const_Global.schedule_secondary_task(new TimerTask() {

      @Override
      public void run()
      {
        if (compareStatus != null)
        {
          compareStatus = !compareStatus.equals(calculate_status()) ? calculate_status() : compareStatus;
          if (compareStatus.equals(calculate_status()))
            run_ping(compareStatus);
        }
        else compareStatus = calculate_status();
      }

    }, 1000L, 4500L);
  }

  public use_Tailwind(use_TailwindTrack e)
  {
    this(e, use_MastaTemp::doNothing);
  }

  public use_Tailwind()
  {
    this(null);
  }

  private void run_ping(tailwind_Status e)
  {
    statusListener.forEach(x -> x.forYou(e));
  }

  public use_TailwindTrack current_track()
  {
    return currentTrack;
  }

  public void add_status_listener(evnt_TailwindStatus e)
  {
    statusListener.add(e);
  }

  public void rm_status_listener(evnt_TailwindStatus e)
  {
    statusListener.remove(e);
  }

  public synchronized void open(File e)
  {
    open(new use_TailwindTrack(e));
  }

  public synchronized void open(String filePath)
  {
    open(new File(filePath));
  }

  public tailwind_Status calculate_status()
  {
    return (isPlaying && !isPaused) || sourceLine.isActive() ? tailwind_Status.PLAYING
        : !isPlaying && isPaused ? tailwind_Status.PAUSED
            : sourceLine == null || !sourceLine.isOpen() ? tailwind_Status.CLOSED
                : sourceLine.isOpen() ? tailwind_Status.ARMED : compareStatus;
  }

  public final synchronized void open(use_TailwindTrack e)
  {
    if (!isClosed.get())
      close();
    isClosed.set(!isClosed.get());
    run_ping(tailwind_Status.ARMED);
  }

  @Override
  public final synchronized void play() // acts like a resume() but can play the current loaded
  {
    if (currentTrack != null && !sourceLine.isActive())
      play(currentTrack);
    else if (currentTrack != null && sourceLine.isActive())
      isPaused = false;
  }

  @Override
  public final synchronized void play(use_TailwindTrack e)
  {
    if (currentTrack.playable())
    {
      bruh = new Thread(() -> {
        try
        {
          ais = AudioSystem.getAudioInputStream(e.getContentFile());
          af = ais.getFormat();
          DataLine.Info inf = new DataLine.Info(SourceDataLine.class, af);
          sourceLine = (SourceDataLine) AudioSystem.getLine(inf);
          sourceLine.open(af);

          int frame_size_bytes = af.getFrameSize(), buffer_length_frames = sourceLine.getBufferSize() / 8,
              buffer_length_bytes = buffer_length_frames * frame_size_bytes;
          byte[] buffer = new byte[buffer_length_bytes];
          sourceLine.start();
          isPlaying = true;
          isPaused = false;
          while (isPlaying && ais.available() > 0)
          {
            int bytes_read = ais.read(buffer);
            if (isPaused)
            {
              pauseTime = System.currentTimeMillis();
              sourceLine.stop();
              while (isPaused)
                Thread.yield();
              resumeTime = System.currentTimeMillis();
              totalPausedTime += (resumeTime - pauseTime);
              sourceLine.start();
            }
            if (bytes_read == const_Commons.EOF)
              break; // EOF
            sourceLine.write(buffer, 0, bytes_read);
            currentFrame += bytes_read / frame_size_bytes;
          }
          isPlaying = false;
          sourceLine.drain();
          sourceLine.close();
          ais.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exception)
        {
          error_callback.forYou(new struct_Pair<>(tailwind_Status.FAILED_PLAY, exception));
        }
      });

      bruh.start();

      run_ping(tailwind_Status.PLAYING);
    }
  }

  @Override
  public final synchronized void pause()
  {
    if (currentTrack.playable())
    {
      isPaused = true;
      run_ping(tailwind_Status.PAUSED);
    }
  }

  @Override
  public final synchronized void stop()
  {
    isPlaying = false;
  }

  @Override
  public final synchronized void close()
  {
    if (sourceLine != null && sourceLine.isOpen())
    {
      sourceLine.drain();
      sourceLine.close();
    }
    if (isPlaying || !isPaused)
      isPlaying = false;
    run_ping(tailwind_Status.CLOSED);
  }

  public final synchronized void seekTo(long milliseconds) // skips to an absolute position in the file
  {
    milliseconds = milliseconds < 0 ? 0 : milliseconds > time_ms() ? time_ms() : milliseconds;
    int n = (int) ((milliseconds / 1000D) * af.getFrameRate()), frame_size_bytes = af.getFrameSize(),
        bytes_skipped = (n - currentFrame) * frame_size_bytes;
    try
    {
      ais.skip(bytes_skipped);
      currentFrame = n;
      if (isPaused)
        totalPausedTime = milliseconds;
    } catch (IOException e)
    {
      error_callback.forYou(new struct_Pair<>(tailwind_Status.FAILED_SEEK, e));
    }
    run_ping(tailwind_Status.SEEK);
  }

  public final synchronized void seekFrom(long milliseconds) // skips from the current position milliseconds
  {
    long n = (long) (currentFrame / af.getFrameRate() * 1000 + milliseconds);
    n = n < 0 ? 0 : n > time_ms() ? time_ms() : n;
    int a = (int) (n / 1000D * af.getFrameRate()), frame_size_bytes = af.getFrameSize(),
        bytes_skipped = (a - currentFrame) * frame_size_bytes;
    try
    {
      ais.skip(bytes_skipped);
      currentFrame = a;

    } catch (IOException e)
    {
      error_callback.forYou(new struct_Pair<>(tailwind_Status.FAILED_SEEK, e));
    }
    run_ping(tailwind_Status.SEEK);
  }

  @Override
  public final synchronized long time_ms() // returns the length of the track in milliseconds
  {
    return (long) ((ais.getFrameLength() * 1000L) / af.getFrameRate());
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}

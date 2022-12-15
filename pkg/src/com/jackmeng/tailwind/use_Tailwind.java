package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jackmeng.core.abst.impl_ForYou;
import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.core.abst.use_MastaTemp;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Task;
import com.jackmeng.tailwind.use_TailwindTrack.tailwindtrack_Tags;
import com.jackmeng.util.const_Commons;
import com.jackmeng.util.use_Struct.struct_Pair;

/**
 * This is the main class that represents a Tailwind Player that
 * is not natively implement
 *
 * @author Jack Meng
 */
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
    PAUSED, PLAYING, CLOSED, STOPPED, ARMED, SEEK, FAILED_SEEK, FAILED_PLAY, FAIL_CLOSED, FAIL_STOPPING, FAIL_ARMING, FAIL_PAUSING, FAIL_UNKNOWN, EOF;
  }

  private transient List< evnt_TailwindStatus > statusListener;
  private transient impl_ForYou< struct_Pair< tailwind_Status, Exception > > error_callback;
  private use_TailwindTrack currentTrack; // AKA the starting track, can be NULL
  private transient AudioInputStream ais;
  private transient AudioFormat af;
  private transient SourceDataLine sourceLine;
  private transient Thread bernard;
  private transient tailwind_Status compareStatus = null; // a default ultimas status is "null"
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
      compareStatus = x;
      // compareStatus = x.name().startsWith("FAILED") ? null : calculate_status();
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (sourceLine != null)
        sourceLine.close();
    })); // safe resource juggling
    /*----------------------------------------------------------------------------------------------------- /
    / const_Global.schedule_secondary_task(new TimerTask() {                                                /
    /                                                                                                       /
    /   @Override                                                                                           /
    /   public void run()                                                                                   /
    /   {                                                                                                   /
    /     if (compareStatus != null)                                                                        /
    /     {                                                                                                 /
    /       compareStatus = !compareStatus.equals(calculate_status()) ? calculate_status() : compareStatus; /
    /       if (compareStatus.equals(calculate_status()))                                                   /
    /         run_ping(compareStatus);                                                                      /
    /     }                                                                                                 /
    /     else compareStatus = calculate_status();                                                          /
    /   }                                                                                                   /
    /                                                                                                       /
    / }, 1000L, 4500L);                                                                                     /
    /------------------------------------------------------------------------------------------------------*/
  }

  public use_Tailwind(use_TailwindTrack e)
  {
    this(e, use_MastaTemp::doNothing);
  }

  public use_Tailwind()
  {
    this((use_TailwindTrack) null);
  }

  public use_Tailwind(impl_ForYou< struct_Pair< tailwind_Status, Exception > > e)
  {

  }

  private void run_ping(tailwind_Status e)
  {
    use_Task.async_N2(() -> statusListener.forEach(x -> x.tailwind_status(e)));
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
    return (isPlaying && !isPaused) || (sourceLine != null && sourceLine.isActive()) ? tailwind_Status.PLAYING
        : !isPlaying && isPaused ? tailwind_Status.PAUSED
            : sourceLine != null && !sourceLine.isOpen() ? tailwind_Status.CLOSED
                : sourceLine != null && sourceLine.isOpen() ? tailwind_Status.ARMED : compareStatus;
  }

  public static AudioInputStream get_audio_inputstream(URL locale)
  {
    try
    {
      AudioInputStream ais;
      use_TailwindContentType target = use_TailwindContentType.format_by_name(locale.toExternalForm());
      if (target == use_TailwindContentType.MP3)
      {
        ais = AudioSystem.getAudioInputStream(locale);
        AudioFormat base = ais.getFormat();
        AudioFormat decode = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            base.getSampleRate(),
            16,
            base.getChannels(),
            base.getChannels() * 2,
            base.getSampleRate(),
            false);
        ais = AudioSystem.getAudioInputStream(decode, ais);
        return ais;
      }
      else if (target.equals(use_TailwindContentType.WAV) || target.equals(use_TailwindContentType.AIFF)
          || target.equals(use_TailwindContentType.AIFC) || target.equals(use_TailwindContentType.AU))
      {
        ais = AudioSystem.getAudioInputStream(locale);
        return ais;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public final synchronized void open(use_TailwindTrack e)
  {
    if (!isClosed.get())
      close();
    isClosed.set(!isClosed.get());
    this.currentTrack = e;
    run_ping(tailwind_Status.ARMED);
  }

  @Override
  public final synchronized void play()
  {
    if (currentTrack != null && currentTrack.playable())
    {
      bernard = new Thread(() -> {
        try
        {
          ais = get_audio_inputstream(currentTrack.getContentFile().toURI().toURL());
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
          int bytes_read;
          while (isPlaying && !isPaused && sourceLine.isOpen()
              && (bytes_read = ais.read(buffer)) != -1)
          {
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
              break;
            sourceLine.write(buffer, 0, bytes_read);
            currentFrame += bytes_read / frame_size_bytes;
          }
          isPlaying = false;
          masta_drainage();
          ais.close();
          run_ping(tailwind_Status.EOF);
        } catch (IOException | LineUnavailableException exception)
        {
          error_callback.forYou(new struct_Pair<>(tailwind_Status.FAILED_PLAY, exception));
        }
      });
      bernard.setPriority(Thread.MAX_PRIORITY);
      bernard.start();
      run_ping(tailwind_Status.PLAYING);
    }
  }

  private void masta_drainage() // template method used for play()
  {
    if (sourceLine != null)
    {
      sourceLine.drain();
      sourceLine.close();
    }
  }

  public final synchronized void resume()
  {
    if (currentTrack != null && currentTrack.playable())
    {
      if (!isPlaying)
      {
        play();
        pstream.log.warn("PLAY_NEVER_PLAYED");
      }
      else if (isPlaying)
      {
        pstream.log.warn("RESUMING");
        isPaused = false;
        run_ping(tailwind_Status.PLAYING); // or should i make a separate enum for Resumed???
      }
    }

  }

  @Override
  public final synchronized void pause()
  {
    if (currentTrack != null && currentTrack.playable())
    {
      isPaused = true;
      run_ping(tailwind_Status.PAUSED);
    }
  }

  @Override
  public final synchronized void stop()
  {
    if (currentTrack != null && currentTrack.playable())
      isPlaying = false;
  }

  @Override
  public final synchronized void close()
  {
    if (currentTrack != null && currentTrack.playable())
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
  }

  public final synchronized void seekTo(long milliseconds) // skips to an absolute position in the file
  {
    if (currentTrack != null && currentTrack.playable())
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
  }

  public final synchronized void seekFrom(long milliseconds) // skips from the current position milliseconds
  {
    if (currentTrack != null && currentTrack.playable())
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
  }

  @Override
  public final synchronized long time_ms() // returns the length of the track in milliseconds
  {
    return currentTrack != null && currentTrack.playable()
        ? (long) ((ais.getFrameLength() * 1000L) / af.getFrameRate())
        : ((int) currentTrack.get(tailwindtrack_Tags.MEDIA_DURATION)) * 1000L;
  }

  public final synchronized long current_pos_ms()
  {
    long cpos = (long) ((currentFrame / af.getFrameRate()) * 1000L);
    if (isPaused)
      cpos -= totalPausedTime;
    return cpos;
  }

  public final synchronized void fade_in(long fade_duration)
  {
    if (currentTrack.playable() && sourceLine != null)
    {
      fade_duration = fade_duration < 0 ? 0 : fade_duration > time_ms() ? time_ms() : fade_duration;
      float init = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getValue(),
          maxima = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum();
      long start = System.currentTimeMillis();
      long elapsed = 0L;
      while (elapsed < fade_duration)
      {
        ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN))
            .setValue(init + (maxima - init) * elapsed / fade_duration);
        elapsed = System.currentTimeMillis() - start;
      }
    }
  }

  public final synchronized void fade_out(long fade_duration)
  {
    if (currentTrack.playable() && sourceLine != null)
    {
      fade_duration = fade_duration < 0 ? 0 : fade_duration > time_ms() ? time_ms() : fade_duration;
      float init = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getValue(),
          minima = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum();
      long start = System.currentTimeMillis();
      long elapsed = 0L;
      while (elapsed < fade_duration)
      {
        ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN))
            .setValue(init - (init - minima) * elapsed / fade_duration);
        elapsed = System.currentTimeMillis() - start;
      }
    }
  }

  public final synchronized void gain_percent(float percent)
  {
    percent = percent < 0F ? 0F : percent > 1F ? 1F : percent;
    float minima = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum(),
        maxima = ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum(),
        gain = (float) (Math.log(percent) / Math.log(10.0D) * 20.0D);
    gain = minima + (maxima - minima) * gain / 100F;
    ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(gain);
  }

  public final tailwind_Status state()
  {
    return compareStatus;
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

}

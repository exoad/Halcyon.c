package com.jackmeng;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.TimerTask;

import java.awt.image.BufferedImage;

import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.core.util.pstream;
import com.jackmeng.core.util.use_Chronos;
import com.jackmeng.core.util.use_FSys;

public class use_ClientProfile
    implements
    impl_Identifiable,
    Runnable,
    Serializable,
    Cloneable
{
  private boolean locked = false;
  private final String name;
  private final String saveLocation;
  private transient BufferedImage userAvatar;
  private float totalTimeUsed_Hours, currentTimeUsed_Minutes; // the LONG_MAX value should be enough I hope, its like
                                                              // 100000000 so centuries

  public static use_ClientProfile load_instance(String location)
  {
    File t = new File(location);
    if (!t.exists() || !t.isFile())
    {
      use_FSys.writeToFile_O(System.getProperty("user.name") + "\n0", t);
    }
    String[] s = new String[2];
    use_FSys.interpolated_ReadFromFile(new File(location), (x, y) -> s[x.intValue()] = y);
    String[] tr = s;
    if (s.length != 2)
      tr = new String[] { System.getProperty("user.name"), "0" };
    pstream.log.info("LOADED_CLIENT_PROFILE: " + Arrays.toString(tr));
    try
    {
      Float.parseFloat(tr[1]);
    } catch (NumberFormatException | NullPointerException e)
    {
      pstream.log.warn("Failed to load...\nRequirements:\nTime (lfloat): Found: " + tr[1] + " Requires: (lfloat)");
    }
    return new use_ClientProfile(false, location,
        tr[0] == null || tr[0].isBlank() ? System.getProperty("user.name") : tr[0],
        Float.parseFloat(tr[1] == null ? "0" : tr[1]));
  }

  private use_ClientProfile(boolean locked, String saveLocation, String name, float totalTimeUsed)
  {
    this.saveLocation = saveLocation;
    this.totalTimeUsed_Hours = totalTimeUsed;
    this.name = name;
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      pstream.log.info("CLIENT_PROFILE_STATS:\nTime_spent(minutes_total): " + this.totalTimeUsed_Hours * 60);
      use_FSys.writeToFile_O(this.stringify(), new File(saveLocation));
    }));
  }

  public synchronized void sync()
  {
    use_FSys.writeToFile_O(this.stringify(), new File(saveLocation));
  }

  public synchronized void addCurrentTime_Minutes_ToHoursVar(float time)
  {
    time = Math.abs(time);
    totalTimeUsed_Hours += time / 60;
  }

  public float exposeCurrentMinutes()
  {
    return currentTimeUsed_Minutes;
  }

  public String getUser_Name()
  {
    return name;
  }

  public float exposeTotalHours()
  {
    return totalTimeUsed_Hours;
  }

  public BufferedImage exposeUserPfp()
  {
    return userAvatar;
  }

  private synchronized void finalizeTime()
  {
    totalTimeUsed_Hours += currentTimeUsed_Minutes / 60;
    currentTimeUsed_Minutes = 0;
  }

  public String stringify()
  {
    finalizeTime();
    return name + "\n" + totalTimeUsed_Hours + "\n";
  }

  @Override public synchronized void run()
  {
    if (!locked)
    {
      const_Core.schedule_secondary_task(new TimerTask() {
        private float s = System.currentTimeMillis();

        @Override public void run()
        {
          float t = System.currentTimeMillis();
          currentTimeUsed_Minutes += use_Chronos.millisToMinutes(t - s);
          s = t;
        }
      }, 1000L, 3000L);
    }
  }

  @Override public Object clone() throws CloneNotSupportedException {
    Object clone = super.clone();
    return new use_ClientProfile(true, saveLocation, name, totalTimeUsed_Hours);
  }
}

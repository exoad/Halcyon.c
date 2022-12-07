package com.jackmeng.halcyon;

import java.io.File;
import java.util.TimerTask;

import com.jackmeng.const_Global;
import com.jackmeng.halcyon.abst.impl_Identifiable;
import com.jackmeng.sys.use_Chronos;
import com.jackmeng.sys.use_FSys;

public class use_ClientProfile
    implements
    impl_Identifiable,
    Runnable
{
  private String name, saveLocation;
  private long totalTimeUsed_Hours, currentTimeUsed_Minutes; // the LONG_MAX value should be enough I hope, its like
                                                             // 100000000 so centuries

  public static use_ClientProfile load_instance(String location)
  {
    String[] s = new String[2];
    use_FSys.interpolated_ReadFromFile(new File(location), (x, y) -> s[x.intValue()] = y.toString());
    return new use_ClientProfile(location, s[0], Long.parseLong(s[1]));
  }

  private use_ClientProfile(String saveLocation, String name, long totalTimeUsed)
  {
    this.saveLocation = saveLocation;
    this.totalTimeUsed_Hours = totalTimeUsed;
    this.name = name;
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      use_FSys.writeToFile_O(this.stringify(), new File(saveLocation));
    }));
  }

  public void addCurrentTime_Minutes(long time)
  {
    time = Math.abs(time);
    totalTimeUsed_Hours += time / 60;
  }

  public long exposeCurrentMinutes()
  {
    return currentTimeUsed_Minutes;
  }

  public long exposeTotalHours()
  {
    return totalTimeUsed_Hours;
  }

  private void finalizeTime()
  {
    totalTimeUsed_Hours += currentTimeUsed_Minutes / 60;
    currentTimeUsed_Minutes = 0;
  }

  public String stringify()
  {
    finalizeTime();
    return name + "\n" + totalTimeUsed_Hours + "\n";
  }

  @Override
  public void run()
  {
    const_Global.schedule_secondary_task(new TimerTask() {
      private long s = System.currentTimeMillis();

      @Override
      public void run()
      {
        long t = System.currentTimeMillis();
        currentTimeUsed_Minutes += use_Chronos.millisToMinutes(t - s);
        s = t;
      }

    }, 1000L, 3000L);
  }
}

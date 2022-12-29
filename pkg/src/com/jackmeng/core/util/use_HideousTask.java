package com.jackmeng.core.util;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jackmeng.core.abst.impl_Identifiable;

// super based thread ;)

/**
 * A hideous thread that kills running threads when a new task is submitted.
 *
 * How this task runner works:
 * <ul>
 * <li>Submit a task</li>
 * <li>The old task (if there is) is abandoned/dropped and the current
 * submission is run. The old task does not receive any compensations.</li>
 * <li>The current submission runs asynchronously and outside dependent tasks
 * waiting for the result can either wait or be alerted later for when the task
 * is done</li>
 * <li>Once the current submission finishes, the task runner destroys the
 * submission and returns to a dormant state. To force a destruction of the
 * runner to either remove it from memory or from use, call the
 * {@link #destroy()} method.
 * <li>At any point in time when the currentTask is finished, the result is
 * stored in overwritable variable
 * and can be accessed using {@link #get()}.
 * </ul>
 *
 * Note: {@link #run()} simply tries to run the currentTask if it has not been
 * killed yet.
 *
 * This object should be reused for similar tasks.
 *
 * THIS SHOULD NOT BE USED FOR ANY DIRECT GRAPHICS2D API RELATED OPERATIONS!!!
 *
 * REMEMBER JAVA SWING IS NOT THREAD SAFE.
 *
 * @author Jack Meng
 */
public final class use_HideousTask< T >
    implements
    Runnable,
    Serializable,
    impl_Identifiable
{
  private String myName;
  private transient Thread curr;
  private transient T res;
  private transient AtomicBoolean running;
  private transient Callable< T > currentTask;

  // wildcard because we literally dont know what might be thrown at this task
  // runner. Thus the return type is left for runtime
  // However intake type must be clearly defined. Most likely a Void
  public use_HideousTask(Callable< T > r, String name)
  {
    this.myName = name;
    this.currentTask = r;
    this.running = new AtomicBoolean(false);
    this.curr = new Thread(myName);
  }

  public synchronized void destroy()
  {
    if (is_processing())
    {
      curr.interrupt();
      curr = new Thread();
      running.set(false);
      currentTask = null;
    }
  }

  /**
   * Resets everything in this task manager
   */
  public synchronized void shakedown()
  {
    destroy();
    res = null;
    curr = new Thread(myName);
  }

  public boolean is_processing()
  {
    return curr.isAlive() || running.get() || !curr.isInterrupted();
  }

  @has_Nullable public T get()
  {
    return res;
  }

  public synchronized void gentle_push(Callable< T > task)
  {
    destroy();
    currentTask = task;
  }

  /**
   * Forcefully push the currentTask off a cliff and place the submission and make
   * it run.
   *
   * No compensation for the old task's listeners and handlers: they simply will
   * not be notified of what happened.
   *
   * The current task is automatically started for execution. If you do not want
   * execution
   * to begin right away, you may use {@link #gentle_push(Callable)} which only
   * kills and
   * sets up the current task to be ran later.
   *
   * @param task
   *          The current submission
   */
  public synchronized void push(Callable< T > task)
  {
    gentle_push(task);
    run();
  }

  @Override public String id()
  {
    return myName != null ? myName : getClass().getCanonicalName() + "->" + hashCode();
  }

  @Override public String toString()
  {
    return id();
  }

  @Override public void run()
  {
    destroy();
    if (currentTask != null && !running.get())
    {
      curr = new Thread(() -> {
        T e = null;
        try
        {
          e = currentTask.call();
        } catch (Exception e1)
        {
          e1.printStackTrace();
        }
        pstream.log.warn("RUNNING A HIDEOUS TASK FOR: " + myName);
        running.set(false);
        res = e;
        curr.interrupt();
        running.set(false);
        currentTask = null;
      });
      curr.start();
      running.set(true);
    }
  }
}

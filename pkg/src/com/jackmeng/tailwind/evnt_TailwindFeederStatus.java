package com.jackmeng.tailwind;

import com.jackmeng.tailwind.use_TailwindFeeder.tailwindfeeder_FeederStates;

@FunctionalInterface
/**
 * Similar to evnt_TailwindStatus in that both are functional
 * interfaces for tailwind stream status updates. This definition
 * specifically defines functionality for a TailwindFeeder object.
 *
 * @author Jack Meng
 */
public interface evnt_TailwindFeederStatus
{
  void feeder_state(tailwindfeeder_FeederStates e);
}

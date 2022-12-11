package com.jackmeng.tailwind;

import com.jackmeng.tailwind.use_TailwindFeeder.tailwindfeeder_FeederStates;

@FunctionalInterface
public interface evnt_TailwindFeederStatus
{
  void feeder_state(tailwindfeeder_FeederStates e);
}

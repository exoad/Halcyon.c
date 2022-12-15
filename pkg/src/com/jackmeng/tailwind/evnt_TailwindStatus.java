package com.jackmeng.tailwind;

import com.jackmeng.tailwind.use_Tailwind.tailwind_Status;

@FunctionalInterface
/**
 * A receiver functional interface that is used to alert
 * any listeners of updates to a tailwind stream object
 *
 * @author Jack Meng
 */
public abstract interface evnt_TailwindStatus
{
    void tailwind_status(tailwind_Status e);
}

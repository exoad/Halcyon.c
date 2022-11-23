package com.jackmeng.halcyon.ploogin;

import com.jackmeng.halcyon.abst.impl_App;

public abstract interface impl_Ploogin
    extends impl_App
{
  public String description();

  public String author();

  public default void unload()
  {

  }

  public default long version()
  {
    /*------------------------------------------------------------------- /
    / should be used to default to the date of the release in the format: /
    / YYYY_MM_DD                                                          /
    / underscores used for readability                                    /
    /--------------------------------------------------------------------*/
    return 2022_12_31;
  }

  public default String repository()
  {
    /*--------------------------------------------------------------- /
    / by default returns nothing of the locale repository to indicate /
    / either closed source or no source permissive.                   /
    / the author should only use this to return a HTTP/HTTPS URL      /
    / or a MAILTO                                                     /
    /----------------------------------------------------------------*/
    return "";
  }

  public default String tooltip()
  {
    return description();
  }
}

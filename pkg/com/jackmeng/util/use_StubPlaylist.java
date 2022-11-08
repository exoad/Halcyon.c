package com.jackmeng.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jackmeng.tailwind.use_TailwindPlaylist;

public class use_StubPlaylist
    extends use_TailwindPlaylist
{

  public use_StubPlaylist(use_TailwindPlaylist.playlist_Traits trait, String playListName, String[] children,
      String[] endings)
  {
    super(playListName, children, endings, trait);
  }

  private void check_validity()
  {
    List< String > temp = new ArrayList<>();
    Collections.copy(temp, super.children);
    for (String t : children)
    {
      File e = new File(t);
      for (String r : endings)
      {
        if (t.endsWith(r) && e.exists() && e.isFile())
        {
          temp.add(t);
        }
      }
    }
    if (traits.autosort)
    {
      sort();
    }
  }

  /**
   * @param endings
   */
  @Override
  public void init(String[] endings)
  {
  }

  /**
   * @return String
   */
  @Override
  public String getCanonicalParent_2()
  {
    return getParent();
  }

  /**
   * @return String
   */
  @Override
  public String getCanonicalParent_1()
  {
    return getParent();
  }

  @Override
  public void refresh()
  {
    check_validity();
  }
}

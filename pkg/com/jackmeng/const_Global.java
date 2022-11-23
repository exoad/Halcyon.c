package com.jackmeng;

import java.util.Timer;

import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.abst.impl_App;
import com.jackmeng.halcyon.abst.impl_ForYou;
import com.jackmeng.tailwind.use_TailwindPlaylist;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.tailwind.use_TailwindPlaylist.playlist_Traits;
import com.jackmeng.util.use_ListenerPool_ForYou;
import com.jackmeng.util.use_Pool;

public final class const_Global
{
  private const_Global()
  {
  }

  public static final use_Pool< impl_App > APPS_POOL = new use_Pool<>();
  public static final use_Pool< use_TailwindPlaylist > PLAY_LIST_POOL = new use_Pool<>();
  public static final use_Pool< use_TailwindTrack > LIKE_LIST_POOL = new use_Pool<>();
  public static final use_ListenerPool_ForYou< use_TailwindTrack, impl_ForYou< use_TailwindTrack > > SELECTION_LISTENERS = new use_ListenerPool_ForYou<>();
  public static final Timer GENERAL_LOOP = new Timer("Halcyon;General_Loop");

  /**
   * @param id
   *          -> The absolute path of the folder to be added as a playlist
   */
  public static void append_to_Playlist(String parentFolder)
  {
    /*---------------------------------------------------------------------- /
    / method should only be used to add ACTUALY file system findable folders /
    /-----------------------------------------------------------------------*/
    if (PLAY_LIST_POOL.get(parentFolder) == null)
    {
      PLAY_LIST_POOL
          .addPoolObject(new use_TailwindPlaylist(new playlist_Traits(false, true, true, false), parentFolder,
              use_Halcyon.acceptedEndings()));
    }
  }

  public static void append_to_liked(String fileABS)
  {
    if (LIKE_LIST_POOL.get(fileABS) == null)
      LIKE_LIST_POOL.addPoolObject(new use_TailwindTrack(fileABS));
  }
}

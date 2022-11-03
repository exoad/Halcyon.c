package com.jackmeng.halcyon;

import java.util.Timer;

import com.jackmeng.halcyon.apps.impl_App;
import com.jackmeng.util.use_Playlist;
import com.jackmeng.util.use_Pool;
import com.jackmeng.util.use_Playlist.playlist_Traits;

public final class const_Global {
  private const_Global() {
  }

  public static final use_Pool<impl_App> APPS_POOL = new use_Pool<>();
  public static final use_Pool<use_Playlist> PLAY_LIST_POOL = new use_Pool<>();
  public static final use_Pool<use_Playlist> LIKE_LIST_POOL = new use_Pool<>();
  public static final Timer GENERAL_LOOP = new Timer("Halcyon;General_Loop");

  /**
   * @param id -> The absolute path of the folder to be added as a playlist
   */
  public static void append_to_Playlist(String parentFolder) {
    /*---------------------------------------------------------------------- /
    / method should only be used to add ACTUALY file system findable folders /
    /-----------------------------------------------------------------------*/
    if (PLAY_LIST_POOL.get(parentFolder) == null) {
      PLAY_LIST_POOL.addPoolObject(new use_Playlist(new playlist_Traits(false, true, true, false), parentFolder,
          use_HalcyonProperties.acceptedEndings()));
    }
  }
}

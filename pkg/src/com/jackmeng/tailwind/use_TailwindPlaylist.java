package com.jackmeng.tailwind;

import com.jackmeng.sys.use_Program;
import com.jackmeng.util.use_Commons;
import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.abst.impl_Identifiable;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class use_TailwindPlaylist
    implements
    Iterable< String >,
    impl_Identifiable
{

  /*--------------------------------------------------------------------- /
  / A playlist can generate its own children from the provided folder.    /
  / This is done so using .list(). However manual entries can be inserted /
  / along the way                                                         /
  /----------------------------------------------------------------------*/

  public static class playlist_Traits
  {
    public boolean readonly, closeable, refreshable, autosort;

    public playlist_Traits(boolean readonly, boolean closeable, boolean refreshable, boolean autosort)
    {
      this.readonly = readonly;
      this.closeable = closeable;
      this.refreshable = refreshable;
      this.autosort = autosort;
    }
  }

  /*-------------------------------------------------------------------------------------------------------- /
   / LIST:STRING "CHILDREN" MUST CONTAIN ALL CHILDREN FILES OF THE VARIABLE IN FILE ABSOLUTE FORMAT          /
   /                                                                                                         /
   / STRING "PARENT" MUST REPRESENT THE PARENT DIRECTORY OF ALL FILES IN THE SUBDIRECTORY                    /
   /                                                                                                         /
   / STRING[] "ENDINGS" MUST REPRESENT VALID FILE EXTENSIONS THAT ARE USED TO CHECK VALIDITY OF THE CHILDREN /
   /--------------------------------------------------------------------------------------------------------*/

  protected final String parent;
  protected String[] endings;
  protected List< String > children;
  protected final playlist_Traits traits;
  private boolean use_standard = true;

  public use_TailwindPlaylist(playlist_Traits traits, String parent, String[] endings)
  {
    this.parent = parent;
    this.endings = endings;
    this.traits = traits;
    use_Program.probable_error(() -> init(endings));
  }

  public use_TailwindPlaylist(playlist_Traits traits, String parent)
  {
    this(traits, parent, null);
  }

  protected use_TailwindPlaylist(String name, String[] children, String[] endings, playlist_Traits traits)
  {
    this.parent = name;
    this.children = new ArrayList<>();
    this.children.addAll(Arrays.asList(children));
    this.traits = traits;
    this.endings = endings;
    use_standard = false;
  }

  /**
   * @return String
   */
  public String getParent()
  {
    return parent;
  }

  /**
   * @return String
   */
  public String getCanonicalParent_1()
  {
    return new File(getParent()).getName();
  }

  /**
   * @return String
   */
  public String getCanonicalParent_2()
  {
    return getParent().split(
        use_Halcyon.getFileSeparator())[getParent().split(use_Halcyon.getFileSeparator()).length
            - 1];
  }

  /**
   * @return playlist_Traits
   */
  public playlist_Traits expose_traits()
  {
    return traits;
  }

  /**
   * @param endings
   */
  public void init(String[] endings)
  {
    if (use_standard)
    {
      List< String > temp = new ArrayList<>();
      for (String r : new File(parent).list((x, y) -> {
        for (String r : endings)
        {
          if (y.endsWith(r))
          {
            return true;
          }
        }
        return false;
      }))
      {
        temp.add(r);
      }
      children = temp;
      if (traits.autosort)
      {
        sort();
      }
    }
  }

  /**
   * @param child
   */
  public void addChild(String child)
  {
    File t = new File(child);
    if (!use_Commons.ends_with(child, endings) || !t.isFile() || !t.exists() || !t.canRead()
        || !child.split(
            use_Halcyon.getFileSeparator())[child.split(use_Halcyon.getFileSeparator()).length - 2]
                .equals(parent))
      return;
    children.add(child);
  }

  public void refresh()
  {
    if (traits.refreshable)
      init(endings);
  }

  /**
   * @return String[]
   */
  public String[] getChildrens()
  {
    return children.toArray(new String[0]);
  }

  public use_TailwindTrack[] child_tracks()
  {
    return as_tracks().toArray(new use_TailwindTrack[0]);
  }

  public void sort()
  {
    Collections.sort(children);
  }

  /**
   * @param name
   *          Absolute path of the children to find
   * @return (true||false) depending on whether a suspected children exists.
   */
  public boolean isChildren(String name)
  {
    return Collections.binarySearch(children, name) != -1;
  }

  /**
   * @param e
   *          The name of the FOLDER construct
   * @return A {@link com.jackmeng.tailwind.use_TailwindPlaylist} object
   */
  public static use_TailwindPlaylist construct(File e, String[] endings)
  {
    return new use_TailwindPlaylist(e.getAbsolutePath(), e.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name)
      {
        for (String e : endings)
        {
          if (name.endsWith(e))
          {
            return true;
          }
        }
        return false;
      }
    }), endings, new playlist_Traits(false, true, true, false));
  }

  /**
   * @return Iterator:String
   */
  @Override
  public Iterator< String > iterator()
  {
    return children.iterator();
  }

  public Iterator< use_TailwindTrack > deep_iterator()
  {
    return as_tracks().iterator();
  }

  public Collection< use_TailwindTrack > as_tracks()
  {
    List< use_TailwindTrack > tracks = new ArrayList<>();
    children.forEach(x -> tracks.add(new use_TailwindTrack(x)));
    return tracks;
  }

  /**
   * @return String
   */
  @Override
  public String id()
  {
    return getParent();
  }

  /**
   * @return String
   */
  public String toString()
  {
    return getParent() + "(" + getCanonicalParent_1() + ":" + children + ")";
  }
}

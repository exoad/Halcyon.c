package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.WeakHashMap;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.jackmeng.core.abst.impl_Identifiable;
import com.jackmeng.core.gui.const_ResourceManager;
import com.jackmeng.sys.pstream;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;
import com.jackmeng.util.use_Commons;
import com.jackmeng.util.use_ResourceFetcher;

import java.awt.image.*;

import static com.jackmeng.core.const_Lang.*;

import java.awt.*;

/**
 * A very low level yet concrete definition of a MusicTrack which can be used
 * and parsed by a impl_Tailwind implementation to read data acquired from this
 * file.
 * <br>
 * NOTE: Data is not directly streamed from this object!
 *
 * @author Jack Meng
 */
public final class use_TailwindTrack
    implements
    impl_Identifiable,
    Serializable
{
  /*---------------------------- /
  / represents a "an audio" file /
  /-----------------------------*/

  private String locale;
  private boolean hasArtwork;

  public enum tailwindtrack_Tags {
    MEDIA_ARTIST(FieldKey.ARTIST, _lang(LANG_UNKNOWN)), MEDIA_ART(FieldKey.COVER_ART,
        use_ResourceFetcher.fetcher.getFromAsImage(
            const_ResourceManager.GUI_DISK_ICON)), MEDIA_DURATION(null, "0"), MEDIA_BITRATE(null,
                "0kpbs"), MEDIA_SAMPLERATE(null, "0.0khz"), MEDIA_ALBUM(FieldKey.ALBUM,
                    _lang(LANG_UNKNOWN)), MEDIA_GENRE(FieldKey.GENRE,
                        _lang(LANG_UNKNOWN)), MEDIA_COMMENT(FieldKey.COMMENT, "??"), MEDIA_YEAR(FieldKey.YEAR,
                            _lang(LANG_UNKNOWN)), MEDIA_LANGUAGE(FieldKey.LANGUAGE,
                                _lang(LANG_UNKNOWN)), MEDIA_COMPOSER(FieldKey.COMPOSER,
                                    _lang(LANG_UNKNOWN)), MEDIA_LYRICS(FieldKey.LYRICS,
                                        "???"), MEDIA_COPYRIGHT(FieldKey.COPYRIGHT,
                                            _lang(LANG_UNKNOWN)), MEDIA_TITLE(FieldKey.TITLE,
                                                _lang(LANG_UNKNOWN) + ".music"), MEDIA_ART_COLOR_PRIMA(null, null);

    public final Object value;
    public final FieldKey key;

    private tailwindtrack_Tags(FieldKey key, Object Defvalue)
    {
      this.key = key;
      this.value = Defvalue;
    }
  }

  private static WeakHashMap< String, Object > lazyColors_Cache = new WeakHashMap<>(10);

  private File content;
  private transient Tag tag;
  private transient AudioHeader header;
  private boolean playable = true;
  /*-------------------------------------------------------------- /
  / private transient Map< tailwindtrack_Tags, Object > MediaTags; /
  /---------------------------------------------------------------*/

  private static Object iem(Object e, tailwindtrack_Tags elseE)
  {
    return use_Commons.str_empty((String) e) ? elseE.value : e;
  }

  public use_TailwindTrack(String str)
  {
    this(new File(str), true);
  }

  public use_TailwindTrack(File file)
  {
    this(file, true);
  }

  public use_TailwindTrack(URL path)
  {
    this(new File(path.getFile()), true);
  }

  public use_TailwindTrack(URL path, boolean playable)
  {
    this(new File(path.getFile()), playable);
  }

  public use_TailwindTrack(String str, boolean playable)
  {
    this(new File(str), playable);
  }

  public use_TailwindTrack(File file, boolean playable)
  {
    hasArtwork = false;
    this.playable = playable;
    setContentFile(file);
    __init__();
  }

  private void __init__()
  {
    /*------------------------------------------------------------------------------------------ /
    / this method uses the default configured content file and generates the specified data from /
    / that recognized file                                                                       /
    /-------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------------------------------------- /
    / MediaTags = new WeakHashMap<>();                                                                          /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_BITRATE,                                                           /
    /     header == null ? tailwindtrack_Tags.MEDIA_BITRATE.value : header.getBitRate());                       /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_SAMPLERATE,                                                        /
    /     header == null ? tailwindtrack_Tags.MEDIA_SAMPLERATE : header.getSampleRate());                       /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_GENRE,                                                             /
    /     tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value                                                    /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_GENRE.key), tailwindtrack_Tags.MEDIA_GENRE));         /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_ALBUM,                                                             /
    /     tag == null ? tailwindtrack_Tags.MEDIA_ALBUM.value                                                    /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_ALBUM.key), tailwindtrack_Tags.MEDIA_ALBUM));         /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_ARTIST,                                                            /
    /     tag == null ? tailwindtrack_Tags.MEDIA_ARTIST.value                                                   /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_ARTIST.key), tailwindtrack_Tags.MEDIA_ARTIST));       /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_COMMENT,                                                           /
    /     tag == null ? tailwindtrack_Tags.MEDIA_COMMENT.value                                                  /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COMMENT.key), tailwindtrack_Tags.MEDIA_COMMENT));     /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_COMPOSER,                                                          /
    /     tag == null ? tailwindtrack_Tags.MEDIA_COMPOSER.value                                                 /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COMPOSER.key), tailwindtrack_Tags.MEDIA_COMPOSER));   /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_COPYRIGHT,                                                         /
    /     tag == null ? tailwindtrack_Tags.MEDIA_COPYRIGHT.value                                                /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COPYRIGHT.key), tailwindtrack_Tags.MEDIA_COPYRIGHT)); /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_DURATION,                                                          /
    /     header == null ? tailwindtrack_Tags.MEDIA_DURATION.value : header.getTrackLength());                  /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_LANGUAGE,                                                          /
    /     tag == null ? tailwindtrack_Tags.MEDIA_LANGUAGE.value                                                 /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_LANGUAGE.key), tailwindtrack_Tags.MEDIA_LANGUAGE));   /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_LYRICS,                                                            /
    /     tag == null ? tailwindtrack_Tags.MEDIA_LYRICS.value                                                   /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_LYRICS.key), tailwindtrack_Tags.MEDIA_LYRICS));       /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_YEAR,                                                              /
    /     tag == null ? tailwindtrack_Tags.MEDIA_YEAR.value                                                     /
    /         : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_YEAR.key), tailwindtrack_Tags.MEDIA_YEAR));           /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_TITLE,                                                             /
    /     tag == null ? content.getName()                                                                       /
    /         : use_Primitives.str_empty(tag.getFirst(tailwindtrack_Tags.MEDIA_TITLE.key)) ? content.getName()  /
    /             : tailwindtrack_Tags.MEDIA_TITLE.value);                                                      /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_ART, get_artwork());                                               /
    /----------------------------------------------------------------------------------------------------------*/

    AudioFile af = null;
    try
    {
      af = AudioFileIO.read(content);
    } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e)
    {
      pstream.log.err(e);
    }
    if (af != null)
    {
      tag = af.getTag();
      header = af.getAudioHeader();
    }

    /*---------------------------------------------------------------------------------------------------------------- /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_ABSOLUTE_LOCATION, header == null ? "0" : header.getBitRate());                /
    / MediaTags.put(tailwindtrack_Tags.MEDIA_GENRE,                                                                         /
    /     tag == null || str_empty(tag.getFirst(FieldKey.GENRE)) ? _lang(LANG_UNKNOWN) : tag.getFirst(FieldKey.GENRE)); /
    / MediaTags.put(tailwindtrack_Tags.)                                                                                    /
    /-----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------- /
    / MediaTags.put(tailwind_Tags.MEDIA_ABSOLUTE_LOCATION,) /
    /------------------------------------------------------*/
  }

  public Object get(tailwindtrack_Tags er)
  {
    if (er == tailwindtrack_Tags.MEDIA_BITRATE)
      return header == null ? er.value : header.getBitRate();
    else if (er == tailwindtrack_Tags.MEDIA_SAMPLERATE)
      return header.getSampleRate();
    else if (er == tailwindtrack_Tags.MEDIA_ART)
      return get_artwork();
    else if (er == tailwindtrack_Tags.MEDIA_ART_COLOR_PRIMA)
    {
      BufferedImage img = get_artwork();
      if (lazyColors_Cache.containsKey(content.getAbsolutePath()))
        return lazyColors_Cache.get(content.getAbsolutePath());
      if (img == null)
      {
        lazyColors_Cache.put(content.getAbsolutePath(), er.value);
        return er.value;
      }
      Color r = use_Color.make(use_Image.accurate_accent_color_1(img));
      lazyColors_Cache.put(content.getAbsolutePath(), r);
      return r;
    }
    else if (er == tailwindtrack_Tags.MEDIA_DURATION)
      return header.getTrackLength();
    else if (er == tailwindtrack_Tags.MEDIA_TITLE)
      return tag == null ? content.getName()
          : use_Commons.str_empty(tag.getFirst(er.key)) ? content.getName() : tag.getFirst(er.key);
    else if (tag != null && tag.getFirst(er.key) != null)
      return er == tailwindtrack_Tags.MEDIA_YEAR ? get_artwork() : iem(tag.getFirst(er.key), er);
    return er.value;
  }

  private BufferedImage art()
  {

    BufferedImage img = null;
    try
    {
      if (content.getAbsolutePath().endsWith(".mp3"))
      {
        MP3File mp = null;
        try
        {
          mp = new MP3File(content);
        } catch (IOException | TagException | ReadOnlyFileException | CannotReadException
            | InvalidAudioFrameException e1)
        {
          e1.printStackTrace();
        }
        assert mp != null;
        if (mp.getTag().getFirstArtwork() != null)
        {
          try
          {
            hasArtwork = true;
            img = (BufferedImage) mp.getTag().getFirstArtwork().getImage();
          } catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
      return Objects.requireNonNullElseGet(img, () -> {
        hasArtwork = false;
        return (BufferedImage) tailwindtrack_Tags.MEDIA_ART.value;
      });
    } catch (NullPointerException e)
    {
      hasArtwork = false;
      return (BufferedImage) tailwindtrack_Tags.MEDIA_ART.value;
    }
  }

  public BufferedImage get_artwork()
  {
    return art();
  }

  public boolean has_artwork()
  {
    return hasArtwork;
  }

  public void refresh()
  {
    __init__();
  }

  public void set_playability(boolean e)
  {
    playable = e;
  }

  public boolean playable()
  {
    return playable;
  }

  public void setContentFile(File f)
  {
    /*---------------------------- /
    / MAKE SURE TO CALL refresh(); /
    /-----------------------------*/
    this.content = f;
  }

  public File getContentFile()
  {
    return content;
  }

  @Override
  public String id()
  {
    return locale;
  }

  public String toString()
  {
    return "Track:" + content.getAbsolutePath();
  }
}

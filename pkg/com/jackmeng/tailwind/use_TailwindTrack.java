package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

import com.jackmeng.halcyon.apps.impl_Identifiable;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.sys.pstream;
import com.jackmeng.util.use_Primitives;
import com.jackmeng.util.use_ResourceFetcher;

import java.awt.image.*;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public final class use_TailwindTrack implements impl_Identifiable
{
  /*---------------------------- /
  / represents a "an audio" file /
  /-----------------------------*/

  private String locale;

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
                                        "???"), MEDIA_COPYRIGHT(FieldKey.COPYRIGHT, _lang(LANG_UNKNOWN));

    public final Object value;
    public final FieldKey key;

    private tailwindtrack_Tags(FieldKey key, Object Defvalue)
    {
      this.key = key;
      this.value = Defvalue;
    }
  }

  private File content;
  private Tag tag;
  private AudioHeader header;
  private Map< tailwindtrack_Tags, Object > tags;

  private static Object iem(Object e, tailwindtrack_Tags elseE)
  {
    return e instanceof String ? (use_Primitives.str_empty((String) e) ? elseE.value : e) : e == null ? elseE.value : e;
  }

  public use_TailwindTrack(String str)
  {
    this(new File(str));
  }

  public use_TailwindTrack(File file)
  {
    setContentFile(file);
  }

  public use_TailwindTrack(URL path)
  {
    this(new File(path.getFile()));
  }

  private void __init__()
  {
    /*------------------------------------------------------------------------------------------ /
    / this method uses the default configured content file and generates the specified data from /
    / that recognized file                                                                       /
    /-------------------------------------------------------------------------------------------*/
    AudioFile r = null;
    try
    {
      r = AudioFileIO.read(content);
    } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e)
    {
      pstream.log.err(e);
    }
    if (r != null)
    {
      tag = r.getTag();
      header = r.getAudioHeader();
    }
    tags = new WeakHashMap<>();
    tags.put(tailwindtrack_Tags.MEDIA_BITRATE,
        header == null ? tailwindtrack_Tags.MEDIA_BITRATE.value : header.getBitRate());
    tags.put(tailwindtrack_Tags.MEDIA_SAMPLERATE,
        header == null ? tailwindtrack_Tags.MEDIA_SAMPLERATE : header.getSampleRate());
    tags.put(tailwindtrack_Tags.MEDIA_GENRE,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_GENRE.key), tailwindtrack_Tags.MEDIA_GENRE));
    tags.put(tailwindtrack_Tags.MEDIA_ALBUM,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_ALBUM.key), tailwindtrack_Tags.MEDIA_ALBUM));
    tags.put(tailwindtrack_Tags.MEDIA_ARTIST,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_ARTIST.key), tailwindtrack_Tags.MEDIA_ARTIST));
    tags.put(tailwindtrack_Tags.MEDIA_COMMENT,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COMMENT.key), tailwindtrack_Tags.MEDIA_COMMENT));
    tags.put(tailwindtrack_Tags.MEDIA_COMPOSER,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COMPOSER.key), tailwindtrack_Tags.MEDIA_COMPOSER));
    tags.put(tailwindtrack_Tags.MEDIA_COPYRIGHT,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_COPYRIGHT.key), tailwindtrack_Tags.MEDIA_COPYRIGHT));
    tags.put(tailwindtrack_Tags.MEDIA_DURATION,
        header == null ? tailwindtrack_Tags.MEDIA_DURATION.value : header.getPreciseTrackLength());
    tags.put(tailwindtrack_Tags.MEDIA_LANGUAGE,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_LANGUAGE.key), tailwindtrack_Tags.MEDIA_LANGUAGE));
    tags.put(tailwindtrack_Tags.MEDIA_LYRICS,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_LYRICS.key), tailwindtrack_Tags.MEDIA_LYRICS));
    tags.put(tailwindtrack_Tags.MEDIA_YEAR,
        tag == null ? tailwindtrack_Tags.MEDIA_GENRE.value
            : iem(tag.getFirst(tailwindtrack_Tags.MEDIA_YEAR.key), tailwindtrack_Tags.MEDIA_YEAR));
    tags.put(tailwindtrack_Tags.MEDIA_ART, get_artwork());

    /*---------------------------------------------------------------------------------------------------------------- /
    / tags.put(tailwindtrack_Tags.MEDIA_ABSOLUTE_LOCATION, header == null ? "0" : header.getBitRate());                /
    / tags.put(tailwindtrack_Tags.MEDIA_GENRE,                                                                         /
    /     tag == null || str_empty(tag.getFirst(FieldKey.GENRE)) ? _lang(LANG_UNKNOWN) : tag.getFirst(FieldKey.GENRE)); /
    / tags.put(tailwindtrack_Tags.)                                                                                    /
    /-----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------- /
    / tags.put(tailwind_Tags.MEDIA_ABSOLUTE_LOCATION,) /
    /------------------------------------------------------*/
  }

  public BufferedImage get_artwork()
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
            img = (BufferedImage) mp.getTag().getFirstArtwork().getImage();
          } catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
      return Objects.requireNonNullElseGet(img, () -> {
        return (BufferedImage) tailwindtrack_Tags.MEDIA_ART.value;
      });
    } catch (NullPointerException e)
    {
      return (BufferedImage) tailwindtrack_Tags.MEDIA_ART.value;
    }
  }

  public void refresh()
  {
    __init__();
  }

  public void setContentFile(File f)
  {
    this.content = f;
    __init__();
  }

  public File getContentFile()
  {
    return content;
  }

  public Optional< Object > get(tailwindtrack_Tags key)
  {
    return Optional.of(tags.get(key));
  }

  @Override
  public String id()
  {
    return locale;
  }

  public String toString()
  {
    return "Track:" + tag.toString();
  }
}

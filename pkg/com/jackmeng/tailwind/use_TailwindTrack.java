package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.jackmeng.halcyon.apps.impl_Identifiable;
import com.jackmeng.sys.pstream;

import static com.jackmeng.halcyon.gui.const_Lang.*;
import static com.jackmeng.util.use_Primitives.*;

public final class use_TailwindTrack implements impl_Identifiable
{
  /*---------------------------- /
  / represents a "an audio" file /
  /-----------------------------*/

  private String locale;

  public enum tailwindtrack_Tags {
    MEDIA_ARTIST, MEDIA_ART, MEDIA_ABSOLUTE_LOCATION, MEDIA_DURATION, MEDIA_BITRATE, MEDIA_SAMPLERATE, MEDIA_ALBUM, MEDIA_GENRE
  }

  private File content;
  private Tag tag;
  private AudioHeader header;
  private Map< tailwindtrack_Tags, Object > tags;

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
    tags.put(tailwindtrack_Tags.MEDIA_ABSOLUTE_LOCATION, header == null ? "0" : header.getBitRate());
    tags.put(tailwindtrack_Tags.MEDIA_GENRE,
        tag == null || str_empty(tag.getFirst(FieldKey.GENRE)) ? _lang(LANG_UNKNOWN) : tag.getFirst(FieldKey.GENRE));
    /*----------------------------------------------------- /
    / tags.put(tailwind_Tags.MEDIA_ABSOLUTE_LOCATION,) /
    /------------------------------------------------------*/
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

  public Optional< Object > get_tag(tailwindtrack_Tags key)
  {
    return Optional.of(tags.get(key));
  }

  @Override
  public String id()
  {
    return locale;
  }
}

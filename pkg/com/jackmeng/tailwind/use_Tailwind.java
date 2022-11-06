package com.jackmeng.tailwind;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
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

import com.jackmeng.sys.pstream;

import static com.jackmeng.util.use_Primitives.str_empty;
import static com.jackmeng.halcyon.gui.const_Lang.*;

public class use_Tailwind {
  /*----------------------------------------------------- /
  / master class for the tailwind audio processor and the /
  / "frontman" for the library                            /
  /------------------------------------------------------*/

  public enum tailwind_Tags {
    MEDIA_ARTIST,
    MEDIA_ART,
    MEDIA_ABSOLUTE_LOCATION,
    MEDIA_DURATION,
    MEDIA_BITRATE,
    MEDIA_SAMPLERATE,
    MEDIA_ALBUM,
    MEDIA_GENRE
  }

  private File content;
  private Tag tag;
  private AudioHeader header;
  private Map<tailwind_Tags, Object> tags;

  public use_Tailwind(URL path) {
    this(new File(path.getFile()));
  }

  public use_Tailwind(File file) {
    /*----------------------------------------------------------------------------------------------- /
    / no direct re-assignment of the variable "content" should be used as SetContentFile specifically /
    / specifies a refresh of the tags generated thus permitting reuse of the same object              /
    /------------------------------------------------------------------------------------------------*/
    setContentFile(file);
  }

  public use_Tailwind(String filePath) {
    this(new File(filePath));
  }

  private void _init() {
    /*------------------------------------------------------------------------------------------ /
    / this method uses the default configured content file and generates the specified data from /
    / that recognized file                                                                       /
    /-------------------------------------------------------------------------------------------*/
    AudioFile r = null;
    try {
      r = AudioFileIO.read(content);
    } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
      pstream.log.err(e);
    }
    if(r != null) {
      tag = r.getTag();
      header = r.getAudioHeader();
    }
    tags = new WeakHashMap<>();
    tags.put(tailwind_Tags.MEDIA_ABSOLUTE_LOCATION, header == null ? "0" : header.getBitRate());
    tags.put(tailwind_Tags.MEDIA_GENRE, tag == null || str_empty(tag.getFirst(FieldKey.GENRE)) ? _lang(LANG_UNKNOWN) : tag.getFirst(FieldKey.GENRE));
    /*----------------------------------------------------- /
    / tags.put(tailwind_Tags.MEDIA_ABSOLUTE_LOCATION,) /
    /------------------------------------------------------*/
  }

  public void setContentFile(File f) {
    this.content = f;
    _init();
  }

  public File getContentFile() {
    return content;
  }
}

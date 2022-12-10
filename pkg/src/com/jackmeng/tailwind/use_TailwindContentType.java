package com.jackmeng.tailwind;

import javax.sound.sampled.AudioFileFormat;

public enum use_TailwindContentType {
  MP3(false, null, "mp3"), WAV(true, AudioFileFormat.Type.WAVE, "wav"), OGG(false, null, "ogg", "oga"), FLAC(false,
      null, "flac"), AIFF(true, AudioFileFormat.Type.AIFF, "aif", "aiff"), AIFC(true, AudioFileFormat.Type.AIFC,
          "aifc"), AU(true, AudioFileFormat.Type.AU, "au"), SND(true, AudioFileFormat.Type.SND, "snd");

  public final boolean write_support;
  public final AudioFileFormat.Type type;
  private final String[] file_extensions;

  private use_TailwindContentType(boolean write_support, AudioFileFormat.Type type, String... fileExtensions)
  {
    this.write_support = write_support;
    this.type = type;
    this.file_extensions = fileExtensions;
  }

  public String[] expose_valid_extensions()
  {
    return file_extensions;
  }

  public boolean contains_extension(String ext)
  {
    for (String e : file_extensions)
      if (e.equals(ext))
        return true;
    return false;
  }

  public static use_TailwindContentType format_by_name(String name)
  {
    use_TailwindContentType e = null;
    if (name.contains(".") && !name.endsWith("."))
    {
      String extension = name
          .substring(name.lastIndexOf('.') + 1)
          .toLowerCase();
      for (use_TailwindContentType format : use_TailwindContentType.values())
      {
        if (format.contains_extension(extension))
        {
          e = format;
          break;
        }
      }
    }
    return e;
  }

}

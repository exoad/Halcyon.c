package com.jackmeng.util;

import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_ErrorCode;
import com.jackmeng.sys.use_FSys;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.WeakHashMap;
import java.util.zip.ZipFile;
import java.awt.image.*;

public class use_ResourceFetcher
{

  public static final use_ResourceFetcher fetcher = new use_ResourceFetcher();

  private use_ResourceFetcher()
  {
  }

  /**
   * @param path
   * @return ImageIcon
   */
  public ImageIcon getFromAsImageIcon(String path)
  {
    try
    {
      return new ImageIcon(
          java.util.Objects.requireNonNull(getClass().getResource(path)));
    } catch (NullPointerException e)
    {
      return new ImageIcon(path);
    }
  }

  public BufferedImage getFromAsImage(String path)
  {
    try
    {
      return ImageIO.read(java.util.Objects.requireNonNull(getClass().getResource(path)));
    } catch (Exception e)
    {
      try
      {
        return ImageIO.read(new File(path));
      } catch (IOException e1)
      {
        use_HalcyonFolder.FOLDER.log(e);
      }
    }
    return null;
  }

  private WeakHashMap< String, String > lazyHLL_Cache = new WeakHashMap<>();

  @use_ErrorCode(code = "ERR_SCHEMA_01", description = "Emitted by this method to show an incorrect loading schema. Schema loading is mostly likely to do with loading of important files from the bundled resource folder")
  public String load_n_parse_hll(String path, Object... args)
  {
    String loaded = null;
    if (lazyHLL_Cache.containsKey(path))
    {
      pstream.log.warn("HLL Loading: LAZY Cache");
      loaded = lazyHLL_Cache.get(path);
    }
    try
    {
      lazyHLL_Cache.put(path, loaded == null ? use_FSys.fread_2(path) : loaded);
      return MessageFormat.format(lazyHLL_Cache.get(path), args);
    } catch (IOException e)
    {
      use_HalcyonFolder.FOLDER.log(e);
    }
    return "!ERR_SCHEMA_01!";
  }

  /**
   * @param path
   * @return File
   */
  public File getFromAsFile(String path)
  {
    try
    {
      return new File(
          java.util.Objects.requireNonNull(getClass().getResource(path)).getFile());
    } catch (NullPointerException e)
    {
      return new File(path);
    }
  }

  /**
   * @param zip
   * @param zippedFileName
   * @return File
   */
  public File getFromHLib(String zip, String zippedFileName)
  {
    try (
        ZipFile file = new ZipFile(java.util.Objects.requireNonNull(getClass().getResource(zip + ".hlib").getFile())))
    {
      for (File r : use_FSys.unzip(file, 4096))
      {
        if (r.getName().equals(zippedFileName))
        {
          return r;
        }
      }
    } catch (IOException | NullPointerException e)
    {
      try
      {
        for (File r : use_FSys.unzip(zippedFileName, 4096))
        {
          if (r.getName().equals(zippedFileName))
          {
            return r;
          }
        }
        return null;
      } catch (IOException e1)
      {
        pstream.log.err(e1);
      }
    }
    return null;
  }
}

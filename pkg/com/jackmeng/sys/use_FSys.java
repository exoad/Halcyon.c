package com.jackmeng.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jackmeng.halcyon.abst.impl_ForYou;

public final class use_FSys
{
  private use_FSys()
  {
  }

  /**
   * @param pathToZip
   * @param bufferSize
   * @return File[]
   * @throws IOException
   */
  public static File[] unzip(String pathToZip, int bufferSize) throws IOException
  {
    assert bufferSize > 0;
    ZipFile file = new ZipFile(pathToZip);
    return unzip(file, bufferSize);
  }

  public static boolean s_containsFileOfType(File dir, String... extensions)
  {
    if (!dir.isDirectory() || !dir.exists())
    {
      return false;
    }
    for (String r : dir.list())
    {
      for (String t : extensions)
      {
        if (r.endsWith(t))
        {
          return true;
        }
      }
    }
    return false;
  }

  public static void serialize_OBJ(String locale, Serializable e, impl_ForYou< Exception > errorCallback)
  {
    try
    {
      File t = new File(locale);
      if (!t.exists())
        t.createNewFile();
      FileOutputStream fos = new FileOutputStream(t);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(e);
      oos.close();
      fos.close();
    } catch (Exception err)
    {
      err.printStackTrace();
      errorCallback.forYou(err);
    }
  }

  public static < T > void deserialize_OBJ(String locale, Class< T > example, impl_ForYou< Exception > errorCallback,
      impl_ForYou< T > promise)
  {
      if (!new File(locale).exists())
      {
        promise.forYou(null);
        return;
      }
      T obj = null;
      try
      {
        FileInputStream fis = new FileInputStream(new File(locale));
        ObjectInputStream ois = new ObjectInputStream(fis);
        obj = example.cast(ois.readObject());
        ois.close();
        fis.close();
      } catch (Exception e)
      {
        e.printStackTrace();
        errorCallback.forYou(e);
        promise.forYou(null);
      }
      promise.forYou(obj);

  }

  public static boolean s_containsOnlyFiles(File dir)
  {
    if (!dir.isDirectory() || !dir.exists())
    {
      return false;
    }
    return dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname)
      {
        return pathname.isFile() && pathname.exists();
      }
    }).length != 0;
  }

  /**
   * @param file
   * @param bufferSize
   * @return File[]
   * @throws IOException
   */
  public static File[] unzip(ZipFile file, int bufferSize) throws IOException
  {
    assert bufferSize > 0;
    Enumeration< ? extends ZipEntry > e = file.entries();
    List< File > list = new ArrayList<>();
    while (e.hasMoreElements())
    {
      ZipEntry entry = e.nextElement();
      InputStream in = file.getInputStream(entry);
      File temp = File.createTempFile(file.getName(), null);
      temp.deleteOnExit();

      FileOutputStream fos = new FileOutputStream(temp);
      byte[] buffer = new byte[bufferSize];
      while (in.read(buffer) != -1)
      {
        fos.write(buffer);
      }
      list.add(temp);
      fos.close();
      in.close();
    }
    file.close();
    return list.toArray(new File[0]);
  }

  public static String fread_1(String name) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(name))))
    {
      if (br.ready())
      {
        String temp;
        while ((temp = br.readLine()) != null)
        {
          sb.append(temp);
        }
      }
    }
    return sb.toString();
  }

  public static String fread_2(String name) throws IOException
  {
    Path path = Paths.get(name);
    StringBuilder sb = new StringBuilder();
    Files.lines(path).forEach(sb::append);
    return sb.toString();
  }
}

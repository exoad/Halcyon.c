package com.jackmeng.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class use_FSys {
  private use_FSys() {
  }

  /**
   * @param pathToZip
   * @param bufferSize
   * @return File[]
   * @throws IOException
   */
  public static File[] unzip(String pathToZip, int bufferSize) throws IOException {
    assert bufferSize > 0;
    ZipFile file = new ZipFile(pathToZip);
    return unzip(file, bufferSize);
  }

  /**
   * @param file
   * @param bufferSize
   * @return File[]
   * @throws IOException
   */
  public static File[] unzip(ZipFile file, int bufferSize) throws IOException {
    assert bufferSize > 0;
    Enumeration<? extends ZipEntry> e = file.entries();
    List<File> list = new ArrayList<>();
    while (e.hasMoreElements()) {
      ZipEntry entry = e.nextElement();
      InputStream in = file.getInputStream(entry);
      File temp = File.createTempFile(file.getName(), null);
      temp.deleteOnExit();

      FileOutputStream fos = new FileOutputStream(temp);
      byte[] buffer = new byte[bufferSize];
      while (in.read(buffer) != -1) {
        fos.write(buffer);
      }
      list.add(temp);
      fos.close();
      in.close();
    }
    file.close();
    return list.toArray(new File[0]);
  }

  public static String fread_1(String name) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(name)))) {
      if (br.ready()) {
        String temp;
        while ((temp = br.readLine()) != null) {
          sb.append(temp);
        }
      }
    }
    return sb.toString();
  }

  public static String fread_2(String name) throws IOException {
    Path path = Paths.get(name);
    StringBuilder sb = new StringBuilder();
    Files.lines(path).forEach(sb::append);
    return sb.toString();
  }
}

package com.jackmeng.util;

import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_FSys;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class use_ResourceFetcher {

  public static final use_ResourceFetcher fetcher = new use_ResourceFetcher();

  private use_ResourceFetcher() {
  }

  /**
   * @param path
   * @return ImageIcon
   */
  public ImageIcon getFromAsImageIcon(String path) {
    File t = new File(path);
    if(t.exists()) {
      pstream.log.ok("FETCHING IMAGEICON: " + path + " | Path existence: " + new File(path).exists());
    } else {
      pstream.log.err("FETCHING IMAGEICON: " + path + " | Path existence: " + new File(path).exists());
    }
    try {
      return new ImageIcon(
          java.util.Objects.requireNonNull(getClass().getResource(path)));
    } catch (NullPointerException e) {
      return new ImageIcon(path);
    }
  }

  /**
   * @param path
   * @return File
   */
  public File getFromAsFile(String path) {
    try {
      return new File(
          java.util.Objects.requireNonNull(getClass().getResource(path)).getFile());
    } catch (NullPointerException e) {
      return new File(path);
    }
  }

  /**
   * @param zip
   * @param zippedFileName
   * @return File
   */
  public File getFromHLib(String zip, String zippedFileName) {
    try (
        ZipFile file = new ZipFile(java.util.Objects.requireNonNull(getClass().getResource(zip + ".hlib").getFile()))) {
      for (File r : use_FSys.unzip(file, 4096)) {
        if (r.getName().equals(zippedFileName)) {
          return r;
        }
      }
    } catch (IOException | NullPointerException e) {
      try {
        for (File r : use_FSys.unzip(zippedFileName, 4096)) {
          if (r.getName().equals(zippedFileName)) {
            return r;
          }
        }
        return null;
      } catch (IOException e1) {
        pstream.log.err(e1);
      }
    }
    return null;
  }
}

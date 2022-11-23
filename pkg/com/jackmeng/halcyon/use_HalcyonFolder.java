package com.jackmeng.halcyon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.jackmeng.const_Global;
import com.jackmeng.halcyon.abst.impl_ForYou;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_FSys;
import com.jackmeng.sys.use_Program;
import com.jackmeng.tailwind.use_TailwindPlaylist;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.util.use_Commons;

public final class use_HalcyonFolder
{
  /*------------------------------------------------------------ /
  / represents the default resource folder and provides a lookup /
  / style to find all of the files in that folder                /
  /-------------------------------------------------------------*/
  private static final String MASTA_FOLDA = "halcyon";

  public static final use_HalcyonFolder FOLDER = new use_HalcyonFolder(MASTA_FOLDA);
  public static final byte[] DELIMITER = { 0x0E, 0x00, 0x0E, (byte) 0x9E };

  private File locale;
  private Properties p, pUsr;

  public enum halcyonfolder_Content {
    /*------------------------------------------------------------- /
    / represents the possible "contents" within the halcyon folder: /
    / suffix d -> directory                                         /
    / suffix f -> file                                              /
    /--------------------------------------------------------------*/
    CACHE_d("caches"), USER_d("conf"), SHARED_LIBRARY_d("hlib"), PLOOGINS_d("extern"), LOGS_d("logs"), SYSCONF_f(
        "HALCYON.hal"), LANG_CONF_f("_locale.hal"), MASTADIR_d(
            ""), PLAYLISTS_CONF_f("conf" + use_Halcyon.getFileSeparator() + "personal.hal");

    public final String val;

    private halcyonfolder_Content(String name)
    {
      this.val = MASTA_FOLDA + use_Halcyon.getFileSeparator() + name;
    }

    public File make()
    {
      return new File(val);
    }
  }

  private use_HalcyonFolder(final String r)
  {
    p = new Properties();
    pUsr = new Properties();
    locale = new File(r);
    check();
  }

  private void m_w_f(File e, String str)
  { // make write file
    if (e.exists())
      e.delete();
    else
    {
      try
      {
        e.createNewFile();
      } catch (IOException e1)
      {
        use_Program.error_gui(e1);
      }
    }
    try
    {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(e)));
      bw.write(str + "\n");
      bw.flush();
      bw.close();
    } catch (IOException x)
    {
      use_Program.error_gui(x);
    }
  }

  public void log(String ex)
  {
    check(halcyonfolder_Content.LOGS_d);
    Date d = new Date(System.currentTimeMillis());
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

    m_w_f(
        new File(halcyonfolder_Content.LOGS_d.val + use_Halcyon.getFileSeparator() + "LOG_"
            + new SimpleDateFormat("yyy-MM-dd_HH_mm_ss").format(d) + ".log"),
        "Halcyon/MP4J - LOG EXCEPTION,LOGGED TELEMETRY DATA\nException caught time: " + df.format(d)
            + "\n"
            + ex);
  }

  public void save_conf()
  {
    check();
    try
    {
      p.loadFromXML(new FileInputStream(halcyonfolder_Content.SYSCONF_f.val));
    } catch (IOException e)
    {
      log(e);
    }
    /*------------------------------------ /
    / pulls everything from MutableManager /
    /-------------------------------------*/
    for (use_MUTableDefinition e : use_Halcyon.DEFS)
      p.put(e.key, e.get() == null || e.get().isEmpty() ? e.defaultVal : e.get());
    try
    {
      p.storeToXML(new FileOutputStream(halcyonfolder_Content.SYSCONF_f.make()),
          "HALCYON_DEFAULT_PROGRAM_PROPERTIES\nCONFIGURE TO YOUR LIKINGS WITH CAUTION");
    } catch (IOException e1)
    {
      e1.printStackTrace();
      log(e1);
    }
  }

  public void load_conf()
  {
    check();
    try
    {
      p.loadFromXML(new FileInputStream(halcyonfolder_Content.SYSCONF_f.val));
    } catch (IOException e)
    {
      log(e);
    }
    for (Object r : p.keySet())
      for (use_MUTableDefinition er : use_Halcyon.DEFS)
        if (er.key.equals(r))
          er.validate((String) p.get(er.key));
  }

  public void delete_logs()
  {
    if (halcyonfolder_Content.LOGS_d.make().exists() && halcyonfolder_Content.LOGS_d.make().isDirectory())
    {
      for (File r : halcyonfolder_Content.LOGS_d.make().listFiles())
      { // dont put your files here!! File types are not
        // ignored... sadly, would be a pain to impl and
        // unnecessary
        r.delete();
      }
    }
  }

  public void log(Exception ex)
  {
    log(use_Commons.expand_exception(ex));
  }

  public void check(halcyonfolder_Content e)
  {
    if (e.name().endsWith("d"))
    {
      File r = e.make();
      if (!r.exists() || !r.isDirectory())
        r.mkdir();
    }
    else if (e.name().endsWith("f"))
    {
      File r = e.make();
      if (!r.exists() || !r.isFile())
      {
        try
        {
          r.createNewFile();
        } catch (IOException e1)
        {
          use_Program.error_gui(e1);
        }
      }
    }
    else
      pstream.log.warn("UNRECOGNIZED FOLDER_CONTENT_ENUM_NAME: " + e.name());
  }

  public void serialize(String completeFilename, Serializable e)
  {
    use_FSys.serialize_OBJ(halcyonfolder_Content.CACHE_d.val + use_Halcyon.getFileSeparator() + completeFilename, e,
        this::log);
  }

  public < T > void deserialize(String completeFileName, Class< T > t, impl_ForYou< Exception > error,
      impl_ForYou< T > promise)
  {
    use_FSys.deserialize_OBJ(halcyonfolder_Content.CACHE_d.val + use_Halcyon.getFileSeparator() + completeFileName, t,
        error, promise);
  }

  public void save_playlists()
  {
    check();
    try
    {
      pUsr.loadFromXML(new FileInputStream(halcyonfolder_Content.PLAYLISTS_CONF_f.make()));
    } catch (IOException e1)
    {
      log(e1);
    }
    StringBuilder sb = new StringBuilder();
    for (use_TailwindPlaylist e : const_Global.PLAY_LIST_POOL)
      sb.append(e.getParent() + new String(DELIMITER));
    pUsr.setProperty("playlists", sb.toString());
    sb.setLength(0);
    for (use_TailwindTrack e : const_Global.LIKE_LIST_POOL)
    {
      pstream.log.warn("Saving playlist (APPEND): " + e.id());
      sb.append(e.id() + new String(DELIMITER));
    }
    pUsr.put("liked", sb.toString());
    try
    {
      pUsr.storeToXML(new FileOutputStream(halcyonfolder_Content.PLAYLISTS_CONF_f.make()), "User personalized data");
    } catch (IOException e1)
    {
      log(e1);
    }
  }

  public void master_save()
  {
    save_playlists();
    save_conf();
  }

  public void load_playlists()
  {
    try
    {
      pUsr.loadFromXML(new FileInputStream(halcyonfolder_Content.PLAYLISTS_CONF_f.make()));
    } catch (IOException e1)
    {
      log(e1);
    }
    if (pUsr.get("playlists") != null)
    {
      File f;
      for (String r : ((String) pUsr.get("playlists")).split(new String(DELIMITER)))
      {
        f = new File(r);
        if (f.isDirectory() && f.exists())
        {
          pstream.log.warn("PLAYLIST INVOKE: " + r);
          const_Global.append_to_Playlist(r);
        }
      }
    }
    if (pUsr.get("liked") != null)
    {
      File f;
      for (String r : ((String) pUsr.get("liked")).split(new String(DELIMITER)))
      {
        f = new File(r);
        if (f.isFile() && f.exists())
        {
          pstream.log.warn("LIKED INVOKE: " + r);
          const_Global.append_to_liked(r);
        }
      }
    }
  }

  public void check()
  {
    if (!locale.isDirectory() || !locale.exists())
      locale.mkdir();
    for (halcyonfolder_Content e : halcyonfolder_Content.CACHE_d.getDeclaringClass().getEnumConstants())
      check(e);
  }
}

package com.test;

import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;

import com.jackmeng.Halcyon;
import com.jackmeng.halcyon.use_HalcyonFolder;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Program;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;

public class Test {
  public static void main(String[] args) throws Exception {
    Halcyon.__LINK__();
    BufferedImage ig = ImageIO.read(new File("pkg/com/test/img.png"));

    int i = 1;
    for (TestCase e : new TestCase[] {
        new TestCase("MAFFS_SKILLS", x -> {
          return 1 + 1;
        }, 2),
        new TestCase("Accurate_AccentColor_1", x -> {
          return use_Color.rgbToHex(new int[] { use_Image.accurate_accent_color_1(ig).first,
              use_Image.accurate_accent_color_1(ig).second, use_Image.accurate_accent_color_1(ig).third });
        }),
        new TestCase("HalcyonFolder_Log_Test_1", x -> {
          use_HalcyonFolder hf = use_HalcyonFolder.FOLDER;
          hf.log(new Exception("Amogus"));
          return null;
        }),
    }) {
      pstream.log.warn("Running testcase: " + i);
      e.run();
      i++;
      use_Program.gc();
    }

    /*---------------------------------------------------------------------------------------------------- /
    /   pstream.log.log(use_Color.rgbToHex(new int[] { use_Image.accurate_accent_color_1(ig).first,        /
    /       use_Image.accurate_accent_color_1(ig).second, use_Image.accurate_accent_color_1(ig).third })); /
    / }                                                                                                    /
    /-----------------------------------------------------------------------------------------------------*/
  }
}

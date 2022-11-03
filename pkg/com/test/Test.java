package com.test;

import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;

import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Program;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_Image;

public class Test {
  public static void main(String[] args) throws Exception {
    BufferedImage ig = ImageIO.read(new File("pkg/com/test/img.png"));

    int i = 1;
    for(TestCase e : new TestCase[] {
      new TestCase("Accurate_AccentColor_1", x -> {
        return use_Color.rgbToHex(new int[] { use_Image.accurate_accent_color_1(ig).first,
              use_Image.accurate_accent_color_1(ig).second, use_Image.accurate_accent_color_1(ig).third });
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

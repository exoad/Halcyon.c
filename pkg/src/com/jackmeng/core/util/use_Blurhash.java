package com.jackmeng.core.util;

import java.util.Arrays;

/*----------------------------------------------------------- /
/ implementation of this: https://github.com/woltapp/blurhash /
/ in java                                                     /
/------------------------------------------------------------*/

/*-------------------------------------------------------- /
/ taken from the original Halcyon speed image manipulation /
/ library CloudSpin                                        /
/---------------------------------------------------------*/

/**
 * A blurring to hash implementation in Java
 *
 * @author Jack Meng
 */
public final class use_Blurhash
{

  public static final class blurhash_base83
  {
    public static final char[] TABLE = {
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        '#',
        '$',
        '%',
        '*',
        '+',
        ',',
        '-',
        '.',
        ':',
        ';',
        '=',
        '?',
        '@',
        '[',
        ']',
        '^',
        '_',
        '{',
        '|',
        '}',
        '~',
    };

    private blurhash_base83()
    {
    }

    /**
     * @param val
     * @param exp
     * @return double
     */
    public static double signpow(double val, double exp)
    {
      return Math.copySign(Math.pow(Math.abs(val), exp), val);
    }

    /**
     * Encodes with Base 83.
     *
     * @param val
     *          The value to encode
     * @param length
     *          The length of the value
     * @param buff
     *          The buffer to write to (contains values)
     * @param offset
     *          The offset to start writing at
     */
    public static void encode(long val, int length, char[] buff, int offset)
    {
      int _i = 1;
      for (int i = 1; i <= length; i++)
      {
        int curr = (int) val / _i % 83;
        buff[offset + length - i] = TABLE[curr];
        _i *= 83;
      }
    }

    /**
     * @param val
     * @return long
     */
    public static long encodeDC(double[] val)
    {
      return ((((long) _as_linear(val[0])) << 16) + (((long) _as_linear(val[1])) << 8)
          + _as_linear(val[2]));
    }

    /**
     * @param val
     * @param m
     * @return long
     */
    public static long encodeAC(double[] val, double m)
    {
      return Math
          .round((Math.floor(Math.max(0, Math.min(18, Math.floor(signpow(val[0] / m, 0.5) * 9 + 9.5))))) * 19 * 19
              + (Math.floor(Math.max(0, Math.min(18, Math.floor(signpow(val[1] / m, 0.5) * 9 + 9.5))))) * 19
              + (Math.floor(Math.max(0, Math.min(18, Math.floor(signpow(val[2] / m, 0.5) * 9 + 9.5))))));
    }

    /**
     * Decodes from Base 83
     *
     * @param str
     *          An Encoded String
     * @return The decoded string from base 83
     */
    public static int decode(String str)
    {
      int temp = 0;
      for (char c : str.toCharArray())
      {
        int i = find(c);
        temp = temp * 83 + i;
      }
      return temp;
    }

    /**
     * @param str
     * @param rMv
     * @param color
     */
    public static void decodeAC(String str, double rMv, double[] color)
    {
      int aV = decode(str);
      int qR = aV / (19 * 19);
      int qG = (aV / 19) % 19;
      int qB = aV % 19;
      color[0] = signpow((qR - 9.0) / 9.0, 2.0) * rMv;
      color[1] = signpow((qG - 9.0) / 9.0, 2.0) * rMv;
      color[2] = signpow((qB - 9.0) / 9.0, 2.0) * rMv;
    }

    /**
     * @param str
     * @param colors
     */
    public static void decodeDC(String str, double[] colors)
    {
      int dV = decode(str);
      colors[0] = to_linear(dV >> 16);
      colors[1] = to_linear(dV >> 8 & 0xFF);
      colors[2] = to_linear(dV & 255);
    }

    /**
     * @param c
     * @return int
     */
    public static int find(char c)
    {
      for (int i = 0; i < TABLE.length; i++)
        if (TABLE[i] == c)
          return i;
      return -1;
    }
  }

  /**
   * Represents the colors;
   */
  static double[] __ll = new double[256];

  static
  {
    for (int i = 0; i < __ll.length; i++)
    {
      double _m = i / 255.0d;
      __ll[i] = _m <= 0.04045 ? (_m / 12.92) : (Math.pow((_m + 0.055) / 1.055, 2.4));
    }
  }

  private use_Blurhash()
  {
  }

  /**
   * Finds a max value in an array (2D)
   *
   * @param val
   *          The array
   * @return A max value
   */
  public static double max(double[][] val)
  {
    double max = 0;
    for (double[] doubles : val)
      for (double aDouble : doubles)
        if (aDouble > max)
          max = aDouble;
    return max;
  }

  /**
   * Converts the given number to be within the linear range
   *
   * @param val
   *          The number to convert
   * @return The converted number
   */
  public static double to_linear(int val)
  {
    return val < 0 ? __ll[0] : (val >= 256 ? __ll[255] : __ll[val]);
  }

  /**
   * Converts the given number to be within the sRGB range
   *
   * @param val
   *          The number to convert
   * @return The converted number
   */
  public static int _as_linear(double val)
  {
    int _l = Arrays.binarySearch(__ll, val);
    if (_l < 0)
      _l = ~_l;
    return _l < 0 ? 0 : (_l >= 256 ? 255 : _l);
  }

  /**
   * Encodes the given values into a BlurHash
   *
   * @param pixels
   *          The pixels to encode
   * @param width
   *          The width of the image
   * @param height
   *          The height of the image
   * @param componentX
   *          The x-component of the center of the image
   * @param componentY
   *          The y-component of the center of the image
   * @return The encoded BlurHash as a String
   */
  public static String enc(int[] pixels, int width, int height, int componentX, int componentY)
  {
    double[][] factors = new double[componentX * componentY][3];
    for (int j = 0; j < componentY; j++)
    {
      for (int i = 0; i < componentX; i++)
      {
        double normalisation = (i == 0 && j == 0) ? 1 : 2;
        double r = 0, g = 0, b = 0;
        for (int x = 0; x < width; x++)
        {
          for (int y = 0; y < height; y++)
          {
            double basis = normalisation
                * Math.cos((Math.PI * i * x) / width)
                * Math.cos((Math.PI * j * y) / height);
            int pixel = pixels[y * width + x];
            r += basis * to_linear((pixel >> 16) & 0xff);
            g += basis * to_linear((pixel >> 8) & 0xff);
            b += basis * to_linear(pixel & 0xff);
          }
        }
        double scale = 1.0 / (width * height);
        int index = j * componentX + i;
        factors[index][0] = r * scale;
        factors[index][1] = g * scale;
        factors[index][2] = b * scale;
      }
    }

    int factorsLength = factors.length;
    char[] hash = new char[1 + 1 + 4 + 2 * (factorsLength - 1)];

    long sizeFlag = (long) componentX - 1 + (componentY - 1) * 9;
    blurhash_base83.encode(sizeFlag, 1, hash, 0);

    double maximumValue;
    if (factorsLength > 1)
    {
      double actualMaximumValue = max(factors);
      double quantisedMaximumValue = Math
          .floor(Math.max(0, Math.min(82, Math.floor(actualMaximumValue * 166 - 0.5))));
      maximumValue = (quantisedMaximumValue + 1) / 166;
      blurhash_base83.encode(Math.round(quantisedMaximumValue), 1, hash, 1);
    }
    else
    {
      maximumValue = 1;
      blurhash_base83.encode(0, 1, hash, 1);
    }

    double[] dc = factors[0];
    blurhash_base83.encode(blurhash_base83.encodeDC(dc), 4, hash, 2);

    for (int i = 1; i < factorsLength; i++)
      blurhash_base83.encode(blurhash_base83.encodeAC(factors[i], maximumValue), 2, hash, 6 + 2 * (i - 1));
    return new String(hash);
  }

  /**
   * Decodes the given BlurHash into an array of pixels
   *
   * @param blurHash
   *          The BlurHash to decode (String)
   * @param width
   *          The width of the image
   * @param height
   *          The height of the image
   * @param punch
   *          The punch value of the image; often regarded as the
   *          "sharpness" of the image
   * @return The decoded pixels
   */
  public static int[] dec(String blurHash, int width, int height, double punch)
  {
    int blurHashLength = blurHash.length();
    if (blurHashLength < 6)
      throw new IllegalArgumentException("BlurHash must be at least 6 characters long");
    int sizeInfo = blurhash_base83.decode(blurHash.substring(0, 1));
    int sizeY = sizeInfo / 9 + 1;
    int sizeX = sizeInfo % 9 + 1;

    int quantMaxValue = blurhash_base83.decode(blurHash.substring(1, 2));
    double rmV = (quantMaxValue + 1) / 166.0 * punch;

    double[][] colors = new double[sizeX * sizeY][3];
    blurhash_base83.decodeDC(blurHash.substring(2, 6), colors[0]);
    for (int i = 1; i < sizeX * sizeY; i++)
    {
      blurhash_base83.decodeAC(blurHash.substring(4 + i * 2, 6 + i * 2), rmV, colors[i]);
    }
    int[] pixels = new int[width * height];
    int pos = 0;
    for (int j = 0; j < height; j++)
    {
      for (int i = 0; i < width; i++)
      {
        double r = 0, g = 0, b = 0;
        for (int y = 0; y < sizeY; y++)
        {
          for (int x = 0; x < sizeX; x++)
          {
            double basic = Math.cos(Math.PI * x * i / width) *
                Math.cos(Math.PI * y * j / height);
            double[] color = colors[x + y * sizeX];
            r += (color[0] * basic);
            g += (color[1] * basic);
            b += (color[2] * basic);
          }
        }
        pixels[pos++] = 255 << 24 | (_as_linear(r) & 255) << 16 |
            (_as_linear(g) & 255) << 8 | (_as_linear(b) & 255);
      }
    }
    return pixels;
  }
}
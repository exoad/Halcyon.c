package com.jackmeng.sys;

public enum ansi_Colors {
  /*------------------------------------------------ /
  / Represents all possible STANDARD ANSI COLOR SETS /
  /-------------------------------------------------*/

  RED_BG("\u001B[41m"), GREEN_BG("\u001B[42m"), YELLOW_BG("\u001B[43m"), BLUE_BG("\u001B[44m"), MAGENTA_BG(
      "\u001B[45m"), CYAN_BG("\u001B[46m"), WHITE_BG("\u001B[47m"), BLACK_BG("\u001B[40m"), RESET("\u001B[0m"), BOLD(
          "\u001B[1m"), UNDERLINE("\u001B[4m"), BLINK("\u001B[5m"), REVERSE("\u001B[7m"), HIDDEN("\u001B[8m"), RED_TXT(
              "\u001B[31m"), GREEN_TXT("\u001B[32m"), YELLOW_TXT("\u001B[33m"), BLUE_TXT("\u001B[34m"), MAGENTA_TXT(
                  "\u001B[35m"), CYAN_TXT("\u001B[36m"), WHITE_TXT("\u001B[37m"), BLACK_TXT("\u001B[30m");

  private final String color;

  ansi_Colors(String color)
  {
    this.color = color;
  }

  public String color()
  {
    return color;
  }

  public String brighter()
  {
    return color.endsWith(";1m") ? color : color + ";1m";
  }

  public static String construct(int id)
  {
    return id >= 0 && id <= 255 ? "\u001b[38;5;" + id + "m" : WHITE_TXT.color;
  }
}

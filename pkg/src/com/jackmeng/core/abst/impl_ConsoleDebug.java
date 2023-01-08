package com.jackmeng.core.abst;

import com.jackmeng.core.util.pstream;
import com.jackmeng.core.util.use_AnsiColors;

public interface impl_ConsoleDebug
{
  default String cli_Identifier()
  {
    return use_AnsiColors.BOLD.color() + use_AnsiColors.MAGENTA_BG.color() + use_AnsiColors.BLACK_TXT.color()
        + getClass().getCanonicalName() + "_Info" + use_AnsiColors.RESET.color();
  }

  default void dblog(Object e)
  {
    pstream.log.info(cli_Identifier() + ": " + e);
  }

}

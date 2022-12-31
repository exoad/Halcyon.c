package com.jackmeng.core.abst;

import javax.swing.*;

import java.util.Optional;

public abstract interface impl_App
    extends
    Runnable,
    impl_Identifiable
{
  ImageIcon icon();

  /**
   * Tooltip referencia
   *
   * @return
   */
  String toolTip();

  Optional< ImageIcon > rolloverIcon();

  default boolean hidden()
  {
    return true;
  }
}

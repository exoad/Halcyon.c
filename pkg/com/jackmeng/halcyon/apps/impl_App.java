package com.jackmeng.halcyon.apps;

import javax.swing.*;
import java.util.Optional;

public interface impl_App extends Runnable, impl_Identifiable {
  public ImageIcon icon();

  /**
   * Tooltip referencia
   * @return
   */
  public String toolTip();

  public Optional<ImageIcon> rolloverIcon();

  default public boolean hidden() {
    return true;
  }
}

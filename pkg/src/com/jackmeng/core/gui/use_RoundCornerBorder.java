package com.jackmeng.core.gui;

import javax.swing.border.Border;
import java.awt.*;

public class use_RoundCornerBorder
    implements
    Border
{

  private int radius, strokeThickness;
  private Color color;

  public use_RoundCornerBorder(int radius, int thickness, Color color)
  {
    this.radius = radius;
    this.color = color;
    this.strokeThickness = thickness;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    g2.setStroke(new BasicStroke(strokeThickness));
    g2.setClip(x, y, width, height);
    g2.setColor(color);
    g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    g2.setClip(null);
  }

  @Override
  public Insets getBorderInsets(Component c)
  {
    return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
  }

  @Override
  public boolean isBorderOpaque()
  {
    return true;
  }
}

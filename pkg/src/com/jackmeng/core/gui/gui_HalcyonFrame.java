package com.jackmeng.core.gui;

import com.jackmeng.core.const_MUTableKeys;
import com.jackmeng.core.use_HalcyonCore;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Task;
import com.jackmeng.util.sys_SimpleMaffs;
import com.jackmeng.util.use_Color;
import com.jackmeng.util.use_ResourceFetcher;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class gui_HalcyonFrame
    implements Runnable
{

  public static class TitledFrame
      implements Runnable
  {
    public static class ComponentResizer
        extends MouseAdapter
    {
      private static final Dimension MINIMUM_SIZE = new Dimension(10, 10);
      private static final Dimension MAXIMUM_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

      private static Map< Integer, Integer > cursors = new HashMap<>();
      {
        cursors.put(1, Cursor.N_RESIZE_CURSOR);
        cursors.put(2, Cursor.W_RESIZE_CURSOR);
        cursors.put(4, Cursor.S_RESIZE_CURSOR);
        cursors.put(8, Cursor.E_RESIZE_CURSOR);
        cursors.put(3, Cursor.NW_RESIZE_CURSOR);
        cursors.put(9, Cursor.NE_RESIZE_CURSOR);
        cursors.put(6, Cursor.SW_RESIZE_CURSOR);
        cursors.put(12, Cursor.SE_RESIZE_CURSOR);
      }

      private Insets dragInsets;
      private Dimension snapSize;

      private int direction;
      protected static final int NORTH = 1;
      protected static final int WEST = 2;
      protected static final int SOUTH = 4;
      protected static final int EAST = 8;

      private Cursor sourceCursor;
      private boolean resizing;
      private Rectangle bounds;
      private Point pressed;
      private boolean autoscrolls;

      private Dimension minimumSize = MINIMUM_SIZE;
      private Dimension maximumSize = MAXIMUM_SIZE;

      /**
       * Convenience contructor. All borders are resizable in increments of
       * a single pixel. Components must be registered separately.
       */
      public ComponentResizer()
      {
        this(new Insets(5, 5, 5, 5), new Dimension(1, 1));
      }

      /**
       * Convenience contructor. All borders are resizable in increments of
       * a single pixel. Components can be registered when the class is created
       * or they can be registered separately afterwards.
       *
       * @param components
       *          components to be automatically registered
       */
      public ComponentResizer(Component... components)
      {
        this(new Insets(5, 5, 5, 5), new Dimension(1, 1), components);
      }

      /**
       * Convenience contructor. Eligible borders are resisable in increments of
       * a single pixel. Components can be registered when the class is created
       * or they can be registered separately afterwards.
       *
       * @param dragInsets
       *          Insets specifying which borders are eligible to be
       *          resized.
       * @param components
       *          components to be automatically registered
       */
      public ComponentResizer(Insets dragInsets, Component... components)
      {
        this(dragInsets, new Dimension(1, 1), components);
      }

      /**
       * Create a ComponentResizer.
       *
       * @param dragInsets
       *          Insets specifying which borders are eligible to be
       *          resized.
       * @param snapSize
       *          Specify the dimension to which the border will snap to
       *          when being dragged. Snapping occurs at the halfway mark.
       * @param components
       *          components to be automatically registered
       */
      public ComponentResizer(Insets dragInsets, Dimension snapSize, Component... components)
      {
        setDragInsets(dragInsets);
        setSnapSize(snapSize);
        registerComponent(components);
      }

      /**
       * Get the drag insets
       *
       * @return the drag insets
       */
      public Insets getDragInsets()
      {
        return dragInsets;
      }

      /**
       * Set the drag dragInsets. The insets specify an area where mouseDragged
       * events are recognized from the edge of the border inwards. A value of
       * 0 for any size will imply that the border is not resizable. Otherwise
       * the appropriate drag cursor will appear when the mouse is inside the
       * resizable border area.
       *
       * @param dragInsets
       *          Insets to control which borders are resizeable.
       */
      public void setDragInsets(Insets dragInsets)
      {
        validateMinimumAndInsets(minimumSize, dragInsets);

        this.dragInsets = dragInsets;
      }

      /**
       * Get the components maximum size.
       *
       * @return the maximum size
       */
      public Dimension getMaximumSize()
      {
        return maximumSize;
      }

      /**
       * Specify the maximum size for the component. The component will still
       * be constrained by the size of its parent.
       *
       * @param maximumSize
       *          the maximum size for a component.
       */
      public void setMaximumSize(Dimension maximumSize)
      {
        this.maximumSize = maximumSize;
      }

      /**
       * Get the components minimum size.
       *
       * @return the minimum size
       */
      public Dimension getMinimumSize()
      {
        return minimumSize;
      }

      /**
       * Specify the minimum size for the component. The minimum size is
       * constrained by the drag insets.
       *
       * @param minimumSize
       *          the minimum size for a component.
       */
      public void setMinimumSize(Dimension minimumSize)
      {
        validateMinimumAndInsets(minimumSize, dragInsets);

        this.minimumSize = minimumSize;
      }

      /**
       * Remove listeners from the specified component
       *
       * @param component
       *          the component the listeners are removed from
       */
      public void deregisterComponent(Component... components)
      {
        for (Component component : components)
        {
          component.removeMouseListener(this);
          component.removeMouseMotionListener(this);
        }
      }

      /**
       * Add the required listeners to the specified component
       *
       * @param component
       *          the component the listeners are added to
       */
      public void registerComponent(Component... components)
      {
        for (Component component : components)
        {
          component.addMouseListener(this);
          component.addMouseMotionListener(this);
        }
      }

      /**
       * Get the snap size.
       *
       * @return the snap size.
       */
      public Dimension getSnapSize()
      {
        return snapSize;
      }

      /**
       * Control how many pixels a border must be dragged before the size of
       * the component is changed. The border will snap to the size once
       * dragging has passed the halfway mark.
       *
       * @param snapSize
       *          Dimension object allows you to separately spcify a
       *          horizontal and vertical snap size.
       */
      public void setSnapSize(Dimension snapSize)
      {
        this.snapSize = snapSize;
      }

      /**
       * When the components minimum size is less than the drag insets then
       * we can't determine which border should be resized so we need to
       * prevent this from happening.
       */
      private void validateMinimumAndInsets(Dimension minimum, Insets drag)
      {
        int minimumWidth = drag.left + drag.right;
        int minimumHeight = drag.top + drag.bottom;

        if (minimum.width < minimumWidth
            || minimum.height < minimumHeight)
        {
          String message = "Minimum size cannot be less than drag insets";
          throw new IllegalArgumentException(message);
        }
      }

      /**
       */
      @Override
      public void mouseMoved(MouseEvent e)
      {
        Component source = e.getComponent();
        Point location = e.getPoint();
        direction = 0;

        if (location.x < dragInsets.left)
          direction += WEST;

        if (location.x > source.getWidth() - dragInsets.right - 1)
          direction += EAST;

        if (location.y < dragInsets.top)
          direction += NORTH;

        if (location.y > source.getHeight() - dragInsets.bottom - 1)
          direction += SOUTH;

        // Mouse is no longer over a resizable border

        if (direction == 0)
        {
          source.setCursor(sourceCursor);
        }
        else // use the appropriate resizable cursor
        {
          int cursorType = cursors.get(direction);
          Cursor cursor = Cursor.getPredefinedCursor(cursorType);
          source.setCursor(cursor);
        }
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
        if (!resizing)
        {
          Component source = e.getComponent();
          sourceCursor = source.getCursor();
        }
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        if (!resizing)
        {
          Component source = e.getComponent();
          source.setCursor(sourceCursor);
        }
      }

      @Override
      public void mousePressed(MouseEvent e)
      {
        // The mouseMoved event continually updates this variable

        if (direction == 0)
          return;

        // Setup for resizing. All future dragging calculations are done based
        // on the original bounds of the component and mouse pressed location.

        resizing = true;

        Component source = e.getComponent();
        pressed = e.getPoint();
        SwingUtilities.convertPointToScreen(pressed, source);
        bounds = source.getBounds();

        // Making sure autoscrolls is false will allow for smoother resizing
        // of components

        if (source instanceof JComponent)
        {
          JComponent jc = (JComponent) source;
          autoscrolls = jc.getAutoscrolls();
          jc.setAutoscrolls(false);
        }
      }

      /**
       * Restore the original state of the Component
       */
      @Override
      public void mouseReleased(MouseEvent e)
      {
        resizing = false;

        Component source = e.getComponent();
        source.setCursor(sourceCursor);

        if (source instanceof JComponent)
        {
          ((JComponent) source).setAutoscrolls(autoscrolls);
        }
      }

      /**
       * Resize the component ensuring location and size is within the bounds
       * of the parent container and that the size is within the minimum and
       * maximum constraints.
       *
       * All calculations are done using the bounds of the component when the
       * resizing started.
       */
      @Override
      public void mouseDragged(MouseEvent e)
      {
        if (!resizing)
          return;

        Component source = e.getComponent();
        Point dragged = e.getPoint();
        SwingUtilities.convertPointToScreen(dragged, source);

        changeBounds(source, direction, bounds, pressed, dragged);
      }

      protected void changeBounds(Component source, int direction, Rectangle bounds, Point pressed, Point current)
      {
        // Start with original locaton and size

        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;

        // Resizing the West or North border affects the size and location

        if (WEST == (direction & WEST))
        {
          int drag = getDragDistance(pressed.x, current.x, snapSize.width);
          int maximum = Math.min(width + x, maximumSize.width);
          drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);

          x -= drag;
          width += drag;
        }

        if (NORTH == (direction & NORTH))
        {
          int drag = getDragDistance(pressed.y, current.y, snapSize.height);
          int maximum = Math.min(height + y, maximumSize.height);
          drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);

          y -= drag;
          height += drag;
        }

        // Resizing the East or South border only affects the size

        if (EAST == (direction & EAST))
        {
          int drag = getDragDistance(current.x, pressed.x, snapSize.width);
          Dimension boundingSize = getBoundingSize(source);
          int maximum = Math.min(boundingSize.width - x, maximumSize.width);
          drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);
          width += drag;
        }

        if (SOUTH == (direction & SOUTH))
        {
          int drag = getDragDistance(current.y, pressed.y, snapSize.height);
          Dimension boundingSize = getBoundingSize(source);
          int maximum = Math.min(boundingSize.height - y, maximumSize.height);
          drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);
          height += drag;
        }

        source.setBounds(x, y, width, height);
        source.validate();
      }

      /*
       * Determine how far the mouse has moved from where dragging started
       */
      private int getDragDistance(int larger, int smaller, int snapSize)
      {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
      }

      /*
       * Adjust the drag value to be within the minimum and maximum range.
       */
      private int getDragBounded(int drag, int snapSize, int dimension, int minimum, int maximum)
      {
        while (dimension + drag < minimum)
          drag += snapSize;

        while (dimension + drag > maximum)
          drag -= snapSize;

        return drag;
      }

      /*
       * Keep the size of the component within the bounds of its parent.
       */
      private Dimension getBoundingSize(Component source)
      {
        if (source instanceof Window)
        {
          GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
          Rectangle bounds = env.getMaximumWindowBounds();
          return new Dimension(bounds.width, bounds.height);
        }
        else
        {
          return source.getParent().getSize();
        }
      }
    }

    private final JFrame frame;
    private boolean maximizedFrame = false;
    private final int titleHeight;
    private int pX = 0;
    private int pY = 0;

    public TitledFrame(final TitleBarConfig conf, final int tH, final JComponent content, Runnable endExec)
    {
      this(conf, tH, content);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e)
        {
          endExec.run();
        }

        @Override
        public void windowClosed(WindowEvent e)
        {
          endExec.run();
        }
      });
    }

    private ComponentResizer cr = new ComponentResizer();

    public TitledFrame(final TitleBarConfig conf, final int tH, final JComponent content)
    {
      int titleHeightOffSub = 3;
      int contentOff = tH - titleHeightOffSub;
      frame = new JFrame();

      frame.setTitle(conf.titleStr);
      if (conf.icon != null)
        frame.setIconImage(conf.icon.getImage());
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      if (tH <= titleHeightOffSub || !Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)
          || const_MUTableKeys.title_frame_styling)
      {
        frame.setUndecorated(false);
        this.titleHeight = 0;
        frame.setPreferredSize(content.getPreferredSize());
        frame.getContentPane().add(content);
        pstream.log.info("Launching a [NATIVE] styled window");
      }
      else
      {
        frame.setUndecorated(true);

        this.titleHeight = tH;
        frame.setPreferredSize(
            new Dimension(content.getPreferredSize().width, titleHeight + content.getPreferredSize().height));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.getRootPane()
            .setBorder(BorderFactory.createLineBorder(conf.borderColor != null ? conf.borderColor : Color.BLACK, 3));
        if (content.getMaximumSize() == null
            || !(content.getMaximumSize().width == content.getPreferredSize().width
                && content.getMaximumSize().height == content.getPreferredSize().height)
            || (content.getMaximumSize().width <= frame.getMaximumSize().width
                && content.getMaximumSize().height <= frame.getMaximumSize().height))
        {
          cr.registerComponent(frame);
          long[] r = sys_SimpleMaffs.simplify_ratio(use_GuiUtil.screen_space().width,
              use_GuiUtil.screen_space().height);
          if (r == null || r.length == 0 || r[1] == 0 || r[0] == 0)
            r = new long[] { 1L, 1L };
          cr.setSnapSize(new Dimension((int) (10 * (r[0] / r[1])),
              (int) (10 *
                  (r[0] / r[1]))));
          frame.addMouseMotionListener(cr);
          frame.addMouseListener(cr);
          frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        }
        pstream.log.info("Launching a [CUSTOM] styled window");

        /*------------------------------------------------------------ /
        / dont set the frame background to anything, causes flickering /
        /-------------------------------------------------------------*/

        JPanel titleBar = new JPanel();
        titleBar.setPreferredSize(new Dimension(content.getSize().width, titleHeight));
        titleBar.addComponentListener(new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e)
          {
            if (titleBar.getPreferredSize().height > titleHeight)
              titleBar.setSize(new Dimension(frame.getPreferredSize().width, titleHeight));
          }
        });
        titleBar.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent me)
          {
            pX = me.getX();
            pY = me.getY();
          }

          @Override
          public void mouseDragged(MouseEvent me)
          {
            frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
          }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent me)
          {
            frame.setLocation(frame.getLocation().x + me.getX() - pX, frame.getLocation().y + me.getY() - pY);
          }
        });
        titleBar.setLayout(new BorderLayout(10, 0));

        JLabel titleBarStr = new JLabel(conf.titleStr);
        if (conf.titleStrFont != null)
          titleBarStr.setFont(conf.titleStrFont);
        titleBarStr.setForeground(conf.fg);
        titleBarStr.setHorizontalAlignment(SwingConstants.CENTER);
        titleBarStr.setVerticalAlignment(SwingConstants.CENTER);

        JPanel btns = new JPanel();
        btns.setLayout(new FlowLayout(FlowLayout.CENTER));
        btns.setOpaque(true);

        if (conf.bgMis != null)
        {
          btns.add(gen_Button(conf.bgMis, () -> frame.setAlwaysOnTop(!frame.isAlwaysOnTop())));
        }
        if (conf.bgMini != null)
        {
          btns.add(gen_Button(conf.bgMini, () -> frame.setState(Frame.ICONIFIED)));
        }
        if (conf.bgExp != null)
        {
          btns.add(gen_Button(conf.bgExp, () -> {
            frame.setExtendedState(maximizedFrame ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
            maximizedFrame = !maximizedFrame;
          }));
        }
        btns.add(gen_Button(conf.bgClose, frame::dispose));

        JLabel titleBarICO = new JLabel(conf.icon != null
            ? new ImageIcon(conf.icon.getImage().getScaledInstance(contentOff, contentOff, Image.SCALE_AREA_AVERAGING))
            : new ImageIcon());
        if (conf.icon == null)
        {
          titleBarICO.setPreferredSize(new Dimension(titleHeight, titleHeight));
          titleBarICO.setOpaque(true);
          titleBarICO.setBackground(conf.fg);
        }
        titleBarICO.setVerticalAlignment(SwingConstants.CENTER);

        titleBar.add(titleBarICO, BorderLayout.WEST);
        titleBar.add(titleBarStr, BorderLayout.CENTER);
        titleBar.add(btns, BorderLayout.EAST);
        titleBar.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e)
          {
            if (e.getClickCount() >= 2)
            {
              frame.setExtendedState(maximizedFrame ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
              maximizedFrame = !maximizedFrame;
            }
          }
        });
        final int TITLEBAR_HEIGHT_REFERENDUM = titleBar.getPreferredSize().height;
        titleBar.addComponentListener(new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e)
          {
            titleBar.setSize(new Dimension(frame.getSize().width, TITLEBAR_HEIGHT_REFERENDUM));
            content.setPreferredSize(
                new Dimension(frame.getSize().width, frame.getSize().height - TITLEBAR_HEIGHT_REFERENDUM));
          }
        });
        titleBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleBar.setAlignmentY(Component.TOP_ALIGNMENT);
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.setAlignmentY(Component.TOP_ALIGNMENT);

        frame.getContentPane().add(titleBar);
        frame.getContentPane().add(content);
        frame.getContentPane().add(Box.createHorizontalGlue());
        cr.setMinimumSize(frame.getMinimumSize());

        // all the above stuffs make the titlebar be pushed upwards so it doesn't go
        // wonky on a custom titlebar
        // this took a lot of debugging and mindless code breaking to get it to work!!!
        // also pls fix code, bc rn this code is kind of lets say messy :|

        if (const_MUTableKeys.gui_use_debug)
        {
          titleBar.setBorder(use_HalcyonCore.getDebugBorder());
          SwingUtilities.invokeLater(() -> {
            use_GuiUtil.listComponents_OfContainer(frame).forEach(x -> {
              try
              {
                if (x instanceof JComponent && ((JComponent) x).getBorder() != null)
                  ((JComponent) x).setBorder(use_HalcyonCore.getDebugBorder());
              } catch (Exception e)
              {
                // IGNORE, probably some .setBorder() not supported bs
              }
            });
          });
        }
      }
    }

    /*---------------------------------------------------------------------------------------------------------------- /
    / public void askStatus(struct_Trio< ImageIcon, String, Optional< Runnable > > exec, boolean autoresize)           /
    / {                                                                                                                /
    /   pstream.log.info("Status Update for frame title: " + exec.second);                                             /
    /   use_Task.run_Snb_1(() -> {                                                                                     /
    /     status.setToolTipText(exec.second);                                                                          /
    /     if (autoresize)                                                                                              /
    /     {                                                                                                            /
    /       exec.first.setImage(use_Image                                                                              /
    /           .resize_fast_1(status.getPreferredSize().width, status.getPreferredSize().height, exec.first).getImage()); /
    /     }                                                                                                            /
    /     status.setIcon(exec.first);                                                                                  /
    /     pstream.log.info("Status Update for frame icon: " + exec.first.getDescription());                            /
    /     status.repaint(50L);                                                                                         /
    /     status.revalidate();                                                                                         /
    /     exec.third.ifPresent(Runnable::run);                                                                         /
    /   });                                                                                                            /
    / }                                                                                                                /
    /-----------------------------------------------------------------------------------------------------------------*/

    /*------------------------------------------- /
    / public void askStatus()                     /
    / {                                           /
    /   pstream.log.info("Status cancelling..."); /
    /   use_Task.run_Snb_1(() -> {                /
    /     status.setToolTipText(null);            /
    /     status.setIcon(null);                   /
    /   });                                       /
    / }                                           /
    /--------------------------------------------*/

    public JFrame expose()
    {
      return frame;
    }

    private static class TitleBarButton extends JButton
    {
      private boolean hovering = false;
      private final int size;
      private final Color clr;

      public TitleBarButton(int size, Color color, Runnable func)
      {
        this.size = size;
        this.clr = color;

        addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e)
          {
            hovering = true;
            SwingUtilities.invokeLater(() -> repaint(50L));
          }

          @Override
          public void mouseExited(MouseEvent e)
          {
            hovering = false;
            SwingUtilities.invokeLater(() -> repaint(50L));
          }

          @Override
          public void mouseEntered(MouseEvent e)
          {
            hovering = true;
            SwingUtilities.invokeLater(() -> repaint(70L));
          }

          @Override
          public void mouseClicked(MouseEvent e)
          {
            hovering = true;
            SwingUtilities.invokeLater(() -> repaint(70L));
          }
        });

        addActionListener(x -> func.run());
        setRolloverEnabled(false);
        setBorder(null);
        setContentAreaFilled(false);
        setAlignmentY(Component.CENTER_ALIGNMENT);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(size + 1, size + 1));
        setOpaque(true);
      }

      @Override
      public void paintComponent(Graphics g)
      {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getSize().width, getSize().height); // fixes the overlaying issue and prevents old content
                                                               // from being overused
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(clr);
        g2.fillOval(0, 0, size, size);
        if (hovering && this.clr != null)
        {
          clr.brighter();
          g2.setColor(use_Color.darker(clr, 0.6D));
          // ON LOW RES SCREENS, MUST BE ODD NUMBER TO WORK PROPERLY WITH
          int SMALL_RADIUS = size - (size / 2) % 2 != 0 ? size - (size / 2) + 1 : size - (size / 2);
          g2.fillOval((size / 2) - (SMALL_RADIUS / 2), (size / 2) - (SMALL_RADIUS / 2), SMALL_RADIUS, SMALL_RADIUS);
        }
        g2.dispose();
        g.dispose();
      }

    }

    private static JButton gen_Button(Color color, Runnable func)
    {
      return new TitleBarButton(15, color, func);
    }

    @Override
    public void run()
    {
      /*--------------------------------------------------------------------------------------- /
      / AHHH there's some funky shit with why it sometimes renders the frame with Transparency. /
      /-----------------------------2-----------------------------------------------------------*/
      frame.setAutoRequestFocus(true);
      frame.pack();
      frame.setLocation(new Point(use_GuiUtil.center_OfScreen().first - (frame.getSize().width / 2),
          use_GuiUtil.center_OfScreen().second - (frame.getSize().height / 2)));
      frame.setVisible(true);
    }
  }

  public static class TitleBarConfig
  {
    public String titleStr;
    public ImageIcon icon;
    public Color fg, bg, bgClose, bgMini, bgExp, bgMis, borderColor;
    public Font titleStrFont;

    // required parameters: 8
    public TitleBarConfig(String str, ImageIcon ico, Font titleStrFont, Color fg, Color bg, Color bgClose, Color bgMini,
        Color bgExp, Color bgMis, Color borderColor)
    {
      this.titleStr = str;
      this.icon = ico;
      this.fg = fg;
      this.bg = bg;
      this.bgClose = bgClose;
      this.bgMini = bgMini;
      this.bgExp = bgExp;
      this.bgMis = bgMis;
      this.titleStrFont = titleStrFont;
      this.borderColor = borderColor;
    }
  }

  private final TitledFrame frame;

  public gui_HalcyonFrame(JComponent top, JComponent bottom)
  {
    if (const_Manager.DEBUG_GRAPHICS)
    {
      top.setOpaque(true);
      top.setBackground(use_Color.rndColor());
      bottom.setOpaque(true);
      bottom.setBackground(use_Color.rndColor());
    }

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
    splitPane.setDividerLocation((top.getPreferredSize().height + bottom.getPreferredSize().height) / 2);
    splitPane.setDividerSize(0);

    frame = new TitledFrame(new TitleBarConfig("Halcyon",
        use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.GUI_PROGRAM_LOGO),
        use_HalcyonCore.regularFont().deriveFont(const_Manager.PROGRAM_DEFAULT_FONT_SIZE),
        const_ColorManager.DEFAULT_GREEN_FG, const_ColorManager.DEFAULT_BG, const_ColorManager.DEFAULT_RED_FG,
        const_ColorManager.DEFAULT_YELLOW_FG, const_ColorManager.DEFAULT_GREEN_FG, const_ColorManager.DEFAULT_PINK_FG,
        const_ColorManager.DEFAULT_BG),
        const_Manager.FRAME_TITLEBAR_HEIGHT, splitPane);

    frame.expose().addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e)
      {
        /*------------------------------------------------------------------------------ /
        / dont have to wait for the JVM to actually realise the root component is gone!! /
        /-------------------------------------------------------------------------------*/
        System.exit(0);
      }
    });

    /*---------------------------------------------------------------------------------------------------------------- /
    / frame.expose().setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, frame.expose().getPreferredSize().width, /
    /     frame.expose().getPreferredSize().height, 25, 25));                                                          /
    /-----------------------------------------------------------------------------------------------------------------*/
  }

  /**
   * @return JFrame
   */
  public final JFrame expose()
  {
    return frame.expose();
  }

  public final TitledFrame expose_internal()
  {
    return frame;
  }

  @Override
  public final void run()
  {
    use_Task.run_Snb_1(frame);
  }
}

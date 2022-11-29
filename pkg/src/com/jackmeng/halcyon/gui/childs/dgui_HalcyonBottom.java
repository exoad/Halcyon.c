package com.jackmeng.halcyon.gui.childs;

import com.jackmeng.const_Global;
import com.jackmeng.halcyon.const_MUTableKeys;
import com.jackmeng.halcyon.use_Halcyon;
import com.jackmeng.halcyon.abst.evnt_RemoveTab;
import com.jackmeng.halcyon.abst.evnt_SelectPlaylistTrack;
import com.jackmeng.halcyon.abst.impl_Guard;
import com.jackmeng.halcyon.abst.impl_HalcyonRefreshable;
import com.jackmeng.halcyon.gui.const_ColorManager;
import com.jackmeng.halcyon.gui.const_Manager;
import com.jackmeng.halcyon.gui.const_ResourceManager;
import com.jackmeng.sys.pstream;
import com.jackmeng.sys.use_Task;
import com.jackmeng.tailwind.use_TailwindPlaylist;
import com.jackmeng.tailwind.use_TailwindTrack;
import com.jackmeng.util.use_Commons;
import com.jackmeng.util.use_Image;
import com.jackmeng.util.use_ResourceFetcher;
import com.jackmeng.util.use_Struct.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import static com.jackmeng.halcyon.gui.const_Lang.*;

public class dgui_HalcyonBottom
    extends JSplitPane
{

  public static class dgui_HalcyonFileList
      extends JScrollPane
      implements
      impl_HalcyonRefreshable< struct_Pair< Optional< String >, Optional< use_TailwindPlaylist > > >,
      evnt_SelectPlaylistTrack
  {

    private TitledBorder border;
    private final JTabbedPane pane;

    /*-------------------------------------------------------------------------------------------------------------- /
    / Map "guiTrees" representation:                                                                                 /
    /     element_1 ->                                                                                               /
    /       Type: String                                                                                             /
    /       Description: "The id of this tree"                                                                       /
    /     element_2 ->                                                                                               /
    /       Type: struct_Trio                                                                                           /
    /         element_1 ->                                                                                           /
    /           Type: JTree                                                                                          /
    /           Description: "The JTree component representing this tree"                                            /
    /         element_2 ->                                                                                           /
    /           Type: MutableTreeNode                                                                                /
    /           Description: "The Root Node"                                                                         /
    /         element_3 ->                                                                                           /
    /           Type: List:MutableTreeNode                                                                           /
    /           Description: "All sub nodes under the root node"                                                     /
    /       Description: "The UI Elements of this tree"                                                              /
    /                                                                                                                /
    / private transient Map<String, struct_Trio<JTree, DefaultMutableTreeNode, List<DefaultMutableTreeNode>>> guiTrees; /
    /                                                                                                                /
    /---------------------------------------------------------------------------------------------------------------*/

    private final transient Map< String, struct_Trio< struct_Pair< Integer, JTree >, DefaultMutableTreeNode, java.util.List< DefaultMutableTreeNode > > > guiTrees;
    /*------------------------------------------------------------------------------------------------------------------ /
    / stupid linter wants this to be transient for no goddamn reason when JComponent is serialized and inherited here????/
    /-------------------------------------------------------------------------------------------------------------------*/

    /*---------------------------------------------- /
    / Common IDs for the default constant PLAYLISTS: /
    / "lip" -> "liked playlists"                     /
    /-----------------------------------------------*/

    private class filelist_TabButtons
        extends JPanel
    {
      private final transient evnt_RemoveTab listener;

      public filelist_TabButtons(String labelStr, evnt_RemoveTab listener)
      {
        this.listener = listener;

        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);

        JLabel label = new JLabel(labelStr);

        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        add(new filelist_CloseTab());
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
      }

      private class filelist_CloseTab
          extends JButton
          implements ActionListener
      {

        public filelist_CloseTab()
        {
          setPreferredSize(new Dimension(const_Manager.DGUI_APPS_FILELIST_TABBUTTONS_ICON_W_H,
              const_Manager.DGUI_APPS_FILELIST_TABBUTTONS_ICON_W_H));
          setToolTipText("Close Tab");
          setUI(getUI());
          setContentAreaFilled(false);
          setBorderPainted(false);
          setFocusable(false);
          setRolloverEnabled(false);
          addActionListener(this);
        }

        @Override
        public void paintComponent(Graphics g)
        {
          super.paintComponent(g);
          Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setStroke(new BasicStroke(1.5F));
          g2.setColor(const_ColorManager.DEFAULT_GREEN_FG);
          g2.drawLine(3, 3, getWidth() - 3, getHeight() - 3);
          g2.drawLine(getWidth() - 3, 3, 3, getHeight() - 3);
          g2.dispose();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
          int i = dgui_HalcyonFileList.this.pane.indexOfTabComponent(filelist_TabButtons.this);
          if (i != -1)
          {
            dgui_HalcyonFileList.this.pane.remove(i);
            filelist_TabButtons.this.listener.removedTab();
          }
        }
      }
    }

    public dgui_HalcyonFileList()
    {
      setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH - const_Manager.DGUI_APPS_WIDTH,
          const_Manager.DGUI_APPS_FILELIST_HEIGHT));
      setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      setPreferredSize(new Dimension(const_Manager.DGUI_APPS_FILELIST_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT));
      /*----------------------------------------------------------------- /
      / Dont add setMinimumSize(); it messes up app list resizing ability /
      /------------------------------------------------------------------*/

      pane = new JTabbedPane();
      pane.setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH - const_Manager.DGUI_APPS_WIDTH,
          const_Manager.DGUI_APPS_FILELIST_HEIGHT));
      pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      pane.setFocusable(false);
      pane.setOpaque(true);
      pane.setFont(use_Halcyon.regularFont().deriveFont(const_Manager.PROGRAM_DEFAULT_FONT_SIZE));

      if (const_MUTableKeys.use_filelist_titled_border)
      {
        border = BorderFactory.createTitledBorder(_lang(LANG_FILELIST_BORDER_TITLE));
        border.setBorder(BorderFactory.createEmptyBorder());
        border.setTitleFont(use_Halcyon.boldFont().deriveFont(15F));
        setBorder(border);
        pane.addChangeListener(x -> {
          JTabbedPane t = (JTabbedPane) x.getSource();
          border
              .setTitle(t.getSelectedIndex() >= 0 && t.getSelectedComponent() != null
                  ? (t.getSelectedIndex() + 1) + " | "
                      + (t.getSelectedComponent().getName() == null ? " "
                          : new File(t.getSelectedComponent().getName()).getName())
                      + " | " + (t.getSelectedComponent().getName() == null ? " "
                          : t.getSelectedComponent().getName())
                  : _lang(LANG_FILELIST_BORDER_TITLE));
          repaint(100L); // requires this repaint command in order for the title to actually display.
        });
      }
      else
        setBorder(BorderFactory.createEmptyBorder());

      getViewport().add(pane);

      guiTrees = new HashMap<>();

      /*-------------------------------------------------------------------------- /
      / this should be at bottom to avoid NULLABLE swing components being called!! /
      /---------------------------------------------------------------------------*/
      const_Global.PLAY_LIST_POOL.addRefreshable(this);

      const_Global.PLAY_LIST_POOL.setGuard(new impl_Guard< use_TailwindPlaylist >() {
        @Override
        public boolean check(use_TailwindPlaylist e)
        {
          if (const_Global.PLAY_LIST_POOL.hasObj(e))
          {
            pstream.log.warn("I HAVE THIS PLAYLIST ADDED!!!");
            for (int i = 0; i < pane.getTabCount(); i++)
            {
              if (pane.getTabComponentAt(i).getName().equals(e.id()))
              {
                pane.setSelectedIndex(i);
              }
            }
            return false;
          }
          pstream.log.warn("I DONT HAVE THIS PLAYLIST ADDED");
          return true;
        }
      });

      /*-----------------------------------
      ----------------------------------------------------------- /
      / THIS PART DESIGNATES THE DEFAULT CREATED POOL OF PLAYLISTS THAT ARE                            /
      / NOT TO BE MODIFIABLE BY THE END USER                                                           /
      /                                                                                                /
      / [!] REPLACE THE "NEW STRING[0]" PART OF THIS ADDITION WITH ACTUAL FETCHED LIKED LIST CHILDRENS /
      /-----------------------------------------------------------------------------------------------*/
      /*---------------------------------------------------------------------------------------------------------------- /
      / const_Pools.PLAY_LIST_POOL.addPoolObject(                                                                        /
      /     new use_StubPlaylist(new playlist_Traits(true, false, true, false), _lang(LANG_PLAYLIST_DEFAULT_LIKED_TITLE), new String[0], /
      /         use_HalcyonProperties.acceptedEndings()));                                                               /
      /-----------------------------------------------------------------------------------------------------------------*/
      const_Global.SELECTION_LISTENERS.add_listener(this);
    }

    /**
     * @param list
     */
    public void pokeFileList(use_TailwindPlaylist list)
    {

      DefaultMutableTreeNode root = new DefaultMutableTreeNode(list.getParent());

      java.util.List< DefaultMutableTreeNode > nodes = new ArrayList<>();

      list.forEach(x -> {
        if (x != null)
        {
          DefaultMutableTreeNode node = new DefaultMutableTreeNode(x);
          nodes.add(node);
          root.add(node);
          node.setParent(root);
        }
      });

      final JTree tree = new JTree(root);
      tree.setName(list.getParent());
      tree.setRootVisible(true);
      tree.setShowsRootHandles(true);
      tree.setExpandsSelectedPaths(true);
      tree.setScrollsOnExpand(true);
      tree.setEditable(false);
      tree.setRequestFocusEnabled(false);
      tree.setScrollsOnExpand(true);
      tree.setAutoscrolls(true);
      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
      renderer.setClosedIcon(use_Image.resize_fast_1(const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.DGUI_FILELIST_LEAF_CLOSED)));
      renderer.setOpenIcon(use_Image.resize_fast_1(const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.DGUI_FILELIST_LEAF_OPEN)));
      renderer.setLeafIcon(use_Image.resize_fast_1(const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          const_Manager.DGUI_APPS_FILELIST_LEAF_ICON_W_H,
          use_ResourceFetcher.fetcher.getFromAsImageIcon(const_ResourceManager.DGUI_FILELIST_LEAF)));

      tree.setCellRenderer(renderer);
      tree.addMouseListener(new MouseAdapter() {

        private void call(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
          if (SwingUtilities.isRightMouseButton(e))
            call(e);
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
          if (SwingUtilities.isRightMouseButton(e))
            call(e);
        }

        private File last = null;

        @Override
        public void mouseClicked(MouseEvent e)
        {

          if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
          {
            JTree tree1 = (JTree) e.getSource();
            TreePath path = tree1.getSelectionPath();
            if (path != null)
            {
              DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
              if (node.getParent() != null && !node.getParent().toString().equals(tree.getParent().toString()))
              {
                File f = new File(tree.getName() + use_Halcyon.getFileSeparator() + node);
                if (last == null)
                  last = f;

                use_TailwindTrack er = new use_TailwindTrack(f);
                use_Task.run_Snb_1(() -> const_Global.SELECTION_LISTENERS.forEach(x -> x.forYou(er)));
                last = f;
              }
            }
          }
        }
      });
      pstream.log.info("INSERTS > " + list.getCanonicalParent_1());

      JScrollPane jsp = new JScrollPane();
      jsp.setAutoscrolls(true);
      jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      jsp.setBorder(BorderFactory.createEmptyBorder());
      jsp.setName(list.getParent());
      jsp.getViewport().add(tree);

      pane.addTab(use_Commons.strong_delimiter(list.getCanonicalParent_1(), "...", 2), jsp);
      if (list.expose_traits().closeable)
      {
        filelist_TabButtons buttons = new filelist_TabButtons(list.getCanonicalParent_1(), () -> {
          guiTrees.remove(list.id());
          const_Global.PLAY_LIST_POOL.removePoolObject_ID(list.id());
        });
        pane.setTabComponentAt(pane.getTabCount() - 1, buttons);
      }
      pane.setToolTipTextAt(pane.getTabCount() - 1, list.getParent());
      pane.getTabComponentAt(pane.getTabCount() - 1).setName(list.getParent());
      guiTrees.put(list.id(),
          new struct_Trio<>(
              new struct_Pair<>(pane.getTabCount() - 1, tree), root, nodes));
    }

    /**
     * @param refreshed
     */
    @Override
    public void refresh(struct_Pair< Optional< String >, Optional< use_TailwindPlaylist > > refreshed)
    {
      refreshed.second.ifPresent(this::pokeFileList);
    }

    @Override
    public void dry_refresh()
    {
      const_Global.PLAY_LIST_POOL.objs().forEach(
          x -> pokeFileList(const_Global.PLAY_LIST_POOL.get(x)));
    }

    @Override
    public void forYou(use_TailwindTrack e)
    {
      pstream.log.info("USER SELECTED TRACK: " + e.getContentFile().getAbsolutePath());
    }
  }

  private final dgui_HalcyonApps apps;

  public dgui_HalcyonBottom()
  {
    apps = new dgui_HalcyonApps();
    dgui_HalcyonFileList filelistTabs = new dgui_HalcyonFileList();

    setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    setPreferredSize(new Dimension(const_Manager.FRAME_MIN_WIDTH, const_Manager.DGUI_APPS_FILELIST_HEIGHT));
    setLeftComponent(apps);
    setRightComponent(filelistTabs);
  }
}

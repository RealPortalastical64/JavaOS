package p1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;

import javax.imageio.ImageIO;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.nio.channels.FileChannel;

import java.net.URL;

/**
A basic File Browser.  Requires 1.6+ for the Desktop & SwingWorker
classes, amongst other minor things.

Includes support classes FileTableModel & FileTreeCellRenderer.

@TODO Bugs
<li>Fix keyboard focus issues - especially when functions like
rename/delete etc. are called that update nodes & file lists.
<li>Needs more testing in general.

@TODO Functionality
<li>Double clicking a directory in the table, should update the tree
<li>Move progress bar?
<li>Add other file display modes (besides table) in CardLayout?
<li>Menus + other cruft?
<li>Implement history/back
<li>Allow multiple selection
<li>Add file search

@author Andrew Thompson
@version 2011-06-08
@see http://codereview.stackexchange.com/q/4446/7784
@license LGPL
 */
class FileBrowser {

    /** Title of the application */
    public static final String APP_TITLE = "File Explorer";
    /** Used to open/edit/print files. */
    private Desktop desktop;
    /** Provides nice icons and names for files. */
    private FileSystemView fileSystemView;

    /** currently selected File. */
    private File currentFile;

    /** Main GUI container */
    private JPanel gui;

    /** File-system tree. Built Lazily */
    private JTree tree;
    private DefaultTreeModel treeModel;

    /** Directory listing */
    private JTable table;
    private JProgressBar progressBar;
    /** Table model for File[]. */
    private FileTableModel fileTableModel;
    private ListSelectionListener listSelectionListener;
    private boolean cellSizesSet = false;
    private int rowIconPadding = 6;

    /* File controls. */
    private JButton openFile;
    private JButton printFile;
    private JButton editFile;
    private PatchedButton setBg;

    /* File details. */
    private JLabel fileName;
    private JTextField path;
    private JLabel date;
    private JLabel size;
    private JCheckBox readable;
    private JCheckBox writable;
    private JCheckBox executable;
    private JRadioButton isDirectory;
    private JRadioButton isFile;

    /* GUI options/containers for new File/Directory creation.  Created lazily. */
    private JPanel newFilePanel;
    private JRadioButton newTypeFile;
    private JTextField name;

    public Container getGui() {
        if (gui==null) {
            gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));

            fileSystemView = FileSystemView.getFileSystemView();
            desktop = Desktop.getDesktop();

            JPanel detailView = new JPanel(new BorderLayout(3,3));

            table = new JTable();
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setAutoCreateRowSorter(true);
            table.setShowVerticalLines(false);

            listSelectionListener = new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {
                    int row = table.getSelectionModel().getLeadSelectionIndex();
                    setFileDetails( ((FileTableModel)table.getModel()).getFile(row) );
                }
            };
            table.getSelectionModel().addListSelectionListener(listSelectionListener);
            JScrollPane tableScroll = new JScrollPane(table);
            Dimension d = tableScroll.getPreferredSize();
            tableScroll.setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
            detailView.add(tableScroll, BorderLayout.CENTER);

            // the File tree
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            treeModel = new DefaultTreeModel(root);

            TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
                    public void valueChanged(TreeSelectionEvent tse){
                        DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                        showChildren(node);
                        setFileDetails((File)node.getUserObject());
                    }
                };

            // show the file system roots.
            File[] roots = fileSystemView.getRoots();
            for (File fileSystemRoot : roots) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add( node );
                File[] files = fileSystemView.getFiles(fileSystemRoot, true);
                for (File file : files) {
                    if (file.isDirectory()) {
                        node.add(new DefaultMutableTreeNode(file));
                    }
                }
                //
            }

            tree = new JTree(treeModel);
            tree.setRootVisible(false);
            tree.addTreeSelectionListener(treeSelectionListener);
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.expandRow(0);
            JScrollPane treeScroll = new JScrollPane(tree);

            // as per trashgod tip
            tree.setVisibleRowCount(15);

            Dimension preferredSize = treeScroll.getPreferredSize();
            Dimension widePreferred = new Dimension(
                    200,
                    (int)preferredSize.getHeight());
            treeScroll.setPreferredSize( widePreferred );

            // details for a File
            JPanel fileMainDetails = new JPanel(new BorderLayout(4,2));
            fileMainDetails.setBorder(new EmptyBorder(0,6,0,6));

            JPanel fileDetailsLabels = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);

            JPanel fileDetailsValues = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);

            fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));
            fileName = new JLabel();
            fileDetailsValues.add(fileName);
            fileDetailsLabels.add(new JLabel("Path", JLabel.TRAILING));
            path = new JTextField(5);
            path.setEditable(false);
            fileDetailsValues.add(path);
            fileDetailsLabels.add(new JLabel("Last modified", JLabel.TRAILING));
            date = new JLabel();
            fileDetailsValues.add(date);
            fileDetailsLabels.add(new JLabel("File size", JLabel.TRAILING));
            size = new JLabel();
            fileDetailsValues.add(size);
            fileDetailsLabels.add(new JLabel("Type", JLabel.TRAILING));

            JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING,4,0));

            isDirectory = new JRadioButton("Directory");
            flags.add(isDirectory);

            isFile = new JRadioButton("File");
            flags.add(isFile);
            fileDetailsValues.add(flags);

            JToolBar toolBar = new JToolBar();
            // mnemonics stop working in a floated toolbar
            toolBar.setFloatable(false);

            JButton locateFile = new JButton("Locate");
            locateFile.setMnemonic('l');

            locateFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            System.out.println("Locate: " + currentFile.getParentFile());
                            desktop.open(currentFile.getParentFile());
                        } catch(Throwable t) {
                            showThrowable(t);
                        }
                        gui.repaint();
                    }
                });
            toolBar.add(locateFile);

            openFile = new JButton("Open");
            openFile.setMnemonic('o');

            openFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            System.out.println("Open: " + currentFile);
                            if(currentFile.toString().endsWith(".txt"))
                                Notepad.main(currentFile.toString());
                            else if(currentFile.toString().endsWith(".png") || currentFile.toString().endsWith(".jpg") || currentFile.toString().endsWith(".gif") || currentFile.toString().endsWith(".jpeg"))
                                new ImageViewer(currentFile);
                            else
                                desktop.open(currentFile);
                        } catch(Throwable t) {
                            showThrowable(t);
                        }
                        gui.repaint();
                    }
                });
            toolBar.add(openFile);

            editFile = new JButton("Edit");
            editFile.setMnemonic('e');
            editFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            desktop.edit(currentFile);
                        } catch(Throwable t) {
                            showThrowable(t);
                        }
                    }
                });
            toolBar.add(editFile);

            printFile = new JButton("Print");
            printFile.setMnemonic('p');
            printFile.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            desktop.print(currentFile);
                        } catch(Throwable t) {
                            showThrowable(t);
                        }
                    }
                });
            toolBar.add(printFile);

            setBg = new PatchedButton("Set Background");
            setBg.setMnemonic('b');

            setBg.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        changeBackground(currentFile);
                    }
                });
            setBg.setEnabled(false);setBg.setForeground(setBg.getForeground());
            toolBar.add(setBg);

            // Check the actions are supported on this platform!
            openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));
            editFile.setEnabled(desktop.isSupported(Desktop.Action.EDIT));
            printFile.setEnabled(desktop.isSupported(Desktop.Action.PRINT));

            flags.add(new JLabel("::  Flags"));
            readable = new JCheckBox("Read  ");
            readable.setMnemonic('a');
            flags.add(readable);

            writable = new JCheckBox("Write  ");
            writable.setMnemonic('w');
            flags.add(writable);

            executable = new JCheckBox("Execute");
            executable.setMnemonic('x');
            flags.add(executable);

            int count = fileDetailsLabels.getComponentCount();
            for (int ii=0; ii<count; ii++) {
                fileDetailsLabels.getComponent(ii).setEnabled(false);
            }

            count = flags.getComponentCount();
            for (int ii=0; ii<count; ii++) {
                flags.getComponent(ii).setEnabled(false);
            }

            JPanel fileView = new JPanel(new BorderLayout(3,3));

            fileView.add(toolBar,BorderLayout.NORTH);
            fileView.add(fileMainDetails,BorderLayout.CENTER);

            detailView.add(fileView, BorderLayout.SOUTH);

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    treeScroll,
                    detailView);
            gui.add(splitPane, BorderLayout.CENTER);

            JPanel simpleOutput = new JPanel(new BorderLayout(3,3));
            progressBar = new JProgressBar();
            simpleOutput.add(progressBar, BorderLayout.EAST);
            progressBar.setVisible(false);

            gui.add(simpleOutput, BorderLayout.SOUTH);

        }
        return gui;
    }

    public void showRootFile() {
        // ensure the main files are displayed
        tree.setSelectionInterval(0,0);
    }

    private TreePath findTreePath(File find) {
        for (int ii=0; ii<tree.getRowCount(); ii++) {
            TreePath treePath = tree.getPathForRow(ii);
            Object object = treePath.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
            File nodeFile = (File)node.getUserObject();

            if (nodeFile==find) {
                return treePath;
            }
        }
        // not found!
        return null;
    }

    public void changeBackground(File bg){
        try 
        {
            File file = new File("bgpath.txt");
            FileWriter fw = new FileWriter(file, false);
            fw.write((bg.getAbsolutePath()).toString());
            fw.close();
            //setBackground();
            int x=JOptionPane.showConfirmDialog(MainMenu.f,"You must log off for the changes to take effect. Log off now?","Log off required",JOptionPane.YES_NO_OPTION);
            if(x==JOptionPane.YES_OPTION) {MainMenu.f.dispose();
                login.main(null);}

        }
        catch(IOException d) 
        {
            d.printStackTrace();
        }
    }

    private void showErrorMessage(String errorMessage, String errorTitle) {
        JOptionPane.showMessageDialog(
            gui,
            errorMessage,
            errorTitle,
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showThrowable(Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(
            gui,
            t.toString(),
            t.getMessage(),
            JOptionPane.ERROR_MESSAGE
        );
        gui.repaint();
    }

    /** Update the table on the EDT */
    private void setTableData(final File[] files) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (fileTableModel==null) {
                        fileTableModel = new FileTableModel();
                        table.setModel(fileTableModel);
                    }
                    table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                    fileTableModel.setFiles(files);
                    table.getSelectionModel().addListSelectionListener(listSelectionListener);
                    if (!cellSizesSet) {
                        Icon icon = fileSystemView.getSystemIcon(files[0]);

                        // size adjustment to better account for icons
                        table.setRowHeight( icon.getIconHeight()+rowIconPadding );

                        setColumnWidth(0,-1);
                        setColumnWidth(3,60);
                        table.getColumnModel().getColumn(3).setMaxWidth(120);
                        setColumnWidth(4,-1);
                        setColumnWidth(5,-1);
                        setColumnWidth(6,-1);
                        setColumnWidth(7,-1);
                        setColumnWidth(8,-1);
                        setColumnWidth(9,-1);

                        cellSizesSet = true;
                    }
                }
            });
    }

    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width<0) {
            // use the preferred width of the header..
            JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
            Dimension preferred = label.getPreferredSize();
            // altered 10->14 as per camickr comment.
            width = (int)preferred.getWidth()+14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    /** Add the files that are contained within the directory of this node.
    Thanks to Hovercraft Full Of Eels for the SwingWorker fix. */
    private void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true); //!!
                        if (node.isLeaf()) {
                            for (File child : files) {
                                if (child.isDirectory()) {
                                    publish(child);
                                }
                            }
                        }
                        setTableData(files);
                    }
                    return null;
                }

                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }

                @Override
                protected void done() {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                    tree.setEnabled(true);
                }
            };
        worker.execute();
    }

    /** Update the File details view with the details of this File. */
    private void setFileDetails(File file) {
        currentFile = file;
        if(currentFile.toString().endsWith(".png") || currentFile.toString().endsWith(".jpg") || currentFile.toString().endsWith(".gif") || currentFile.toString().endsWith(".jpeg")){
            setBg.setEnabled(true);setBg.setForeground(Color.WHITE);}
        else{
            setBg.setEnabled(false);setBg.setForeground(setBg.getForeground());}
        Icon icon = fileSystemView.getSystemIcon(file);
        fileName.setIcon(icon);
        fileName.setText(fileSystemView.getSystemDisplayName(file));
        path.setText(file.getPath());
        date.setText(new Date(file.lastModified()).toString());
        size.setText(file.length() + " bytes");
        readable.setSelected(file.canRead());
        writable.setSelected(file.canWrite());
        executable.setSelected(file.canExecute());
        isDirectory.setSelected(file.isDirectory());

        isFile.setSelected(file.isFile());

        JFrame f = (JFrame)gui.getTopLevelAncestor();
        if (f!=null) {
            f.setTitle(
                APP_TITLE +
                " :: " +
                fileSystemView.getSystemDisplayName(file) );
        }

        gui.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    JInternalFrame f = new JInternalFrame(APP_TITLE,true,true,true,true);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setLocation(150,50);  
                    FileBrowser FileBrowser = new FileBrowser();
                    f.setContentPane(FileBrowser.getGui());

                    try {
                        URL urlBig = FileBrowser.getClass().getResource("fb-icon-32x32.png");
                        URL urlSmall = FileBrowser.getClass().getResource("fb-icon-16x16.png");
                        ArrayList<Image> images = new ArrayList<Image>();
                        images.add( ImageIO.read(urlBig) );
                        images.add( ImageIO.read(urlSmall) );
                        //f.setIconImages(images);
                    } catch(Exception weTried) {}

                    f.pack();
                    //f.setLocationByPlatform(true);
                    f.setMinimumSize(f.getSize());
                    f.setVisible(true);
                    MainMenu.f.add(f);

                    FileBrowser.showRootFile();
                }
            });
    }
}


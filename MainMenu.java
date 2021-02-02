package p1;

import javax.swing.*;
import javax.imageio.*;
import javax.swing.UIManager.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;   
import java.awt.image.*;
import java.io.*;  
import java.nio.file.*;
import java.util.*; 
import java.text.*;

class MainMenu implements ActionListener 
{  

    JMenu menu1, menu2, menu3, submenu;
    JMenuItem i1, i2, i3, i4, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, j1, j2, j3, j4;
    
    JLabel statusBar; private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    int x;
    public static JFrame f;
    JPanel contentPane;
    JFileChooser chooser;
    File bg;
    Path path;
    File file;
    Scanner in;
    FileWriter fw;
    ButtonGroup group;
    LookAndFeel l;
    MainMenu(){  
        f= new JFrame("JavaOS");
        f.setExtendedState(f.MAXIMIZED_BOTH);
        file = new File("bgpath.txt");
        try
        {
            if(!file.exists())
                file.createNewFile();
        }
        catch(Exception e){}
        setBackground();
        
        l = UIManager.getLookAndFeel();
        group = new ButtonGroup();
        JMenuBar mb=new JMenuBar();  
        menu1=new JMenu("Programs");
        menu3=new JMenu("Home");
        
        menu2=new JMenu("\u06de"); menu2.setFont(new Font("Logo", Font.BOLD, 12));
        
        menu3.setFont(new Font("Home", Font.BOLD, 12));
        UIManager.put("Logo", f);
        UIManager.put("Home", f);
        submenu = new JMenu("Games");
        
        i9=new JMenuItem("Clock");  
        i1=new JMenuItem("File Explorer"); 
        i14=new JMenuItem("Web Browser");
        i2=new JMenuItem("Notepad"); 
        i3=new JMenuItem("Calculator");
        i4=new JMenuItem("Calendar");
        i11=new JMenuItem("Image Viewer");
        i15=new JMenuItem("Audio Player");
        i12=new JMenuItem("Paint");
        //i13=new JMenuItem("Sticky Notes");
        i6=new JMenuItem("Minesweeper");
        i7=new JMenuItem("Solitaire");
        i8=new JMenuItem("Picture Puzzle");
        i10=new JMenuItem("Chess");
        j1=new JMenuItem("Exit");
        j2=new JMenuItem("Logoff");
        j3=new JMenuItem("Change Background...");
        j4=new JMenuItem("About JavaOS...");
        i2.addActionListener(this);
        i1.addActionListener(this);
        i3.addActionListener(this);
        i4.addActionListener(this);
        //i13.addActionListener(this);
        i14.addActionListener(this);
        i6.addActionListener(this);
        i7.addActionListener(this);
        i10.addActionListener(this);
        i8.addActionListener(this);
        j1.addActionListener(this);
        j2.addActionListener(this);
        j3.addActionListener(this);
        i11.addActionListener(this);
        i15.addActionListener(this);
        i9.addActionListener(this);
        i12.addActionListener(this);
        j4.addActionListener(this);
        submenu.add(i6); submenu.add(i7); submenu.add(i8); submenu.add(i10);
        menu1.add(i9); menu1.add(i1); menu1.add(i14); menu1.add(i2); menu1.add(i3); menu1.add(i4);
        menu1.add(i11); menu1.add(i15); menu1.add(i12); //menu1.add(i13);
        menu1.add(submenu);
        menu2.add(j3); menu2.addSeparator(); menu2.add(j2); menu2.add(j1); menu2.addSeparator(); menu2.add(j4);
        mb.add(menu2);
        mb.add(menu3);
        mb.add(menu1);  
        f.setJMenuBar(mb);
        f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    java.awt.Window[] windows = Window.getWindows();
                    for (int i = 0; i < windows.length; i++)
                    {
                        windows[i].toFront();
                    }
                }
            });
        chooser=new JFileChooser();

        statusBar=new JLabel("",JLabel.RIGHT);
        statusBar.setPreferredSize(new Dimension(100, 20));
        f.add(statusBar,BorderLayout.SOUTH); 
        f.setSize(400,400);
        f.setLayout(null);
        

        f.setUndecorated(true);
        Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        Date date = getDate();
                        String dateString = simpleDateFormat.format(date);
                        statusBar.setText(dateString);
                        f.repaint();
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        Thread t = new Thread(runnable);
        t.start();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    } 

    public static java.util.Date getDate() {
        java.util.Date date = new java.util.Date();
        return date;
    }

    public void actionPerformed(ActionEvent e) {    
        if(e.getSource()==i2)
            new Notepad();
        else if(e.getSource()==i1)
            FileBrowser.main(null);
        else if(e.getSource()==i9)
            DigitalClock.main(null);
        else if(e.getSource()==i15)
            SwingAudioPlayer.main(null);
        else if(e.getSource()==i3)
            Program.main(null);
        else if(e.getSource()==i4)
            CalendarProgram.main(null);
        else if(e.getSource()==i11)
            new ImageViewer();
        else if(e.getSource()==i6)
            Minesweeper.main(null);
        else if(e.getSource()==i12)
            InputWH.main(null);
        //else if(e.getSource()==i13)
        //    Main.main(null);
        else if(e.getSource()==i7)
            GameS.main(null);
        else if(e.getSource()==i14)
            SkeletonBrowserTest.main(null);
        else if(e.getSource()==i10)
            Game.main(null);
        else if(e.getSource()==i8)
            PuzzleEx.main(null);
        else if(e.getSource()==j1){
            x=JOptionPane.showConfirmDialog(f,"Exit JavaOS?","Exit?",JOptionPane.YES_NO_OPTION);
            if(x==JOptionPane.YES_OPTION) {f.dispose(); Shutdown.main(null);}}
        else if(e.getSource()==j2) {
            f.dispose();
            login.main(null);
        }
        else if(e.getSource()==j3)
            openFile();
        else if(e.getSource()==j4)
            new About();
        
    } 

    void openFile()
    {

        chooser.setDialogTitle("Change Background");
        chooser.setApproveButtonText("Open"); 
        chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        chooser.setApproveButtonToolTipText("Open");

        bg=null;
        do
        {
            if(chooser.showOpenDialog(f)!=JFileChooser.APPROVE_OPTION)
                return;
            bg=chooser.getSelectedFile();

            if(bg.exists())	break;

            JOptionPane.showMessageDialog(f,
                "<html>"+bg.getName()+"<br>file not found.<br>"+
                "Please verify the correct file name was given.<html>",
                "Open",	JOptionPane.INFORMATION_MESSAGE);

        } while(true);

        changeBackground(bg);
        
    }

    public void changeBackground(File bg){
        try 
        {

            fw = new FileWriter(file, false);
            fw.write((bg.getAbsolutePath()).toString());
            fw.close();
            //setBackground();
            x=JOptionPane.showConfirmDialog(f,"You must log off for the changes to take effect. Log off now?","Log off required",JOptionPane.YES_NO_OPTION);
        if(x==JOptionPane.YES_OPTION) {f.dispose();
            login.main(null);}

        }
        catch(IOException d) 
        {
            d.printStackTrace();
        }
    }
    

    void setBackground(){
        try 
        {
            //f.setVisible(false);
            //f.setUndecorated(true);
            if(file.length() == 0)
            return;
            String path = (new Scanner(file).useDelimiter("\\Z").next()).replace("\\","\\\\");
            f.setContentPane(new JLabel(new ImageIcon(path)));
            //f.setExtendedState(f.MAXIMIZED_BOTH);
            //f.setVisible(true);
            f.repaint();
        }
        catch(IOException d) 
        {
            d.printStackTrace();
        }
    }

    public static void main(String [] args)  
    {  
        new MainMenu();  
    }}  

package p1;

// Use a JEditorPane to display the contents of a file on a Web server.
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;

/*
 * Author: Richard Cai
 * Date: 05/31/2016
 */

public class SkeletonBrowser extends JInternalFrame 
{
    private JTextField enterField; // JTextField to enter site name
    private JEditorPane contentsArea; // to display Web site
    private JEditorPane historyArea; // to display history in hyperlinks

    private JButton home; //Goes to default URL
    private JButton forward; //revisits forward site
    private JButton back; //revisits previous site
    private JButton go; //goes to website in textfield entry
    private JButton selectHome;
    private JButton selectHistory;

    private JPanel prefsPanel; //Panel for holding buttons

    private JTextField setHome;
    private JTextField numHistory;

    private JMenuItem about;
    private JMenuItem prefs;
    private JMenuItem exit;
    private JMenuItem displayHistory;
    private JMenuItem clearHistory;

    //ArrayList to hold history
    private LinkedList <String> history = new LinkedList<String>();
    private LinkedList <String> absoluteHistoryCopy = new LinkedList<String>();
    private LinkedList <String> forwardHistory = new LinkedList<String>();
    private LinkedList <String> absoluteHistory = new LinkedList<String>();
    //private ArrayList<String> history = new ArrayList<String>();

    private int historyIndex = -1;
    private String temp;
    private String trueHistory;
    private String homeFile = "home.txt";
    private String historyFile = "history.txt";
    private String line = null;
    private int countHistory;

    
    // set up GUI
    public SkeletonBrowser()
    {
        super( "Web Browser" );

        // create enterField and register its listener
        enterField = new JTextField( "",70 );
        enterField.setToolTipText("Enter file URL here");
        enterField.addActionListener(new ActionListener() 
            {
                // get document specified by user
                public void actionPerformed( ActionEvent event )
                {
                    getThePage( event.getActionCommand(),true );
                } // end method actionPerformed
            } // end inner class
        ); // end call to addActionListener

        //creating ItemHandler
        ItemHandler handler = new ItemHandler();

        //creating menuHandler
        MenuHandler menuHandler = new MenuHandler();

        //Creating Buttons and Button Panel

        back = new JButton("<");
        back.addActionListener(handler);
        forward = new JButton(">");
        forward.addActionListener(handler);
        home = new JButton("\u2302");
        home.addActionListener(handler);
        go = new JButton ("Go");
        go.addActionListener(handler);

        selectHome = new JButton("Set Home");
        selectHome.addActionListener(menuHandler);
        selectHistory = new JButton("Set Items");
        selectHistory.addActionListener(menuHandler);
BufferedReader bufferedReader;
String text = "";
        try {
            FileReader fileReader = new FileReader("home.txt");

            // Always wrap FileReader in BufferedReader.
            bufferedReader = new BufferedReader(fileReader);

            text = bufferedReader.readLine().toString();
            bufferedReader.close();     
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this,"Error - File Not Found!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(this,"Could not load home page.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        
        setHome = new JTextField(text, 15);
        setHome.setToolTipText("Set home");
        try {
                    FileReader fileReader = new FileReader("history.txt");

                    // Always wrap FileReader in BufferedReader.
                    bufferedReader = new BufferedReader(fileReader);

                    text = bufferedReader.readLine().toString();

                    bufferedReader.close();     
                }
                catch(FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"File Not Found!","ERROR",JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Inane Error","ERROR",JOptionPane.ERROR_MESSAGE);
                }
        numHistory = new JTextField(text, 15);
        numHistory.setToolTipText("Number of items in history");

        prefsPanel = new JPanel();

        JPanel buttonPanel = new JPanel();
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); //Set toolbar to not draggable

        toolBar.add(back);
        toolBar.add(forward);
        toolBar.add(home);
        toolBar.add(enterField);
        toolBar.add(go);

        add(toolBar,BorderLayout.NORTH);

        //Creating menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenu historyMenu = new JMenu("History"); //historyMenu, has items displayHistory and clearHistory

        displayHistory = new JMenuItem("Show History");
        clearHistory = new JMenuItem("Clear History");

        historyMenu.add(displayHistory);
        historyMenu.add(clearHistory);

        displayHistory.addActionListener(menuHandler);
        clearHistory.addActionListener(menuHandler);

        
        about = new JMenuItem("About");
        prefs = new JMenuItem("Preferences");
        exit = new JMenuItem("Exit");

        //Adding Menu Items to FileMenu
        fileMenu.add(about);
        fileMenu.add(prefs);
        fileMenu.add(exit);

        //Adding itemHandler to menuItems
        about.addActionListener(menuHandler);
        prefs.addActionListener(menuHandler);
        exit.addActionListener(menuHandler);

        menuBar.add(fileMenu);
        menuBar.add(historyMenu);
        setJMenuBar(menuBar);

        prefsPanel.add(setHome);
        prefsPanel.add(numHistory);
        prefsPanel.add(selectHome);
        prefsPanel.add(selectHistory);

        
        historyArea = new JEditorPane("text/html",trueHistory); // Creating clickable hyperlinks for history
        historyArea.setEditable( false );
        historyArea.addHyperlinkListener(
            new HyperlinkListener() 
            {
                // if user clicked hyperlink, go to specified page
                public void hyperlinkUpdate( HyperlinkEvent event )
                {
                    if ( event.getEventType() == 
                    HyperlinkEvent.EventType.ACTIVATED )
                        getThePage( event.getURL().toString(),true );
                } // end method hyperlinkUpdatehyperlinkUpdate
            } // end inner class
        ); // end call to addHyperlinkListener

        contentsArea = new JEditorPane(); // create contentsArea
        contentsArea.setEditable( false );

        contentsArea.addHyperlinkListener(
            new HyperlinkListener() 
            {
                // if user clicked hyperlink, go to specified page
                public void hyperlinkUpdate( HyperlinkEvent event )
                {
                    if ( event.getEventType() == 
                    HyperlinkEvent.EventType.ACTIVATED )
                        getThePage( event.getURL().toString(),true);
                } // end method hyperlinkUpdate
            } // end inner class
        ); // end call to addHyperlinkListener

        add( new JScrollPane( contentsArea ), BorderLayout.CENTER );
        setSize( 800, 800 ); // set size of window
        //setResizable(true);
        //setMaximizable(true);
        setClosable(true);

        setIconifiable(true);
        MainMenu.f.add(this);
        setLocation(150,50);
        setVisible( true ); // show window
        try {
            FileReader fileReader = new FileReader("home.txt");

            // Always wrap FileReader in BufferedReader.
            bufferedReader = new BufferedReader(fileReader);

            getThePage(bufferedReader.readLine().toString(),true);
            bufferedReader.close();     
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this,"Error - File Not Found!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(this,"Could not load home page.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    } // end SkeletonBrowser constructor

    //ItemHandler class
    private class ItemHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (event.getActionCommand() == "<"){
                if (!(history.first() == null) && (history.size() != 1)) {
                    temp = history.removeFront();
                    forwardHistory.addFront(temp);
                    getThePage(history.first(),false);
                }
            }

            if (event.getActionCommand() == ">"){
                if (forwardHistory.size() != 0){
                    history.addFront(forwardHistory.first());
                    getThePage(forwardHistory.removeFront(),false);
                }
            }

            if (event.getActionCommand() == "Go"){
                getThePage(enterField.getText().toString(),true);
            }
            if (event.getActionCommand() == "\u2302"){
                try {
                    FileReader fileReader = new FileReader("home.txt");

                    // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    getThePage(bufferedReader.readLine().toString(),true);
                    bufferedReader.close();     
                }
                catch(FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Error - File Not Found!","ERROR",JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Could not load home page.","ERROR",JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

    private class MenuHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == about){
                JOptionPane.showMessageDialog(SkeletonBrowser.this," Web Browser v1.0 Author: Richard Cai.","About Info",JOptionPane.PLAIN_MESSAGE);
            }

            //Test pref code
            else if (event.getSource() == prefs ){
                int option = JOptionPane.showConfirmDialog(SkeletonBrowser.this, prefsPanel, "Preferences", JOptionPane.PLAIN_MESSAGE);
            }

            else if (event.getSource() == exit){
                dispose();
            }
            else if (event.getSource() == displayHistory){
                trueHistory = "";

                try {
                    FileReader fileReader = new FileReader("history.txt");

                    // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    countHistory = Integer.parseInt(bufferedReader.readLine().toString());

                    bufferedReader.close();     
                }
                catch(FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"File Not Found!","ERROR",JOptionPane.ERROR_MESSAGE);
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Inane Error","ERROR",JOptionPane.ERROR_MESSAGE);
                }

                //System.out.print(countHistory);
                int i = 0;
                while ((absoluteHistory.size() > 0) && (i < countHistory)){
                    String url = absoluteHistory.removeFront();
                    absoluteHistoryCopy.addFront(url);

                    trueHistory+=("<a href="+url+">"+url+"</a>"+ "<br>");   
                    i++;
                }

                
                historyArea.setText(trueHistory);
                JOptionPane.showMessageDialog(SkeletonBrowser.this,historyArea,"Browser History",JOptionPane.PLAIN_MESSAGE);
                trueHistory = "";

                while (absoluteHistoryCopy.size() != 0){
                    absoluteHistory.addFront(absoluteHistoryCopy.removeFront());
                }

            }
            else if (event.getSource() == clearHistory){
                absoluteHistory.makeEmpty();
                JOptionPane.showMessageDialog(SkeletonBrowser.this,"History cleared!");
            }

            if (event.getActionCommand() == "Set Home"){
                if (setHome.getText().startsWith("http")){
                    try{
                        FileWriter fileWriter = new FileWriter(homeFile);
                        BufferedWriter buffereredWriter = new BufferedWriter(fileWriter);

                        buffereredWriter.write(setHome.getText().toString());

                        buffereredWriter.close();

                    }
                    catch(IOException ex){
                        JOptionPane.showMessageDialog(SkeletonBrowser.this,"Inane Error","ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }

                else{
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Error - make sure URL begins with https:// and is valid","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }

            if (event.getActionCommand() == "Set Items"){
                try {
                    if (Integer.parseInt(numHistory.getText()) > 0){
                        FileWriter fileWriter = new FileWriter(historyFile);
                        BufferedWriter buffereredWriter = new BufferedWriter(fileWriter);

                        buffereredWriter.write(numHistory.getText().toString());

                        buffereredWriter.close();
                    }
                    else{
                        JOptionPane.showMessageDialog(SkeletonBrowser.this,"Error make sure number is positive","ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(SkeletonBrowser.this,"Inane Error","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }       
        }
    }

    // load document
    private void getThePage( String location,Boolean save)
    {
        if (!(location.startsWith("https"))){
            location = "https://" + location;
        }

        try // load document and display location 
        {    
            contentsArea.setPage( location ); // set the page
            enterField.setText( location ); // set the text

            if (save){
                history.addFront(location);
                absoluteHistory.addFront(location);
            }

        } // end try
        catch ( IOException ioException ) 
        {
            JOptionPane.showMessageDialog( this,
                "Error retrieving specified URL", "Bad URL", 
                JOptionPane.ERROR_MESSAGE );
        } // end catch
    } // end method getThePage
} // end class SkeletonBrowser

package p1;



import javax.swing.*;
import java.awt.*;
import javax.swing.UIManager.*;
import java.awt.image.*;

class run
{  
    
    static JFrame f;
    LookAndFeel l;
    static int t = 0;

    
    run(){  
        
       
       

        UIManager.put( "control", new Color( 128, 128, 128) );
  UIManager.put( "info", new Color(180,180,180) );
  UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
  UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
  UIManager.put( "nimbusDisabledText", new Color( 180, 180, 180) );
  UIManager.put( "nimbusFocus", new Color(115,164,209) );
  UIManager.put( "nimbusGreen", new Color(176,179,50) );
  UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
  UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
  UIManager.put( "nimbusOrange", new Color(191,98,4) );
  UIManager.put( "nimbusRed", new Color(169,46,34) );
  UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
  UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
  UIManager.put( "text", new Color( 230, 230, 230) );

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            } 
        }catch(Exception weTried) {
        }
        try{Runtime.getRuntime().exec
        (new String[] {
          "cmd.exe", 
          "/c",
          "\"Shows Desktop.lnk\""});}catch(Exception e){}
        f= new JFrame("");
        
        f.setExtendedState(f.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.requestFocus();f.toFront();
        
        f.toBack();
        
         f.setVisible(true);
         BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

// Create a new blank cursor.
Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
    cursorImg, new Point(0, 0), "blank cursor");

// Set the blank cursor to the JFrame.
f.getContentPane().setCursor(blankCursor);
        Boot.main(null);
        //f.dispose();
    }
    public static void main()  
    {  
        new run();  
    }
    public static void main(String [] args)
    {
        new run();
    }
}  

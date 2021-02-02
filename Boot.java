
package p1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.*;
import java.awt.event.ActionListener;
//w  ww .  ja v  a  2  s . c o m
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager.*;
import java.awt.image.*;

public class Boot {
  public static void main(String[] args) {
       run.f.setExtendedState(run.f.ICONIFIED);
       run.f.setExtendedState(run.f.MAXIMIZED_BOTH);
       run.f.toFront();
       run.f.requestFocus();
       run.f.setFocusableWindowState(false); 
    SplashScreen splashScreen = new SplashScreen();
  }
}
class SplashScreen extends JWindow {
  static JProgressBar progressBar = new JProgressBar();
  
  static int count = 1, TIMER_PAUSE = 25, PROGBAR_MAX = 500;
  static Timer progressBarTimer;
  ActionListener al = new ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      //progressBar.setValue(count);
      if (PROGBAR_MAX == count) {
        progressBarTimer.stop();
        SplashScreen.this.setVisible(false);
        login.main(null);
      }
      count++;
    }
  };

  public SplashScreen() {
    Container container = getContentPane();
    progressBar.setIndeterminate(true);
    JPanel panel = new JPanel();
    panel.setBorder(new EtchedBorder());
    container.add(panel, BorderLayout.CENTER);
    Icon logo = new ImageIcon(getClass().getResource("JavaOS logo boot.png"));
    JLabel label = new JLabel("");
    label.setIcon(logo);
    label.setFont(new Font("Verdana", Font.BOLD, 14));
    panel.add(label);
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

// Create a new blank cursor.
Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
    cursorImg, new Point(0, 0), "blank cursor");

// Set the blank cursor to the JFrame.
getContentPane().setCursor(blankCursor);
    progressBar.setMaximum(PROGBAR_MAX);
    container.add(progressBar, BorderLayout.SOUTH);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    UIManager.put( "nimbusOrange", new Color( 66, 139, 221) );
    try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            } 
        }catch(Exception weTried) {
        }
    startProgressBar();
    UIManager.put( "nimbusOrange", new Color(191,98,4) );
    try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            } 
        }catch(Exception weTried) {
        }
  }
  private void startProgressBar() {
    progressBarTimer = new Timer(TIMER_PAUSE, al);
    progressBarTimer.start();
  }
  
}

package p1;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import java.util.*;
import java.text.*;
 
public class DigitalClock {
 
  public static void main(String[] arguments) {
 
    ClockLabel dateLable = new ClockLabel("date");
    ClockLabel timeLable = new ClockLabel("time");
    ClockLabel dayLable = new ClockLabel("day");
 
    JInternalFrame f = new JInternalFrame("Clock",false,true,false,true);
    MainMenu.f.add(f);
    f.setSize(300,150);
    f.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    f.setLayout(new GridLayout(3, 1));
 f.setLocation(150,50);  
    f.add(dateLable);
    f.add(timeLable);
    f.add(dayLable);
 
    f.getContentPane().setBackground(Color.black);
 
    f.setVisible(true);
  }
}
 
class ClockLabel extends JLabel implements ActionListener {
 
  String type;
  SimpleDateFormat sdf;
 
  public ClockLabel(String type) {
    this.type = type;
    setForeground(Color.green);
 
    switch (type) {
      case "date" : sdf = new SimpleDateFormat("  MMMM dd yyyy");
                    setFont(new Font("sans-serif", Font.PLAIN, 12));
                    setHorizontalAlignment(SwingConstants.LEFT);
                    break;
      case "time" : sdf = new SimpleDateFormat("hh:mm:ss a");
                    setFont(new Font("sans-serif", Font.PLAIN, 40));
                    setHorizontalAlignment(SwingConstants.CENTER);
                    break;
      case "day"  : sdf = new SimpleDateFormat("EEEE  ");
                    setFont(new Font("sans-serif", Font.PLAIN, 16));
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    break;
      default     : sdf = new SimpleDateFormat();
                    break;
    }
 
    Timer t = new Timer(1000, this);
    t.start();
  }
 
  public void actionPerformed(ActionEvent ae) {
    Date d = new Date();
    setText(sdf.format(d));
  }
}
 
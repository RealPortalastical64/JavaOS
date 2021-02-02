package p1;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/******************************************************/
class FontDemo extends JFrame
{
    FontChooser dialog=null; 
    JTextArea ta;
    JButton fontButton;

    FontDemo()
    {
        super("Font");

        ta=new JTextArea(7,20);
        fontButton=new JButton("Set Font");

        ActionListener ac=new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    if(dialog==null)
                        dialog=new FontChooser(ta.getFont());
                    if(dialog.showDialog(FontDemo.this,"Choose a font"))
                    {
                        FontDemo.this.ta.setFont(dialog.createFont());
                    }
                }
            };
        fontButton.addActionListener(ac);

        add(ta,BorderLayout.CENTER);
        add(fontButton,BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50,50,400,400);
        ta.append("\n\n The quick brown fox jumps over the lazy dog.");
        ta.append("\n\n0123456789");
        ta.append("\n~!@#$%^&*()_+|?><");
        setVisible(true);
    }
    ////////////////////////////////
    public static void main(String[] args)
    {
        new FontDemo();
    }

}
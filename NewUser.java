package p1;

import javax.swing.*;
import javax.swing.event.*;   
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class NewUser extends JFrame {
    JButton create;
    JPanel newUserPanel;
    JTextField txuserer;
    JTextField passer;

    public NewUser(){
        super("Add New User");

        create = new JButton("Add");
        newUserPanel = new JPanel();
        txuserer = new JTextField(15);
        passer = new JPasswordField(15);
getRootPane().setDefaultButton(create);
        setSize(300,200);
        setLocation(500,280);
        setResizable(false);
        newUserPanel.setLayout (null); 

        txuserer.setBounds(70,30,150,20);
        passer.setBounds(70,65,150,20);
        create.setBounds(110,100,80,20);

        newUserPanel.add(create);
        newUserPanel.add(txuserer);
        newUserPanel.add(passer);

        getContentPane().add(newUserPanel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener frameClose=new WindowAdapter()          
            {                     
                public void windowClosing(WindowEvent we)             
                {                         
                    setVisible(false); //you can't see me!
                    dispose();
                    login.main(null);//Destroy the JFrame object
                }                  
            }; 
            addWindowListener(frameClose);    
        setVisible(true);

        Writer writer = null;
        File check = new File("userPass.txt");
        if(check.exists()){

            //Checks if the file exists. will not add anything if the file does exist.
        }else{
            try{
                File texting = new File("userPass.txt");
                writer = new BufferedWriter(new FileWriter(texting));
                writer.write("message");
            }catch(IOException e){
                e.printStackTrace();
            }
        }


        create.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        File file = new File("userPass.txt");
                        Scanner scan = new Scanner(file);;

                        FileWriter filewrite = new FileWriter(file, true);

                        String usertxter = " ";
                        String punamer = txuserer.getText();
                        String ppaswder = passer.getText();
                        while (scan.hasNext()) {
                            usertxter = scan.nextLine();
                        }

                        if(punamer.equals(usertxter)) {
                            JOptionPane.showMessageDialog(null,"The username already exists.","Username already exists",
        JOptionPane.INFORMATION_MESSAGE);
                            txuserer.setText("");
                            passer.setText("");
                            txuserer.requestFocus();

                        } 
                        else if(punamer.equals("")){
                            JOptionPane.showMessageDialog(null,"Invalid username","Invalid",
        JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            filewrite.write(punamer+"\n" +ppaswder+ "\n");
                            filewrite.close();
                            JOptionPane.showMessageDialog(null,"Account successfully created.","Account created",
        JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            login.main(null);


                        }
                    } catch (IOException d) {
                        d.printStackTrace();
                    }

                }
            });
    } 

}
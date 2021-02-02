package p1;

import javax.swing.*;
import javax.swing.UIManager.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class login extends JFrame {
    JButton blogin;
    JPanel loginpanel;
    JTextField txuser;
    JTextField pass;
    JButton newUSer;
    JLabel username;
    JLabel password;

    public login(){
        super("Login");

        blogin = new JButton("Log In");
        loginpanel = new JPanel();
        txuser = new JTextField(15);
        pass = new JPasswordField(15);
        newUSer = new JButton("New User");
        username = new JLabel("Username");
        password = new JLabel("Password");
getRootPane().setDefaultButton(blogin);
        setSize(300,200);
        setResizable(false);
       
        setLocationRelativeTo(null);
        loginpanel.setLayout (null); 

        txuser.setBounds(83,30,163,20);
        pass.setBounds(83,65,163,20);
        blogin.setBounds(110,100,80,20);
        newUSer.setBounds(100,135,100,20);
        username.setBounds(20,28,80,29);
        password.setBounds(20,63,80,29);

        loginpanel.add(blogin);
        loginpanel.add(txuser);
        loginpanel.add(pass);
        loginpanel.add(newUSer);
        loginpanel.add(username);
        loginpanel.add(password);

        getContentPane().add(loginpanel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener frameClose=new WindowAdapter()          
            {                     
                public void windowClosing(WindowEvent we)             
                {                         
                    dispose();
                    Shutdown.main(null);
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

        blogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        File file = new File("userPass.txt");
                        Scanner scan = new Scanner(file);;
                        String line = null;
                        FileWriter filewrite = new FileWriter(file, true);

                        String usertxt = " ";
                        String passtxt = " ";
                        String puname = txuser.getText();
                        String ppaswd = pass.getText();
                        boolean isCorrect = false;

                        while (scan.hasNext()) {
                            usertxt = scan.nextLine();
                            passtxt = scan.nextLine();
                            if(puname.equals(usertxt) && ppaswd.equals(passtxt)) {
                                MainMenu menu =new MainMenu();
                                dispose();
                                isCorrect = true;
                            }
                        }

                        if(!isCorrect){

                            JOptionPane.showMessageDialog(null,"Invalid username/password","Invalid",
        JOptionPane.INFORMATION_MESSAGE);
                            txuser.setText("");
                            pass.setText("");
                            txuser.requestFocus();
                        }
                    } catch (IOException d) {
                        d.printStackTrace();
                    }

                }
            });

        newUSer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    NewUser user = new NewUser();
                    dispose();

                }
            });
    } 

    public static void main(String [] args){
        run.f.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        new login();
    }
}
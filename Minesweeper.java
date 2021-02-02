package p1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**u 
 * Java Minesweeper Game
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 */

public class Minesweeper extends JInternalFrame implements ActionListener {

    private JLabel statusbar;
    BoardS board;
    JMenuBar mb;
    JMenu menu;
    JMenuItem newGame;
    public Minesweeper() {

        initUI();
    }

    private void initUI() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        newGame = new JMenuItem("New Game");
        menu = new JMenu("File");
        mb = new JMenuBar();
        menu.add(newGame);
        mb.add(menu);
        setMenuBar(mb);
        newGame.addActionListener(this);
        board = new BoardS(statusbar);
        add(board);
        setLocation(150,50);  
        setResizable(false);
        pack();
        setSize(getWidth(), getHeight() + 5);
        setMaximizable(false);
        setClosable(true);
        setVisible(true);
        setIconifiable(true);
        MainMenu.f.add(this);

        setTitle("Minesweeper");
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {    
        if(e.getSource()==newGame)
        {
            board.newGame();
        }
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

                var ex = new Minesweeper();
                ex.setVisible(true);
            });
    }
}

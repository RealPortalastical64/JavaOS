
package p1;



import javax.imageio.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.awt.image.*;

import java.io.*;

import java.util.*;
import java.util.Collections;
import java.util.List;

class MyButton extends JButton {

    private boolean isLastButton;

    public MyButton() {

        super();

        initUI();
    }

    public MyButton(Image image) {

        super(new ImageIcon(image));

        initUI();
    }

    private void initUI() {

        isLastButton = false;
        BorderFactory.createLineBorder(Color.gray);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        });
    }

    public void setLastButton() {

        isLastButton = true;
    }

    public boolean isLastButton() {

        return isLastButton;
    }
}

public class PuzzleEx extends JInternalFrame implements ActionListener {

    private JPanel panel;
    private JMenuBar menuBar;
    private JMenuBar menuBarSolved;
    private JMenu fileMenu;
    private JMenu fileMenuSolved;
    private JMenuItem solveItem;
    private JMenuItem newGame;
    private BufferedImage source;
    private BufferedImage resized;
    private Image image;
    private MyButton lastButton;
    private int width, height;

    private List<MyButton> buttons;
    private List<MyButton> buttonsSolved;
    private List<Point> solution;

    private final int NUMBER_OF_BUTTONS = 12;
    private final int DESIRED_WIDTH = 300;


    public PuzzleEx(int x, int y) {

        initUI(x, y);
    }

    private void initUI(int x, int y) {

        solution = new ArrayList<>();

        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));

        buttons = new ArrayList<>();
        menuBar = new JMenuBar();
        menuBarSolved = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenuSolved = new JMenu("File");
        solveItem = new JMenuItem("Solve");
        newGame = new JMenuItem("New Game");
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBarSolved.add(fileMenuSolved);
        fileMenu.add(solveItem);
        fileMenu.add(newGame);
        fileMenuSolved.add(newGame);
        solveItem.addActionListener(this);
        newGame.addActionListener(this);
        panel = new JPanel();
        setLocation(x, y);  
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(4, 3, 0, 0));

        try {
            source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, DESIRED_WIDTH, h,
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load image", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);

        add(panel, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {

                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));

                var button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2) {

                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {

                    buttons.add(button);
                }
            }
        }

        Collections.shuffle(buttons);
        buttons.add(lastButton);

        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {

            var btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }

        pack();

        setTitle("Puzzle");
        setResizable(false);
        
			setMaximizable(false);
			setClosable(true);
			setVisible(true);
			MainMenu.f.add(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setLocationRelativeTo(null);
    }
    public void internalFrameActivated(InternalFrameEvent e) {
        while(e.getInternalFrame().isSelected()){
        e.getInternalFrame().toFront();
    }
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == solveItem)
        {
            buttonsSolved = new ArrayList<>();
            panel.removeAll();

            for (JComponent btn : buttons) {

                panel.remove(btn);
            }

            
            for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {

                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));

                var button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                buttonsSolved.add(button);
            }
        }

            for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {

            var btn = buttonsSolved.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }
        setJMenuBar(menuBarSolved);
            checkSolutionSolved();
        }
        else if(e.getSource() == newGame)
        {
            
            PuzzleEx n = new PuzzleEx(getX(),getY());
            dispose();
            try{n.setSelected(true);}
            catch(Exception ex){}
        }
    }
    private int getNewHeight(int w, int h) {

        double ratio = DESIRED_WIDTH / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }

    private BufferedImage loadImage() throws IOException {

        var bimg = ImageIO.read(new File("java.png"));

        return bimg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width,
                                      int height, int type) {

        var resizedImage = new BufferedImage(width, height, type);
        var g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            checkButton(e);
            checkSolution();
        }

        private void checkButton(ActionEvent e) {

            int lidx = 0;

            for (MyButton button : buttons) {
                if (button.isLastButton()) {
                    lidx = buttons.indexOf(button);
                }
            }

            var button = (JButton) e.getSource();
            int bidx = buttons.indexOf(button);

            if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                    || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
                Collections.swap(buttons, bidx, lidx);
                updateButtons();
            }
        }

        private void updateButtons() {

            panel.removeAll();

            for (JComponent btn : buttons) {

                panel.add(btn);
            }

            panel.validate();
        }
    }

    private void checkSolution() {

        var current = new ArrayList<Point>();

        for (JComponent btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
        }

        if (compareList(solution, current)) {
            JOptionPane.showMessageDialog(panel, "You solved it!",
                    "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
private void checkSolutionSolved() {

        var current = new ArrayList<Point>();

        for (JComponent btn : buttonsSolved) {
            current.add((Point) btn.getClientProperty("position"));
        }

        if (compareList(solution, current)) {
            JOptionPane.showMessageDialog(panel, "You solved it!",
                    "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public static boolean compareList(List ls1, List ls2) {

        return ls1.toString().contentEquals(ls2.toString());
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var puzzle = new PuzzleEx(150, 50);
            puzzle.setVisible(true);
        });
    }
}
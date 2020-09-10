package views;

import controllers.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main Game Window View for Cluedo Game
 */
public class MainView {

    //Window dimensions
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private Action upAction;
    private Action downAction;
    private Action leftAction;
    private Action rightAction;
    private Action suggestAction;
    private Action accuseAction;
    private Action rollAction;

    // == Controllers ==================
    private MainController mainController;

    // == View Elements ================
    private JFrame gameWindow;
    private Graphics graphic;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newGameMI;

    private JLabel playerNameLabel;
    private BoardView boardPanel;
    private JButton rollButton;
    private JButton suggestButton;
    private JButton accuseButton;
    private JLabel movesLabel;
    private JTextArea cluesTextArea;

    public MainView(MainController mc) {
        mainController = mc;
        displayGameWindow();
    }

    /*
     * Create all elements on game window
     *  - get player setup info
     *  - call displayBoard to generate game board
     *  - handle player turns
     */
    private void displayGameWindow() {
        //Define main window specs =========================================================
        gameWindow = new JFrame("Cluedo");
        gameWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameWindow.setResizable(true);

        gameWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        gameWindow.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Create menu bar ==================================================================
        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        newGameMI = new JMenuItem("New Game");
        newGameMI.addActionListener(e -> {
            mainController.newGameMethod();
        });
        menu.add(newGameMI);

        //Create game display ==============================================================

        //Player name label
        playerNameLabel = new JLabel("Player Name: ");
        playerNameLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        gameWindow.add(playerNameLabel, c);

        //Game board panel
        boardPanel = new BoardView(mainController);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        gameWindow.add(boardPanel, c);

        //Roll button
        rollButton = new JButton("Roll [R]");
        rollButton.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        gameWindow.add(rollButton, c);
        rollButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainController.rollDice();
            }
        });

        //Suggest button
        suggestButton = new JButton("Suggest [J]");
        suggestButton.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        gameWindow.add(suggestButton, c);
        suggestButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainController.suggestMethod();
            }
        });

        //Accuse button
        accuseButton = new JButton("Accuse [K]");
        accuseButton.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        gameWindow.add(accuseButton, c);
        accuseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainController.accuseMethod();
            }
        });

        //Moves label
        movesLabel = new JLabel("Moves Left: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        gameWindow.add(movesLabel, c);

        //Clues text area
        JPanel textPane = new JPanel();
        textPane.setLayout(new BorderLayout());
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        textPane.setPreferredSize(new Dimension(450, 40));
        gameWindow.add(textPane, c);

        cluesTextArea = new JTextArea();
        cluesTextArea.setEditable(false);                       //Prevent user from editing text area
        JScrollPane cluesSP = new JScrollPane(cluesTextArea);   //Add scroll pane to text area
        textPane.add(cluesSP, BorderLayout.CENTER);

        gameWindow.setJMenuBar(menuBar);
        gameWindow.pack();
        gameWindow.setVisible(true);

        JOptionPane.showMessageDialog(
                gameWindow,
                "Please click 'New Game' through the Menu in the top left to start a new game " +
                        "\nMovement of the players can be done through the [W] [A] [S] [D] keys " +
                        "\nRolling, Suggestions and Accusations can be done through buttons or by pressing [R] [J] [K] respectively",
                "Welcome!",
                JOptionPane.PLAIN_MESSAGE
        );

        gameWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String ObjButtons[] = {"Yes", "No"};
                int promptResult = JOptionPane.showOptionDialog(
                        gameWindow,
                        "Are you sure you want to exit?",
                        "Online Examination System",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        ObjButtons,
                        ObjButtons[1]);
                if(promptResult == 0) {
                    System.exit(0);
                }
            }
        });

        //Initialize Key Bindings ============================================================
        upAction = new UpAction();
        downAction = new DownAction();
        leftAction = new LeftAction();
        rightAction = new RightAction();
        rollAction = new RollAction();
        suggestAction = new SuggestAction();
        accuseAction = new AccuseAction();

        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('w'), "upAction");
        boardPanel.getActionMap().put("upAction", upAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('s'), "downAction");
        boardPanel.getActionMap().put("downAction", downAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('a'), "leftAction");
        boardPanel.getActionMap().put("leftAction", leftAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('d'), "rightAction");
        boardPanel.getActionMap().put("rightAction", rightAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('r'), "rollAction");
        boardPanel.getActionMap().put("rollAction", rollAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('j'), "suggestAction");
        boardPanel.getActionMap().put("suggestAction", suggestAction);
        boardPanel.getInputMap().put(KeyStroke.getKeyStroke('k'), "accuseAction");
        boardPanel.getActionMap().put("accuseAction", accuseAction);
    }

    public void setSuggestButton(boolean bool) {
        suggestButton.setEnabled(bool);
    }

    public void setAccuseButton(boolean bool) {
        accuseButton.setEnabled(bool);
    }

    public void setRollButton(boolean bool) {
        rollButton.setEnabled(bool);
    }

    public void setPlayerNameLabel(String name, String colour) {
        playerNameLabel.setText("Player Name: " + name + " (" + colour + ")");
    }

    public void setCluesTextArea(String str) {
        cluesTextArea.setText("");
        cluesTextArea.append(str);
        cluesTextArea.setCaretPosition(0);
    }

    public JFrame getGameWindow() {
        return gameWindow;
    }

    public void updateMoves(int i) {
        movesLabel.setText("Moves Left: " + i);
    }

    public void updateBoard(){
        boardPanel.repaint();
        boardPanel.revalidate();
    }

    // ================== Key Actions Setup ==================
    public class UpAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainController.movementController(1);
        }
    }

    public class DownAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainController.movementController(3);
        }
    }

    public class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainController.movementController(4);
        }
    }

    public class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainController.movementController(2);
        }
    }

    public class RollAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(rollButton.isEnabled()){
                mainController.rollDice();
            }
        }
    }

    public class AccuseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(accuseButton.isEnabled()){
                mainController.accuseMethod();
            }
        }
    }

    public class SuggestAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(suggestButton.isEnabled()){
                mainController.suggestMethod();
            }
        }
    }

    public void setBoardFocus() {
        boardPanel.requestFocusInWindow();
    }
}

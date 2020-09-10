package views;

import controllers.MainController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Popup window for new game setup
 */
public class NewGameView extends WindowAdapter {

    // == Controllers ==================
    private MainController mainController;

    // == View Elements ================
    private int playerAmount = 0;   //number of players
    private int counter = 0;

    private ArrayList<Suspects> suspects;
    private ArrayList<Weapon> weapons;
    private ArrayList<Room> rooms;
    private static ArrayList<Cell> STARTING_POSITIONS;

    private Map<String, Player> players = new HashMap<>();
    private ArrayList<String> playerTurnOrder = new ArrayList<>();

    public NewGameView(MainController mc, ArrayList<Suspects> susObjs, ArrayList<Weapon> weaObjs, ArrayList<Room> rmObjs,  ArrayList<Cell> startPos) {
        mainController = mc;
        suspects = susObjs;
        weapons = weaObjs;
        rooms = rmObjs;
        STARTING_POSITIONS = startPos;
        createDisplay();
    }

    private void createDisplay() {
        JFrame newFrame = new JFrame("Player Setup");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setResizable(false);
        newFrame.addWindowListener(this);

        newFrame.setMinimumSize(new Dimension(500, 500));
        newFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Player name text field
        JLabel pNameLabel = new JLabel("Player Name: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(20, 0, 0, 0);
        newFrame.add(pNameLabel, c);

        JTextField playerNameTF = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(20, 0, 0, 0);
        newFrame.add(playerNameTF, c);

        //Suspects radio buttons
        ButtonGroup suspectButtons = new ButtonGroup();

        JLabel suspectsLabel = new JLabel("Player Suspect: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(20, 0, 0, 0);
        newFrame.add(suspectsLabel, c);

        JRadioButton suspect1 = new JRadioButton(suspects.get(0).toString());
        suspect1.setActionCommand("0");
        suspectButtons.add(suspect1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 0, 0);
        newFrame.add(suspect1, c);

        JRadioButton suspect2 = new JRadioButton(suspects.get(1).toString());
        suspect2.setActionCommand("1");
        suspectButtons.add(suspect2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(2, 0, 0, 0);
        newFrame.add(suspect2, c);

        JRadioButton suspect3 = new JRadioButton(suspects.get(2).toString());
        suspect3.setActionCommand("2");
        suspectButtons.add(suspect3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(2, 0, 0, 0);
        newFrame.add(suspect3, c);

        JRadioButton suspect4 = new JRadioButton(suspects.get(3).toString());
        suspect4.setActionCommand("3");
        suspectButtons.add(suspect4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.insets = new Insets(2, 0, 0, 0);
        newFrame.add(suspect4, c);

        JRadioButton suspect5 = new JRadioButton(suspects.get(4).toString());
        suspect5.setActionCommand("4");
        suspectButtons.add(suspect5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        c.insets = new Insets(2, 0, 0, 0);
        newFrame.add(suspect5, c);

        JRadioButton suspect6 = new JRadioButton(suspects.get(5).toString());
        suspect6.setActionCommand("5");
        suspectButtons.add(suspect6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.insets = new Insets(2, 0, 0, 0);
        newFrame.add(suspect6, c);

        //Error display message
        JLabel errorMessage = new JLabel("Please enter a unique name and select a suspect!");
        errorMessage.setFont(new Font("Dialog", Font.BOLD, 12));
        errorMessage.setForeground(Color.red);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;
        errorMessage.setVisible(false);
        newFrame.add(errorMessage, c);

        //Button to create next player
        JButton nextPlayer = new JButton("Next Player ->");
        nextPlayer.setEnabled(false);
        nextPlayer.addActionListener(e -> {
            //Ensure a radio button is selected and name is entered
            if ((suspectButtons.getSelection() == null) || playerNameTF.getText().equals("") || players.containsKey(playerNameTF.getText())) {
                errorMessage.setVisible(true);
            } else if (!(suspectButtons.getSelection() == null) && !playerNameTF.getText().equals("")){
                if (counter < playerAmount) {
                    //Create player object
                    int suspectIndex = Integer.parseInt(suspectButtons.getSelection().getActionCommand());
                    String playerName = playerNameTF.getText();
                    players.put(playerName, new Player(playerName, suspects.get(suspectIndex), STARTING_POSITIONS.get(counter), suspects, weapons, rooms));
                    playerTurnOrder.add(playerName);

                    //Reset creator elements
                    suspectButtons.getSelection().setEnabled(false);
                    suspectButtons.clearSelection();
                    errorMessage.setVisible(false);

                    //Check if all players created
                    counter++;
                    if (counter == playerAmount) {
                        counter = 0;
                        newFrame.dispose();
                    }
                } else {
                    counter = 0;
                    newFrame.dispose();
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 0, 0);
        newFrame.add(nextPlayer, c);

        //Number of players combobox
        Integer[] numPlayers = {3, 4, 5, 6};
        JComboBox<Integer> playerNumChooser = new JComboBox<>(numPlayers);
        playerNumChooser.setPreferredSize(new Dimension(50, 30));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        newFrame.add(playerNumChooser, c);

        //Next button to player creation
        JButton nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(100, 30));
        nextButton.addActionListener(e -> {
            playerNumChooser.setEnabled(false);
            nextButton.setEnabled(false);
            nextPlayer.setEnabled(true);
            playerAmount = (Integer) playerNumChooser.getSelectedItem();
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        newFrame.add(nextButton, c);

        newFrame.pack();
        newFrame.setVisible(true);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        mainController.setPlayers(players);
        mainController.setPlayerTurnOrder(playerTurnOrder);
        mainController.setup();
    }
}

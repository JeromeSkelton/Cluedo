package controllers;

import model.*;
import views.BoardView;
import views.MainView;
import views.NewGameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainController {

    // == VIEWS ================================================================================
    private static MainView mainView;

    // == MODELS ===============================================================================
    private static Board board = new Board();

    private ArrayList<Weapon> weapons = new ArrayList<>(Arrays.asList(
            new Weapon("Candlestick"), new Weapon("Dagger"), new Weapon("Lead Pipe"),
            new Weapon("Revolver"), new Weapon("Rope"), new Weapon("Spanner")));

    private ArrayList<Suspects> suspects = new ArrayList<>(Arrays.asList(
            new Suspects("Miss Scarlett"), new Suspects("Colonel Mustard"), new Suspects("Mrs. White"),
            new Suspects("Mr. Green"), new Suspects("Mrs. Peacock"), new Suspects("Professor Plum")
    ));

    private ArrayList<Room> rooms = new ArrayList<>(Arrays.asList(
            new Room("Kitchen"), new Room("Ball Room"), new Room("Conservatory"),
            new Room("Dinning Room"), new Room("Billiard Room"), new Room("Library"),
            new Room("Lounge"), new Room("Hall"), new Room("Study")
    ));

    private static final ArrayList<Cell> STARTING_POSITIONS = new ArrayList<>(Arrays.asList(
            board.getCells()[0][5], board.getCells()[23][5], board.getCells()[23][12],
            board.getCells()[16][0], board.getCells()[7][0], board.getCells()[0][19]));

    private Map<String, Player> players = new HashMap<>();

    private ArrayList<Item> murderPocket;

    // == GAME STATE ===========================================================================

    //Current player status
    private ArrayList<String> playerTurnOrder = new ArrayList<>();
    private int turnCount = 0;
    private Player currentPlayer;
    private int currentMoves;
    private int totalLost = 0;

    public void newGameMethod() {
        NewGameView ngw = new NewGameView(this, suspects, weapons, rooms, STARTING_POSITIONS);
    }
    
    public void accuseMethod() {
        //========================= Initialize the Buttons and PopUp Menu =========================
        JFrame frame = new JFrame("Accusation!");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 120);
        frame.setLocation(430, 100);

        JPanel panel = new JPanel();

        frame.add(panel);

        Suspects[] suspect = {suspects.get(0), suspects.get(1),suspects.get(2),suspects.get(3),suspects.get(4), suspects.get(5)};
        Weapon[] weapon = {weapons.get(0), weapons.get(1),weapons.get(2),weapons.get(3),weapons.get(4), weapons.get(5)};
        Room[] room = {rooms.get(0), rooms.get(1),rooms.get(2),rooms.get(3),rooms.get(4), rooms.get(5)};

        final JComboBox<Suspects> selChoice = new JComboBox<Suspects>(suspect);
        final JComboBox<Weapon> selWeap = new JComboBox<Weapon>(weapon);
        final JComboBox<Room> selRoom = new JComboBox<Room>(room);

        selChoice.setVisible(true);
        selWeap.setVisible(true);
        selRoom.setVisible(true);
        panel.add(selChoice);
        panel.add(selWeap);
        panel.add(selRoom);

        JButton btn = new JButton("OK");
        panel.add(btn);

        JButton cancelBtn = new JButton("CANCEL");
        panel.add(cancelBtn);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Item> accusation = new ArrayList<>();

                Object susChoice = selChoice.getSelectedItem(); //gets suspect item from drop-down menu
                Object weapChoice = selWeap.getSelectedItem(); //gets weapon item from drop-down menu
                Object roomChoice = selRoom.getSelectedItem(); //gets room item from drop-down menu

                accusation.add((Item)susChoice);
                accusation.add((Item)weapChoice);
                accusation.add((Item)roomChoice);

                frame.dispose();

                if (murderPocket.containsAll(accusation)) {
                    //PLAYER HAS WON
                    JOptionPane.showMessageDialog(
                            frame,
                            "Congratulations! Player " + currentPlayer.getName() + " has successfully deduced that \n" +
                            murderPocket.get(0).getName() + " used the " + murderPocket.get(1).getName() + " to kill someone in the " + murderPocket.get(2).getName(),
                            "Congratulations!",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    System.exit(0);
                } else {
                    //PLAYER HAS LOST
                    currentPlayer.setLost(true);
                    JOptionPane.showMessageDialog(
                            frame,
                            "Sorry you have failed to deduce the correct murder scene.",
                            "Player Game Over",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    totalLost++;

                    if (totalLost == players.size()) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Sorry all players have been unable to deduce the correct murder scene.",
                                "Game Over",
                                JOptionPane.PLAIN_MESSAGE
                        );

                        JOptionPane.showMessageDialog(
                                frame,
                                murderPocket.get(0).getName() + " used the " + murderPocket.get(1).getName() + " to kill someone in the " + murderPocket.get(2).getName() + " and successfully escaped",
                                "Game Over",
                                JOptionPane.PLAIN_MESSAGE
                        );

                        System.exit(0);
                    } else {
                        nextPlayerTurn();
                    }
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.setBoardFocus();
                frame.dispose();
            }
        });
    }

    public void suggestMethod() {

        //========================= Initialize the Buttons and PopUp Menu =========================

        JFrame frame = new JFrame("Suggestion!");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(250, 200));

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Suspects[] suspect = new Suspects[suspects.size()];
        suspect = suspects.toArray(suspect);
        Weapon[] weapon = new Weapon[weapons.size()];
        weapon = weapons.toArray(weapon);

        JComboBox<Suspects> selChoice = new JComboBox<>(suspect);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        frame.add(selChoice, c);

        JComboBox<Weapon> selWeap = new JComboBox<>(weapon);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        frame.add(selWeap, c);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.setBoardFocus();
                frame.dispose();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        frame.add(cancelBtn, c);

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Item> suggestion = new ArrayList<>();

                Suspects susChoice = (Suspects) selChoice.getSelectedItem(); //gets suspect item from drop-down menu
                Weapon weapChoice = (Weapon) selWeap.getSelectedItem(); //gets weapon item from drop-down menu
                Room roomChoice = currentPlayer.getCurrRoom(); //gets room the current player is in

                suggestion.add(susChoice);
                suggestion.add(weapChoice);
                suggestion.add(roomChoice);

                suggestionController(frame, suggestion);

                nextPlayerTurn();

                frame.dispose();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        frame.add(okBtn, c);

        frame.pack();
        frame.setVisible(true);
    }

    private void suggestionController(Frame frame, ArrayList<Item> sug) {
        for (Player p : players.values()) {
            if (p.equals(currentPlayer)) {
                continue;
            }

            if (Collections.disjoint(sug, p.getCards())) {
                continue;
            }

            //Get possible refute items
            ArrayList<Item> refuteList = new ArrayList<>();
            for (int i = 0; i < sug.size(); i++) {
                if (p.getCards().contains(sug.get(i))) {
                    refuteList.add(sug.get(i));
                }
            }

            //Refute items combobox chooser
            Item[] refuteItems = new Item[refuteList.size()];
            refuteItems = refuteList.toArray(refuteItems);

            Item refutationItem = (Item)JOptionPane.showInputDialog(
                    frame,
                    "Player " + p.getName() + " - Please select a card to refute " + currentPlayer.getName() + "'s suggestion: ",
                    "Refute",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    refuteItems,
                    refuteItems[0]
            );

            if (!(refutationItem == null)) {
                currentPlayer.receiveClues(refutationItem);
                System.out.println("Received clue: " + refutationItem.getName());
            }
        }
    }

    private void nextPlayerTurn() {
        String currPlayerName;
        do {
            currPlayerName = playerTurnOrder.get(turnCount % players.size());
            currentPlayer = players.get(currPlayerName);
            turnCount++;
        } while (currentPlayer.isLost());

        currentMoves = 0;

        mainView.setPlayerNameLabel(currPlayerName, currentPlayer.getColorToString());
        mainView.setCluesTextArea(currentPlayer.checkClues());
        mainView.setRollButton(true);
        mainView.updateMoves(currentMoves);

        checkSuggestButton();

        JOptionPane.showMessageDialog(
                mainView.getGameWindow(),
                "Player " + currPlayerName + "'s turn! Please roll before attempting to move",
                "Turn Over",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void checkSuggestButton() {
        if (currentPlayer.getPos().getType() > 0 && currentPlayer.getPos().getType() <= 10) {
            mainView.setSuggestButton(true);
        } else {
            mainView.setSuggestButton(false);
        }
    }

    //========================= CluedoGame method calls below =========================

    public void setup() {
        mainView.setBoardFocus();
        // Shuffle decks
        ArrayList<Suspects> susCopy = (ArrayList<Suspects>) suspects.clone();
        ArrayList<Weapon> weaCopy = (ArrayList<Weapon>) weapons.clone();
        ArrayList<Room> rooCopy = (ArrayList<Room>) rooms.clone();

        Collections.shuffle(weaCopy);
        Collections.shuffle(susCopy);
        Collections.shuffle(rooCopy);

        // Pick one random card from each deck to be in the murderPocket
        murderPocket = new ArrayList<>();
        murderPocket.add(susCopy.get(0));
        susCopy.remove(0);
        murderPocket.add(weaCopy.get(0));
        weaCopy.remove(0);
        murderPocket.add(rooCopy.get(0));
        rooCopy.remove(0);

        // Rest of cards in to deck and shuffle deck
        Stack<Item> deck = new Stack<>();
        deck.addAll(weaCopy);
        deck.addAll(susCopy);
        deck.addAll(rooCopy);
        Collections.shuffle(deck);

        // Deal cards to players hand
        int count = 0;
        while (!deck.isEmpty()) {
            if (count == players.size()) {
                count = 0;
            }

            players.get(playerTurnOrder.get(count)).addCard(deck.pop());
            count++;
        }

        //TODO: DEBUGGING
        System.out.println("MURDER POCKET: ");
        for (Item i : murderPocket) {
            System.out.print(" " + i.getName());
        }
        System.out.println("");

        for (Player p : players.values()) {
            System.out.println("Player - " + p.getName() + ": " + p.getPos().getX() + " " + p.getPos().getY());
            for (Item i : p.getCards()) {
                System.out.print(" " + i.getName());
            }
            System.out.println("");
        }

        mainView.updateBoard();

        mainView.setAccuseButton(true);

        nextPlayerTurn();
    }

    public void rollDice() {
        Random rand = new Random();
        int roll = rand.nextInt(13);

        if (roll == 0) {
            roll = 1;
        }

        mainView.setRollButton(false);      //reset when next player's turn starts
        mainView.updateMoves(roll);

        currentMoves = roll;
        mainView.setBoardFocus();
    }

    public void movementController(int choice) {
        if(currentMoves != 0) {
            //update new cell pos
            int[] newCoord = currentPlayer.move(choice);
            if (newCoord[0] == -1 || newCoord[1] == -1) {
                System.out.println("Not a valid movement");
                return;
            }

            Cell newPos = board.getCells()[newCoord[0]][newCoord[1]];

            //update old cell pos
            Cell oldPos = currentPlayer.getPos();
            if (oldPos.getType() != 0) {

            } else {
                oldPos.setType(0);
            }

            oldPos.setOccupant(0);

            if (newPos.getType() == 13 || newPos.getOccupant() > 0) {
                System.out.println("Not a valid movement");
                newPos = oldPos; //set newPos to current pos if its an invalid movement
                return;
            } else {
                currentPlayer.setPos(newPos);
            }

            if(newPos != oldPos){
                currentMoves--;
                mainView.updateMoves(currentMoves);
            }

            checkSuggestButton();
            mainView.updateBoard();

            if (currentMoves == 0){
                nextPlayerTurn();
            }
        }
    }

    public Cell[][] getCells() {
        return board.getCells();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayerTurnOrder(ArrayList<String> playerTurnOrder) {
        this.playerTurnOrder = playerTurnOrder;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public static void main(String[] args) {
        MainController mc = new MainController();
        mainView = new MainView(mc);
    }
}

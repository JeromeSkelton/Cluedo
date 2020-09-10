package model;

import java.awt.*;
import java.util.*;

/**
 * model.Player class to handle all player actions and track position and known clues
 */
public class Player {

    private final String separate = "===========================================\n";

    //player state
    private String name;
    private Suspects playerSuspect;
    private Cell pos;
    private boolean lost;
    private ArrayList<Item> cards = new ArrayList<>();

    private final Map<Suspects, String> suspects;
    private final Map<Weapon, String> weapons;
    private final Map<Room, String> rooms;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 650;
    private static final int RECT_WIDTH = WINDOW_WIDTH / 24;
    private static final int RECT_HEIGHT = WINDOW_HEIGHT / 25;
    private static final int spacing = 2;

    /**
     * Initialise model.Player object
     * @param name - player name
     * @param playerSuspect - suspect character chosen by player
     * @param startPos - starting position of player on board
     * @param susObjs - list of suspects in game
     * @param weaObjs - list of weapons in game
     * @param rmObjs - list of rooms in game
     */
    public Player(String name, Suspects playerSuspect, Cell startPos, ArrayList<Suspects> susObjs, ArrayList<Weapon> weaObjs, ArrayList<Room> rmObjs) {
        this.name = name;
        this.playerSuspect = playerSuspect;
        this.pos = startPos;
        this.lost = false;

        this.suspects = new HashMap<>();
        for (Suspects sus : susObjs) {
            suspects.put(sus, " ");
        }

        this.weapons = new HashMap<>();
        for (Weapon wea : weaObjs) {
            weapons.put(wea, " ");
        }

        this.rooms = new HashMap<>();
        for (Room rm : rmObjs) {
            rooms.put(rm, " ");
        }
    }

    /**
     * Finds next cell co-ordinate based on direction passed in
     * @param dir - direction to move player
     * @return int[x][y] co-ordinate array of new cell position
     */
    public int[] move(Integer dir) {
        //get next co-ordinate - handle movable checks
        int x = pos.getX();
        int y = pos.getY();

        if (dir.equals(1)) {
            // North movement
            y--;
            if (y < 0) { y = -1; }
        } else if (dir.equals(3)) {
            // South movement
            y++;
            if (y > 24) { y = -1; }
        } else if (dir.equals(2)) {
            // East movement
            x++;
            if (x > 23) { x = -1; }
        } else if (dir.equals(4)) {
            // West movement
            x--;
            if (x < 0) { x = -1; }
        }

        return new int[]{x, y};
    }

    /**
     * Receive clues from other players due to refutation
     * @param clue - received from other players
     */
    public void receiveClues(Item clue) {
        //find relevant map to update by checking if clue is right type & exists in the map
        if (clue instanceof Suspects && suspects.containsKey(clue)) {
            suspects.replace((Suspects) clue, "X");
        } else if (clue instanceof Weapon && weapons.containsKey(clue)) {
            weapons.replace((Weapon) clue, "X");
        } else if (clue instanceof Room && rooms.containsKey(clue)) {
            rooms.replace((Room) clue, "X");
        }
    }

    /**
     * Display current clue report of player.
     *  - Will display an "X" against a received clue
     *  - Will display an " " against clues not yet received
     */
    public String checkClues() {
        StringBuilder sb = new StringBuilder();
        sb.append("MANSION CLUE REPORT\n");
        sb.append("WHO?\n");
        for (Map.Entry<Suspects, String> entry : suspects.entrySet()) {
            String temp = " - " + entry.getKey().getName() + " | " + entry.getValue() + " | \n";
            sb.append(temp);
        }
        sb.append("WHAT?\n");
        for (Map.Entry<Weapon, String> entry : weapons.entrySet()) {
            String temp = " - " + entry.getKey().getName() + " | " + entry.getValue() + " | \n";
            sb.append(temp);
        }
        sb.append("WHERE?\n");
        for (Map.Entry<Room, String> entry : rooms.entrySet()) {
            String temp = " - " + entry.getKey().getName() + " | " + entry.getValue() + " | \n";
            sb.append(temp);
        }

        return sb.toString();
    }

    public void draw(Graphics g){
        //TODO: Get player position in 2D Board and draw it
        if (playerSuspect.getName().equals("Miss Scarlett")){
            g.setColor(Color.RED);
        } else if (playerSuspect.getName().equals("Colonel Mustard")){
            g.setColor(Color.ORANGE);
        } else if(playerSuspect.getName().equals("Mrs. White")){
            g.setColor(Color.WHITE);
        } else if (playerSuspect.getName().equals("Mr. Green")){
            g.setColor(Color.GREEN);
        } else if(playerSuspect.getName().equals("Mrs. Peacock")){
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.PINK);
        }
        g.fillOval((spacing + pos.getX() * RECT_WIDTH) + (RECT_WIDTH / 8) - 1, spacing + pos.getY() * RECT_HEIGHT + RECT_HEIGHT, (RECT_HEIGHT - 2 * spacing), RECT_HEIGHT - 2 * spacing);
    }

    public Room getCurrRoom() {
        return pos.getRoom();
    }

    public void setPos(Cell newPos) {
        this.pos = newPos;
    }

    public Cell getPos() {
        return this.pos;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public void addCard(Item card) {
        cards.add(card);
        receiveClues(card);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getCards() {
        return cards;
    }

    public String getColorToString() {
        if (playerSuspect.getName().equals("Miss Scarlett")){
            return "Red";
        } else if (playerSuspect.getName().equals("Colonel Mustard")){
            return "Orange";
        } else if(playerSuspect.getName().equals("Mrs. White")){
            return "White";
        } else if (playerSuspect.getName().equals("Mr. Green")){
            return "Green";
        } else if(playerSuspect.getName().equals("Mrs. Peacock")){
            return "Blue";
        } else {
            return "Pink";
        }
    }
}

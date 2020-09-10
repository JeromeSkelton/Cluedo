package model;

import java.awt.*;

public class Cell {
    private int x;
    private int y;
    private int type;
    private int occupant;
    private Room room;
    private Color color;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 650;
    private static final int RECT_WIDTH = WINDOW_WIDTH / 24;
    private static final int RECT_HEIGHT = WINDOW_HEIGHT / 25;
    private static final int spacing = 2;

    Cell(int x, int y, int type, int occ, Room room, Color col) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.occupant = occ;
        this.room = room;
        color = col;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getOccupant() {
        return occupant;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOccupant(int occupant) {
        this.occupant = occupant;
    }

    public Room getRoom(){
        return room;
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(spacing + x * RECT_WIDTH, spacing + y * RECT_HEIGHT + RECT_HEIGHT, RECT_WIDTH - 2 * spacing, RECT_HEIGHT - 2 * spacing);
    }
}

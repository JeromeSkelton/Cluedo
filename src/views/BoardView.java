package views;

import controllers.MainController;
import model.Cell;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardView extends JPanel {

    private static final int RECT_X = 0;
    private static final int RECT_Y = RECT_X;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 650;
    private static final int RECT_WIDTH = WINDOW_WIDTH / 24;
    private static final int RECT_HEIGHT = WINDOW_HEIGHT / 25;


    private static final int spacing = 2;
    private static MainController mainController;
    private Cell[][] cells = new Cell[24][25];

    public BoardView(MainController mc){
        mainController = mc;
        cells = mc.getCells();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(RECT_X, RECT_Y + RECT_HEIGHT - spacing, WINDOW_WIDTH - spacing * 2, WINDOW_HEIGHT);
        g.setColor(Color.yellow);

        for (int y = 0; y < cells[0].length; y++) {
            for (int x = 0; x < cells.length; x++) {
                cells[x][y].draw(g);
            }
        }

        if (!mainController.getPlayers().isEmpty()){
            for (Player player : mainController.getPlayers().values()) {
                player.draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(WINDOW_WIDTH + 2, WINDOW_HEIGHT + 2);
    }



}

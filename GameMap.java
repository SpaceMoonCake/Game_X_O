package GameXO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {

    public static final int GAME_MODE_HUMAN_AND_AI = 0;
    public static final int GAME_MODE_HUMAN_AND_HUMAN = 1;

    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;

    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private int stateGameOver;

    private boolean isGameOver;
    private boolean initializedMap;

    public static final Random RANDOM = new Random();

    private int mapSizeX;
    private int mapSizeY;
    private int winLength;
    private int[][] map;

    private int sellWidth;
    private int sellHeight;


    GameMap(){
        setBackground(Color.PINK);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                update(e);
            }
        });
        initializedMap = false;

    }

    void startNewGame(int mode, int mapSizeX, int mapSizeY, int winLength){
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        this.winLength = winLength;
        this.map = new int [mapSizeX][mapSizeY];
        isGameOver = false;
        initializedMap = true;
        repaint();
    }

    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        render(g);
    }

    private void render (Graphics g) {
        if (!initializedMap) return;
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.CYAN);

        sellWidth = width/mapSizeX;
        sellHeight = height/mapSizeY;

        for (int i = 0; i < mapSizeY; i++){
            int y = i * sellHeight;
            g.drawLine(0, y, width, y);
        }

        for (int i = 0; i < mapSizeX; i++){
            int x = i * sellWidth;
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < mapSizeY; y++) {
            for (int x = 0; x < mapSizeX; x++) {

                if (isEmptyCell(x,y)) {
                    continue;
                }

                if (map[y][x] == DOT_HUMAN) {
                    g.setColor(new Color(192, 192, 192));
                    g.fillOval(x * sellWidth, y * sellHeight, sellWidth, sellHeight);
                } else if (map[y][x] == DOT_AI) {
                    g.setColor(Color.CYAN);
                    g.fillOval(x * sellWidth, y * sellHeight, sellWidth, sellHeight);
                } else {
                    throw new RuntimeException("Can't paint cellX " + x + " cellY " + y);
                }

            }
        }

        if (isGameOver) {
            showMessageGameOver(g);
        }
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 45));

        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString("Ничья", 180, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString("Победил человек", 100, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString("Победил ИИ", 120, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected game over state: " + stateGameOver);
        }
    }

    private void setGameOver(int gameOverState) {
        stateGameOver = gameOverState;
        isGameOver = true;
        repaint();
    }

    private void update(MouseEvent e) {
        if (!initializedMap) return;
        if (isGameOver) return;

        int cellX = e.getX() / sellWidth;
        int cellY = e.getY() / sellHeight;

        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) {
            return;
        }
        map[cellY][cellX] = DOT_HUMAN;

        if (checkWin(DOT_HUMAN)) {
            setGameOver(STATE_WIN_HUMAN);
            return;
        }

        if (isFullMap()) {
            setGameOver(STATE_DRAW);
            return;
        }

        aiTurn();
        repaint();
        if (checkWin(DOT_AI)){
            setGameOver(STATE_WIN_AI);
            return;
        }
        if (isFullMap()) {
            setGameOver(STATE_DRAW);
            return;
        }

    }

    public void aiTurn() {
        if (turnAIWinCell()) { //выиграет-ли игрок на следующем ходу?
            return;
        }
        if (turnHumanWinCell()) { //выиграет-ли комп на следующем ходу?
            return;
        }
        int x;
        int y;
        do {
            x = RANDOM.nextInt(mapSizeX);
            y = RANDOM.nextInt(mapSizeY);
        } while (!isEmptyCell(x, y));
        map[y][x] = DOT_AI;
    }

    private boolean turnAIWinCell() {
        for (int i = 0; i < mapSizeY; i++) {
            for (int j = 0; j < mapSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    map[i][j] = DOT_AI;               // поставим нолик в каждую клетку поля по очереди
                    if (checkWin(DOT_AI)) {
                        return true;    // если мы выиграли, вернём истину, оставив нолик в выигрышной позиции
                    }
                    map[i][j] = DOT_EMPTY;            // если нет - вернём обратно пустоту в клетку и пойдём дальше
                }
            }
        }
        return false;
    }

    // Проверка, выиграет-ли игрок своим следующим ходом
    private boolean turnHumanWinCell() {
        for (int i = 0; i < mapSizeY; i++) {
            for (int j = 0; j < mapSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    map[i][j] = DOT_HUMAN;            // поставим крестик в каждую клетку по очереди
                    if (checkWin(DOT_HUMAN)) {            // если игрок победит
                        map[i][j] = DOT_AI;            // поставить на то место нолик
                        return true;
                    }
                    map[i][j] = DOT_EMPTY;            // в противном случае вернуть на место пустоту
                }
            }
        }
        return false;
    }

    // проверка на победу
    private boolean checkWin(int c) {
        for (int i = 0; i < mapSizeX; i++) {            // ползём по всему полю
            for (int j = 0; j < mapSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLength, c)) {
                    return true;    // проверим линию по х
                }
                if (checkLine(i, j, 1, 1, winLength, c)) {
                    return true;    // проверим по диагонали х у
                }
                if (checkLine(i, j, 0, 1, winLength, c)) {
                    return true;    // проверим линию по у
                }
                if (checkLine(i, j, 1, -1, winLength, c)) {
                    return true;    // проверим по диагонали х -у
                }
            }
        }
        return false;
    }

    // проверка линии
    private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
        final int farX = x + (len - 1) * vx;            // посчитаем конец проверяемой линии
        final int farY = y + (len - 1) * vy;
        if (!isValidCell(farX, farY)) {
            return false;    // проверим не выйдет-ли проверяемая линия за пределы поля
        }
        for (int i = 0; i < len; i++) {                    // ползём по проверяемой линии
            if (map[y + i * vy][x + i * vx] != c) {
                return false;    // проверим одинаковые-ли символы в ячейках
            }
        }
        return true;
    }

    public boolean isFullMap() {
        for (int i = 0; i < mapSizeY; i++) {
            for (int j = 0; j < mapSizeX; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < mapSizeX && y >= 0 && y < mapSizeY;
    }

    public boolean isEmptyCell(int x, int y) {
        return map[y][x] == DOT_EMPTY;
    }

}

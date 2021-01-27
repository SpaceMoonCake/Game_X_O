package GameXO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_COORDINATE_X = 700;
    private static final int WINDOW_COORDINATE_Y = 250;

    private Settings settingsWindow;
    private GameMap gameMap;

    MainWindow(){
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(WINDOW_COORDINATE_X, WINDOW_COORDINATE_Y);
        setTitle("Игра: Крестики - Нолики");
        setResizable(false);

        gameMap = new GameMap();
        settingsWindow = new Settings(this);

        JButton btnStartGame = new JButton("Новая игра!");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsWindow.setVisible(true);
            }
        });

        JButton btnExitGame = new JButton("Выйти из игры");
        btnExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(1, 2));
        panelButton.add(btnExitGame);
        panelButton.add(btnStartGame);
        add(panelButton, BorderLayout.SOUTH);

        add(gameMap);


    }

    void startNewGame(int mode, int mapSizeX, int mapSizeY, int winLength){
        gameMap.startNewGame(mode, mapSizeX, mapSizeY, winLength);
    }



}

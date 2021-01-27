package GameXO;

import javafx.scene.control.Slider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private JRadioButton HumanAndAI;
    private JRadioButton HumanAndHuman;
    private JSlider sliderMapSize;
    private JSlider sliderWinLength;

    private static final int MAP_MIN_SIZE = 3;
    private static final int MAP_MAX_SIZE = 10;
    private static final int MAP_MIN_WIN_SIZE = 3;
    private static final String MAP_SIZE_TEXT= "Размерность поля: ";
    private static final String MAP_WIN_TEXT= "Длина выигрышной позиции: ";


    private MainWindow mainWindow;

    Settings(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new GridLayout(10,1));

        Rectangle newWindowBounds = mainWindow.getBounds();
        int posX = (int) newWindowBounds.getCenterX() - WINDOW_WIDTH/2;
        int posY = (int) newWindowBounds.getCenterY() - WINDOW_HEIGHT/2;

        setLocation(posX, posY);
        setResizable(false);
        setTitle("Настройки новой игры");

        addSettingsMode();
        addSettingsSize();

        JButton btnStartGame = new JButton("Начать играть!");

        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickStartGame();
            }
        });

        add(btnStartGame);
    }

    private void addSettingsMode(){
        add(new JLabel("Выберите режим игры:"));
        HumanAndAI = new JRadioButton("Human and AI", true);
        HumanAndHuman = new JRadioButton("Human and Human");
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(HumanAndAI);
        gameMode.add(HumanAndHuman);
        add(HumanAndAI);
        add(HumanAndHuman);
    }

    private void addSettingsSize(){
        JLabel textMapSize = new JLabel(MAP_SIZE_TEXT + MAP_MIN_SIZE);
        JLabel textWinLength = new JLabel(MAP_WIN_TEXT + MAP_MIN_WIN_SIZE);

        sliderMapSize = new JSlider(MAP_MIN_SIZE, MAP_MAX_SIZE, MAP_MIN_SIZE);
        sliderMapSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderMapSize.getValue();
                textMapSize.setText(MAP_SIZE_TEXT + currentValue);
                sliderWinLength.setMaximum(currentValue);
            }
        });

        sliderWinLength = new JSlider(MAP_MIN_WIN_SIZE, MAP_MIN_SIZE, MAP_MIN_SIZE);
        sliderWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textWinLength.setText(MAP_WIN_TEXT + sliderWinLength.getValue());
            }
        });

        add(new JLabel("Выберите размер поля:"));
        add(textMapSize);
        add(sliderMapSize);
        add(new JLabel("Выберите длину выигрышной позиции:"));
        add(textWinLength);
        add(sliderWinLength);

    }

    private void clickStartGame (){
        int gameMode;

        if (HumanAndAI.isSelected()){
            gameMode = GameMap.GAME_MODE_HUMAN_AND_AI;
            ;
        } else if (HumanAndHuman.isSelected()){
            gameMode = GameMap.GAME_MODE_HUMAN_AND_HUMAN;
        } else {
            throw new RuntimeException("Неизвестный режим игры!");
        }

        int mapSize = sliderMapSize.getValue();
        int winLength = sliderWinLength.getValue();

        mainWindow.startNewGame(gameMode, mapSize, mapSize, winLength);
        setVisible(false);

    }
}

package Controller;
import Model.PacmanModel;
import View.PacmanView;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.scene.media.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class PacmanController implements EventHandler<KeyEvent> {
    final private static double FRAMES_PER_SECOND = 5.5;

    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label gameOverLabel;
    @FXML private PacmanView pacManView;
    private PacmanModel pacManModel;
    private static final String[] levelFiles = {
            "src/main/resources/levels/level1.txt",
            "src/main/resources/levels/level2.txt",
            "src/main/resources/levels/level3.txt",
            "src/main/resources/levels/level4.txt",
            "src/main/resources/levels/level5.txt",
            "src/main/resources/levels/level6.txt",
            "src/main/resources/levels/level7.txt",
            "src/main/resources/levels/level8.txt",
            "src/main/resources/levels/level9.txt",
            "src/main/resources/levels/level10.txt",
            
    };


    //src/main/resources/levels/level3.txt"

    private Timer timer;
    private static int ghostEatingModeCounter;
    private boolean paused;
    MediaPlayer mediaPlayer;

    public PacmanController() {
        this.paused = false;
    }

    public void initialize() {
        String musicFile = "src/main/resources/music/pacman-waka-waka.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(20);
        mediaPlayer.play();
        String file = this.getLevelFile(0);
        this.pacManModel = new PacmanModel();
        this.update(PacmanModel.Direction.NONE);
        ghostEatingModeCounter = 25;
        this.startTimer();

    }

    private void startTimer() {

        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        update(pacManModel.getCurrentDirection());
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    private void update(PacmanModel.Direction direction) {
        this.pacManModel.step(direction);
        this.pacManView.update(pacManModel);
        this.scoreLabel.setText(String.format("Puntos: %d", this.pacManModel.getScore()));
        this.levelLabel.setText(String.format("Nivel: %d", this.pacManModel.getLevel()));
        if (pacManModel.isGameOver()) {
            this.gameOverLabel.setText(String.format("GAME OVER"));
            mediaPlayer.pause();
            pause();
        }
        if (pacManModel.isYouWon()) {
            this.gameOverLabel.setText(String.format("Ganaste!"));
        }

        if (pacManModel.isGhostEatingMode()) {
            ghostEatingModeCounter--;
        }
        if (ghostEatingModeCounter == 0 && pacManModel.isGhostEatingMode()) {
            pacManModel.setGhostEatingMode(false);
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {

        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        PacmanModel.Direction direction = PacmanModel.Direction.NONE;
        if (code == KeyCode.LEFT) {
            direction = PacmanModel.Direction.LEFT;
        } else if (code == KeyCode.RIGHT) {
            direction = PacmanModel.Direction.RIGHT;
        } else if (code == KeyCode.UP) {
            direction = PacmanModel.Direction.UP;
        } else if (code == KeyCode.DOWN) {
            direction = PacmanModel.Direction.DOWN;
        } else if (code == KeyCode.G) {
            mediaPlayer.play();
            pause();
            this.pacManModel.startNewGame();
            this.gameOverLabel.setText(String.format(""));
            paused = false;
            this.startTimer();
        } else {
            keyRecognized = false;
        }
        if (keyRecognized) {
            keyEvent.consume();
            pacManModel.setCurrentDirection(direction);
        }
    }

    public void pause() {
        this.timer.cancel();
        this.paused = true;
    }

    public double getBoardWidth() {
        return PacmanView.CELL_WIDTH * this.pacManView.getColumnCount();
    }

    public double getBoardHeight() {
        return PacmanView.CELL_WIDTH * this.pacManView.getRowCount();
    }

    public static void setGhostEatingModeCounter() {
        ghostEatingModeCounter = 25;
    }

    public static int getGhostEatingModeCounter() {
        return ghostEatingModeCounter;
    }

    public static String getLevelFile(int x)
    {
        return levelFiles[x];
    }

    public boolean getPaused() {
        return paused;
    }
}
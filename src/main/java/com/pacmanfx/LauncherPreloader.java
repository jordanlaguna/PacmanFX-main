package com.pacmanfx;
import Controller.InitPreloader;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Objects;

/**
 * The type Launcher preloader.
 */
public class LauncherPreloader extends Preloader {

    private Stage preloaderStage;
    private Scene scene;
    MediaPlayer mediaPlayer;
    /**
     * Instantiates a new Launcher preloader.
     */
    public LauncherPreloader() {
    }

    @Override
    public void init() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoadScreen.fxml")));
        scene = new Scene(root);
        String musicFile = "src/main/resources/music/pacman-song.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(2);
        mediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) {
        this.preloaderStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info){
        if(info instanceof ProgressNotification){
            InitPreloader.lblLoadingg.setText("Loading " + ((ProgressNotification) info).getProgress() * 100 + " % ");
            InitPreloader.statprogressBar.setProgress(((ProgressNotification)info).getProgress());
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type  = info.getType();
        switch(type){
            case BEFORE_START:
                System.out.println("BEFORE_START");
                preloaderStage.hide();
                break;
        }
    }
}
package com.pacmanfx;
import Controller.PacmanController;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;


public class MainApp extends Application {

    private static final int COUNT_LIMIT = 10;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/start.fxml")));
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress = (double) i / 10;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(MainApp.class, LauncherPreloader.class, args);
    }
}

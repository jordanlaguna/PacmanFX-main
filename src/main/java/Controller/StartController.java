package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;
import Controller.PacmanController;
import javafx.stage.StageStyle;

public class StartController implements Initializable {

    @FXML
    private Button btStart;

    @FXML
    private Button btPlayer;

    @FXML
    private Button btSetting;

    @FXML
    private Button btExit;
    @FXML
    private AnchorPane paneOne;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void Start(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacman.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("PACMAN");

        PacmanController controller = loader.getController();
        root.setOnKeyPressed(controller);
        double sceneWidth = controller.getBoardWidth() + 20.0;
        double sceneHeight = controller.getBoardHeight() + 100.0;

        primaryStage.setScene(new Scene(root, sceneWidth, sceneHeight));

        primaryStage.show();
        root.requestFocus();

        Stage myStage = (Stage) this.paneOne.getScene().getWindow();
        myStage.close();

    }

    @FXML
    void Player(ActionEvent event) {}

    @FXML
    void Settings(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LevelScreen.fxml")));
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Exit(ActionEvent event) {
        Platform.exit();
    }
}

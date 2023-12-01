package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelScreenController implements Initializable {

    @FXML
    private Button btnClose;
    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
        if (primaryStage != null) {
            primaryStage.show();
        }
    }
}

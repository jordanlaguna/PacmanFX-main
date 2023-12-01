package Controller;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class InitPreloader implements Initializable {

    @FXML
    private ProgressBar progressBar;
    public static ProgressBar statprogressBar;
    public Label lblLoading;
    public static Label lblLoadingg;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        lblLoadingg = lblLoading;
        statprogressBar = progressBar;

    }
}

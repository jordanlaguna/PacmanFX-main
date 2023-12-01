package View;
import Controller.PacmanController;
import Model.PacmanModel;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Model.PacmanModel.CellValue;

import java.util.Objects;

public class PacmanView extends Group {
    public final static double CELL_WIDTH = 25.0;

    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
    private Image pacmanRightImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image ghost1Image;
    private Image ghost2Image;
    private Image ghost3Image;
    private Image ghost4Image;
    private Image blueGhostImage;
    private Image wallImage;
    private Image bigDotImage;
    private Image smallDotImage;

    public PacmanView() {
        this.pacmanRightImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pacmanRight.gif")));
        this.pacmanUpImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pacmanUp.gif")));
        this.pacmanDownImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pacmanDown.gif")));
        this.pacmanLeftImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pacmanLeft.gif")));
        this.ghost1Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/blinkyGhost.gif")));
        this.ghost2Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pinkyGhost.gif")));
        this.ghost3Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/inkyGhost.gif")));
        this.ghost4Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/clydeGhost.gif")));
        this.blueGhostImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ghostBlue.gif")));
        this.wallImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imagenesMuro/wall2.png")));
        this.bigDotImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whitedot.png")));
        this.smallDotImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/smalldot.png")));
    }

    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    public void update(PacmanModel model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++){
            for (int column = 0; column < this.columnCount; column++){
                CellValue value = model.getCellValue(row, column);
                if (value == CellValue.WALL) {
                    this.cellViews[row][column].setImage(this.wallImage);
                }
                else if (value == CellValue.BIGDOT) {
                    this.cellViews[row][column].setImage(this.bigDotImage);
                }
                else if (value == CellValue.SMALLDOT) {
                    this.cellViews[row][column].setImage(this.smallDotImage);
                }
                else {
                    this.cellViews[row][column].setImage(null);
                }
                if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && (PacmanModel.getLastDirection() == PacmanModel.Direction.RIGHT || PacmanModel.getLastDirection() == PacmanModel.Direction.NONE)) {
                    this.cellViews[row][column].setImage(this.pacmanRightImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacmanModel.getLastDirection() == PacmanModel.Direction.LEFT) {
                    this.cellViews[row][column].setImage(this.pacmanLeftImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacmanModel.getLastDirection() == PacmanModel.Direction.UP) {
                    this.cellViews[row][column].setImage(this.pacmanUpImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacmanModel.getLastDirection() == PacmanModel.Direction.DOWN) {
                    this.cellViews[row][column].setImage(this.pacmanDownImage);
                }
                if (PacmanModel.isGhostEatingMode() && (PacmanController.getGhostEatingModeCounter() == 6 || PacmanController.getGhostEatingModeCounter() == 4 || PacmanController.getGhostEatingModeCounter() == 2)) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                    if (row == model.getGhost3Location().getX() && column == model.getGhost3Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost3Image);
                    }
                    if (row == model.getGhost4Location().getX() && column == model.getGhost4Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost4Image);
                    }
                    
                }
                else if (PacmanModel.isGhostEatingMode()) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                    if (row == model.getGhost3Location().getX() && column == model.getGhost3Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                    if (row == model.getGhost4Location().getX() && column == model.getGhost4Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                     
                }
                else {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                    if (row == model.getGhost3Location().getX() && column == model.getGhost3Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost3Image);
                    }
                    if (row == model.getGhost4Location().getX() && column == model.getGhost4Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost4Image);
                    }
                     
                }
            }
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }
}
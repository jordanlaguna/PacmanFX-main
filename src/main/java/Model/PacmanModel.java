package Model;

import Controller.PacmanController;
import javafx.geometry.Point2D;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.util.*;


public class PacmanModel {

    public enum CellValue {
        EMPTY, SMALLDOT, BIGDOT, WALL, GHOST1HOME, GHOST2HOME, GHOST3HOME, GHOST4HOME, PACMANHOME
    }

    ;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    ;
    @FXML
    private int rowCount;
    @FXML
    private int columnCount;
    private CellValue[][] grid;
    private int score;
    private int level;
    private int dotCount;
    private static boolean gameOver;
    private static boolean youWon;
    private static boolean ghostEatingMode;
    private Point2D pacmanLocation;
    private Point2D pacmanVelocity;
    private Point2D ghost1Location;
    private Point2D ghost1Velocity;
    private Point2D ghost2Location;
    private Point2D ghost2Velocity;
    private Point2D ghost3Location;
    private Point2D ghost3Velocity;
    private Point2D ghost4Location;
    private Point2D ghost4Velocity;

    private static Direction lastDirection;
    private static Direction currentDirection;
    private int pacmanLives;

    public PacmanModel() {
        this.startNewGame();
        pacmanLives = 3;
    }

    MediaPlayer mediaPlayer;

    public void initializeLevel(String fileName) {

        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                columnCount++;
            }
            rowCount++;
        }
        columnCount = columnCount / rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new CellValue[rowCount][columnCount];
        int row = 0;
        int pacmanRow = 0;
        int pacmanColumn = 0;
        int ghost1Row = 0;
        int ghost1Column = 0;
        int ghost2Row = 0;
        int ghost2Column = 0;
        int ghost3Row = 0;
        int ghost3Column = 0;
        int ghost4Row = 0;
        int ghost4Column = 0;

        comio = 0;

        while (scanner2.hasNextLine()) {
            int column = 0;
            String line = scanner2.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String value = lineScanner.next();
                CellValue thisValue;
                if (value.equals("W")) {
                    thisValue = CellValue.WALL;
                } else if (value.equals("S")) {
                    thisValue = CellValue.SMALLDOT;
                    dotCount++;
                } else if (value.equals("B")) {
                    thisValue = CellValue.BIGDOT;
                    dotCount++;
                } else if (value.equals("1")) {
                    thisValue = CellValue.GHOST1HOME;
                    ghost1Row = row;
                    ghost1Column = column;
                } else if (value.equals("2")) {
                    thisValue = CellValue.GHOST2HOME;
                    ghost2Row = row;
                    ghost2Column = column;
                } else if (value.equals("3")) {
                    thisValue = CellValue.GHOST3HOME;
                    ghost3Row = row;
                    ghost3Column = column;
                } else if (value.equals("4")) {
                    thisValue = CellValue.GHOST4HOME;
                    ghost4Row = row;
                    ghost4Column = column;
                } else if (value.equals("P")) {
                    thisValue = CellValue.PACMANHOME;
                    pacmanRow = row;
                    pacmanColumn = column;
                } else {
                    thisValue = CellValue.EMPTY;
                }
                grid[row][column] = thisValue;
                column++;
            }
            row++;
        }
        pacmanLocation = new Point2D(pacmanRow, pacmanColumn);
        pacmanVelocity = new Point2D(0, 0);
        ghost1Location = new Point2D(ghost1Row, ghost1Column);
        ghost1Velocity = new Point2D(-1, 0);
        ghost2Location = new Point2D(ghost2Row, ghost2Column);
        ghost2Velocity = new Point2D(-1, 0);
        ghost3Location = new Point2D(ghost3Row, ghost3Column);
        ghost3Velocity = new Point2D(-1, 0);
        ghost4Location = new Point2D(ghost4Row, ghost4Column);
        ghost4Velocity = new Point2D(-1, 0);
        currentDirection = Direction.NONE;
        lastDirection = Direction.NONE;

        //int comio = 0;
    }

    public void startNewGame() {
        this.gameOver = false;
        this.youWon = false;
        this.ghostEatingMode = false;
        dotCount = 0;
        rowCount = 0;
        columnCount = 0;
        this.score = 0;
        this.level = 1;
        this.initializeLevel(PacmanController.getLevelFile(0));
    }

    public void startNextLevel() {
        if (this.isLevelComplete()) {
            this.level++;
            rowCount = 0;
            columnCount = 0;
            youWon = false;
            ghostEatingMode = false;
            try {
                this.initializeLevel(PacmanController.getLevelFile(level - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                youWon = true;
                gameOver = true;
                level--;
            }
        }
    }

    public void movePacman(Direction direction) {
        Point2D potentialPacmanVelocity = changeVelocity(direction);
        Point2D potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
        potentialPacmanLocation = setGoingOffscreenNewLocation(potentialPacmanLocation);
        if (direction.equals(lastDirection)) {
            if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                pacmanVelocity = changeVelocity(Direction.NONE);
                setLastDirection(Direction.NONE);
            } else {
                pacmanVelocity = potentialPacmanVelocity;
                pacmanLocation = potentialPacmanLocation;
            }
        } else {
            if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                potentialPacmanVelocity = changeVelocity(lastDirection);
                potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
                if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                    pacmanVelocity = changeVelocity(Direction.NONE);
                    setLastDirection(Direction.NONE);
                } else {
                    pacmanVelocity = changeVelocity(lastDirection);
                    pacmanLocation = pacmanLocation.add(pacmanVelocity);
                }
            } else {
                pacmanVelocity = potentialPacmanVelocity;
                pacmanLocation = potentialPacmanLocation;
                setLastDirection(direction);
            }
        }
    }

    public void moveGhosts() {
        Point2D[] ghost1Data = moveAGhost(ghost1Velocity, ghost1Location);
        Point2D[] ghost2Data = moveAGhost(ghost2Velocity, ghost2Location);
        Point2D[] ghost3Data = moveAGhost(ghost3Velocity, ghost3Location);
        Point2D[] ghost4Data = moveAGhost(ghost4Velocity, ghost4Location);
        ghost1Velocity = ghost1Data[0];
        ghost1Location = ghost1Data[1];
        ghost2Velocity = ghost2Data[0];
        ghost2Location = ghost2Data[1];
        ghost3Velocity = ghost3Data[0];
        ghost3Location = ghost3Data[1];
        ghost4Velocity = ghost4Data[0];
        ghost4Location = ghost4Data[1];

    }

    public Point2D[] moveAGhost(Point2D velocity, Point2D location) {
        Random generator = new Random();
        if (!ghostEatingMode) {
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(Direction.UP);
                } else {
                    velocity = changeVelocity(Direction.DOWN);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(Direction.LEFT);
                } else {
                    velocity = changeVelocity(Direction.RIGHT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else {
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        if (ghostEatingMode) {
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(Direction.DOWN);
                } else {
                    velocity = changeVelocity(Direction.UP);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(Direction.RIGHT);
                } else {
                    velocity = changeVelocity(Direction.LEFT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else {
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        Point2D[] data = {velocity, location};
        return data;

    }

    public Point2D setGoingOffscreenNewLocation(Point2D objectLocation) {
        if (objectLocation.getY() >= columnCount) {
            objectLocation = new Point2D(objectLocation.getX(), 0);
        }
        if (objectLocation.getY() < 0) {
            objectLocation = new Point2D(objectLocation.getX(), columnCount - 1);
        }
        return objectLocation;
    }

    public Direction intToDirection(int x) {
        if (x == 0) {
            return Direction.LEFT;
        } else if (x == 1) {
            return Direction.RIGHT;
        } else if (x == 2) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public void sendGhost1Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST1HOME) {
                    ghost1Location = new Point2D(row, column);
                }
            }
        }
        ghost1Velocity = new Point2D(-1, 0);
    }

    public void sendGhost2Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST2HOME) {
                    ghost2Location = new Point2D(row, column);
                }
            }
        }
        ghost2Velocity = new Point2D(-1, 0);
    }

    public void sendGhost3Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST3HOME) {
                    ghost3Location = new Point2D(row, column);
                }
            }
        }
        ghost3Velocity = new Point2D(-1, 0);
    }

    public void sendGhost4Home() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == CellValue.GHOST4HOME) {
                    ghost4Location = new Point2D(row, column);
                }
            }
        }
        ghost4Velocity = new Point2D(-1, 0);
    }

    int comio = 0;

    public void step(Direction direction) {
        if (pacmanLives > 0) {
            this.movePacman(direction);
            CellValue pacmanLocationCellValue = grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()];
            if (pacmanLocationCellValue == CellValue.SMALLDOT) {
                grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
                dotCount--;
                score += 10;
            }
            if (pacmanLocationCellValue == CellValue.BIGDOT) {
                grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
                dotCount--;
                score += 50;
                ghostEatingMode = true;
                PacmanController.setGhostEatingModeCounter();
            }
        }
        if (ghostEatingMode) {
            if (comio == 0) {
                if (pacmanLocation.equals(ghost1Location)) {
                    sendGhost1Home();
                    String musicFile = "pacman-eating-ghost.mp3";
                    AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                    mApplause.play();
                    score += 300;
                    comio++;
                }

                if (pacmanLocation.equals(ghost2Location)) {
                    sendGhost2Home();
                    String musicFile = "pacman-eating-ghost.mp3";
                    AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                    mApplause.play();
                    score += 300;
                    comio++;
                }
                if (pacmanLocation.equals(ghost3Location)) {
                    sendGhost3Home();
                    String musicFile = "pacman-eating-ghost.mp3";
                    AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                    mApplause.play();
                    score += 300;
                    comio++;
                }
                if (pacmanLocation.equals(ghost4Location)) {
                    sendGhost4Home();
                    String musicFile = "pacman-eating-ghost.mp3";
                    AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                    mApplause.play();
                    score += 300;
                    comio++;
                }
            }
            if (pacmanLocation.equals(ghost1Location)) {
                sendGhost1Home();
                String musicFile = "pacman-eating-ghost.mp3";
                AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                mApplause.play();
                score += 100;
            }
            if (pacmanLocation.equals(ghost2Location)) {
                sendGhost2Home();
                String musicFile = "pacman-eating-ghost.mp3";
                AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                mApplause.play();
                score += 100;
            }
            if (pacmanLocation.equals(ghost3Location)) {
                sendGhost3Home();
                String musicFile = "pacman-eating-ghost.mp3";
                AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                mApplause.play();
                score += 100;
            }
            if (pacmanLocation.equals(ghost4Location)) {
                sendGhost4Home();
                String musicFile = "pacman-eating-ghost.mp3";
                AudioClip mApplause = new AudioClip(Objects.requireNonNull(this.getClass().getResource("" + "/music/pacman-eating-ghost.mp3")).toExternalForm());
                mApplause.play();
                score += 100;
            }

        } else {
            if (pacmanLocation.equals(ghost1Location) || pacmanLocation.equals(ghost2Location) || pacmanLocation.equals(ghost3Location) || pacmanLocation.equals(ghost4Location)) {
                // Restar una vida y realizar las acciones necesarias (reubicar a Pac-Man, fantasmas, etc.)
                pacmanLives--;
                System.out.println("Una vida menos: " + pacmanLives);


                if (pacmanLocation.equals(ghost1Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost3Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost4Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }

            }
            if (ghostEatingMode) {
                if (pacmanLocation.equals(ghost1Location)) {
                    sendGhost1Home();
                    score += 100;
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    sendGhost2Home();
                    score += 100;
                }
                if (pacmanLocation.equals(ghost3Location)) {
                    sendGhost3Home();
                    score += 100;
                }
                if (pacmanLocation.equals(ghost4Location)) {
                    sendGhost4Home();
                    score += 100;
                }
            } else {
                if (pacmanLocation.equals(ghost1Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost2Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost3Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }
                if (pacmanLocation.equals(ghost4Location)) {
                    gameOver = true;
                    pacmanVelocity = new Point2D(0, 0);
                }

            }

        }
        this.movePacman(direction);
        this.moveGhosts();
        if (this.isLevelComplete()) {
            // Pac-Man ha perdido todas las vidas, aquí puedes realizar acciones relacionadas con el final del juego.
            gameOver = true;
            pacmanVelocity = new Point2D(0, 0);
            startNextLevel();

        } else {
            if (pacmanLives == 0) {

                System.out.println("Game Over");

                System.exit(0);
            }

        }
    }

    public Point2D changeVelocity(Direction direction) {
        if (direction == Direction.LEFT) {
            return new Point2D(0, -1);
        } else if (direction == Direction.RIGHT) {
            return new Point2D(0, 1);
        } else if (direction == Direction.UP) {
            return new Point2D(-1, 0);
        } else if (direction == Direction.DOWN) {
            return new Point2D(1, 0);
        } else {
            return new Point2D(0, 0);
        }
    }

    public static boolean isGhostEatingMode() {
        return ghostEatingMode;
    }

    public static void setGhostEatingMode(boolean ghostEatingModeBool) {
        ghostEatingMode = ghostEatingModeBool;
    }

    public static boolean isYouWon() {
        return youWon;
    }

    public boolean isLevelComplete() {
        return this.dotCount == 0;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public CellValue[][] getGrid() {
        return grid;
    }

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.grid.length && column >= 0 && column < this.grid[0].length;
        return this.grid[row][column];
    }

    public static Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
        currentDirection = direction;
    }

    public static Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction direction) {
        lastDirection = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int points) {
        this.score += points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDotCount() {
        return dotCount;
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Point2D getPacmanLocation() {
        return pacmanLocation;
    }

    public void setPacmanLocation(Point2D pacmanLocation) {
        this.pacmanLocation = pacmanLocation;
    }

    public Point2D getGhost1Location() {
        return ghost1Location;
    }

    public void setGhost1Location(Point2D ghost1Location) {
        this.ghost1Location = ghost1Location;
    }

    public Point2D getGhost3Location() {
        return ghost3Location;
    }

    public void setGhost3Location(Point2D ghost3Location) {
        this.ghost3Location = ghost3Location;
    }

    public Point2D getGhost4Location() {
        return ghost4Location;
    }

    public void setGhost4Location(Point2D ghost4Location) {
        this.ghost4Location = ghost4Location;
    }

    public Point2D getGhost2Location() {
        return ghost2Location;
    }

    public void setGhost2Location(Point2D ghost2Location) {
        this.ghost2Location = ghost2Location;
    }

    public Point2D getPacmanVelocity() {
        return pacmanVelocity;
    }

    public void setPacmanVelocity(Point2D velocity) {
        this.pacmanVelocity = velocity;
    }

    public Point2D getGhost1Velocity() {
        return ghost1Velocity;
    }

    public void setGhost1Velocity(Point2D ghost1Velocity) {
        this.ghost1Velocity = ghost1Velocity;
    }

    public Point2D getGhost3Velocity() {
        return ghost3Velocity;
    }

    public void setGhost3Velocity(Point2D ghost3Velocity) {
        this.ghost3Velocity = ghost3Velocity;
    }

    public Point2D getGhost4Velocity() {
        return ghost4Velocity;
    }

    public void setGhost4Velocity(Point2D ghost4Velocity) {
        this.ghost4Velocity = ghost4Velocity;
    }

}
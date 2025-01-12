package org.example.game.view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.game.HelloApplication;
import org.example.game.HelloController;
import org.example.game.controller.AIPlayer;
import org.example.game.controller.MovementsController;
import org.example.game.model.GameBoard;
import org.example.game.model.Movements;
import org.example.game.model.Tile;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BoardController {
    @FXML
    private GridPane gameGrid;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label result;
    @FXML
    private AnchorPane cover;
    @FXML
    private AnchorPane anc;
    @FXML
    private Button newGameButton;
    @FXML
    private Button redoBtn;
    @FXML
    private Button undoBtn;
    @FXML
    private Button res;
    private AIPlayer aiPlayer;
    @FXML
    private Label timerLabel;
    private Timeline moveTimer;
    private int timeLeft;


    private static GameBoard gameBoard;
    private MovementsController movementsC;
    private int rLimit;
    private int uLimit;
    private boolean playerTurn = true;
    @FXML
    private Button competitionModeBtn;
    @FXML
    private Button ai;
    @FXML
    private HBox btn;
    @FXML
    private StackPane togglePane;
    @FXML private Pane pane;
    @FXML private VBox op;
    @FXML private Button p;
    @FXML private ImageView vol;
    private Media main;
    private MediaPlayer mainMusic;
    private int AILimit = 0;
    private boolean compMode = false;
    private int m = 0;


    @FXML
    public void initialize() {
        vol.setVisible(false);
        main = new Media(Objects.requireNonNull(HelloApplication.class.getResource("soundEffects/8-bit-game-158815.mp3")).toExternalForm());
        mainMusic = new MediaPlayer(main);
        ai.setDisable(false);
        undoBtn.setVisible(true);
        redoBtn.setVisible(true);
        redoBtn.setDisable(true);
        undoBtn.setDisable(true);
        ai.setText("\uD83D\uDCA1");
        btn.getChildren().remove(timerLabel);
        competitionModeBtn.setText("competition mode: Off");
        btn.getChildren().remove(ai);
        btn.getChildren().add(ai);
        ai.setVisible(true);
        aiPlayer = new AIPlayer();
        rLimit = 0;
        uLimit = 0;
        gameBoard = new GameBoard(this);
        cover.setVisible(false);
        movementsC = new MovementsController(gameBoard);
        startGame();
        gameGrid.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    switch (event.getCode()) {
                        case LEFT, RIGHT, UP, DOWN -> handleKeyPress(event.getCode().toString());
                        default -> event.consume();
                    }
                });
            }
        });
        newGameButton.setOnAction(event -> {
            resetGame();
            cover.setVisible(false);
            redoBtn.setDisable(true);
            undoBtn.setDisable(true);
            rLimit = 0;
            uLimit = 0;
            gameGrid.setDisable(false);
            ai.setDisable(false);
            AILimit = 0;
        });
        res.setOnAction(event -> {
            resetGame();
            cover.setVisible(false);
            redoBtn.setDisable(false);
            undoBtn.setDisable(false);
        });

        Rectangle background = new Rectangle(40, 20, Color.GRAY);
        background.setArcWidth(20);
        background.setArcHeight(20);
        Circle toggleCircle = new Circle(10, Color.WHITE);
        toggleCircle.setTranslateX(-10);
        togglePane = new StackPane(background, toggleCircle);
        pane.getChildren().add(togglePane);
        togglePane.setOnMouseClicked(event -> {
            startCompetitionMode(toggleCircle, background);
        });
        redoBtn.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), redoBtn);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        redoBtn.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), redoBtn);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
        undoBtn.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), undoBtn);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        undoBtn.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), undoBtn);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
    }

    private boolean isOn = false;

    public StackPane createEmptyCell() {
        StackPane cell = new StackPane();
        cell.setPrefSize(60, 60);

        Rectangle backgroundTile = new Rectangle(60, 60, Color.rgb(205, 193, 180));
        backgroundTile.setArcWidth(15);
        backgroundTile.setArcHeight(15);

        cell.getChildren().add(backgroundTile);
        return cell;
    }


    private void startGame() {
        randomAdder();
        randomAdder();
        updateUI();
    }

    private void resetGame() {

        gameBoard.resetBoard();
        startGame();
    }

    public void randomAdder() {
        Random random = new Random();
        int value = random.nextInt(10) < 7 ? 2 : 4; // 70% chance for 2
        int row, col;
        do {
            row = random.nextInt(gameBoard.getSize());
            col = random.nextInt(gameBoard.getSize());
        } while (isFilled(row, col));

        addTile(value, row, col);
    }


    private boolean isFilled(int row, int col) {
        Tile current = gameBoard.getHead();
        while (current != null) {
            if (current.getRow() == row && current.getColumn() == col) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    private void addTile(int value, int row, int col) {
        Tile newTile = new Tile(value, col, row);
        if (gameBoard.getHead() == null) {
            gameBoard.setHead(newTile);
        } else {
            Tile current = gameBoard.getHead();
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newTile);
        }
    }

    public void updateUI() {
        gameGrid.getChildren().clear();
        for (int row = 0; row < gameBoard.getSize(); row++) {
            for (int col = 0; col < gameBoard.getSize(); col++) {
                gameGrid.add(createEmptyCell(), col, row);
            }
        }

        Tile current = gameBoard.getHead();
        while (current != null) {
            StackPane cell = new StackPane();
            cell.setPrefSize(60, 60);

            Rectangle tileRectangle = new Rectangle(60, 60, getTileColor(current.getValue()));
            tileRectangle.setArcWidth(15);
            tileRectangle.setArcHeight(15);

            Text text = new Text(String.valueOf(current.getValue()));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            text.setFill(current.getValue() <= 4 ? Color.rgb(119, 110, 101) : Color.WHITE);

            cell.getChildren().addAll(tileRectangle, text);
            gameGrid.add(cell, current.getColumn(), current.getRow());
            current = current.getNext();
        }

        int newScore = gameBoard.getScore();
        if (newScore > Integer.parseInt(scoreLabel.getText())) {
            highlightScoreLabel();
        }

        scoreLabel.setText(String.valueOf(newScore));
        System.out.println("UI updated.");
    }

    private void highlightScoreLabel() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), scoreLabel);
        scaleTransition.setByX(0.2);
        scaleTransition.setByY(0.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.setOnFinished(event -> scoreLabel.setTextFill(Color.WHITE));
        scaleTransition.play();
    }


    private Color getTileColor(int value) {
        return switch (value) {
            case 2 -> Color.rgb(238, 228, 218);
            case 4 -> Color.rgb(237, 224, 200);
            case 8 -> Color.rgb(242, 177, 121);
            case 16 -> Color.rgb(245, 149, 99);
            case 32 -> Color.rgb(246, 124, 95);
            case 64 -> Color.rgb(246, 94, 59);
            case 128 -> Color.rgb(237, 207, 114);
            case 256 -> Color.rgb(237, 204, 97);
            case 512 -> Color.rgb(237, 200, 80);
            case 1024 -> Color.rgb(237, 197, 63);
            case 2048 -> Color.rgb(237, 194, 46);
            default -> Color.rgb(205, 193, 180);
        };
    }

    public void handleKeyPress(String direction) {
        List<Movements> movements = null;
        is = !is;
        undoBtn.setDisable(false);
        gameBoard.saveState();

            switch (direction) {
                case "LEFT" -> movements = movementsC.moveLeft();
                case "RIGHT" -> movements = movementsC.moveRight();
                case "UP" -> movements = movementsC.moveUp();
                case "DOWN" -> movements = movementsC.moveDown();
            }


        if (movements != null && !movements.isEmpty()) {
            animateMovements(movements);
            gameBoard.addRandomTile();
            updateUI();
            if (gameBoard.hasWon()) {
                cover.setVisible(true);
                showCoverWithAnimation("YOU WON!", "New Game");
            }
            if (gameBoard.GameOver()) {
                gameGrid.setDisable(true);
                showCoverWithAnimation("Game over!", "Try again");
                cover.setVisible(true);
                result.setText("Game over!");
                res.setText("Try again");
                redoBtn.setDisable(true);
                undoBtn.setDisable(true);

            }
        }
    }

    private void animateMovements(List<Movements> movements) {
        ParallelTransition parallelTransition = new ParallelTransition();

        for (Movements movement : movements) {
            Tile tile = movement.getTile();
            int targetRow = movement.getTargetRow();
            int targetCol = movement.getTargetCol();

            StackPane tileUI = (StackPane) gameGrid.getChildren().filtered(node ->
                    GridPane.getRowIndex(node) == tile.getRow() && GridPane.getColumnIndex(node) == tile.getColumn()).get(0);

            double translateX = (targetCol - tile.getColumn()) * 70;
            double translateY = (targetRow - tile.getRow()) * 70;
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), tileUI);
            transition.setByX(translateX);
            transition.setByY(translateY);

            if (movement.isMerging()) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), tileUI);
                scaleTransition.setByX(0.2);
                scaleTransition.setByY(0.2);
                scaleTransition.setAutoReverse(true);
                scaleTransition.setCycleCount(2);

                SequentialTransition mergeEffect = new SequentialTransition(transition, scaleTransition);
                parallelTransition.getChildren().add(mergeEffect);
            } else {
                parallelTransition.getChildren().add(transition);
            }
            transition.setOnFinished(event -> {
                tile.setRow(targetRow);
                tile.setColumn(targetCol);
            });
        }
        parallelTransition.setOnFinished(event -> updateUI());
        parallelTransition.play();
    }


    @FXML
    public void handleUndo() {
        ++uLimit;
        if (uLimit < 6) {
            undoBtn.setDisable(false);
            if (rLimit < 5)
                redoBtn.setDisable(false);
            if (gameBoard.undo()) {
                updateUI();
                System.out.println("Undo stack size: ");
                System.out.println("Redo stack size: ");
            }
        } else
            undoBtn.setDisable(true);

    }

    @FXML
    public void handleRedo() {
        ++rLimit;
        if (rLimit < 6) {
            redoBtn.setDisable(false);
            if (gameBoard.redo()) {
                updateUI();

                System.out.println("Undo stack size: ");
                System.out.println("Redo stack size: ");
            } else {
                redoBtn.setDisable(true);
                System.out.println("No more undo available.");
            }
        } else
            redoBtn.setDisable(true);

    }

    @FXML
    public void AIPlayer() {
        if(AILimit < 10) {
            char bestMove = aiPlayer.findBestMove(gameBoard, 3);
            aiPlayer.won = false;
            if (aiPlayer.won)
                showCoverWithAnimation("YOU WON!", "New Game");
            if (bestMove != 'n') {
                System.out.println("AI chose: " + bestMove);
                switch (bestMove) {
                    case 'U' -> movementsC.moveUp();
                    case 'D' -> movementsC.moveDown();
                    case 'L' -> movementsC.moveLeft();
                    case 'R' -> movementsC.moveRight();
                }
                gameBoard.addRandomTile();
                updateUI();
            } else {
                gameGrid.setDisable(true);
                cover.setVisible(true);
                result.setText("Game over!");
                res.setText("Try again");
                showCoverWithAnimation("Game over!", "Try again");
                redoBtn.setDisable(true);
                undoBtn.setDisable(true);
            }
        }
        else ai.setDisable(true);
        ++AILimit;
    }

    private void showCoverWithAnimation(String message, String buttonText) {
        result.setText(message);
        res.setText(buttonText);

        cover.setVisible(true);
        cover.setOpacity(0);
        cover.setScaleX(0.8);
        cover.setScaleY(0.8);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), cover);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(300), cover);
        zoomIn.setFromX(0.8);
        zoomIn.setFromY(0.8);
        zoomIn.setToX(1);
        zoomIn.setToY(1);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, zoomIn);
        parallelTransition.play();
    }

    boolean is;

    private void startCompetitionMode(Circle toggleCircle, Rectangle background) {
        mainMusic.setVolume(0.3);
        mainMusic = new MediaPlayer(main);
        mainMusic.setVolume(0.3);
        mainMusic.setOnEndOfMedia(()->
        {
            mainMusic.seek(Duration.seconds(0));;
        });
        isOn = !isOn;
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), toggleCircle);
        transition.setToX(isOn ? 10 : -10);
        transition.play();
        background.setFill(isOn ? Color.rgb(196, 181, 170) : Color.GRAY);
        if (isOn) {
            competitionModeBtn.setText("competition mode: On");
            undoBtn.setVisible(false);
            redoBtn.setVisible(false);
            btn.getChildren().remove(ai);
            btn.getChildren().remove(timerLabel);
            btn.getChildren().add(timerLabel);
            resetGame();
            playerTurn = true;
            new Thread(() -> {
                mainMusic.play();
                while (isOn && !gameBoard.GameOver()) {
                    if (playerTurn) {
                        vol.setVisible(true);
                        Platform.runLater(() -> timerLabel.setText("Your Turn: \uD83D\uDD52 5s"));
                        is = false;

                        try {
                            for (int i = 5; i > 0; i--) {
                                if (!isOn) return;
                                int timeLeft = i;
                                Platform.runLater(() -> timerLabel.setText("Your Turn: \uD83D\uDD52 " + timeLeft + "s"));
                                if (is) {
                                    break;
                                }
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        playerTurn = false;
                    } else {
                        Platform.runLater(() -> timerLabel.setText("AI is Thinking..."));
                        char bestMove = aiPlayer.findBestMove(gameBoard, 3);

                        if (bestMove != 'n') {
                            switch (bestMove) {
                                case 'U' -> movementsC.moveUp();
                                case 'D' -> movementsC.moveDown();
                                case 'L' -> movementsC.moveLeft();
                                case 'R' -> movementsC.moveRight();
                            }

                            Platform.runLater(() -> {
                                gameBoard.addRandomTile();
                                updateUI();
                            });
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }

                        playerTurn = true;
                    }
                }

                if (isOn) {
                    Platform.runLater(() -> {
                        if (gameBoard.getScore() >= 2048) {
                            showCoverWithAnimation("You Won!", "Play Again");
                        } else {
                            showCoverWithAnimation("You Lost!", "Try Again");
                        }
                    });
                }
            }).start();
        } else {
            vol.setVisible(false);
            initialize();
        }
    }
    private boolean o = true;
    public void plus(MouseEvent event) {
        mainMusic.stop();
    }
    private boolean vo = false;

    public void vol(MouseEvent event) {
        if(!vo)
        {
            Image artistPic = new Image(Objects.requireNonNull(HelloController.class.getResource("images/img_3.png")).toExternalForm());
            vol.setImage(artistPic);
            mainMusic.setMute(true);
        } else
        {
            Image artistPic = new Image(Objects.requireNonNull(HelloController.class.getResource("images/img_2.png")).toExternalForm());
            vol.setImage(artistPic);
            mainMusic.setMute(false);
        }

        vo = !vo;

    }
}

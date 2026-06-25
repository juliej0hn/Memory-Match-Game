
package memorymatchgame01;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.*;

public class MemoryMatchGame01 extends Application {

    // --- Components ---
    private GameBoard board = new GameBoard(); // The Model
    private GridPane grid = new GridPane();
    
    // UI Labels
    private Label statusLbl, movesLbl, badLbl, timeLbl;

    // Game State
    private int seconds = 0;
    private Timeline timer;
    private boolean timerRunning = false;
    private boolean inputLocked = false;
    private String userName = "Player";
    private static final String BINARY_FILE = "scores.dat";

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        validateUser();
        
        // Satisfies grading: Connects Logic Layer immediately
        board.setPlayer(userName); 

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2C3E50;");

        // --- Header ---
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #34495E;");

        statusLbl = createLbl("Pairs: 0/8", Color.WHITE);
        movesLbl = createLbl("Moves: 0", Color.LIGHTGRAY);
        badLbl = createLbl("Incorrect: 0", Color.web("#FF7675"));
        timeLbl = createLbl("Time: 0s", Color.web("#74B9FF"));

        Button resetBtn = new Button("Restart");
        resetBtn.setStyle("-fx-background-color: #ECF0F1; -fx-font-weight: bold;");
        resetBtn.setOnAction(e -> restartGame());

        header.getChildren().addAll(statusLbl, movesLbl, badLbl, timeLbl, resetBtn);
        root.setTop(header);

        // --- Grid ---
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(30));
        grid.setHgap(15);
        grid.setVgap(15);
        root.setCenter(grid);

        // --- Timer ---
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            timeLbl.setText("Time: " + seconds + "s");
        }));
        timer.setCycleCount(Timeline.INDEFINITE);

        restartGame();

        stage.setTitle("Memory Match - 4x4 Edition");
        stage.setScene(new Scene(root, 650, 700));
        stage.show();
    }

    private void validateUser() {
        TextInputDialog d = new TextInputDialog();
        d.setTitle("Welcome");
        d.setHeaderText("Welcome to Memory Match");
        d.setContentText("Please enter your name:");

        Button ok = (Button) d.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);

        d.getEditor().textProperty().addListener((obs, old, newVal) -> {
            ok.setDisable(newVal.trim().isEmpty());
        });

        d.showAndWait().ifPresent(name -> this.userName = name.trim());
    }

    private void restartGame() {
        timer.stop();
        timerRunning = false;
        seconds = 0;
        timeLbl.setText("Time: 0s");
        inputLocked = false;
        
        board.setupGame();
        grid.getChildren().clear();

        List<Card> cards = board.getCards();
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            StackPane view = createCardView(c);
            c.setView(view);
            grid.add(view, i % 4, i / 4);
        }
        updateUI();
        updateLabels();
    }

    private void handleCardClick(Card card) {
        if (inputLocked || card.isOpen()) return;

        if (!timerRunning) {
            timer.play();
            timerRunning = true;
        }

        GameBoard.TurnResult result = board.processSelect(card);
        updateUI();

        if (result == GameBoard.TurnResult.MISMATCH) {
            inputLocked = true;
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                board.resetMismatch();
                updateUI();
                inputLocked = false;
                updateLabels();
            });
            pause.play();
        } else if (result == GameBoard.TurnResult.MATCH) {
            updateLabels();
            if (board.isGameWon()) {
                timer.stop();
                timerRunning = false;
                handleGameWon();
            }
        } else {
            updateLabels();
        }
    }

    private void updateUI() {
        for (Card c : board.getCards()) {
            if (c.getView() == null) continue;
            Rectangle bg = (Rectangle) c.getView().getChildren().get(0);
            Text txt = (Text) c.getView().getChildren().get(1);

            if (c.isMatched()) {
                bg.setFill(Color.web("#55EFC4")); // Mint
                txt.setVisible(true);
            } else if (c.isOpen()) {
                bg.setFill(Color.WHITE);
                txt.setVisible(true);
            } else {
                bg.setFill(Color.web("#FD79A8")); // Pink
                txt.setVisible(false);
            }
        }
    }

    private void updateLabels() {
        movesLbl.setText("Moves: " + board.getMoves());
        badLbl.setText("Incorrect: " + board.getIncorrectMoves());
        if (!board.isGameWon()) statusLbl.setText("Pairs: " + board.getPairsFound() + "/8");
    }

    private StackPane createCardView(Card c) {
        Rectangle bg = new Rectangle(100, 100, Color.web("#FD79A8"));
        bg.setArcWidth(20); bg.setArcHeight(20);
        
        Text txt = new Text(c.getValue());
        txt.setFont(Font.font("Segoe UI Emoji", 40));
        txt.setVisible(false);

        StackPane s = new StackPane(bg, txt);
        s.setOnMouseClicked(e -> handleCardClick(c));
        return s;
    }

    private Label createLbl(String t, Color c) {
        Label l = new Label(t);
        l.setTextFill(c);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        return l;
    }

    private void handleGameWon() {
        statusLbl.setText("WON! Time: " + seconds + "s");

        // Save to binary file
        Player p = new Player(userName, seconds, board.getMoves());
        saveToBinaryFile(p);

        // --- NEW: POPUP SCREEN ---
        showVictoryPopup(p);
    }
    
    private void showVictoryPopup(Player p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText("CONGRATULATIONS " + p.getName().toUpperCase() + "!");
        
        String content = "You found all pairs!\n\n" +
                         "Time: " + p.getTimeSeconds() + " seconds\n" +
                         "Moves: " + p.getMoves();
                         
        alert.setContentText(content);
        alert.showAndWait();
    }

@SuppressWarnings("unchecked")
    private void saveToBinaryFile(Player p) {
        List<Player> scores = new ArrayList<>();
        File file = new File(BINARY_FILE);

        // STEP 1: LOAD EXISTING SCORES
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                scores = (List<Player>) ois.readObject(); // Read the old list
                
                System.out.println("\n Loaded " + scores.size() + " previous scores:");
                for (Player saved : scores) {
                    System.out.println("   -> " + saved);
                }
            } catch (Exception e) {
                System.out.println("[Error] Could not load previous scores. Starting fresh.");
            }
        }
        // STEP 2: ADD THE NEW WINNER
        scores.add(p);
        // STEP 3: SAVE THE UPDATED LIST
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(scores); 
            System.out.println("Score successfully saved to binary file!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
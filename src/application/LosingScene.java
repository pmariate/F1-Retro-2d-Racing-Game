package application;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class LosingScene extends Application {
    private static final int SCENE_WIDTH = 1950;
    private static final int SCENE_HEIGHT = 990;
    private static final double BUTTON_X = 1000;
   // private static final double START_BUTTON_Y = -150;
    private static final double HOW_TO_PLAY_BUTTON_Y = 420;
    private static final double DEVELOPERS_BUTTON_Y = 600;

    private MainMenu menu;
    private Scene scene;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    private Image lose;
    private MediaPlayer mediaPlayer;
    //private ImageView startButtonImageView;
    private ImageView howToPlayButtonImageView;
    private ImageView developersButtonImageView;
    private Stage primaryStage;

    public LosingScene() {
        this.root = new Group();
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        this.canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.lose = new Image("images/lose.png", SCENE_WIDTH, SCENE_HEIGHT, false, false);

        // Initialize button image views
        //this.startButtonImageView = createButton("images/start.png", 450, -150);
        howToPlayButtonImageView = createButton("images/howToPlay.png", BUTTON_X, HOW_TO_PLAY_BUTTON_Y);
        developersButtonImageView = createButton("images/about.png", BUTTON_X, DEVELOPERS_BUTTON_Y);

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Assign primaryStage to the field
        this.primaryStage.setTitle("Developers");
        this.addComponents();
        this.setupMusic();
        this.setupMenuButton();

        // Set up event handlers for buttons
        howToPlayButtonImageView.setOnMouseClicked(event -> launchHowToPlay());
        developersButtonImageView.setOnMouseClicked(event -> launchDevelopers());

        root.getChildren().addAll(howToPlayButtonImageView, developersButtonImageView);

        this.primaryStage.setScene(this.scene);
        this.primaryStage.show();
    }

    private ImageView createButton(String imagePath, double x, double y) {
        ImageView buttonImageView = loadImageView(imagePath);
        buttonImageView.setFitWidth(650);
        buttonImageView.setFitHeight(370);
        buttonImageView.setTranslateX(x);
        buttonImageView.setTranslateY(y);
        return buttonImageView;
    }

    private ImageView loadImageView(String imagePath) {
        Image buttonImage = new Image(imagePath);
        return new ImageView(buttonImage);
    }

    private void addComponents() {
        this.gc.drawImage(this.lose, 0, 0); // background image
    }

    private void setupMusic() {
        String musicFile = "sounds/devSound.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        this.mediaPlayer.setVolume(0.5);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
    }

    private void setupMenuButton() {
        Image menuButton = new Image("images/backArrow.png");
        ImageView menuButtonImageView = new ImageView(menuButton);
        menuButtonImageView.setFitWidth(200);
        menuButtonImageView.setFitHeight(140);
        menuButtonImageView.setTranslateX(10);
        menuButtonImageView.setTranslateY(5);

        this.root.getChildren().add(canvas);
        root.getChildren().add(menuButtonImageView);

        menuButtonImageView.setOnMouseClicked(event -> launchMainMenu());
        howToPlayButtonImageView.setOnMouseClicked(event -> launchHowToPlay());
        developersButtonImageView.setOnMouseClicked(event -> launchDevelopers());
    }

    private void launchMainMenu() {
        this.mediaPlayer.stop();
        MainMenu menu = new MainMenu();
        menu.start(primaryStage);
    }

    private void launchHowToPlay() {
        this.mediaPlayer.stop();
        HowToPlay howToPlay = new HowToPlay(this.menu);
        howToPlay.start(primaryStage);
    }

    private void launchDevelopers() {
        this.mediaPlayer.stop();
        Developers developers = new Developers(this.menu);
        developers.start(primaryStage);
    }
}

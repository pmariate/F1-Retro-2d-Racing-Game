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

/**
 * The HowToPlay class represents the JavaFX application for displaying instructions on how to play.
 * It includes a background image, a back-to-menu button, and background music.
 */
public class HowToPlay extends Application {
    private static final int SCENE_WIDTH = 1950;
    private static final int SCENE_HEIGHT = 990;

    private MainMenu menu;
    private Stage primaryStage;
    private Scene scene;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    private Image devs;
    private MediaPlayer mediaPlayer;

    /**
     * Constructs a new instance of HowToPlay, initializing the scene, canvas, graphics context,
     * background image, and other necessary components.
     * @param menu
     */
    public HowToPlay(MainMenu menu) {
    	this.menu = menu;
        this.root = new Group();
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        this.canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.devs = new Image("images/instructions.png", SCENE_WIDTH, SCENE_HEIGHT, false, false);
    }

    /**
     * The entry point for launching the JavaFX application.
     * Initializes the primary stage, sets the title, adds components, and displays the stage.
     * @param primaryStage The main stage of the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("How To Play");
    	this.primaryStage.setMaximized(true);
        this.addComponents();
        this.setupMenuButton();
        this.setupBackgroundMusic();

        this.primaryStage.setScene(this.scene);
        this.primaryStage.show();
    }

    /**
     * Adds visual components, such as the background image, to the scene.
     */
    private void addComponents() {
        this.gc.drawImage(this.devs, 0, 0);
    }

    /**
     * Sets up the back-to-menu button with its image and click event handler.
     */
    private void setupMenuButton() {
        Image menuButton = new Image("images/backArrow.png");
        ImageView menuButtonImageView = new ImageView(menuButton);
        menuButtonImageView.setFitWidth(200);
        menuButtonImageView.setFitHeight(140);
        menuButtonImageView.setTranslateX(10);
        menuButtonImageView.setTranslateY(5);

        this.root.getChildren().add(canvas);
        root.getChildren().add(menuButtonImageView);

        menuButtonImageView.setOnMouseClicked(event -> launchMenu());
    }

    /**
     * Configures and plays the background music associated with the HowToPlay screen.
     */
    private void setupBackgroundMusic() {
        String musicFile = "sounds/devSound.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        this.mediaPlayer.setVolume(0.5);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
    }

    /**
     * Stops the background music and launches the main menu screen.
     */
    private void launchMenu() {
    	this.mediaPlayer.stop();
        MainMenu menu = new MainMenu();
        menu.start(primaryStage);
    }
}

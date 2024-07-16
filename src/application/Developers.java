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

public class Developers extends Application {
    private static final int SCENE_WIDTH = 1950;
    private static final int SCENE_HEIGHT = 990;

    private MainMenu menu;
    private Stage primaryStage;
    private Scene scene;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    private Image developers;
    private MediaPlayer mediaPlayer;

    public Developers(MainMenu menu) {
    	this.menu = menu;
        this.root = new Group();
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        this.canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.developers = new Image("images/developers.png", SCENE_WIDTH, SCENE_HEIGHT, false, false);
    }

    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("Developers");
    	this.primaryStage.setMaximized(true);
        this.addComponents();
        this.setupMusic();
        this.setupMenuButton();

        this.primaryStage.setScene(this.scene);
        this.primaryStage.show();
    }

    private void addComponents() {
        this.gc.drawImage(this.developers, 0, 0); // background image
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

        menuButtonImageView.setOnMouseClicked(event -> launchMenu());
    }

    private void launchMenu() {
    	this.mediaPlayer.stop();
        MainMenu menu = new MainMenu();
        menu.start(primaryStage);
    }
}

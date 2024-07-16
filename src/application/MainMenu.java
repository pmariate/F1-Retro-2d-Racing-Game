package application;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MainMenu extends Application {

    private static final int SCENE_WIDTH = 1950;
    private static final int SCENE_HEIGHT = 990;
    private static final double BUTTON_X = 450;
    private static final double START_BUTTON_Y = -150;
    private static final double HOW_TO_PLAY_BUTTON_Y = 0;
    private static final double DEVELOPERS_BUTTON_Y = 160;

    private Stage primaryStage;
    private ToggleButton musicToggleButton;
    private ImageView musicPlayImageView;
    private ImageView musicPauseImageView;
    private ImageView startButtonImageView;
    private ImageView howToPlayButtonImageView;
    private ImageView developersButtonImageView;
    private Scene scene;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("F1 Retro");
    	this.primaryStage.setMaximized(true);
        StackPane root = new StackPane();
        scene = createScene(root);

        startButtonImageView.setOnMouseClicked(event -> launchGameProper());
        howToPlayButtonImageView.setOnMouseClicked(event -> launchHowToPlay());
        developersButtonImageView.setOnMouseClicked(event -> launchDevelopers());

        musicToggleButton.setOnAction(event -> handleMusicToggle());
        this.primaryStage.setOnCloseRequest(event -> this.mediaPlayer.stop());

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    private Scene createScene(StackPane root) {
        root.getChildren().add(loadBackgroundImage());

        startButtonImageView = createButton("images/start.png", BUTTON_X, START_BUTTON_Y);
        howToPlayButtonImageView = createButton("images/howToPlay.png", BUTTON_X, HOW_TO_PLAY_BUTTON_Y);
        developersButtonImageView = createButton("images/about.png", BUTTON_X, DEVELOPERS_BUTTON_Y);

        double iconWidth = 100;
        double iconHeight = 80;

        Image playIcon = new Image("images/play.png");
        musicPlayImageView = new ImageView(playIcon);

        Image pauseIcon = new Image("images/pause.png");
        musicPauseImageView = new ImageView(pauseIcon);

        musicPlayImageView.setFitWidth(iconWidth);
        musicPlayImageView.setFitHeight(iconHeight);
        musicPlayImageView.setTranslateX(0);
        musicPlayImageView.setTranslateY(0);

        musicPauseImageView.setFitWidth(iconWidth);
        musicPauseImageView.setFitHeight(iconHeight);
        musicPauseImageView.setTranslateX(0);
        musicPauseImageView.setTranslateY(0);

        musicToggleButton = new ToggleButton();
        musicToggleButton.setGraphic(musicPlayImageView);
        musicToggleButton.setTranslateX(-900);
        musicToggleButton.setTranslateY(-440);

        root.getChildren().addAll(startButtonImageView, howToPlayButtonImageView,
                developersButtonImageView, musicToggleButton);

        loadAndPlayBackgroundMusic();

        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private ImageView createButton(String imagePath, double x, double y) {
        ImageView buttonImageView = loadImageView(imagePath);
        buttonImageView.setFitWidth(600);
        buttonImageView.setFitHeight(320);
        buttonImageView.setTranslateX(x);
        buttonImageView.setTranslateY(y);
        return buttonImageView;
    }

    private ImageView loadImageView(String imagePath) {
        Image buttonImage = new Image(imagePath);
        return new ImageView(buttonImage);
    }

    private ImageView loadBackgroundImage() {
        return loadImageView("images/bg.png");
    }

    private void loadAndPlayBackgroundMusic() {
        String musicFile = "sounds/racing.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        this.mediaPlayer.setVolume(1.0);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
    }

    private void handleMusicToggle() {
        if (musicToggleButton.isSelected()) {
            this.mediaPlayer.play();
            musicToggleButton.setGraphic(musicPlayImageView);
        } else {
            this.mediaPlayer.pause();
            musicToggleButton.setGraphic(musicPauseImageView);
        }
    }

    private void launchGameProper() {
        this.mediaPlayer.stop();
        GameTimer game = new GameTimer(this);
        this.primaryStage.hide();
        game.startGame();
    }

    void launchWinningScene() {
        this.mediaPlayer.stop();
        WinningScene winningScene = new WinningScene(this);
        winningScene.start(this.primaryStage);
    }

    private void launchHowToPlay() {
        this.mediaPlayer.stop();
        HowToPlay howToPlay = new HowToPlay(this);
        howToPlay.start(this.primaryStage);
    }


    private void launchDevelopers() {
        this.mediaPlayer.stop();
        Developers developers = new Developers(this);
        developers.start(this.primaryStage);
    }

    void exitGame()
    {
    	this.primaryStage.close();
    }

	public void launchLosingScene() {
		this.mediaPlayer.stop();
        LosingScene losingScene = new LosingScene();
        losingScene.start(this.primaryStage);
	}
}

package application;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
	private MainMenu menu;
	private GameProper game;

	public GameTimer(MainMenu menu) {
		this.menu = menu;
		this.game = new GameProper(menu);

	}
	public void startGame()
	{
		this.game.startGame();
	}
	@Override
	public void handle(long currentNanoTime) {


	}

}

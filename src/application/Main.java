/********************************************************************
 *
 *
 * Description:
 *
 * CMSC 22 Game Project: F1 Retro
 *
 *
 *
 * (c) Institute of Computer Science, CAS, UPLB
 * @author Ariate, Princess Joy | Berana, Christian | Diocadiz, Gabrielle Therese
 * @created_date 2023-12-10 17:00
 *
 ********************************************************************/

package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		MainMenu menu = new MainMenu();
		menu.start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

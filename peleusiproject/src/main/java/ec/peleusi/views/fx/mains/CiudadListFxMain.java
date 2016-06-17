package ec.peleusi.views.fx.mains;

import java.io.IOException;

import ec.peleusi.views.fx.controllers.CiudadListFxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CiudadListFxMain extends Application {
	private static Parent parent;
	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) throws IOException {
		primaryStage=stage;
		primaryStage.setTitle("Lista de Ciudades");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../designs/CiudadListFx.fxml"));
		loader.setController(new CiudadListFxController());
		parent = (Parent) loader.load();

		
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
		/*
		 * FadeTransition ft = new FadeTransition(Duration.millis(900), parent);
		 * ft.setFromValue(0.0); ft.setToValue(0.97); ft.play();
		 */

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}

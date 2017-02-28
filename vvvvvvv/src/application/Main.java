package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;


public class Main extends Application {

	AnchorPane root;
	Scene scene;
	Stage stage;

	@FXML
	private AnchorPane pane;

	@FXML
	private Label lab;
	@Override
	public void start(Stage primaryStage) {
		try {


			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource( "MainForm.fxml"));
			root = (AnchorPane) loader.load();
			scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage = primaryStage;
			stage.setScene(scene);
			stage.show();
			System.out.println("Iniciou");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void sayhello(){
		System.out.println("OLAA");

		Circle cicle = new Circle();
		cicle.setRadius(40);
		cicle.setLayoutX(200);
		cicle.setLayoutY(200);
		//Adicionar uma bola ao anchorPane
		lab.setText("OPA");
		pane.getChildren().add(cicle);
	}

	@FXML
	private void moverBola()
	{
		//Mover a forma
		int index = pane.getChildren().size()-1;
		pane.getChildren().get(index).setLayoutX(pane.getChildren().get(index).getLayoutX()+10);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

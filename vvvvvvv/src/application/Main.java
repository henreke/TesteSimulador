package application;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	AnchorPane root;
	Scene scene;
	Stage stage;
	Circle cicle;

	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;


	//Parte do jBox2D
	//BouncyBall ball = new BouncyBall(45,90, Utility.BALL_RADIUS, Color.ALICEBLUE);
    BouncyBall ball = new BouncyBall(Utility.toPosX(300),Utility.toPosX(300), Utility.BALL_RADIUS, Color.ALICEBLUE);

	final Timeline timeline =  new Timeline();

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
			scene = new Scene(root,600,600);
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

		cicle = new Circle();
		Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
		LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		cicle.setRadius(Utility.toPixelHeight(8));
		cicle.setLayoutX(200);
		cicle.setLayoutY(200);
		cicle.setFill(lg1);


		cicle.setCursor(Cursor.HAND);
		cicle.setOnMousePressed(circleOnMousePressedEventHandler);
		cicle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		cicle.setOnMouseReleased(circleOnFim);


		//Funciona em parte
		/*
		cicle.setOnMouseMoved(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub

				//cicle.setCenterX(((MouseEvent)event).getX());
	        	//cicle.setCenterY(((MouseEvent)event).getY());

	        	System.out.println(Utility.world.getBodyCount());
	        	System.out.println(((MouseEvent)event).getSceneY());
	        	//Isso funciona
	        	//ball.body.setLinearVelocity(new Vec2(-100,0));

	        	Utility.world.destroyBody(ball.body);
	        	ball.bd.position.set( Utility.toPosX((float)((MouseEvent)event).getSceneX()),Utility.toPosX((float) ((MouseEvent)event).getSceneY()));
	        	ball.body = Utility.world.createBody(ball.bd);
	        	ball.body.createFixture(ball.fd);
	        	ball.node.setUserData(ball.body);
	        	//ball.node.setLayoutX(((MouseEvent)event).getX());
	        	//ball.node.setLayoutY(((MouseEvent)event).getY());
			}
		});*/
		//Adicionar uma bola ao anchorPane
		pane.getChildren().add(cicle);

		//Parte do jbox2D
		Utility.addGround(200, 10);

		Utility.addWall(0, 400, 1, 400);
        Utility.addWall(Utility.toPosX(400), 400, 1, 400);

        int angulo = 20;
        float angulorad = (float) (angulo*Math.PI/180f);
        System.out.println(angulorad);

		Utility.addRampa(0, Utility.toPosY(300), 400, 10,angulorad);

		Rectangle rampa = new Rectangle(400, 10);

		//rampa.setRotate(angulo);
		rampa.setLayoutX(0);
		rampa.setLayoutY(310);
		////Rotate(angulo negativo,valor de x em relacao a origem do corpo, y, z)
		rampa.getTransforms().add(new Rotate(-angulo,0,0,0));
		pane.getChildren().add(rampa);


		timeline.setCycleCount(Timeline.INDEFINITE);
	    Duration duration = Duration.seconds(1.0/60.0); //duracao do tempo quadro

	    EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                        //Create time step. Set Iteration count 8 for velocity and 3 for positions
                        Utility.world.step(1.0f/60.f, 8, 3);

                        //Move balls to the new position computed by JBox2D
                        Body body = (Body)ball.node.getUserData();
                        float teste = ball.body.getPosition().x;
                        //body2.getPosition().x
                        float xpos = Utility.toPixelPosX(body.getPosition().x);
                        float ypos = Utility.toPixelPosY(body.getPosition().y);
                        float angulo2 = body.getAngle();
                        //ball.node.setLayoutX(xpos);
                        //ball.node.setLayoutY(ypos);
                        cicle.setLayoutX( Utility.toPixelPosX(teste));
                        cicle.setLayoutY(Utility.toPixelPosY(ball.body.getPosition().y));
                        cicle.setRotate(Utility.tograus(-angulo2));
           }
        };

        KeyFrame frame = new KeyFrame(duration, ae, null,null);

        timeline.getKeyFrames().add(frame);
        timeline.playFromStart();
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

	EventHandler<MouseEvent> circleOnMousePressedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            orgSceneX = t.getSceneX();
		            orgSceneY = t.getSceneY();
		            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
		            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
		        }
		    };

	  EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            double offsetX = t.getSceneX() - orgSceneX;
		            double offsetY = t.getSceneY() - orgSceneY;
		            double newTranslateX = orgTranslateX + offsetX;
		            double newTranslateY = orgTranslateY + offsetY;

		            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
		            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
		        }
		    };

	EventHandler<MouseEvent> circleOnFim = new EventHandler<MouseEvent>(){
		@Override
        public void handle(MouseEvent t) {
			System.out.println("Fim");
			 double offsetX = t.getSceneX() - orgSceneX;
	            double offsetY = t.getSceneY() - orgSceneY;
	            double newTranslateX = orgTranslateX + offsetX;
	            double newTranslateY = orgTranslateY + offsetY;

	            System.out.println(cicle.getLayoutX());
	            System.out.println(cicle.getLayoutY());
			Utility.world.destroyBody(ball.body);

        	//ball.bd.position.set( Utility.toPosX((float)cicle.getLayoutX()),Utility.toPosY((float)cicle.getLayoutY())+15);
			ball.bd.position.set(45,90);
        	ball.body = Utility.world.createBody(ball.bd);
        	ball.body.createFixture(ball.fd);
        	ball.node.setUserData(ball.body);
		}

	};
}

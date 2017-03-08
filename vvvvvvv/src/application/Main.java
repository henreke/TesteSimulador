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
	World2 world;
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    boolean pare = false;
	//Parte do jBox2D
	//BouncyBall ball = new BouncyBall(45,90, Utility.BALL_RADIUS, Color.ALICEBLUE);
    BouncyBall ball = new BouncyBall(45,90, Utility.BALL_RADIUS, Color.ALICEBLUE);

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
			this.world = new World2(new Vec2(0.0f,-10.0f));
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

                        //body2.getPosition().x
                        float xpos = Utility.toPixelPosX(body.getPosition().x);
                        float ypos = Utility.toPixelPosY(body.getPosition().y);
                        float angulo2 = body.getAngle();
                        //ball.node.setLayoutX(xpos);
                        //ball.node.setLayoutY(ypos);

                        //Atualizacao do layout

                        Vec2 nova = ball.body.getPosition();
                        cicle.setLayoutX(Utility.toPixelPosX(nova.x));
                        cicle.setLayoutY(Utility.toPixelPosY(nova.y));
                        cicle.setRotate(Utility.tograus(-angulo2));


                        if (pare){
                        	timeline.stop();


                	        System.out.println("Posicao atual");
                	        System.out.println( Utility.toPixelPosX(nova.x));
                	        System.out.println( cicle);
                	        System.out.println( Utility.toPixelPosY(nova.y));



                        }
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
		//int index = pane.getChildren().size()-1;
		//pane.getChildren().get(index).setLayoutX(pane.getChildren().get(index).getLayoutX()+10);

		if (this.world == null)
			this.world = new World2(new Vec2(0.0f,-10.0f));
		int index = this.world.CriarCirculo(8, 200, 20, Color.RED);
		pane.getChildren().add(world.bodys.get(index).shape);





	}

	@FXML
	private void addRampa(){

		if (this.world == null)
			this.world = new World2(new Vec2(0.0f,-10.0f));

		int angulo = 20;
		int index = 0;
		//index = this.world.addRampa(0, 300, 400, 10,angulo);
		//pane.getChildren().add(world.bodys.get(index).shape);
		
		index = world.addSensor(0, 400, 600, 1);
		pane.getChildren().add(world.bodys.get(index).shape);
		
		world.addGround(200, 15);

		index = world.addWall(0, 0, 4, 600);
		pane.getChildren().add(world.bodys.get(index).shape);
		index = world.addWall(600, 0, 4, 600);
		pane.getChildren().add(world.bodys.get(index).shape);



	}
	@FXML
	private void starttime(){
		timeline.setCycleCount(Timeline.INDEFINITE);
		Duration duration = Duration.seconds(1.0/60.0); //duracao do tempo quadro


	        KeyFrame frame = new KeyFrame(duration, eventotempo, null,null);

	        timeline.getKeyFrames().add(frame);
	        timeline.playFromStart();
	}
	public static void main(String[] args) {
		launch(args);
	}

	EventHandler<ActionEvent> eventotempo = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent t) {
                    //Create time step. Set Iteration count 8 for velocity and 3 for positions
       			world.tick();
       }
    };
	EventHandler<MouseEvent> circleOnMousePressedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            //orgSceneX = t.getSceneX();
		            //orgSceneY = t.getSceneY();
		           // orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
		           // orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
		            //System.out.println("clique");
		            timeline.stop();
		        }
		    };

	  EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            //double offsetX = t.getSceneX() - orgSceneX;
		            //double offsetY = t.getSceneY() - orgSceneY;
		            //double newTranslateX = orgTranslateX + offsetX;
		            //double newTranslateY = orgTranslateY + offsetY;

		            //((Circle)(t.getSource())).setTranslateX(newTranslateX);
		            //((Circle)(t.getSource())).setTranslateY(newTranslateY);
		            ((Circle)(t.getSource())).setLayoutX(t.getSceneX());
		            ((Circle)(t.getSource())).setLayoutY(t.getSceneY());;
		        }
		    };

	EventHandler<MouseEvent> circleOnFim = new EventHandler<MouseEvent>(){
		@Override
        public void handle(MouseEvent t) {
			System.out.println("Cicle.getLayout()");
			System.out.println(cicle.getLayoutX());
	        System.out.println(cicle.getLayoutY());

	        System.out.println("Mouse ponto");
			System.out.println(t.getSceneX());
	        System.out.println(t.getSceneY());

			//Utility.world.destroyBody(ball.body);

        	//ball.bd.position.set( Utility.toPosX((float)cicle.getLayoutX()),Utility.toPosY((float)cicle.getLayoutY())+15);
			//ball.bd.position.set(45,90);
        	//ball.body = Utility.world.createBody(ball.bd);
        	//ball.body.createFixture(ball.fd);
        	//ball.node.setUserData(ball.body);
	        float xpos = Utility.toPosX((float)t.getSceneX());
            float ypos = Utility.toPosY((float)t.getSceneY());
            System.out.println("Posicao calculada");
            System.out.println(xpos);
	        System.out.println(ypos);
	        Vec2 patual = ball.body.getPosition();
	        System.out.println("Posicao bola");
	        System.out.println(patual.x);
	        System.out.println(patual.y);

	        //aqui onde ocorre realmente a mudanca de posicao
	        ball.body.setTransform(new Vec2(xpos, ypos),0);
	        //zerando a velocidade para a bola n sair quicando como vinha antes
	        ball.body.setLinearVelocity(new Vec2(0,0));

	        Vec2 nova = ball.body.getPosition();
	        System.out.println("Posicao atual");
	        System.out.println(nova.x);
	        System.out.println(nova.y);
	        //pare = true;
	        timeline.playFromStart();
		}

	};
}

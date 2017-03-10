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
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
	private AnchorPane playground;
	
	@FXML
	private AnchorPane toolbox;
	
	@FXML
	private SplitPane split_toolbox_playground; 
	
	@FXML
	private AnchorPane paineltop;

	@FXML
	private Label lab;
	
	//Parte de adicionar Formas
	@FXML
	private Circle circle2;
	
	boolean adicionando = false;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {


			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource( "MainForm.fxml"));
			root = (AnchorPane) loader.load();
			Utility.CalcularTela();
			scene = new Scene(root,Utility.WIDTH,Utility.HEIGHT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage = primaryStage;
			stage.setScene(scene);
			stage.show();
			this.world = new World2(new Vec2(0.0f,-10.0f));
			System.out.println("Iniciou");
			
			
			
	        //fim
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
		playground.getChildren().add(cicle);

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
		playground.getChildren().add(rampa);


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
		//int index = playground.getChildren().size()-1;
		//playground.getChildren().get(index).setLayoutX(playground.getChildren().get(index).getLayoutX()+10);

		if (this.world == null)
			this.world = new World2(new Vec2(0.0f,-10.0f));
		int index = this.world.CriarCirculo(8, 200, 20, Color.RED);
		playground.getChildren().add(world.bodys.get(index).shape);





	}


	@FXML
	private void starttime(){
		System.out.println("Tamanho do Painel de simulação");
		System.out.println(playground.getWidth());
		System.out.println(playground.getHeight());
		Utility.WIDTH = (int)playground.getWidth();
		Utility.HEIGHT = (int)playground.getHeight();
		if (world != null){
			if (timeline.getKeyFrames().isEmpty()){
				//Configuracao do timer
				Duration duration = Duration.seconds(1.0/60.0); //duracao do tempo quadro
		        KeyFrame frame = new KeyFrame(duration, eventotempo, null,null);
		        timeline.getKeyFrames().add(frame);
			}
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.playFromStart();
		}
		
		
//		circle2.setOnMousePressed(circleOnMousePressedEventHandler);
//		circle2.setOnMouseDragged(circleOnMouseDraggedEventHandler);
//		circle2.setOnMouseReleased(circleOnFim);
	}
	@FXML
	private void stoptime(){
		timeline.stop();
	}
	
	@FXML
	private void recomecar(){
		Utility.CalcularTela();
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
		            /*orgSceneX = t.getSceneX();
		            orgSceneY = t.getSceneY();
		            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
		            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();*/
		            //System.out.println("clique");
		            timeline.stop();
		        }
		    };
		    
		    
	  @FXML
	  private void ShapeMousePressed(MouseEvent t){
		  
		  orgSceneX = t.getSceneX();
          orgSceneY = t.getSceneY();
          orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
          orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
		  adicionando = false;
	  }

	  @FXML
	  private void ShapeMouseDragged(MouseEvent t){
		  
	
		  double offsetX = t.getSceneX() - orgSceneX;
          double offsetY = t.getSceneY() - orgSceneY;
          double newTranslateX = orgTranslateX + offsetX;
          double newTranslateY = orgTranslateY + offsetY;

          ((Shape)(t.getSource())).setTranslateX(newTranslateX);
          ((Shape)(t.getSource())).setTranslateY(newTranslateY);
          double posX = ((Shape)(t.getSource())).getBoundsInParent().getMinX();
          
          if (!adicionando){
        	  if (posX<0){
        		  
        		  adicionando = true;

        		  
        		  playground.getChildren().add(circle2);
        		  circle2.setLayoutX(t.getSceneX());
        	  }
          }
          System.out.println();
          
	  }
	  
	  @FXML
	  private void ShapeMouseEnded(){
		  
	  }
	  
	  @FXML
	  private void ShapeMouseOverPane(MouseEvent t){
		  
//		  if (adicionando){
//			 if (!playground.getChildren().contains(((Shape)(t.getSource())))){
//				 playground.getChildren().add(((Shape)(t.getSource())));
//				 
//			 }
//		  }
		  System.out.println("Mouse movendo");
		  System.out.println(t.getSceneX());
		  System.out.println(t.getSceneY());
		  System.out.println(t.getY());
		 // System.out.println();
	  }
	  
	  EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            /*double offsetX = t.getSceneX() - orgSceneX;
		            double offsetY = t.getSceneY() - orgSceneY;
		            double newTranslateX = orgTranslateX + offsetX;
		            double newTranslateY = orgTranslateY + offsetY;
					
		            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
		            ((Circle)(t.getSource())).setTranslateY(newTranslateY);*/
		            ((Shape)(t.getSource())).setLayoutX(t.getSceneX());
		            ((Shape)(t.getSource())).setLayoutY(t.getSceneY()-paineltop.getHeight());

		        }
		    };

	EventHandler<MouseEvent> circleOnFim = new EventHandler<MouseEvent>(){
		@Override
        public void handle(MouseEvent t) {
			//System.out.println("Cicle.getLayout()");
			//System.out.println(cicle.getLayoutX());
	        //System.out.println(cicle.getLayoutY());

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
            float ypos = Utility.toPosY((float)t.getSceneY()-(float)paineltop.getHeight());
            System.out.println("Posicao calculada");
            System.out.println(xpos);
	        System.out.println(ypos);
	        Vec2 patual = ball.body.getPosition();
	        System.out.println("Posicao bola");
	        System.out.println(patual.x);
	        System.out.println(patual.y);

	        //aqui onde ocorre realmente a mudanca de posicao
	        Body3 ball = world.bodys.get((int)((Shape)(t.getSource())).getUserData());
	        		
	        ball.physics.setTransform(new Vec2(xpos+ball.physics_width, ypos-ball.physics_height),0);
	        
	        //zerando a velocidade para a bola n sair quicando como vinha antes
	        ball.physics.setLinearVelocity(new Vec2(0,0));

	        Vec2 nova = ball.physics.getPosition();
	        System.out.println("Posicao atual");
	        System.out.println(nova.x);
	        System.out.println(nova.y);
	        //pare = true;
	        timeline.playFromStart();
		}

	};
	EventHandler<MouseEvent> RampaOnFim = new EventHandler<MouseEvent>(){
		@Override
        public void handle(MouseEvent t) {
			//System.out.println("Cicle.getLayout()");
			//System.out.println(cicle.getLayoutX());
	        //System.out.println(cicle.getLayoutY());

	        System.out.println("Mouse ponto");
			System.out.println(t.getSceneX());
	        System.out.println(t.getSceneY());

	        float xpos = Utility.toPosX((float)t.getSceneX());
            float ypos = Utility.toPosY((float)t.getSceneY()-(float)paineltop.getHeight());
            System.out.println("Posicao calculada");
            System.out.println(xpos);
	        System.out.println(ypos);
	        Vec2 patual = ball.body.getPosition();
	        System.out.println("Posicao bola");
	        System.out.println(patual.x);
	        System.out.println(patual.y);

	        //aqui onde ocorre realmente a mudanca de posicao
	        Body3 ball = world.bodys.get((int)((Shape)(t.getSource())).getUserData());
	        
	        //float theta = ba
	        		
	        //ball.physics.setTransform(new Vec2(xpos+ball.physics_width, ypos-ball.physics_height),0);
	        Vec2 novaposicao = ball.getCenterPosition(xpos, ypos);
	        ball.physics.setTransform(new Vec2(novaposicao.x, novaposicao.y),0);
	        //zerando a velocidade para a bola n sair quicando como vinha antes
	        ball.physics.setLinearVelocity(new Vec2(0,0));

	        Vec2 nova = ball.physics.getPosition();
	        System.out.println("Posicao atual");
	        System.out.println(nova.x);
	        System.out.println(nova.y);
	        //pare = true;
	        timeline.playFromStart();
		}

	};
	//Eventos das formas que são adicionadas
	
	@FXML
	private void addCirculo(){
		
		if (this.world == null)
		{
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
		}
		int index = this.world.CriarCirculo(8, 200, 20, Color.RED);
		playground.getChildren().add(world.bodys.get(index).shape);

		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);
		
	}
	
	@FXML
	private void addGround(){
		
		if (this.world == null){
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
		}

		int angulo = 20;
		int index = 0;
		//index = this.world.addRampa(0, 300, 400, 10,angulo);
		//playground.getChildren().add(world.bodys.get(index).shape);
		index = world.addGround(0, 400, 800, 10);
		playground.getChildren().add(world.bodys.get(index).shape);
		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);
		
		/*
		index = world.addSensor(0, 400, 600, 1);
		playground.getChildren().add(world.bodys.get(index).shape);
		playground.getChildren().add(world.bodys.get(index).imagem);
		

		index = world.addWall(0, 0, 4, 600);
		playground.getChildren().add(world.bodys.get(index).shape);
		index = world.addWall(600, 0, 4, 600);
		playground.getChildren().add(world.bodys.get(index).shape);*/
		
	}
	
	@FXML
	private void addWall(){
		
		if (this.world == null){
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
		}

		int angulo = 20;
		int index = 0;
		//index = this.world.addRampa(0, 300, 400, 10,angulo);
		//playground.getChildren().add(world.bodys.get(index).shape);
		index = world.addWall(100, 00, 8, 600);
		playground.getChildren().add(world.bodys.get(index).shape);
		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);
		
	}
	
	@FXML
	private void addRampa(){

		if (this.world == null){
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
		}

		int angulo = 20;
		int index = 0;
		index = this.world.addRampa(10, 300, 700, 10,angulo);
		playground.getChildren().add(world.bodys.get(index).shape);
		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(RampaOnFim);
		world.bodys.get(index).shape.setUserData(index);
		
//		index = world.addSensor(0, 400, 600, 1);
//		playground.getChildren().add(world.bodys.get(index).shape);
//		playground.getChildren().add(world.bodys.get(index).imagem);
//		world.addGround(200, 15);
//
//		index = world.addWall(0, 0, 4, 600);
//		playground.getChildren().add(world.bodys.get(index).shape);
//		index = world.addWall(600, 0, 4, 600);
//		playground.getChildren().add(world.bodys.get(index).shape);



	}
}

package application;

import java.awt.Scrollbar;

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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
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
    long tempo = 0;
    int tempo_segundos = 0;
    boolean pare = false;


	//Parte do jBox2D
	//BouncyBall ball = new BouncyBall(45,90, Utility.BALL_RADIUS, Color.ALICEBLUE);
    BouncyBall ball = new BouncyBall(45,90, Utility.BALL_RADIUS, Color.ALICEBLUE);
    int index_selecionado;



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

	//Parte das variaveis do objeto
	@FXML
	private TextField Vx;
	@FXML
	private TextField Vy;
	@FXML
	private TextField Coef_atrito;
	@FXML
	private TextField Coef_elasticidade;
	@FXML
	private TextField Massa;

	//Gráfico partegvhf
	@FXML
	private LineChart grafico;
	
	@FXML
	private ComboBox cmbvariaveisgrafico;

	//Formas
	@FXML
	private TextField largura;
	@FXML
	private TextField altura;
	@FXML
	private TextField angulo;

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

		if (this.world == null)
			this.world = new World2(new Vec2(0.0f,-10.0f));
		int index = this.world.CriarCirculo(8, 200, 20, Color.RED);
		playground.getChildren().add(world.bodys.get(index).shape);

	}


	@FXML
	private void starttime(){
		ConfigGrafico();
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

	}

	private void ConfigGrafico(){

		grafico.setCreateSymbols(false);

	}

	@FXML
	private void stoptime(){
		timeline.stop();



	}

	@FXML
	private void recomecar(){

		if (index_selecionado > -1)
		{
			world.bodys.get(index_selecionado).pontos_grafico.getData().clear();
			//grafico.getData().add(world.bodys.get(index_selecionado).pontos_grafico.getData());
		}
		tempo = 0;
		tempo_segundos = 0;
	}
	public static void main(String[] args) {
		launch(args);

	}

	EventHandler<ActionEvent> eventotempo = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent t) {
			//Create time step. Set Iteration count 8 for velocity and 3 for positions

			world.tick();
			tempo++;
			if (tempo > 20)
			{
				tempo = 0;
				tempo_segundos++;

				if (index_selecionado > -1){
					float velocidade = world.bodys.get(index_selecionado).physics.getPosition().y;
					//world.bodys.get(index_selecionado).pontos_grafico.getData().add(new XYChart.Data(tempo_segundos,velocidade));
					world.bodys.get(index_selecionado).pontos_grafico.getData().add(new XYChart.Data(tempo_segundos,velocidade));

				}
			}
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
		  System.out.print("Posi��o X: ");
		  System.out.println(t.getSceneX());
		  System.out.print("Posi��o Y crua: ");
		  System.out.println(t.getSceneY());
		  System.out.print("Posi��o Y corrigida: ");
		  System.out.println(t.getY());
		 // System.out.println();
	  }
	  //Eventos de arrastar e soltar

		EventHandler<MouseEvent> circleOnMousePressedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            /*orgSceneX = t.getSceneX();
		            orgSceneY = t.getSceneY();
		            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
		            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();*/
		            //System.out.println("clique");
		        	Body3 ball = world.bodys.get((int)((Shape)(t.getSource())).getUserData());
		        	ball.GuardarPosicaoCentroInicial();
		        	ball.GuardarPosicaoVerticeInicial();
		        	CarregarVariaveisObjetos(ball);
		        	carregarFormasObjetos(ball);
		        	grafico.getData().add(world.bodys.get(index_selecionado).pontos_grafico);
		        	grafico.getXAxis().setAutoRanging(true);
		        	grafico.getYAxis().setAutoRanging(true);
		            timeline.stop();
		        }
		    };

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
		            float xpos = Utility.toPosX((float)t.getSceneX());
		            float ypos = Utility.toPosY((float)t.getSceneY()-(float)paineltop.getHeight());
		            Body3 ball = world.bodys.get((int)((Shape)(t.getSource())).getUserData()); //Retorna index
		            ball.moverObjeto(xpos, ypos);

		        }
		    };

	EventHandler<MouseEvent> circleOnFim = new EventHandler<MouseEvent>(){
		@Override
        public void handle(MouseEvent t) {
	        timeline.playFromStart();
		}

	};


	private void CarregarVariaveisObjetos(Body3 body3){

		Body body = body3.physics;
		Vx.setText(String.format("%.2f", body.m_linearVelocity.x));
		Vy.setText(String.format("%.2f", body.m_linearVelocity.y));
		Coef_atrito.setText(String.format("%.2f", body.getFixtureList().m_friction));
		Coef_elasticidade.setText(String.format("%.2f", body.getFixtureList().m_restitution));
		Massa.setText(String.format("%.2f", body.getMass()));
		this.index_selecionado = (int)body3.shape.getUserData();

	}

	private void carregarFormasObjetos(Body3 body3){

		largura.setText(String.format("%.2f", body3.shape_width));
		altura.setText(String.format("%.2f", body3.shape_height));
		angulo.setText(String.format("%.2f", body3.shape.getRotate()));

	}
	//Eventos das formas que são adicionadas

	@FXML
	private void addCirculo(){

		if (this.world == null)
		{
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
			Utility.TAMANHO_REAL_ALTURA = ((Utility.HEIGHT*1.0f)/Utility.WIDTH)*Utility.TAMANHO_REAL_LARGURA;

		}
		int index = this.world.CriarCirculo(8, 200, 20, Color.RED);
		playground.getChildren().add(world.bodys.get(index).shape);

		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);

	}

	@FXML
	private void addQuadrado(){

		if (this.world == null){
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
			Utility.TAMANHO_REAL_ALTURA = ((Utility.HEIGHT*1.0f)/Utility.WIDTH)*Utility.TAMANHO_REAL_LARGURA;
		}

		int index = 0;
		index = this.world.CriarQuadrado(40, 40, 200, 100, Color.BLUE);
		playground.getChildren().add(world.bodys.get(index).shape);

		System.out.println("Angulo calculado");
		System.out.println(Utility.tograus(world.bodys.get(index).physics.getAngle()));
		System.out.println(world.bodys.get(index).shape.rotateProperty().get());
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
			Utility.TAMANHO_REAL_ALTURA = ((Utility.HEIGHT*1.0f)/Utility.WIDTH)*Utility.TAMANHO_REAL_LARGURA;
		}


		int index = 0;

		index = world.addGround(0, 400, 800, 10);
		playground.getChildren().add(world.bodys.get(index).shape);
		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);



	}

	@FXML
	private void addWall(){

		if (this.world == null){
			this.world = new World2(new Vec2(0.0f,-10.0f));
			Utility.WIDTH = (int)playground.getWidth();
			Utility.HEIGHT = (int)playground.getHeight();
			Utility.TAMANHO_REAL_ALTURA = ((Utility.HEIGHT*1.0f)/Utility.WIDTH)*Utility.TAMANHO_REAL_LARGURA;
		}

		int index = 0;

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
			Utility.TAMANHO_REAL_ALTURA = ((Utility.HEIGHT*1.0f)/Utility.WIDTH)*Utility.TAMANHO_REAL_LARGURA;
		}

		int angulo = 20;
		int index = 0;
		index = this.world.addRampa(100, 300, 500, 100,angulo);
		playground.getChildren().add(world.bodys.get(index).shape);

		System.out.println("Angulo calculado");
		System.out.println(Utility.tograus(world.bodys.get(index).physics.getAngle()));
		System.out.println(world.bodys.get(index).shape.rotateProperty().get());
		world.bodys.get(index).shape.setOnMousePressed(circleOnMousePressedEventHandler);
		world.bodys.get(index).shape.setOnMouseDragged(circleOnMouseDraggedEventHandler);
		world.bodys.get(index).shape.setOnMouseReleased(circleOnFim);
		world.bodys.get(index).shape.setUserData(index);



	}


	//Aplicar nvas caracteristicas
	@FXML
	private void aplicarCaracteristicas(){

		if (this.index_selecionado >-1){
			System.out.println(Vx.getText().replace(',', '.'));
			Vec2 velocidade = new Vec2( Float.parseFloat(Vx.getText().replace(',', '.')),Float.parseFloat(Vy.getText().replace(',', '.')) );
			world.bodys.get(index_selecionado).physics.setLinearVelocity(velocidade);
			world.bodys.get(index_selecionado).physics.getFixtureList().m_friction = Float.parseFloat(Coef_atrito.getText().replace(',', '.'));
			world.bodys.get(index_selecionado).physics.getFixtureList().m_restitution = Float.parseFloat(Coef_elasticidade.getText().replace(',', '.'));
			//world.bodys.get(index_selecionado).physics.mass = Float.parseFloat(Coef_atrito.getText().replace(',', '.'));
			world.bodys.get(index_selecionado).physics.resetMassData();

		}

	}

	//Formas
	@FXML
	private void modificarForma(){
		if (index_selecionado>-1){

			float largura = Float.parseFloat(this.largura.getText().replace(',', '.'));
			float altura = Float.parseFloat(this.altura.getText().replace(',', '.'));
			float angulo = Float.parseFloat(this.angulo.getText().replace(',', '.'));
			world.bodys.get(index_selecionado).resize(largura, altura,angulo);

		}
	}




}
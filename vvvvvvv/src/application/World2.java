package application;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.geometry.Point3D;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.ArrayList;
public class World2 extends org.jbox2d.dynamics.World{


	public ArrayList<Body3> bodys;
    public ArrayList<Shape> shapes;


	public World2(Vec2 gravity) {
		super(gravity);

		bodys = new ArrayList<Body3>();
		shapes = new ArrayList<Shape>();

	}

	public void tick(float fps, int velocityIterations, int positionIterations){

		this.step(1.0f/fps, velocityIterations, positionIterations);
		refreshWorld();

	}
	public void tick()
	{
		this.step(1.0f/60.f, 8, 3);
		refreshWorld();
	}

	private void refreshWorld(){

		for (Body3 body : bodys){

			//Body3 body = bodys.get(i);

			float angulo = body.physics.getAngle();


	        Vec2 nova = body.physics.getPosition();
	        if (nova.x < -20)
	        {
	        	//this.destroyBody(body.physics);
	        	//this.bodys.remove(body);
	        }
	        else{

	        	if (!body.fixo){
	        		body.shape.setRotate(Utility.tograus(-angulo));
	        		body.shape.setLayoutX(Utility.toPixelPosX(nova.x)-(float)(body.shape_width/2));
	        		//body.shape.setLayoutX(300);
		        	body.shape.setLayoutY(Utility.toPixelPosY(nova.y)-(float)(body.shape_height/2));


	        	}
	        	//System.out.println(body.physics.m_linearVelocity.y);
	        	//System.out.println(body.shape.get);
	        }


		}

	}
	// TODO
	/**
	 *Aqui vamos criar uma lista dos Body que ser�o criados no World2 feito
	 *
	 *seria uma boa tambem o World2 possuir a Scena que ele vai brincar
	 *Essa classe seria responsavel por atualizar a scena
	 *pelos bodys
	 *colocar uma funcao pra ir avancando o tempo
	 *ter uma propriedade do tamanho da tela
	 */


	public int CriarCirculo(float raio,int pixelX, int pixelY,Color cor){

		/// Todos os par�metros passados ser�o em  pixels.

		float posX = Utility.toPosX(pixelX);
		float posY = Utility.toPosY(pixelY);
		float raioreal = Utility.toPosX(raio);

		//criar a defini��o da bola visual
		Circle ball = new Circle();
        //ball.setRadius(Utility.toPixelWidth(raio));
		ball.setRadius(Utility.toPixelWidth(raio));
        ball.setLayoutX(pixelX);
        ball.setLayoutY(pixelY);
        ball.setFill(cor);


      //cria agora a definicao da bola para  o jbox2D
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posX,posY);
        CircleShape cs = new CircleShape();
        cs.m_radius = raio;// * 0.1f;
        //bd.linearVelocity = new Vec2(0.0f,0.0f);

      //cria a fixture da bola
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 4.6f;
        fd.friction = 0.1f;
        fd.restitution = 0.1f;
        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();
        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
        Body3 body = new Body3();
        body.physics =this.createBody(bd);

        body.shape = ball;
        body.physics.createFixture(fd);

        body.physics.setUserData(body);
        body.shape_height =0;
        body.shape_width = 0;
        body.shape_type = Body3.Forma.CIRCLE;
        bodys.add(body);
        Contato contato = new Contato();
        this.setContactListener(contato);
        return bodys.indexOf(body);
	}

	public int CriarQuadrado(float width, float height,int posX, int posY,Color cor){

		/// Todos os par�metros passados ser�o em  pixels.

		 	PolygonShape ps = new PolygonShape();
	        ps.setAsBox(Utility.toWidth(width/2), Utility.toHeight(height/2));

	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;
	        fd.density = 1.0f;
	        fd.friction = 0.3f;
	        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
	        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();

	        BodyDef bd = new BodyDef();
	        bd.type = BodyType.DYNAMIC;
	        bd.allowSleep = false;
	        float x = (Utility.toPosX(posX+width/2));
	    	float y =  Utility.toPosY(posY+height/2);
	        bd.position.set(x,y);




	      //Forma do quadrado
	        Rectangle parede = new Rectangle(width, height);
			parede.setLayoutX(posX);
			parede.setLayoutY(posY);


			Body3 body = new Body3();

	        body.physics = this.createBody(bd); //aqui ta diferente do tutorial
	        body.physics.createFixture(fd);
	        body.physics.setUserData(body);

	        body.shape = parede;

	        body.physics_width = Utility.toWidth(width/2);
	        body.physics_height = Utility.toHeight(height/2);
	        body.shape_height = height;
	        body.shape_width = width;
	        body.shape_type = Body3.Forma.RECTANGLE;
	        bodys.add(body);
	        //world.createBody(bd).createShape(sd);;
			return bodys.indexOf(body);
	}

	 public  void addGround(float width, float height){
	        PolygonShape ps = new PolygonShape();
	        ps.setAsBox(width, height);
	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;
	        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
	        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();
	        BodyDef bd = new BodyDef();
	        bd.position = new Vec2(0.0f,-10f);

	        this.createBody(bd).createFixture(fd);
	    }
	 public  int addGround(float posX, float posY,float width,float height){

	        PolygonShape ps = new PolygonShape();
	        ps.setAsBox(Utility.toWidth(width/2), Utility.toHeight(height/2));

	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;
	        fd.density = 1.0f;
	        fd.friction = 0.3f;
	        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
	        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();
	        BodyDef bd = new BodyDef();
	        float x = (Utility.toPosX(posX+width/2));
	    	float y =  Utility.toPosY(posY+height/2);
	        bd.position.set(x,y);




	      //Forma Parede
	        Rectangle parede = new Rectangle(width, height);
			parede.setLayoutX(posX);
			parede.setLayoutY(posY);


			Body3 body = new Body3();

	        body.physics = this.createBody(bd); //aqui ta diferente do tutorial
	        body.physics.createFixture(fd);
	        body.physics.setUserData(body);

	        body.shape = parede;
	        body.fixo = true;

	        body.physics_width = Utility.toWidth(width/2);
	        body.physics_height = Utility.toHeight(height/2);
	        body.shape_height = height;
	        body.shape_width = width;
	        body.shape_type = Body3.Forma.RECTANGLE;
	        bodys.add(body);
	        //world.createBody(bd).createShape(sd);;
			return bodys.indexOf(body);
	    }

	    //adiciona uma parede

	    public  int addWall(float posX, float posY,float width,float height){

	        PolygonShape ps = new PolygonShape();
	        ps.setAsBox(Utility.toWidth(width/2), Utility.toHeight(height/2));

	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;
	        fd.density = 1.0f;
	        fd.friction = 0.3f;
	        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
	        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();
	        BodyDef bd = new BodyDef();
	        float x = (Utility.toPosX(posX+width/2));
	    	float y =  Utility.toPosY(posY+height/2);
	        bd.position.set(x,y);

	        //Teste sensor
//	        PolygonShape ps2 = new PolygonShape();
//	        ps2.setAsBox(width+10, height);
//	        FixtureDef fd2 = new FixtureDef();
//	        fd2.shape = ps2;
//	        fd2.isSensor = true;
//	        fd2.filter.categoryBits = Body3.ContactType.BODYS.tipo();
//	        fd2.filter.maskBits = Body3.ContactType.BODYS.tipo();


	      //Forma Parede
	        Rectangle parede = new Rectangle(width, height);
			parede.setLayoutX(posX);
			parede.setLayoutY(posY);


			Body3 body = new Body3();

	        body.physics = this.createBody(bd); //aqui ta diferente do tutorial
	        body.physics.createFixture(fd);
	       // body.physics.createFixture(fd2);
	        body.physics.setUserData(body);

	        body.shape = parede;
	        body.fixo = true;

	        body.physics_width = Utility.toWidth(width/2);
	        body.physics_height = Utility.toHeight(height/2);
	        body.shape_height = height;
	        body.shape_width = width;
	        body.shape_type = Body3.Forma.RECTANGLE;
	        bodys.add(body);
	        //world.createBody(bd).createShape(sd);;
			return bodys.indexOf(body);
	    }
	//Adiciona uma rampa
    public  int addRampa(int PosX, int PosY, float width, float height, float rotation) {

    	/**
    	 Metodo de teste
    	 */
    	PolygonShape ps = new PolygonShape();
        ps.setAsBox(Utility.toWidth(width)/2f, Utility.toHeight(height)/2f);


        FixtureDef fd = new FixtureDef();

        fd.shape = ps;
        fd.friction = 0.1f;
        fd.filter.categoryBits = Body3.ContactType.BODYS.tipo();
        fd.filter.maskBits = Body3.ContactType.BODYS.tipo();
    	BodyDef bd = new BodyDef();
    	float x = (Utility.toPosX(PosX+width/2));
    	float y =  Utility.toPosY(PosY+height/2);
        bd.position.set(x, y);
        if (rotation != 0f) { bd.angle = Utility.torad(rotation); }



        Body3 body = new Body3();

        body.physics = this.createBody(bd);
        body.physics.createFixture(fd);




		//Forma
        Rectangle rampa = new Rectangle(width, height);

		rampa.setLayoutX(PosX);
		rampa.setLayoutY(PosY);
		rampa.setRotate(-rotation-0.27);
		//rampa.getTransforms().add(new Rotate(-rotation, width/2,height/2,0));

//		System.out.println("Meio do fgdfg bixo");
//
//		System.out.println(PosX+ width/2);
//		System.out.println(Utility.toPixelPosX(body.physics.getPosition().x));
//		System.out.println(PosY+height/2);
//		System.out.println( Utility.toPixelPosY(body.physics.getPosition().y));
//		//rampa.setRotationAxis(new Point3D(Utility.toPixelPosX(bd.position.x),Utility.toPixelPosY(bd.position.y),0));
//		//rampa.rotationAxisProperty();


		body.shape = rampa;
		body.fixo = true;
		body.physics.setUserData(body);

		body.physics_width = Utility.toWidth(width/2);
        body.physics_height = Utility.toHeight(height/2);
        body.physics_angle = bd.angle;

        body.shape_height = height;
        body.shape_width = width;
        body.shape_type = Body3.Forma.RECTANGLE;
		bodys.add(body);
        //world.createBody(bd).createShape(sd);;
		return bodys.indexOf(body);
    }


    public  int addSensor(float posX, float posY,float width,float height){

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(Utility.toWidth(width), Utility.toHeight(height));


        BodyDef bd = new BodyDef();
        float x = Utility.toPosX(posX);
    	float y =  Utility.toPosY(posY);
        bd.position.set(x,y);

        //Teste sensor
        PolygonShape ps2 = new PolygonShape();
        ps2.setAsBox(width, height);
        FixtureDef fd2 = new FixtureDef();
        fd2.shape = ps2;
        fd2.isSensor = true;
        fd2.filter.categoryBits = Body3.ContactType.BODYS.tipo();
        fd2.filter.maskBits = Body3.ContactType.BODYS.tipo();


      //Forma Parede
       Rectangle parede = new Rectangle(width, height);
		parede.setLayoutX(posX);
		parede.setLayoutY(posY);
		parede.setFill(Color.GREEN);

		//Imagem do sensor
        Image image = new Image(World2.class.getResourceAsStream("sensor.png"));
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(60);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        iv.setLayoutX(posX);
        iv.setLayoutY(posY);

		Body3 body = new Body3();


        body.physics = this.createBody(bd); //aqui ta diferente do tutorial
        body.physics.createFixture(fd2);
        body.physics.setUserData(body);

        body.imagem = iv;
        body.shape = parede;
        body.fixo = true;
        body.shape_height = height;
        body.shape_width = width;
        bodys.add(body);
        //world.createBody(bd).createShape(sd);;
		return bodys.indexOf(body);
    }


}
class Body3{

	public Body physics;
	public Shape shape;
	public ImageView  imagem;
	public boolean fixo = false;
	public String nome = "";
	public float physics_width = 0;
	public float physics_height = 0;
	public float physics_angle = 0;
	public float pcx_inicial = 0;
	public float pcy_inicial = 0;
	public float pvx_inicial = 0;
	public float pvy_inicial = 0;
	public float shape_width = 0;
	public float shape_height = 0;
	public Forma shape_type = Forma.RECTANGLE;
	public  XYChart.Series pontos_grafico = new Series();

	public float getCosAngle(){
		return (float) Math.cos(physics_angle);
	}
	public float getAngleGama(){
		System.out.println(Math.atan(physics_width/physics_height));
		return (float) Math.atan(physics_width/physics_height);
	}
	public float getDiagonal(){
		return (float) Math.sqrt(physics_height*physics_height+physics_width*physics_width);
	}
	public float getAngleTheta(){
		return physics.getAngle() - getAngleGama();
	}
	public Vec2 getCenterPosition(float Pvx,float Pvy){
		float theta = getAngleTheta();
		float diagonal = getDiagonal();
		float x = (float) (Pvx+Math.cos(theta)*diagonal);
		float y = (float) (Pvy+Math.sin(theta)*diagonal);
		return new Vec2( x, y);
	}
	public void setPosicaoCentroInicial(float px, float py){
		pcx_inicial = px;
		pcy_inicial = py;
	}
	public void GuardarPosicaoCentroInicial(){
		pcx_inicial = physics.getPosition().x;
		pcy_inicial = physics.getPosition().y;
	}
	public void setPosicaoVerticeInicial(float px, float py){
		pvx_inicial = px;
		pvy_inicial = py;
	}
	public void GuardarPosicaoVerticeInicial(){
		pvx_inicial = Utility.toPosX((float) shape.getLayoutX());
		pvy_inicial = Utility.toPosY((float) shape.getLayoutY());
	}

	public Vec2 CalcularDeltaPosicao(float px,float py){
		return new Vec2(px-pvx_inicial,py-pvy_inicial);
	}
	public void resize(float width, float height){

		Fixture fx = this.physics.m_fixtureList;
		FixtureDef fd = new FixtureDef();

		float height_incial = 0;
		float width_inicial = 0;
		org.jbox2d.collision.shapes.Shape ps;

		if (this.shape_type == Forma.RECTANGLE){

			ps = new PolygonShape();
			((PolygonShape)ps).setAsBox(Utility.toWidth(width/2), Utility.toHeight(height/2));
			height_incial = (float)((Rectangle)this.shape).getHeight();
			width_inicial = (float)((Rectangle)this.shape).getWidth();
			((Rectangle)this.shape).setWidth(width);
			((Rectangle)this.shape).setHeight(height);
			this.shape_height = height;
			this.shape_width = width;

			 //Parte visual
	        Vec2 nova = this.physics.getPosition();
	        this.shape.setLayoutX(Utility.toPixelPosX(nova.x)-(float)(this.shape_width/2));
			//body.shape.setLayoutX(300);
	    	this.shape.setLayoutY(Utility.toPixelPosY(nova.y)-(float)(this.shape_height/2));
		}
		else
		{
			ps = new CircleShape();
			((CircleShape)ps).m_radius = Utility.toWidth(width);
			((Circle)this.shape).setRadius(width);
		}


        fd.shape = ps;
        fd.density = fx.getDensity();
        fd.friction = fx.getFriction();
        fd.filter.categoryBits = fx.m_filter.categoryBits;// Body3.ContactType.BODYS.tipo();
        fd.filter.maskBits = fx.m_filter.maskBits;// Body3.ContactType.BODYS.tipo();

        this.physics.destroyFixture(fx);
        this.physics.createFixture(fd);




	}
	public static enum ContactType{
		BODYS (0x0002),
		SENSOR (0x0004);

		private final int tipo;
		ContactType(int tipo){
			this.tipo = tipo;
		}
		public int tipo(){
			return tipo;
		}
	}
	public static enum Forma{
		CIRCLE,RECTANGLE
	}

}
class Contato implements ContactListener{
	/**
	 * Fazer uma lista de corpos que podem ser chocados, na verdade uma tupla o corpo e seu contato
	 * A ideia � fazer o sensor
	 * no teste do choque verificar se ele esta na lista ao inv�s de usar um 4 procurar nessa lista se
	 * h� a tupla e ai tratar
	 * colocar maskbits talvez seja interessantes
	 * http://www.iforce2d.net/b2dtut/sensors
	 * http://www.iforce2d.net/b2dtut/user-data
	 */

	@Override
	public void beginContact(Contact arg0) {
		// TODO Auto-generated method stub
		Fixture fixtureA = arg0.getFixtureA();
		Fixture fixtureB = arg0.getFixtureB();
		Body3 body = (Body3)fixtureB.getBody().getUserData();


		if ((body != null) && (!fixtureB.isSensor()))
		{
			System.out.println("Contato");
			System.out.println(body.physics.getLinearVelocity().y);
			System.out.print("Posicao ");
			System.out.println(body.physics.getPosition().y);
		}

		body = (Body3)fixtureA.getBody().getUserData();


		if ((body != null) && (!fixtureA.isSensor()))
		{
			System.out.println("Contato");
			System.out.println(body.physics.getLinearVelocity().y);
			System.out.print("Posicao ");
			System.out.println(body.physics.getPosition().y);
		}

	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub

	}


}
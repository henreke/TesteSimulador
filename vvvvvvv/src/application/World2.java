package application;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
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
	        	this.destroyBody(body.physics);
	        	this.bodys.remove(body);
	        }
	        else{

	        	if (!body.fixo){
	        		body.shape.setRotate(Utility.tograus(-angulo));
	        		body.shape.setLayoutX(Utility.toPixelPosX(nova.x));
		        	body.shape.setLayoutY(Utility.toPixelPosY(nova.y));
	        	}
	        	System.out.println(body.physics.m_linearVelocity.y);
	        	//System.out.println(body.shape.get);
	        }


		}

	}
	// TODO
	/**
	 *Aqui vamos criar uma lista dos Body que serão criados no World2 feito
	 *
	 *seria uma boa tambem o World2 possuir a Scena que ele vai brincar
	 *Essa classe seria responsavel por atualizar a scena
	 *pelos bodys
	 *colocar uma funcao pra ir avancando o tempo
	 *ter uma propriedade do tamanho da tela
	 */


	public int CriarCirculo(float raio,int pixelX, int pixelY,Color cor){

		/// Todos os parâmetros passados serão em  pixels.

		float posX = Utility.toPosX(pixelX);
		float posY = Utility.toPosY(pixelY);
		float raioreal = Utility.toPosX(raio);

		//criar a definição da bola visual
		Circle ball = new Circle();
        ball.setRadius(Utility.toPixelHeight(raio));
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
        fd.restitution = 0.8f;

        Body3 body = new Body3();
        body.physics =this.createBody(bd);

        body.shape = ball;
        body.physics.createFixture(fd);

        bodys.add(body);

        return bodys.indexOf(body);
	}

	 public  void addGround(float width, float height){
	        PolygonShape ps = new PolygonShape();
	        ps.setAsBox(width, height);
	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;

	        BodyDef bd = new BodyDef();
	        bd.position = new Vec2(0.0f,0f);

	        this.createBody(bd).createFixture(fd);
	    }

	    //adiciona uma parede

	    public  void addWall(float posX, float posY,float width,float height){

	        PolygonShape ps = new PolygonShape();
	        ps.setAsBox(width, height);

	        FixtureDef fd = new FixtureDef();
	        fd.shape = ps;
	        fd.density = 1.0f;
	        fd.friction = 0.3f;

	        BodyDef bd = new BodyDef();
	        bd.position.set(posX,posY);
	        this.createBody(bd).createFixture(fd); //aqui ta diferente do tutorial
	    }
	//Adiciona uma rampa
    public  int addRampa(int PosX, int PosY, float width, float height, float rotation) {

    	/**
    	 Metodo de teste
    	 */
    	PolygonShape ps = new PolygonShape();
        ps.setAsBox(Utility.toWidth(width), Utility.toHeight(height)/2f);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = 0.1f;
    	BodyDef bd = new BodyDef();
    	float x = Utility.toPosX(PosX);
    	float y =  Utility.toPosY(PosY);
        bd.position.set(x, y);
        if (rotation != 0f) { bd.angle = Utility.torad(rotation); }

        Body3 body = new Body3();

        body.physics = this.createBody(bd);
        body.physics.createFixture(fd);

        Rectangle rampa = new Rectangle(width, height);

		//Forma
		rampa.setLayoutX(PosX);
		rampa.setLayoutY(PosY-rampa.getHeight());
		rampa.getTransforms().add(new Rotate(-rotation,0,0,0));

		body.shape = rampa;
		body.fixo = true;
		bodys.add(body);
        //world.createBody(bd).createShape(sd);;
		return bodys.indexOf(body);
    }

}
class Body3{

	public Body physics;
	public Shape shape;
	public boolean fixo = false;

}

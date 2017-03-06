package application;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import java.util.ArrayList;
public class World2 extends org.jbox2d.dynamics.World{


	public ArrayList<Body2> bodys;



	public World2(Vec2 gravity) {
		super(gravity);

		bodys = new ArrayList<Body2>();

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

		for (Body2 body2 : this.bodys){

			body2.refreshPosicaoShape();

		}

	}
	// TODO
	/**
	 *Aqui vamos criar uma lista dos Body que serão criados no World2 feito
	 *Os corpos vão possuir uma propriedada dos pixels
	 *seria uma boa tambem o World2 possuir a Scena que ele vai brincar
	 *Essa classe seria responsavel por atualizar a scena
	 *pelos bodys
	 *colocar uma funcao pra ir avancando o tempo
	 *ter uma propriedade do tamanho da tela
	 */


	public void CriarCirculo(float raio,int pixelX, int pixelY,Color cor){

		/// Todos os parâmetros passados serão em  pixels.

		float posX = Utility.toPosX(pixelX);
		float posY = Utility.toPosY(pixelY);
		float raioreal = Utility.toPosX(raio);

		//criar a definição da bola visual
		Circle ball = new Circle();
        ball.setRadius(raio);
        ball.setFill(cor);


      //cria agora a definicao da bola para  o jbox2D
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posX,posY);
        CircleShape cs = new CircleShape();
        cs.m_radius = raio * 0.1f;

      //cria a fixture da bola
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 4.6f;
        fd.friction = 0.1f;
        fd.restitution = 0.8f;

        bodys.add((Body2) this.createBody(bd));
        bodys.get(bodys.size()-1).createFixture(fd);
        bodys.get(bodys.size()-1).shape = ball;

	}

}

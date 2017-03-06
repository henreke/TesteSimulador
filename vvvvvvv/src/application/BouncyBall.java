package application;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author henrique
 */
public class BouncyBall {

   //Java fx UI para a bola
    public Node node;

    public Body body;
    public BodyDef bd;
    public FixtureDef fd;
    // posicao X e Y no mundo
    private float posX;
    private float posY;

    //Raio da bola
    private int radius;

    private Color color;

    public BouncyBall(float posX, float posY, int radius, Color color){

        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.color = color;
        node = create();
    }

    private Node create(){

        Circle ball = new Circle();
        ball.setRadius(radius);
        ball.setFill(color);

        ball.setLayoutX(Utility.toPixelPosX(posX));
        ball.setLayoutY(Utility.toPixelPosY(posY));

        //cria agora a definicao da bola para  o jbox2D
        bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posX,posY);
        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;

        //cria a fixture da bola
        fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 4.6f;
        fd.friction = 0.0f;
        fd.restitution = 0.8f;

        body = Utility.world.createBody(bd);
        body.createFixture(fd);
        ball.setUserData(body);
        return ball;
    }
}


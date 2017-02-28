package application;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author henrique
 */
public class Utility {

    //Cria o mundo
    public static final World world = new World(new Vec2(0.0f,-10.0f));

    //Largura e altura da tela
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    //Raio das bolas em pixels
    public static final int BALL_RADIUS = 8;

    //Metodo adiciona o chao
    public static void addGround(float width, float height){
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, height);
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;

        BodyDef bd = new BodyDef();
        bd.position = new Vec2(0.0f,-10f);

        world.createBody(bd).createFixture(fd);
    }

    //adiciona uma parede

    public static void addWall(float posX, float posY,float width,float height){

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.3f;

        BodyDef bd = new BodyDef();
        bd.position.set(posX,posY);
        world.createBody(bd).createFixture(fd); //aqui ta diferente do tutorial
    }

    //Adiciona uma rampa
    public static void addRampa(float x, float y, float width, float height, float rotation) {

    	/**
    	 Metodo de teste
    	 */
    	PolygonShape ps = new PolygonShape();
        ps.setAsBox(width / 2f, height / 2f);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = 0.1f;
    	BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        if (rotation != 0f) { bd.angle = rotation; }
        world.createBody(bd).createFixture(fd);



        //world.createBody(bd).createShape(sd);;
    }

    //Converte uma coordenada em pixel
    public static float toPixelPosX(float posX){
        float x = WIDTH*posX/100.0f;
        return x;
    }
    //converte um coordenada em pixel para uma coordenada no jbox
    public static float toPosX(float posX){
        float x = (posX*100.0f*1.0f)/WIDTH;
        return x;
    }

    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    public static float toPixelPosY(float posY) {
        float y = HEIGHT - (1.0f*HEIGHT) * posY / 100.0f;
        return y;
    }

    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public static float toPosY(float posY) {
        float y = 100.0f - ((posY * 100*1.0f) /HEIGHT) ;
        return y;
    }

    //Convert a JBox2D width to pixel width
    public static float toPixelWidth(float width) {
        return WIDTH*width / 100.0f;
    }

    //Convert a JBox2D height to pixel height
    public static float toPixelHeight(float height) {
        return HEIGHT*height/100.0f;
    }

    public static float tograus(float rad){
    	return (float) (rad*180/Math.PI);
    }
    public static float torad(float deg){
    	return (float) (deg*Math.PI/180);
    }
}


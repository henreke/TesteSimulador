package application;

import java.awt.Dimension;
import java.awt.Toolkit;

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
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    public static int WIDTH_TOTAL = 600;
    public static int HEIGHT_TOTAL = 600;

    //Raio das bolas em pixels
    public static final int BALL_RADIUS = 8;

    //Tamanho do mundo
    public static final float TAMANHO_REAL = 100f;

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
        float x = WIDTH*posX/TAMANHO_REAL;
        return x;
    }
    //converte um coordenada em pixel para uma coordenada no jbox
    public static float toPosX(float posX){
        float x = (posX*TAMANHO_REAL*1.0f)/WIDTH;
        return x;
    }

    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    public static float toPixelPosY(float posY) {
        float y = HEIGHT - (1.0f*HEIGHT) * posY / TAMANHO_REAL;
        return y;
    }

    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    public static float toPosY(float posY) {
        float y = TAMANHO_REAL - ((posY * TAMANHO_REAL*1.0f) /HEIGHT) ;
        return y;
    }

    //Convert a JBox2D width to pixel width
    public static float toPixelWidth(float width) {
        return WIDTH*width / TAMANHO_REAL;
    }

    //Convert a JBox2D height to pixel height
    public static float toPixelHeight(float height) {
        return HEIGHT*height/TAMANHO_REAL;
    }
    //converte pixel height to jbox2d height
    public static float toHeight(float height){
    	return height*TAMANHO_REAL/HEIGHT;
    }

    //converte pixel width to jbox2d height
    public static float toWidth(float width){
    	return width*TAMANHO_REAL/WIDTH;
    }
    public static float tograus(float rad){
    	return (float) (Math.toDegrees(rad));
    }
    public static float torad(float deg){
    	return (float) (Math.toRadians(deg));
    }

    public static void CalcularTela(){

    	WIDTH_TOTAL = Toolkit.getDefaultToolkit().getScreenSize().width;
    	HEIGHT_TOTAL = Toolkit.getDefaultToolkit().getScreenSize().height;

    	WIDTH = (int) (WIDTH_TOTAL*0.8);
    	HEIGHT = (int)(HEIGHT_TOTAL*0.8);
    	/* Para vários monitores
    	 * GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
    	 * */

    }
}


package application;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import javafx.scene.shape.Shape;

public class Body2 extends Body{

	public Body2(BodyDef bd, World world) {
		super(bd, world);
		// TODO Auto-generated constructor stub
	}

	public Shape shape;

	public void refreshPosicaoShape()
	{
		//Atualizacao do layout
		float angulo = this.getAngle();

        Vec2 nova = this.getPosition();
        shape.setLayoutX(Utility.toPixelPosX(nova.x));
        shape.setLayoutY(Utility.toPixelPosY(nova.y));
        shape.setRotate(Utility.tograus(-angulo));

	}

}

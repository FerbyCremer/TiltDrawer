import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Marble extends Sphere{
	int radius = 16;
	int active;
	paint paint;
	double roughness;
	static float friction;
	static double velocityX, velocityY;
	public static boolean state;

	public Marble(){
		Sphere marble = new Sphere(16);
		if (this.isHover()){
			marble.setMaterial(new PhongMaterial(Color.CYAN));
		}
		state = false;
		friction = 0.0f;
	}

	public void rotate(int degreesX, int degreesY, double velocityX, double velocityY){
		this.setRotate((degreesX*velocityX)/16);
		this.setRotate((degreesY*velocityY)/16);

	}
	public void move(int degreesX, int degreesY){
		
		if(degreesX != 0)
			velocityX = velocityCalc(velocityX, degreesX, 0.056);
		if(degreesY != 0)
			velocityY = velocityCalc(velocityY, degreesY, 0.056);
		System.out.println(this.getTranslateX() + " , " + this.getTranslateZ());

		this.setTranslateX(this.getTranslateX() + velocityX);
		this.setTranslateZ(this.getTranslateZ() + velocityY);

		if(!this.getBoundsInParent().intersects(tilt.slate.platform.getBoundsInParent())){
			if(this.getTranslateX() >= tilt.slate.platform.getBoundsInParent().getMaxX())
				this.setTranslateX(tilt.slate.platform.getBoundsInParent().getMaxX());
			else if(this.getTranslateX() <= tilt.slate.platform.getBoundsInParent().getMinX())
				this.setTranslateX(tilt.slate.platform.getBoundsInParent().getMinX());
			if(this.getTranslateZ() >= tilt.slate.platform.getBoundsInParent().getMaxZ())
				this.setTranslateZ(tilt.slate.platform.getBoundsInParent().getMaxZ());
			else if(this.getTranslateZ() <= tilt.slate.platform.getBoundsInParent().getMinZ())
				this.setTranslateZ(tilt.slate.platform.getBoundsInParent().getMinZ());
		}
		if(state == true)
			tilt.slate.paintPath(this.getTranslateX(), this.getTranslateZ());
	}
	public double velocityCalc(double velocity, int degrees, double d){
		//if(degrees <  0)
			friction = friction * -1;
		double velocityNew = velocity;
		System.out.println("degrees: " + degrees + ", velocity: " + velocityNew);
		velocityNew += (( 9.81 * Math.sin(Math.toRadians(degrees))) - (friction * 9.81 * Math.cos(Math.toRadians(degrees))));
		System.out.println("degrees: " + degrees + ", velocity: " + velocityNew);
		return velocityNew;
	}
	public float setFriction(int marbleMat, int slateMat){
		friction = tilt.friction[marbleMat][slateMat];
		return friction;
	}
}

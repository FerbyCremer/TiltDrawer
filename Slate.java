import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
//@SuppressWarnings("unused")
public class Slate{
	// GLOBAL VARIABLES
	final Box platform;
	Cylinder[] puddle = new Cylinder[4];
	static paint[] paint = new paint[4];
	final Box water;
	Box surface;
	static Marble marble;
	static paint mpaint;
	int degreesX = 0;
	int degreesY = 0;

	SubScene s;
	Stage window;

	Group root;

	public Slate(){
		window = new Stage();
		mpaint = new paint(); 
		root = new Group();
		PerspectiveCamera camera = new PerspectiveCamera();
		PhongMaterial wood = new PhongMaterial(Color.BLACK);
		wood.setSpecularColor(Color.AZURE);
		wood.setSpecularPower(32);
		platform = new Box(700, 35, 700);
		platform.setMaterial(wood);
		platform.setDrawMode(DrawMode.FILL);
		platform.setTranslateX(0);
		platform.setTranslateY(0);
		platform.setTranslateZ(0);

		water = new Box(150, 35, 50);
		PhongMaterial liquid = new PhongMaterial(Color.POWDERBLUE);
		liquid.setSpecularPower(50);
		water.setMaterial(liquid);
		water.setDrawMode(DrawMode.FILL);
		water.setTranslateX(0);
		water.setTranslateY(-2);
		water.setTranslateZ(-315);

		for (int i = 0; i < 4; i++){

			paint[i] = new paint();

			puddle[i] = new Cylinder(25,1);
			puddle[i].materialProperty().set(paint[i]);
			puddle[i].setMaterial(paint[i].value);
			puddle[i].setDrawMode(DrawMode.FILL);
			puddle[i].setTranslateX(300);
			puddle[i].setTranslateY(-18);

			final int j = i;
			// === PAINT CONTROL LISTENERS
			puddle[i].setOnMouseClicked( new EventHandler<MouseEvent>() {
				public void handle( MouseEvent e ){
					final Sliders paints = new Sliders(tilt.grid, paint[j]);
					paints.apply.setOnAction(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent e){
							paint[j] = new paint(paints.paintV.red, paints.paintV.green, paints.paintV.blue, paints.paintV.alphaV, Sliders.visc, Sliders.strain);
							puddle[j].setMaterial(paint[j].value);
							tilt.states = new gages(tilt.grid);
						}
					});
					paints.cancel.setOnAction(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent e){
							tilt.states = new gages(tilt.grid);
						}
					});
				}});

			// --- Mouse ENTER
			puddle[i].setOnMouseEntered( new EventHandler<MouseEvent>() {
				public void handle( MouseEvent e ){
					if(puddle[j].contains(e.getX(), e.getY())){
						puddle[j].setDrawMode(DrawMode.LINE);
						puddle[j].setMaterial(new PhongMaterial(Color.CYAN));}
				}});

			// --- Mouse EXIT
			puddle[i].setOnMouseExited( new EventHandler<MouseEvent>() {
				public void handle( MouseEvent e ){
					if(!puddle[j].contains(e.getX(), e.getY())){
						puddle[j].setDrawMode(DrawMode.FILL);
						puddle[j].setMaterial(paint[j].value);
					}
				}});
		}

		puddle[0].setTranslateZ(-200);
		puddle[1].setTranslateZ(-75);
		puddle[2].setTranslateZ(75);
		puddle[3].setTranslateZ(200);

		PhongMaterial paper = new PhongMaterial(Color.IVORY);
		paper.setSpecularPower(5);
		surface = new Box (600, 1, 600);
		surface.setMaterial(paper);
		surface.setDrawMode(DrawMode.FILL);
		surface.setTranslateX(-40);
		surface.setTranslateY(-18.5);
		surface.setTranslateZ(20);

		marble = new Marble();
		marble.setRadius(16);
		marble.paint = new paint();
		marble.paint.setSpecularPower(60);
		marble.setMaterial(marble.paint.value);
		marble.setDrawMode(DrawMode.FILL);
		marble.setTranslateX(0);
		marble.setTranslateY(-32);
		
		PointLight light = new PointLight(Color.LIGHTCYAN);
		light.setTranslateY(-400);
		light.setTranslateX(-300);
		light.setTranslateZ(250);
		
		PointLight accent = new PointLight(Color.LAVENDERBLUSH);
		accent.setTranslateY(-600);
		accent.setTranslateX(500);
		accent.setTranslateZ(-500);

		root.getChildren().add(light);
		root.getChildren().add( accent);
		root.getChildren().addAll(platform, water, puddle[0], puddle[1], puddle[2], puddle[3], surface, marble);
		Group g = new Group();
		g.getChildren().add(camera);
		g.getChildren().add(root);

		s = new SubScene(g, tilt.WIDTH, tilt.HEIGHT - 275, true, SceneAntialiasing.BALANCED);
		s.setFill(Color.SLATEGREY);
		s.setCamera(camera);
		camera.getTransforms().addAll( new Translate(-600, -200, 300), new Rotate(20, Rotate.Y_AXIS),
				new Rotate(-30, Rotate.X_AXIS));
		s.setDepthTest(DepthTest.ENABLE);

	}
	public void paintPath(double x, double z){
		marble.paint.load = 0;
			Box path = new Box(marble.paint.width, marble.paint.height, marble.paint.depth);
			path.setTranslateX(x);
			path.setTranslateZ(z);
			path.setTranslateY(-18 - (marble.paint.height));
			path.setMaterial(marble.paint.value);
			path.setDrawMode(DrawMode.FILL);
			root.getChildren().add(path);
			for(int i = 0; i < 4; i++){
				if(tilt.states.loading[i].getLength() < 0)
					marble.active++;
			}
			for(int i = 0; i < 4; i++){
				if(tilt.states.loading[i].getLength() < 0){
					paint[i].load -= (float) (marble.paint.volume/marble.active);
					tilt.states.loading[i].setLength(-(paint[i].load / 9));
				}
				else
					tilt.states.loading[i].setLength(0);
				marble.paint.load += paint[i].load;
			}
			if(marble.paint.load <= 0){
				for(int i = 0; i < 4; i++){
					paint[i].load = 0;
					tilt.states.loading[i].setLength(0);
				}
				Marble.state = false;
				mpaint.load = 0;
				mpaint = new paint();
				return;
			}
			System.out.println("after: " + marble.paint.load);
		
	}
}

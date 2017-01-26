import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class tilt extends Application{
	static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	static final int WIDTH = screen.width;
	static final int HEIGHT = screen.height - 50;
	static private BufferedImage img = new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	// **** initial platform
	static Slate slate = new Slate();
	// file chooser
	private static FileChooser chooser = new FileChooser();
	//complete friction matrix				concrete | wood | glass | Iron | Brass
	public final static float[][] friction = {{0.7f, 0.62f, 0.35f, 0.6f, 0.58f},	//concrete 
											  {0.62f, 0.5f, 0.3f, 0.49f, 0.3f}, 	//wood
											  {0.35f, 0.22f, 0.17f, 0.27f, 0.2f},	//glass
											  {0.6f, 0.49f, 0.24f, 0.35f, 0.3f}, 	//iron
											  {0.58f, 0.37f, 0.2f, 0.3f, 0.35f}};	//brass
	static GridPane grid = new GridPane();
	public static gages states = new gages(grid);
	//default materials on start
	static int marbleMat = 2;
	static int slateMat = 1;
	// Layouts
	static BorderPane layout = new BorderPane();
	HBox buttons = new HBox();
	final static Button toggler = new Button("Drawing Toggle");
	static GridPane panel = new GridPane();
	static Stage window;
	static Scene scene;
	static SubScene slateScene;

	static Timeline timeline;

	public static void main(String[] args) {
		launch(args);
	}
	//######################################################################
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle( "CAP 3027 2016 - TiltDraw - Jennifer Cremer" );

		// ----- setup file chooser dialog
		chooser.setInitialDirectory(new File("."));

		slateScene = slate.s;
		Group g = new Group();
		g.getChildren().add(slateScene);

		Marble.friction = friction[marbleMat][slateMat];

		//---add menu
		addMenu();

		//--- marble mouse listeners
		slateScene.setOnMouseClicked(new EventHandler<MouseEvent> (){
			public void handle(MouseEvent e) {
				settingAttributes(e);
			}
		});
		// --- draw toggle
		Ellipse button = new Ellipse(0,0,15,15);
		toggler.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
		toggler.setTextFill(Color.RED);
		toggler.setShape(button);
		toggler.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				Marble.state = (Marble.state == false) ? true : false;
				toggler.setTextFill((toggler.getTextFill() == Color.RED) ? Color.GREEN : Color.RED);
			}
		});
	/*	// --- Undo Button
		final Button ubutton = new Button( "Undo Roll" );
		ubutton.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				// ******* COMMAND PATTERN GOES HERE *****
			}
		});
		// --- Redo Button
		final Button rbutton = new Button( "Redo Roll" );
		rbutton.setOnAction( new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				// ****** COMMAND PATTERN GOES HERE TOO *****
			}
		});
		
		ubutton.setPrefSize(300,70);
		rbutton.setPrefSize(300, 70);*/
		toggler.setPrefSize(150, 90);
		states = new gages(grid);
		panel.add(grid, 0, 0);
		buttons.setSpacing(17);
		buttons.setPadding(new Insets(10,10,10,10));
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		buttons.getChildren().addAll( toggler, panel/*, ubutton, rbutton*/);
		layout.setBottom(buttons);
		layout.setCenter(g);
		scene = new Scene(layout, WIDTH, HEIGHT);
		window.setScene(scene);
		window.sizeToScene();
		window.show();

		final LongProperty lastUpdateTime = new SimpleLongProperty();
		final AnimationTimer rotTimer = new AnimationTimer(){
			@Override public void handle(long timestamp){
				if(lastUpdateTime.get() > 0){
					final double elapsedMilliSeconds = (timestamp - lastUpdateTime.get())/1000000;
					final double rotationAngles = elapsedMilliSeconds / 560;
					final double oldAngle = slate.root.getRotate();
					final double newAngle = oldAngle + rotationAngles;
					slate.root.setRotate(newAngle);
				}
				lastUpdateTime.set(timestamp);
			}
		};

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent e){
				rotTimer.stop();
				if (e.getCode() == KeyCode.UP){
					if(!e.isShiftDown()){
						rotTimer.start();
						slate.root.setRotationAxis(Rotate.X_AXIS);
						slate.degreesY++;
						slate.root.setRotate(-slate.degreesY);
					}
					Slate.marble.move(0, slate.degreesY);
					Slate.marble.rotate(0, slate.degreesY, 0, Marble.velocityY);
				}
				else if (e.getCode() == KeyCode.DOWN){
					if(!e.isShiftDown()){
						rotTimer.start();
						slate.root.setRotationAxis(Rotate.X_AXIS);
						slate.degreesY--;
						slate.root.setRotate(-slate.degreesY);	
					}
					Slate.marble.move(0, slate.degreesY);
					Slate.marble.rotate(0, slate.degreesY, 0, Marble.velocityY);
				}
				else if (e.getCode() == KeyCode.RIGHT){
					if(!e.isShiftDown()){
						rotTimer.start();
						slate.root.setRotationAxis(Rotate.Z_AXIS);
						slate.degreesX++;
						slate.root.setRotate(slate.degreesX);
					}
					Slate.marble.move(slate.degreesX, 0);
					Slate.marble.rotate(slate.degreesX, 0, Marble.velocityX, 0);
				}
				else if (e.getCode() == KeyCode.LEFT){
					if(!e.isShiftDown()){
						rotTimer.start();
						slate.root.setRotationAxis(Rotate.Z_AXIS);
						slate.degreesX--;
						slate.root.setRotate(slate.degreesX);
					}
					Slate.marble.move(slate.degreesX, 0);
					Slate.marble.rotate(slate.degreesX, 0, Marble.velocityX, 0);
				}
			}});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
				rotTimer.stop();
				if(!e.isShiftDown() && (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)){
					rotTimer.start();
					do{
						if(slate.degreesY < 0)
							slate.degreesY++;
						else
							slate.degreesY--;
					}while(slate.degreesY != 0);
					slate.root.setRotationAxis(Rotate.X_AXIS);
					slate.root.setRotate(-slate.degreesY);
					slate.degreesY = 0;
					Marble.velocityY = 0;
				}
				if(!e.isShiftDown() && (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.LEFT)){
					rotTimer.start();
					do{
						if(slate.degreesX < 0)
							slate.degreesX++;
						else
							slate.degreesX--;
					}while(slate.degreesX != 0);
					slate.root.setRotationAxis(Rotate.Z_AXIS);
					slate.root.setRotate(-slate.degreesX);
					slate.degreesX = 0;
					Marble.velocityX = 0;
				}
				rotTimer.stop();
			}
		});

		doLoading();
	}
	private static void addMenu(){
		//---------------------------------------------------
		//setup menu bar
		// === File Menu
		Menu menu = new Menu("File");
		// --- new slate
		MenuItem item = new MenuItem("New");
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle( ActionEvent e ){		//*** run on EDT
				initialSlate();
			}
		});
		// --- save
		MenuItem save = new MenuItem("Save Image");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle( ActionEvent e ){			//*** run on EDT
				save();
			}
		});
		// --- Exit
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle( ActionEvent e ){		//*** run on EDT
				window.close();
				System.exit(0);
			}
		});
		menu.getItems().add(item);
		menu.getItems().add(save);
		menu.getItems().add(exit);
		/*// === Slate Menu
		JMenu slateMenu = new JMenu("Slate");
		// --- new slate
		JMenuItem sitem = new JMenuItem("Load Texture");
		sitem.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ){		//*** run on EDT
				loadSlateTexture();
			}
		});
		slateMenu.add(sitem);
		// --- save
		sitem = new JMenuItem("Clear Texture");
		sitem.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ){		//*** run on EDT
				cleanSlate();
			}
		});
		slateMenu.add(sitem);*/

		// === attach menu to menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu);
		layout.setTop(menuBar);
	}
	//===================================================================
	public static void settingAttributes(MouseEvent e){
		if(e.getTarget() == Slate.marble || e.getTarget() == slate.surface){
			final MaterialPicker Picked = new MaterialPicker(grid, e);
			if(e.getTarget() == Slate.marble){
				Picked.materials.setValue(marbleMat);
				Picked.apply.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						Slate.marble.setMaterial(new PhongMaterial(Color.ALICEBLUE));
						marbleMat = Picked.picked;
						gages states = new gages(grid);
					}
				});
			}
			else{
				Picked.materials.setValue(slateMat);
				Picked.apply.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						slateMat = Picked.picked;
						gages states = new gages(grid);
					}
				});
			}
			Slate.marble.setFriction(marbleMat, slateMat);
			Picked.cancel.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					gages states = new gages(grid);
				}});
		}
		else if(e.getTarget() == slate.puddle[0] || e.getTarget() == slate.puddle[1] || e.getTarget() == slate.puddle[2] || e.getTarget() == slate.puddle[3] ){
			e.consume();}
		else{
			gages states = new gages(grid);
		}
	}

	// =====================================================================
	// === initialSlate()
	private static void initialSlate(){
		slate = new Slate();
		gages states = new gages(grid);
	}
	// === loading animations
	public static void doLoading(){
		timeline = new Timeline(new KeyFrame(Duration.millis(25), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				if(!Slate.marble.getBoundsInParent().intersects(slate.puddle[0].getBoundsInParent()) && !Slate.marble.getBoundsInParent().intersects(slate.puddle[1].getBoundsInParent()) && !Slate.marble.getBoundsInParent().intersects(slate.puddle[2].getBoundsInParent()) && !Slate.marble.getBoundsInParent().intersects(slate.puddle[3].getBoundsInParent()) && !Slate.marble.getBoundsInParent().intersects(slate.water.getBoundsInParent()))
					Slate.marble.paint = Slate.mpaint;
				else{
					for(int i = 0; i < 4; i++){
						if(Slate.marble.getBoundsInParent().intersects(slate.puddle[i].getBoundsInParent()))
							states.fillGage(states.loading[i]);
						else if(Slate.marble.getBoundsInParent().intersects(slate.water.getBoundsInParent()))
							states.emptyGages();
					}
				}
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	// === save()
	private static void save(){
				chooser.showSaveDialog(window);
				File outputFile = new File("TiltDraw.png");
				try
				{
					javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(slateScene.snapshot(null, new WritableImage((int)slateScene.getWidth(), (int)slateScene.getHeight())), img), "png", outputFile );
				}
				catch ( IOException e ){}
	}
}

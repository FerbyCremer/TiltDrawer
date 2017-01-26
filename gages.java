import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class gages{
	Ellipse[] temp = new Ellipse[4];
	public Arc[] loading = new Arc[4];
	/*Text xAngle, yAngle;
	Text x, y;*/

	public gages(final GridPane grid){
		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		grid.getRowConstraints().clear();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 10, 10));

		Text title = new Text("Marble's States");
		title.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
	    grid.add(title, 0, 0, 2, 1); 

	    for(int i = 0, j = 3; i < 4; i++, j--){
	    	temp[i] = new Ellipse(0, 0, 48, 48);
	    	temp[i].setStroke(Color.SILVER);
	    	temp[i].setStrokeType(StrokeType.OUTSIDE);
	    	temp[i].setFill(Color.TRANSPARENT);
	    	loading[i] = new Arc(0,0,48,48,90.0f,-(Slate.paint[i].load/9));
			loading[i].setType(ArcType.ROUND);
			loading[i].setFill(Slate.paint[i].value.getDiffuseColor());
			Group set = new Group(temp[i], loading[i]);
			grid.add(set, j, 1);
	    }
/*	    
		x = new Text("Xº: ");
		x.setFont(Font.font("Garamond", FontWeight.BOLD, 14));
	    grid.add(x, 4, 1);
	    StringBuilder ax = new StringBuilder(tilt.slate.degreesX);
	    xAngle = new Text(ax.toString());
	    xAngle.setFill(Color.BLACK);
	    xAngle.setFont(Font.font("Garamond", FontWeight.BOLD, 14));
	    grid.add(xAngle, 5, 1);
	    StringBuilder ay = new StringBuilder(tilt.slate.degreesY);
	    y = new Text("Yº: ");
		y.setFont(Font.font("Garamond", FontWeight.BOLD, 14));
	    grid.add(y, 6, 1);	    
	    yAngle = new Text(ay.toString());
	    yAngle.setFill(Color.BLACK);
	    yAngle.setFont(Font.font("Garamond", FontWeight.BOLD, 14));
	    grid.add(yAngle, 7, 1);
	    */
		return;
	}
	
	
	protected void fillGage(Arc gage){
		for(int i = 0; i < 4; i++){
			if( loading[i].equals(gage) && (loading[i].getLength() > -360 && loading[i].getLength() < 0.00001)){
				gage.setLength(-(Slate.paint[i].load += 9) / 9);
				System.out.println("paint load " + Slate.paint[i].load);
				Slate.mpaint = Slate.mpaint.paintInterp( Slate.marble.paint, Slate.paint[i] );
				Slate.mpaint.load += Slate.paint[i].load;
				Slate.marble.setMaterial(Slate.mpaint.value);
			}
		}
	}
	
	public void emptyGages(){
		for(int i = 0; i < 4; i++){
				loading[i].setLength(0.00);
				Slate.paint[i].load = 0;
				Slate.marble.paint = new paint();
				Slate.mpaint = new paint();
				Slate.mpaint.load = 0;
			}	
		}
	
}

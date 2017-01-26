import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MaterialPicker  {

	Slider materials;
	int picked;
	float frictionCoef;
	final Button apply = new Button("Apply");
	final Button cancel = new Button("Cancel");

	public MaterialPicker(final GridPane grid, final Event event){

		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		grid.getRowConstraints().clear();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 10, 10));
		Label mat = new Label("Material Attribute");
		mat.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
		grid.add(mat, 0, 0, 2, 1); 

		materials = new Slider(0, 4, 0);
		materials.setMajorTickUnit(1);
		materials.setShowTickMarks(true);
		materials.setShowTickLabels(true);
		materials.setSnapToTicks(true);
		grid.add(materials, 0,1);

		materials.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> material, Number old, Number newVal){
				picked = newVal.intValue();

			}
		});
		grid.add(apply, 2, 2);

		grid.add(cancel, 3, 2);

		return;
	}
}

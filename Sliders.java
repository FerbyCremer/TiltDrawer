import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Sliders{
	public paint paintV;
	int red, green, blue;
	int step;
	static double alphaV;
	public static float visc;
	public static float strain;
	final Slider alpha, Red, Green, Blue, Visc, Strain;
	final Label a, r, g, b, v, s;
	
	final Button apply = new Button("Apply");
	final Button cancel = new Button("Cancel");

public Sliders(final GridPane grid, final paint material){
	grid.getChildren().clear();
	grid.getColumnConstraints().clear();
	grid.getRowConstraints().clear();
	grid.setHgap(5);
	grid.setVgap(5);
	grid.setPadding(new Insets(0, 2, 2, 2));
	paintV = material;
	red = paintV.red;
	green = paintV.green;
	blue = paintV.blue;
	alphaV = paintV.alphaV;
	visc = paintV.visc;
	strain = paintV.strain;
	
	Label title = new Label("Paint Parameters");
	title.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
    grid.add(title, 0, 0, 2, 1); 
	
	alpha = new Slider(0, 1.0, material.alphaV);
	a = new Label("Alpha Channel");
	final Label av = new Label(Integer.toString((int)(alpha.getValue()*255)));
	
	alpha.setMajorTickUnit(.15);
	grid.add(a, 0, 1);
	grid.add(alpha, 1, 1);
	grid.add(av, 2, 1);
	
	alpha.valueProperty().addListener(new ChangeListener<Number>(){
		public void changed(ObservableValue<? extends Number> alpha, Number old, Number newVal){
			alphaV = newVal.doubleValue();
			av.setText(Integer.toString((int)(alphaV*255)));
			paintV.alphaV = alphaV;
		}
	});

	Red = new Slider(0, 255, ((paint)material).red);
	Red.setMajorTickUnit(15);
	r = new Label("Red Channel");
	final Label rv = new Label(Integer.toString((int)(Red.getValue())));
	grid.add(r, 0, 3);
	grid.add(Red, 1, 3);
	grid.add(rv, 2, 3);
	
		Red.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> redV, Number old, Number newVal){
				red = newVal.intValue();
				ColorGradients(grid);
				rv.setText(Integer.toString(red));
				paintV.red = red;
			}
		});
		
	Green = new Slider(0, 255, ((paint)material).green);
	g = new Label("Green Channel");
	Green.setMajorTickUnit(15);
	final Label gv = new Label(Integer.toString((int)(Green.getValue())));
	grid.add(g, 0, 5);
	grid.add(Green, 1, 5);
	grid.add(gv, 2, 5);
	
	Green.valueProperty().addListener(new ChangeListener<Number>(){
		public void changed(ObservableValue<? extends Number> greenV, Number old, Number newVal){
			green = newVal.intValue();
			ColorGradients(grid);
			gv.setText(Integer.toString((int)(green)));
			paintV.green = green;
		}
	});
	
	Blue = new Slider(0, 255, ((paint)material).blue);
	b = new Label("Blue Channel");
	Blue.setMajorTickUnit(15);
	final Label bv = new Label(Integer.toString((int)(Blue.getValue())));
	grid.add(b, 0, 7);
	grid.add(Blue, 1, 7);
	grid.add(bv, 2, 7);
	
	Blue.valueProperty().addListener(new ChangeListener<Number>(){
		public void changed(ObservableValue<? extends Number> blueV, Number old, Number newVal){
			blue = newVal.intValue();
			ColorGradients(grid);
			bv.setText(Integer.toString((int)(blue)));
			paintV.blue = blue;
		}
	});
	
	ColorGradients(grid);

	Visc = new Slider(10, 80, ((paint)material).visc);
	v = new Label("% Viscous Effects");
	Visc.setMajorTickUnit(5);
	final Text Vv = new Text(Integer.toString((int)Visc.getValue()));
	grid.add(v, 0, 8);
	grid.add(Visc, 1, 8);
	
	Visc.valueProperty().addListener(new ChangeListener<Number>(){
		public void changed(ObservableValue<? extends Number> viscV, Number old, Number newVal){
			visc = newVal.floatValue();
			Vv.setText(Integer.toString((int)(visc)));
			paintV.visc = visc;
		}
	});
	
	Strain = new Slider(0, 100, ((paint)material).strain);
	s = new Label("Tensor Behavior");
	Strain.setMajorTickUnit(20);
	final Text Sv = new Text(Integer.toString((int)Strain.getValue()));
	grid.add(s, 0, 9);
	grid.add(Strain, 1, 9);
	
	Strain.valueProperty().addListener(new ChangeListener<Number>(){
		public void changed(ObservableValue<? extends Number> strainV, Number old, Number newVal){
			strain = newVal.floatValue();
			Sv.setText(Integer.toString((int)(strain)));
			paintV.strain = strain;
			
		}
	});

	grid.add(apply, 2,10);
	grid.add(cancel, 3,10);
	return;
}
	public Color[] alphaSlide(){
		int steps = 256;
		//delta
		double dAlpha = (1 - alphaV)/(steps-1);
		//initialize increment
		double nAlpha = alphaV;
		Color pixel[] = new Color[256];
		// =======================================================================
		for(int n = 0; n < step; n++){
			pixel[n] = Color.color(red, green, blue, nAlpha);
			nAlpha += dAlpha;
		}
		return pixel;
	}
	protected Color[] redSlide(){
		int steps = 256;
		//delta
		double dRed = (double)(255 - 0)/(steps-1);
		//initialize increment
		double nRed = 0;
		Color pixel[] = new Color[256];
		for(int n = 0; n < steps; n++){
			pixel[n] = Color.rgb((int) nRed, green, blue, 1.0);
			nRed += dRed;
		}
		return pixel;
	}
	public Color[] greenSlide(){
		int steps = 256;
		//delta
		double dGreen = (double)(255 - 0)/(steps-1);
		//initialize increment
		double nGreen = 0;
		Color pixel[] = new Color[256];
		// =======================================================================
		for(int n = 0; n < steps; n++){
			pixel[n] = Color.rgb(red, (int)nGreen, blue, 1.0);
			nGreen += dGreen;
		}
		return pixel;
	}
	public Color[] blueSlide(){
		int steps = 256;
		//delta
		double dBlue = (double)(255 - 0)/(steps-1);
		//initialize increment
		double nBlue = 0;
		Color pixel[] = new Color[256];
		// =======================================================================
		for(int n = 0; n < steps; n++){
			pixel[n] = Color.rgb(red, green, (int)nBlue, 1);
			nBlue += dBlue;
		}
		return pixel;
	}
	public void ColorGradients(GridPane grid){
		Color[] reds = redSlide();
		GridPane redValue = new GridPane();
		for(int i = 0; i < 256; i++){
			Rectangle rect = new Rectangle(1,6);
			rect.setFill(reds[i]);
			redValue.add(rect, i, 0);
		}
		grid.add(redValue, 1, 2);
		
		Color[] greens = greenSlide();
		GridPane greenValue = new GridPane();
		for(int i = 0; i < 256; i++){
			Rectangle rect = new Rectangle(1,6);
			rect.setFill(greens[i]);
			greenValue.add(rect, i, 0);
		}
		grid.add(greenValue, 1, 4);
		
		Color[] blues = blueSlide();
		GridPane blueValue = new GridPane();
		for(int i = 0; i < 256; i++){
			Rectangle rect = new Rectangle(1,6);
			rect.setFill(blues[i]);
			blueValue.add(rect, i, 0);
		}
		grid.add(blueValue, 1, 6);
	}
}

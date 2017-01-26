import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


public class paint extends PhongMaterial{

	public PhongMaterial value;
	public int red, green, blue;
	public double alphaV;
	public float visc, strain;
	public float width, depth,  height;
	public float volume;
	float load;
	
	public paint(){
		alphaV = 1;
		red = 0;
		green = 0;
		blue = 0;
		value = new PhongMaterial(Color.rgb(255, 255, 255, alphaV));
		visc = 0.0f;
		strain = 1.0f;
		load = 0.0f;
		width = strain / 20;
		depth = width;
		height = visc / 5;
		volume = width * depth * height;
	}

	public paint( int red, int green, int blue, double alpha, float visc, float strain){
		this.alphaV = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.value = new PhongMaterial(Color.rgb(this.red, this.green, this.blue, this.alphaV));
		this.visc = visc;
		this.strain = strain;
		this.height = this.strain * 0.05f;		// scaling factor of 1/20, strain = tendency of fluid to deform under forces
		this.width = this.visc * 0.2f;			// scaling factor of 1/5, viscosity = stickiness factor
		this.depth = this.width;
		this.volume = this.width * this.depth * this.height;
	}

	public paint paintInterp(paint paint1, paint paint2){
		//delta
		
		int percent1 = (int)(paint1.load * 0.0077);		//percent of total marble load => 4*SA
		int percent2 = (int)(paint2.load * 0.0077);
		double dAlpha = (paint2.alphaV*percent2 + paint1.alphaV*percent1) / (percent2 + percent1);
		int dRed = (paint2.red*percent2 + paint1.red*percent1) / (percent2 + percent1);
		int dGreen = (paint2.green*percent2 + paint1.green*percent1) / (percent2 + percent1);
		int dBlue = (paint2.blue*percent2 + paint1.blue*percent1) / (percent2 + percent1);

		float viscAvg = (paint2.visc*percent2 + paint1.visc*percent1)/(percent2 + percent1);
		float strainAvg = (paint2.strain*percent2 + paint1.strain*percent1)/(percent2 + percent1);

		paint pixel = new paint( dRed, dGreen, dBlue, dAlpha, viscAvg, strainAvg);
		return pixel;
	}
	public float viscAvg(paint paint1, paint paint2){
		double percent1 = paint1.load * 0.007716;		//percent of total marble load => 4*SA
		double percent2 = paint2.load * 0.007716;
		float viscAvg = (float)((paint2.visc*percent2 + paint1.visc*percent1)/(percent2 + percent1));
		return viscAvg;
	}
	public float strainAvg(paint paint1, paint paint2){
		double percent1 = paint1.load * 0.007716;		//percent of total marble load => 4*SA
		double percent2 = paint2.load * 0.007716;
		float strainAvg = (float)((paint2.strain*percent2 + paint1.strain*percent1)/(percent2 + percent1));
		return strainAvg;
	}
}

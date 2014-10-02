package Refactoring.Control;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class Constant {

    //Metrics /Related
	public static float CUBESIZE;
	public static float BOXINTERVAL;
	public static float HALFBOXINTERVAL;
	public static float MOVEDISTANCE;
    public static float FRAMEDEPTH;
    public static float BOARDFRAMETHICKNESS;

	//Movement Directions
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	public static final int TOP = 1;
	public static final int DOWN = -1;

    //Color Related
    public static final int RGB255 = 255;

	//Time Interval Related
	public static final int INITIALFALLINTERVAL = 500; //milliseconds
	public static final int ROTATIONINTERVAL = 400; //milliseconds
	public static final int MOVEINTERVAL = 150; //milliseconds

    //Conversion Related
    public static final int NANOTIMETOMILLISECONDS = 1/1000000;

	public Constant(float cubeSize) {
		setCUBESIZE(cubeSize);
	}

	public static void setCUBESIZE(float CUBESIZE) {
		Constant.CUBESIZE = CUBESIZE;
        Constant.BOXINTERVAL = 0.5f * Constant.CUBESIZE;
        Constant.HALFBOXINTERVAL = 0.25f * Constant.CUBESIZE;
        Constant.MOVEDISTANCE = 2.5f * Constant.CUBESIZE;
        Constant.FRAMEDEPTH = 1.5f * Constant.CUBESIZE;
        Constant.BOARDFRAMETHICKNESS = 0.25f * Constant.CUBESIZE;
    }
}

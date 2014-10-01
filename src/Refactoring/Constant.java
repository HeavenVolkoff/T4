package Refactoring;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class Constant {

	public static float CUBESIZE;
	public static float BOXINTERVAL;
	public static float HALFBOXINTERVAL;
	public static float MOVEDISTANCE;

	//Movement Directions
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	public static final int TOP = 1;
	public static final int DOWN = -1;

    //Color Related
    public static final int RGB255 = 255;

	public Constant(float cubeSize) {
		setCUBESIZE(cubeSize);
	}

	public static void setCUBESIZE(float CUBESIZE) {
		Constant.CUBESIZE = CUBESIZE;
        Constant.BOXINTERVAL = 0.5f * Constant.CUBESIZE;
        Constant.HALFBOXINTERVAL = 0.25f * Constant.CUBESIZE;
        Constant.MOVEDISTANCE = 2.5f * Constant.CUBESIZE;
    }
}

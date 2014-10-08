//REFACTORED STATUS: OK.

package Refactoring.Control;

import com.jme3.math.ColorRGBA;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class Constant {

    //Metrics Related
	public static float CUBESIZE;
	public static float BOXINTERVAL;
	public static float HALFBOXINTERVAL;
	public static float MOVEDISTANCE;
    public static float BOARDFRAMEDEPTH;
    public static float BOARDFRAMETHICKNESS;

	//Movement Directions Related
	public static final int TOLEFT = -1;
	public static final int TORIGHT = 1;
	public static final int TOUP = 1;
	public static final int TODOWN = -1;

	//Rotation Related
	public static final int CLOCKWISE = 90;
	public static final int COUNTERCLOCKWISE = -90;

    //Color Related
    public static final int RGB255 = 255;

	//Time Interval Related
	public static final int INITIALFALLINTERVAL = 500; //milliseconds
	public static final int ROTATIONINTERVAL = 400; //milliseconds
	public static final int MOVEINTERVAL = 150; //milliseconds
    public static final int BOARDCHUNCKFALLINTERVAL = 150; //milliseconds

    //Conversion Related
    public static final int NANOTIMETOMILLISECONDS = 1/1000000;

    //Correction Related
    public static final float THICKNESSCORRECTION = 1.5f;

    //Position Related
    public static final String TOP = "T";
    public static final String RIGHT = "R";
    public static final String BOTTOM = "B";
    public static final String LEFT = "L";

    //Color Related
    public static final ColorRGBA BOARDCOLOR = ColorRGBA.DarkGray;

    //File Related
    public static final String PIECECONTROLERCONFIGFILE = "/Configurations/PieceControls.ini";

    public Constant(float cubeSize) {
		setCUBESIZE(cubeSize);
	}

	public static void setCUBESIZE(float CUBESIZE) {
		Constant.CUBESIZE = CUBESIZE;
        Constant.BOXINTERVAL = 0.5f * Constant.CUBESIZE;
        Constant.HALFBOXINTERVAL = 0.25f * Constant.CUBESIZE;
        Constant.MOVEDISTANCE = 2.5f * Constant.CUBESIZE;
        Constant.BOARDFRAMEDEPTH = 1.5f * Constant.CUBESIZE;
        Constant.BOARDFRAMETHICKNESS = 0.25f * Constant.CUBESIZE;
    }
}

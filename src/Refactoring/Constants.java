package Refactoring;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class Constants {

	private static float CUBESIZE;
	public static float BOXINTERVAL;
	public static float HALFBOXINTERVAL;
	public static float FALLDISTANCE;

	//Movement Directions
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	public static final int TOP = 1;
	public static final int DOWN = -1;

	public Constants(float cubeSize) {
		setCUBESIZE(cubeSize);
		BOXINTERVAL = 0.5f * getCUBESIZE();
		HALFBOXINTERVAL = 0.25f * getCUBESIZE();
		FALLDISTANCE = 2.5f * getCUBESIZE();
	}

	public static float getCUBESIZE() {
		return CUBESIZE;
	}

	public static void setCUBESIZE(float CUBESIZE) {
		Constants.CUBESIZE = CUBESIZE;
	}
}

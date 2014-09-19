import com.jme3.system.AppSettings;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Main {
	public static TetrisBase app;

    public static void main(String[] args) {
		app = new TetrisBase();
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(120);
        app.setSettings(newSetting);
		app.start();
	}
}
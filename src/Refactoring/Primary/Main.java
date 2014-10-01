package Refactoring.Primary;

import Primary.T4Base;
import com.jme3.system.AppSettings;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Main {
	public static Primary.T4Base app;

    public static void main(String[] args) {
		app = new T4Base();
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(60);
        app.setSettings(newSetting);
		app.start();
	}
}
//REFACTORED STATUS: ON GOING.

package Refactoring.Primary;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;

import java.util.logging.Logger;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class T4Base extends SimpleApplication {
    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
	Logger logger = Logger.getLogger(T4Base.class.getName());

    @Override
    public void simpleInitApp() {

    }

	public AssetManager getAssetManager(){
		return assetManager;
	}

    public InputManager getInputManager(){
        return inputManager;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

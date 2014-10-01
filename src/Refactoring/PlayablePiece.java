package Refactoring;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PlayablePiece extends Piece {

	public PlayablePiece(String fileName, float cubeSize, Vector3f pos, ColorRGBA color, AssetManager assetManager, Control controller){
        super(fileName, cubeSize, pos, color, assetManager);
		if(controller != null && !controller.isSetSpatial()){
			addControl(controller);
		}

        //Align piece to board grid
        move(0, -(getChildren().get(getChildren().size()-1).getWorldBound().getCenter().y), 0);
	}
}

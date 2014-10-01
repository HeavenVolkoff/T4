package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Control.Control;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PlayablePiece extends Piece {

	private long startFallTime;
	private int pieceFallingTime;
	private boolean falling;
	private Vector3f[] boxAbsolutePoint;
	private boolean rotating;

	public PlayablePiece(String fileName, Vector3f pos, boolean falling, ColorRGBA color, Control controller){
        super(fileName, pos, color);

		if(controller != null && !controller.isSetSpatial()){
			addControl(controller);
		}

		this.rotating = false;
		this.startFallTime = System.nanoTime();
		this.pieceFallingTime = Constant.INITIALFALLINTERVAL;
		this.falling = falling;
		this.boxAbsolutePoint = new Vector3f[getNumBox()];

		int count = 0;
		for (Spatial geometry : super.getBoxGeometries()) {
			this.boxAbsolutePoint[count] = geometry.getWorldBound().getCenter();
			count++;
		}

		//Align piece to board grid
		move(0, -(getChildren().get(getChildren().size()-1).getWorldBound().getCenter().y), 0);
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public long getStartFallTime() {
		return startFallTime;
	}

	public void setStartFallTime(long startFallTime) {
		this.startFallTime = startFallTime;
	}

	public int getPieceFallingTime() {
		return pieceFallingTime;
	}

	public void setPieceFallingTime(int pieceFallingTime) {
		this.pieceFallingTime = pieceFallingTime;
	}

	public boolean isRotating() {
		return rotating;
	}

	public void setRotating(boolean rotating) {
		this.rotating = rotating;
	}

	public Vector3f[] getBoxAbsolutePoint() {
		return boxAbsolutePoint;
	}
}

//REFACTORED STATUS: OK.

package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Control.Model.Control;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.List;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PlayablePiece extends Piece {

	private long startFallTime;
	private int pieceFallingTime;
	private boolean falling;
	private Vector3f[] boxAbsolutePoint;
	private boolean rotating;
    private boolean controlable;

    //======================== Class Constructors ==========================//
    public PlayablePiece(String fileName, Vector3f pos, boolean falling, ColorRGBA color, Control controller){
        super(fileName, pos, color);

        if(controller != null && !controller.isSetSpatial()){
            addControl(controller);
        }

        this.rotating = false;
        this.controlable = false;
        this.startFallTime = System.nanoTime();
        this.pieceFallingTime = Constant.INITIALFALLINTERVAL;
        this.falling = falling;

        constructGeosAbsolutePoints();

        //Align piece to board grid
        if (getChild("Pivot0") != null && getChild("Pivot1") != null){
            move(0, -((getChildren().get(getChildren().size()-1).getWorldBound().getCenter().y+Constant.CUBESIZE/2) - pos.y), 0);
        }else{
            move(Constant.HALFBOXINTERVAL+Constant.CUBESIZE, -((getChildren().get(getChildren().size()-1).getWorldBound().getCenter().y+Constant.CUBESIZE/2) - pos.y), 0);
        }
    }
    //======================================================================//

    private void constructGeosAbsolutePoints() {
        this.boxAbsolutePoint = new Vector3f[getNumBox()];

        int count = 0;
        for (Spatial geometry : super.getBoxGeometries()) {
            this.boxAbsolutePoint[count] = geometry.getWorldBound().getCenter();
            count++;
        }
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

    public boolean isControlable() {
        return controlable;
    }

    public void setControlable(boolean controlable) {
        this.controlable = controlable;
    }
}

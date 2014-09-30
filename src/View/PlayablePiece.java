package View;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.Control;

/**
 * Created by blackpearl on 29/09/14.
 */
public class PlayablePiece extends Piece {

    //===================Constant===================//
    //Movement Directions
    public final int LEFT = -1;
    public final int RIGHT = 1;
    public final int TOP = 1;
    public final int DOWN = -1;
    //==============================================//

    private long startFallTime;
    private int pieceFallingTime;
    private boolean falling;
    private Vector3f[] boxAbsolutePoint;
    private boolean rotating;

    public PlayablePiece(float cubeSize, float posX, float posY, float posZ, String fileName, ColorRGBA color, AssetManager assetManager, Control controler) {
        super(cubeSize, posX, posY, posZ, fileName, color, assetManager);

        if (controler != null) {
            addControl(controler);
        }

        this.rotating = false;
        this.startFallTime = System.nanoTime();
        this.pieceFallingTime = 500;
        this.falling = true; // Start falling

        setBoxesAbsolutePos();
    }

    private void setBoxesAbsolutePos(){
        this.boxAbsolutePoint = new Vector3f[super.getNumBox()];
        int count = 0;
        for (Geometry geometry : super.getBoxGeometries()) {
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

    public int getPieceFallingTime() {
        return pieceFallingTime;
    }

    public void setPieceFallingTime(int pieceFallingTime) {
        this.pieceFallingTime = pieceFallingTime;
    }

    public long getStartFallTime() {
        return startFallTime;
    }

    public void setStartFallTime(long startFallTime) {
        this.startFallTime = startFallTime;
    }

    public Vector3f[] getBoxAbsolutePoint() {
        return boxAbsolutePoint;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }
}

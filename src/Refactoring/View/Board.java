//REFACTORED STATUS: ON GOING.

package Refactoring.View;

import Primary.Main;
import Refactoring.Control.Constant;
import Refactoring.Model.Alpha;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackpearl on 01/10/14.
 */
public class Board extends Frame{

    private Integer[][] geometryIndexMap;
    private int col;
    private int row;
    private float geoAlpha;

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    public Board(int col, int row){
		super("board", "BLR", new Vector3f(0, 0, 0), new Vector3f(col * Constant.MOVEDISTANCE, row * Constant.MOVEDISTANCE, Constant.BOARDFRAMEDEPTH), Constant.BOARDFRAMETHICKNESS, ColorRGBA.DarkGray);
		this.col = col;
		this.row = row;
		this.geometryIndexMap = new Integer[col][row];
        this.geoAlpha = 1;
	}

	public Vector3f[] posRelativeToBoard(Vector3f[] absolutePos){
		Vector3f[] pos = new Vector3f[absolutePos.length];
		for(int i = 0; i < absolutePos.length; i++){
			pos[i] = new Vector3f();
			pos[i].setX(Math.round((((absolutePos[i].distance(new Vector3f(getBarPos('L').getX(), absolutePos[i].getY(), 0))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
			pos[i].setY(Math.round((((absolutePos[i].distance(new Vector3f(absolutePos[i].getX(), getBarPos('B').getY(), 0))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
			pos[i].setZ(0);
		}
		return pos;
	}

	public boolean hitBottom(Vector3f[] pieceGeoAbsolutePos){
		Vector3f[] pos = posRelativeToBoard(pieceGeoAbsolutePos);
		for (Vector3f geoPos : pos){
			if(geoPos.getY() == 0 || ((int)geoPos.getY()<row && geometryIndexMap[(int) geoPos.getX()][(int) geoPos.getY() - 1] != null)){
				return true;
			}
		}
		return false;
	}

	public boolean hitLeft(Vector3f[] pieceGeoAbsolutePos){
		Vector3f[] pos = posRelativeToBoard(pieceGeoAbsolutePos);
		for (Vector3f geoPos : pos){
			if(geoPos.getX() == 0 || geometryIndexMap[(int) geoPos.getX() - 1][(int) geoPos.getY()] != null){
				return true;
			}
		}
		return false;
	}

	public boolean hitRigth(Vector3f[] pieceGeoAbsolutePos){
		Vector3f[] pos = posRelativeToBoard(pieceGeoAbsolutePos);
		for (Vector3f geoPos : pos){
			if(geoPos.getX() == (this.col - 1) || geometryIndexMap[(int) geoPos.getX() + 1][(int) geoPos.getY()] != null){
				return true;
			}
		}
		return false;
	}

	public boolean addPiece(Vector3f[] pieceGeoAbsolutePos, Material mat){
		int count = 0;
		Vector3f[] pos = posRelativeToBoard(pieceGeoAbsolutePos);
		for (Vector3f geoPos : pos){
			if ((int)geoPos.getY() < row) {
				Geometry geo = new Geometry("Box" + String.valueOf((int) geoPos.getX()) + String.valueOf((int) geoPos.getY()), new Box(Constant.CUBESIZE, Constant.CUBESIZE, Constant.CUBESIZE));
				geo.setLocalTranslation(pieceGeoAbsolutePos[count].getX(), pieceGeoAbsolutePos[count].getY(), this.pos.getZ());
				geo.setMaterial(mat);
				geometryIndexMap[(int)geoPos.getX()][(int)geoPos.getY()] = attachChild(geo);
				count++;
			}else{
				return false;
			}
		}
		Main.app.getScore().updateScore(1,10);//NOT REFACTORED YET
		return true;
	}

	public List<Integer> getCompleteLines(){
		List<Integer> completedLines = new ArrayList<Integer>();

		for(int lineIndex = 0; lineIndex < row; lineIndex++){
			int count = 0;
			while(geometryIndexMap[lineIndex][count] != null && count < col){
				count++;
			}
			if(count == col){
				completedLines.add(lineIndex);
			}
		}
		return completedLines;
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
	public void destroyCompletedLines(){
		List<Integer> completedLines = getCompleteLines();
        for (int i = 0; i < completedLines.size(); i++) {
            Main.app.getScore().updateScore(i + 1, 100);//NOT REFACTORED YET
            for (Integer geoIndex : geometryIndexMap[completedLines.get(i)]) {
                detachChildAt(geoIndex);
                geoIndex = null;
            }
        }
        //REGROUP LINES
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

//REFACTORED STATUS: OK.

package Refactoring.View;

import Old.Primary.Main;
import Refactoring.Control.Constant;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackpearl on 01/10/14.
 */

    /* TODO: Old.View.Board Modifications:
    - Modular Old.View.Board
    - Old.View.Board Inventor
    - Tornado Mode
    - Pendulum Light
    - Closing Wall
    */

public class Board extends Frame{

    private Integer[][] geometryIndexMap;
    private int col;
    private int row;

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    //======================== Class Constructors ==========================//
    public Board(int col, int row){
		super("board", Constant.RIGHT+Constant.BOTTOM+Constant.LEFT, new Vector3f(0, 0, 0), new Vector3f(col * Constant.MOVEDISTANCE, row * Constant.MOVEDISTANCE, Constant.BOARDFRAMEDEPTH), Constant.BOARDFRAMETHICKNESS, Constant.BOARDCOLOR);
		this.col = col;
		this.row = row;
		this.geometryIndexMap = new Integer[col][row];
	}
    //======================================================================//

	public Vector3f[] posRelativeToBoard(Vector3f[] absolutePos){
		Vector3f[] pos = new Vector3f[absolutePos.length];
		for(int i = 0; i < absolutePos.length; i++){
			pos[i] = new Vector3f();
			pos[i].setX(Math.round((((absolutePos[i].distance(new Vector3f(getBarPos(Constant.LEFT).getX(), absolutePos[i].getY(), 0))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
			pos[i].setY(Math.round((((absolutePos[i].distance(new Vector3f(absolutePos[i].getX(), getBarPos(Constant.BOTTOM).getY(), 0))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
			pos[i].setZ(0);
		}
		return pos;
	}

    public Vector2f posRelativeToBoard(Vector2f absolutePos){
        Vector2f pos = new Vector2f();
        pos.setX(Math.round((((absolutePos.distance(new Vector2f(getBarPos(Constant.LEFT).getX(), absolutePos.getY()))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
        pos.setY(Math.round((((absolutePos.distance(new Vector2f(absolutePos.getX(), getBarPos(Constant.BOTTOM).getY()))) - (Constant.BOARDFRAMETHICKNESS + Constant.CUBESIZE)) / Constant.MOVEDISTANCE)));
        return pos;
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
		//Main.app.getScore().updateScore(1,10);//NOT REFACTORED YET
		return true;
	}

	public List<Integer> getCompleteLines(){
		List<Integer> completedLines = new ArrayList<Integer>();

		for(int lineIndex = 0; lineIndex < col; lineIndex++){
			int count = 0;
			while(count < row && geometryIndexMap[lineIndex][count] != null){
				count++;
			}
			if(count == row){
				completedLines.add(lineIndex);
			}
		}
		return completedLines;
	}

	public void destroyCompletedLines(){
		List<Integer> completedLines = getCompleteLines();
        for (int i = 0; i < completedLines.size(); i++) {
            Main.app.getScore().updateScore(i + 1, 100);//NOT REFACTORED YET
            for (Integer geoIndex : geometryIndexMap[completedLines.get(i)]) {
                detachChildAt(geoIndex);
                geoIndex = null;
            }
            for (int j = completedLines.get(i)+1; j < row-1; j++){//NO EFFECT REGROUP LINES
                for (int k = 0; k <  geometryIndexMap[completedLines.get(j)].length; k++) {
                    if (geometryIndexMap[completedLines.get(j)][k] != null) {
                        getChild(geometryIndexMap[completedLines.get(j)][k]).move(0, -Constant.MOVEDISTANCE, 0);
                        geometryIndexMap[completedLines.get(j) - 1][k] = geometryIndexMap[completedLines.get(j)][k];
                        geometryIndexMap[completedLines.get(j)][k] = null;
                    }
                }
            }
        }
	}

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Integer[][] getGeometryIndexMap() {
        return geometryIndexMap;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

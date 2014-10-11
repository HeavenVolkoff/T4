//REFACTORED STATUS: OK.

package Refactoring.View;

import Refactoring.Control.Constant;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
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

    private String[][] geometryIndexMap;
    private int col;
    private int row;

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    //======================== Class Constructors ==========================//
    public Board(int col, int row){
		super("board", Constant.RIGHT+Constant.BOTTOM+Constant.LEFT, new Vector3f(0, 0, 0), new Vector3f(col * Constant.MOVEDISTANCE, row * Constant.MOVEDISTANCE, Constant.BOARDFRAMEDEPTH), Constant.BOARDFRAMETHICKNESS, Constant.BOARDCOLOR);
		this.col = col;
		this.row = row;
		this.geometryIndexMap = new String[row][col];
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
                geometryIndexMap[(int)geoPos.getY()][(int)geoPos.getX()] = "Box" + String.valueOf((int) geoPos.getX()) + String.valueOf((int) geoPos.getY());
				geo.setLocalTranslation(pieceGeoAbsolutePos[count].getX(), pieceGeoAbsolutePos[count].getY(), this.pos.getZ());
				geo.setMaterial(mat);
				attachChild(geo);
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

		for(int lineIndex = 0; lineIndex < row; lineIndex++){
			int count = 0;
			while(count < col && geometryIndexMap[lineIndex][count] != null){
				count++;
			}
			if(count == col){
				completedLines.add(lineIndex);
			}
		}
		return completedLines;
	}

	public void destroyCompletedLines(){
		List<Integer> completedLines = getCompleteLines();
        int correction = 0;

        for (int lineNum = completedLines.size()-1; lineNum >= 0; lineNum--) {
            Integer completedLine = completedLines.get(lineNum);
            //Main.app.getScore().updateScore(i + 1, 100);//NOT REFACTORED YET
            for (int i = 0; i < col; i++) {
                detachChildNamed(geometryIndexMap[completedLine][i]);
                geometryIndexMap[completedLine][i] = null;
            }

            for (int j = completedLine + 1; j < row; j++) {//NO EFFECT REGROUP LINES
                for (int k = 0; k < col; k++) {
                    if (geometryIndexMap[j][k] != null) {
                        Spatial geo = getChild(geometryIndexMap[j][k]);
                        geo.move(0, -Constant.MOVEDISTANCE, 0);
                        geo.setName("Box" + String.valueOf(k) + String.valueOf(j - 1));
                        geometryIndexMap[j - 1][k] = "Box" + String.valueOf(k) + String.valueOf(j - 1);
                        geometryIndexMap[j][k] = null;
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

    public String[][] getGeometryIndexMap() {
        return geometryIndexMap;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

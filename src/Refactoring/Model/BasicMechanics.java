//REFACTORED STATUS: ON GOING.

package Refactoring.Model;

import Refactoring.Control.Constant;
import Refactoring.View.Board;
import Refactoring.View.PlayablePiece;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Created by blackpearl on 04/10/14.
 */

public final class BasicMechanics {

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    public static boolean hitBottom(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getY() == 0 || ((int)geoPos.getY()<board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getX()][(int) geoPos.getY() - 1] != null)){
                return true;
            }
        }
        return false;
    }

    public static boolean hitLeft(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getX() == 0 || board.getGeometryIndexMap()[(int) geoPos.getX() - 1][(int) geoPos.getY()] != null){
                return true;
            }
        }
        return false;
    }

    public static boolean hitRigth(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getX() == (board.getCol() - 1) || board.getGeometryIndexMap()[(int) geoPos.getX() + 1][(int) geoPos.getY()] != null){
                return true;
            }
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////

    //P0////11//
    //22////P0//

    /*
------------------------------------------------------
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||  ||  ||  ||  ||  |   -2
    |  ||  ||aa||aa||aa||aa||  |   -1
    |  ||  ||aa||P0||00||aa||  |    0
    |  ||  ||aa||00||P1||aa||  |    1
    |  ||  ||aa||aa||aa||aa||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4

    Clockwise P0.x < P1.x:
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||  ||  ||  ||  ||  |   -2
    |  ||aa||aa||aa||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||P1||00||aa||  ||  |    1
    |  ||aa||aa||aa||aa||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a esquerda => Correção X+1)

------------------------------------------------------
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||  ||  ||  ||  ||  |   -2
    |  ||aa||aa||aa||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||P1||00||aa||  ||  |    1
    |  ||aa||aa||aa||aa||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4

    Clockwise P0.x > P1.x:
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||aa||aa||aa||aa||  ||  |   -2
    |  ||aa||P1||00||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||aa||aa||aa||  ||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a cima => Correção Y+1)

	  0   1   2   3   4   5   6
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3  0
    |  ||aa||aa||aa||aa||  ||  |   -2  1
    |  ||aa||P1||00||aa||  ||  |   -1  2
    |  ||aa||00||P0||aa||  ||  |    0  3
    |  ||aa||aa||aa||aa||  ||  |    1  4
    |  ||  ||  ||  ||  ||  ||  |    2  5
    |  ||  ||  ||  ||  ||  ||  |    3  6
    |  ||  ||  ||  ||  ||  ||  |    4  7
     */


    private int[][] buildRotationMatrix(PlayablePiece piece, int angle, Board board) {
        int[][] matrix = new int[2*piece.getNumBox()+1][2*piece.getNumBox()+1];

        Vector2f[] pivotPos = new Vector2f[2];
        for (Spatial geo : piece.getBoxGeometries()){
            if (geo.getName().equals("Pivot")){
                if (pivotPos[0] == null){
                    pivotPos[0] = new Vector2f(geo.getWorldBound().getCenter().x,geo.getWorldBound().getCenter().y);
                }else if(pivotPos[1] == null){
                    pivotPos[1] = new Vector2f(geo.getWorldBound().getCenter().x,geo.getWorldBound().getCenter().y);
                    break;
                }
            }
        }
		if (pivotPos[0] != null){
			Vector2f matrixPivotPos = board.posRelativeToBoard(pivotPos[0]);

			if (angle == Constant.CLOCKWISE) {
				for (Vector3f geoPos : piece.getBoxAbsolutePoint()) {
					matrix[(int) matrixPivotPos.getX() - (int) geoPos.getX() + piece.getNumBox()][(int) matrixPivotPos.getY() - (int) geoPos.getY() + piece.getNumBox()] = 1;
				}
			}else if(angle == Constant.COUNTERCLOCKWISE){
				for (Vector3f geoPos : piece.getBoxAbsolutePoint()) {
					matrix[piece.getNumBox() + ((int) matrixPivotPos.getX() - (int) geoPos.getX()) * -1][piece.getNumBox() + ((int) matrixPivotPos.getY() - (int) geoPos.getY()) * -1] = 1;
				}
			}
			matrix[piece.getNumBox()][piece.getNumBox()] = 2;
		}
        if(pivotPos[1] != null) {
            if (pivotPos[0].x > pivotPos[1].x){
				for (int[] line : matrix){
					for (int i = 1; i < line.length - 1; i++){
						line[i] = line[i-1];
					}
				}
			}else{
				for (int line = 1; line < matrix.length - 2; line++){
					for(int i = 0; i < matrix[line].length - 1; i++){
						matrix[line][i] = matrix[line - 1][i];
					}
				}
			}
        }
		return matrix;
    }

	public static boolean canRotate(){
		return true;
	}

    public boolean canRotate(PlayablePiece piece, int angle) {
        return false;
    }

    public void setGameOver(boolean gameOver, float alphaVal) {
    }

    public boolean isGameOver(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

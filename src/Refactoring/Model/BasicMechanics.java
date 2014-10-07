//REFACTORED STATUS: ON GOING.

package Refactoring.Model;

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
        if(angle == 90) {
            Vector2f matrixPivotPos = board.posRelativeToBoard(pivotPos[0]);
            if (pivotPos[0].x > pivotPos[1].x){

            }
        }


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

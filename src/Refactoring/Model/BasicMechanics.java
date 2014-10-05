//REFACTORED STATUS: ON GOING.

package Refactoring.Model;

import Refactoring.View.Board;
import Refactoring.View.PlayablePiece;
import com.jme3.math.Vector3f;

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
    private int[][] buildRotationMatrix(PlayablePiece piece, int angle) {
        return new int[0][];
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

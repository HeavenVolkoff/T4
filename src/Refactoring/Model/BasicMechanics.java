//REFACTORED STATUS: ON GOING.

package Refactoring.Model;

import Refactoring.Control.Constant;
import Refactoring.View.Board;
import Refactoring.View.PlayablePiece;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.lwjgl.Sys;

import java.util.Arrays;

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
            if(geoPos.getX() == 0 || (geoPos.getY() < board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getX() - 1][(int) geoPos.getY()] != null)){
                return true;
            }
        }
        return false;
    }

    public static boolean hitRight(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getX() == (board.getCol() - 1) || (geoPos.getY() < board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getX() + 1][(int) geoPos.getY()] != null)){
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




      0   1   2   3   4   5   6   7   8   9
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 19
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 18
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 17
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 16
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 15
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 14
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 13
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 12
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 11
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 10
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  |  9
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  |  8
    |  ||  ||  ||p0||pp||  ||  ||  ||  ||  |  7
    |  ||  ||  ||pp||p1||pp||pp||  ||  ||  |  6
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  |  5
    |  ||  ||  ||55||  ||  ||  ||  ||  ||  |  4
    |  ||  ||  ||55||  ||  ||  ||  ||  ||  |  3
    |  ||  ||  ||55||22||  ||  ||  ||  ||  |  2
    |  ||11||  ||55||22||33||33||  ||44||44|  1
    |11||11||11||22||22||33||33||44||44||  |  0

    |  ||  ||  ||  ||  ||  ||  ||  |
    |  ||  ||  ||  ||  ||  ||  ||  |
    |  ||  ||  ||  ||  ||  ||  ||  |
    |  ||  ||  ||pp||p0||  ||  ||  |
    |  ||  ||  ||p1||pp||  ||  ||  |
    |  ||  ||  ||pp||  ||  ||  ||  |
    |  ||  ||  ||pp||  ||  ||  ||  |
    |  ||  ||  ||  ||  ||  ||  ||  |
    |  ||  ||  ||  ||  ||  ||  ||  |
    |  ||  ||  ||  ||  ||  ||  ||  |

      0   1   2   3   4   5   6   7   8   9
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 19
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 18
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 17
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 16
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 15
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 14
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 13
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 12
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 11
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  | 10
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  |  9
    |  ||  ||  ||  ||  ||  ||  ||  ||  ||  |  8
    |  ||  ||pp||p0||  ||  ||  ||  ||  ||  |  7
    |  ||  ||p1||pp||  ||  ||  ||  ||  ||  |  6
    |  ||  ||pp||  ||  ||  ||  ||  ||  ||  |  5
    |  ||  ||pp||55||  ||  ||  ||  ||  ||  |  4
    |  ||  ||  ||55||  ||  ||  ||  ||  ||  |  3
    |  ||  ||  ||55||22||  ||  ||  ||  ||  |  2
    |  ||11||  ||55||22||33||33||  ||44||44|  1
    |11||11||11||22||22||33||33||44||44||  |  0
     */


    private static int[][] buildRotationMatrix(PlayablePiece piece, Vector2f[] pivotPos, int angle, Board board) {
        int[][] matrix = new int[2*piece.getNumBox()+1][2*piece.getNumBox()+1];

        Spatial[] pivots = {piece.getChild("Pivot0"), piece.getChild("Pivot1")};
        pivotPos[0] = new Vector2f(pivots[0].getWorldBound().getCenter().x,pivots[0].getWorldBound().getCenter().y);
        if (pivots[1] != null) {
            pivotPos[1] = new Vector2f(pivots[1].getWorldBound().getCenter().x,pivots[1].getWorldBound().getCenter().y);
        }

       	if (pivotPos[0] != null){
			Vector2f matrixPivotPos = board.posRelativeToBoard(pivotPos[0]);

			if (angle == Constant.CLOCKWISE) {
                for (Vector3f geoPos : board.posRelativeToBoard(piece.getBoxAbsolutePoint())) {
                    matrix[piece.getNumBox() + ((int) matrixPivotPos.getX() - (int) geoPos.getX()) * -1][piece.getNumBox() + ((int) matrixPivotPos.getY() - (int) geoPos.getY()) * -1] = 1;
                }
			}else if(angle == Constant.COUNTERCLOCKWISE){
                for (Vector3f geoPos : board.posRelativeToBoard(piece.getBoxAbsolutePoint())) {
                    matrix[(int) matrixPivotPos.getX() - (int) geoPos.getX() + piece.getNumBox()][(int) matrixPivotPos.getY() - (int) geoPos.getY() + piece.getNumBox()] = 1;
                }
			}
			matrix[piece.getNumBox()][piece.getNumBox()] = 2;
		}
		return matrix;
    }

	public static boolean canRotate(PlayablePiece piece, int angle, Board board){
        Vector2f[] pivotPos = new Vector2f[2];
        int [][] matrix = buildRotationMatrix(piece, pivotPos, angle, board);

        for(int[] line : matrix){
            for(int pos : line){
                System.out.print(pos);
            }
            System.out.println("");
        }
        System.out.println("");

        int boxBoardPosX;
        int boxBoardPosY;
        Vector2f pivotMatrixPos = new Vector2f();
        Vector2f pivotBoardPos = new Vector2f();

        //Get Pivot Matrix Pos
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix.length; j++){
                if (matrix[i][j]==2){
                    pivotMatrixPos.setX(i);
                    pivotMatrixPos.setY(j);
                    break;
                }
            }
        }
        //Get Pivot Board Pos
        Spatial pivot = piece.getChild("Pivot0");
        pivotBoardPos = board.posRelativeToBoard(new Vector2f(pivot.getWorldBound().getCenter().x,pivot.getWorldBound().getCenter().y));

        System.out.println(pivotBoardPos);

        if(pivotPos[1] != null) {
            if (pivotPos[0].x < pivotPos[1].x){
                pivotBoardPos.x += 1;
            }else{
                pivotBoardPos.y -= 1;
            }
        }

        //Verify Board & Piece Transposition
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix.length; j++){
                if (matrix[i][j]==1){
                    boxBoardPosY = (int)pivotBoardPos.getY()+ (i-(int)pivotMatrixPos.getX())*-1;
                    boxBoardPosX = (int)pivotBoardPos.getX()+ (j-(int)pivotMatrixPos.getY());
                    if (boxBoardPosX < 0 || boxBoardPosX >= board.getCol() || boxBoardPosY < 0){
                        return false;
                    }
                    if (boxBoardPosX >= 0 && boxBoardPosY >= 0 && boxBoardPosX < board.getCol() && boxBoardPosY < board.getRow() && board.getGeometryIndexMap()[boxBoardPosX][boxBoardPosY] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
	}

    public void setGameOver(boolean gameOver, float alphaVal) {
    }

    public boolean isGameOver(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

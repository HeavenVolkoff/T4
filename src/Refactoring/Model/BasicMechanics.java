//REFACTORED STATUS: OK.

package Refactoring.Model;

import Refactoring.Primary.Main;
import Refactoring.Control.Constant;
import Refactoring.View.Board;
import Refactoring.View.Piece;
import Refactoring.View.PlayablePiece;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Created by blackpearl on 04/10/14.
 */

public final class BasicMechanics {

    static boolean gameOver = false;

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    public static boolean hitBottom(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getY() == 0 || ((int)geoPos.getY()<board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getY() - 1][(int) geoPos.getX()] != null)){
                return true;
            }
        }
        return false;
    }

    public static boolean hitLeft(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getX() == 0 || (geoPos.getY() < board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getY()][(int) geoPos.getX() - 1] != null)){
                return true;
            }
        }
        return false;
    }

    public static boolean hitRight(Vector3f[] pieceGeoAbsolutePos, Board board){
        Vector3f[] pos = board.posRelativeToBoard(pieceGeoAbsolutePos);
        for (Vector3f geoPos : pos){
            if(geoPos.getX() == (board.getCol() - 1) || (geoPos.getY() < board.getRow() && board.getGeometryIndexMap()[(int) geoPos.getY()][(int) geoPos.getX() + 1] != null)){
                return true;
            }
        }
        return false;
    }

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

    private static Vector2f correctPivotPos(Vector2f[] pivotPos, int angle){
        Vector2f correction = new Vector2f(0,0);
        if (angle == -90) {
            if (pivotPos[0].y > pivotPos[1].y) { //A cima
                if (pivotPos[0].x > pivotPos[1].x) { //A direita
                    correction.y -= 1;
                } else { //A esquerda
                    correction.x += 1;
                }
            } else { //A Baixo
                if (pivotPos[0].x > pivotPos[1].x) { //A direita
                    correction.x -= 1;
                } else { //A esquerda
                    correction.y += 1;
                }
            }
        }else if (angle == 90){
            if (pivotPos[0].y > pivotPos[1].y) { //A cima
                if (pivotPos[0].x > pivotPos[1].x) { //A direita
                    correction.x -= 1;
                } else { //A esquerda
                    correction.y -= 1;
                }
            } else { //A Baixo
                if (pivotPos[0].x > pivotPos[1].x) { //A direita
                    correction.y += 1;
                } else { //A esquerda
                    correction.x += 1;
                }
            }
        }
        return correction;
    }

	public static boolean canRotate(PlayablePiece piece, int angle, Board board){
        Vector2f[] pivotPos = new Vector2f[2];
        int [][] matrix = buildRotationMatrix(piece, pivotPos, angle, board);

        int boxBoardPosX;
        int boxBoardPosY;
        Vector2f pivotMatrixPos = new Vector2f();
        Vector2f pivotBoardPos;

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

        if(pivotPos[1] != null) {
            Vector2f correction = correctPivotPos(pivotPos, angle);
            pivotBoardPos.x += correction.x;
            pivotBoardPos.y += correction.y;
        }

        //Verify Board & Piece Transposition
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix.length; j++){
                if (matrix[i][j]==1){
                    boxBoardPosY = (int)pivotBoardPos.getY()+ (i-(int)pivotMatrixPos.getX())*-1;
                    boxBoardPosX = (int)pivotBoardPos.getX()+ (j-(int)pivotMatrixPos.getY());
                    if (boxBoardPosX < 0 || boxBoardPosX >= board.getCol() || boxBoardPosY < 0 || boxBoardPosY >= board.getRow()){
                        return false;
                    }else if (board.getGeometryIndexMap()[boxBoardPosY][boxBoardPosX] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
	}

    public static void gameOver(float alphaVal) {
        gameOver = true;
        Main.app.getBoard().setAlpha(alphaVal);
        Main.app.getNextPiece().setAlpha(alphaVal);
        //Add LevelBar, and Score Alpha
        Main.app.setCurrentPiece(null);
        Main.app.setMessages(new Piece("GameOver.piece", new Vector3f(0,0,+1), ColorRGBA.White));
    }

    public static boolean isGameOver(Vector3f[] pieceBoxesAbsolutePos){
        for(Vector3f boxPos : Main.app.getBoard().posRelativeToBoard(pieceBoxesAbsolutePos)){
            if(boxPos.getY() <= Main.app.getBoard().getRow()){
                if(Main.app.getBoard().getGeometryIndexMap()[(int)boxPos.getY() - 1][(int)boxPos.getX()] != null){
                    gameOver(0.1f);
                    return true;
                }
            }else if(Main.app.getBoard().getGeometryIndexMap()[Main.app.getBoard().getRow() - 1][(int)boxPos.getX()] != null) {
                gameOver(0.1f);
                return true;
            }
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

////////////////////////////////////////////////////////////////WIKI////////////////////////////////////////////////////
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

    Clockwise
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
    |  ||  ||aa||aa||aa||aa||  |   -1
    |  ||  ||aa||P0||00||aa||  |    0
    |  ||  ||aa||00||P1||aa||  |    1
    |  ||  ||aa||aa||aa||aa||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4

    CouterClockwise
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||aa||aa||aa||aa||  |   -2
    |  ||  ||aa||00||P1||aa||  |   -1
    |  ||  ||aa||P0||00||aa||  |    0
    |  ||  ||aa||aa||aa||aa||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a esquerda => Correção Y-1)

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

    Clockwise
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||aa||aa||aa||aa||  ||  |   -2
    |  ||aa||P1||00||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||aa||aa||aa||  ||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a cima => Correção Y-1)
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

    CouterClockwise
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||  ||  ||  ||  ||  |   -2
    |  ||  ||aa||aa||aa||aa||  |   -1
    |  ||  ||aa||P0||00||aa||  |    0
    |  ||  ||aa||00||P1||aa||  |    1
    |  ||  ||aa||aa||aa||aa||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a cima => Correção X-1)
------------------------------------------------------
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||aa||aa||aa||aa||  |   -2
    |  ||  ||aa||00||P1||aa||  |   -1
    |  ||  ||aa||P0||00||aa||  |    0
    |  ||  ||aa||aa||aa||aa||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4

    CouterClockwise
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||aa||aa||aa||aa||  ||  |   -2
    |  ||aa||P1||00||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||aa||aa||aa||  ||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a cima => Correção X+1)
------------------------------------------------------
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||aa||aa||aa||aa||  ||  |   -2
    |  ||aa||P1||00||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||aa||aa||aa||  ||  |    1
    |  ||  ||  ||  ||  ||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4

    CouterClockwise
     -3  -2  -1   0   1   2   3
    |  ||  ||  ||  ||  ||  ||  |   -3
    |  ||  ||  ||  ||  ||  ||  |   -2
    |  ||aa||aa||aa||aa||  ||  |   -1
    |  ||aa||00||P0||aa||  ||  |    0
    |  ||aa||P1||00||aa||  ||  |    1
    |  ||aa||aa||aa||aa||  ||  |    2
    |  ||  ||  ||  ||  ||  ||  |    3
    |  ||  ||  ||  ||  ||  ||  |    4
    (Anda para a cima => Correção Y+1)

------------------------------------------------------
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

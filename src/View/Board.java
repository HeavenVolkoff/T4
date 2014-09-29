package View;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import Primary.Main;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

/* TODO: View.Board Modifications:
    - Modular View.Board
    - View.Board Inventor
    - Tornado Mode
    - Pendulum Light
    - Closing Wall
*/
/*
	TODO: Bug-Fix:
	 - Crazy GameOver with Cube and I pieces
 */
public class Board extends Node {

    private Geometry[][] geoMap;
    private float cubeSize;
	private int col;
	private int row;
    private boolean gameOver;
    private Geometry[] frame;

    public Board(int col, int row, float cubeSize, AssetManager assetManager){
        this.cubeSize = cubeSize;
        this.frame = new Geometry[3];
        this.col = col;
        this.row = row;
        this.gameOver = false;
        this.geoMap = new Geometry[col][row];

        //============================== Frame Material Def =======================
        Material frameMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        frameMaterial.setColor("Ambient", ColorRGBA.DarkGray);
        frameMaterial.setColor("Diffuse", ColorRGBA.DarkGray);
        frameMaterial.setColor("Specular", ColorRGBA.DarkGray);
        frameMaterial.setFloat("Shininess", 2);
        frameMaterial.setBoolean("UseMaterialColors", true);
        //=========================================================================

        buildFrames(frameMaterial);
    }

    private void buildFrames(Material frameMaterial) {
        Box bottom = new Box(col * cubeSize * 1.25f, cubeSize * 0.25f, cubeSize * 1.5f);
        frame[0] = new Geometry("BottomBoard", bottom);
        frame[0].setLocalTranslation(new Vector3f(0, -row * cubeSize * 1.25f, 0));
        frame[0].setMaterial(frameMaterial);
        attachChild(frame[0]);

        Box Left = new Box(cubeSize * 0.25f, row * cubeSize * 1.25f, cubeSize * 1.5f);
        frame[1] = new Geometry("LeftBoard", Left);
        frame[1].setLocalTranslation(new Vector3f(-(col * cubeSize * 1.25f), 0, 0));
        frame[1].setMaterial(frameMaterial);
        attachChild(frame[1]);

        Box Right = new Box(cubeSize * 0.25f, row * cubeSize * 1.25f, cubeSize * 1.5f);
        frame[2] = new Geometry("RightBoard", Right);
        frame[2].setLocalTranslation(new Vector3f(+(col * cubeSize * 1.25f), 0, 0));
        frame[2].setMaterial(frameMaterial);
        attachChild(frame[2]);
    }

    private Vector3f[] piecePosRelativeToBoard(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        Vector3f[] pos = new Vector3f[boxNum];
        for(int i = 0; i < pieceBoxesAbsolutePos.length; i++){
            pos[i] = new Vector3f();
            pos[i].setX(Math.round((((pieceBoxesAbsolutePos[i].distance(new Vector3f(frame[1].getWorldTranslation().getX(), pieceBoxesAbsolutePos[i].getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
            pos[i].setY(Math.round((((pieceBoxesAbsolutePos[i].distance(new Vector3f(pieceBoxesAbsolutePos[i].getX(), frame[0].getWorldTranslation().getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
            pos[i].setZ(0);
        }
        return pos;
    }

    private Vector3f boxPosRelativeToBoard(Vector3f BoxAbsolutePos){
        Vector3f pos = new Vector3f();
        pos.setX(Math.round((((BoxAbsolutePos.distance(new Vector3f(frame[1].getWorldTranslation().getX(), BoxAbsolutePos.getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
        pos.setY(Math.round((((BoxAbsolutePos.distance(new Vector3f(BoxAbsolutePos.getX(), frame[0].getWorldTranslation().getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
        pos.setZ(0);
        return pos;
    }

    private int[][] buildRotationMatrix(PlayablePiece piece, int angle){
        int[][] matrix = new int[2*piece.getNumBox()+1][2*piece.getNumBox()+1];
        Geometry pivot = null;
        Vector3f pos;
        for (Geometry geo : piece.getBoxGeometries()){
            if (geo.getName().equals("Pivot")){
                pivot = geo;
                break;
            }
        }
        if (pivot != null){
            if (angle == 90) {
                pos = boxPosRelativeToBoard(pivot.getWorldBound().getCenter());
                for (Vector3f geo : piecePosRelativeToBoard(piece.getBoxAbsolutePoint(), piece.getNumBox())) {
                    if ((int) pos.getX() - (int) geo.getX() == 0 && (int) pos.getY() - (int) geo.getY() == 0) {
                        matrix[piece.getNumBox()][piece.getNumBox()] = 2;
                    } else {
                        if (piece.getInvert() == 0) {
                            matrix[(int) pos.getX() - (int) geo.getX() + piece.getNumBox()][(int) pos.getY() - (int) geo.getY() + piece.getNumBox()] = 1;
                        } else {
                            matrix[piece.getNumBox() + ((int) pos.getX() - (int) geo.getX()) * -1][piece.getNumBox() + ((int) pos.getY() - (int) geo.getY()) * -1] = 1;
                        }
                    }
                }
            }else{
                pos = boxPosRelativeToBoard(pivot.getWorldBound().getCenter());
                for (Vector3f geo : piecePosRelativeToBoard(piece.getBoxAbsolutePoint(), piece.getNumBox())) {
                    if ((int) pos.getX() - (int) geo.getX() == 0 && (int) pos.getY() - (int) geo.getY() == 0) {
                        matrix[piece.getNumBox()][piece.getNumBox()] = 2;
                    } else {
                        if (piece.getInvert() == 0) {
                            matrix[piece.getNumBox() + ((int) pos.getX() - (int) geo.getX()) * -1][piece.getNumBox() + ((int) pos.getY() - (int) geo.getY()) * -1] = 1;
                        } else {
                            matrix[piece.getNumBox() + ((int) pos.getX() - (int) geo.getX()) * +1][piece.getNumBox() + ((int) pos.getY() - (int) geo.getY()) * +1] = 1;
                        }
                    }
                }
            }
        }
        return matrix;
    }

    public boolean canRotate(PlayablePiece piece, int angle){
        int [][] matrix = buildRotationMatrix(piece, angle);
        int boxBoardPosX;
        int boxBoardPosY;
        Vector3f pivotMatrixPos = new Vector3f();
        Vector3f pivotBoardPos = new Vector3f();

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
        //Get Pivot View.Board Pos
        for (Geometry geo : piece.getBoxGeometries()){
            if (geo.getName().equals("Pivot")){
                pivotBoardPos = boxPosRelativeToBoard(geo.getWorldBound().getCenter());
                break;
            }
        }
        //Verify View.Board & View.Piece Transposition
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix.length; j++){
                if (matrix[i][j]==1){
                    boxBoardPosY = (int)pivotBoardPos.getY()+ (i-(int)pivotMatrixPos.getX())*-1;
                    boxBoardPosX = (int)pivotBoardPos.getX()+ (j-(int)pivotMatrixPos.getY());
                    if (boxBoardPosX < 0 || boxBoardPosX >= col || boxBoardPosY < 0){
                        return false;
                    }
                    if (boxBoardPosX >= 0 && boxBoardPosY >= 0 && boxBoardPosX < col && boxBoardPosY < row && geoMap[boxBoardPosX][boxBoardPosY] != null) {
                        return false;
                    }
                }
            }
        }
		return true;
    }

    public boolean isGameOver(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for(Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if(boxPos.getY() <= row){
                if(geoMap[(int)boxPos.getX()][(int)boxPos.getY() - 1] != null){
                    return true;
                }
            }else if(geoMap[(int)boxPos.getX()][row - 1] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hitBottomFrame(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if (boxPos.getY() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hitBottomPiece(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if ((int)boxPos.getX()<col && (int)boxPos.getY()<row && (int)boxPos.getY()-1>=0) {
                if (geoMap[(int) boxPos.getX()][(int) boxPos.getY() - 1] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hitLeftFrame(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if (boxPos.getX() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hitLeftPiece(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if ((int)boxPos.getX()<col && (int)boxPos.getY()<row && (int)boxPos.getX()-1>=0) {
                if (geoMap[(int) boxPos.getX()-1][(int) boxPos.getY()] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hitRightFrame(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if (boxPos.getX() == col-1) {
                return true;
            }
        }
        return false;
    }

    public boolean hitRightPiece(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        for (Vector3f boxPos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if ((int)boxPos.getX()+1<col && (int)boxPos.getY()<row) {
                if (geoMap[(int) boxPos.getX()+1][(int) boxPos.getY()] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addPiece(Vector3f[] pieceBoxesAbsolutePos, int boxNum, Material mat){
        int count = 0;
        for (Vector3f piecePos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            if ((int)piecePos.getY() < row) {
				Box box = new Box(cubeSize, cubeSize, cubeSize);
				geoMap[(int) piecePos.getX()][(int) piecePos.getY()] = new Geometry("Box" + String.valueOf((int) piecePos.getX()) + String.valueOf((int) piecePos.getY()), box);
				geoMap[(int) piecePos.getX()][(int) piecePos.getY()].setLocalTranslation(pieceBoxesAbsolutePos[count].getX(), pieceBoxesAbsolutePos[count].getY(), 0);
				geoMap[(int) piecePos.getX()][(int) piecePos.getY()].setMaterial(mat);
				attachChild(geoMap[(int) piecePos.getX()][(int) piecePos.getY()]);
				count++;
			}else{
				return false;
			}
        }
        Main.app.getScore().updateScore(1,10);
		return true;
    }

    private void destroyLine(int line){
        //Erase Line
        for (int i = 0; i < col; i++){
            if (geoMap[i][line] != null){
                detachChild(geoMap[i][line]);
                geoMap[i][line] = null;
            }
        }
        //Move other lines
        for (int j = line; j < row-1; j++){
            for (int i = 0; i < col; i++) {
                if (geoMap[i][j + 1] != null) {
                    geoMap[i][j] = geoMap[i][j + 1];
                    geoMap[i][j].setName("Box" + String.valueOf(i) + String.valueOf(j));
                    geoMap[i][j].setLocalTranslation(geoMap[i][j].getWorldBound().getCenter().getX(), geoMap[i][j].getWorldBound().getCenter().getY() - 2.5f * cubeSize, geoMap[i][j].getWorldBound().getCenter().getZ());
                    geoMap[i][j + 1] = null;
                }
            }
        }
    }

    private boolean lineComplete(int Line){
        int boxesInLine = 0;
        for (int i = 0; i < col; i++){
            if (geoMap[i][Line] != null){
                boxesInLine++;
            }
        }
        return boxesInLine == col;
    }

    private int getCompleteLineNum(){
        for (int line = 0; line < row; line++) {
            if (lineComplete(line)) {
                return line;
            }
        }
        return -1;
    }

    public void destroyCompletedLines(){
        int line = getCompleteLineNum();
        int lineCount = 1;
        while (line != -1){
            //Erase Line
            destroyLine(line);
            //Update line num
            line = getCompleteLineNum();

            Main.app.getScore().updateScore(lineCount, 100);

            lineCount += 1;
        }
		if (lineCount > 1){
			Main.app.getScore().setStreakMultiplier(Main.app.getScore().getStreakMultiplier()+1);
		}else{
			Main.app.getScore().setStreakMultiplier(1);
		}
    }

    private void setFrameAlpha(float alphaVal){
		for (Geometry aFrame : frame) {
			if (aFrame != null) {
				ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
				alpha.a = alphaVal;
				aFrame.getMaterial().setColor("Diffuse", alpha);
				aFrame.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
				aFrame.getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
				aFrame.getMaterial().setBoolean("UseAlpha", true);
			}
		}
    }

    private void setBoxesAlpha(float alphaVal) {
		for (Geometry[] aGeoMap : geoMap) {
			for (Geometry anAGeoMap : aGeoMap) {
				if (anAGeoMap != null) {
					ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
					alpha.a = alphaVal;
					anAGeoMap.getMaterial().setColor("Diffuse", alpha);
					anAGeoMap.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
					anAGeoMap.getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
					anAGeoMap.getMaterial().setBoolean("UseAlpha", true);
				}
			}
		}
    }

    public void setGameOver(boolean gameOver, float alphaVal) {
        this.gameOver = gameOver;
        if (gameOver) {
            setBoxesAlpha(alphaVal);
            setFrameAlpha(alphaVal);
            Main.app.getNextPiece().setAlpha(alphaVal);
            Main.app.getLevelBar().setAlpha(alphaVal);
            Main.app.getScore().getDisplayScore().setAlpha(alphaVal);
            Main.app.getLevelBar().getDisplayLvl().setAlpha(alphaVal);
        }
    }
}

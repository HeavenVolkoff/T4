package Tetris;

//Java

//jMoneyFramework
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import java.util.Arrays;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Board extends Node {
    private Geometry[][] geoMap;
    private float cubeSize;
	private int objNum;
	private int col;
	private int row;
    public Geometry[] frame;

    public Board(int col, int row, float cubeSize, Material mat){
        this.cubeSize = cubeSize;
        this.frame = new Geometry[3];
        this.objNum = 0;
        this.col = col;
        this.row = row;
        this.geoMap = new Geometry[col][row];

        Box bottom = new Box(col*cubeSize*1.25f,cubeSize*0.25f,cubeSize*1.5f);
        frame[0] = new Geometry("BottomBoard",bottom);
        frame[0].setLocalTranslation(new Vector3f(0, -row * cubeSize * 1.25f, 0));
        frame[0].setMaterial(mat);
        attachChild(frame[0]);

        Box Left = new Box(cubeSize*0.25f,row*cubeSize*1.25f,cubeSize*1.5f);
        frame[1] = new Geometry("LeftBoard",Left);
        frame[1].setLocalTranslation(new Vector3f(-(col * cubeSize * 1.25f), 0, 0));
        frame[1].setMaterial(mat);
        attachChild(frame[1]);

        Box Right = new Box(cubeSize*0.25f,row*cubeSize*1.25f,cubeSize*1.5f);
        frame[2] = new Geometry("RightBoard",Right);
        frame[2].setLocalTranslation(new Vector3f(+(col * cubeSize * 1.25f), 0, 0));
        frame[2].setMaterial(mat);
        attachChild(frame[2]);
    }

    public Vector3f[] piecePosRelativeToBoard(Vector3f[] pieceBoxesAbsolutePos, int boxNum){
        Vector3f[] pos = new Vector3f[boxNum];
        for(int i = 0; i < pieceBoxesAbsolutePos.length; i++){
            pos[i] = new Vector3f();
            pos[i].setX(Math.round((((pieceBoxesAbsolutePos[i].distance(new Vector3f(frame[1].getWorldTranslation().getX(), pieceBoxesAbsolutePos[i].getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
            pos[i].setY(Math.round((((pieceBoxesAbsolutePos[i].distance(new Vector3f(pieceBoxesAbsolutePos[i].getX(), frame[0].getWorldTranslation().getY(), 0))) - (cubeSize * 1.25f)) / (cubeSize * 2.5f))));
            pos[i].setZ(0);
        }
        return pos;
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

    public void addPiece(Vector3f[] pieceBoxesAbsolutePos, int boxNum, Material mat){
        int count = 0;
        for (Vector3f piecePos : piecePosRelativeToBoard(pieceBoxesAbsolutePos, boxNum)){
            Box box = new Box(cubeSize,cubeSize,cubeSize);
            geoMap[(int)piecePos.getX()][(int)piecePos.getY()] = new Geometry("Box"+String.valueOf((int)piecePos.getX())+String.valueOf((int)piecePos.getY()),box);
            geoMap[(int)piecePos.getX()][(int)piecePos.getY()].setLocalTranslation(pieceBoxesAbsolutePos[count].getX(),pieceBoxesAbsolutePos[count].getY(),0);
            geoMap[(int)piecePos.getX()][(int)piecePos.getY()].setMaterial(mat);
            attachChild(geoMap[(int) piecePos.getX()][(int) piecePos.getY()]);
            count++;
        }
    }

    public void destroyLine(int line){
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

    public boolean lineComplete(int Line){
        int boxesInLine = 0;
        for (int i = 0; i < col; i++){
            if (geoMap[i][Line] != null){
                boxesInLine++;
            }
        }
        if (boxesInLine == col){
            return true;
        }
        return false;
    }

    public int getCompleteLineNum(){
        for (int line = 0; line < row; line++) {
            if (lineComplete(line)) {
                return line;
            }
        }
        return -1;
    }

    public void destroyCompletedLines(){
        int line = getCompleteLineNum();
        while (line != -1){
            //Erase Line
            destroyLine(line);
            //Update line num
            line = getCompleteLineNum();
        }
    }

    public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
}

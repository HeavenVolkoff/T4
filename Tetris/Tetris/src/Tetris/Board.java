package Tetris;

//Java

//jMoneyFramework
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Board extends Node {
	private Box[][] map;
    private Geometry[][] geoMap;
    private int[][] posMap;
	private int objNum;
	private int col;
	private int row;
    private float cubeSize;
    private int[] colStartFloor;

    public Board(int col, int row, float cubeSize, Material mat){
        Box bottom = new Box(col*cubeSize*1.25f,cubeSize*0.25f,cubeSize*1.5f);
        Geometry geoBottom = new Geometry("BottomBoard",bottom);
        geoBottom.setLocalTranslation(new Vector3f(0, -row*cubeSize*1.25f, 0));
        geoBottom.setMaterial(mat);
        attachChild(geoBottom);

        Box Left = new Box(cubeSize*0.25f,row*cubeSize*1.25f,cubeSize*1.5f);
        Geometry geoLeft = new Geometry("LeftBoard",Left);
        geoLeft.setLocalTranslation(new Vector3f(-(col*cubeSize*1.25f), 0, 0));
        geoLeft.setMaterial(mat);
        attachChild(geoLeft);

        Box Right = new Box(cubeSize*0.25f,row*cubeSize*1.25f,cubeSize*1.5f);
        Geometry geoRight = new Geometry("RightBoard",Right);
        geoRight.setLocalTranslation(new Vector3f(+(col*cubeSize*1.25f), 0, 0));
        geoRight.setMaterial(mat);
        attachChild(geoRight);
    }

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public void addBlock(int x, int y, Piece piece){
		Box b = new Box(piece.getCubeSize(), piece.getCubeSize(), piece.getCubeSize());
		Geometry geom = new Geometry("Box", b);
	}
}

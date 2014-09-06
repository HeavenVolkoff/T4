package Tetris;

//Java
import java.util.List;

//jMoneyFramework
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * T4 
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Board extends Node {
	private Box[][] matrix;
	private int objNum;
	private int col;
	private int row;

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

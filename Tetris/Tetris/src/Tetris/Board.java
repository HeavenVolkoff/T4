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
	private int objNum;
	private int col;
	private int row;
    private float cubeSize;
    public Geometry[] frame;

    public Board(int col, int row, float cubeSize, Material mat){
        this.cubeSize = cubeSize;
        frame = new Geometry[3];

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

    public boolean hitBottomFrame(Vector3f[] pieceBoxesAbsolutePos, float tolerance){
        float frameThickness = cubeSize*0.25f;
        for (Vector3f pieceAbsolutePos : pieceBoxesAbsolutePos){
            if (pieceAbsolutePos.distance(new Vector3f(pieceAbsolutePos.x,frame[0].getWorldBound().getCenter().y,0))+frameThickness-cubeSize*1.5f<=tolerance){
                return true;
            }
        }
        return false;
    }

    public boolean hitLeftFrame(Vector3f[] pieceBoxesAbsolutePos, float tolerance){
        float frameThickness = cubeSize*0.25f;
        for (Vector3f pieceAbsolutePos : pieceBoxesAbsolutePos){
            if (pieceAbsolutePos.distance(new Vector3f(frame[1].getWorldBound().getCenter().x,pieceAbsolutePos.y,0))+frameThickness-cubeSize*1.5f<=tolerance){
                return true;
            }
        }
        return false;
    }

    public boolean hitRightFrame(Vector3f[] pieceBoxesAbsolutePos, float tolerance) {
        float frameThickness = cubeSize * 0.25f;
        for (Vector3f pieceAbsolutePos : pieceBoxesAbsolutePos) {
            if (pieceAbsolutePos.distance(new Vector3f(frame[2].getWorldBound().getCenter().x, pieceAbsolutePos.y, 0)) + frameThickness - cubeSize * 1.5f <= tolerance) {
                return true;
            }
        }
        return false;
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

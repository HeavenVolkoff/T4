package Tetris;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Piece extends Node {

	//===================Constant===================//
    //Movement Directions
	public final int LEFT = -1;
	public final int RIGHT = 1;
    public final int TOP = 1;
    public final int DOWN = -1;

    //Piece Type
	/*
		TODO: (Novos tipos de peças)
		- peça fantasma:
			A peça atravessa as outras enquanto em estado fantasma, e se solidifica ao apertar uma tecla. Se a peça
		    passar o jogo sem ser solidificada, ela empurra tudo para cima e se solidifica.)
		- board variavel
	*/
	public final int CUBE = 0;
	public final int LINE = 1;
	public final int Z = 2;
	public final int T = 3;
	public final int L = 4;
    public final int T4 = 5;
	//==============================================//

    private Integer PieceIndex; //rootNode Index
	private float cubeSize;
	private long startFallTime;
    private int pieceFallingTime;
    private boolean falling;
	private float posX;
	private float posY;
    private Vector3f[] boxAbsolutePoint;
    private int numBox;


	//================ Class Constructors==========================//
    public Piece() {} //empty serialization constructor

	public Piece(float cubeSize, int pieceType, float posX, float posY, Material mat, float rotate, int invert, Control controler){
        super("rotationPivot");

        addControl(controler);

        this.PieceIndex = null;
        this.cubeSize = cubeSize;
		this.startFallTime = 0;
        this.pieceFallingTime = 500;
        this.falling = true; // Start falling
		this.posX = posX+(cubeSize * 1.25f);
		this.posY = posY;
		setLocalTranslation(new Vector3f(this.posX, this.posY, 0));

		rotate(0, (float) (invert * Math.PI), rotate);

        if (pieceType == T4){
            numBox = 17;
        }else{
            numBox = 4;
        }

        for(Geometry geoPiece : constructPiece(pieceType, numBox, this.posX, this.posY, mat)) {
            attachChild(geoPiece);
        }

        if (pieceType == CUBE){
            this.posX = posX+(cubeSize * 1.25f);
            this.posY = posY+(cubeSize * 1.25f);
            setLocalTranslation(new Vector3f(this.posX , this.posY , 0));
        }
	} //Specific Piece Constructor

    public Piece(float cubeSize, float posX, float posY, Material mat, Control controler){
        super("rotationPivot");

        addControl(controler);

        this.PieceIndex = null;
        this.cubeSize = cubeSize;
        this.startFallTime = 0;
        this.pieceFallingTime = 500;
        this.falling = true; // Start falling
        this.posX = posX+(cubeSize * 1.25f);
        this.posY = posY;
        setLocalTranslation(new Vector3f(this.posX, this.posY, 0));

		int invert = ((int)(Math.random()*10)%2);
		rotate(0, (float) (invert * Math.PI), 0);

        int pieceType = ((int)(Math.random()*10)%5);

        if (pieceType == T4){
            numBox = 17;
        }else{
            numBox = 4;
        }

        for(Geometry geoPiece : constructPiece(pieceType, numBox, this.posX, this.posY, mat)) {
            attachChild(geoPiece);
        }

        if (pieceType == CUBE){
            this.posX = posX+(cubeSize * 2.50f);
            this.posY = posY+(cubeSize * 1.25f);
            setLocalTranslation(new Vector3f(this.posX , this.posY , 0));
        }
    } //Random Piece Constructor
	//==============================================================//

	//================= Constructor Manager=========================//
	private Geometry[] constructPiece(int pieceType, int numBox, float posX, float posY, Material mat){
        Box[] pieceItens = new Box[numBox];
		Geometry[] geoItens = new Geometry[numBox];
        boxAbsolutePoint = new Vector3f[numBox];

		int count = 0;
		for(Box boxObj : pieceItens){
			boxObj = new Box(cubeSize, cubeSize, cubeSize);
			geoItens[count] = new Geometry("Box"+count, boxObj);
			geoItens[count].setMaterial(mat);
			count++;
		}

		switch(pieceType){
			case 0:
				constructCube(geoItens,boxAbsolutePoint);
				break;

			case 1:
				constructLine(geoItens,boxAbsolutePoint);
				break;

			case 2:
				constructZ(geoItens,boxAbsolutePoint);
				break;

			case 3:
				constructT(geoItens,boxAbsolutePoint);
				break;

			case 4:
				constructL(geoItens,boxAbsolutePoint);
				break;

            case 5:
                constructT4(geoItens,boxAbsolutePoint);
                break;
			default:

				break;
		}

		return geoItens;
	}
	//==============================================================//

	//----------------- Specific Pieces Constructors ----------------//
	private void constructCube(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//Cube
		/*          ***Center Pivot***
			|0||1|
			|2||3|
		 */

		geoItens[0].setLocalTranslation(new Vector3f( -(cubeSize * 1.25f) , -(cubeSize * 1.25f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();

		geoItens[1].setLocalTranslation(new Vector3f( +(cubeSize * 1.25f) , -(cubeSize * 1.25f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();

		geoItens[2].setLocalTranslation(new Vector3f( -(cubeSize * 1.25f) , +(cubeSize * 1.25f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();

		geoItens[3].setLocalTranslation(new Vector3f(+(cubeSize * 1.25f), +(cubeSize * 1.25f), 0));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}

	private void constructLine(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//Line
			/*          ***|2| Pivot***
				|0||1||2||3|
			 */

		geoItens[0].setLocalTranslation(new Vector3f(+(cubeSize * 5.00f), 0 , 0));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
		geoItens[1].setLocalTranslation(new Vector3f(+(cubeSize * 2.50f), 0 , 0));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
		geoItens[2].setLocalTranslation(new Vector3f(+(cubeSize * 0.00f), 0 , 0));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
		geoItens[3].setLocalTranslation(new Vector3f(-(cubeSize * 2.50f), 0 , 0));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}

	private void constructZ(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//Z
			/*          ***|1| Pivot***
			  	   |2||3|
				|0||1|
			 */

        geoItens[0].setLocalTranslation(new Vector3f( -(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
        geoItens[1].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
        geoItens[2].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation(new Vector3f( +(cubeSize * 2.50f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();

	}

	private void constructT(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//T
			/*          ***|1| Pivot***
				   |3|
				|0||1||2|
			 */

        geoItens[0].setLocalTranslation(new Vector3f( -(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
        geoItens[1].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
        geoItens[2].setLocalTranslation(new Vector3f( +(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}

	private void constructL(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//L
			/*          ***|2| Pivot***
				      |3|
				|0||1||2|
			 */

        geoItens[0].setLocalTranslation(new Vector3f( -(cubeSize * 5.00f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
        geoItens[1].setLocalTranslation(new Vector3f( -(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
        geoItens[2].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}

    private void constructT4(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
        //L
			/*                     ***|8| Pivot***
		     |0||1||2|| || ||3|
			    |4|| || ||5||6|
			    |7|| ||8|| ||9|
			    |A|| ||B||C||D||E|
			    |F|| || || ||G|
			 */

        geoItens[0].setLocalTranslation( new Vector3f( -(cubeSize * 6.75f) , +(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
        geoItens[1].setLocalTranslation( new Vector3f( -(cubeSize * 4.50f) , +(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
        geoItens[2].setLocalTranslation( new Vector3f( -(cubeSize * 2.25f) , +(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation( new Vector3f( +(cubeSize * 4.50f) , +(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
        geoItens[4].setLocalTranslation( new Vector3f( -(cubeSize * 4.50f) , +(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[4] = geoItens[4].getWorldBound().getCenter();
        geoItens[5].setLocalTranslation( new Vector3f( +(cubeSize * 2.25f) , +(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[5] = geoItens[5].getWorldBound().getCenter();
        geoItens[6].setLocalTranslation( new Vector3f( +(cubeSize * 4.50f) , +(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[6] = geoItens[6].getWorldBound().getCenter();
        geoItens[7].setLocalTranslation( new Vector3f( -(cubeSize * 4.50f) , -(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[7] = geoItens[7].getWorldBound().getCenter();
        geoItens[8].setLocalTranslation( new Vector3f( +(cubeSize * 0.00f) , -(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[8] = geoItens[8].getWorldBound().getCenter();
        geoItens[9].setLocalTranslation( new Vector3f( +(cubeSize * 4.50f) , -(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[9] = geoItens[9].getWorldBound().getCenter();
        geoItens[10].setLocalTranslation(new Vector3f( -(cubeSize * 4.50f) , -(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[10] = geoItens[10].getWorldBound().getCenter();
        geoItens[11].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , -(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[11] = geoItens[11].getWorldBound().getCenter();
        geoItens[12].setLocalTranslation(new Vector3f( +(cubeSize * 2.25f) , -(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[12] = geoItens[12].getWorldBound().getCenter();
        geoItens[13].setLocalTranslation(new Vector3f( +(cubeSize * 4.50f) , -(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[13] = geoItens[13].getWorldBound().getCenter();
        geoItens[14].setLocalTranslation(new Vector3f( +(cubeSize * 6.75f) , -(cubeSize * 2.25f) , 0 ));
        boxAbsolutePoint[14] = geoItens[14].getWorldBound().getCenter();
        geoItens[15].setLocalTranslation(new Vector3f( -(cubeSize * 4.50f) , -(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[15] = geoItens[15].getWorldBound().getCenter();
        geoItens[16].setLocalTranslation(new Vector3f( +(cubeSize * 4.50f) , -(cubeSize * 4.50f) , 0 ));
        boxAbsolutePoint[16] = geoItens[16].getWorldBound().getCenter();
    }
	//--------------------------------------------------------------//

	public float getCubeSize() {
		return cubeSize;
	}

	public float getPosX() {
		return posX;
	}

    public void setPosX(float posX) {
        this.posX = posX;
    }

	public float getPosY() {
		return posY;
	}

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public Integer getPieceIndex() {
		return PieceIndex;
	}

	public void setPieceIndex(Integer pieceIndex) {
		PieceIndex = pieceIndex;
	}

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public int getPieceFallingTime() {
        return pieceFallingTime;
    }

    public void setPieceFallingTime(int pieceFallingTime) {
        this.pieceFallingTime = pieceFallingTime;
    }

    public long getStartFallTime() {
        return startFallTime;
    }

    public void setStartFallTime(long startFallTime) {
        this.startFallTime = startFallTime;
    }

    public Vector3f[] getBoxAbsolutePoint() {
        return boxAbsolutePoint;
    }

    public void setBoxAbsolutePoint(Vector3f[] boxAbsolutePoint) {
        this.boxAbsolutePoint = boxAbsolutePoint;
    }

    public float getNumBox() {
        return numBox;
    }

}

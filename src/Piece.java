import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Piece extends Node implements Cloneable{

	//===================Constant===================//
    //Movement Directions
	public final int LEFT = -1;
	public final int RIGHT = 1;
    public final int TOP = 1;
    public final int DOWN = -1;

    //Piece Type
	public final int CUBE = 0;
	public final int LINE = 1;
	public final int Z = 2;
	public final int T = 3;
	public final int L = 4;
    public final int T4 = 5;
  	/*
		TODO: (Novos tipos de peças)
		- peça fantasma:
			- A peça atravessa as outras enquanto em estado fantasma, e se solidifica ao apertar uma tecla. Se a peça
		    passar o jogo sem ser solidificada, ela empurra tudo para cima e se solidifica.)
		    - Blocos Luminosos
		    - Bloco Infected
	*/

    //==============================================//

	private float cubeSize;
	private long startFallTime;
    private int pieceFallingTime;
    private boolean falling;
	private float posX;
	private float posY;
    private int numBox;
    private Vector3f[] boxAbsolutePoint;
    private Geometry[] boxGeometries;
    private Material material;
    private int initialType;
    private int initialInvert;


	//================ Class Constructors==========================//
	/*
		TODO: Piece constructors
			-add read piece color from file
			-add char-color index to .piece file
	 */
    public Piece() {} //empty serialization constructor

	public Piece(float cubeSize, int pieceType, float posX, float posY, float rotate, int invert, AssetManager assetManager, Control controler){
        super("rotationPivot");

		if (controler != null) {
			addControl(controler);
		}

        this.cubeSize = cubeSize;
		this.startFallTime = System.nanoTime();
        this.pieceFallingTime = 500;
        this.initialType = pieceType;
        this.initialInvert = invert;
        this.posX = posX+(cubeSize * 1.25f);
		this.posY = posY;
		setLocalTranslation(new Vector3f(this.posX, this.posY, 0)); //Have to move before fall
        this.falling = true; // Start falling

        rotate(0, (float) (invert * Math.PI), rotate);

        if (pieceType == T4){
            numBox = 17;
        }else{
            numBox = 4;
        }

        boxAbsolutePoint = new Vector3f[numBox];

        boxGeometries = constructPiece(pieceType, numBox, createSpecificMaterial(pieceType, assetManager));
        for(Geometry geoPiece : boxGeometries) {
            attachChild(geoPiece);
        }

        if (pieceType == CUBE){
            this.posX = posX+(cubeSize * 2.50f);
            this.posY = posY+(cubeSize * 1.25f);
            setLocalTranslation(new Vector3f(this.posX , this.posY , 0));
        }
	} //Specific Piece Constructor

    public Piece(float cubeSize, float posX, float posY, AssetManager assetManager, Control controler){
        super("rotationPivot");

        if (controler != null) {
            addControl(controler);
        }

        this.cubeSize = cubeSize;
        this.startFallTime = System.nanoTime();
        this.pieceFallingTime = 500;
        this.posX = posX+(cubeSize * 1.25f);
        this.posY = posY;
        setLocalTranslation(new Vector3f(this.posX, this.posY, 0)); //Have to move before fall
        this.falling = true; // Start falling

		this.initialInvert = ((int)(Math.random()*10)%2);
		rotate(0, (float) (this.initialInvert * Math.PI), 0);

        this.initialType = ((int)(Math.random()*10)%5);

        if (this.initialType == T4){
            numBox = 17;
        }else {
            numBox = 4;
        }

        boxAbsolutePoint = new Vector3f[numBox];

        boxGeometries = constructPiece(this.initialType, numBox, createSpecificMaterial(this.initialType, assetManager));
        for(Geometry geoPiece : boxGeometries) {
            attachChild(geoPiece);
        }

        if (this.initialType == CUBE){
            this.posX = posX+(cubeSize * 2.50f);
            this.posY = posY+(cubeSize * 1.25f);
            setLocalTranslation(new Vector3f(this.posX , this.posY , 0));
        }
    } //Random Piece Constructor

    public Piece(float cubeSize, float posX, float posY, float posZ, String fileName, ColorRGBA color, AssetManager assetManager, Control controler){
        super("rotationPivot");

        if (controler != null) {
            addControl(controler);
        }

        this.cubeSize = cubeSize;
        this.startFallTime = System.nanoTime();
        this.pieceFallingTime = 500;
        this.posX = posX+(cubeSize * 1.25f);
        this.posY = posY - 0.5f * cubeSize;
        setLocalTranslation(new Vector3f(this.posX, this.posY, 0)); //Have to move before fall
        this.falling = true; // Start falling

        this.initialInvert = 0;

        this.initialType = -1;

        numBox = 0;

		try {
			Path path = Paths.get("./resources/customPieces/"+fileName);
			constructFromString(Files.readAllLines(path), createColoredMaterial(color, assetManager), posZ);
		} catch (IOException e) {
			e.printStackTrace();
		}
    } //Load from file Constructor

	public Piece(float cubeSize, float posX, float posY, float posZ, List<String> lines, ColorRGBA color, AssetManager assetManager, Control controler){
		super("rotationPivot");

		if (controler != null) {
			addControl(controler);
		}

		this.cubeSize = cubeSize;
		this.startFallTime = System.nanoTime();
		this.pieceFallingTime = 500;
		this.posX = posX+(cubeSize * 1.25f);
		this.posY = posY - 0.5f * cubeSize;
		setLocalTranslation(new Vector3f(this.posX, this.posY, 0)); //Have to move before fall
		this.falling = true; // Start falling

		this.initialInvert = 0;

		this.initialType = -1;

		numBox = 0;

		constructFromString(lines, createColoredMaterial(color, assetManager), posZ);
	} //Load from string Constructor
	// ==============================================================//

    //===================Material Manager===========================//
    private Material createSpecificMaterial(int pieceType, AssetManager assetManager){
        material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        switch(pieceType) {
            case CUBE:
                setMaterialColor(material, ColorRGBA.Red, 2);
                break;
            case LINE:
                setMaterialColor(material, ColorRGBA.Blue, 2);
                break;
            case Z:
                setMaterialColor(material, ColorRGBA.Yellow, 2);
                break;
            case T:
                setMaterialColor(material, ColorRGBA.Green, 2);
                break;
            case L:
                setMaterialColor(material, ColorRGBA.Brown, 2);
                break;
            default:
                setMaterialColor(material, ColorRGBA.randomColor(), 2);
                break;
        }
        return material;
    }

    private Material createColoredMaterial(ColorRGBA color, AssetManager assetManager){
        material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        setMaterialColor(material, color, 2);

        return material;
    }

    private void setMaterialColor(Material material, ColorRGBA color, float shine){
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);
        material.setColor("Specular", color);
        material.setFloat("Shininess", shine);
        material.setBoolean("UseMaterialColors", true);
    }
    //==============================================================//

	//================= Constructor Manager=========================//
	private Geometry[] constructPiece(int pieceType, int numBox, Material mat){
        Box [] boxes = new Box[numBox];
		Geometry[] geoItens = new Geometry[numBox];

		int count = 0;
		for(Box boxObj : boxes){
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
                //constructT4(geoItens,boxAbsolutePoint);
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
        geoItens[2].setName("Pivot");
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
        geoItens[1].setName("Pivot");
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
        geoItens[1].setName("Pivot");
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
        geoItens[2].setLocalTranslation(new Vector3f( +(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation(new Vector3f( +(cubeSize * 0.00f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}

	private void constructL(Geometry[] geoItens, Vector3f[] boxAbsolutePoint){
		//L
			/*          ***|1| Pivot***
				      |3|
				|0||1||2|
			 */

        geoItens[0].setLocalTranslation(new Vector3f( -(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[0] = geoItens[0].getWorldBound().getCenter();
        geoItens[1].setLocalTranslation(new Vector3f( -(cubeSize * 0.00f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[1] = geoItens[1].getWorldBound().getCenter();
		geoItens[1].setName("Pivot");
        geoItens[2].setLocalTranslation(new Vector3f( +(cubeSize * 2.50f) , +(cubeSize * 0.00f) , 0 ));
        boxAbsolutePoint[2] = geoItens[2].getWorldBound().getCenter();
        geoItens[3].setLocalTranslation(new Vector3f( +(cubeSize * 2.50f) , +(cubeSize * 2.50f) , 0 ));
        boxAbsolutePoint[3] = geoItens[3].getWorldBound().getCenter();
	}
	//--------------------------------------------------------------//

	private void constructFromString(List<String> lines, Material mat, float posZ){
    	List<Geometry> geometries = new ArrayList<Geometry>();
        int boxNum = 0;
        float posX = 0;
		float posY = 0;
        float pivotPosX = 0;
        float pivotPosY = 0;

        for (String line : lines){
            for (int i = 0; i<line.length(); i++){
                if (line.charAt(i) == '|'){
                    posX += 0.25f*cubeSize;
                }else{
					if (line.charAt(i) == ' '){
                        posX += 1f*cubeSize;
                    }else{
                        if (line.charAt(i) == '0' && line.charAt(i+1) == '0'){
                            posX += 1f*cubeSize;
                            Box box = new Box(cubeSize, cubeSize, cubeSize);
                            geometries.add(new Geometry("Box"+boxNum,box));
                            geometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                            geometries.get(boxNum).setMaterial(mat);
                            attachChild(geometries.get(boxNum));
                            posX += 1f*cubeSize;
                            boxNum += 1;
                            i += 1;
                        }else{
                            if (line.charAt(i) == 'P' && line.charAt(i+1) == 'P'){
                                pivotPosX = posX+1f*cubeSize;
                                pivotPosY = posY;
                                posX += 2f*cubeSize;
                                i += 1;
                            }else{
                                if (line.charAt(i) == '0' && line.charAt(i+1) == 'P'){
                                    posX += 1f*cubeSize;
                                    pivotPosX = posX;
                                    pivotPosY = posY;
                                    Box box = new Box(cubeSize, cubeSize, cubeSize);
                                    geometries.add(new Geometry("Pivot",box));
                                    geometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                                    geometries.get(boxNum).setMaterial(mat);
                                    attachChild(geometries.get(boxNum));
                                    posX += 1f*cubeSize;
                                    boxNum += 1;
                                    i += 1;
                                }
                            }
                        }
                    }
                }
            }
            posX = 0;
			posY -= 2.5f*cubeSize;
        }

        this.boxAbsolutePoint = new Vector3f[boxNum];



		this.boxGeometries = new Geometry[boxNum];
        this.numBox = 0;
		for (Geometry geometry : geometries) {
			this.boxGeometries[this.numBox] = geometry;
			geometry.move(-pivotPosX, -1 * (pivotPosY), 0);
			this.boxAbsolutePoint[numBox] = geometry.getWorldBound().getCenter();
			this.numBox += 1;
		}
    }

    public void setAlpha(float alphaVal){
        for (int i = 0; i < boxGeometries.length; i++){
            if (boxGeometries[i] != null){
                ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
                alpha.a = alphaVal;
                boxGeometries[i].getMaterial().setColor("Diffuse", alpha);
                boxGeometries[i].getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                boxGeometries[i].getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
                boxGeometries[i].getMaterial().setBoolean("UseAlpha",true);
            }
        }
    }

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

    public int getNumBox() {
        return numBox;
    }

    public Geometry[] getBoxGeometries() {
        return boxGeometries;
    }

    public Material getMat() {
        return material;
    }

    public int getInitialType() {
        return initialType;
    }

    public int getInitialInvert() {
        return initialInvert;
    }
}

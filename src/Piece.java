import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;

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
    //==============================================//

  	/*
		TODO: (Novos tipos de peças)
		- peça fantasma:
			- A peça atravessa as outras enquanto em estado fantasma, e se solidifica ao apertar uma tecla. Se a peça
		    passar o jogo sem ser solidificada, ela empurra tudo para cima e se solidifica.)
		- Blocos Luminosos
		- Bloco Infected
		- Bloco que se multiplica (aumenta em 1 unidade o contorno do jogo até a original ser destruida)
		- Bloco explosivo (radius 3x3)
		- Bloco drill
		- Fusao de propriedades
	*/


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
    private String fileName;
    private int Invert;
    private float alpha;
    private boolean rotating;


	//================ Class Constructors==========================//
	/*
		TODO: Piece constructors
			-add read piece color from file
			-add char-color index to .piece file
	 */
    public Piece(float cubeSize, float posX, float posY, float posZ, String fileName, ColorRGBA color, AssetManager assetManager, Control controler){
        super("rotationPivot");

        if (controler != null) {
            addControl(controler);
        }

        this.rotating = false;
        this.cubeSize = cubeSize;
        this.startFallTime = System.nanoTime();
        this.pieceFallingTime = 500;
        this.posX = posX+(cubeSize * 1.25f);
        this.posY = posY - 0.5f * cubeSize;
        setLocalTranslation(new Vector3f(this.posX, this.posY, 0)); //Have to move before fall
        this.falling = true; // Start falling
        this.alpha = color.a;

        this.Invert = 0;

        this.fileName = fileName;

        numBox = 0;

		try {
			Path path = Paths.get("./resources/customPieces/"+fileName);
			constructFromString(Files.readAllLines(path), createColoredMaterial(color, assetManager), posZ);
		} catch (IOException e) {
			e.printStackTrace();
		}
    } //Load from file Constructor
	// ==============================================================//

    //===================Material Manager===========================//
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

	private void constructFromString(List<String> lines, Material mat, float posZ){
    	List<Geometry> geometries = new ArrayList<Geometry>();
        int boxNum = 0;
        float posX = 0;
		float posY = 0;
        List<Float> pivotPosX = new ArrayList<Float>();
        List<Float> pivotPosY = new ArrayList<Float>();
        float absolutePivotPosX;
        float absolutePivotPosY;

        for (int lineNum = 0;lineNum < lines.size(); lineNum++){
            for (int i = 0; i<lines.get(lineNum).length(); i++){
                if (lines.get(lineNum).equals("//INFO//")) {
                    setPieceFileProp(lines, lineNum);
                    lineNum = lines.size()-1;
                }else if (lines.get(lineNum).charAt(i) == '|'){
                    posX += 0.25f*cubeSize;
                }else if (lines.get(lineNum).charAt(i) == ' '){
                    posX += 1f*cubeSize;
                }else if (lines.get(lineNum).charAt(i) == '0' && lines.get(lineNum).charAt(i+1) == '0'){
                    posX += 1f*cubeSize;
                    Box box = new Box(cubeSize, cubeSize, cubeSize);
                    geometries.add(new Geometry("Box"+boxNum,box));
                    geometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    geometries.get(boxNum).setMaterial(mat);
                    attachChild(geometries.get(boxNum));
                    posX += 1f*cubeSize;
                    boxNum += 1;
                    i += 1;
                }else if (lines.get(lineNum).charAt(i) == 'P' && lines.get(lineNum).charAt(i+1) == 'P'){
                    pivotPosX.add(posX+1f*cubeSize);
                    pivotPosY.add(posY);
                    posX += 2f*cubeSize;
                    i += 1;
                }else if (lines.get(lineNum).charAt(i) == '0' && lines.get(lineNum).charAt(i+1) == 'P') {
                    posX += 1f * cubeSize;
                    pivotPosX.add(posX);
                    pivotPosY.add(posY);
                    Box box = new Box(cubeSize, cubeSize, cubeSize);
                    geometries.add(new Geometry("Pivot", box));
                    geometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    geometries.get(boxNum).setMaterial(mat);
                    attachChild(geometries.get(boxNum));
                    posX += 1f * cubeSize;
                    boxNum += 1;
                    i += 1;
                }
            }
            posX = 0;
			posY -= 2.5f*cubeSize;
        }

        this.posX += -(5f*cubeSize + (minFromFloatList(pivotPosX)-minFromGeoListX(geometries)));
        this.posY += -(2.5f*cubeSize);

        absolutePivotPosX = getPivotPosFromMultiplePoints(pivotPosX);
        absolutePivotPosY = getPivotPosFromMultiplePoints(pivotPosY);
        if (existListVariation(pivotPosX)) {
            this.posX += cubeSize * 1.25f + minFromFloatList(pivotPosX);
        }else {
            this.posX += minFromFloatList(pivotPosX);
        }
        if (existListVariation(pivotPosY)) {
            this.posY -= cubeSize * 1.25f + minFromFloatList(pivotPosY);
        }else{
            this.posY -= minFromFloatList(pivotPosY);
        }
        setLocalTranslation(this.getPosX(), this.getPosY(), this.getWorldBound().getCenter().getZ());

        this.boxAbsolutePoint = new Vector3f[boxNum];
		this.boxGeometries = new Geometry[boxNum];
        this.numBox = 0;
		for (Geometry geometry : geometries) {
			this.boxGeometries[this.numBox] = geometry;
			geometry.move(-absolutePivotPosX, -1 * (absolutePivotPosY), 0);
			this.boxAbsolutePoint[numBox] = geometry.getWorldBound().getCenter();
			this.numBox += 1;
		}
    }

    private void setPieceFileProp(List<String> lines, int lineNum){
        if (lines.get(lineNum+2).equals("0")){
            this.Invert = 0;
        }else{
            this.Invert = 1;
        }
        float red = Float.parseFloat(lines.get(lineNum+4))/255;
        float green = Float.parseFloat(lines.get(lineNum+6))/255;
        float blue = Float.parseFloat(lines.get(lineNum+8))/255;
        float alpha = Float.parseFloat(lines.get(lineNum+10));
        if (red != -1 && green != -1 && blue != -1 && alpha != -1) {
            setMaterialColor(getMat(), new ColorRGBA(red, green, blue, alpha), 3);
        }
    }

    private float minFromGeoListX(List<Geometry> geo){
        float min = geo.get(0).getWorldBound().getCenter().getX();
        for (Geometry item : geo){
            if (item.getWorldBound().getCenter().getX() < min){
                min = item.getWorldBound().getCenter().getX();
            }
        }
        return min;
    }

    private float minFromFloatList(List<Float> nums){
        float min = nums.get(0);
        for (float item : nums){
            if (item < min){
                min = item;
            }
        }
        return min;
    }

    private boolean existListVariation(List<Float> nums){
        float standard = nums.get(0);
        for (float item : nums){
            if (item != standard){
                return true;
            }
        }
        return false;
    }

    private float getPivotPosFromMultiplePoints(List<Float> pivotPos){
        float sum = 0;
        float count = 0;
        float min = minFromFloatList(pivotPos);

        for (float pos : pivotPos) {
            sum += pos-min;
            count++;
        }
        return min+(sum/count);
    }

    public void setAlpha(float alphaVal){
		for (Geometry boxGeometry : boxGeometries) {
			if (boxGeometry != null) {
				ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
				alpha.a = alphaVal;
				boxGeometry.getMaterial().setColor("Diffuse", alpha);
				boxGeometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
				boxGeometry.getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
				boxGeometry.getMaterial().setBoolean("UseAlpha", true);
			}
		}
        alpha = alphaVal;
    }

    public float getAlpha(){
        return alpha;
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

    public int getNumBox() {
        return numBox;
    }

    public Geometry[] getBoxGeometries() {
        return boxGeometries;
    }

    public Material getMat() {
        return material;
    }

    public String getFileName() {
        return fileName;
    }

    public int getInvert() {
        return Invert;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }
}

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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

public class DisplayNumbers extends Node {

    private float cubeSize;
    private float posX;
    private float posY;
    private int value;
    private List<Geometry> boxGeometries;
    private int numBox;
    private Material material;
    private int maxDigits;
    private float width;
    private float height;

    //================ Class Constructors==========================//
    public DisplayNumbers(float cubeSize, float posX, float posY, int maxDigits, int initialValue, ColorRGBA color, AssetManager assetManager){
        this.cubeSize = cubeSize;
        this.posX = posX;
        this.posY = posY;
        this.value = initialValue;
        this.maxDigits = maxDigits;

        this.width = ((maxDigits*(3*cubeSize*2.5f)) - cubeSize*0.5f) + ((maxDigits-1f)*cubeSize*0.5f);
        this.height = 5f*2.5f*cubeSize - 0.5f*cubeSize;

        createColoredMaterial(color, assetManager);

        boxGeometries = new ArrayList<Geometry>();

		buildNumbers(this.maxDigits);
    }
    //=============================================================//

    public void buildNumbers(int maxDigits){ //
		try {
			Path path = Paths.get("./resources/customPieces/numbers/8.piece");
			float piecePosX = 0;

			for (int i = 0; i < maxDigits; i++) {
				System.out.println(maxDigits);
				constructFromString(Files.readAllLines(path), 0, 0, material, 0);
				piecePosX = piecePosX+2.5f*cubeSize*3+1.5f*cubeSize;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

    private void constructFromString(List<String> lines, float posX, float posY, Material mat, float posZ){
        int boxNum = 0;
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
                    boxGeometries.add(new Geometry("Box"+boxNum,box));
                    boxGeometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    boxGeometries.get(boxNum).setMaterial(mat);
                    attachChild(boxGeometries.get(boxNum));
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
                    boxGeometries.add(new Geometry("Pivot", box));
                    boxGeometries.get(boxNum).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    boxGeometries.get(boxNum).setMaterial(mat);
                    attachChild(boxGeometries.get(boxNum));
                    posX += 1f * cubeSize;
                    boxNum += 1;
                    i += 1;
                }
            }
            posX = 0;
            posY -= 2.5f*cubeSize;
        }

        this.posX += -(5f*cubeSize + (minFromFloatList(pivotPosX)-minFromGeoListX(boxGeometries)));
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
        //setLocalTranslation(this.getPosX(), this.getPosY(), this.getWorldBound().getCenter().getZ());

        this.numBox = 0;
        for (Geometry geometry : boxGeometries) {
            geometry.move(-absolutePivotPosX, -1 * (absolutePivotPosY), 0);
            this.numBox += 1;
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

    public float getPivotPosFromMultiplePoints(List<Float> pivotPos){
        float sum = 0;
        float count = 0;
        float min = minFromFloatList(pivotPos);

        for (float pos : pivotPos) {
            sum += pos-min;
            count++;
        }
        return min+(sum/count);
    }

    private void setPieceFileProp(List<String> lines, int lineNum){
        float red = Float.parseFloat(lines.get(lineNum+4));
        float green = Float.parseFloat(lines.get(lineNum+6));
        float blue = Float.parseFloat(lines.get(lineNum+8));
        float alpha = Float.parseFloat(lines.get(lineNum+10));
        if (red != -1 && green != -1 && blue != -1 && alpha != -1) {
            setMaterialColor(material, new ColorRGBA(red, green, blue, alpha), 3);
        }
    }

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

    public Material getMaterial() {
        return material;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosX() {
        return posX;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

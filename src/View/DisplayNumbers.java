package View;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
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
    private List<Geometry> boxGeometries;
    private int numBox;
    private Material material;
    private int maxDigits;

    //================ Class Constructors =========================//
    public DisplayNumbers(float cubeSize, float posX, float posY, int maxDigits, int initialValue, ColorRGBA color, AssetManager assetManager){
        this.cubeSize = cubeSize;
        this.maxDigits = maxDigits;

        float width = ((maxDigits * (3 * cubeSize * 2.5f)) - cubeSize * 0.5f) + ((maxDigits - 1f) * cubeSize * 0.5f);
        float height = 5f * 2.5f * cubeSize - 0.5f * cubeSize;

        setColoredMaterial(color, assetManager);

        boxGeometries = new ArrayList<Geometry>();

		buildNumbers(this.maxDigits);

        move(-width *0.75f+posX, +height *0.75f+posY, 0);

        write(initialValue);
    }
    //=============================================================//

	//===================Material Manager===========================//
	private void setColoredMaterial(ColorRGBA color, AssetManager assetManager){
		this.material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		setMaterialColor(this.material, color, 2);
	}

	private void setMaterialColor(Material material, ColorRGBA color, float shine){
		material.setColor("Ambient", color);
		material.setColor("Diffuse", color);
		material.setColor("Specular", color);
		material.setFloat("Shininess", shine);
		material.setBoolean("UseMaterialColors", true);
	}
	//==============================================================//

	//================ Construct Display Number ===================//
	private void buildNumbers(int maxDigits){ //
		try {
			Path path = Paths.get("./resources/customPieces/numbers/NumBase.piece");

			for (int i = 0; i < maxDigits; i++) {
				constructFromString(Files.readAllLines(path), String.valueOf(i), material, 0);
                for (Geometry geo : boxGeometries){
                    geo.move(2.5f*cubeSize*3+1.5f*cubeSize,0,0);
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

    private void constructFromString(List<String> lines, String digitName, Material mat, float posZ){
        float posX = 0;
        float posY = 0;

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
                    boxGeometries.add(new Geometry(digitName+numBox,box));
                    boxGeometries.get(numBox).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    boxGeometries.get(numBox).setMaterial(mat);
                    attachChild(boxGeometries.get(numBox));
                    posX += 1f*cubeSize;
                    numBox += 1;
                    i += 1;
                }else if (lines.get(lineNum).charAt(i) == 'P' && lines.get(lineNum).charAt(i+1) == 'P'){
                    posX += 2f*cubeSize;
                    i += 1;
                }else if (lines.get(lineNum).charAt(i) == '0' && lines.get(lineNum).charAt(i+1) == 'P') {
                    posX += 1f * cubeSize;
                    Box box = new Box(cubeSize, cubeSize, cubeSize);
                    boxGeometries.add(new Geometry(digitName+numBox, box));
                    boxGeometries.get(numBox).setLocalTranslation(new Vector3f(posX, posY, posZ));
                    boxGeometries.get(numBox).setMaterial(mat);
                    attachChild(boxGeometries.get(numBox));
                    posX += 1f * cubeSize;
                    numBox += 1;
                    i += 1;
                }
            }
            posX = 0;
            posY -= 2.5f*cubeSize;
        }
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
	//=============================================================//

	public void write(int val){
		int[] visibleGeo = new int[maxDigits*15];
		for (int i = 0; i < maxDigits*15; i+=15){
			if (val == 0 && i != 0){
				break;
			}

			if (val%10 == 0){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=0; visibleGeo[i+8]=1;
				visibleGeo[i+9]=1; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 1){
				visibleGeo[i]=0;   visibleGeo[i+1]=0; visibleGeo[i+2]=1;
				visibleGeo[i+3]=0; visibleGeo[i+4]=1; visibleGeo[i+5]=1;
				visibleGeo[i+6]=0; visibleGeo[i+7]=0; visibleGeo[i+8]=1;
				visibleGeo[i+9]=0; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=0;visibleGeo[i+13]=0;visibleGeo[i+14]=1;
			}else if (val%10 == 2){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=0; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=1; visibleGeo[i+10]=0;visibleGeo[i+11]=0;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 3){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=0; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=0; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 4){
				visibleGeo[i]=1;   visibleGeo[i+1]=0; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=0; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=0;visibleGeo[i+13]=0;visibleGeo[i+14]=1;
			}else if (val%10 == 5){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=0;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=0; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 6){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=0;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=1; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 7){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=0; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=0; visibleGeo[i+7]=1; visibleGeo[i+8]=0;
				visibleGeo[i+9]=1; visibleGeo[i+10]=0;visibleGeo[i+11]=0;
				visibleGeo[i+12]=1;visibleGeo[i+13]=0;visibleGeo[i+14]=0;
			}else if (val%10 == 8){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=1; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}else if (val%10 == 9){
				visibleGeo[i]=1;   visibleGeo[i+1]=1; visibleGeo[i+2]=1;
				visibleGeo[i+3]=1; visibleGeo[i+4]=0; visibleGeo[i+5]=1;
				visibleGeo[i+6]=1; visibleGeo[i+7]=1; visibleGeo[i+8]=1;
				visibleGeo[i+9]=0; visibleGeo[i+10]=0;visibleGeo[i+11]=1;
				visibleGeo[i+12]=1;visibleGeo[i+13]=1;visibleGeo[i+14]=1;
			}

			if (val == 0){
				break;
			}
			val = val/10;
		}
		for (int i = 0; i < maxDigits*15; i++){
			if (visibleGeo[i] == 1){
				attachChild(boxGeometries.get(i));
			}else{
				detachChild(boxGeometries.get(i));
			}
		}
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
    }

}

//REFACTORED STATUS: OK.

package Refactoring.View;

import Refactoring.Primary.Main;
import Refactoring.Control.Constant;
import Refactoring.Model.Alpha;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * T4
 * Created by BlackPearl & HeavenVolkoff & yKane
 */

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

public class Piece extends Node implements Cloneable, Savable, Alpha {
	protected Vector3f pos;
	protected Material material;
    protected float alpha;
	protected static final Logger logger = Logger.getLogger(Piece.class.getName());
    private float resizeFactor;

	//======================== Class Constructors ==========================//
	public Piece(String fileName, Vector3f pos, ColorRGBA color){
		super(fileName);
		this.pos = pos;
		this.alpha = 1;
        this.resizeFactor = 1;
        this.material = createColoredMaterial(color);

		setLocalTranslation(pos); //move piece to position

		if (!constructFromString(Main.app.getPieceLoader().getPieceMemoryMap().get(fileName), 1, material)) {
            logger.log(Level.SEVERE, "Can Not Construct Piece {0}.", fileName);
        }
	}

    public Piece(String fileName, Vector3f pos, float resizeFactor, ColorRGBA color){
        super(fileName);
        this.pos = pos;
        this.alpha = 1;
        this.resizeFactor = resizeFactor;
        this.material = createColoredMaterial(color);

        setLocalTranslation(pos); //move piece to position

        if (!constructFromString(Main.app.getPieceLoader().getPieceMemoryMap().get(fileName), resizeFactor, material)) {
            logger.log(Level.SEVERE, "Can Not Construct Piece {0}.", fileName);
        }
    }
    //======================================================================//

	//======================== Material Manager ============================//
	private Material createColoredMaterial(ColorRGBA color){
		Material material = new Material(Main.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
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
	//======================================================================//

	private boolean constructFromString(List<String> lines, float resizeFactor, Material material){
		Vector2f pos = new Vector2f();
		List<Vector2f> pivotPos = new ArrayList<Vector2f>();

		for (int lineNum = 0;lineNum < lines.size(); lineNum++){
			if(lines.get(lineNum).equals("//INFO//")){
				this.setPieceFileProp(lines, lineNum);
                break;
			}else {
                char[] charArray = lines.get(lineNum).toCharArray();
                for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
                    switch (charArray[i]) {
                        case '|':
                            pos.x += Constant.HALFBOXINTERVAL * resizeFactor;
                            break;
                        case ' ':
                            pos.x += Constant.CUBESIZE * resizeFactor;
                            break;
                        case '0':
                            if (charArray[i+1] == '0') {
                                createGeometry("Geometry" + getChildren().size(), new Vector3f(pos.x + Constant.CUBESIZE * resizeFactor, pos.y, this.pos.z), Constant.CUBESIZE * resizeFactor);
                                pos.x += Constant.CUBESIZE * 2 * resizeFactor;
                                i += 1;
                            } else if (charArray[i+1] == 'P') {
                                pivotPos.add(new Vector2f(pos.x + Constant.CUBESIZE * resizeFactor, pos.y));
                                createGeometry("Pivot"+(pivotPos.size()-1), new Vector3f(pos.x + Constant.CUBESIZE * resizeFactor, pos.y, this.pos.z), Constant.CUBESIZE * resizeFactor);
                                pos.x += Constant.CUBESIZE * 2 * resizeFactor;
                                i += 1;
                            }else{
                                logger.log(Level.SEVERE, "Invalid Character {0} at Piece File", i);
                                return false;
                            }
                            break;
                        case 'P':
                            if (charArray[i+1] == 'P') {
                                pivotPos.add(new Vector2f(pos.x + Constant.CUBESIZE * resizeFactor, pos.y));
                                pos.x += Constant.CUBESIZE * 2 * resizeFactor;
                                i += 1;
                            }else{
                                logger.log(Level.SEVERE, "Invalid Character {0} at Piece File", i);
                                return false;
                            }
                            break;
                        default:
                            logger.log(Level.SEVERE, "Invalid Character {0} at Piece File", i);
                            return false;
                    }
                }
                pos.x = 0;
                pos.y -= Constant.MOVEDISTANCE * resizeFactor;
            }
		}

        initializePivotPos(pivotPos);
        return true;
	}

    private void initializePivotPos(List<Vector2f> pivotPos){
        Vector2f geometriesPivotCorrectionVal;
        switch (pivotPos.size()) {
            case 1:
                geometriesPivotCorrectionVal = new Vector2f(-(pivotPos.get(0).x), -(pivotPos.get(0).y));
                break;
            case 2:
                Vector2f topLeftPivot = new Vector2f();
                Vector2f distance = new Vector2f();
                if (pivotPos.get(0).x <= pivotPos.get(1).x){
                    distance.x = pivotPos.get(1).x - pivotPos.get(0).x;
                    topLeftPivot.x = pivotPos.get(0).x;
                }else{
                    distance.x = pivotPos.get(0).x - pivotPos.get(1).x;
                    topLeftPivot.x = pivotPos.get(1).x;
                }
                if (pivotPos.get(0).y <= pivotPos.get(1).y){
                    distance.y = pivotPos.get(1).y - pivotPos.get(0).y;
                    topLeftPivot.y = pivotPos.get(1).y;
                }else{
                    distance.y = pivotPos.get(0).y - pivotPos.get(1).y;
                    topLeftPivot.y = pivotPos.get(0).y;
                }
                geometriesPivotCorrectionVal = new Vector2f(-((distance.x)/2 + topLeftPivot.x), +((distance.y)/2 + topLeftPivot.y));
                break;
            default:
                geometriesPivotCorrectionVal = new Vector2f();
                break;
        }

        for (Spatial geometry : getChildren()) {
            geometry.move(geometriesPivotCorrectionVal.x,geometriesPivotCorrectionVal.y, this.pos.z);
        }
    }

    protected void createGeometry(String name, Vector3f pos, float cubeSize){
        Geometry geometry = new Geometry(name, new Box(cubeSize, cubeSize, cubeSize));
        geometry.setLocalTranslation(pos);
        geometry.setMaterial(this.material);
        attachChild(geometry);
    }

    private void setPieceFileProp(List<String> lines, int lineNum){
        float red = Float.parseFloat(lines.get(lineNum+2)) / Constant.RGB255;
        float green = Float.parseFloat(lines.get(lineNum+4)) / Constant.RGB255;
        float blue = Float.parseFloat(lines.get(lineNum+6)) / Constant.RGB255;
        float alpha = Float.parseFloat(lines.get(lineNum+8));
        if (red != -1 && green != -1 && blue != -1 && alpha != -1) {
            setMaterialColor(this.material, new ColorRGBA(red, green, blue, alpha), 3);
            this.alpha = alpha;
        }
    }

	@Override
    public void setAlpha(float alphaVal){
        for (Spatial boxGeometry : getChildren()) {
            if (boxGeometry != null) {
                ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
                alpha.a = alphaVal;
                ((Geometry)boxGeometry).getMaterial().setColor("Diffuse", alpha);
                ((Geometry)boxGeometry).getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                ((Geometry)boxGeometry).getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
                ((Geometry)boxGeometry).getMaterial().setBoolean("UseAlpha", true);
            }
        }
        alpha = alphaVal;
    }

	@Override
    public float getAlpha(){
        return alpha;
    }

    public Vector3f getPos() {
        return pos;
    }

	public void setPosX(float posX) {
		this.pos.setX(posX);
	}

	public void setPosY(float posY) {
		this.pos.setY(posY);
	}

	public int getNumBox() {
        return getChildren().size();
    }

    public List<Spatial> getBoxGeometries() {
		return getChildren();
	}

    public Material getMat() {
        return material;
    }

    public float getWidth(){
        if (getChildren().size() > 0) {
            float min = getChildren().get(0).getWorldBound().getCenter().x;
            float max = getChildren().get(0).getWorldBound().getCenter().x;
            for (Spatial geo : getChildren()) {
                if (geo.getWorldBound().getCenter().x < min){
                    min = geo.getWorldBound().getCenter().x;
                }
                if (geo.getWorldBound().getCenter().x > max){
                    max = geo.getWorldBound().getCenter().x;
                }
            }
            return max-min+2*Constant.CUBESIZE*resizeFactor;
        }
        return 0;
    }
}

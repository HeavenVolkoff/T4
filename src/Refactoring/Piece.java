package Refactoring;

import com.jme3.asset.AssetManager;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import javax.vecmath.Vector2f;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * T4
 * Created by BlackPearl & HeavenVolkoff & yKane
 */

public abstract class Piece extends Node implements Cloneable, Savable {
	protected float cubeSize;
	protected Vector3f pos;
	protected int numBox;
	protected List<Geometry> boxGeometries;
	protected Material material;
	private float alpha;
	protected static final Logger logger = Logger.getLogger(Piece.class.getName());

	//======================== Class Constructors ==========================//
	public Piece(){} //Serialization only. Do not use.

	public Piece(String fileName, float cubeSize, Vector3f pos, ColorRGBA color, AssetManager assetManager){
		super(fileName);
		this.cubeSize = cubeSize;
		this.pos = pos;
		this.alpha = 1;
		this.boxGeometries = new ArrayList<Geometry>();

		setLocalTranslation(pos); //move piece to position

		try {
			Path pieceFile = Paths.get("./resources/Pieces/" + fileName);
			constructFromString(Files.readAllLines(pieceFile), createColoredMaterial(color, assetManager));
		} catch (IOException exception) {
			logger.log(Level.SEVERE, "Piece file {0} not found, please report this error.", fileName);
			logger.log(Level.FINE, "Piece {0} Constructor Error while loading file, exception {1}", new Object[]{fileName, exception});
		}
	}
	//======================== Material Manager ============================//
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
	//======================================================================//


	private boolean constructFromString(List<String> lines, Material material){
		Constant.BOXINTERVAL.getter();
		Vector2f pos = new Vector2f();
		List<Vector2f> pivotPos = new ArrayList<Vector2f>();
		Vector2f pivotAbsolutePos = new Vector2f();

		for (int lineNum = 0;lineNum < lines.size(); lineNum++){
			if(lines.get(lineNum).equals("//INFO//")){
				this.setPieceFileProp(lines, lineNum);
				break;
			}else {
				for (char i : lines.get(lineNum).toCharArray()) {
					switch (i) {
						case '|':
							pos.x += ;
							break;
						case ' ':

							break;
						case '0':

							break;
						case 'P':

							break;
						default:
							logger.log(Level.SEVERE, "Invalid Character {0} at Piece File", i);
							return false;
							break;
					}
				}
			}
		}
	}
}

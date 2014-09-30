package Refactoring;

import com.jme3.export.JmeExporter;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import java.io.File;
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
	protected Geometry[] boxGeometries;
	protected Material material;
	private float alpha;

	//======================== Class Constructors ==========================//
	public Piece(){} //Serialization only. Do not use.

	public Piece(String fileName, float cubeSize, Vector3f pos, Material material, Logger logger){
		super(fileName);
		this.cubeSize = cubeSize;
		this.pos = pos;
		this.alpha = 1;

		setLocalTranslation(pos); //move piece to position

		try {
			Path pieceFile = Paths.get("./resources/Pieces/" + fileName);
			constructFromString(Files.readAllLines(pieceFile), material, pos.getZ());
		} catch (IOException exception) {
			logger.log(Level.SEVERE, "Pice file {0} not found, please report this error.", fileName);
			logger.log(Level.FINE, "Piece {0} Constructor Error while loading file, exception {1} // {2} // {3}", new Object[]{fileName, exception.getStackTrace(), exception.getSuppressed(), exception.getCause()});
		}
	}

	private boolean constructFromString(List<String> lines, Material mat, float posZ){
		List<Geometry> geometries = new ArrayList<Geometry>();
		List<Float> pivotPosX = new ArrayList<Float>();
		List<Float> pivotPosY = new ArrayList<Float>();
		int boxNum = 0;
		float posX = 0;
		float posY = 0;
		float absolutePivotPosX;
		float absolutePivotPosY;

		for (int lineNum = 0;lineNum < lines.size(); lineNum++){
			for (int i = 0; i<lines.get(lineNum).length(); i++){
				if (lines.get(lineNum).equals("//INFO//")) {
					setPieceFileProp(lines, lineNum);
					break;
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

		this.boxGeometries = new Geometry[boxNum];
		this.numBox = 0;
		for (Geometry geometry : geometries) {
			this.boxGeometries[this.numBox] = geometry;
			geometry.move(-absolutePivotPosX, -1 * (absolutePivotPosY), 0);
			this.numBox += 1;
		}
	}

	private void setMaterialColor(Material material, ColorRGBA color, float shine){
		material.setColor("Ambient", color);
		material.setColor("Diffuse", color);
		material.setColor("Specular", color);
		material.setFloat("Shininess", shine);
		material.setBoolean("UseMaterialColors", true);
	}

	public void write(Logger logger) throws IOException{
		BinaryExporter exporter = BinaryExporter.getInstance();
		File file = new File("./resources/PieceModels");
		try {
			exporter.save(this, file);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error: Failed to save game!, exception: {0}", ex);
		}
	}

}

package Refactoring;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * T4
 * Created by BlackPearl & HeavenVolkoff & yKane
 */

public abstract class Piece extends Node implements Cloneable {
	protected String fileName;
	protected float cubeSize;
	protected float posX;
	protected float posY;
	protected int numBox;
	protected Vector3f[] boxAbsolutePoint;
	protected Geometry[] boxGeometries;
	protected Material material;
	private int Invert;
	private float alpha;

	//======================== Class Constructors ==========================//
	public Piece(){} //Serialization only. Do not use.

	public Piece(String fileName, float cubeSize, Vector3f pos, Material material, Control controller){
		super(fileName);

		if(controller != null || controller.isSetSpatial()){
			addControl(controller);
		}
	}

}

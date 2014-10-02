package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Primary.Main;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import java.util.logging.Logger;

/**
 * Created by blackpearl on 01/10/14.
 */
public class Frame extends Node {

    protected Vector3f pos;
    protected Material material;
    private float alpha;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());

    //======================== Class Constructors ==========================//
    public Frame(String fileName, Vector3f pos, ColorRGBA color) {
        super(fileName);
        this.pos = pos;
        this.alpha = 1;
        this.material = createColoredMaterial(ColorRGBA.randomColor());

        setLocalTranslation(pos); //move piece to position

    }

    //======================== Material Manager ============================//
    private Material createColoredMaterial(ColorRGBA color){
        this.material = new Material(Main.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
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

    private void createBar(String name, int barType, Vector3f pos){
        Geometry geometry = new Geometry(name, new Box(Constant.CUBESIZE, Constant.CUBESIZE, Constant.CUBESIZE));
        geometry.setLocalTranslation(pos);
        geometry.setMaterial(this.material);
        attachChild(geometry);
    }

}

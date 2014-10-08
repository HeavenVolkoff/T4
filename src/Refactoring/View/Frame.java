//REFACTORED STATUS: OK.

package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Model.Alpha;
import Refactoring.Primary.Main;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Created by blackpearl on 01/10/14.
 */
public class Frame extends Node implements Alpha{

    protected Vector3f pos;
    protected Vector3f size;
    protected Material material;
    protected float frameAlpha;

    //======================== Class Constructors ==========================//
    public Frame(String name, String parts, Vector3f pos, Vector3f size, float thickness, ColorRGBA color) {
        super(name);
        this.pos = pos;
        this.frameAlpha = 1;
        this.material = createColoredMaterial(color);
        this.size = size;

        setLocalTranslation(pos); //move piece to position

        constructFrames(parts, thickness, size.z);
    }
    //=====================================================================//

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

    private void constructFrames(String parts, float thickness, float depth){
        char[] charArray = parts.toUpperCase().toCharArray();
        for (char aChar : charArray) {
            switch (aChar) {
                case 'T':
                    createBar("Top", new Vector3f(0, (this.size.y / 2) + (thickness / 2), 0), new Vector2f(this.size.x/2, thickness), depth);
                    break;
                case 'R':
                    createBar("Right", new Vector3f((this.size.x / 2) + (thickness / 2), 0, 0), new Vector2f(thickness, (this.size.y/2)+thickness* Constant.THICKNESSCORRECTION), depth);
                    break;
                case 'B':
                    createBar("Bottom", new Vector3f(0, -((this.size.y / 2) + (thickness / 2)), 0), new Vector2f(this.size.x/2, thickness), depth);
                    break;
                case 'L':
                    createBar("Left", new Vector3f(-((this.size.x / 2) + (thickness / 2)), 0, 0), new Vector2f(thickness, (this.size.y/2)+thickness*Constant.THICKNESSCORRECTION), depth);
                    break;
                default:
                    break;
            }
        }
    }

    private void createBar(String name, Vector3f pos, Vector2f size, float depth){
        Geometry geometry = new Geometry(name, new Box(size.x, size.y, depth));
        geometry.move(pos);
        geometry.setMaterial(this.material);
        attachChild(geometry);
    }

	protected Vector3f getBarPos(String part){
		switch (part.charAt(0)) {
			case 'T':
				return getChild("Top").getWorldBound().getCenter();
			case 'R':
				return getChild("Right").getWorldBound().getCenter();
			case 'B':
				return getChild("Bottom").getWorldBound().getCenter();
			case 'L':
				return getChild("Left").getWorldBound().getCenter();
			default:
				return null;
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
        frameAlpha = alphaVal;
    }

	@Override
    public float getAlpha(){
        return frameAlpha;
    }
}
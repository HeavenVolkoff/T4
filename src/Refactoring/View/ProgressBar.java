package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Primary.Main;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 10/11/2014.
 */
public class ProgressBar extends Frame {

    private float max;
    private float progress;
    private float width;
    private ColorRGBA barColor;
    private Geometry progressBar;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());

    public ProgressBar(Vector3f pos, Vector3f size, float max, float progress, ColorRGBA frameColor, ColorRGBA barColor) {
        super("ProgressBar", Constant.TOP+Constant.LEFT+Constant.BOTTOM+Constant.RIGHT, pos, size, Constant.PROGRESSBARTHICKNESS, frameColor);

        this.max = max;
        this.barColor = barColor;

        this.width = 0;
        setProgress(progress);
    }

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

    public void setProgress(float progress){
        if (progress <= this.max) {
            if (this.width == 0) {
                detachChildNamed("ProgressGeo");
                this.progress = progress;
                this.width = (this.progress * ((this.size.x / 2) - (Constant.PROGRESSBARSPACING))) / max;
                Box box = new Box(this.width, (size.y / 2) - Constant.PROGRESSBARSPACING * 2, size.z);
                progressBar = new Geometry("ProgressGeo", box);
                progressBar.move(-((((this.size.x / 2) - (Constant.PROGRESSBARSPACING))) - this.width), 0, 0);
                progressBar.setMaterial(createColoredMaterial(this.barColor));
                attachChild(progressBar);
            } else {
                float oldWidth = this.width;
                this.progress = progress;
                this.width = (this.progress * ((this.size.x / 2) - (Constant.PROGRESSBARSPACING))) / max;
                float scaleFactor = this.width / oldWidth;
                progressBar.scale(scaleFactor, 1, 1);
                progressBar.setLocalTranslation(-((((this.size.x / 2) - (Constant.PROGRESSBARSPACING))) - this.width), 0, 0);
            }
        }else{
            logger.log(Level.WARNING, "ProgressBar Value Exceeded Max Value. (Max = {0} / Value = {1})", new Object[]{this.max, progress});
        }
    }

    public void setMax(float max) {
        this.max = max;
        setProgress(this.progress);
    }

    public float getProgress() {
        return progress;
    }
}

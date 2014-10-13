package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Primary.Main;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
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
    private ParticleEmitter particlesBar;
    private float frameAlpha;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());

    public ProgressBar(Vector3f pos, Vector3f size, float max, float progress, ColorRGBA frameColor, ColorRGBA barColor) {
        super("ProgressBar", Constant.TOP+Constant.LEFT+Constant.BOTTOM+Constant.RIGHT, pos, size, Constant.PROGRESSBARTHICKNESS, frameColor);

        this.max = max;
        this.barColor = barColor;

        generateParticles();

        this.width = 0;
        setProgress(progress);
    }

    private void generateParticles(){
        float size = (this.size.y/5f);
        Material particleMat = new Material(Main.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        particleMat.setTexture("Texture", Main.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
        particlesBar = new ParticleEmitter("BarParticles", ParticleMesh.Type.Triangle, 30);
        particlesBar.setMaterial(particleMat);
        particlesBar.setImagesX(2);
        particlesBar.setImagesY(2); // 2x2 texture animation
        particlesBar.setEndColor( new ColorRGBA(1f, 0f, 0f, 0.1f));   // red
        particlesBar.setStartColor(new ColorRGBA(1f, 0.6f, 0f, 0.2f)); // yellow
        particlesBar.getParticleInfluencer().setInitialVelocity(new Vector3f(-size*0.1f,size*1,0));
        particlesBar.setNumParticles(40);
        particlesBar.setStartSize(size*5f);
        particlesBar.setEndSize(size);
        particlesBar.setGravity(0,0,0);
        particlesBar.setLowLife(0.5f);
        particlesBar.setHighLife(3f);
        particlesBar.getParticleInfluencer().setVelocityVariation(size*2f);
        particlesBar.setLocalTranslation(pos);
        attachChild(particlesBar);
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
            if (particlesBar != null){
                particlesBar.setLocalTranslation(progressBar.getLocalTranslation().x+width, progressBar.getLocalTranslation().y, size.z*1.5f);
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

    public ParticleEmitter getParticlesBar() {
        return particlesBar;
    }

    @Override
    public void setAlpha(float alphaVal){
        detachChild(particlesBar);
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

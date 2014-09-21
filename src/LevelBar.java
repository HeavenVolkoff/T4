import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
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

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class LevelBar extends Node {

    protected Geometry[] frame;
    protected Geometry percentageGeo;
    private Material percentageGeoMat;
    protected float percentageGeoWidth;
    private int max;
	private int score;
    private float posX;
    private float posY;
    private float barWidth;
    protected float cubeSize;
    AssetManager assetManager;
    private List<List<String>> numbers;
    private float numWidth;
    private int maxDigits;
    private Piece[] lvlDigits;
    private ParticleEmitter particlesLvlBar;
    private Piece lvlPiece;


    public LevelBar(float cubeSize, int maxDigits, float posX, float posY, float barWidth, int max, ColorRGBA color, AssetManager assetManager, EffectController effectController){
        this.posX = posX;
        this.posY = posY;
        this.barWidth = barWidth;
        this.max = max;
        this.cubeSize = cubeSize;
        this.assetManager = assetManager;
        this.maxDigits = maxDigits;

		if (effectController != null) {
			addControl(effectController);
		}

        generateParticles();

        lvlDigits = new Piece[maxDigits];

        generateLevelBarFrame(generateFrameMaterial(assetManager));

        createColoredMaterial(color, assetManager);

        this.percentageGeoWidth = 0f;
        Box box = new Box(percentageGeoWidth,cubeSize,cubeSize);
        percentageGeo= new Geometry("ProgressBar",box);
        percentageGeo.setLocalTranslation(new Vector3f(posX - barWidth * 1.5f, posY, 0));
        percentageGeo.setMaterial(percentageGeoMat);
        correctBarXPos();
        attachChild(percentageGeo);

        lvlPiece = new Piece(cubeSize*0.3f, posX + barWidth * 0.10f, posY - 0.25f * cubeSize, 0, "LVL.piece",ColorRGBA.White, assetManager, null);
        attachChild(lvlPiece);

        this.numbers = new ArrayList<List<String>>();

        for(int i = 0; i<=9; i++){
            numbers.add(loadFromFile( i+".piece" ));
        }

        this.numWidth = ((maxDigits*(3*cubeSize*2.5f*0.3f)) - cubeSize*0.5f*0.3f) + ((maxDigits-1f)*cubeSize*0.5f*0.3f);

        showLevel();
    }

    private void generateParticles(){
        Material particleMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        particleMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        particlesLvlBar = new ParticleEmitter("LvlBarParticles", ParticleMesh.Type.Triangle, 30);
        particlesLvlBar.setMaterial(particleMat);
        particlesLvlBar.setImagesX(2);
        particlesLvlBar.setImagesY(2); // 2x2 texture animation
        particlesLvlBar.setEndColor(  new ColorRGBA(1f, 0f, 0f, 0.4f));   // red
        particlesLvlBar.setStartColor(new ColorRGBA(1f, 0.6f, 0f, 0.4f)); // yellow
        particlesLvlBar.getParticleInfluencer().setInitialVelocity(new Vector3f(-cubeSize*0.1f,cubeSize*1,0));
        particlesLvlBar.setNumParticles(40);
        particlesLvlBar.setStartSize(cubeSize*5f);
        particlesLvlBar.setEndSize(cubeSize);
        particlesLvlBar.setGravity(0,0,0);
        particlesLvlBar.setLowLife(0.5f);
        particlesLvlBar.setHighLife(3f);
        particlesLvlBar.getParticleInfluencer().setVelocityVariation(cubeSize*2f);
    }

    private void generateLevelBarFrame(Material mat){
        frame = new Geometry[4];

        Box box = new Box(barWidth,0.25f*cubeSize,cubeSize);
        frame[0] = new Geometry("TopFrame",box);
        frame[0].setLocalTranslation(new Vector3f(posX-barWidth*0.5f,posY+2.25f*cubeSize,0));
        frame[0].setMaterial(mat);
        attachChild(frame[0]);

        box = new Box(barWidth,0.25f*cubeSize,cubeSize);
        frame[1] = new Geometry("BottomFrame",box);
        frame[1].setLocalTranslation(new Vector3f(posX - barWidth * 0.5f, posY - 2.25f * cubeSize, 0));
        frame[1].setMaterial(mat);
        attachChild(frame[1]);

        box = new Box(0.25f*cubeSize,2.5f*cubeSize,cubeSize);
        frame[2] = new Geometry("LeftFrame",box);
        frame[2].setLocalTranslation(new Vector3f(posX - barWidth * 1.5f - 0.25f * cubeSize, posY, 0));
        frame[2].setMaterial(mat);
        attachChild(frame[2]);

        box = new Box(0.25f*cubeSize,2.5f*cubeSize,cubeSize);
        frame[3] = new Geometry("RightFrame",box);
        frame[3].setLocalTranslation(new Vector3f(posX + barWidth * 0.5f + 0.25f * cubeSize, posY, 0));
        frame[3].setMaterial(mat);
        attachChild(frame[3]);
    }

    private Material generateFrameMaterial(AssetManager assetManager){
        //============================== Frame Material Def =======================
        Material frameMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        frameMaterial.setColor("Ambient", ColorRGBA.DarkGray);
        frameMaterial.setColor("Diffuse", ColorRGBA.DarkGray);
        frameMaterial.setColor("Specular", ColorRGBA.DarkGray);
        frameMaterial.setFloat("Shininess", 2);
        frameMaterial.setBoolean("UseMaterialColors", true);
        return frameMaterial;
        //=========================================================================
    }

    public void showLevel() { //
        int level = Main.app.getScore().getLevel();
        float piecePosX = -(posX - barWidth * 1.5f - 1.5f * cubeSize - 3*maxDigits*cubeSize*0.8f);
        int counter = 0;

		do{
			if (lvlDigits[counter] != null){
				detachChild(lvlDigits[counter]);
			}
			lvlDigits[counter] = new Piece(this.cubeSize * 0.3f, 0f, 0f, 0, numbers.get(level % 10), -1, ColorRGBA.White, assetManager, null);
			lvlDigits[counter].move((float) (this.numWidth * 0.5) - (piecePosX), posY - 0.25f * cubeSize, 0f);
			piecePosX = piecePosX + 2.5f * cubeSize * 0.3f * 3 + 1.5f * cubeSize * 0.3f;

			level = level / 10;

			attachChild(lvlDigits[counter]);
			counter++;
		}while (level != 0);
    }

    public List<String> loadFromFile(String fileName) {
        try {
            Path path = Paths.get("./resources/customPieces/numbers/" + fileName);
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Material createColoredMaterial(ColorRGBA color, AssetManager assetManager){
        percentageGeoMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        setMaterialColor(percentageGeoMat, color, 2);

        return percentageGeoMat;
    }

    private void setMaterialColor(Material material, ColorRGBA color, float shine){
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);
        material.setColor("Specular", color);
        material.setFloat("Shininess", shine);
        material.setBoolean("UseMaterialColors", true);
    }

    private void correctBarXPos(){
        percentageGeo.setLocalTranslation(frame[2].getWorldBound().getCenter().getX()+0.25f*cubeSize+percentageGeoWidth,percentageGeo.getWorldBound().getCenter().getY(),percentageGeo.getWorldBound().getCenter().getZ());
        particlesLvlBar.setLocalTranslation(percentageGeo.getWorldBound().getCenter().getX()+percentageGeoWidth,percentageGeo.getWorldBound().getCenter().getY(),percentageGeo.getWorldBound().getCenter().getZ()+cubeSize*1.2f);
    }

    public void setValue(int value){
        if (percentageGeoWidth == 0) {//setLocalScale bug-fix for 0 size spatial
			detachChild(percentageGeo);
			percentageGeoWidth = 1f;
			Box box = new Box(percentageGeoWidth, cubeSize, cubeSize);
			percentageGeo = new Geometry("ProgressBar", box);
			percentageGeo.setMaterial(percentageGeoMat);
			percentageGeo.move(0, posY, 0);
			correctBarXPos();
			attachChild(percentageGeo);
		}
		percentageGeoWidth = (value*100/max)*barWidth/100;
		this.percentageGeo.setLocalScale(percentageGeoWidth,1,1);
		correctBarXPos();
    }

    public void setMax(int max) {
        this.max = max;
    }

	public void resetPercentageGeo(){
		detachChild(percentageGeo);
		percentageGeoWidth = 0f;
		Box box = new Box(percentageGeoWidth, cubeSize, cubeSize);
		percentageGeo = new Geometry("ProgressBar", box);
		percentageGeo.setMaterial(percentageGeoMat);
		percentageGeo.move(0, posY, 0);
		correctBarXPos();
		attachChild(percentageGeo);
	}

    public void setFrameAlpha(float alphaVal){
        for (int i = 0; i < frame.length; i++){
            if (frame[i] != null){
                ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
                alpha.a = alphaVal;
                frame[i].getMaterial().setColor("Diffuse", alpha);
                frame[i].getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                frame[i].getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
                frame[i].getMaterial().setBoolean("UseAlpha",true);
            }
        }
    }

    public void setBarAlpha(float alphaVal) {
        ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
        alpha.a = alphaVal;
        percentageGeo.getMaterial().setColor("Diffuse", alpha);
        percentageGeo.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        percentageGeo.getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaVal);
        percentageGeo.getMaterial().setBoolean("UseAlpha", true);
    }

    public void setLvlAlpha(float alphaVal){
        for (int i = 0; i < lvlDigits.length; i++){
            if (lvlDigits[i] != null){
                lvlDigits[i].setAlpha(alphaVal);
            }
        }
    }

    public void setAlpha(float alphaVal){
        setFrameAlpha(alphaVal);
        setBarAlpha(alphaVal);
        setLvlAlpha(alphaVal);
        if (lvlPiece != null) {
            lvlPiece.setAlpha(alphaVal);
        }
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

    public ParticleEmitter getParticlesLvlBar() {
        return particlesLvlBar;
    }
}

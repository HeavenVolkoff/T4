package Tetris;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Created by blackpearl on 12/09/14.
 */
public class LevelBar extends Node {

    private Geometry[] frame;
    private Geometry percentageGeo;
    private Material percentageGeoMat;
    private float percentageGeoWidth;
    private int max;
    private int value;
    private float posX;
    private float posY;
    private float barWidth;
    private float cubeSize;


    public LevelBar(float cubeSize, float posX, float posY, float barWidth, int max, Material mat, ColorRGBA color, AssetManager assetManager){
        this.posX = posX;
        this.posY = posY;
        this.barWidth = barWidth;
        this.max = max;
        this.value = 0;
        this.cubeSize = cubeSize;

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

        createColoredMaterial(color, assetManager);

        this.percentageGeoWidth = 0f;
        box = new Box(percentageGeoWidth,cubeSize,cubeSize);
        percentageGeo= new Geometry("ProgressBar",box);
        percentageGeo.setLocalTranslation(new Vector3f(posX - barWidth * 1.5f, posY, 0));
        percentageGeo.setMaterial(percentageGeoMat);
        correctBarXPos();
        attachChild(percentageGeo);
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
    }

    public void setValue(int value){
        detachChild(percentageGeo);
        percentageGeoWidth = (value*100/max)*barWidth/100;
        Box box = new Box(percentageGeoWidth,cubeSize,cubeSize);
        percentageGeo= new Geometry("ProgressBar",box);
        percentageGeo.setMaterial(percentageGeoMat);
        percentageGeo.move(0, posY, 0);
        correctBarXPos();
        attachChild(percentageGeo);
    }

    public void setMax(int max) {
        this.max = max;
    }
}

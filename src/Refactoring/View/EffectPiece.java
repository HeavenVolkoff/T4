package Refactoring.View;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import org.lwjgl.Sys;

import java.util.*;

/**
 * Created by Raphael on 10/12/2014.
 */
public class EffectPiece extends Piece {

    List<Spatial> selectBoxes;
    List<Spatial> priorSelectBoxes;
    int numberOfAlphaBoxes;
    float minAlpha;
    NavigableMap<String, Float> AlphaIndicator;
    ColorRGBA color;

    public EffectPiece(String fileName, int numberOfAlphaBoxes, float minAlpha, Vector3f pos, ColorRGBA color) {
        super(fileName, pos, color);

        this.numberOfAlphaBoxes = numberOfAlphaBoxes;
        this.minAlpha = minAlpha;
        this.color = color;

        priorSelectBoxes = new ArrayList<Spatial>();
        selectBoxes = new ArrayList<Spatial>();

        AlphaIndicator = new TreeMap<>();

        for (Spatial geo : getChildren()) {
            AlphaIndicator.put(geo.getName(), 1f);
        }

        selectRandomBoxes();
    }

    public void selectRandomBoxes(){
        List<Spatial> selectedGeo = new ArrayList<Spatial>();
        for (int i = 0; i < numberOfAlphaBoxes; i++){
            int idx = new Random().nextInt(getChildren().size());
            selectedGeo.add((getChildren().get(idx)));
        }
        selectBoxes = selectedGeo;
    }

    public void setGeoAlpha(Spatial geo, float alphaVal){
        if (geo != null) {
            ColorRGBA alpha = new ColorRGBA(this.color);
            alpha.a = alphaVal;
            Material material = createColoredMaterial(this.color);
            material.setColor("Diffuse", alpha);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            material.getAdditionalRenderState().setAlphaFallOff(alphaVal);
            material.setBoolean("UseAlpha", true);
            geo.setMaterial(material);
            AlphaIndicator.put(geo.getName(), alphaVal);
        }
    }

    public float getGeoAlpha(Spatial geo){
        return AlphaIndicator.get(geo.getName());
    }

    public void alphaEffect(float tpf) {
        int count = 0;
        for (int i = 0; i < selectBoxes.size(); i++) {
            if (getGeoAlpha(selectBoxes.get(i)) - tpf >= minAlpha) {
                setGeoAlpha(selectBoxes.get(i), getGeoAlpha(selectBoxes.get(i)) - tpf);
            } else {
                setGeoAlpha(selectBoxes.get(i), minAlpha);
            }
            if (getGeoAlpha(selectBoxes.get(i)) <= minAlpha) {
                priorSelectBoxes.add(selectBoxes.get(i));
                count++;
            }
        }

        for (Spatial geo : priorSelectBoxes){
            if (selectBoxes.contains(geo)){
                selectBoxes.remove(geo);
            }
        }
        if (selectBoxes.size() == 0){
            selectRandomBoxes();
        }

        for (int i = 0; i < priorSelectBoxes.size(); i++) {
            if (getGeoAlpha(priorSelectBoxes.get(i)) < 1) {
                if (getGeoAlpha(priorSelectBoxes.get(i)) + tpf <= 1) {
                    setGeoAlpha(priorSelectBoxes.get(i), getGeoAlpha(priorSelectBoxes.get(i)) + tpf);
                } else {
                    setGeoAlpha(priorSelectBoxes.get(i), 1);
                }
            } else {
                setGeoAlpha(priorSelectBoxes.get(i), 1);
                priorSelectBoxes.remove(i);
            }
        }
    }
}

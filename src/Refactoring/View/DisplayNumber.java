package Refactoring.View;

import Refactoring.Control.Constant;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 10/11/2014.
 */
public class DisplayNumber extends Node {

    private BoxedNumber[] numbers;
    private Vector3f pos;
    private int maxDigits;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());
    private int value;
    private float size;
    private float frameAlpha;

    public DisplayNumber(Vector3f pos, float resizeFactor, int initialValue, int maxDigits, ColorRGBA color){
        this.pos = pos;
        this.numbers = new BoxedNumber[maxDigits];
        this.maxDigits = maxDigits;
        this.frameAlpha = 1;

        setLocalTranslation(this.pos);

        setupNumbers(resizeFactor, color);

        write(initialValue);
    }

    private void setupNumbers(float resizeFactor, ColorRGBA color){
        size = ((((3*Constant.CUBESIZE)+(2*Constant.BOXINTERVAL))*maxDigits)+(Constant.CUBESIZE*4)*(maxDigits-1))*resizeFactor;
        for (int i = 0; i < this.maxDigits; i++){
            numbers[i] = new BoxedNumber(new Vector3f(-(size/2)+(i*((3*Constant.CUBESIZE)+(2*Constant.BOXINTERVAL)+(Constant.CUBESIZE*4))*resizeFactor), 0, 0), resizeFactor, null, color);
            attachChild(numbers[i]);
        }
    }

    public void write(int value){
        int val = value;
        if (String.valueOf(value).length() <= maxDigits) {
            for (int i = 0; i < this.maxDigits; i++) {
                if (val != 0) {
                        numbers[this.maxDigits - 1 - i].write(val % 10);
                        val = val / 10;
                } else {
                    if (i == 0) {
                        numbers[this.maxDigits - 1 - i].write(0);
                    }else {
                        numbers[this.maxDigits - 1 - i].writeNull();
                    }
                }
                attachChild(numbers[this.maxDigits - 1 - i]);
            }
            this.value = value;
        }else{
            this.value = 9;
            for (int i = 0; i < this.maxDigits; i++) {
                numbers[i].write(9);
                this.value += (i*10)*9;
            }
            logger.log(Level.WARNING, "Out of Range Number Send to DisplayNumber {0}.", val);
        }
    }

    public float getSize() {
        return size;
    }

    public int getValue() {
        return value;
    }

    public void setAlpha(float alphaVal){
        for(BoxedNumber boxed : numbers){
            boxed.setAlpha(alphaVal);
        }
        frameAlpha = alphaVal;
    }

    public float getAlpha(){
        return frameAlpha;
    }
}
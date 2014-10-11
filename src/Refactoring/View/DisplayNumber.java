package Refactoring.View;

import Refactoring.Control.Constant;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

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

    public DisplayNumber(Vector3f pos, float resizeFactor, int initialValue, int maxDigits, ColorRGBA color){
        this.pos = pos;
        this.numbers = new BoxedNumber[maxDigits];
        this.maxDigits = maxDigits;

        setLocalTranslation(this.pos);

        setupNumbers(resizeFactor, color);

        write(initialValue);
    }

    private void setupNumbers(float resizeFactor, ColorRGBA color){
        float size = ((((3*Constant.CUBESIZE)+(2*Constant.BOXINTERVAL))*maxDigits)+(Constant.CUBESIZE*4)*(maxDigits-1))*resizeFactor;
        for (int i = 0; i < this.maxDigits; i++){
            numbers[i] = new BoxedNumber(new Vector3f(-(size/2)+(i*((3*Constant.CUBESIZE)+(2*Constant.BOXINTERVAL)+(Constant.CUBESIZE*4))*resizeFactor), pos.y, pos.z), resizeFactor, null, color);
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
        }else{
            for (int i = 0; i < this.maxDigits; i++) {
                numbers[i].write(9);
            }
            logger.log(Level.WARNING, "Out of Range Number Send to DisplayNumber {0}.", val);
        }
    }
}
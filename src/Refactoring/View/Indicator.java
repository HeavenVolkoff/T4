package Refactoring.View;

import Refactoring.Control.Constant;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Created by Raphael on 10/12/2014.
 */
public class Indicator extends Node {

    DisplayNumber display;
    Piece label;

    public Indicator(String pieceFileName, Vector3f pos, float resizeFactor, int initialLvl, ColorRGBA color){
        display = new DisplayNumber(pos, resizeFactor, initialLvl, 2, color);
        label = new Piece(pieceFileName, pos, resizeFactor, color);
        float labelSize = label.getWidth();
        float lvlSize = display.getSize();
        label.move((labelSize/2)-((lvlSize+labelSize)/2),0,0);
        display.move((labelSize)+(lvlSize/2)+(Constant.CUBESIZE*10*resizeFactor)-((lvlSize+labelSize)/2),0,0);
        attachChild(label);
        attachChild(display);
    }

    public void writeValue(int val){
        display.write(val);
    }

    public int getValue(){
        return display.getValue();
    }
}

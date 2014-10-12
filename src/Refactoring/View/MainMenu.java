package Refactoring.View;

import Refactoring.Control.MenuControler;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Created by Raphael on 10/12/2014.
 */
public class MainMenu extends Node {

    Piece t4Piece;

    public MainMenu(MenuControler controler, ColorRGBA t4Color, ColorRGBA menuItensColor){
        if (controler != null){
            addControl(controler);
        }

        t4Piece = new Piece("T4.piece", new Vector3f(0,2.5f,0), t4Color);
        t4Piece.rotate(0,(float)Math.toRadians(-15),0);
        attachChild(t4Piece);
    }


}

package Refactoring.View;

import Refactoring.Control.MenuControler;
import Refactoring.Primary.Main;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Created by Raphael on 10/12/2014.
 */
public class MainMenu extends Node {

    EffectPiece t4Piece;
    MenuOptions menuOptions;

    public MainMenu(MenuControler controler, ColorRGBA t4Color, MenuOptions menuOptions){
        if (controler != null){
            addControl(controler);
        }
        if (menuOptions != null){
            this.menuOptions = menuOptions;
        }

        t4Piece = new EffectPiece("T4.piece", 5, 0.15f, new Vector3f(0,2.5f,0), t4Color);
        t4Piece.rotate(0,(float)Math.toRadians(-15),0);
        attachChild(t4Piece);
    }


    public EffectPiece getT4Piece() {
        return t4Piece;
    }

    public MenuOptions getMenuOptions() {
        return menuOptions;
    }

    public void setupText(String info, ColorRGBA color){
        BitmapText hudText = new BitmapText(Main.app.getGuiFont(), false);
        hudText.setSize(Main.app.getGuiFont().getCharSet().getRenderedSize());      // font size
        hudText.setColor(color);                             // font color
        hudText.setText(info);             // the text
        hudText.setLocalTranslation(0, hudText.getLineHeight(), 0); // position
        Main.app.getGuiNode().attachChild(hudText);
    }
}

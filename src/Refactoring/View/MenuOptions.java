package Refactoring.View;

import Refactoring.Control.Constant;
import Refactoring.Primary.Main;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Created by Raphael on 10/12/2014.
 */
public class MenuOptions extends Node {

    Vector3f pos;
    String[] menuItems;
    float[] menuWidth;
    int selectedMenu;
    ColorRGBA color;
    ColorRGBA highlightColor;
    Piece[] itemPieces;

    public MenuOptions(Vector3f pos, String[] menuItems, String[] itemPieceNames, int selectedMenu, ColorRGBA color, ColorRGBA highlightColor){
        this.pos = pos;
        this.menuItems = menuItems;
        this.selectedMenu = selectedMenu;
        this.color = color;
        this.menuWidth = new float[menuItems.length];
        this.itemPieces = new Piece[menuItems.length];
        this.highlightColor = highlightColor;

        buildMenu(itemPieceNames);
    }

    private void buildMenu(String[] itemPieceNames){
        float posY = pos.y;
        for (int i = 0; i < menuItems.length; i++){
            Spatial menuItem = Main.app.getAssetManager().loadModel("Models/"+menuItems[i]);
            menuItem.setName(menuItems[i]);
            menuItem.setLocalTranslation(0,posY,0);
            posY -= Constant.MENUITEMSDISTANCE;
            menuItem.setMaterial(createColoredMaterial(this.color));
            attachChild(menuItem);
        }
        for (int i = 0; i < getChildren().size(); i++){
            getChildren().get(i).move(0,-(posY)/2,0);
            menuWidth[i] = ( (BoundingBox)getChildren().get(i).getWorldBound()).getXExtent();
            itemPieces[i] = new Piece(itemPieceNames[i], getChildren().get(i).getWorldBound().getCenter(), 0.4f, color);
        }
        for (int i = 0; i < itemPieces.length; i++){
            itemPieces[i].move(-(menuWidth[i] + itemPieces[i].getWidth()), 0 ,0);
            attachChild(itemPieces[i]);
        }
        getChild(menuItems[selectedMenu]).setMaterial(createColoredMaterial(highlightColor));
    }

    public void rotateSelected(float tpf){
        itemPieces[selectedMenu].rotate(0, 0, tpf);
    }

    public void selectOption(int option){
        if (option >= 0 && option < menuItems.length){
            selectedMenu = option;
            for (int i = 0; i < menuItems.length; i++){
                if (i == selectedMenu){
                    getChild(menuItems[i]).setMaterial(createColoredMaterial(highlightColor));
                }else{
                    getChild(menuItems[i]).setMaterial(createColoredMaterial(color));
                }
            }
        }
    }

    public int getSelectedMenu() {
        return selectedMenu;
    }

    public void executeAction(){
        switch (menuItems[selectedMenu]){
            case "EndlessModeItem.j3o":
                Main.app.startEndless();
                break;

            case "ExitItem.j3o":
                Main.app.requestClose(true);
                break;

            default:
                break;
        }
    }

    //======================== Material Manager ============================//
    protected Material createColoredMaterial(ColorRGBA color){
        Material material = new Material(Main.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        setMaterialColor(material, color, 2);

        return material;
    }

    protected void setMaterialColor(Material material, ColorRGBA color, float shine){
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);
        material.setColor("Specular", color);
        material.setFloat("Shininess", shine);
        material.setBoolean("UseMaterialColors", true);
    }
    //======================================================================//

}

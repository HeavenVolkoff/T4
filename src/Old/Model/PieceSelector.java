package Old.Model;

import Old.View.Piece;
import Old.Primary.Main;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class PieceSelector extends Node {

    private  NavigableMap<Double, String> map;
    private  Random random;
    private double total = 0;
    private List<String> nameList;
    private List<List<String>> pieceList;
    private Piece unlockPiece;
    private boolean unlock;

    public PieceSelector(List<String> nameList, AssetManager assetManager){
        this.map = new TreeMap<Double, String>();
        this.random = new Random();

        this.nameList = nameList;

        pieceList = new ArrayList<List<String>>();
        buildPieceList(this.nameList);

        buildMap();

        unlock = false;
        unlockPiece = new Piece(0.07f, 0, 0, 1.2f, "Messages/NewUnlock.piece", ColorRGBA.Orange, assetManager);
    }

    private void unlockedByLvl(){
        unlock = true;
        unlockPiece.setAlpha(0f);
        attachChild(unlockPiece);
     }

    public void verifyUnlockedPieces(int lvl) {
		for(int i = 0; i < pieceList.size(); i++){
            for (int lineNum = 0; lineNum < pieceList.get(i).size(); lineNum++) {
				if (pieceList.get(i).get(lineNum).equals("//INFO//")) {
                    verifyUnlockProp(pieceList.get(i), lineNum, nameList.get(i), lvl);
                    lineNum = pieceList.get(i).size() - 1;
                }
            }
        }
    }

    private void verifyUnlockProp(List<String> lines, int lineNum, String fileName, int lvl){
        float lvlMin = -1;
        float chance = -1;
        if (lines.size()-1>=lineNum+10){
            lvlMin = Float.parseFloat(lines.get(lineNum + 10));
        }
        if (lines.size()-1>=lineNum+12) {
            chance = Float.parseFloat(lines.get(lineNum + 12));
        }

        if (lvlMin == lvl){
            if (lvlMin != -1) {
                addToMap(chance, fileName);
                unlockedByLvl();
            }
        }
    }

    private void buildMap(){
		for (String aNameList : nameList) {
			addFromString(loadFromFile(aNameList), aNameList);
		}
    }

    private void addFromString(List<String> lines, String fileName){
        for (int lineNum = 0;lineNum < lines.size(); lineNum++){
            for (int i = 0; i<lines.get(lineNum).length(); i++){
                if (lines.get(lineNum).equals("//INFO//")) {
                    verifyStringProp(lines, lineNum, fileName);
                    lineNum = lines.size()-1;
                }
            }
        }
    }

    private void verifyStringProp(List<String> lines, int lineNum, String fileName){
        float lvlMin = -1;
        float chance = -1;
        if (lines.size()-1>=lineNum+10){
            lvlMin = Float.parseFloat(lines.get(lineNum + 10));
        }
        if (lines.size()-1>=lineNum+12) {
            chance = Float.parseFloat(lines.get(lineNum + 12));
        }

        if (lvlMin != -1 && lvlMin <= Main.app.getScore().getLevel()){
            if (lvlMin != -1) {
                addToMap(chance, fileName);
            }
        }
    }

    private void buildPieceList(List<String> files){
        for (String fileName : files){
            pieceList.add(loadFromFile(fileName));
        }
    }

    private List<String> loadFromFile(String fileName) {
        try {
            Path path = Paths.get("./resources/Pieces/" + fileName);
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addToMap(double weight, String itemName) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, itemName);
    }

    public String randomizeFromMap() {
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }

    public Piece getUnlockPiece() {
        return unlockPiece;
    }

    public boolean isUnlock() {
        if (unlockPiece.getAlpha() >= 1){
            unlock = false;
        }
        return unlock;
    }
}

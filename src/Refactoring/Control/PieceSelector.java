package Refactoring.Control;

import Refactoring.Primary.Main;

import java.util.*;

/**
 * Created by Raphael on 10/11/2014.
 */
public class PieceSelector {

    private Random random;
    private double total;
    private NavigableMap<Double, String> piecesRandomicMap;

    public PieceSelector() {
        this.piecesRandomicMap = new TreeMap<Double, String>();
        this.total = 0;
        this.random = new Random();

        verifyUnlockedPieces(0);
    }

    public void verifyUnlockedPieces(int lvl) {
        for (Map.Entry<String, List<String>> entry : Main.app.getPieceLoader().getPieceMemoryMap().entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).equals("//INFO//")) {
                    verifyUnlockProp(entry.getValue(), i, entry.getKey(), lvl);
                    break;
                }
            }
        }
    }

    private void verifyUnlockProp(List<String> lines, int lineNum, String fileName, int lvl) {
        float lvlMin = -1;
        float chance = -1;
        if (lines.size() - 1 >= lineNum + 10) {
            lvlMin = Float.parseFloat(lines.get(lineNum + 10));
        }
        if (lines.size() - 1 >= lineNum + 12) {
            chance = Float.parseFloat(lines.get(lineNum + 12));
        }

        if (lvlMin == lvl) {
            if (lvlMin != -1) {
                addToPiecesRandomicMap(chance, fileName);
                //Unlocked Effect //Not Refactored Yet
            }
        }
    }

    public String randomizeFromRandomicMap() {
        double value = random.nextDouble() * total;
        return piecesRandomicMap.ceilingEntry(value).getValue();
    }

    private void addToPiecesRandomicMap(double weight, String itemName) {
        if (weight <= 0) return;
        total += weight;
        piecesRandomicMap.put(total, itemName);
    }

}

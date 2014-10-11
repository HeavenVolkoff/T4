package Refactoring.Model;

import Refactoring.Control.Constant;
import Refactoring.View.Piece;
import Refactoring.View.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 10/11/2014.
 */

public class AssetLoader {

    private NavigableMap<String, List<String>> pieceMemoryMap;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());

    public AssetLoader(String directory){
        this.pieceMemoryMap = new TreeMap<>();

        loadToMemoryMap(directory);
    }

    public void loadToMemoryMap(String directory){
        System.out.println("Loading From "+directory);
        final File folder = new File(directory);
        List<String> files = listFilesFromFolder(folder);

        for (int i = 0; i < files.size(); i++){
            this.pieceMemoryMap.put(files.get(i),loadFromFile(files.get(i), directory));
            System.out.println("Loading: " + files.get(i) +" "+ (i*100/files.size()) + "% done.");
        }
        System.out.println("Loading: 100% done.");
        System.out.println("-----------------------------------------------------");
    }

    private List<String> listFilesFromFolder(final File folder) {
        List<String> files = new ArrayList<String>();
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                files.add(fileEntry.getName());
            }
        }
        return files;
    }

    private List<String> loadFromFile(String fileName, String directory) {
        try {
            Path path = Paths.get(directory + fileName);
            return Files.readAllLines(path);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can not access file {0}, maybe you do not have permission. Please report this error.", fileName);
            return null;
        }
    }

    public NavigableMap<String, List<String>> getPieceMemoryMap() {
        return pieceMemoryMap;
    }
}

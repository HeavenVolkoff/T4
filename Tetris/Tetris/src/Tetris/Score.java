package Tetris;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by HeavenVolkoff on 12/09/14.
 */
public class Score extends Node {

	private int score;

    private int multiplier; //Streak
	private int level;
	private int jump;
	private List<List<String>> numbers;
	float posX;
	float posY;
    float width;
    float height;
    float cubeSize;
    int maxDigits;
    float speedMultiplyer;
    AssetManager assetManager;

	public Score(float cubeSize, int maxDigits ,float posX, float posY, float speedMultiplyer, AssetManager assetManager){
		this.score = 0;
		this.multiplier = 1;
		this.level = 1;
		this.numbers = new ArrayList<List<String>>();
		this.posX = posX;
		this.posY = posY;
        this.cubeSize = cubeSize;
        this.jump = 1000;
        this.maxDigits = maxDigits;
        this.speedMultiplyer = speedMultiplyer;
        this.assetManager = assetManager;

		for(int i = 0; i<=9; i++){
			numbers.add(loadFromFile( i+".piece" ));
		}

        this.width = ((maxDigits*(3*cubeSize*2.5f)) - cubeSize*0.5f) + ((maxDigits-1f)*cubeSize*0.5f);
        this.height = 5f*2.5f*cubeSize - 0.5f*cubeSize;

        showScore();

        setLocalTranslation(new Vector3f(posX-this.width*0.5f,posY-this.height*0.5f,0));
	}

    public void showScore(){ //
        int scoreTemp = score;
        float piecePosX = 0;
        while (true){
           Piece piece = new Piece(this.cubeSize, 0f, 0f, 0, numbers.get((int)scoreTemp%10), ColorRGBA.White, assetManager ,null);
           piece.move((float)(this.width*0.5)-(piecePosX),0f,0f);
           piecePosX = piecePosX+2.5f*cubeSize*3+1.5f*cubeSize;

           scoreTemp = scoreTemp/10;

           attachChild(piece);

           if (scoreTemp==0){
               break;
           }
        }
    }

	public List<String> loadFromFile(String fileName) {
		try {
			String appPath = new File(".").getCanonicalPath();
			Path path = Paths.get(appPath + "/resources/customPieces/numbers/" + fileName);
			return Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateScore(int destroyedCubes, int value){
		this.score += (value * destroyedCubes * this.multiplier);

		if(this.score >= jump){
			level++;
			jump *= 2.2f;
            Main.app.getLevelBar().showLevel();
            Main.app.getControl().setFullFallSpeed((int)(Main.app.getControl().getFullFallSpeed()*(1f-speedMultiplyer)));
            if (Main.app.getControl().isAcelerated()){
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed()/4);
            }else{
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed());
            }
		}

        detachAllChildren();
        showScore();

        Main.app.getLevelBar().setMax(jump);
        Main.app.getLevelBar().setValue(this.score);
	}

	public void resetScore(){
		this.score = 0;
        this.multiplier = 0;
	}

	public int getScore() {
		return score;
	}

    public int getMultiplier() {
        return multiplier;
    }

    public int getLevel() {
        return level;
    }
}

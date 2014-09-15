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
    private int oldScore;
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
        this.oldScore = 0;
		this.multiplier = 0;
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
        this.oldScore = this.score;
        System.out.println("Fall Interval: "+Main.app.getControl().getFullFallSpeed());
        System.out.println("Old Score: "+this.score);
        System.out.println("Max Score: "+this.jump);
        System.out.println("Destroyed Cubes: "+destroyedCubes);
        System.out.println("Multiplier: "+this.multiplier);
        if (this.multiplier != 0) {
            this.score += (value * destroyedCubes * this.multiplier);
        }else{
            this.score += (value * destroyedCubes);
        }
        System.out.println("New Score: "+this.score);
        System.out.println("Score Awarded: "+(this.score-this.oldScore));

        if(this.score >= jump){
            System.out.println("Old Level: "+this.level);
			level++;
			jump *= 2.2f;
            Main.app.getLevelBar().showLevel();
            Main.app.getControl().setFullFallSpeed((int)(Main.app.getControl().getFullFallSpeed()*(1f-speedMultiplyer)));
            if (Main.app.getControl().isAcelerated()){
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed()/4);
            }else{
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed());
            }
            System.out.println("New Max Score: "+this.jump);
		}
        System.out.println("Level: "+this.level);
        System.out.println("------------------------------------------------");

        detachAllChildren();
        showScore();

        Main.app.getLevelBar().setMax(jump);
        Main.app.getLevelBar().setValue(this.score);
	}

	public void resetScore(){
		this.score = 0;
        this.oldScore = 0;
        this.multiplier = 0;
	}

	public int getScore() {
		return score;
	}

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getLevel() {
        return level;
    }
}

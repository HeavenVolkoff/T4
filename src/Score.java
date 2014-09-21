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
    private int streakMultiplier; //Streak
	private int level;
	private int jump;
	private int jumpLast;
	private List<List<String>> numbers;
	float posX;
	float posY;
    float width;
    float height;
    float cubeSize;
    int maxDigits;
    float speedMultiplier;
    AssetManager assetManager;

	public Score(float cubeSize, int maxDigits ,float posX, float posY, float speedMultiplyer, AssetManager assetManager){
		this.score = 0;
        this.oldScore = 0;
		this.streakMultiplier = 1;
		this.level = 1;
		this.numbers = new ArrayList<List<String>>();
		this.posX = posX;
		this.posY = posY;
        this.cubeSize = cubeSize;
        this.jump = 1000;
		this.jumpLast = 0;
        this.maxDigits = maxDigits;
        this.speedMultiplier = speedMultiplyer;
        this.assetManager = assetManager;

		for(int i = 0; i<=9; i++){
			numbers.add(loadFromFile( i+".piece" ));
		}

        this.width = ((maxDigits*(3*cubeSize*2.5f)) - cubeSize*0.5f) + ((maxDigits-1f)*cubeSize*0.5f);
        this.height = 5f*2.5f*cubeSize - 0.5f*cubeSize;

        showScore(0);

        setLocalTranslation(new Vector3f(posX-this.width*0.5f,posY-this.height*0.5f,0));
	}

    public void showScore(int scoreValue){ //
        int score = scoreValue;
        float piecePosX = 0;

        detachAllChildren();

		do{
			Piece piece = new Piece(this.cubeSize, 0f, 0f, 0, numbers.get(score%10), -1, ColorRGBA.White, assetManager ,null);
			piece.move((float)(this.width*0.5)-(piecePosX),0f,0f);
			piecePosX = piecePosX+2.5f*cubeSize*3+1.5f*cubeSize;

			score = score/10;

			attachChild(piece);
		}while (score != 0);
    }

	public List<String> loadFromFile(String fileName) {
		try {
			Path path = Paths.get("./resources/customPieces/numbers/" + fileName);
			return Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateScore(int valMul, int value){
        this.oldScore = this.score;
        Main.app.getDebugMenu(0).setText("Fall Interval: "+Main.app.getControl().getFullFallSpeed());
		Main.app.getDebugMenu(1).setText("Old Score: "+this.score);
		Main.app.getDebugMenu(2).setText("Max Score: "+this.jump);
		Main.app.getDebugMenu(3).setText("Destroyed Cubes: "+valMul);
		Main.app.getDebugMenu(4).setText("Multiplier: "+this.streakMultiplier);

		this.score += (value * valMul * this.streakMultiplier);

		Main.app.getDebugMenu(5).setText("New Score: "+this.score);
		Main.app.getDebugMenu(6).setText("Score Awarded: "+(this.score-this.oldScore));

        if(this.score >= jump){
			Main.app.getDebugMenu(7).setText("Old Level: "+this.level);
			level++;
			jumpLast = jump;
			jump *= 2.1f;
			Main.app.getControl().setFullFallSpeed((int)(Main.app.getControl().getFullFallSpeed()*(1f-speedMultiplier)));
            if (Main.app.getControl().isAcelerated()){
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed()/4);
            }else{
                ((Piece)Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed());
            }
			Main.app.getDebugMenu(8).setText("New Max Score: "+this.jump);
		}
		Main.app.getDebugMenu(9).setText("Level: "+this.level);
	}

    public void setAlpha(float alphaVal){
        for (int i = 0; i < children.size(); i++){
            if (getChild(i) != null){
                ((Piece)getChild(i)).setAlpha(alphaVal);
            }
        }
    }

	public int getScore() {
		return score;
	}

    public int getStreakMultiplier() {
        return streakMultiplier;
    }

    public void setStreakMultiplier(int multiplier) {
        this.streakMultiplier = multiplier;
    }

    public int getLevel() {
        return level;
    }

	public int getJumpLast() {
		return jumpLast;
	}

    public int getJump() {
        return jump;
    }
}

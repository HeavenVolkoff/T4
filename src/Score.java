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

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Score extends Node {

	private int score;
    private int oldScore;
    private int streakMultiplier; //Streak
	private int level;
	private int jump;
	private int jumpLast;
	float posX;
	float posY;
    float cubeSize;
    int maxDigits;
    float speedMultiplier;
    AssetManager assetManager;

	public Score(float cubeSize, int maxDigits ,float posX, float posY, float speedMultiplyer, AssetManager assetManager){
		this.score = 0;
        this.oldScore = 0;
		this.streakMultiplier = 1;
		this.level = 1;
		this.posX = posX;
		this.posY = posY;
        this.cubeSize = cubeSize;
        this.jump = 1000;
		this.jumpLast = 0;
        this.maxDigits = maxDigits;
        this.speedMultiplier = speedMultiplyer;
        this.assetManager = assetManager;
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

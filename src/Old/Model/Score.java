package Old.Model;

import Old.View.DisplayNumbers;
import Old.View.PlayablePiece;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;

import Old.Primary.Main;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Score {

	private int score;
    private int streakMultiplier;
	private int level;
	private int jump;
	private int jumpLast;
    private float speedMultiplier;
	private DisplayNumbers displayScore;

	public Score(float speedMultiplyer){
		this.score = 0;
		this.streakMultiplier = 1;
		this.level = 1;
        this.jump = 1000;
		this.jumpLast = 0;
        this.speedMultiplier = speedMultiplyer;
	}

	public void createDisplayScore(float cubeSize, float posX, float posY, int maxDigits, int initialValue, ColorRGBA color, AssetManager assetManager){
		this.displayScore = new DisplayNumbers(cubeSize, posX, posY, maxDigits, initialValue, color, assetManager);
	}

	public DisplayNumbers getDisplayScore() {
		return displayScore;
	}

	public void updateScore(int valMul, int value){
		this.score += (value * valMul * this.streakMultiplier);

        if(this.score >= jump){
			level++;
            Main.app.getPieceSelector().verifyUnlockedPieces(level);
			jumpLast = jump;
			jump *= 2.1f;
			Main.app.getControl().setFullFallSpeed((int)(Main.app.getControl().getFullFallSpeed()*(1f-speedMultiplier)));
            if (Main.app.getControl().isAccelerated()){
                ((PlayablePiece) Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed()/4);
            }else{
                ((PlayablePiece) Main.app.getControl().getSpatial()).setPieceFallingTime(Main.app.getControl().getFullFallSpeed());
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

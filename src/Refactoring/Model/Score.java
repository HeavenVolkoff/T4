package Refactoring.Model;

import Old.View.DisplayNumbers;
import Refactoring.Control.Constant;
import Refactoring.Primary.Main;

/**
 * Created by Raphael on 10/11/2014.
 */
public class Score {

    private int score;
    private int streakMultiplier;
    private int level;
    private int nextLevelScore;
    private int priorLevelScore;
    private float speedMultiplier;

    public Score(float speedMultiplyer){
        this.score = 0;
        this.streakMultiplier = 1;
        this.level = 1;
        this.nextLevelScore = Constant.INITIALJUMP;
        this.priorLevelScore = 0;
        this.speedMultiplier = speedMultiplyer;
    }

    public void updateScore(int value){
        this.score += (value * this.streakMultiplier);

        if(this.score >= this.nextLevelScore){
            this.level++;
            Main.app.getPieceSelector().verifyUnlockedPieces(level);
            this.priorLevelScore = this.nextLevelScore;

            this.nextLevelScore *= 2.1f;//Update Score Formula

            Main.app.getControl().setFullFallSpeed((int)(Main.app.getControl().getFullFallSpeed()*(1f-speedMultiplier)));//Update Speed

            //Update Actual Piece Speed
            if (Main.app.getCurrentPiece() != null) {
                if (Main.app.getControl().isAccelerated()){
                    Main.app.getCurrentPiece().setPieceFallingTime(Main.app.getControl().getFullFallSpeed() / 4);
                }else{
                    Main.app.getCurrentPiece().setPieceFallingTime(Main.app.getControl().getFullFallSpeed());
                }
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

    public int getPriorLevelScore() {
        return priorLevelScore;
    }

    public int getNextLevelScore() {
        return nextLevelScore;
    }
}

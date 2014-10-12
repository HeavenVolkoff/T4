package Refactoring.Control;

import Refactoring.Primary.Main;

/**
 * Created by Raphael on 10/11/2014.
 */
public class PointsEffectController{

    float increment;
    int scoreToNextLvl;
    int priorLevelScore;


    public PointsEffectController(){
            this.scoreToNextLvl = Main.app.getScore().getNextLevelScore();
            this.priorLevelScore = Main.app.getScore().getPriorLevelScore();
    }

    private int updateScore(float tpf, float multiplyer){
        if (Main.app.getScoreDisplay() != null && Main.app.getScore() != null){
            if (Main.app.getScoreDisplay().getValue() < Main.app.getScore().getScore()){
                increment += (multiplyer*tpf)+(0.1f*(Main.app.getScore().getScore()-Main.app.getScoreDisplay().getValue()));
                if ((int)(Main.app.getScoreDisplay().getValue()+increment) <= Main.app.getScore().getScore()){
                    Main.app.getScoreDisplay().write((int)(Main.app.getScoreDisplay().getValue()+increment));
                    return (int)increment;
                }else{
                    Main.app.getScoreDisplay().write(Main.app.getScore().getScore());
                    return (int)increment;
                }
            }
        }
        return -1;
    }

    private void updateProgress(){
        if (Main.app.getLevelBar() != null) {
            if ((int)(Main.app.getLevelBar().getProgress()+increment) <= this.scoreToNextLvl-this.priorLevelScore) {
                Main.app.getLevelBar().setProgress((int)((Main.app.getLevelBar().getProgress() + increment)));
            }else if ((int)(Main.app.getLevelBar().getProgress()+increment) > this.scoreToNextLvl-this.priorLevelScore){
                int incrementLeftOvers = ((int)(Main.app.getLevelBar().getProgress()+increment))-(this.scoreToNextLvl-this.priorLevelScore);
                this.priorLevelScore = this.scoreToNextLvl;
                this.scoreToNextLvl *= 2.1f;
                Main.app.getLevelBar().setMax(this.scoreToNextLvl-this.priorLevelScore);
                Main.app.getLevelBar().setProgress(incrementLeftOvers);
                Main.app.getLvlIndicator().writeValue(Main.app.getLvlIndicator().getValue()+1);
            }
        }
    }

    public void controlUpdate(float tpf) {
        if (updateScore(tpf, 80) != -1){
            updateProgress();
            increment = 0;
        }
    }
}

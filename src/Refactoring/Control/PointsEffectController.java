package Refactoring.Control;

import Refactoring.Primary.Main;

/**
 * Created by Raphael on 10/11/2014.
 */
public class PointsEffectController{

    float increment;
    int scoreToNextLvl;
    int priorLevelScore;
    boolean changedNumParticles = false;

    public PointsEffectController(){
            this.scoreToNextLvl = Main.app.getScore().getNextLevelScore();
            this.priorLevelScore = Main.app.getScore().getPriorLevelScore();
    }

    private int updateScore(float tpf, float multiplyer){
        if (Main.app.getScoreDisplay() != null && Main.app.getScore() != null){
            if (Main.app.getScoreDisplay().getValue() < Main.app.getScore().getScore()){
                increment += (multiplyer*tpf)+(0.01f*(Main.app.getScore().getScore()-Main.app.getScoreDisplay().getValue()));
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
        if (Main.app.getScoreDisplay().getValue() == Main.app.getScore().getScore()) {
            Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(0);
            changedNumParticles = false;
        }else{
            if (!changedNumParticles) {
                float variation = (Main.app.getScore().getScore() - (Main.app.getLevelBar().getProgress() + this.priorLevelScore));
                if (variation < 10) {
                    Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(8);
                    Main.app.getLevelBar().getParticlesBar().setNumParticles(10);
                } else if (variation >= 10 && variation <= 100) {
                    Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(12);
                    Main.app.getLevelBar().getParticlesBar().setNumParticles(20);
                } else if (variation >= 100 && variation <= 500) {
                    Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(15);
                    Main.app.getLevelBar().getParticlesBar().setNumParticles(40);
                } else if (variation >= 500 && variation <= 1000) {
                    Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(20);
                    Main.app.getLevelBar().getParticlesBar().setNumParticles(50);
                } else if (variation >= 1000 && variation <= 2000) {
                    Main.app.getLevelBar().getParticlesBar().setParticlesPerSec(26);
                    Main.app.getLevelBar().getParticlesBar().setNumParticles(52);
                }
                Main.app.getLevelBar().getParticlesBar().emitAllParticles();
                Main.app.getLevelBar().attachChild(Main.app.getLevelBar().getParticlesBar());
                changedNumParticles = true;
            }
        }
        if (updateScore(tpf, 100) != -1){
            updateProgress();
            increment = 0;
        }
    }
}

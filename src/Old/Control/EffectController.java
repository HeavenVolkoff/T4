package Old.Control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import Old.View.LevelBar;
import com.jme3.scene.control.AbstractControl;
import Old.Primary.Main;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class EffectController extends AbstractControl{

    private int lvlBarActualJumpScore; //Asynchronous jump score for level bar counting
    private int lvlBarOldJumpScore; //Asynchronous old jump score for level bar counting
    private int scoreValue; //Asynchronous score for level bar counting

	/*
	 *  _______________________
	 * |___________|___________|
	 * ^-lvlBarOldJumpScore
	 * 			   ^-scoreValue
	 * 			   			   ^-lvlBarActualJumpScore
	 */

    public EffectController(){
        this.scoreValue = 0;
        this.lvlBarActualJumpScore = Main.app.getScore().getJump();
        this.lvlBarOldJumpScore = Main.app.getScore().getJumpLast();
    }

	void resizeBar(float tpf) {
		if (((LevelBar) spatial).getScore() < (Main.app.getScore().getScore() - this.lvlBarOldJumpScore)) {

			if ((int) (((LevelBar) spatial).getScore() * 0.1f * tpf) > 1 && ((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf) <= Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()) {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf));
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf));
			} else {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + 1);
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + 1);
			}
            ((LevelBar) spatial).getParticlesLvlBar().setParticlesPerSec(8);
            ((LevelBar) spatial).getParticlesLvlBar().emitAllParticles();
            ((LevelBar) spatial).attachChild(((LevelBar) spatial).getParticlesLvlBar());
		}else{
            ((LevelBar) spatial).getParticlesLvlBar().setParticlesPerSec(0);
        }
        if (((LevelBar) spatial).getScore() + this.lvlBarOldJumpScore >= this.lvlBarActualJumpScore){
            Main.app.getLevelBar().setMax(Main.app.getScore().getJump() - Main.app.getScore().getJumpLast());
            ((LevelBar) spatial).resetPercentageGeo();
            Main.app.getLevelBar().getDisplayLvl().write(++Main.app.getLevelBar().level);
            ((LevelBar) spatial).setScore(0);
            this.lvlBarActualJumpScore = Main.app.getScore().getJump();
            this.lvlBarOldJumpScore = Main.app.getScore().getJumpLast();
        }
        updateScore(tpf);
	}

    void updateScore(float tpf){
        if (scoreValue < Main.app.getLevelBar().getScore()+lvlBarOldJumpScore) {
            if ((int)(scoreValue * 0.1f * tpf) > 1 && (scoreValue + (int)(scoreValue * 0.1f * tpf)) < Main.app.getLevelBar().getScore()) {
                scoreValue += (int)(scoreValue * 0.1f * tpf);
            } else {
                scoreValue += 1;
            }
            Main.app.getScore().getDisplayScore().write(scoreValue);
        }
        if (Main.app.getPieceSelector() != null){
            if (Main.app.getPieceSelector().isUnlock()){
                if (Main.app.getPieceSelector().getUnlockPiece().getAlpha()+0.01f <= 1) {
                    Main.app.getPieceSelector().getUnlockPiece().setAlpha(Main.app.getPieceSelector().getUnlockPiece().getAlpha() + 0.01f);
                }else{
                    Main.app.getPieceSelector().getUnlockPiece().setAlpha(1);
                }
            }else{
                if (Main.app.getPieceSelector().getUnlockPiece().getAlpha()-0.008f > 0) {
                    Main.app.getPieceSelector().getUnlockPiece().setAlpha(Main.app.getPieceSelector().getUnlockPiece().getAlpha() - 0.008f);
                }else{
                    if (Main.app.getPieceSelector().getUnlockPiece().getAlpha()-0.008f != 0) {
                        Main.app.getPieceSelector().getUnlockPiece().setAlpha(0);
                    }
                    if (Main.app.getPieceSelector().getQuantity() > 0){
                        Main.app.getPieceSelector().detachChild(Main.app.getPieceSelector().getUnlockPiece());
                    }
                }
            }
        }
    }

    @Override
	public void controlUpdate(float tpf){
        if (spatial.getClass().getName().equals("Old.View.LevelBar")){
            resizeBar(tpf);
        }
	}

	@Override
	public void controlRender(RenderManager rm ,ViewPort vp){
		/* Optional: rendering manipulation (for advanced users) */
	}
}

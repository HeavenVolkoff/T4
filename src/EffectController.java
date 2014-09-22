import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class EffectController extends AbstractControl{

	private int lvlBarCallTimes;
    private int lvlBarActualJumpScore;
    private int lvlBarOldJumpScore;
    private int scoreValue;

    public EffectController(){
        this.lvlBarCallTimes = 0;
        this.scoreValue = 0;
        this.lvlBarActualJumpScore = Main.app.getScore().getJump();
        this.lvlBarOldJumpScore = Main.app.getScore().getJumpLast();
    }

	public void resizeBar(float tpf) {
		if (((LevelBar) spatial).getScore() < (Main.app.getScore().getScore() - this.lvlBarOldJumpScore)) {
            lvlBarCallTimes += 1;

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
			Main.app.getDebugMenu(11).setText("Bar Score: " + ((LevelBar) spatial).getScore());
			Main.app.getDebugMenu(12).setText("Score Score: " + (Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()));
			Main.app.getDebugMenu(13).setText("Call Times: " + lvlBarCallTimes);
		}else{
            ((LevelBar) spatial).getParticlesLvlBar().setParticlesPerSec(0);
        }
        if (((LevelBar) spatial).getScore() + this.lvlBarOldJumpScore >= this.lvlBarActualJumpScore){
            Main.app.getLevelBar().setMax(Main.app.getScore().getJump() - Main.app.getScore().getJumpLast());
            ((LevelBar) spatial).resetPercentageGeo();
            Main.app.getDisplayLvl().write(Main.app.getScore().getLevel());
            ((LevelBar) spatial).setScore(0);
            this.lvlBarActualJumpScore = Main.app.getScore().getJump();
            this.lvlBarOldJumpScore = Main.app.getScore().getJumpLast();
        }
        ((EffectController)Main.app.getScore().getControl(0)).updateScore(tpf);
	}

    public void updateScore(float tpf){
        if (scoreValue < Main.app.getLevelBar().getScore()+((EffectController)Main.app.getLevelBar().getControl(0)).getLvlBarOldJumpScore()) {
            if ((int)(scoreValue * 0.1f * tpf) > 1 && (scoreValue + (int)(scoreValue * 0.1f * tpf)) < ((Score) spatial).getScore()) {
                scoreValue += (int)(scoreValue * 0.1f * tpf);
            } else {
                scoreValue += 1;
            }
            Main.app.getDisplayScore().write(scoreValue);
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

    public int getLvlBarOldJumpScore() {
        return lvlBarOldJumpScore;
    }

    @Override
	public void controlUpdate(float tpf){
        if (spatial.getClass().getName().equals("LevelBar")){
            resizeBar(tpf);
        }
	}

	@Override
	public void controlRender(RenderManager rm ,ViewPort vp){
		/* Optional: rendering manipulation (for advanced users) */
	}

	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
	}
}

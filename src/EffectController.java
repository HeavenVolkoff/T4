import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Created by HeavenVolkoff on 17/09/14.
 */
public class EffectController extends AbstractControl{

	private int callTimes;
    private int actualJumpScore;
    private int oldJumpScore;

    public EffectController(){
        this.callTimes = 0;
        this.actualJumpScore = Main.app.getScore().getJump();
        this.oldJumpScore = Main.app.getScore().getJumpLast();
    }

	public void resizeBar(float tpf) {
		if (((LevelBar) spatial).getScore() < (Main.app.getScore().getScore() - this.oldJumpScore)) {
			callTimes += 1;
			if ((int) (((LevelBar) spatial).getScore() * 0.1f * tpf) > 1 && ((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf) <= Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()) {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf));
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f * tpf));
			} else {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + 1);
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + 1);
			}
			Main.app.getDebugMenu(11).setText("Bar Score: " + ((LevelBar) spatial).getScore());
			Main.app.getDebugMenu(12).setText("Score Score: " + (Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()));
			Main.app.getDebugMenu(13).setText("Call Times: " + callTimes);
		}
        if (((LevelBar) spatial).getScore() + this.oldJumpScore == this.actualJumpScore){
            Main.app.getLevelBar().setMax(Main.app.getScore().getJump() - Main.app.getScore().getJumpLast());
            ((LevelBar) spatial).resetPercentageGeo();
            Main.app.getLevelBar().showLevel();
            ((LevelBar) spatial).setScore(0);
            this.actualJumpScore = Main.app.getScore().getJump();
            this.oldJumpScore = Main.app.getScore().getJumpLast();
        }
	}

	@Override
	public void controlUpdate(float tpf){
		resizeBar(tpf*10);
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

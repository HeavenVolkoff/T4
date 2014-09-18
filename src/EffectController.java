import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Created by HeavenVolkoff on 17/09/14.
 */
public class EffectController extends AbstractControl{

	private int callTimes = 0;
	private int Barjump;

	public void resizeBar() {
		if (((LevelBar) spatial).getScore() < (Main.app.getScore().getScore() - Main.app.getScore().getJumpLast())) {
			callTimes += 1;
			if ((int) (((LevelBar) spatial).getScore() * 0.1f) > 1 && ((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f) <= Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()) {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f));
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + (int) (((LevelBar) spatial).getScore() * 0.1f));
			} else {
				((LevelBar) spatial).setValue(((LevelBar) spatial).getScore() + 1);
				((LevelBar) spatial).setScore(((LevelBar) spatial).getScore() + 1);
			}
			Main.app.getDebugMenu(11).setText("Bar Score: " + ((LevelBar) spatial).getScore());
			Main.app.getDebugMenu(12).setText("Score Score: " + (Main.app.getScore().getScore() - Main.app.getScore().getJumpLast()));
			Main.app.getDebugMenu(13).setText("Call Times: " + callTimes);
		}
	}

	@Override
	public void controlUpdate(float tpf){
		resizeBar();
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

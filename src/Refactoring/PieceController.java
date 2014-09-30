package Refactoring;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PieceController extends AbstractControl implements Control {
	public PieceController(){}

	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
	}

	@Override
	protected void controlUpdate(float tpf){
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
	}

	public boolean isSetSpatial(){
		return spatial != null;
	}
}

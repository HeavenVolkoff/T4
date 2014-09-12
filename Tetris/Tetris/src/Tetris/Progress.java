package Tetris;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/*
 * Created by HeavenVolkoff on 12/09/14.
 */

public class Progress extends AbstractControl implements Cloneable{

	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
		if(spatial != null){
			spatial
		}else{

		}
	}

	@Override
	protected void controlUpdate(float tpf){

	}

	@Override
	public Control cloneForSpatial(Spatial spatial){
		final PieceController control = new PieceController();
    /* Optional: use setters to copy userdata into the cloned control */
		// control.setIndex(i); // example
		control.setSpatial(spatial);
		return control;
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
	}

}

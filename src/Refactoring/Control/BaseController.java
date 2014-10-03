//REFACTORED STATUS: OK.

package Refactoring.Control;

import Refactoring.Model.ConfigManager;
import Old.Model.Keys;
import Refactoring.Control.Model.Control;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public abstract class BaseController extends AbstractControl implements Control {

	private List<Keys> hotKeys = new ArrayList<Keys>();
	private ActionListener actionKeyPress;
	private AnalogListener analogKeyPress;

    public BaseController() {
        this.actionKeyPress = new ActionListener() {
            public void onAction(String name, boolean pressed, float tpf) {
                keyActions(name, pressed);
                getKeyByActionName(name).setStartTime(System.nanoTime());
            }
        };
        this.analogKeyPress = new AnalogListener() {
            public void onAnalog(String name, float pressed, float tpf) {
                Keys keyPressed = getKeyByActionName(name);
                if ((int)((System.nanoTime()-keyPressed.getStartTime())*Constant.NANOTIMETOMILLISECONDS) >= keyPressed.getRepeatTime()) { //Elapsed time >= Repeat Time
                    keyActions(name, true);
                    getKeyByActionName(name).setStartTime(System.nanoTime());
                }
            }
        };
    }

    private Keys getKeyByActionName(String actionName){
        for (Keys key : hotKeys) {
            if(key.getActionName().equals(actionName)) {
                return key;
            }
        }
        return null;
    }

    protected boolean loadHotKeys(String fileName) {
        ConfigManager pieceControlerManager = new ConfigManager(fileName);
        if (pieceControlerManager.load(fileName)) {
            for (String itemKey : pieceControlerManager.getItems()) {
                String[] infos = pieceControlerManager.getValue(itemKey).split(";");
                if (infos[1].equals("-1")) {
                    hotKeys.add(new Keys(itemKey, Integer.parseInt(infos[0])));
                } else {
                    hotKeys.add(new Keys(itemKey, Integer.parseInt(infos[0]), Integer.parseInt(infos[1])));
                }
            }
            if (hotKeys.size() != 0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    protected boolean setupDefaultHotKeys(String fileName) {
        ConfigManager pieceControlerManager = new ConfigManager(fileName);
        if (pieceControlerManager.load(fileName)) {
            for (Keys key : hotKeys) {
                if (key.getRepeatTime() != 0) {
                    pieceControlerManager.defineKey(key.getActionName(), key.getId() + ";" + key.getRepeatTime());
                } else {
                    pieceControlerManager.defineKey(key.getActionName(), key.getId() + ";-1");
                }
            }
            if (pieceControlerManager.save()) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    protected abstract void keyActions(String name, boolean pressed);

    @Override
    protected abstract void controlUpdate(float tpf);

    @Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){
     /* Optional: rendering manipulation (for advanced users) */
	}

	@Override
	public boolean isSetSpatial(){
		return spatial != null;
	}
}

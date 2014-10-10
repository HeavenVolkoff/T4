//REFACTORED STATUS: OK.

package Refactoring.Control;

import Refactoring.Primary.Main;
import Refactoring.Model.ConfigManager;
import Refactoring.Control.Model.Control;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
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

	protected List<Keys> hotKeys = new ArrayList<Keys>();
	private ActionListener actionKeyPress;
	private AnalogListener analogKeyPress;

    //======================== Class Constructors ==========================//
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
                if ((int)((System.nanoTime()-keyPressed.getStartTime())/Constant.NANOTIMETOMILLISECONDS) >= keyPressed.getRepeatTime()) { //Elapsed time >= Repeat Time
                    keyActions(name, true);
                    getKeyByActionName(name).setStartTime(System.nanoTime());
                }
            }
        };
    }
    //======================================================================//

    private Keys getKeyByActionName(String actionName){
        for (Keys key : hotKeys) {
            if(key.getActionName().equals(actionName)) {
                return key;
            }
        }
        return null;
    }

    private void createMapingsAndListeners(){
        for (Keys key : hotKeys) {
            Main.app.getInputManager().addMapping(key.getActionName(), new KeyTrigger(key.getId()));
            if (key.getOnAction()) {
                Main.app.getInputManager().addListener(this.actionKeyPress, key.getActionName());
            } else {
                Main.app.getInputManager().addListener(this.actionKeyPress, key.getActionName());
                Main.app.getInputManager().addListener(this.analogKeyPress, key.getActionName());
            }
        }
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
                createMapingsAndListeners();
                return true;
            }
        }

        hotKeys.clear();
        setupDefaultHotKeys();

        createMapingsAndListeners();

        saveHotKeys(fileName);
        return false;
    }

    protected boolean saveHotKeys(String fileName) {
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
            }
        }
        return false;
    }

    protected abstract void setupDefaultHotKeys();

    public boolean setHotKey(String actionName, int keyCode, String fileName) {
        ConfigManager pieceControlerManager = new ConfigManager(fileName);
        for (Keys key : hotKeys) {
            if (key.getActionName().equals(actionName)) {
                Main.app.getInputManager().deleteMapping(actionName);
                key.setId(keyCode);
                Main.app.getInputManager().addMapping(actionName, new KeyTrigger(keyCode));
                if (key.getOnAction()) {
                    Main.app.getInputManager().addListener(this.actionKeyPress, actionName);
                    if (!pieceControlerManager.defineKey(actionName, keyCode + ";-1")) {
                        return false;
                    }
                } else {
                    Main.app.getInputManager().addListener(this.actionKeyPress, actionName);
                    Main.app.getInputManager().addListener(this.analogKeyPress, actionName);
                    if (!pieceControlerManager.defineKey(actionName, keyCode + ";" + key.getRepeatTime())){
                        return false;
                    }
                }
                if (pieceControlerManager.save()){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public List<Keys> getHotKeys() {
        return this.hotKeys;
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

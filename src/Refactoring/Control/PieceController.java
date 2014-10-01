package Refactoring.Control;

import Model.*;
import Model.Keys;
import Refactoring.Model.INIProperties;
import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PieceController extends AbstractControl implements Control, INIProperties {

	private List<Model.Keys> hotKeys = new ArrayList<Keys>();
	private ActionListener actionKeyPress;
	private AnalogListener analogKeyPress;
	private boolean accelerated;
	private int fullFallSpeed;
	protected static final Logger logger = Logger.getLogger(PieceController.class.getName());

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

	@Override
	public boolean isSetSpatial(){
		return spatial != null;
	}

	@Override
	public void saveINIFile(String file) {
		Properties iniFile = new Properties();
		try {
			iniFile.load(new FileInputStream(file));
			for (Keys key : hotKeys) {
				iniFile.setProperty(key.getActionName(), String.valueOf(key.getId()));
			}
			iniFile.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Configuration error. Unable to save file {0}", file);
		}
	}

	@Override
	public void createINIFile(String file) {
		Properties iniFile = new Properties();
		try {
			iniFile.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Configuration error. Unable to save file {0}", file);
		}
	}

	@Override
	public void loadINIFile(String file) {
		Properties iniFile = new Properties();
		try {
			iniFile.load(new FileInputStream(file));
			hotKeys.add(new Keys("ChangePiece", Integer.parseInt(iniFile.getProperty("ChangePiece"))));
			hotKeys.add(new Keys("AccelerateFall", Integer.parseInt(iniFile.getProperty("AccelerateFall"))));
			//==================Set Default Analog Model.Keys=====================//
			hotKeys.add(new Keys("RotateClockwise", Integer.parseInt(iniFile.getProperty("RotateClockwise")), Constant.ROTATIONINTERVAL));
			hotKeys.add(new Keys("RotateCounterClockwise", Integer.parseInt(iniFile.getProperty("RotateCounterClockwise")), Constant.ROTATIONINTERVAL));
			hotKeys.add(new Keys("MoveRight", Integer.parseInt(iniFile.getProperty("MoveRight")), Constant.MOVEINTERVAL));
			hotKeys.add(new Keys("MoveLeft", Integer.parseInt(iniFile.getProperty("MoveLeft")), Constant.MOVEINTERVAL));
		} catch (IOException e) {
			logger.log(Level.WARNING, "Configuration error. Unable to load from file {0}", file);
			setupINIProperties();
		}
	}

	@Override
	public void setupINIProperties() {
		hotKeys.add(new Keys("ChangePiece", KeyInput.KEY_RETURN));
		hotKeys.add(new Keys("AccelerateFall", KeyInput.KEY_DOWN));
		//==================Set Default Analog Model.Keys=====================//
		hotKeys.add(new Keys("RotateClockwise", KeyInput.KEY_UP, Constant.ROTATIONINTERVAL));
		hotKeys.add(new Keys("RotateCounterClockwise", KeyInput.KEY_SPACE, Constant.ROTATIONINTERVAL));
		hotKeys.add(new Keys("MoveRight", KeyInput.KEY_RIGHT, Constant.MOVEINTERVAL));
		hotKeys.add(new Keys("MoveLeft", KeyInput.KEY_LEFT, Constant.MOVEINTERVAL));
	}
}

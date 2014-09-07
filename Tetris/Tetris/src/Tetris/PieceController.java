package Tetris;

import com.jme3.export.*;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class PieceController extends AbstractControl implements Savable, Cloneable {

	private List<Keys> hotKeys = new ArrayList<Keys>();
	private ActionListener actionKeyPress;
	private AnalogListener analogKeyPress;   

	//=========================== Constructors =====================//
	public PieceController(){} //empty serialization constructor

	public PieceController(InputManager inputManager, int timeKeyRepeat) {
        this.actionKeyPress = new ActionListener() {
            public void onAction(String name, boolean pressed, float tpf) {
                keyActions(name, pressed);
            }
        };
        this.analogKeyPress = new AnalogListener() {
            public void onAnalog(String name, float pressed, float tpf) {
                Keys keyPressed = getKeyByActionName(name);
                int elapsedTime = (int) ((System.nanoTime() - keyPressed.getStartTime()) / 1000000);
                if (elapsedTime >= keyPressed.getRepeatTime()) {
                    keyActions(name, true);
                }
            }
        };

        try {
            String appPath = new File(".").getCanonicalPath();
            File file = new File(appPath + "/PieceControls.ini");
            if (file.exists() && !file.isDirectory()) {
                loadHotKeys(appPath + "/PieceControls.ini");
            }else{
                hotKeys.add(new Keys("ChangePiece", KeyInput.KEY_RETURN));
                hotKeys.add(new Keys("AccelerateFall", KeyInput.KEY_SPACE));
                //==================Set Default Analog Keys=====================//
                hotKeys.add(new Keys("RotateClockwise", KeyInput.KEY_DOWN, 400));
                hotKeys.add(new Keys("RotateCounterClockwise", KeyInput.KEY_UP, 400));
                hotKeys.add(new Keys("MoveRight", KeyInput.KEY_RIGHT, 150));
                hotKeys.add(new Keys("MoveLeft", KeyInput.KEY_LEFT, 150));
                createHotKeysFile(appPath + "/PieceControls.ini");
                saveHotKeys(appPath + "/PieceControls.ini");
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to handle control file!", ex);
        }

        for (Keys key : hotKeys) {
            inputManager.addMapping(key.getActionName(), new KeyTrigger(key.getId()));
            if (key.getOnAction()) {
                inputManager.addListener(this.actionKeyPress, key.getActionName());
            } else {
                inputManager.addListener(this.actionKeyPress, key.getActionName());
                inputManager.addListener(this.analogKeyPress, key.getActionName());
            }
        }
    }
	//==============================================================//

    //===================Handle HotKeys Save File===================//
    public void saveHotKeys(String file){
        Properties INIFile = new Properties();
        try {
            INIFile.load(new FileInputStream(file));
            for (Keys key : hotKeys) {
                INIFile.setProperty(key.getActionName(), String.valueOf(key.getId()));
            }
            INIFile.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }

    public void createHotKeysFile(String file){
        Properties INIFile = new Properties();
        try {
            INIFile.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }

    public void loadHotKeys(String file){
        Properties INIFile = new Properties();
        try {
            INIFile.load(new FileInputStream(file));
            hotKeys.add(new Keys("ChangePiece", Integer.parseInt(INIFile.getProperty("ChangePiece"))));
            hotKeys.add(new Keys("AccelerateFall", Integer.parseInt(INIFile.getProperty("AccelerateFall"))));
            //==================Set Default Analog Keys=====================//
            hotKeys.add(new Keys("RotateClockwise", Integer.parseInt(INIFile.getProperty("RotateClockwise")), 400));
            hotKeys.add(new Keys("RotateCounterClockwise", Integer.parseInt(INIFile.getProperty("RotateCounterClockwise")), 400));
            hotKeys.add(new Keys("MoveRight", Integer.parseInt(INIFile.getProperty("MoveRight")), 150));
            hotKeys.add(new Keys("MoveLeft", Integer.parseInt(INIFile.getProperty("MoveLeft")), 150));
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }
    //==============================================================//

    private Keys getKeyByActionName(String actionName){
        for (Keys key : hotKeys) {
            if(key.getActionName().equals(actionName)) {
                return key;
            }
        }
        return null;
    }

	private void keyActions(String name, boolean pressed){
		if (name.equals("ChangePiece") && pressed){
            int actualPieceFalingTime = ((Piece)spatial).getPieceFallingTime();
            setSpatial(null);
            Main.app.setCurrentPiece(new Piece(0.1f, 0f, 3.4f, Main.app.mat, this));
            ((Piece)spatial).setPieceFallingTime(actualPieceFalingTime);
		}else if(name.equals("AccelerateFall")) {
            if  (pressed){
                ((Piece)spatial).setPieceFallingTime(((Piece)spatial).getPieceFallingTime()/4);
            }else{
                ((Piece)spatial).setPieceFallingTime(((Piece)spatial).getPieceFallingTime()*4);
            }
		}else if(name.equals("RotateClockwise") && pressed){
			rotate(0, 0, 90);
		}else if(name.equals("RotateCounterClockwise") && pressed){
			rotate(0, 0, -90);
		}else if(name.equals("MoveRight") && pressed){
			moveX(((Piece)spatial).RIGHT,(2.5f*((Piece)spatial).getCubeSize()));
		}else if(name.equals("MoveLeft") && pressed){
			moveX(((Piece)spatial).LEFT,(2.5f*((Piece)spatial).getCubeSize()));
		}

        getKeyByActionName(name).setStartTime(System.nanoTime());
	} //Specific Key Algorithm Execution

	public List<Keys> getHotKeys() {
		return this.hotKeys;
	}

	public void setHotKey(InputManager inputManager, String actionName, int keyCode){
		for(Keys key : hotKeys){
			if(key.getActionName().equals(actionName)){
				inputManager.deleteMapping(actionName);
				key.setId(keyCode);
				inputManager.addMapping(actionName, new KeyTrigger(keyCode));
				if(key.getOnAction()) {
					inputManager.addListener(this.actionKeyPress, actionName);
				}else{
                    inputManager.addListener(this.actionKeyPress, key.getActionName());
					inputManager.addListener(this.analogKeyPress, actionName);
				}
			}
		}
	}


    //==========================Movement============================//
    public void rotate(float degreesX, float degreesY, float degreesZ){
        spatial.rotate((float) Math.toRadians(degreesX), (float) Math.toRadians(degreesY), (float) Math.toRadians(degreesZ));
    }

    public void moveX(int orientation, float distance){
        spatial.setLocalTranslation(new Vector3f(((Piece)spatial).getPosX() + (distance * orientation), ((Piece)spatial).getPosY(), 0));
        ((Piece)spatial).setPosX(((Piece)spatial).getPosX()+(distance*orientation));
    }

    public void moveY(int orientation, float distance){
        spatial.setLocalTranslation(new Vector3f(((Piece)spatial).getPosX() , ((Piece)spatial).getPosY()+(distance*orientation), 0));
        ((Piece)spatial).setPosY(((Piece)spatial).getPosY()+(distance*orientation));
    }

    public void fall(float heightRelativeToCubeSize) {
        int keyElapsedTime = (int) ((System.nanoTime() - ((Piece)spatial).getStartFallTime()) / 1000000);
        if (keyElapsedTime >= ((Piece)spatial).getPieceFallingTime()) {
            if (((Piece)spatial).isFalling()) {
                moveY(((Piece)spatial).DOWN, ((Piece)spatial).getCubeSize() * heightRelativeToCubeSize);
            }
            ((Piece)spatial).setStartFallTime(System.nanoTime());
        }
    }
    //==============================================================//

    /** This method is called when the control is added to the spatial,
	 * and when the control is removed from the spatial (setting a null value).
	 * It can be used for both initialization and cleanup. */
	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
    /* Example:
    if (spatial != null){
        // initialize
    }else{
        // cleanup
    }
    */
	}

	/** Implement your spatial's behaviour here.
	 * From here you can modify the scene graph and the spatial
	 * (transform them, get and set userdata, etc).
	 * This loop controls the spatial while the Control is enabled. */
	@Override
	protected void controlUpdate(float tpf){
		if(spatial != null) {
            fall(2.5f);
		}
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

	@Override
 	public void read(JmeImporter im) throws IOException {
		super.read(im);

	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		super.write(ex);
	}

}
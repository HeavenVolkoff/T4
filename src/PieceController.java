import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class PieceController extends AbstractControl implements Cloneable {

	private List<Keys> hotKeys = new ArrayList<Keys>();
	private ActionListener actionKeyPress;
	private AnalogListener analogKeyPress;
    private AssetManager assetManager;
    private boolean accelerated;
    private int fullFallSpeed;

	//=========================== Constructors =====================//
    public PieceController() {}

    public PieceController(int fullFallSpeed, InputManager inputManager, AssetManager assetManager, int timeKeyRepeat) {
        this.fullFallSpeed = fullFallSpeed;
        this.accelerated = false;
        this.assetManager = assetManager;
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
                setupDefaultKeys();
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

    private void setupDefaultKeys(){
        hotKeys.add(new Keys("ChangePiece", KeyInput.KEY_RETURN));
        hotKeys.add(new Keys("AccelerateFall", KeyInput.KEY_SPACE));
        //==================Set Default Analog Keys=====================//
        hotKeys.add(new Keys("RotateClockwise", KeyInput.KEY_DOWN, 400));
        hotKeys.add(new Keys("RotateCounterClockwise", KeyInput.KEY_UP, 400));
        hotKeys.add(new Keys("MoveRight", KeyInput.KEY_RIGHT, 150));
        hotKeys.add(new Keys("MoveLeft", KeyInput.KEY_LEFT, 150));
    }

    //===================Handle HotKeys Save File===================//
    private void saveHotKeys(String file){
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

    private void createHotKeysFile(String file){
        Properties INIFile;
		INIFile = new Properties();
		try {
            INIFile.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }
    }

    private void loadHotKeys(String file){
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
            setSpatial(null);
            Main.app.setCurrentPiece(new Piece(0.15f, 0f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), 0, Main.app.getNextPiece().getFileName(), ColorRGBA.randomColor(),  assetManager, this));
            if (this.accelerated) {
                ((Piece) spatial).setPieceFallingTime(fullFallSpeed/4);
            }else{
                ((Piece) spatial).setPieceFallingTime(fullFallSpeed);
            }
            Main.app.setNextPiece(new Piece(0.15f, 2f, 2.5f, 0, Main.app.getPieceSelector().randomizeFromMap(),ColorRGBA.randomColor(), assetManager ,null));
		}else if(name.equals("AccelerateFall")) {
            if  (pressed){
                ((Piece)spatial).setPieceFallingTime(fullFallSpeed/4);
                this.accelerated = true;
            }else{
                ((Piece)spatial).setPieceFallingTime(fullFallSpeed);
                this.accelerated = false;
            }
		}else if(name.equals("RotateClockwise") && pressed){
			if(((Piece)spatial).getInvert() == 0){
                ((Piece)spatial).setRotating(true);
				if (Main.app.getBoard().canRotate((Piece) spatial, -90)){
					rotate(0, 0, -90);
				}
			}else{
                ((Piece)spatial).setRotating(true);
				if (Main.app.getBoard().canRotate((Piece) spatial, 90)){
					rotate(0, 0, 90);
				}
			}
        }else if(name.equals("RotateCounterClockwise") && pressed){
            if(((Piece)spatial).getInvert() == 0){
                ((Piece)spatial).setRotating(true);
                if (Main.app.getBoard().canRotate((Piece) spatial, 90)){
                    rotate(0, 0, 90);
                }
            }else{
                ((Piece)spatial).setRotating(true);
                if (Main.app.getBoard().canRotate((Piece) spatial, -90)){
                    rotate(0, 0, -90);
                }
            }
		}else if(name.equals("MoveRight") && pressed){
            if (!Main.app.getBoard().hitRightFrame(((Piece) spatial).getBoxAbsolutePoint(),((Piece) spatial).getNumBox()) &&
                !Main.app.getBoard().hitRightPiece(((Piece) spatial).getBoxAbsolutePoint(),((Piece) spatial).getNumBox()) &&
				!((Piece) spatial).isRotating()){
                    moveX(((Piece) spatial).RIGHT, (2.5f * ((Piece) spatial).getCubeSize()));
            }
		}else if(name.equals("MoveLeft") && pressed){
            if (!Main.app.getBoard().hitLeftFrame(((Piece) spatial).getBoxAbsolutePoint(),((Piece) spatial).getNumBox()) &&
            	!Main.app.getBoard().hitLeftPiece(((Piece) spatial).getBoxAbsolutePoint(),((Piece) spatial).getNumBox()) &&
				!((Piece) spatial).isRotating()){
                moveX(((Piece) spatial).LEFT, (2.5f * ((Piece) spatial).getCubeSize()));
            }
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
    private void rotate(float degreesX, float degreesY, float degreesZ){
        spatial.rotate((float) Math.toRadians(degreesX), (float) Math.toRadians(degreesY), (float) Math.toRadians(degreesZ));
    }

    private void moveX(int orientation, float distance){
        spatial.setLocalTranslation(new Vector3f(((Piece) spatial).getPosX() + (distance * orientation), ((Piece) spatial).getPosY(), 0));
        ((Piece) spatial).setPosX(((Piece) spatial).getPosX() + (distance * orientation));
    }

    private void moveY(int orientation, float distance){
        spatial.setLocalTranslation(new Vector3f(((Piece)spatial).getPosX() , ((Piece)spatial).getPosY()+(distance*orientation), 0));
        ((Piece)spatial).setPosY(((Piece) spatial).getPosY() + (distance * orientation));
    }

    private void fall(float heightRelativeToCubeSize) {
        int keyElapsedTime = (int) ((System.nanoTime() - ((Piece)spatial).getStartFallTime()) / 1000000);
        if (keyElapsedTime >= ((Piece)spatial).getPieceFallingTime()) {
            if (!Main.app.getBoard().gameOver(((Piece) spatial).getBoxAbsolutePoint(),((Piece) spatial).getNumBox())){
                if (((Piece)spatial).isFalling()) {
                    //Not hit Horizontal frame
                    if (!Main.app.getBoard().hitBottomFrame(((Piece) spatial).getBoxAbsolutePoint(), ((Piece) spatial).getNumBox()) &&
                            !Main.app.getBoard().hitBottomPiece(((Piece) spatial).getBoxAbsolutePoint(), ((Piece) spatial).getNumBox())) {
                        moveY(((Piece) spatial).DOWN, ((Piece) spatial).getCubeSize() * heightRelativeToCubeSize);
                    } else {
                        if (Main.app.getBoard().addPiece(((Piece) spatial).getBoxAbsolutePoint(), ((Piece) spatial).getNumBox(), ((Piece) spatial).getMat())) {
							keyActions("ChangePiece", true);
							Main.app.getBoard().destroyCompletedLines();
						}else{
							Main.app.setCurrentPiece(new Piece(0.1f, 2*0.1f, -1, 1.2f, "GameOver.piece", ColorRGBA.White, assetManager, null));
							Main.app.getBoard().setGameOver(true, 0.1f);
						}
                    }
                }
            }else{
                if (!Main.app.getBoard().isGameOver()) {
                    Main.app.setCurrentPiece(new Piece(0.1f, 2*0.1f, -1, 1.2f, "GameOver.piece", ColorRGBA.White, assetManager, null));
                    Main.app.getBoard().setGameOver(true, 0.1f);
                }
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
            ((Piece)spatial).setRotating(false);
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

    public int getFullFallSpeed() {
        return fullFallSpeed;
    }

    public void setFullFallSpeed(int fullFallSpeed) {
        this.fullFallSpeed = fullFallSpeed;
    }

    public boolean isAccelerated() {
        return accelerated;
    }
}

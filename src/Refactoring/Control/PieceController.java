//REFACTORED STATUS: ON GOING.

package Refactoring.Control;

import Refactoring.View.PlayablePiece;
import com.jme3.input.KeyInput;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by blackpearl on 03/10/14.
 */
public class PieceController extends BaseController{

    private boolean accelerated;
    private int fullFallSpeed;
    protected static final Logger logger = Logger.getLogger(PieceController.class.getName());

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    //=========================== Constructors =====================//
    PieceController(){
        this.fullFallSpeed = Constant.INITIALFALLINTERVAL;
        this.accelerated = false;

        if (!this.loadHotKeys(Constant.PIECECONTROLERCONFIGFILE)){
            //=================Set Default OnAction Keys====================//
            hotKeys.add(new Keys("ChangePiece", KeyInput.KEY_RETURN));
            hotKeys.add(new Keys("AccelerateFall", KeyInput.KEY_DOWN));
            //==================Set Default Analog Keys=====================//
            hotKeys.add(new Keys("RotateClockwise", KeyInput.KEY_UP, Constant.ROTATIONINTERVAL));
            hotKeys.add(new Keys("RotateCounterClockwise", KeyInput.KEY_SPACE, Constant.ROTATIONINTERVAL));
            hotKeys.add(new Keys("MoveRight", KeyInput.KEY_RIGHT, Constant.MOVEINTERVAL));
            hotKeys.add(new Keys("MoveLeft", KeyInput.KEY_LEFT, Constant.MOVEINTERVAL));
            if (!this.setupDefaultHotKeys(Constant.PIECECONTROLERCONFIGFILE)){
                logger.log(Level.WARNING, "Can't save file {0}, maybe you don't have permission, if you do, please report this error.", Constant.PIECECONTROLERCONFIGFILE);
            }
        }
    }
    //==============================================================//

    @Override
    protected void controlUpdate(float tpf) {
        if(spatial != null) {
            fall(Constant.MOVEDISTANCE);
            ((PlayablePiece)spatial).setRotating(false);
        }
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
    @Override
    protected void keyActions(String name, boolean pressed) {
        if (name.equals("ChangePiece") && pressed){

        }else if(name.equals("AccelerateFall")) {

        }else if(name.equals("RotateClockwise") && pressed){

        }else if(name.equals("RotateCounterClockwise") && pressed){

        }else if(name.equals("MoveRight") && pressed){

        }else if(name.equals("MoveLeft") && pressed){

        }
    }

    //==========================Movement============================//
    private void rotate(float degreesX, float degreesY, float degreesZ){
    }

    private void moveX(int orientation, float distance){
    }

    private void moveY(int orientation, float distance){
    }

    private void fall(float heightRelativeToCubeSize) {
    }
    //==============================================================//
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

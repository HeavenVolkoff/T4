//REFACTORED STATUS: ON GOING.

package Refactoring.Control;

import Refactoring.Model.BasicMechanics;
import Refactoring.Primary.Main;
import Refactoring.View.Piece;
import Refactoring.View.PlayablePiece;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by blackpearl on 03/10/14.
 */
public class PieceController extends BaseController {

    private boolean accelerated;
    private int fullFallSpeed;
    protected static final Logger logger = Logger.getLogger(PieceController.class.getName());

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    //=========================== Constructors =====================//
    public PieceController(int initialFallSpeed){
        super(Constant.PIECECONTROLERCONFIGFILE);
        this.fullFallSpeed = initialFallSpeed;
        this.accelerated = false;

        if (!this.loadHotKeys()){
            logger.log(Level.WARNING, "Can not load file {0}, maybe you do not have permission, if you do, please report this error.", Constant.PIECECONTROLERCONFIGFILE);
        }
    }
    //==============================================================//

    @Override
    protected void setupDefaultHotKeys(){
        hotKeys.add(new Keys("ChangePiece", KeyInput.KEY_RETURN));
        hotKeys.add(new Keys("AccelerateFall", KeyInput.KEY_DOWN));
        //==================Set Default Analog Keys=====================//
        hotKeys.add(new Keys("RotateClockwise", KeyInput.KEY_UP, Constant.ROTATIONINTERVAL));
        hotKeys.add(new Keys("RotateCounterClockwise", KeyInput.KEY_SPACE, Constant.ROTATIONINTERVAL));
        hotKeys.add(new Keys("MoveRight", KeyInput.KEY_RIGHT, Constant.MOVEINTERVAL));
        hotKeys.add(new Keys("MoveLeft", KeyInput.KEY_LEFT, Constant.MOVEINTERVAL));
        hotKeys.add(new Keys("Escape", KeyInput.KEY_ESCAPE, Constant.MOVEINTERVAL));
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(spatial != null) {
            fall();
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
		if (pressed) {
			switch (name) {
				case "ChangePiece":
					setSpatial(null);
					Main.app.setCurrentPiece(new PlayablePiece(Main.app.getNextPiece().getName(), new Vector3f(0f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), 0), true, ColorRGBA.randomColor(), this));
					if (this.accelerated) {
						((PlayablePiece) spatial).setPieceFallingTime(fullFallSpeed/4);
					}else{
						((PlayablePiece) spatial).setPieceFallingTime(fullFallSpeed);
					}
					Main.app.setNextPiece(new Piece(Main.app.getPieceSelector().randomizeFromRandomicMap(), new Vector3f(Main.app.getBoard().getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 10, Main.app.getBoard().getRow()*Constant.MOVEDISTANCE/3f, 0), ColorRGBA.randomColor()));
					break;

				case "AccelerateFall":
                    ((PlayablePiece)spatial).setPieceFallingTime(fullFallSpeed/4);
                    this.accelerated = true;
					break;

				case "RotateClockwise":
                    ((PlayablePiece)spatial).setRotating(true);
                    rotate(Constant.CLOCKWISE);
					break;

				case "RotateCounterClockwise":
                    ((PlayablePiece)spatial).setRotating(true);
                    rotate(Constant.COUNTERCLOCKWISE);
					break;

				case "MoveRight":
                    if (!BasicMechanics.hitRight(((PlayablePiece) spatial).getBoxAbsolutePoint(), Main.app.getBoard()) && !((PlayablePiece) spatial).isRotating()){
                        moveX(Constant.TORIGHT, Constant.MOVEDISTANCE);
                    }
					break;

				case "MoveLeft":
                    if (!BasicMechanics.hitLeft(((PlayablePiece) spatial).getBoxAbsolutePoint(), Main.app.getBoard()) && !((PlayablePiece) spatial).isRotating()){
                        moveX(Constant.TOLEFT, Constant.MOVEDISTANCE);
                    }
					break;

                case "Escape":
                    if (BasicMechanics.isGameOver()){
                        Main.app.unloadEndless();
                        Main.app.startMenu();
                    }
                    break;

                default:
					break;
			}
		}else{
			switch (name) {
				case "AccelerateFall":
                    ((PlayablePiece)spatial).setPieceFallingTime(fullFallSpeed);
                    this.accelerated = false;
					break;

				default:
					break;
			}

		}
    }

    //==========================Movement============================//
    private void rotate(int degreesZ){
		if(BasicMechanics.canRotate((PlayablePiece)spatial, degreesZ, Main.app.getBoard())){
			spatial.rotate( 0, 0, (float) Math.toRadians(degreesZ));
        }
    }

    private void moveX(int orientation, float distance){
		spatial.move(distance * orientation, 0, 0);
		((Piece) spatial).setPosX(distance * orientation);
    }

    private void moveY(int orientation, float distance){
		spatial.move(0, distance * orientation, 0);
		((Piece) spatial).setPosY(distance * orientation);
    }

    private void fall() {
		int keyElapsedTime = (int) ((System.nanoTime() - ((PlayablePiece)spatial).getStartFallTime()) / Constant.NANOTIMETOMILLISECONDS);

		if (keyElapsedTime >= ((PlayablePiece)spatial).getPieceFallingTime()) {
			if (((PlayablePiece)spatial).isFalling()) {
				//Not hit Horizontal frame
				if (!BasicMechanics.hitBottom(((PlayablePiece) spatial).getBoxAbsolutePoint(), Main.app.getBoard())) {
					moveY(Constant.TODOWN, Constant.MOVEDISTANCE);
					if (this.accelerated){
					    Main.app.getScore().updateScore(Main.app.getScore().getLevel());
					}
				}else if (Main.app.getBoard().addPiece(((PlayablePiece) spatial).getBoxAbsolutePoint(), ((PlayablePiece) spatial).getMat())) {
					keyActions("ChangePiece", true);
					BasicMechanics.verifyGameOver(((PlayablePiece) spatial).getBoxAbsolutePoint());
					Main.app.getBoard().destroyCompletedLines();
				}else{
					BasicMechanics.verifyGameOver(((PlayablePiece) spatial).getBoxAbsolutePoint());
				}

			}
			((PlayablePiece)spatial).setStartFallTime(System.nanoTime());
		}
    }
    //==============================================================//
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

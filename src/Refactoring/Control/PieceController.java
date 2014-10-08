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
public class PieceController extends BaseController{

    private boolean accelerated;
    private int fullFallSpeed;
    protected static final Logger logger = Logger.getLogger(PieceController.class.getName());

    ///////////////////////////////////////////REFACTORED///////////////////////////////////////////////////////////////
    //=========================== Constructors =====================//
    public PieceController(int initialFallSpeed){
        super();
        this.fullFallSpeed = initialFallSpeed;
        this.accelerated = false;

        if (!this.loadHotKeys(Constant.PIECECONTROLERCONFIGFILE)){
            logger.log(Level.WARNING, "Can't load file {0}, maybe you don't have permission, if you do, please report this error.", Constant.PIECECONTROLERCONFIGFILE);
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
					Main.app.setNextPiece(new Piece(/*Main.app.getPieceSelector().randomizeFromMap())*/"Z.piece", new Vector3f(2f, 2.5f, 0), ColorRGBA.randomColor()));
					break;

				case "AccelerateFall":

					break;

				case "RotateClockwise":

					break;

				case "RotateCounterClockwise":

					break;

				case "MoveRight":

					break;

				case "MoveLeft":

					break;

				default:
					break;
			}
		}else{
			switch (name) {
				case "AccelerateFall":

					break;

				default:
					break;
			}

		}
    }

    //==========================Movement============================//
    private void rotate(float degreesX, float degreesY, float degreesZ){
		if(BasicMechanics.canRotate()){
			spatial.rotate((float) Math.toRadians(degreesX), (float) Math.toRadians(degreesY), (float) Math.toRadians(degreesZ));
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
		int keyElapsedTime = (int) ((System.nanoTime() - ((PlayablePiece)spatial).getStartFallTime()) / 1000000);

		if (keyElapsedTime >= ((PlayablePiece)spatial).getPieceFallingTime()) {
			if (((PlayablePiece)spatial).isFalling()) {
				//Not hit Horizontal frame
				System.out.println(BasicMechanics.hitBottom(((PlayablePiece) spatial).getBoxAbsolutePoint(), Main.app.getBoard()));
				if (!BasicMechanics.hitBottom(((PlayablePiece) spatial).getBoxAbsolutePoint(), Main.app.getBoard())) {
					moveY(Constant.TODOWN, Constant.MOVEDISTANCE);
					if (this.accelerated){
						//Main.app.getScore().updateScore(Main.app.getScore().getLevel(), 1);
					}
				}else if (Main.app.getBoard().addPiece(((PlayablePiece) spatial).getBoxAbsolutePoint(), ((PlayablePiece) spatial).getMat())) {
					keyActions("ChangePiece", true);
					/*
					NEED REFACTOR
					if(Main.app.getBoard().isGameOver(((PlayablePiece) spatial).getBoxAbsolutePoint(), ((PlayablePiece) spatial).getNumBox())){
						Main.app.setCurrentPiece(new PlayablePiece(0.1f, 2*0.1f, -1, 1.2f, "Messages/GameOver.piece", ColorRGBA.White, assetManager, null));
						Main.app.getBoard().setGameOver(true, 0.1f);
					}
					*/
					Main.app.getBoard().destroyCompletedLines();
				}else{
					/*
					NEED REFACTOR
					Main.app.setCurrentPiece(new PlayablePiece(0.1f, 2*0.1f, -1, 1.2f, "Messages/GameOver.piece", ColorRGBA.White, assetManager, null));
					Main.app.getBoard().setGameOver(true, 0.1f);
					*/
				}

			}
			((PlayablePiece)spatial).setStartFallTime(System.nanoTime());
		}
    }
    //==============================================================//
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

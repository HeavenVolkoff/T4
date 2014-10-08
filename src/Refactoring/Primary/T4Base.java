//REFACTORED STATUS: ON GOING.

package Refactoring.Primary;


import Refactoring.Control.Constant;
import Refactoring.Control.PieceController;
import Refactoring.View.Board;
import Refactoring.View.Piece;
import Refactoring.View.PlayablePiece;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class T4Base extends SimpleApplication {
    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
    private PlayablePiece currentPiece; //Current Old.View.Piece on Screen
    private Piece nextPiece; //Next Old.View.Piece to be on Screen
    private PieceController control;
    private Board board;
    //private Score score;
    //private LevelBar levelBar;
    //private EffectController lvlBarController;
    //private List<BitmapText> debugMenu = new ArrayList<BitmapText>();
    //private PieceSelector pieceSelector;

    @Override
    public void simpleInitApp() {
		Constant.setCUBESIZE(0.15f);

		setupLights();

		startEndless();

		//Fixed Cam
		flyCam.setEnabled(false);
    }

	public void startEndless() {
		board = new Board(10, 20);
		rootNode.attachChild(board);

		control = new PieceController(500);
		currentPiece = new PlayablePiece("O.piece",new Vector3f(0f, 0.15f + (0.15f * 20 * 1.5f) - (4.5f * 0.15f), 0), true, ColorRGBA.randomColor(), control);
		rootNode.attachChild(currentPiece);

		nextPiece = new Piece("L.piece", new Vector3f(2f, 2.5f, 0), ColorRGBA.randomColor());
		rootNode.attachChild(nextPiece);
	}

	private void setupLights(){
		//TODO: Create Independent Light function
		SpotLight spot = new SpotLight();
		spot.setSpotRange(100f);                           // distance
		spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
		spot.setSpotOuterAngle(50f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
		spot.setColor(ColorRGBA.White.mult(1.3f));         // light color
		spot.setPosition(cam.getLocation());               // shine from camera loc
		spot.setDirection(cam.getDirection());             // shine forward from camera loc
		rootNode.addLight(spot);
	}

	@Override
	public void simpleUpdate(float tpf){
		/*if (control != null) {
			control.controlUpdate(tpf);
		}*/
	}

	public void setNextPiece(Piece nextPiece){
		this.rootNode.detachChild(this.nextPiece);

		this.nextPiece = nextPiece;
		this.rootNode.attachChild(this.nextPiece);
	}

	public AssetManager getAssetManager(){
		return assetManager;
	}

    public InputManager getInputManager(){
        return inputManager;
    }

    public PlayablePiece getCurrentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(PlayablePiece currentPiece) {
		this.rootNode.detachChild(this.currentPiece);

		this.currentPiece = currentPiece;
		this.rootNode.attachChild(this.currentPiece);
    }

    public Board getBoard() {
        return board;
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    public PieceController getControl() {
        return control;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

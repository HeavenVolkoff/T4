import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class TetrisBase extends SimpleApplication {

	private Piece currentPiece; //Current Piece on Screen
    private Piece nextPiece; //Next Piece to be on Screen
    private PieceController control;
    private FadeFilter fade;
    private Board board;
    private Score score;
    private LevelBar levelBar;
	private EffectController lvlBarController;
    private EffectController scoreController;
	private List<BitmapText> debugMenu = new ArrayList<BitmapText>();

	@Override
	public void simpleInitApp(){
        //TODO: Create Light function
        //============================== light Def ================================
        SpotLight spot = new SpotLight();
        spot.setSpotRange(100f);                           // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(50f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(1.3f));         // light color
        spot.setPosition(cam.getLocation());               // shine from camera loc
        spot.setDirection(cam.getDirection());             // shine forward from camera loc
        rootNode.addLight(spot);
        //=========================================================================

		//Create Debug Menu
	    float lineSize = 0;
		for(int i = 0; i < 14; i++){
			debugMenu.add(new BitmapText(guiFont, false));
			debugMenu.get(i).setSize(guiFont.getCharSet().getRenderedSize());
			debugMenu.get(i).setColor(ColorRGBA.White);
			debugMenu.get(i).setText("Debug Info");
			debugMenu.get(i).setLocalTranslation(0, lineSize + debugMenu.get(i).getLineHeight() + 200, 0); // position
			guiNode.attachChild(debugMenu.get(i));
			lineSize += debugMenu.get(i).getLineHeight();
		}

        //Create Board
        board = new Board(10, 20, 0.15f, assetManager);
        rootNode.attachChild(board);

        //Create and Defined Current Piece Controller
        control = new PieceController(500, inputManager, assetManager, 300);

        //Create Current Piece
		currentPiece = new Piece(0.15f, 00f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), 0, "Cage.piece",ColorRGBA.randomColor(), assetManager ,control);
		currentPiece.setFalling(true);
        rootNode.attachChild(currentPiece);

        //Create Next Piece
        nextPiece = new Piece(0.15f, 3.2f, 2.5f, assetManager, null);
        rootNode.attachChild(nextPiece);
        nextPiece.setFalling(false);

        //Create Score
        score = new Score(0.05f,6,-2.7f,3f, 0.1f,assetManager);
        scoreController = new EffectController();
        score.addControl(scoreController);
        rootNode.attachChild(score);

        //Create LevelBar
		lvlBarController = new EffectController();
        levelBar = new LevelBar(0.05f, 2,-2.7f, 2f, 1.1f, 1000, ColorRGBA.Cyan, assetManager, lvlBarController);
        rootNode.attachChild(levelBar);

        //============================== Fade Effect ==============================/*
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fade = new FadeFilter(5);
        fpp.addFilter(fade);
        fade.setValue(0);
        viewPort.addProcessor(fpp);
        fade.fadeIn();
        //=========================================================================

		//Fixed Cam
		flyCam.setEnabled(false);

	}

	@Override
	public void simpleUpdate(float tpf){
		control.controlUpdate(tpf);
        lvlBarController.controlUpdate(tpf);
        //scoreController.controlUpdate(tpf);
	}

	@Override
	public void simpleRender(RenderManager rm){
       /* (optional) Make advanced modifications to frameBuffer and scene graph. */
	}

	public void setCurrentPiece(Piece currentPiece){
        this.rootNode.detachChild(this.currentPiece);

		this.currentPiece = currentPiece;
		this.rootNode.attachChild(this.currentPiece);
	}

    public Piece getNextPiece() {
        return nextPiece;
    }

    public void setNextPiece(Piece nextPiece){
        this.rootNode.detachChild(this.nextPiece);

        this.nextPiece = nextPiece;
        this.rootNode.attachChild(this.nextPiece);
        this.nextPiece.setFalling(false);
    }

    public Board getBoard() {
        return board;
    }

    public Score getScore() {
        return score;
    }

    public LevelBar getLevelBar() {
        return levelBar;
    }

    public FadeFilter getFade(){
        return this.fade;
    }

    public PieceController getControl() {
        return control;
    }

	public BitmapText getDebugMenu(int index) {
		return debugMenu.get(index);
	}
}

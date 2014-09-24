package Primary;

import Control.EffectController;
import Control.PieceController;

import Model.PieceSelector;
import Model.Score;
import View.Board;
import View.LevelBar;
import View.Piece;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.RenderManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class T4Base extends SimpleApplication {

	private Piece currentPiece; //Current View.Piece on Screen
    private Piece nextPiece; //Next View.Piece to be on Screen
    private PieceController control;
    private Board board;
    private Score score;
    private LevelBar levelBar;
	private EffectController lvlBarController;
	private List<BitmapText> debugMenu = new ArrayList<BitmapText>();
    private PieceSelector pieceSelector;

	@Override
	public void simpleInitApp(){
        setupLights();

        setupDebugMenu(14);

        //Create View.Board
        board = new Board(10, 20, 0.15f, assetManager);
        rootNode.attachChild(board);

        //Create Model.Score
        score = new Score(0.1f);
        score.createDisplayScore(0.05f,-3.45f,2.5f, 6, 0, ColorRGBA.White, assetManager);
        rootNode.attachChild(score.getDisplayScore());

        //View.Piece Selector Test
        pieceSelector = new PieceSelector(Arrays.asList(("Beast.piece"),("Q.piece"),("FemaleSeaHorse.piece"),("M.piece"),("Stick.piece"),("Y.piece"),("SeaHorse.piece"),("Snake.piece"),("Corner.piece"),("U.piece"),("X.piece"),("W.piece"), ("I.piece"), ("L.piece"), ("J.piece"), ("T.piece"), ("Z.piece"), ("S.piece"), ("O.piece"), ("Plus.piece"), ("Cage.piece")), assetManager);
        rootNode.attachChild(pieceSelector);

        //Create View.LevelBar
		lvlBarController = new EffectController();
        levelBar = new LevelBar(0.05f, -2.7f, 2f, 1.1f, 1000, ColorRGBA.Cyan, assetManager, lvlBarController);
		levelBar.createDisplayLvl(0.05f*0.3f,-4.7f, 1.95f, 2, 1, ColorRGBA.White, assetManager);
		rootNode.attachChild(levelBar.getDisplayLvl());
        rootNode.attachChild(levelBar);

        //Create Current View.Piece
        control = new PieceController(500, inputManager, assetManager, 300);
        currentPiece = new Piece(0.15f, 00f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), 0, pieceSelector.randomizeFromMap(),ColorRGBA.randomColor(), assetManager ,control);
        currentPiece.setFalling(true);
        rootNode.attachChild(currentPiece);

        //Create Next View.Piece
        nextPiece = new Piece(0.15f, 2f, 2.5f, 0, pieceSelector.randomizeFromMap(),ColorRGBA.randomColor(), assetManager ,null);
        rootNode.attachChild(nextPiece);
        nextPiece.setFalling(false);

        setupFadeFilter(5);

		//Fixed Cam
		flyCam.setEnabled(false);
	}

    private void setupLights(){
        //TODO: Create Light function
        SpotLight spot = new SpotLight();
        spot.setSpotRange(100f);                           // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(50f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(1.3f));         // light color
        spot.setPosition(cam.getLocation());               // shine from camera loc
        spot.setDirection(cam.getDirection());             // shine forward from camera loc
        rootNode.addLight(spot);
    }

    private void setupDebugMenu(int menuItems){
        //Create Debug Menu
        float lineSize = 0;
        for(int i = 0; i < menuItems; i++){
            debugMenu.add(new BitmapText(guiFont, false));
            debugMenu.get(i).setSize(guiFont.getCharSet().getRenderedSize());
            debugMenu.get(i).setColor(ColorRGBA.White);
            debugMenu.get(i).setText("Debug Info");
            debugMenu.get(i).setLocalTranslation(0, lineSize + debugMenu.get(i).getLineHeight() + 200, 0); // position
            guiNode.attachChild(debugMenu.get(i));
            lineSize += debugMenu.get(i).getLineHeight();
        }

    }

    private void setupFadeFilter(int time){
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        FadeFilter fade = new FadeFilter(time);
        fpp.addFilter(fade);
        fade.setValue(0);
        viewPort.addProcessor(fpp);
        fade.fadeIn();
    }

	@Override
	public void simpleUpdate(float tpf){
		control.controlUpdate(tpf);
        lvlBarController.controlUpdate(tpf);
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

    public PieceController getControl() {
        return control;
    }

	public BitmapText getDebugMenu(int index) {
		return debugMenu.get(index);
	}

    public PieceSelector getPieceSelector() {
        return pieceSelector;
    }

}

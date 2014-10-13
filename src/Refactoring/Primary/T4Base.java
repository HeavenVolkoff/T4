//REFACTORED STATUS: ON GOING.

package Refactoring.Primary;


import Refactoring.Control.*;
import Refactoring.Model.AssetLoader;
import Refactoring.Model.BasicMechanics;
import Refactoring.Model.Score;
import Refactoring.View.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.lang.reflect.Array;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */

public class T4Base extends SimpleApplication {
    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
    private PlayablePiece currentPiece;
    private Piece nextPiece;
    private Piece messages;
    private PieceController control;
    private Board board;
    private PieceSelector pieceSelector;
    private AssetLoader pieceLoader;
    private Score score;
    private DisplayNumber scoreDisplay;
    private PointsEffectController pointsControler;
    private ProgressBar levelBar;
    private Indicator lvlIndicator;
    private Indicator multiplierIndicator;
    private MenuOptions options;
    private MainMenu mainMenu;
    private MenuControler menuControler;
    private Frame nextPieceFrame;

    @Override
    public void simpleInitApp() {
		Constant.setCUBESIZE(0.15f);

		setupLights();

        pieceLoader = new AssetLoader(Constant.PIECERESOURCEFOLDER);
        pieceLoader.loadToMemoryMap(Constant.MESSAGESRESOURCEFOLDER);

        setupFadeFilter(5);

        startLoading();

        startMenu();

        //startEndless();

		//Fixed Cam
		flyCam.setEnabled(false);

        //Remove Statistics
        setDisplayFps(false);
        setDisplayStatView(false);
    }

    public void startMenu(){
        rootNode.detachAllChildren();

        Main.app.getInputManager().reset();
        Main.app.getInputManager().clearMappings();
        Main.app.getInputManager().clearRawInputListeners();

        menuControler = new MenuControler();

        options = new MenuOptions(new Vector3f(0,-20*Constant.CUBESIZE,0), new String[]{"StoryModeItem.j3o", "EndlessModeItem.j3o", "PieceFactorItem.j3o", "OptionsItem.j3o", "ExitItem.j3o"}, new String[]{"L.piece", "T.piece", "O.piece", "I.piece", "J.piece"}, 0, new ColorRGBA(0/255f, 73/255f, 178/255f, 1f), ColorRGBA.Cyan);
        rootNode.attachChild(options);

        mainMenu = new MainMenu(menuControler, ColorRGBA.Orange, options);
        mainMenu.setupText("T4 - Bricks Remade. (Raphael Almeida, Guilherme Freire e Vitor Augusto).", ColorRGBA.DarkGray);
        rootNode.attachChild(mainMenu);
    }

    public void unloadStartMenu(){
        menuControler = null;
        options = null;
        mainMenu = null;
        guiNode.detachAllChildren();
    }

    private void startLoading(){
        assetManager.registerLocator("./assets", FileLocator.class);

        pieceLoader = new AssetLoader(Constant.PIECERESOURCEFOLDER);
        pieceLoader.loadToMemoryMap(Constant.MESSAGESRESOURCEFOLDER);
        pieceLoader.loadToMemoryMap(Constant.NUMBERRESOURCEFOLDER);
        pieceLoader.loadToMemoryMap(Constant.SYMBOLSSOURCEFOLDER);
    }

    public void startEndless() {
        rootNode.detachAllChildren();
        unloadStartMenu();

        Main.app.getInputManager().reset();
        Main.app.getInputManager().clearMappings();
        Main.app.getInputManager().clearRawInputListeners();

        pieceSelector = new PieceSelector();

		board = new Board(10, 20);
		rootNode.attachChild(board);

        score = new Score(0.1f);

        scoreDisplay = new DisplayNumber(new Vector3f(-(board.getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 8.6f),board.getRow()*Constant.MOVEDISTANCE/3f,0), Constant.SCORENUMBERRESIZEFACTOR, 0, 6, ColorRGBA.White);
        rootNode.attachChild(scoreDisplay);

        control = new PieceController(500);
		currentPiece = new PlayablePiece(pieceSelector.randomizeFromRandomicMap(), new Vector3f(0f, 0.15f + (0.15f * 20 * 1.5f) - (4.5f * 0.15f), 0), true, ColorRGBA.randomColor(), control);
		rootNode.attachChild(currentPiece);

        nextPiece = new Piece(pieceSelector.randomizeFromRandomicMap(), new Vector3f(board.getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 10, board.getRow()*Constant.MOVEDISTANCE/3f, 0), ColorRGBA.randomColor());
        rootNode.attachChild(nextPiece);

        nextPieceFrame = new Frame("NextPieceFrame", Constant.TOP+Constant.LEFT+Constant.BOTTOM+Constant.RIGHT, nextPiece.getPos(), new Vector3f(6*Constant.MOVEDISTANCE, 6*Constant.MOVEDISTANCE, Constant.CUBESIZE), Constant.CUBESIZE/4, ColorRGBA.Gray);
        rootNode.attachChild(nextPieceFrame);

        levelBar = new ProgressBar(new Vector3f(-(board.getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 8f), board.getRow()*Constant.MOVEDISTANCE/4f, 0), new Vector3f(Constant.CUBESIZE*13f, Constant.CUBESIZE*1.6f, Constant.CUBESIZE/4), Constant.INITIALJUMP, 0, ColorRGBA.DarkGray, ColorRGBA.Cyan);
        rootNode.attachChild(levelBar);

        lvlIndicator = new Indicator("LVL.piece", new Vector3f(-(board.getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 10f)-(Constant.CUBESIZE*10f), board.getRow()*Constant.MOVEDISTANCE/4f, 0), Constant.LVLRESIZEFACTOR, 1, ColorRGBA.White);
        rootNode.attachChild(lvlIndicator);

        multiplierIndicator = new Indicator("Multiplier.piece", new Vector3f(-(board.getCol()*Constant.MOVEDISTANCE/2 + Constant.CUBESIZE * 3f)-(Constant.CUBESIZE*10f), board.getRow()*Constant.MOVEDISTANCE/5.5f, 0), Constant.LVLRESIZEFACTOR, 1, ColorRGBA.White);
        rootNode.attachChild(multiplierIndicator);

        pointsControler = new PointsEffectController();

        BasicMechanics.verifyGameOver(currentPiece.getBoxAbsolutePoint());
    }

    public void unloadEndless(){
        pieceSelector = null;
        board = null;
        score = null;
        scoreDisplay = null;
        control = null;
        currentPiece = null;
        nextPiece = null;
        levelBar = null;
        lvlIndicator = null;
        multiplierIndicator = null;
        pointsControler = null;
    }

    private void setupFadeFilter(int time){
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        FadeFilter fade = new FadeFilter(time);
        fpp.addFilter(fade);
        fade.setValue(0);
        viewPort.addProcessor(fpp);
        fade.fadeIn();
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
		if (pointsControler != null) {
            pointsControler.controlUpdate(tpf);
		}
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
        if (this.currentPiece != null) {
            this.rootNode.attachChild(this.currentPiece);
        }
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

    public Piece getMessages() {
        return messages;
    }

    public void setMessages(Piece messages) {
        if (this.messages != null) {
            this.rootNode.detachChild(this.messages);
        }
        this.messages = messages;
        this.rootNode.attachChild(this.messages);
    }

    public PieceSelector getPieceSelector() {
        return pieceSelector;
    }

    public AssetLoader getPieceLoader() {
        return pieceLoader;
    }

    public Score getScore() {
        return score;
    }

    public DisplayNumber getScoreDisplay() {
        return scoreDisplay;
    }

    public ProgressBar getLevelBar() {
        return levelBar;
    }

    public Indicator getLvlIndicator() {
        return lvlIndicator;
    }

    public Indicator getMultiplierIndicator() {
        return multiplierIndicator;
    }

    public Frame getNextPieceFrame() {
        return nextPieceFrame;
    }

    public BitmapFont getGuiFont(){
        return this.guiFont;
    }

    public Node getGuiNode(){
        return  guiNode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

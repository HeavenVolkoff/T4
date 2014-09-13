package Tetris;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import javafx.scene.control.ProgressBar;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

/*
TODO:
 */

public class TetrisBase extends SimpleApplication {

	private Piece currentPiece; //Current Piece on Screen
    private Piece nextPiece; //Next Piece to be on Screen
    private PieceController control;
	public Material mat;
    private FadeFilter fade;
    private Board board;
    private Score score;
    private LevelBar levelBar;

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

        //============================== Frame Material Def =======================
		mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Ambient", ColorRGBA.DarkGray);
        ColorRGBA alpha = new ColorRGBA(ColorRGBA.DarkGray);
//        alpha.a = 0.1f;
        mat.setColor("Diffuse", alpha);
//        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
//        mat.getAdditionalRenderState().setAlphaFallOff(0.2f);
//        mat.setBoolean("UseAlpha",true);
        mat.setColor("Specular", ColorRGBA.DarkGray);
        mat.setFloat("Shininess", 2);
        mat.setBoolean("UseMaterialColors", true);
        //=========================================================================

        //Create Board
        board = new Board(10,20,0.15f,mat);
        rootNode.attachChild(board);

        //Create and Defined Current Piece Controller
        control = new PieceController(inputManager, assetManager, 300);

        //Create Current Piece
		currentPiece = new Piece(0.15f, 00f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), 0, "O.piece",ColorRGBA.randomColor(), assetManager ,control);
		currentPiece.setFalling(true);
        rootNode.attachChild(currentPiece);

        //Create Next Piece
        nextPiece = new Piece(0.15f, 3.2f, 2.5f, assetManager, null);
        rootNode.attachChild(nextPiece);
        nextPiece.setFalling(false);

        //Create Score
        score = new Score(0.05f,6,-2.7f,3f,assetManager);
        rootNode.attachChild(score);

        //Create LevelBar
        levelBar = new LevelBar(0.05f, 2,-2.7f, 2f, 1.1f, 1000, mat, ColorRGBA.Cyan, assetManager);
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
}

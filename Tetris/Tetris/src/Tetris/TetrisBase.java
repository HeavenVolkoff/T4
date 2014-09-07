package Tetris;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;

/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class TetrisBase extends SimpleApplication {

	private Piece currentPiece; //Current Piece on Screen
    private PieceController control;
	public Material mat;
    Board board;

	@Override
	public void simpleInitApp(){
		//Material Def
		mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");

        board = new Board(10,20,0.15f,mat);
        rootNode.attachChild(board);

        //Create and Defined Current Piece Controller
        control = new PieceController(inputManager, 300);

        //Create Initial Piece
		currentPiece = new Piece(0.15f, 5, 00f, 0.15f+(0.15f*20*1.5f)-(4.5f*0.15f), mat, 0, 0, control);
		this.currentPiece.setPieceIndex(rootNode.attachChild(currentPiece));

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
		if(this.currentPiece.getPieceIndex() != null) {
			this.rootNode.detachChildAt(this.currentPiece.getPieceIndex()-1);
		}
		this.currentPiece = currentPiece;
		this.currentPiece.setPieceIndex(this.rootNode.attachChild(this.currentPiece));
	}
}

//REFACTORED STATUS: ON GOING.

package Refactoring.Primary;


import Old.Control.EffectController;
import Old.Model.PieceSelector;
import Old.Model.Score;
import Old.View.LevelBar;
import Refactoring.Control.PieceController;
import Refactoring.View.Board;
import Refactoring.View.Piece;
import Refactoring.View.PlayablePiece;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by HeavenVolkoff on 30/09/14.
 */
public class T4Base extends SimpleApplication {
    ///////////////////////////////////////////NOT READY YET////////////////////////////////////////////////////////////
    private PlayablePiece currentPiece; //Current Old.View.Piece on Screen
    private Piece nextPiece; //Next Old.View.Piece to be on Screen
    private PieceController control;
    private Board board;
    private Score score;
    private LevelBar levelBar;
    private EffectController lvlBarController;
    private List<BitmapText> debugMenu = new ArrayList<BitmapText>();
    private PieceSelector pieceSelector;

    @Override
    public void simpleInitApp() {

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
        this.currentPiece = currentPiece;
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

package Refactoring;

/**
 * Created by HeavenVolkoff on 29/09/14.
 */
public class PlayablePiece extends Piece {

	public PlayablePiece(Control controller){
		if(controller != null && !controller.isSetSpatial()){
			addControl(controller);
		}
	}
}

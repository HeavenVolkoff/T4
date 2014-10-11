package Refactoring.View;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 10/11/2014.
 */

public class BoxedNumber extends Piece{

    Spatial[] geoList;
    protected static final Logger logger = Logger.getLogger(Piece.class.getName());

    public BoxedNumber(Vector3f pos, float resizeFactor, Integer value, ColorRGBA color) {
        super("NumBase.piece", pos, resizeFactor, color);

        this.geoList = new Spatial[15];

        int count = 0;
        for (Spatial geo : getChildren()){
            this.geoList[count] = geo;
            count++;
        }

        if (value != null) {
            write(value);
        }else{
            writeNull();
        }
    }
    
    public void write(int value){
        if (value >=0 && value <= 9){
            int[] visibleGeo = new int[15];
            switch (value){
            case 0:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=0; visibleGeo[8]=1;
                visibleGeo[9]=1; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 1:
                visibleGeo[0]=0;   visibleGeo[1]=0; visibleGeo[2]=1;
                visibleGeo[3]=0; visibleGeo[4]=1; visibleGeo[5]=1;
                visibleGeo[6]=0; visibleGeo[7]=0; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=0;visibleGeo[13]=0;visibleGeo[14]=1;
                break;
            case 2:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=0; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=1; visibleGeo[10]=0;visibleGeo[11]=0;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 3:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=0; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 4:
                visibleGeo[0]=1;   visibleGeo[1]=0; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=0;visibleGeo[13]=0;visibleGeo[14]=1;
                break;
            case 5:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=0;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 6:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=0;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=1; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 7:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=0; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=0; visibleGeo[7]=1; visibleGeo[8]=0;
                visibleGeo[9]=1; visibleGeo[10]=0;visibleGeo[11]=0;
                visibleGeo[12]=1;visibleGeo[13]=0;visibleGeo[14]=0;
                break;
            case 8:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=1; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            case 9:
                visibleGeo[0]=1;   visibleGeo[1]=1; visibleGeo[2]=1;
                visibleGeo[3]=1; visibleGeo[4]=0; visibleGeo[5]=1;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=1;
                visibleGeo[12]=1;visibleGeo[13]=1;visibleGeo[14]=1;
                break;
            default:
                //Write -  
                visibleGeo[0]=0;   visibleGeo[1]=0; visibleGeo[2]=0;
                visibleGeo[3]=0; visibleGeo[4]=0; visibleGeo[5]=0;
                visibleGeo[6]=1; visibleGeo[7]=1; visibleGeo[8]=1;
                visibleGeo[9]=0; visibleGeo[10]=0;visibleGeo[11]=0;
                visibleGeo[12]=0;visibleGeo[13]=0;visibleGeo[14]=0;
                logger.log(Level.WARNING, "Invalid Number Send to BoxedNumber {0}.", value);
                break;
            }
            detachAllChildren();
            for (int i = 0; i < 15; i++){
                if (visibleGeo[i] == 1) {
                    attachChild(geoList[i]);
                }else{
                    detachChild(geoList[i]);
                }
            }
        }else{
            logger.log(Level.WARNING, "Out of Range Number Send to BoxedNumber {0}.", value);
        }
    }

    public void writeNull(){
        detachAllChildren();
    }
}

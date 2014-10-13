package Refactoring.Control;

import Refactoring.Primary.Main;
import Refactoring.View.MainMenu;
import com.jme3.input.KeyInput;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 10/12/2014.
 */
public class MenuControler extends BaseController {

    protected static final Logger logger = Logger.getLogger(PieceController.class.getName());

    public MenuControler() {
        super(Constant.MENUCONTROLERCONFIGFILE);

        if (!this.loadHotKeys()){
            logger.log(Level.WARNING, "Can not load file {0}, maybe you do not have permission, if you do, please report this error.", Constant.MENUCONTROLERCONFIGFILE);
        }
    }

    @Override
    protected void setupDefaultHotKeys() {
        //==================Set Default Analog Keys=====================//
        hotKeys.add(new Keys("ConfirmItem", KeyInput.KEY_RETURN, Constant.MOVEINTERVAL));
        hotKeys.add(new Keys("MenuUP", KeyInput.KEY_UP, Constant.MOVEINTERVAL));
        hotKeys.add(new Keys("MenuDown", KeyInput.KEY_DOWN, Constant.MOVEINTERVAL));
    }

    @Override
    protected void keyActions(String name, boolean pressed) {
        if (pressed) {
            switch (name) {
                case "ConfirmItem":
                    if (((MainMenu)spatial).getMenuOptions() != null) {
                        ((MainMenu) spatial).getMenuOptions().executeAction();
                    }
                    break;

                case "MenuUP":
                    if (((MainMenu)spatial).getMenuOptions() != null) {
                        ((MainMenu)spatial).getMenuOptions().selectOption(((MainMenu)spatial).getMenuOptions().getSelectedMenu()-1);
                    }
                    break;

                case "MenuDown":
                    if (((MainMenu)spatial).getMenuOptions() != null) {
                        ((MainMenu)spatial).getMenuOptions().selectOption(((MainMenu)spatial).getMenuOptions().getSelectedMenu()+1);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(spatial != null) {
            ((MainMenu)spatial).getT4Piece().alphaEffect(tpf / 2);
            if (((MainMenu)spatial).getMenuOptions() != null){
                ((MainMenu)spatial).getMenuOptions().rotateSelected(tpf);
            }
        }
    }
}

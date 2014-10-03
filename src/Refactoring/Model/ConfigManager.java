//REFACTORED STATUS: OK.

package Refactoring.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by blackpearl on 03/10/14.
 */

public class ConfigManager {

    Properties iniFile;
    String fileName;
    protected static final Logger logger = Logger.getLogger(ConfigManager.class.getName());

    //======================== Class Constructors ==========================//
    public ConfigManager (String fileName){
        this.iniFile = new Properties();

        if (!load(fileName)) {
            if (!create(fileName)){
                this.fileName = "";
            }
        }
    }
    //======================================================================//

    public boolean load(String fileName){
        try {
            String appPath = new File(".").getCanonicalPath();
            File file = new File(appPath + fileName);
            if (file.exists() && !file.isDirectory()) {
                iniFile.load(new FileInputStream(file));
                this.fileName = fileName;
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
        logger.log(Level.WARNING, "Error while trying to load {0} file, please report this error.", fileName);
        return false;
        }
    }

    private boolean create(String fileName){
        try {
            String appPath = new File(".").getCanonicalPath();
            File file = new File(appPath + fileName);
            if (!file.isDirectory()) {
                iniFile.store(new FileOutputStream(fileName), null);
                load(fileName);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while trying to create {0} file, please report this error.", fileName);
            return false;
        }
    }

    public boolean defineKey(String key, String value){
        if (!fileName.equals("")){
            if (iniFile.stringPropertyNames().contains(key)){
                iniFile.setProperty(key, value);
                return true;
            }else{
                iniFile.put(key, value);
                return true;
            }
        }
        return false;
    }

    public Set<String> getItems(){
        if (!fileName.equals("")) {
            return iniFile.stringPropertyNames();
        } else {
            return null;
        }
    }

    public String getValue(String key){
        if (!fileName.equals("")){
            if (iniFile.stringPropertyNames().contains(key)){
                return iniFile.getProperty(key);
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    public  boolean save(){
        if (!fileName.equals("")) {
            try {
                iniFile.store(new FileOutputStream(this.fileName), null);
                return true;
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error while trying to save {0} file, please report this error.", this.fileName);
                return false;
            }
        }else{
            return false;
        }
    }
}

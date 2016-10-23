package intem.models;

import intem.Config;
import intem.InTem;

import java.io.*;

import java.util.Properties;


/**
 * @class OptionLoader
 * Created by minh on 25/09/16.
 */
public class OptionLoader {

    private Properties prop;
    private OutputStream output;
    private InputStream input;
    private String file;

    public OptionLoader(String file) {
        this.file = file;
        prop = new Properties();
        output = null;
        input = null;

        try {
            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException e){
                File myFile = new File(file);
                myFile.createNewFile();
                input = new FileInputStream(myFile);
            }


            prop.load(input);

        } catch (IOException io) {
            io.printStackTrace();
            InTem.app().alert(Config.APP_ALERT_ERROR, "Opp!, can not open/save configuration file,");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    InTem.app().alert(Config.APP_ALERT_ERROR, "Opp!, can not open/save configuration file,");
                }
            }

        }
    }

    public String get(String key) {
        return prop.getProperty(key, "");
    }

    public String get(String key, String def) {
        return prop.getProperty(key, def);
    }

    public void put(String key, String value) {
        prop.setProperty(key, value);
    }

    public int getInt(String key){
        return new Integer(get(key));
    }

    public int getInt(String key, Integer def){
        return new Integer(get(key, def.toString()));
    }

    public void put(String key, Integer value){
        put(key, value.toString());
    }

    public void save() {
        try {
            output = new FileOutputStream(file);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
            InTem.app().alert(Config.APP_ALERT_ERROR, "Opp!, can not open/save configuration file,");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    InTem.app().alert(Config.APP_ALERT_ERROR, "Opp!, can not open/save configuration file,");
                }
            }
        }
    }
}

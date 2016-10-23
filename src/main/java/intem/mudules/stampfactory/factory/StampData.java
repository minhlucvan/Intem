package intem.mudules.stampfactory.factory;

import com.csvreader.CsvReader;
import intem.mudules.stampfactory.StampFactory;
import intem.mudules.stampfactory.StampFactoryEvent;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by minh on 28/09/16.
 */
public class StampData {
    private String serialKey;
    private String codeKey;
    private String path;

    private ArrayList<Entity> dataArray;
    private boolean isLoaded;

    private StampFactory factory;

    public class Entity {
        public String serial;
        public String code;
        public String qrPath;
    }

    public StampData() {

    }

    public StampData(String path, StampFactory fac) {
        this.path = path;
        this.factory = fac;
        this.dataArray = new ArrayList<Entity>();
        this.serialKey = "Serial";
        this.codeKey = "Code";
        this.isLoaded = false;
        readFile();
    }

    private String pareStampCode(String raw){
        int i = raw.lastIndexOf("/") + 1;
        return raw.substring(i);
    }

    private String parseQRPath(Entity ent){
        //return ent.code + "-" + ent.serial + ".png";
        return "test.png";
    }

    private void readFile() {
        try {
            CsvReader stamps = new CsvReader(path);

            stamps.readHeaders();

            while (stamps.readRecord()) {
                Entity stamp = new Entity();
                stamp.code = pareStampCode(stamps.get(0));
                stamp.serial = stamps.get(1);
                stamp.qrPath = parseQRPath(stamp);
                dataArray.add(stamp);
            }
            stamps.close();

            StampFactoryEvent e = new StampFactoryEvent(StampFactoryEvent.DATA_LOADED);
            e.setMessage("CSV data file has been loaded successfully.");
            factory.trigger(e);
            isLoaded = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            StampFactoryEvent evt = new StampFactoryEvent(StampFactoryEvent.DATA_LOAD_FAILE);
            evt.setMessage("can not load CSV data file.");
            factory.trigger(evt);
            isLoaded = false;
        }
    }

    public String getSerialKey() {
        return serialKey;
    }

    public void setSerialKey(String serialKey) {
        this.serialKey = serialKey;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StampFactory getFactory() {
        return factory;
    }

    public void setFactory(StampFactory factory) {
        this.factory = factory;
    }

    public ArrayList<Entity> toArrayList() {
        return dataArray;
    }
}

package intem.mudules.stampfactory.input;

import java.util.HashMap;

/**
 * Created by minh on 27/09/16.
 */
public class StampFactoryOption {

    public static final int INPUT_CSV = 0;

    public static final int OUTPUT_PNG = 1;
    public static final int OUTPUT_PDF = 2;
    public static final int OUTPUT_SINGLE = 3;
    public static final int OUTPUT_COLLECTION = 4;

    public static final int STAMP_WIDTH = 5;
    public static final int STAMP_HEIGHT = 6;
    public static final int PAGE_HEIGHT = 7;
    public static final int PAGE_WIDTH = 8;
    public static final int STAMP_GAP_X = 9;
    public static final int STAMP_GAP_Y = 10;

    public static final int START_REPLACER = 11;
    public static final int END_REPLACER = 12;

    public static final int QR_CODE_URI = 13;
    public static final int SERI_URI = 14;
    public static final int STAMP_CODE_URI = 15;

    public static final int IMAGE_TAG = 16;

    public static final int INPUT_QRCODE_DIR = 17;

    public static final int OUTPUT_LOCATION = 18;

    private HashMap<Integer, String> options;

    public StampFactoryOption(){
        options = new HashMap<Integer, String>();
    }

    public void put(int key, String value){
        options.put(key, value);
    }

    public void put(int key, int value){
        options.put(key, String.valueOf(value));
    }

    public String get(int key){
        return options.get(key);
    }

    public int getInt(int key){
        return Integer.parseInt(options.get(key));
    }

    public static StampFactoryOption getDefault(){
        return new StampFactoryOption();
    }
}

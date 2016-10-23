package intem;

import intem.models.OptionLoader;

/**
 * Created by minh on 01/10/16.
 */
public class Setting {
    public static final int STAMP_CODE_XPATH_KEY = 356;
    public static final int STAMP_SERI_XPATH_KEY = 312;
    public static final int STAMP_QRCODE_XPATH_KEY = 251;
    public static final int STAMP_OUTPUT_FORMAT_KEY = 532;
    public static final int STAMP_TEMPLATE_PATHP_KEY = 124;
    public static final int STAMP_SERI_PATH_KEY = 783;
    public static final int STAMP_QR_PATH_KEY = 372;

    private static final String loaderPath = Config.rootPath + "/config/setting.conf";
    
    private OptionLoader optionLoader;

    public Setting(){
        optionLoader = new OptionLoader(loaderPath);
    }

    public String getOption(String key){
        return optionLoader.get(key);
    }

    public String getOption(String key, String def){
        return optionLoader.get(key, def);
    }

    public  void setOption(String key, String value){
        optionLoader.put(key, value);
    }

    public int getOptionInt(String key){
        return optionLoader.getInt(key);
    }

    public  int getOptionInt(String key, int def){
        return optionLoader.getInt(key, def);
    }

    public void setOption(String key, int value){
        optionLoader.put(key, value);
    }

    public boolean save(){
        optionLoader.save();
        return true;
    }
}

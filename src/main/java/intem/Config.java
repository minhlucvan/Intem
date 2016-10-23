package intem;

import intem.models.OptionLoader;

import java.net.URLDecoder;

/**
 * Created by minh on 25/09/16.
 */
public class Config {
    public static String rootPath;
    private static InTem app;

    public static final int APP_ALERT_ERROR = 216;

    public static final String APP_STATUS_KEY = "APP_STATUS";
    public static final String MAIN_WINDOW_ICON_KEY = "MAIN_WINDOW_ICON";
    public static final String MAIN_WINDOW_TITLE_KEY = "MAIN_WINDOW_TITLE";
    public static final String MAIN_WINDOW_WIDTH_KEY = "MAIN_WINDOW_WIDTH";
    public static final String MAIN_WINDOW_HEIGHT_KEY = "MAIN_WINDOW_HEIGHT";
    public static final String APP_CONFIG_FILE = "APP_CONFIG_FILE";

    public static final String UI_DEFAULT_BTN_TEXT_KEY = "UI_DEFAULT_BTN_TEXT";
    public static final String UI_INTERACT_BTN_ICON_KEY = "UI_INTERACT_BTN_ICON_KEY";

    public static final String UI_TEXT_DOCUMENT_LOAD_START_KEY = "UI_TEXT_DOCUMENT_LOAD_START";
    public static final String UI_TEXT_DOCUMENT_LOAD_DONE_KEY = "UI_TEXT_DOCUMENT_LOAD_DONE";
    public static final String UI_TEXT_DOCUMENT_BUILD_SART_KEY = "UI_TEXT_DOCUMENT_BUILD_SART";
    public static final String UI_TEXT_DOCUMENT_BUILD_DONE_KEY = "UI_TEXT_DOCUMENT_BUILD_DONE";
    public static final String UI_TEXT_DOCUMENT_RENDER_SART_KEY = "UI_TEXT_DOCUMENT_RENDER_SART";
    public static final String UI_TEXT_DOCUMENT_RENDER_DONE_KEY = "UI_TEXT_DOCUMENT_RENDER_DONE";
    public static final String UI_TEXT_BROSWE_TEMPLATE_KEY = "UI_TEXT_BROSWE_TEMPLATE";
    public static final String UI_TEXT_CODE_INPUT_TITLE_KEY = "UI_TEXT_CODE_INPUT_TITLE";
    public static final String UI_TEXT_SERI_INPUT_TITLE_KEY = "UI_TEXT_SERI_INPUT_TITLE";
    public static final String UI_TEXT_QR_INPUT_TITLE_KEY = "UI_TEXT_QR_INPUT_TITLE";
    public static final String UI_TEXT_BROSWE_SERI_KEY = "UI_TEXT_BROSWE_SERI";
    public static final String UI_TEXT_BROSE_QR_KEY = "UI_TEXT_BROSE_QR";
    public static final String UI_TEXT_START_PRINT_BTN_KEY = "UI_TEXT_START_PRINT_BTN";
    public static final String UI_TEXT_OUTPUT_TYPE_TITLE_KEY = "UI_TEXT_OUTPUT_TYPE_TITLE";
    public static final String UI_TEXT_APP_READY_STATUS_KEY = "UI_TEXT_APP_READY_STATUS";
    public static final String UI_TEXT_OUTPUT_LOCAL_TITLE_KEY = "UI_TEXT_OUTPUT_LOCAL_TITLE";
    public static final String UI_TEXT_OUTPUT_LOCAL_BTN_KEY = "UI_TEXT_OUTPUT_LOCAL_BTN";
    public static final String UI_TEXT_STAMP_WIDTH_KEY = "UI_TEXT_STAMP_WIDTH";
    public static final String UI_TEXT_STAMP_HEIGHT_KEY = "UI_TEXT_STAMP_HEIGHT";
    public static final String UI_TEXT_STAMP_SIZE_KEY = "UI_TEXT_STAMP_SIZE";
    public static final String UI_TEXT_OUTPUT_PAPER_SIZE_KEY = "UI_TEXT_OUTPUT_PAPER_SIZE";

    public static final String FACTORY_STAMP_WIDTH_DEFAULT_KEY = "FACTORY_STAMP_WIDTH_DEFAULT";
    public static final String FACTORY_STAMP_HEIGHT_DEFAULT_KEY = "FACTORY_STAMP_HEIGHT_DEFAULT";
    public static final String OUTPUT_LOCATION_DEFAULT_KEY = "OUTPUT_LOCATION_DEFAULT";
    public static final String OUTPUT_NAME_DEFAULT_KEY = "OUTPUT_NAME_DEFAULT";
    public static final String OUTPUT_PDF_PAPER_SIZE_KEY = "OUTPUT_PDF_PAPER_SIZE";
    public static final String UI_TEXT_OUTPUT_NAME_KEY = "UI_TEXT_OUTPUT_NAME";

    private static String configFile = "app.conf";

    private static OptionLoader optionLoader;

    public Config(InTem app){
        Config.app = app;
        Config.rootPath = getRootPath();

        this.optionLoader = new OptionLoader(rootPath+"/config/"+configFile);

    }

    public void setUp(){
        setOption(MAIN_WINDOW_ICON_KEY, rootPath + "assets/window-icon.png");
        setOption(MAIN_WINDOW_TITLE_KEY, "InTem - EzCheck Stamp Factory");
        setOption(APP_STATUS_KEY, "data-set-up-complete");
        setOption(APP_CONFIG_FILE, rootPath+"config/"+configFile);
        setOption(MAIN_WINDOW_WIDTH_KEY, 800);
        setOption(MAIN_WINDOW_HEIGHT_KEY, 600);

        setOption(UI_DEFAULT_BTN_TEXT_KEY, "*");
        setOption(UI_INTERACT_BTN_ICON_KEY, rootPath+"assets/interact-btn-icon.png");

        setOption(UI_TEXT_BROSE_QR_KEY, "Choose QRcode folder...");
        setOption(UI_TEXT_BROSWE_SERI_KEY, "Choose Serials file...");
        setOption(UI_TEXT_BROSWE_TEMPLATE_KEY, "Choose Template file...");
        setOption(UI_TEXT_CODE_INPUT_TITLE_KEY, "Stamp code URI:");
        setOption(UI_TEXT_DOCUMENT_BUILD_DONE_KEY, "Document built.");
        setOption(UI_TEXT_DOCUMENT_BUILD_SART_KEY, "Document building.");
        setOption(UI_TEXT_DOCUMENT_RENDER_DONE_KEY, "Document rendered.");
        setOption(UI_TEXT_DOCUMENT_RENDER_SART_KEY, "Document rendering.");
        setOption(UI_TEXT_DOCUMENT_LOAD_DONE_KEY, "Document loaded.");
        setOption(UI_TEXT_DOCUMENT_LOAD_START_KEY, "Document loading.");
        setOption(UI_TEXT_SERI_INPUT_TITLE_KEY, "Stamp serial URI:");
        setOption(UI_TEXT_QR_INPUT_TITLE_KEY, "Stamp QRcode URI:");
        setOption(UI_TEXT_START_PRINT_BTN_KEY, "START PRINT");
        setOption(UI_TEXT_OUTPUT_TYPE_TITLE_KEY, "Output format:");
        setOption(UI_TEXT_APP_READY_STATUS_KEY, "App ready....");
        setOption(UI_TEXT_OUTPUT_LOCAL_BTN_KEY, "Browse...");
        setOption(UI_TEXT_OUTPUT_LOCAL_TITLE_KEY, "Output location:");
        setOption(UI_TEXT_STAMP_HEIGHT_KEY, "Height:");
        setOption(UI_TEXT_STAMP_WIDTH_KEY, "Width:");
        setOption(UI_TEXT_OUTPUT_NAME_KEY, "Output file name:");
        setOption(FACTORY_STAMP_HEIGHT_DEFAULT_KEY, "200");
        setOption(FACTORY_STAMP_WIDTH_DEFAULT_KEY, "300");
        setOption(OUTPUT_LOCATION_DEFAULT_KEY, "");
        setOption(OUTPUT_NAME_DEFAULT_KEY, "stamps.pdf");
        setOption(OUTPUT_PDF_PAPER_SIZE_KEY, "A4");
        setOption(UI_TEXT_STAMP_SIZE_KEY, "Stamp size:");
        setOption(UI_TEXT_OUTPUT_PAPER_SIZE_KEY, "Page size:");
        save();
    }

    public static String getOption(String key){
        return optionLoader.get(key);
    }

    public static String getOption(String key, String def){
        return optionLoader.get(key, def);
    }

    public static void setOption(String key, String value){
        optionLoader.put(key, value);
    }

    public static int getOptionInt(String key){
        return optionLoader.getInt(key);
    }

    public static int getOptionInt(String key, int def){
        return optionLoader.getInt(key, def);
    }

    public static void setOption(String key, int value){
        optionLoader.put(key, value);
    }

    public static boolean save(){
        optionLoader.save();
        return true;
    }

    public static boolean isSetup(){
        String appStatus = getOption(Config.APP_STATUS_KEY, "empty");

        return !appStatus.equals("empty");
    }

    private String getRootPath(){
        try {
            String path = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            return decodedPath;
        } catch (Exception e){
            app.stop("Opp! can not set application root path.");
            return "";
        }
    }
}

package intem.views;

import intem.Config;
import intem.InTem;

/**
 * Created by minh on 25/09/16.
 */
public class GUI {

    private InTem app;
    private Window window;

    public GUI(InTem inTem){
        app = inTem;
        this.init();
        this.lauch();
    }

    private void init(){
        String title = Config.getOption(Config.MAIN_WINDOW_TITLE_KEY, "");
        int width = Config.getOptionInt(Config.MAIN_WINDOW_WIDTH_KEY, 800);
        int height = Config.getOptionInt(Config.MAIN_WINDOW_HEIGHT_KEY, 600);

        window = new MainWindow(title);
        window.setSize(width, height);
    }

    private void lauch(){
        //TODO
    }

    public void alert(String msg){
        window.alert(msg);
    }

    public void setStatus(String stt){
        window.setStatus(stt);
    }
}

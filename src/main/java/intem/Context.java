package intem;

/**
 * Created by minh on 27/09/16.
 */
public class Context {
    private static InTem app;

    public static void setApp(InTem app) {
        Context.app = app;
    }

    public static InTem app() {
        return app;
    }
}

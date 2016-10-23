package intem.mudules.stampfactory;

/**
 * Created by minh on 29/09/16.
 */
public class StampFactoryEvent {
    public static final int TEMPLATE_LOADED = 744;
    public static final int TEMPLATE_LOAD_FAILE = 973;
    public static final int DATA_LOADED = 736;
    public static final int DATA_LOAD_FAILE = 312;
    private static final int FACTORY_INIT = 95;
    private static final int FACTORY_FINISH = 242;
    private static final int FACROTY_ERROR = 392;
    private static final int FACTORY_READY = 629;
    private static final int STAMP_START = 0;
    private static final int STAMP_FINISH = 573;

    private int type;
    private String message;
    private Object data;

    public StampFactoryEvent(int type, String msg) {
        this(type, msg, new Object());
    }

    public StampFactoryEvent(int type, String msg, Object data){
        this.type = type;
        this.message = msg;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public StampFactoryEvent(int type){
        this(type, "", new Object());
    }

}

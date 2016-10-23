package intem.actions;

import java.util.ArrayList;

/**
 * Created by minh on 25/09/16.
 */
public class Event {
    public static final int TYPE_DEFAULT = 0;
    public static final int APP_SETTING_UP = 1;
    public static final int APP_SETTING_COMPLETE = 2;
    public static final int TEM_TEMPLATE_SELECTED = 3;
    public static final int FACTORY_PRINT_TO_FILE = 4;
    public static final int APP_READY = 5;
    public static final int TEM_TEMPLATE_LOADED = 6;
    public static final int TEM_TEMPLATE_RENDER_DONE = 7;
    public static final int FACTORY_PRINT_START_PRINT = 8;
    public static final int TEM_TEMPLATE_RENDER_START = 9;
    public static final int TEM_SERIAL_FILE_SELECTED = 10;
    public static final int TEM_SERIAL_FILE_LOADED = 11;
    public static final int TEM_SERIAL_FILE_LOAD_FAILE = 12;
    public static final int TEM_TEMPLATE_LOAD_FAILE = 13;
    public static final int FACTORY_SET_QRCODE_DIR = 14;
    public static final int FACTORY_SET_OUTPUT_DIR = 15;
    public static final int FACTORY_SET_QRCODE_URI = 16;
    public static final int FACTORY_SET_STAMP_CODE_URI = 19;
    public static final int FACTORY_SET_STAMP_SERRY_URI = 20;
    public static final int END_DEFAULT_EVENT = 21;

    public int type;
    public String message;
    public Object data;
    protected boolean isHandled;
    protected boolean isCanceled;

    public Event(int type){
        this(type, "");
    }

    public Event(int type, String msg){
        this.type = type;
        this.message = msg;
        this.isHandled = false;
        this.isCanceled = false;
    }

    protected void cancel(){
        isCanceled = true;
    }

    protected void handle(){
        isHandled = true;
    }

    public static boolean isValid(Event e){
        return isValid(e.type);
    }

    public static boolean isValid(int type){
        return  (type > TYPE_DEFAULT && type < END_DEFAULT_EVENT);
    }

    public static ArrayList<ArrayList<EventListener>> getEventsList(){

        ArrayList<ArrayList<EventListener>> events = new ArrayList<ArrayList<EventListener>>();

        for(int i = 0; i < END_DEFAULT_EVENT; i++){
            events.add(new ArrayList<EventListener>());
        }

        return events;
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
}

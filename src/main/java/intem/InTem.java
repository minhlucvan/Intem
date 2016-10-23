package intem;

import intem.actions.AbstractEventListener;
import intem.actions.Event;
import intem.actions.EventListener;
import intem.mudules.stampfactory.StampFactory;
import intem.mudules.stampfactory.StampFactoryActionListener;
import intem.mudules.stampfactory.StampFactoryEvent;
import intem.mudules.stampfactory.input.StampFactoryInput;
import intem.mudules.stampfactory.input.StampFactoryOption;
import intem.mudules.stampfactory.output.PDFStampCollection;
import intem.mudules.stampfactory.output.StampFactoryOutput;
import intem.views.GUI;
import intem.views.Window;

import java.util.ArrayList;

/**
 * Created by minh on 25/09/16.
 */
public class InTem {

    private static InTem instance;

    private Config config;
    private GUI gui;
    private ArrayList<ArrayList<EventListener>> eventList;

    private StampFactory factory;
    private StampFactoryOption factoryOption;
    private StampFactoryInput input;
    private PDFStampCollection ouput;

    public InTem(){
        Context.setApp(this);
        eventList = Event.getEventsList();
        eventInit();
        config = new Config(this);
        gui = new GUI(this);

        if(!config.isSetup()){
            this.setUp();
        }

        trigger(Event.APP_READY);
    }

    public void run(){
        //TODO
    }

    public void quit(){
        //TODO
    }

    public void stop(String message){
        //TODO
    }

    public void alert(int type, String message){
        //TODO
    }

    public void alert(String message){
        //TODO
    }

    public void log(String msg){
        //TODO
    }

    public Config config(){
        //TODO
        return null;
    }

    public Window gui(){
        //TODO
        return null;
    }

    public boolean blockUI(){
        //TODO
        return true;
    }

    public boolean blockUI(String message){
        //TODO
        return true;
    }

    public boolean blockUI(String message, boolean spinner){
        return true;
    }

    public boolean blockUI(String message, boolean spinner, int live){
        //TODO
        return true;
    }

    public boolean unBlockUI(){
        return true;
    }

    public void trigger(int event){
        trigger(new Event(event));
    }

    public boolean trigger(Event event){
        if(!Event.isValid(event)) return false;
        for (EventListener callback : eventList.get(event.type)){
            callback.handle(event);
        }
        return true;
    }

    public boolean addListener(int eventType, EventListener listener){
        if(!Event.isValid(eventType)) return false;

        eventList.get(eventType).add(listener);

        return true;
    }

    private void eventInit(){
        this.addListener(Event.APP_SETTING_UP, new AppSettingListener());
        this.addListener(Event.APP_READY, new OnAppReadyListener());
        this.addListener(Event.TEM_TEMPLATE_SELECTED, new OnTemplateSelected());
        this.addListener(Event.TEM_TEMPLATE_LOADED, new OnTemplatedLoaded());
        this.addListener(Event.TEM_SERIAL_FILE_SELECTED, new OnSerialFileSelected());
        this.addListener(Event.TEM_SERIAL_FILE_LOADED, new OnSerialFileLoaded());
        this.addListener(Event.TEM_SERIAL_FILE_LOAD_FAILE, new OnSerialFileLoadFaile());
        this.addListener(Event.TEM_TEMPLATE_LOAD_FAILE, new OnTemplateLoadFalie());
        this.addListener(Event.FACTORY_PRINT_START_PRINT, new OnPrintStartBtn());
        this.addListener(Event.FACTORY_SET_QRCODE_DIR, new OnQRCodeDirSet());
        this.addListener(Event.FACTORY_SET_QRCODE_URI, new OnQRCodeURISet());
        this.addListener(Event.FACTORY_SET_STAMP_CODE_URI, new OnStampCodeURISet());
        this.addListener(Event.FACTORY_SET_STAMP_SERRY_URI, new OnStampSeriURISet());

    }

    private void setFactoryEvent(){
        factory.on(StampFactoryEvent.TEMPLATE_LOADED, new StampFactoryActionListener() {
            public void handle(StampFactoryEvent e) {
                Event ev = new Event(Event.TEM_TEMPLATE_LOADED);
                ev.setData(e.getData());
                InTem.this.trigger(ev);
            }
        });

        factory.on(StampFactoryEvent.TEMPLATE_LOAD_FAILE, new StampFactoryActionListener() {
            public void handle(StampFactoryEvent e) {
                Event ev = new Event(Event.TEM_TEMPLATE_LOAD_FAILE);
                ev.setMessage(e.getMessage());
                InTem.this.trigger(ev);
            }
        });


        factory.on(StampFactoryEvent.DATA_LOADED, new StampFactoryActionListener() {
            public void handle(StampFactoryEvent e) {
                Event ev = new Event(Event.TEM_SERIAL_FILE_LOADED);
                ev.setMessage(e.getMessage());
                InTem.this.trigger(ev);
            }
        });

        factory.on(StampFactoryEvent.DATA_LOAD_FAILE, new StampFactoryActionListener() {
            public void handle(StampFactoryEvent e) {
                Event ev = new Event(Event.TEM_SERIAL_FILE_LOAD_FAILE);
                ev.setMessage(e.getMessage());
                InTem.this.trigger(ev);
            }
        });
    }

    private void setUp(){
        this.trigger(new Event(Event.APP_SETTING_UP,  "app is setting up, please wait..."));
        this.config.setUp();
        this.trigger(new Event(Event.APP_SETTING_COMPLETE,  "setting completed, your app is ready to run!"));
    }

    public static InTem app(){
        return instance;
    }

    private class AppSettingListener extends AbstractEventListener {
        public void handle(Event event) {
            InTem app = InTem.this;
            String message = event.message;
            app.blockUI(message, true);
        }
    }

    public static void main(String[] args){
        instance = new InTem();
        instance.run();
    }

    private class OnQRCodeDirSet extends AbstractEventListener{
        @Override
        public void handle(Event event) {
            factoryOption.put(StampFactoryOption.INPUT_QRCODE_DIR, event.getMessage());
        }
    }

    private class OnOutputDirSet extends AbstractEventListener{
        @Override
        public void handle(Event event) {
            factoryOption.put(StampFactoryOption.OUTPUT_LOCATION, event.getMessage());
        }
    }

    private class OnQRCodeURISet extends AbstractEventListener{
        @Override
        public void handle(Event event) {
            factoryOption.put(StampFactoryOption.QR_CODE_URI, event.getMessage());
        }
    }

    private class OnStampCodeURISet extends AbstractEventListener{
        @Override
        public void handle(Event event) {
            factoryOption.put(StampFactoryOption.STAMP_CODE_URI, event.getMessage());
        }
    }

    private class OnStampSeriURISet extends AbstractEventListener{
        @Override
        public void handle(Event event) {
            factoryOption.put(StampFactoryOption.SERI_URI, event.getMessage());
        }
    }

    private class OnAppReadyListener extends AbstractEventListener{

        public void handle(Event event) {
            input = new StampFactoryInput();
            ouput = new PDFStampCollection();
            factoryOption = input.getOptions();
            factory = new StampFactory(input, ouput);
            setFactoryEvent();
        }
    }

    private class OnPrintStartBtn extends AbstractEventListener{
        public void handle(Event event) {
            factory.render();
        }
    }

    private class OnTemplateSelected extends AbstractEventListener{
        public void handle(Event event) {
            input.setTemplate(event.getMessage());
        }
    }

    private class OnTemplatedLoaded extends AbstractEventListener{
        public void handle(Event event) {

        }
    }

    private class OnTemplateLoadFalie extends AbstractEventListener{
        public void handle(Event event) {
            gui.setStatus(event.getMessage());
        }
    }

    private class OnSerialFileSelected extends AbstractEventListener{
        public void handle(Event event) {
            input.setData(event.getMessage());
        }
    }

    private class OnSerialFileLoaded extends AbstractEventListener{
        public void handle(Event event) {
            gui.setStatus(event.getMessage());
        }
    }

    private class OnSerialFileLoadFaile extends AbstractEventListener{
        public void handle(Event event) {
            gui.setStatus(event.getMessage());
        }
    }
}
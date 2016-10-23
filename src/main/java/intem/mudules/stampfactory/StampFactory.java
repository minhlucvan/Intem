package intem.mudules.stampfactory;

import intem.mudules.stampfactory.factory.SVGStampFactory;
import intem.mudules.stampfactory.factory.StampData;
import intem.mudules.stampfactory.factory.StampDocument;
import intem.mudules.stampfactory.input.StampFactoryInput;
import intem.mudules.stampfactory.input.StampFactoryOption;
import intem.mudules.stampfactory.output.PDFStampCollection;
import intem.mudules.stampfactory.output.StampFactoryOutput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by minh on 27/09/16.
 */
public class StampFactory implements StampFactoryAPI {

    private StampFactoryInput input;
    private PDFStampCollection output;
    private SVGStampFactory factory;

    private HashMap<Integer, StampFactoryActionListener> eventHandlers;

    public StampFactory(StampFactoryInput i, PDFStampCollection o){
        eventHandlers = new HashMap<Integer, StampFactoryActionListener>();
        input = i;
        output = o;
        input.setFactory(this);
        factory = new SVGStampFactory();
    }

    public void setOption(StampFactoryOption option) {
        input.setOption(option);
    }

    public void setTemplate(StampDocument document) {
        input.setTemplate(document);
    }

    public void setData(StampData data) {
        input.setData(data);
    }

    public void setOutPutOption() {

    }

    public void render() {
        factory.setInput(input);
        ArrayList<StampData.Entity> stamps = input.getData().toArrayList();
        for ( StampData.Entity stamp: stamps) {
            StampDocument newStamp = factory.render(stamp);
            output.addStamp(newStamp);
            System.out.println("stamp done: " + stamps.indexOf(stamp));
        }
        output.save();
    }

    public void trigger(StampFactoryEvent e){
        StampFactoryActionListener handler = eventHandlers.get(e.getType());
        if(handler != null){
            handler.handle(e);
        }
    }

    public void on(int type, StampFactoryActionListener handler){
        eventHandlers.put(type, handler);
    }
}

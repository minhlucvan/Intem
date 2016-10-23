package intem.mudules.stampfactory.input;

import intem.models.Stamp;
import intem.mudules.stampfactory.StampFactory;
import intem.mudules.stampfactory.StampFactoryActionListener;
import intem.mudules.stampfactory.StampFactoryEvent;
import intem.mudules.stampfactory.factory.StampData;
import intem.mudules.stampfactory.factory.StampDocument;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by minh on 28/09/16.
 */
public class StampFactoryInput {
    private StampFactory factory;

    private StampFactoryOption options;
    private StampDocument document;
    private StampData data;

    public StampFactoryInput(){
        this(StampFactoryOption.getDefault());
    }

    public StampFactoryInput(StampFactoryOption opt){
        document = new StampDocument();
        data = new StampData();
        options = opt;
    }

    public void setTemplate(String path){
        try {
            // Parse the factory file into a Document.
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            URL url = new URL(path);
            Document domDoc = f.createDocument(url.toString());

            document.setDoc(domDoc);

            StampFactoryEvent e = new StampFactoryEvent(StampFactoryEvent.TEMPLATE_LOADED);
            e.setData(domDoc);
            e.setMessage(path);
            factory.trigger(e);

        } catch (Exception ex) {
            ex.printStackTrace();
            StampFactoryEvent e = new StampFactoryEvent(StampFactoryEvent.TEMPLATE_LOAD_FAILE);
            e.setData(path);
            e.setMessage("can not load template file");
            factory.trigger(e);
        }
    }

    public void setTemplate(StampDocument doc){
        document = doc;
    }

    public void setOption(StampFactoryOption opt){
        options = opt;
    }

    public void setData(StampData dat){
        data = dat;
        data.setFactory(factory);
    }

    public  void setData(String path){
        StampData dat = new StampData(path, factory);
        setData(dat);
    }

    public String getOption(int key){
      return options.get(key);
    }

    public int getOPtionInt(int key){
        return Integer.parseInt(getOption(key));
    }


    public ArrayList<StampDocument> getInputArray(){
        return null;
    }

    public StampFactoryOption getOptions() {
        return options;
    }

    public void setOptions(StampFactoryOption options) {
        this.options = options;
    }

    public StampDocument getDocument() {
        return document;
    }

    public void setDocument(StampDocument document) {
        this.document = document;
    }

    public StampData getData() {
        return data;
    }


    public StampFactory getFactory() {
        return factory;
    }

    public void setFactory(StampFactory factory) {
        this.factory = factory;
        this.data.setFactory(factory);
    }


}

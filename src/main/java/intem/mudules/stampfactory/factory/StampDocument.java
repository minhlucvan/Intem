package intem.mudules.stampfactory.factory;

import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.prism.BufferedImageTools;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by minh on 28/09/16.
 */
public class StampDocument {
    private Document doc;
    private double width;
    private double height;

    public StampDocument(){
        width = 0.0;
        height = 0.0;
    }

    public void setString(String uri, String text) {
        //TODO
    }

    public void setImage(String uri, String data) {
        //TODO
    }

    public Document getStamp() {
        return doc;
    }

    public void save(String path) {
        //TODO
    }

    public StampDocument render() {
        return this;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }


    public Element getElementByXpath(String xpath) {
        String path = xpath.replace("/svg@1/", "");
        String[] mods = path.split("/");

        Element root = doc.getDocumentElement();
        Element el = root;
        for ( String mod : mods ) {
            int at = mod.indexOf("@");
            String tag = mod.substring(0, at - 1);
            String index = mod.substring(at + 1, mod.length());

            NodeList nodes = el.getChildNodes();
            el = (Element) nodes.item(Integer.valueOf(index));
        }

        return el;
    }

    public void setAttribute(String xpath, String att, String val) {
        Element el = getElementByXpath(xpath);
        el.setAttribute(att, val);
    }

    public void setAttribute(String xpath, String att, byte[] val) {
        //TODO
    }

    public void setTextContent(String xpath, String val) {
        Element el = getElementByXpath(xpath);
        el.getChildNodes().item(0).setNodeValue(val);
    }
    private GraphicsNode getGraphicsRoot(){
        UserAgent agent = new UserAgentAdapter();
        DocumentLoader loader= new DocumentLoader(agent);
        BridgeContext context = new BridgeContext(agent, loader);
        context.setDynamic(true);
        GVTBuilder builder= new GVTBuilder();
        GraphicsNode root= builder.build(context, doc);
        return root;
    }
    public double getWidth(){
        if(width == 0.0){
            GraphicsNode root = getGraphicsRoot();
            width = root.getPrimitiveBounds().getWidth();
        }

        return width;
    }
    public double getHeight(){
        if(height == 0.0){
            GraphicsNode root = getGraphicsRoot();
            height = root.getPrimitiveBounds().getHeight();
        }

        return height;
    }

    public BufferedImage toBufferedImage(){
        float w = (float)getWidth();
        float h = (float)getHeight();
        return toBufferedImage(w, h);
    }

    public BufferedImage toBufferedImage(float width, float height) {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();

        TranscoderInput transcoderInput = new TranscoderInput(doc);
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

        PNGTranscoder pngTranscoder = new PNGTranscoder();
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 3*height);
        pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 3*width);

        try {
            pngTranscoder.transcode(transcoderInput, transcoderOutput);

            resultByteStream.flush();

            return ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));
        } catch (TranscoderException ex) {
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }


        return null;
    }

    public Document getDoc() {
        return doc;
    }
}

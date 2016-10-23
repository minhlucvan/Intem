package intem.mudules.stampfactory.factory;

import intem.mudules.stampfactory.StampFactory;
import intem.mudules.stampfactory.input.StampFactoryInput;
import intem.mudules.stampfactory.input.StampFactoryOption;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by minh on 27/09/16.
 */
public class SVGStampFactory {
    private String imageType;
    private StampFactoryInput input;
    private StampDocument tempalte;
    private String qrcodeDir;
    private Element seriEl;
    private Element qrEl;
    private Element codeEl;

    public SVGStampFactory(){
        qrcodeDir="";
        imageType = "png";
    }

    public StampDocument render(StampDocument doc, StampData.Entity ent, String qr){

        seriEl.getChildNodes().item(0).setNodeValue(ent.serial);
        codeEl.getChildNodes().item(0).setNodeValue(ent.code);
       // qrEl.removeAttribute("xlink:href");
        //qrEl.setAttributeNS("xlinkNS", "xlink:href", qr);
        String xlinkNS = "http://www.w3.org/1999.xlink";;
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        try {
            System.out.println(tempalte.getDoc().getAttributes().item(0));
        } catch ( Exception e ){
            e.printStackTrace();
        }

//        qrEl = tempalte.getDoc().createElementNS(svgNS, "image");
 //       qrEl.setAttributeNS(xlinkNS, "xlink:href", qr);
        return tempalte;
    }

    public StampDocument render(StampData.Entity ent){
        String qr = getQRcode(qrcodeDir+ "/" + ent.qrPath);
        return render(tempalte, ent, qr);
    }

    private BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public String getQRcode(String path){
        String rs = "";
        try {
            InputStream input = new FileInputStream(path);
            Image image = ImageIO.read(input);
            BufferedImage buff = toBufferedImage(image);
            rs = toBase64(buff, imageType);
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return "data:image/png;base64," + rs;
    }

    private String toBase64(BufferedImage image, String type){
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public StampFactoryInput getInput() {
        return input;
    }

    public void setInput(StampFactoryInput input) {
        this.input = input;
        tempalte = input.getDocument();
        qrcodeDir = input.getOption(StampFactoryOption.INPUT_QRCODE_DIR);
        String seriPath = input.getOption(StampFactoryOption.SERI_URI);
        String codePath = input.getOption(StampFactoryOption.STAMP_CODE_URI);
        String qrPath = input.getOption(StampFactoryOption.QR_CODE_URI);
        String imageTag = input.getOption(StampFactoryOption.IMAGE_TAG);

        seriEl = tempalte.getElementByXpath(seriPath);
        codeEl = tempalte.getElementByXpath(codePath);
        qrEl = tempalte.getElementByXpath(qrPath);;
    }

    public StampDocument getTempalte() {
        return tempalte;
    }

    public void setTempalte(StampDocument tempalte) {
        this.tempalte = tempalte;
    }

    public String getQrcodeDir() {
        return qrcodeDir;
    }

    public void setQrcodeDir(String qrcodeDir) {
        this.qrcodeDir = qrcodeDir;
    }
}

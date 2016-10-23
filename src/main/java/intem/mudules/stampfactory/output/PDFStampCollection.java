package intem.mudules.stampfactory.output;

import com.sun.prism.paint.Color;
import intem.mudules.stampfactory.factory.StampDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by minh on 27/09/16.
 */
public class PDFStampCollection {
    private static final double MM_SCALE = 0.352778;

    private PDDocument pdfDoc;
    private PDPage pdfPage;
    private int pageIndex;
    private int row;
    private int col;
    private int maxRow;
    private int maxCol;
    private int pointerX;
    private int pointerY;
    private int stamp_width;
    private int stamp_height;
    private float maxWidth;
    private float maxHeight;
    private String output;

    public PDFStampCollection() {
        pdfPage = new PDPage(PDRectangle.A4);
        this.pdfDoc = new PDDocument();
        maxRow = 9;
        maxCol = 2;
        output = "stamps.pdf";
        pointerX = 0;
        pointerY = 0;
        calcGrids();
    }

    private void calcGrids(){
        maxWidth = pdfPage.getMediaBox().getWidth();
        maxHeight = pdfPage.getMediaBox().getHeight();
        maxRow = (int)Math.floor(maxHeight/stamp_height)-1;
        maxCol = (int)Math.floor(maxWidth/stamp_width) - 1;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    private boolean needNewPage(){
        return (col >= maxCol && row >= maxRow);
    }

    private void addNewPage(){
        pdfDoc.addPage(pdfPage);
        pdfPage = new PDPage(PDRectangle.A4);
    }

    private void nextPointer(){
        if(col == maxCol && row == maxRow){
            row = 0;
            col = 0;
        } else if(col == maxCol){
            row++;
            col = 0;
        } else {
            col++;
        }
       // System.out.println("r: "+ row +"; c: "+col);
        pointerY = row*stamp_height;
        pointerX = col*stamp_width;
    }

    public void addStamp(StampDocument stamp){
        try{
            BufferedImage awtImage = stamp.toBufferedImage();
            stamp_width = awtImage.getWidth()/3;
            stamp_height = awtImage.getHeight()/3;
            calcGrids();

            PDImageXObject  pdImageXObject = LosslessFactory.createFromImage(pdfDoc, awtImage);
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdfPage, true, false);

            contentStream.drawImage(pdImageXObject, pointerX, maxHeight - pointerY , stamp_width , stamp_height);
//            System.out.println("w: " + stamp_width + "h: " +stamp_height);
//            contentStream.setStrokingColor(java.awt.Color.BLACK);
//            contentStream.addRect(pointerX, pointerY, stamp_width, stamp_height);
//            contentStream.closeAndStroke();
            contentStream.close();
            if(needNewPage()){
                addNewPage();
            }
            nextPointer();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void save() {
        try{
            pdfDoc.addPage(pdfPage);
            pdfDoc.save(output);
        } catch (IOException ex){
            ex.printStackTrace();;
        }
    }
}

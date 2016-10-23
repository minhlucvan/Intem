package intem.views;

import intem.*;
import intem.actions.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import intem.actions.Event;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.AbstractParentNode;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.util.DocumentDescriptor;
import org.apache.batik.dom.util.DocumentFactory;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.renderer.ImageRendererFactory;
import org.apache.batik.svggen.DefaultImageHandler;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.Base64EncoderStream;
import org.apache.batik.util.XMLConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.*;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.XMLReader;


/**
 * Created by minh on 25/09/16.
 */
public class FormInTem extends JPanel {
    private static final int SVG_WAIT_CLICK_STAMP_CODE = 161;
    private static final int SVG_WAIT_CLICK_SEIAL = 83;
    private static final int SVG_WAIT_CLICK_QRCODE = 454;

    private JSVGCanvas svg;
    private Document doc;
    private JLabel statusLabel;

    private JTextField codeInput;
    private JTextField seriInput;
    private JTextField qrInput;

    private boolean isWaitingSVGClick;
    private int actionForSVGClick;

    public FormInTem() {
        super();

        this.setLayout(new GridLayout(0, 2));
        this.setSize(800, 600);
        setContent();
        eventRegister();
    }

    private void setContent() {
        JPanel templateForm = getTemplateForm();
        JPanel factoryForm = getFactoryForm();

        this.add(templateForm);
        this.add(factoryForm);
    }


    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    private JSVGCanvas getSVGCancas() {

        JSVGCanvas svgCanvas = new JSVGCanvas();

        // jsvgCanvas.setSize(100, 100);
        svgCanvas.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));

        // Set the JSVGCanvas listeners.
        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_LOAD_START_KEY));
            }

            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_LOAD_DONE_KEY));
            }
        });

        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildStarted(GVTTreeBuilderEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_BUILD_SART_KEY));
            }

            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_BUILD_DONE_KEY));
            }
        });

        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_RENDER_SART_KEY));
                Context.app().trigger(Event.TEM_TEMPLATE_RENDER_START);
            }

            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                statusLabel.setText(Config.getOption(Config.UI_TEXT_DOCUMENT_RENDER_DONE_KEY));
                Context.app().trigger(Event.TEM_TEMPLATE_RENDER_DONE);
            }
        });


        return svgCanvas;
    }

    private JPanel getReviewPanel() {
        JPanel review = new JPanel();
        review.setLayout(new BorderLayout());
        review.setSize(400, 400);

        JLabel svgTitle = new JLabel("template: default");
        review.add(svgTitle, BorderLayout.PAGE_START);

        svg = getSVGCancas();
        review.add(svg, BorderLayout.CENTER);

        JButton fileBtn = new JButton(Config.getOption(Config.UI_TEXT_BROSWE_TEMPLATE_KEY));
        review.add(fileBtn, BorderLayout.PAGE_END);


        fileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser fc = new JFileChooser(".");
                int choice = fc.showOpenDialog(FormInTem.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    InTem.app().trigger(new intem.actions.Event(intem.actions.Event.TEM_TEMPLATE_SELECTED, f.toURI().toString()));
                }
            }
        });

        return review;
    }

    private JPanel getFormGroup(String title, JComponent content) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        container.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        JLabel tit = new JLabel(title);
        container.add(tit, BorderLayout.PAGE_START);

        container.add(content, BorderLayout.CENTER);

        return container;
    }

    private JButton getIconBtn(String path) {
        JButton button = new JButton();
        try {
            button.setIcon(new ImageIcon(path));
        } catch (Exception ex) {
            ex.printStackTrace();
            button.setText(Config.getOption(Config.UI_DEFAULT_BTN_TEXT_KEY));
        }
        return button;
    }

    private JPanel getTemplateSettingForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        panel.add(content, BorderLayout.CENTER);

        content.setLayout(new GridLayout(5, 0));

        JPanel codeInputGroup = new JPanel();
        codeInputGroup.setLayout(new BorderLayout());

        codeInput = new JTextField();
        codeInputGroup.add(codeInput, BorderLayout.CENTER);
        codeInput.setColumns(20);

        JButton codeSelect = getIconBtn(Config.getOption(Config.UI_INTERACT_BTN_ICON_KEY, ""));
        codeInputGroup.add(codeSelect, BorderLayout.LINE_END);

        content.add(getFormGroup(Config.getOption(Config.UI_TEXT_CODE_INPUT_TITLE_KEY), codeInputGroup));

        JPanel seriInputGroup = new JPanel();
        seriInputGroup.setLayout(new BorderLayout());

        seriInput = new JTextField();
        seriInputGroup.add(seriInput, BorderLayout.CENTER);
        seriInput.setColumns(20);

        JButton seriSelect = getIconBtn(Config.getOption(Config.UI_INTERACT_BTN_ICON_KEY, ""));
        seriInputGroup.add(seriSelect, BorderLayout.LINE_END);

        content.add(getFormGroup(Config.getOption(Config.UI_TEXT_SERI_INPUT_TITLE_KEY), seriInputGroup));

        JPanel qrInputGroup = new JPanel();
        qrInputGroup.setLayout(new BorderLayout());

        qrInput = new JTextField();
        qrInputGroup.add(qrInput, BorderLayout.CENTER);
        qrInput.setColumns(20);

        JButton qrSelect = getIconBtn(Config.getOption(Config.UI_INTERACT_BTN_ICON_KEY, ""));
        qrInputGroup.add(qrSelect, BorderLayout.LINE_END);

        content.add(getFormGroup(Config.getOption(Config.UI_TEXT_QR_INPUT_TITLE_KEY), qrInputGroup));

        codeSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isWaitingSVGClick){
                    endWaitForSVGClick();
                } else {
                    waitForSVGClick(SVG_WAIT_CLICK_STAMP_CODE);
                }
            }
        });

        seriSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isWaitingSVGClick){
                    endWaitForSVGClick();
                } else {
                    waitForSVGClick(SVG_WAIT_CLICK_SEIAL);
                }
            }
        });

        qrSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isWaitingSVGClick){
                    endWaitForSVGClick();
                } else {
                    waitForSVGClick(SVG_WAIT_CLICK_QRCODE);
                }
            }
        });

        return panel;
    }

    private void waitForSVGClick( int waitFor){
        isWaitingSVGClick = true;
        actionForSVGClick = waitFor;
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.setCursor(cursor);
        svg.setCursor(cursor);
    }

    private void endWaitForSVGClick(){
        isWaitingSVGClick = false;

        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        this.setCursor(cursor);
        svg.setCursor(cursor);
    }

    private JPanel getTemplateForm() {
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(2, 0));
        form.setSize(400, 300);
        form.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));

        JPanel review = getReviewPanel();
        form.add(review);

        JPanel teploption = getTemplateSettingForm();
        form.add(teploption);

        return form;
    }

    private JPanel getFactorySettingForm() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        container.add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(5, 0));

        JPanel seriBtnGroup = new JPanel();
        panel.add(seriBtnGroup);
        seriBtnGroup.setLayout(new BorderLayout());
        seriBtnGroup.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton seriBtn = new JButton(Config.getOption(Config.UI_TEXT_BROSWE_SERI_KEY));
        seriBtnGroup.add(seriBtn, BorderLayout.CENTER);

        JPanel qrBtnGroup = new JPanel();
        panel.add(qrBtnGroup);
        qrBtnGroup.setLayout(new BorderLayout());
        qrBtnGroup.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton qrBtn = new JButton(Config.getOption(Config.UI_TEXT_BROSE_QR_KEY));
        qrBtnGroup.add(qrBtn);

        String[] sizes =  {"A0", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10"};

        // create a combo box with the given vector
        JComboBox comboType = new JComboBox(sizes);
        comboType.setSelectedItem(Config.getOption(Config.OUTPUT_PDF_PAPER_SIZE_KEY));
        panel.add(getFormGroup(Config.getOption(Config.UI_TEXT_OUTPUT_PAPER_SIZE_KEY), comboType));

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new GridLayout(1, 2));
        panel.add(getFormGroup(Config.getOption(Config.UI_TEXT_STAMP_SIZE_KEY), sizePanel));

        JTextField widthInput = new JTextField(Config.getOption(Config.FACTORY_STAMP_WIDTH_DEFAULT_KEY));
        JPanel width = new JPanel();
        width.setLayout(new BorderLayout());

        width.add(new JLabel(Config.getOption(Config.UI_TEXT_STAMP_WIDTH_KEY)), BorderLayout.WEST);
        width.add(widthInput, BorderLayout.CENTER);
        sizePanel.add(width);

        JTextField heightInput = new JTextField(Config.getOption(Config.FACTORY_STAMP_HEIGHT_DEFAULT_KEY));
        JPanel height = new JPanel();
        height.setLayout(new BorderLayout());
        height.add(new JLabel(Config.getOption(Config.UI_TEXT_STAMP_HEIGHT_KEY)), BorderLayout.WEST);
        height.add(heightInput, BorderLayout.CENTER);

        sizePanel.add(height);

        seriBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fc = new JFileChooser(".");
                int choice = fc.showOpenDialog(FormInTem.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    InTem.app().trigger(new intem.actions.Event(intem.actions.Event.TEM_SERIAL_FILE_SELECTED, f.getAbsolutePath()));
                }
            }
        });

        qrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                chooser.setDialogTitle("Select QRCODE Folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                int choice = chooser.showOpenDialog(FormInTem.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    String dir = chooser.getSelectedFile().getPath();
                    Event evt = new Event(Event.FACTORY_SET_QRCODE_DIR);
                    evt.setMessage(dir);
                    Context.app().trigger(evt);
                }
            }
        });
        return container;
    }

    private JPanel getFactoryFormContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 0));

        JPanel factory = getFactorySettingForm();
        panel.add(factory);

        JPanel output = getOutPutFormContent();
        panel.add(output);

        return panel;
    }

    private JPanel getOutPutFormContent() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        container.add(panel);
        panel.setLayout(new GridLayout(4, 0));

        JPanel outputTYpeGroup = new JPanel();
        outputTYpeGroup.setLayout(new BorderLayout());

        ArrayList<String> languages = new ArrayList<String>();
        languages.add("PDF");
        languages.add("PNG");

        // create a combo box with the given vector
        JComboBox comboType = new JComboBox(languages.toArray());
        outputTYpeGroup.add(comboType, BorderLayout.CENTER);
        panel.add(getFormGroup(Config.getOption(Config.UI_TEXT_OUTPUT_TYPE_TITLE_KEY), outputTYpeGroup));

        JPanel ouputName = new JPanel();
        ouputName.setLayout(new BorderLayout());
        JTextField outputNameInput = new JTextField(Config.getOption(Config.OUTPUT_NAME_DEFAULT_KEY));
        ouputName.add(outputNameInput, BorderLayout.CENTER);
        panel.add(getFormGroup(Config.getOption(Config.UI_TEXT_OUTPUT_NAME_KEY), ouputName));

        JPanel ouputLocal = new JPanel();
        ouputLocal.setLayout(new BorderLayout());
        JButton ouputLocalBtn = new JButton(Config.getOption(Config.UI_TEXT_OUTPUT_LOCAL_BTN_KEY));
        ouputLocal.add(ouputLocalBtn, BorderLayout.CENTER);
        panel.add(getFormGroup(Config.getOption(Config.UI_TEXT_OUTPUT_LOCAL_TITLE_KEY), ouputLocal));

        JButton printBtn = new JButton(Config.getOption(Config.UI_TEXT_START_PRINT_BTN_KEY));
        panel.add(printBtn);

        printBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Context.app().trigger(new intem.actions.Event(intem.actions.Event.FACTORY_PRINT_START_PRINT, ""));
            }
        });

        ouputLocalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                chooser.setDialogTitle("Select Output Folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                int choice = chooser.showOpenDialog(FormInTem.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File dir = chooser.getCurrentDirectory();
                    Event evt = new Event(Event.FACTORY_SET_OUTPUT_DIR);
                    evt.setData(dir);
                    Context.app().trigger(evt);
                }
            }
        });

        return container;
    }

    private JPanel getFactoryForm() {
        JPanel form = new JPanel();
        form.setLayout(new BorderLayout());
        form.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

        JLabel title = new JLabel("Factory options");
        form.add(title, BorderLayout.PAGE_START);

        JPanel content = getFactoryFormContent();
        form.add(content, BorderLayout.CENTER);

        return form;
    }

    private void addDocEvent(Document doc) {
        final Element root = doc.getDocumentElement();

        String[] tags = {"text", "image"};
        for (String tag : tags) {
            NodeList nlPath = root.getElementsByTagName(tag);

            for (int i = nlPath.getLength() - 1; i >= 0; i--) {
                Element elem = (Element) nlPath.item(i);

                EventTarget target = (EventTarget) elem;
                target.addEventListener("click", new EventListener() {
                    public void handleEvent(org.w3c.dom.events.Event evt) {
                        if (evt.getType().equals("click") && isWaitingSVGClick) {
                           Element tar = (Element)evt.getTarget();
                            svgClickElement(getXPath(tar));
                        }
                    }
                }, false);
            }
        }

    }

    private void svgClickElement(String xpath){
        Event ev = new Event(Event.END_DEFAULT_EVENT);

        switch (actionForSVGClick){
            case SVG_WAIT_CLICK_STAMP_CODE:{
                codeInput.setText(xpath);
                ev = new Event(Event.FACTORY_SET_STAMP_CODE_URI, codeInput.getText());
                break;
            }
            case SVG_WAIT_CLICK_SEIAL: {
                seriInput.setText(xpath);
                ev = new Event(Event.FACTORY_SET_STAMP_SERRY_URI, seriInput.getText());
                break;
            }
            case SVG_WAIT_CLICK_QRCODE: {
                qrInput.setText(xpath);
                ev = new Event(Event.FACTORY_SET_QRCODE_URI, qrInput.getText());
                break;
            }
            default: break;
        }
        Context.app().trigger(ev);
        endWaitForSVGClick();
    }

    private int indexOfNode(Node node) {
        int index;
        Node sibling;

        index = 0;
        while ((sibling = node.getPreviousSibling()) != null) {
            node = sibling;
            ++index;
        }

        return index;
    }

    private String getXPath(Element node)
    {
        Node parent = node.getParentNode();
        if (node.getTagName().equals("svg")) {
            return "/" + node.getTagName()+"@1";
        }
        return getXPath((Element) parent) + "/" + node.getTagName() + "@"+ indexOfNode(node);
    }

    private void eventRegister() {
        Context.app().addListener(Event.TEM_TEMPLATE_LOADED, new AbstractEventListener() {
            public void handle(intem.actions.Event event) {
                Object data = event.getData();
                if (!(data instanceof Document)) return;

                Document doc = (Document) data;
                svg.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
                svg.setDocument(doc);
                addDocEvent(doc);
            }
        });
    }
}

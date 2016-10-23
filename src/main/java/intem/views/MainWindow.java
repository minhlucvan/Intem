package intem.views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by minh on 25/09/16.
 */
public class MainWindow extends Window {
    public MainWindow(String title){
        super(title);
        FormInTem content = new FormInTem();
        content.setStatusLabel(myStatusLabel);
        this.add(content, BorderLayout.CENTER);
    }
}

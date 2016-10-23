package intem.views;

import intem.Config;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by minh on 25/09/16.
 */
public class Window extends JFrame{
    private ImageIcon myIcon;
    protected JMenuBar myMenuBar;
    protected JPanel myStatusBar;
    protected JLabel myStatusLabel;

    Window(String title){
        super(title);
        myIcon = new ImageIcon(Config.getOption(Config.MAIN_WINDOW_ICON_KEY));

        this.setIconImage(myIcon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        this.setLayout(new BorderLayout());
        myMenuBar = getMyMenuBar();
        this.setJMenuBar(myMenuBar);

        myStatusBar = getStatusBar();
        this.add(myStatusBar, BorderLayout.PAGE_END);

        JPanel leftBorder = new JPanel();
        this.add(leftBorder, BorderLayout.WEST);
        leftBorder.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JPanel rightBorder = new JPanel();
        this.add(rightBorder, BorderLayout.EAST);
        leftBorder.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

    }

    private JMenuBar getMyMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        JMenuItem setting = new JMenuItem("Setting");
        file.add(setting);

        JMenuItem about = new JMenuItem("About");
        file.add(about);

        JMenu factory = new JMenu("Factory");
        menuBar.add(factory);

        JMenu templates = new JMenu("Template");
        menuBar.add(templates);

        JMenu history = new JMenu("History");
        menuBar.add(history);

        return menuBar;
    }
    private JPanel getStatusBar(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 6, 0));

        myStatusLabel = new JLabel(Config.getOption(Config.UI_TEXT_APP_READY_STATUS_KEY));
        panel.add(myStatusLabel, BorderLayout.CENTER);

        Font font1 = new Font("SansSerif", Font.PLAIN, 11);
        myStatusLabel.setFont(font1);

        return panel;
    }

    public void alert(String msg){
        JOptionPane.showMessageDialog(this,
                "Eggs are not supposed to be green." );
    }

    public void setStatus(String stt){
        myStatusLabel.setText(stt);
    }
}

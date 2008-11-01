/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import bezier.serializer.Serializer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Gussoh
 */
public class SerializerWindow extends JFrame implements StateChangeListener, WindowListener, KeyListener {

    private BezierPanel bp;
    private JTextArea t;
    private Serializer serializer;
    private Color defaultBackgroundColor;
    private JLabel statusPanel = new JLabel();

    public SerializerWindow(BezierPanel bp, Serializer serializer) {
        super("CPLot");
        this.bp = bp;
        this.serializer = serializer;
        bp.addChangeListener(this);
        t = new JTextArea();
        JScrollPane scp = new JScrollPane(t);

        defaultBackgroundColor = t.getBackground();
        setLayout(new BorderLayout());
        add(scp, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        addWindowListener(this);
        t.addKeyListener(this);
        try {
            t.setText(serializer.getData(false));
            t.setEditable(true);
            t.setBackground(defaultBackgroundColor);
            statusPanel.setText("");
        } catch (ParseException ex) {
            statusPanel.setText(ex.getMessage());
            t.setBackground(Color.PINK);
            t.setEditable(false);
        }
        t.setPreferredSize(new Dimension(300, 300));
        pack();
        setVisible(true);
    }

    public void stateChanged() {
        if (isVisible() && !(t.hasFocus() || hasFocus())) {
            try {
                t.setText(serializer.getData(false));
                t.setEditable(true);
                t.setBackground(defaultBackgroundColor);
                statusPanel.setText("");
            } catch (ParseException ex) {
                statusPanel.setText(ex.getMessage());
                t.setBackground(Color.PINK);
                t.setEditable(false);
            }
        }
    }

    public void windowOpened(WindowEvent e) {
        try {
            t.setText(serializer.getData(false));
            t.setEditable(true);
            t.setBackground(defaultBackgroundColor);
            statusPanel.setText("");
        } catch (ParseException ex) {
            statusPanel.setText(ex.getMessage());
            t.setBackground(Color.PINK);
            t.setEditable(false);
        }

    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if(!t.isEditable()) {
            return;
        }
        try {
            serializer.setData(t.getText(), true);
            t.setBackground(defaultBackgroundColor);
            bp.repaint();
            statusPanel.setText("");
        } catch (ParseException ex) {
            statusPanel.setText(ex.getMessage());
            t.setBackground(Color.PINK);
        }
    }
}

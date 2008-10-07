/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import bezier.serializer.CPlotSerializer;
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
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Gussoh
 */
public class CPlotWindow extends JFrame implements StateChangeListener, WindowListener, KeyListener {

    private BezierPanel bp;
    private JTextArea t;
    private CPlotSerializer cPlotSerializer;
    private Color defaultBackgroundColor;
    private JLabel statusPanel = new JLabel();

    public CPlotWindow(BezierPanel bp) {
        super("CPLot");
        this.bp = bp;
        cPlotSerializer = new CPlotSerializer(bp);
        bp.addChangeListener(this);
        t = new JTextArea();
        JScrollPane scp = new JScrollPane(t);

        defaultBackgroundColor = t.getBackground();
        setLayout(new BorderLayout());
        add(scp, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        addWindowListener(this);
        t.addKeyListener(this);
        t.setText(cPlotSerializer.getData());
        t.setPreferredSize(new Dimension(300, 300));
        pack();
        setVisible(true);
    }

    public void stateChanged() {
        if (isVisible() && !(t.hasFocus() || hasFocus())) {
            t.setText(cPlotSerializer.getData());
        }
    }

    public void windowOpened(WindowEvent e) {
        t.setText(cPlotSerializer.getData());
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
        try {
            cPlotSerializer.setData(t.getText(), true);
            t.setBackground(defaultBackgroundColor);
            bp.repaint();
            statusPanel.setText("");
        } catch (ParseException ex) {
            statusPanel.setText(ex.getMessage());
            t.setBackground(Color.PINK);
        }
    }
}

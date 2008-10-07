/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import bezier.curves.BezierCurve;
import bezier.curves.Curve;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Gussoh
 */
public class BezierPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ComponentListener {

    private boolean viewControlPolygon = true;
    private BezierView bezierView;
    private double snap = 3;
    private List<Curve> curves = new ArrayList<Curve>();
    private Point2D movingPoint = null;
    private Curve currentCurve = null;
    private Point2D proposedPoint = null;
    private Point2D hoverPoint = null;
    private List<StateChangeListener> changeListeners = new ArrayList<StateChangeListener>();
    private boolean viewControlPoints = true;
    private Point2D translate = new Point2D.Double(0, 0);
    private Point2D mouseLastDragPoint = new Point2D.Double(0, 0);
    private double scale = 1;

    public BezierPanel(BezierView bw) {
        bezierView = bw;
        setBackground(Color.BLACK);
        setOpaque(true);
        addMouseListener(bezierView);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        bw.getFrame().addKeyListener(this);
        updateTranslateAndScaleLabel();
        bezierView.getFrame().addComponentListener(this);
    }

    public void addChangeListener(StateChangeListener c) {
        changeListeners.add(c);
    }

    public void removeChangeListener(StateChangeListener c) {
        changeListeners.remove(c);
    }

    public void scaleAllCurves(double scale) {
        HashSet<Point2D> movedPoints = new HashSet<Point2D>();
        for (Curve curve : curves) {
            for (Point2D p : curve.getControlPoints()) {
                if (!movedPoints.contains(p)) {
                    p.setLocation(p.getX() * scale, p.getY() * scale);
                    movedPoints.add(p);
                }
            }
        }

        stateChanged();
        repaint();
    }

    public void translateAllCurves(double x, double y) {
        HashSet<Point2D> movedPoints = new HashSet<Point2D>();
        for (Curve curve : curves) {
            for (Point2D p : curve.getControlPoints()) {
                if (!movedPoints.contains(p)) {
                    p.setLocation(p.getX() + x, p.getY() + y);
                    movedPoints.add(p);
                }
            }
        }

        stateChanged();
        repaint();
    }

    private void stateChanged() {
        for (StateChangeListener changeListener : changeListeners) {
            changeListener.stateChanged();
        }
    }

    public BezierView getBezierView() {
        return bezierView;
    }

    public void setCurves(List<Curve> curves) {
        this.curves = curves;
        stateChanged();
        repaint();
    }

    public List<Curve> getCurves() {
        return curves;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.scale(scale, scale);
        g.translate(translate.getX(), translate.getY());


        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Stroke controlPointStroke = new BasicStroke((float) ((snap * 2) * (1 / scale)));
        Stroke controlPolygonStroke = new BasicStroke(1);
        Stroke curveStroke = new BasicStroke(2);
        Stroke curveStrokeSelected = new BasicStroke(3);
        Stroke coordinateSystemStroke = new BasicStroke(3);
        
        // coordinate system
        g.setStroke(coordinateSystemStroke);
        g.setColor(Color.WHITE);
        
        Line2D x = new Line2D.Double(0, scaleAndTranslatePoint(new Point2D.Double(0, 0)).getY(), 0, scaleAndTranslatePoint(new Point2D.Double(getWidth(), getHeight())).getY());
        g.draw(x);
        Line2D y = new Line2D.Double(scaleAndTranslatePoint(new Point2D.Double(0, 0)).getX(), 0, scaleAndTranslatePoint(new Point2D.Double(getWidth(), getHeight())).getX(), 0);
        g.draw(y);
        
        for (Curve curve : curves) {

            Point2D underHoverPoint = null;
            if (hoverPoint != null) {
                underHoverPoint = getClosestControlPoint(proposedPoint, snap, proposedPoint);
            }

            boolean hovered =
                    (hoverPoint != null && curve.getControlPoints().contains(hoverPoint)) ||
                    (underHoverPoint != null && curve.getControlPoints().contains(underHoverPoint));

            if (viewControlPolygon || hovered) {
                g.setColor(Color.GRAY);
                g.setStroke(controlPolygonStroke);
                curve.paintControlPolygon(g);
            }

            if (hovered) {
                g.setColor(Color.YELLOW);
                g.setStroke(curveStrokeSelected);
            } else {
                g.setColor(Color.GREEN);
                g.setStroke(curveStroke);
            }
            curve.paintCurve(g);

            if (viewControlPoints || hovered) {
                g.setColor(Color.CYAN);
                g.setStroke(controlPointStroke);
                curve.paintControlPoints(g);
            }
        }
    }

    public void zoomFit() {
        if (curves.isEmpty()) {
            scale = 1;
            translate =
                    new Point2D.Double(0, 0);
        } else {
            double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE, yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;

            for (Curve curve : curves) {
                for (Point2D point : curve.getControlPoints()) {
                    xMin = Math.min(point.getX(), xMin);
                    yMin =
                            Math.min(point.getY(), yMin);
                    xMax =
                            Math.max(point.getX(), xMax);
                    yMax =
                            Math.max(point.getY(), yMax);
                }

            }

            translate = new Point2D.Double(-xMin, -yMin);
            double diffX = xMax - xMin, diffY = yMax - yMin;

            scale =
                    (double) getWidth() / diffX;
            scale =
                    Math.min((double) getHeight() / diffY, scale);
        }

        updateTranslateAndScaleLabel();
        repaint();

    }

    public void resetView() {

        translate = new Point2D.Double(0, 0);
        scale =
                1;
        updateTranslateAndScaleLabel();

        repaint();

    }

    public void degreeElevation(Point2D point) {

        Point2D closest = getClosestControlPoint(point, snap);

        if (closest != null) {
            Curve c = null;

            for (Curve curve : curves) {
                for (Point2D controlPoint : curve.getControlPoints()) {
                    if (closest.equals(controlPoint)) {
                        c = curve;
                    }

                }
            }

            if (c != null && c instanceof BezierCurve) {
                ((BezierCurve) c).degreeElevation();
            }

        }
    }

    public void setViewControlPolygon(boolean selected) {
        viewControlPolygon = selected;
        repaint();

    }

    public void setViewControlPoints(boolean viewControlPoints) {
        this.viewControlPoints = viewControlPoints;
        repaint();

    }

    private Point2D getClosestControlPoint(Point2D point, double maxDistance, Point2D butNotThisOne) {
        Point2D closest = null;
        double distance = maxDistance / scale;
        for (Curve curve : curves) {
            for (Point2D controlPoint : curve.getControlPoints()) {
                if (point.distance(controlPoint) <= distance && controlPoint != butNotThisOne) {
                    closest = controlPoint;
                    distance =
                            point.distance(controlPoint);
                }

            }

        }

        return closest;
    }

    private Point2D getClosestControlPoint(Point2D point, double maxDistance) {
        Point2D closest = null;
        double distance = maxDistance / scale;
        for (Curve curve : curves) {
            for (Point2D controlPoint : curve.getControlPoints()) {
                if (point.distance(controlPoint) <= distance) {
                    closest = controlPoint;
                    distance =
                            point.distance(controlPoint);
                }

            }

        }

        return closest;
    }

    private void removeControlPoint(Point2D point) {
        if (point == null) {
            return;
        }

        Curve cs = null;

        for (Curve curve : curves) {
            for (Point2D controlPoint : curve.getControlPoints()) {
                if (point.equals(controlPoint)) {
                    cs = curve;
                }

            }

        }
        if (cs != null) {
            cs.removeControlPoint(point);
            if (!cs.isValid()) {
                curves.remove(cs);
            }

        }

        stateChanged();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            movingPoint = getClosestControlPoint(scaleAndTranslatePoint(e.getPoint()), snap);
            if (movingPoint == proposedPoint) {
                movingPoint = null;
            }

        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (currentCurve != null) {
                if (proposedPoint != null) {
                    removeControlPoint(proposedPoint);
                }

                currentCurve = null;
            } else {
                removeControlPoint(getClosestControlPoint(scaleAndTranslatePoint(e.getPoint()), snap));
            }

        }


        mouseLastDragPoint.setLocation(e.getPoint());

    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {

            if (!e.isAltDown()) {

                if (movingPoint == null || e.isControlDown()) {
                    if (currentCurve == null) {
                        // create new curve

                        if (hoverPoint != null && e.isControlDown()) {
                            currentCurve = new BezierCurve(hoverPoint);
                        } else {
                            currentCurve = new BezierCurve(scaleAndTranslatePoint(e.getPoint()));
                        }

                        curves.add(currentCurve);
                        proposedPoint =
                                scaleAndTranslatePoint(e.getPoint());
                        currentCurve.addControlPoint(proposedPoint);

                        stateChanged();

                    } else {
                        if (proposedPoint != null) {
                            proposedPoint = scaleAndTranslatePoint(e.getPoint());

                            Point2D underHoverPoint = getClosestControlPoint(proposedPoint, snap, hoverPoint);
                            if (underHoverPoint != null && e.isControlDown()) {
                                currentCurve.removeControlPoint(proposedPoint);
                                currentCurve.addControlPoint(underHoverPoint);
                                currentCurve.addControlPoint(proposedPoint);
                            } else {
                                currentCurve.addControlPoint(proposedPoint);
                            }

                            stateChanged();
                        }

                    }
                } else { // has been moving point

                    stateChanged();
                }

                movingPoint = null;
            }

        }

        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

        double mouseMovedX = (e.getPoint().getX() - mouseLastDragPoint.getX()) / scale;
        double mouseMovedY = (e.getPoint().getY() - mouseLastDragPoint.getY()) / scale;

        if (e.isAltDown()) { // TRANSLATE VIEWPORT

            translate.setLocation(
                    translate.getX() + mouseMovedX,
                    translate.getY() + mouseMovedY);

            updateTranslateAndScaleLabel();

        } else if (e.isShiftDown() && movingPoint != null) { // MOVE CURVE

            for (Curve curve : curves) {
                if (curve.getControlPoints().contains(movingPoint)) {
                    for (Point2D p : curve.getControlPoints()) {
                        p.setLocation(p.getX() + mouseMovedX, p.getY() + mouseMovedY);
                    }

                }
            }
        } else if (movingPoint != null) { // DRAW NEW CURVE

            movingPoint.setLocation(scaleAndTranslatePoint(e.getPoint()));
        }

        mouseLastDragPoint = e.getPoint();
        repaint();

    }

    public void mouseMoved(MouseEvent e) {
        if (proposedPoint != null) {
            proposedPoint.setLocation(scaleAndTranslatePoint(e.getPoint()));
        }

        hoverPoint = getClosestControlPoint(scaleAndTranslatePoint(e.getPoint()), snap);
        if (hoverPoint == null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.isShiftDown()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        if (hoverPoint != null) {

            bezierView.hoverPointPositionLabel.setText("X: " + nf.format(hoverPoint.getX()) + ", Y: " + nf.format(hoverPoint.getY()));
        } else {
            bezierView.hoverPointPositionLabel.setText("(X: " + nf.format(scaleAndTranslatePoint(e.getPoint()).getX()) + ", Y: " + nf.format(scaleAndTranslatePoint(e.getPoint()).getY()) + ")");
        }

        repaint();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isAltDown()) {
            // Very strange bugs appears when zooming and translating at the same time.
            return;
        }

        double scaleDiff = (double) Math.abs(e.getWheelRotation()) * (1.0 - .04 * Math.signum(e.getWheelRotation()));
        scale *=
                scaleDiff;
        scaleDiff -=
                1;
        translate.setLocation(
                translate.getX() - e.getPoint().getX() * scaleDiff / scale,
                translate.getY() - e.getPoint().getY() * scaleDiff / scale);
        updateTranslateAndScaleLabel();

        repaint();

    }

    private Point2D scaleAndTranslatePoint(Point2D point) {
        return new Point2D.Double(point.getX() / scale - translate.getX(), point.getY() / scale - translate.getY());
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ALT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT && hoverPoint != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ALT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    public void updateTranslateAndScaleLabel() {
        NumberFormat percentNf = NumberFormat.getPercentInstance();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        bezierView.translateAndScaleLabel.setText("Scale: " + percentNf.format(scale) + ", X: " + nf.format(translate.getX()) + ", Y: " + nf.format(translate.getY()));
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        Point2D areaSize = scaleAndTranslatePoint(new Point2D.Double(getWidth(), getHeight()));
        translate.setLocation(areaSize.getX() * .05, areaSize.getY() * .95);
        updateTranslateAndScaleLabel();
    }

    public void componentHidden(ComponentEvent e) {
    }
}

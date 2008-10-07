/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier.curves;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gussoh
 */
public class BezierCurve extends Curve {

    public BezierCurve(Point2D startPoint) {
        getControlPoints().add(startPoint);
    }

    @Override
    public void paintCurve(Graphics2D g, int quality) {
        paintAdaptiveRendering(getControlPoints(), g, quality);
    }

    private static double getMaxHeightByQuality(int quality) {
        if(quality == LOW_QUALITY) {
            return 10;
        } else if(quality == MEDIUM_QUALITY) {
            return 5;
        } else {
            return 0.1;
        }
    }
    
    private static double getHeight(List<Point2D> curve) {
        if (curve.size() <= 2) {
            return 0;
        }
        Iterator<Point2D> i = curve.iterator();
        Point2D startPoint = i.next();
        Point2D endPoint = curve.get(curve.size() - 1);
        Line2D l = new Line2D.Double(startPoint, endPoint);

        double maxDistance = 0;
        while (i.hasNext()) {
            Point2D point = i.next();
            double distance = l.ptLineDist(point);
            maxDistance = Math.max(distance, maxDistance);
        }

        return maxDistance;
    }

    public static LinkedList<Point2D> splitLeft(List<Point2D> points) {
        if (points.size() == 1) {
            return new LinkedList<Point2D>(points);
        } else {
            List<Point2D> l = new LinkedList<Point2D>();
            Iterator<Point2D> i = points.iterator();
            Point2D p1, p2;
            p1 = i.next();
            do {
                p2 = i.next();
                //dataräkninar!!
                Point2D n = new Point2D.Double(((p2.getX() - p1.getX()) / 2) + p1.getX(), ((p2.getY() - p1.getY()) / 2) + p1.getY());
                l.add(n);
                p1 = p2;
            } while (i.hasNext());

            LinkedList<Point2D> returned = splitLeft(l);
            returned.addFirst(points.get(0));
            return returned;
        }
    }

    public static LinkedList<Point2D> splitRight(List<Point2D> points) {
        if (points.size() == 1) {
            return new LinkedList<Point2D>(points);
        } else {
            LinkedList<Point2D> l = new LinkedList<Point2D>();
            Iterator<Point2D> i = points.iterator();
            Point2D p1, p2;
            p1 = i.next();
            do {
                p2 = i.next();
                //dataräkninar!!
                Point2D n = new Point2D.Double(((p2.getX() - p1.getX()) / 2) + p1.getX(), ((p2.getY() - p1.getY()) / 2) + p1.getY());
                l.add(n);
                p1 = p2;
            } while (i.hasNext());

            LinkedList<Point2D> returned = splitRight(l);
            returned.addLast(points.get(points.size() - 1));
            return returned;
        }
    }

    public static void paintAdaptiveRendering(List<Point2D> curve, Graphics2D g, int quality) {
        if (getHeight(curve) > getMaxHeightByQuality(quality)) {
            paintAdaptiveRendering(splitLeft(curve), g, quality);
            paintAdaptiveRendering(splitRight(curve), g, quality);
        } else {
            Line2D l = new Line2D.Double(curve.get(0), curve.get(curve.size() - 1));
            g.draw(l);
        }
    }

    @Override
    public void addControlPoint(Point2D point) {
        getControlPoints().add(point);
    }

    @Override
    public void removeControlPoint(Point2D point) {
        if(getControlPoints().contains(point)) {
            if(getControlPoints().size() >= 2) {
                getControlPoints().remove(point);
            }
        }
    }

    @Override
    public boolean isValid() {
        if(getControlPoints().size() <= 1) {
            return false;
        }
        
        return true;
    }
    
    public void degreeElevation() {
        List<Point2D> cp = getControlPoints();
        int n = cp.size() - 1;
        ArrayList<Point2D> ncp = new ArrayList<Point2D>(n + 2);
        
        // First is the same
        ncp.add(cp.get(0));
        
        for (int i = 1; i < n + 1 /* dont go through the last one */; i++) {
            Point2D newPoint = new Point2D.Double(
                    ((double) i / (n + 1)) * cp.get(i - 1).getX() + (1 - ((double) i / (n + 1))) * cp.get(i).getX(),
                    ((double) i / (n + 1)) * cp.get(i - 1).getY() + (1 - ((double) i / (n + 1))) * cp.get(i).getY());
            ncp.add(newPoint);
        }
        
        // last is the same
        ncp.add(cp.get(n));
        
        setControlPoints(ncp);
    }
}

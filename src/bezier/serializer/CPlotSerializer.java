/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier.serializer;

import bezier.BezierPanel;
import bezier.curves.BezierCurve;
import bezier.curves.Curve;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Gussoh
 */
public class CPlotSerializer implements Serializer {

    private BezierPanel bp;

    public CPlotSerializer(BezierPanel bp) {
        this.bp = bp;
    }

    public String getData() {
        List<Curve> curves = bp.getCurves();

        // find largets positions
        double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE, yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;

        if (curves.isEmpty()) {
            xMin = 0.0;
            xMax = 1.0;
            yMin = 0.0;
            yMax = 1.0;
        } else {
            for (Curve curve : curves) {
                for (Point2D point : curve.getControlPoints()) {
                    xMin = Math.min(point.getX(), xMin);
                    yMin = Math.min(point.getY(), yMin);
                    xMax = Math.max(point.getX(), xMax);
                    yMax = Math.max(point.getY(), yMax);
                }
            }

            // flip coordinate system
            for (Curve curve : curves) {
                for (Point2D point : curve.getControlPoints()) {
                }
            }
        }

        int diff = (int) Math.ceil(Math.max(xMax - xMin, yMax - yMin));

        xMax = xMin + diff;
        yMax = yMin + diff;

        StringBuilder sb = new StringBuilder();
        sb.append("WIND\n");
        sb.append(xMin).append('\t').append(xMax).append('\t').append(-yMax).append('\t').append(-yMin).append("\n\n");

        sb.append("VIEW\n");
        sb.append(0).append('\t').append(1).append('\t').append(0).append('\t').append(1).append("\n\n");

        int counter = 0;
        for (Curve curve : curves) {
            sb.append("STOR\n");
            sb.append(counter).append("\n");
            sb.append(curve.getControlPoints().size() - 1).append("\n");
            for (Point2D point : curve.getControlPoints()) {
                sb.append(point.getX()).append('\t').append(-point.getY()).append('\t').append(0).append('\t').append(1).append("\n");
            }
            sb.append("CPLO\n");
            sb.append(counter++).append("\n\n");
        }

        sb.append("EXIT\n");
        return sb.toString();
    }

    public void setData(String data, boolean supressWarnings) throws ParseException {
        StringTokenizer st = new StringTokenizer(data);
        TreeMap<Integer, Curve> curves = new TreeMap<Integer, Curve>();
        HashMap<Point2D, Point2D> points = new HashMap<Point2D, Point2D>();

        boolean weightWarningShowed = false;
        boolean zWarningShowed = false;
        String zValue = null;
        String weightWarningMessage = "Weight was not 1. Rational bezier curves not supported. ";
        String zWarningMessage = "Different Z values. 3D bezier curves not supported.";

        String warnings = "";
        try {
            while (st.hasMoreTokens()) {
                String command = st.nextToken();
                if (command.equalsIgnoreCase("STOR")) {
                    int id = Integer.parseInt(st.nextToken());
                    int degree = Integer.parseInt(st.nextToken());

                    Curve c = null;

                    Point2D temp = new Point2D.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
                    if (points.containsKey(temp)) {
                        c = new BezierCurve(points.get(temp));
                    } else {
                        c = new BezierCurve(temp);
                        points.put(temp, temp);
                    }

                    curves.put(id, c);
                    zValue = st.nextToken(); // z-variable

                    if (!st.nextToken().equals("1") && !weightWarningShowed) {
                        weightWarningShowed = true;
                        if (supressWarnings) {
                            warnings += weightWarningMessage;
                        } else {
                            JOptionPane.showMessageDialog(bp, weightWarningMessage, "Parse warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    for (int i = 0; i < degree; i++) {
                        
                        temp = new Point2D.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
                        if (points.containsKey(temp)) {
                            c.addControlPoint(points.get(temp));
                        } else {
                            c.addControlPoint(temp);
                            points.put(temp, temp);
                        }

                        String newZ = st.nextToken();
                        if (!newZ.equals(zValue) && !zWarningShowed) {
                            zWarningShowed = true;
                            if (supressWarnings) {
                                warnings += zWarningMessage;
                            } else {
                                JOptionPane.showMessageDialog(bp, zWarningMessage, "Parse warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        // weight
                        if (!st.nextToken().equals("1") && !weightWarningShowed) {
                            weightWarningShowed = true;
                            if (supressWarnings) {
                                warnings += weightWarningMessage;
                            } else {
                                JOptionPane.showMessageDialog(bp, weightWarningMessage, "Parse warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                }
            }

            // find largets positions - to flip the coordinate system
            double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE, yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;

            if (curves.isEmpty()) {
                xMin = 0.0;
                xMax = 1.0;
                yMin = 0.0;
                yMax = 1.0;
            } else {
                for (Curve curve : curves.values()) {
                    for (Point2D point : curve.getControlPoints()) {
                        xMin = Math.min(point.getX(), xMin);
                        yMin = Math.min(point.getY(), yMin);
                        xMax = Math.max(point.getX(), xMax);
                        yMax = Math.max(point.getY(), yMax);
                    }
                }
            }

            int max = (int) Math.ceil(Math.max(xMax, yMax));
            int min = (int) Math.floor(Math.min(xMin, yMin));

            // flip it!
            HashSet<Point2D> movedPoints = new HashSet<Point2D>();
            for (Curve curve : curves.values()) {
                for (Point2D p : curve.getControlPoints()) {
                    if (!movedPoints.contains(p)) {
                        p.setLocation(p.getX(), -p.getY());
                        movedPoints.add(p);
                    }
                }
            }

            bp.setCurves(new ArrayList<Curve>(curves.values()));
            bp.repaint();
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Unexpected end of file", data.length());
        } catch (NumberFormatException e) {
            throw new ParseException("Could not parse number: " + e, 0);
        }
        if (warnings.length() != 0) {
            throw new ParseException(warnings, 0);
        }
    }

    public String getName() {
        return "CPlot";
    }
}

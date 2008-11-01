/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier.serializer;

import bezier.BezierPanel;
import bezier.curves.BSplineCurve;
import bezier.curves.BezierCurve;
import bezier.curves.Curve;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Gussoh
 */
public class JianminSerializer implements Serializer {

    private BezierPanel bp;

    public JianminSerializer(BezierPanel bp) {
        this.bp = bp;
    }

    public String getData(boolean convert) throws ParseException {
        List<BSplineCurve> curves = new ArrayList<BSplineCurve>(bp.getCurves().size());
        for (Curve curve : bp.getCurves()) {
            if (!(curve instanceof BSplineCurve)) {
                if (!convert) {
                    throw new ParseException("Other curves than B-Spline curves is not supported by Jianmin format", 0);
                } else if (curve instanceof BezierCurve) {
                    curves.add(new BSplineCurve((BezierCurve) curve));
                } else {
                    throw new ParseException("Could not convert curve to B-Spline curve(s)", 0);
                }
            } else {
                curves.add((BSplineCurve) curve);
            }
        }


        StringBuilder sb = new StringBuilder();
        for (BSplineCurve curve : curves) {
            sb.append(curve.getDegree()).append('\n');
            sb.append(curve.getControlPoints().size()).append('\n');
            for (Double knot : curve.getKnots()) {
                sb.append(knot).append(' ');
            }
            sb.append('\n');
            for (Point2D point2D : curve.getControlPoints()) {
                sb.append(point2D.getX()).append(' ').append(-point2D.getY()).append('\n');
            }
            sb.append('\n').append('\n');
        }

        return sb.toString();
    }

    public void setData(String data, boolean supressWarnings) throws ParseException {
        List<Curve> curves = new ArrayList<Curve>();
        Scanner s = new Scanner(data);
        try {
            while (s.hasNext()) {
                int degree = s.nextInt();
                int nControlPoints = s.nextInt();
                List<Double> knots = new ArrayList<Double>();
                for (int i = 0; i < degree + nControlPoints + 1; i++) {
                    knots.add(Double.parseDouble(s.next()));
                }

                List<Point2D> cps = new ArrayList<Point2D>();
                for (int i = 0; i < nControlPoints; i++) {
                    cps.add(new Point2D.Double(Double.parseDouble(s.next()), -Double.parseDouble(s.next())));
                }

                curves.add(new BSplineCurve(cps, knots, degree));
            }
            
            bp.setCurves(curves);
        } catch (Exception e) {
            e.printStackTrace();
            if(supressWarnings) {
                throw new ParseException(e.toString(), 0);
            } else {
                JOptionPane.showMessageDialog(bp, "Could not parse: " + e.getMessage(), "Error parsing", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getName() {
        return "Jianmin B-Spline";
    }

    public String getFileExtension() {
        return "txt";
    }
}

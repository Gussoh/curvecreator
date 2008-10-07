/*
 * ImageExportDialog.java
 *
 * Created on den 7 oktober 2008, 19:23
 */
package bezier;

import bezier.curves.Curve;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author  Andreas
 */
public class ImageExportDialog extends javax.swing.JDialog {

    private BufferedImage previewImage = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
    
    private List<Curve> curves;
    private Color backgroundColor = Color.BLACK,  curveColor = Color.GREEN.brighter();
    private Color fieldBackgroundColor = new JTextField().getBackground();
    private float strokeWidth = 2.0f;
    private float strokeSliderMultiplier = 0.1f;
    private int imageWidth;
    private int imageHeight;
    private JPanel imagePanel = new JPanel() {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.clearRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            drawToImage(previewImage, Curve.HIGH_QUALITY);
            g.drawImage(previewImage, 0, 0, null);
        }
    };

    /** Creates new form ImageExportDialog */
    public ImageExportDialog(java.awt.Frame parent, boolean modal, List<Curve> curves) {
        super(parent, modal);
        initComponents();
        this.curves = curves;
        imagePanel.setPreferredSize(new Dimension(200, 200));
        previewPanel.add(imagePanel);
        imageWidth = Integer.parseInt(imageWidthField.getText());
        imageHeight = Integer.parseInt(imageHeightField.getText());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void createPreviewImage() {
        int maxValue = Math.max(imageHeight, imageWidth);
        previewImage = new BufferedImage(
                (int) Math.max((((float) imageWidth / maxValue) * 200), 1),
                (int) Math.max((((float) imageHeight / maxValue) * 200), 1),
                BufferedImage.TYPE_4BYTE_ABGR);
    }

    private void drawToImage(BufferedImage image, int quality) {
        Graphics2D g2 = image.createGraphics();
        if (antiAliasingCheckBox1.isSelected()) {
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        }
        g2.setBackground(backgroundColor);
        g2.clearRect(0, 0, image.getWidth(), image.getHeight());
        Rectangle2D rect = getBoundingRect();
        double scale = Math.min(image.getWidth() / rect.getWidth(), image.getHeight() / rect.getHeight());
        g2.scale(scale, scale);
        g2.translate(-rect.getX(), -rect.getY());

        g2.setColor(curveColor);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Curve curve : curves) {
            curve.paintCurve(g2, quality);
        }
    }

    private Rectangle2D.Double getBoundingRect() {
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
        }
        return new Rectangle2D.Double(xMin - strokeWidth, yMin - strokeWidth, xMax - xMin + strokeWidth, yMax - yMin + strokeWidth);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        previewPanel = new javax.swing.JPanel();
        changeCurveColorButton = new javax.swing.JButton();
        changeBackgroundColorButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        imageWidthField = new javax.swing.JTextField();
        imageHeightField = new javax.swing.JTextField();
        strokeSlider = new javax.swing.JSlider();
        strokeWidthLabel = new javax.swing.JLabel();
        antiAliasingCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(bezier.BezierApp.class).getContext().getResourceMap(ImageExportDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        previewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("previewPanel.border.title"))); // NOI18N
        previewPanel.setName("previewPanel"); // NOI18N

        changeCurveColorButton.setText(resourceMap.getString("changeCurveColorButton.text")); // NOI18N
        changeCurveColorButton.setName("changeCurveColorButton"); // NOI18N
        changeCurveColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeCurveColorButtonActionPerformed(evt);
            }
        });

        changeBackgroundColorButton.setText(resourceMap.getString("changeBackgroundColorButton.text")); // NOI18N
        changeBackgroundColorButton.setName("changeBackgroundColorButton"); // NOI18N
        changeBackgroundColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeBackgroundColorButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        imageWidthField.setText(resourceMap.getString("imageWidthField.text")); // NOI18N
        imageWidthField.setName("imageWidthField"); // NOI18N
        imageWidthField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                imageWidthFieldKeyReleased(evt);
            }
        });

        imageHeightField.setText(resourceMap.getString("imageHeightField.text")); // NOI18N
        imageHeightField.setName("imageHeightField"); // NOI18N
        imageHeightField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                imageHeightFieldKeyReleased(evt);
            }
        });

        strokeSlider.setMaximum(1000);
        strokeSlider.setMinimum(1);
        strokeSlider.setName("strokeSlider"); // NOI18N
        strokeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                strokeSliderStateChanged(evt);
            }
        });

        strokeWidthLabel.setText(resourceMap.getString("strokeWidthLabel.text")); // NOI18N
        strokeWidthLabel.setName("strokeWidthLabel"); // NOI18N

        antiAliasingCheckBox1.setSelected(true);
        antiAliasingCheckBox1.setText(resourceMap.getString("antiAliasingCheckBox1.text")); // NOI18N
        antiAliasingCheckBox1.setName("antiAliasingCheckBox1"); // NOI18N
        antiAliasingCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antiAliasingCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(strokeWidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(changeBackgroundColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                    .addComponent(imageWidthField)
                                    .addComponent(imageHeightField)
                                    .addComponent(antiAliasingCheckBox1)))
                            .addComponent(strokeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(changeCurveColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(changeCurveColorButton)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(changeBackgroundColorButton)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(strokeWidthLabel))
                            .addComponent(strokeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(imageWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(imageHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(antiAliasingCheckBox1)
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(cancelButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
    fileChooser.showSaveDialog(this);
    File f = fileChooser.getSelectedFile();
    if (!f.getName().toLowerCase().endsWith(".png")) {
        f = new File(f.getAbsolutePath() + ".png");
    }

    BufferedImage finalImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
    drawToImage(finalImage, Curve.EXPORT_QUALITY);
    try {
        ImageIO.write(finalImage, "png", f);//GEN-LAST:event_saveButtonActionPerformed
        dispose();
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, ex);
    }
}

private void changeCurveColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeCurveColorButtonActionPerformed
    JColorChooser chooser = new JColorChooser(curveColor);
    Color color = chooser.showDialog(this, "Choose curve color", curveColor);
    if (color != null) {
        curveColor = color;
        imagePanel.repaint();
    }
}//GEN-LAST:event_changeCurveColorButtonActionPerformed

private void changeBackgroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeBackgroundColorButtonActionPerformed
    JColorChooser chooser = new JColorChooser(backgroundColor);
    Color color = chooser.showDialog(this, "Choose curve color", backgroundColor);
    if (color != null) {
        backgroundColor = color;
        imagePanel.repaint();
    }
}//GEN-LAST:event_changeBackgroundColorButtonActionPerformed

private void strokeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_strokeSliderStateChanged
    strokeWidth = strokeSlider.getValue() * strokeSliderMultiplier;
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(1);
    nf.setMinimumFractionDigits(1);
    strokeWidthLabel.setText(nf.format(strokeWidth));
    imagePanel.repaint();
}//GEN-LAST:event_strokeSliderStateChanged

private void imageHeightFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageHeightFieldKeyReleased
    try {
        int value = Integer.parseInt(imageHeightField.getText());
        if (value < 1 || value > 20000) {
            imageHeightField.setBackground(Color.PINK);
        } else {
            imageHeightField.setBackground(fieldBackgroundColor);
            imageHeight = value;
            createPreviewImage();
            repaint();
        }

    } catch (NumberFormatException ex) {
        imageHeightField.setBackground(Color.PINK);
    }
}//GEN-LAST:event_imageHeightFieldKeyReleased

private void imageWidthFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageWidthFieldKeyReleased
    try {
        int value = Integer.parseInt(imageWidthField.getText());
        if (value < 16 || value > 20000) {
            imageWidthField.setBackground(Color.PINK);
        } else {
            imageWidthField.setBackground(fieldBackgroundColor);
            imageWidth = value;
            createPreviewImage();
            repaint();
        }
    } catch (NumberFormatException ex) {
        imageWidthField.setBackground(Color.PINK);
    }
}//GEN-LAST:event_imageWidthFieldKeyReleased

private void antiAliasingCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_antiAliasingCheckBox1ActionPerformed
    imagePanel.repaint();
}//GEN-LAST:event_antiAliasingCheckBox1ActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    dispose();
}//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox antiAliasingCheckBox1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton changeBackgroundColorButton;
    private javax.swing.JButton changeCurveColorButton;
    private javax.swing.JTextField imageHeightField;
    private javax.swing.JTextField imageWidthField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JSlider strokeSlider;
    private javax.swing.JLabel strokeWidthLabel;
    // End of variables declaration//GEN-END:variables
}

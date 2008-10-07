/*
 * BezierView.java
 */
package bezier;

import bezier.curves.Curve;
import bezier.serializer.CPlotSerializer;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.ButtonGroup;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The application's main frame.
 */
public class BezierView extends FrameView implements MouseListener, StateChangeListener {

    private boolean degreeElevation = false;
    private boolean splitCurve = false;
    private BezierPanel bezierPanel;
    private boolean saved = true;
    private CPlotWindow cPlotWindow;

    public BezierView(SingleFrameApplication app) {
        super(app);

        initComponents();

        getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                translateAndScaleLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    translateAndScaleLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(lowQualityRadioButtonMenuItem);
        bg.add(mediumQualityRadioButtonMenuItem);
        bg.add(highQualityRadioButtonMenuItem);

        mainPanel.setLayout(new BorderLayout());
        bezierPanel = new BezierPanel(this);
        mainPanel.add(bezierPanel);
        bezierPanel.addChangeListener(this);

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = BezierApp.getApplication().getMainFrame();
            aboutBox = new BezierAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        BezierApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        degreeElevationMenuItem = new javax.swing.JMenuItem();
        splitCurveMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        scaleCurvesMenuItem = new javax.swing.JMenuItem();
        translateCurvesMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewControlPolyconCheckboxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewControlPointsCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewCoordinateAxelsMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        zoomFitMenuItem = new javax.swing.JMenuItem();
        resetViewMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        cplotMenuItem = new javax.swing.JCheckBoxMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        lowQualityRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        mediumQualityRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        highQualityRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        curveInfoMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        translateAndScaleLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        statusMessageLabel = new javax.swing.JLabel();
        hoverPointPositionLabel = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1161, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(bezier.BezierApp.class).getContext().getResourceMap(BezierView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText(resourceMap.getString("newMenuItem.text")); // NOI18N
        newMenuItem.setName("newMenuItem"); // NOI18N
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(bezier.BezierApp.class).getContext().getActionMap(BezierView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        degreeElevationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        degreeElevationMenuItem.setText(resourceMap.getString("degreeElevationMenuItem.text")); // NOI18N
        degreeElevationMenuItem.setName("degreeElevationMenuItem"); // NOI18N
        degreeElevationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                degreeElevationMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(degreeElevationMenuItem);

        splitCurveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        splitCurveMenuItem.setText(resourceMap.getString("splitCurveMenuItem.text")); // NOI18N
        splitCurveMenuItem.setName("splitCurveMenuItem"); // NOI18N
        splitCurveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                splitCurveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(splitCurveMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jMenu1.add(jSeparator3);

        scaleCurvesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        scaleCurvesMenuItem.setText(resourceMap.getString("scaleCurvesMenuItem.text")); // NOI18N
        scaleCurvesMenuItem.setName("scaleCurvesMenuItem"); // NOI18N
        scaleCurvesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleCurvesMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(scaleCurvesMenuItem);

        translateCurvesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        translateCurvesMenuItem.setText(resourceMap.getString("translateCurvesMenuItem.text")); // NOI18N
        translateCurvesMenuItem.setName("translateCurvesMenuItem"); // NOI18N
        translateCurvesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                translateCurvesMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(translateCurvesMenuItem);

        menuBar.add(jMenu1);

        viewMenu.setText(resourceMap.getString("viewMenu.text")); // NOI18N
        viewMenu.setName("viewMenu"); // NOI18N
        viewMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                viewMenuMenuSelected(evt);
            }
        });

        viewControlPolyconCheckboxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        viewControlPolyconCheckboxMenuItem.setSelected(true);
        viewControlPolyconCheckboxMenuItem.setText(resourceMap.getString("viewControlPolyconCheckboxMenuItem.text")); // NOI18N
        viewControlPolyconCheckboxMenuItem.setName("viewControlPolyconCheckboxMenuItem"); // NOI18N
        viewControlPolyconCheckboxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewControlPolygonCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewControlPolyconCheckboxMenuItem);

        viewControlPointsCheckBoxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        viewControlPointsCheckBoxMenuItem.setSelected(true);
        viewControlPointsCheckBoxMenuItem.setText(resourceMap.getString("viewControlPointsCheckBoxMenuItem.text")); // NOI18N
        viewControlPointsCheckBoxMenuItem.setName("viewControlPointsCheckBoxMenuItem"); // NOI18N
        viewControlPointsCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewControlPointsCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewControlPointsCheckBoxMenuItem);

        viewCoordinateAxelsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        viewCoordinateAxelsMenuItem.setSelected(true);
        viewCoordinateAxelsMenuItem.setText(resourceMap.getString("viewCoordinateAxelsMenuItem.text")); // NOI18N
        viewCoordinateAxelsMenuItem.setName("viewCoordinateAxelsMenuItem"); // NOI18N
        viewCoordinateAxelsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewCoordinateAxelsMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewCoordinateAxelsMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        viewMenu.add(jSeparator1);

        zoomFitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        zoomFitMenuItem.setText(resourceMap.getString("zoomFitMenuItem.text")); // NOI18N
        zoomFitMenuItem.setName("zoomFitMenuItem"); // NOI18N
        zoomFitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomFitMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomFitMenuItem);

        resetViewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        resetViewMenuItem.setText(resourceMap.getString("resetViewMenuItem.text")); // NOI18N
        resetViewMenuItem.setName("resetViewMenuItem"); // NOI18N
        resetViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetViewMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(resetViewMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        viewMenu.add(jSeparator2);

        cplotMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        cplotMenuItem.setText(resourceMap.getString("cplotMenuItem.text")); // NOI18N
        cplotMenuItem.setName("cplotMenuItem"); // NOI18N
        cplotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cplotMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(cplotMenuItem);

        jSeparator4.setName("jSeparator4"); // NOI18N
        viewMenu.add(jSeparator4);

        lowQualityRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        lowQualityRadioButtonMenuItem.setName("lowQualityRadioButtonMenuItem"); // NOI18N
        lowQualityRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowQualityRadioButtonMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(lowQualityRadioButtonMenuItem);

        mediumQualityRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mediumQualityRadioButtonMenuItem.setName("mediumQualityRadioButtonMenuItem"); // NOI18N
        mediumQualityRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mediumQualityRadioButtonMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(mediumQualityRadioButtonMenuItem);

        highQualityRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        highQualityRadioButtonMenuItem.setSelected(true);
        highQualityRadioButtonMenuItem.setName("highQualityRadioButtonMenuItem"); // NOI18N
        highQualityRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highQualityRadioButtonMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(highQualityRadioButtonMenuItem);

        menuBar.add(viewMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setText(resourceMap.getString("helpMenuItem.text")); // NOI18N
        helpMenuItem.setName("helpMenuItem"); // NOI18N
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        curveInfoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        curveInfoMenuItem.setText(resourceMap.getString("curveInfoMenuItem.text")); // NOI18N
        curveInfoMenuItem.setName("curveInfoMenuItem"); // NOI18N
        curveInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curveInfoMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(curveInfoMenuItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        translateAndScaleLabel.setText(resourceMap.getString("translateAndScaleLabel.text")); // NOI18N
        translateAndScaleLabel.setName("translateAndScaleLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        hoverPointPositionLabel.setText(resourceMap.getString("hoverPointPositionLabel.text")); // NOI18N
        hoverPointPositionLabel.setName("hoverPointPositionLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1161, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(translateAndScaleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hoverPointPositionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addGap(723, 723, 723)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(translateAndScaleLabel)
                    .addComponent(statusMessageLabel)
                    .addComponent(hoverPointPositionLabel))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("empty-statement")
private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed

        if (!maybeSaveFile()) {
            return;
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("CPlot File", "dat"));
        jfc.showOpenDialog(bezierPanel);
        if (jfc.getSelectedFile() != null) {

            bezierPanel.reset();
            try {
                CPlotSerializer sps = new CPlotSerializer(bezierPanel);
                BufferedReader in = new BufferedReader(new FileReader(jfc.getSelectedFile()));
                StringBuilder sb = new StringBuilder();
                //read file into a string
                while (true) {
                    String str = in.readLine();
                    if (str == null) {
                        break;
                    }
                    sb.append(str).append('\n');
                }
                in.close();
                try {
                    sps.setData(sb.toString(), false);
                    bezierPanel.zoomFit();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Could not parse file:\n" + ex, "Error parsing", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Could not read file:\n" + ex, "Error reading", JOptionPane.ERROR_MESSAGE);
            }
        }
}//GEN-LAST:event_openMenuItemActionPerformed

private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
    save();
}//GEN-LAST:event_saveMenuItemActionPerformed

    private boolean save() {
        boolean saveSucceded = true;

        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("CPlot File", "dat"));
        jfc.showSaveDialog(bezierPanel);
        if (jfc.getSelectedFile() != null) {
            File outFile = jfc.getSelectedFile();
            // Fix file extension
            if (!outFile.getName().toLowerCase().endsWith(".dat")) {
                outFile = new File(outFile.getAbsolutePath() + ".dat");
            }

            FileWriter fw = null;
            try {
                CPlotSerializer sps = new CPlotSerializer(bezierPanel);
                fw = new FileWriter(outFile);
                fw.append(sps.getData());
            } catch (IOException ex) {
                saveSucceded = false;
                JOptionPane.showMessageDialog(mainPanel, "Could not write file:\n" + ex, "Error writing", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Could not write file:\n" + ex, "Error writing", JOptionPane.ERROR_MESSAGE);
                    saveSucceded = false;
                }
            }
        } else {
            saveSucceded = false;
        }

        if (saveSucceded) {
            saved = true;
        }
        return saveSucceded;
    }

    /**
     * 
     * @return true if new or open should proceed, otherwise false.
     */
    public boolean maybeSaveFile() {
        if (saved) {
            return true;
        } else {
            int response = JOptionPane.showConfirmDialog(mainPanel, "You have not saved. Do you want to save?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                return save();
            } else if (response == JOptionPane.NO_OPTION) {
                return true;
            }
            return false;
        }
    }

private void degreeElevationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_degreeElevationMenuItemActionPerformed
    translateAndScaleLabel.setText("Click on a control point of the bezier curve");
    degreeElevation = true;
}//GEN-LAST:event_degreeElevationMenuItemActionPerformed

private void viewControlPolygonCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewControlPolygonCheckBoxMenuItemActionPerformed
    bezierPanel.setViewControlPolygon(viewControlPolyconCheckboxMenuItem.isSelected());
}//GEN-LAST:event_viewControlPolygonCheckBoxMenuItemActionPerformed

private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
    if (maybeSaveFile()) {
        bezierPanel.reset();
        saved = true;
    }
}//GEN-LAST:event_newMenuItemActionPerformed

private void cplotMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cplotMenuItemActionPerformed
    if (cplotMenuItem.isSelected()) {
        if (cPlotWindow == null) {
            cPlotWindow = new CPlotWindow(bezierPanel);
        }
        cPlotWindow.setVisible(true);
    } else {
        cPlotWindow.dispose();
    }
}//GEN-LAST:event_cplotMenuItemActionPerformed

private void viewMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_viewMenuMenuSelected
    if (cPlotWindow == null) {
        cplotMenuItem.setSelected(false);
    } else {
        cplotMenuItem.setSelected(cPlotWindow.isVisible());
    }
}//GEN-LAST:event_viewMenuMenuSelected

private void viewControlPointsCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewControlPointsCheckBoxMenuItemActionPerformed
    bezierPanel.setViewControlPoints(viewControlPointsCheckBoxMenuItem.isSelected());
}//GEN-LAST:event_viewControlPointsCheckBoxMenuItemActionPerformed

private void zoomFitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomFitMenuItemActionPerformed
    bezierPanel.zoomFit();
}//GEN-LAST:event_zoomFitMenuItemActionPerformed

private void resetViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetViewMenuItemActionPerformed
    bezierPanel.resetView();
}//GEN-LAST:event_resetViewMenuItemActionPerformed

private void scaleCurvesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleCurvesMenuItemActionPerformed
    String response = JOptionPane.showInputDialog("Scale factor:", "1");
    if (response != null) {
        try {
            bezierPanel.scaleAllCurves(Double.parseDouble(response));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, response + " is not a number");
        }
    }
}//GEN-LAST:event_scaleCurvesMenuItemActionPerformed

private void translateCurvesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateCurvesMenuItemActionPerformed
    String response = JOptionPane.showInputDialog("Move curves:", "0 x 0");

    if (response != null) {
        String[] parts = response.split("x");
        if (parts.length == 2) {
            try {
                bezierPanel.translateAllCurves(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainPanel, response + " is not a number");
            }
        }
    }
}//GEN-LAST:event_translateCurvesMenuItemActionPerformed

private void highQualityRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highQualityRadioButtonMenuItemActionPerformed
    bezierPanel.setQuality(Curve.HIGH_QUALITY);
}//GEN-LAST:event_highQualityRadioButtonMenuItemActionPerformed

private void mediumQualityRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mediumQualityRadioButtonMenuItemActionPerformed
    bezierPanel.setQuality(Curve.MEDIUM_QUALITY);
}//GEN-LAST:event_mediumQualityRadioButtonMenuItemActionPerformed

private void lowQualityRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowQualityRadioButtonMenuItemActionPerformed
    bezierPanel.setQuality(Curve.LOW_QUALITY);
}//GEN-LAST:event_lowQualityRadioButtonMenuItemActionPerformed

private void viewCoordinateAxelsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewCoordinateAxelsMenuItemActionPerformed
    bezierPanel.setViewCoordinateAxis(viewCoordinateAxelsMenuItem.isSelected());
}//GEN-LAST:event_viewCoordinateAxelsMenuItemActionPerformed

private void curveInfoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curveInfoMenuItemActionPerformed

    JFrame mainFrame = BezierApp.getApplication().getMainFrame();
    InfoBox box = new InfoBox(getFrame(), bezierPanel);
    box.setLocationRelativeTo(mainFrame);
    box.setVisible(true);
}//GEN-LAST:event_curveInfoMenuItemActionPerformed

private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
    HelpBox box = new HelpBox(getFrame());
    box.setLocationRelativeTo(getFrame());
    box.setVisible(true);
}//GEN-LAST:event_helpMenuItemActionPerformed

private void splitCurveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splitCurveMenuItemActionPerformed
    translateAndScaleLabel.setText("Click on a control point of the bezier curve");
    splitCurve = true;
}//GEN-LAST:event_splitCurveMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem cplotMenuItem;
    private javax.swing.JMenuItem curveInfoMenuItem;
    private javax.swing.JMenuItem degreeElevationMenuItem;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JRadioButtonMenuItem highQualityRadioButtonMenuItem;
    public javax.swing.JLabel hoverPointPositionLabel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JRadioButtonMenuItem lowQualityRadioButtonMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JRadioButtonMenuItem mediumQualityRadioButtonMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem resetViewMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem scaleCurvesMenuItem;
    private javax.swing.JMenuItem splitCurveMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    public javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    public javax.swing.JLabel translateAndScaleLabel;
    private javax.swing.JMenuItem translateCurvesMenuItem;
    private javax.swing.JCheckBoxMenuItem viewControlPointsCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem viewControlPolyconCheckboxMenuItem;
    private javax.swing.JCheckBoxMenuItem viewCoordinateAxelsMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem zoomFitMenuItem;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    public void mouseClicked(MouseEvent e) {
        if (degreeElevation || splitCurve) {
            translateAndScaleLabel.setText("");
            if(degreeElevation) {
                bezierPanel.degreeElevation(e.getPoint());
            } else if(splitCurve) {
                bezierPanel.splitCurve(e.getPoint());
            }
            degreeElevation = false;
            splitCurve = false;
        }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void stateChanged() {
        saved = false;
    }
}

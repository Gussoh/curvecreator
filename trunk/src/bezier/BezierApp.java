/*
 * BezierApp.java
 * This is supposed to be the model class...
 */

package bezier;

import java.util.EventObject;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class BezierApp extends SingleFrameApplication {

    BezierView bezierView;
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        bezierView = new BezierView(this);
        show(bezierView);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
        addExitListener(new ExitListener() {

            public boolean canExit(EventObject arg0) {
                return bezierView.maybeSaveFile();
            }

            public void willExit(EventObject arg0) {
            }
        });
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of BezierApp
     */
    public static BezierApp getApplication() {
        return Application.getInstance(BezierApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(BezierApp.class, args);
    }
}

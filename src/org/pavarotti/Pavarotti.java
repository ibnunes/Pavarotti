/*
 * Copyright (C) 2019 Ovelhas do Presépio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pavarotti;

import org.pavarotti.ui.intf.*;
import org.pavarotti.ui.viewer.*;
import org.pavarotti.ui.controller.*;
import org.pavarotti.ui.controller.Program.ViewerInfo;

import javax.swing.JOptionPane;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Pavarotti {
    private static final Version DEFAULT_VERSION = new Version(1, 0, 5, Version.Stage.Final);
    private static Program app;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args, DEFAULT_VERSION, new ViewerInfo[] {new ViewerInfo(new TUI(), "TUI")});
    }
    
    /**
     * Launches the application
     * @param args the command line arguments
     * @param VERSION the version of the program
     * @param viewers the list of viewers to be considered
     */
    public static void launch(String args[], final Version VERSION, ViewerInfo viewers[]) {
        try {
            app = new Program(VERSION);
            for (ViewerInfo v : viewers)
                app.addViewer(v.viewer, v.argument);
            app.launch(args);
        } catch (Exception e) {
            String msg = String.format("A fatal error has occurred!\n%s: %s\n", e.getClass(), e.getMessage());
            System.out.println(msg);
            JOptionPane.showMessageDialog( null, msg, "Pavarotti " + VERSION, JOptionPane.ERROR_MESSAGE );
            app.emergencyStop();
        }
    }
    
    /**
     * Allows for external launch by an external Java/JavaFX project
     * @param viewer the reference to the original project
     * @param argument the argument that indicates the launch from this viewer
     */
    public static void launchFromExternal(Viewer viewer, String argument) {
        final ViewerInfo[] VIEWERS = new ViewerInfo[] {new ViewerInfo(viewer, argument)};
        launch(new String[] {"--mode", argument}, DEFAULT_VERSION, VIEWERS);
    }
    
    /**
     * Allows for external launch by an external Java/JavaFX project
     * @param viewer the reference to the original project
     * @param argument the argument that indicates the launch from this viewer
     * @param version the personalized version of the application
     */
    public static void launchFromExternal(Viewer viewer, String argument, Version version) {
        final ViewerInfo[] VIEWERS = new ViewerInfo[] {new ViewerInfo(viewer, argument)};
        launch(new String[] {"--mode", argument}, version, VIEWERS);
    }
}

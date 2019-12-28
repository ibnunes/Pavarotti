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

import javax.swing.JOptionPane;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Pavarotti {
    private static final Version VERSION = new Version(1, 0, 3, Version.Stage.FINAL);
    private static Program app;
    
    private static Viewer tui = null;
    private static Viewer gui = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            app = new Program(VERSION);
            app.addViewer(tui == null ? new TUI() : tui, "TUI");
            app.addViewer(gui == null ? new GUI() : gui, "GUI");
            app.launch(args);
        } catch (Exception e) {
            String msg = String.format("A fatal error has occurred!\n%s: %s\n", e.getClass(), e.getMessage());
            System.out.println(msg);
            JOptionPane.showMessageDialog( null, msg, "Pavarotti " + VERSION, JOptionPane.ERROR_MESSAGE );
        }
    }
    
    /**
     * Allows for external launchViewer by JavaFX project
     * @param args the command line arguments
     * @param ui the reference to the original project
     */
    public static void launchgui(String[] args, Viewer ui) {
        Pavarotti.gui = ui;
        main(new String [] {"--mode", "GUI"});
    }
    
    /**
     * Allows for external launchViewer by Java project
     * @param args the command line arguments
     * @param ui the reference to the original project
     */
    public static void launchtui(String[] args, Viewer ui) {
        Pavarotti.tui = ui;
        main(new String [] {"--mode", "TUI"});
    }
}

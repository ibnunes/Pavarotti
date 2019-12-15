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

import org.pavarotti.ui.viewer.*;
import org.pavarotti.ui.controller.*;

import javax.swing.JOptionPane;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Pavarotti {
    private static final Version VERSION = new Version(1, 0, 2, Version.Stage.FINAL);
    private static Application app;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            app = new Application(VERSION);
            app.addViewer(new TUI(), "TUI");
            app.addViewer(new GUI(), "GUI");
            app.launch(args);
        } catch (Exception e) {
            String msg = String.format("A fatal error has occurred!\n%s: %s\n", e.getClass(), e.getMessage());
            System.out.println(msg);
            JOptionPane.showMessageDialog( null, msg, "Pavarotti " + VERSION, JOptionPane.ERROR_MESSAGE );
        }
    }
}

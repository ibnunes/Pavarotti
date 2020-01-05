/*
 * Copyright (C) 2019 Igor Nunes & Beatriz Costa
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
package org.pavarotti.ui.viewer;

import org.pavarotti.ui.intf.Viewer;
import org.pavarotti.ui.intf.Controller;

/**
 * NOT IMPLEMENTED
 * @author Igor Nunes
 * @author Beatriz Costa
 */
public class GUI implements Viewer {
    @Override
    public void loadViewer() {
        // TODO
    }
    
    @Override
    public void launchViewer(String[] args) throws Exception {
        throw new Exception("Not ready for use.");
        // TODO
    }
    
    @Override
    public void emergencyStop() {
        // TODO
    }
    
    @Override
    public void bindController(Controller controller) {
        // TO-DO
    }
    
    @Override
    public void firstSetup() {
        // TODO
    }
    
    @Override
    public void showHelp() {
        // TODO
    }
    
    @Override
    public void showAbout() {
        // TODO
    }
    
    @Override
    public void showInfo(String msg) {
        // TODO
    }
    
    @Override
    public void showMessage(String msg) {
        // TODO
    }
    
    @Override
    public void showWarning(String msg) {
        // TODO
    }
    
    @Override
    public void showError(String msg) {
        // TODO
    }
}

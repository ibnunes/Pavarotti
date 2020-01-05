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
package org.pavarotti.ui.intf;

/**
 *
 * @author Ovelhas do Presépio
 */
public interface Viewer {
    abstract void loadViewer();
    abstract void launchViewer(String[] args) throws Exception;
    abstract void emergencyStop();
    abstract void bindController(Controller controller);
    abstract void showHelp();
    abstract void showAbout();
    abstract void showInfo(String msg);
    abstract void showMessage(String msg);
    abstract void showWarning(String msg);
    abstract void showError(String msg);
    abstract void firstSetup();
}

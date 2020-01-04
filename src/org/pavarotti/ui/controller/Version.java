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
package org.pavarotti.ui.controller;

import org.pavarotti.ui.intf.Versioning;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Version extends Versioning {
    /**
     * @return a string representation of the version
     */
    @Override
    public String toString() {
        return String.format("%d.%d.%d%s", major, minor, iteration, (stage == Versioning.Stage.Final) ? "" : ("-" + stage));
    }

    /**
     * Constructor of the class
     * @param major the major version of the application
     * @param minor the minor version of the application
     * @param iteration the iteration version of the application
     * @param stage the stage of the application (ALPHA, BETA or FINAL) 
     */
    public Version(int major, int minor, int iteration, Versioning.Stage stage) {
        this.major     = major;
        this.minor     = minor;
        this.iteration = iteration;
        this.stage     = stage;
    }
}

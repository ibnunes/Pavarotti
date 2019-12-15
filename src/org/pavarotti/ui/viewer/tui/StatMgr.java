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
package org.pavarotti.ui.viewer.tui;

import org.pavarotti.ui.intf.*;

import java.time.LocalDateTime;

/**
 *
 * @author Ovelhas do Presépio
 */
public class StatMgr {
    private static Controller control;
    private static Viewer view;
    
    /**
     * Binds the controller of the application
     * @param control the controller to bind
     * @param view the viewer to bind
     */
    public static void bind(Controller control, Viewer view) {
        StatMgr.control = control;
        StatMgr.view    = view;
    }
    
    /**
     * Average of spectators for a given performance
     */
    public static void averageSpectators() {
        view.showMessage("=== Média de espetadores de um espetáculo\n === ");
        String ID = PerformanceMgr.getID(false);
        if (ID.isEmpty()) return;
        PerformanceMgr.show(ID);
        Double avg = control.averageSpectators(ID);
        if (avg == -1.) {
            view.showError("Não foi possível determinar a média de espetadores.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Média de espetadores: " + avg.toString() + "\n");
        Flow.pause();
    }
    
    /**
     * Best day for a given performance
     */
    public static void bestPerformanceDay() {
        view.showMessage("=== Dia com mais espetadores num espetáculo ===\n");
        String ID = PerformanceMgr.getID(false);
        if (ID.isEmpty()) return;
        LocalDateTime dt = control.bestPerformanceDay(ID);
        if (dt == null) {
            view.showError("Não foi possível determinar o dia com mais espetadores.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Dia e hora: " + dt.toString() + "\n");
        Flow.pause();
    }
    
    /**
     * Worst day for a given performance
     */
    public static void worstPerformanceDay() {
        view.showMessage("=== Dia com menos espetadores num espetáculo ===\n");
        String ID = PerformanceMgr.getID(false);
        if (ID.isEmpty()) return;
        LocalDateTime dt = control.worstPerformanceDay(ID);
        if (dt == null) {
            view.showError("Não foi possível determinar o dia com menos espetadores.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Dia e hora: " + dt.toString() + "\n");
        Flow.pause();
    }
    
    /**
     * Most watched performance
     */
    public static void mostWatchedPerformance() {
        view.showMessage("=== Espetáculo mais visto ===\n");
        String ID = control.mostWatchedPerformance();
        if (ID.isEmpty()) {
            view.showError("Não foi possível determinar o espetáculo mais visto.\n");
            Flow.pause();
            return;
        }
        PerformanceMgr.show(ID);
        Flow.pause();
    }
    
    /**
     * Least watched performance
     */
    public static void leastWatchedPerformance() {
        view.showMessage("=== Espetáculo menos visto ===\n");
        String ID = control.leastWatchedPerformance();
        if (ID.isEmpty()) {
            view.showError("Não foi possível determinar o espetáculo menos visto.\n");
            Flow.pause();
            return;
        }
        PerformanceMgr.show(ID);
        Flow.pause();
    }
    
    /**
     * Total of all spectators of a performance
     */
    public static void sumAllSpectators() {
        view.showMessage("=== Total de espetadores de um espetáculo ===\n");
        String ID = PerformanceMgr.getID(false);
        if (ID.isEmpty()) return;
        PerformanceMgr.show(ID);
        Long sum = control.sumAllSpectators(ID);
        if (sum == -1.) {
            view.showError("Não foi possível determinar o total de espetadores.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Total de espetadores: " + sum.toString() + "\n");
        Flow.pause();
    }
}
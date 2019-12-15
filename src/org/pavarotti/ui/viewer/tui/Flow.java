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

import java.io.IOException;
import org.dma.io.Read;
import org.pavarotti.ui.intf.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Flow {
    private static Viewer view;
    
    public static void bind(Viewer view) {
        Flow.view = view;
    }
    
    // -------------------------------------------------------------------------
    /**
     * Pauses the program until ENTER is pressed
     * @param prompt the prompt to show at pause
     */
    public static void pause(String prompt) {
        view.showMessage(prompt);
        try {
            Read.asString();
        } catch (IOException e) {
            view.showError("Um erro fatal aconteceu ao sair do modo de pausa.\n");
            view.showInfo("O programa irá tentar prosseguir...\n");
        }
        view.showMessage("\n");
    }
    
    /**
     * Pauses the program until ENTER is pressed.
     * Presents the default prompt "Prima qualquer tecla para continuar... "
     */
    public static void pause() {
        pause("Prima qualquer tecla para continuar... ");
    }
}

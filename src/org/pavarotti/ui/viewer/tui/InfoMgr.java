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
import org.pavarotti.core.intf.*;

import java.io.IOException;
import org.dma.io.Read;

/**
 *
 * @author Ovelhas do Presépio
 */
public class InfoMgr {
    private static Controller control;
    private static Viewer view;
    
    /**
     * Binds the controller of the application
     * @param control the controller to bind
     * @param view the viewer to bind
     */
    public static void bind(Controller control, Viewer view) {
        InfoMgr.control = control;
        InfoMgr.view    = view;
    }
    
    /**
     * Processes first setup
     */
    public static void firstSetup() {
        view.showMessage("=== PRIMEIRA CONFIGURAÇÃO ===\n");
        
        view.showMessage("INFORMAÇÃO DA COMPANHIA\n");
        String name    = getString("nome da companhia");
        String country = getString("país");
        String city    = getString("cidade");
        
        view.showMessage("\nINFORMAÇÃO DO DIRETOR\n");
        Integer director = 0;
        if (control.getAllDirectors().isEmpty()) {
            view.showInfo("Ainda não existem diretores definidos.\n");
            view.showMessage("Um novo diretor será criado.\n");
            StaffMgr.makeDirector();
            director = control.getAllDirectors().get(0).getID();
        } else {
            boolean done = false;
            do {
                char option = '\0';
                do {
                    view.showMessage("Usar diretor já existente? (S/N)? ");
                    option = Character.toUpperCase(Read.tryAsChar());
                } while (option != 'S' && option != 'N');

                if (option == 'S') {
                    int ID = StaffMgr.getID();
                    try {
                        director = control.searchDirector(ID).getID();
                        done = true;
                    } catch (NullPointerException e) {
                        view.showError("ID não existe.\n");
                    }
                } else {
                    view.showMessage("Um novo diretor será criado.\n");
                    StaffMgr.makeDirector();
                    director = control.getAllDirectors().get(control.getAllDirectors().size()-1).getID();
                    done = true;
                }
            } while (!done);
        }
        
        control.setupCompanyInfo(name, city, country, director);
    }
    
    /**
     * Gets a string from the user
     * @param WHAT what is being read, for I/O purposes
     * @return the string
     */
    private static String getString(final String WHAT) {
        String s = "";
        do {
            try {
                view.showMessage("Inserir " + WHAT + ": ");
                s = Read.asString();
            } catch (IOException e) {
                view.showError("Erro ao ler o nome.\n");
            }
        } while (s.isEmpty());
        return s;
    }
}

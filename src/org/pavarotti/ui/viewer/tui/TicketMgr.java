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

import org.pavarotti.core.throwable.*;
import org.pavarotti.ui.intf.*;
import org.util.Menu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.dma.io.Read;

/**
 *
 * @author Ovelhas do Presépio
 */
public class TicketMgr {
    private static Controller control;
    private static Viewer view;
    
    private static Menu whenmenu;
    
    /**
     * Binds the controller of the application
     * @param control the controller to bind
     * @param view the viewer to bind
     */
    public static void bind(Controller control, Viewer view) {
        TicketMgr.control = control;
        TicketMgr.view = view;
    }
    
    // -------------------------------------------------------------------------
    /**
     * Sells tickets
     */
    public static void sell() {
        view.showMessage("=== VENDA DE BILHETES ===\n");
        try {
            view.showMessage("Indique o espetáculo por ID:\n");
            String indperf = PerformanceMgr.getID(false);
            if (indperf.isEmpty()) return;
            
            view.showMessage("INFORMAÇÃO DO ESPETÁCULO:\n");
            PerformanceMgr.show(control.searchPerformance(indperf));
            Flow.pause("");
            
            final ArrayList<LocalDateTime> LIST = PerformanceMgr.getOnlyWhensByID(indperf);
            if (LIST.isEmpty()) return;
            loadMenu(LIST, indperf);
            
            short indtick;
            boolean is_available;
            Integer available, sold, capacity;
            do {
                whenmenu.show();
                indtick = whenmenu.getOption("Selecione: ", "Opção inexistente.", "Opção inválida.");
                if (indtick == whenmenu.getExit()) {
                    view.showInfo("Operação cancelada pelo operador.\n\n");
                    return;
                }
                indtick--;
                is_available = control.hasAvailableTickets(indperf, indtick);
                if (!is_available) {
                    view.showInfo("A sala está encerrada.\n");
                } else {
                    capacity  = control.hallCapacity(indperf);
                    sold      = control.ticketsSold(indperf, indtick);
                    available = control.ticketsAvailable(indperf, indtick);
                    view.showMessage(
                            String.format(
                                    "DE %d LUGARES:\n\tDisponíveis: %d\n\t   Vendidos: %d\n",
                                    capacity, available, sold
                            )
                    );
                }
            } while (!is_available);
            
            showHall(indperf, indtick);
            String seat = "";
            boolean ok = false;
            do {
                try {
                    view.showMessage("Indique o lugar, de A01 a J20: ");
                    seat = Read.tryAsString().toUpperCase();
                    ok = control.sellTicket(indperf, seat, indtick);
                    if (!ok) {
                        view.showWarning("Não foi possível vender o bilhete.\n\n");
                        return;
                    }
                } catch (TicketSoldException e) {
                    view.showError("Este lugar já está vendido!\n");
                }
            } while (!ok);
            
            if (!requestConfirmation("==> VENDER BILHETE? (S/N) ", "Venda cancelada pelo operador.\n\n"))
                return;
            
            view.showInfo("Bilhete vendido com sucesso.\n");
            view.showMessage("\tDia e hora: " + LIST.get(indtick).toString() + "\n");
            view.showMessage("\t     Lugar: " + seat + "\n\n");
            view.showMessage("\t     Preço: " + control.getTicketPrice(indperf, seat) + "€\n\n");
            
        } catch (Exception e) {
            view.showError("Ocorreu um erro inesperado no sistema de venda de bilhetes.\n");
        }
    }
    
    /**
     * Refunds a ticket
     */
    public static void refund() {
        view.showMessage("=== DEVOLUÇÃO DE UM BILHETE ===\n");
        try {
            view.showMessage("Indique o espetáculo por ID:\n");
            String indperf = PerformanceMgr.getID(false);
            if (indperf.isEmpty()) return;
            
            view.showMessage("INFORMAÇÃO DO ESPETÁCULO:\n");
            PerformanceMgr.show(control.searchPerformance(indperf));
            Flow.pause("");
            
            final ArrayList<LocalDateTime> LIST = PerformanceMgr.getOnlyWhensByID(indperf);
            if (LIST.isEmpty()) return;
            loadMenu(LIST, indperf);
            
            short indtick;
            whenmenu.show();
            indtick = whenmenu.getOption("Selecione: ", "Opção inexistente.", "Opção inválida.");
            if (indtick == whenmenu.getExit()) {
                view.showInfo("Operação cancelada pelo operador.\n\n");
                return;
            }
            indtick--;
            
            showHall(indperf, indtick);
            String seat = "";
            boolean ok = false;
            do {
                try {
                    view.showMessage("Indique o lugar, de A01 a J20: ");
                    seat = Read.tryAsString().toUpperCase();
                    ok = control.refundTicket(indperf, seat, indtick);
                    if (!ok) {
                        view.showWarning("Não foi possível vender o bilhete.\n\n");
                        return;
                    }
                } catch (TicketNotSoldException e) {
                    view.showError("Este lugar não foi vendido!\n\n");
                }
            } while (!ok);
            
            if (!requestConfirmation("==> DEVOLVER BILHETE? (S/N) ", "Devolução cancelada pelo operador.\n\n"))
                return;
            
            view.showInfo("Bilhete devolvido com sucesso.\n");
            view.showMessage("\tDia e hora: " + LIST.get(indtick).toString() + "\n");
            view.showMessage("\t     Lugar: " + seat + "\n");
            view.showMessage("\tA devolver: " + control.getTicketPrice(indperf, seat) + "€\n\n");
            
        } catch (Exception e) {
            view.showError("Ocorreu um erro na devolução do bilhete.\n");
        }
    }
    
    // -------------------------------------------------------------------------
    private static void loadMenu(final ArrayList<LocalDateTime> LIST, String indperf) {
        Integer available, sold;
        whenmenu = new Menu("DATAS E HORAS:");
        for (int i = 0; i < LIST.size(); i++) {
            sold      = control.ticketsSold(indperf, i);
            available = control.ticketsAvailable(indperf, i);
            whenmenu.addItem(
                    String.format(
                            "%35s (Disp: %3d, Vend: %3d)",
                            LIST.get(i).toString(),
                            available, sold
                    ),
                    Menu.NOTHING
            );
        }
        whenmenu.addItem("CANCELAR", Menu.NOTHING, true);
    }
    
    /**
     * Requests a yes/no confirmation
     * @param PROMPT the prompt
     * @param DISP_CANCEL the information if canceled
     * @return true if yes, false if no
     */
    private static boolean requestConfirmation(final String PROMPT, final String DISP_CANCEL) {
        char option;
        do {
            view.showMessage(PROMPT);
            option = Character.toUpperCase(Read.tryAsChar());
        } while (option != 'S' && option != 'N');

        if (option == 'N') {
            view.showInfo(DISP_CANCEL);
            return false;
        }
        return true;
    }
    
    /**
     * Shows concert hall
     */
    private static void showHall(final String SHOWID, final short WHENINDEX) {
        final ArrayList<ArrayList<Boolean>> MATRIX = control.hallSoldMatrix(SHOWID, WHENINDEX);
        final int ROWS = MATRIX.size();
        final int COLS = MATRIX.get(0).size();
        view.showMessage("SALA (## vendido, .. disponível):\n    ");
        for (int j = 0; j < COLS; j++) {
            view.showMessage(String.format(" %02d ", j));
        }
        view.showMessage("\n    ");
        for (int j = 0; j < COLS; j++) {
            view.showMessage(String.format(" -- ", j));
        }
        view.showMessage("\n");
        for (int i = 0; i < ROWS; i++) {
            view.showMessage(String.format("%c | ", (char)((int)'A' + i)));
            for (int j = 0; j < COLS; j++) {
                view.showMessage((MATRIX.get(i).get(j)) ? " ## " : " .. ");
            }
            view.showMessage("\n");
        }
    }
}

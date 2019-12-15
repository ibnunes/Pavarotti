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
import org.pavarotti.core.components.Performance;
import org.dma.io.Read;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 *
 * @author Ovelhas do Presépio
 */
public class PerformanceMgr {
    private static Controller control;
    private static Viewer view;
    
    private static final String INPUT_CANCEL = "CANCELAR";
    
    private static enum StaffType {
        SINGER, DANCER, DIRECTOR;
    }
    
    /**
     * Binds the controller of the application
     * @param control the controller to bind
     * @param view the viewer to bind
     */
    public static void bind(Controller control, Viewer view) {
        PerformanceMgr.control = control;
        PerformanceMgr.view    = view;
    }
    
    // -------------------------------------------------------------------------
    /**
     * Makes a new performance
     */
    public static void make() {
        view.showMessage("=== NOVO ESPETÁCULO ===\n");
        view.showInfo("O ID dos espetáculos são fornecidos pelo OPERADOR. Pode conter números e letras.\n");
        
        try {
            String showID = getID(true);
            if (showID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada pelo operador.\n");
                return;
            }
            String name = getName();
            Double price = getPrice();

            view.showMessage("Inserir os dias e horas do espetáculo:\n");
            ArrayList<LocalDateTime> when = getManyWhen();
            if (when.isEmpty()) return;

            view.showMessage("Inserir o ID do diretor da ópera:\n");
            Integer opera = getStaffID(StaffType.DIRECTOR, false);
            if (opera == -2) return;

            view.showMessage("Inserir o ID do diretor de casting:\n");
            Integer casting = getStaffID(StaffType.DIRECTOR, false);
            if (casting == -2) return;

            view.showMessage("Inserir os IDs dos cantores associados:\n");
            ArrayList<Integer> singers = getManyStaffID(StaffType.SINGER, false);
            if (singers.isEmpty()) return;

            view.showMessage("Inserir os IDs dos dançarinos associados:\n");
            ArrayList<Integer> dancers = getManyStaffID(StaffType.DANCER, false);
            if (dancers.isEmpty()) return;

            control.newPerformance(showID, name, when, price, opera, casting, singers, dancers);
        
        } catch (Exception e) {
            view.showError(
                    "Ocorreu um erro na criação do espetáculo. Operação cancelada.\n" +
                    e.getClass() + ": " + e.getMessage() + "\n\n"
            );
        }
        Flow.pause();
    }
    
    // -------------------------------------------------------------------------
    /**
     * Lists all performances available
     */
    public static void showAll() {
        view.showMessage("=== LISTAR TODOS OS ESPETÁCULOS ===\n");
        final ArrayList<Performance> list = control.getAllPerformances();
        showList(list);
        Flow.pause();
    }
    
    /**
     * Lists the given list of performances
     * @param list 
     */
    private static void showList(final ArrayList<Performance> list) {
        list.forEach((Performance p) -> show(p));
    }
    
    /**
     * Shows a performance
     * @param p the performance
     */
    public static void show(final Performance p) {
        view.showMessage(p.toString());
    }
    
    /**
     * Shows a performance
     * @param ID the performance ID
     */
    public static void show(final String ID) {
        show(control.searchPerformance(ID));
    }
    
    /**
     * Gets the dates and times of a performance by ID
     * @return the dates and times
     * @param ID 
     */
    public static ArrayList<LocalDateTime> getOnlyWhensByID(String ID) {
        ArrayList<LocalDateTime> result = new ArrayList<>();
        Performance p = control.searchPerformance(ID);
        if (p == null) {
            view.showWarning("ID não existe.\n");
            return result;
        }
        for (int i = 0; i < p.getHall().size(); i++) {
            result.add(p.getHall(i).getWhen());
        }
        return result;
    }
    
    // -------------------------------------------------------------------------
    /**
     * Removes a performance from the list
     */
    public static void delete() {
        view.showMessage("=== ELIMINAR UM ESPETÁCULO ===\n");
        String ID = getID(false);
        if (ID.equals(INPUT_CANCEL)) {
            view.showInfo("Operação cancelada.\n");
            return;
        }
        if (!control.deletePerformance(ID))
            view.showWarning("O espetáculo não pôde ser removido.\n");
        else
            view.showInfo("Espetáculo eliminado com sucesso.\n");
    }
    
    // -------------------------------------------------------------------------
    /**
     * Peeks a performance by ID
     */
    public static void peekByID() {
        view.showMessage("=== CONSULTAR ESPETÁCULO POR ID ===\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            show(control.searchPerformance(ID));
        } catch (Exception e) {
            view.showError("Ocorreu um erro inesperado na consulta do espetáculo!\n");
        }
        Flow.pause();
    }
    
    /**
     * Peeks performances by dates
     */
    public static void peekByDate() {
        view.showMessage("=== CONSULTAR ESPETÁCULO POR DATA ===\n");
        view.showMessage("--- Ver espetáculos entre 2 datas ---\n");
        try {
            view.showMessage("Limite inferior de pesquisa:\n");
            LocalDateTime after  = getWhen();
            view.showMessage("Limite superior de pesquisa:\n");
            LocalDateTime before = getWhen();
            if (after.compareTo(before) >= 0) {
                view.showWarning("Inseriu um limite superior localizado antes do limite inferior.\n");
                LocalDateTime aux = after;
                after = before;
                before = aux;
                view.showInfo("As datas serão consideradas na ordem correta.\n");
            }
            final ArrayList<Performance> list = control.searchPerformanceByDate(after, before);
            if (list == null) {
                view.showWarning("Não foi possível obter resultados.\n");
                return;
            }
            if (list.isEmpty()) {
                view.showInfo("Não há resultados para esta pesquisa.\n");
                return;
            }
            view.showMessage("RESULTADOS:\n");
            showList(list);
        } catch (Exception e) {
            view.showError("Ocorreu um erro inesperado na consulta de espetáculos!\n");
        }
        Flow.pause();
    }
    
    // -------------------------------------------------------------------------
    /**
     * Changes the name of a performance
     */
    public static void changeName() {
        view.showMessage("=== ALTERAÇÃO DO NOME DE UM ESPETÁCULO ===\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            String name = getName();
            control.modifyPerformanceName(ID, name);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    /**
     * Changes the dates and times of a performance
     */
    public static void changeWhen() {
        view.showMessage("=== ALTERAÇÃO DAS DATAS DE UM ESPETÁCULO ===\n");
        view.showWarning("Serão inseridas novas datas, todas as antigas serão PERDIDAS.\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            ArrayList<LocalDateTime> when = getManyWhen();
            control.modifyPerformanceWhen(ID, when);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    /**
     * Changes the list of singers of a performance
     */
    public static void changeSingers() {
        view.showMessage("=== ALTERAÇÃO DOS CANTORES DE UM ESPETÁCULO ===\n");
        view.showWarning("Serão inseridas novos cantores, todos os antigos serão PERDIDOS.\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            ArrayList<Integer> singers = getManyStaffID(StaffType.SINGER, false);
            if (singers.isEmpty()) return;
            control.modifyPerformanceSingers(ID, singers);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    /**
     * Changes the list of dancers of a performance
     */
    public static void changeDancers() {
        view.showMessage("=== ALTERAÇÃO DOS DANÇARINOS DE UM ESPETÁCULO ===\n");
        view.showWarning("Serão inseridas novos dançarinos, todos os antigos serão PERDIDOS.\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            ArrayList<Integer> dancers = getManyStaffID(StaffType.DANCER, false);
            if (dancers.isEmpty()) return;
            control.modifyPerformanceDancers(ID, dancers);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    /**
     * Changes the directors of a performance
     */
    public static void changeDirectors() {
        view.showMessage("=== ALTERAÇÃO DOS DIRETORES DE UM ESPETÁCULO ===\n");
        view.showWarning("Serão inseridas novos diretores, todos os antigos serão PERDIDOS.\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            view.showMessage("Indique o ID do novo diretor de ópera:\n");
            Integer opera = getStaffID(StaffType.DIRECTOR, false);
            if (opera == -2) return;
            view.showMessage("Indique o ID do novo diretor de casting:\n");
            Integer casting = getStaffID(StaffType.DIRECTOR, false);
            if (casting == -2) return;
            control.modifyPerformanceDirectors(ID, opera, casting);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    /**
     * Changes the base price of a performance
     */
    public static void changePrice() {
        view.showMessage("=== ALTERAÇÃO DO PREÇO DE UM ESPETÁCULO ===\n");
        view.showInfo("Escreva CANCELAR no ID para cancelar a operação.\n");
        try {
            String ID = getID(false);
            if (ID.equals(INPUT_CANCEL)) {
                view.showInfo("Operação cancelada.\n");
                return;
            }
            Double price = getPrice();
            control.modifyPerformancePrice(ID, price);
        } catch (Exception e) {
            view.showError("Ocorreu um erro a modificar o espetáculo.\n");
        }
    }
    
    // -------------------------------------------------------------------------
    /**
     * Gets a valid ID
     * @param isNew indicates if a new ID is needed (true) or an existent one (false)
     * @return the ID
     */
    public static String getID(boolean isNew) {
        if (control.getAllPerformances().isEmpty()) {
            view.showWarning("Não existem espetáculos!\n");
            return "";
        }
        
        boolean exists = true;
        String ID = "";
        do {
            try {
                view.showMessage("Indique " + (isNew ? "um novo" : "o") + " ID do espetáculo: ");
                ID = Read.asString().toUpperCase().trim();
                if (ID.isEmpty()) continue;
                if (ID.equals(INPUT_CANCEL))
                    return ID;
                exists = control.performanceIDExists(ID);
                if ( (exists && isNew) || (!exists && !isNew) )
                    view.showInfo("ID " + (isNew ? "já" : "não") + " existe!\n");
            } catch (IOException e) {
                view.showError("Ocorreu um erro a ler o ID.\n");
            }
        } while ( (exists && isNew) || (!exists && !isNew) );
        return ID;
    }
    
    /**
     * Gets a valid name for a performance
     * @return the name
     */
    private static String getName() {
        String name = "";
        do {
            try {
                view.showMessage("Insira um nome: ");
                name = Read.asString();
            } catch (IOException e) {
                view.showError("Ocorreu um erro a ler o nome.\n");
            }
        } while (name.isEmpty());
        return name;
    }
    
    /**
     * Gets a valid price in Euros
     * @return the price
     */
    private static Double getPrice() {
        Double price = -1.;
        try {
            view.showMessage("Insira um preço em Euros: ");
            price = Read.asDouble();
            if (price < 0.) {
                view.showWarning("Inseriu um preço negativo! Será assumido o valor absoluto.\n");
                price = Math.abs(price);
            }
        } catch (IOException e) {
            view.showError("Ocorreu um erro a ler o preço.\n");
        }
        return price;
    }
    
    /**
     * Gets a valid date and time
     * @return the date and time
     */
    public static LocalDateTime getWhen() {
        String s;
        LocalDateTime d = null;
        do {
            try {
                view.showMessage("Inserir data (aaaa-mm-ddThh:mm:ss): ");
                s = Read.asString();
                d = LocalDateTime.parse(s);
                break;
            } catch (IOException e) {
                view.showError("Erro ao ler texto.\n");
            } catch (DateTimeParseException e) {
                view.showWarning("Data não está no formato aaaa-mm-ddThh:mm:ss.\n");
            }
        } while (true);
        return d;
    }
    
    /**
     * @return a list of dates and times
     */
    private static ArrayList<LocalDateTime> getManyWhen() {
        ArrayList<LocalDateTime> when = new ArrayList<>();
        char option = '\0';
        do {
            LocalDateTime newWhen = getWhen();
            if (when.contains(newWhen)) {
                view.showInfo("Data e hora já inseridos anteriormente. Tente outra.\n");
                continue;
            } else {
                when.add(newWhen);
            }
            do {
                view.showMessage("Deseja inserir uma nova data e hora? (S/N) ");
                option = Character.toUpperCase(Read.tryAsChar());
            } while (option != 'S' && option != 'N');
        } while (option != 'N');
        return when;
    }
    
    /**
     * Gets a valid existent staff ID
     * @param TYPE the staff type
     * @return the ID
     */
    private static Integer getStaffID(final StaffType TYPE, boolean isNew) {
        Integer ID = -1;
        
        if (!isNew)
            switch (TYPE) {
                case SINGER:
                    if (control.getAllSingers().isEmpty()) {
                        view.showWarning("Não há cantores!\n");
                        return -1;
                    }
                case DANCER:
                    if (control.getAllDancers().isEmpty()) {
                        view.showWarning("Não há dançarinos!\n");
                        return -1;
                    }
                case DIRECTOR:
                    if (control.getAllDirectors().isEmpty()) {
                        view.showWarning("Não há diretores!\n");
                        return -1;
                    }
                default:
                    break;
            }
        
        boolean exists = false;
        do {
            try {
                view.showMessage("Indique o ID: ");
                String input = "";
                do {
                    input = Read.asString();
                } while (input.isEmpty());
                if (input.toUpperCase().equals(INPUT_CANCEL))
                    return -2;
                ID = Integer.valueOf(input);
                if (!isNew) {
                    switch (TYPE) {
                        case SINGER:   exists = (control.searchSinger(ID)   != null); break;
                        case DANCER:   exists = (control.searchDancer(ID)   != null); break;
                        case DIRECTOR: exists = (control.searchDirector(ID) != null); break;
                        default: break;
                    }
                    if (!exists)
                        view.showInfo("ID não existe.\n");
                } else {
                    exists = true;
                }
            } catch (IOException e) {
                view.showError("Ocorreu um erro a ler o ID.\n");
            }
        } while (!exists);
        return ID;
    }
    
    /**
     * @param TYPE the staff type
     * @return a list of new IDs
     */
    private static ArrayList<Integer> getManyStaffID(final StaffType TYPE, boolean isNew) {
        ArrayList<Integer> list = new ArrayList<>();
        char option = '\0';
        do {
            Integer newID = getStaffID(TYPE, isNew);
            if (newID == -1) {
                view.showWarning("Não foram inseridos membros na lista.\n");
                break;
            }
            if (newID == -2) {
                view.showInfo("Operação cancelada pelo utilizador.\n");
                list.clear();
                break;
            }
            if (list.contains(newID)) {
                view.showInfo("ID já foi inserido. Tente outro.\n");
                break;
            }
            list.add(newID);
            do {
                view.showMessage("Deseja indicar outro? (S/N) ");
                option = Character.toUpperCase(Read.tryAsChar());
            } while (option != 'S' && option != 'N');
        } while (option != 'N');
        return list;
    }
}

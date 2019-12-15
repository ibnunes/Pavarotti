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
import org.pavarotti.core.components.*;
import org.dma.io.Read;
import org.util.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 *
 * @author Ovelhas do Presépio
 */
public class StaffMgr {
    private static Controller control;
    private static Viewer view;
    
    private static Menu modify;
    
    private static enum StaffType {
        SINGER, DANCER, DIRECTOR;
        
        public String asUpperCase() {
            switch (this) {
                case SINGER:   return "CANTOR";
                case DANCER:   return "DANÇARINO";
                case DIRECTOR: return "DIRETOR";
                default:       return "";
            }
        }
        
        public String asLowerCase() {
            switch (this) {
                case SINGER:   return "cantor";
                case DANCER:   return "dançarino";
                case DIRECTOR: return "diretor";
                default:       return "";
            }
        }
        
        public String asCapitalized() {
            switch (this) {
                case SINGER:   return "Cantor";
                case DANCER:   return "Dançarino";
                case DIRECTOR: return "Diretor";
                default:       return "";
            }
        }
    }
    
    /**
     * Binds the controller of the application
     * @param control the controller to bind
     * @param view the viewer to bind
     */
    public static void bind(Controller control, Viewer view) {
        StaffMgr.control = control;
        StaffMgr.view    = view;
        load();
    }
    
    /**
     * Loads the UI elements necessary for this class
     */
    private static void load() {
        modify = new Menu("MODIFICAR?");
        modify.addItem("Nome"                , Menu.NOTHING);
        modify.addItem("Posição"             , Menu.NOTHING);
        modify.addItem("Data de nascimento"  , Menu.NOTHING);
        modify.addItem("Confirmar alterações", Menu.NOTHING, true);
        modify.addItem("CANCELAR TUDO"       , Menu.NOTHING);
    }
    
    /**
     * Runs the menu in a very simplified way, just getting the option
     * @param menu the menu to run
     * @return the selected option, which is valid
     */
    private static short runMenu(Menu menu) {
        short option;
        menu.show();
        option = menu.getOption("Opção: ", "Opção não presente no menu.", "Erro na leitura.");
        System.out.printf("\n");
        return option;
    }
    
    // -------------------------------------------------------------------------
    /**
     * Creates a new singer
     */
    public static void makeSinger() {
        makeStaff(StaffType.SINGER);
    }
    
    /**
     * Creates a new dancer
     */
    public static void makeDancer() {
        makeStaff(StaffType.DANCER);
    }
    
    /**
     * Creates a new director
     */
    public static void makeDirector() {
        makeStaff(StaffType.DIRECTOR);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param TYPE the staff member type
     */
    private static <T extends Person & StaffMember> void makeStaff(final StaffType TYPE) {
        view.showMessage("=== NOVO " + TYPE.asUpperCase() + " ===\n");
        String name = getName();
        char gender = getGender();
        String position = getPosition();
        LocalDate birthday = getBirthday();
        boolean ok = false;
        switch (TYPE) {
            case SINGER:   ok = control.newSinger(name, gender, position, birthday);   break;
            case DANCER:   ok = control.newDancer(name, gender, position, birthday);   break;
            case DIRECTOR: ok = control.newDirector(name, gender, position, birthday); break;
            default:       break;
        }
        if (!ok) {
            view.showError("Não foi possível criar o novo " + TYPE.asLowerCase() + ".\n");
        } else {
            view.showInfo("O novo " + TYPE.asLowerCase() + " foi criado com sucesso.\n");
        }
        Flow.pause();
    }
    
    // -------------------------------------------------------------------------
    /**
     * Lists all the singers
     */
    public static void listSingers() {
        view.showMessage("=== LISTA DE TODOS OS CANTORES ===\n");
        listStaff(control.getAllSingers());
    }
    
    /**
     * Lists all the dancers
     */
    public static void listDancers() {
        view.showMessage("=== LISTA DE TODOS OS DANÇARINOS ===\n");
        listStaff(control.getAllDancers());
    }
    
    /**
     * Lists all the directors
     */
    public static void listDirectors() {
        view.showMessage("=== LISTA DE TODOS OS DIRETORES ===\n");
        listStaff(control.getAllDirectors());
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param staff the list of staff members
     */
    private static <T extends Person & StaffMember> void listStaff(ArrayList<T> staff) {
        if (staff != null) if (staff.size() > 0) {
            view.showMessage("| ID | Admissão | Posição | Nome |\n");
            staff.forEach(
                    (T p) -> {
                        view.showMessage(
                                String.format(
                                        "| %6d | %s | %s | %s |\n",
                                        p.getID(),
                                        p.getAdmission(),
                                        p.getPosition(),
                                        p.getName()
                                )
                        );
                    }
            );
            Flow.pause();
        }
    }
    
    // -------------------------------------------------------------------------
    /**
     * Peeks a singer by ID
     */
    public static void peekSingerByID() {
        peekStaffByID(StaffType.SINGER);
    }
    
    /**
     * Peeks a dancer by ID
     */
    public static void peekDancerByID() {
        peekStaffByID(StaffType.DANCER);
    }
    
    /**
     * Peeks a director by ID
     */
    public static void peekDirectorByID() {
        peekStaffByID(StaffType.DIRECTOR);
    }
    
    /**
     * Peeks a staff member by ID
     * @param <T> a class that extends Person and implements StaffMember
     * @param TYPE the staff member type
     */
    private static <T extends Person & StaffMember> void peekStaffByID(final StaffType TYPE) {
        view.showMessage("=== CONSULTAR " + TYPE.asUpperCase() + " POR ID ===\n");
        int ID = getID();
        T t = null;
        switch (TYPE) {
            case SINGER:   t = control.searchSinger(ID); break;
            case DANCER:   t = control.searchDancer(ID); break;
            case DIRECTOR: t = control.searchDirector(ID); break;
            default: break;
        }
        if (t == null) {
            view.showWarning("ID não existe.\n");
            return;
        }
        view.showMessage("Informação do " + TYPE.asLowerCase() + ":\n");
        showStaff(t);
        Flow.pause();
    }
    
    /**
     * Peeks singers by name
     */
    public static void peekSingerByName() {
        peekStaffByName(StaffType.SINGER);
    }
    
    /**
     * Peeks dancers by name
     */
    public static void peekDancerByName() {
        peekStaffByName(StaffType.DANCER);
    }
    
    /**
     * Peeks directors by name
     */
    public static void peekDirectorByName() {
        peekStaffByName(StaffType.DIRECTOR);
    }
    
    /**
     * Peeks staff members by name
     * @param <T> a class that extends Person and implements StaffMember
     * @param TYPE the staff member type
     */
    private static <T extends Person & StaffMember> void peekStaffByName(final StaffType TYPE) {
        view.showMessage("=== CONSULTAR " + TYPE.asUpperCase() + " POR NOME ===\n");
        String name = getName();
        ArrayList<T> t = null;
        switch (TYPE) {
            case SINGER:   t = control.searchSingers(name);   break;
            case DANCER:   t = control.searchDancers(name);   break;
            case DIRECTOR: t = control.searchDirectors(name); break;
            default:       break;
        }
        if (t == null) {
            view.showInfo("Não foram encontrados resultados.\n");
            Flow.pause();
            return;
        } else if (t.isEmpty()) {
            view.showInfo("Não foram encontrados resultados.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Resultados da pesquisa:\n");
        listStaff(t);
        Flow.pause();
    }
    
    /**
     * @param member the member
     */
    private static <T extends Person & StaffMember> void showStaff(T member) {
        view.showMessage(member.toString());
    }
    // -------------------------------------------------------------------------
    /**
     * Deletes a singer by ID
     */
    public static void deleteSinger() {
        deleteStaff(StaffType.SINGER);
    }
    
    /**
     * Deletes a dancer by ID
     */
    public static void deleteDancer() {
        deleteStaff(StaffType.DANCER);
    }
    
    /**
     * Deletes a director by ID
     */
    public static void deleteDirector() {
        deleteStaff(StaffType.DIRECTOR);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param TYPE the staff type
     */
    private static <T extends Person & StaffMember> void deleteStaff(final StaffType TYPE) {
        view.showMessage("=== APAGAR UM " + TYPE.asUpperCase() + " ===\n");
        final int ID = getID();
        T t = null;
        switch (TYPE) {
            case SINGER:   t = control.searchSinger(ID);   break;
            case DANCER:   t = control.searchDancer(ID);   break;
            case DIRECTOR: t = control.searchDirector(ID); break;
            default:       break;
        }
        if (t == null) {
            view.showWarning("ID não existe.\n");
            Flow.pause();
            return;
        }
        view.showMessage("Confirma que é este " + TYPE.asLowerCase() + " que deseja apagar?\n");
        showStaff(t);
        char option = '\0';
        do {
            view.showMessage("(S/N)? ");
            option = Character.toUpperCase(Read.tryAsChar());
        } while (option != 'S' && option != 'N');
        if (option == 'N') {
            view.showInfo("Operação cancelada.\n");
            Flow.pause();
            return;
        }
        boolean ok = false;
        switch (TYPE) {
            case SINGER:   ok = control.deleteSinger(ID);   break;
            case DANCER:   ok = control.deleteDancer(ID);   break;
            case DIRECTOR: ok = control.deleteDirector(ID); break;
            default:       break;
        }
        view.showInfo(TYPE.asCapitalized() + (ok ? " apagado com sucesso." : " não foi apagado.") + "\n");
        Flow.pause();
    }
    
    // -------------------------------------------------------------------------
    /**
     * Modifies a singer given the ID
     */
    public static void changeSinger() {
        changeStaff(StaffType.SINGER);
    }
    
    /**
     * Modifies a dancer given the ID
     */
    public static void changeDancer() {
        changeStaff(StaffType.DANCER);
    }
    
    /**
     * Modifies a Director given the ID
     */
    public static void changeDirector() {
        changeStaff(StaffType.DIRECTOR);
    }
    
    /**
     * Changes the staff member
     * @param <T> a class that extends Person and implements StaffMember
     * @param TYPE the staff type
     */
    private static <T extends Person & StaffMember> void changeStaff(final StaffType TYPE) {
        view.showMessage("=== MODIFICAR " + TYPE.asUpperCase() + " ===\n");
        final int ID = getID();
        
        T old = null;
        switch (TYPE) {
            case SINGER:   old = control.searchSinger(ID);   break;
            case DANCER:   old = control.searchDancer(ID);   break;
            case DIRECTOR: old = control.searchDirector(ID); break;
            default:       break;
        }
        if (old == null) {
            view.showWarning("ID não existe.\n");
            Flow.pause();
            return;
        }
        
        T p = null;
        try {
            switch (TYPE) {
                case SINGER:
                case DANCER:   p = (T) ((Performer) old).clone(); break;
                case DIRECTOR: p = (T) ((Director) old).clone(); break;
                default:       break;
            }
        } catch (CloneNotSupportedException e) {
            view.showError("Ocorreu um erro a obter os dados antigos.\n");
            return;
        }
        if (p == null) {
            view.showError("Não foi possível obter os dados antigos, ou eles estão corrompidos.\n");
            return;
        }
        
        short option;
        do {
            view.showMessage("Informação actual do " + TYPE.asLowerCase() + ":");
            showStaff(p);
            switch (option = runMenu(modify)) {
                case 1:  p.setName(getName());         break;
                case 2:  p.setPosition(getPosition()); break;
                case 3:  p.setBirthday(getBirthday()); break;
                case 4:  break;
                case 5:  view.showInfo("As alterações foram descartadas.\n"); return;
                default: break;
            }
        } while (option != modify.getExit());
        
        switch (TYPE) {
            case SINGER:   control.modifySinger(ID, p);   break;
            case DANCER:   control.modifyDancer(ID, p);   break;
            case DIRECTOR: control.modifyDirector(ID, p); break;
            default:       break;
        }
        view.showInfo("As alterações foram guardadas.\n");
        Flow.pause();
    }
    
    // -------------------------------------------------------------------------
    /**
     * Gets a valid name
     * @return the name
     */
    public static String getName() {
        String name = "";
        do {
            try {
                view.showMessage("Inserir o nome: ");
                name = Read.asString();
            } catch (IOException e) {
                view.showError("Erro ao ler o nome.\n");
            }
        } while (name.isEmpty());
        return name;
    }
    
    /**
     * Gets a valid position
     * @return the position
     */
    public static String getPosition() {
        String position = "";
        do {
            try {
                view.showMessage("Inserir a posição: ");
                position = Read.asString();
            } catch (IOException e) {
                view.showError("Erro ao ler a posição.\n");
            }
        } while (position.isEmpty());
        return position;
    }
    
    /**
     * Gets a valid gender
     * @return the gender
     */
    public static char getGender() {
        char gender = '\0';
        do {
            try {
                view.showMessage("Inserir o género (M/F/?): ");
                gender = Read.asChar();
                if (!Read.isAlpha(gender)) {
                    view.showMessage("Não inseriu uma letra válida.\n");
                }
            } catch (IOException e) {
                view.showError("Erro ao ler o género.\n");
            }
        } while (!Read.isAlpha(gender));
        return gender;
    }
    
    /**
     * Gets a valid ID
     * @return the ID
     */
    public static int getID() {
        int ID = -1;
        do {
            try {
                view.showMessage("Inserir ID: ");
                ID = Read.asInt();
            } catch (IOException e) {
                view.showError("Erro ao ler o ID.\n");
            }
        } while (ID < 0);
        return ID;
    }
    
    /**
     * Gets a valid birthday date
     * @return the birthday date
     */
    public static LocalDate getBirthday() {
        String s;
        LocalDate d = LocalDate.now();
        do {
            try {
                view.showMessage("Inserir data (aaaa-mm-dd): ");
                s = Read.asString();
                d = LocalDate.parse(s);
                break;
            } catch (IOException e) {
                view.showError("Erro ao ler texto.\n");
            } catch (DateTimeParseException e) {
                view.showWarning("Data não está no formato aaaa-mm-dd.\n");
            }
        } while (true);
        return d;
    }
}

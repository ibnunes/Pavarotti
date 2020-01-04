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

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.pavarotti.ui.intf.*;
import org.pavarotti.core.api.Company;
import org.pavarotti.core.components.*;
import org.pavarotti.core.intf.*;
import org.pavarotti.core.throwable.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Program extends Controller {
    /**
     * Stores a new viewer and the respective argument that triggers it via command line
     */
    public static class ViewerInfo {
        public Viewer viewer;
        public String argument;
        
        public ViewerInfo(Viewer viewer, String argument) {
            this.viewer   = viewer;
            this.argument = argument;
        }
    }
    
    /**
     * Stores flags regarding the status of the application
     */
    private class StateMachine {
        protected class Changed {
            public boolean info;
            public boolean shows;
            public boolean singers;
            public boolean dancers;
            public boolean directors;
            
            public Changed() {
                this.info      = false;
                this.dancers   = false;
                this.directors = false;
                this.shows     = false;
                this.singers   = false;
            }
            
            public void reset() {
                this.info      = false;
                this.dancers   = false;
                this.directors = false;
                this.shows     = false;
                this.singers   = false;
            }
        }
        
        protected byte checkfile;
        public Changed changed;
        
        public StateMachine() {
            changed = new Changed();
            checkfile = 0;
        }
    }
    
    /**
     * The Opera Company
     */
    private Company core;
    
    private static final String FNAME_COMPANYINFO  = "compinfo.dat";
    private static final String FNAME_SINGERS      = "perfsing.dat";
    private static final String FNAME_DANCERS      = "perfdanc.dat";
    private static final String FNAME_DIRECTORS    = "director.dat";
    private static final String FNAME_PERFORMANCES = "perfshow.dat";
    
    private static final String ERRMSG_LOADALL =
            "Erro a carregar pelo menos um dos ficheiros. Os dados estão corrompidos.\n";
    private static final String ERRMSG_NOID =
            "ID não existe!\n";
    
    /**
     * Assigns the names of the files
     */
    private void assignFiles() {
        core.setCompanyFileName(FNAME_COMPANYINFO);
        core.singers.setFileName(FNAME_SINGERS);
        core.dancers.setFileName(FNAME_DANCERS);
        core.directors.setFileName(FNAME_DIRECTORS);
        core.setPerformancesFileName(FNAME_PERFORMANCES);
    }
    
    /**
     * Checks if all the files exist and manages to recover from any errors.
     * Uses checksum to do so.
     */
    private void checkFilesIntegrity() {
        flags.checkfile = core.checkFiles();
        
        if (flags.checkfile != Company.CHECK_SUM)
            currentviewer.showWarning(ERRMSG_LOADALL);
        else
            return;
        
        if ((flags.checkfile & Company.CHECK_INFO) != Company.CHECK_INFO) {
            currentviewer.showWarning("O ficheiro de informação da Companhia não existe!\n");
            currentviewer.firstSetup();
            createCompanyInfoFile();
            try {
                core.saveCompanyInfo();
                core.saveDirectors();
                flags.changed.directors = true;
            } catch (IOException e) {
                currentviewer.showError("Erro inesperado ao guardar informação da companhia em disco.\n");
            }
        }
        
        if ((flags.checkfile & Company.CHECK_SINGERS) != Company.CHECK_SINGERS) {
            currentviewer.showWarning("O ficheiro de cantores não existe!\n");
            createStaffFile(core.singers);
        }
        
        if ((flags.checkfile & Company.CHECK_DANCERS) != Company.CHECK_DANCERS) {
            currentviewer.showWarning("O ficheiro de dançarinos não existe!\n");
            createStaffFile(core.dancers);
        }
        
        if ((flags.checkfile & Company.CHECK_DIRECTORS) != Company.CHECK_DIRECTORS) {
            currentviewer.showWarning("O ficheiro de diretores não existe!\n");
            createStaffFile(core.directors);
        }
        
        if ((flags.checkfile & Company.CHECK_PERFORMANCES) != Company.CHECK_PERFORMANCES) {
            currentviewer.showWarning("O ficheiro de espetáculos não existe!\n");
            createPerformancesFile();
        }
    }
    
    /**
     * Creates the company information file if non-existent
     */
    private void createCompanyInfoFile() {
        try {
            if (!core.createCompanyFile()) {
                currentviewer.showWarning(
                        String.format(
                                "Não foi possível criar o ficheiro %s por motivos desconhecidos.\n",
                                core.getCompanyFileName()
                        )
                );
            }
        } catch (IOException ioe) {
            currentviewer.showError(
                    String.format(
                            "Ocorreu um erro ao criar o ficheiro %s.\n",
                            core.getCompanyFileName()
                    )
            );
        } catch (SecurityException se) {
            currentviewer.showError(
                    String.format(
                            "O programa não tem permissões para criar o ficheiro %s em disco.\n",
                            core.getCompanyFileName()
                    )
            );
        }
    }
    
    /**
     * Creates the performances file if non-existent
     */
    private void createPerformancesFile() {
        try {
            if (!core.createPerformancesFile()) {
                currentviewer.showWarning(
                        String.format(
                                "Não foi possível criar o ficheiro %s por motivos desconhecidos.\n",
                                core.getPerformancesFileName()
                        )
                );
            }
        } catch (IOException ioe) {
            currentviewer.showError(
                    String.format(
                            "Ocorreu um erro ao criar o ficheiro %s.\n",
                            core.getPerformancesFileName()
                    )
            );
        } catch (SecurityException se) {
            currentviewer.showError(
                    String.format(
                            "O programa não tem permissões para criar o ficheiro %s em disco.\n",
                            core.getPerformancesFileName()
                    )
            );
        }
    }
    
    /**
     * @param staff the Staff object
     */
    private void createStaffFile(Staff staff) {
        try {
            if (!staff.createFile()) {
                currentviewer.showWarning(
                        String.format(
                                "Não foi possível criar o ficheiro %s por motivos desconhecidos.\n",
                                staff.getFileName()
                        )
                );
            }
        } catch (IOException ioe) {
            currentviewer.showError(
                    String.format(
                            "Ocorreu um erro ao criar o ficheiro %s.\n",
                            staff.getFileName()
                    )
            );
        } catch (SecurityException se) {
            currentviewer.showError(
                    String.format(
                            "O programa não tem permissões para criar o ficheiro %s em disco.\n",
                            staff.getFileName()
                    )
            );
        }
    }
    
    // Properties of the Controller
    private final Version version;
    private Viewer currentviewer;
    private final ArrayList<ViewerInfo> viewers;
    private StateMachine flags;
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    /**
     * Constructor of the class
     * @param version the version of the application
     */
    public Program(Version version) {
        this.currentviewer = null;
        this.viewers       = new ArrayList<>();
        this.version       = version;
        this.startStateMachine();
        this.startCore();
    }
    
    /**
     * Constructor of the class
     * @param major the major version of the application
     * @param minor the minor version of the application
     * @param iteration the iteration version of the application
     * @param stage the stage of the application (Alpha, Beta or Final)
     */
    public Program(int major, int minor, int iteration, Version.Stage stage) {
        this.currentviewer = null;
        this.viewers       = new ArrayList<>();
        this.version       = new Version(major, minor, iteration, stage);
        this.startStateMachine();
        this.startCore();
    }
    
    /**
     * Starts the State Machine of the Controller
     */
    private void startStateMachine() {
        this.flags = new StateMachine();
    }
    
    /**
     * Starts the core API
     */
    private void startCore() {
        this.core = new Company();
    }
    
    /**
     * Processes all arguments via command line
     * @param args the command line arguments
     * @return true if the application should proceed as standardized by the controller
     * @throws Exception 
     */
    private boolean runArgs(String[] args) throws Exception {
        if (args.length == 0)
            return true;
        
        // Transforms all arguments to upper case
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toUpperCase();
        }
        
        switch (args[0]) {
            case "--HELP":     // Access to help, must be first, ignores further arguments
            case "-H":
            case "-?":
                this.currentviewer.showHelp();
                return false;

            case "--ABOUT":    // Access to about, must be first, ignores further arguments
            case "-A":
                this.currentviewer.showAbout();
                return false;

            case "--MODE":      // Changes interface mode, must have binded viewers
            case "-M":
                if (args.length >= 2) {
                    Viewer v = this.getViewer(args[1]);
                    if (v != null)
                        this.bindViewer(v);
                    else
                        throw new Exception(String.format("No viewer found for %s.", args[1]));
                } else {
                    throw new Exception("Insufficient arguments.");
                }
                break;

            default:            // Anything else - invalid arguments
                throw new Exception("Invalid arguments");
        }
        
        return true;
    }
    
    /**
     * Launches the application
     * @param args the command line arguments
     * @throws Exception 
     */
    @Override
    public void launch(String[] args) throws Exception {
        if (this.currentviewer == null)
            throw new Exception("No viewer binded.");
        if (this.viewers.isEmpty())
            throw new Exception("No viewers available.");
        if (runArgs(args)) {
            this.currentviewer.loadViewer();
            assignFiles();
            checkFilesIntegrity();
            if (!core.loadCompanyInfo())
                currentviewer.showError("Ficheiro de informação da Companhia corrompido.\n");
            if (!core.loadSingers())
                currentviewer.showError("Ficheiro de cantores corrompido.\n");
            if (!core.loadDancers())
                currentviewer.showError("Ficheiro de dançarinos corrompido.\n");
            if (!core.loadDirectors())
                currentviewer.showError("Ficheiro de diretores corrompido.\n");
            if (!core.loadPerformances())
                currentviewer.showError("Ficheiro de espetáculos corrompido.\n");
            
            this.currentviewer.launchViewer(args);
        }
    }
    
    /**
     * Manages the end of the application and everything needed to do before closing
     * @throws Exception 
     */
    @Override
    public void stop() throws Exception {
        this.refresh();
    }
    
    /**
     * Refreshes the files depending on the changed status reported by the state machine
     * @throws Exception 
     */
    @Override
    public void refresh() throws Exception {
        if (flags.changed.info)      core.saveCompanyInfo();
        if (flags.changed.singers)   core.saveSingers();
        if (flags.changed.dancers)   core.saveDancers();
        if (flags.changed.directors) core.saveDirectors();
        if (flags.changed.shows)     core.savePerformances();
        flags.changed.reset();
    }
    
    /**
     * Binds the new viewer and loads its components
     * @param viewer the viewer to be binded
     */
    @Override
    public void bindViewer(Viewer viewer) {
        this.currentviewer = viewer;
        this.currentviewer.bindController(this);
    }
    
    /**
     * Adds a new viewer to the list of available viewers
     * @param viewer the viewer to add
     * @param arg the command line argument that should trigger its binding
     */
    @Override
    public void addViewer(Viewer viewer, String arg) {
        this.viewers.add(new ViewerInfo(viewer, arg));
        if (this.viewers.size() == 1)
            this.bindViewer(viewer);
    }
    
    /**
     * Gets the viewer given the index on the list of available viewers
     * @param index the index to get from
     * @return the viewer
     * @throws IndexOutOfBoundsException 
     */
    @Override
    public Viewer getViewer(int index) throws IndexOutOfBoundsException {
        return this.viewers.get(index).viewer;
    }
    
    /**
     * Gets the viewer given the correspondent command line argument 
     * @param arg the command line argument
     * @return the viewer
     */
    @Override
    public Viewer getViewer(String arg) {
        for (int i = 0; i < this.viewers.size(); i++) {
            if (arg.equals(this.viewers.get(i).argument)) {
                return this.viewers.get(i).viewer;
            }
        }
        return null;
    }
    
    /**
     * Gets the version of the application
     * @return the version
     */
    @Override
    public Version getVersion() {
        return this.version;
    }
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    @Override
    public void setupCompanyInfo(String name, String city, String country, Integer director) {
        try {
            core.info.setName(name);
            core.info.setCity(city);
            core.info.setCountry(country);
            core.info.setDirector(director);
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro inesperado ao definir as informações da Companhia.\n");
        }
        flags.changed.info = true;
    }
    
    @Override
    public String getCompanyName() {
        return core.getCompanyName();
    }
    
    @Override
    public String getCompanyCity() {
        return core.getCompanyCity();
    }
    
    @Override
    public String getCompanyCountry() {
        return core.getCompanyCountry();
    }
    
    @Override
    public Integer getCompanyDirector() {
        return core.getCompanyDirector();
    }
    
    @Override
    public String getCompanyDirectorName() {
        return core.getCompanyDirectorName();
    }
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    /**
     * Converts a character to a gender
     * @param gender the gender of the singer ('M' for male, 'F' for female, other char for other genders).
     * @return the gender enumerator
     */
    private Person.Gender charToGender(char gender) {
        return
                (gender == 'M') ? Person.Gender.Male :
                (gender == 'F') ? Person.Gender.Female :
                Person.Gender.Other;
    }
    
    /**
     * Generic method to add a staff member to a staff list
     * @param staff the staff class where to add
     * @param name the name to set
     * @param gender the gender to set
     * @param position the position of the staff member
     * @param LIST the list of staff to allow the generation of a distinct ID
     * @return true if created successfully, false otherwise
     */
    private boolean newStaff(boolean isDirector, Staff staff, String name, char gender, String position, LocalDate birthday, final ArrayList<StaffMember>[] LIST) {
        try {
            if (isDirector)
                staff.insert(new Director(name, charToGender(gender), birthday, position, LocalDate.now(), LIST[0]));
            else
                staff.insert(new Performer(name, charToGender(gender), birthday, position, LocalDate.now(), LIST));
        } catch (Exception e) {
            this.currentviewer.showError(String.format("ERROR: %s: %s\n", e.getClass(), e.getMessage()));
            return false;
        }
        return true;
    }
    
    /**
     * @param name the name of the singer
     * @param gender the gender of the singer ('M' for male, 'F' for female, other char for other genders).
     * @param position the position of the singer
     * @param birthday the birthday
     * @return true if created successfully, false otherwise
     */
    @Override
    public boolean newSinger(String name, char gender, String position, LocalDate birthday) {
        boolean ok =
                newStaff(
                        false, core.singers, name, gender, position, birthday,
                        new ArrayList[] {core.singers.getList(), core.dancers.getList()}
                );
        flags.changed.singers = ok;
        return ok;
    }
    
    /**
     * @param name the name of the dancer
     * @param gender the gender of the dancer ('M' for male, 'F' for female, other char for other genders).
     * @param position the position of the dancer
     * @param birthday the birthday
     * @return true if created successfully, false otherwise
     */
    @Override
    public boolean newDancer(String name, char gender, String position, LocalDate birthday) {
        boolean ok =
                newStaff(
                        false, core.dancers, name, gender, position, birthday,
                        new ArrayList[] {core.singers.getList(), core.dancers.getList()}
                );
        flags.changed.dancers = ok;
        return ok;
    }
    
    /**
     * @param name the name of the director
     * @param gender the gender of the director ('M' for male, 'F' for female, other char for other genders).
     * @param position the position of the director
     * @param birthday the birthday
     * @return true if created successfully, false otherwise
     */
    @Override
    public boolean newDirector(String name, char gender, String position, LocalDate birthday) {
        boolean ok =
                newStaff(
                        true, core.directors, name, gender, position, birthday,
                        new ArrayList[] {core.directors.getList()}
                );
        flags.changed.directors = ok;
        return ok;
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @return the list of all singers
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> getAllSingers() {
        return (ArrayList<T>) this.core.singers.getList();
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @return the list of all dancers
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> getAllDancers() {
        return (ArrayList<T>) this.core.dancers.getList();
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @return the list of all directors
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> getAllDirectors() {
        return (ArrayList<T>) this.core.directors.getList();
    }
    
    /**
     * @param ID the ID to remove
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean deleteSinger(int ID) {
        boolean ok = deleteStaff(core.singers, ID);
        flags.changed.singers = ok;
        return ok;
    }
    
    /**
     * @param ID the ID to remove
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean deleteDancer(int ID) {
        boolean ok = deleteStaff(core.dancers, ID);
        flags.changed.dancers = ok;
        return ok;
    }
    
    /**
     * @param ID the ID to remove
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean deleteDirector(int ID) {
        boolean ok = deleteStaff(core.directors, ID);
        flags.changed.directors = ok;
        return ok;
    }
    
    /**
     * @param staff the staff list where to search from
     * @param ID the ID to remove
     * @return true if deleted, false otherwise
     */
    public boolean deleteStaff(Staff staff, final int ID) {
        try {
            staff.delete(staff.search(ID));
        } catch (IndexOutOfBoundsException e) {
            currentviewer.showError(ERRMSG_NOID);
            return false;
        }
        return true;
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID to search
     * @return the object, or null if not found
     */
    @Override
    public <T extends Person & StaffMember> T searchSinger(int ID) {
        return searchStaff(ID, (Staff) core.singers);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID to search
     * @return the object, or null if not found
     */
    @Override
    public <T extends Person & StaffMember> T searchDancer(int ID) {
        return searchStaff(ID, (Staff) core.dancers);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID to search
     * @return the object, or null if not found
     */
    @Override
    public <T extends Person & StaffMember> T searchDirector(int ID) {
        return searchStaff(ID, (Staff) core.directors);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID to search
     * @param staff the staff list where to search
     * @return the object, or null if not found
     */
    private <T extends Person & StaffMember> T searchStaff(int ID, Staff staff) {
        int index = staff.search(ID);
        try {
            return (T) staff.peek(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param name the name to search for
     * @return the list that matches the given name
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> searchSingers(String name) {
        return searchStaff(core.singers, name);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param name the name to search for
     * @return the list that matches the given name
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> searchDancers(String name) {
        return searchStaff(core.dancers, name);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param name the name to search for
     * @return the list that matches the given name
     */
    @Override
    public <T extends Person & StaffMember> ArrayList<T> searchDirectors(String name) {
        return searchStaff(core.directors, name);
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param staff the staff where to search
     * @param name the name to search for
     * @return the list that matches the given name
     */
    private <T extends Person & StaffMember> ArrayList<T> searchStaff(Staff staff, String name) {
        return staff.getAllByName(name);
    }
    
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID of the singer to change
     * @param t new new singer
     */
    @Override
    public <T extends Person & StaffMember> void modifySinger(int ID, T t) {
        modifyStaff(core.singers, ID, t);
        flags.changed.singers = true;
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID of the singer to change
     * @param t the new dancer
     */
    @Override
    public <T extends Person & StaffMember> void modifyDancer(int ID, T t) {
        modifyStaff(core.dancers, ID, t);
        flags.changed.dancers = true;
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param ID the ID of the singer to change
     * @param t the new director
     */
    @Override
    public <T extends Person & StaffMember> void modifyDirector(int ID, T t) {
        modifyStaff(core.directors, ID, t);
        flags.changed.directors = true;
    }
    
    /**
     * @param <T> a class that extends Person and implements StaffMember
     * @param staff the staff list where to modify
     * @param ID the ID
     * @param t the new staff member
     */
    private <T extends Person & StaffMember> void modifyStaff(Staff staff, int ID, T t) {
        try {
            staff.modify(staff.search(ID), t);
        } catch (IndexOutOfBoundsException e) {
            currentviewer.showError(ERRMSG_NOID);
        }
    }
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    /**
     * @param ID the ID to check
     * @return true if the ID exists, false otherwise
     */
    @Override
    public boolean performanceIDExists(String ID) {
        return core.hasPerformanceID(ID);
    }
    
    /**
     * @param ID the ID
     * @param name the name
     * @param when the dates
     * @param basePrice the price
     * @param operadirector the opera director
     * @param castingdirector the casting director
     * @param singers the singers
     * @param dancers the dancers
     * @return true if successfully created
     */
    @Override
    public boolean newPerformance
            (
                    String ID, String name, ArrayList<LocalDateTime> when, Double basePrice,
                    Integer operadirector, Integer castingdirector,
                    ArrayList<Integer> singers, ArrayList<Integer> dancers
            )
    {
        try {
            Performance p = new Performance(ID, name, basePrice, when);
            p.setCastingDirector(castingdirector);
            p.setOperaDirector(operadirector);
            p.setSingers(singers);
            p.setDancers(dancers);
            core.addPerformance(p);
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro inesperado ao criar o espetáculo.\n");
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    /**
     * @return the list of performances
     */
    @Override
    public ArrayList<Performance> getAllPerformances() {
        return core.getPerformances();
    }
    
    /**
     * @param after the inferior limit
     * @param before the superior limit
     * @return the list of matching performances
     */
    @Override
    public ArrayList<Performance> searchPerformanceByDate(LocalDateTime after, LocalDateTime before) {
        core.sortPerformancesByDate();
        final ArrayList<Performance> list = core.getPerformances();
        ArrayList<Performance> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getHall().get(0).getWhen().compareTo(after) >= 0)
                result.add(list.get(i));
            if (list.get(i).getHall().get(0).getWhen().compareTo(before) >= 0)
                break;
        }
        core.sortPerformancesByID();
        return result;
    }
    
    /**
     * @param ID the ID to search
     * @return the performance
     */
    @Override
    public Performance searchPerformance(String ID) {
        return core.getPerformanceByID(ID);
    }
    
    /**
     * @param ID the ID which performance is to be removed
     * @return true if removes, false otherwise
     */
    @Override
    public boolean deletePerformance(String ID) {
        boolean ok = false;
        try {
            ok = core.removePerformance(ID);
            flags.changed.shows = ok;
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro inesperado ao remover o espetáculo.\n");
        }
        return ok;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param name the new name
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformanceName(String ID, String name) {
        try {
            core.getPerformanceByID(ID).setName(name);
        } catch (Exception e) {
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param when the new dates and times
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformanceWhen(String ID, ArrayList<LocalDateTime> when) {
        try {
            core.getPerformanceByID(ID).setWhen(when);
        } catch (Exception e) {
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param singers the new singers list
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformanceSingers(String ID, ArrayList<Integer> singers) {
        try {
            core.getPerformanceByID(ID).setSingers(singers);
        } catch (Exception e) {
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param dancers the new dancers list
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformanceDancers(String ID, ArrayList<Integer> dancers) {
        try {
            core.getPerformanceByID(ID).setDancers(dancers);
        } catch (Exception e) {
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param opera the new opera director
     * @param casting the new casting director
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformanceDirectors(String ID, Integer opera, Integer casting) {
        try {
            core.getPerformanceByID(ID).setCastingDirector(casting);
            flags.changed.shows = true;
            core.getPerformanceByID(ID).setOperaDirector(opera);
            flags.changed.shows = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * @param ID the ID which performance is to be modified
     * @param price the new price
     * @return true if changed, false otherwise
     */
    @Override
    public boolean modifyPerformancePrice(String ID, Double price) {
        try {
            core.getPerformanceByID(ID).setBasePrice(price);
        } catch (Exception e) {
            return false;
        }
        flags.changed.shows = true;
        return true;
    }
    
    @Override
    public double getPerformancePrice(String ID) {
        try {
            return core.getPerformanceByID(ID).getBasePrice();
        } catch (Exception e) {
            return 0.;
        }
    }
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    @Override
    public Ticket getTicketInfo(String showID, String seat, int index) {
        try {
            return core.getTicketInfo(showID, seat, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Provavelmente não há datas marcadas.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a pesquisar o preço.\n");
        }
        return null;
    }
    
    /**
     * @param showID the show ID
     * @param seat the seat
     * @return the price
     */
    @Override
    public Double getTicketPrice(String showID, String seat) {
        try {
            return core.getTicketPrice(showID, seat);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Provavelmente não há datas marcadas.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a pesquisar o preço.\n");
        }
        return 0.;
    }
    
    /**
     * @param showID the show ID
     * @param seat the seat
     * @param index the index related to the day and time
     * @return the price
     */
    @Override
    public boolean isTicketSold(String showID, String seat, int index) {
        try {
            return core.isTicketSold(showID, seat, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a verificar o bilhete.\n");
        }
        return false;
    }
    
    /**
     * @param showID the show ID
     * @param seat the seat
     * @return the price
     * @throws TicketNotSoldException
     */
    @Override
    public LocalDateTime whenTicketSold(String showID, String seat, int index) throws TicketNotSoldException {
        try {
            return core.whenTicketSold(showID, seat, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a verificar o bilhete.\n");
        }
        return LocalDateTime.now();
    }
    
    /**
     * @param showID the show ID
     * @param seat the seat
     * @return the price
     * @throws TicketSoldException
     */
    @Override
    public boolean sellTicket(String showID, String seat, int index) throws TicketSoldException {
        try {
            core.sellTicket(showID, seat, index);
            flags.changed.shows = true;
            return true;
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a vender o bilhete.\n");
        }
        return false;
    }
    
    /**
     * @param showID the show ID
     * @param seat the seat
     * @return the price
     * @throws TicketNotSoldException
     */
    @Override
    public boolean refundTicket(String showID, String seat, int index) throws TicketNotSoldException {
        try {
            core.refundTicket(showID, seat, index);
            flags.changed.shows = true;
            return true;
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (SeatNotFoundException snfe) {
            currentviewer.showError("O lugar " + snfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a devolver o bilhete.\n");
        }
        return false;
    }
    
    @Override
    public boolean hasAvailableTickets(String showID, int index) {
        try {
            return core.hasAvailableTickets(showID, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a devolver o bilhete.\n");
        }
        return false;
    }
    
    /**
     * @param showID the performance ID
     * @param index the index of the date
     * @return the number of tickets available
     */
    @Override
    public Integer ticketsAvailable(String showID, int index) {
        try {
            return core.ticketsAvailable(showID, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a verificar os bilhetes.\n");
        }
        return 0;
    }
    
    /**
     * @param showID the performance ID
     * @param index the index of the date
     * @return the number of tickets sold
     */
    @Override
    public Integer ticketsSold(String showID, int index) {
        try {
            return core.ticketsSold(showID, index);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a verificar os bilhetes.\n");
        }
        return 0;
    }
    
    /**
     * @param showID the performance ID
     * @return the capacity of the hall
     */
    @Override
    public Integer hallCapacity(String showID) {
        try {
            return core.hallCapacity(showID);
        } catch (PerformanceNotFoundException pnfe) {
            currentviewer.showError("O espetáculo " + pnfe.getMessage() + " não existe!\n");
        } catch (IndexOutOfBoundsException iobe) {
            currentviewer.showError("Acedeu a uma data não existente.\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a verificar os bilhetes.\n");
        }
        return 0;
    }
    
    /**
     * @param ID the performance ID
     * @param index the index
     * @return the hall matrix
     */
    @Override
    public ArrayList<ArrayList<Boolean>> hallSoldMatrix(String ID, int index) {
        return core.getPerformanceByID(ID).getHall(index).hallSoldMatrix();
    }
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    /**
     * @param showID the performance ID
     * @return the average spectators of the performance
     */
    @Override
    public Double averageSpectators(String showID) {
        try {
            return core.stats.averageSpectators(showID);
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a calcular a média.\n");
        }
        return -1.;
    }
    
    /**
     * @param showID the performance ID
     * @return the best performance day
     */
    @Override
    public LocalDateTime bestPerformanceDay(String showID) {
        try {
            return core.stats.bestPerformanceDay(showID);
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a obter o melhor dia.\n");
        }
        return null;
    }
    
    /**
     * @param showID the performance ID
     * @return the worst performance day
     */
    @Override
    public LocalDateTime worstPerformanceDay(String showID) {
        try {
            return core.stats.worstPerformanceDay(showID);
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a obter o pior dia.\n");
        }
        return null;
    }
    
    /**
     * @return the most watched performance of all time
     */
    @Override
    public String mostWatchedPerformance() {
        try {
            return core.stats.mostWatchedPerformance();
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a determinar o melhor espetáculo.\n");
        }
        return "";
    }
    
    /**
     * @return the least watched performance of all time
     */
    @Override
    public String leastWatchedPerformance() {
        try {
            return core.stats.leastWatchedPerformance();
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a determinar o pior espetáculo.\n");
        }
        return "";
    }
    
    /**
     * @param ID the performance ID
     * @return the summation of spectators
     */
    @Override
    public long sumAllSpectators(String ID) {
        try {
            return core.stats.sumAllSpectators(ID);
        } catch (PerformanceNotFoundException e) {
            currentviewer.showError("O espetáculo " + e.getMessage() + " não existe!\n");
        } catch (Exception e) {
            currentviewer.showError("Ocorreu um erro grave a determinar os espetadores.\n");
        }
        return -1;
    }
}
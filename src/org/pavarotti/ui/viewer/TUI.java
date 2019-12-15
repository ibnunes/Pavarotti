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
package org.pavarotti.ui.viewer;

import org.pavarotti.ui.intf.Controller;
import org.pavarotti.ui.intf.Viewer;
import org.util.*;
import org.pavarotti.ui.viewer.tui.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class TUI implements Viewer {
    private static final String NOOPTMSG = "Opção inválida: não existe no menu.";
    private static final String ERRMSG   = "Opção inválida: erro na leitura.";

    private Menu main;
    private Menu staff;
    private Menu singers;
    private Menu dancers;
    private Menu directors;
    private Menu tickets;
    private Menu stats;
    private Menu shows;
    private Menu peeksingers;
    private Menu peekdancers;
    private Menu peekdirectors;
    private Menu peekperformances;
    private Menu modifyperformances;
    
    private Controller controller;

    /**
    * Loads all necessary TUI elements and other objects of broad use
    */
    @Override
    public void load() {
        main = new Menu("MENU PRINCIPAL");
        main.addItem("Gerir staff"       , () -> runMenu(staff));
        main.addItem("Gerir espectaculos", () -> runMenu(shows));
        main.addItem("Vender bilhetes"   , () -> runMenu(tickets));
        main.addItem("Estatísticas"      , () -> runMenu(stats));
        main.addItem("Acerca"            , () -> showAbout());
        main.addItem("Ajuda"             , () -> showHelp());
        main.addItem("SAIR"              , Menu.NOTHING, true);
        
        staff = new Menu("GESTÃO DO STAFF");
        staff.addItem("Gerir cantores"    , () -> runMenu(singers));
        staff.addItem("Gerir dançarinos"  , () -> runMenu(dancers));
        staff.addItem("Gerir directores"  , () -> runMenu(directors));
        staff.addItem("SAIR"              , Menu.NOTHING, true);

        singers = new Menu("CANTORES");
        singers.addItem("Inserir"  , () -> StaffMgr.makeSinger());
        singers.addItem("Modificar", () -> StaffMgr.changeSinger());
        singers.addItem("Apagar"   , () -> StaffMgr.deleteSinger());
        singers.addItem("Consultar", () -> runMenu(peeksingers));
        singers.addItem("Listar"   , () -> StaffMgr.listSingers());
        singers.addItem("SAIR"     , Menu.NOTHING, true);
        
        dancers = new Menu("DANÇARINOS");
        dancers.addItem("Inserir"  , () -> StaffMgr.makeDancer());
        dancers.addItem("Modificar", () -> StaffMgr.changeDancer());
        dancers.addItem("Apagar"   , () -> StaffMgr.deleteDancer());
        dancers.addItem("Consultar", () -> runMenu(peekdancers));
        dancers.addItem("Listar"   , () -> StaffMgr.listDancers());
        dancers.addItem("SAIR"     , Menu.NOTHING, true);

        directors = new Menu("DIRECTORES");
        directors.addItem("Inserir"  , () -> StaffMgr.makeDirector());
        directors.addItem("Modificar", () -> StaffMgr.changeDirector());
        directors.addItem("Apagar"   , () -> StaffMgr.deleteDirector());
        directors.addItem("Consultar", () -> runMenu(peekdirectors));
        directors.addItem("Listar"   , () -> StaffMgr.listDirectors());
        directors.addItem("SAIR"     , Menu.NOTHING, true);

        shows = new Menu("ESPECTACULOS");
        shows.addItem("Inserir"  , () -> PerformanceMgr.make());
        shows.addItem("Modificar", () -> runMenu(modifyperformances));
        shows.addItem("Apagar"   , () -> PerformanceMgr.delete());
        shows.addItem("Consultar", () -> runMenu(peekperformances));
        shows.addItem("Listar"   , () -> PerformanceMgr.showAll());
        shows.addItem("SAIR"     , Menu.NOTHING, true);
        
        peeksingers = new Menu("CONSULTAR CANTORES");
        peeksingers.addItem("Consultar por ID"  , () -> StaffMgr.peekSingerByID());
        peeksingers.addItem("Consultar por nome", () -> StaffMgr.peekSingerByName());
        peeksingers.addItem("SAIR"              , Menu.NOTHING, true);
        
        peekdancers = new Menu("CONSULTAR DANÇARINOS");
        peekdancers.addItem("Consultar por ID"  , () -> StaffMgr.peekDancerByID());
        peekdancers.addItem("Consultar por nome", () -> StaffMgr.peekDancerByName());
        peekdancers.addItem("SAIR"              , Menu.NOTHING, true);
        
        peekdirectors = new Menu("CONSULTAR DIRETORES");
        peekdirectors.addItem("Consultar por ID"  , () -> StaffMgr.peekDirectorByID());
        peekdirectors.addItem("Consultar por nome", () -> StaffMgr.peekDirectorByName());
        peekdirectors.addItem("SAIR"              , Menu.NOTHING, true);
        
        peekperformances = new Menu("CONSULTAR ESPETÁCULOS");
        peekperformances.addItem("Consultar por ID"  , () -> PerformanceMgr.peekByID());
        peekperformances.addItem("Consultar por data", () -> PerformanceMgr.peekByDate());
        peekperformances.addItem("SAIR"              , Menu.NOTHING, true);
        
        modifyperformances = new Menu("MODIFICAR ESPETÁCULOS");
        modifyperformances.addItem("Modificar nome"       , () -> PerformanceMgr.changeName());
        modifyperformances.addItem("Modificar datas"      , () -> PerformanceMgr.changeWhen());
        modifyperformances.addItem("Modificar cantores"   , () -> PerformanceMgr.changeSingers());
        modifyperformances.addItem("Modificar dançarinos" , () -> PerformanceMgr.changeDancers());
        modifyperformances.addItem("Modificar diretores"  , () -> PerformanceMgr.changeDirectors());
        modifyperformances.addItem("Modificar preço base" , () -> PerformanceMgr.changePrice());
        modifyperformances.addItem("SAIR"                 , Menu.NOTHING, true);
        
        tickets = new Menu("VENDA DE BILHETES");
        tickets.addItem("Venda"    , () -> TicketMgr.sell());
        tickets.addItem("Devolução", () -> TicketMgr.refund());
        tickets.addItem("SAIR"     , Menu.NOTHING, true);
        
        stats = new Menu("ESTATÍSTICAS");
        stats.addItem("Total de espetadores de um espetáculo"   , () -> StatMgr.sumAllSpectators());
        stats.addItem("Média de espetadores de um espetáculo"   , () -> StatMgr.averageSpectators());
        stats.addItem("Dia com mais espetadores num espetáculo" , () -> StatMgr.bestPerformanceDay());
        stats.addItem("Dia com menos espetadores num espetáculo", () -> StatMgr.worstPerformanceDay());
        stats.addItem("Espetáculo mais visto"                   , () -> StatMgr.mostWatchedPerformance());
        stats.addItem("Espetáculo menos visto"                  , () -> StatMgr.leastWatchedPerformance());
        stats.addItem("SAIR", Menu.NOTHING, true);
    }

    /**
     * Generic method to run the menus of this application
     * @param menu the menu to be run
     */
    private void runMenu(Menu menu) {
        if (!menu.hasExit()) {
            showError("Menu mal construído.\nParagem de emergência!\n=== HALT! ===\n");
            System.exit(-1);
        }
        
        short option;
        do {
            menu.show();
            option = menu.getOption("Opção: ", NOOPTMSG, ERRMSG);
            System.out.printf("\n");    // just for aesthetics
            try {
                menu.execute(option);
                controller.refresh();
            } catch (Exception e) {
                printExceptionOnMenu(option, menu.getTitle(), e);
            }
        } while (option != menu.getExit());
    }
    
    /**
     * Outputs a message whenever an error occurs while executing a menu option
     * @param option the option executed
     * @param title the title of the menu
     * @param e the Exception thrown
     */
    private static void printExceptionOnMenu(short option, String title, Exception e) {
        System.out.printf("Erro a executar opção %d do menu \"%s\":\n\t%s\n", option, title, e.getMessage());
    }
    
    /**
     * Makes the first setup of the company information
     */
    @Override
    public void firstSetup() {
        InfoMgr.firstSetup();
    }
    
    /**
     * Shows help regarding the application
     */
    @Override
    public void showHelp() {
        String version = controller.getVersion().toString();
        System.out.printf(Colors.WHITE_BACKGROUND + Colors.BLACK + "PAVAROTTI, versão %s" + Colors.RESET + "\n", version);
        System.out.printf("Esta aplicação assume o seguinte:\n");
        System.out.printf("\t1) A Companhia de Ópera gere vários espetáculos;\n");
        System.out.printf("\t2) Cada espetáculo tem o seu próprio staff independente;\n");
        System.out.printf("\t3) Existe apenas uma Sala de Espetáculos.\n");
        System.out.printf("O utilizador pode gerir todo o staff e os espetáculos.\n");
        System.out.printf("Para cada espetáculo, só é permitido inserir staff previamente criado.\n");
        System.out.printf("Estatística essencial é providenciada.\n\n");
        Flow.pause();
    }
    
    /**
     * Shows information about the application
     */
    @Override
    public void showAbout() {
        String version = controller.getVersion().toString();
        System.out.printf(Colors.WHITE_BACKGROUND + Colors.BLACK + "===== PAVAROTTI =====" + Colors.RESET + "\n");
        System.out.printf("Opera Company Manager\n\n");
        System.out.printf(" Versão: %s\n", version);
        System.out.printf("Autores: Ovelhas do Presépio\n\n");
        System.out.printf("INFORMAÇÃO DA COMPANHIA:\n");
        System.out.printf("\t   Name: %s\n",   controller.getCompanyName());
        System.out.printf("\t   País: %s\n",   controller.getCompanyCountry());
        System.out.printf("\t Cidade: %s\n",   controller.getCompanyCity());
        System.out.printf("\tDiretor: %s\n\n", controller.getCompanyDirectorName());
        Flow.pause();
    }
    
    /**
     * @param msg the message to show
     */
    @Override
    public void showInfo(String msg) {
        System.out.printf("Informação: %s", msg);
    }
    
    /**
     * @param msg the message to show
     */
    @Override
    public void showMessage(String msg) {
        System.out.printf("%s", msg);
    }
    
    /**
     * @param msg the message to show
     */
    @Override
    public void showWarning(String msg) {
        System.out.printf("ATENÇÃO! %s", msg);
    }
    
    /**
     * @param msg the message to show
     */
    @Override
    public void showError(String msg) {
        System.out.printf("ERRO! %s", msg);
    }
    
    /**
     * Launches the TUI application
     * @param args the command line arguments
     * @throws Exception 
     */
    @Override
    public void launch(String[] args) throws Exception {
        this.runMenu(this.main);
        this.controller.stop();
    }
    
    /**
     * @param controller the controller to bind
     */
    @Override
    public void bindController(Controller controller) {
        this.controller = controller;
        InfoMgr.bind(controller, this);
        StaffMgr.bind(controller, this);
        PerformanceMgr.bind(controller, this);
        TicketMgr.bind(controller, this);
        StatMgr.bind(controller, this);
        Flow.bind(this);
    }
    
    /**
     * @return a String representation of the class
     */
    @Override
    public String toString() {
        return
                String.format(
                        "TUI {\n\tController binded: %s\n}", 
                        (this.controller == null) ? "NO" : "YES"
                );
    }
    
    /**
     * The constructor of the class
     */
    public TUI() {
        /* Loader is called by controller when necessary */
    }
}

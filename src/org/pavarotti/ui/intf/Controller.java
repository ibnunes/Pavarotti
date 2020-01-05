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

import java.time.*;
import java.util.ArrayList;

import org.pavarotti.core.intf.*;
import org.pavarotti.core.components.*;
import org.pavarotti.core.throwable.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public abstract class Controller {
    // -------------------------------------------------------------------------
    // Controller initialization and application startup
    public abstract void launch(String[] args) throws Exception;
    public abstract void stop() throws Exception;
    public abstract void emergencyStop();
    public abstract void refresh() throws Exception;
    public abstract void bindViewer(Viewer viewer);
    public abstract Viewer getViewer(int index) throws IndexOutOfBoundsException;
    public abstract Viewer getViewer(String arg);
    public abstract void addViewer(Viewer viewer, String arg);
    public abstract Versioning getVersion();
    
    
    // -------------------------------------------------------------------------
    // API to communicate with the Model
    
    // Company information management
    public abstract void setupCompanyInfo(String name, String city, String country, Integer director);
    public abstract String getCompanyName();
    public abstract String getCompanyCity();
    public abstract String getCompanyCountry();
    public abstract Integer getCompanyDirector();
    public abstract String getCompanyDirectorName();
    
    // Staff management
    public abstract boolean newSinger(String name, char gender, String position, LocalDate birthday);
    public abstract boolean newDancer(String name, char gender, String position, LocalDate birthday);
    public abstract boolean newDirector(String name, char gender, String position, LocalDate birthday);
    public abstract <T extends Person & StaffMember> ArrayList<T> getAllSingers();
    public abstract <T extends Person & StaffMember> ArrayList<T> getAllDancers();
    public abstract <T extends Person & StaffMember> ArrayList<T> getAllDirectors();
    public abstract boolean deleteSinger(int ID);
    public abstract boolean deleteDancer(int ID);
    public abstract boolean deleteDirector(int ID);
    public abstract <T extends Person & StaffMember> T searchSinger(int ID);
    public abstract <T extends Person & StaffMember> T searchDancer(int ID);
    public abstract <T extends Person & StaffMember> T searchDirector(int ID);
    public abstract <T extends Person & StaffMember> ArrayList<T> searchSingers(String name);
    public abstract <T extends Person & StaffMember> ArrayList<T> searchDancers(String name);
    public abstract <T extends Person & StaffMember> ArrayList<T> searchDirectors(String name);
    public abstract <T extends Person & StaffMember> void modifySinger(int ID, T t);
    public abstract <T extends Person & StaffMember> void modifyDancer(int ID, T t);
    public abstract <T extends Person & StaffMember> void modifyDirector(int ID, T t);
    
    // Performances management
    public abstract boolean performanceIDExists(String ID);
    public abstract boolean newPerformance
            (
                    String ID, String name, ArrayList<LocalDateTime> when, Double basePrice,
                    Integer operadirector, Integer castingdirector,
                    ArrayList<Integer> singers, ArrayList<Integer> dancers
            );
    public abstract ArrayList<Performance> getAllPerformances();
    public abstract Performance searchPerformance(String ID);
    public abstract ArrayList<Performance> searchPerformanceByDate(LocalDateTime after, LocalDateTime before);
    public abstract boolean deletePerformance(String ID);
    public abstract boolean modifyPerformanceName(String ID, String name);
    public abstract boolean modifyPerformanceWhen(String ID, ArrayList<LocalDateTime> when);
    public abstract boolean modifyPerformanceSingers(String ID, ArrayList<Integer> singers);
    public abstract boolean modifyPerformanceDancers(String ID, ArrayList<Integer> dancers);
    public abstract boolean modifyPerformanceDirectors(String ID, Integer opera, Integer casting);
    public abstract boolean modifyPerformancePrice(String ID, Double price);
    public abstract double getPerformancePrice(String ID);
    
    // Tickets management
    public abstract Ticket getTicketInfo(String showID, String seat, int index);
    public abstract Double getTicketPrice(String showID, String seat);
    public abstract boolean isTicketSold(String showID, String seat, int index);
    public abstract LocalDateTime whenTicketSold(String showID, String seat, int index) throws TicketNotSoldException;
    public abstract boolean sellTicket(String showID, String seat, int index) throws TicketSoldException;
    public abstract boolean refundTicket(String showID, String seat, int index) throws TicketNotSoldException;
    public abstract boolean hasAvailableTickets(String showID, int index);
    public abstract Integer ticketsAvailable(String showID, int index);
    public abstract Integer ticketsSold(String showID, int index);
    public abstract Integer hallCapacity(String showID);
    public abstract ArrayList<ArrayList<Boolean>> hallSoldMatrix(String ID, int index);
    
    // Statistics
    public abstract Double averageSpectators(String ID);
    public abstract LocalDateTime bestPerformanceDay(String ID);
    public abstract LocalDateTime worstPerformanceDay(String ID);
    public abstract String mostWatchedPerformance();
    public abstract String leastWatchedPerformance();
    public abstract long sumAllSpectators(String ID);
}

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
package org.pavarotti.core.api;

import java.util.ArrayList;
import java.io.*;
import java.time.LocalDateTime;

import org.dma.io.Streamer;
import org.pavarotti.core.components.*;
import org.pavarotti.core.throwable.*;
import org.pavarotti.core.stat.*;
import org.dma.io.Disk;

/**
 * This is an API to the core of the application
 * @author Ovelhas do Presépio
 */
public class Company {    
    private       ArrayList<Performance> performances;
    private final Streamer<Performance>  perfstreamer;
    
    public        CompanyInfo           info;
    private final Streamer<CompanyInfo> infostreamer;
    
    public Staff<Performer> singers;
    public Staff<Performer> dancers;
    public Staff<Director>  directors;
    
    public Statistics stats;
    
    /**
     * The constructor of the class
     */
    public Company() {
        this.info         = new CompanyInfo("", "", "", 0);
        this.singers      = new Staff<>();
        this.dancers      = new Staff<>();
        this.directors    = new Staff<>();
        this.performances = new ArrayList<>();
        this.perfstreamer = new Streamer<>();
        this.infostreamer = new Streamer<>();
        this.stats        = new Statistics(this);
    }
    
    /**
     * The constructor of the class
     * @param name the name of the company
     * @param city the city
     * @param country the country
     */
    public Company(String name, String city, String country) {
        this.info         = new CompanyInfo(name, city, country, 0);
        this.singers      = new Staff<>();
        this.dancers      = new Staff<>();
        this.directors    = new Staff<>();
        this.performances = new ArrayList<>();
        this.perfstreamer = new Streamer<>();
        this.infostreamer = new Streamer<>();
        this.stats        = new Statistics(this);
    }
    
    /**
     * The constructor of the class
     * @param name the name of the company
     * @param city the city
     * @param country the country
     * @param companydirector the director of the company
     */
    public Company(String name, String city, String country, Integer companydirector) {
        this.info         = new CompanyInfo(name, city, country, companydirector);
        this.singers      = new Staff<>();
        this.dancers      = new Staff<>();
        this.directors    = new Staff<>();
        this.performances = new ArrayList<>();
        this.perfstreamer = new Streamer<>();
        this.infostreamer = new Streamer<>();
        this.stats        = new Statistics(this);
    }
    
    public static final byte CHECK_INFO         = 1;
    public static final byte CHECK_SINGERS      = 2;
    public static final byte CHECK_DANCERS      = 4;
    public static final byte CHECK_DIRECTORS    = 8;
    public static final byte CHECK_PERFORMANCES = 16;
    public static final byte CHECK_SUM =
            CHECK_INFO + CHECK_SINGERS + CHECK_DANCERS +
            CHECK_DIRECTORS + CHECK_PERFORMANCES;
    
    /**
     * Checks if all files exist
     * @return A checksum that indicates the files missing where
     *       1 - company info,
     *       2 - singers,
     *       4 - dancers,
     *       8 - performances,
     *      16 - directors.
     */
    public byte checkFiles() {
        byte check = 0;
        if (Disk.fileExists(this.infostreamer.getFileName())) check += CHECK_INFO;
        if (Disk.fileExists(this.singers.getFileName()))      check += CHECK_SINGERS;
        if (Disk.fileExists(this.dancers.getFileName()))      check += CHECK_DANCERS;
        if (Disk.fileExists(this.directors.getFileName()))    check += CHECK_DIRECTORS;
        if (Disk.fileExists(this.perfstreamer.getFileName())) check += CHECK_PERFORMANCES;
        return check;
    }
    
    /**
     * @return true if there is no company information, false otherwise
     */
    public boolean needsFirstSetup() {
        return ((this.checkFiles() & CHECK_INFO) == 0);
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadAll() throws IOException {
        return
                this.loadCompanyInfo() &&
                this.loadDancers() &&
                this.loadDirectors() &&
                this.loadPerformances() &&
                this.loadSingers();
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadCompanyInfo() throws IOException {
        return (info = this.infostreamer.loadFromFile()) != null;
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadSingers() throws IOException {
        return this.singers.loadFromFile();
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadDancers() throws IOException {
        return this.dancers.loadFromFile();
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadDirectors() throws IOException {
        return this.directors.loadFromFile();
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadPerformances() throws IOException {
        return (this.performances = this.perfstreamer.loadAllFromFile()) != null;
    }
    
    /**
     * @return true if file created successfully, false otherwise
     * @throws IOException 
     */
    public boolean createPerformancesFile() throws IOException, SecurityException {
        return this.perfstreamer.createFile();
    }
    
    /**
     * @throws IOException 
     */
    public void saveAll() throws IOException {
        this.saveCompanyInfo();
        this.saveSingers();
        this.saveDancers();
        this.saveDirectors();
        this.savePerformances();
    }
    
    /**
     * @return the file name
     */
    public String getCompanyFileName() {
        return this.infostreamer.getFileName();
    }
    
    /**
     * @param fname the file name
     */
    public void setCompanyFileName(String fname) {
        this.infostreamer.setFileName(fname);
    }
    
    /**
     * @return true if the file could be created, false otherwise
     * @throws IOException
     * @throws SecurityException 
     */
    public boolean createCompanyFile() throws IOException, SecurityException {
        return this.infostreamer.createFile();
    }
    
    /**
     * @throws IOException 
     */
    public void saveCompanyInfo() throws IOException {
        this.infostreamer.saveToFile(info);
    }
    
    /**
     * @throws IOException 
     */
    public void saveSingers() throws IOException {
        this.singers.saveToFile();
    }
    
    /**
     * @throws IOException 
     */
    public void saveDancers() throws IOException {
        this.dancers.saveToFile();
    }
    
    /**
     * @throws IOException 
     */
    public void saveDirectors() throws IOException {
        this.directors.saveToFile();
    }
    
    /**
     * @throws IOException 
     */
    public void savePerformances() throws IOException {
        this.perfstreamer.saveToFile(this.performances);
    }
    
    /**
     * @return the Company name
     */
    public String getCompanyName() {
        return this.info.getName();
    }
    
    /**
     * @param name the Company name to set
     */
    public void setCompanyName(String name) {
        this.info.setName(name);
    }
    
    /**
     * @return the company city
     */
    public String getCompanyCity() {
        return this.info.getCity();
    }
    
    /**
     * @param city the company city to set
     */
    public void setCompanyCity(String city) {
        this.info.setCity(city);
    }
    
    /**
     * @return the company country
     */
    public String getCompanyCountry() {
        return this.info.getCountry();
    }
    
    /**
     * @param country the company country to set
     */
    public void setCompanyCountry(String country) {
        this.info.setCountry(country);
    }
    
    /**
     * @return the Company Director
     */
    public Integer getCompanyDirector() {
        return this.info.getDirector();
    }
    
    /**
     * @return the Company Director name
     */
    public String getCompanyDirectorName() {
        int ID = this.info.getDirector();
        int index = this.directors.search(ID);
        return this.directors.peek(index).getName();
    }
    
    /**
     * @param director the Company Director to set
     */
    public void setCompanyDirector(Integer director) {
        this.info.setDirector(director);
    }

    /**
     * @return the performances
     */
    public ArrayList<Performance> getPerformances() {
        return this.performances;
    }
    
    /**
     * @param ID the ID
     * @return the performance
     */
    public Performance getPerformanceByID(String ID) {
        int index = getPerformanceIndexByID(ID);
        if (index == -1) return null;
        return this.performances.get(index);
    }
    
    /**
     * @param ID the ID
     * @return the performance index
     */
    public int getPerformanceIndexByID(String ID) {
        for (int i = 0; i < this.performances.size(); i++) {
            if (this.performances.get(i).getID().equals(ID))
                return i;
        }
        return -1;
    }
    
    /**
     * @param ID the ID of the performance
     * @param p the new performance
     * @throws IndexOutOfBoundsException
     */
    public void modifyPerformance(String ID, Performance p) throws IndexOutOfBoundsException {
        this.performances.set(this.getPerformanceIndexByID(ID), p);
    }

    /**
     * @param performances the performances to set
     */
    public void setPerformances(ArrayList<Performance> performances) {
        this.performances.clear();
        this.performances.addAll(performances);
        this.sortPerformancesByDate();
    }
    
    /**
     * @param performance the performance to add
     */
    public void addPerformance(Performance performance) {
        this.performances.add(performance);
        this.sortPerformancesByDate();
    }
    
    /**
     * Sorts the performances by first date
     */
    public void sortPerformancesByDate() {
        this.performances.sort(
                (Performance a, Performance b) -> {
                    return a.getHall().get(0).getWhen().compareTo(b.getHall().get(0).getWhen());
                }
        );
    }
    
    /**
     * Sorts the performances by ID
     */
    public void sortPerformancesByID() {
        this.performances.sort(
                (Performance a, Performance b) -> {
                    return a.getID().compareTo(b.getID());
                }
        );
    }
    
    /**
     * @param ID the ID to check
     * @return true if the ID exists, false otherwise
     */
    public boolean hasPerformanceID(String ID) {
        return Performance.hasID(performances, ID);
    }
    
    /**
     * @param ID the ID which performance is to be removed
     * @return true if removes, false otherwise
     */
    public boolean removePerformance(String ID) {
        int index = this.getPerformanceIndexByID(ID);
        if (index == -1) return false;
        this.performances.remove(index);
        return true;
    }
    
    /**
     * @param fname the file name to set
     */
    public void setPerformancesFileName(String fname) {
        this.perfstreamer.setFileName(fname);
    }
    
    /**
     * @return the file name
     */
    public String getPerformancesFileName() {
        return this.perfstreamer.getFileName();
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @param index the index
     * @return complete information about the ticket
     * @throws IndexOutOfBoundsException
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    @Deprecated
    public Ticket getTicketInfo(String showID, String seat, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(0).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        return this.performances.get(indperf).getHall(index).getTicket(indtick);
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @return the price
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public Double getTicketPrice(String showID, String seat) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(0).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        return this.performances.get(indperf).getHall(0).getTicket(indtick).getPrice();
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @param index the index given the date
     * @return true if sold, false otherwise
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public boolean isTicketSold(String showID, String seat, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(index).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        return this.performances.get(indperf).getHall(index).getTicket(indtick).isSold();
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @param index the index
     * @return when the ticket was sold
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public LocalDateTime whenTicketSold(String showID, String seat, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException, TicketNotSoldException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(index).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        return this.performances.get(indperf).getHall(index).getTicket(indtick).getWhen();
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @param index the index
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException
     * @throws TicketSoldException 
     */
    public void sellTicket(String showID, String seat, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException, TicketSoldException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(index).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        this.performances.get(indperf).getHall(index).getTicket(indtick).sell();
    }
    
    /**
     * @param showID the performance ID
     * @param seat the seat
     * @param index the index
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException
     * @throws TicketNotSoldException 
     */
    public void refundTicket(String showID, String seat, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException, TicketNotSoldException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        int indtick = this.performances.get(indperf).getHall(index).searchTicket(seat);
        if (indtick == -1)
            throw new SeatNotFoundException(seat);
        this.performances.get(indperf).getHall(index).getTicket(indtick).refund();
    }
    
    /**
     * @param showID the performance ID
     * @param index the index
     * @return true if available, false otherwise
     * @throws IndexOutOfBoundsException
     * @throws PerformanceNotFoundException
     * @throws TicketNotSoldException 
     */
    public boolean hasAvailableTickets(String showID, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        return this.performances.get(indperf).ticketsAvailable(index);
    }
    
    /**
     * @param showID the performance ID
     * @param index the index
     * @return the seats available
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public Integer ticketsAvailable(String showID, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        return this.performances.get(indperf).getHall(index).countAvailable();
    }
    
    /**
     * @param showID the performance ID
     * @param index the index
     * @return the seats available
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public Integer ticketsSold(String showID, int index) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        return this.performances.get(indperf).getHall(index).countSold();
    }
    
    /**
     * @param showID the performance ID
     * @return the seats available
     * @throws PerformanceNotFoundException
     * @throws SeatNotFoundException 
     */
    public Integer hallCapacity(String showID) throws IndexOutOfBoundsException, PerformanceNotFoundException, SeatNotFoundException {
        int indperf = this.getPerformanceIndexByID(showID);
        if (indperf == -1)
            throw new PerformanceNotFoundException(showID);
        return this.performances.get(indperf).getHall(0).getCapacity();
    }
}

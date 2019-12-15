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
package org.pavarotti.core.components;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Ovelhas do Presépio
 */
public class ConcertHall implements Serializable {
    private static final long serialVersionUID = 8704451451184707606L;
    
    private final int ROWS = 10;                // de   A a   J
    private final int COLS = 20;                // de  01 a  20
    private final int CAPACITY = ROWS * COLS;   // de A01 a J20
    private final ArrayList<Ticket> tickets;
    private Double basePrice;
    private LocalDateTime when;
    
    /**
     * @return the number of columns
     */
    public int getRows() {
        return ROWS;
    }
    
    /**
     * @return the number of rows
     */
    public int getCols() {
        return COLS;
    }
    
    /**
     * @return the first seat
     */
    public String getFirstSeat() {
        return tickets.get(0).getSeat();
    }
    
    /**
     * @return the last seat
     */
    public String getLastSeat() {
        return tickets.get(CAPACITY-1).getSeat();
    }
    
    /**
     * @return a matrix that shows the status of sold
     */
    public ArrayList<ArrayList<Boolean>> hallSoldMatrix() {
        ArrayList<ArrayList<Boolean>> result = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < ROWS; i++) {
            ArrayList<Boolean> aux = new ArrayList<>();
            for (int j = 0; j < COLS; j++) {
                aux.add(tickets.get(count).isSold());
                count++;
            }
            result.add(aux);
        }
        return result;
    }
    
    /**
     * @return when
     */
    public LocalDateTime getWhen() {
        return when;
    }
    
    /**
     * @param when the when
     */
    public void setWhen(LocalDateTime when) {
        this.when = when;
    }
    
    /**
     * @return the total capacity
     */
    public int getCapacity() {
        return CAPACITY;
    }
    
    /**
     * @return the tickets
     */
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }
    
    /**
     * @param index the index of the seat (usually provided by searchTicket)
     * @return the ticket
     */
    public Ticket getTicket(int index) {
        return tickets.get(index);
    }
    
    /**
     * @param SEAT the seat to search for
     * @return the index of the seat
     */
    public Integer searchTicket(final String SEAT) {
        for (Integer i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getSeat().equalsIgnoreCase(SEAT))
                return i;
        }
        return -1;
    }
    
    /**
     * @return the sold seats
     */
    public int countSold() {
        int count = 0;
        for (Integer i = 0; i < tickets.size(); i++) {
            if (this.tickets.get(i).isSold())
                count++;
        }
        return count;
    }
    
    /**
     * @return the available seats
     */
    public int countAvailable() {
        return CAPACITY - countSold();
    }
    
    /**
     * Sets the initial state for the seats
     * @param basePrice the base price for tickets
     */
    private void initTickets() {
        tickets.clear();
        for (Integer i = 0; i < CAPACITY; i++) {
            tickets.add(new Ticket(intToPrice(basePrice, i), intToSeat(i)));
        }
    }
    
    /**
     * Converts an integer to a correspondent seat number (e.g. 25 is "B05")
     * @param i the integer
     * @return the seat number
     */
    private String intToSeat(Integer i) {
        return String.format("%c%02d", (char) ((i / COLS) + (int) 'A'), (i % COLS));
    }
    
    /**
     * Converts an integer to a correspondent seat price
     * @param bp the base price
     * @param i the integer representing the seat
     * @return the price for that seat
     */
    private Double intToPrice(Double bp, Integer i) {
        final int    R  = i / 20;
        final double PV = (-0.45 * R) / (ROWS - 1) + 1.30;
        return bp * PV;
    }
    
    /**
     * @return the base price
     */
    public Double getBasePrice() {
        return basePrice;
    }
    
    /**
     * @param basePrice the base price
     */
    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
        this.initTickets();
    }
    
    /**
     * The constructor of the class
     * @param basePrice the base price
     * @param when the when
     */
    public ConcertHall(Double basePrice, LocalDateTime when) {
        this.when      = when;
        this.tickets   = new ArrayList<>();
        this.basePrice = basePrice;
        initTickets();
    }
    
    /**
     * The constructor of the class
     * @param basePrice the base price
     * @param when the when
     * @param tickets the tickets
     */
    protected ConcertHall(Double basePrice, LocalDateTime when, ArrayList<Ticket> tickets) {
        this.when      = when;
        this.tickets   = tickets;
        this.basePrice = basePrice;
    }
}

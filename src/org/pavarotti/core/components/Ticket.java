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

import java.time.LocalDateTime;
import java.io.Serializable;

import org.pavarotti.core.throwable.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Ticket implements Serializable {
    private static final long serialVersionUID = -7622471492532679852L;
    
    private Double  price;
    private String  seat;
    private boolean sold;
    private LocalDateTime when;
    
    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the seat description
     */
    public String getSeat() {
        return seat;
    }

    /**
     * @param seat the seat description to set
     */
    public void setSeat(String seat) {
        this.seat = seat;
    }
    
    /**
     * @return true if seat has been sold
     */
    public boolean isSold() {
        return sold;
    }
    
    /**
     * @throws TicketSoldException 
     */
    public void sell() throws TicketSoldException {
        if (sold) throw new TicketSoldException(
                String.format(
                        "Already sold on the %s.",
                        when.toString()
                )
        );
        switchSold();
        when = LocalDateTime.now();
    }
    
    /**
     * @throws TicketNotSoldException 
     */
    public void refund() throws TicketNotSoldException {
        if (!sold) throw new TicketNotSoldException("Not sold yet.");
        switchSold();
        when = null;
    }
    
    /**
     * @return the date and time the ticket was sold
     * @throws TicketNotSoldException 
     */
    public LocalDateTime getWhen() throws TicketNotSoldException {
        if (when == null || !sold) throw new TicketNotSoldException("Not sold yet.");
        return when;
    }
    
    /**
     * Switches from sold to not sold and vice-versa
     */
    private void switchSold() {
        sold = !sold;
    }
    
    /**
     * The constructor of the class
     * @param price the initial price to set
     * @param seat the initial seat description to set
     */
    public Ticket(Double price, String seat) {
        this.price = price;
        this.seat  = seat;
        this.sold  = false;
        this.when  = null;
    }
    
    /**
     * The constructor of the class
     * @param price the initial price to set
     * @param seat the initial seat description to set
     * @param sold is it sold already?
     * @param when when was it sold?
     */
    protected Ticket(Double price, String seat, boolean sold, LocalDateTime when) {
        this.price = price;
        this.seat  = seat;
        this.sold  = sold;
        this.when  = when;
    }
}

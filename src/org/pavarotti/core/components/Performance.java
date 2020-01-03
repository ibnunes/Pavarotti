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

import java.util.ArrayList;
import java.io.Serializable;
import java.time.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Performance implements Serializable {
    private static final long serialVersionUID = -4635520548766619856L;
    
    private final String ID;
    private String name;
    private ArrayList<ConcertHall> hall;
    
    private ArrayList<Integer> singers;
    private ArrayList<Integer> dancers;
    private Integer operadirector;
    private Integer castingdirector;
    
    /**
     * @return the string representation of the class
     */
    @Override
    public String toString() {
        String s = String.format("ID: %s\nName: %s\n", ID, name);
        s += "When:\n";
        for (int i = 0; i < hall.size(); i++)
            s += String.format("\t%s\n", hall.get(i).getWhen().toString());
        s += "\nSingers: ";
        for (int i = 0; i < singers.size(); i++)
            s += String.format("%6d ", singers.get(i));
        s += "\nDancers: ";
        for (int i = 0; i < dancers.size(); i++)
            s += String.format("%6d ", dancers.get(i));
        s += "\nOpera director: " + String.format("%6d", operadirector) + "\n";
        s += "Casting director: " + String.format("%6d", castingdirector) + "\n";
        return s;
    }
    
    /**
     * @return a clone
     * @throws CloneNotSupportedException
     */
    @Override
    public Performance clone() throws CloneNotSupportedException {
        super.clone();
        Performance p = new Performance(ID, name, hall.get(0).getBasePrice(), new ArrayList<>());
        p.setHall(hall);
        p.setDancers(dancers);
        p.setSingers(singers);
        p.setCastingDirector(castingdirector);
        p.setOperaDirector(operadirector);
        return p;
    }
    
    /**
     * @return the ID
     */
    public String getID() {
        return this.ID;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @param name the name to set
     * @return the instance itself
     */
    public Performance withName(String name) {
        this.setName(name);
        return this;
    }
    
    /**
     * @return the Opera Director
     */
    public Integer getOperaDirector() {
        return this.operadirector;
    }
    
    /**
     * @param operadirector the new Opera Director
     */
    public void setOperaDirector(Integer operadirector) {
        this.operadirector = operadirector;
    }
    
    /**
     * @param operadirector the new Opera Director
     * @return the instance itself
     */
    public Performance withOperaDirector(Integer operadirector) {
        this.setOperaDirector(operadirector);
        return this;
    }
    
    /**
     * @return the Casting Director
     */
    public Integer getCastingDirector() {
        return this.castingdirector;
    }
    
    /**
     * @param castingdirector the new Casting Director
     */
    public void setCastingDirector(Integer castingdirector) {
        this.castingdirector = castingdirector;
    }
    
    /**
     * @param castingdirector the new Casting Director
     * @return the instance itself
     */
    public Performance withCastingDirector(Integer castingdirector) {
        this.setCastingDirector(castingdirector);
        return this;
    }
    
    /**
     * @return when the performance will take place
     */
    public ArrayList<ConcertHall> getHall() {
        return this.hall;
    }
    
    /**
     * @param hall the hall
     */
    public void setHall(ArrayList<ConcertHall> hall) {
        this.hall.clear();
        this.hall.addAll(hall);
        this.sortWhen();
    }
    
    /**
     * @param hall the new hall
     */
    public void addHall(ConcertHall hall) {
        this.hall.add(hall);
        this.sortWhen();
    }
    
    /**
     * Sorts the dates and times
     */
    private void sortWhen() {
        this.hall.sort( (ConcertHall a, ConcertHall b) -> { return a.getWhen().compareTo(b.getWhen()); } );
    }
    
    /**
     * @param when the new when
     */
    public void setWhen(ArrayList<LocalDateTime> when) throws IndexOutOfBoundsException {
        final Double PRICE = hall.get(0).getBasePrice();
        final int MIN = (when.size() <= hall.size()) ? when.size() : hall.size();
        final int MAX = (when.size() >= hall.size()) ? when.size() : hall.size();
        for (int i = 0; i < MIN; i++)
            hall.get(i).setWhen(when.get(i));
        if (when.size() > hall.size())
            for (int i = MIN; i < MAX; i++)
                hall.add(new ConcertHall(PRICE, when.get(i)));
    }
    
    /**
     * @param basePrice the new vase price
     */
    public void setBasePrice(Double basePrice) {
        for (int i = 0; i < hall.size(); i++)
            hall.get(i).setBasePrice(basePrice);
    }
    
    /**
     * @param hall the hall
     * @return the instance itself
     */
    public Performance withHall(ArrayList<ConcertHall> hall) {
        this.setHall(hall);
        return this;
    }
    
    /**
     * @param index the index
     * @return the hall
     */
    public ConcertHall getHall(int index) {
        return hall.get(index);
    }
    
    /**
     * @return the base price
     */
    public Double getBasePrice() {
        if (hall.size() > 0)
            return hall.get(0).getBasePrice();
        else
            return 0.;
    }
    
    /**
     * @param when the when
     * @return the index
     */
    public Integer searchHall(LocalDateTime when) {
        for (int i = 0; i < hall.size(); i++) {
            if (hall.get(i).getWhen().equals(when))
                return i;
        }
        return -1;
    }
    
    /**
     * @return the singers
     */
    public ArrayList<Integer> getSingers() {
        return this.singers;
    }
    
    /**
     * @param singers the new singers to set
     */
    public void setSingers(ArrayList<Integer> singers) {
        this.singers.clear();
        this.withSingers(singers);
    }
    
    /**
     * @param singers the new singers to add
     * @return the instance itself
     */
    public Performance withSingers(ArrayList<Integer> singers) {
        this.singers.addAll(singers);
        return this;
    }
    
    /**
     * @param singer the new singer
     * @return the instance itself
     */
    public Performance addSinger(Integer singer) {
        this.singers.add(singer);
        return this;
    }
    
    /**
     * @param ID the ID of the singer to remove
     * @return true of deleted, false otherwise
     */
    public boolean deleteSinger(int ID) {
        int index = this.searchSinger(ID);
        if (index == -1)
            return false;
        this.singers.remove(index);
        return true;
    }
    
    /**
     * @param ID the ID to search for
     * @return the index in the list of singers
     */
    private int searchSinger(Integer ID) {
        return this.singers.indexOf(ID);
    }
    
    /**
     * @param ID the ID to search for
     * @return the instance with the singer
     */
    public Integer getSinger(int ID) {
        int index = this.singers.indexOf(ID);
        if (index == -1)
            return null;
        return this.singers.get(index);
    }
    
    /**
     * @param ID the ID of the singer to modify
     * @param newSinger the new singer to modify in the list
     * @return true if modified, false otherwise
     * @throws IndexOutOfBoundsException 
     */
    public boolean modifySinger(Integer ID, Integer newSinger) {
        int index = this.searchSinger(ID);
        if (index == -1)
            return false;
        this.singers.set(index, newSinger);
        return true;
    }
    
    /**
     * @param dancers the dancers to set
     */
    public void setDancers(ArrayList<Integer> dancers) {
        this.dancers.clear();
        this.withDancers(dancers);
    }
    
    /**
     * @return the dancers
     */
    public ArrayList<Integer> getDancers() {
        return this.dancers;
    }
    
    /**
     * @param dancers the dancers to add
     * @return the instance itself
     */
    public Performance withDancers(ArrayList<Integer> dancers) {
        this.dancers.addAll(dancers);
        return this;
    }
    
    /**
     * @param dancer the new dancer
     * @return the instance itself
     */
    public Performance addDancer(Integer dancer) {
        this.dancers.add(dancer);
        return this;
    }
    
    /**
     * @param ID the ID of the dancer to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDancers(int ID) {
        int index = this.searchDancer(ID);
        if (index == -1)
            return false;
        this.dancers.remove(index);
        return true;
    }
    
    /**
     * @param ID the ID to search for
     * @return the index in the list of dancers
     */
    private int searchDancer(int ID) {
        return this.dancers.indexOf(ID);
    }
    
    /**
     * @param ID the ID to search for
     * @return the instance with the dancer
     */
    public Integer getDancer(int ID) {
        int index = this.dancers.indexOf(ID);
        if (index == -1)
            return null;
        return this.dancers.get(index);
    }
    
    /**
     * @param ID the ID of the dancer to modify
     * @param newDancer the new dancer to modify in the list
     * @return true if modified, false otherwise
     */
    public boolean modifyDancer(Integer ID, Integer newDancer) {
        int index = this.searchDancer(ID);
        if (index == -1)
            return false;
        this.dancers.set(index, newDancer);
        return true;
    }
    
    /**
     * @param index the index
     * @return true if tickets available, false otherwise
     */
    public boolean ticketsAvailable(int index) {
        return
                (this.hall.get(index).countSold() < this.hall.get(index).getCapacity()) &&
                this.hall.get(0).getWhen().isAfter(LocalDateTime.now());
    }
    
    /**
     * Constructor of the class
     */
    protected Performance() {
        this.ID              = "";
        this.name            = "";
        this.hall            = new ArrayList<>();
        this.singers         = new ArrayList<>();
        this.dancers         = new ArrayList<>();
        this.operadirector   = 0;
        this.castingdirector = 0;
    }
    
    /**
     * Constructor of the class
     * @param ID the ID to set - recommended to have been check with hasID previously if needed
     */
    public Performance(String ID) {
        this.ID              = ID;
        this.name            = "";
        this.hall            = new ArrayList<>();
        this.singers         = new ArrayList<>();
        this.dancers         = new ArrayList<>();
        this.operadirector   = 0;
        this.castingdirector = 0;
    }
    
    /**
     * Constructor of the class
     * @param ID the ID to set - recommended to have been check with hasID previously if needed
     * @param name the name to set
     * @param hall the hall
     */
    public Performance(String ID, String name, ArrayList<ConcertHall> hall) {
        this.ID              = ID;
        this.name            = name;
        this.hall            = hall;
        this.sortWhen();
        this.singers         = new ArrayList<>();
        this.dancers         = new ArrayList<>();
        this.operadirector   = 0;
        this.castingdirector = 0;
    }
    
    /**
     * Constructor of the class
     * @param ID the ID
     * @param name the name
     * @param hall the hall
     * @param basePrice the base price
     * @param singers the singers
     * @param dancers the dancers
     * @param opera the opera director
     * @param casting the casting director
     */
    protected Performance(
            String ID, String name, ArrayList<ConcertHall> hall, Double basePrice,
            ArrayList<Integer> singers, ArrayList<Integer> dancers,
            Integer opera, Integer casting
    ) {
        this.ID              = ID;
        this.name            = name;
        this.hall            = hall;
        this.sortWhen();
        this.singers         = singers;
        this.dancers         = dancers;
        this.operadirector   = opera;
        this.castingdirector = casting;
    }
    
    /**
     * Constructor of the class
     * @param ID the ID to set - recommended to have been check with hasID previously if needed
     * @param name the name to set
     * @param when the when
     * @param basePrice the base price
     */
    public Performance(String ID, String name, Double basePrice, ArrayList<LocalDateTime> when) {
        this.ID              = ID;
        this.name            = name;
        this.hall            = new ArrayList<>();
        for (int i = 0; i < when.size(); i++)
            this.hall.add(new ConcertHall(basePrice, when.get(i)));
        this.sortWhen();
        this.singers         = new ArrayList<>();
        this.dancers         = new ArrayList<>();
        this.operadirector   = 0;
        this.castingdirector = 0;
    }
    
    /**
     * Checks if a given ID already exists in a list of performance events
     * @param list the list to search in
     * @param ID the ID to be tested
     * @return true if ID exists, false if it's a new unique ID
     */
    public static boolean hasID(final ArrayList<Performance> list, final String ID) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getID().equals(ID))
                return true;
        return false;
    }
}

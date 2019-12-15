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
package org.pavarotti.core.stat;

import org.pavarotti.core.api.*;
import org.pavarotti.core.throwable.*;
import org.pavarotti.core.components.*;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Statistics {
    private final Company core;
    
    /**
     * The constructor of the class.
     * Binds the core to this class.
     * @param core the core of the program 
     */
    public Statistics(Company core) {
        super();
        this.core = core;
    }
    
    /**
     * Calculates the average of spectators for a given performance
     * @param ID the performance ID
     * @return the average
     * @throws PerformanceNotFoundException
     */
    public Double averageSpectators(String ID) throws PerformanceNotFoundException {
        // Finds the index of the performance (by ID) and throws exception if not found
        int index = core.getPerformanceIndexByID(ID);
        if (index == -1)
            throw new PerformanceNotFoundException(ID);
        
        // Performance found, get a pointer to the object for easier use
        final Performance PERF = core.getPerformances().get(index);
        final int SIZE = PERF.getHall().size();
        
        // Compute the sumation of spectators
        int sum = 0;
        for (int i = 0; i < SIZE; i++) {
            sum += PERF.getHall(i).countSold();
        }
        // Returns the average
        return ((double) sum) / SIZE;
    }
    
    /**
     * Calculates the best day for a given performance
     * @param ID the performance ID
     * @return the date
     * @throws PerformanceNotFoundException
     */
    public LocalDateTime bestPerformanceDay(String ID) throws PerformanceNotFoundException {
        // Finds the index of the performance (by ID) and throws exception if not found
        int index = core.getPerformanceIndexByID(ID);
        if (index == -1)
            throw new PerformanceNotFoundException(ID);
        
        // Performance found, get a pointer to the object for easier use
        final Performance PERF = core.getPerformances().get(index);
        final int SIZE = PERF.getHall().size();
        
        // Compute the maximum number of spectators of a performance
        LocalDateTime date = null;
        int max = 0;
        for (int i = 0; i < SIZE; i++) {
            int sold = PERF.getHall(i).countSold();
            if (max <= sold){
                max = sold;
                date = PERF.getHall(i).getWhen();
            }
        }
        
        //returns the date of the performance with the most spectators
        return date;
    }
    
    /**
     * Calculates the worst day for a given performance
     * @param ID the performance ID
     * @return the date
     * @throws PerformanceNotFoundException
     */
    public LocalDateTime worstPerformanceDay(String ID) throws PerformanceNotFoundException {
        // Finds the index of the performance (by ID) and throws exception if not found
        int index = core.getPerformanceIndexByID(ID);
        if (index == -1)
            throw new PerformanceNotFoundException(ID);
        
        // Performance found, get a pointer to the object for easier use
        final Performance PERF = core.getPerformances().get(index);
        final int SIZE = PERF.getHall().size();
        
        // Compute the minimum number of spectators of a performance
        LocalDateTime date = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < SIZE; i++) {
            int sold = PERF.getHall(i).countSold();
            if (min >= sold){
                min = sold;
                date = PERF.getHall(i).getWhen();
            }
        }

        //returns the date of the performance with the least spectators
        return date;
    }
    
    /**
     * Computes the sum of all spectators for a given performance
     * @param index
     * @return the summation
     * @throws PerformanceNotFoundException
     */
    public long sumAllSpectators(int index) throws PerformanceNotFoundException{
        long sum = 0;
        try{
            if(index < 0)
                throw new PerformanceNotFoundException();
            
            final Performance PERF = core.getPerformances().get(index);
            final int SIZE = PERF.getHall().size();
            for (int i = 0; i < SIZE; i++) {
                sum += (long) PERF.getHall(i).countSold();
            }
        }catch (IndexOutOfBoundsException e){
            throw new PerformanceNotFoundException("N/A");
        }
        return sum;
    }
    
    /**
     * Computes the sum of all spectators for a given performance
     * @param ID the performance ID
     * @return the summation
     * @throws PerformanceNotFoundException
     */
    public long sumAllSpectators(String ID) throws PerformanceNotFoundException{
        int index = core.getPerformanceIndexByID(ID);
        if (index == -1)
            throw new PerformanceNotFoundException(ID);
        return sumAllSpectators(index);
    }
    
    /**
     * Computes the most watched performance
     * @return the ID
     * @throws PerformanceNotFoundException
     */
    public String mostWatchedPerformance() throws PerformanceNotFoundException {
        final ArrayList<Performance> PERF = core.getPerformances();
        final int SIZE = core.getPerformances().size();
        String ID = "";
        long max = 0;
        for (int i = 0; i < SIZE; i++){
            long sum = sumAllSpectators(i);
            if (max <= sum) {
                max = sum;
                ID = PERF.get(i).getID();
            }
        }
        return ID;
    }
    
    /**
     * Computes the least watched performance
     * @return the ID
     * @throws PerformanceNotFoundException
     */
     public String leastWatchedPerformance() throws PerformanceNotFoundException {
        final ArrayList<Performance> PERF = core.getPerformances();
        final int SIZE = core.getPerformances().size();
        String ID = "";
        long min = Integer.MAX_VALUE;
        for (int i = 0; i < SIZE; i++) {
            long sum = sumAllSpectators(i);
            if (min >= sum) { 
                min = sum;
                ID = PERF.get(i).getID();
            }
        }
        return ID;
    }
}
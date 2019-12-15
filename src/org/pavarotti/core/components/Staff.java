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

import java.util.*;
import java.io.*;

import org.pavarotti.core.intf.*;
import org.dma.io.Streamer;

/**
 * @param <T> a class that extends Person and implements StaffMember
 * @author Ovelhas do Presépio
 */
public class Staff<T extends Person & StaffMember> {
    private final Streamer<T> streamer;
    private ArrayList<T>      list;
    
    /**
     * @param member the new member to add
     * @return the instance itself
     */
    public Staff insert(T member) {
        this.list.add(member);
        return this;
    }
    
    /**
     * @param list the list of new members to add
     * @return the instance itself
     */
    public Staff insertAll(ArrayList<T> list) {
        this.list.addAll(list);
        return this;
    }
    
    /**
     * @param index the index of the element to delete
     * @return the instance itself
     */
    public Staff delete(int index) throws IndexOutOfBoundsException {
        this.list.remove(index);
        return this;
    }
    
    /**
     * Clears the list
     */
    public void clear() {
        this.list.clear();
    }
    
    /**
     * @param index the index of the element to peek
     * @return the instance itself
     */
    public T peek(int index) throws IndexOutOfBoundsException {
        return this.list.get(index);
    }
    
    /**
     * @param index the index of the element to modify
     * @param member the instance with the new data
     * @return the instance itself
     */
    public Staff modify(int index, T member) throws IndexOutOfBoundsException {
        this.list.set(index, member);
        return this;
    }
    
    /**
     * @return the list of StaffMember instances in the form of an ArrayList
     */
    public ArrayList<T> getList() {
        return this.list;
    }
    
    /**
     * @param name the name to search for
     * @return the index of the first instance with the name, if any
     */
    public int search(String name) {
        return this.searchFromIndex(name, 0);
    }
    
    /**
     * @param name the name to search for
     * @param index the index to start from
     * @return the index of the first instance with the name, if any
     */
    public int searchFromIndex(String name, int index) {
        final String NAME = name.toUpperCase();
        for (int i = index; i < this.list.size(); i++) {
            if (this.list.get(i).getName().toUpperCase().contains(NAME))
                return i;
        }
        return -1;
    }
    
    /**
     * @param name the name to search for
     * @return the list of all staff members who match the name at some extent
     */
    public ArrayList<T> getAllByName(String name) {
        ArrayList<T> result = new ArrayList<>();
        int index = 0;
        while (index != -1) {
            index = searchFromIndex(name, index);
            if (index != -1) {
                result.add(peek(index));
                index++;
            }
        }
        return result;
    }
    
    /**
     * @param ID the ID to search for
     * @return the index of the instance with the ID, if any
     */
    public int search(int ID) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getID() == ID)
                return i;
        }
        return -1;
    }
    
    /**
     * @return the instance itself with the instances sorted by ID
     */
    public Staff sortByID() {
        this.list.sort(
                (StaffMember m1, StaffMember m2) -> {
                    int a = m1.getID();
                    int b = m2.getID();
                    return (a > b) ? 1 : (a < b) ? -1 : 0;
                }
        );
        return this;
    }
    
    /**
     * @return the file name to save to and load from
     */
    public String getFileName() {
        return this.streamer.getFileName();
    }
    
    /**
     * @param fname the file name to save to and load from
     */
    public void setFileName(String fname) {
        this.streamer.setFileName(fname);
    }
    
    /**
     * @return true if the file could be created, false otherwise
     * @throws IOException
     * @throws SecurityException 
     */
    public boolean createFile() throws IOException, SecurityException {
        return this.streamer.createFile();
    }
    
    /**
     * @throws IOException 
     */
    public void saveToFile() throws IOException {
        this.streamer.saveToFile(this.list);
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public boolean loadFromFile() throws IOException {
        return (this.list = this.streamer.loadAllFromFile()) != null;
    }
    
    /**
     * @param fmt format to use: must have exactly in order a %d for ID, %s for Name and %s for Position
     * @return a String with all the instances listed
     * @throws IllegalFormatException 
     */
    public String asString(String fmt) throws IllegalFormatException {
        String s = "";
        StaffMember m;
        for (int i = 0; i < this.list.size(); i++) {
            m = this.list.get(i);
            s += String.format(fmt, m.getID(), m.getName(), m.getPosition());
        }
        return s;
    }
    
    /**
     * @return a standard String representation of all the elements listed by the class
     */
    @Override
    public String toString() {
        return this.asString("%d: %s (%s)\n");
    }
    
    /**
     * Constructor of the class
     */
    public Staff() {
        this.list     = new ArrayList<>();
        this.streamer = new Streamer<>();
    }
}

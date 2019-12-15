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

import java.time.*;
import java.io.Serializable;
import java.util.ArrayList;

import org.pavarotti.core.intf.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Performer extends Person implements StaffMember, Serializable, Cloneable {
    private static final long serialVersionUID = 7227977930094301212L;
    
    private static final int ID_MIN = 10000;
    private static final int ID_MAX = 99999;
    
    private int ID;
    private String position;
    private LocalDate admission;
    
    /**
     * Clones the current object
     * @return the clone
     * @throws CloneNotSupportedException
     */
    @Override
    public Performer clone() throws CloneNotSupportedException {
        super.clone();
        return new Performer(this.ID, this.getName(), this.getGender(), this.getBirthday(), this.position, this.admission);
    }
    
    /**
     * @return the ID
     */
    @Override
    public int getID() {
        return this.ID;
    }
    
    /**
     * Sets the ID
     * @param ID the ID to set
     */
    protected void setID(int ID) {
        this.ID = ID;
    }
    
    /**
     * Sets the admission date
     * @param admission the admission date to set
     */
    protected void setAdmission(LocalDate admission) {
        this.admission = admission;
    }
    
    /**
     * @param name the name to set
     * @return the instance itself
     */
    @Override
    public Performer withName(String name) {
        this.setName(name);
        return this;
    }
    
    /**
     * @param position the position to set
     */
    @Override
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * @param position the position to set
     * @return the instance itself
     */
    @Override
    public Performer withPosition(String position) {
        this.setPosition(position);
        return this;
    }
    
    /**
     * @return the position
     */
    @Override
    public String getPosition() {
        return this.position;
    }
    
    /**
     * @return the admission date
     */
    @Override
    public LocalDate getAdmission() {
        return this.admission;
    }
    
    /**
     * @return a String representation of the instance
     */
    @Override
    public String toString() {
        return String.format("ID: %6d\n\t Position: %s\n\t     Name: %s\n\tAdmission: %s\n", this.ID, this.position, this.getName(), this.admission);
    }
    
    /**
     * The constructor of the class
     */
    public Performer() {
        super("", Person.Gender.Other, LocalDate.now());
        this.ID = StaffMember.generateID(ID_MIN, ID_MAX, null);
        this.admission = LocalDate.now();
        this.position = "";
    }
    
    /**
     * The constructor of the class
     * @param name the name to initialize with
     * @param gender the gender of the performer
     * @param birthday the birthday
     * @param position the position
     * @param admission the admission
     * @param list a collection of existing performers to avoid generating an ID that was already attributed
     */
    public Performer(String name, Gender gender, LocalDate birthday, String position, LocalDate admission, ArrayList<StaffMember>[] list) {
        super(name, gender, birthday);
        this.ID        = StaffMember.generateID(ID_MIN, ID_MAX, list);
        this.admission = admission;
        this.position  = position;
    }
    
    /**
     * The constructor of the class
     * @param ID the ID
     * @param name the name
     * @param gender the gender
     * @param birthday the birthday
     * @param position the position
     * @param admission the admission
     */
    public Performer(Integer ID, String name, Gender gender, LocalDate birthday, String position, LocalDate admission) {
        super(name, gender, birthday);
        this.ID        = ID;
        this.admission = admission;
        this.position  = position;
    }
}

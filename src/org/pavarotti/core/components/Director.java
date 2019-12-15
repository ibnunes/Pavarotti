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
public class Director extends Person implements StaffMember, Serializable, Cloneable {
    private static final long serialVersionUID = 6968049795119580241L;
    
    private static final int ID_MIN = 1000;
    private static final int ID_MAX = 9999;
    
    private int ID;
    private String position;
    private LocalDate admission;
    
    /**
     * Clones the director
     * @return the clone
     * @throws CloneNotSupportedException
     */
    @Override
    public Director clone() throws CloneNotSupportedException {
        super.clone();
        return new Director(ID, getName(), getGender(), getBirthday(), position, admission);
    }
    
    /**
     * @return the ID
     */
    @Override
    public int getID() {
        return this.ID;
    }
    
    /**
     * Sets the ID for private purposes
     * @param ID the ID to set
     */
    protected void setID(int ID) {
        this.ID = ID;
    }
    
    /**
     * @param name the name to set
     * @return the instance itself
     */
    @Override
    public Director withName(String name) {
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
    public Director withPosition(String position) {
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
        return String.format(
                "ID: %6d\n\t  Posição: %s\n\t     Nome: %s\n\t  Género: %s\n\tAdmissão: %s\n",
                this.ID, this.position, this.getName(), this.getGender(), this.admission
        );
    }
    
    /**
     * The constructor of the class
     * @param name the name
     * @param gender the gender
     * @param position the position
     * @param birthday the birthday
     * @param admission the admission date
     * @param list the list where to generate an ID from
     */
    public Director(String name, Gender gender, LocalDate birthday, String position, LocalDate admission, ArrayList<StaffMember> list) {
        super(name, gender, birthday);
        this.ID        = StaffMember.generateID(ID_MIN, ID_MAX, new ArrayList[] {list});
        this.admission = admission;
        this.position  = position;
    }
    
    /**
     * The constructor of the class
     * @param ID the ID
     * @param name the name
     * @param gender the gender
     * @param position the position
     * @param birthday the birthday
     * @param admission the admission date
     */
    public Director(Integer ID, String name, Gender gender, LocalDate birthday, String position, LocalDate admission) {
        super(name, gender, birthday);
        this.ID        = ID;
        this.admission = admission;
        this.position  = position;
    }
}

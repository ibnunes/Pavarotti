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
package org.pavarotti.core.intf;

import java.time.LocalDate;
import java.io.Serializable;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Person implements Serializable, Cloneable {
    private static final long serialVersionUID = 3403070964896248127L;
    
    public static enum Gender {
        Male, Female, Other
    }
    
    private String name;
    private LocalDate birthday;
    private Gender gender;
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @param name the name to set
     * @return the instance itself
     */
    public Person withName(String name) {
        this.setName(name);
        return this;
    }

    /**
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    /**
     * @param birthday the birthday to set
     * @return the instance itself
     */
    public Person withBirthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    /**
     * @param gender the gender to set
     * @return the instance itself
     */
    public Person withGender(Gender gender) {
        this.setGender(gender);
        return this;
    }
    
    /**
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return
                String.format(
                        "Name: %s\nGender: %s\nBirthday: %s",
                        this.name,
                        this.gender,
                        this.birthday
                );
    }
    
    /**
     * The constructor of the class
     */
    protected Person() {
        this.name     = "";
        this.gender   = Gender.Other;
        this.birthday = LocalDate.now();
    }
    
    /**
     * The constructor of the class
     * @param name the name to initialize with
     * @param gender the gender of the person
     */
    public Person(String name, Gender gender) {
        this.name     = name;
        this.gender   = gender;
        this.birthday = LocalDate.now();
    }
    
    /**
     * The constructor of the class
     * @param name the name to initialize with
     * @param gender the gender of the person
     * @param birthday the birthday
     */
    public Person(String name, Gender gender, LocalDate birthday) {
        this.name     = name;
        this.gender   = gender;
        this.birthday = birthday;
    }
    
    /**
     * The cloner
     * @return a clone
     * @throws CloneNotSupportedException 
     */
//    @Override
//    public Person clone() throws CloneNotSupportedException {
//        super.clone();
//        return new Person(name, gender, birthday);
//    }
}

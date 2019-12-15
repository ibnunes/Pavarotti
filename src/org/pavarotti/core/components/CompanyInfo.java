/*
 * Copyright (C) 2019 igor_
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

import java.io.*;

/**
 *
 * @author igor_
 */
public class CompanyInfo implements Serializable {
    private static final long serialVersionUID = -1327998953294788027L;
    
    private String  name;
    private String  city;
    private String  country;
    private Integer director;

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
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the company director
     */
    public Integer getDirector() {
        return this.director;
    }

    /**
     * @param director the company director to set
     */
    public void setDirector(Integer director) {
        this.director = director;
    }

    /**
     * The constructor of the class
     * @param name the name of the Company
     * @param city the city where the Company is located
     * @param country the country where the Company is located
     * @param director the Company Director
     */
    public CompanyInfo(String name, String city, String country, Integer director) {
        this.name     = name;
        this.city     = city;
        this.country  = country;
        this.director = director;
    }
}

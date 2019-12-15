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

import java.time.*;
import java.util.ArrayList;

/**
 *
 * @author Ovelhas do Presépio
 */
public interface StaffMember {
    abstract int getID();
    abstract void setName(String name);
    abstract <T extends Person & StaffMember> T withName(String name);
    abstract String getName();
    abstract void setPosition(String position);
    abstract <T extends Person & StaffMember> T withPosition(String position);
    abstract String getPosition();
    abstract LocalDate getAdmission();
    
    // abstract <T extends Person & StaffMember> T clone() throws CloneNotSupportedException;
    
    /**
     * Checks if a given ID already exists in an ArrayList of StaffMember instances
     * @param LIST the ArrayList of StaffMember instances where to search
     * @param ID the ID to look up for
     * @return true of ID exists, false otherwise
     */
    static boolean hasID(final ArrayList<? extends StaffMember> LIST, final int ID) {
        if (LIST != null)
            for (int i = 0; i < LIST.size(); i++) {
                if (LIST.get(i).getID() == ID)
                    return true;
            }
        return false;
    }
    
    /**
     * Generates a new ID in a given interval, guaranteeing it does now exist already in a given collection
     * @param MIN lower bound for ID
     * @param MAX upper bound for ID
     * @param LIST the set of lists where to check already existent IDs
     * @return a new randomly generated ID that does not exist on the given collection
     */
    static int generateID(final int MIN, final int MAX, final ArrayList<? extends StaffMember>[] LIST) {
        // Assuming the list is sorted by ID. Ignores MAX for now.
        int lastID = MIN - 1;
        if (LIST != null)
            for (int i = 0; i < LIST.length; i++) {
                if (LIST[i] != null) if (LIST[i].size() > 0) {
                    int l = LIST[i].get(LIST[i].size()-1).getID();
                    if (lastID < l) lastID = l;
                }
            }
        return lastID + 1;
    }
}

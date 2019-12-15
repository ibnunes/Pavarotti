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
package org.dma.io;

import java.io.*;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Read {
    /**
     * @return A string read from the keyboard
     * @throws IOException 
     */
    public static String asString() throws IOException {
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }
    
    /**
     * @return An integer read from the keyboard
     * @throws IOException 
     */
    public static int asInt() throws IOException {
        return Integer.valueOf(asString().trim()).intValue();
    }
    
    /**
     * @return A double read from the keyboard
     * @throws IOException 
     */
    public static double asDouble() throws IOException {
        return Double.valueOf(asString().trim()).doubleValue();
    }
    
    /**
     * @return A float read from the keyboard
     * @throws IOException 
     */
    public static float asFloat() throws IOException {
        return Float.valueOf(asString().trim()).floatValue();
    }
    
    /**
     * @return A boolean read from the keyboard
     * @throws IOException 
     */
    public static boolean asBoolean() throws IOException {
        return Boolean.valueOf(asString().trim()).booleanValue();
    }
    
    /**
     * @return A char read from the keyboard
     * @throws IOException 
     */
    public static char asChar() throws IOException {
        return asString().charAt(0);
    }
    
    /**
     * @return A byte read from the keyboard
     * @throws IOException 
     */
    public static byte asByte() throws IOException {
        return Byte.valueOf(asString().trim()).byteValue();
    }
    
    /**
     * @return A short integer read from the keyboard
     * @throws IOException 
     */
    public static short asShort() throws IOException {
        return Short.valueOf(asString().trim()).shortValue();
    }
    
    /**
     * @return A long read from the keyboard
     * @throws IOException 
     */
    public static long asLong() throws IOException {
        return Long.valueOf(asString().trim()).longValue();
    }
    
    /**
     * @return A string read from the keyboard
     */
    public static String tryAsString() {
        String s = "";
        try {
            s = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        } catch (IOException e) {
            System.out.println("Error while reading string from buffer.");
        }
        return s;
    }
    
    /**
     * @return An integer read from the keyboard
     */
    public static int tryAsInt() {
        while (true) {
            try {
                return Integer.valueOf(tryAsString().trim()).intValue();
            } catch (Exception e) {
                System.out.println("Error while reading integer from buffer.");
            }
        }
    }
    
    /**
     * @return A double read from the keyboard
     */
    public static double tryAsDouble() {
        while (true) {
            try {
                return Double.valueOf(tryAsString().trim()).doubleValue();
            } catch (Exception e) {
                System.out.println("Error while reading double from buffer.");
            }
        }
    }
    
    /**
     * @return A float read from the keyboard
     */
    public static float tryAsFloat() {
        while (true) {
            try {
                return Float.valueOf(tryAsString().trim()).floatValue();
            } catch (Exception e) {
                System.out.println("Error while reading float from buffer.");
            }
        }
    }
    
    /**
     * @return A boolean read from the keyboard
     */
    public static boolean tryAsBoolean() {
        while (true) {
            try {
                return Boolean.valueOf(tryAsString().trim()).booleanValue();
            } catch (Exception e) {
                System.out.println("Error while reading boolean from buffer.");
            }
        }
    }
    
    /**
     * @return A char read from the keyboard
     */
    public static char tryAsChar() {
        while (true) {
            try {
                return tryAsString().charAt(0);
            } catch (Exception e) {
                System.out.println("Error while reading char from buffer.");
            }
        }
    }
    
    /**
     * @return A byte read from the keyboard
     */
    public static byte tryAsByte() {
        while (true) {
            try {
                return Byte.valueOf(tryAsString().trim()).byteValue();
            } catch (Exception e) {
                System.out.println("Error while reading byte from buffer.");
            }
        }
    }
    
    /**
     * @return A short integer read from the keyboard
     */
    public static short tryAsShort() {
        while (true) {
            try {
                return Short.valueOf(tryAsString().trim()).shortValue();
            } catch (Exception e) {
                System.out.println("Error while reading short from buffer.");
            }
        }
    }
    
    /**
     * @return A long integer read from the keyboard
     */
    public static long tryAsLong() {
        while (true) {
            try {
                return Long.valueOf(tryAsString().trim()).longValue();
            } catch (Exception e) {
                System.out.println("Error while reading short from buffer.");
            }
        }
    }
    
    /**
     * @param c the char
     * @return true if is between 'A' and 'Z' or 'a' and 'z'.
     */
    public static boolean isAlpha(char c) {
        return
                ((c >= 'A') && (c <= 'Z')) ||
                ((c >= 'a') && (c <= 'z'));
    }
}

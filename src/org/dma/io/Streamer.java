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
import java.util.ArrayList;

/**
 * 
 * @author Ovelhas do Presépio
 */
public class Streamer<T> {
    private String fname;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    
    /**
     * @return the file name
     */
    public String getFileName() {
        return this.fname;
    }
    
    /**
     * @param fname the file name
     */
    public void setFileName(String fname) {
        this.fname = fname;
    }
    
    /**
     * @param fname the file name
     * @return the instance itself
     */
    public Streamer withFileName(String fname) {
        this.setFileName(fname);
        return this;
    }
    
    /**
     * Save contents of a Serializable object to the specified file
     * @param list the list of objects to be saved
     * @throws IOException
     */
    public void saveToFile(ArrayList<T> list) throws IOException {
        this.openOutputStream();
        // this.objOut.reset();
        objOut.writeObject(list);
        objOut.flush();
        this.closeOutputStream();
    }
    
    /**
     * Save one object of a Serializable type to the specified file
     * Will erase the saved list of objects in the file if they exist
     * @param t the object to save
     * @throws IOException 
     */
    public void saveToFile(T t) throws IOException {
        this.openOutputStream();
        // this.objOut.reset();
        objOut.writeObject(t);
        objOut.flush();
        this.closeOutputStream();
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public ArrayList<T> loadAllFromFile() throws IOException {
        ArrayList<T> list = new ArrayList<>();
        try {
            this.openInputStream();
            list = (ArrayList<T>) objIn.readObject();
        } catch (ClassNotFoundException e) {
            return list;
        } catch (EOFException e) {
            return list;
        }
        this.closeInputStream();
        return list;
    }
    
    /**
     * @return true if load successful, false if exception thrown
     * @throws IOException 
     */
    public T loadFromFile() throws IOException {
        T t;
        try {
            this.openInputStream();
            t = (T) objIn.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (EOFException e) {
            return null;
        }
        this.closeInputStream();
        return t;
    }
    
    /**
     * @return true if the file was successfully created, and false otherwise
     * @throws IOException
     * @throws SecurityException
     */
    public boolean createFile() throws IOException, SecurityException {
        if (fname.equals("")) throw new IOException("File name not defined");
        File f = new File(System.getProperty("user.dir"), this.fname);
        return f.createNewFile();
    }
    
    /**
     * Closes input stream
     * @throws IOException 
     */
    private void closeInputStream() throws IOException {
        this.objIn.close();
    }
    
    /**
     * Closes output stream
     * @throws IOException 
     */
    private void closeOutputStream() throws IOException {
        this.objOut.close();
    }
    
    /**
     * Opens input stream with the previously defined file name
     * @throws IOException 
     */
    private void openInputStream() throws IOException {
        this.objIn = new ObjectInputStream(new FileInputStream(this.fname));
    }
    
    /**
     * Opens output stream with the previously defined file name
     * @throws IOException 
     */
    private void openOutputStream() throws IOException {
        this.objOut = new ObjectOutputStream(new FileOutputStream(this.fname));
    }
    
    /**
     * Constructor of the class
     */
    public Streamer() {
        this.fname = "";
    }
    
    /**
     * Constructor of the class
     * @param fname the file name
     */
    public Streamer(String fname) {
        this.fname = fname;
    }
}

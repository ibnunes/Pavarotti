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
package org.util;

import java.util.ArrayList;
import java.util.function.Function;

import org.dma.io.Read;

/**
 *
 * @author Ovelhas do Presépio
 */
public class Menu {
    public class MenuItem {
        private String prompt;
        private Thunk procedure;
        
        /**
         * @return the prompt
         */
        public String getPrompt() {
            return this.prompt;
        }
        
        /**
         * @param prompt the prompt to set
         * @return the instance itself
         */
        public MenuItem setPrompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        /**
         * @return the procedure
         */
        public Thunk getProcedure() {
            return this.procedure;
        }
        
        /**
         * @param procedure the procedure to set
         * @return the instance itself
         */
        public MenuItem setProcedure(Thunk procedure) {
            this.procedure = procedure;
            return this;
        }
        
        /**
         * @return the string representation of the instance
         */
        @Override
        public String toString() {
            return this.getPrompt();
        }
        
        /**
         * @param prompt the initial prompt to set
         * @param procedure the initial procedure to set
         */
        public MenuItem(String prompt, Thunk procedure) {
            this.procedure = procedure;
            this.prompt = prompt;
        }
    }
    
    /**
     * Public provider of a void procedure
     */
    final public static Thunk NOTHING = () -> {};
    
    /**
     * Properties of the class
     */
    private String title;
    private ArrayList<MenuItem> items;
    private short index = 0;
    private short exit = 0;

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the items
     */
    public ArrayList<MenuItem> getItems() {
        return this.items;
    }
    
    /**
     * @return the exit
     */
    public short getExit() {
        return this.exit;
    }
    
    /**
     * @return flag that indicates if there is an exit option
     */
    public boolean hasExit() {
        return this.exit != 0;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<MenuItem> items) {
        this.items.clear();
        this.index = 0;
        for (MenuItem item : items) {
            this.items.add(item.setPrompt(String.format("%d > %s", ++this.index, item.getPrompt())));
        }
    }
    
    /**
     * @param item the item to add
     * @return the instance itself
     */
    public Menu addItem(MenuItem item) {
        return this.addItem(item, false);
    }
    
    /**
     * @param item the item to add
     * @param isExit flag that indicates if this is the exit option
     * @return the instance itself
     */
    public Menu addItem(MenuItem item, boolean isExit) {
        this.items.add(item.setPrompt(String.format("%d > %s", ++this.index, item.getPrompt())));
        if (isExit) this.exit = this.index;
        return this;
    }
    
    /**
     * @param prompt the prompt to set
     * @param procedure the procedure to set
     */
    public void addItem(String prompt, Thunk procedure) {
        this.addItem(prompt, procedure, false);
    }
    
    /**
     * @param prompt the prompt to set
     * @param procedure the procedure to set
     * @param isExit flag that indicates if this is the exit option
     */
    public void addItem(String prompt, Thunk procedure, boolean isExit) {
        this.items.add(new MenuItem(String.format("%d > %s", ++this.index, prompt), procedure));
        if (isExit) this.exit = this.index;
    }
    
    /**
     * Prints the menu to the screen
     */
    public void show() {
        System.out.print(this);
    }
    
    /**
     * @param PROMPT the prompt to show the user asking for a menu option
     * @param NOOPTION the message to show if an unavailable option is selected
     * @param ERRMSG the message to show if an error occurs while getting an option
     * @return the option to choose from the menu
     */
    public short getOption(final String PROMPT, final String NOOPTION, final String ERRMSG) {
        Function<Short, Boolean> condition = n -> (n > 0) && (n <= this.index);
        short option = 0;
        
        do {
            try {
                System.out.printf("%s", PROMPT);
                option = Read.asShort();
                if (!condition.apply(option)) {
                    System.out.println(NOOPTION);
                }
            } catch (Exception e) {
                System.out.println(ERRMSG);
            }
        } while (!condition.apply(option));
        
        return option;
    }
    
    public short getOption(final String prompt) {
        return this.getOption(prompt, "Option not in the menu.", "That is not an option.");
    }
    
    /**
     * @param option the option non zero indexed to be executed
     * @throws Exception 
     */
    public void execute(short option) throws Exception {
        this.items.get(option-1).getProcedure().apply();
    }
    
    /**
     * @return the String representation of the class
     */
    @Override
    public String toString() {
        String output = String.format("%s\n", this.title);
        for (MenuItem item : this.items) {
            output += String.format("%s\n", item);
        }
        return output;
    }
    
    /**
     * Constructor of the class
     */
    public Menu() {
        this.items = new ArrayList<>();
        this.title = "";
    }
    
    /**
     * Constructor of the class
     * @param title the initial title to set
     */
    public Menu(String title) {
        this.items = new ArrayList<>();
        this.title = title;
    }
}

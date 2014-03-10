/*
 * Copyright (C) 2014 Allen
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

package classdiagrameditor;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Allen
 */
public class Momento {
    private static Momento instance_ = null;
    private static List<Element> savedState_ = new LinkedList<Element>();
    private static List<Element> previousState_ = new LinkedList<Element>();
    
    // State of mouse dragging action
    public enum MomentoActionList {
        UNDO, // Undo last action
        REDO, // Redo last action
    };
    
    /**
     * getInstace - public function used for returning singleton class 
     * @return instance_
     */
    public static Momento getInstance() {
        if (instance_ == null)
            return new Momento();
        else
            return instance_;
    }
    
    /**
     * saveState - saves the current state of the element list into memory
     * @param state - contains the current state information to save
     * @param action - action is used to determine whether if the action requested
     *   is UNDO or REDO
     */
    public void setState(List<Element> state, MomentoActionList action) {
        if (action == MomentoActionList.UNDO) {
            for (Element e : state) {
                if (!savedState_.contains(e))
                    savedState_.add(0,e);
            }
        }
        else {
            for (Element e : state) {
                if (!previousState_.contains(e))
                previousState_.add(0,e);
            }
        }
    }
    
    /**
     * getState - returns a state in the element list saved in memory
     * @param action - action is used to determine whether if the action requested
     *   is UNDO or REDO
     * @return previous saved state
     */
    public List<Element> getState(MomentoActionList action) {
        if (action == MomentoActionList.UNDO) {
            return savedState_;
        }
        else
            return previousState_;
    }
}

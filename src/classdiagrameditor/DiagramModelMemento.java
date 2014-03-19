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

/**
 * Testing editor check in, 1..2..3..
 * @author Allen
 */
public class DiagramModelMemento {
    private DiagramModel savedState_;
    private int pass_;

    /**
     * saveState - saves the current state of the element list into memory
     * @param state - contains the current state information to save
     */
    public void setState(DiagramModel state, String pass) {
        pass_ = pass.hashCode();
        savedState_ = state;
    }
    
    /**
     * getState - returns a state in the element list saved in memory
     * @return saved state
     */
    public DiagramModel getState(String pass) {
        int h = pass.hashCode();
        
        if (this.pass_ == h)
            return savedState_;
        else
            return null;
    }
}

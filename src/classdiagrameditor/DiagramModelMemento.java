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
 * Testing editor check in, 1..2..3..
 * @author Allen
 */
public class DiagramModelMemento {
    private final List<Element> savedState_ = new LinkedList<Element>();

    /**
     * saveState - saves the current state of the element list into memory
     * @param state - contains the current state information to save
     */
    public void setState(DiagramModel state) {
        for (Element e : state) {
            savedState_.add(0, e.makeCopy());
        }
    }
    
    /**
     * getState - returns a state in the element list saved in memory
     * @return saved state
     */
    public List<Element> getState() {
        return savedState_;
    }
}

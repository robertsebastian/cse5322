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

import java.awt.Point;

/**
 *
 * @author Allen
 */
public class CompositionRelationship extends RelationshipElement{
    
    CompositionRelationship(Element src, Element dest, Point pos) {
        super(src, dest, pos);
    }

    CompositionRelationship(RelationshipElement e) {
        super(e);
    }
    
    CompositionRelationship() {
        super();
    }
    
    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit((CompositionRelationship)this);
    }
    
    @Override
    public  Element makeCopy() {
        return new CompositionRelationship(this);
    }
}

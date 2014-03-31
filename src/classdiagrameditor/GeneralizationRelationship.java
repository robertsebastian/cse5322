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
 *
 * @author Allen
 */
public class GeneralizationRelationship extends RelationshipElement{
    
    GeneralizationRelationship(ClassElement src, ClassElement dest) {
        super(src, dest);
        super.setLabel("NewRelation" + getId());
        super.setSrcMultiplicity("1");
        super.setDestMultiplicity("1");
        super.setSource(src);
        super.setDest(dest);
    }

    GeneralizationRelationship(RelationshipElement e) {
        super(e);
        super.setLabel("NewRelation" + getId());
        super.setSrcMultiplicity("1");
        super.setDestMultiplicity("1");
        super.setSource(e.getSource());
        super.setDest(e.getDest());
    }
    
    GeneralizationRelationship() {
        super();
    }
    
    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}

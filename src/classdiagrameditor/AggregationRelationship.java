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
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Allen
 */
public class AggregationRelationship extends RelationshipElement{
    
    AggregationRelationship(Element src, Element dest, Point pos) {
        super(src, dest, pos);
        super.setLabel("NewRelation" + getId());
        super.setSrcMultiplicity("1");
        super.setDestMultiplicity("1");
        super.setStyle(Style.AGGREGATION);
    }

    AggregationRelationship(RelationshipElement e) {
        super(e);
        super.setLabel("NewRelation" + getId());
        super.setSrcMultiplicity("1");
        super.setDestMultiplicity("1");
        super.setStyle(Style.AGGREGATION);
    }
    
    AggregationRelationship(long id) {
        super(id);
        super.setStyle(Style.AGGREGATION);
    }
    
    AggregationRelationship() {
        super();
        super.setStyle(Style.AGGREGATION);
    }
    
    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit((AggregationRelationship)this);
    }
    
    @Override
    public  Element makeCopy() {
        return new AggregationRelationship(this);
    }
    
    public boolean getMembersSet() {
        return super.getMembersSet();
    }
    
    public void setID(long newID) {
        super.setID(newID);
    }
    
    public void setXMLreader(XMLStreamReader newReader) {
        super.setXMLreader(newReader);
    }
    
    public void readXML() {
        super.readXML();
    }
}

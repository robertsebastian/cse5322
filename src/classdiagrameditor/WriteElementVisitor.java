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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Allen
 */
public class WriteElementVisitor implements ElementVisitor{
    private final XMLStreamWriter writer_;
    
    public WriteElementVisitor(XMLStreamWriter writer) {
        writer_ = writer;
    }
    
    @Override
    public void visit(ClassElement element) {
        try {
            writer_.writeStartElement("ClassElement");
            // Write Position
            writer_.writeStartElement("Position");
            writer_.writeAttribute("X", Integer.toString(element.getBoxLocation().x));
            writer_.writeAttribute("Y", Integer.toString(element.getBoxLocation().y));
            writer_.writeEndElement();
            
            // Write Size
            writer_.writeStartElement("Size");
            writer_.writeAttribute("Width", Integer.toString(element.getBoxSize().width));
            writer_.writeAttribute("Height", Integer.toString(element.getBoxSize().height));
            writer_.writeEndElement();
            
            // Write Name
            writer_.writeStartElement("Name");
            writer_.writeAttribute("Name", element.getName());
            writer_.writeEndElement();
            
            // Write isAbstract
            writer_.writeStartElement("isAbstract");
            if (element.getIsAbstract())
                writer_.writeAttribute("isAbstract", "true");
            else
                writer_.writeAttribute("isAbstract", "false");
            writer_.writeEndElement();
            
            // Write Properties
            writer_.writeStartElement("Properties");
            int count = 1;
            for (Object property : element.getAttributes()) {
                writer_.writeAttribute("property" + Integer.toString(count), property.toString());
                count++;
            } 
            writer_.writeEndElement();
            
            // Write Operations
            writer_.writeStartElement("Operations");
            count = 1;
            for (Object operation : element.getOperations()) {
                writer_.writeAttribute("operation" + Integer.toString(count), operation.toString());
                count++;
            } 
            writer_.writeEndElement();
            
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    public void visit(RelationshipElement element) {
        try {
            // Write style
            writer_.writeStartElement("Style");
            writer_.writeAttribute("Style", element.getStyle().toString());
            writer_.writeEndElement();

            // Write name
            writer_.writeStartElement("Label");
            writer_.writeAttribute("Label", element.getLabel());
            writer_.writeEndElement();
            
            // Write Source Class Name
            writer_.writeStartElement("SourceClass");
            writer_.writeAttribute("Source", Long.toString(element.getSourceId()));
            writer_.writeEndElement();
            
            // Write Source Class Name
            writer_.writeStartElement("DestinationClass");
            writer_.writeAttribute("Destination", Long.toString(element.getDestId()));
            writer_.writeEndElement();
            
            // Write srcMultiplicity
            writer_.writeStartElement("srcMultiplicity");
            writer_.writeAttribute("srcMultiplicity", element.getSrcMultiplicity());
            writer_.writeEndElement();
            
            // Write destMultiplicity
            writer_.writeStartElement("destMultiplicity");
            writer_.writeAttribute("destMultiplicity", element.getDestMultiplicity());
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
}

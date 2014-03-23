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

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.stream.XMLOutputFactory;
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
            // Write name
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
            for (Object property : element.getProperties()) {
                writer_.writeAttribute("property", property.toString());
            } 
            writer_.writeEndElement();
            
            // Write Operations
            writer_.writeStartElement("Operations");
            for (Object operation : element.getOperations()) {
                writer_.writeAttribute("operation", operation.toString());
            } 
            writer_.writeEndElement();
            
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public void visit(RelationshipElement element) {
        try {
            writer_.writeStartElement("RelationshipElement");
            // Write name
            writer_.writeStartElement("Label");
            writer_.writeAttribute("Label", element.getLabel());
            writer_.writeEndElement();
            
            // Write srcMultiplicity
            writer_.writeStartElement("srcMultiplicity");
            writer_.writeAttribute("srcMultiplicity", element.getSrcMultiplicity());
            writer_.writeEndElement();
            
            // Write destMultiplicity
            writer_.writeStartElement("destMultiplicity");
            writer_.writeAttribute("destMultiplicity", element.getDestMultiplicity());
            writer_.writeEndElement();
            
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public void visit(CommentElement e) {

    }
}

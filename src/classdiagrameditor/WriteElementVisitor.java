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
    private final DiagramManager diagram_;
    private final File fileName_;
    
    public WriteElementVisitor(DiagramManager diagram, File fileName) {
        diagram_ = diagram;
        fileName_ = fileName;
    }
    
    @Override
    public void visit(ClassElement element) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

         try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(
                    new FileWriter(fileName_, true));

            writer.writeStartDocument();
            writer.writeStartElement("ClassElement");
            // Write name
            writer.writeStartElement("Name");
            writer.writeAttribute("Name", element.getName());
            writer.writeEndElement();
            
            // Write isAbstract
            writer.writeStartElement("isAbstract");
            if (element.getIsAbstract())
                writer.writeAttribute("isAbstract", "true");
            else
                writer.writeAttribute("isAbstract", "false");
            writer.writeEndElement();
            
            // Write Properties
            writer.writeStartElement("Properties");
            for (Object property : element.getProperties()) {
                writer.writeAttribute("property", property.toString());
            } 
            writer.writeEndElement();
            
            // Write Operations
            writer.writeStartElement("Operations");
            for (Object operation : element.getOperations()) {
                writer.writeAttribute("operation", operation.toString());
            } 
            writer.writeEndElement();
            
            writer.writeEndDocument();
            writer.flush();
            writer.close();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public void visit(RelationshipElement element) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(
                    new FileWriter(fileName_, true));

            writer.writeStartDocument();
            writer.writeStartElement("RelationshipElement");
            // Write name
            writer.writeStartElement("Label");
            writer.writeAttribute("Label", element.getLabel());
            writer.writeEndElement();
            
            // Write srcMultiplicity
            writer.writeStartElement("srcMultiplicity");
            writer.writeAttribute("srcMultiplicity", element.getSrcMultiplicity());
            writer.writeEndElement();
            
            // Write destMultiplicity
            writer.writeStartElement("destMultiplicity");
            writer.writeAttribute("destMultiplicity", element.getDestMultiplicity());
            writer.writeEndElement();
            
            writer.writeEndDocument();
            writer.flush();
            writer.close();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public void visit(CommentElement e) {

    }
}

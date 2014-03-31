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
            for (Object property : element.getProperties()) {
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
    
    @Override
    public boolean visit(ClassElement e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor ClassElement not supported yet."); //To change body of generated methods, choose Tools | Templates.        
    }
    
    @Override
    public void visit(AggregationRelationship element) {
        try {
            writer_.writeStartElement("AggregationRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(AggregationRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor AggregationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(AssociationRelationship element) {
        try {
            writer_.writeStartElement("AssociationRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(AssociationRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor AssociationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(GeneralizationRelationship element) {
        try {
            writer_.writeStartElement("GeneralizationRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(GeneralizationRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor GeneralizationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(CompositionRelationship element) {
        try {
            writer_.writeStartElement("CompositionRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(CompositionRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor CompositionRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(RealizationRelationship element) {
        try {
            writer_.writeStartElement("RealizationRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(RealizationRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor RealizationRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(DependencyRelationship element) {
        try {
            writer_.writeStartElement("DependencyRelationship");
            writeRelationshipElement(element);
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    @Override
    public boolean visit(DependencyRelationship e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor DependencyRelationship not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void writeRelationshipElement(RelationshipElement element) {
        try {
            // Write name
            writer_.writeStartElement("Label");
            writer_.writeAttribute("Label", element.getLabel());
            writer_.writeEndElement();
            
            // Write Source Class Name
            writer_.writeStartElement("SourceClass");
            writer_.writeAttribute("Source", element.getSrcClassName());
            writer_.writeEndElement();
            
            // Write Source Class Name
            writer_.writeStartElement("DestinationClass");
            writer_.writeAttribute("Destination", element.getDestClassName());
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
    
    @Override
    public void visit(CommentElement e) {

    }
    
    @Override
    public boolean visit(CommentElement e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor CommentElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(PackageElement e) {

    }
    
    @Override
    public boolean visit(PackageElement e, String name) {
        throw new UnsupportedOperationException("WriteElementVisitor PackageElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

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

    public void writeProperty(ClassElement.Property prop) throws XMLStreamException {
        writer_.writeStartElement("Property");
        writer_.writeAttribute("class", prop.getClass().getName());
        writer_.writeAttribute("scope", prop.scope.toString());
        writer_.writeAttribute("visibility", prop.visibility.toString());
        writer_.writeAttribute("name", prop.name);
        writer_.writeAttribute("type", prop.type);
        writer_.writeAttribute("isAbstract", Boolean.toString(prop.isAbstract));
        writer_.writeEndElement();
    }
    
    @Override
    public void visit(ClassElement element) {
        try {
            writer_.writeStartElement("Element");
            writer_.writeAttribute("id", Long.toString(element.getId()));
            writer_.writeAttribute("class", ClassElement.class.getName());
            writer_.writeAttribute("role", element.getRole());

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
            
            // Write Package
            writer_.writeStartElement("Package");
            writer_.writeAttribute("Package", element.getPackage());
            writer_.writeEndElement();
            
            // Write isAbstract
            writer_.writeStartElement("isAbstract");
            if (element.getIsAbstract())
                writer_.writeAttribute("isAbstract", "true");
            else
                writer_.writeAttribute("isAbstract", "false");
            writer_.writeEndElement();
            
            // Write Properties
            for (ClassElement.Attribute attr : element.getAttributes()) {
                writeProperty(attr);
            }
            for (ClassElement.Operation op : element.getOperations()) {
                writeProperty(op);
                for (ClassElement.Parameter param : op.parameters) {
                    writeProperty(param);
                }
            }
            
            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    private void visitGenericRelationship(RelationshipElement element) {
        try {
            // Class name
            writer_.writeStartElement("Element");
            writer_.writeAttribute("id", Long.toString(element.getId()));
            writer_.writeAttribute("class", element.getClass().getName());
            writer_.writeAttribute("srcRole", element.getSrcRole());
            writer_.writeAttribute("destRole", element.getDestRole());
            writer_.writeAttribute("srcAnchor", Integer.toString(element.getSrcAnchor()));
            writer_.writeAttribute("destAnchor", Integer.toString(element.getDestAnchor()));

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

            writer_.writeEndElement();
         } catch (XMLStreamException e) {
             e.printStackTrace();
         }
    }
    
    public void visit(DependencyRelationship element) {
        visitGenericRelationship(element);
    }
    
    public void visit(AggregationRelationship element) {
        visitGenericRelationship(element);
    }
    
    public void visit(AssociationRelationship element) {
        visitGenericRelationship(element);
    }
    
    public void visit(CompositionRelationship element) {
        visitGenericRelationship(element);
    }
    
    public void visit(InheritanceRelationship element) {
        visitGenericRelationship(element);
    }
}

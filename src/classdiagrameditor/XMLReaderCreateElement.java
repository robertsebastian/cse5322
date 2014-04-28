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
import java.lang.reflect.Method;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Allen
 */
public class XMLReaderCreateElement {
    private final DiagramModel diagramModel_;
    private final XMLStreamReader reader_;
    
    public XMLReaderCreateElement(DiagramModel diagramModel, XMLStreamReader reader) {
        diagramModel_ = diagramModel;
        reader_ = reader;
    }
    
    public void readFile() {
        //no paramater
	Class noparams[] = {};
        
        try {
            //load the AppTest at runtime
            Class cls = Class.forName("classdiagrameditor.XMLReaderCreateElement");
            Object obj = cls.newInstance();

            // Parse the XML
            while(reader_.hasNext()) {
                reader_.next();
                Method method = cls.getDeclaredMethod(reader_.getLocalName(), noparams);
                method.invoke(obj, null);
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
	}
    }
    public void ClassElement(){
        Point pos = new Point(0,0);
        ClassElement e = new ClassElement(pos);
        diagramModel_.add(e);
    }
}

package classdiagrameditor;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ClassElement extends BoxElement {
    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class

    public String getName() {return name_;}
    public void setName(String name) {name_ = name;}
    public boolean getIsAbstract() {return isAbstract_;}
    public void setIsAbstract(boolean value) {isAbstract_ = value;}

    private final List<Attribute> attributes_ = new ArrayList<Attribute>(); // List of class properties
    private final List<Operation> operations_ = new ArrayList<Operation>(); // List of class properties
    public List<Attribute> getAttributes() {return attributes_;}
    public List<Operation> getOperations() {return operations_;}
    
    public enum ScopeType {Classifier, Instance};
    public static enum VisibilityType {Default, Public, Protected, Private, Package};

    private static final EnumMap<VisibilityType, String> visibilityStr = new EnumMap<VisibilityType, String>(VisibilityType.class) {{
        put(VisibilityType.Default,   " ");
        put(VisibilityType.Public,    "+");
        put(VisibilityType.Protected, "#");
        put(VisibilityType.Private,   "-");
        put(VisibilityType.Package,   "~");
    }};

    public static class Property {
        public String type;
        public String name;
        public VisibilityType visibility;
        public ScopeType scope;

        public Property(String _name, String _type, VisibilityType _visibility, ScopeType _scope) {
            name         = _name;
            type         = _type;
            visibility   = _visibility;
            scope        = _scope;
        }

    }

    public static class Attribute extends Property {
        public Attribute() {
            this("attribute", "int", VisibilityType.Public, ScopeType.Instance);
        }

        public Attribute(String _name, String _type, VisibilityType _visibility, ScopeType _scope) {
            super(_name, _type, _visibility, _scope);
        }

        @Override
        public String toString() {
            return visibilityStr.get(visibility) + " " + name + (type.isEmpty() ? "" : " : " + type);
        }
    }

    public static class Parameter extends Property {
        public Parameter() {
            this("parameter", "int");
        }

        public Parameter(String _name, String _type) {
            super(_name, _type, VisibilityType.Public, ScopeType.Instance);
        }

        @Override
        public String toString() {
            if (type.isEmpty()) return name;
            return name + " : " + type;
        }
    }

    public static class Operation extends Property {
        public LinkedList<Parameter> parameters = new LinkedList<Parameter>();

        public Operation() {
            this("operation", "void", VisibilityType.Public, ScopeType.Instance);
        }

        public Operation(String _name, String _type, VisibilityType _visibility, ScopeType _scope) {
            super(_name, _type, _visibility, _scope);
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(visibilityStr.get(visibility)).append(" ");
            b.append(name).append("(");
            Joiner.on(", ").appendTo(b, parameters);
            b.append(")");
            if (!type.isEmpty()) b.append(" : ").append(type);

            return b.toString();
        }
    }

    public ClassElement(Point pos) {
        super(pos);

        name_ = "NewClass" + getId();
        isAbstract_ = false;
    }

    public ClassElement(ClassElement e) {
        super(e);

        name_ = e.name_;
        isAbstract_ = e.isAbstract_;
        attributes_.addAll(e.attributes_);
        operations_.addAll(e.operations_);
    }
    
    public ClassElement(long id) {
        super(id);
    }
    
    public ClassElement() {}
    
    @Override
    public Element makeCopy() {
        return new ClassElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
    
    @Override
    public void readXML(XMLStreamReader reader_) throws XMLStreamException {
        // Read Position
        reader_.next(); // Position Beginning
        Point p = new Point(Integer.parseInt(reader_.getAttributeValue(0)),
                            Integer.parseInt(reader_.getAttributeValue(1)));
        setBoxLocation(p);
        reader_.next(); // Position End

        // Read Size
        reader_.next(); // Size Beginning
        Dimension d = new Dimension(Integer.parseInt(reader_.getAttributeValue(0)),
                            Integer.parseInt(reader_.getAttributeValue(1)));
        setBoxSize(d);
        reader_.next(); // Size End

        // Read Name
        reader_.next(); // Name Beginning
        setName(reader_.getAttributeValue(0));
        reader_.next(); // Name End

        // Read isAbstract
        reader_.next(); // isAbstract Beginning
        setIsAbstract(reader_.getAttributeValue(0).matches("true"));
        reader_.next(); // isAbstract End

        // Read Properties
        reader_.next(); // Properties Beginning
        while (reader_.getLocalName().equals("Property")) {
            ClassElement.Property prop = null;
            try {
                prop = (ClassElement.Property)Class.forName(
                        reader_.getAttributeValue(null, "class")).newInstance();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClassElement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ClassElement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ClassElement.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop.name = reader_.getAttributeValue(null, "name");
            prop.type = reader_.getAttributeValue(null, "type");
            prop.visibility = ClassElement.VisibilityType.valueOf(
                    reader_.getAttributeValue(null, "visibility"));
            prop.scope = ClassElement.ScopeType.valueOf(
                    reader_.getAttributeValue(null, "scope"));

            if (prop instanceof ClassElement.Attribute)
                getAttributes().add((ClassElement.Attribute)prop);
            if (prop instanceof ClassElement.Operation)
                getOperations().add((ClassElement.Operation)prop);
            if (prop instanceof ClassElement.Parameter)
                Iterables.getLast(getOperations()).parameters.add((ClassElement.Parameter)prop);

            reader_.next(); // Read end of element
            reader_.next(); // Read start of next element
        }
    }
}
package classdiagrameditor;

import com.google.common.base.Joiner;
import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

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
    
    @Override
    public Element makeCopy() {
        return new ClassElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}
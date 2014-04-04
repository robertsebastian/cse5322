package classdiagrameditor;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class ClassElement extends BoxElement {
    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class
    private List<String> attributes_; // List of class properties
    private List<String> operations_; // List of class operations

    public enum PropertiesType {ATTRIBUTES, OPERATIONS};
    
    private enum ScopeType {CLASSIFIER, INSTANCE};

    public String getName() {return name_;}
    public void setName(String name) {name_ = name;}
    public boolean getIsAbstract() {return isAbstract_;}
    public void setIsAbstract(boolean value) {isAbstract_ = value;}
    public List getAttributes() {return attributes_;}
    public List getOperations() {return operations_;}
    
    private class Member {
        
        private String message;
        private ScopeType scope;
        
        public Member (String val, ScopeType scopeT) {
            message = val;
            scope = scopeT;
        }
        
        public void setAttibute(String val) {
            message = val;
        }
        
        public void setScope(ScopeType scopeT) {
            scope = scopeT;
        }
        
        public String getAttibute(String val) {
            return message;
        }
        
        public ScopeType getScope(ScopeType scopeT) {
            return scope;
        } 
    }

    public ClassElement(Point pos) {
        super(pos);

        name_ = "NewClass" + getId();
        isAbstract_ = false;
        attributes_ = new LinkedList<String>();
        operations_ = new LinkedList<String>();

        attributes_.add("attribute1 : String");
        attributes_.add("attribute2 : String");
        attributes_.add("attribute3 : String");
        attributes_.add("attribute4 : String");

        operations_.add("operation1(String x, int y)");
        operations_.add("operation2(double x, double y, double z)");
        operations_.add("operation3(List x, int y)");
    }

    public ClassElement(ClassElement e) {
        super(e);

        name_ = e.name_;
        isAbstract_ = e.isAbstract_;
        attributes_ = new LinkedList<String>(e.attributes_);
        operations_ = new LinkedList<String>(e.operations_);
    }
    
    public ClassElement() {
        super();
        attributes_ = new LinkedList<String>();
        operations_ = new LinkedList<String>();
    }
    
    @Override
    public Element makeCopy() {
        return new ClassElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
    
    public List getProperties(PropertiesType type) {
        return type == PropertiesType.ATTRIBUTES ? getAttributes() : getOperations();
    }
}
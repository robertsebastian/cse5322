package classdiagrameditor;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ClassElement extends BoxElement {
    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class
    private List<String> properties_; // List of class properties
    private List<String> operations_; // List of class operations

    public String getName() {return name_;}
    public void setName(String name) {name_ = name;}
    public boolean getIsAbstract() {return isAbstract_;}
    public void setIsAbstract(boolean value) {isAbstract_ = value;}
    public List getProperties() {return properties_;}
    public void addProperty(String property) {properties_.add(property);}
    public List getOperations() {return operations_;}
    public void addOperation(String operation) {operations_.add(operation);}
    public Point getLocation() {return super.getBoxLocation();}
    public void setLocation(Point pos) {super.setBoxLocation(pos);}

    public ClassElement(Point pos) {
        super(pos);

        name_ = "NewClass" + getId();
        isAbstract_ = false;
        properties_ = new LinkedList<String>();
        operations_ = new LinkedList<String>();

        properties_.add("attribute1 : String");
        properties_.add("attribute2 : String");
        properties_.add("attribute3 : String");
        properties_.add("attribute4 : String");

        operations_.add("operation1(String x, int y)");
        operations_.add("operation2(double x, double y, double z)");
        operations_.add("operation3(List x, int y)");
    }

    public ClassElement(ClassElement e) {
        super(e);

        name_ = e.name_;
        isAbstract_ = e.isAbstract_;
        properties_ = new LinkedList<String>(e.properties_);
        operations_ = new LinkedList<String>(e.operations_);
    }
    
    public ClassElement() {
        super();
        properties_ = new LinkedList<String>();
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
    
    @Override
    public boolean accept(ElementVisitor elementVisitor, String name) {
        return elementVisitor.visit(this, name);
    }
}
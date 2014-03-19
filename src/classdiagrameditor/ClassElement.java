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
    public boolean getIsAbstract() { return isAbstract_;}
    public List getProperties() {return properties_;}
    public List getOperations() {return operations_;}

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
    
    @Override
    public Element makeCopy() {
        return new ClassElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}
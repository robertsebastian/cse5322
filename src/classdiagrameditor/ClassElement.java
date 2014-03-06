package classdiagrameditor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ClassElement extends Element {
    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class
    private List<String> properties_; // List of class properties
    private List<String> operations_; // List of class operations

    private Rectangle area_ = new Rectangle();

    public String getName() {return name_;}
    public Collection getProperties() {return properties_;}
    public Collection getOperations() {return operations_;}

    public ClassElement(Point pos) {
        super(pos);

        area_.setLocation(pos);
        area_.setSize(200, 200);
        setBounds(area_);

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

    @Override
    public Point getLocation() {
        return area_.getLocation();
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }

    @Override
    public void drag(Point point, int dx, int dy) {
        super.drag(point, dx, dy);
        area_.translate(dx, dy);
        setBounds(area_);
        notifyObservers();
    }
}
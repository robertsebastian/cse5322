package classdiagrameditor;

import java.awt.Point;
import java.awt.Rectangle;

public class PackageElement extends BoxElement {
    private String name_;

    private final Rectangle innerArea_ = new Rectangle();

    public PackageElement(Point pos) {
        super(pos);
        name_ = "NewPackage" + getId();
    }

    public PackageElement(PackageElement e) {
        super(e);
        name_ = e.name_;
        innerArea_.setRect(e.innerArea_);
    }

    public void setName(String name) {name_ = name;}
    public String getName() {return name_;}

    @Override
    public Element makeCopy() {
        return new PackageElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }   

    @Override
    public boolean intersects(Rectangle rectangle) {
        return super.intersects(rectangle); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(Point point) {
        return super.contains(point); //To change body of generated methods, choose Tools | Templates.
    }

    public void computeInnerArea() {

    }
}

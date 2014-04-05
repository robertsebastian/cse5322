package classdiagrameditor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ClassElement extends BoxElement {
    private String name_;             // Name of class
    private boolean isAbstract_;      // True if this represents an abstract class
    private final List<Member> attributes_ = new ArrayList<Member>(); // List of class properties
    private final List<Member> operations_ = new ArrayList<Member>(); // List of class operations

    public enum PropertiesType {Attributes, Operations};
    public enum ScopeType {Classifier, Instance};
    public enum VisibilityType {None, Public, Protected, Private, Package};

    public String getName() {return name_;}
    public void setName(String name) {name_ = name;}
    public boolean getIsAbstract() {return isAbstract_;}
    public void setIsAbstract(boolean value) {isAbstract_ = value;}
    public List<Member> getAttributes() {return attributes_;}
    public List<Member> getOperations() {return operations_;}
    
    public static class Member {
        public VisibilityType visibility;
        public ScopeType scope;
        public String text;

        public Member(ScopeType _scope, VisibilityType _visibility, String _text) {
            scope      = _scope;
            visibility = _visibility;
            text       = _text;
        }
    }

    public ClassElement(Point pos) {
        super(pos);

        name_ = "NewClass" + getId();
        isAbstract_ = false;

        attributes_.add(new Member(ScopeType.Instance, VisibilityType.Public, "attribute1 : String"));
        attributes_.add(new Member(ScopeType.Instance, VisibilityType.Protected, "attribute2 : String"));
        attributes_.add(new Member(ScopeType.Classifier, VisibilityType.Private, "attribute3 : String"));

        operations_.add(new Member(ScopeType.Instance, VisibilityType.Private, "operation1(String x, int y)"));
        operations_.add(new Member(ScopeType.Instance, VisibilityType.Private, "operation2(double x, double y, double z)"));
        operations_.add(new Member(ScopeType.Instance, VisibilityType.Private, "operation3(List x, int y)"));
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
    
    public List<Member> getProperties(PropertiesType type) {
        return type == PropertiesType.Attributes ? getAttributes() : getOperations();
    }
}
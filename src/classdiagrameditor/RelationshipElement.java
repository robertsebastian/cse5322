package classdiagrameditor;

public class RelationshipElement extends LineConnectorElement {
    private String label;
    private String srcMultiplicity;
    private String destMultiplicity;
    
    public enum RelationshipType {
        AGGREGATION, COMPOSITION, INHERITANCE, ASSOCIATION, DEPENDENCY
    }
    
    RelationshipElement(ClassElement src, ClassElement dest) {
        super(src, dest);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}
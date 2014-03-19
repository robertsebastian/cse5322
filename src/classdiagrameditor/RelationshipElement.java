package classdiagrameditor;

public class RelationshipElement extends LineConnectorElement {
    private String label_;
    private String srcMultiplicity_;
    private String destMultiplicity_;
    
    public enum RelationshipType {
        AGGREGATION, COMPOSITION, INHERITANCE, ASSOCIATION, DEPENDENCY
    }
    
    public String getLabel() {return label_;}
    public String getSrcMultiplicity() {return srcMultiplicity_;}
    public String getDestMultiplicity() {return destMultiplicity_;}
    
    RelationshipElement(ClassElement src, ClassElement dest) {
        super(src, dest);
        label_ = "NewRelation" + getId();
        srcMultiplicity_ = "1";
        destMultiplicity_ = "1";
    }

    RelationshipElement(RelationshipElement e) {
        super(e);
        label_ = "NewRelation" + getId();
        srcMultiplicity_ = "1";
        destMultiplicity_ = "1";
    }

    @Override
    public Element makeCopy() {
        return new RelationshipElement(this);
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}
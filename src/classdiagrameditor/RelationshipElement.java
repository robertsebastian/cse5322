package classdiagrameditor;

public class RelationshipElement extends LineConnectorElement {
    private String label_;
    private String srcMultiplicity_;
    private String destMultiplicity_;
    private String srcClassName_;
    private String destClassName_;

    public enum Style {
        AGGREGATION, COMPOSITION, INHERITANCE, ASSOCIATION, DEPENDENCY
    }
    private Style style_ = Style.DEPENDENCY;

    public Style getStyle() {return style_;}
    public void setStyle(Style style) {style_ = style;}

    public String getLabel() {return label_;}
    public void setLabel(String label) {label_ = label;}
    public String getSrcMultiplicity() {return srcMultiplicity_;}
    public void setSrcMultiplicity(String srcMultiplicity) {
        srcMultiplicity_ = srcMultiplicity;
    }
    public String getDestMultiplicity() {return destMultiplicity_;}
    public void setDestMultiplicity(String destMultiplicity) {
        destMultiplicity_ = destMultiplicity;
    }
    public void setSource(ClassElement src) {
        super.setSource(src); 
        srcClassName_ = src.getName();
    }
    public String getSrcClassName() {return srcClassName_;}
    public void setDest(ClassElement dest) {
        super.setDest(dest);
        destClassName_ = dest.getName();
    }
    public String getDestClassName() {return destClassName_;}

    RelationshipElement() {
        super();
    }
    
    RelationshipElement(ClassElement src, ClassElement dest) {
        super(src, dest);
        label_ = "NewRelation" + getId();
        srcMultiplicity_ = "1";
        destMultiplicity_ = "1";
        srcClassName_ = src.getName();
        destClassName_ = dest.getName();
    }

    RelationshipElement(RelationshipElement e) {
        super(e);
        label_ = "NewRelation" + getId();
        srcMultiplicity_ = "1";
        destMultiplicity_ = "1";
        srcClassName_ = e.getSrcClassName();
        destClassName_ = e.getDestClassName();
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
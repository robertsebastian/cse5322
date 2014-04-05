package classdiagrameditor;

import java.awt.Point;

public class RelationshipElement extends LineConnectorElement {
    private String label_;
    private String srcMultiplicity_;
    private String destMultiplicity_;

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

    RelationshipElement() {
        super();
    }
    
    RelationshipElement(Element src, Element dest, Point pos) {
        super(src, dest, pos);
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
    
    public void setSrc(Element e) {
        super.setSource(e);
    }
    
    public void setDest(Element e) {
        super.setDest(e);
    }
}
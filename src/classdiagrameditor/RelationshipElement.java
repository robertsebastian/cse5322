package classdiagrameditor;

import java.awt.Point;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class RelationshipElement extends LineConnectorElement {
    private XMLStreamReader reader_;  // Reader for dynamically reading in values
    private boolean membersSet_ = false;
    private String label_;
    private String srcMultiplicity_;
    private String destMultiplicity_;

    public enum Style {
        AGGREGATION, COMPOSITION, INHERITANCE, ASSOCIATION, DEPENDENCY
    }
    private Style style_ = null;

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

    RelationshipElement(long id) {
        super(id);
    }
    
    RelationshipElement() {}
    
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

    public void setID(long newID) {
        super.setID(newID);
    }
    
    /**
     * Set this object's XML Stream Reader.
     * @param newReader of this object
     */
    public void setXMLreader(XMLStreamReader newReader) {
        reader_ = newReader;
    }
    
    public boolean getMembersSet() {
        return membersSet_;
    }
    
    @Override
    public abstract Element makeCopy();// {
        //return new RelationshipElement(this);
    //}

    @Override
    public abstract void accept(ElementVisitor elementVisitor);// {
        //elementVisitor.visit(this);
    //}
    
    public void readXML() {
        try {
            membersSet_ = true;
            
            // Read Style
            reader_.next(); // Style Beginning
            setStyle(RelationshipElement.Style.valueOf(reader_.getAttributeValue(0)));
            reader_.next(); // Style End

            // Read Label
            reader_.next(); // Label Beginning
            setLabel(reader_.getAttributeValue(0));
            reader_.next(); // Label End

            // Read Source Class ID
            reader_.next(); // Source Class ID Beginning
            setSource(Long.parseLong(reader_.getAttributeValue(0)));
            reader_.next(); // Source Class ID End

            // Read Destination Class ID
            reader_.next(); // Destination Class ID Beginning
            setDest(Long.parseLong(reader_.getAttributeValue(0)));
            reader_.next(); // Destination Class ID End

            // Read SrcMultiplicity
            reader_.next(); // SrcMultiplicity Beginning
            setSrcMultiplicity(reader_.getAttributeValue(0));
            reader_.next(); // SrcMultiplicity End

            // Read DestMultiplicity
            reader_.next(); // DestMultiplicity Beginning
            setDestMultiplicity(reader_.getAttributeValue(0));
            reader_.next(); // DestMultiplicity End
            
            reader_.next(); // Read element type End
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
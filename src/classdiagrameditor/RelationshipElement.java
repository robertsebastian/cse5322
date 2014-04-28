package classdiagrameditor;

import java.awt.Point;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class RelationshipElement extends LineConnectorElement {
    private String label_ = "";
    private String srcMultiplicity_ = "";
    private String destMultiplicity_ = "";
    private String srcRole_ = "";
    private String destRole_ = "";

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

    public String getSrcRole() {return srcRole_;}
    public void setSrcRole(String r) {srcRole_ = r;}
    public String getDestRole() {return destRole_;}
    public void setDestRole(String r) {destRole_ = r;}

    RelationshipElement() {}
    
    RelationshipElement(Element src, Element dest, Point pos) {
        super(src, dest, pos);
    }

    RelationshipElement(RelationshipElement e) {
        super(e);
        label_            = e.label_;
        srcMultiplicity_  = e.srcMultiplicity_;
        destMultiplicity_ = e.destMultiplicity_;
        srcRole_          = e.srcRole_;
        destRole_         = e.destRole_;
    }

    @Override
    public void readXML(XMLStreamReader reader_) throws XMLStreamException {
        setSrcRole(reader_.getAttributeValue(null, "srcRole"));
        setDestRole(reader_.getAttributeValue(null, "destRole"));
        setSrcAnchor(Integer.parseInt(reader_.getAttributeValue(null, "srcAnchor")));
        setDestAnchor(Integer.parseInt(reader_.getAttributeValue(null, "destAnchor")));

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
    }
}
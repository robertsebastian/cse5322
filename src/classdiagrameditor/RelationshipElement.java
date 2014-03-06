package classdiagrameditor;

public class RelationshipElement extends Element {
    private final Element src_;
    private final Element dest_;

    public RelationshipElement(Element src, Element dest) {
        super();
        src_ = src;
        dest_ = dest;

        src_.registerObserver(this);
        dest_.registerObserver(this);
    }

    public Element getSource() {return src_;}
    public Element getDest() {return dest_;}

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}
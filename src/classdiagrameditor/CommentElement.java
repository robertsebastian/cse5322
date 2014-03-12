package classdiagrameditor;

import java.awt.Point;

public class CommentElement extends BoxElement {
    private String text_;

    public CommentElement(Point pos) {
        super(pos);
    }

    public void setText(String text) {text_ = text;}
    public String getText() {return text_;}

    @Override
    public Element makeCopy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accept(ElementVisitor elementVisitor) {
        elementVisitor.visit(this);
    }
}

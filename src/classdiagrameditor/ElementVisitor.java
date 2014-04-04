package classdiagrameditor;

public interface ElementVisitor {
    public void visit(ClassElement e);
    public void visit(RelationshipElement e);
}

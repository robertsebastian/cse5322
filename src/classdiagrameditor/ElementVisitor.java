package classdiagrameditor;

public interface ElementVisitor {
    public void visit(ClassElement e);
    public void visit(RelationshipElement e);
    public void visit(CommentElement e);
    public void visit(PackageElement e);
}

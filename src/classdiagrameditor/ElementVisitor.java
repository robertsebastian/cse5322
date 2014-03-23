package classdiagrameditor;

public interface ElementVisitor {
    public void visit(ClassElement e);
    public boolean visit(ClassElement e, String name);
    public void visit(RelationshipElement e);
    public boolean visit(RelationshipElement e, String name);
    public void visit(CommentElement e);
    public boolean visit(CommentElement e, String name);
    public void visit(PackageElement e);
    public boolean visit(PackageElement e, String name);
}

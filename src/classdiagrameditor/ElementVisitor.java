package classdiagrameditor;

public interface ElementVisitor {
    public void visit(ClassElement e);
    public boolean visit(ClassElement e, String name);
    public void visit(AggregationRelationship e);
    public boolean visit(AggregationRelationship e, String name);
    public void visit(AssociationRelationship e);
    public boolean visit(AssociationRelationship e, String name);
    public void visit(GeneralizationRelationship e);
    public boolean visit(GeneralizationRelationship e, String name);
    public void visit(CompositionRelationship e);
    public boolean visit(CompositionRelationship e, String name);
    public void visit(RealizationRelationship e);
    public boolean visit(RealizationRelationship e, String name);
    public void visit(DependencyRelationship e);
    public boolean visit(DependencyRelationship e, String name);
    public void visit(CommentElement e);
    public boolean visit(CommentElement e, String name);
    public void visit(PackageElement e);
    public boolean visit(PackageElement e, String name);
}

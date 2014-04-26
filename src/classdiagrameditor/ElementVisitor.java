package classdiagrameditor;

public interface ElementVisitor {
    public void visit(ClassElement e);
    public void visit(DependencyRelationship e);
    public void visit(AggregationRelationship e);
    public void visit(AssociationRelationship e);
    public void visit(CompositionRelationship e);
    public void visit(InheritanceRelationship e);
}

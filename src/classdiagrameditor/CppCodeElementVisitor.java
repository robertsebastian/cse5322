/*
 * Copyright (C) 2014 maryingst
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package classdiagrameditor;

/**
 *
 * @author maryingst
 */
public class CppCodeElementVisitor extends CodeElementVisitor{

    public CppCodeElementVisitor(String CurrentDirectory){
        super(CurrentDirectory);
    }
    @Override
    public void visit(ClassElement e) {
        System.out.print("visiting Class\n");
    }
    
    @Override
    public void visit(DependencyRelationship e) {
        System.out.print("visiting DependencyRelationship\n");
    }
    
    @Override
    public void visit(AggregationRelationship e) {
        System.out.print("visiting AggregationRelationship\n");
    }
    
    @Override
    public void visit(AssociationRelationship e) {
        System.out.print("visiting AggregationRelationship\n");
    }
    
    @Override
    public void visit(CompositionRelationship e) {
        System.out.print("visiting CompositionRelationship\n");
    }
    
    @Override
    public void visit(InheritanceRelationship e) {
        System.out.print("visiting InheritanceRelationship\n");
    }
}

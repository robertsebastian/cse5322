/*
 * Copyright (C) 2014 Allen
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
 * @author Allen
 */
public class CompareElementVisitor implements ElementVisitor{
    public CompareElementVisitor() {}
    
    @Override
    public void visit(ClassElement e) {
        throw new UnsupportedOperationException("CompareElementVisitor ClassElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean visit(ClassElement e, String name) {
        return (e.getName().equals(name));
    }
    
    @Override
    public void visit(RelationshipElement e) {
        throw new UnsupportedOperationException("CompareElementVisitor RelationshipElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean visit(RelationshipElement e, String name) {
        throw new UnsupportedOperationException("CompareElementVisitor RelationshipElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(CommentElement e) {
        throw new UnsupportedOperationException("CompareElementVisitor CommentElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean visit(CommentElement e, String name) {
        throw new UnsupportedOperationException("CompareElementVisitor CommentElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void visit(PackageElement e) {
        throw new UnsupportedOperationException("CompareElementVisitor PackageElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean visit(PackageElement e, String name) {
        throw new UnsupportedOperationException("CompareElementVisitor PackageElement not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

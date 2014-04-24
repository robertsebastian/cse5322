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

import java.io.File;
import java.util.Iterator;
import java.util.Set;
/**
 *
 * @author maryingst
 */
public class CodeGenerator implements SelectionObserver{
    public enum languageEnum {
        JAVA, CPP
    }
    
    private languageEnum SpecifiedLanguage;
    private String SpecifiedDirectory;
    private DiagramManager diagram_;
    private Set<Element> SelectedElements;
    
    public CodeGenerator () {
        // Set the default directory and language
        SpecifiedLanguage = languageEnum.JAVA;
        SpecifiedDirectory = System.getProperty("user.home");
        diagram_ = null;
        SelectedElements = null;
    }
    
    public void notifySelectionChanged(DiagramManager manager, Set<Element> selection){
        diagram_ = manager;
        SelectedElements = selection;        
    }
    
    public Boolean generate (languageEnum newlanguage, String newDirectory) {
        Boolean status = false;
        SpecifiedLanguage = newlanguage;
        if(newDirectory != null){
            File directorycheck = new File(newDirectory);
            if(directorycheck.exists() && directorycheck.isDirectory()){
                SpecifiedDirectory = newDirectory;
            }
        }
        
        CodeElementVisitor CodeVisitor;
        
        switch(SpecifiedLanguage){
            
            case CPP:
                CodeVisitor = new CppCodeElementVisitor(SpecifiedDirectory);
                break;
            case JAVA:
            default:
                CodeVisitor = new JavaCodeElementVisitor(SpecifiedDirectory);
                break;
        }
        
        for (Element curElement : SelectedElements)
            if(curElement != null){
                curElement.accept(CodeVisitor);
                 
        }
        
        return status;
    }
}

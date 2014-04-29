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

import classdiagrameditor.ClassElement.Property;
import classdiagrameditor.ClassElement.ScopeType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maryingst
 */
public class CppCodeElementVisitor extends CodeElementVisitor{
    
    private final String CodeDir;
    private final File Include;
    private final File Source;

    public CppCodeElementVisitor(String CurrentDirectory){
        super(CurrentDirectory);
        CodeDir = CodeGenerator.getInstance().getDirectory() + "/" +CodeGenerator.getInstance().getManager().getName();
        Include = new File(CodeDir + "/include");
        Include.mkdirs();
        Source = new File(CodeDir + "/src");
        Source.mkdirs();        
    }
    
    private PrintWriter CreateClassFile(String FilePath){
        PrintWriter NewClassFile = null;
        String FileString = FilePath;
        
        try {
                File myFile = new File(FileString);
                if(myFile.exists()) myFile.delete();
                myFile.createNewFile();
                NewClassFile = new PrintWriter(FileString);
        } catch (FileNotFoundException ex) {
                Logger.getLogger(CppCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(CppCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return NewClassFile;
    }
    
    private String WriteParameters(Property newProp){
        String PropString = "";
        PropString += "( ";
                    
            for (Iterator<ClassElement.Parameter> it = ((ClassElement.Operation)newProp).parameters.iterator(); it.hasNext();) {
                ClassElement.Parameter Parm = it.next();
                PropString += Parm.type + " ";
                if(Parm.scope == ScopeType.Classifier)
                    PropString += "static ";
                PropString += Parm.name;
                if(it.hasNext())
                    PropString += ", ";
            }
                    
        PropString += ")";
        
        return PropString;
    }
    
    private String WriteProperty(Property newProp){
        String PropString = "";
        
        PropString += "\t" + newProp.type + " ";
        
        if(newProp.scope == ScopeType.Classifier)
            PropString += "static ";
        PropString +=  newProp.name;
        if(newProp.getClass() == ClassElement.Operation.class){
            PropString += WriteParameters(newProp);
        }        
        return PropString;
    }
    
    @Override
    public void visit(ClassElement e) {
        if(Include.exists() && Source.exists()){
            
            List<Property> PrivateProps = new LinkedList<Property>();
            List<Property> PublicProps = new LinkedList<Property>();
            List<Property> ProtectedProps = new LinkedList<Property>();
            List<Property> PackageProps = new LinkedList<Property>();
            for(Property curProp: e.getAttributes()){                
                switch(curProp.visibility){
                    case Private: PrivateProps.add(curProp); break;
                    case Protected: ProtectedProps.add(curProp); break;
                    case Package: PackageProps.add(curProp); break;
                    default: case Public: PublicProps.add(curProp); break;
                }    
            }
            for(Property curProp: e.getOperations()){
                switch(curProp.visibility){
                    case Private: PrivateProps.add(curProp); break;
                    case Protected: ProtectedProps.add(curProp); break;
                    case Package: PackageProps.add(curProp); break;
                    default: case Public: PublicProps.add(curProp); break;
                } 
            }
            
            PrintWriter NewClassInclude = CreateClassFile(Include.getPath() + "/" + e.getName() + ".h");
            PrintWriter NewClassSrc = CreateClassFile(Source.getPath() + "/" + e.getName() + ".cpp");
            String SourceFileString = "";
            String IncludeFileString = "";
            
            IncludeFileString += "class " + e.getName() + "\n{\n";
            SourceFileString += "#include \"" + e.getName() + ".h\"\n\n";
            
            if(!PackageProps.isEmpty()){
                IncludeFileString += "namespace {\n";
                for(Property Pack: PackageProps) IncludeFileString += WriteProperty(Pack) + ";\n";
                IncludeFileString += "}\n";
            }
            
            Boolean hitConstructor = false;
            Boolean hitDestructor = false;
            IncludeFileString += "protected:\n";
            for(Property Prot: ProtectedProps){
                IncludeFileString += WriteProperty(Prot) + ";\n";
                
                if(Prot.getClass() == ClassElement.Operation.class){
                    if(Prot.name.startsWith(e.getName())) hitConstructor = true;
                    if(Prot.name.startsWith("~" + e.getName())) hitDestructor = true;
                    SourceFileString += Prot.type + " ";
                    if(Prot.scope == ScopeType.Classifier)
                        SourceFileString += "static ";
                    SourceFileString += e.getName() + "::" + Prot.name + WriteParameters(Prot) + "\n{\n\t";
                    if(!(Prot.type.startsWith("void") || Prot.type.isEmpty())){
                        SourceFileString += Prot.type + " Result;\n\n return Result;";
                    }
                    SourceFileString += "\n}\n\n";
                }
            }
            IncludeFileString += "private:\n";
            for(Property Priv: PrivateProps){
                IncludeFileString += WriteProperty(Priv) + ";\n";
                
                if(Priv.getClass() == ClassElement.Operation.class){
                    if(Priv.name.startsWith(e.getName())) hitConstructor = true;
                    if(Priv.name.startsWith("~" + e.getName())) hitDestructor = true;
                    SourceFileString += Priv.type + " ";
                    if(Priv.scope == ScopeType.Classifier)
                        SourceFileString += "static ";
                    SourceFileString += e.getName() + "::" + Priv.name + WriteParameters(Priv) + "\n{\n\t";
                    if(!(Priv.type.startsWith("void") || Priv.type.isEmpty())){
                        SourceFileString += Priv.type + " Result;\n\n return Result;";
                    }
                    SourceFileString += "\n}\n\n";
                }
            }
            
            IncludeFileString += "public:\n";
            for(Property Pub: PublicProps){
                IncludeFileString += WriteProperty(Pub) + ";\n";
                
                if(Pub.getClass() == ClassElement.Operation.class){
                    if(Pub.name.startsWith(e.getName())) hitConstructor = true;
                    if(Pub.name.startsWith("~" + e.getName())) hitDestructor = true;
                    SourceFileString += Pub.type + " ";
                    if(Pub.scope == ScopeType.Classifier)
                        SourceFileString += "static ";
                    SourceFileString += e.getName() + "::" + Pub.name + WriteParameters(Pub) + "\n{\n\t";
                    if(!(Pub.type.startsWith("void") || Pub.type.isEmpty())){
                        SourceFileString += Pub.type + " Result;\n\n return Result;";
                    }
                    SourceFileString += "\n}\n\n";
                }
            }
            
            if(!hitConstructor){
                IncludeFileString += "\t" + e.getName() + "( );\n";
                SourceFileString += e.getName() + "::" + e.getName() + "( ) { }\n\n";
            }
            if(!hitDestructor){
                IncludeFileString += "\t~" + e.getName() + "( );\n";
                SourceFileString += e.getName() + "::~" + e.getName() + "( ) { }\n\n";
            }
            
            IncludeFileString += "\n}";
            if(NewClassInclude != null){
                NewClassInclude.write(IncludeFileString);
                NewClassInclude.close();
            }
            if(NewClassSrc != null){
                NewClassSrc.write(SourceFileString);
                NewClassSrc.close();
            }
        }
    }
    
    @Override
    public void visit(DependencyRelationship e) {
        System.out.print("Unable to determine dependency\n");
    }
    
    @Override
    public void visit(AggregationRelationship e) {
        if(Include.exists()){
            List<String> myLines = null;
            try {
                myLines = Files.readAllLines(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(myLines != null){
                for(Iterator<String> it = myLines.iterator(); it.hasNext();) {
                    String Line = it.next();
                
                    if(Line.contains("private:")){
                        myLines.add(myLines.lastIndexOf(Line) + 1, "\t" + ((ClassElement)e.getDest()).getName() + " *my" + ((ClassElement)e.getDest()).getName() + ";");
                        break;
                    }
                }
            
                myLines.add(0, "#include \"" + ((ClassElement)e.getDest()).getName() + ".h\"");
                File myFile = new File(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h");
                if(myFile.exists()) myFile.delete();
                try {
                    myFile.createNewFile();
                    Files.write(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), myLines, Charset.defaultCharset());
                } catch (IOException ex) {
                    Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    public void visit(AssociationRelationship e) {
        if(Include.exists()){
            List<String> myLines = null;
            try {
                myLines = Files.readAllLines(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if( myLines != null){
                for(Iterator<String> it = myLines.iterator(); it.hasNext();) {
                    String Line = it.next();
                
                    if(Line.contains("public:")){
                        myLines.add(myLines.lastIndexOf(Line) + 1, "\tfriend class " + ((ClassElement)e.getDest()).getName() + ";");
                        break;
                    }
                }
            
                myLines.add(0, "#include \"" + ((ClassElement)e.getDest()).getName() + ".h\"");
                File myFile = new File(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h");
                if(myFile.exists()) myFile.delete();
                try {
                    myFile.createNewFile();
                    Files.write(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), myLines, Charset.defaultCharset());
                } catch (IOException ex) {
                    Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    public void visit(CompositionRelationship e) {
        if(Include.exists()){
            List<String> myLines = null;
            try {
                myLines = Files.readAllLines(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(myLines != null){
                for(Iterator<String> it = myLines.iterator(); it.hasNext();) {
                    String Line = it.next();
                
                    if(Line.contains("private:")){
                        myLines.add(myLines.lastIndexOf(Line) + 1, "\t" + ((ClassElement)e.getDest()).getName() + " my" + ((ClassElement)e.getDest()).getName() + ";");
                        break;
                    }
                }
                myLines.add(0, "#include \"" + ((ClassElement)e.getDest()).getName() + ".h\"");
                File myFile = new File(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h");
                if(myFile.exists()) myFile.delete();
                try {
                    myFile.createNewFile();
                    Files.write(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), myLines, Charset.defaultCharset());
                } catch (IOException ex) {
                    Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    public void visit(InheritanceRelationship e) {
        if(Include.exists()){
            List<String> myLines = null;
            try {
                myLines = Files.readAllLines(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), Charset.defaultCharset());
            } catch (IOException ex) {
                Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(myLines != null) {
                for(Iterator<String> it = myLines.iterator(); it.hasNext();) {
                    String Line = it.next();
                
                    if(Line.contains("class ")){
                        myLines.set(myLines.lastIndexOf(Line), "class " + ((ClassElement)e.getSource()).getName() + ": public " + ((ClassElement)e.getDest()).getName());
                        break;
                    }
                }
                    myLines.add(0, "#include \"" + ((ClassElement)e.getDest()).getName() + ".h\"");
                    File myFile = new File(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h");
                    if(myFile.exists()) myFile.delete();
                try {
                    myFile.createNewFile();
                    Files.write(Paths.get(Include.getAbsolutePath() + "/" + ((ClassElement)e.getSource()).getName() + ".h"), myLines, Charset.defaultCharset());
                } catch (IOException ex) {
                    Logger.getLogger(JavaCodeElementVisitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

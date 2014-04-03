/*
 * Copyright (C) 2014 alex
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

import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author alex
 */
public class ClassDiagramEditorApp extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public ClassDiagramEditorApp() {
        initComponents();
        deleteMemory();
        
        // At program startup, these menu items are invalid
        menuItemCloseProject.setEnabled(false);
        menuItemSaveProject.setEnabled(false);
        menuItemDeleteProject.setEnabled(false);
        menuItemAddClass.setEnabled(false);
        menuItemAddRelationship.setEnabled(false);
        menuItemAddDiagram.setEnabled(false);
        menuItemDeleteSelection.setEnabled(false);
        menuItemUndo.setEnabled(false);
        menuItemRedo.setEnabled(false);
        staleProject = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSplitPane2 = new javax.swing.JSplitPane();
        classPropertiesForm = new classdiagrameditor.ClassPropertiesForm();
        jPanel1 = new javax.swing.JPanel();
        diagramTabPane = new javax.swing.JTabbedPane();
        ClassDiagramToolBar = new javax.swing.JToolBar();
        ClassButton = new javax.swing.JButton();
        RelationButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
        ReDoButton = new javax.swing.JButton();
        JavaRadio = new javax.swing.JRadioButton();
        CppRadio = new javax.swing.JRadioButton();
        GenerateButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuItemNewProject = new javax.swing.JMenuItem();
        menuItemOpenProject = new javax.swing.JMenuItem();
        menuItemSaveProject = new javax.swing.JMenuItem();
        menuItemCloseProject = new javax.swing.JMenuItem();
        menuItemExit = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        menuItemAddDiagram = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuItemAddClass = new javax.swing.JMenuItem();
        menuItemAddRelationship = new javax.swing.JMenuItem();
        menuItemAddPackage = new javax.swing.JMenuItem();
        editMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItemCut = new javax.swing.JMenuItem();
        menuItemCopy = new javax.swing.JMenuItem();
        menuItemPaste = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        menuItemUndo = new javax.swing.JMenuItem();
        menuItemRedo = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuItemDeleteSelection = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItemDeleteProject = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("mainFrame"); // NOI18N

        jSplitPane2.setContinuousLayout(true);
        jSplitPane2.setLeftComponent(classPropertiesForm);

        diagramTabPane.setPreferredSize(new java.awt.Dimension(640, 480));

        ClassDiagramToolBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ClassDiagramToolBar.setRollover(true);
        ClassDiagramToolBar.setMaximumSize(new java.awt.Dimension(32767, 32767));
        ClassDiagramToolBar.setMinimumSize(new java.awt.Dimension(5, 5));
        ClassDiagramToolBar.setName("ClassDiagramToolBar"); // NOI18N
        ClassDiagramToolBar.setNextFocusableComponent(diagramTabPane);
        ClassDiagramToolBar.setPreferredSize(new java.awt.Dimension(640, 25));

        ClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/class.png"))); // NOI18N
        ClassButton.setToolTipText("Add a Class");
        buttonGroup1.add(ClassButton);
        ClassButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ClassButton.setMargin(new java.awt.Insets(0, 14, 0, 14));
        ClassButton.setMaximumSize(new java.awt.Dimension(69, 35));
        ClassButton.setMinimumSize(new java.awt.Dimension(69, 35));
        ClassButton.setName("AddClassButton"); // NOI18N
        ClassButton.setPreferredSize(new java.awt.Dimension(69, 35));
        ClassButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddClassActionPerformed(evt);
            }
        });
        ClassDiagramToolBar.add(ClassButton);

        RelationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/relation.png"))); // NOI18N
        RelationButton.setToolTipText("Add a Relationship");
        buttonGroup1.add(RelationButton);
        RelationButton.setFocusable(false);
        RelationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RelationButton.setMargin(new java.awt.Insets(0, 14, 0, 14));
        RelationButton.setMaximumSize(new java.awt.Dimension(85, 35));
        RelationButton.setMinimumSize(new java.awt.Dimension(85, 35));
        RelationButton.setName("AddRelationshipButton"); // NOI18N
        RelationButton.setPreferredSize(new java.awt.Dimension(85, 35));
        RelationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RelationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddRelationshipActionPerformed(evt);
            }
        });
        ClassDiagramToolBar.add(RelationButton);

        UndoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/undo.png"))); // NOI18N
        UndoButton.setToolTipText("Undo");
        buttonGroup1.add(UndoButton);
        UndoButton.setFocusable(false);
        UndoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        UndoButton.setMargin(new java.awt.Insets(0, 14, 0, 14));
        UndoButton.setMaximumSize(new java.awt.Dimension(35, 35));
        UndoButton.setMinimumSize(new java.awt.Dimension(35, 35));
        UndoButton.setPreferredSize(new java.awt.Dimension(35, 35));
        UndoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        UndoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemUndoActionPerformed(evt);
            }
        });
        ClassDiagramToolBar.add(UndoButton);

        ReDoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/redo.png"))); // NOI18N
        buttonGroup1.add(ReDoButton);
        ReDoButton.setFocusable(false);
        ReDoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ReDoButton.setMargin(new java.awt.Insets(0, 14, 0, 14));
        ReDoButton.setMaximumSize(new java.awt.Dimension(35, 35));
        ReDoButton.setMinimumSize(new java.awt.Dimension(35, 35));
        ReDoButton.setPreferredSize(new java.awt.Dimension(35, 35));
        ReDoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ReDoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRedoActionPerformed(evt);
            }
        });
        ClassDiagramToolBar.add(ReDoButton);

        buttonGroup2.add(JavaRadio);
        JavaRadio.setSelected(true);
        JavaRadio.setText("Java");
        JavaRadio.setToolTipText("Select for code generation in Java");
        JavaRadio.setFocusable(false);
        JavaRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JavaRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JavaRadio.setMargin(new java.awt.Insets(5, 2, 5, 2));
        JavaRadio.setMaximumSize(new java.awt.Dimension(35, 35));
        JavaRadio.setMinimumSize(new java.awt.Dimension(35, 35));
        JavaRadio.setPreferredSize(new java.awt.Dimension(35, 35));
        JavaRadio.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        ClassDiagramToolBar.add(JavaRadio);

        buttonGroup2.add(CppRadio);
        CppRadio.setText("C++");
        CppRadio.setToolTipText("Select for code generation in C++");
        CppRadio.setFocusable(false);
        CppRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CppRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CppRadio.setMargin(new java.awt.Insets(5, 2, 5, 2));
        CppRadio.setMaximumSize(new java.awt.Dimension(35, 35));
        CppRadio.setMinimumSize(new java.awt.Dimension(35, 35));
        CppRadio.setPreferredSize(new java.awt.Dimension(35, 35));
        CppRadio.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        ClassDiagramToolBar.add(CppRadio);

        GenerateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/generate.png"))); // NOI18N
        GenerateButton.setToolTipText("Generate Code");
        buttonGroup1.add(GenerateButton);
        GenerateButton.setFocusable(false);
        GenerateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GenerateButton.setMaximumSize(new java.awt.Dimension(35, 35));
        GenerateButton.setMinimumSize(new java.awt.Dimension(35, 35));
        GenerateButton.setPreferredSize(new java.awt.Dimension(35, 35));
        GenerateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ClassDiagramToolBar.add(GenerateButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ClassDiagramToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(diagramTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(ClassDiagramToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diagramTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane2.setRightComponent(jPanel1);

        fileMenu.setText("File");

        menuItemNewProject.setText("New Project...");
        menuItemNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNewProjectActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemNewProject);

        menuItemOpenProject.setText("Open Project...");
        menuItemOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpenProjectActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemOpenProject);

        menuItemSaveProject.setText("Save Project...");
        menuItemSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveProjectActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemSaveProject);

        menuItemCloseProject.setText("Close Project...");
        menuItemCloseProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCloseProjectActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemCloseProject);

        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemExit);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");

        menuItemAddDiagram.setText("Add Diagram...");
        menuItemAddDiagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddDiagramActionPerformed(evt);
            }
        });
        editMenu.add(menuItemAddDiagram);
        editMenu.add(jSeparator2);

        menuItemAddClass.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        menuItemAddClass.setText("Add Class...");
        menuItemAddClass.setToolTipText("");
        menuItemAddClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddClassActionPerformed(evt);
            }
        });
        editMenu.add(menuItemAddClass);

        menuItemAddRelationship.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        menuItemAddRelationship.setText("Add Relationship...");
        menuItemAddRelationship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddRelationshipActionPerformed(evt);
            }
        });
        editMenu.add(menuItemAddRelationship);

        menuItemAddPackage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        menuItemAddPackage.setText("Add Package...");
        menuItemAddPackage.setEnabled(false);
        menuItemAddPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddPackageActionPerformed(evt);
            }
        });
        editMenu.add(menuItemAddPackage);
        editMenu.add(editMenuSeparator1);

        menuItemCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menuItemCut.setText("Cut");
        menuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCutActionPerformed(evt);
            }
        });
        editMenu.add(menuItemCut);

        menuItemCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        menuItemCopy.setText("Copy");
        menuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCopyActionPerformed(evt);
            }
        });
        editMenu.add(menuItemCopy);

        menuItemPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        menuItemPaste.setText("Paste");
        menuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPasteActionPerformed(evt);
            }
        });
        editMenu.add(menuItemPaste);
        editMenu.add(jSeparator4);

        menuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        menuItemUndo.setText("Undo");
        menuItemUndo.setToolTipText("");
        menuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemUndoActionPerformed(evt);
            }
        });
        editMenu.add(menuItemUndo);

        menuItemRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuItemRedo.setText("Redo");
        menuItemRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRedoActionPerformed(evt);
            }
        });
        editMenu.add(menuItemRedo);
        editMenu.add(jSeparator3);

        menuItemDeleteSelection.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        menuItemDeleteSelection.setText("Delete Selection...");
        menuItemDeleteSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteSelectionActionPerformed(evt);
            }
        });
        editMenu.add(menuItemDeleteSelection);
        editMenu.add(jSeparator1);

        menuItemDeleteProject.setText("Delete Project...");
        menuItemDeleteProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteProjectActionPerformed(evt);
            }
        });
        editMenu.add(menuItemDeleteProject);

        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 765, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    private void menuItemAddClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddClassActionPerformed
        getEditor().addClass();
        menuItemDeleteSelection.setEnabled(true);
        staleProject = true;
    }//GEN-LAST:event_menuItemAddClassActionPerformed

    private void menuItemAddRelationshipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddRelationshipActionPerformed
        getEditor().addRelationship();
        menuItemDeleteSelection.setEnabled(true);
        staleProject = true;
    }//GEN-LAST:event_menuItemAddRelationshipActionPerformed

    private void menuItemNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewProjectActionPerformed
        if(diagramTabPane.getTabCount() == 0)
        {
            addTab();
            
            // there is now a diagram tab, so enable the appropriate menu options
            menuItemsEnabled(true);
        }
        else
        {
            if(staleProject == true)
            {
                int button = JOptionPane.YES_NO_OPTION;
                int response = JOptionPane.showConfirmDialog(this, "Would you like to save any changes to the current project first?", "Warning", button);

                if(response == JOptionPane.YES_OPTION)
                {
                    menuItemSaveProject();
                }                       
            }
            deleteMemory();
            addTab();
        }
        // for new projects clear out projectFile, reset to stale to it's always prompted for saving
        mProjectFile = null;
        staleProject = true;
        setTitle("Project: <untitled>");
    }//GEN-LAST:event_menuItemNewProjectActionPerformed

    private void addTab() {
        final String title = "Diagram" + (diagramTabPane.getTabCount()+1);
        EditorPanel tabDiagram = new classdiagrameditor.EditorPanel();

        tabDiagram.getManager().registerSelectionObserver(classPropertiesForm);

        addCloseButtonToTab(new JScrollPane(tabDiagram), title);
    }

    // Get the currently selected editor
    public EditorPanel getEditor() {
        return (EditorPanel)((JScrollPane)diagramTabPane.getSelectedComponent()).getViewport().getView();
    }

    private void forceDiagramRedraw() {
        getEditor().repaint(getEditor().getBounds());
    }
    
    private void addCloseButtonToTab(JScrollPane scrollPane, String title)
    {
        diagramTabPane.addTab(title, scrollPane);
        int index = diagramTabPane.indexOfComponent(scrollPane);
        diagramTabPane.setTabComponentAt(index, new ButtonTabComponent(diagramTabPane));
        diagramTabPane.setSelectedIndex(index);
    }

    private void menuItemOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenProjectActionPerformed

        if (diagramTabPane.getTabCount() > 0 && staleProject == true)
        {
            int button = JOptionPane.YES_NO_OPTION;
            int response = JOptionPane.showConfirmDialog(this, "Would you like to save any changes to the current project first?", "Warning", button);

            if(response == JOptionPane.YES_OPTION)
            {
                menuItemSaveProject();
                deleteMemory();
                JOptionPane.showMessageDialog(this, "Project saved.", "Success!", PLAIN_MESSAGE);
            }         
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("XML file", new String []{"xml"});
        chooser.setFileFilter(filter);
        Component parent = null;
        String userPath = "user.home";
        chooser.setCurrentDirectory(new File(System.getProperty(userPath)));
        
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("Opening project file: " + chooser.getSelectedFile().getAbsolutePath());
            mProjectFile = chooser.getSelectedFile();
            setTitle("Project: " +mProjectFile.getName());
            
            deleteMemory();
            if(diagramTabPane.getTabCount() == 0)
            {
                // there is now a diagram tab, so enable the appropriate menu options
                menuItemsEnabled(true);
                
                XMLInputFactory factory = XMLInputFactory.newInstance();

                try {
                    XMLStreamReader reader = factory.createXMLStreamReader(
                            new FileReader(mProjectFile));
                    
                    // Read Project Start
                    reader.next();

                    // Parse the XML
                    while(reader.hasNext()) {
                        reader.next(); // Read element type Beginning

                        // Create element at runtime
                        String myDiagram = reader.getLocalName();

                        if (!myDiagram.equals("Project")) {
                            EditorPanel tabDiagram = new classdiagrameditor.EditorPanel();
                            addCloseButtonToTab(new JScrollPane(tabDiagram), myDiagram);

                            JScrollPane jsp = (JScrollPane) diagramTabPane.getComponentAt(diagramTabPane.getSelectedIndex());
                            EditorPanel ep = (EditorPanel)jsp.getViewport().getView();

                            // Read ElementCount
                            reader.next(); // ElementCount Beginning
                            int count = Integer.parseInt(reader.getAttributeValue(0));
                            reader.next(); // ElementCount End

                            ep.openFile(reader, count);

                            reader.next(); // Read element type End
                        }
                    }
                    // Read Project End
                    reader.next();

                    // Close the reader
                    reader.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            staleProject = false;
        }
    }//GEN-LAST:event_menuItemOpenProjectActionPerformed

    private void menuItemSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveProjectActionPerformed
        // TODO add your handling code here:
        menuItemSaveProject();
    }//GEN-LAST:event_menuItemSaveProjectActionPerformed

    private void menuItemSaveProject()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("XML file", new String []{"xml"});
        chooser.setFileFilter(filter);
        Component parent = null;
        String userPath = "user.home";
        chooser.setCurrentDirectory(new File(System.getProperty(userPath)));
        chooser.setSelectedFile(mProjectFile);

        int retrival = chooser.showSaveDialog(parent);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                mProjectFile = chooser.getSelectedFile();
                
                // Clean out previous file
                XMLOutputFactory factory = XMLOutputFactory.newInstance();

                try {
                    // Delete previous contents
                    XMLStreamWriter writer = factory.createXMLStreamWriter(
                        new FileWriter(mProjectFile));
                    writer.flush();
                    writer.close();
                    
                    writer = factory.createXMLStreamWriter(
                        new FileWriter(mProjectFile, true));

                    writer.writeStartDocument();
                    writer.writeStartElement("Project");
                    
                    for (int index = 0; index < diagramTabPane.getComponentCount() - 1; index++) {
                        writer.writeStartElement(diagramTabPane.getTitleAt(index));                                             
                        
                        JScrollPane jsp = (JScrollPane) diagramTabPane.getComponentAt(index);
                        EditorPanel ep = (EditorPanel)jsp.getViewport().getView();
                        
                        // Write Element Count
                        writer.writeStartElement("Elements");
                        writer.writeAttribute("Count", Integer.toString(ep.elementCount()));
                        writer.writeEndElement();
                        
                        // Write Elements
                        ep.saveFile(writer);
                        
                        writer.writeEndElement();
                    }
                    
                    writer.writeEndElement();
                    writer.writeEndDocument();
                    writer.flush();
                    writer.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                setTitle("Project: " +mProjectFile.getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("projectPanel saveProject() called");       
    }
    
    private void menuItemCloseProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCloseProjectActionPerformed
        menuItemCloseProject(staleProject);
    }//GEN-LAST:event_menuItemCloseProjectActionPerformed

    private void menuItemCloseProject(boolean stale)
    {        
        if(stale == true)
        {
            int button = JOptionPane.YES_NO_OPTION;
            int response = JOptionPane.showConfirmDialog(this, "Would you like to save any changes to the current project first?", "Warning", button);

            if(response == JOptionPane.YES_OPTION)
            {
                menuItemSaveProject();
            }         
        }
        
        deleteMemory();
        
        // project closed, these menu items are now invalid
        menuItemsEnabled(false);
        
        setTitle("");
    }
    
    private void menuItemDeleteProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteProjectActionPerformed

        int button = JOptionPane.YES_NO_OPTION;
        String question = "Are you sure you want to delete project: " + mProjectFile.getName() + "?";
        int response = JOptionPane.showConfirmDialog(this, question, "Warning", button);

        if(response == JOptionPane.YES_OPTION)
        {
            deleteMemory();
            boolean success = mProjectFile.getAbsoluteFile().delete();
            if(success)
                JOptionPane.showMessageDialog(this, "Project deleted.", "Success!", PLAIN_MESSAGE);
            
            setTitle("");
        }
        
        // these menu items are now invalid
        menuItemsEnabled(false);
    }//GEN-LAST:event_menuItemDeleteProjectActionPerformed

    private void menuItemAddDiagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddDiagramActionPerformed
        addTab();
    }//GEN-LAST:event_menuItemAddDiagramActionPerformed

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed

        menuItemCloseProject(staleProject);
        System.exit(0);
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemAddPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddPackageActionPerformed
        getEditor().addPackage();
        staleProject = true;
    }//GEN-LAST:event_menuItemAddPackageActionPerformed

    private void menuItemDeleteSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteSelectionActionPerformed
        getEditor().deleteSelection();
    }//GEN-LAST:event_menuItemDeleteSelectionActionPerformed

    private void menuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCopyActionPerformed
        getEditor().getManager().copy();
    }//GEN-LAST:event_menuItemCopyActionPerformed

    private void menuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCutActionPerformed
        getEditor().getManager().cut();
    }//GEN-LAST:event_menuItemCutActionPerformed

    private void menuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemPasteActionPerformed
        getEditor().getManager().paste();
    }//GEN-LAST:event_menuItemPasteActionPerformed

    private void menuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {
        getEditor().undoLastAction();
    }

    private void menuItemRedoActionPerformed(java.awt.event.ActionEvent evt) {
        getEditor().redoLastAction();
    }
    
    private void deleteMemory() {
        while (diagramTabPane.getComponentCount() > 1) {
            getEditor().deleteDiagram();
            diagramTabPane.remove(diagramTabPane.getSelectedComponent());
        }
        diagramTabPane.removeAll();
    }
    
    private void menuItemsEnabled(boolean enabled) {
        menuItemCloseProject.setEnabled(enabled);
        menuItemSaveProject.setEnabled(enabled);
        menuItemDeleteProject.setEnabled(enabled);
        menuItemAddClass.setEnabled(enabled);
        menuItemAddRelationship.setEnabled(enabled);
        menuItemAddPackage.setEnabled(enabled);
        menuItemAddDiagram.setEnabled(enabled);
        menuItemUndo.setEnabled(enabled);
        menuItemRedo.setEnabled(enabled);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClassDiagramEditorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClassDiagramEditorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClassDiagramEditorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClassDiagramEditorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClassDiagramEditorApp().setVisible(true);
            }
        });
    }
    
    private File mProjectFile;
    private boolean staleProject;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClassButton;
    private javax.swing.JToolBar ClassDiagramToolBar;
    private javax.swing.JRadioButton CppRadio;
    private javax.swing.JButton GenerateButton;
    private javax.swing.JRadioButton JavaRadio;
    private javax.swing.JButton ReDoButton;
    private javax.swing.JButton RelationButton;
    private javax.swing.JButton UndoButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private classdiagrameditor.ClassPropertiesForm classPropertiesForm;
    private javax.swing.JTabbedPane diagramTabPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPopupMenu.Separator editMenuSeparator1;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JMenuItem menuItemAddClass;
    private javax.swing.JMenuItem menuItemAddDiagram;
    private javax.swing.JMenuItem menuItemAddPackage;
    private javax.swing.JMenuItem menuItemAddRelationship;
    private javax.swing.JMenuItem menuItemCloseProject;
    private javax.swing.JMenuItem menuItemCopy;
    private javax.swing.JMenuItem menuItemCut;
    private javax.swing.JMenuItem menuItemDeleteProject;
    private javax.swing.JMenuItem menuItemDeleteSelection;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemNewProject;
    private javax.swing.JMenuItem menuItemOpenProject;
    private javax.swing.JMenuItem menuItemPaste;
    private javax.swing.JMenuItem menuItemRedo;
    private javax.swing.JMenuItem menuItemSaveProject;
    private javax.swing.JMenuItem menuItemUndo;
    // End of variables declaration//GEN-END:variables
}

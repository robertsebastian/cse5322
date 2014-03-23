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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
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
        jTabbedPane2.removeAll();
        
        // At program startup, these menu items are invalid
        menuItemCloseProject.setEnabled(false);
        menuItemSaveProject.setEnabled(false);
        menuItemDeleteProject.setEnabled(false);
        menuItemAddClass.setEnabled(false);
        menuItemAddRelationship.setEnabled(false);
        menuItemAddDiagram.setEnabled(false);
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

        jSplitPane2 = new javax.swing.JSplitPane();
        classPropertiesForm2 = new classdiagrameditor.ClassPropertiesForm();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        editorPanel = new classdiagrameditor.EditorPanel();
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
        menuItemUndo = new javax.swing.JMenuItem();
        menuItemRedo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItemDeleteProject = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane2.setContinuousLayout(true);
        jSplitPane2.setLeftComponent(classPropertiesForm2);

        javax.swing.GroupLayout editorPanelLayout = new javax.swing.GroupLayout(editorPanel);
        editorPanel.setLayout(editorPanelLayout);
        editorPanelLayout.setHorizontalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 812, Short.MAX_VALUE)
        );
        editorPanelLayout.setVerticalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 717, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab1", editorPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
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
        menuItemAddPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddPackageActionPerformed(evt);
            }
        });
        editMenu.add(menuItemAddPackage);
        editMenu.add(editMenuSeparator1);

        menuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        menuItemUndo.setText("Undo");
        menuItemUndo.setToolTipText("");
        menuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemUndoActionPerformed(evt);
            }
        });
        editMenu.add(menuItemUndo);
        menuItemUndo.getAccessibleContext().setAccessibleParent(editorPanel);

        menuItemRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuItemRedo.setText("Redo");
        menuItemRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRedoActionPerformed(evt);
            }
        });
        editMenu.add(menuItemRedo);
        menuItemRedo.getAccessibleContext().setAccessibleParent(editorPanel);

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
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemAddClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddClassActionPerformed
        editorPanel.addClass();
        staleProject = true;
        // this needs to be set up so that it only
        // happens after user has placed the class
        // but its ok for now...
        menuItemUndo.setEnabled(true);
        menuItemRedo.setEnabled(true);
    }//GEN-LAST:event_menuItemAddClassActionPerformed

    private void menuItemAddRelationshipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddRelationshipActionPerformed
        editorPanel.addRelationship();
        staleProject = true;
    }//GEN-LAST:event_menuItemAddRelationshipActionPerformed

    private void menuItemNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewProjectActionPerformed
        if(jTabbedPane2.getTabCount() == 0)
        {
            addTab();
            
            // there is now a diagram tab, so enable the appropriate menu options
            menuItemCloseProject.setEnabled(true);
            menuItemSaveProject.setEnabled(true);
            menuItemDeleteProject.setEnabled(true);
            menuItemAddClass.setEnabled(true);
            menuItemAddRelationship.setEnabled(true);
            menuItemAddDiagram.setEnabled(true);
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
            jTabbedPane2.removeAll();
            addTab();
        }
        // for new projects clear out projectFile, reset to stale to it's always prompted for saving
        mProjectFile = null;
        staleProject = true;
        setTitle("Project: <untitled>");
    }//GEN-LAST:event_menuItemNewProjectActionPerformed

    private void addTab() {
        final String title = "Diagram " + (jTabbedPane2.getTabCount()+1);
        editorPanel = new classdiagrameditor.EditorPanel();
        final JScrollPane scrollPane = new JScrollPane(editorPanel);
        //final EditorPanel newPanel = new classdiagrameditor.EditorPanel();
        
        if (jTabbedPane2.getTabCount() == 0)
        {
            jTabbedPane2.addTab(title, scrollPane);
        }
        else
        {
            addCloseButtonToTab(scrollPane, title);
        }
        
        /*int pos = jTabbedPane2.indexOfComponent(scrollPane);
        FlowLayout f = new FlowLayout(FlowLayout.RIGHT, 1, 0);
        JPanel pnlTab = new JPanel(f);
        pnlTab.setOpaque(false);
        JButton btnClose = new JButton();
        btnClose.setOpaque(false);
        btnClose.setFocusable(false);
        pnlTab.add(btnClose);
        jTabbedPane2.setTabComponentAt(pos, pnlTab);
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTabbedPane2.remove(scrollPane);
            }
        };
        btnClose.addActionListener(listener);
        jTabbedPane2.setSelectedComponent(scrollPane);*/
    }
    
    private void addCloseButtonToTab(JScrollPane scrollPane, String title)
    {
        jTabbedPane2.addTab(title, scrollPane);
        int index = jTabbedPane2.indexOfComponent(scrollPane);
        ButtonTabComponent tabButton = new ButtonTabComponent(jTabbedPane2);
        jTabbedPane2.setTabComponentAt(index, tabButton);
    }
    
    
    private void menuItemOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenProjectActionPerformed

        if (jTabbedPane2.getTabCount() > 0 && staleProject == true)
        {
            int button = JOptionPane.YES_NO_OPTION;
            int response = JOptionPane.showConfirmDialog(this, "Would you like to save any changes to the current project first?", "Warning", button);

            if(response == JOptionPane.YES_OPTION)
            {
                menuItemSaveProject();
                jTabbedPane2.removeAll();
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
            
            editorPanel.openFile(mProjectFile);
            
            // just faking it for now...            
            jTabbedPane2.removeAll();
            if(jTabbedPane2.getTabCount() == 0)
            {
                addTab();
                
                // there is now a diagram tab, so enable the appropriate menu options
                menuItemCloseProject.setEnabled(true);
                menuItemSaveProject.setEnabled(true);
                menuItemDeleteProject.setEnabled(true);
                menuItemAddClass.setEnabled(true);
                menuItemAddRelationship.setEnabled(true);
                menuItemAddDiagram.setEnabled(true);
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
                    editorPanel.saveFile(writer);
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
        
        jTabbedPane2.removeAll();
        
        // project closed, these menu items are now invalid
        menuItemAddClass.setEnabled(false);
        menuItemAddRelationship.setEnabled(false);
        menuItemAddDiagram.setEnabled(false);     
        
        setTitle("");
    }
    
    private void menuItemDeleteProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteProjectActionPerformed

        int button = JOptionPane.YES_NO_OPTION;
        String question = "Are you sure you want to delete project: " + mProjectFile.getName() + "?";
        int response = JOptionPane.showConfirmDialog(this, question, "Warning", button);

        if(response == JOptionPane.YES_OPTION)
        {
            jTabbedPane2.removeAll();
            boolean success = mProjectFile.getAbsoluteFile().delete();
            if(success)
                JOptionPane.showMessageDialog(this, "Project deleted.", "Success!", PLAIN_MESSAGE);

            
            // these menu items are now invalid
            menuItemCloseProject.setEnabled(false);
            menuItemSaveProject.setEnabled(false);
            menuItemDeleteProject.setEnabled(false);
            menuItemAddClass.setEnabled(false);
            menuItemAddRelationship.setEnabled(false);
            menuItemAddDiagram.setEnabled(false);
            menuItemUndo.setEnabled(false);
            menuItemRedo.setEnabled(false);
            
            setTitle("");
        }         
    }//GEN-LAST:event_menuItemDeleteProjectActionPerformed

    private void menuItemAddDiagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddDiagramActionPerformed
        if(jTabbedPane2.getTabCount() > 0)
        {
           addTab();
        }
    }//GEN-LAST:event_menuItemAddDiagramActionPerformed

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed

        menuItemCloseProject(staleProject);
        System.exit(0);
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemAddPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddPackageActionPerformed
        editorPanel.addPackage();
        staleProject = true;
    }//GEN-LAST:event_menuItemAddPackageActionPerformed

    private void menuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {
        editorPanel.undoLastAction();
    }

    private void menuItemRedoActionPerformed(java.awt.event.ActionEvent evt) {
        editorPanel.redoLastAction();
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
    private classdiagrameditor.ClassPropertiesForm classPropertiesForm2;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPopupMenu.Separator editMenuSeparator1;
    private classdiagrameditor.EditorPanel editorPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JMenuItem menuItemAddClass;
    private javax.swing.JMenuItem menuItemAddDiagram;
    private javax.swing.JMenuItem menuItemAddPackage;
    private javax.swing.JMenuItem menuItemAddRelationship;
    private javax.swing.JMenuItem menuItemCloseProject;
    private javax.swing.JMenuItem menuItemDeleteProject;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemNewProject;
    private javax.swing.JMenuItem menuItemOpenProject;
    private javax.swing.JMenuItem menuItemRedo;
    private javax.swing.JMenuItem menuItemSaveProject;
    private javax.swing.JMenuItem menuItemUndo;
    // End of variables declaration//GEN-END:variables
}

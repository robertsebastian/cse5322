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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Set;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

/**
 *
 * @author alex
 */
public class ClassPropertiesForm extends javax.swing.JPanel
    implements SelectionObserver, DocumentListener
{
    private ClassElement element_;
    private DiagramManager diagram_;
    private DefaultMutableTreeNode popupNode_ = null;

    private final DefaultMutableTreeNode root_ = new DefaultMutableTreeNode("Properties");
    private final DefaultMutableTreeNode attributes_ = new DefaultMutableTreeNode("Attributes");
    private final DefaultMutableTreeNode operations_ = new DefaultMutableTreeNode("Operations");
    private final DefaultTreeModel       treeModel_ = new DefaultTreeModel(root_);


    /**
     * Creates new form ClassPropertiesForm
     */
    public ClassPropertiesForm() {
        initComponents();
        setVisible(false);

        root_.add(attributes_);
        root_.add(operations_);

        propertiesTree.addMouseListener(new ClassPropertyPopupMenuAdapter());
        propertiesTree.getCellEditor().addCellEditorListener(new CellEditorListener() {

            public void editingStopped(ChangeEvent e) {
                if (diagram_ != null) diagram_.notifyElementModified();
            }

            public void editingCanceled(ChangeEvent e) {
                if (diagram_ != null) diagram_.notifyElementModified();
            }
        });
    }

    private class ClassPropertyPopupMenuAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) handlePopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) handlePopup(e);
        }

        // Show appropriate popup menu depending on what node was selected
        private void handlePopup(MouseEvent e) {
            JTree tree = (JTree)e.getComponent();
            if (tree.getRowForLocation(e.getX(), e.getY()) == -1) return;

            popupNode_ = (DefaultMutableTreeNode)tree.getPathForLocation(
                    e.getX(), e.getY()).getLastPathComponent();

            if (popupNode_ == attributes_) {
                    attributeRootMenu.show(tree, e.getX(), e.getY());
            } else if (popupNode_ == operations_) {
                operationRootMenu.show(tree, e.getX(), e.getY());
            } else if (popupNode_.getUserObject() instanceof ClassElement.Parameter) {
                parameterMenu.show(tree, e.getX(), e.getY());
            } else if (popupNode_.getUserObject() instanceof ClassElement.Operation) {
                operationMenu.show(tree, e.getX(), e.getY());
            } else if (popupNode_.getUserObject() instanceof ClassElement.Attribute) {
                attributeMenu.show(tree, e.getX(), e.getY());
            }
        }
    }

    private class ClassPropertyEditor extends AbstractCellEditor implements TreeCellEditor {
        DefaultMutableTreeNode value_;

        // Extract values from editor component and update property
        public Object getCellEditorValue() {
            ClassElement.Property prop = (ClassElement.Property)value_.getUserObject();
            prop.name = itemNameText.getText();
            prop.type = itemTypeText.getText();
            prop.visibility = (ClassElement.VisibilityType)itemVisibilityBox.getSelectedItem();
            prop.scope = (ClassElement.ScopeType)itemScopeBox.getSelectedItem();

            return value_;
        }

        // Cell is editable if it's a property or parameter
        @Override
        public boolean isCellEditable(EventObject e) {
            if(e != null && (e.getSource() instanceof JTree) && (e instanceof MouseEvent)) {
                JTree tree = (JTree)e.getSource();
                MouseEvent m = (MouseEvent)e;
                TreePath path = tree.getPathForLocation(m.getX(), m.getY()); 

                Object n = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
                return n instanceof ClassElement.Property;
            }
            return false;
        }

        // Get the class property editor component to show here
        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            value_ = (DefaultMutableTreeNode)value;

            ClassElement.Property prop = (ClassElement.Property)value_.getUserObject();
            itemNameText.setText(prop.name);
            itemTypeText.setText(prop.type);
            itemVisibilityBox.setSelectedItem(prop.visibility);
            itemScopeBox.setSelectedItem(prop.scope);

            // Parameter doesn't use these fields, so hide them
            itemVisibilityBox.setVisible(!(prop instanceof ClassElement.Parameter));
            itemScopeBox.setVisible(!(prop instanceof ClassElement.Parameter));
            return cellEditor;
        }
    }

    @Override
    public void notifySelectionChanged(DiagramManager diagram, Set<Element> selection) {
        element_ = null;
        diagram_ = diagram;
        if (selection.size() == 1) {
            for (Element e : selection) {
                if (e instanceof ClassElement) {
                    element_ = (ClassElement)e;
        
                    updateTreeNodes();
                    nameText.setText(element_.getName());
                    isAbstractCheckBox.setSelected(element_.getIsAbstract());
                }
            }
        }

        setVisible(element_ != null);
    }

    private void updateTreeNodes() {
        // Clear all attributes and operations
        attributes_.removeAllChildren();
        operations_.removeAllChildren();

        // Nothing else to do if we're not displaying an element
        if (element_ == null) {
            treeModel_.nodeStructureChanged(root_);
            expandTree();
            return;
        }

        // Step through element and add all of its attributes and operations
        for (ClassElement.Attribute attr : element_.getAttributes()) {
            attributes_.add(new DefaultMutableTreeNode(attr));
        }
        for (ClassElement.Operation op : element_.getOperations()) {
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(op);
            operations_.add(n);
            for (ClassElement.Parameter par : op.parameters) {
                n.add(new DefaultMutableTreeNode(par));
            }
        }

        treeModel_.nodeStructureChanged(root_);
        expandTree();

        if (diagram_ != null) diagram_.notifyElementModified();
    }

    private void expandTree() {
        for (int i = 0; i < propertiesTree.getRowCount(); i++) {
            propertiesTree.expandRow(i);
        }
    }

    private void nameTextChanged() {
        if (element_ == null) return;
        
        element_.setName(nameText.getText());
        
        if (diagram_ != null) diagram_.notifyElementModified();
    }
    
    public void insertUpdate(DocumentEvent e) {
        nameTextChanged();
    }

    public void removeUpdate(DocumentEvent e) {
        nameTextChanged();
    }

    public void changedUpdate(DocumentEvent e) {
        nameTextChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cellEditor = new javax.swing.JPanel();
        itemVisibilityBox = new javax.swing.JComboBox();
        itemScopeBox = new javax.swing.JComboBox();
        itemNameText = new javax.swing.JTextField();
        itemTypeText = new javax.swing.JTextField();
        attributeRootMenu = new javax.swing.JPopupMenu();
        addAttributeItem = new javax.swing.JMenuItem();
        operationRootMenu = new javax.swing.JPopupMenu();
        addOperationItem = new javax.swing.JMenuItem();
        attributeMenu = new javax.swing.JPopupMenu();
        deleteAttributeItem = new javax.swing.JMenuItem();
        operationMenu = new javax.swing.JPopupMenu();
        addParameterItem = new javax.swing.JMenuItem();
        deleteOperationItem = new javax.swing.JMenuItem();
        parameterMenu = new javax.swing.JPopupMenu();
        deleteParameterItem = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        propertiesTree = new javax.swing.JTree();
        isAbstractCheckBox = new javax.swing.JCheckBox();

        cellEditor.setOpaque(false);

        itemVisibilityBox.setModel(new DefaultComboBoxModel(ClassElement.VisibilityType.values())
        );
        itemVisibilityBox.setOpaque(false);

        itemScopeBox.setModel(new DefaultComboBoxModel(ClassElement.ScopeType.values()));

        itemNameText.setText("jTextField2");

        itemTypeText.setText("jTextField3");

        javax.swing.GroupLayout cellEditorLayout = new javax.swing.GroupLayout(cellEditor);
        cellEditor.setLayout(cellEditorLayout);
        cellEditorLayout.setHorizontalGroup(
            cellEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemNameText, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
            .addComponent(itemTypeText)
            .addComponent(itemScopeBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(itemVisibilityBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cellEditorLayout.setVerticalGroup(
            cellEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cellEditorLayout.createSequentialGroup()
                .addComponent(itemNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itemTypeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itemScopeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itemVisibilityBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        addAttributeItem.setText("Add Attribute");
        addAttributeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAttributeItemActionPerformed(evt);
            }
        });
        attributeRootMenu.add(addAttributeItem);

        addOperationItem.setText("Add Operation");
        addOperationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOperationItemActionPerformed(evt);
            }
        });
        operationRootMenu.add(addOperationItem);

        deleteAttributeItem.setText("Delete");
        deleteAttributeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAttributeItemActionPerformed(evt);
            }
        });
        attributeMenu.add(deleteAttributeItem);

        addParameterItem.setText("Add Parameter");
        addParameterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParameterItemActionPerformed(evt);
            }
        });
        operationMenu.add(addParameterItem);

        deleteOperationItem.setText("Delete");
        deleteOperationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteOperationItemActionPerformed(evt);
            }
        });
        operationMenu.add(deleteOperationItem);

        deleteParameterItem.setText("Delete");
        deleteParameterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParameterItemActionPerformed(evt);
            }
        });
        parameterMenu.add(deleteParameterItem);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 10, 5));
        setPreferredSize(new java.awt.Dimension(200, 768));

        jLabel1.setText("Name");

        nameText.getDocument().addDocumentListener(this);

        propertiesTree.setModel(treeModel_);
        propertiesTree.setCellEditor(new ClassPropertyEditor());
        propertiesTree.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        propertiesTree.setDragEnabled(true);
        propertiesTree.setDropMode(javax.swing.DropMode.INSERT);
        propertiesTree.setEditable(true);
        propertiesTree.setRootVisible(false);
        propertiesTree.setShowsRootHandles(true);
        jScrollPane3.setViewportView(propertiesTree);

        isAbstractCheckBox.setText("Abstract");
        isAbstractCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isAbstractCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameText))
            .addGroup(layout.createSequentialGroup()
                .addComponent(isAbstractCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isAbstractCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addAttributeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAttributeItemActionPerformed
        element_.getAttributes().add(new ClassElement.Attribute());
        updateTreeNodes();
    }//GEN-LAST:event_addAttributeItemActionPerformed

    private void addOperationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOperationItemActionPerformed
        element_.getOperations().add(new ClassElement.Operation());
        updateTreeNodes();
    }//GEN-LAST:event_addOperationItemActionPerformed

    private void addParameterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addParameterItemActionPerformed
        ((ClassElement.Operation)popupNode_.getUserObject()).parameters.add(
                new ClassElement.Parameter());
        updateTreeNodes();
    }//GEN-LAST:event_addParameterItemActionPerformed

    private void deleteOperationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteOperationItemActionPerformed
        element_.getOperations().remove((ClassElement.Operation)popupNode_.getUserObject());
        updateTreeNodes();
    }//GEN-LAST:event_deleteOperationItemActionPerformed

    private void deleteAttributeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAttributeItemActionPerformed
        element_.getAttributes().remove((ClassElement.Attribute)popupNode_.getUserObject());
        updateTreeNodes();
    }//GEN-LAST:event_deleteAttributeItemActionPerformed

    private void deleteParameterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteParameterItemActionPerformed
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)popupNode_.getParent();
        ClassElement.Operation op = (ClassElement.Operation)parent.getUserObject();
        op.parameters.remove((ClassElement.Parameter)popupNode_.getUserObject());
        updateTreeNodes();
    }//GEN-LAST:event_deleteParameterItemActionPerformed

    private void isAbstractCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isAbstractCheckBoxActionPerformed
        if (element_ == null) return;
        
        element_.setIsAbstract(isAbstractCheckBox.isSelected());
        
        if (diagram_ != null) diagram_.notifyElementModified();
    }//GEN-LAST:event_isAbstractCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addAttributeItem;
    private javax.swing.JMenuItem addOperationItem;
    private javax.swing.JMenuItem addParameterItem;
    private javax.swing.JPopupMenu attributeMenu;
    private javax.swing.JPopupMenu attributeRootMenu;
    private javax.swing.JPanel cellEditor;
    private javax.swing.JMenuItem deleteAttributeItem;
    private javax.swing.JMenuItem deleteOperationItem;
    private javax.swing.JMenuItem deleteParameterItem;
    private javax.swing.JCheckBox isAbstractCheckBox;
    private javax.swing.JTextField itemNameText;
    private javax.swing.JComboBox itemScopeBox;
    private javax.swing.JTextField itemTypeText;
    private javax.swing.JComboBox itemVisibilityBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField nameText;
    private javax.swing.JPopupMenu operationMenu;
    private javax.swing.JPopupMenu operationRootMenu;
    private javax.swing.JPopupMenu parameterMenu;
    private javax.swing.JTree propertiesTree;
    // End of variables declaration//GEN-END:variables
}

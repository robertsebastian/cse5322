/*
 * Copyright (C) 2014 djc
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author djc
 */
public class TabbedPaneListener extends MouseAdapter {
    private final JTextField editor = new JTextField();
    private final JTabbedPane tabbedPane;
    private int editingIdx = -1;
    private int len = -1;
    private Dimension dim;
    private Component tabComponent;

    public TabbedPaneListener(final JTabbedPane tabbedPane) {
        super();
        this.tabbedPane = tabbedPane;
        editor.setBorder(BorderFactory.createEmptyBorder());
        editor.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                renameTabTitle();
            }
        });
        editor.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    renameTabTitle();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelEditing();
                } else {
                    editor.setPreferredSize(editor.getText().length() > len ? null : dim);
                    tabbedPane.revalidate();
                }
            }
        });
        tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start-editing");
        tabbedPane.getActionMap().put("start-editing", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                startEditing();
            }
        });
    }
    
    @Override public void mouseClicked(MouseEvent me) {
        Rectangle rect = tabbedPane.getUI().getTabBounds(tabbedPane, tabbedPane.getSelectedIndex());
        
        // if double-clicking the tab pane's tab component area
        if (rect != null && rect.contains(me.getPoint()) && me.getClickCount() == 2) {
            startEditing();
        } else {
            renameTabTitle();
        }
    }
    
    
    // put the tab pane's tab component in edit mode
    private void startEditing() {
        editingIdx = tabbedPane.getSelectedIndex();
        tabComponent = tabbedPane.getTabComponentAt(editingIdx);
        tabbedPane.setTabComponentAt(editingIdx, editor);
        editor.setVisible(true);
        editor.setText(tabbedPane.getTitleAt(editingIdx));
        editor.selectAll();
        editor.requestFocusInWindow();
        len = editor.getText().length();
        dim = editor.getPreferredSize();
        editor.setMinimumSize(dim);
    }
    
    // cancel the edit mode of the tab pane's tab component
    private void cancelEditing() {
        if (editingIdx >= 0) {
            tabbedPane.setTabComponentAt(editingIdx, tabComponent);
            editor.setVisible(false);
            editingIdx = -1;
            len = -1;
            tabComponent = null;
            editor.setPreferredSize(null);
            tabbedPane.requestFocusInWindow();
        }
    }
    
    // rename the title on the tab
    private void renameTabTitle() {
        boolean found = false;
        String title = editor.getText().trim();
        if (editingIdx >= 0 && !title.isEmpty()) {
            // make sure title doesn't already exist on other tabs
            for(int i = 0; i < tabbedPane.getTabCount(); i++) {
                if(tabbedPane.getTitleAt(i).equals(title)) {
                    found = true;
                    break;
                }
            }
            if(!found) tabbedPane.setTitleAt(editingIdx, title);
        }
        ((EditorPanel)((JScrollPane)tabbedPane.getSelectedComponent()).getViewport().getView()).getManager().rename(title);
        cancelEditing();
    }    
}

package classdiagrameditor;

import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

public class ClassPropertiesTableModel extends AbstractTableModel
    implements SelectionObserver {

    ClassElement element_;

    public ClassPropertiesTableModel() {
        //DiagramManager.getInstance().registerSelectionObserver(this);
    }

    @Override
    public int getRowCount() {
        if (element_ == null) return 0;
        return element_.getProperties().size() + 1;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "Properties";
    }

    @Override
    public Object getValueAt(int row, int col) {
        List properties = element_.getProperties();

        if (row >= properties.size()) return "";
        return properties.get(row);
    }

    @Override
    public void notifySelectionChanged(Set<Element> selection) {
        element_ = null;
        if (selection.size() == 1) {
            for (Element e : selection) {
                if (e instanceof ClassElement) {
                    element_ = (ClassElement)e;
                }
            }
        }
        fireTableStructureChanged();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object val, int row, int col) {
        String str = (String)val;
        List properties = element_.getProperties();

        if(str.isEmpty()) {
            properties.remove(row);
            fireTableStructureChanged();
        } else if (row >= properties.size()) {
            properties.add(str);
            fireTableStructureChanged();
        } else {
            element_.getProperties().set(row, (String)val);
        }
    }
}
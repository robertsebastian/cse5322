package classdiagrameditor;

import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

public class ClassPropertiesTableModel extends AbstractTableModel {

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

    public void setElement(ClassElement element) {
        element_ = element;
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
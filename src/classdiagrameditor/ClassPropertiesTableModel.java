package classdiagrameditor;

import java.util.Set;
import javax.swing.table.AbstractTableModel;

public class ClassPropertiesTableModel extends AbstractTableModel
    implements SelectionObserver {

    ClassElement element_;

    public ClassPropertiesTableModel() {
        DiagramManager.getInstance().registerSelectionObserver(this);
    }

    @Override
    public int getRowCount() {
        if (element_ == null) return 0;
        return element_.getProperties().size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        return element_.getProperties().get(rowIndex);
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
}

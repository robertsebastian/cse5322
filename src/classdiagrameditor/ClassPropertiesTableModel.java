package classdiagrameditor;

import com.google.common.base.CaseFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClassPropertiesTableModel extends AbstractTableModel {

    ClassElement element_;
    ClassElement.PropertiesType propertiesType_;

    public ClassPropertiesTableModel(ClassElement.PropertiesType propertiesType) {
        propertiesType_ = propertiesType;
    }

    @Override
    public int getRowCount() {
        if (element_ == null) return 0;
        return element_.getProperties(propertiesType_).size() + 1;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, propertiesType_.toString());
    }

    @Override
    public Object getValueAt(int row, int col) {
        List properties = element_.getProperties(propertiesType_);

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
        List properties = element_.getAttributes();

        if(str.isEmpty()) {
            properties.remove(row);
            fireTableStructureChanged();
        } else if (row >= properties.size()) {
            properties.add(str);
            fireTableStructureChanged();
        } else {
            element_.getAttributes().set(row, (String)val);
        }
    }
}
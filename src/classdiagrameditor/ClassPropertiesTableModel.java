package classdiagrameditor;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClassPropertiesTableModel extends AbstractTableModel {
    private ClassElement element_;
    private final ClassElement.PropertiesType propertiesType_;

    private final String[] colNames_ = new String[] {"Visibility", "Scope", "Property"};

    public ClassPropertiesTableModel(ClassElement.PropertiesType propertiesType) {
        propertiesType_ = propertiesType;
        colNames_[2] = propertiesType_.toString();
    }

    @Override
    public int getRowCount() {
        if (element_ == null) return 0;
        return element_.getProperties(propertiesType_).size();
    }

    @Override
    public int getColumnCount() {
        return colNames_.length;
    }

    @Override
    public String getColumnName(int col) {
        return colNames_[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        List<ClassElement.Member> properties = element_.getProperties(propertiesType_);

        if (col == 0) return properties.get(row).visibility.toString();
        if (col == 1) return properties.get(row).scope.toString();
        return properties.get(row).text;
    }

    public void setElement(ClassElement element) {
        element_ = element;
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object val, int row, int col) {
        List<ClassElement.Member> properties = element_.getProperties(propertiesType_);
        
        if (col == 0) properties.get(row).visibility =
                Enum.valueOf(ClassElement.VisibilityType.class, val.toString());
        if (col == 1) properties.get(row).scope =
                Enum.valueOf(ClassElement.ScopeType.class, val.toString());
        if (col == 2) properties.get(row).text = (String)val;

        fireTableDataChanged();
    }
}
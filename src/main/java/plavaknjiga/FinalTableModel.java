package plavaknjiga;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FinalTableModel extends AbstractTableModel {
    private List<Record> recordList;
    private String[] columnNames = {"Ime", "Broj spisa"};

    public FinalTableModel(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return recordList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Record record = recordList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return record.getName();
            case 1:
                return record.getAct();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex==0) {
            return String.class;
        }else return int.class;
    }
}

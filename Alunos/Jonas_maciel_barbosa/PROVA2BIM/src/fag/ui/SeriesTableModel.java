package fag.ui;

import fag.model.Series;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SeriesTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
            "Nome", "Idioma", "Generos", "Nota", "Estado", "Estreia", "Termino", "Emissora"
    };

    private List<Series> series = new ArrayList<>();

    public void setSeries(List<Series> series) {
        this.series = series != null ? series : new ArrayList<>();
        fireTableDataChanged();
    }

    public Series getSeriesAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= series.size()) {
            return null;
        }
        return series.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return series.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Series s = series.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getName();
            case 1: return s.getLanguage() != null ? s.getLanguage() : "-";
            case 2: return s.getGenresAsText();
            case 3: return s.getRating() != null ? s.getRating() : "-";
            case 4: return s.getStatus() != null ? s.getStatus().getDescricaoPtBr() : "-";
            case 5: return s.getPremiered() != null ? s.getPremiered() : "-";
            case 6: return s.getEnded() != null ? s.getEnded() : "-";
            case 7: return s.getNetwork() != null ? s.getNetwork() : "-";
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

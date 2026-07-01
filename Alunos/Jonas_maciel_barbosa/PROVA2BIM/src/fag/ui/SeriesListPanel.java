package fag.ui;

import fag.exceptions.PersistenceException;
import fag.model.AppData;
import fag.model.ListType;
import fag.model.Series;
import fag.model.UserLibrary;
import fag.service.PersistenceService;
import fag.service.SeriesComparators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class SeriesListPanel extends JPanel {

    private final ListType listType;
    private final AppData appData;
    private final PersistenceService persistenceService;
    private final Supplier<String> currentUsernameProvider;

    private final SeriesTableModel tableModel = new SeriesTableModel();
    private final JTable table = new JTable(tableModel);
    private final JComboBox<String> sortCombo = new JComboBox<>(
            new String[]{"Ordem alfabetica", "Nota geral", "Estado", "Data de estreia"});
    private final JLabel countLabel = new JLabel();

    public SeriesListPanel(ListType listType, AppData appData, PersistenceService persistenceService,
                            Supplier<String> currentUsernameProvider) {
        this.listType = listType;
        this.appData = appData;
        this.persistenceService = persistenceService;
        this.currentUsernameProvider = currentUsernameProvider;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Ordenar por:"));
        topPanel.add(sortCombo);
        topPanel.add(countLabel);
        add(topPanel, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton detailsButton = new JButton("Ver Detalhes");
        JButton removeButton = new JButton("Remover da Lista");
        bottomPanel.add(detailsButton);
        bottomPanel.add(removeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        sortCombo.addActionListener(e -> refresh());
        detailsButton.addActionListener(e -> openDetails());
        removeButton.addActionListener(e -> removeSelected());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openDetails();
                }
            }
        });

        refresh();
    }

    public void refresh() {
        UserLibrary library = appData.getOrCreateUser(currentUsernameProvider.get());
        List<Series> series = appData.resolve(library.getIds(listType).stream().toList());

        Comparator<Series> comparator;
        switch (String.valueOf(sortCombo.getSelectedItem())) {
            case "Nota geral":
                comparator = SeriesComparators.byRatingDesc();
                break;
            case "Estado":
                comparator = SeriesComparators.byStatus();
                break;
            case "Data de estreia":
                comparator = SeriesComparators.byPremiereDate();
                break;
            default:
                comparator = SeriesComparators.byName();
        }
        series.sort(comparator);

        tableModel.setSeries(series);
        countLabel.setText("  (" + series.size() + " serie(s))");
    }

    private void openDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma serie na lista.",
                    "Nenhuma serie selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Series series = tableModel.getSeriesAt(row);
        if (series == null) {
            return;
        }
        UserLibrary library = appData.getOrCreateUser(currentUsernameProvider.get());
        Window owner = SwingUtilities.getWindowAncestor(this);
        SeriesDetailDialog dialog = new SeriesDetailDialog(owner, series, appData, library,
                persistenceService, this::refresh);
        dialog.setVisible(true);
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma serie na lista para remover.",
                    "Nenhuma serie selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Series series = tableModel.getSeriesAt(row);
        if (series == null) {
            return;
        }
        UserLibrary library = appData.getOrCreateUser(currentUsernameProvider.get());
        library.remove(listType, series.getId());
        try {
            persistenceService.save(appData);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel salvar a remocao: " + ex.getMessage(),
                    "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
            library.add(listType, series.getId());
            return;
        }
        refresh();
    }
}

package fag.ui;

import fag.exceptions.ApiException;
import fag.model.AppData;
import fag.model.Series;
import fag.model.UserLibrary;
import fag.service.PersistenceService;
import fag.service.TVMazeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchPanel extends JPanel {

    private final TVMazeService tvMazeService = new TVMazeService();
    private final AppData appData;
    private final PersistenceService persistenceService;
    private final Runnable onDataChanged;

    private final JTextField searchField = new JTextField(30);
    private final JButton searchButton = new JButton("Buscar");
    private final JButton detailsButton = new JButton("Ver Detalhes");
    private final SeriesTableModel tableModel = new SeriesTableModel();
    private final JTable table = new JTable(tableModel);
    private final JLabel statusLabel = new JLabel(" ");

    public SearchPanel(AppData appData, PersistenceService persistenceService, Runnable onDataChanged) {
        this.appData = appData;
        this.persistenceService = persistenceService;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Nome da serie:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(detailsButton);
        bottomPanel.add(buttonsPanel, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openDetails();
                }
            }
        });
        detailsButton.addActionListener(e -> openDetails());
    }

    private void performSearch() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma serie para buscar.",
                    "Campo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        searchButton.setEnabled(false);
        statusLabel.setText("Buscando...");

        SwingWorker<List<Series>, Void> worker = new SwingWorker<List<Series>, Void>() {
            @Override
            protected List<Series> doInBackground() throws Exception {
                return tvMazeService.searchShows(query);
            }

            @Override
            protected void done() {
                searchButton.setEnabled(true);
                try {
                    List<Series> results = get();
                    tableModel.setSeries(results);
                    for (Series s : results) {
                        appData.cacheSeries(s);
                    }
                    statusLabel.setText(results.size() + " resultado(s) encontrado(s).");
                    if (results.isEmpty()) {
                        JOptionPane.showMessageDialog(SearchPanel.this,
                                "Nenhuma serie encontrada para \"" + query + "\".",
                                "Sem resultados", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    statusLabel.setText("Busca interrompida.");
                } catch (ExecutionException ee) {
                    Throwable cause = ee.getCause();
                    String msg = (cause instanceof ApiException) ? cause.getMessage()
                            : "Ocorreu um erro inesperado ao buscar series.";
                    statusLabel.setText("Falha na busca.");
                    JOptionPane.showMessageDialog(SearchPanel.this, msg,
                            "Erro na busca", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void openDetails() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma serie na lista de resultados.",
                    "Nenhuma serie selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        Series series = tableModel.getSeriesAt(modelRow);
        if (series == null) {
            return;
        }
        UserLibrary library = appData.getOrCreateUser(currentUsernameSafe());
        Window owner = SwingUtilities.getWindowAncestor(this);
        SeriesDetailDialog dialog = new SeriesDetailDialog(owner, series, appData, library,
                persistenceService, onDataChanged);
        dialog.setVisible(true);
    }

    private String currentUsernameSafe() {
        if (this.currentUsernameProvider != null) {
            return currentUsernameProvider.get();
        }
        return "Convidado";
    }

    private java.util.function.Supplier<String> currentUsernameProvider;

    public void setCurrentUsernameProvider(java.util.function.Supplier<String> provider) {
        this.currentUsernameProvider = provider;
    }
}

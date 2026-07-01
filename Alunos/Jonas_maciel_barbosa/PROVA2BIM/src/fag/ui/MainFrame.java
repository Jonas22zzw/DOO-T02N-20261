package fag.ui;

import fag.exceptions.PersistenceException;
import fag.model.AppData;
import fag.model.ListType;
import fag.service.PersistenceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final AppData appData;
    private final PersistenceService persistenceService;
    private String currentUsername;

    private final JLabel userLabel = new JLabel();
    private SeriesListPanel favoritesPanel;
    private SeriesListPanel watchedPanel;
    private SeriesListPanel wantToWatchPanel;

    public MainFrame(AppData appData, PersistenceService persistenceService, String initialUsername) {
        super("TV Tracker - Acompanhamento de Series");
        this.appData = appData;
        this.persistenceService = persistenceService;
        this.currentUsername = initialUsername;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndExit();
            }
        });

        updateUserLabel();
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        topBar.add(userLabel, BorderLayout.WEST);

        JButton trocarUsuarioBtn = new JButton("Trocar Usuario");
        trocarUsuarioBtn.addActionListener(e -> trocarUsuario());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(trocarUsuarioBtn);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();

        SearchPanel searchPanel = new SearchPanel(appData, persistenceService, this::refreshAllLists);
        searchPanel.setCurrentUsernameProvider(() -> currentUsername);

        favoritesPanel = new SeriesListPanel(ListType.FAVORITES, appData, persistenceService, () -> currentUsername);
        watchedPanel = new SeriesListPanel(ListType.WATCHED, appData, persistenceService, () -> currentUsername);
        wantToWatchPanel = new SeriesListPanel(ListType.WANT_TO_WATCH, appData, persistenceService, () -> currentUsername);

        tabs.addTab("Buscar Series", searchPanel);
        tabs.addTab("Favoritos", favoritesPanel);
        tabs.addTab("Ja Assistidas", watchedPanel);
        tabs.addTab("Quero Assistir", wantToWatchPanel);

        tabs.addChangeListener(e -> refreshAllLists());

        return tabs;
    }

    private void refreshAllLists() {
        if (favoritesPanel != null) favoritesPanel.refresh();
        if (watchedPanel != null) watchedPanel.refresh();
        if (wantToWatchPanel != null) wantToWatchPanel.refresh();
    }

    private void updateUserLabel() {
        userLabel.setText("Usuario atual: " + currentUsername);
    }

    private void trocarUsuario() {
        LoginDialog dialog = new LoginDialog(this, appData);
        dialog.setVisible(true);
        String escolhido = dialog.getChosenUsername();
        if (escolhido != null && !escolhido.isEmpty()) {
            this.currentUsername = escolhido;
            appData.getOrCreateUser(escolhido);
            updateUserLabel();
            refreshAllLists();
            trySave();
        }
    }

    private void confirmAndExit() {
        trySave();
        dispose();
        System.exit(0);
    }

    private void trySave() {
        try {
            persistenceService.save(appData);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel salvar os dados antes de sair: " + ex.getMessage(),
                    "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }
}

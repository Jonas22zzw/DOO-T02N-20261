package fag;

import fag.exceptions.PersistenceException;
import fag.model.AppData;
import fag.service.PersistenceService;
import fag.ui.LoginDialog;
import fag.ui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro inesperado, mas o sistema continuara em execucao.\n\nDetalhes: "
                            + throwable.getMessage(),
                    "Erro inesperado", JOptionPane.ERROR_MESSAGE));
        });

        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }

        PersistenceService persistenceService = new PersistenceService();
        AppData appData;
        try {
            appData = persistenceService.load();
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel carregar os dados salvos. O sistema iniciara com uma base vazia.\n\nDetalhes: "
                            + ex.getMessage(),
                    "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
            appData = new AppData();
        }

        LoginDialog loginDialog = new LoginDialog(null, appData);
        loginDialog.setVisible(true);
        String username = loginDialog.getChosenUsername();
        if (username == null || username.isEmpty()) {

            return;
        }
        appData.getOrCreateUser(username);

        try {
            persistenceService.save(appData);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel salvar os dados iniciais: " + ex.getMessage(),
                    "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }

        MainFrame frame = new MainFrame(appData, persistenceService, username);
        frame.setVisible(true);
    }
}

package fag.ui;

import fag.model.AppData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginDialog extends JDialog {

    private String chosenUsername;

    public LoginDialog(Frame owner, AppData appData) {
        super(owner, "Identifique-se", true);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Bem-vindo(a) ao TV Tracker!");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(10));

        List<String> existentes = appData.getUsernames();
        JComboBox<String> combo = null;
        if (!existentes.isEmpty()) {
            content.add(new JLabel("Selecione um usuario existente:"));
            combo = new JComboBox<>(existentes.toArray(new String[0]));
            combo.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(combo);
            content.add(Box.createVerticalStrut(10));
        }

        content.add(new JLabel("Ou informe um nome/apelido novo:"));
        JTextField textField = new JTextField();
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(textField);

        add(content, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton entrarBtn = new JButton("Entrar");
        buttons.add(entrarBtn);
        add(buttons, BorderLayout.SOUTH);

        final JComboBox<String> comboFinal = combo;
        entrarBtn.addActionListener(e -> {
            String novo = textField.getText() == null ? "" : textField.getText().trim();
            if (!novo.isEmpty()) {
                chosenUsername = novo;
            } else if (comboFinal != null && comboFinal.getSelectedItem() != null) {
                chosenUsername = (String) comboFinal.getSelectedItem();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Informe um nome/apelido para continuar.",
                        "Nome obrigatorio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setVisible(false);
        });

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                chosenUsername = null;
                setVisible(false);
            }
        });

        setSize(360, 260);
        setLocationRelativeTo(owner);
    }

    public String getChosenUsername() {
        return chosenUsername;
    }
}

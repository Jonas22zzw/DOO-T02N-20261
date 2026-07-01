package fag.ui;

import fag.exceptions.PersistenceException;
import fag.model.AppData;
import fag.model.ListType;
import fag.model.Series;
import fag.model.UserLibrary;
import fag.service.PersistenceService;

import javax.swing.*;
import java.awt.*;

public class SeriesDetailDialog extends JDialog {

    public SeriesDetailDialog(Window owner, Series series, AppData appData,
                               UserLibrary library, PersistenceService persistenceService,
                               Runnable onChangeCallback) {
        super(owner, "Detalhes da Serie", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        info.add(bold(series.getName() != null ? series.getName() : "(sem nome)", 18f));
        info.add(Box.createVerticalStrut(8));
        info.add(new JLabel("Idioma: " + orTraco(series.getLanguage())));
        info.add(new JLabel("Generos: " + (series.getGenres().isEmpty() ? "-" : series.getGenresAsText())));
        info.add(new JLabel("Nota geral: " + (series.getRating() != null ? series.getRating() : "-")));
        info.add(new JLabel("Estado: " + (series.getStatus() != null ? series.getStatus().getDescricaoPtBr() : "-")));
        info.add(new JLabel("Data de estreia: " + orTraco(series.getPremiered())));
        info.add(new JLabel("Data de termino: " + orTraco(series.getEnded())));
        info.add(new JLabel("Emissora: " + orTraco(series.getNetwork())));
        info.add(Box.createVerticalStrut(8));

        String resumo = series.getSummaryPlainText();
        JTextArea resumoArea = new JTextArea(resumo == null || resumo.isEmpty() ? "Sem sinopse disponivel." : resumo);
        resumoArea.setLineWrap(true);
        resumoArea.setWrapStyleWord(true);
        resumoArea.setEditable(false);
        resumoArea.setOpaque(false);
        JScrollPane resumoScroll = new JScrollPane(resumoArea);
        resumoScroll.setPreferredSize(new Dimension(420, 100));
        info.add(resumoScroll);

        add(info, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(1, 3, 10, 0));
        actions.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JToggleButton favBtn = createToggleButton("Favoritos", ListType.FAVORITES, series, library);
        JToggleButton watchedBtn = createToggleButton("Ja Assistida", ListType.WATCHED, series, library);
        JToggleButton wantBtn = createToggleButton("Quero Assistir", ListType.WANT_TO_WATCH, series, library);

        for (JToggleButton btn : new JToggleButton[]{favBtn, watchedBtn, wantBtn}) {
            btn.addActionListener(e -> {
                ListType type = (ListType) btn.getClientProperty("listType");
                try {
                    appData.cacheSeries(series);
                    library.toggle(type, series.getId());
                    persistenceService.save(appData);
                    if (onChangeCallback != null) {
                        onChangeCallback.run();
                    }
                    refreshToggleAppearance(btn, library.contains(type, series.getId()));
                } catch (PersistenceException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Nao foi possivel salvar a alteracao: " + ex.getMessage(),
                            "Erro ao salvar", JOptionPane.ERROR_MESSAGE);

                    library.toggle(type, series.getId());
                    btn.setSelected(!btn.isSelected());
                }
            });
            actions.add(btn);
        }

        add(actions, BorderLayout.SOUTH);

        setSize(480, 460);
        setLocationRelativeTo(owner);
    }

    private JToggleButton createToggleButton(String label, ListType type, Series series, UserLibrary library) {
        JToggleButton btn = new JToggleButton(label);
        btn.putClientProperty("listType", type);
        boolean selected = library.contains(type, series.getId());
        refreshToggleAppearance(btn, selected);
        return btn;
    }

    private void refreshToggleAppearance(JToggleButton btn, boolean selected) {
        btn.setSelected(selected);
        String base = ((ListType) btn.getClientProperty("listType")).getLabel();
        btn.setText(selected ? ("[OK] " + base) : base);
    }

    private JLabel bold(String text, float size) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, size));
        return l;
    }

    private String orTraco(String value) {
        return (value == null || value.isEmpty()) ? "-" : value;
    }
}

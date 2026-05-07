import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraSwing extends JFrame {
    private JTextField txtNum1, txtNum2, txtResultado;

    public CalculadoraSwing() {
        setTitle("Calculadora");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        
        add(new JLabel(" Número 1:"));
        txtNum1 = new JTextField();
        add(txtNum1);

        add(new JLabel(" Número 2:"));
        txtNum2 = new JTextField();
        add(txtNum2);

        add(new JLabel(" Operação:"));
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4));
        
        String[] operacoes = {"+", "-", "*", "/"};
        for (String op : operacoes) {
            JButton btn = new JButton(op);
            btn.addActionListener(new OperacaoHandler(op));
            painelBotoes.add(btn);
        }
        add(painelBotoes);

        add(new JLabel(" Resultado:"));
        txtResultado = new JTextField();
        txtResultado.setEditable(false);
        add(txtResultado);
    }

    private class OperacaoHandler implements ActionListener {
        private String op;
        public OperacaoHandler(String op) { this.op = op; }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String t1 = txtNum1.getText();
                String t2 = txtNum2.getText();

                verificarDedoColado(t1);
                verificarDedoColado(t2);

                double n1 = validarEntrada(t1);
                double n2 = validarEntrada(t2);
                double res = 0; 

                switch (op) {
                    case "+": res = n1 + n2; break;
                    case "-": res = n1 - n2; break;
                    case "*": res = n1 * n2; break;
                    case "/": 
                        if (n2 == 0) throw new CalculadoraException("Erro: Divisão por zero!");
                        res = n1 / n2; 
                        break;
                }
                txtResultado.setText(String.valueOf(res));

            } catch (CalculadoraException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro de Regra", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, insira apenas números!", "Entrada Inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void verificarDedoColado(String texto) throws CalculadoraException {
        // Regra: Se tiver mais de 3 caracteres e todos forem iguais ao primeiro
        if (texto.length() > 3 && texto.chars().allMatch(c -> c == texto.charAt(0))) {
            throw new CalculadoraException("Erro: Tá com o dedo colado no '" + texto.charAt(0) + "'?");
        }
    }

    private double validarEntrada(String texto) throws NumberFormatException {
        return Double.parseDouble(texto.replace(",", "."));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraSwing().setVisible(true));
    }
}

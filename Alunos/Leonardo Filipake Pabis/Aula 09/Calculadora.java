import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Calculadora {
    static class CalculadoraException extends Exception {
    public CalculadoraException(String mensagem){
        super(mensagem);
    }
}
    public static double calculo = 0;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculadora");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel resultadoPanel = new JPanel();
        resultadoPanel.setBackground(Color.gray);
        
        JTextField resultadoField = new JTextField();
        resultadoPanel.add(resultadoField);

        frame.add(resultadoPanel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.white);

        JLabel primeiroNumero = new JLabel("Primeiro número:");
        panel.add(primeiroNumero);
        JTextField campo1 = new JTextField();
        panel.add(campo1);

        JLabel segundoNumero = new JLabel("Segundo número:");
        panel.add(segundoNumero);
        JTextField campo2 = new JTextField();
        panel.add(campo2);

       
        

        JButton soma = new JButton("+");
        panel.add(soma);
        JButton subtracao = new JButton("-");
        JButton multiplicacao = new JButton("*");
        JButton divisao = new JButton("/");

        JLabel resultado = new JLabel("Resultado: "+calculo);
        panel.add(resultado);
        resultado.setHorizontalAlignment(SwingConstants.CENTER);

        soma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculo = 0;
                double num1 = stringToNumero(campo1.getText());
                double num2 = stringToNumero(campo2.getText());
                double soma = num1 + num2;
                calculo = soma;
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static double stringToNumero(String texto){
        double numero = Double.parseDouble(texto);
        return numero;
    }
}

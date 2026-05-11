import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Calculadora2 {
    static class CalculadoraException extends Exception {
    public CalculadoraException(String mensagem){
        super(mensagem);
        }
    }

    public static double resultado = 0;
    public static double inputAtual = 0;
    public static String input1 = null;
    public static String input2 = null;
    public static String operacao = null;
    public static void main(String[] args) {

        JFrame frame = new JFrame("Calculadora");
        frame.setLayout(null);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.white);

        JPanel resultadoPanel = new JPanel();
        resultadoPanel.setBounds(100, 25, 200, 50);
        resultadoPanel.setBackground(Color.lightGray);
        

        JTextField resultadoField = new JTextField();
        resultadoField.setPreferredSize(new Dimension(175, 40));
        resultadoField.setBackground(Color.lightGray);
        resultadoField.setText(Double.toString(resultado));
        resultadoField.setBorder(new LineBorder(Color.black,2));
        resultadoField.setEditable(false);
        resultadoField.setHorizontalAlignment(JTextField.CENTER);
    
        resultadoPanel.add(resultadoField);

        JPanel botoes = new JPanel();
        botoes.setBounds(12, 100, 360, 450);
        botoes.setBackground(Color.lightGray);
        botoes.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));

        JButton clear = new JButton();
        clear.setPreferredSize(new Dimension(77, 50));
        clear.setText("C");
        botoes.add(clear);

        JPanel vazio = new JPanel();
        vazio.setPreferredSize(new Dimension(165, 50));
        vazio.setBackground(Color.lightGray);
        botoes.add(vazio);

        JButton divisao = new JButton();
        divisao.setPreferredSize(new Dimension(77, 50));
        divisao.setText("/");
        botoes.add(divisao);

        JButton sete = new JButton();
        sete.setPreferredSize(new Dimension(77, 50));
        sete.setText("7");
        botoes.add(sete);

        JButton oito = new JButton();
        oito.setPreferredSize(new Dimension(77, 50));
        oito.setText("8");
        botoes.add(oito);

        JButton nove = new JButton();
        nove.setPreferredSize(new Dimension(77, 50));
        nove.setText("9");
        botoes.add(nove);

        JButton multiplicacao = new JButton();
        multiplicacao.setPreferredSize(new Dimension(77, 50));
        multiplicacao.setText("*");
        botoes.add(multiplicacao);

        JButton quatro = new JButton();
        quatro.setPreferredSize(new Dimension(77, 50));
        quatro.setText("4");
        botoes.add(quatro);

        JButton cinco = new JButton();
        cinco.setPreferredSize(new Dimension(77, 50));
        cinco.setText("5");
        botoes.add(cinco);

        JButton seis = new JButton();
        seis.setPreferredSize(new Dimension(77, 50));
        seis.setText("6");
        botoes.add(seis);

        JButton menos = new JButton();
        menos.setPreferredSize(new Dimension(77, 50));
        menos.setText("-");
        botoes.add(menos);

        JButton um = new JButton();
        um.setPreferredSize(new Dimension(77, 50));
        um.setText("1");
        botoes.add(um);

        JButton dois = new JButton();
        dois.setPreferredSize(new Dimension(77, 50));
        dois.setText("2");
        botoes.add(dois);

        JButton tres = new JButton();
        tres.setPreferredSize(new Dimension(77, 50));
        tres.setText("3");
        botoes.add(tres);

        JButton mais = new JButton();
        mais.setPreferredSize(new Dimension(77, 50));
        mais.setText("+");
        botoes.add(mais);

        JPanel vazio1 = new JPanel();
        vazio1.setPreferredSize(new Dimension(77, 50));
        vazio1.setBackground(Color.lightGray);
        botoes.add(vazio1);

        JButton zero = new JButton();
        zero.setPreferredSize(new Dimension(77, 50));
        zero.setText("0");
        botoes.add(zero);

        JPanel vazio2 = new JPanel();
        vazio2.setPreferredSize(new Dimension(77, 50));
        vazio2.setBackground(Color.lightGray);
        botoes.add(vazio2);

        JButton igual = new JButton();
        igual.setPreferredSize(new Dimension(77, 50));
        igual.setText("=");
        botoes.add(igual);

        frame.add(resultadoPanel);
        frame.add(botoes);
        frame.setVisible(true);

        um.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputAtual == 0){
                    resultadoField.setText("1");
                    inputAtual = 1;
                }else{
                resultadoField.setText(resultadoField.getText() + '1');
                }
            }
        });

        mais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorAtual = Double.parseDouble(resultadoField.getText());

                    if (inputAtual == 0){
                        resultado = valorAtual;
                    }else {
                        resultado += valorAtual;
                    }
                    operacao = "+";
                    inputAtual = 0;
                    resultadoField.setText("0.0");
                } catch (NumberFormatException ex) {
                    resultadoField.setText("Error");
                }
            }
        });

        igual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorAtual = Double.parseDouble(resultadoField.getText());

                    if (operacao.equals("+")){
                        resultado += valorAtual;
                    }

                    resultadoField.setText(Double.toString(resultado));

                    resultado = 0;
                    inputAtual = 0;
                } catch (Exception ex) {
                    resultadoField.setText("Error");
                }
            }
        });
    
    }
}

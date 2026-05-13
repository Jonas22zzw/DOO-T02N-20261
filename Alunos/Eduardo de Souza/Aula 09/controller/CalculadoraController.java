package calculadora.controller;

import calculadora.exception.CalculadoraException;
import calculadora.view.CalculadoraView;

public class CalculadoraController {

	private CalculadoraView view;

	private StringBuilder expressao = new StringBuilder();

	public CalculadoraController(CalculadoraView view) {

		this.view = view;

		eventos();
	}

	private boolean ehOperador(char c) {

		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private void adicionarValor(javax.swing.JButton button, String valor) {

		button.addActionListener(e -> {

			if ("+-*/".contains(valor)) {

				if (expressao.length() == 0) {
					return;
				}

				char ultimoCaractere = expressao.charAt(expressao.length() - 1);

				if (ehOperador(ultimoCaractere)) {
					return;
				}
			}

			expressao.append(valor);

			view.label_result.setText(expressao.toString());
		});
	}

	private void calcularResultado() {
	
		view.button_equal.addActionListener(e -> {
	
			try {
	
				if (expressao.length() == 0) {
					return;
				}
	
				char ultimoCaractere =
					expressao.charAt(expressao.length() - 1);
	
				if (ehOperador(ultimoCaractere)) {
	
					expressao.deleteCharAt(expressao.length() - 1);
				}
	
				String conta = expressao.toString();
	
				double resultado = resolverExpressao(conta);
	
				expressao.setLength(0);
	
				expressao.append(resultado);
	
				view.label_result.setText(expressao.toString());
	
			} catch (CalculadoraException ex) {

				view.label_result.setText(ex.getMessage());

				expressao.setLength(0);
			}
		});
	}

	private void limpar() {

		view.button_c.addActionListener(e -> {

			expressao.setLength(0);

			view.label_result.setText("");
		});
	}

	private double resolverExpressao(String conta) throws CalculadoraException {

		double resultado = 0;

		String numeroAtual = "";

		char operadorAtual = '+';

		for (int i = 0; i < conta.length(); i++) {

			char c = conta.charAt(i);

			if (!Character.isDigit(c) && !ehOperador(c) && c != '.') {

				throw new CalculadoraException(
					"Entrada inválida: " + c
				);
			}

			if (Character.isDigit(c) || c == '.') {

				numeroAtual += c;
			}

			if (ehOperador(c) || i == conta.length() - 1) {

				if (numeroAtual.isEmpty()) {

					throw new CalculadoraException(
						"Número inválido."
					);
				}

				double numero;

				try {

					numero = Double.parseDouble(numeroAtual);

				} catch (NumberFormatException ex) {

					throw new CalculadoraException(
						"Entrada numérica inválida."
					);
				}

				switch (operadorAtual) {

					case '+':
						resultado += numero;
						break;

					case '-':
						resultado -= numero;
						break;

					case '*':
						resultado *= numero;
						break;

					case '/':

						if (numero == 0) {

							throw new CalculadoraException(
								"Divisão por zero não permitida."
							);
						}

						resultado /= numero;
						break;
				}

				operadorAtual = c;

				numeroAtual = "";
			}
		}

		return resultado;
	}
	
	public void eventos() {

		adicionarValor(view.button_zero, "0");
		adicionarValor(view.button_one, "1");
		adicionarValor(view.button_two, "2");
		adicionarValor(view.button_three, "3");
		adicionarValor(view.button_four, "4");
		adicionarValor(view.button_five, "5");
		adicionarValor(view.button_six, "6");
		adicionarValor(view.button_seven, "7");
		adicionarValor(view.button_eight, "8");
		adicionarValor(view.button_nine, "9");

		adicionarValor(view.button_sum, "+");
		adicionarValor(view.button_minus, "-");
		adicionarValor(view.button_multiply, "*");
		adicionarValor(view.button_divide, "/");

		calcularResultado();

		limpar();
	}
}
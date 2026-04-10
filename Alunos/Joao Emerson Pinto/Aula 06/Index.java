import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import objetos.Cliente;
import objetos.Loja;
import objetos.RegistroVenda;
import objetos.Resultados;
import objetos.Vendedor;

public class Index {

    static Scanner scan = new Scanner(System.in);
    static ArrayList<RegistroVenda> vendas = new ArrayList<>();
    static ArrayList<Loja> lojas = new ArrayList<>();
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<Vendedor> vendedores = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        Resultados.clearTerminal();
        menu();
    }

    public static void menu() {
        int opcao = -1;

        do {
            System.out.println("================================");
            System.out.println("( 1 ) Calcular valor total ");
            System.out.println("( 2 ) Listar registro de vendas ");
            System.out.println("( 3 ) Consultar total vendido por dia ");
            System.out.println("( 4 ) Consultar total vendido por mes ");
            System.out.println("( 5 ) Menu de cadastros ");
            System.out.println("( 6 ) Popular listas ");
            System.out.println("( 0 ) Sair ");
            System.out.println("================================");

            System.out.print("Informe uma opcao: ");
            opcao = scan.nextInt();
            scan.nextLine();

            validaOpcao(opcao);
        } while (opcao != 0);
    }

    public static void validaOpcao(int opcao) {
        if (opcao == 1) {
            calculaTotal();
        } else if (opcao == 2) {
            Resultados.mostraVendas(vendas);
        } else if (opcao == 3) {
            consultarVendasPorDia();
        } else if (opcao == 4) {
            consultarVendasPorMes();
        } else if (opcao == 5) {
            menuCadastros();
        } else if (opcao == 6) {
            popularListas();
        } else if (opcao == 0) {
            System.out.println("Saindo...");
        } else {
            System.out.println("Opcao invalida, tente novamente.");
        }
    }

    public static void menuCadastros() {
        int opcao = -1;

        do {
            System.out.println("================================");
            System.out.println("( 1 ) Cadastrar loja ");
            System.out.println("( 2 ) Cadastrar cliente ");
            System.out.println("( 3 ) Cadastrar vendedor ");
            System.out.println("( 4 ) Listar lojas cadastradas ");
            System.out.println("( 0 ) Voltar ");
            System.out.println("================================");

            System.out.print("Informe uma opcao: ");
            opcao = scan.nextInt();
            scan.nextLine();

            if (opcao == 1) {
                cadastrarLoja();
            } else if (opcao == 2) {
                cadastrarCliente();
            } else if (opcao == 3) {
                cadastrarVendedor();
            } else if (opcao == 4) {
                listarLojasCadastradas();
            } else if (opcao != 0) {
                System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    public static void cadastrarLoja() {
        System.out.println("================================");
        System.out.print("Nome fantasia: ");
        String nomeFantasia = scan.nextLine();

        System.out.print("Razao social: ");
        String razaoSocial = scan.nextLine();

        System.out.print("CNPJ: ");
        String cnpj = scan.nextLine();

        System.out.print("Cidade: ");
        String cidade = scan.nextLine();

        System.out.print("Bairro: ");
        String bairro = scan.nextLine();

        System.out.print("Rua: ");
        String rua = scan.nextLine();

        Loja loja = new Loja(nomeFantasia, razaoSocial, cnpj, cidade, bairro, rua, null, null);
        lojas.add(loja);

        System.out.println("Loja cadastrada com sucesso.");
    }

    public static void cadastrarCliente() {
        if (lojas.isEmpty()) {
            System.out.println("Cadastre uma loja antes de cadastrar clientes.");
            return;
        }

        System.out.println("================================");
        System.out.print("Nome do cliente: ");
        String nome = scan.nextLine();

        System.out.print("Idade do cliente: ");
        int idade = scan.nextInt();
        scan.nextLine();

        System.out.print("Cidade: ");
        String cidade = scan.nextLine();

        System.out.print("Bairro: ");
        String bairro = scan.nextLine();

        System.out.print("Rua: ");
        String rua = scan.nextLine();

        Cliente cliente = new Cliente(nome, idade, cidade, bairro, rua);
        clientes.add(cliente);

        Loja lojaEscolhida = selecionarLoja();

        if (lojaEscolhida != null) {
            lojaEscolhida.adicionarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso na loja " + lojaEscolhida.getNomeFantasia() + ".");
        }
    }

    public static void cadastrarVendedor() {
        if (lojas.isEmpty()) {
            System.out.println("Cadastre uma loja antes de cadastrar vendedores.");
            return;
        }

        System.out.println("================================");
        System.out.print("Nome do vendedor: ");
        String nome = scan.nextLine();

        System.out.print("Idade do vendedor: ");
        int idade = scan.nextInt();
        scan.nextLine();

        System.out.print("Cidade: ");
        String cidade = scan.nextLine();

        System.out.print("Bairro: ");
        String bairro = scan.nextLine();

        System.out.print("Rua: ");
        String rua = scan.nextLine();

        System.out.print("Salario base: ");
        double salarioBase = scan.nextDouble();

        double[] salariosRecebidos = new double[3];
        for (int i = 0; i < salariosRecebidos.length; i++) {
            System.out.print("Informe o salario recebido " + (i + 1) + ": ");
            salariosRecebidos[i] = scan.nextDouble();
        }
        scan.nextLine();

        Loja lojaEscolhida = selecionarLoja();

        if (lojaEscolhida == null) {
            System.out.println("Loja invalida.");
            return;
        }

        Vendedor vendedor = new Vendedor(
            nome,
            idade,
            lojaEscolhida.getNomeFantasia(),
            cidade,
            bairro,
            rua,
            salarioBase,
            salariosRecebidos
        );

        vendedores.add(vendedor);
        lojaEscolhida.adicionarVendedor(vendedor);

        System.out.println("Vendedor cadastrado com sucesso na loja " + lojaEscolhida.getNomeFantasia() + ".");
    }

    public static Loja selecionarLoja() {
        System.out.println("Selecione a loja:");

        for (int i = 0; i < lojas.size(); i++) {
            System.out.println((i + 1) + " - " + lojas.get(i).getNomeFantasia());
        }

        System.out.print("Opcao: ");
        int indice = scan.nextInt();
        scan.nextLine();

        if (indice < 1 || indice > lojas.size()) {
            return null;
        }

        return lojas.get(indice - 1);
    }

    public static void listarLojasCadastradas() {
        System.out.println("================================");

        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada.");
            return;
        }

        for (Loja loja : lojas) {
            loja.apresentarse();
            System.out.println("Quantidade de clientes: " + loja.contarClientes());
            System.out.println("Quantidade de vendedores: " + loja.contarVendedores());
            System.out.println("================================");
        }
    }

    public static void calculaTotal() {
        System.out.println("================================");

        System.out.print("Informe o valor do produto: ");
        float valorProduto = scan.nextFloat();

        System.out.print("Informe a quantidade: ");
        int quantidade = scan.nextInt();
        scan.nextLine();

        double valorTotal = valorProduto * quantidade;
        Resultados.mostraTotalCompra(valorProduto, quantidade, valorTotal);

        if (quantidade >= 10) {
            valorTotal = calculaDesconto(valorTotal);
        }

        calculaTroco(valorTotal);
    }

    public static void calculaTroco(double valorTotal) {
        System.out.println("================================");
        System.out.println("...Calcular troco... ");

        System.out.print("Informe o valor pago: ");
        double valorPago = scan.nextDouble();
        scan.nextLine();

        double troco = valorPago - valorTotal;

        if (troco < 0) {
            System.out.printf("Valor insuficiente, faltam: %.2f%n", Math.abs(troco));
        } else {
            Resultados.mostraTroco(valorTotal, valorPago, troco);

            LocalDate dataVenda = lerDataVenda();
            vendas.add(new RegistroVenda(dataVenda, valorTotal));

            System.out.println("Venda registrada com sucesso na data " + dataVenda.format(formatter));
        }
    }

    public static double calculaDesconto(double valorTotal) {
        double valorDesconto = 95;
        double desconto = (valorTotal * valorDesconto) / 100;
        Resultados.mostraDesconto(desconto);
        return desconto;
    }

    public static LocalDate lerDataVenda() {
        while (true) {
            try {
                System.out.print("Informe a data da venda (dd/MM/yyyy): ");
                String dataTexto = scan.nextLine();
                return LocalDate.parse(dataTexto, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida. Tente novamente no formato dd/MM/yyyy.");
            }
        }
    }

    public static void consultarVendasPorDia() {
        try {
            System.out.print("Informe a data para consulta (dd/MM/yyyy): ");
            String dataTexto = scan.nextLine();

            LocalDate dataConsulta = LocalDate.parse(dataTexto, formatter);
            double totalDia = 0;

            for (RegistroVenda venda : vendas) {
                if (venda.getData().equals(dataConsulta)) {
                    totalDia += venda.getValor();
                }
            }

            Resultados.mostraTotalDia(dataConsulta, totalDia);
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato dd/MM/yyyy.");
        }
    }

    public static void consultarVendasPorMes() {
        try {
            System.out.print("Informe o mes e ano para consulta (MM/yyyy): ");
            String mesAno = scan.nextLine();

            String[] partes = mesAno.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);
            double totalMes = 0;

            for (RegistroVenda venda : vendas) {
                if (venda.getData().getMonthValue() == mes && venda.getData().getYear() == ano) {
                    totalMes += venda.getValor();
                }
            }

            Resultados.mostraTotalMes(mes, ano, totalMes);
        } catch (Exception e) {
            System.out.println("Formato invalido. Use MM/yyyy.");
        }
    }

    public static void popularListas() {
        lojas.clear();
        clientes.clear();
        vendedores.clear();

        Loja matriz = new Loja(
            "My Plant",
            "Dona Gabrielinha Flores LTDA",
            "12.345.678/0001-10",
            "Cascavel",
            "Centro",
            "Rua das Margaridas, 100",
            null,
            null
        );

        Loja filial = new Loja(
            "Gabrielinha Flores Jardim",
            "Dona Gabrielinha Flores LTDA",
            "12.345.678/0002-00",
            "Cascavel",
            "Batel",
            "Avenida das Flores, 250",
            null,
            null
        );

        Cliente cliente1 = new Cliente("Marina", 29, "Cascavel", "Centro", "Rua das Palmeiras");
        Cliente cliente2 = new Cliente("Renato", 41, "Cascavel", "Batel", "Rua das Orquideas");
        Cliente cliente3 = new Cliente("Lucia", 35, "Cascavel", "Agua Verde", "Rua dos Lirios");

        Vendedor vendedor1 = new Vendedor(
            "Paula",
            32,
            matriz.getNomeFantasia(),
            "Cascavel",
            "Centro",
            "Rua Tulipa",
            2200.0,
            new double[] { 2200.0, 2250.0, 2300.0 }
        );

        Vendedor vendedor2 = new Vendedor(
            "Rafael",
            28,
            filial.getNomeFantasia(),
            "Cascavel",
            "Batel",
            "Rua Girassol",
            2100.0,
            new double[] { 2100.0, 2150.0, 2180.0 }
        );

        matriz.adicionarCliente(cliente1);
        matriz.adicionarCliente(cliente2);
        matriz.adicionarVendedor(vendedor1);

        filial.adicionarCliente(cliente3);
        filial.adicionarVendedor(vendedor2);

        lojas.add(matriz);
        lojas.add(filial);

        clientes.add(cliente1);
        clientes.add(cliente2);
        clientes.add(cliente3);

        vendedores.add(vendedor1);
        vendedores.add(vendedor2);

        System.out.println("================================");
        System.out.println("Listas populadas com sucesso.");
        System.out.println("================================");
    }
}

package Concessionaria.view;

import Concessionaria.helper.Utils;
import Concessionaria.model.FipeItem;
import Concessionaria.model.Veiculo;
import Concessionaria.service.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Veiculo> frota = new ArrayList<>();
    static Double saldo = 100000.00;
    static Double lucroPrejuizoTotal = 0.00;

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("  BEM-VINDO AO SISTEMA DE VEÍCULOS   ");
        System.out.println("=====================================");
        menu();
    }

    public static void menu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1 - Adicionar veículo (Comprar)");
        System.out.println("2 - Listar veículos");
        System.out.println("3 - Editar veículo (Valor de venda)");
        System.out.println("4 - Vender veículo");
        System.out.println("5 - Exibir saldo");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro();

        switch (opcao) {
            case 1:
                adicionarVeiculo();
                break;
            case 2:
                listarVeiculos();
                break;
            case 3:
                editarVeiculo();
                break;
            case 4:
                venderVeiculo();
                break;
            case 5:
                exibirSaldo();
                break;
            case 6:
                System.out.println("Saindo do sistema... Até logo!");
                System.exit(0);
            default:
                System.out.println("Opção inválida! Tente novamente.");
                Utils.pausar(1);
        }
        menu(); // Recursão para manter o menu ativo
    }

    private static void adicionarVeiculo() {
        System.out.println("\n--- COMPRAR VEÍCULO (API FIPE) ---");

        // 1. Marcas
        System.out.println("Buscando marcas...");
        String jsonMarcas = ApiService.get("/marcas");
        List<FipeItem> marcas = ApiService.extrairItens(jsonMarcas);
        FipeItem marcaEscolhida = selecionarItemLista(marcas, "Marca");
        if (marcaEscolhida == null) return;

        // 2. Modelos
        System.out.println("Buscando modelos de " + marcaEscolhida.getNome() + "...");
        String jsonModelos = ApiService.get("/marcas/" + marcaEscolhida.getCodigo() + "/modelos");
        List<FipeItem> modelos = ApiService.extrairModelos(jsonModelos);
        FipeItem modeloEscolhido = selecionarItemLista(modelos, "Modelo");
        if (modeloEscolhido == null) return;

        // 3. Anos
        System.out.println("Buscando anos...");
        String jsonAnos = ApiService.get("/marcas/" + marcaEscolhida.getCodigo() + "/modelos/" + modeloEscolhido.getCodigo() + "/anos");
        List<FipeItem> anos = ApiService.extrairItens(jsonAnos);
        FipeItem anoEscolhido = selecionarItemLista(anos, "Ano");
        if (anoEscolhido == null) return;

        // 4. Detalhes Finais
        System.out.println("Buscando dados finais do veículo...");
        String jsonVeiculo = ApiService.get("/marcas/" + marcaEscolhida.getCodigo() +
                "/modelos/" + modeloEscolhido.getCodigo() +
                "/anos/" + anoEscolhido.getCodigo());

        Double valorFipe = ApiService.extrairValorFipe(jsonVeiculo);

        System.out.println("\n--- Veículo Encontrado ---");
        System.out.println("Marca: " + marcaEscolhida.getNome());
        System.out.println("Modelo: " + modeloEscolhido.getNome());
        System.out.println("Ano: " + anoEscolhido.getNome());
        System.out.println("Valor FIPE: " + Utils.doubleParaString(valorFipe));

        System.out.print("\nQual o valor PAGO na compra deste veículo? R$ ");
        Double valorCompra = lerDouble();

        System.out.print("Qual o valor DESEJADO para venda? R$ ");
        Double valorVenda = lerDouble();

        if (valorCompra > saldo) {
            System.out.println("Erro: Saldo insuficiente para realizar esta compra! Seu saldo é: " + Utils.doubleParaString(saldo));
            Utils.pausar(2);
            return;
        }

        // Processamento da Compra
        saldo -= valorCompra;
        Veiculo novoVeiculo = new Veiculo(marcaEscolhida.getNome(), modeloEscolhido.getNome(), anoEscolhido.getNome(), valorCompra, valorVenda, valorFipe);
        frota.add(novoVeiculo);

        System.out.println("Veículo comprado com sucesso! Saldo atualizado.");
        Utils.pausar(2);
    }

    private static void listarVeiculos() {
        System.out.println("\n--- LISTA DE VEÍCULOS ---");
        if (frota.isEmpty()) {
            System.out.println("A concessionária não possui veículos no momento.");
        } else {
            for (int i = 0; i < frota.size(); i++) {
                System.out.println("[" + i + "] " + frota.get(i));
            }
        }
        Utils.pausar(2);
    }

    private static void editarVeiculo() {
        System.out.println("\n--- EDITAR VEÍCULO ---");
        listarVeiculos();
        if (frota.isEmpty()) return;

        System.out.print("Digite o número do veículo que deseja editar: ");
        int index = lerInteiro();

        if (index >= 0 && index < frota.size()) {
            Veiculo v = frota.get(index);
            System.out.print("Digite o novo valor de venda para o " + v.getModelo() + ": R$ ");
            Double novoValor = lerDouble();
            v.setValorVenda(novoValor);
            System.out.println("Valor de venda atualizado com sucesso!");
        } else {
            System.out.println("Veículo não encontrado.");
        }
        Utils.pausar(1);
    }

    private static void venderVeiculo() {
        System.out.println("\n--- VENDER VEÍCULO ---");
        listarVeiculos();
        if (frota.isEmpty()) return;

        System.out.print("Digite o número do veículo para venda: ");
        int index = lerInteiro();

        if (index >= 0 && index < frota.size()) {
            Veiculo v = frota.get(index);
            System.out.println("Veículo selecionado: " + v.getModelo() + " | Valor de Venda Cadastrado: " + Utils.doubleParaString(v.getValorVenda()));

            System.out.println("1 - Vender pelo valor cadastrado (" + Utils.doubleParaString(v.getValorVenda()) + ")");
            System.out.println("2 - Vender por um valor diferente");
            System.out.print("Escolha: ");
            int opVenda = lerInteiro();

            Double valorFinalVenda = v.getValorVenda();

            if (opVenda == 2) {
                System.out.print("Digite o novo valor negociado: R$ ");
                valorFinalVenda = lerDouble();
            } else if (opVenda != 1) {
                System.out.println("Opção inválida. Venda cancelada.");
                Utils.pausar(1);
                return;
            }

            // Processamento da venda
            saldo += valorFinalVenda;
            Double lucroDestaVenda = valorFinalVenda - v.getValorCompra();
            lucroPrejuizoTotal += lucroDestaVenda;

            frota.remove(index);

            System.out.println("Veículo vendido com sucesso por " + Utils.doubleParaString(valorFinalVenda) + "!");
            if (lucroDestaVenda >= 0) {
                System.out.println("Lucro nesta operação: " + Utils.doubleParaString(lucroDestaVenda));
            } else {
                System.out.println("Prejuízo nesta operação: " + Utils.doubleParaString(lucroDestaVenda));
            }
        } else {
            System.out.println("Veículo não encontrado.");
        }
        Utils.pausar(2);
    }

    private static void exibirSaldo() {
        System.out.println("\n--- DADOS FINANCEIROS ---");
        System.out.println("Saldo Disponível (Caixa): " + Utils.doubleParaString(saldo));

        if (lucroPrejuizoTotal >= 0) {
            System.out.println("Lucro Total Acumulado: " + Utils.doubleParaString(lucroPrejuizoTotal));
        } else {
            System.out.println("Prejuízo Total Acumulado: " + Utils.doubleParaString(lucroPrejuizoTotal));
        }

        Double patrimonioEmVeiculos = 0.0;
        for (Veiculo v : frota) {
            patrimonioEmVeiculos += v.getValorCompra();
        }
        System.out.println("Patrimônio investido (Custo): " + Utils.doubleParaString(patrimonioEmVeiculos));
        System.out.println("Patrimônio Geral da Loja (Saldo + Pátio): " + Utils.doubleParaString(saldo + patrimonioEmVeiculos));
        Utils.pausar(3);
    }

    // --- MÉTODOS AUXILIARES ---

    private static FipeItem selecionarItemLista(List<FipeItem> lista, String nomeLista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhum dado encontrado para " + nomeLista + ".");
            return null;
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("[" + i + "] " + lista.get(i).getNome());
        }
        System.out.print("Escolha o número do(a) " + nomeLista + " desejado(a): ");
        int index = lerInteiro();

        if (index >= 0 && index < lista.size()) {
            return lista.get(index);
        } else {
            System.out.println("Opção inválida.");
            return null;
        }
    }

    private static int lerInteiro() {
        try {
            return Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Retorna -1 para tratar como erro nas validações
        }
    }

    private static Double lerDouble() {
        try {
            return Double.parseDouble(input.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Considerando 0.0");
            return 0.0;
        }
    }
}
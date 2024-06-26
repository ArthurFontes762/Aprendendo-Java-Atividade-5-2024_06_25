import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteSQL clienteSQL = new ClienteSQL();

        clienteSQL.conectar(); // Conectar ao banco de dados

        List<Cliente> clientes = new ArrayList<>();
        System.out.println("Digite os clientes (digite 'fim' para finalizar):");

        // Pedir ao usuário para digitar clientes até que digite 'fim'
        while (true) {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            if (nome.equalsIgnoreCase("fim")) {
                break;
            }
            System.out.print("Idade: ");
            int idade = 0;
            try {
                idade = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (Exception e) {
                System.out.println("Erro ao ler idade. Insira um número inteiro válido.");
                scanner.nextLine(); // Limpar o buffer em caso de erro
                continue;
            }

            Cliente cliente = new Cliente(nome, idade);
            clientes.add(cliente);
        }

        clienteSQL.adicionarVarios(clientes); // Adicionar clientes ao banco de dados

        System.out.println("\nClientes maiores de idade:");
        clienteSQL.retornarMaioresDeIdade(); // Imprimir clientes maiores de idade

        System.out.println("\nSinalizando menores de idade...");
        clienteSQL.sinalizarMenoresDeIdade(); // Atualizar nomes dos clientes menores de idade

        System.out.println("\nClientes após sinalização:");
        clienteSQL.retornarMaioresDeIdade(); // Imprimir todos os clientes novamente

        System.out.println("\nLimpando clientes menores de idade...");
        clienteSQL.limparBancoDeDados(); // Remover clientes menores de idade do banco de dados

        System.out.println("\nClientes após limpeza:");
        clienteSQL.retornarMaioresDeIdade(); // Imprimir clientes restantes

        clienteSQL.fecharConexao(); // Fechar a conexão com o banco de dados ao final
        scanner.close(); // Fechar scanner
    }
}
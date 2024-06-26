import java.sql.*;
import java.util.List;

public class ClienteSQL {
    private Connection conexao;

    // Método para conectar ao banco de dados SQLite
    public void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection("jdbc:sqlite:Clientes.db");
            criarTabela(); // Verifica e cria a tabela se não existir
            System.out.println("Conexão realizada! Banco de dados: ");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC do SQLite não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }

    // Método para criar a tabela 'clientes' se não existir
    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, idade INTEGER NOT NULL)";
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabela criada ou já existe.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Método para adicionar vários clientes ao banco de dados
    public void adicionarVarios(List<Cliente> clientes) {
        String sql = "INSERT INTO clientes (nome, idade) VALUES (?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            conexao.setAutoCommit(false); // Iniciar transação
            for (Cliente cliente : clientes) {
                pstmt.setString(1, cliente.getNome());
                pstmt.setInt(2, cliente.getIdade());
                pstmt.addBatch(); // Adicionar ao batch
            }
            pstmt.executeBatch(); // Executar o batch
            conexao.commit(); // Confirmar a transação
            System.out.println("Clientes inseridos.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    // Método para retornar e imprimir clientes maiores de idade
    public void retornarMaioresDeIdade() {
        String sql = "SELECT nome, idade FROM clientes WHERE idade >= 18";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                System.out.println("Nome: " + nome + ", Idade: " + idade);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar clientes: " + e.getMessage());
        }
    }

    // Método para atualizar nomes de clientes menores de idade
    public void sinalizarMenoresDeIdade() {
        String sql = "SELECT id, nome, idade FROM clientes WHERE idade < 18";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome") + " (menor)";
                atualizarNomeCliente(id, nome);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar clientes: " + e.getMessage());
        }
    }

    // Método auxiliar para atualizar o nome de um cliente no banco de dados
    private void atualizarNomeCliente(int id, String novoNome) {
        String sql = "UPDATE clientes SET nome = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    // Método para limpar clientes menores de idade do banco de dados
    public void limparBancoDeDados() {
        String sql = "DELETE FROM clientes WHERE idade < 18";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    // Método para fechar a conexão com o banco de dados
    public void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
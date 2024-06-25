import java.sql.*; // Vai chamar a biblioteca toda do SQL
import java.util.ArrayList;
import java.util.List;

public class ClienteSQL {
    private Connection connection;

    // Método para conectar ao banco de dados SQLite
    public void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\LabInfo\\Downloads\\Aprendendo-Java-Atividade-5-2024_06_25\\Aula 5 - Banco de Dados Por Lotes Usando Laco De Repeticao\\banco.db");
            createTable(); // Isso vai criar uma tabela clientes se ainda não existir
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para criar a tabela "clientes" se não existir
    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "idade INTEGER NOT NULL)");
            connection.commit();
            System.out.println("Tabela criada ou já existe.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Método para adicionar vários clientes ao banco de dados
    public void adicionarVarios(List<Cliente> clientes) {
        String sql = "INSERT INTO clientes (nome, idade) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false); // Vai iniciar transação
            for (Cliente cliente : clientes) {
                pstmt.setString(1, cliente.getNome());
                pstmt.setInt(2, cliente.getIdade());
                pstmt.addBatch(); // Vai adicionar ao Batch
            }
            pstmt.executeBatch(); // Vai executar o Batch
            connection.commit(); // Commit(Confirma) da transação
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retornarMaioresDeIdade() {
        String sql = "SELECT nome, idade FROM clientes WHERE idade >= 18";
        try (Statement stmt = connection)
    }
}

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {

	/** Modulo de conexo **/
	// Parametros de conexao
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password = "Pablo1764@";

	// Metodos de conexao
	private Connection conectar() {
		Connection connection;

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	/** CRUD CREATE **/
	public void create(JavaBeans contato) {
		String insert = "insert into contatos(nome,fone,email) values (?, ?, ?)";

		try {
			// Abrir a conexao com o banco
			Connection con = conectar();

			// Preparar a query para execucao ao BD - JDBC
			PreparedStatement preparedStatement = con.prepareStatement(insert);

			// Subistituir as interrogacoes, ou seja, os parametros, pelo conteudo das
			// variaveis
			preparedStatement.setString(1, contato.getNome());
			preparedStatement.setString(2, contato.getFone());
			preparedStatement.setString(3, contato.getEmail());

			// Executar a query
			preparedStatement.executeUpdate();

			// Encerrar a conexao com o BD
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/** CRUD READ **/
	public ArrayList<JavaBeans> listarContatos() {
		// Criando um objeto para acessar a classe JB;
		ArrayList<JavaBeans> contatos = new ArrayList<JavaBeans>();

		String read = "select * from contatos order by idcon";

		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			// Executar laço
			while (rs.next()) {
				// Variaveis de apoio que recebem os dados do banco
				String idcon = rs.getString(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);

				// Armazenando o ArrayList
				contatos.add(new JavaBeans(idcon, nome, fone, email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
			return null;
		}
	}
	
	
	/* CRUD UPDATE */
	// Selecionar contato
	public void selecionarContato(JavaBeans contato) {
		String read2 = "select * from contatos where idcon = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read2);
			pst.setString(1, contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				//Setar as variaveis JavaBeans
				contato.setIdcon(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}
	
	/* CRUD EDTIAR CONTATO */
	public void alterarContato(JavaBeans contato) {
		String create = "update contatos set nome=?,fone=?,email=? where idcon=?";
		
		try {
			Connection connection = conectar();
			PreparedStatement pst = connection.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setString(4, contato.getIdcon());
			pst.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	/* CRUD DELETE */
	public void deletarContato(JavaBeans contato) {
		String delete = "delete from contatos where idcon=?";
		
		try {
			Connection connection = conectar();
			PreparedStatement pst = connection.prepareStatement(delete);
			pst.setString(1, contato.getIdcon());
			pst.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	

	// Teste de conexao
	public void testeConexao() {
		try {
			Connection connection = conectar();
			System.out.println(connection);
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

package controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.net.httpserver.Request;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main")) {
			contatos(request, response);
		} else if (action.equals("/insert")) {
			novoContato(request, response);
		} else if (action.equals("/select")) {
			listarContato(request, response);
		} else if (action.equals("/update")) {
			editarContato(request, response);
		} else if (action.equals("/delete")) {
			deletarContato(request, response);
		} else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		} else {
			response.sendRedirect("index.html");
		}
	}

	// Listar Contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.sendRedirect("agenda.jsp");
		// Criando um objeto que ira receber os dados JB;
		ArrayList<JavaBeans> lista = dao.listarContatos(); // Um objeto de nome lista e executa o método listar
															// contatos.

		// Teste de recebimento
		/*
		 * for (int i = 0; i < lista.size(); i++) {
		 * System.out.println(lista.get(i).getIdcon());
		 * System.out.println(lista.get(i).getNome());
		 * System.out.println(lista.get(i).getFone());
		 * System.out.println(lista.get(i).getEmail()); }
		 */

		// Encaminhar a lista ao documento agenda.jsp
		System.out.println("Lista retornada do DAO: " + lista);
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}

	protected void novoContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Setar as variaveis JB
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));

		// Invocar o metodo create passando o objeto contato
		dao.create(contato);

		// Redirecionar para o documento agenda.jsp
		response.sendRedirect("agenda.jsp");

		// Teste
		System.out.println("Nome: " + contato.getNome());
		System.out.println("Fone: " + contato.getFone());
		System.out.println("Email: " + contato.getEmail());
	}

	// Editar Contato
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recebimento do id do contato que sera editado
		String idcon = request.getParameter("idcon");
		System.out.println(idcon);

		// Setar a variavel JavaBeans
		contato.setIdcon(idcon);

		// Executar o metodo selecionarContato (DAO)
		dao.selecionarContato(contato);

		// Setar os atributos do formulario com o conteudo JavaBeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());

		// Encaminhar ao documento editando.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editando.jsp");
		rd.forward(request, response);

		// Teste de recebimento
		System.out.println(contato.getIdcon());
		System.out.println(contato.getNome());
		System.out.println(contato.getFone());
		System.out.println(contato.getEmail());
	}

	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Setar as variaveis JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));

		// Executar o metodo alterar contato
		dao.alterarContato(contato);

		// Redirecionar para o documento agenda.jsp (atualizando as alteracoes)
		response.sendRedirect("main");

		// Teste de recebimento
		System.out.println(request.getParameter("idcon"));
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("fone"));
		System.out.println(request.getParameter("email"));
	}

	private void deletarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recebimento do id co contato a ser excluido (validador.js)
		String idcon = request.getParameter("idcon");

		// Setar a variavel idcon JavaBeans
		contato.setIdcon(idcon);

		// Executar o metodo deletarContato (DAO) passando o objeto contato
		dao.deletarContato(contato);

		// Redirecionar para o documento agenda.jsp (atualizando as alteracoes)
		response.sendRedirect("main");

	}
	
	private void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document document = new Document();
		try {
			// Tipo de conteudo
			response.setContentType("application/pdf");
			
			// Nome do documento
			response.addHeader("Content-Disposition", "inline; filename=contatos.pdf");
			
			// Criar o documento
			PdfWriter.getInstance(document, response.getOutputStream());
			
			// Abrir o documento -> conteudo
			document.open();
			document.add(new Paragraph("Lista de contatos:"));
			document.add(new Paragraph(" "));
			
			// Criar uma tabela
			PdfPTable table = new PdfPTable(3);
			
			// Cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			table.addCell(col1);
			table.addCell(col2);
			table.addCell(col3);
			
			// Popular a tabela com os contatos do BD
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for(int i = 0; i < lista.size(); i++) {
				table.addCell(lista.get(i).getNome());
				table.addCell(lista.get(i).getFone());
				table.addCell(lista.get(i).getEmail());
			}
			
			document.add(table);
			
			document.close();
		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
			document.close();
		}
	}
}

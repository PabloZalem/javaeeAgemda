<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.JavaBeans"%>
<%@ page import="java.util.ArrayList"%>
<%
	ArrayList<JavaBeans> lista = (ArrayList<JavaBeans>) request.getAttribute("contatos");
/* for (int i = 0; i < lista.size(); i++) {
	 out.println(lista.get(i).getIdcon());
	 out.println(lista.get(i).getNome());
	 out.println(lista.get(i).getFone());
	 out.println(lista.get(i).getEmail());
} */
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<title>Agenda de contatos</title>
<link rel="icon" href="images/phone.png">
<link rel="stylesheet" href="style.css">
</head>
<body>
	<h1>Agenda de Contatos</h1>
	<a href="novo.html" class="Botao1">Novo contato</a>
	<script src="scripts/confirmador.js"></script>
	<table id="tabela">
		<thead>
			<tr>
				<th>Id</th>
				<th>Nome</th>
				<th>Fone</th>
				<th>E-mail</th>
				<th>Opções</th>
			</tr>
		</thead>
		<tbody>
			<% if (lista != null) { %>
			<% for (int i = 0; i < lista.size(); i++) {%>
			<tr>
				<td><%=lista.get(i).getIdcon()%></td>
				<td><%=lista.get(i).getNome()%></td>
				<td><%=lista.get(i).getFone()%></td>
				<td><%=lista.get(i).getEmail()%></td>
				<td><a href="select?idcon=<%=lista.get(i).getIdcon()%>" class="Botao1">Editar</a>
				<a href="javascript: confirmar(<%=lista.get(i).getIdcon()%>)" class="Botao2">Excluir</a>
				</td>
			</tr>
			<% } %>
			<%
			} else {
			%>
			<tr>
				<td colspan="4">Nenhum contato encontrado.</td>
			</tr>
			<% } %>
		</tbody>
	</table>
</body>
</html>
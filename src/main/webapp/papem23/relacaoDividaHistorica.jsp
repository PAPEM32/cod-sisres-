<%@ page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.sisres.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sisres.utilitaria.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.or/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Confirmar Dívida</title>

<jsp:include page="/importacoes.html" />

</head>
<body>
	<!-- Caso haja algum erro o struts substitui <html:errors /> -->
	<html:errors />

	<html:messages name="mensagemSucesso" id="message" message="true">
		<bean:message key="messages.header" />
		<bean:message key="messages.prefix" />
		<bean:write name="message" />
		<bean:message key="messages.suffix" />
		<bean:message key="messages.footer" />
	</html:messages>

	<!-- ImportaÃ§Ã£o do cabeÃ§alho e menu -->
	<jsp:include page="/default/cabecalho.jsp" />

	<!-- Formulário -->
	<!-- <form id="formCadastro" name="formCadastro">-->
	<div class="clear"></div>

	<div class="titulo">Confirmar Cadastro</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">

			<div class="subtitulo">Dívidas históricas em estado de espera</div>
			<% 
					ArrayList<Divida> listDividaHistorica = (ArrayList<Divida>)session.getAttribute("listDividaHistorica");
				if (listDividaHistorica!= null && !listDividaHistorica.isEmpty()) {
				%>
			<div class="corpoSubTitulo">
				<table class="displaytag" width="100%" border="0">
					<thead>
						<tr>
							<th>Matrícula Financeira</th>
							<th>período</th>
							<th>Nome</th>
							<th>Valor</th>
						</tr>
					</thead>
					<% 
				
					Iterator it = listDividaHistorica.iterator();
					int i = 0;
					String linha = "linha_" + i;
					String cssClass = null;
				   while(it.hasNext()) {
				     Divida divida = (Divida) it.next();
				     pageContext.setAttribute("divida",divida);
				     Pessoa pessoa = divida.getPessoa();
				     pageContext.setAttribute("pessoa",pessoa);
				     Lancamento lancamento = divida.getLancamentoInicial();
				     pageContext.setAttribute("lancamento",lancamento);
					
				     if(i%2==0)
				     {
				    	 cssClass = "odd";
				     }
				     else
				     {
				    	 cssClass = "even";
				     }
				     
					%>
					<tr id="<%= linha %>" class="<%=cssClass%> hand"
						onmouseover="$('<%= linha %>').morph('background:#cfe2f5',{duration:0})"
						onmouseout="$('<%= linha %>').morph('background:#f4f4f4',{duration:0,delay:0})"
						onclick="window.location.assign('confirmarDividaHistoricaDetalhe.do?index=<%= i %>')">
						<td>${pessoa.matFin}</td>
						<td>${divida.processoInicioStr}até
							${divida.processoTerminoStr}</td>


						<td>${pessoa.nome}</td>

						<% String DivValorEspera = "R$ " + Utilitaria.formatarValor("######0.00",divida.getValor())  ;%>

						<td><%=DivValorEspera%></td>
					</tr>
					<%
									i++;
									linha = "linha_" + i;
							        }
							        	
							    %>

				</table>
				<%
					}
					
				%>

			</div>

			<!-- <div class="barraBts">
					<a href="#">Cadastrar</a> <a href="javascript:document.getElementById('formCadastro').reset();">Limpar Formulário</a> 
				</div> -->
		</div>

	</div>
	<!-- </form> -->
	<!-- RodapÃ© -->
	<div class="clear"></div>
	<jsp:include page="/default/rodape.jsp" />

	<!-- fecha div #container -->
	<!-- fecha div #overall -->
</body>
<!-- Scripts que são ativados após o desenho do Body -->

<script>
	getDimensao();//captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
</script>
</html>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="com.sisres.model.OC"%>
<%@ page import="com.sisres.model.Motivo"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.struts.action.ActionErrors"%>

<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.sisres.utilitaria.Utilitaria"%>

<%@page import="com.sisres.utilitaria.Utilitaria"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Alterar senha</title>
<jsp:include page="/importacoes.html" />

<script>
	jQuery(document).ready( function(){
		jQuery("#limpForm").click(function(){
			jQuery("input").val("");
		});
	});	
</script>

</head>

<body>

<html:errors />
	<html:messages name="mensagemSucesso" id="message" message="true">
		<bean:message key="messages.header" />
		<bean:message key="messages.prefix" />
		<bean:write name="message" />
		<bean:message key="messages.suffix" />
		<bean:message key="messages.footer" />
	</html:messages>
	
	<jsp:include page="/default/cabecalho.jsp" />
	
	<!-- 	<div id="overall" class="overall"> -->
<!--  	<div id="container" class="container">  -->


<!-- 		<div id="cabecalho" class="cabecalho" style=""> -->
<!-- 			<div id="logo" class="logo"></div> -->
<!-- 		</div> -->

		<div class="clear"></div>

		<div class="titulo">&nbsp;Alterar Senha</div>

		<div id="conteudo" class="corpoTitulo"
			style="background-image: url('../imagens/linhas.png');">
			<div id="innerConteudo"
				style="background-image: url('../imagens/principal.png'); background-position: right; height: 100%;">
				<br /> <br /> <br /> <br />
				<div id="topo-carrinho">
					<br />
					<html:form action="/default/alterarSenha" method="post">
		                    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspSenha Atual:
		                    <html:password property="senhaAtual" />
						<br />
						<br />
                     		&nbsp&nbsp&nbsp&nbsp&nbsp&nbspDigitar nova senha:  
		                    <html:password property="novaSenha" />
						<br />
						<br />
		                    Confirmar nova senha:
		                    <html:password property="confirmaSenha" />
						<html:submit value="Alterar Senha" />
					</html:form>
					<br />
				</div>

			</div>

		</div>

		<!-- Rodapé -->
		<jsp:include page="/default/rodape.jsp" />
<!-- 	</div> -->
	<!-- 	</div> -->
	<script>
		getDimensao();//captura dimensão da tela do browser;
		ajustaContainer();//ajusta container para a largura mí­nima;
	</script>

</body>
</html>

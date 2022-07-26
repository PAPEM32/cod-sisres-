<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.sisres.model.*"%>
<%@page import="javax.swing.JFileChooser"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Importar dados da papeleta SIAPE</title>
<jsp:include page="/importacoes.html" />

</head>

<body>
	<%-- <jsp:useBean id="dao" class="com.sisres.dao.DAOPapeleta"></jsp:useBean> --%>
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
	<!--<form id="formRelatorioDividasOc" action="geraRelatorio.do"  method="post">-->
	<html:form action="/oc/geraRelatorio.do" method="post">
		<div class="clear"></div>

		<div class="titulo">Importar dados da papeleta SIAPE</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Dados da Papeleta</div>

				<div id="perDHistTitle" class="subtitulo">
					<table id="myTable" width="100%" border="0" class="displaytag">
						<tr>
							<td><b>CPF</b></td>
							<td><b>NOME</b></td>
							<td><b>NIP</b></td>
							<td><b>MATRICULA</b></td>
							<td><b>NÂ° PAPELETA</b></td>
							<td><b>DATA PAPELETA</b></td>
							<td><b>VALOR LÃQUIDO</b></td>
							<td><b>DATA FALECIMENTO</b></td>
							<td><b>Mês INICIO BLOQUEIO</b></td>
							<td><b>ANO INICIO BLOQUEIO</b></td>
							<td><b>Mês FIM BLOQUEIO</b></td>
							<td><b>ANO FIM BLOQUEIO</b></td>
							<td><b>MOTIVO</b></td>
							<td><b>PERÃODO DE BLOQUEIO</b></td>
							<td><b>BANCO</b></td>
							<td><b>AGÃNCIA</b></td>
							<td><b>CONTA CORRENTE</b></td>
						</tr>
					</table>
				</div>

				<div>
					<table>
						<%-- <c:if test="${empty papeletas }">
						<c:redirect url="mvc?logica=ListaPapeletaSIAPELogic" />
					</c:if> --%>
						<c:forEach var="papeleta" items="${dao.consulta}">
							<tr>
								<td>${papeleta.cpfpessoa}</td>
								<td>${papeleta.nomecpessoa}</td>
								<td><c:if test="${not empty papeleta.nip}">${papeleta.nip}</c:if>
									<c:if test="${empty papeleta.nip}">NÃ£o informado</c:if></td>
								<td>${papeleta.matricula}</td>
								<td>${papeleta.nropapeleta}</td>
								<td>${papeleta.dtemissaopapeleta}</td>
								<td>${papeleta.valorliquido}</td>
								<td>${papeleta.dtfalecimento}</td>
								<td>${papeleta.mes_ini_bloq}</td>
								<td>${papeleta.ano_ini_bloq}</td>
								<td>${papeleta.mes_fim_bloq}</td>
								<td>${papeleta.ano_fim_bloq}</td>
								<td>${papeleta.nomemotivo}</td>
								<td>${papeleta.periodobloqueio}</td>
								<td>${papeleta.banco}</td>
								<td>${papeleta.agencia}</td>
								<td>${papeleta.conta}</td>
							</tr>
						</c:forEach>

					</table>

				</div>
				<div class="barraBts">

					<input type="submit" value="Importar Papeleta">
				</div>
			</div>
		</div>

		<!-- </form> -->
	</html:form>
	<!-- RodapÃ© -->
	<div class="clear"></div>
	<jsp:include page="/default/rodape.jsp" />

</body>
<!-- Scripts que são ativados após o desenho do Body -->
<script>
	getDimensao();//captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
	maskDo();
	init();
	jQuery("#myTable").tablesorter({
		widthFixed : true,
		headers : {
			0 : {
				sorter : false
			}
		}
	}).tablesorterPager({
		container : jQuery("#pager")
	});
	function consultar() {
		JFileChooser
		filechoose = new JFileChooser();
		filechoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		filechoose.showOpenDialog(null);
		session.setAttribute("caminho do arquivo", filechoose.getSelectedFile()
				.toString());
	}
</script>
</html>

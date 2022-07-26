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
<%-- <jsp:useBean id="dao" class="com.sisres.dao.DAOPapeleta" /> --%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Importar dados da papeleta SIAPE</title>
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
	<!--<form id="formRelatorioDividasOc" action="geraRelatorio.do"  method="post">-->
	<html:form action="/oc/geraRelatorio.do" method="post">
		<div class="clear"></div>

		<div class="titulo">Importar dados da papeleta SIAPE</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Dados da Papeleta</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsultaPapeleta">
							<td id="linha_num_papeleta" style="width: 125px;"
								title="N&uacute;mero da Papeleta" class="branco">N&uacute;mero
								da Papeleta</td>
							<td id="input_num_papeleta" title="N&uacute;mero da Papeleta">
								<input id="numeroPapeleta" name="numeroPapeleta"
								style="width: 70px;" maxlength="9" type="text" validate="numero" />
							</td>
						</tr>
						<tr class="formConsultaPapeleta">
							<td id="linha_data_papeleta" style="width: 125px;"
								title="Data de Emissão" class="branco">Data de Emissão</td>
							<td id="input_data_papeleta" title="data"><input
								id="dataPapeleta" name="dataPapeleta" style="width: 70px;"
								maxlength="10" type="text" validate="diamesano" /></td>
						</tr>
					</table>
				</div>
				<div class="barraBts">
					<input type="submit" value="Consultar Papeleta"><br>
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
</script>
</html>

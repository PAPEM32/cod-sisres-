<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Gerar Arquivo de Dívidas SIPM</title>
<jsp:include page="/importacoes.html" />

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
	<!-- ImportaÃ§Ã£o do cabeÃ§alho e menu -->
	<jsp:include page="/default/cabecalho.jsp" />
	<!-- Formulário -->
	<form id="formArquivoDividasSIPM" action="geraArquivo.do" method="post">
		<div class="clear"></div>

		<div class="titulo">Gerar Arquivo de Dívidas SIPM</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo"></div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 200px;">Mês/Ano *</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 150px;"><input id="mesAno" name="mesAno"
								style="width: 155px;" value="" maxlength="7" type="text"
								validate="mesano" /></td>

							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<!-- <tr class="formConsulta">
						<td id="linha_doc_data" title="Campo requerido" class="branco" style="width:200px;">Caminho *</td>
							<td id="input_doc_data" title="Campo requerido" style="width:200px;">
						<input id="caminho" name="Caminho" style=" width : 192px; height : 20px;" value="" type="file"  />
						
						</td>
						
						</tr> -->

					</table>


					<div class="barraBts">
						<% Object arquivoGerado =  session.getAttribute("arquivoGerado");
						if ( (arquivoGerado != null)  && ((Boolean)arquivoGerado == true)){ 
						     session.removeAttribute("arquivoGerado");
						%>
						<!-- <a href="../sipm/ArquivoSIPM.zip">Clique aqui para baixar arquivo</a> -->

						<%} else {%>
						<a
							href="javascript:document.getElementById('formArquivoDividasSIPM').submit();">Gerar
							Arquivo</a>
						<%} %>
					</div>
				</div>
	</form>
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
	jQuery("#myTable").tablesorter({widthFixed: true,headers: { 0: { sorter: false}}}).tablesorterPager({container: jQuery("#pager")});
</script>
</html>

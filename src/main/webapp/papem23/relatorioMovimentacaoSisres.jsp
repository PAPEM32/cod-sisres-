<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.sisres.model.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Emitir Relatório de Movimentação do SISRES</title>

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
	<div>
		<div>
			<!-- Formulário -->
			<html:form action="/papem23/geraRelatorioMovimentacao.do"
				method="post">
				<div class="clear"></div>

				<div class="titulo">Emitir Relatório de Movimentação do SISRES
				</div>
				<div id="conteudo" class="corpoTitulo">
					<div id="innerConteudo">
						<div class="subtitulo">Dados do Relatório</div>
						<div class="corpoSubTitulo">
							<table width="100%" border="0">
								<tr class="formConsulta">
									<td id="linha_opcao" title="Campo requerido" class="branco"
										style="width: 120px;">Opção *</td>
									<td id="input_opcao" title="Campo requerido"
										style="width: 200px;"><input id="radiodetalhado"
										type="radio" name="detalhado" class="radioRack" value="D" />Por
										OC <input id="radiototais" type="radio" name="totalGeral"
										class="radioRack" value="T" /> Total Geral</td>
									<!-- <td id="linha_doc_data" title="Campo requerido" class="branco" style="width:90px;">Mês/Ano *</td> -->
									<td id="input_doc_data" title="Campo requerido"
										style="width: 150px;" maxlenght="7"><html:text
											name="relatorioMovimentacaoSisresForm" property="mesAno"
											value="${param.mesAno}"></html:text></td>
									<td style="border: 1px solid #fff;">&nbsp;</td>
								</tr>
								<tr class="formConsulta" style="display: none;">
									<td id="linha_opcao" title="Campo requerido" class="cinzaE"
										style="width: 120px;">Responsável</td>
									<td id="input_opcao" title="Campo requerido" class="cinza"
										style="width: 200px; font-weight: normal;">&nbsp;</td>
									<td style="border: 1px solid #fff;">&nbsp;</td>
								</tr>
							</table>
						</div>
						<div class="subtitulo">Ordenador de Despesas</div>
						<div class="corpoSubTitulo">
							<table width="100%" border="0">
								<tr class="formMovimentacaoSisres">
									<td id="linha_nome_ord" class="branco" style="width: 60px;">Nome
										*</td>
									<td id="input_nome_ord" style="width: 500px;" colspan='3'>
										<html:text name="relatorioMovimentacaoSisresForm"
											property="ordenador" value="${param.ordenador}"
											style="width:525px;" maxlength="60"></html:text>
									</td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
								<tr class="formMovimentacaoSisres">
									<td id="linha_posto_ord" title="Campo requerido" class="branco"
										style="width: 60px;">Posto *</td>
									<td id="input_posto_ord" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm" property="ptOrdenador"
											value="${param.ptOrdenador}" style="width:224px;"
											maxlength="2">
										</html:text></td>

									<td id="linha_F_ord" title="Campo requerido" class="branco"
										style="width: 50px;">Função *</td>
									<td id="input_F_ord" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm"
											property="funcOrdenador" value="${param.funcOrdenador}"
											style="width:224px;" maxlength="">
										</html:text></td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
							</table>
						</div>

						<div class="subtitulo">Agente Fiscal</div>
						<div class="corpoSubTitulo">
							<table width="100%" border="0">
								<tr class="formMovimentacaoSisres">
									<td id="linha_nome_ag" class="branco" style="width: 60px;">Nome
										*</td>
									<td id="input_nome_ag" style="width: 500px;" colspan='3'>
										<html:text name="relatorioMovimentacaoSisresForm"
											property="agente" value="${param.agente}"
											style="width:525px;" maxlength="60">
										</html:text>
									</td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
								<tr class="formMovimentacaoSisres">
									<td id="linha_posto_ag" title="Campo requerido" class="branco"
										style="width: 60px;">Posto *</td>
									<td id="input_posto_ag" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm" property="ptAgente"
											value="${param.ptAgente}" style="width:224px;" maxlength="2">
										</html:text></td>
									<td id="linha_F_ag" title="Campo requerido" class="branco"
										style="width: 50px;">Função *</td>
									<td id="input_F_ag" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm" property="funcAgente"
											value="${param.funcAgente}" style="width:224px;" maxlength="">
										</html:text></td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
							</table>
						</div>

						<div class="subtitulo">Oficial Encarregado</div>
						<div class="corpoSubTitulo">
							<table width="100%" border="0">
								<tr class="formMovimentacaoSisres">
									<td id="linha_nome_ofE" class="branco" style="width: 60px;">Nome
										*</td>
									<td id="input_nome_ofE" style="width: 500px;" colspan='3'>
										<html:text name="relatorioMovimentacaoSisresForm"
											property="encarregado" value="${param.encarregado}"
											style="width:525px;" maxlength="60"></html:text>
									</td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
								<tr class="formMovimentacaoSisres">
									<td id="linha_posto_ofE" title="Campo requerido" class="branco"
										style="width: 60px;">Posto *</td>
									<td id="input_posto_ofE" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm"
											property="ptEncarregado" value="${param.ptEncarregado}"
											style="width:224px;" maxlength="2">
										</html:text></td>
									<td id="linha_F_ofE" title="Campo requerido" class="branco"
										style="width: 50px;">Função *</td>
									<td id="input_F_ofE" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm"
											property="funcEncarregado" value="${param.funcEncarregado}"
											style="width:224px;" maxlength="">
										</html:text></td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
							</table>
						</div>

						<div class="subtitulo">Oficial Relator</div>
						<div class="corpoSubTitulo">
							<table width="100%" border="0">
								<tr class="formMovimentacaoSisres">
									<td id="linha_nome_ofR" class="branco" style="width: 60px;">Nome
										*</td>
									<td id="input_nome_ofR" style="width: 500px;" colspan='3'>
										<html:text name="relatorioMovimentacaoSisresForm"
											property="relator" value="${param.relator}"
											style="width:525px;" maxlength="60"></html:text>
									</td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
								<tr class="formMovimentacaoSisres">
									<td id="linha_posto_ofR" title="Campo requerido" class="branco"
										style="width: 60px;">Posto *</td>
									<td id="input_posto_ofR" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm" property="ptRelator"
											value="${param.ptRelator}" style="width:224px;" maxlength="2"></html:text></td>
									<td id="linha_F_ofR" title="Campo requerido" class="branco"
										style="width: 50px;">Função *</td>
									<td id="input_F_ofR" title="Campo requerido"
										style="width: 225px;"><html:text
											name="relatorioMovimentacaoSisresForm" property="funcRelator"
											value="${param.funcRelator}" style="width:224px;"
											maxlength=""></html:text></td>
									<td style="border: 1px solid #fff">&nbsp;</td>
								</tr>
							</table>
						</div>
						<div class="barraBts">
							<html:submit>Emitir Relatório</html:submit>
						</div>
					</div>
				</div>
			</html:form>

			<!-- RodapÃ© -->
			<div class="clear"></div>
			<jsp:include page="/default/rodape.jsp" />
		</div>
		<!-- fecha div #container -->
	</div>
	<!-- fecha div #overall -->

</body>

<!-- Scripts que são ativados após o desenho do Body -->
<script>
	getDimensao();//captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
	init();
	maskDo();
	//jQuery("#myTable").tablesorter({widthFixed: true,headers: { 0: { sorter: false}}}).tablesorterPager({container: jQuery("#pager")});
</script>

</html>
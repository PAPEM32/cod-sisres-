<%@ page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.sisres.model.*"%>
<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.sisres.utilitaria.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.or/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Confirmar ImportaÃ§Ã£o de Dívida</title>

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
	<%
   
	Divida divida = (Divida)session.getAttribute("divida");
	pageContext.setAttribute("divida",divida);
	System.out.println(divida.getId());
	Pessoa pessoa = divida.getPessoa();
	pageContext.setAttribute("pessoa",pessoa);
	Lancamento lancamento = divida.getLancamentoInicial();
	pageContext.setAttribute("lancamento",lancamento);
	
	//String SdataDocAutorizacao = lancamento.getDataDocAutorizacao().toString();
	DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
	String SdataDocAutorizacao = formatador.format(lancamento.getDataDocAutorizacao());
	String SdataDocEnvio = formatador.format(divida.getDataDocEnvio());
	String SdataMotivo = formatador.format(divida.getDataMotivo());
	
%>

	<!-- Formulário -->
	<form id="formConfirmaDivHist" name="formConfirmaDivHist"
		action="confirmarDividaHistorica.do" method="post">

		<div class="clear"></div>

		<div class="titulo">Confirmar Cadastro - Detalhamento da
			D&iacute;vida Hist&oacute;rica</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Informações sobre a Dívida</div>

				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">

							<td id="linha_cod_divida" class="cinzaE" style="width: 100px;">C&oacute;digo
								da D&iacute;vida</td>
							<td id="input_cod_divida" class="cinza" style="width: 60px;">
								<input id="codDividaD" name="codDividaD" style="width: 100px;"
								value="${divida.codigo}" type="text" readonly="readonly"
								disabled="disabled" /> <input id="codDivida" name="codDivida"
								value="${divida.codigo}" type="hidden" />
							</td>
							<td id="matricula" class="cinzaE" style="width: 120px;">Matrícula
								Financeira</td>
							<td id="input_SISPAG" class="cinza" style="width: 80px;"><input
								id="matFinD" name="matFinD" style="width: 100px;"
								value="${pessoa.matFin}" maxlength="7" type="text"
								readonly="readonly" disabled="disabled" /> <input id="matFin"
								name="matFin" value="${pessoa.matFin}" type="hidden" /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>

						</tr>
					</table>
				</div>
				<div id="perDHistTitle" class="subtitulo" style="">período da
					Dívida Histórica</div>
				<div id="perDHistBody" class="corpoSubTitulo" style="">
					<table width="100%" border="0">
						<tr class="formConsulta">

							<td id="linha_inicioDivHist" class="cinzaE" style="width: 50px;">Início</td>
							<td id="input_inicioDivHist" class="cinza" style="width: 60px;">
								<input id="processoDataInicioD" name="processoDataInicioD"
								style="width: 60px;" value="${divida.processoInicioStr}"
								maxlength="7" type="text" readonly="readonly"
								disabled="disabled" /> <input id="processoDataInicio"
								name="processoDataInicio" value="${divida.processoInicioStr}"
								type="hidden" />
							</td>

							<td id="linha_terminoDivHist" class="cinzaE" style="width: 50px;">Término</td>
							<td id="input_terminoDivHist" class="cinza" style="width: 60px;">
								<input id="processoDataTerminoD" name="processoDataTerminoD"
								style="width: 60px;" value="${divida.processoTerminoStr}"
								maxlength="7" type="text" readonly="readonly"
								disabled="disabled" /> <input id="processoDataTermino"
								name="processoDataTermino" value="${divida.processoTerminoStr}"
								maxlength="7" type="hidden" />
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>

					</table>
				</div>
				<div class="subtitulo">Informações Básicas</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_nome" class="cinzaE" style="width: 60px;">Nome</td>
							<td id="input_nome" colspan="3" class="cinza"
								style="width: 250px;" width="466"><input id="nomeD"
								name="nomeD" style="width: 423px;" value="${pessoa.nome}"
								maxlength="60" type="text" disabled="disabled" /> <input
								id="nome" name="nome" value="${pessoa.nome}" type="hidden" /></td>
							<td id="linha_cpf" class="cinzaE" style="width: 60px;">CPF</td>



							<td id="input_cpf" class="cinza" style="width: 60px;" width="221">
								<input id="cpfD" name="cpfD" style="width: 200px;"
								value="${pessoa.cpf}" maxlength="11" type="text"
								disabled="disabled" /> <input id="cpf" name="cpf"
								value="${pessoa.cpf}" type="hidden" />
							</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_posto" class="cinzaE" style="width: 60px;">Posto</td>
							<td id="input_posto" class="cinza" style="width: 60px;"
								width="253"><input id="postoD" name="postoD"
								style="width: 268px;" value="${pessoa.posto}" maxlength="2"
								type="text" disabled="disabled" /> <input id="posto"
								name="posto" value="${pessoa.posto}" type="hidden" /></td>

							<td id="linha_om" class="cinzaE" style="width: 60px;">Situação</td>
							<td id="input_om" class="cinza" style="width: 100px;">
								<% if(pessoa.getSituacao().equals("P")) { %> <input id="situacaoD"
								name="situacaoD" style="width: 250px;" value="PENSIONISTA"
								maxlength="" type="text" disabled="disabled" /> <input
								id="situacao" name="situacao" value="PENSIONISTA" maxlength=""
								type="hidden" /> <%}else if (pessoa.getSituacao().equals("A")) { %>
								<input id="situacaoD" name="situacaoD" style="width: 250px;"
								value="ATIVO" maxlength="" type="text" disabled="disabled" /> <input
								id="situacao" name="situacao" value="ATIVO" maxlength=""
								type="hidden" /> <%}else { %> <input id="situacaoD"
								name="situacaoD" style="width: 250px;" value="INATIVO"
								maxlength="" type="text" disabled="disabled" /> <input
								id="situacao" name="situacao" value="INATIVO" maxlength=""
								type="hidden" /> <%} %> <!-- 							<input id="situacaoD" name="situacaoD" style="width:250px;" value="${pessoa.situacao}" maxlength="" type="text" disabled="disabled" />
								<input id="situacao" name="situacao" value="${pessoa.situacao}" maxlength="" type="hidden"  />   -->
							</td>

							<td id="linha_oc" class="cinzaE" style="width: 60px;">OC</td>
							<td id="input_oc" class="cinza" style="width: 60px;" width="120">
								<input id="oc_NomeD" style="width: 300px;" name="ocNomeD"
								value="${divida.oc.nome}" maxlength="" type="text"
								disabled="disabled" /> <input id="oc_Nome" name="ocNome"
								value="${divida.oc.nome}" type="hidden" />
							</td>

							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="subtitulo">Informações Bancárias</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_banco" class="cinzaE" style="width: 90px;">Banco</td>
							<td id="input_banco" class="cinza" style="width: 80px;"><input
								id="bancoD" name="bancoD" style="width: 100px;"
								value="${divida.banco}" maxlength="7" type="text"
								disabled="disabled" /> <input id="banco" name="banco"
								value="${divida.banco}" type="hidden" /></td>

							<td id="linha_agencia" class="cinzaE" style="width: 60px;">Agência</td>
							<td id="input_agencia" class="cinza" style="width: 80px;"><input
								id="agenciaD" name="agenciaD" style="width: 95px;"
								value="${divida.agencia}" maxlength="4" type="text"
								disabled="disabled" /> <input id="agencia" name="agencia"
								value="${divida.agencia}" type="hidden" /></td>
							<td id="linha_cc" class="cinzaE" style="width: 100px;">Conta-Corrente</td>
							<td id="input_cc" class="cinza" style="width: 80px;"><input
								id="ccD" name="contaCorrenteD" style="width: 115px;"
								value="${divida.contaCorrente}" maxlength="11" type="text"
								disabled="disabled" /> <input id="cc" name="contaCorrente"
								value="${divida.contaCorrente}" type="hidden" /></td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_valor_divida" class="cinzaE" style="width: 90px;">Valor
								da Dívida</td>
							<td id="input_valor_divida" class="cinza" style="width: 80px;">

								<% String DivValorConf = Utilitaria.formatarValor("######0.00",divida.getValor());%>

								<input id="imput_valor_dividaD" name="valorDividaStrD"
								style="width: 100px;" value="<%=DivValorConf %>" maxlength="15"
								type="text" disabled="disabled" /> <input
								id="imput_valor_divida" name="valorDividaStr"
								value="<%=DivValorConf %>" type="hidden" /> <!-- 	<input id="imput_valor_dividaD" name="valorDividaStrD" style="width:100px;" value="${divida.valor}" maxlength="15" type="text" disabled="disabled"/>
							<input id="imput_valor_divida" name="valorDividaStr" value="${divida.valor}" type="hidden" />  -->


								<td style="border: 1px solid #fff" colspan="5">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="subtitulo">Documento Autorizando Inclusão da
					Dívida</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_origem_doc_envio" class="cinzaE"
								style="width: 50px;">Origem</td>
							<td id="input_origem_doc_envio" class="cinza" style="width: 60px";">
								<input id="origemDocAut" name="origemDocAut"
								style="width: 95px;" value="${lancamento.origemDocAutorizacao}"
								maxlength="10" type="text" validade="numero" readonly="readonly" />
							</td>
							<td style="border: 1px solid #fff;" colspan="5">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_tipo_doc" class="cinzaE" style="width: 50px;">Tipo</td>
							<td id="input_tipo_doc" class="cinza" style="width: 60px;">
								<input id="tipoDocAut" name="tipoDocAut" style="width: 95px;"
								value="${lancamento.tipoDocAutorizacao}" maxlength="10"
								type="text" readonly="readonly" />
							</td>

							<td id="linha_numero_doc" class="cinzaE" style="width: 60px;">N&uacute;mero</td>
							<td id="input_numero_doc" class="cinza" style="width: 40px;">
								<input id="numero_doc" name="numeroDocAut" style="width: 200px;"
								value="${lancamento.numeroDocAutorizacao}" maxlength="50"
								type="text" readonly="readonly" />
							</td>

							<td id="linha_data_doc" class="cinzaE" style="width: 50px;">Data</td>
							<td id="input_data_doc" class="cinza" style="width: 80px;">
								<input id="data_doc" name="dataDocAut" style="width: 95px;"
								value=<%=SdataDocAutorizacao %> maxlength="10" type="text"
								validate="diamesano" readonly="readonly" />
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>

					</table>
				</div>

				<div class="subtitulo">Documento de Envio das Informações
					Cadastradas pelo SIPM</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_tipo_doc_sipm" class="cinzaE" style="width: 50px;">Tipo</td>
							<td id="input_tipo_doc_sipm" class="cinza" style="width: 60px;">
								<input id="tipo_doc_sipm" name="tipoDocEnvio"
								style="width: 95px;" value="${divida.docEnvio}" maxlength="10"
								type="text" readonly="readonly" />
							</td>

							<td id="linha_numero_doc_sipm" class="cinzaE"
								style="width: 60px;">N&uacute;mero</td>
							<td id="input_numero_doc_sipm" class="cinza" style="width: 40px;">
								<input id="numero_doc_sipm" name="numeroDocEnvio"
								style="width: 95px;" value="${divida.numeroDocEnvio}"
								maxlength="10" type="text" validate="numero" readonly="readonly" />
							</td>

							<td id="linha_data_doc_sipm" class="cinzaE" style="width: 50px;">Data</td>
							<td id="input_data_doc_sipm" class="cinza" style="width: 80px;">
								<input id="data_doc_sipm" name="dataDocEnvio"
								style="width: 95px;" value=<%=SdataDocEnvio%> maxlength="10"
								type="text" validate="diamesano" readonly="readonly" />
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>

					</table>
				</div>

				<div class="subtitulo">Informações Complementares</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">

							<td id="linha_data_Motivo" class="cinzaE" style="width: 90px;">Data
								do motivo</td>
							<td id="input_data_Motivo" class="cinza" style="width: 80px;">
								<input id="data_MotivoD" name="dataMotivoD"
								style="width: 105px;" value=<%=SdataMotivo %> maxlength="10"
								type="text" disabled="disabled" /> <input id="data_Motivo"
								name="dataMotivo" value=<%=SdataMotivo %> type="hidden" />
							</td>

							<td id="linha_motivo" class="cinzaE" style="width: 80px;">Motivo</td>
							<td id="input_motivo" class="cinza" style="width: 300px"><input
								type="text" id="motivoD" name="motivoD" style="width: 400px"
								maxlength="50" value="${divida.motivo}" disabled="disabled" />
								<input type="hidden" id="idMotivo" name="idMotivo"
								value="${divida.idMotivo}" /> <input type="hidden" id="motivo"
								name="motivo" value="${divida.motivo}" /></td>

							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>

					</table>
				</div>
				<!--
				<div class="subtituloEnfase">
				Informações de ConfirmaÃ§Ã£o
				</div>
				<div class="corpoSubTituloEnfase">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_doc_autorizacao" title="Campo requerido" class="branco" style="width:200px;">Tipo de Doc. de ConfirmaÃ§Ã£o *</td>
							<td id="input_doc_autorizacao" title="Campo requerido" style="width:150px;">
								<input id="doc_autorizacao" style="width:150px;" value="" maxlength="10" type="text"/>
							</td>
							<td id="linha_doc_autorizacao" title="Campo requerido" class="branco" style="width:150px;">Número do Documento *</td>
							<td id="input_doc_autorizacao" title="Campo requerido" style="width:150px;">
								<input id="doc_autorizacao" style="width:150px;" value="" maxlength="10" type="text"/>
							</td>
					
							<td id="linha_fato_gerador" title="Campo requerido" class="branco" style="width:180px;">Data da ConfirmaÃ§Ã£o *</td>
							<td id="input_fato_gerador" title="Campo requerido" style="width:100px;">
								<input id="fato_gerador" style="width:95px;" value="dd/mm/aaaa" maxlength="7" type="text"/>
							</td>
							<td style="border:1px solid #fff;">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_motivo" title="Campo requerido" class="branco" style="width:200px;">CertidÃ£o de Ãbito *</td>
							<td id="input_motivo" title="Campo requerido" style="width:510px" colspan="5">
								<input type="text" id="motivo" style="width:510px" maxlength="50">
							</td>
						</tr>
					</table>
				</div>-->
				<div class="barraBts">

					<a
						href="javascript:document.getElementById('formConfirmaDivHist').submit();">Confirmar
						Dívida</a>

					<!--   <a href="javascript:history.go(-1)">Voltar</a>  -->
					<a href="../papem23/confirmarDividaHistoricaEmEspera.do">Voltar</a>
					<!-- Comentado por 3SG Wallace Roque
          <a href="javascript:document.getElementById('formConfirmaDivHist').reset();">Limpar Formulário</a> 
          -->
				</div>
			</div>
		</div>
	</form>
	<!-- RodapÃ© -->
	<div class="clear"></div>
	<div class="rodape">&nbsp;</div>
	<!-- fecha div #container -->
	<!-- fecha div #overall -->
</body>
<!-- Scripts que são ativados após o desenho do Body -->
<script>
    maskDo();
	getDimensao();    //captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
</script>
</html>

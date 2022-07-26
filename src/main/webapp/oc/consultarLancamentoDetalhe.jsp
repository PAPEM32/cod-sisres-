
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">

<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.OC"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.sisres.utilitaria.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8" />
<title>SisRes - Estornar - Detalhe do lanÃ§amento</title>

<jsp:include page="/importacoes.html" />

<script>
	jQuery(document).ready( function(){
		jQuery("#divEstorno").hide();
		
		jQuery("#mostraForm").click(function() {
			jQuery("#divEstorno").show();
			jQuery(window).scrollTo( '#pontoestornar', { duration:500, axis:'y'});
		});
	});	
</script>
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
	<form id="formEstorno" action="../papem23/estornoReversao.do"
		method="get">
		<div class="clear"></div>

		<div class="titulo">Detalhe do Lançamento</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Informações sobre o Lançamento</div>

				<div class="corpoSubTitulo">
					<table width="100%" border="0">

						<% 
					Lancamento lancamento = (Lancamento)session.getAttribute("lancamento");
            		pageContext.setAttribute("lancamento",lancamento);
            		//session.setAttribute("lancamento",lancamento);
            		Divida divida = (Divida)session.getAttribute("divida");
            		pageContext.setAttribute("divida",divida);                		
            		Pessoa pessoa = divida.getPessoa();
            		pageContext.setAttribute("pessoa",pessoa);
            		
					   //Lancamento lancamento = (Lancamento)session.getAttribute("lancamento2");
					   //Divida divida = (Divida)session.getAttribute("divida");
					   //Pessoa pessoa = divida.getPessoa();
					   OC oc = divida.getOc();
					   String origem = "";
					   if (divida.getCgs()=="S"){
						  origem = "SISPAG";
					   }else{
						  origem = "SIAFE"; 
					   }
					   
					   
					   
					   String LancValor = Utilitaria.formatarValor("######0.00",lancamento.getValor());
					   
					   DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
                       
		                String FdataLancamento = formatador.format(lancamento.getDataLancamento());
		                String FdataDocAutorizacao = formatador.format(lancamento.getDataDocAutorizacao());
							      
					 %>
						<tr class="formConsulta">
							<td id="linha_tipo" class="cinzaE" style="width: 120px;">Tipo</td>
							<td class="cinza" style="width: 250px; font-weight: normal;"
								colspan="3">
								<!-- 						${lancamento.tipo}	</td> -->
								${lancamento.tipoLancNome}
							</td>
							<td style="border: 1px solid #fff;" colspan="3">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_code" class="cinzaE" style="width: 120px;">Origem
								da Reversão</td>
							<td class="cinza" style="font-weight: normal; width: 250px;"
								colspan="3">${lancamento.origemDocAutorizacao}</td>
							<td style="border: 1px solid #fff;" colspan="3">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_code" class="cinzaE" style="width: 120px;">Código</td>
							<td class="cinza" style="width: 100px; font-weight: normal;">
								${lancamento.codigo}</td>
							<td id="responsavel" class="cinzaE" style="width: 60px;">Responsável</td>
							<td class="cinza" style="width: 70px; font-weight: normal;">
								${lancamento.responsavel}</td>
							<td id="data" class="cinzaE" style="width: 50px;">Data</td>
							<td class="cinza" style="width: 120px; font-weight: normal;">
								<%=FdataLancamento %>
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>

					</table>
				</div>

				<div class="subtitulo">Informações sobre Documento de
					Autorização</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">

						<tr class="formConsulta">
							<td id="linha_doc_numero" class="cinzaE" style="width: 60px;">Número</td>
							<td class="cinza" style="width: 60px; font-weight: normal;">
								${lancamento.numeroDocAutorizacao}</td>

							<td id="linha_doc_tipo" class="cinzaE" style="width: 60px;">Tipo</td>
							<td id="input_doc_tipo" class="cinza"
								style="width: 90px; font-weight: normal;">
								${lancamento.tipoDocAutorizacao}</td>
							<td id="doc_data" class="cinzaE" style="width: 60px;">Data</td>
							<td class="cinza" style="width: 90px; font-weight: normal;">
								<%=FdataDocAutorizacao %>
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="subtitulo">Informações da Ordem Bancária</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_banco" class="cinzaE" style="width: 60px;">Banco</td>
							<td id="input_banco" class="cinza"
								style="width: 100px; font-weight: normal;">${divida.banco}
							</td>

							<td id="linha_agencia" class="cinzaE" style="width: 60px;">Agência</td>
							<td id="input_agencia" class="cinza"
								style="width: 100px; font-weight: normal;">
								${divida.agencia}</td>
							<td id="linha_cc" class="cinzaE" style="width: 100px;">Conta-Corrente</td>
							<td id="input_cc" class="cinza"
								style="width: 100px; font-weight: normal;">
								${divida.contaCorrente}</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_valor" class="cinzaE" style="width: 60px;">Valor</td>
							<td id="input_valor" class="cinza"
								style="width: 100px; font-weight: normal;"><%=LancValor %>
							</td>
							<td id="linha_ope" class="cinzaE" style="width: 60px;">Operador</td>
							<td id="input_ope" class="cinza"
								style="width: 100px; font-weight: normal;">${lancamento.operador}</td>
							<td style="border: 1px solid #fff;" colspan="3">&nbsp;</td>
						</tr>
					</table>
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_valor" class="cinzaE" style="width: 120px;">Observações</td>
							<td id="input_valor" class="cinza"
								style="width: 600px; font-weight: normal; white-space: normal;">
								${divida.motivo}</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="subtitulo">Informações sobre a Dívida</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_tipo_divida" class="cinzaE" style="width: 60px;">Tipo</td>
							<td class="cinza" style="width: 90px; font-weight: normal;">
								${divida.tipo}</td>

							<td style="border: 1px solid #fff;">&nbsp;</td>

						</tr>
						<tr class="formConsulta">

							<%if(divida.getCgs().equals("S")){%>
							<td id="matricula" class="cinzaE" style="width: 120px;">Matrícula
								Financeira</td>
							<td id="input_SISPAG" class="cinza"
								style="width: 100px; font-weight: normal;">
								${pessoa.matFin}</td>
							<td class="cinzaE" style="width: 120px;">Sistema de Origem</td>
							<td class="cinza" style="width: 100px; font-weight: normal;">
								SISPAG</td>
							<%}else {%>
							<td id="matricula" class="cinzaE" style="width: 120px;">Matrícula
								SIAPE</td>
							<td id="input_SISPAG" class="cinza"
								style="width: 100px; font-weight: normal;">
								${pessoa.matSIAPE}</td>
							<td class="cinzaE" style="width: 120px;">Sistema de Origem</td>
							<td class="cinza" style="width: 100px; font-weight: normal;">
								SIAPE</td>
							<%}  %>
						</tr>

						<tr class="formConsulta">

							<td id="linha_inicioDivHist" class="cinzaE" style="width: 60px;">Início</td>
							<td id="input_inicioDivHist" class="cinza"
								style="width: 90px; font-weight: normal;">-</td>

							<td id="linha_terminoDivHist" class="cinzaE" style="width: 80px;">Término</td>
							<td id="input_terminoDivHist" class="cinza"
								style="width: 90px; font-weight: normal;">-</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>

						</tr>
					</table>
					<br />
					<table width="2%" border="0">
						<tr class="formConsulta">
							<td id="linha_nome" class="cinzaE" style="width: 60px;">Nome</td>
							<td id="input_nome" class="cinza"
								style="width: 600px; font-weight: normal;" colspan="7">
								${pessoa.nome}</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_cpf" class="cinzaE" style="width: 80px;">CPF</td>
							<td id="input_cpf" class="cinza"
								style="width: 100px; font-weight: normal;">${pessoa.cpf}</td>
							<td id="linha_posto" class="cinzaE" style="width: 60px;">Posto</td>
							<td id="input_posto" class="cinza"
								style="width: 150px; font-weight: normal;">${pessoa.posto}
							</td>
							<td id="linha_oc" class="cinzaE" style="width: 60px;">OC</td>
							<td id="input_oc" class="cinza"
								style="width: 250px; font-weight: normal;">${divida.oc.oc}
							</td>

							<td id="linha_om" class="cinzaE" style="width: 80px;">Situação</td>
							<td id="input_om" class="cinza"
								style="width: 70px; font-weight: normal;">
								${pessoa.situacao}</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>
				<div class="subtitulo">Observações sobre o Lançamento</div>
				<div class="corpoSubTitulo" style="padding-left: 15px;">
					${lancamento.observacao}</div>
				<div id="pontoestornar" class="barraBts">
					<% if (request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR")){%>
					<a href="javascript:void(0)" id="mostraForm">Estornar
						Lançamento</a>
					<!-- <a href="javascript:window.location.assign('UC-08-incluirLancamentoReversao.htm')">Incluir Lançamento de Reversão</a> -->
					<%}//fecha condiÃ§Ã£o sobre o perfil%>
					<a href="javascript:history.go(-1)">Voltar</a>
				</div>
				<% if (request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR")){%>
				<div id="divEstorno">
					<div class="subtituloEnfase">Estornar Lançamento de Reversão
					</div>
					<div class="corpoSubTituloEnfase">
						<table width="100%" border="0">
							<tr class="formConsulta">
								<td id="linha_motivo" title="Campo requerido" class="branco"
									style="width: 200px;">Motivo *</td>
								<td id="input_motivo" title="Campo requerido"
									style="width: 150px;" colspan="3"><textarea id="motivo"
										name="motivo" style="width: 480px; overflow: auto;" rows="11">${param.motivo}</textarea>
								</td>
								<td style="border: 1px solid #fff;">&nbsp;</td>
							</tr>
						</table>
					</div>
					<div class="barraBts">
						<a
							href="javascript:document.getElementById('formEstorno').submit();">Confirmar</a>
					</div>
				</div>
				<!-- fecha div#divEstorno -->
				<%}//fecha condiÃ§Ã£o sobre o perfil %>
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
	//jQuery('#pane-target').stop().scrollTo( target, { duration:500, axis:'y'});
	jQuery(window).scrollTo( '#pontoestornar', { duration:500, axis:'y'});
</script>
</html>

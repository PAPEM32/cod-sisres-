<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="com.sisres.utilitaria.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sisres.model.*"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.PrintWriter"%>


<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>

<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%//@taglib uri="http://java.sun.com/jsf/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<%-- <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br"> --%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>SisRes - Consultar Dívida</title>

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
	<!-- <form id="formConsulta" action="consultarDividaAction.do" method="get"> -->
	<div class="clear"></div>

	<div class="titulo">Consultar Dívida</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">
			<!-- <form id="formCadastro" name="formCadastro"> -->
			<div class="subtitulo">Informações sobre a Dívida</div>
			<form id="formConsulta" action="consultarDividaAction.do"
				method="get">
				<input type="hidden" name="pagina" value="" />
				<input type="hidden" name="registrosPorPagina" value="" />

				<%

// Criado uma dÃ­vida na session para persistir dos dados do filtro nas pÃ¡ginas subsequentes
    Pessoa pessoa = null;
    Pedido pedido = null;
    
	Divida dividaFiltro = (Divida) session.getAttribute("dividaFiltro");
	
	if(dividaFiltro != null){    
		 pessoa = dividaFiltro.getPessoa();
		 pedido = dividaFiltro.getPedido();
	}// teste para verificar nova funcionalidade 
	
	//Motivo
   String dataMotivo = "";
	String motivo = "";
   // motivo = dividaFiltro.getMotivo();
   // if (dividaFiltro.getDataMotivo() != null) 
    //		dataMotivo = dividaFiltro.getDataMotivo().toString();
    
	
    
%>


				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">

							<td id="linha_cod_divida" title="C&oacute;digo da D&iacute;vida"
								class="branco" style="width: 100px;">C&oacute;digo da
								D&iacute;vida</td>
							<td id="input_cod_divida" title="C&oacute;digo da D&iacute;vida"
								style="width: 80px;"><input id="cod_divida"
								name="codDivida" style="width: 125px;" maxlength="7" type="text"
								validate="numero"
								<% if((dividaFiltro != null) && (dividaFiltro.getCodigo()!= 0)){ %>
								value="<%=dividaFiltro.getCodigo()%>" <%}else{ %> value=""
								<%} %> /></td>

							<td id="linha_tipo_divida" title="Tipo da d&iacute;vida"
								class="branco" style="width: 90px;">Tipo</td>
							<td title="Tipo da d&iacute;vida" style="width: 130px;"><select
								id="tipo_divida" name="tipoDivida">
									<option value="">-</option>
									<%if(dividaFiltro != null){   %>
									<option value="<%=dividaFiltro.getTipo()%>" selected="selected"><%=dividaFiltro.getTipo()%></option>
									<%}%>

									<option id="tipoMes" value="Mês"
										onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
										Mês</option>
									<option id="tipoHist" value="Histórica"
										onclick="$('perDHistTitle').appear();$('perDHistBody').appear();$('docEnvioTitle').appear();$('docEnvioBody').appear();">
										Histórica</option>
									<option id="tipoRespareg" value="RESPAREG"
										onclick="$('perDHistTitle').fade({duration:0.3}); $('perDHistBody').fade({duration:0.3}); $('docEnvioTitle').fade(); $('docEnvioBody').fade();">
										RESPAREG</option>
							</select>

								<td id="linha_estado" title="Estado" class="branco"
								style="width: 90px;">Estado</td>
								<td title="Estado" style="width: 130px;"><select
									id="statusDivida" name="statusDivida">
										<option value="">-</option>
										<!--    <%if(dividaFiltro != null){   %>
       	<option value="<%=dividaFiltro.getEstado()%>" selected="selected"><% if(dividaFiltro.getEstado().equals("CONCLUIDO")){%> CONFIRMADO
       	                                                                  <% }else{%><%=dividaFiltro.getEstado()%><%}%>
       	</option>
     <%}%> -->
										<option id="estadoespera" value="EM ESPERA"
											onclick="$('perDHistTitle').fade({duration:0.3}); $('perDHistBody').fade({duration:0.3}); $('docEnvioTitle').fade(); $('docEnvioBody').fade();">
											EM ESPERA</option>
										<option id="estadoconfirmado" value="CONCLUIDO"
											onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
											CONFIRMADO</option>
								</select>
                                                                                                                           
                                                                            
						</tr>

						<tr class="formConsulta">
							<td id="linha_sistema_origem" title="Origem da Dívida Histórica"
								class="branco" style="width: 80px;">Origem</td>
							<td title="Origem da Dívida Histórica" style="width: 130px;">
								<input id="radioSIAPE" type="radio" name="sistemaOrigem"
								value="N" class="radioRack"
								<%if ((dividaFiltro != null) && dividaFiltro.getCgs().equals("N")) {%>
								checked="checked" <%}%> /> SIAPE <input id="radioSISPAG"
								type="radio" name="sistemaOrigem" value="S" class="radioRack"
								<%if ((dividaFiltro != null) && dividaFiltro.getCgs().equals("S")) {%>
								checked="checked" <%}%> /> SISPAG
							</td>
							<td id="matr_financeira" title="Matrícula Financeira"
								class="branco" style="width: 120px;">Matrícula Financeira</td>
							<td id="input_SIAPE" title="Matrícula Financeira"
								style="width: 90px;"><input id="matrfin" name="matfin"
								style="width: 120px;" maxlength="9" type="text"
								validate="numero" <%if (dividaFiltro != null) {%>
								value="<%=dividaFiltro.getPessoa().getMatFin()%>" <%} else {%>
								value="" <%}%> />
								<td id="ped_rever" title="Pedido de Reversão" class="branco"
								style="width: 90px;">Pedido de Reversão</td>
								<td id="input_SIAPE" title="Pedido de Reversao"
								style="width: 90px;"><input id="pedrev"
									name="pedidoReversao" style="width: 140px;" maxlength="9"
									type="text" <%if (pedido != null) {%>
									value="<%=dividaFiltro.getPedido().getCodigo()%>" <%} else {%>
									value="" <%}%> /></td>
								<td>(Para selecionar dí­vidas sem pedido de Reversão
									preencha com 0)</td> <!--<td title="Origem da Dívida Histórica" style="width:150px;">
									<input id="radioSIAPE" type="radio" name="sistema_origem" class="radioRack" onclick="$('m_SISPAG').hide();$('input_SISPAG').hide();$('m_SIAPE').appear();$('input_SIAPE').appear();"> SIAPE <input id="radioSISPAG" type="radio" name="sistema_origem" class="radioRack" onclick="$('m_SIAPE').hide();$('input_SIAPE').hide();$('m_SISPAG').appear();$('input_SISPAG').appear();"> SISPAG
									<script>$('radioSISPAG').checked = "";$('radioSIAPE').checked = "checked";</script>
								</td>
								<td id="m_SIAPE" title="Matrícula do SIAPE" class="branco" style="width:120px;">Matr. do SIAPE</td>
								<td id="input_SIAPE" title="Matrícula do SIAPE" style="width:150px;">
									<input id="SIAPE" style="width:150px;" value="" maxlength="7" type="text"/>
								</td>
							
								<td id="m_SISPAG" title="Matrícula do SISPAG" class="branco" style="width:120px;display:none;">Matr. do SISPAG</td>
								<td id="input_SISPAG" title="Matrícula do SISPAG" style="width:150px;display:none;">
									<input id="SISPAG" style="width:150px;" value="" maxlength="7" type="text"/>
								</td>
              -->
								<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">Nome</td>
							<td id="input_nome" title="Nome" style="width: 300px;"
								colspan="3"><input id="nome" name="nome"
								style="width: 390px;" maxlength="100" type="text"
								<%if (dividaFiltro != null) {%>
								value="<%=dividaFiltro.getPessoa().getNome()%>" <%} else {%>
								value="" <%}%> /></td>


							<td id="linha_estado" title="Estado" class="branco"
								style="width: 90px;">Valor</td>
							<td title="Estado" style="width: 130px;"><select
								id="statusDivida" name="opvalor">
									<option value="">-</option>
									<%
										if (session.getAttribute("opValor") != null) {
									%>
									<option value="<%=session.getAttribute("opValor")%>"
										selected="selected"><%=session.getAttribute("opValor")%>
									</option>
									<%
										}
									%>
									<option id="estadoespera" value="="
										onclick="$('perDHistTitle').fade({duration:0.3}); $('perDHistBody').fade({duration:0.3}); $('docEnvioTitle').fade(); $('docEnvioBody').fade();">
										=</option>
									<option id="estadoconfirmado" value=">"
										onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
										></option>
									<option id="estadoconfirmado"
										value="<"  	
           onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
										<</option>
							</select> <input id="pvalor" name="pvalor" style="width: 120px;"
								type="text" validate="numero"
								<%if (session.getAttribute("pValor") != null) {%>
								value=" <%=session.getAttribute("pValor")%>" <%} else {%>
								value="" <%}%> /></td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_cpf" title="cpf" class="branco"
								style="width: 100px;">CPF</td>
							<td id="input_cpf" title="cpf" style="width: 300px;"
								colspan="3"><input id="cpf" name="cpf"
								style="width: 390px;" maxlength="100" type="text"  <%if (dividaFiltro != null) {%>
								value="<%=dividaFiltro.getPessoa().getCpf()%>" <%} else {%>
								value="" <%}%>  /></td>
								
							<!-- <td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">OC</td>
							<td id="input_nome" title="oc" style="width: 300px;"
								colspan="3"><input id="oc" name="oc"
								style="width: 390px;" maxlength="100" type="text" /></td>
								
							<td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">Motivo</td>
							<td id="input_nome" title="oc" style="width: 300px;"
								colspan="3"><input id="motivo" name="motivo"
								style="width: 390px;" maxlength="100" type="text" /></td>
								
							<td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">Estado</td>
							<td id="input_nome" title="oc" style="width: 300px;"
								colspan="3"><input id="motivo" name="estado"
								style="width: 390px;" maxlength="100" type="text" /></td> -->
								
						       
                                                                
                                                                
								
						<!--	<td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">Valor Atual</td>
							<td id="input_nome" title="oc" style="width: 300px;"
								colspan="3"><input id="motivo" name="vatual"
								style="width: 390px;" maxlength="100" type="text" /></td>
								
							<td id="linha_nome" title="Nome" class="branco"
								style="width: 100px;">Pedido de Reversão</td>
							<td id="input_nome" title="oc" style="width: 300px;"
								colspan="3"><input id="motivo" name="reversao"
								style="width: 390px;" maxlength="100" type="text" /></td> -->
						</tr>
					</table>
				</div>


				<div class="subtitulo">Informações complementares:</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td class="branco" id="linha_motivo" style="width: 60px;"
								title="Campo requerido">Motivo</td>
							<td colspan="" id="motivo" style="width: 300px"
								title="Campo requerido">
								<!-- 			<input type="text" id="motivo" name="motivo" style="width:300px" maxlength="50" value="${param.motivo}" />
 --> <select id="motivo" name="motivo" style="width: 300px;">
									<option value="">-</option>
									<%
										if (session.getAttribute("listaMotivo") != null) {
											ArrayList<Motivo> listaMotivo = (ArrayList<Motivo>) session.getAttribute("listaMotivo");
									%>
									<%
										for (Motivo motivoDivida : listaMotivo) { //loop lista de motivo
												if (motivoDivida != null) {
									%>
									<option value="<%=motivoDivida.getNome()%>"
										<%if (motivoDivida.getNome().equals(motivo)) {
							out.print(" selected");
						}%>><%=motivoDivida.getNome()%></option>
									<%
										} //fecha if 
											} // fecha loop lista de motivo
									%>
									<%
										} //fecha if teste de request
									%>
							</select>
							</td>
						</tr>
					</table>
				</div>



				<div class="subtitulo">Documento Autorizando Inclusão da
					Dívida</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_origem_doc_envio" title="Campo requerido"
								class="branco" style="width: 100px;">OC de Origem</td>
							<td id="input_origem_doc_envio" title="Campo requerido"
								style="width: 100px;" colspan="3"><select
								id="tipo_origem_doc_envio" name="origemDocInclusao"
								style="width: 300px;">
									<option value="">-</option>

									<%
										if (session.getAttribute("listaOc") != null) {
											ArrayList<OC> listaOc = (ArrayList<OC>) session.getAttribute("listaOc");
									%>
									<%
										for (OC ocDividaOrigem : listaOc) {
												if (listaOc != null) {
									%>
									<option value="<%=ocDividaOrigem.getOc()%>"
										<%if (ocDividaOrigem.getOc().equals(ocDividaOrigem)) {
							out.print(" selected");
						}%>><%=ocDividaOrigem.getOc() + " - " + ocDividaOrigem.getNome()%></option>

									<%
										}
											} // fecha loop lista de motivo
									%>
									<%
										} //fecha if teste de request
									%>
							</select> <!-- input id="tipo_origem_doc_envio" name = "origemDocInclusao" value="" maxlength=3" type="text" validate="numero" /-->
							</td>
							<td style="border: 1px solid #fff;" colspan="3">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_tipo_doc" title="Campo requerido" class="branco"
								style="width: 100px;">Tipo</td>
							<td id="input_tipo_doc" title="Campo requerido"
								style="width: 100px;"><select id="tipo_doc"
								name="tipoDocInclusao" style="width: 100px;">
									<option value="">-</option>
									<%
										if (dividaFiltro != null && !dividaFiltro.getLancamentoInicial().getTipoDocAutorizacao().equals("")) {
									%>
									<option
										value="<%=dividaFiltro.getLancamentoInicial().getTipoDocAutorizacao()%>"
										selected="selected"><%=dividaFiltro.getLancamentoInicial().getTipoDocAutorizacao()%></option>
									<%
										}
									%>
									<option value="Ofí­cio">Ofí­cio</option>
									<option value="Mensagem">Mensagem</option>
									<option value="CI">CI</option>
									<option value="CP">CP</option>
									<option value="Papeleta">Papeleta</option>
									<option value="OS">OS</option>
							</select></td>

							<td id="linha_numero_doc" title="Campo requerido" class="branco"
								style="width: 60px;">N&uacute;mero</td>
							<td id="input_numero_doc" title="Campo requerido"
								style="width: 100px;"><input id="numero_doc"
								name="numDocInclusao" style="width: 200px;" maxlength="50"
								type="text" <%if (dividaFiltro != null) {%>
								value="<%=dividaFiltro.getLancamentoInicial().getNumeroDocAutorizacao()%>"
								<%} else {%> value="" <%}%> /></td>

							<td id="linha_data_doc" title="Campo requerido" class="branco"
								style="width: 60px;">Data</td>
							<td id="input_data_doc" title="Campo requerido"
								style="width: 100px;"><input id="data_doc"
								name="dtDocInclusao" style="width: 95px;" maxlength="10"
								type="text" validate="diamesano"
								<%if ((dividaFiltro != null) && (dividaFiltro.getLancamentoInicial().getDataDocAutorizacao() != null)) {
				System.out.println(
						" data do doc autorizacao" + dividaFiltro.getLancamentoInicial().getDataDocAutorizacao());
				DateFormat dfD = new SimpleDateFormat("MM/dd/yyyy");
				System.out.println(dfD.format(dividaFiltro.getLancamentoInicial().getDataDocAutorizacao()));%>
								value="<%=dfD.format(dividaFiltro.getLancamentoInicial().getDataDocAutorizacao())%>"
								<%} else {%> value="" <%}%> /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<%
							session.removeAttribute("dividaFiltro");
						%>
					</table>

				</div>

				<!-- <div class="subtitulo">Informações Complementares</div>-->
				<!-- <div class="corpoSubTitulo">
<table width="100%" border="0">
	<tr class="formConsulta">
		<!-- <td id="linha_pedido_reversao" class="branco" style="width:200px;">Pedido
		de reversão gerado?</td>
		<td id="input_pedido_reversao" name = "pedidoReversao" style="width:120px;"><input
			id="radiopedidosim" type="radio" 
			name="pedidoReversao" value="pedidoReversao_Sim"class="radioRack" onclick=""> Sim <input id="radiopedidonao"
			type="radio" name="pedidoReversao" checked="checked" value="nao" class="radioRack"/> NÃ£o </td>

		<td id="linha_tipo_saldo" title="Campo requerido" class="branco"
			style="width:100px;">Tipo de Saldo</td>
		<td id="input_tipo_saldo" name = "tipoSaldo" title="Campo requerido"
			style="width:100px;"><select>
			<option>------</option>
			<option>Saldo menor que zero</option>
			<option>Saldo maior que zero</option>
			<option>Saldo igual a zero</option>
			<option>Qualquer Saldo</option>
		</select></td>
		<td style="border:1px solid #fff;">&nbsp;</td>
	</tr>
</table>
</div>  -->
				<div class="barraBts">
					<a href="javascript:consultar('filtro');">Filtrar</a>
					<!-- <a href="javascript:document.getElementById('formConsulta').reset();"></a>-->
					<a
						href="javascript:clear_form_elements(document.getElementById('formConsulta'));">Limpar
						Formulário</a>
				</div>
			</form>
			<%
				ArrayList<Divida> listDivida = (ArrayList<Divida>) session.getAttribute("listDivida");
				if (listDivida != null && !listDivida.isEmpty()) {
					request.setAttribute("listDivida", listDivida);
			%>
			<div class="subtituloEnfase">Dívidas encontradas:</div>
			<display:table name="listDivida" pagesize="15" class="displaytag"
				id="listaDeDividas">
				<display:column property="id" sortable="true" title="ID"
					href="../oc/consultarDividaDetalhe.do" paramId="index"
					paramProperty="id" style="text-align:center" />
				<display:column property="pessoa.matFin" sortable="true"
					title="Matrícula Financeira" style="text-align:center" />
				<display:column property="pessoa.matSIAPE" sortable="true"
					title="Matrícula SIAPE" style="text-align:center" />
				<display:column property="pessoa.nome" sortable="true" title="Nome"
					style="text-align:center" />
				<display:column property="pessoa.cpf" title="CPF"
					style="text-align:center" />
				<display:column property="oc.nome" title="OC"
					style="text-align:center" />
				<display:column property="motivo" title="Motivo"
					style="text-align:center" />
				<display:column property="tipo" title="Tipo da Dívida"
					style="text-align:center" />	
	Tipo da divida
	<display:column title="Estado" style="text-align:center">
					<%
						if (((Divida) pageContext.getAttribute("listaDeDividas")).getEstado().equals("CONCLUIDO")) {
					%>
	      CONFIRMADO
	      <%
						} else {
					%>
	     EM ESPERA
	    <%
						}
					%>
				</display:column>
				<display:column title="período" style="text-align:center">
					<%
						String periodo;
									if (((Divida) pageContext.getAttribute("listaDeDividas")).getMesTermino() != null
											|| ((Divida) pageContext.getAttribute("listaDeDividas")).getAnoTermino() != null) {
										periodo = ((Divida) pageContext.getAttribute("listaDeDividas")).getMesProcessoGeracao()
												+ "/"
												+ ((Divida) pageContext.getAttribute("listaDeDividas")).getAnoProcessoGeracao()
												+ " até " + ((Divida) pageContext.getAttribute("listaDeDividas")).getMesTermino()
												+ "/" + ((Divida) pageContext.getAttribute("listaDeDividas")).getAnoTermino();
									} else {
										periodo = ((Divida) pageContext.getAttribute("listaDeDividas")).getMesProcessoGeracao()
												+ "/"
												+ ((Divida) pageContext.getAttribute("listaDeDividas")).getAnoProcessoGeracao();
									}
					%>
					<%=periodo%>
				</display:column>
				<display:column property="valor" title="Valor Atual"
					format="R$  {0,number,0.00}" style="text-align:right" />
				<display:column property="pedido.codigo" title="Pedido de Reversão"
					style="text-align:center" />
			</display:table>



			<div class="corpoSubTituloEnfase">


				<%
					}
				%>

			</div>

			<%
				//}
			%>
			<!-- RodapÃ© -->
		</div>
		<div class="clear"></div>
		<jsp:include page="/default/rodape.jsp" />

		<!-- fecha div #container -->
		<!-- fecha div #overall -->
</body>
<!-- Scripts que são ativados após o desenho do Body -->
<script>
	getDimensao(); //captura dimensão da tela do browser;
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

	function consultar(paginacao) {

		//		document.getElementById('formConsulta').registrosPorPagina.value = document.getElementById('registrosPorPagina').options[document.getElementById('registrosPorPagina').selectedIndex].value;

		//		if(paginacao == "filtro") {
		//			document.getElementById('formConsulta').pagina.value = 1;
		//		} else if(paginacao == "anterior" && parseInt(document.getElementById('paginaAtual').value) > 1) {
		//			document.getElementById('formConsulta').pagina.value = parseInt(document.getElementById('paginaAtual').value) - 1;
		//		} else if(paginacao == "proximo" && parseInt(document.getElementById('paginaAtual').value) < parseInt(document.getElementById('paginaTotal').value)) {

		//				  			document.getElementById('formConsulta').pagina.value = parseInt(document.getElementById('paginaAtual').value) + 1;
		//						}
		//		else if(paginacao == "primeiro") {
		//			document.getElementById('formConsulta').pagina.value = 1;
		//		}
		//		else if(paginacao == "ultimo") {
		//			document.getElementById('formConsulta').pagina.value = parseInt(document.getElementById('paginaTotal').value);
		//		}

		document.getElementById('formConsulta').submit();
	}

	function clear_form_elements(ele) {

		tags = ele.getElementsByTagName('input');
		for (i = 0; i < tags.length; i++) {
			switch (tags[i].type) {
			case 'text':
				tags[i].value = '';
				break;
			case 'radio':
				tags[i].checked = false;
				break;
			}
		}

		tags = ele.getElementsByTagName('select');
		for (i = 0; i < tags.length; i++) {
			if (tags[i].type == 'select-one') {
				tags[i].selectedIndex = 0;
			} else {
				for (j = 0; j < tags[i].options.length; j++) {
					tags[i].options[j].selected = false;
				}
			}
		}
		//	    var tableRef = document.getElementById('myTable');
		//	    while ( tableRef.rows.length > 0 )
		//	    {
		//	     tableRef.deleteRow(0);
		//	    }

	}
</script>


</html>

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
<%@ page import="com.sisres.utilitaria.Utilitaria"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%//@taglib uri="http://java.sun.com/jsf/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8" />
<title>SisRes - Alterar Dívida do Mês do SISPAG</title>
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


	<%				
			  //OC
			   String ocCodigo = "";
			   String ocCodigoOrigem;	
			   String ocNome = "";
			   String motivo = "";		
			   String dataMotivo = "";			   
 
				Divida divida = (Divida)session.getAttribute("divida");
				divida.setTipo(divida.getTipo());
				System.out.println("Tipo divida do jsp:  " +divida.getTipo());

				

				   OC ocDivida = divida.getOc();
				   if (ocDivida != null){
				    	ocCodigo= ocDivida.getOc();        
				      	ocNome= ocDivida.getNome();     }  

				      //Motivo
				      motivo = divida.getMotivo();
				      if (divida.getDataMotivo() != null) 
				      		dataMotivo = divida.getDataMotivo().toString();
				      				      
				      System.out.println("Motivo do jsp:  " +divida.getMotivo());
				   
				if(divida.getTipo().equals("Histórica ")) {
					divida.setDocEnvio(divida.getDocEnvio());
					divida.setDataDocEnvio(divida.getDataDocEnvio());
					divida.setNumeroDocEnvio(divida.getNumeroDocEnvio()); }	

								
				Pessoa pessoa = divida.getPessoa();
				Lancamento lancamento = divida.getLancamentoInicial();
				ocCodigoOrigem = lancamento.getOrigemDocAutorizacao();
				pageContext.setAttribute("pessoa",pessoa);
				
            	String FDataDocEnvio = ""; 
            	
                DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");             	
            	
                if(divida.getDataDocEnvio() != null){
                    FDataDocEnvio = formatador.format(divida.getDataDocEnvio());
                 }				
				
				session.setAttribute("matFin", pessoa.getMatFin());
				
				
				String numeroDocAutorizacao = divida.getLancamentoInicial().getNumeroDocAutorizacao();
				String tipo_Divida = divida.getTipo();
								
				String filtro = (String) session.getAttribute("filtro");
				if( (filtro != null) && (filtro.equals("true"))){
					// pega a dÃ­vida retornada na consulta
					Divida dividaFiltro = (Divida)session.getAttribute("dividaItem");
										
					divida.setBanco(dividaFiltro.getBanco());
					divida.setAgencia(dividaFiltro.getAgencia());
					divida.setContaCorrente(dividaFiltro.getContaCorrente());
					divida.setPessoa(dividaFiltro.getPessoa());
					divida.setOc(divida.getOc());
					divida.setTipo(divida.getTipo());
					System.out.println("Pessoa do jsp:  " +divida.getPessoa().getId());
					System.out.println("Tipo divida do jsp:  " +divida.getTipo());
					pessoa = dividaFiltro.getPessoa();
					
					//session.setAttribute("matFin", dividaFiltro.getPessoa().getMatFin().trim());
					
					// limpa atributos da session					
					session.removeAttribute("filtro");
					session.removeAttribute("dividaItem");
		//			System.out.println("Tipo divida do jsp:  " +divida.getTipo());					
				}
				

                %>

<!-- Caso haja algum erro o struts substitui <html:errors /> -->



	<!-- ImportaÃ§Ã£o do cabçalho e menu -->
	<jsp:include page="/default/cabecalho.jsp" />
	<div class="clear"></div>

	<% if(divida.getTipo().equals("Mês ")) {%>
	<%if(divida.getCgs().equals("S")){%>
	<div class="titulo">Alterar D&iacute;vida do M&ecirc;s do SISPAG</div>
	<%}else{ %>
	<div class="titulo">Alterar D&iacute;vida do M&ecirc;s do SIAPE</div>
	<%} %>
	<%}else{ %>
	<%if(divida.getCgs().equals("S")){%>
	<div class="titulo">Alterar D&iacute;vida Hist&oacute;rica do
		SISPAG</div>
	<%} else { %>
	<div class="titulo">Alterar D&iacute;vida Hist&oacute;rica do
		SIAPE</div>
	<%} %>
	<%}	%>

	<div class="subtitulo">Informações sobre a Dívida</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">
			<tr class="formConsulta">

				<td id="linha_id_divida" class="cinzaE" style="width: 120px;">ID</td>
				<td id="input_id_divida" class="cinza" style="width: 60px;">
					${divida.id}</td>
				<td id="linha_cod_divida" class="cinzaE" style="width: 120px;">C&oacute;digo
					da D&iacute;vida</td>
				<td id="input_cod_divida" class="cinza" style="width: 60px;">
					${divida.codigo}</td>

				<td id="linha_tipo_divida" class="cinzaE" style="width: 50px;">Tipo</td>
				<td class="cinza" style="width: 60px;">${divida.tipo}</td>
				<form id="alterarDividaMesSispagForm" method="post"
					action="../oc/AlterarDividaAction.do">
					<td class="nodisplay"><input type="text" name="tipoDivida"
						maxlength="60" value="<%=tipo_Divida%>" readonly="readonly" /></td>

					<!-- 								<input  class="cinza" name="tipo_divida" maxlength="60" value="<%=divida.getTipo()%>" readonly="readonly" type="hidden">								
								</td>    <%=divida.getTipo()%>"  /></td>   -->

					<!--<td id="linha_sistema_origem" class="cinzaE" style="width:50px;">Origem</td>
								<td class="cinza" style="width:60px;">
									${divida.oc}
								</td>-->

					<td style="border: 1px solid #fff;">&nbsp;</td>
			</tr>
			<tr class="formConsulta">
				<td id="matricula" class="cinzaE" style="width: 120px;">Matrícula
					Financeira</td>
				<%if(divida.getCgs().equals("S")){%>
				<td id="input_SISPAG" class="cinza"
					style="width: 100px; font-weight: normal;">${pessoa.matFin}</td>
				<td class="cinzaE" style="width: 120px;">Sistema de Origem</td>
				<td class="cinza" style="width: 100px; font-weight: normal;">
					SISPAG</td>
				<%}else {%>
				<td id="input_SISPAG" class="cinza"
					style="width: 100px; font-weight: normal;">${pessoa.matSIAPE}
				</td>
				<td class="cinzaE" style="width: 120px;">Sistema de Origem</td>
				<td class="cinza" style="width: 100px; font-weight: normal;">
					SIAPE</td>
				<%}	%>
				<td id="estado" class="cinzaE" style="width: 120px;">Estado</td>
				<td id="input_SISPAG" class="cinza"
					style="width: 100px; font-weight: normal;">
					<% if(divida.getEstado().equals("CONCLUIDO")) { %> CONFIRMADO <%}else{ %>
					EM ESPERA <%} %>

				</td>
			</tr>
		</table>
	</div>

	<div id="perDHistTitle" class="subtitulo" style="">período da
		Dívida</div>
	<div id="perDHistBody" class="corpoSubTitulo" style="">
		<table width="100%" border="0">


			<% if((divida.getMesTermino() != null) || (divida.getAnoTermino() != null)) { %>
			<tr class="formConsulta">

				<td id="linha_inicioDivHist" class="cinzaE" style="width: 50px;">Início</td>
				<td id="input_inicioDivHist" class="cinza" style="width: 60px;">
					${divida.mesProcessoGeracao}/${divida.anoProcessoGeracao}</td>

				<td id="linha_terminoDivHist" class="cinzaE" style="width: 50px;">Término</td>
				<td id="input_terminoDivHist" class="cinza" style="width: 60px;">
					${divida.mesTermino}/${divida.anoTermino}</td>


				<%}  else {%>

				<tr class="formConsulta">

					<td id="linha_inicioDivHist" class="cinzaE" style="width: 50px;">Processo
						Pagamento</td>
					<td id="input_inicioDivHist" class="cinza" style="width: 60px;">
						${divida.mesProcessoGeracao}/${divida.anoProcessoGeracao}</td>

					<!-- 								<td id="input_inicioDivHist" class="cinza" style="width:60px;">
									<input id="processoDataInicioD" name="processoDataInicioD" style="width:60px;" value="${divida.mesProcessoGeracao}/${divida.anoProcessoGeracao}" maxlength="7" type="text" readonly="readonly" disabled="disabled"/>
								</td>   -->

					<%} %>
					<td style="border: 1px solid #fff;">&nbsp;</td>
				</tr>
		</table>
	</div>


	<!-- 		<div class="titulo">	Alterar D&iacute;vida do M&ecirc;s do SISPAG</div>   -->

	<!--		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Informações sobre a Dívida</div>
				
				<div class="corpoSubTitulo">  
				<form id="filtrarMatFinSispagForm" method="post" action="filtrarMatFinSispag.do">
					<table width="100%" border="0">
						<tr class="formConsulta">							                							
								<td id="m_SISPAG" title="Campo requerido" class="cinzaE" style="width:125px;">Matrícula Financeira *</td>
								<td id="input_SISPAG" title="Campo requerido" style="width:80px;">
								     <input type="text" name="matFin" value="${sessionScope.matFin}">
								</td>
								<td id="tdi_SISPAG" title="Campo requerido" style="width:50px;" class="formBt">
									<a href="javascript:document.getElementById('filtrarMatFinSispagForm').submit();">Filtrar</a>
																		
    							</td>
    							
    		   					<td style="border:1px solid #fff;">&nbsp;</td>							
						</tr>
					</table>
					</form>
				</div> <!--  Fim Div corpoSubTitulo -->

	<!-- #################### -->
	<!-- 	<form id="alterarDividaMesSispagForm" method="post" action="../oc/AlterarDividaAction.do">  -->
	<!--
				<div id="perDHistTitle" class="subtitulo" style="display:none;">período da Dívida Histórica	</div>
				<div id="perDHistBody" class="corpoSubTitulo" style="display:none;">
					<table width="100%" border="0">
						<tr class="formConsulta">
							
								<td id="linha_inicioDivHist" title="Campo requerido" class="branco" style="width:120px;">Início *</td>
								<td id="input_inicioDivHist" title="Campo requerido" style="width:100px;">
									<input id="inicioDivHist" style="width:95px;" value="mm/aaaa" maxlength="7" type="text"/>
								</td>
						
								<td id="linha_terminoDivHist" title="Campo requerido" class="branco" style="width:120px;">Término *</td>
								<td id="input_terminoDivHist" title="Campo requerido" style="width:100px;">
									<input id="terminoDivHist" style="width:95px;" value="mm/aaaa" maxlength="7" type="text"/>
								</td>
								<td style="border:1px solid #fff;">&nbsp;</td>
							
						</tr>
					</table>
					-->
	<!-- /div-->
	<!--  Fim Div perDHistBody -->
	<!-- <div class="subtitulo">
				Matrículas
				</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="m_SIAPE" title="Campo requerido" class="branco" style="width:120px;">Matr. do SIAPE *</td>
							<td id="input_SIAPE" title="Campo requerido" style="width:150px;">
								<input id="SIAPE" style="width:150px;" value="" maxlength="7" type="text"/>
							</td>
					
							<td id="m_SISPAG" title="Campo requerido" class="branco" style="width:120px;display:none;">Matr. do SISPAG *</td>
							<td id="input_SISPAG" title="Campo requerido" style="width:150px;display:none;">
								<input id="SISPAG" style="width:150px;" value="" maxlength="7" type="text"/>
							</td>
							<td style="border:1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>-->

	<div class="subtitulo">Informações Pessoais</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">
			<tr class="formConsulta">
				<!--  		<td id="linha_cod_divida" class="cinzaE" style="width:100px;">C&oacute;digo da D&iacute;vida</td>  
								<td id="input_cod_divida" class="cinza" style="width:60px;"><%= divida.getCodigo() %></td>  -->
				<td id="linha_nome" title="Campo requerido" class="cinzaE"
					style="width: 60px;">Nome *</td>
				<td id="input_nome" title="Campo requerido" style="width: 280px;"
					colspan='3'>${pessoa.nome}</td>
				<td id="linha_cpf" title="Campo requerido" class="cinzaE"
					style="width: 60px;">CPF *</td>
				<td id="input_cpf" title="Campo requerido" style="width: 100px;">${pessoa.cpf}</td>
				<td style="border: 1px solid #fff">&nbsp;</td>
			</tr>
			<tr class="formConsulta">
				<td id="linha_posto" title="Campo requerido" class="cinzaE"
					style="width: 60px;">Posto *</td>
				<td id="input_posto" title="Campo requerido" style="width: 100px;">${pessoa.posto}</td>
				<td id="linha_oc" title="Campo requerido" class="cinzaE"
					style="width: 60px;">OC *</td>
				<td id="input_oc" title="Campo requerido" style="width: 100px;">${divida.oc.oc}</td>
				<td id="linha_om" title="Campo requerido" class="cinzaE"
					style="width: 60px;">Situação *</td>
				<td id="input_om" title="Campo requerido" style="width: 100px;">${pessoa.situacao}</td>
				<td style="border: 1px solid #fff">&nbsp;</td>
			</tr>
		</table>
	</div>
	<div class="subtitulo">Informações Bancárias</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">
			<tr class="formConsulta">
				<td id="linha_banco" title="Campo requerido" class="cinzaE"
					style="width: 90px;">Banco *</td>
				<td id="input_banco" title="Campo requerido" style="width: 100px;"><%=divida.getBanco()%></td>

				<td id="linha_agencia" title="Campo requerido" class="cinzaE"
					style="width: 100px;">Agência *</td>
				<td id="input_agencia" title="Campo requerido" style="width: 100px;"><%=divida.getAgencia()%></td>
				<td id="linha_cc" title="Campo requerido" class="cinzaE"
					style="width: 100px;">Conta-Corrente *</td>
				<td id="input_cc" title="Campo requerido" style="width: 100px;"><%=divida.getContaCorrente()%></td>
				<td style="border: 1px solid #fff">&nbsp;</td>
			</tr>
			<tr class="formConsulta">
				<td id="linha_valor_divida" title="Campo requerido" class="cinzaE"
					style="width: 100px;">Valor Atual *</td>
				<% if (divida.getEstado().equals("EM ESPERA")){%>
				<input type="text" name="valor_divida" maxlength="50"
					style="width: 200px"
					value="<%=Utilitaria.formatarValor("#,###,##0.00" ,divida.getValor()) %>">
					<%} else { %>
					<td id="input_valor_divida" title="Campo requerido"
					style="width: 100px;"><%=Utilitaria.formatarValor("#,###,##0.00" ,divida.getValor()) %></td>
					<%} %>
					<td style="border: 1px solid #fff" colspan="5">&nbsp;</td>
			</tr>
		</table>
	</div>
	<div class="subtitulo">Documento Autorizando Inclusão da Dívida</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">
			<tr class="formConsulta">
				<td id="linha_origem_doc" title="Campo requerido" class="branco"
					style="width: 60px;">Origem *</td>
				<!--  <td id="input_origem_doc" title="Campo requerido" style="width:100px;">																					
						 		<input type="text" name="origemDoc" maxlength="3" value="<%=divida.getLancamentoInicial().getOrigemDocAutorizacao()%>" style=" height : 19px;">   
							</td>  -->

				<td id="input_origem_doc" title="Campo requerido"
					style="width: 200px;" colspan="3"><select
					id="input_origem_doc" name="origemDoc" style="width: 400px;">
						<%
						if (session.getAttribute("listaOc") != null){							
							ArrayList<OC> listaOc = (ArrayList<OC>)session.getAttribute("listaOc");
					%>
						<%
								for(OC ocDividaOrigem:listaOc){ 
									if (listaOc != null) {										
					%>
						<option value="<%=ocDividaOrigem.getOc()%>"
							<%
										if (ocDividaOrigem.getOc().equals(ocCodigoOrigem)) {	out.print(" selected");   }	
					%>><%=ocDividaOrigem.getOc()+" - "+ocDividaOrigem.getNome()%></option>
						<%				} //fecha if 
								} // fecha loop lista de motivo
					%>
						<%
							} //fecha if teste de request
					%>
				</select></td>


				<td style="border: 1px solid #fff;" colspan="5">&nbsp;</td>

			</tr>
			<tr class="formConsulta">
				<td id="linha_tipo_doc" title="Campo requerido" class="branco"
					style="width: 60px;">Tipo *</td>
				<td id="input_tipo_doc" title="Campo requerido"
					style="width: 100px;"><select id="tipo_doc" name="tipoDoc"
					style="width: 100px;">
						<option
							value=<%=divida.getLancamentoInicial().getTipoDocAutorizacao()%>
							selected="selected"><%= divida.getLancamentoInicial().getTipoDocAutorizacao()%></option>
						<option value="oficio">Ofício</option>
						<option value="mensagem">Mensagem</option>
						<option value="CI">CI</option>
						<option value="CP">CP</option>
						<option value="papeleta">Papeleta</option>
						<option value="os">OS</option>
				</select></td>

				<td id="linha_numero_doc" title="Campo requerido" class="branco"
					style="width: 60px;">N&uacute;mero *</td>
				<td id="input_numero_doc" title="Campo requerido"
					style="width: 200px;"><input type="text" name="numeroDoc"
					maxlength="50" style="width: 200px"
					value="<%=lancamento.getNumeroDocAutorizacao()%>"></td>

				<td id="linha_data_doc" title="Campo requerido" class="branco"
					style="width: 60px;">Data *</td>
				<td id="input_data_doc" title="Campo requerido"
					style="width: 100px;"><input type="text" name="dataDoc"
					maxlength="10"
					value="<%=formatador.format(divida.getLancamentoInicial().getDataDocAutorizacao())%>"
					validate="diamesano"></td>
				<td style="border: 1px solid #fff;">&nbsp;</td>
			</tr>

		</table>
	</div>
	<% if((divida.getMesTermino() != null) || (divida.getAnoTermino() != null)) { %>
	<div class="subtitulo">Documento de envio &agrave; PAPEM</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">

			<tr class="formConsulta">
				<td id="linha_tipo_doc_envio" title="Campo requerido" class="branco"
					style="width: 60px;">Tipo *</td>
				<td id="input_tipo_doc_envio" title="Campo requerido"
					style="width: 100px;"><select id="tipoDocEnvio"
					name="tipoDocEnvio" style="width: 100px;">
						<option value=${divida.docEnvio } selected="selected">${divida.docEnvio}</option>
						<option value="Ofício">Ofí­cio</option>
						<option value="Mensagem">Mensagem</option>
						<option value="CI">CI</option>
						<option value="CP">CP</option>
						<option value="Papeleta">Papeleta</option>
						<option value="OS">OS</option>
				</select></td>

				<td id="linha_numero_doc_envio" title="Campo requerido"
					class="branco" style="width: 60px;">N&uacute;mero *</td>
				<td id="numdocEnviostr" title="Campo requerido"
					style="width: 100px;"><input id="numDocEnvio"
					name="numDocEnvio" style="width: 95px;"
					value="${divida.numeroDocEnvio}" maxlength="7" type="text"
					validate="numero" /></td>

				<td id="linha_data_doc" title="Campo requerido" class="branco"
					style="width: 60px;">Data *</td>
				<td id="input_data_doc" title="Campo requerido"
					style="width: 100px;"><input id="dataDocEnvio"
					name="dataDocEnvio" style="width: 95px;"
					value="<%=FDataDocEnvio %>" maxlength="10" type="text"
					validate="diamesano" /></td>
				<td style="border: 1px solid #fff;">&nbsp;</td>
			</tr>
		</table>
	</div>
	<%}  %>


	<div class="subtitulo">Informações Complementares</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0" style="width: 600px;">
			<tr class="formConsulta" style="width: 599px;">

				<td id="linha_fato_gerador" title="Campo requerido" class="branco"
					style="width: 100px;">Data do Motivo *</td>
				<td id="input_fato_gerador" title="Campo requerido"
					style="width: 100px;"><input type="text" name="dataMotivo"
					value="<%=formatador.format(divida.getDataMotivo())%>"
					validate="diamesano"></td>

				<td id="linha_motivo" title="Campo requerido" class="branco"
					style="width: 50px;">Motivo *</td>

				<td colspan="" id="input_motivo" style="width: 300px"
					title="Campo requerido"><select id="input_motivo"
					name="motivo" style="width: 300px;">
						<% if (session.getAttribute("listaMotivo") != null){							
							ArrayList<Motivo> listaMotivo = (ArrayList<Motivo>)session.getAttribute("listaMotivo");
					%>
						<%
								for(Motivo motivoDivida:listaMotivo){ //loop lista de motivo

									if (motivoDivida != null) {										
					%>
						<option value="<%=motivoDivida.getNome()%>"
							<%
										if (motivoDivida.getNome().equals(motivo)) {	out.print(" selected");   }	
					%>><%=motivoDivida.getNome()%></option>
						<%				} //fecha if 										
								} // fecha loop lista de motivo
					%>
						<%
							} //fecha if teste de request
					%>
				</select></td>



				<!-- 		<td id="input_motivo" title="Campo requerido" style="width:350px" >
								<input type="text" name="motivo" value="<%=divida.getMotivo()%>">
							</td>   -->

				<td style="border: 1px solid #fff;">Â </td>
			</tr>

		</table>
	</div>

	<div class="subtitulo">Motivo da Alteração:</div>
	<div class="corpoSubTitulo">
		<table width="100%" border="0">

			<tr class="formConsulta">
				<td id="input_tipo_doc_envio" class="branco" style="width: 300px;">
					<input type="text" name="observacao" style="width: 1000px;"
					value="">
				</td>
		</table>
	</div>

	<div class="barraBts">
		<!--<a href="javascript:avoid();" onclick="$('wait').style.display=''">Cadastrar</a>-->

		<a
			href="javascript:document.getElementById('alterarDividaMesSispagForm').submit();">Alterar</a>
		<a
			href="javascript:document.getElementById('alterarDividaMesSispagForm').reset();">Limpar
			Formulário</a> <a href="../oc/consultarDividaDetalhe.jsp">Voltar</a>
	</div>

	<!-- </form> -->
	<!--comment-->


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
	maskDo();
</script>

</html>

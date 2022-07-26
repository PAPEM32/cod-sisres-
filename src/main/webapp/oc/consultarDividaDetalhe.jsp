<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="com.acesso.verificacao.VerificarPerfil"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.sisres.utilitaria.*"%>
<%@ page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts.action.ActionErrors"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br" xml:lang="pt-br">

   
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Detalhe da Dívida</title>

<jsp:include page="/importacoes.html" />

<script>
	jQuery(document).ready( function(){
		jQuery("#incluirReversao").hide(); //div
		
		jQuery("#mostraForm").click(function() { //botao
			jQuery("#incluirReversao").show(); //div
			jQuery("#lancamentoperdao").hide(); //div
			jQuery("#lancamentodevolucao").hide();  //div
                        jQuery("#incluirStatus").hide(); //div
			jQuery(window).scrollTo( '#pontoReversao', { duration:500, axis:'y'});
		});
	});	

</script>

<script>
	jQuery(document).ready( function(){
		jQuery("#incluirStatus").hide(); //div
                
                jQuery("#mostraFormStatus").click(function() { //botao
                    jQuery("#incluirStatus").show(); //div
                    jQuery("#lancamentoperdao").hide(); //div
		    jQuery("#lancamentodevolucao").hide();  //div
                    jQuery("#incluirReversao").hide();
		    jQuery(window).scrollTo( '#pontoReversao', { duration:500, axis:'y'});
                    
         });
    });
</script>

<script>
	jQuery(document).ready( function(){
		jQuery("#lancamentodevolucao").hide();
		jQuery("#mostraForm2").click(function() {
			jQuery("#lancamentodevolucao").show();
			jQuery("#lancamentoperdao").hide(); 
			jQuery("#incluirReversao").hide(); 
                        jQuery("#incluirStatus").hide(); //div
			jQuery(window).scrollTo( '#pontoReversao', { duration:500, axis:'y'});
		});
	});	
</script>


<script>
	jQuery(document).ready( function(){
		jQuery("#lancamentoperdao").hide();
		jQuery("#mostraForm3").click(function() {
			jQuery("#lancamentoperdao").show();
			jQuery("#lancamentodevolucao").hide(); 
			jQuery("#incluirReversao").hide();
                        jQuery("#incluirStatus").hide(); //div
            jQuery(window).scrollTo( '#pontoReversao', { duration:500, axis:'y'});
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


	<%
		
		        Divida divida = (Divida)session.getAttribute("divida");
                session.setAttribute("divida",divida);
                
                divida.setDocEnvio(divida.getDocEnvio());
                              
                Pessoa pessoa = divida.getPessoa();
                pageContext.setAttribute("pessoa",pessoa);
                Lancamento lancamento = divida.getLancamentoInicial();
            	pageContext.setAttribute("lancamento",lancamento);
                
            	String FDataDocEnvio = ""; 
            	
            	String DivValor = Utilitaria.formatarValor("######0.00",divida.getValor());
              
                DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
                        
                String FdataDocAutorizacao = formatador.format(lancamento.getDataDocAutorizacao());
            	
                if(divida.getDataDocEnvio() != null){
                   FDataDocEnvio = formatador.format(divida.getDataDocEnvio());
                }
                
                String FDataMotivo = formatador.format(divida.getDataMotivo());

          %>

	<div class="clear"></div>
	<%if(divida.getCgs().equals("S")){%>
	<div class="titulo">Detalhe da Dívida ${divida.tipo} do SISPAG</div>
	<%} else{%>
	<div class="titulo">Detalhe da Dívida ${divida.tipo} do SIAPE</div>
	<%} %>

	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">

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
						<!--  								<td id="linha_tipo_divida" title="Campo requerido" style="width:50px;">
				 			     <input id="tipo_divida" name="tipo_divida" style="width:60px;" maxlength="60" type="text"  value="${divida.tipo}"  /></td>   -->


						<!--<td id="linha_sistema_origem" class="cinzaE" style="width:50px;">Origem</td>
								<td class="cinza" style="width:60px;">
									${divida.oc}
								</td>-->

						<td style="border: 1px solid #fff;">&nbsp;</td>

					</tr>
					<tr class="formConsulta">
						<%if(divida.getCgs().equals("S")){%>
						<td id="matricula" class="cinzaE" style="width: 120px;">Matrícula
							Financeira</td>
						<td id="input_SISPAG" class="cinza"
							style="width: 100px; font-weight: normal;">${pessoa.matFin}
						</td>
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

							<!-- 							<td id="input_inicioDivHist" class="cinza" style="width:60px;">
									<input id="processoDataInicioD" name="processoDataInicioD" style="width:60px;" value="${divida.mesProcessoGeracao}/${divida.anoProcessoGeracao}" maxlength="7"  readonly="readonly"/>
								</td>   -->

							<%} %>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
				</table>
			</div>
			<div class="subtitulo">Informações Pessoais</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_nome" class="cinzaE" style="width: 40px;">Nome</td>
						<td id="input_nome" colspan="3" class="cinza"
							style="width: 220px; font-weight: normal;">${pessoa.nome}</td>
						<td id="linha_cpf" class="cinzaE" style="width: 50px;">CPF</td>
						<td id="input_cpf" class="cinza"
							style="width: 50px; font-weight: normal;">${pessoa.cpf}</td>
						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_posto" class="cinzaE" style="width: 40px;">Posto</td>
						<td id="input_posto" class="cinza"
							style="width: 50px; font-weight: normal;">${pessoa.posto}</td>
						<td id="linha_oc" class="cinzaE" style="width: 50px;">OC</td>
						<td id="input_oc" class="cinza"
							style="width: 50px; font-weight: normal;">${divida.oc.oc}</td>

						<td id="linha_om" class="cinzaE" style="width: 50px;">Situa&ccedil;&atilde;o</td>
						<td id="input_om" class="cinza"
							style="width: 50px; font-weight: normal;">
							${pessoa.situacao}</td>
						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="subtitulo">Informações Bancárias</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_banco" class="cinzaE" style="width: 90px;">Banco</td>
						<td id="input_banco" class="cinza"
							style="width: 90px; font-weight: normal;">${divida.banco}</td>

						<td id="linha_agencia" class="cinzaE" style="width: 50px;">Agência</td>
						<td id="input_agencia" class="cinza"
							style="width: 50px; font-weight: normal;">${divida.agencia}
						</td>
						<td id="linha_cc" class="cinzaE" style="width: 90px;">Conta-Corrente</td>
						<td id="input_cc" class="cinza"
							style="width: 90px; font-weight: normal;">
							${divida.contaCorrente}</td>
						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_valor_divida" class="cinzaE" style="width: 90px;">Valor
							Atual</td>
						<td id="input_valor_divida" class="cinza"
							style="width: 90px; font-weight: normal;"><%=DivValor %></td>
						<td style="border: 1px solid #fff" colspan="5">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="subtitulo">Documento de Autorização de Inclusão da
				Dívida</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_origem_doc" class="cinzaE" style="width: 50px;">Origem</td>
						<td id="input_origem_doc" class="cinza" style="width: 50px;">
							${lancamento.origemDocAutorizacao}</td>
						<td style="border: 1px solid #fff;" colspan="5">&nbsp;</td>

					</tr>
					<tr class="formConsulta">
						<td id="linha_tipo_doc" class="cinzaE" style="width: 50px;">Tipo</td>
						<td id="input_tipo_doc" class="cinza" style="width: 60px;">
							${lancamento.tipoDocAutorizacao}</td>

						<td id="linha_numero_doc" class="cinzaE" style="width: 50px;">N&uacute;mero</td>
						<td id="input_numero_doc" class="cinza" style="width: 50px;">
							${lancamento.numeroDocAutorizacao}</td>

						<td id="linha_data_doc" class="cinzaE" style="width: 50px;">Data</td>
						<td id="input_data_doc" class="cinza" style="width: 70px;"><%=FdataDocAutorizacao %>
						</td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>

				</table>
			</div>

			<% if((divida.getMesTermino() != null) || (divida.getAnoTermino() != null)) { %>

			<div class="subtitulo">Documento de envio &agrave; PAPEM</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">

					<tr class="formConsulta">
						<td id="linha_tipo_doc_envio" class="cinzaE" style="width: 50px;">Tipo</td>
						<td id="input_tipo_doc_envio" class="cinza" style="width: 60px;">
							${divida.docEnvio}</td>

						<td id="linha_numero_doc_envio" class="cinzaE"
							style="width: 50px;">N&uacute;mero</td>
						<td id="input_numero_doc_envio" class="cinza" style="width: 50px;">
							${divida.numeroDocEnvio}</td>

						<td id="linha_data_doc" class="cinzaE" style="width: 50px;">Data</td>

						<td id="input_data_doc" class="cinza" style="width: 70px;"><%=FDataDocEnvio %>
						</td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>

				</table>
			</div>
			<%} %>
			<!--Inicio Atualizadao sol T. Charles -->
			<div class="subtitulo">Dívida em andamento </div>
                            <div class="corpoSubTitulo">
                                <table width="100%" border="0">
                                    <tr class="formConsulta">
                                        <td id="linha_tipo_dividaAnd_status" class="cinzaE" style="width: 50px;">Status</td>
                                        <td id="input_tipo_dividaAnd_status" class="cinza" style="width: 60px;">${divida.dividaEmAndamento.descricao}</td>
                                        
                                        <td id="linha_dividaAnd_origem" class="cinzaE" style="width: 50px;">Origem</td>
                                        <td id="input_dividaAnd_origem" class="cinza" style="width: 70px;">${divida.dividaEmAndamento.origemDocInclusaoStatus}</td>
                                        
                                        <td id="linha_dividaAnd_tipo" class="cinzaE" style="width: 50px;">Tipo </td>
                                        <td id="input_dividaAnd_tipo" class="cinza" style="width: 70px;">${divida.dividaEmAndamento.tipoDocAutorizacaoInclusaoStatus}</td>
                                                                    
                                    </tr>
                                    <tr>
										<td id="linha_dividaAnd_numero" class="cinzaE" style="width: 50px;">Número </td>
                                        <td id="input_dividaAnd_numero" class="cinza" style="width: 70px;">${divida.dividaEmAndamento.numDoc}</td>
                                        

					                    <td id="linha_dividaAnd_dataDoc" class="cinzaE" style="width: 50px;">Data</td>
                                        <td id="input_dividaAnd_dataDoc" class="cinza" style="width: 70px;">${divida.dividaEmAndamento.dataDocInclusaoStatus}</td>
                                        
                                    </tr>
                                    <tr>
                                    <td id="linha_dividaAnd_obs" class="cinzaE" style="width: 50px;">Observações</td>
                                        <td id="input_dividaAnd_obs" class="cinza" colspan="3" style="width: 70px;">${divida.dividaEmAndamento.observacaoStatusDivida}</td>
                                        
                                    </tr>
                                    
                                </table>
                            </div>
			</div>
			<!--fim Atualizadao sol T. Charles -->
			<div class="subtitulo">Informações Complementares</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">

						<td id="linha_fato_gerador" class="cinzaE" style="width: 90px;">Data
							do Motivo</td>
						<td id="input_fato_gerador" class="cinza" style="width: 90px;">
							<%=FDataMotivo %>
						</td>

						<td id="linha_motivo" class="cinzaE" style="width: 50px;">Motivo</td>
						<td id="input_motivo" class="cinza" style="width: 250px"
							colspan="">${divida.motivo}</td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>

				</table>
			</div>
			<!-- <div class="subtitulo">
					Registro de AlteraÃ§Ãµes
				</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">

            <tr class="formConsulta">
            
            	<td id="linha_historico" class="cinzaE" style="width:140px; vertical-align:top;">HistÃ³rico de Altera&ccedil;&otilde;es</td>
							<td id="input_historico" style="width:350px" colspan="">
              	<div style="background: #E1E2E2; overflow-y:auto; height:100px;">
                	Dia: 25/03/2009<br /> 
                  Usuário: FC Beltrano <br />
									Campos Alterados: Agência, Conta-corrente <br /> 
                  Motivo: Em atendimento a msg 201500/MAR/2009<br />
                	<hr />
                	Dia: 19/03/2009<br /> 
                  Usuário: 3SG Cicrano <br />
									Campos Alterados: Nome, CPF <br /> 
                  Motivo: CorreÃ§Ã£o de dados<br />
                </div> 
								<textarea style="width:345px; overflow:auto;" rows="5" disabled="disabled">Alteração do dia: 19/03/2009<pre><br /> Usuário: 3SG Wallace Roque<br>
Campos Alterados: Nome, CPF<br /> Motivo: CorreÃ§Ã£o de dados<br /></textarea>
							</td>
              
							<td style="border:1px solid #fff;">&nbsp;</td>
						</tr>
            
					</table>
				</div>-->
			<div class="barraBts" id="pontoReversao">
				<a
					href="../oc/consultarListaLancamentos.do?dividaId=<%= divida.getId()%>">Visualizar
					Lançamentos</a>
				
				<% if ((request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR"))){%>
				<a href="javascript:void(0)" id="mostraFormStatus">Incluir Status</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>

				<% if ((request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR") || request.getSession().getAttribute("role").equals("SIPM")) && divida.getEstado().equals("CONCLUIDO")){%>
				<a href="javascript:void(0)" id="mostraForm">Incluir Reversão</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>
				<% if ((request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR") || request.getSession().getAttribute("role").equals("SIPM"))  && divida.getEstado().equals("CONCLUIDO")){%>
				<a
					href="javascript:window.location.assign('../oc/alterarDivida.jsp');">Alterar
					Dívida</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>

				<% if ((request.getSession().getAttribute("role").equals("SIPM"))  && divida.getEstado().equals("EM ESPERA")){%>
				<a
					href="javascript:window.location.assign('../oc/alterarDivida.jsp');">Alterar
					Dívida</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>

				<!--      	<a href="javascript:window.location.assign('../oc/alterarDivida.jsp');">Alterar Dívida</a> -->
				<% if ((request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR") || request.getSession().getAttribute("role").equals("SIPM")) && divida.getEstado().equals("CONCLUIDO")){%>
				<a href="javascript:void(0)" id="mostraForm2">Incluir Devolução</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>

				<% if ((request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR") || request.getSession().getAttribute("role").equals("SIPM")) && divida.getEstado().equals("CONCLUIDO")){%>
				<a href="javascript:void(0)" id="mostraForm3">Incluir Perdão</a>
				<%}//fecha condiÃ§Ã£o sobre o perfil%>

				<% if  (!session.getAttribute("perfilUsuario").equals("OC") && divida.getEstado().equals("EM ESPERA")){   %>
				<a href="javascript:window.location.assign('excluirDivida.do');">Excluir
					Dívida</a>
				<% }%>


				<!--  	<a href="javascript:void(0)" id="mostraForm2">Incluir Devolução</a> 
          	<a href="javascript:void(0)" id="mostraForm3">Incluir Perdão</a>  -->
				<a href="../oc/consultarDivida.jsp">Voltar</a>

			</div>
			<!-- <div class="subtituloEnfase">
					Lançaamentos
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
								<input id="fato_gerador" style="width:95px;" value="mm/aaaa" maxlength="7" type="text"/>
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
				</div>
				<div class="barraBts">
					<a href="#">Confirmar Dívida</a> <a href="javascript:document.getElementById('formCadastro').reset();">Limpar Formulário</a> 
				</div>-->
		</div>
		<% 
ArrayList<Lancamento> listLancamentos = (ArrayList<Lancamento>)session.getAttribute("listLancamentos");
if (listLancamentos!= null) {%>
		<div id="visualizarLancamento">
			<div class="subtituloEnfase">Visualizar Lançamentos</div>
			<div class="corpoSubTituloEnfase">
				<table width="100%" border="0" class="displaytag">
					<thead>
						<tr>
							<th>Código</th>
							<th>Valor</th>
							<th>Operador</th>
							<th nowrap="nowrap">Tipo Doc. Autorização</th>
							<th>Origem</th>
							<th>Nº Doc. Autorização</th>
							<th>Data Doc. Autorização</th>
							<th>Responsável</th>
							<th>Tipo do Lançamento</th>
						</tr>
					</thead>
					<% 
	Iterator<Lancamento> it = listLancamentos.iterator();
	int i = 0;
	String linha = "linha_" + i;
	
   while(it.hasNext()) {
	 Lancamento lancamento2 = it.next();
	 //session.removeAttribute("lancamento");
     session.setAttribute("lancamento2",lancamento2);
	 pageContext.removeAttribute("lancamento");
     //pageContext.setAttribute("lancamento2",lancamento2);
        
 	%>
					<tr id="<%= linha %>" class="<%=i%2==0?"odd":"even" %> hand"
						onmouseover="$('<%= linha %>').morph('background:#cfe2f5',{duration:0})"
						onmouseout="$('<%= linha %>').morph('background:#f4f4f4',{duration:0,delay:0})"
						onclick="window.location.assign('consultarLancamentoDetalhe.do?index2=<%= i%>')">
						<td class="center">${lancamento2.codigo}</td>
						<%
		    String LacValor = Utilitaria.formatarValor("######0.00",lancamento2.getValor());
         %>
						<td><%= LacValor %></td>
						<td>${lancamento2.operador}</td>
						<td>${lancamento2.tipoDocAutorizacao}</td>
						<td>${lancamento2.origemDocAutorizacao}</td>
						<td>${lancamento2.numeroDocAutorizacao}</td>
						<%String LDataDocAut = formatador.format(lancamento2.getDataDocAutorizacao()); %>
						<td><%=LDataDocAut %></td>
						<td>${lancamento2.responsavel}</td>
						<td>${lancamento2.tipoLancNome}</td>
						<%
		i++;
		linha = "linha_" + i;
        }
        	
        }
        	
        %>
					</tr>

				</table>

			</div>
			<!-- Fecha div#visualizarLancamento  -->
			<button class="subtituloEnfase">Dívida em andamento - Incluir Status</button>
			<!-- Inicio div#incluirStatus -->
                        
                        <% if (request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR")){%>
                        <div id="incluirStatus">
                            <!--<p>DIV INCLUIR STATUS</p> -->
                            <!-- Formulário Incluir Status -->
                            <form id="incluirStatusForm" action="../papem23/incluirStatusAction.do" method="post" accept-charset="utf-8" >
				<div class="subtituloEnfase">Dívida em andamento - Incluir Status</div>
                                <div class="corpoSubTituloEnfase">
									<table width="100%" border="0">
										<tr class="formConsulta" style="height: 25px;">
										 <td id="linha_status" title="Campo requerido" class="branco"
											style="width: 100px;">Status</td>
											<td id="input_status" title="Campo requerido" style="width: 100px;">
												<select id="status"	name="statusDivida" style="width: 300px;">
													<option value="" selected="selected"></option>
													<option value="Ativo">Ativo</option>
													<option value="EmParcelamento">Em Parcelamento</option>
													<option value="ImpossZerarSaldoDevedor">Impossibilidade de zerar saldo devedor</option>
													<option value="DividaAtiva">Dívida Ativa</option>
													<option value="Cobranca_Judicial">Cobrança Judicial</option>
													<option value="Prescrito">Prescrito</option>
													<option value="Restituido">Restituído</option>
												</select>
											</td>	
											<td id="linha_doc_origem" title="Campo requerido" class="branco"
									            style="width: 100px;">Origem do documento *</td>
								           <td id="input_doc_origem" title="Campo requerido"
									            style="width: 105px;"><input id="doc_origemInclusaoStatus"
									            name="origemDocInclusaoStatus" style="width: 100px;"
									            maxlength="10" type="text" /></td>
									       
							                
									        <td style="width: 100px;">&nbsp;</td>
							                <td style="width: 100px;">&nbsp;</td>
									      </tr>      
		
							<tr class="formConsulta" style="height: 25px;">
							<td id="linha_doc_tipo" title="Campo requerido" class="branco"
									style="width: 100px;">Tipo de Documento *</td>
								<td id="input_doc_tipo" title="Campo requerido"
									style="width: 100px;"><select id="tipo_doc"
									name="tipoDocAutorizacaoInclusaoStatus" style="width: 100px;">
										<option value="" selected="selected"></option>
										<option value="Ofí­cio">Ofício</option>
										<option value="Ofí­cio do Banco">Ofício do Banco</option>
										<option value="Mensagem">Mensagem</option>
										<option value="Carta">Carta</option>
										<option value="CI">CI</option>
										<option value="CP">CP</option>
										<option value="OS">OS</option>
								    </select>
								</td>
								<td id="linha_data_doc_inclusao_status" title="Campo requerido"
								class="branco" style="width: 100px;">Data documento *</td>
							<td id="input_data_doc_inclusao_status" title="Campo requerido"
								style="width: 150px;"><input id="data_doc_inclusao_status"
								name="dataDocInclusaoStatus" style="width: 100px;" value="" maxlength="10"
								type="text" validate="diamesano" /></td>
								<td style="width: 100px;"></td>
							                <td style="width: 100px;">&nbsp;</td>
								</tr>
								<tr class="formConsulta" style="height: 25px;">
								<td id="linha_numDoc" title="Campo requerido" class="branco"
									            style="width: 100px;">Número *</td>
								           <td id="input_numDoc" title="Campo requerido"
									            style="width: 105px;"><input id="doc_numDoc"
									            name="numDoc" style="width: 150px;"
									            maxlength="100" type="text" /></td>
								</tr>
								
											<tr class="formConsulta">
								              <td id="linha_obs_status" title="Campo requerido" class="branco"
									            style="width: 100px;">Observação</td>
								             <td id="input_obs_status" title="Campo requerido"
									            style="width: 100px;" maxlength="600" colspan="3">
									            <textarea  id="doc_obs_status" name="observacaoStatusDivida"
										           style="overflow: auto; height: 110px; width: 585px;" rows="11"></textarea>
										    </td>
										    <td style="width: 100px;" colspan="2">&nbsp;</td>
							                

							               </tr>
										
										<tr>
						<td colspan="6" align="center">
						   <div class="barraBts">
						      <a href="javascript:document.getElementById('incluirStatusForm').submit();">Confirmar</a>
						   </div> 
						</td>
					    </tr>
									</table>
                                </div>
                               
                               
                            </form>
                            
                            <!-- Fim Formulário Incluir Status -->
                        </div>
                        <%}//fecha condiÃ§Ã£o sobre o perfil %>
                        <!-- fim#incluirStatus -->


			<% if (request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR")){%>
			<div id="incluirReversao">

				<!-- Formulário -->
				<form id="incluirReversaoForm"
					action="../papem23/incluirReversaoAction.do" method="get">

					<div class="subtituloEnfase">Incluir Lançamento de Reversão</div>
					<div class="corpoSubTituloEnfase">
						<table width="100%" border="0">
							<tr class="formConsulta" style="height: 25px;">


								<td id="linha_doc_tipo" title="Campo requerido" class="branco"
									style="width: 100px;">Tipo de Documento *</td>
								<td id="input_doc_tipo" title="Campo requerido"
									style="width: 100px;"><select id="tipo_doc"
									name="tipoDocAutorizacao" style="width: 150px;">
										<option value="" selected="selected"></option>
										<option value="Ofí­cio">Ofício</option>
										<option value="Ofí­cio do Banco">Ofício do Banco</option>
										<option value="Mensagem">Mensagem</option>
										<option value="Carta">Carta</option>
										<option value="CI">CI</option>
										<option value="CP">CP</option>
										<option value="OS">OS</option>
								</select></td>

								<td id="linha_doc_id" title="Campo requerido" class="branco"
									style="width: 200px;" width="243">Número do Doc. de
									Reversão *</td>
								<td id="input_doc_id" title="Campo requerido"
									style="width: 105px;"><input id="doc_numaut"
									name="numeroDocAutorizacaoStr" style="width: 100px;" value=""
									type="text" /></td>


							</tr>
							<tr class="formConsulta">
								<td id="linha_doc_data" title="Campo requerido" class="branco"
									style="width: 200px;">Data do Documento *</td>
								<td id="input_doc_data" title="Campo requerido"
									style="width: 105px;"><input id="doc_data"
									name="dataDocAutorizacao" style="width: 100px;" maxlength="10"
									type="text" validate="diamesano" /></td>
								<td id="linha_doc_id" title="Campo requerido" class="branco"
									style="width: 100px;">Valor *</td>
								<td id="input_doc_id" title="Campo requerido"
									style="width: 150px;"><input id="doc_valor"
									name="valorStr" style="width: 100px;" value="" maxlength="14"
									type="text" validate="dinheiro" /></td>

							</tr>
							<tr class="formConsulta">
								<td id="linha_doc_origem" title="Campo requerido" class="branco"
									style="width: 200px;">Origem do documento de reversão *</td>
								<td id="input_doc_origem" title="Campo requerido"
									style="width: 105px;" width="639"><input id="doc_origem"
									name="origemDocAutorizacao" style="width: 300px;"
									maxlength="30" type="text" /></td>
								<td style="width: 100px;"></td>
								<td style="width: 100px;"></td>

							</tr>
							<tr class="formConsulta">
								<td id="linha_doc_data" title="Campo requerido" class="branco"
									style="width: 100px;">Observação</td>
								<td id="input_doc_data" title="Campo requerido"
									style="width: 100px;" maxlength="600" colspan="3"><textarea
										id="doc_obs" name="observacao"
										style="overflow: auto; height: 110px; width: 585px;" rows="11"></textarea></td>

							</tr>
							<tr>
								<td colspan="4" align="center">

									<div class="barraBts">
										<a
											href="javascript:document.getElementById('incluirReversaoForm').submit();">Confirmar</a>
									</div> <!-- input type="submit" value="Confirmar Lançamento" /> -->
								</td>
							</tr>
						</table>
					</div>


				</form>
			</div>
			<!-- fecha div#incluirReversao -->
			<%}//fecha condiÃ§Ã£o sobre o perfil %>

		</div>

		<div id="lancamentodevolucao">
			<!-- Formulário -->
			<form id="IncluirDevolucaoForm" method="post"
				action="../papem23/incluirDevolucao.do">

				<div class="subtituloEnfase">Incluir Lançamento de Devolução</div>

				<div class="corpoSubTituloEnfase">
					<table width="100%" border="0">
						<tr class="formConsulta" style="height: 25px;">
							<td id="linha_doc_devolucao" title="Campo requerido"
								class="branco" style="width: 200px;" width="243">Tipo de
								Documento *</td>
							<td id="input_data_doc" title="Campo requerido"
								style="width: 105px;"><select id="tipo_doc"
								name="tipoDocAutorizacao" style="width: 100px;">
									<option value="" selected="selected"></option>
									<option value="Ofício">Ofí­cio</option>
									<option value="Mensagem">Mensagem</option>
									<option value="Carta">Carta</option>
									<option value="CI">CI</option>
									<option value="CP">CP</option>
									<option value="Papeleta">Papeleta</option>
									<option value="OS">GRU</option>
									<option value="OS">OS</option>
							</select></td>
							<td id="linha_data_perdao" title="Campo requerido" class="branco"
								style="width: 150px;">Data do Documento *</td>
							<td id="input_data_perdao" title="Campo requerido"
								style="width: 100px;"><input id="data_perdao"
								name="dataDocAutorizacao" style="width: 100px;" value=""
								maxlength="10" type="text" validate="diamesano" /></td>

						</tr>
						<tr class="formConsulta">
							<td id="linha_ordem_bancaria" title="Campo requerido"
								class="branco" style="width: 200px;">Nº da Ordem Bancária*</td>
							<td id="input_ordem_bancaria" title="Campo requerido"
								style="width: 105px;"><input id="data_bancaria"
								name="numeroOB" style="width: 100px;" maxlength="10" type="text"
								validate="numero" /></td>
							<td id="linha_data_bancaria" title="Campo requerido"
								class="branco" style="width: 100px;">Data da Ordem
								Bancária*</td>
							<td id="input_data_bancaria" title="Campo requerido"
								style="width: 150px;"><input id="doc_data_bancaria"
								name="dataOB" style="width: 100px;" value="" maxlength="10"
								type="text" validate="diamesano" /></td>

						</tr>
						<tr class="formConsulta" style="width: 829px;">
							<td id="linha_valor_lancamento" title="Campo requerido"
								class="branco" style="width: 200px;">Valor do Lançamento*</td>
							<td id="input_valor_lancamento title" "Campo
								requerido" style="width: 105px;" width="639"><input
								id="valor_lancamento" name="valor" style="width: 100px;"
								maxlength="30" type="text" validate="dinheiro" /></td>
							<td style="width: 100px;"></td>
							<td style="width: 100px;"></td>

						</tr>
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 100px;">Observação</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 100px;" maxlength="600" colspan="3"><textarea
									id="doc_obs" name="observacao"
									style="overflow: auto; height: 110px; width: 585px;" rows="11"></textarea></td>

						</tr>
						<tr class="formConsulta" style="width: 829px;">
							<input id="origemDocAutorizacao" name="origemDocAutorizacao"
								style="width: 100px;" maxlength="30" type="hidden"
								validate="numero" value=${lancamento.origemDocAutorizacao } />
							<!-- </td> -->
							<td style="width: 100px;"></td>
							<td style="width: 100px;"></td>

						</tr>
						<tr>
							<td colspan="4" align="center">

								<div class="barraBts">
									<a
										href="javascript:document.getElementById('IncluirDevolucaoForm').submit();">Confirmar</a>
								</div> <!-- input type="submit" value="Confirmar Lançamento" /> -->
							</td>
						</tr>

					</table>
				</div>
			</form>
		</div>


		<div id="lancamentoperdao" style="height: 267px;">
			<form id="IncluirPerdaoForm" method="post"
				action="../papem23/incluirPerdao.do">

				<div class="subtituloEnfase">Incluir Lançamento de Perdão de
					Dívida</div>
				<!-- Formulário -->

				<div class="corpoSubTituloEnfase">
					<table width="100%" border="0">
						<tr class="formConsulta" style="height: 25px;">
							<td id="linha_id_doc_perdao_divida" title="Campo requerido"
								class="branco" style="width: 200px;" width="243">Identificação
								do Documento *</td>
							<td id="input_id_doc_perdao_divida" title="Campo requerido"
								style="width: 105px;"><select id="tipo_doc"
								name="tipoDocAutorizacao" style="width: 100px;">
									<option value="" selected="selected"></option>
									<option value="Ofí­cio">Ofício</option>
									<option value="Ofício do Banco">Ofí­cio do Banco</option>
									<option value="Mensagem">Mensagem</option>
									<option value="CI">CI</option>
									<option value="CP">CP</option>
									<option value="OS">OS</option>
							</select></td>
							<td id="linha_data_doc_perdao_divida" title="Campo requerido"
								class="branco" style="width: 150px;">Data do Documento *</td>
							<td id="input_data_doc_perdao_divida" title="Campo requerido"
								style="width: 100px;"><input id="perdao_divida"
								name="dataDocAutorizacao" style="width: 100px;" value=""
								maxlength="10" type="text" validate="diamesano" /></td>

						</tr>
						<tr class="formConsulta" style="height: 25px;">
							<td id="linha_ordem_bancaria" title="Campo requerido"
								class="branco" style="width: 200px;" width="243">Nº  do
								Documento*</td>
							<td id="input_ordem_bancaria" title="Campo requerido"
								style="width: 105px;"><input id="data_bancaria"
								name="numeroDoc" style="width: 100px;" maxlength="10"
								type="text" validate="numero" /></td>
							<td id="linha_valor_perdao_divida" title="Campo requerido"
								class="branco" style="width: 200px;">Valor do Lançamento *</td>
							<td id="input_valor_perdao_divida" title="Campo requerido"
								style="width: 105px;"><input id="valor_perdao_divida"
								name="valor" style="width: 100px;" maxlength="10" type="text"
								value="" validate="dinheiro" /></td>

						</tr>
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 100px;">Observação</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 459px;" maxlength="600" colspan="3"><textarea
									id="doc_obs" name="observacao"
									style="overflow: auto; height: 110px; width: 441px;" rows="11"></textarea></td>

						</tr>
						<tr>
							<td colspan="4" align="center">

								<div class="barraBts">
									<a
										href="javascript:document.getElementById('IncluirPerdaoForm').submit();">Confirmar</a>

								</div> <!-- input type="submit" value="Confirmar Lançamento" /> -->
							</td>
						</tr>

					</table>
				</div>
			</form>
		</div>

	</div>


	<!-- RodapÃ© -->
	<!-- 		<div class="clear" style=" height : 42px;"></div>
			<jsp:include page="/default/rodape.jsp" />   -->
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



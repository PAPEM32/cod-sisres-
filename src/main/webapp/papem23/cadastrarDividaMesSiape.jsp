
<%@ page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.sisres.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sisres.utilitaria.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.or/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%@page import="java.text.SimpleDateFormat"%><html
	xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Cadastrar Dívida do Mês do SIAPE</title>

<jsp:include page="/importacoes.html" />
<script>
jQuery(document).ready( function(){

	jQuery("#limpaForm").click(function() {
		jQuery("input").val("");
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

	<div class="clear"></div>

	<div class="titulo">Cadastrar Dívida do M&ecirc;s do SIAPE</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">
			<div class="subtitulo">Informações sobre a Dívida</div>
			<div class="corpoSubTitulo">


				<form id="formCadastrar" name="formCadastrar"
					action="cadastrarDividaMesSiapeAction.do" method="post">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="tdlabel_SISPAG" title="Campo requerido" class="branco"
								style="width: 120px;">Matrícula SIAPE</td>
							<td id="tdi_SISPAG" title="Campo requerido" style="width: 105px;">
								<input id="SISPAG" name="matFin" style="width: 100px;" value=""
								maxlength="9" type="text" validate="numero" />
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>

						</tr>
					</table>
			</div>

			<%

String processoDataStr = (String) session.getAttribute("processoDataStr");

Date dataAtual = new Date();   
SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy"); 

String dataStr = sdf.format(dataAtual); 
System.out.println(dataAtual);
System.out.println("Processo:"+dataStr);

%>



			<div class="subtitulo">Informações Básicas</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_pgto" class="branco" style="width: 140px;">Processo
							Pagamento *</td>
						<td id="input_pgto" style="width: 60px;"><input
							id="processoDataStr" name="processoDataStr" style="width: 60px;"
							value="<%=processoDataStr %>" maxlength="7" type="text" -/> <input
							id="processoDataStr" name="processoDataStr"
							value="<%=processoDataStr %>" type="hidden" /></td>
						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_nome" class="branco" style="width: 140px;">Nome
							*</td>
						<td id="input_nome" style="width: 255px;"><input id="nome"
							name="nome" style="width: 250px;" value=" " maxlength="60"
							type="text" /></td>
						<td id="linha_cpf" title="Campo requerido" class="branco"
							style="width: 60px;">CPF *</td>
						<td id="input_cpf" title="Campo requerido" style="width: 120px;">
							<input id="cpf" name="cpf" style="width: 205px;" value=" "
							maxlength="11" type="text" />
						</td>
						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_posto" title="Campo requerido" class="branco"
							style="width: 140px;">Função *</td>
						<td id="input_posto" title="Campo requerido" style="width: 255px;">
							<input id="posto" name="posto" style="width: 250px;" value=""
							maxlength="2" type="text" />
						</td>
						<td id="linha_oc" title="Campo requerido" class="branco"
							style="width: 30px;">OC *</td>
						<td id="input_oc" title="Campo requerido" style="width: 400px;">
							<select name="ocCodigo">
								<option selected="selected" value="">Selecione...</option>
								<%
				if(session.getAttribute("listaOc") != null)
				{
					List<OC> listaOC = (ArrayList<OC>)session.getAttribute("listaOc");
					for(int i=0; i<listaOC.size(); i++)
					{
			%>
								<option value="<%=listaOC.get(i).getOc()%>"><%=listaOC.get(i).getOc()%>
									-
									<%=listaOC.get(i).getNome()%></option>;
								<%
					}
				}
			%>
						</select>
						</td>


						<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_situacao" class="branco" style="width: 60px;">Situação
							*</td>
						<td id="input_situacao" style="width: 255px;"><select
							name="situacao">
								<option selected="selected" value="">Selecione...</option>
								<%
				if(session.getAttribute("listaSituacoes") != null)
				{
					List<Situacao> listaSituacoes = (ArrayList<Situacao>)session.getAttribute("listaSituacoes");
					for(int i=0; i<listaSituacoes.size(); i++)
					{
			%>
								<option value="<%=listaSituacoes.get(i).getCodigo()%>"><%=listaSituacoes.get(i).getCodigo()%>
									-
									<%=listaSituacoes.get(i).getNome()%></option>;
								<%
					}
				}
			%>
						</select></td>
					</tr>
				</table>
			</div>
			<div class="subtitulo">Informações Bancárias</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td id="linha_banco" title="Campo requerido" class="branco"
							style="width: 90px;">Banco *</td>
						<td id="input_banco" title="Campo requerido" style="width: 105px;">
							<select name="banco">
								<option selected="selected" value="">Selecione...</option>
								<%
				if(session.getAttribute("listaBancos") != null)
				{
					List<Banco> listaBanco = (ArrayList<Banco>)session.getAttribute("listaBancos");
					for(int i=0; i<listaBanco.size(); i++)
					{
			%>
								<option value="<%=listaBanco.get(i).getCodigo()%>"><%=listaBanco.get(i).getCodigo()%>
									-
									<%=listaBanco.get(i).getNome()%></option>;
								<%
					}
				}
			%>
						</select>

							<td id="linha_agencia" title="Campo requerido" class="branco"
							style="width: 100px;">Agência *</td>
							<td id="input_agencia" title="Campo requerido"
							style="width: 100px;"><input id="agencia" name="agencia"
								style="width: 95px;" value="" maxlength="4" type="text" /> <input
								id="agencia" name="agencia" value="" type="hidden" /></td>

							<td id="linha_cc" title="Campo requerido" class="branco"
							style="width: 100px;">Conta-Corrente *</td>
							<td id="input_cc" title="Campo requerido" style="width: 120px;">
								<input id="cc" name="contaCorrente" style="width: 115px;"
								value="" maxlength="11" type="text" /> <input id="cc"
								name="contaCorrente" value="" type="hidden" />
						</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_valor_divida" title="Campo requerido" class="branco"
							style="width: 100px;">Valor da Dívida *</td>



						<td id="input_valor_divida" title="Campo requerido"
							style="width: 105px;"><input id="valor_divida"
							name="valorDividaStr" style="width: 100px;" value=""
							maxlength="15" type="text" /></td>
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
						<td id="input_origem_doc" title="Campo requerido"
							style="width: 200px;" colspan="3"><select id="origemDocAut"
							name="origemDocAut" style="width: 300px;">
								<option value="">-</option>
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
								//		if (ocDividaOrigem.getOc().equals(ocCodigo)) {	out.print(" selected");   }	
					%>><%=ocDividaOrigem.getOc()+" - "+ocDividaOrigem.getNome()%></option>
								<%				} //fecha if 
								} // fecha loop lista de motivo
					%>
								<%
							} //fecha if teste de request
					%>
						</select> <!-- input id="origemDocAut" name="origemDocAut" style="width:95px;" value=""
				maxlength="3" type="text" validate="numero" /--></td>
						<td style="border: 1px solid #fff;" colspan="3">&nbsp;</td>

					</tr>
					<tr class="formConsulta">
						<td id="linha_tipo_doc" title="Campo requerido" class="branco"
							style="width: 60px;">Tipo *</td>
						<td id="input_tipo_doc" title="Campo requerido"
							style="width: 100px;"><select id="tipo_doc"
							name="tipoDocAut" style="width: 100px;">
								<option value="">-</option>
								<option value="Ofí­cio">Ofício</option>
								<option value="Mensagem">Mensagem</option>
								<option value="CI">CI</option>
								<option value="CP">CP</option>
								<option value="Papeleta">Papeleta</option>
								<option value="OS">OS</option>
						</select> <%	
			//String [] listTipoDocOrigem = {"oficio", "mensagem", "ci", "papeleta" , "os"};
			
			//out.println ("<select id='tipo_doc' name='tipoDocOrigem' style='width:100px;'>");
		
			//if ((tipoDocOrigem != null)) {
			//	out.println ("<option value='UNKNOWN' selected='selected'>UNKNOWN</option>");
			//}
			//for (int i =0; i < listTipoDocOrigem.length; i++)
			//{
			//	out.println ("<option value='" + listTipoDocOrigem[i] + "'"); 
			//      if ( listTipoDocOrigem[i].equals(tipoDocOrigem) )
			//	{
			//		out.println (" selected");
			//	}
			//	out.println (">" + tipoDocOrigem[i] + "</option>");
%> <%
			//}
			//out.println ("</select></td>");
%>
							<td id="linha_numero_doc" title="Campo requerido" class="branco"
							style="width: 60px;">N&uacute;mero *</td>
							<td id="input_numero_doc" title="Campo requerido"
							style="width: 100px;"><input id="numero_doc"
								name="numeroDocAut" style="width: 200px;" value=""
								maxlength="50" type="text" /></td>

							<td id="linha_data_doc" title="Campo requerido" class="branco"
							style="width: 60px;">Data *</td>
							<td id="input_data_doc" title="Campo requerido"
							style="width: 100px;"><input id="data_doc" name="dataDocAut"
								style="width: 95px;" value="" maxlength="10" type="text"
								validate="diamesano" /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>

				</table>
			</div>
			<div class="subtitulo">Informações Complementares</div>
			<div class="corpoSubTitulo">
				<table width="100%" border="0">
					<tr class="formConsulta">
						<td class="branco" id="linha_motivo" style="width: 60px;"
							title="Campo requerido">Motivo *</td>
						<td colspan="" id="input_motivo" style="width: 300px"
							title="Campo requerido">
							<!-- 			<input type="text" id="motivo" name="motivo" style="width:300px" maxlength="50" value="${param.motivo}" />
 --> <select id="input_motivo" name="motivo" style="width: 300px;">
								<option value="">-</option>
								<%
						if (session.getAttribute("listaMotivo") != null){							
							ArrayList<Motivo> listaMotivo = (ArrayList<Motivo>)session.getAttribute("listaMotivo");
					%>
								<%
								for(Motivo motivoDivida:listaMotivo){ //loop lista de motivo
									if (motivoDivida != null) {										
					%>
								<option value="<%=motivoDivida.getNome()%>"
									<%
	//									if (motivoDivida.getNome().equals(motivo)) {	out.print(" selected");   }	
					%>><%=motivoDivida.getNome()%></option>
								<%				} //fecha if 
								} // fecha loop lista de motivo
					%>
								<%
							} //fecha if teste de request
					%>
						</select>
						</td>
						<td class="branco" id="linha_data_motivo" style="width: 100px;"
							title="Campo requerido">Data do Motivo *</td>
						<td id="input_data_motivo" style="width: 50px;"
							title="Campo requerido"><input id="dataMotivo"
							name="dataMotivo" style="width: 105px;" value="" maxlength="10"
							type="text" validate="diamesano" /></td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>
					<tr class="formConsulta">
						<td id="linha_observacao" title="Campo requerido" class="branco"
							style="width: 100px;">Observação *</td>
						<td id="input_observacao" title="Campo requerido"
							style="width: 300px" colspan="2"><input type="text"
							id="observacao" name="observacao" style="width: 300px"
							maxlength="50" value="" /></td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
						<td style="border: 1px solid #fff;">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="barraBts">
				<a
					href="javascript:document.getElementById('formCadastrar').submit();">Cadastrar</a>
				<a href="javascript:void(0)" onclick="jQuery('input').val('');"
					id="limpaForm">Limpar Formulário</a>
			</div>
		</div>
	</div>



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
<%@ page import="com.sisres.model.Divida"%>
<%@ page import="com.sisres.model.Pessoa"%>
<%@ page import="com.sisres.model.Lancamento"%>
<%@ page import="com.sisres.utilitaria.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sisres.model.*"%>

<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%//@taglib uri="http://java.sun.com/jsf/core" prefix="c" %>


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8" />
<title>SisRes - Consultar Reversão</title>

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

	<div class="clear"></div>

	<div class="titulo">Consultar Reversão</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">
			<div class="subtitulo">Informações sobre a Reversão</div>

			<form id="formConsultaReversao" action="consultarReversaoAction.do"
				method="get">
				<input type="hidden" name="pagina" value="" />
				<input type="hidden" name="registrosPorPagina" value="" />
				<%
		// Criado uma dÃ­vida na session para persistir dos dados do filtro nas pÃ¡ginas subsequentes
			Divida dividaFiltro = (Divida) session.getAttribute("reversaoFiltro"); 
			if(dividaFiltro != null){    
				Pessoa pessoa = dividaFiltro.getPessoa();
				Pedido pedido = dividaFiltro.getPedido();
			}// teste para verificar nova funcionalidade 
		%>

				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsultaReversao">
							<td id="linha_cod_reversao" title="C&oacute;digo da Reversão"
								class="branco" style="width: 100px;">C&oacute;digo da
								Reversão</td>
							<td id="input_cod_reversao" title="C&oacute;digo da Reversão"
								style="width: 80px;"><input id="codReversao"
								name="codReversao" style="width: 125px;" maxlength="7"
								type="text" validate="numero"
								<% if((dividaFiltro != null) && (dividaFiltro.getPedido().getCodigo()!= null)){ %>
								value="<%=dividaFiltro.getPedido().getCodigo()%>" <%}else{ %>
								value="" <%} %> /></td>

							<td id="linha_tipo_divida" title="Tipo da d&iacute;vida"
								class="branco" style="width: 90px;">Tipo</td>
							<td title="Tipo da d&iacute;vida" style="width: 130px;"><select
								id="tipoDivida" name="tipoDivida">
									<option value="">-</option>
									<%if(dividaFiltro != null){   %>
									<%if(dividaFiltro.getTipo() != null) {%>
									<option value="<%=dividaFiltro.getTipo()%>" selected="selected"><%=dividaFiltro.getTipo()%></option>
									<%}%>
									<%}%>
									<option id="tipoMes" value="Mês"
										onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
										Mês</option>
									<option id="tipoHist" value="Histórica"
										onclick="$('perDHistTitle').appear();$('perDHistBody').appear();$('docEnvioTitle').appear();$('docEnvioBody').appear();">
										Histórica</option>
							</select>

								<td id="linha_banco" title="Banco" class="branco"
								style="width: 90px;">Banco</td>
								<td title="Banco" style="width: 130px;"><select
									id="codBanco" name="codBanco" style="width: 300px;">
										<option value="">-</option>
										<%
								if (session.getAttribute("listaBanco") != null){							
									ArrayList<Banco> listaBanco = (ArrayList<Banco>)session.getAttribute("listaBanco");
							%>
										<%
									for(Banco banco:listaBanco){ 
										if (listaBanco != null) {										
							%>
										<option value="<%=banco.getId()%>">
											<%=banco.getCodigo()+ " - " + banco.getNome()%>
										</option>
										<%			} //fecha if 
									} // fecha loop lista de motivo
								%>
										<%
								} //fecha if teste de request
								%>
								</select>
						</tr>
						<tr class="formConsultaReversao">
							<td id="Ano" title="Ano" class="branco" style="width: 120px;">Ano</td>
							<td id="ano" title="ano" style="width: 90px;"><input
								id="ano" name="ano" style="width: 120px;" maxlength="4"
								type="text" validate="numero"
								<%if ((dividaFiltro != null) && (dividaFiltro.getPedido().getAno() != 0)){ %>
								value="<%=dividaFiltro.getPedido().getAno()%>" <%}else{ %>
								value="" <%} %> /></td>
							<td id="dataReversao" title="dataReversão" class="branco"
								style="width: 120px;">Data Pedido</td>
							<td id="input_dataReversao" title="data de Reversão"
								style="width: 90px;"><input id="dataReversao"
								name="dataReversao" style="width: 120px;" maxlength="10"
								type="text" validate="diamesano"
								<%if((dividaFiltro != null) && dividaFiltro.getPedido().getRespData()!= null){ 
		 					System.out.println(" data da reversao" + dividaFiltro.getPedido().getRespData());
							DateFormat dfD = new SimpleDateFormat("dd/MM/yyyy");
							System.out.println(dfD.format(dividaFiltro.getPedido().getRespData()));			
						%>
								value="<%=dividaFiltro.getPedido().getRespData()%>" <%}else{ %>
								value="" <%} %> /></td>
						</tr>
					</table>
				</div>
				<div class="barraBts">
					<!--<input type="submit" value="Filtrar" /> -->
					<!--<a href="javascript:clear_form_elements(document.getElementById('formConsultaReversao'));">Limpar Formulário</a> -->
					<!--<input type="reset" value="Limpar Formulário" onclick="javascript:clear_form_elements(document.getElementById('formConsultaReversao'));"/>-->
					<a href="javascript:consultar('filtro');">Filtrar</a>
					<!-- <a href="javascript:document.getElementById('formConsulta').reset();"></a>-->
					<a
						href="javascript:clear_form_elements(document.getElementById('formConsultaReversao'));">Limpar
						Formulário</a>
				</div>
			</form>
			<%
ArrayList<Divida> listDivida = (ArrayList<Divida>)session.getAttribute("listReversao");
if (listDivida!= null && !listDivida.isEmpty()) {
	request.setAttribute("listDivida", listDivida);
%>
			<div class="subtituloEnfase">Pedidos de Reversão Encontrados:</div>
			<div class="corpoSubTituloEnfase">
				<display:table name="listDivida" pagesize="15" class="displaytag">
					<display:column property="pedido.codigo" title="Código do Pedido"
						style="width:60px;text-align:center"></display:column>
					<display:column property="pessoa.matFin"
						title="Matrícula Financeira" style="width:90px;text-align:center" />
					<display:column property="pessoa.matSIAPE" title="Matrícula SIAPE"
						style="width:90px;text-align:center" />
					<display:column property="pessoa.nome" title="Nome"
						style="width:180px;text-align:center" />
					<display:column property="processoInicioStr" title="Mês"
						style="width:90px;text-align:center" />
					<display:column property="pedido.ano" title="Ano"
						style="width:100px;text-align:center" />
					<display:column property="banco" title="Banco"
						style="width:120px;text-align:center" />
					<display:column property="agencia" title="Agência"
						style="width:120px;text-align:center" />
					<display:column property="contaCorrente" title="Conta"
						style="width:90px;text-align:center" />
					<display:column property="valor" title="Valor"
						style="width:90px;text-align:right" format="R$  {0,number,0.00}" />
				</display:table>
			</div>
			<%	
}
%>
		</div>
	</div>
	<jsp:include page="/default/rodape.jsp" />
	<script language="javascript">
getDimensao();    //captura dimensão da tela do browser;
ajustaContainer();//ajusta container para a largura mÃ­nima;
maskDo();
init();

function consultar(paginacao)
{
	document.getElementById('formConsultaReversao').submit();
}
function clear_form_elements(ele) {

    tags = ele.getElementsByTagName('input');
    for(i = 0; i < tags.length; i++) {
        switch(tags[i].type) {
            case 'text':
                tags[i].value = '';
                break;
            case 'radio':
                tags[i].checked = false;
                break;
        }
    }
   
    tags = ele.getElementsByTagName('select');
    for(i = 0; i < tags.length; i++) {
        if(tags[i].type == 'select-one') {
            tags[i].selectedIndex = 0;
        }
        else {
            for(j = 0; j < tags[i].options.length; j++) {
                tags[i].options[j].selected = false;
            }
        }
    }
//    var tableRef = document.getElementById('myTableConsultaReversao');
 //   while ( tableRef.rows.length > 0 ) 
 //   {
 //    tableRef.deleteRow(0);
 //   }
    
    	   
}

</script>
</body>
</html>
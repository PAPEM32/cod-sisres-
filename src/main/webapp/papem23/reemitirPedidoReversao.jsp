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
<title>SisRes - Reemitir Pedido de Reversão</title>

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

	<div class="titulo">Reemitir Pedido de Reversão</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">
			<div class="subtitulo">Informações sobre a Reversão</div>
			<!-- xxxx -->
			<form id="formFiltrarPedidoReversao"
				action="reemitirPedidoReversaoAction.do" method="post">

				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsultaReversao">
							<td id="linha_cod_reversao" title="C&oacute;digo da Reversão"
								class="branco" style="width: 100px;">C&oacute;digo da
								Reversão</td>
							<td id="input_cod_reversao" title="C&oacute;digo da Reversão"
								style="width: 80px;"><input id="codigoPedidoReversao"
								name="codigoPedidoReversao" style="width: 125px;" maxlength="7"
								type="text" validate="numero" /></td>

							<td id="Ano" title="Ano" class="branco" style="width: 120px;">Ano</td>
							<td id="ano" title="ano" style="width: 90px;"><input
								id="anoReversao" name="anoReversao" style="width: 120px;"
								maxlength="4" type="text" validate="numero" /></td>


						</tr>
					</table>
					<input type="hidden" name="acao" value="filtrarPedidoReversao" />
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
//ArrayList<Divida> listaDividasPedidoReversao = (ArrayList<Divida>)session.getAttribute("listaDividasPedidoReversao");
String[][] listaDividasPedidoReversao = (String[][])session.getAttribute("listaDividasPedidoReversao");
//if (listaDividasPedidoReversao!= null && !listaDividasPedidoReversao.isEmpty()) {
	if (listaDividasPedidoReversao!= null) {
	request.setAttribute("listaDividasPedidoReversao", listaDividasPedidoReversao);
%>
			<div class="subtituloEnfase">Pedidos de Reversão Encontrados:</div>
			<div class="corpoSubTituloEnfase">
				<display:table name="listaDividasPedidoReversao" pagesize="15"
					class="displaytag">
					<display:column property="[5]" title="Código do Pedido"
						style="width:60px;text-align:center"></display:column>
					<display:column property="[11]" title="Matrícula Financeira"
						style="width:90px;text-align:center" />
					<display:column property="[18]" title="Matrícula SIAPE"
						style="width:90px;text-align:center" />
					<display:column property="[12]" title="Nome"
						style="width:180px;text-align:center" />
					<display:column property="[14]" title="Mês"
						style="width:90px;text-align:center" />
					<display:column property="[6]" title="Ano"
						style="width:100px;text-align:center" />
					<display:column property="[4]" title="Banco"
						style="width:120px;text-align:center" />
					<display:column property="[1]" title="Agência"
						style="width:120px;text-align:center" />
					<display:column property="[2]" title="Conta"
						style="width:90px;text-align:center" />
					<display:column property="[10]" title="Valor"
						style="width:90px;text-align:right" format="R$  {0,number,0.00}" />
				</display:table>

			</div>
			<form id="formReenviarPedidoReversao"
				action="reemitirPedidoReversaoAction.do" method="post">
				<div class="subtitulo">Enc. Divisão de Controles Especiais</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formMovimentacaoSisres">
							<td id="linha_nome_ord" class="branco" style="width: 60px;">Nome
								*</td>
							<td id="input_nome_ord" style="width: 500px;" colspan='3'><input
								type="text" id="ordenador" name="ordenador" value=""
								style="width: 500px;" maxlength="60" /></td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formMovimentacaoSisres">
							<td id="linha_posto_ord" title="Campo requerido" class="branco"
								style="width: 60px;">Posto *</td>
							<td id="input_posto_ord" title="Campo requerido"
								style="width: 225px;"><input type="text" id="ptOrdenador"
								name="ptOrdenador" value="" style="width: 224px;" maxlength="2" />
							</td>

							<td id="linha_F_ord" title="Campo requerido" class="branco"
								style="width: 50px;">Função *</td>
							<td id="input_F_ord" title="Campo requerido"
								style="width: 225px;"><input type="text" id="fcOrdenador"
								name="fcOrdenador" value="" style="width: 220px;" maxlength="50" />
							</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>

				<div class="subtitulo">Tec. Finanças e Controle</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formMovimentacaoSisres">
							<td id="linha_nome_ag" class="branco" style="width: 60px;">Nome
								*</td>
							<td id="input_nome_ag" style="width: 500px;" colspan='3'><input
								type="text" id="agente" name="agente" value=""
								style="width: 500px;" maxlength="60" /></td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
						<tr class="formMovimentacaoSisres">
							<td id="linha_posto_ag" title="Campo requerido" class="branco"
								style="width: 60px;">Posto *</td>
							<td id="input_posto_ag" title="Campo requerido"
								style="width: 225px;"><input type="text" id="ptAgente"
								name="ptAgente" value="" style="width: 224px;" maxlength="2" />
							</td>
							<td id="linha_F_ag" title="Campo requerido" class="branco"
								style="width: 50px;">Função *</td>
							<td id="input_F_ag" title="Campo requerido" style="width: 225px;">
								<input type="text" id="fcAgente" name="fcAgente" value=""
								style="width: 220px;" maxlength="" />
							</td>
							<td style="border: 1px solid #fff">&nbsp;</td>
						</tr>
					</table>
				</div>
				<input type="hidden" name="acao" value="reemitirPedidoReversao" />
				<div class="barraBts">
					<input type="submit" value="Reemitir Reversão" />
				</div>
			</form>

		</div>
	</div>

	<%	
}
%>
	<!-- </div>
</div> -->
	<jsp:include page="/default/rodape.jsp" />
	<script language="javascript">
getDimensao();    //captura dimensão da tela do browser;
ajustaContainer();//ajusta container para a largura mÃ­nima;
maskDo();
init();

function consultar(paginacao)
{
	document.getElementById('formFiltrarPedidoReversao').submit();
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
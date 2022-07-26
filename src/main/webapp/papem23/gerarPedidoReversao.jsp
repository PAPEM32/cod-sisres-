<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@ page import="com.sisres.model.*"%>
<%@ page import="com.sisres.utilitaria.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Gerar Pedido de Bloqueio e Reversão</title>
<script type="text/javascript"
	src="../library/javascript/scriptaculous/prototype.js"></script>
<jsp:include page="/importacoes.html" />
<script>

function sendRequest(){
    jQuery.ajax({
      url: "../papem23/gerarPedidoReversaoAction.do",
      global: false,
      type: "POST",
      data: jQuery('#gerarPedido').serialize(),
      dataType: "html",
      success: function(msg){
         
          var teste = msg;

          if(teste.toString() == "ok" ){
            jQuery('#gerarPedido').attr('target','_blank');
            jQuery('#gerarPedido').submit();

          }
          else{       	  
              jQuery('#gerarPedido').attr('target','_self');
              jQuery('#gerarPedido').submit();

          }
      },
      error: function(msg){
    	  jQuery('#gerarPedido').attr('target','_self');
          jQuery('#gerarPedido').submit();
          }
   })
   document.getElementById('linkGerarReversao').onclick = function(){ return false }
}
function init()
{
	field = document.getElementsByName('listaDeDividas');
	
	for (i=0; i < field.length; i++)
	{
		if(isIE)
		{
			field[i].onclick = function(){multiCheck(event,field);selectCheckBox(event)}	
		}
		else
		{
			field[i].onclick = function(event){multiCheck(event,field);selectCheckBox(event)}
		}
	}
}
</script>
<script type="text/javascript">

    function toggle(){
	alert('Entrou aqui!!!');
        var checkboxes = document.getElementsByName('listaDeDividas');

            for (var i in checkboxes){
                checkboxes[i].checked = 'FALSE';
            }
         
    }
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

	<!-- Formulário -->
	<!--  <form id="formConsulta" action="../papem23/filtrarPedidoReversaoAction.do" method="post"> -->
	<div class="clear"></div>

	<div class="titulo">Consultar Reversão</div>
	<div id="conteudo" class="corpoTitulo">
		<div id="innerConteudo">
			<div class="subtitulo">Informações para consulta</div>

			<form id="formConsulta"
				action="../papem23/filtrarPedidoReversaoAction.do" method="post">
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">


							<td id="linha_tipo_divida" title="Tipo da d&iacute;vida"
								class="branco" style="width: 90px;">Tipo</td>
							<td title="Tipo da d&iacute;vida" style="width: 150px;"><select
								id="tipo_divida" name="tipoDivida" style="width: 150px;">
									<option id="tipoMes" value="1"
										onclick="$('perDHistTitle').fade({duration:0.3});$('perDHistBody').fade({duration:0.3});$('docEnvioTitle').fade();$('docEnvioBody').fade();">
										Mês</option>
									<option id="tipoHist" value="2"
										onclick="$('perDHistTitle').appear();$('perDHistBody').appear();$('docEnvioTitle').appear();$('docEnvioBody').appear();">
										Histórica</option>
							</select></td>

							<td id="linha_Banco" title="Banco" class="branco"
								style="width: 90px;">Banco</td>

							<td id="linha_Banco" title="Campo requerido"
								style="width: 150px;">
								<!-- input id="banco" name = "banco" style="width:150px;" value=""
			maxlength="10" type="text" validate="numero" /--> <select id="banco"
								name="banco" style="width: 300px;">
									<%
						if (session.getAttribute("listaBanco") != null){							
							ArrayList<Banco> listaBanco = (ArrayList<Banco>)session.getAttribute("listaBanco");
					%>
									<%
								for(Banco banco:listaBanco){ 
									if (listaBanco != null) {										
					%>
									<option value="<%=banco.getId()%>"><%=banco.getCodigo()+ " - " + banco.getNome()%></option>
									<%				} //fecha if 
								} // fecha loop lista de motivo
					%>
									<%
							} //fecha if teste de request
					%>
							</select>
							</td>
							<td style="border: 1px solid #fff;">&nbsp;</td>


						</tr>


					</table>
				</div>

			</form>
			<div class="barraBts">
				<a
					href="javascript:document.getElementById('formConsulta').submit()">Filtrar</a>
			</div>
			<!-- </form>  -->


			<% 
ArrayList<Divida> listDividaReversao = (ArrayList<Divida>)session.getAttribute("listDividaReversao");
String banco = (String) session.getAttribute("banco");
String codTipo = (String) session.getAttribute("codTipo");
if (listDividaReversao!= null && !listDividaReversao.isEmpty()) {
	request.setAttribute("listDividaReversao",listDividaReversao);
	
%>
			<div class="subtituloEnfase">
				Dívidas encontradas:
				<%=listDividaReversao.size() %></div>

			<form id="gerarPedido" name="gerarPedido"
				action="gerarRelatorioReversao.do" method="post">
				<input name="banco" id="banco" value="<%= banco %>" type="hidden" />
				<input name="codTipo" id="codTipo" value="<%= codTipo %>"
					type="hidden" />
				<display:table name="listDividaReversao" class="displaytag"
					id="listDivida" pagesize="200" form="gerarPedido"
					excludedParams="_chk">
					<display:column class="alignCenter">
						<input type="checkbox" name="listaDeDividas"
							value="<%=((Divida)pageContext.getAttribute("listDivida")).getId() %>" />
					</display:column>

					<display:column property="id" sortable="true" title="ID"
						style="text-align:center" />
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
					<display:column property="tipo" title="Tipo da Dívida"
						style="text-align:center" />
					<display:column title="período" style="text-align:center">
						<% String periodo;
			if( ((Divida)pageContext.getAttribute("listDivida")).getMesTermino()!=null || ((Divida)pageContext.getAttribute("listDivida")).getAnoTermino()!=null )
			{
				periodo = ((Divida)pageContext.getAttribute("listDivida")).getMesProcessoGeracao() + "/"+
				((Divida)pageContext.getAttribute("listDivida")).getAnoProcessoGeracao() + " até "+
				((Divida)pageContext.getAttribute("listDivida")).getMesTermino()+"/"+
				((Divida)pageContext.getAttribute("listDivida")).getAnoTermino();
			}
			else
			{
				periodo = ((Divida)pageContext.getAttribute("listDivida")).getMesProcessoGeracao() + "/"+
				((Divida)pageContext.getAttribute("listDivida")).getAnoProcessoGeracao();
			}
				
		%>
						<%=periodo %>
					</display:column>
					<display:column property="valor" title="Valor Atual"
						format="R$  {0,number,0.00}" style="text-align:right" />

				</display:table>
		</div>


		<div class="subtituloEnfase">Dívidas encontradas:</div>
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
					<td id="input_F_ord" title="Campo requerido" style="width: 225px;">
						<input type="text" id="fcOrdenador" name="fcOrdenador" value=""
						style="width: 220px;" maxlength="50" />
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
						name="ptAgente" value="" style="width: 224px;" maxlength="2" /></td>
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
		<!-- </form>	 -->
		<div class="barraBts">
			<!-- a href="javascript:sendRequest();"-->
			<a id="linkGerarReversao" href="javascript:sendRequest();">Gerar
				Reversão</a>
			<!--  <a href="javascript:document.getElementById('gerarPedido').submit()">Gerar Reversão</a>-->
		</div>
	</div>
	<!-- </div> -->

	<% 
			   session.removeAttribute("listDividaReversao");
			   session.removeAttribute("banco"); 
			   session.removeAttribute("codTipo"); 
			%>

	<% } //fecha o if 
%>
	<!-- fecha div #innerConteudo -->
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
	init();
	jQuery("#myTable").tablesorter({widthFixed: true,headers: { 0: { sorter: false}}}).tablesorterPager({container: jQuery("#pager")});
</script>
</html>
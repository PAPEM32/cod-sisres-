<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.sisres.model.*"%>
<%@page import="javax.swing.JFileChooser"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Relatório de Dívida Mensal do SISRES - Data de
	Inclusão</title>
<jsp:include page="/importacoes.html" />
<script>
function sendRequest(){
    jQuery.ajax({
    
      global: false,
      type: "POST",
      data: jQuery('#formRelatorioDividasOcDTInclusao').serialize(),
      dataType: "html",
      success: function(msg){
         
          var teste = msg;
          if(teste.toString() == "ok" ){
        	
            jQuery('#formRelatorioDividasOcDTInclusao').attr('target','_self');
            jQuery('#formRelatorioDividasOcDTInclusao').submit();

          }
          else{
        	  
              jQuery('#formRelatorioDividasOcDTInclusao').attr('target','_blank');
              jQuery('#formRelatorioDividasOcDTInclusao').submit();
              

          }
      },
      error: function(msg){
    	  
    	  jQuery('#formRelatorioDividasOcDTInclusao').attr('target','_self');
          jQuery('#formRelatorioDividasOcDTInclusao').submit();
          }
   })
}
function init()
{
	field = document.getElementsByName('listaDeOC');
	
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
	<html:form action="/oc/geraRelatorioDTInclusao.do" method="post">
		<div class="clear"></div>

		<div class="titulo">Relatório de Dívida Mensal do SISRES - Data
			de Inclusão</div>
		<div id="conteudo" class="corpoTitulo">
			<div id="innerConteudo">
				<div class="subtitulo">Dados do Relatório</div>
				<div class="corpoSubTitulo">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 200px;">Mês/Ano *</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 150px;"><input id="mesAno" name="mesAno"
								style="width: 155px;" value="${param.mesAno}" maxlength="7"
								type="text" validate="mesano" /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_observacao" title="Campo requerido" class="branco"
								style="width: 200px;">Observação</td>
							<td id="input_observacao" title="Campo requerido"
								style="width: 150px;"><textarea id="observacao"
									name="observacao" style="width: 155px; overflow: auto;" rows=8>${param.observacao}</textarea>
							</td>

							<td style="border: 1px solid #fff;">&nbsp;</td>

						</tr>

					</table>

				</div>
				<div id="perDHistTitle" class="subtitulo">Escolha as OC para o
					relatório</div>
				<div id="perDHistBody" class="corpoSubTitulo">
					<%
						if (session.getAttribute("listadeoc") != null){ //teste de request
							ArrayList<OC> listadeoc = (ArrayList<OC>)session.getAttribute("listadeoc");
					%>
					<!-- <table width="100%" border="0">-->
					<table id="myTable" width="100%" border="0" class="displaytag">
						<thead>
							<tr>
								<th style="width: 20px;"><input id="check_all" value="all"
									type="checkbox"
									onclick="checkAll(this,document.getElementsByName('listaDeOC'))" />
								</th>
								<th colspan="2">Selecionar Todos</th>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
								for(OC oc:listadeoc){ //loop lista de oc
									if (oc != null) { //teste de oc vazia
						%>
							<tr class="<%=i%2==0?"odd":"even" %>">
								<td style="width: 20px;"><input name="listaDeOC"
									value="<%=oc.getOc()%>" type="checkbox" /></td>
								<td><%=oc.getOc() %></td>
								<td><%=oc.getNome()%></td>
							</tr>

							<%			} //fecha if do teste de oc
							i++;	
								} // fecha loop lista de oc
						%>
						</tbody>
					</table>
					<%
							} //fecha if teste de request
						%>
				</div>
				<div class="barraBts">

					<html:submit value="Emitir Relatório" />
				</div>
			</div>
		</div>

	</html:form>
	<!-- RodapÃ© -->
	<div class="clear"></div>
	<jsp:include page="/default/rodape.jsp" />

</body>
<!-- Scripts que são ativados após o desenho do Body -->
<script>
	getDimensao();//captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
	maskDo();
	init();
	jQuery("#myTable").tablesorter({widthFixed: true,headers: { 0: { sorter: false}}}).tablesorterPager({container: jQuery("#pager")});
	function consultar() {
		 JFileChooser filechoose = new JFileChooser();     
		 filechoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		 filechoose.showOpenDialog(null); 
		 session.setAttribute("caminho do arquivo",filechoose.getSelectedFile().toString());
		}
	
</script>
</html>

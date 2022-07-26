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

<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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

		<div class="titulo">&nbsp;Alterar Senha</div>

		<div id="conteudo" class="corpoTitulo"
			style="background-image: url('../imagens/linhas.png');">
			<div id="innerConteudo"
				style="background-image: url('../imagens/principal.png'); background-position: right; height: 100%;">
				<br /> <br /> <br /> <br />
				<div id="topo-carrinho">
					<br />
					<html:form action="/default/alterarSenha" method="post">
		                    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspSenha Atual:
		                    <html:password property="senhaAtual" />
						<br />
						<br />
                     		&nbsp&nbsp&nbsp&nbsp&nbsp&nbspDigitar nova senha:  
		                    <html:password property="novaSenha" />
						<br />
						<br />
		                    Confirmar nova senha:
		                    <html:password property="confirmaSenha" />
						<html:submit value="Alterar Senha" />
					</html:form>
					<br />
				</div>

			</div>

		</div>
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
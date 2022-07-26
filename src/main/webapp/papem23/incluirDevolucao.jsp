<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="com.sisres.model.*"%>

<html:html>
<head>
<title></title>
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
	<html:form action="/papem23/incluirDevolucao.do" method="POST">


		<%
    Divida divida = (Divida) session.getAttribute("divida");
    Lancamento lancamento = new Lancamento();
    lancamento.setDividaId(divida.getId());
	session.setAttribute("lancamento", lancamento);
	
    %>
    
    Tipo do Documento:
        <html:text property="tipoDocAutorizacao" />

		</br>
    Data do Documento: <html:text property="dataDocAutorizacao" />
		</br>
    Número da Ordem Bancária:
      	<html:text property="numeroOB" />
		<br />

		<br />Data da Ordem Banc&aacute;ria:
      	<html:text property="dataOB" />
		<br />
    
    Valores:
      	<html:text property="valor" />
		<br />
      
    Observação:	
	  		<html:text property="observacao" />
		<br />
      	
    Origem do Documento de Lançamento:  	
      	<html:text property="origemDocAutorizacao" />${lancamento.origemDocAutorizacao}  
      	<br />




		<html:submit value="Ok" />

	</html:form>
</body>
</html:html>

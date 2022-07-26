<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="com.sisres.model.*"%>

<html:html>
<head>
<title></title>
</head>
<body>

	<html:form action="/papem23/incluirPerdao.do" method="POST">


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
    Valor:
      	<html:text property="valor" />
		<br />
      
    Observação:	
	  		<html:text property="observacao" />
		<br />

		<html:submit value="Ok" />

	</html:form>
</body>
</html:html>

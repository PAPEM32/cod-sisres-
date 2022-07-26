<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Sistema de Responsabilidade</title>
<!-- Bibliotecas Javascript -->

<script type="text/javascript" src="dthr.js"></script>

<script type="text/javascript"
	src="../library/javascript/suckerfish_keyboard.js"></script>
<!--[if lte IE 6]>
<script type="text/javascript" src="../library/javascript/suckerfish_ie.js"></script>
<![endif]-->

<!-- JQuery -->

<script type="text/javascript" src="../library/javascript/jquery_002.js"></script>
<script type="text/javascript" src="../library/javascript/jquery.js"></script>

<!-- Hack pra css leitura cross browser -->
<script type="text/javascript"
	src="../library/javascript/css_browser_selector.js"></script>

<!-- Prototype/Scriptaculous -->
<script type="text/javascript"
	src="../library/javascript/scriptaculous/prototype.js"></script>
<script type="text/javascript"
	src="../library/javascript/scriptaculous/scriptaculous.js"></script>

<!-- Bibliotecas JS particulares do sistema -->
<script type="text/javascript" src="../library/javascript/sisresJS.js"></script>
<script type="text/javascript"
	src="../library/javascript/sisresValidation.js"></script>

<!-- Controladora de eventos JS -->
<script type="text/javascript"
	src="../library/javascript/sisresJScontrol.js"></script>

<!-- Bibliotecas CSS -->
<link href="../library/css/padrao_layout.css" rel="stylesheet"
	type="text/css" media="screen" />

</head>
<body onload="data_atual()">


	<!-- Caso haja algum erro o struts insere: -->
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

	<div class="titulo">&nbsp;</div>
	<div id="conteudo" class="corpoTitulo"
		style="background-image: url('../imagens/linhas.png');">
		<div id="innerConteudo"
			style="background-image: url('../imagens/principal.png'); background-position: right; height: 100%;">

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
</script>
</html>

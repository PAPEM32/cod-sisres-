<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-br">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SisRes - Incluir Lançamento de Reversão</title>

<!-- Bibliotecas Javascript -->
<script type="text/javascript"
	src="library/javascript/suckerfish_keyboard.js"></script>
<!--[if lte IE 6]>
<script type="text/javascript" src="library/javascript/suckerfish_ie.js"></script>
<![endif]-->

<!-- JQuery -->
<script type="text/javascript" src="library/javascript/jquery_002.js"></script>
<script type="text/javascript" src="library/javascript/jquery.js"></script>

<!-- Hack pra css leitura cross browser -->
<script type="text/javascript"
	src="library/javascript/css_browser_selector.js"></script>

<!-- Prototype/Scriptaculous -->
<script type="text/javascript"
	src="library/javascript/scriptaculous/prototype.js"></script>
<script type="text/javascript"
	src="library/javascript/scriptaculous/scriptaculous.js"></script>

<!-- Bibliotecas JS particulares do sistema -->
<script type="text/javascript" src="library/javascript/sisresJS.js"></script>
<script type="text/javascript"
	src="library/javascript/sisresValidation.js"></script>

<!-- Controladora de eventos JS -->
<script type="text/javascript"
	src="library/javascript/sisresJScontrol.js"></script>

<!-- Bibliotecas CSS -->
<link href="library/css/padrao_layout.css" rel="stylesheet"
	type="text/css" media="screen" />

</head>
<body>
	<!-- Container -->
	<div id="overall" class="overall">
		<div id="container" class="container">

			<!-- CabeÃ§alho -->
			<div id="cabecalho" class="cabecalho" style="">
				<div id="logo" class="logo"></div>
				<div id="usuario" class="usuario">
					<b>Nome de Guerra [</b>99990001<b>]</b> ( <a href="#">sair</a> | <a
						href="#">minha conta</a> ) <br><br><br><br><br><b>(</b>
										13:00 <b>)</b> 27/02/2009 
				</div>
			</div>
			<!-- fecha div #cabecalho -->

			<!-- Menu -->
			<div id="menu" class="menu">
				<ul id="nav" class="nav">
					<li><a href="#">Manter D&iacute;vida</a>
						<ul>
							<li><a href="cadastrarDivida.htm">Cadastrar
									D&iacute;vida</a></li>
							<li><a href="#">Confirmar Cadastro</a></li>
							<li><a href="#">Alterar D&iacute;vida</a></li>
							<li><a href="#">Excluir D&iacute;vida</a></li>
							<li><a href="#">Visualizar D&iacute;vidas</a></li>
						</ul></li>
					<li><a href="#">RESPAREG</a>
						<ul>
							<li><a href="#">Tratar D&iacute;vidas</a></li>
							<li><a href="#">Visualizar D&iacute;vidas</a></li>
						</ul></li>
					<li><a href="#">Relatórios</a>
						<ul>
							<li><a href="#">Dívidas do Mês e Históricas por OC</a></li>
							<li><a href="#">Mensal de Movimentação</a></li>
						</ul></li>
				</ul>
			</div>
			<!-- fecha div #menu -->
			<form id="incluirReversaoForm" action="incluirReversaoAction.do"
				method="get">
				<div id="pontoestornar" class="subtituloEnfase">Incluir
					Lançamento de Reversão</div>
				<div class="corpoSubTituloEnfase">
					<table width="100%" border="0">
						<tr class="formConsulta">
							<td id="linha_doc_id" title="Campo requerido" class="branco"
								style="width: 200px;">IdentificaÃ§Ã£o do Doc. de Reversão *</td>
							<td id="input_doc_id" title="Campo requerido"
								style="width: 150px;"><input id="doc_id"
								style="width: 150px;" value="" maxlength="10" type="text" /></td>
							<td id="linha_doc_tipo" title="Campo requerido" class="branco"
								style="width: 150px;">Tipo de Documento *</td>
							<td id="input_doc_tipo" title="Campo requerido"
								style="width: 150px;"><input id="doc_tipo"
								style="width: 150px;" value="" maxlength="10" type="text" /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 200px;">Data do Documento *</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 150px;"><input id="doc_data"
								style="width: 150px;" value="mm/aaaa" maxlength="7" type="text" /></td>
							<td id="linha_doc_id" title="Campo requerido" class="branco"
								style="width: 150px;">Valor *</td>
							<td id="input_doc_id" title="Campo requerido"
								style="width: 150px;"><input id="doc_id"
								style="width: 150px;" value="" maxlength="10" type="text" /></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
						<tr class="formConsulta">
							<td id="linha_doc_data" title="Campo requerido" class="branco"
								style="width: 200px;">Observação *</td>
							<td id="input_doc_data" title="Campo requerido"
								style="width: 150px;" colspan="3"><textarea id="doc_data"
									style="width: 480px; overflow: auto;" rows="11"></textarea></td>
							<td style="border: 1px solid #fff;">&nbsp;</td>
						</tr>
					</table>
				</div>
				<input type="submit" value="Confirmar Lançamento" />
			</form>
			<!-- RodapÃ© -->
			<div class="clear"></div>
			<div class="rodape">&nbsp;</div>
		</div>
		<!-- fecha div #container -->
	</div>
	<!-- fecha div #overall -->
</body>
<!-- Scripts que são ativados após o desenho do Body -->
<script>
	getDimensao();//captura dimensão da tela do browser;
	ajustaContainer();//ajusta container para a largura mÃ­nima;
</script>
</html>
<%@page import="java.util.*"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.acesso.verificacao.*"%>
<%
	String nomeguerra = (String) session.getAttribute("nomeUsuario"); 
	String perfil = (String)session.getAttribute("perfilUsuario");
	String oc = (String) session.getAttribute("codOc");
	Privilegios privilegios = (Privilegios) session.getAttribute("privilegios");
	
%>
<script type="text/javascript" src="dthr.js"></script>
<!-- Container -->
<!-- 
<center>
<div id="wait" class="wait" style="display:none;">
<div
	style="display:block;background-color:#70b151;border:1px solid #355824;color:white;font-size:14px;position:relative; top:40%;width:70%;padding:20px; ">Cadastro
efetuado com sucesso.<br>
<br />
<a href="javascript:avoid();" onclick="$('wait').hide();">OK</a></div>
</div>
</center>
-->

<div id="overall" class="overall">
	<div id="container" class="container">
		<!-- Cabeçalho -->
		<div align="right">
			<div id="horas"
				style="position: relative; top: 95px; color: #fff; right: 30px;">
				<script type="text/javascript" language="JavaScript">

//document.write(  myweekday + month + year + " - " + timeValue); 
</script>
			</div>

		</div>
		<div id="cabecalho" class="cabecalho" style="">
			<div id="logo" class="logo"></div> 
			
			
			<div id="usuario" class="usuario">
				<b>Nome de Guerra [</b><%=nomeguerra %><b>]</b> ( <b> <a
					href="../logout.do">sair</a>
				</b> ) <br></br> <br /> <br /> <br /> <br /> <br />
			</div>
			<!-- fecha div #cabecalho -->
			<!-- Menu -->
			<div id="menu" class="menu">
				<ul id="nav" class="nav">
					<li <%if(!privilegios.verificaNomeItem("GERENCIAR DIVIDA")){ %>
						style="display: none;" <%}%>><a href="#">Gerenciar Dívida</a>
						<ul>
							<li <%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA")){ %>
								style="display: none;" <%} %>><a href="#">Cadastrar
									Dívida do Mês</a>
								<ul>
									<li
										<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA DO MES SISPAG")){ %>
										style="display: none;" <%} %>><a
										href="../sipm/cadastrarDividaMesSispag.do">SISPAG</a></li>
									<li
										<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA DO MES SIAPE")){ %>
										style="display: none;" <%} %>><a
										href="../papem23/cadastrarDividaMesSiape.do">SIAPE</a></li>
								</ul></li>
							<!-- fecha "Cadastrar Dívida do Mês" -->

							<li
								<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA HISTORICA")){ %>
								style="display: none;" <%} %>><a href="#">Cadastrar
									Dívida Histórica</a>
								<ul>
									<li
										<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA HISTORICA SISPAG")){ %>
										style="display: none;" <%} %>><a
										href="../sipm/cadastrarDividaHistoricaSispag.do">SISPAG</a></li>


									<li
										<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA HISTORICA SIAPE")){ %>
										style="display: none;" <%} %>><a
										href="../papem23/cadastrarDividaHistoricaSiape.do">SIAPE</a></li>

								</ul></li>
							<!-- fecha "Cadastrar Dívida Histórica -->


							<li
								<%if(!privilegios.verificaNomeItem("IMPORTAR DADOS DA PAPELETA")) { %>
								style="display: none;" <%} %>><a href="#">Importar
									Dados da Papeleta</a>
								<ul>
									<%-- <li <%if(!privilegios.verificaNomeItem("PAPELETA SISPAG")){ %> style="display:none;" <%} %>>
  				<a href="../papem23/papeletaSISPAG.do">Papeleta SISPAG</a>
    		</li> --%>
									<li <%if(!privilegios.verificaNomeItem("PAPELETA SIAPE")){ %>
										style="display: none;" <%} %>><a
										href="../papem23/papeletaSIAPE.jsp">Papeleta SIAPE</a></li>
								</ul></li>

							<!-- 
		<li style="display:none;"<a href="/OC/insereRespareg.do">Cadastrar Dívida RESPAREG</a> />
        	<li <%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA RESPAREG")){ %> style="display:none;" <%} %>>   
    			<a href="../OC/insereRespareg.do">Cadastrar Dívida RESPAREG</a> 
    			
    			
    		</li> -->
							<!--  fecha "Cadastrar Dívida RESPAREG" -->


							<li <%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO")) {%>
								style="display: none;" <%}%>><a href="#">Confirmar
									Cadastro</a>
								<ul>
									<li
										<%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO HISTORICO")) {%>
										style="display: none;" <%}%>><a
										href="../papem23/confirmarDividaHistoricaEmEspera.do">Dívida
											Histórica</a></li>

									<li
										<%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO MES")) {%>
										style="display: none;" <%}%>><a
										href="../papem23/confirmarDividaMensalEmEspera.do">Dívida
											Mensal</a></li>
									<!-- fecha "Confirmar Cadastro" -->
								</ul></li>

						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("CONSULTAR")) {%>
						style="display: none;" <%}%>><a href="#">Consultar</a>
						<ul>
							<li <%if (!privilegios.verificaNomeItem("CONSULTAR DIVIDA")) {%>
								style="display: none;" <%}%>><a
								href="../oc/criaConsultaDivida.do">Consultar Dívida</a></li>
							<li <%if (!privilegios.verificaNomeItem("CONSULTAR REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/criaConsultaReversao.do">Consultar Reversão</a>
							</li>
							<!-- fecha "CONSULTAR REVERSAO" -->
						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("GERENCIAR REVERSAO")) {%>
						style="display: none;" <%}%>><a href="#">Gerenciar
							Reversão</a>
						<ul>
							<li
								<%if (!privilegios.verificaNomeItem("GERAR PEDIDO DE REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/gerarReversao.do">Gerar Pedido de Reversão</a>
							</li>
							<!-- fecha "Gerar Pedido de Reversão" -->

							<li
								<%if (!privilegios.verificaNomeItem("REEMITIR PEDIDO DE REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/reemitirPedidoReversao.jsp">Reemitir Pedido
									de Reversão</a></li>
							<!-- fecha "Reemitir Pedido de Reversão" -->
						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("RELATORIO")) {%>
						style="display: none;" <%}%>><a href="#">Relatórios</a>
						<ul>
							<li
								<%if (!privilegios.verificaNomeItem("DIVIDAS POR OC DT_DIVIDA")) {%>
								style="display: none;" <%}%>><a
								href="../oc/relatorioDividasOc.do">Dívidas Mensais por OC
									(Data Dívida)</a></li>

							<li
								<%if (!privilegios.verificaNomeItem("DIVIDAS POR OC DT_INCLUSAO")) {%>
								style="display: none;" <%}%>><a
								href="../oc/relatorioDividasOcDTInclusao.do">Dívidas Mensais
									por OC (Data Inclusão)</a></li>

							<li
								<%if (!privilegios.verificaNomeItem("MENSAL DE MOVIMENTACAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/relatorioMovimentacaoSisres.jsp">Mensal de
									Movimentação</a></li>
							<li
								<%if (!privilegios.verificaNomeItem("GERAR ARQUIVO DIVIDA SIPM")) {%>
								style="display: none;" <%}%>><a
								href="../sipm/gerarArquivoSIPM.jsp">Gerar Arquivo de Dívidas
									SIPM</a></li>
						</ul></li>
					<!-- fecha "Relatório" -->
					<li>
					<a href="../default/alterarSenha.jsp">Alterar Senha</a>
					</li>

				</ul>
			</div>

			<!-- fecha div #menu -->
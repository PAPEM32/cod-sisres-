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
		<!-- Cabe�alho -->
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
						style="display: none;" <%}%>><a href="#">Gerenciar D�vida</a>
						<ul>
							<li <%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA")){ %>
								style="display: none;" <%} %>><a href="#">Cadastrar
									D�vida do M�s</a>
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
							<!-- fecha "Cadastrar D�vida do M�s" -->

							<li
								<%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA HISTORICA")){ %>
								style="display: none;" <%} %>><a href="#">Cadastrar
									D�vida Hist�rica</a>
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
							<!-- fecha "Cadastrar D�vida Hist�rica -->


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
		<li style="display:none;"<a href="/OC/insereRespareg.do">Cadastrar D�vida RESPAREG</a> />
        	<li <%if(!privilegios.verificaNomeItem("CADASTRAR DIVIDA RESPAREG")){ %> style="display:none;" <%} %>>   
    			<a href="../OC/insereRespareg.do">Cadastrar D�vida RESPAREG</a> 
    			
    			
    		</li> -->
							<!--  fecha "Cadastrar D�vida RESPAREG" -->


							<li <%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO")) {%>
								style="display: none;" <%}%>><a href="#">Confirmar
									Cadastro</a>
								<ul>
									<li
										<%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO HISTORICO")) {%>
										style="display: none;" <%}%>><a
										href="../papem23/confirmarDividaHistoricaEmEspera.do">D�vida
											Hist�rica</a></li>

									<li
										<%if (!privilegios.verificaNomeItem("CONFIRMAR CADASTRO MES")) {%>
										style="display: none;" <%}%>><a
										href="../papem23/confirmarDividaMensalEmEspera.do">D�vida
											Mensal</a></li>
									<!-- fecha "Confirmar Cadastro" -->
								</ul></li>

						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("CONSULTAR")) {%>
						style="display: none;" <%}%>><a href="#">Consultar</a>
						<ul>
							<li <%if (!privilegios.verificaNomeItem("CONSULTAR DIVIDA")) {%>
								style="display: none;" <%}%>><a
								href="../oc/criaConsultaDivida.do">Consultar D�vida</a></li>
							<li <%if (!privilegios.verificaNomeItem("CONSULTAR REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/criaConsultaReversao.do">Consultar Revers�o</a>
							</li>
							<!-- fecha "CONSULTAR REVERSAO" -->
						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("GERENCIAR REVERSAO")) {%>
						style="display: none;" <%}%>><a href="#">Gerenciar
							Revers�o</a>
						<ul>
							<li
								<%if (!privilegios.verificaNomeItem("GERAR PEDIDO DE REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/gerarReversao.do">Gerar Pedido de Revers�o</a>
							</li>
							<!-- fecha "Gerar Pedido de Revers�o" -->

							<li
								<%if (!privilegios.verificaNomeItem("REEMITIR PEDIDO DE REVERSAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/reemitirPedidoReversao.jsp">Reemitir Pedido
									de Revers�o</a></li>
							<!-- fecha "Reemitir Pedido de Revers�o" -->
						</ul></li>

					<li <%if (!privilegios.verificaNomeItem("RELATORIO")) {%>
						style="display: none;" <%}%>><a href="#">Relat�rios</a>
						<ul>
							<li
								<%if (!privilegios.verificaNomeItem("DIVIDAS POR OC DT_DIVIDA")) {%>
								style="display: none;" <%}%>><a
								href="../oc/relatorioDividasOc.do">D�vidas Mensais por OC
									(Data D�vida)</a></li>

							<li
								<%if (!privilegios.verificaNomeItem("DIVIDAS POR OC DT_INCLUSAO")) {%>
								style="display: none;" <%}%>><a
								href="../oc/relatorioDividasOcDTInclusao.do">D�vidas Mensais
									por OC (Data Inclus�o)</a></li>

							<li
								<%if (!privilegios.verificaNomeItem("MENSAL DE MOVIMENTACAO")) {%>
								style="display: none;" <%}%>><a
								href="../papem23/relatorioMovimentacaoSisres.jsp">Mensal de
									Movimenta��o</a></li>
							<li
								<%if (!privilegios.verificaNomeItem("GERAR ARQUIVO DIVIDA SIPM")) {%>
								style="display: none;" <%}%>><a
								href="../sipm/gerarArquivoSIPM.jsp">Gerar Arquivo de D�vidas
									SIPM</a></li>
						</ul></li>
					<!-- fecha "Relat�rio" -->
					<li>
					<a href="../default/alterarSenha.jsp">Alterar Senha</a>
					</li>

				</ul>
			</div>

			<!-- fecha div #menu -->
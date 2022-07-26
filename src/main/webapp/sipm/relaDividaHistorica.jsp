<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%

//Pego o array da sessão 

  String[][][] div_OC_CC = (String[][][])session.getAttribute("div_OC_CC");
  String matFin = (String) session.getAttribute("matFin");

   session.removeAttribute("div_OC_CC");

%>


<!-- Container -->
<!--CAIXA DE INFORMAÃ‡ÃƒO REFERENTE A DÃVIDAS
QUE EM SEU PERÃODO POSSUAM MAIS DE UMA OC E/OU
INFORMAÃ‡Ã•ES BANCÃRIAS.
ESTA CAIXA DEVERÃ SER GERADA AUTOMATICAMENTE-->
<div id="caixa_informacao"
	style="visibility: visible; position: absolute; background-color: #f4f4f4; border: 2px solid #f00; width: 440px; padding: 10px; text-align: left; top: 30%; right: 15px; z-index: 4;">
	<!-- p>Existem mais de uma OC e/ou informa&ccedil;&otilde;es banc&aacute;rias para a matr&iacute;cula financeira e per&iacute;odo informado. </p-->
	<p>Por favor, selecione a d&iacute;vida a ser cadastrada
		relacionada ao per&iacute;odo, OC e informa&ccedil;&otilde;es
		banc&aacute;rias:</p>

	<ul style="list-style: none;">




		<%
      
      String oc = (String) session.getAttribute("codOc");
      
      oc = oc.substring(0,3);
      
      String CodOc = "";
      
      for(int i = 0; i < div_OC_CC.length; i++){

    		for(int j = 0; j < div_OC_CC[i].length; j++){ 
    		
    		
    	
    			DateFormat formatador = new SimpleDateFormat("MM/yyyy"); 
                
                String dataIni = div_OC_CC[i][j][3].substring(4,6)+"/"+div_OC_CC[i][j][3].substring(0,4);
                String dataFim = div_OC_CC[i][j][4].substring(4,6)+"/"+div_OC_CC[i][j][4].substring(0,4);
            	
                 
                CodOc = div_OC_CC[i][j][0];        
        
                
                CodOc = CodOc.substring(0,3);
                
    			
       %>
		<form name="formDadosBancarios<%=i %><%=j %>"
			action="../sipm/filtrarDividaHistAction.do" method="post">

			<%  if (div_OC_CC[i][j][7].equals("S")){ %>

			<input type="radio" name="periodo" disabled="disabled"> <b>período:
				<%=dataIni %> - <%=dataFim %></b> <span style="color: #f00;">
				&nbsp; DÍVIDA JÁ CADASTRADA </span>

			<% }  else { %>


			<%  if ((oc.equals(CodOc)) ||(request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR"))){ %>

			<input type="radio" name="periodo"
				onclick="formDadosBancarios<%=i %><%=j %>.submit()"> <b>período:
				<%=dataIni %> - <%=dataFim %></b>
			<%  } else { %>
			<li><input type="radio" name="periodo" disabled="disabled">
				<b>período: <%=dataIni %> - <%=dataFim %></b> <span
				style="color: #f00;"> &nbsp; DÍVIDA PERTENCENTE A OUTRA OC </span> <% } 
			          
			          
                }    
			          %> <input type="hidden" name="dataIni"
				value="<%=div_OC_CC[i][j][3]%>"> <input type="hidden"
				name="dataFim" value="<%=div_OC_CC[i][j][4]%>"> <input
				type="hidden" name="oc" value="<%=div_OC_CC[i][j][0]%>"> <input
				type="hidden" name="matFin" value="<%=matFin%>">
				<ul>
					<li><b>OC:</b> <%=div_OC_CC[i][j][2] %></li>
					<%
              	String agencia;
              	if(div_OC_CC[i][j][5]=="")
              	{
              		agencia="Não Disponí­vel";	
              	}
              	else
              	{
              		agencia = div_OC_CC[i][j][5];
              	}
              	String contaCorrente;
              	if(div_OC_CC[i][j][6]=="")
              	{
              		contaCorrente="Não Disponí­vel";
              	}
              	else
              	{
              		contaCorrente=div_OC_CC[i][j][6];
              	}
              	
              	
              %>
					<li><b>Informações Bancárias:</b> Agência: <%=agencia%>
						Conta-Corrente: <%=contaCorrente %></li>
				</ul></li>
		</form>
		<br />


		<% 
             
             }
    		}  %>

	</ul>





	<a href="javascript:void(0)" onclick="jQuery('#corpo').empty();"
		id="limpaForm">Fechar</a>

</div>
<!--FECHA CAIXA DE INFORMAÃ‡ÃƒO-->
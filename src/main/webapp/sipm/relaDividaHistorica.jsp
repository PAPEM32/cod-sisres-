<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%

//Pego o array da sess�o 

  String[][][] div_OC_CC = (String[][][])session.getAttribute("div_OC_CC");
  String matFin = (String) session.getAttribute("matFin");

   session.removeAttribute("div_OC_CC");

%>


<!-- Container -->
<!--CAIXA DE INFORMAÇÃO REFERENTE A DÍVIDAS
QUE EM SEU PERÍODO POSSUAM MAIS DE UMA OC E/OU
INFORMAÇÕES BANCÁRIAS.
ESTA CAIXA DEVERÁ SER GERADA AUTOMATICAMENTE-->
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

			<input type="radio" name="periodo" disabled="disabled"> <b>per�odo:
				<%=dataIni %> - <%=dataFim %></b> <span style="color: #f00;">
				&nbsp; D�VIDA J� CADASTRADA </span>

			<% }  else { %>


			<%  if ((oc.equals(CodOc)) ||(request.getSession().getAttribute("role").equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR"))){ %>

			<input type="radio" name="periodo"
				onclick="formDadosBancarios<%=i %><%=j %>.submit()"> <b>per�odo:
				<%=dataIni %> - <%=dataFim %></b>
			<%  } else { %>
			<li><input type="radio" name="periodo" disabled="disabled">
				<b>per�odo: <%=dataIni %> - <%=dataFim %></b> <span
				style="color: #f00;"> &nbsp; D�VIDA PERTENCENTE A OUTRA OC </span> <% } 
			          
			          
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
              		agencia="N�o Dispon�vel";	
              	}
              	else
              	{
              		agencia = div_OC_CC[i][j][5];
              	}
              	String contaCorrente;
              	if(div_OC_CC[i][j][6]=="")
              	{
              		contaCorrente="N�o Dispon�vel";
              	}
              	else
              	{
              		contaCorrente=div_OC_CC[i][j][6];
              	}
              	
              	
              %>
					<li><b>Informa��es Banc�rias:</b> Ag�ncia: <%=agencia%>
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
<!--FECHA CAIXA DE INFORMAÇÃO-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="org.eclipse.birt.report.engine.api.EngineException"%>
<%@ page import="org.eclipse.birt.report.engine.api.HTMLRenderOption"%>
<%@ page import="org.eclipse.birt.report.engine.api.IReportEngine"%>
<%@ page import="org.eclipse.birt.report.engine.api.IReportRunnable"%>
<%@ page import="org.eclipse.birt.report.engine.api.IRunAndRenderTask"%>
<%@ page import="org.eclipse.birt.report.engine.api.PDFRenderOption"%>
<%@ page import="org.eclipse.birt.report.model.api.AutoTextHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.CellHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.ColumnHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.ElementFactory"%>
<%@ page import="org.eclipse.birt.report.model.api.GridHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.ReportDesignHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.RowHandle"%>
<%@ page
	import="org.eclipse.birt.report.model.api.SimpleMasterPageHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.StyleHandle"%>
<%@ page import="org.eclipse.birt.report.model.api.TextItemHandle"%>
<%@ page
	import="org.eclipse.birt.report.model.api.activity.SemanticException"%>
<%@ page
	import="org.eclipse.birt.report.model.api.command.ContentException"%>
<%@ page
	import="org.eclipse.birt.report.model.api.command.NameException"%>
<%@ page
	import="org.eclipse.birt.report.model.api.elements.DesignChoiceConstants"%>
<%@ page import="org.eclipse.birt.report.model.elements.AutoText"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="com.sisres.utilitaria.Utilitaria"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.sisres.report.BirtEngine"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


</head>
<body>
	<%
try {
	 IReportEngine birtReportEngine   = null;
	 Logger logger           = Logger.getLogger( "org.eclipse.birt" );
	 ReportDesignHandle designHandle  = null;
	 ElementFactory designFactory     = null;
	 String path = application.getRealPath("");

	 String[][] listDivida = (String[][]) request.getSession().getAttribute("listaPedidoReversao");

	 
	 response.setContentType( "application/pdf" ); 
	 response.setHeader ("Content-Disposition","inline; filename=Relatorio.pdf");
	 
	birtReportEngine = BirtEngine.getBirtEngine(application);
	application.log("vou comeÃ§ar agora a montar o relatorio....");
	IReportRunnable design;

		 // para trab no linux
		design = birtReportEngine.openReportDesign(path + "//WEB-INF//classes//com//sisres//report//sample.rptdesign");

		application.log("Abri o design");

		// Variavle que recebe o meu Design para que eu possa trabalhar com ele
		designHandle = (ReportDesignHandle)  design.getDesignHandle();

		// variavel que recebe uma fabrica para que eu possa criar meu rel em tempo de execuÃ§Ã£o
		designFactory = designHandle.getElementFactory();

		GridHandle gridCabeca = designFactory.newGridItem("CabecalhoGrid", 2, 3);

		gridCabeca.setProperty(GridHandle.WIDTH_PROP, "100%");

		CellHandle gridCellHandle = (CellHandle) gridCabeca.getCell(1,1);
		
		// PRIMEIRA LINHA DO TITULO
		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		
		TextTitulo.setContent("<font size='3'>MARINHA DO BRASIL <br> "+
				              "PAGADORIA DE PESSOAL DA MARINHA</font>");
		
		//TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(2);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);
		gridCellHandle.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");
		
		//SEGUNDA LINHA DO TITULO
		TextItemHandle label3 = designHandle.getElementFactory().newTextItem("titulo2");
		CellHandle cell2 = (CellHandle) gridCabeca.getCell(2,1);	
		cell2.getContent().add(label3);
		label3.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		
		if(listDivida[0][7].equals("1")){
			
			label3.setContent("<font size='2'><p>PEDIDO DE BLOQUEIO E REVERSÃO</p></font>");
		} else {
			label3.setContent("<font size='2'></p>PEDIDO DE REVERSÃO</p></font>");
		}

		//label3.setStyleName("cabecalho");
		cell2.setRowSpan(1);
		cell2.setColumnSpan(2);
		cell2.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");
		
		
		//TERCEIRA LINHA DO TITULO
		
		TextItemHandle TextoTitulo = designHandle.getElementFactory().newTextItem("titulo3");
		CellHandle cell3 = (CellHandle) gridCabeca.getCell(3,1);	
		cell3.getContent().add(TextoTitulo);
		TextoTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
			
		TextoTitulo.setContent("<p>Solicito as providÃªncias de V.Sa. para serem revertidos, atravÃ©s de DOC eletrÃ´nico, Ã  conta 170500-8 <br> "+
				               "da Agência MinistÃ©rio da Fazenda (4201.3), do Banco do Brasil S.A. (001), em nome da Pagadoria de Pessoal da Marinha,<br> "+
				               "preenchendo o campo CPF/CGC com código de depÃ³sito 673200000019008-7, o valor total ou saldo <br> "+
				               "em conta dos servidores abaixo relacionado(s). </p> "+
				               "<p>OBS: Solicito, ainda, a V.Sa. devolver uma via deste Pedido de Reversão com informaÃ§Ã£o do(s) valor(es) e <br> " +
				               "da(s) referÃªncia(s) a(s) reversão(Ãµes)(data, nÃºmero de aviso de crÃ©dito e outros dados pertinentes).</p>");


		//TextoTitulo.setStyleName("cabecalho");
		cell3.setColumnSpan(2);
		cell3.setRowSpan(1);
		cell3.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");
		
		ColumnHandle col = (ColumnHandle) gridCabeca.getColumns().get(0);
		 col.setProperty("width", "70%");
		 col              = (ColumnHandle) gridCabeca.getColumns().get(1);
		 col.setProperty("width", "30%");
		
		

		designHandle.getBody().add(gridCabeca);
		
		int qtd = 0;
		int count = 0;

		//for listDivida

		qtd += listDivida.length;
		
		application.log("QTDE: " + qtd);

		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 8, 3 + qtd);

		application.log("criei a grid");
		RowHandle row1 = (RowHandle) gridHandle.getRows().get(0);

		gridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");


		application.log("comeÃ§o o cabeÃ§alho"); 
		//CellHandle 
		gridCellHandle = (CellHandle) gridHandle.getCell(1,1);

		//TextItemHandle 
		TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextTitulo.setContent("<p align='left'>NotificaÃ§Ã£o: "+listDivida[0][5]+"/"+listDivida[0][6]+"</p>");
		
		System.out.println(TextTitulo);
		
		
		//TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(1);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);
		
		
		
		CellHandle cellv = (CellHandle) gridHandle.getCell(1,2);

		TextItemHandle TextV = designHandle.getElementFactory().newTextItem("titulo");
		TextV.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextV.setContent("");
		TextV.setStyleName("cabecalho");
		cellv.setColumnSpan(1);
		cellv.setRowSpan(1);
		cellv.getContent().add(TextV);


		//TextItemHandle 
		label3 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		//CellHandle 
		cell3 = (CellHandle) gridHandle.getCell(2,1);	
		cell3.getContent().add(label3);
		label3.setContent("Dados Bancarios"); //$NON-NLS-1$
		label3.setStyleName("dettd");
		cell3.setColumnSpan(3);
		cell3.setRowSpan(1);

		TextItemHandle label4 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell4 = (CellHandle) gridHandle.getCell(2,4);	
		cell4.getContent().add(label4);
		label4.setContent("Dados Pessoais"); //$NON-NLS-1$
		label4.setStyleName("dettd");
		cell4.setColumnSpan(3);
		cell4.setRowSpan(1);
		
		TextItemHandle label11 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell11 = (CellHandle) gridHandle.getCell(2,7);	
		cell11.getContent().add(label11);
		label11.setContent("Dados da Dívida"); //$NON-NLS-1$
		label11.setStyleName("dettd");
		cell11.setColumnSpan(2);
		cell11.setRowSpan(1);
		


		TextItemHandle label5 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell5 = (CellHandle) gridHandle.getCell(3,1);	
		cell5.getContent().add(label5);
		label5.setContent("Banco"); //$NON-NLS-1$
		label5.setStyleName("dettd");
		cell5.setColumnSpan(1);
		cell5.setRowSpan(1);


		TextItemHandle label6 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell6 = (CellHandle) gridHandle.getCell(3,2);	
		cell6.getContent().add(label6);
		label6.setContent("Agência"); //$NON-NLS-1$
		label6.setStyleName("dettd");
		cell6.setColumnSpan(1);
		cell6.setRowSpan(1);


		TextItemHandle label7 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell7 = (CellHandle) gridHandle.getCell(3,3);	
		cell7.getContent().add(label7);
		label7.setContent("Conta Corrente"); //$NON-NLS-1$
		label7.setStyleName("dettd");
		cell7.setColumnSpan(1);
		cell7.setRowSpan(1);
		
		TextItemHandle label12 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell12 = (CellHandle) gridHandle.getCell(3,4);	
		cell12.getContent().add(label12);
		label12.setContent("Matricula"); //$NON-NLS-1$
		label12.setStyleName("dettd");
		cell12.setColumnSpan(1);
		cell12.setRowSpan(1);
		
		TextItemHandle label13 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell13 = (CellHandle) gridHandle.getCell(3,5);	
		cell13.getContent().add(label13);
		label13.setContent("Nome"); //$NON-NLS-1$
		label13.setStyleName("dettd");
		cell13.setColumnSpan(1);
		cell13.setRowSpan(1);
		
		TextItemHandle label14 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell14 = (CellHandle) gridHandle.getCell(3,6);	
		cell14.getContent().add(label14);
		label14.setContent("CPF"); //$NON-NLS-1$
		label14.setStyleName("dettd");
		cell14.setColumnSpan(1);
		cell14.setRowSpan(1);

		TextItemHandle label9 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell9 = (CellHandle) gridHandle.getCell(3,7);	
		cell9.getContent().add(label9);
		label9.setContent("Mês de Bloqueio"); //$NON-NLS-1$
		label9.setStyleName("dettd");
		cell9.setColumnSpan(1);
		cell9.setRowSpan(1);
	
		TextItemHandle label10 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell10 = (CellHandle) gridHandle.getCell(3,8);	
		cell10.getContent().add(label10);
		label10.setContent("Valor Solicitado (R$)"); //$NON-NLS-1$
		label10.setStyleName("dettd");
		cell10.setColumnSpan(1);
		cell10.setRowSpan(1);



			for(int i = 0;  i < listDivida.length; i++){


				application.log("estou passando pelo for");
				
				String valorDividaStr = Utilitaria.formatarValor("######0.00",listDivida[i][10]);

	

				TextItemHandle lb_BC = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_BC = (CellHandle) gridHandle.getCell(count+4,1);	
				cl_BC.getContent().add(lb_BC);
				lb_BC.setContent(listDivida[i][4].trim()); //$NON-NLS-1$
				lb_BC.setStyleName("campostd");
				cl_BC.setColumnSpan(1);
				cl_BC.setRowSpan(1);

				TextItemHandle lb_AG = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_AG = (CellHandle) gridHandle.getCell(count+4,2);	
				cl_AG.getContent().add(lb_AG);
				lb_AG.setContent(listDivida[i][1]); //$NON-NLS-1$
				lb_AG.setStyleName("campostd");
				cl_AG.setColumnSpan(1);
				cl_AG.setRowSpan(1);


				TextItemHandle lb_CC = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_CC = (CellHandle) gridHandle.getCell(count+4,3);	
				cl_CC.getContent().add(lb_CC);
				lb_CC.setContent(listDivida[i][2]); //$NON-NLS-1$
				lb_CC.setStyleName("campostd");
				cl_CC.setColumnSpan(1);
				cl_CC.setRowSpan(1);

				TextItemHandle lb_MF = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_MF = (CellHandle) gridHandle.getCell(count+4,4);	
				cl_MF.getContent().add(lb_MF);
				lb_MF.setContent(listDivida[i][11]); //$NON-NLS-1$
				lb_MF.setStyleName("campostd");
				cl_MF.setColumnSpan(1);
				cl_MF.setRowSpan(1);
				
				TextItemHandle lb_NM = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_NM = (CellHandle) gridHandle.getCell(count+4,5);	
				cl_NM.getContent().add(lb_NM);
				lb_NM.setContent(listDivida[i][12]); //$NON-NLS-1$
				lb_NM.setStyleName("campostd");
				cl_NM.setColumnSpan(1);
				cl_NM.setRowSpan(1);
				
				TextItemHandle lb_CPF = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_CPF = (CellHandle) gridHandle.getCell(count+4,6);	
				cl_CPF.getContent().add(lb_CPF);
				lb_CPF.setContent(listDivida[i][13]); //$NON-NLS-1$
				lb_CPF.setStyleName("campostd");
				cl_CPF.setColumnSpan(1);
				cl_CPF.setRowSpan(1);
				
				
				TextItemHandle lb_DATA = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_DATA = (CellHandle) gridHandle.getCell(count+4,7);	
				cl_DATA.getContent().add(lb_DATA);
				
				
				
				if((listDivida[i][16] != null) || (listDivida[i][17] != null)) { 
								
				    lb_DATA.setContent(listDivida[i][14]+"/"+listDivida[i][15]+" até "+listDivida[i][16]+"/"+listDivida[i][17]); //$NON-NLS-1$
				} else {
					lb_DATA.setContent(listDivida[i][14]+"/"+listDivida[i][15]);
				}
				
				
				
				lb_DATA.setStyleName("campostd");
				cl_DATA.setColumnSpan(1);
				cl_DATA.setRowSpan(1);

				TextItemHandle lb_VL = designHandle.getElementFactory().newTextItem("campo"+i);
				CellHandle cl_VL = (CellHandle) gridHandle.getCell(count+4,8);	
				cl_VL.getContent().add(lb_VL);
				lb_VL.setContent(valorDividaStr); //$NON-NLS-1$
				lb_VL.setStyleName("campostd");
				cl_VL.setColumnSpan(1);
				cl_VL.setRowSpan(1);

			}
		application.log("Final do for(int j = 0;  j < listDivida.length; j++)");
		
		//ColumnHandle 
		col = (ColumnHandle) gridHandle.getColumns().get(0);
		col.setProperty("width", "22%");

		col = (ColumnHandle) gridHandle.getColumns().get(1);
		col.setProperty("width", "9%");

		col = (ColumnHandle) gridHandle.getColumns().get(2);
		col.setProperty("width", "9%");

		col = (ColumnHandle) gridHandle.getColumns().get(3);
		col.setProperty("width", "9%");

		col = (ColumnHandle) gridHandle.getColumns().get(4);
		col.setProperty("width", "19%");

		col = (ColumnHandle) gridHandle.getColumns().get(5);
		col.setProperty("width", "9%");
		
		col = (ColumnHandle) gridHandle.getColumns().get(6);
		col.setProperty("width", "13%");

		col = (ColumnHandle) gridHandle.getColumns().get(7);
		col.setProperty("width", "10%");



		

		designHandle.getBody().add(gridHandle);

		// create responsÃ¡veis
		
		application.log("tento criar o footer");

		application.log("tento pegar o master page");
		SimpleMasterPageHandle simpleMasterPage = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);

		application.log("Coloco o footer");
		//simpleMasterPage.getPageFooter().add(createFooter(codUsuario));
		application.log("agora vou redenrizar....");
		
		IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
		PDFRenderOption pdfOptions = new PDFRenderOption();
		pdfOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		pdfOptions.setOutputStream(response.getOutputStream());
		task.setRenderOption(pdfOptions);
		//run report
		task.run();
		task.close();

		application.log("Acabou a tarefa");
	} catch (EngineException e) {
		// TODO Bloco catch gerado automaticamente
		e.printStackTrace();
	}  
		
	
%>
</body>
</html>
package com.sisres.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.AutoText;

import com.sisres.model.GerarPedidoReversaoSvc;
import com.sisres.model.SispagException;
import com.sisres.report.BirtEngine;
import com.sisres.utilitaria.Utilitaria;
import com.sisres.view.GerarPedidoReversaoForm;

public class GerarRelPedidoReversaoAction extends Action {

	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger("org.eclipse.birt");
	private ReportDesignHandle designHandle = null;
	private ElementFactory designFactory = null;
	private String path;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();

		// response.setContentType( "application/pdf" );
		// response.setHeader ("Content-Disposition","inline; filename=Relatorio.pdf");

		// context.log("entrei pelo get e vou começar agora....");

		GerarPedidoReversaoForm myForm = (GerarPedidoReversaoForm) request.getSession().getAttribute("myForm");

		try {

			GerarPedidoReversaoSvc GerarPedidoReversaoSvc = new GerarPedidoReversaoSvc();

			String[][] listPedidoReversao = GerarPedidoReversaoSvc.getGerarRelPedidoReversaoList();

			if (listPedidoReversao.length != 0) {
				// Despacha para view

				String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

				// Obtendo o path da aplicação

				path = request.getSession().getServletContext().getRealPath("");
				request.setAttribute("listaPedidoReversao", listPedidoReversao);

				try {

					renderizaTela(listPedidoReversao, codUsuario, context, response, myForm);

					return null;

				} catch (ContentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SemanticException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				errors.add("listaDividaReversao", new ActionMessage("error.listaDividaReversao.empty"));
				if (errors.size() > 0) {
					saveErrors(request, errors);
					return mapping.findForward("erro");
				}
			}
			return mapping.findForward("success");
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		} catch (SispagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void renderizaTela(String[][] listPedidoReversao, String codUsuario, ServletContext context,
			HttpServletResponse resp, GerarPedidoReversaoForm myForm)
			throws ContentException, NameException, SemanticException, IOException {

		this.birtReportEngine = BirtEngine.getBirtEngine(context);

		/*
		 * //get report name and launch the engine resp.setContentType("text/html");
		 * //resp.setContentType( "application/pdf" ); //resp.setHeader
		 * ("Content-Disposition","inline; filename=test.pdf"); String reportName =
		 * req.getParameter("ReportName"); String[] cols =
		 * (String[])req.getParameterMap().get("dyna1"); ServletContext sc =
		 * req.getSession().getServletContext(); this.birtReportEngine =
		 * BirtEngine.getBirtEngine(sc);
		 * 
		 * IReportRunnable design; try { //Open report design design =
		 * birtReportEngine.openReportDesign( sc.getRealPath("/Reports")+"/"+reportName
		 * ); ReportDesignHandle report = (ReportDesignHandle) design.getDesignHandle(
		 * ); buildReport( cols, report );
		 * 
		 * //create task to run and render report IRunAndRenderTask task =
		 * birtReportEngine.createRunAndRenderTask( design ); task.setAppContext(
		 * contextMap );
		 * 
		 * //set output options HTMLRenderOption options = new HTMLRenderOption();
		 * options.setImageHandler( new HTMLServerImageHandler() );
		 * options.setImageDirectory( sc.getRealPath("/images");
		 * options.setBaseImageURL( req.getContextPath() + "/images" );
		 * options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		 * //options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		 * options.setOutputStream(resp.getOutputStream());
		 * task.setRenderOption(options);
		 * 
		 * //run report task.run(); task.close(); }catch (Exception e){
		 * 
		 * e.printStackTrace(); throw new ServletException( e ); }
		 * 
		 */

		// context.log("vou começar agora a montar o relatorio....");
		IReportRunnable design;

		// abro um rptdesign vazio para que eu possa trabalhar com ele
		try {
			// para trab no windows
			// design = birtReportEngine.openReportDesign(path +
			// "/WEB-INF/classes/com/sisres/report/sample.rptdesign");

			// para trab no linux
			design = birtReportEngine
					.openReportDesign(path + "//WEB-INF//classes//com//sisres//report//sample.rptdesign");

//			context.log("Abri o design");

			// Variavle que recebe o meu Design para que eu possa trabalhar com ele
			designHandle = (ReportDesignHandle) design.getDesignHandle();

			// variavel que recebe uma fabrica para que eu possa criar meu rel em tempo de
			// execução
			designFactory = designHandle.getElementFactory();

			createHeader(listPedidoReversao);

			createBody(listPedidoReversao, context);

			// Criando Local de Assinatura dos Responsaveis
			createResponsaveis(myForm);

			// add footer
			// simpleMasterPage.getPageFooter().add(createFooter());

			// context.log("tento criar o footer");

			// context.log("tento pegar o master page");
			SimpleMasterPageHandle simpleMasterPage = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);

			// context.log("Coloco o footer");
			simpleMasterPage.getPageFooter().add(createFooter(codUsuario));

			// incluo o footer para renderizar
			// sc.log("Mando de novo para o detalhe");
			// designHandle.getMasterPages().add(simpleMasterPage);

			// context.log("agora vou redenrizar....");
			/*
			 * //create task to run and render report IRunAndRenderTask task =
			 * birtReportEngine.createRunAndRenderTask( design ); task.setAppContext(
			 * contextMap );
			 * 
			 * //set output options HTMLRenderOption options = new HTMLRenderOption();
			 * options.setImageHandler( new HTMLServerImageHandler() );
			 * options.setImageDirectory( sc.getRealPath("/images");
			 * options.setBaseImageURL( req.getContextPath() + "/images" );
			 * options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			 * //options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			 * options.setOutputStream(resp.getOutputStream());
			 * task.setRenderOption(options);
			 * 
			 */
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
			// task.setAppContext(contextMap);

			// set output options
			// HTMLRenderOption options = new HTMLRenderOption();
			// options.getUrlEncoding();
			// options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);

			PDFRenderOption pdfOptions = new PDFRenderOption();

			// options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			// pdfOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			pdfOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			// options.
			// options.setOutputStream(response.getOutputStream());
			pdfOptions.setOutputStream(resp.getOutputStream());
			task.setRenderOption(pdfOptions);
			// task.setRenderOption(options);

			// run report
			task.run();
			task.close();

			// context.log("Acabou a tarefa");
		} catch (EngineException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}

	}

	private void createBody(String[][] listDivida, ServletContext sc) throws SemanticException {

		int qtd = 0;
		int count = 0;

		// for listDivida

		qtd += listDivida.length;

		sc.log("QTDE: " + qtd);

		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 8, 3 + qtd);

		sc.log("criei a grid");
		RowHandle row1 = (RowHandle) gridHandle.getRows().get(0);

		gridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");

		sc.log("comecei o cabeçalho");
		CellHandle gridCellHandle = (CellHandle) gridHandle.getCell(1, 1);

		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextTitulo.setContent("<p align='left'>Notificação: " + listDivida[0][5] + "/" + listDivida[0][6] + "</p>");

		// System.out.println(TextTitulo);

		// TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(1);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);

		CellHandle cellv = (CellHandle) gridHandle.getCell(1, 2);

		TextItemHandle TextV = designHandle.getElementFactory().newTextItem("titulo");
		TextV.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextV.setContent("");
		TextV.setStyleName("cabecalho");
		cellv.setColumnSpan(1);
		cellv.setRowSpan(1);
		cellv.getContent().add(TextV);

		TextItemHandle label3 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell3 = (CellHandle) gridHandle.getCell(2, 1);
		cell3.getContent().add(label3);
		label3.setContent("Dados Bancarios"); //$NON-NLS-1$
		label3.setStyleName("dettd");
		cell3.setColumnSpan(3);
		cell3.setRowSpan(1);

		TextItemHandle label4 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell4 = (CellHandle) gridHandle.getCell(2, 4);
		cell4.getContent().add(label4);
		label4.setContent("Dados Pessoais"); //$NON-NLS-1$
		label4.setStyleName("dettd");
		cell4.setColumnSpan(3);
		cell4.setRowSpan(1);

		TextItemHandle label11 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell11 = (CellHandle) gridHandle.getCell(2, 7);
		cell11.getContent().add(label11);
		label11.setContent("Dados da Dívida"); //$NON-NLS-1$
		label11.setStyleName("dettd");
		cell11.setColumnSpan(2);
		cell11.setRowSpan(1);

		TextItemHandle label5 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell5 = (CellHandle) gridHandle.getCell(3, 1);
		cell5.getContent().add(label5);
		label5.setContent("Banco"); //$NON-NLS-1$
		label5.setStyleName("dettd");
		cell5.setColumnSpan(1);
		cell5.setRowSpan(1);

		TextItemHandle label6 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell6 = (CellHandle) gridHandle.getCell(3, 2);
		cell6.getContent().add(label6);
		label6.setContent("Agência"); //$NON-NLS-1$
		label6.setStyleName("dettd");
		cell6.setColumnSpan(1);
		cell6.setRowSpan(1);

		TextItemHandle label7 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell7 = (CellHandle) gridHandle.getCell(3, 3);
		cell7.getContent().add(label7);
		label7.setContent("Conta Corrente"); //$NON-NLS-1$
		label7.setStyleName("dettd");
		cell7.setColumnSpan(1);
		cell7.setRowSpan(1);

		TextItemHandle label12 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell12 = (CellHandle) gridHandle.getCell(3, 4);
		cell12.getContent().add(label12);
		label12.setContent("Matricula"); //$NON-NLS-1$
		label12.setStyleName("dettd");
		cell12.setColumnSpan(1);
		cell12.setRowSpan(1);

		TextItemHandle label13 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell13 = (CellHandle) gridHandle.getCell(3, 5);
		cell13.getContent().add(label13);
		label13.setContent("Nome"); //$NON-NLS-1$
		label13.setStyleName("dettd");
		cell13.setColumnSpan(1);
		cell13.setRowSpan(1);

		TextItemHandle label14 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell14 = (CellHandle) gridHandle.getCell(3, 6);
		cell14.getContent().add(label14);
		label14.setContent("CPF"); //$NON-NLS-1$
		label14.setStyleName("dettd");
		cell14.setColumnSpan(1);
		cell14.setRowSpan(1);

		TextItemHandle label9 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell9 = (CellHandle) gridHandle.getCell(3, 7);
		cell9.getContent().add(label9);
		label9.setContent("Mês de Bloqueio"); //$NON-NLS-1$
		label9.setStyleName("dettd");
		cell9.setColumnSpan(1);
		cell9.setRowSpan(1);

		TextItemHandle label10 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell10 = (CellHandle) gridHandle.getCell(3, 8);
		cell10.getContent().add(label10);
		label10.setContent("Valor Solicitado (R$)"); //$NON-NLS-1$
		label10.setStyleName("dettd");
		cell10.setColumnSpan(1);
		cell10.setRowSpan(1);

		for (int i = 0; i < listDivida.length; i++) {

			sc.log("estou passando pelo for");

			String valorDividaStr = Utilitaria.formatarValor("######0.00", listDivida[i][10]);

			TextItemHandle lb_BC = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_BC = (CellHandle) gridHandle.getCell(count + 4, 1);
			cl_BC.getContent().add(lb_BC);
			lb_BC.setContent(listDivida[i][4].trim()); // $NON-NLS-1$
			lb_BC.setStyleName("campostd");
			cl_BC.setColumnSpan(1);
			cl_BC.setRowSpan(1);

			TextItemHandle lb_AG = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_AG = (CellHandle) gridHandle.getCell(count + 4, 2);
			cl_AG.getContent().add(lb_AG);
			lb_AG.setContent(listDivida[i][1]); // $NON-NLS-1$
			lb_AG.setStyleName("campostd");
			cl_AG.setColumnSpan(1);
			cl_AG.setRowSpan(1);

			TextItemHandle lb_CC = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_CC = (CellHandle) gridHandle.getCell(count + 4, 3);
			cl_CC.getContent().add(lb_CC);
			lb_CC.setContent(listDivida[i][2]); // $NON-NLS-1$
			lb_CC.setStyleName("campostd");
			cl_CC.setColumnSpan(1);
			cl_CC.setRowSpan(1);

			TextItemHandle lb_MF = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_MF = (CellHandle) gridHandle.getCell(count + 4, 4);
			cl_MF.getContent().add(lb_MF);
			lb_MF.setContent(listDivida[i][11]); // $NON-NLS-1$
			lb_MF.setStyleName("campostd");
			cl_MF.setColumnSpan(1);
			cl_MF.setRowSpan(1);

			TextItemHandle lb_NM = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_NM = (CellHandle) gridHandle.getCell(count + 4, 5);
			cl_NM.getContent().add(lb_NM);
			lb_NM.setContent(listDivida[i][12]); // $NON-NLS-1$
			lb_NM.setStyleName("campostd");
			cl_NM.setColumnSpan(1);
			cl_NM.setRowSpan(1);

			TextItemHandle lb_CPF = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_CPF = (CellHandle) gridHandle.getCell(count + 4, 6);
			cl_CPF.getContent().add(lb_CPF);
			lb_CPF.setContent(listDivida[i][13]); // $NON-NLS-1$
			lb_CPF.setStyleName("campostd");
			cl_CPF.setColumnSpan(1);
			cl_CPF.setRowSpan(1);

			TextItemHandle lb_DATA = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_DATA = (CellHandle) gridHandle.getCell(count + 4, 7);
			cl_DATA.getContent().add(lb_DATA);

			if ((listDivida[i][16] != null) || (listDivida[i][17] != null)) {

				lb_DATA.setContent(listDivida[i][14] + "/" + listDivida[i][15] + " até " + listDivida[i][16] + "/" //$NON-NLS-1$
						+ listDivida[i][17]);
			} else {
				lb_DATA.setContent(listDivida[i][14] + "/" + listDivida[i][15]);
			}

			lb_DATA.setStyleName("campostd");
			cl_DATA.setColumnSpan(1);
			cl_DATA.setRowSpan(1);

			TextItemHandle lb_VL = designHandle.getElementFactory().newTextItem("campo" + i);
			CellHandle cl_VL = (CellHandle) gridHandle.getCell(count + 4, 8);
			cl_VL.getContent().add(lb_VL);
			lb_VL.setContent(valorDividaStr); // $NON-NLS-1$
			lb_VL.setStyleName("campostd");
			cl_VL.setColumnSpan(1);
			cl_VL.setRowSpan(1);

		}
		sc.log("Final do for(int j = 0;  j < listDivida.length; j++)");

		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);
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

		/*
		 * TextItemHandle legendBody =
		 * designHandle.getElementFactory().newTextItem("legendBody");
		 * legendBody.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		 * legendBody.setContent("<br>"); designHandle.getBody().add(legendBody);
		 * legendBody.setProperty(StyleHandle.PAGE_BREAK_AFTER_PROP, "always");
		 */

	}

	// método utilizado para criar o rodapé do relatério
	private GridHandle createFooter(String codUsuario) throws SemanticException {

		DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		;
		String data = dataFormatada.format(new Date(System.currentTimeMillis()));

		GridHandle dataGridHandle = designFactory.newGridItem("dataGrid", 5, 1);
		dataGridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");
		RowHandle row1 = (RowHandle) dataGridHandle.getRows().get(0);

		CellHandle gridCellHandle = (CellHandle) row1.getCells().get(0);
		TextItemHandle fenix = designHandle.getElementFactory().newTextItem("Fenix");
		fenix.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_PLAIN);
		fenix.setProperty(StyleHandle.FONT_SIZE_PROP, "10pt");
		fenix.setProperty(StyleHandle.FONT_FAMILY_PROP, DesignChoiceConstants.FONT_FAMILY_SERIF);
		fenix.setContent("Usuário: " + codUsuario);
		gridCellHandle.getContent().add(fenix);

		gridCellHandle = (CellHandle) row1.getCells().get(1);
		AutoTextHandle autoTextPage = designHandle.getElementFactory().newAutoText("Pages");
		autoTextPage.setProperty(AutoText.AUTOTEXT_TYPE_PROP, "page-number");
		autoTextPage.setProperty(StyleHandle.FONT_SIZE_PROP, "10pt");
		autoTextPage.setProperty(StyleHandle.FONT_FAMILY_PROP, DesignChoiceConstants.FONT_FAMILY_SERIF);
		gridCellHandle.getContent().add(autoTextPage);

		gridCellHandle = (CellHandle) row1.getCells().get(2);
		TextItemHandle slash = null;
		slash = designHandle.getElementFactory().newTextItem("Slash");
		slash.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_PLAIN);
		slash.setProperty(StyleHandle.FONT_SIZE_PROP, "10pt");
		slash.setProperty(StyleHandle.FONT_FAMILY_PROP, DesignChoiceConstants.FONT_FAMILY_SERIF);
		slash.setContent("/");

		gridCellHandle.getContent().add(slash);
		gridCellHandle = (CellHandle) row1.getCells().get(3);
		AutoTextHandle autoTextTotalPages = designHandle.getElementFactory().newAutoText("Total");
		autoTextTotalPages.setProperty(AutoText.AUTOTEXT_TYPE_PROP, "total-page");
		autoTextTotalPages.setProperty(StyleHandle.FONT_SIZE_PROP, "10pt");
		autoTextTotalPages.setProperty(StyleHandle.FONT_FAMILY_PROP, DesignChoiceConstants.FONT_FAMILY_SERIF);
		gridCellHandle.getContent().add(autoTextTotalPages);

		gridCellHandle = (CellHandle) row1.getCells().get(4);
		TextItemHandle date = designHandle.getElementFactory().newTextItem("Date");
		date.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_PLAIN);
		date.setProperty(StyleHandle.FONT_SIZE_PROP, "10pt");
		date.setProperty(StyleHandle.FONT_FAMILY_PROP, DesignChoiceConstants.FONT_FAMILY_SERIF);
		date.setContent("Criado em " + data);
		date.setProperty(StyleHandle.TEXT_ALIGN_PROP, "right");
		gridCellHandle.getContent().add(date);

		ColumnHandle col = (ColumnHandle) dataGridHandle.getColumns().get(0);
		col.setProperty("width", "45%");
		col = (ColumnHandle) dataGridHandle.getColumns().get(4);
		col.setProperty("width", "45%");
		col = (ColumnHandle) dataGridHandle.getColumns().get(1);
		col.setProperty("width", "2%");
		col = (ColumnHandle) dataGridHandle.getColumns().get(2);
		col.setProperty("width", "1%");
		col = (ColumnHandle) dataGridHandle.getColumns().get(3);
		col.setProperty("width", "2%");

		return dataGridHandle;

	} // endo do footer

	// método utilizado para acrescentar os responsaveis por assinar o relatério
	private void createResponsaveis(GerarPedidoReversaoForm myForm) throws SemanticException {

		GridHandle gridResponsavel = designFactory.newGridItem("gridResponsavel", 1, 1); // colunas: 2 linhas:5
		gridResponsavel.setProperty(GridHandle.WIDTH_PROP, "100%");

		// Dados do Ordenador
		TextItemHandle labelOrdenador = designHandle.getElementFactory().newTextItem("campo");// .newTextItem("titulo");
		CellHandle cell1 = (CellHandle) gridResponsavel.getCell(1, 1);
		cell1.getContent().add(labelOrdenador);
		labelOrdenador.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		labelOrdenador.setContent("<br><br>" + "<table width=100% border=0 align=center>"
				+ "  <tr><td><div align=center>______________________________________________</div></td>"
				+ "    <td>&nbsp;</td>"
				+ "    <td><div align=center>__________________________________________</div></td>" + "  </tr><tr> "
				+ "    <td width=420><div align=center>Enc. Divisão de Controle Especiais</div></td>"
				+ "    <td width=39>&nbsp;</td> "
				+ "    <td width=367><div align:center>Técnico de Finananças e Controle</div></td>" + "  </tr><tr>"
				+ "    <td><div align=center>" + myForm.getPtOrdenador() + " " + myForm.getOrdenador() + "</div></td>"
				+ "    <td>&nbsp;</td> " + "    <td><div align=center>" + myForm.getPtAgente() + " "
				+ myForm.getAgente() + "</div></td>" + "  </tr> " + "  <tr><td><div align=center>"
				+ myForm.getFcOrdenador() + "</div></td> " + "    <td>&nbsp;</td>" + "    <td><div align=center>"
				+ myForm.getFcAgente() + "</div></td> " + "  </tr></table>");

		cell1.setColumnSpan(1);
		cell1.setRowSpan(1);
		cell1.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		ColumnHandle col = (ColumnHandle) gridResponsavel.getColumns().get(0);
		col.setProperty("width", "100%");
		// col.setProperty("align", "center");

		designHandle.getBody().add(gridResponsavel);
	}

	// método utilizado para criar o header do relatorio
	@SuppressWarnings("deprecation")
	private void createHeader(String[][] listDivida) throws SemanticException {

		// System.out.println("Entrei no createHeader");

		GridHandle gridCabeca = designFactory.newGridItem("CabecalhoGrid", 2, 3);

		gridCabeca.setProperty(GridHandle.WIDTH_PROP, "100%");

		CellHandle gridCellHandle = (CellHandle) gridCabeca.getCell(1, 1);

		// PRIMEIRA LINHA DO TITULO
		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);

		TextTitulo.setContent("<font size='3'>MARINHA DO BRASIL <br> " + "PAGADORIA DE PESSOAL DA MARINHA</font>");

		// TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(2);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);
		gridCellHandle.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		// CellHandle cell4 = (CellHandle) gridCabeca.getCell(1,2);

		// IMAGEM
		/*
		 * try {
		 * 
		 * EmbeddedImage image1 = StructureFactory.createEmbeddedImage();
		 * image1.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
		 * image1.setData(load("../imagens/papem_brasao_small.png"));
		 * image1.setName("logo1");
		 * 
		 * ImageHandle image = factory.newImage( null ); CellHandle cell = (CellHandle)
		 * row.getCells( ).get( 0 ); cell.getContent( ).add( image ); image.setURI(
		 * "http://www.eclipse.org/birt/tutorial/multichip-4.jpg" );
		 * 
		 * 
		 * ImageHandle imageHandle =
		 * designHandle.getElementFactory().newImage("handle");
		 * imageHandle.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
		 * imageHandle.setImageName("logo1");
		 * imageHandle.setURI("../imagens/papem_brasao_small.png");
		 * cell4.getContent().add(imageHandle); cell4.setRowSpan(1);
		 * cell4.setColumnSpan(1);
		 * 
		 * /* } catch (IOException e) { e.printStackTrace(); }
		 */

		/*
		 * TextItemHandle imagem =
		 * designHandle.getElementFactory().newTextItem("imagem");
		 * imagem.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		 * 
		 * CellHandle cell4 = (CellHandle) gridCabeca.getCell(1,2);
		 * 
		 * cell4.getContent().add(imagem);
		 * 
		 * imagem.
		 * setContent("<img src='../imagens/papem_brasao_small.png' height='100px' width='75px'>"
		 * );
		 * 
		 * TextTitulo.setStyleName("cabecalho"); cell4.setRowSpan(2);
		 * cell4.setColumnSpan(1);
		 */

		// SEGUNDA LINHA DO TITULO
		TextItemHandle label3 = designHandle.getElementFactory().newTextItem("titulo2");
		CellHandle cell2 = (CellHandle) gridCabeca.getCell(2, 1);
		cell2.getContent().add(label3);
		label3.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);

		if (listDivida[0][7].equals("1")) {

			label3.setContent("<font size='2'><p>PEDIDO DE BLOQUEIO E REVERSÃO</p></font>");
		} else {
			label3.setContent("<font size='2'></p>PEDIDO DE REVERSÃO</p></font>");
		}

		// label3.setStyleName("cabecalho");
		cell2.setRowSpan(1);
		cell2.setColumnSpan(2);
		cell2.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		// TERCEIRA LINHA DO TITULO

		TextItemHandle TextoTitulo = designHandle.getElementFactory().newTextItem("titulo3");
		CellHandle cell3 = (CellHandle) gridCabeca.getCell(3, 1);
		cell3.getContent().add(TextoTitulo);
		TextoTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);

		TextoTitulo.setContent(
				"<p>Solicito as providências de V.Sa. para serem revertidos, através de DOC eletrônico, à conta 170500-8 <br> "
						+ "da Agência Ministério da Fazenda (4201.3), do Banco do Brasil S.A. (001), em nome da Pagadoria de Pessoal da Marinha,<br> "
						+ "preenchendo o campo CPF/CGC com código de depósito 673200000019008-7, o valor total ou saldo <br> "
						+ "em conta dos servidores abaixo relacionado(s). </p> "
						+ "<p>OBS: Solicito, ainda, a V.Sa. devolver uma via deste Pedido de Reversão com informação do(s) valor(es) e <br> "
						+ "da(s) referência(s) a(s) reversão(ões)(data, número de aviso de crédito e outros dados pertinentes).</p>");

		// TextoTitulo.setStyleName("cabecalho");
		cell3.setColumnSpan(2);
		cell3.setRowSpan(1);
		cell3.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		ColumnHandle col = (ColumnHandle) gridCabeca.getColumns().get(0);
		col.setProperty("width", "70%");
		col = (ColumnHandle) gridCabeca.getColumns().get(1);
		col.setProperty("width", "30%");

		designHandle.getBody().add(gridCabeca);

	}

	private byte[] load(String fileName) throws IOException {
		InputStream is = null;

		is = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
		byte data[] = null;
		if (is != null) {
			try {
				data = new byte[is.available()];
				is.read(data);
			} catch (IOException e1) {
				throw e1;
			}
		}
		return data;
	}

}

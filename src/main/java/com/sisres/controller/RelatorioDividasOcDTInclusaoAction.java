package com.sisres.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.AutoText;

import com.sisres.model.RelatorioSisresSvc;
import com.sisres.report.BirtEngine;
import com.sisres.view.RelatorioDividasOcDTInclusaoForm;

public class RelatorioDividasOcDTInclusaoAction extends org.apache.struts.action.Action {
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger("org.eclipse.birt");
	private ReportDesignHandle designHandle = null;
	private ElementFactory designFactory = null;
	private String path;

	public RelatorioDividasOcDTInclusaoAction() {
	}

	@SuppressWarnings("deprecation")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();

		try {

			RelatorioDividasOcDTInclusaoForm myForm = (RelatorioDividasOcDTInclusaoForm) form;
			ArrayList<String> listOC = new ArrayList<String>();
			String[] listaOC = myForm.getListaDeOC();

			for (int i = 0; i < listaOC.length; i++) {
				listOC.add(listaOC[i]);
			}

			RelatorioSisresSvc relatorioSvc = new RelatorioSisresSvc();
			String[][][][] listadeDivida = (String[][][][]) relatorioSvc
					.getDividasPessoasOCDTInclusao(myForm.getMesStr(), myForm.getAnoStr(), listOC);
			if (listadeDivida != null) {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachment; filename=RelatorioDividasPorOCDTInclusao.pdf");

				// Despacha para view
				String obs = myForm.getObservacao();
				String periodo = myForm.getMes() + "/" + myForm.getAno();
				String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

				// Obtendo o path da aplicação
				path = request.getSession().getServletContext().getRealPath("");

				renderizaTela(listadeDivida, periodo, codUsuario, obs, context, response);
				return null;

			} else {
				errors.add("listadeDivida", new ActionMessage("error.listadeDivida.empty"));
				if (errors.size() > 0) {
					saveErrors(request, errors);
				}
				return mapping.findForward("erro");
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			// Record the error

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		}

	}

	private void renderizaTela(String[][][][] listDivida, String periodo, String codUsuario, String obs,
			ServletContext context, HttpServletResponse resp) throws SemanticException, IOException {

		this.birtReportEngine = BirtEngine.getBirtEngine(context);

		context.log("vou começar agora a montar o relatorio data inclusao....");
		context.log("Estou passando pelo projeto da Rigaud....");

		IReportRunnable design;

		// abro um rptdesign vazio para que eu possa trabalhar com ele
		try {
			// para trab no windows
			// design = birtReportEngine.openReportDesign(path +
			// "/WEB-INF/classes/com/sisres/report/sample.rptdesign");

			// para trab no linux
			design = birtReportEngine
					.openReportDesign(path + "//WEB-INF//classes//com//sisres//report//sample.rptdesign");

			// Variavle que recebe o meu Design para que eu possa trabalhar com ele
			designHandle = (ReportDesignHandle) design.getDesignHandle();

			// variavel que recebe uma fabrica para que eu possa criar meu rel em tempo de
			// execução
			designFactory = designHandle.getElementFactory();

			for (int i = 0; i < listDivida.length; i++) {
				createBody(listDivida[i], periodo, context);
			}

			if (!obs.equals("")) {
				createObs(obs);
			}

			// add footer
			SimpleMasterPageHandle simpleMasterPage = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);

			simpleMasterPage.getPageFooter().add(createFooter(codUsuario));

			// incluo o footer para renderizar
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

			// set output options
			PDFRenderOption pdfOptions = new PDFRenderOption();
			pdfOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			pdfOptions.setOutputStream(resp.getOutputStream());
			task.setRenderOption(pdfOptions);

			// run report
			task.run();
			task.close();

		} catch (EngineException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}

	}

	private void createBody(String[][][] listDivida, String periodo, ServletContext sc) throws SemanticException {

		int qtd = 0;
		String mat = null;
		String mat1 = null;
		int count = 0;

		// Pego a qtd de Dívidas pertencentes a uma OC
		for (int j = 0; j < listDivida.length; j++) {
			qtd += listDivida[j].length;
		}

		sc.log("QTDE: " + qtd);

		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 11, 4 + qtd);

		sc.log("criei a grid");
		RowHandle row1 = (RowHandle) gridHandle.getRows().get(0);

		gridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");

		sc.log("comecei o cabeçaalho");
		CellHandle gridCellHandle = (CellHandle) row1.getCells().get(0);

		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextTitulo.setContent("RELATÓRIO DE DÍVIDAS POR OC - Data de Inclusão da Dívida<br> " + "Período: " + periodo);
		TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(11);
		gridCellHandle.setRowSpan(1);

		gridCellHandle.getContent().add(TextTitulo);

		TextItemHandle label_oc = designHandle.getElementFactory().newTextItem("subtitulo1");
		CellHandle cell2 = (CellHandle) gridHandle.getCell(2, 1);
		cell2.getContent().add(label_oc);
		label_oc.setStyleName("octd");
		label_oc.setContent("OC OC");
		cell2.setColumnSpan(11);
		cell2.setRowSpan(1);

		TextItemHandle label3 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell3 = (CellHandle) gridHandle.getCell(3, 1);
		cell3.getContent().add(label3);
		label3.setContent("Dados Pessoais"); //$NON-NLS-1$
		label3.setStyleName("dettd");
		cell3.setColumnSpan(3);
		cell3.setRowSpan(1);

		TextItemHandle label4 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell4 = (CellHandle) gridHandle.getCell(3, 4);
		cell4.getContent().add(label4);
		label4.setContent("Dados da Dívida"); //$NON-NLS-1$
		label4.setStyleName("dettd");
		cell4.setColumnSpan(8);
		cell4.setRowSpan(1);

		TextItemHandle label5 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell5 = (CellHandle) gridHandle.getCell(4, 1);
		cell5.getContent().add(label5);
		label5.setContent("Nome"); //$NON-NLS-1$
		label5.setStyleName("dettd");
		cell5.setColumnSpan(1);
		cell5.setRowSpan(1);

		TextItemHandle label6 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell6 = (CellHandle) gridHandle.getCell(4, 2);
		cell6.getContent().add(label6);
		label6.setContent("Matricula"); //$NON-NLS-1$
		label6.setStyleName("dettd");
		cell6.setColumnSpan(1);
		cell6.setRowSpan(1);

		TextItemHandle label7 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell7 = (CellHandle) gridHandle.getCell(4, 3);
		cell7.getContent().add(label7);
		label7.setContent("CPF"); //$NON-NLS-1$
		label7.setStyleName("dettd");
		cell7.setColumnSpan(1);
		cell7.setRowSpan(1);

		TextItemHandle label8 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell8 = (CellHandle) gridHandle.getCell(4, 4);
		cell8.getContent().add(label8);
		label8.setContent("Documento"); //$NON-NLS-1$
		label8.setStyleName("dettd");
		cell8.setColumnSpan(1);
		cell8.setRowSpan(1);

		TextItemHandle label9 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell9 = (CellHandle) gridHandle.getCell(4, 5);
		cell9.getContent().add(label9);
		label9.setContent("Data"); //$NON-NLS-1$
		label9.setStyleName("dettd");
		cell9.setColumnSpan(1);
		cell9.setRowSpan(1);

		TextItemHandle label10 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell10 = (CellHandle) gridHandle.getCell(4, 6);
		cell10.getContent().add(label10);
		label10.setContent("Motivo"); //$NON-NLS-1$
		label10.setStyleName("dettd");
		cell10.setColumnSpan(1);
		cell10.setRowSpan(1);

		TextItemHandle label11 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell11 = (CellHandle) gridHandle.getCell(4, 7);
		cell11.getContent().add(label11);
		label11.setContent("Valor Original (R$)"); //$NON-NLS-1$
		label11.setStyleName("dettd");
		cell11.setColumnSpan(1);
		cell11.setRowSpan(1);

		TextItemHandle label12 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell12 = (CellHandle) gridHandle.getCell(4, 8);
		cell12.getContent().add(label12);
		label12.setContent("Revertido (R$)"); //$NON-NLS-1$
		label12.setStyleName("dettd");
		cell12.setColumnSpan(1);
		cell12.setRowSpan(1);

		TextItemHandle label13 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell13 = (CellHandle) gridHandle.getCell(4, 9);
		cell13.getContent().add(label13);
		label13.setContent("Devolvido (R$)"); //$NON-NLS-1$
		label13.setStyleName("dettd");
		cell13.setColumnSpan(1);
		cell13.setRowSpan(1);

		TextItemHandle label14 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell14 = (CellHandle) gridHandle.getCell(4, 10);
		cell14.getContent().add(label14);
		label14.setContent("Perdoado (R$)"); //$NON-NLS-1$
		label14.setStyleName("dettd");
		cell14.setColumnSpan(1);
		cell14.setRowSpan(1);

		TextItemHandle label15 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell15 = (CellHandle) gridHandle.getCell(4, 11);
		cell15.getContent().add(label15);
		label15.setContent("À Regularizar (R$)"); //$NON-NLS-1$
		label15.setStyleName("dettd");
		cell15.setColumnSpan(1);
		cell15.setRowSpan(1);

		label_oc.setContent("Organização Centralizadora: " + listDivida[0][0][0]); //$NON-NLS-1$
		// Para cada dívida eu farei um loop para pegar cada detalhe respectivo as
		// dívidas
		for (int j = 0; j < listDivida.length; j++) {

			sc.log("qtd de oc com Pessoa" + listDivida.length);
			sc.log("qtd de dívidas" + listDivida[j].length);

			for (int k = 0; k < listDivida[j].length; k++) {

				sc.log("estou passando pelo for");

				mat = listDivida[j][k][1];

				sc.log(mat);
				sc.log(mat1);

				/* DADOS PESSOAIS */

				if (!mat.equals(mat1)) {

					/*
					 * Posição [1] --> 'Matrícula' [2] --> 'Nome' [3] --> 'CPF' [4] --> 'Número do
					 * Documento' [5] --> 'Data do Documento' [6] --> 'Motivo' [7] --> 'Valor
					 * Original' [8] --> 'Valor Revertido' [9] --> 'Valor Devolvido' [10] --> 'Valor
					 * Perdoado' [11] --> 'Valor a regularizar' Ex.: listDivida[j][k][2] refere-se
					 * ao valor do campo 'Nome'
					 */

					sc.log("Insere diferente");
					sc.log(listDivida[j][k][2]);
					sc.log("Linha: " + count);

					TextItemHandle lb_nome = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_nome = (CellHandle) gridHandle.getCell(count + 5, 1);
					cl_nome.getContent().add(lb_nome);
					lb_nome.setContent(listDivida[j][k][2]); // $NON-NLS-1$
					lb_nome.setStyleName("campostd");
					cl_nome.setColumnSpan(1);
					cl_nome.setRowSpan(1);

					TextItemHandle lb_mat = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_mat = (CellHandle) gridHandle.getCell(count + 5, 2);
					cl_mat.getContent().add(lb_mat);
					lb_mat.setContent(listDivida[j][k][1]); // $NON-NLS-1$
					lb_mat.setStyleName("campostd");
					cl_mat.setColumnSpan(1);
					cl_mat.setRowSpan(1);

					TextItemHandle lb_cpf = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_cpf = (CellHandle) gridHandle.getCell(count + 5, 3);
					cl_cpf.getContent().add(lb_cpf);
					lb_cpf.setContent(listDivida[j][k][3]); // $NON-NLS-1$
					lb_cpf.setStyleName("campostd");
					cl_cpf.setColumnSpan(1);
					cl_cpf.setRowSpan(1);

				} else {
					sc.log("Insere igual");
					sc.log("Linha: " + count + 5);

					TextItemHandle lb_nome = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_nome = (CellHandle) gridHandle.getCell(count + 5, 1);
					cl_nome.getContent().add(lb_nome);
					lb_nome.setContent("-"); //$NON-NLS-1$
					lb_nome.setStyleName("campostd");
					cl_nome.setColumnSpan(1);
					cl_nome.setRowSpan(1);

					TextItemHandle lb_mat = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_mat = (CellHandle) gridHandle.getCell(count + 5, 2);
					cl_mat.getContent().add(lb_mat);
					lb_mat.setContent("-"); //$NON-NLS-1$
					lb_mat.setStyleName("campostd");
					cl_mat.setColumnSpan(1);
					cl_mat.setRowSpan(1);

					TextItemHandle lb_cpf = designHandle.getElementFactory().newTextItem("campo" + k);
					CellHandle cl_cpf = (CellHandle) gridHandle.getCell(count + 5, 3);
					cl_cpf.getContent().add(lb_cpf);
					lb_cpf.setContent("-"); //$NON-NLS-1$
					lb_cpf.setStyleName("campostd");
					cl_cpf.setColumnSpan(1);
					cl_cpf.setRowSpan(1);

				} // fecha-else

				mat1 = mat;
				/*
				 * DADOS DAS DIVIDAS
				 * 
				 * NumberFormatValue currencyValueFormat = new NumberFormatValue();
				 * currencyValueFormat.setCategory("Currency");
				 * currencyValueFormat.setPattern("$#,##0.00");
				 */

				TextItemHandle lb_Doc = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_Doc = (CellHandle) gridHandle.getCell(count + 5, 4);
				cl_Doc.getContent().add(lb_Doc);
				lb_Doc.setContent(listDivida[j][k][4]); // $NON-NLS-1$
				lb_Doc.setStyleName("campostd");
				cl_Doc.setColumnSpan(1);
				cl_Doc.setRowSpan(1);

				TextItemHandle lb_Data = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_Data = (CellHandle) gridHandle.getCell(count + 5, 5);
				cl_Data.getContent().add(lb_Data);
				lb_Data.setContent(listDivida[j][k][5]); // $NON-NLS-1$
				lb_Data.setStyleName("campostd");
				cl_Data.setColumnSpan(1);
				cl_Data.setRowSpan(1);

				TextItemHandle lb_Motivo = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_Motivo = (CellHandle) gridHandle.getCell(count + 5, 6);
				cl_Motivo.getContent().add(lb_Motivo);
				lb_Motivo.setContent(listDivida[j][k][6]); // $NON-NLS-1$
				lb_Motivo.setStyleName("campostd");
				cl_Motivo.setColumnSpan(1);
				cl_Motivo.setRowSpan(1);

				TextItemHandle lb_VL_original = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_VL_original = (CellHandle) gridHandle.getCell(count + 5, 7);
				cl_VL_original.getContent().add(lb_VL_original);
				lb_VL_original.setContent(listDivida[j][k][7]); // $NON-NLS-1$
				lb_VL_original.setStyleName("campostd");
				cl_VL_original.setColumnSpan(1);
				cl_VL_original.setRowSpan(1);

				TextItemHandle lb_revertido = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_revertido = (CellHandle) gridHandle.getCell(count + 5, 8);
				cl_revertido.getContent().add(lb_revertido);
				lb_revertido.setContent(listDivida[j][k][8]); // $NON-NLS-1$
				lb_revertido.setStyleName("campostd");
				cl_revertido.setColumnSpan(1);
				cl_revertido.setRowSpan(1);

				TextItemHandle lb_Devolvido = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_Devolvido = (CellHandle) gridHandle.getCell(count + 5, 9);
				cl_Devolvido.getContent().add(lb_Devolvido);
				lb_Devolvido.setContent(listDivida[j][k][9]); // $NON-NLS-1$
				lb_Devolvido.setStyleName("campostd");
				cl_Devolvido.setColumnSpan(1);
				cl_Devolvido.setRowSpan(1);

				TextItemHandle lb_Perdoado = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_perdoado = (CellHandle) gridHandle.getCell(count + 5, 10);
				cl_perdoado.getContent().add(lb_Perdoado);
				lb_Perdoado.setContent(listDivida[j][k][10]); // $NON-NLS-1$
				lb_Perdoado.setStyleName("campostd");
				cl_perdoado.setColumnSpan(1);
				cl_perdoado.setRowSpan(1);

				TextItemHandle lb_Regularizar = designHandle.getElementFactory().newTextItem("campo" + k);
				CellHandle cl_regularizar = (CellHandle) gridHandle.getCell(count + 5, 11);
				cl_regularizar.getContent().add(lb_Regularizar);
				lb_Regularizar.setContent(listDivida[j][k][11]); // $NON-NLS-1$
				sc.log(listDivida[j][k][11]);
				lb_Regularizar.setStyleName("campostd");
				cl_regularizar.setColumnSpan(1);
				cl_regularizar.setRowSpan(1);

				count++;
				sc.log("count =" + count);

			} // fecha-for(int k = 0; k < listDivida[j].length; k++)

		} // fecha-for(int j = 0; j < listDivida.length; j++)
		sc.log("Final do for(int j = 0;  j < listDivida.length; j++)");
		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);

		col.setProperty("width", "20%");

		col = (ColumnHandle) gridHandle.getColumns().get(1);
		col.setProperty("width", "8%");

		col = (ColumnHandle) gridHandle.getColumns().get(2);
		col.setProperty("width", "9%");

		col = (ColumnHandle) gridHandle.getColumns().get(3);
		col.setProperty("width", "6%");

		col = (ColumnHandle) gridHandle.getColumns().get(4);
		col.setProperty("width", "6%");

		col = (ColumnHandle) gridHandle.getColumns().get(5);
		col.setProperty("width", "12%");

		col = (ColumnHandle) gridHandle.getColumns().get(6);
		col.setProperty("width", "9%");

		col = (ColumnHandle) gridHandle.getColumns().get(7);
		col.setProperty("width", "7%");

		col = (ColumnHandle) gridHandle.getColumns().get(8);
		col.setProperty("width", "7%");

		col = (ColumnHandle) gridHandle.getColumns().get(9);
		col.setProperty("width", "7%");

		col = (ColumnHandle) gridHandle.getColumns().get(10);
		col.setProperty("width", "9%");

		designHandle.getBody().add(gridHandle);

		TextItemHandle legendBody = designHandle.getElementFactory().newTextItem("legendBody");

		legendBody.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);

		legendBody.setContent("<br>");

		designHandle.getBody().add(legendBody);

		legendBody.setProperty(StyleHandle.PAGE_BREAK_AFTER_PROP, "always");

	}

	private void createObs(String obs) throws SemanticException {

		GridHandle gridObs = designFactory.newGridItem("gridObs", 1, 2);

		gridObs.setProperty(GridHandle.WIDTH_PROP, "100%");

		TextItemHandle label_obs = designHandle.getElementFactory().newTextItem("tituloObs");
		CellHandle cell1 = (CellHandle) gridObs.getCell(1, 1);
		cell1.getContent().add(label_obs);
		label_obs.setStyleName("octd");
		label_obs.setContent("Observações");
		cell1.setColumnSpan(1);
		cell1.setRowSpan(1);

		TextItemHandle label_obs_text = designHandle.getElementFactory().newTextItem("tituloObs");
		CellHandle cell2 = (CellHandle) gridObs.getCell(2, 1);
		cell2.getContent().add(label_obs_text);
		label_obs_text.setStyleName("campostd");
		label_obs_text.setContent(obs);
		cell2.setColumnSpan(1);
		cell2.setRowSpan(1);

		ColumnHandle col = (ColumnHandle) gridObs.getColumns().get(0);
		col.setProperty("width", "100%");

		designHandle.getBody().add(gridObs);

	}

	private GridHandle createFooter(String codUsuario) throws SemanticException {

		DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		;
		String data = formatador.format(new Date(System.currentTimeMillis()));

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

		// designHandle.getBody().add(dataGridHandle);

		return dataGridHandle;

	}

}
package com.sisres.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.sisres.model.RelatorioMovimentacaoSisresSvc;
import com.sisres.model.SispagException;
import com.sisres.report.BirtEngine;
import com.sisres.utilitaria.Utilitaria;
import com.sisres.view.RelatorioMovimentacaoSisresForm;

public class RelatorioMovimentacaoSisresAction extends org.apache.struts.action.Action {
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger("org.eclipse.birt");
	private ReportDesignHandle designHandle = null;
	private ElementFactory designFactory = null;
	private String path;

	public RelatorioMovimentacaoSisresAction() {

	}

	@SuppressWarnings("deprecation")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();

		try {
			RelatorioMovimentacaoSisresForm myForm = (RelatorioMovimentacaoSisresForm) form;
			RelatorioMovimentacaoSisresSvc relatorioSvc = new RelatorioMovimentacaoSisresSvc();
			String[][] listaDeMovimentacao = relatorioSvc.getDividasMovimentacao(myForm.getMesStr(),
					(myForm.getAnoStr()).substring(2));
			// pegar a Lista dos valores movimentados no ano
			if (listaDeMovimentacao.length != 0) {
				// Despacha para view
				String periodo = myForm.getMes() + "/" + myForm.getAno();
				String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");
				// Obtendo o path da aplicação
				path = request.getSession().getServletContext().getRealPath("");

				response.setContentType("application/pdf");
//				response.setHeader ("Content-Disposition","inline; filename=Relatorio.pdf");
				response.setHeader("Content-Disposition", "attachment; filename=RelatorioMovimentacaoSisres.pdf");

				renderizaTela(listaDeMovimentacao, periodo, codUsuario, context, response, myForm);
				return mapping.findForward("relatorio");
			} else {
				errors.add("listaMovimentacao", new ActionMessage("error.listaMovimentacao.empty"));
				if (errors.size() > 0) {
					saveErrors(request, errors);
					return mapping.findForward("erro");
				}
			}
			return mapping.findForward("relatorio");
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}
	}

	// método utilizado para renderizar a tela do relatério
	private void renderizaTela(String[][] listaMovimentacao, String periodo, String codUsuario, ServletContext context,
			HttpServletResponse resp, RelatorioMovimentacaoSisresForm myForm) throws SemanticException, IOException {
		ActionErrors errors = new ActionErrors();

		if (listaMovimentacao.length != 0) {
			this.birtReportEngine = BirtEngine.getBirtEngine(context);
			IReportRunnable design;
			// abro um rptdesign vazio para que eu possa trabalhar com ele
			try {
				// para trab no windows
				// design = birtReportEngine.openReportDesign(path +
				// "/WEB-INF/classes/com/sisres/report/sample.rptdesign");

				// para trab no linux
				design = birtReportEngine
						.openReportDesign(path + "//WEB-INF//classes//com//sisres//report//sample.rptdesign");

				// Variavel que recebe o meu Design para que eu possa trabalhar com ele
				designHandle = (ReportDesignHandle) design.getDesignHandle();
				// variavel que recebe uma fabrica para que eu possa criar meu rel em tempo de
				// execução
				designFactory = designHandle.getElementFactory();
				// criando as informações default do relatério;
				createInfoPadrao();
				// Criando o Título do Relatério
				// O padrão apresentado na data passada para o header é mm/dd/aaaa;
				createHeader(myForm.getMesStr() + "/01/" + myForm.getAnoStr(), context);
				/* Se o tipo de relatério for detalhado criar body e total geral */
				if (myForm.getDetalhado().equals("D")) {
					// Criando o Corpo do Relatério
					for (int i = 0; i < listaMovimentacao.length; i++) {
						createBody(listaMovimentacao[i], periodo, context, i + 1);
					}
					// criando total geral
					createTotalGeral(listaMovimentacao);
				} else {
					createTotalGeral(listaMovimentacao);
				}
				// Criando Local de Assinatura dos Responsaveis
				createResponsaveis(myForm);
				// Criando o rodapé do relatério
				SimpleMasterPageHandle simpleMasterPage = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);
				simpleMasterPage.getPageFooter().add(createFooter(codUsuario));
				IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
				PDFRenderOption pdfOptions = new PDFRenderOption();
				pdfOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
				pdfOptions.setOutputStream(resp.getOutputStream());
				task.setRenderOption(pdfOptions);
				task.run();
				task.close();
			} catch (EngineException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}
		} else {
			errors.add("listaMovimentacao", new ActionMessage("error.listaMovimentacao.empty"));
		}

	}

// método utilizado para criar o corpo do relatério
	private void createBody(String[] listaMovimentacao, String periodo, ServletContext sc, int i)
			throws SemanticException {
		int count = 0;
		int totalLinhas = listaMovimentacao.length;
		// declarando a Grid e o tamanho das colunas e das linhas
		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 4, 3 + totalLinhas);
		// criando os labels das colunas
		createLabel(gridHandle);
		// Colunas: 4 - Linhas: 3 + totalLinhas = 3 linhas preenchidas mais o total de
		// linhas de retorno da query

		// criando a segunda linha para colocar os dados da Organização Centralizadora
		TextItemHandle label_oc = designHandle.getElementFactory().newTextItem("subtitulo1");
		CellHandle cell2 = (CellHandle) gridHandle.getCell(2, 1);
		cell2.getContent().add(label_oc);
		label_oc.setStyleName("octd");
		label_oc.setContent("OC OC");
		cell2.setColumnSpan(4);
		cell2.setRowSpan(1);
		label_oc.setContent("Organização Centralizadora: " + listaMovimentacao[0] + " - " + listaMovimentacao[1]); //$NON-NLS-1$
		/*
		 * Pegar os valores de cada OC Posições [0] --> 'OC 004 [1] --> Nome da OC [2]
		 * --> 'Saldo Anterior' [3] --> 'Inclusõs' [4] --> 'Exclusões' [5] --> 'Saldo
		 * Atual'
		 * 
		 * Ex.: listMovimentacao[1] refere-se ao valor do campo 'Saldo Anterior'
		 */

		TextItemHandle lbSaldoAnterior = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clSaldoAnterior = (CellHandle) gridHandle.getCell(count + 4,
				1); /* + 4 pois nessa fase já teremos 3 linha preenchidas} */
		clSaldoAnterior.getContent().add(lbSaldoAnterior);
		lbSaldoAnterior.setContent(Utilitaria.formatarValor("#,###,##0.00", listaMovimentacao[2])); //$NON-NLS-1$
		lbSaldoAnterior.setStyleName("campostd");
		clSaldoAnterior.setColumnSpan(1);
		clSaldoAnterior.setRowSpan(1);

		TextItemHandle lbInclusoes = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clInclusoes = (CellHandle) gridHandle.getCell(count + 4, 2);
		clInclusoes.getContent().add(lbInclusoes);
		lbInclusoes.setContent(Utilitaria.formatarValor("#,###,##0.00", listaMovimentacao[3])); //$NON-NLS-1$
		lbInclusoes.setStyleName("campostd");
		clInclusoes.setColumnSpan(1);
		clInclusoes.setRowSpan(1);

		TextItemHandle lbExclusoes = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clExclusoes = (CellHandle) gridHandle.getCell(count + 4, 3);
		clExclusoes.getContent().add(lbExclusoes);
		lbExclusoes.setContent(Utilitaria.formatarValor("#,###,##0.00", listaMovimentacao[4])); //$NON-NLS-1$
		lbExclusoes.setStyleName("campostd");
		clExclusoes.setColumnSpan(1);
		clExclusoes.setRowSpan(1);

		TextItemHandle lbSaldoAtual = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clSaldoAtual = (CellHandle) gridHandle.getCell(count + 4, 4);
		clSaldoAtual.getContent().add(lbSaldoAtual);
		lbSaldoAtual.setContent(Utilitaria.formatarValor("#,###,##0.00", listaMovimentacao[5])); //$NON-NLS-1$
		lbSaldoAtual.setStyleName("campostd");
		clSaldoAtual.setColumnSpan(1);
		clSaldoAtual.setRowSpan(1);

		// determinando o tamanho das colunas para que o tamanho do relatério saia de
		// forma correta
		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(1);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(2);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(3);
		col.setProperty("width", "25%");

		// adicionando o grid do body no design
		designHandle.getBody().add(gridHandle);

		// a cada 5 Organizações Centralizadoras quebrar a página
		if (i % 4 == 0) {
			createQuebraPagina();
		}
	} // end do createBody

	// método utilizado para acrescentar os responsaveis por assinar o relatério
	private void createResponsaveis(RelatorioMovimentacaoSisresForm myForm) throws SemanticException {

		GridHandle gridResponsavel = designFactory.newGridItem("gridResponsavel", 1, 2); // colunas: 2 linhas:5
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
				+ "    <td width=420><div align=center>Ordenador de Despesas</div></td>"
				+ "    <td width=39>&nbsp;</td> " + "    <td width=367><div align:center>Agente Fiscal</div></td>"
				+ "  </tr><tr>" + "    <td><div align=center>" + myForm.getPtOrdenador() + " " + myForm.getOrdenador()
				+ "</div></td>" + "    <td>&nbsp;</td> " + "    <td><div align=center>" + myForm.getPtAgente() + " "
				+ myForm.getAgente() + "</div></td>" + "  </tr> " + "  <tr><td><div align=center>"
				+ myForm.getFuncOrdenador() + "</div></td> " + "    <td>&nbsp;</td>" + "    <td><div align=center>"
				+ myForm.getFuncAgente() + "</div></td> " + "  </tr></table>");

		cell1.setColumnSpan(1);
		cell1.setRowSpan(1);
		cell1.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		TextItemHandle labelEncarregado = designHandle.getElementFactory().newTextItem("campo");
		CellHandle cell3 = (CellHandle) gridResponsavel.getCell(2, 1);
		cell3.getContent().add(labelEncarregado);
		labelEncarregado.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		labelEncarregado.setContent("<br><br>" + "<table width=100% border=0 align=center> "
				+ "  <tr><td><div align=center>______________________________________________</div></td>"
				+ "    <td>&nbsp;</td>"
				+ "    <td><div align=center>__________________________________________</div></td>" + "  </tr><tr> "
				+ "    <td width=420><div align=center>Oficial Encarregado</div></td>" + "    <td width=39>&nbsp;</td> "
				+ "    <td width=367><div align:center>Oficial Relator</div></td>" + "  </tr><tr>"
				+ "    <td><div align=center> " + myForm.getPtEncarregado() + " " + myForm.getEncarregado()
				+ "</div></td>" + "    <td>&nbsp;</td> " + "    <td><div align=center>" + myForm.getPtRelator() + " "
				+ myForm.getRelator() + "</div></td>" + "  </tr> " + "  <tr><td><div align=center>"
				+ myForm.getFuncEncarregado() + "</div></td> " + "    <td>&nbsp;</td>" + "    <td><div align=center>"
				+ myForm.getFuncRelator() + "</div></td> " + "  </tr></table>");

		cell3.setColumnSpan(1);
		cell3.setRowSpan(1);
		cell3.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");

		ColumnHandle col = (ColumnHandle) gridResponsavel.getColumns().get(0);
		col.setProperty("width", "100%");
		// col.setProperty("align", "center");

		designHandle.getBody().add(gridResponsavel);
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

// método utilizado para criar o header do relatorio
	private void createHeader(String periodo, ServletContext sc) throws SemanticException {
		// declarando a Grid e o tamanho das colunas e das linhas
		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 4, 3);
		// Colunas: 4 - Linhas: 3 = 3 linhas preenchidas mais o total de linhas de
		// retorno da query
		// formatando o periodo para que apareça MM/YYYY
		periodo = Utilitaria.formatarData(periodo, "MM/yyyy");
		// criando a primeira linha para colocar o título e o período no relatério
		RowHandle row1 = (RowHandle) gridHandle.getRows().get(0);
		gridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");
		CellHandle gridCellHandle = (CellHandle) row1.getCells().get(0);
		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextTitulo.setContent("RELATÓRIO MENSAL DE MOVIMENTAÇÃO DO SISRES <br> " + "Período: " + periodo);
		TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(4);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);
		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(1);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(2);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(3);
		col.setProperty("width", "25%");
		designHandle.getBody().add(gridHandle);
	}

	private void createTotalGeral(String[][] listMovimentacao) throws SemanticException {

		double totalAnterior = 0, totalInclusao = 0, totalExclusao = 0, totalSaldoAtual = 0;

		for (int i = 0; i < listMovimentacao.length; i++) {
			totalAnterior = totalAnterior + Double.parseDouble((listMovimentacao[i][2].replace(",", ".")));
			totalInclusao = totalInclusao + Double.parseDouble((listMovimentacao[i][3].replace(",", ".")));
			totalExclusao = totalExclusao + Double.parseDouble((listMovimentacao[i][4].replace(",", ".")));
			totalSaldoAtual = totalSaldoAtual + Double.parseDouble((listMovimentacao[i][5].replace(",", ".")));
		}
		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 4, 5);
		// Colocar o total Geral
		TextItemHandle lbTotalGeral = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle clTotalGeral = (CellHandle) gridHandle.getCell(1, 1);
		clTotalGeral.getContent().add(lbTotalGeral);
		lbTotalGeral.setContent("Total Geral"); //$NON-NLS-1$
		lbTotalGeral.setStyleName("dettd");
		clTotalGeral.setColumnSpan(4);
		clTotalGeral.setRowSpan(1);

		// Criar os labels
		createLabel(gridHandle);

		// adicionando os valores
		TextItemHandle lbSaldoAnterior = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clSaldoAnterior = (CellHandle) gridHandle.getCell(3, 1);
		clSaldoAnterior.getContent().add(lbSaldoAnterior);
		lbSaldoAnterior.setContent(String.valueOf(Utilitaria.formatarValor("#,###,##0.00", Math.abs(totalAnterior)))); //$NON-NLS-1$
		lbSaldoAnterior.setStyleName("campostd");
		clSaldoAnterior.setColumnSpan(1);
		clSaldoAnterior.setRowSpan(1);

		TextItemHandle lbInclusoes = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clInclusoes = (CellHandle) gridHandle.getCell(3, 2);
		clInclusoes.getContent().add(lbInclusoes);
		lbInclusoes.setContent(String.valueOf(Utilitaria.formatarValor("#,###,##0.00", Math.abs(totalInclusao)))); //$NON-NLS-1$
		lbInclusoes.setStyleName("campostd");
		clInclusoes.setColumnSpan(1);
		clInclusoes.setRowSpan(1);

		TextItemHandle lbExclusoes = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clExclusoes = (CellHandle) gridHandle.getCell(3, 3);
		clExclusoes.getContent().add(lbExclusoes);
		lbExclusoes.setContent(String.valueOf(Utilitaria.formatarValor("#,###,##0.00", Math.abs(totalExclusao)))); //$NON-NLS-1$
		lbExclusoes.setStyleName("campostd");
		clExclusoes.setColumnSpan(1);
		clExclusoes.setRowSpan(1);

		TextItemHandle lbSaldoAtual = designHandle.getElementFactory().newTextItem("campo");
		CellHandle clSaldoAtual = (CellHandle) gridHandle.getCell(3, 4);
		clSaldoAtual.getContent().add(lbSaldoAtual);
		lbSaldoAtual.setContent(String.valueOf(Utilitaria.formatarValor("#,###,##0.00", Math.abs(totalSaldoAtual)))); //$NON-NLS-1$
		lbSaldoAtual.setStyleName("campostd");
		clSaldoAtual.setColumnSpan(1);
		clSaldoAtual.setRowSpan(1);

		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(1);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(2);
		col.setProperty("width", "25%");

		col = (ColumnHandle) gridHandle.getColumns().get(3);
		col.setProperty("width", "25%");

		designHandle.getBody().add(gridHandle);
	}

	private void createLabel(GridHandle gridHandle) throws SemanticException {

		TextItemHandle label3 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell3 = (CellHandle) gridHandle.getCell(3, 1);
		cell3.getContent().add(label3);
		label3.setContent("Saldo Anterior"); //$NON-NLS-1$
		label3.setStyleName("dettd");
		cell3.setColumnSpan(1);
		cell3.setRowSpan(1);

		TextItemHandle label4 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell4 = (CellHandle) gridHandle.getCell(3, 2);
		cell4.getContent().add(label4);
		label4.setContent("Inclusões"); //$NON-NLS-1$
		label4.setStyleName("dettd");
		cell4.setColumnSpan(1);
		cell4.setRowSpan(1);

		TextItemHandle label5 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell5 = (CellHandle) gridHandle.getCell(3, 3);
		cell5.getContent().add(label5);
		label5.setContent("Exclusões"); //$NON-NLS-1$
		label5.setStyleName("dettd");
		cell5.setColumnSpan(1);
		cell5.setRowSpan(1);

		TextItemHandle label6 = designHandle.getElementFactory().newTextItem("HeadPrincipal");
		CellHandle cell6 = (CellHandle) gridHandle.getCell(3, 4);
		cell6.getContent().add(label6);
		label6.setContent("Saldo Atual"); //$NON-NLS-1$
		label6.setStyleName("dettd");
		cell6.setColumnSpan(1);
		cell6.setRowSpan(1);

	}

	private void createQuebraPagina() throws SemanticException {
		// Quebra de página
		GridHandle gridHandle = designFactory.newGridItem("dataGrid", 1, 1);
		TextItemHandle legendBody = designHandle.getElementFactory().newTextItem("legendBody");
		legendBody.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		legendBody.setContent("<br>");
		designHandle.getBody().add(legendBody);
		legendBody.setProperty(StyleHandle.PAGE_BREAK_AFTER_PROP, "always");
	}

	private void createInfoPadrao() throws SemanticException {
		// declarando a Grid e o tamanho das colunas e das linhas
		GridHandle gridHandle = designFactory.newGridItem("gridHandle", 1, 1);
		// criando a primeira linha para colocar as informações padrões no relatério
		RowHandle row1 = (RowHandle) gridHandle.getRows().get(0);
		gridHandle.setProperty(GridHandle.WIDTH_PROP, "100%");
		CellHandle gridCellHandle = (CellHandle) row1.getCells().get(0);
		TextItemHandle TextTitulo = designHandle.getElementFactory().newTextItem("titulo");
		TextTitulo.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
		TextTitulo.setContent("MARINHA DO BRASIL<br> " + "PAGADORIA DE PESSOAL DA MARINHA <br>");
		// + "PEDIDO DE REVERSÃO<br><br>");
		TextTitulo.setStyleName("cabecalho");
		gridCellHandle.setColumnSpan(1);
		gridCellHandle.setRowSpan(1);
		gridCellHandle.getContent().add(TextTitulo);
		ColumnHandle col = (ColumnHandle) gridHandle.getColumns().get(0);
		col.setProperty("width", "100%");
		designHandle.getBody().add(gridHandle);
	}

}// end da classe

package com.sisres.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.RelatorioSisresSvc;
import com.sisres.model.SispagException;
import com.sisres.view.GerarArquivoDividasSIPMForm;

public class GerarArquivoDividasSIPMAction extends org.apache.struts.action.Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// System.out.println("Entrou no action");
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {

			GerarArquivoDividasSIPMForm myForm = (GerarArquivoDividasSIPMForm) form;
			ArrayList<String> listOC = new ArrayList<String>();
			listOC.add("930");

			RelatorioSisresSvc relatorioSvc = new RelatorioSisresSvc();

			String[][][][] listadeDivida = (String[][][][]) relatorioSvc.getDividasPessoasOC(myForm.getMesStr(),
					myForm.getAnoStr(), listOC);
			if (listadeDivida != null) {

				File fileO = new File("/tmp" + "/ArquivoSISRES" + myForm.getMesStr() + myForm.getAnoStr() + ".txt");
				FileWriter fwO;

				try {
					fwO = new FileWriter(fileO);
					PrintWriter pwO = new PrintWriter(fwO);
					String linha = "";
					// fim da primeira parte
					for (int i = 0; i < listadeDivida.length; i++) {

						for (int j = 0; j < listadeDivida[i].length; j++) {

							for (int k = 0; k < listadeDivida[i][j].length; k++) {

								linha = "";
								for (int l = 0; l < listadeDivida[i][j][k].length; l++) {
									if (l != 0) {
										linha = linha + ";" + listadeDivida[i][j][k][l];
									} else {
										linha = listadeDivida[i][j][k][l];
									}
								}
								pwO.println(linha);
							}
						}
					}
					pwO.flush();
					pwO.close();
					// prepara para cria o arquivo zip

					if (!fileO.exists()) {
						errors.add("listadeDivida", new ActionMessage("erro.arquivoInvalido"));
						saveErrors(request, errors);
						return mapping.findForward("erro");
					}

					// inicio do download

					BufferedInputStream buf = null;
					ServletOutputStream myOut = null;
					FileInputStream input = null;

					try {
						myOut = response.getOutputStream();

						// define que a resposta é um arquivo para download
						response.setContentType("application/download");
						response.setHeader("Content-Disposition", "attachment; filename=" + fileO);

						response.setContentLength((int) fileO.length());

						input = new FileInputStream(fileO);

						buf = new BufferedInputStream(input);
						int readBytes = 0;

						// Lê do arquivo e escreve na saída (ServletOutputStream)
						while ((readBytes = buf.read()) != -1)
							myOut.write(readBytes);
						myOut.flush();
					} finally {
						if (input != null)
							input.close();
						if (myOut != null)
							myOut.close();
					}

					session.setAttribute("arquivoGerado", new Boolean(true));
					messages.add("mensagemSucesso", new ActionMessage("message.arquivoGerado"));
					saveMessages(request, messages);
				} catch (IOException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// fim teste lista

			} else {
				errors.add("listadeDivida", new ActionMessage("error.listadeDivida.empty"));
				if (errors.size() > 0) {
					saveErrors(request, errors);
				}
			}
			// inserir mensagem de sucesso
			return mapping.findForward("sucesso");

			// Handle any unexpected expections
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			// errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ"));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			// errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ"));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		}

	}

}
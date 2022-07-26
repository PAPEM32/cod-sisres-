package com.sisres.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sisres.model.RelatorioSisresSvc;
import com.sisres.view.RelatorioDividasOcDTInclusaoForm;

public class TestaRelatorioDividasOcDTInclusaoAction extends org.apache.struts.action.Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

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
				return mapping.findForward("sucesso");
			} else {
				return mapping.findForward("error");
			}
		} catch (Exception e) {

			return mapping.findForward("error");
		}

	}

}

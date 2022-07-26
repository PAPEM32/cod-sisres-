package com.sisres.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sisres.model.RelatorioSisresSvc;
import com.sisres.view.RelatorioDividasOcForm;

public class TestaRelatorioDividasOcAction extends org.apache.struts.action.Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			RelatorioDividasOcForm myForm = (RelatorioDividasOcForm) form;
			ArrayList<String> listOC = new ArrayList<String>();
			String[] listaOC = myForm.getListaDeOC();

			for (int i = 0; i < listaOC.length; i++) {
				listOC.add(listaOC[i]);
			}

			// System.out.println("Mês do teste action: " + myForm.getMesStr());
			// System.out.println("Ano: " + myForm.getAnoStr());
			// System.out.println(listOC);

			RelatorioSisresSvc relatorioSvc = new RelatorioSisresSvc();
			String[][][][] listadeDivida = (String[][][][]) relatorioSvc.getDividasPessoasOC(myForm.getMesStr(),
					myForm.getAnoStr(), listOC);
			// System.out.println("passei do lista dividas do teste ");

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

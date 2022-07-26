/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisres.controller;

import com.sisres.model.Divida;
import com.sisres.model.IncluirStatusSvc;
import com.sisres.model.SispagException;
import com.sisres.view.IncluirStatusForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author mariana-cruz
 */
public class IncluirStatusAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	HttpServletResponse response)  {
            
        
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
        
      
        
        try{
            
        IncluirStatusForm incluirStatusForm = (IncluirStatusForm) form;
        IncluirStatusSvc incluirStatusSvc = new IncluirStatusSvc();
            
        HttpSession session = request.getSession();
            
        Divida divida = (Divida) session.getAttribute("divida");
        //System.out.println("id divida" + divida.getId());
        
        incluirStatusForm.setIdDivida(divida.getId());
        
        /*    
        String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");
        System.out.println("codigo usuario sispag: "+ codUsuario);
        
              
       
        System.out.println("-----------------");
        
        System.out.println("Status divida:" + incluirStatusForm.getStatusDivida());
        System.out.println("origemDocInclusaoStatus" + incluirStatusForm.getOrigemDocInclusaoStatus() );
        System.out.println("tipoDocAutorizacaoInclusaoStatus " + incluirStatusForm.getTipoDocAutorizacaoInclusaoStatus());
        System.out.println("dataDocInclusaoStatus "+ incluirStatusForm.getDataDocInclusaoStatus());
        System.out.println("observacaoStatusDivida "+ incluirStatusForm.getObservacaoStatusDivida());
        System.out.println("idDivida "+ incluirStatusForm.getIdDivida());
        System.out.println("numDoc" + incluirStatusForm.getNumDoc());
       */
        boolean incluiuStatusComSucesso = false;
       incluiuStatusComSucesso = 
                incluirStatusSvc.salvarStatus(incluirStatusForm.getStatusDivida(),
                                      incluirStatusForm.getOrigemDocInclusaoStatus(),
                                      incluirStatusForm.getTipoDocAutorizacaoInclusaoStatus(),
                                      incluirStatusForm.getDataDocInclusaoStatus(),
                                      incluirStatusForm.getObservacaoStatusDivida(),
                                      incluirStatusForm.getIdDivida(),
                                      incluirStatusForm.getNumDoc(),
                                      divida);
        
        if (incluiuStatusComSucesso){
            //session.setAttribute("divida", divida);
            request.getSession().setAttribute("divida", divida);
            
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inclusao_status_divida"));
            saveMessages(request, messages);

            return mapping.findForward("sucesso");
        }
        
        
        } catch (SispagException se) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
	    if (errors.size() > 0) {
	        saveErrors(request, errors);
	    }
				
	    return mapping.findForward("erro");
        } catch (RuntimeException e){
            e.printStackTrace(System.err);
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.unexpectedError", e.getMessage()));
		return mapping.findForward("erro");
            }
        
        return mapping.findForward("erro");
        
    }
    
}

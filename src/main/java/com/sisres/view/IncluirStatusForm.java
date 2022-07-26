/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisres.view;

import com.sisres.utilitaria.Utilitaria;
import java.sql.Date;
import java.text.Normalizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author mariana-cruz
 */
public class IncluirStatusForm extends ActionForm{
    
    private String statusDivida;
    private String origemDocInclusaoStatus;
    private String tipoDocAutorizacaoInclusaoStatus;
   private String dataDocInclusaoStatus;
    private String observacaoStatusDivida;
    private int idDivida;

    //private String dataInsercaoBancoDados; 
    private String numDoc; 

   

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }


    public int getIdDivida() {
        return idDivida;
    }

    public void setIdDivida(int idDivida) {
        this.idDivida = idDivida;
    }

    public String getOrigemDocInclusaoStatus() {
        return origemDocInclusaoStatus;
    }

    public void setOrigemDocInclusaoStatus(String origemDocInclusaoStatus) {
        this.origemDocInclusaoStatus = origemDocInclusaoStatus;
    }

    public String getTipoDocAutorizacaoInclusaoStatus() {
        return tipoDocAutorizacaoInclusaoStatus;
    }

    public void setTipoDocAutorizacaoInclusaoStatus(String tipoDocAutorizacaoInclusaoStatus) {

        this.tipoDocAutorizacaoInclusaoStatus = tipoDocAutorizacaoInclusaoStatus;

    }

    public String getDataDocInclusaoStatus() {
        return dataDocInclusaoStatus;
    }

    public void setDataDocInclusaoStatus(String dataDocInclusaoStatus) {
        this.dataDocInclusaoStatus = dataDocInclusaoStatus;
    }
    
//    public Date getDataDocInclusaoStatus() {
//        return dataDocInclusaoStatus;
//    }
//
//    public void setDataDocInclusaoStatus(Date dataDocInclusaoStatus) {
//        this.dataDocInclusaoStatus = dataDocInclusaoStatus;
//    }

    public String getObservacaoStatusDivida() {
         String observacaoFormatada = removerAcentos(observacaoStatusDivida);
        //return observacaoStatusDivida;
        return observacaoFormatada;
    }

    public void setObservacaoStatusDivida(String observacaoStatusDivida) {
         String observacaoFormatada = removerAcentos(observacaoStatusDivida);
        this.observacaoStatusDivida = observacaoFormatada;  
        //this.observacaoStatusDivida = observacaoStatusDivida;
    }
    
     public static String removerAcentos(String str) {
    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
     }
    
    public String getStatusDivida() {
        return statusDivida;
    }

    public void setStatusDivida(String statusDivida) {
        this.statusDivida = statusDivida;
    }
    
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
	
        ActionErrors errors = new ActionErrors();
        
        if (statusDivida.length() == 0 ){
            errors.add("statusDivida", new ActionMessage("error.status_divida.required"));
        } 
        
        if (origemDocInclusaoStatus.length() == 0){
            errors.add("origemDocInclusaoStatus", new ActionMessage("error.origemDocInclusaoStatus.required"));
        }
        
        if(tipoDocAutorizacaoInclusaoStatus.length() == 0){
            errors.add("tipoDocAutorizacaoInclusaoStatus", new ActionMessage("error.tipoDocAutorizacaoInclusaoStatus.required"));
        }
        
 
       if(numDoc.length() == 0){
           errors.add("numDoc", new ActionMessage("error.numDoc.required"));
       }
   
        // ===============Transformação da Data========================//
		if (dataDocInclusaoStatus.length() == 0) {
			errors.add("dataDocAutorizacao", new ActionMessage("error.dataDocInclusaoStatus.required"));

		} else {
			if (!validaData(dataDocInclusaoStatus)) {
				errors.add("dataDocInclusaoStatus", new ActionMessage("error.dataDocInclusaoStatus.range"));

			} else {
				String[] arrayData = dataDocInclusaoStatus.trim().split("/");
				dataDocInclusaoStatus = arrayData[2].trim() + "-" + arrayData[1].trim() + "-" + arrayData[0].trim();
			}
		}

        
          return errors;      
                
        }
    
    public boolean validaData(String data) {
		boolean result = true;

		String[] arrayData = data.split("/");
		try {
			int dia = Integer.parseInt(arrayData[0]);
			int mes = Integer.parseInt(arrayData[1]);
			int ano = Integer.parseInt(arrayData[2]);

			// int ANOATUAL = ano;

			// if( (ano!=-1)&&(ano<1900)&&(ano)) Aconselhável fazer no form

			int bisex = ano % 4;

			switch (mes) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if ((dia <= 0) || (dia > 31)) {
					result = false;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if ((dia <= 0) || (dia > 30)) {
					result = false;
				}
				break;
			case 2:
				if (bisex == 0) {
					if ((dia <= 0) || (dia > 29)) {
						result = false;
					}
				} else {
					if ((dia <= 0) || (dia > 28)) {
						result = false;
					}
				}
				break;
			default:
				result = false;
			}// fim do switch

			if (ano < 1900) {
				// System.out.println("passei por aqui validação do ano.");
				result = false;
			}
		} catch (NumberFormatException nfe) {
			result = false;
			// listaErros.add("msgData",new ActionMessage("Data Inválida"));// procurar no
			// properties
		}

		return result;
	}
    
}

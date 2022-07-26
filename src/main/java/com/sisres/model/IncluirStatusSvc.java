/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisres.model;

/**
 *
 * @author mariana-cruz
 */
public class IncluirStatusSvc {
    
    public boolean salvarStatus(String statusDivida,
                             String origemDocInclusaoStatus,
                             String tipoDocAutorizacaoInclusaoStatus,
                             String dataDocInclusaoStatus,
                             String observacaoStatusDivida,
                             int idDivida,
                             String numDoc,
                             Divida divida) throws SispagException{
        
        DAOStatusDivida daoStatusDivida = new DAOStatusDivida();
        boolean incluiuStatus = false;
        
       incluiuStatus =  daoStatusDivida.salvarStatus(statusDivida,
                                       origemDocInclusaoStatus,
                                       tipoDocAutorizacaoInclusaoStatus,
                                       dataDocInclusaoStatus,
                                       observacaoStatusDivida,
                                       idDivida,
                                       numDoc,
                                       divida);
        
       if(!incluiuStatus)
           return false;
       
       return true;
        
    }
    
}

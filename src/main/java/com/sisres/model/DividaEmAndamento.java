/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisres.model;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mariana-cruz
 */
public class DividaEmAndamento {
    
    private String statusDivida; //SRESDIVAND_ID
    private String descricao; //SRESDIVAND_DESCRICAO

   
    private String origemDocInclusaoStatus; //SRESDIVAND_ORIGEM_DOC_INC
    private String tipoDocAutorizacaoInclusaoStatus; //SRESDIVAND_TIPO_DOC_AUT
    private String dataDocInclusaoStatus; //SRESDIVAND_DOC_DT_AUT
   // private Date dataDocInclusaoStatus;
    private String observacaoStatusDivida; //SRESDIVAND_OBS
    private int idDivida; //SRESDIV_ID  
    private String dataInsercaoBancoDados; //SRESDIVAND_DT_INSERCAO_BANCODADOS
    private String numDoc; //SRESDIVAND_NUM_DOC_AUT

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }
    
    
    public String getDataInsercaoBancoDados() {
        return dataInsercaoBancoDados;
    }

    public void setDataInsercaoBancoDados(String dataInsercaoBancoDados) {
        this.dataInsercaoBancoDados = dataInsercaoBancoDados;
    }

     public String getDescricao() { //feito rápido e toscamente pra safar.
         
         String descricaoFormatada = null;
         
         if(this.descricao.equals("ImpossZerarSaldoDevedor"))
         {
             descricaoFormatada = "Impossibilidade de zerar saldo devedor";
             return descricaoFormatada ;
         } else if (this.descricao.equals("Cobranca_Judicial")){
             descricaoFormatada = "Cobranca Judicial";
             return descricaoFormatada ;
         }  else if (this.descricao.equals("EmParcelamento")){
             descricaoFormatada = "Em Parcelamento";
             return descricaoFormatada ;
         } else if (this.descricao.equals("DividaAtiva")){
             descricaoFormatada = "Dívida Ativa";
             return descricaoFormatada ;
         }
         
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        
        
      String data = this.dataDocInclusaoStatus.replaceAll("-", "/");
      String data2 = data.replaceAll("00:00:00.0", "");
      String[] s = data2.split("/"); 
      String novaData = s[2]+"/"+s[1]+"/"+s[0];

        return novaData;
       //return dataDocInclusaoStatus;
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
        return observacaoStatusDivida;
    }

    public void setObservacaoStatusDivida(String observacaoStatusDivida) {
        
        String observacaoFormatada = removerAcentos(observacaoStatusDivida);
        this.observacaoStatusDivida = observacaoFormatada;       
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
    
}

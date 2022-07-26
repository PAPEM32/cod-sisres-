/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisres.model;

import com.sisres.utilitaria.Utilitaria;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mariana-cruz
 */
class DAOStatusDivida {
    
    private DataSource ds;
	// TODO p32 Copiar modelos do DAO Situacao para conections
	private Connection conexao;
	private Logger logger = LoggerFactory.getLogger(DAODivida.class);
    
    public DAOStatusDivida(){
        String nomeDS = "java:/jdbc/sisres2"; //sispag2 //sisres2
		try {

			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(nomeDS);
			if (ds == null)
				throw new Exception("DataSource não pode ser encontrado");
		} catch (Exception e) {
			throw new RuntimeException("DataSource nao pode ser criado" + nomeDS);
		}
    }
    
    private void obterConexao() throws SispagException {
		try {
			conexao = ds.getConnection();
		} catch (SQLException e) {
			throw new SispagException(e.getMessage());
		}
	}
    
    public void fecharConexao(String metodo) throws SQLException {
		this.conexao.close();
		System.out.println("*************** Fechou Conexão " + metodo + " *************");
	}
    
    public boolean salvarStatus(String statusDivida,
                             String origemDocInclusaoStatus,
                             String tipoDocAutorizacaoInclusaoStatus,
                             String dataDocInclusaoStatus,
                             String observacaoStatusDivida,
                             int idDivida,
                             String numDoc,
                             Divida divida) throws SispagException{
        
      //  Date dataDocIn;
       // dataDocIn = Date.valueOf(dataDocInclusaoStatus);
        //System.out.println("dataDocIn: " + dataDocIn);
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = (java.util.Date)formatter.parse(dataDocInclusaoStatus);
        
       // java.sql.Date.valueOf(Utilitaria.formatarData(altform.getDataMotivo(), "yyyy-MM-dd")));
    //    Date dataTeste = Date.valueOf(dataDocInclusaoStatus.substring(0, 10));
     // Date dataTeste2 = dataTeste;
        //Date dataMot = Date.valueOf(dtMotStr.substring(0, 10));

		//dataMotivo = dataMot;
                
        DividaEmAndamento dividaAndamento = new DividaEmAndamento();
        dividaAndamento.setDescricao(statusDivida);
        dividaAndamento.setOrigemDocInclusaoStatus(origemDocInclusaoStatus);
        dividaAndamento.setTipoDocAutorizacaoInclusaoStatus(tipoDocAutorizacaoInclusaoStatus);
        dividaAndamento.setDataDocInclusaoStatus(dataDocInclusaoStatus);
        dividaAndamento.setObservacaoStatusDivida(observacaoStatusDivida);
        dividaAndamento.setIdDivida(idDivida);
        dividaAndamento.setNumDoc(numDoc);
        
        

       
        boolean incluiuStatus = false;
        
        int executouInclusaoStatus = 0;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            
            String sql = "INSERT INTO SRES_DIVIDA_ANDAMENTO (SRESDIVAND_ID, SRESDIVAND_DESCRICAO, SRESDIVAND_ORIGEM_DOC_INC, SRESDIVAND_TIPO_DOC_AUT, SRESDIVAND_DOC_DT_AUT, SRESDIVAND_OBS, SRESDIV_ID, SRESDIVAND_DT_INS_BCODADOS, SRESDIVAND_NUM_DOC_AUT) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";
            
            obterConexao();
            
            ps = conexao.prepareStatement(sql);
            
            ps.setInt(1, geraIdStatusDivida());
            ps.setString(2, statusDivida);
            ps.setString(3, origemDocInclusaoStatus);
            ps.setString(4, tipoDocAutorizacaoInclusaoStatus);
            
            
            //ps.setDate(5, dataDocIn);
           // ps.setDate(5, data);
           
           String data = dataDocInclusaoStatus.replaceAll("-", "/");
            String data2 = data.replaceAll("00:00:00.0", "");
            String[] s = data2.split("/"); 
            String novaData = s[2]+"/"+s[1]+"/"+s[0];
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
sdf.setLenient(false);
java.sql.Date dataConvertida;
        try {
            dataConvertida = new java.sql.Date(sdf.parse(novaData).getTime());
             ps.setDate(5, dataConvertida);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(DAOStatusDivida.class.getName()).log(Level.SEVERE, null, ex);
        }
     
            ps.setString(6, observacaoStatusDivida);
            ps.setInt(7, idDivida);
            ps.setString(8, numDoc);
            
            
            executouInclusaoStatus = ps.executeUpdate();
            
            if (executouInclusaoStatus != 0){
                incluiuStatus = true;
                divida.setDividaEmAndamento(dividaAndamento);
            }
            
        }catch(Exception ex){
            ex.printStackTrace(System.err);
        }
        finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Bloco catch gerado automaticamente
					e.printStackTrace();
				}
			}
			if (conexao != null) {
				try {
					fecharConexao("salvarStatus()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // Fim do blo finally
		return incluiuStatus;
        
    }
    
    //TODO
    private int geraIdStatusDivida(){
        PreparedStatement stmt = null;
		ResultSet results = null;
		int novocod = 0;

		final String RETRIEVE_MAX_SRESDIVAND_ID = "SELECT MAX(SRESDIVAND_ID) FROM SRES_DIVIDA_ANDAMENTO ";
				
		try {
			// Cria sql
			stmt = conexao.prepareStatement(RETRIEVE_MAX_SRESDIVAND_ID);

			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {
				novocod = results.getInt("MAX(SRESDIVAND_ID)") + 1;
			}
			return novocod;
		} catch (SQLException e) {
			// //System.out.println(e);
			return novocod;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (results != null) {
				try {
					results.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
    }
    
}

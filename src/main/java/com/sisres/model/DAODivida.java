package com.sisres.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.struts.action.ActionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sisres.utilitaria.Utilitaria;
import com.sisres.view.FiltrarPedidoReversaoForm;
import com.sisres.view.GerarPedidoReversaoForm;

import papem32.DButils;

public class DAODivida {

	private Divida divida;
	private DataSource ds;
	// TODO p32 Copiar modelos do DAO Situacao para conections
	private Connection connexao;
	private Logger logger = LoggerFactory.getLogger(DAODivida.class);

//INICIO CONSULTA ID DO PEDIDO DE REVERSAO
	// (Welber)
	final String SQL_OBTER_ID_PEDIDO_REVERSAO = "SELECT SRESPED_ID FROM SRES_PEDIDO WHERE SRESPED_COD = ? AND SRESPED_ANO = ? ";
//FIM CONSULTA ID DO PEDIDO DE REVERSAO

//INICIO CONTADOR DE PEDIDOS DE REVERSAO FEITOS NO SISTEMA
	final String SQL_CONTAGEM_PEDIDO = "SELECT COUNT(*) AS QUANTIDADE "
			+ "FROM  sres_divida,sres_pessoa_rr, sres_pedido, sres_tipo_divida, sres_banco "
			+ "WHERE SRESDIV_PED_ID = SRESPED_ID AND " + "SRESDIV_PES_ID = SRESPES_ID AND "
			+ "sresbco_id = sresdiv_bco_id AND " + "srestpdiv_id = sresdiv_tpdiv_id";
//FIM CONTADOR DE PEDIDOS DE REVERSAO FEITOS NO SISTEMA

//INICIO CONSULTA PARA GERACAO DO RELATORIO DE DIVIDAS MENSAIS
	final String SQL_RELDIVIDAMENSAL = " select SRESOM_NOME, decode(SRESPES_MATSIAP ,Null, SRESPES_MATFIN    , SRESPES_MATSIAP) matricula, SRESPES_NOME, SRESPES_CPF,"
			+ " SRESMOT_NOME, "
			+ " sum(decode(SRES_TIPO_LANCAMENTO.SRESTPLANC_NOME,'INICIAL',decode(SRES_LANCAMENTO.SRESLANC_OPERADOR, 'D',SRESLANC_VALOR,- SRESLANC_VALOR),0)) valor_original,   "
			+ " sum(decode(SRES_TIPO_LANCAMENTO.SRESTPLANC_NOME,'REVERSÃO',decode(SRES_LANCAMENTO.SRESLANC_OPERADOR, 'D',SRESLANC_VALOR,- SRESLANC_VALOR),0)) valor_revertido, "
			+ " sum(decode(SRES_TIPO_LANCAMENTO.SRESTPLANC_NOME,'DEVOLUï¿½ï¿½O',decode(SRES_LANCAMENTO.SRESLANC_OPERADOR, 'D',SRESLANC_VALOR,- SRESLANC_VALOR),0)) valor_devolvido,"
			+ " sum(decode(SRES_TIPO_LANCAMENTO.SRESTPLANC_NOME,'PERDOADO',decode(SRES_LANCAMENTO.SRESLANC_OPERADOR, 'D',SRESLANC_VALOR,- SRESLANC_VALOR),0)) valor_perdoado   "
			+ " from  SRES_DIVIDA , SRES_PESSOA_RR, SRES_ORGANIZACAO_MILITAR, SRES_LANCAMENTO, SRES_MOTIVO , SRES_TIPO_LANCAMENTO "
			+ " where SRES_PESSOA_RR.SRESPES_ID          = SRES_DIVIDA.SRESDIV_PES_ID         AND  "
			+ " SRESOM_OM_ID                       = SRES_DIVIDA.SRESDIV_OM_OC_ID       AND "
			+ " SRES_LANCAMENTO.SRESLANC_DIV_ID    = SRES_DIVIDA.SRESDIV_ID             AND "
			+ " SRES_TIPO_LANCAMENTO.SRESTPLANC_ID = SRES_LANCAMENTO.SRESLANC_TPLANC_ID AND "
			+ " SRES_MOTIVO.SRESMOT_ID             = SRES_DIVIDA.SRESDIV_MOT_ID         AND "
			+ " SRESDIV_REF_INICIO_MES =  ?'     AND  SRESDIV_REF_INICIO_ANO =  ?        "
			+ " and SRESOM_OM_OC_COD in (?) "
			+ " GROUP BY SRESOM_NOME,SRESPES_NOME, SRESPES_CPF, SRESMOT_NOME,SRESPES_MATSIAP,SRESPES_MATFIN,SRESPES_MATSIAP "
			+ " order by SRESOM_NOME, SRESPES_NOME ";
//FIM CONSULTA PARA GERACAO DO RELATORIO DE DIVIDAS MENSAIS

//INICIO BLOCO RELATORIO DE DIVIDA MENSAL POR OC - DATA DA DIVIDA	
	final String SQL_QTDOCDIVIDA = " select count(distinct SRESOM_OM_ID) QTD       "
			+ " from SISRES.sres_organizacao_militar, SRES_DIVIDA "
			+ " WHERE  SRESOM_OM_ID  = SRESDIV_OM_OC_ID       AND "
			+ "  SRESDIV_REF_INICIO_MES =  ?     AND  SRESDIV_REF_INICIO_ANO =  ? ";

	final String SQL_CONTAGEM = "SELECT COUNT(*) AS QUANTIDADE "
			+ " FROM sres_divida,sres_pessoa_rr,sres_lancamento ,sres_tipo_divida,sres_motivo, sres_organizacao_militar,sres_banco,sres_pedido, "
			+ " (select sreslanc_div_id div_id, sum(decode(sreslanc_operador, 'D',sreslanc_valor, -sreslanc_valor)) Valor from sres_lancamento group by sreslanc_div_id) tb_valor "
			+ " WHERE tb_valor.div_id = sresdiv_id and sresdiv_pes_id = srespes_id AND sreslanc_div_id = sresdiv_id "
			+ " AND sresdiv_mot_id = sresmot_id AND sresbco_id = sresdiv_bco_id AND "
			+ " srestpdiv_id = sresdiv_tpdiv_id AND sresdiv_om_oc_id = sresom_om_id AND  sresped_id  (+)= sresdiv_ped_id AND "
			+ " sreslanc_codigo = 1 "; 

	final String SQL_OCDIVIDA = " select DISTINCT SRESOM_NOME, SRESOM_OM_ID"
			+ " from SISRES.sres_organizacao_militar, SRES_DIVIDA "
			+ " WHERE  SRESOM_OM_ID  = SRESDIV_OM_OC_ID       AND  "
			+ "  SRESDIV_REF_INICIO_MES =  ?     AND  SRESDIV_REF_INICIO_ANO =  ? ";

	final String SQL_QTDPESSOADIVIDA = " select COUNT(DISTINCT SRESPES_ID)  QTD            "
			+ " from SRES_PESSOA_RR, SRES_DIVIDA         " + " WHERE  SRESPES_ID = SRESDIV_PES_ID AND   "
			+ " SRESDIV_REF_INICIO_MES =  ? AND          " + " SRESDIV_REF_INICIO_ANO =  ? AND SRESDIV_OM_OC_ID = ? ";

	final String SQL_PESSOAOCDIVIDA = " select DISTINCT decode(SRESPES_MATSIAP ,Null, SRESPES_MATFIN    , SRESPES_MATSIAP) matricula, "
			+ " SRESPES_NOME, SRESPES_CPF, SRESPES_ID    " + " from SRES_PESSOA_RR, SRES_DIVIDA         "
			+ " WHERE  SRESPES_ID = SRESDIV_PES_ID AND   " + " SRESDIV_REF_INICIO_MES =  ? AND   "
			+ " SRESDIV_REF_INICIO_ANO =  ? AND SRESDIV_OM_OC_ID = ? " + " ORDER BY  SRESPES_NOME ";

	final String SQL_QTDDIVIDATOTAIS = " SELECT COUNT(SRESDIV_ID) QTD from SRES_DIVIDA "
			+ " WHERE  SRESDIV_REF_INICIO_MES =  ? AND SRESDIV_REF_INICIO_ANO =  ? AND  "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ?";

	final String SQL_DIVIDATOTAIS = " select SRESDIV_ID, SRESLANC_NUM_DOC_AUT, TO_CHAR(SRESLANC_DT_DOC_AUT,'DD/MM/YYYY') SRESLANC_DT_DOC_AUT, tot.INICIAL INICIAL, "
			+ "      tot.REVERSAO REVERSAO, tot.DEVOLUCAO DEVOLUCAO, tot.PERDOADO PERDOADO, SRESMOT_NOME  "
			+ " from SRES_LANCAMENTO l, SRES_DIVIDA d, SRES_MOTIVO,                                "
			+ "      (select SRESLANC_DIV_ID, SUM(DECODE(SRESLANC_TPLANC_ID,'1',SRESLANC_VALOR,0)) INICIAL, "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'2',SRESLANC_VALOR,0)) REVERSAO,  "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'3',SRESLANC_VALOR,0)) DEVOLUCAO, "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'4',SRESLANC_VALOR,0)) PERDOADO   "
			+ " from SRES_DIVIDA, SRES_LANCAMENTO      " + " WHERE SRESLANC_DIV_ID = SRESDIV_ID AND "
			+ " SRESLANC_ID NOT IN ( SELECT DISTINCT SRESLANC_LANC_ID          "
			+ " FROM SRES_LANCAMENTO                     " + " WHERE SRESLANC_LANC_ID IS NOT NULL ) AND "
			+ " SRESLANC_TPLANC_ID <> 5 AND        "
			+ " SRESDIV_REF_INICIO_MES =  ? AND SRESDIV_REF_INICIO_ANO =  ? AND "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ? " + " GROUP BY SRESLANC_DIV_ID ) tot "
			// + " WHERE l.SRESLANC_DIV_ID = d.SRESDIV_ID AND SRESLANC_TPLANC_ID
			// in('1','2','3','4') AND "
			+ " WHERE l.SRESLANC_DIV_ID = d.SRESDIV_ID AND  SRESLANC_TPLANC_ID in('1') AND "
			+ " SRESLANC_ID NOT IN ( SELECT DISTINCT SRESLANC_LANC_ID  "
			+ " FROM SRES_LANCAMENTO WHERE SRESLANC_LANC_ID IS NOT NULL )AND "
			+ " SRESDIV_REF_INICIO_MES =  ?     AND  SRESDIV_REF_INICIO_ANO =  ?  AND            "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ? "
			+ " AND tot.SRESLANC_DIV_ID = l.SRESLANC_DIV_ID  AND SRESMOT_ID   = SRESDIV_MOT_ID   ";
//FIM BLOCO RELATORIO DE DIVIDA MENSAL POR OC - DATA DA DIVIDA

// INICIO BLOCO RELATORIO DE DIVIDA MENSAL POR OC - DATA INCLUSAO NO SISTEMA	
	final String SQL_QTDOCDIVIDADTINCLUSAO = " select count(distinct SRESOM_OM_ID) QTD       "
			+ " from SISRES.sres_organizacao_militar, SRES_DIVIDA "
			+ " WHERE  SRESOM_OM_ID  = SRESDIV_OM_OC_ID       AND "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? ";

	final String SQL_OCDIVIDADTINCLUSAO = " select DISTINCT SRESOM_NOME, SRESOM_OM_ID"
			+ " from SISRES.sres_organizacao_militar, SRES_DIVIDA "
			+ " WHERE  SRESOM_OM_ID  = SRESDIV_OM_OC_ID       AND  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? ";

	final String SQL_QTDPESSOADIVIDADTINCLUSAO = " select COUNT(DISTINCT SRESPES_ID)  QTD            "
			+ " from SRES_PESSOA_RR, SRES_DIVIDA         " + " WHERE  SRESPES_ID = SRESDIV_PES_ID AND   "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ?  " + " AND SRESDIV_OM_OC_ID = ? ";

	final String SQL_PESSOAOCDIVIDADTINCLUSAO = " select DISTINCT decode(SRESPES_MATSIAP ,Null, SRESPES_MATFIN    , SRESPES_MATSIAP) matricula, "
			+ " SRESPES_NOME, SRESPES_CPF, SRESPES_ID    " + " from SRES_PESSOA_RR, SRES_DIVIDA         "
			+ " WHERE  SRESPES_ID = SRESDIV_PES_ID AND   "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? AND SRESDIV_OM_OC_ID = ? "
			+ " ORDER BY  SRESPES_NOME ";

	final String SQL_QTDDIVIDATOTAISDTINCLUSAO = " SELECT COUNT(SRESDIV_ID) QTD from SRES_DIVIDA " + " WHERE  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? AND    "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ?";

	final String SQL_DIVIDATOTAISDTINCLUSAO = " select SRESDIV_ID, SRESLANC_NUM_DOC_AUT, TO_CHAR(SRESLANC_DT_DOC_AUT,'DD/MM/YYYY') SRESLANC_DT_DOC_AUT, tot.INICIAL INICIAL, "
			+ "      tot.REVERSAO REVERSAO, tot.DEVOLUCAO DEVOLUCAO, tot.PERDOADO PERDOADO, SRESMOT_NOME  "
			+ " from SRES_LANCAMENTO l, SRES_DIVIDA d, SRES_MOTIVO,                                "
			+ "      (select SRESLANC_DIV_ID, SUM(DECODE(SRESLANC_TPLANC_ID,'1',SRESLANC_VALOR,0)) INICIAL, "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'2',SRESLANC_VALOR,0)) REVERSAO,  "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'3',SRESLANC_VALOR,0)) DEVOLUCAO, "
			+ "  SUM(DECODE(SRESLANC_TPLANC_ID,'4',SRESLANC_VALOR,0)) PERDOADO   "
			+ " from SRES_DIVIDA, SRES_LANCAMENTO      " + " WHERE SRESLANC_DIV_ID = SRESDIV_ID AND "
			+ " SRESLANC_ID NOT IN ( SELECT DISTINCT SRESLANC_LANC_ID          "
			+ " FROM SRES_LANCAMENTO                     " + " WHERE SRESLANC_LANC_ID IS NOT NULL ) AND "
			+ " SRESLANC_TPLANC_ID <> 5 AND        " + " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? AND    "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ? " + " GROUP BY SRESLANC_DIV_ID ) tot "
			+ " WHERE l.SRESLANC_DIV_ID = d.SRESDIV_ID AND  SRESLANC_TPLANC_ID in('1') AND "
			+ " SRESLANC_ID NOT IN ( SELECT DISTINCT SRESLANC_LANC_ID  "
			+ " FROM SRES_LANCAMENTO WHERE SRESLANC_LANC_ID IS NOT NULL ) AND "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),4,2) =  ? AND	  "
			+ " substr(to_char(sresdiv_resp_dt,'dd/mm/yyyy'),7,4) =  ? AND    "
			+ " SRESDIV_OM_OC_ID = ? AND  SRESDIV_PES_ID = ? "
			+ " AND tot.SRESLANC_DIV_ID = l.SRESLANC_DIV_ID  AND SRESMOT_ID   = SRESDIV_MOT_ID   ";
// FIM BLOCO RELATORIO DE DIVIDA MENSAL POR OC - DATA INCLUSAO NO SISTEMA

//INICIO CONSULTA QUE EXCLUI A DIVIDA NO BANCO
	final String SQL_DELETEDIVIDA = " DELETE FROM SRES_DIVIDA WHERE SRESDIV_ID = ? ";
//FIM CONSULTA QUE EXCLUI A DIVIDA NO BANCO

//INICIO CONSULTA QUE EXCLUI UM LANCAMENTO
	final String SQL_DELETELANCAMENTO = " DELETE FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID = ? ";
//FIM CONSULTA QUE EXCLUI UM LANCAMENTO

	/*
	 * final String SQL_UPDATEDIVIDA = "  UPDATE SRES_DIVIDA SET  " +
	 * "  SRESDIV_OM_OC_ID         = ?,  " +
	 * "  SRESDIV_AGE              = ?,  SRESDIV_CCR  = ?,  SRESDIV_BCO_ID = ?, " +
	 * "  SRESDIV_RESP_ALT         = ?," + "  SRESDIV_RESP_DT   = SYSDATE , " +
	 * "  SRESDIV_PES_ID    = ? , " + "  SRESDIV_MOT_ID    = ?,  " +
	 * "  SRESDIV_DT_MOTIVO = ?,  " + "  SRESDIV_CODIGO    = ?   " +
	 * "  WHERE SRESDIV_ID  = ?   ";
	 */

//INICIO CONSULTA PARA MODIFICAR A DIVIDA
	final String SQL_UPDATEDIVIDA = "  UPDATE SRES_DIVIDA SET  " + "  SRESDIV_DOC_ENVIO        = ?,  "
			+ "  SRESDIV_NUM_DOC_ENVIO    = ?,  " + "  SRESDIV_DT_DOC_ENVIO     = ?,  "
			+ "  SRESDIV_RESP_ALT         = ?,  " + "  SRESDIV_RESP_DT   = SYSDATE,   " + "  SRESDIV_MOT_ID    = ?,  "
			+ "  SRESDIV_DT_MOTIVO = ?   " + "  WHERE SRESDIV_ID  = ?   ";
//FIM CONSULTA PARA MODIFICAR A DIVIDA

//INICIO CONSULTA PARA MODIFICAR O LANCAMENTO
	final String SQL_UPDATELANCAMENTO = " UPDATE  SRES_LANCAMENTO SET   " + " SRESLANC_NUM_DOC_AUT    = ? , "
			+ " SRESLANC_TIPO_DOC_AUT   = ? , " + " SRESLANC_DT_DOC_AUT     = ? , " + " SRESLANC_ORIGEM_DOC_AUT = ? , "
			+ " SRESLANC_OBS            = ? , " + " SRESLANC_RESP_ALT        = ? , "
			+ " SRESLANC_RESP_DT         = SYSDATE  " + " WHERE SRESLANC_ID = ? ";
//INICIO CONSULTA PARA MODIFICAR O LANCAMENTO

//INICIO CONSULTA PARA INCLUIR REVERSAO	
	final String SQL_ADD_REVERSAO = "INSERT INTO SRES_LANCAMENTO(" + "SRESLANC_ID , " + "SRESLANC_CODIGO, "
			+ "SRESLANC_NUM_DOC_AUT, " + "SRESLANC_TIPO_DOC_AUT, " + "SRESLANC_DT_DOC_AUT, "
			+ "SRESLANC_ORIGEM_DOC_AUT, " + "SRESLANC_OPERADOR, " + "SRESLANC_VALOR, " + "SRESLANC_OBS, "
			+ "SRESLANC_RESP_ALT, " + "SRESLANC_DIV_ID, " + "SRESLANC_TPLANC_ID, "
			// + "SRESLANC_LANC_ID, "
			+ "SRESLANC_RESP_DT) " + "VALUES (SQ_SRES_LANCAMENTO_ID.nextval, ?,?,?,?,?,?,?,?,?,?,?,SYSDATE)";
//FIM CONSULTA PARA INCLUIR REVERSAO

//INICIO FAZ CONTAGEM DE LANCAMENTO DE REVERSAO PARA VERIFICAR REDUNDANCIA
	final String SQL_VERIFICA_REVERSAO = "SELECT count(*) QTDE from SRES_LANCAMENTO"
			+ " WHERE  SRESLANC_NUM_DOC_AUT =  ? AND SRESLANC_DT_DOC_AUT =  ? AND  "
			+ " SRESLANC_ORIGEM_DOC_AUT = ? AND  SRESLANC_DIV_ID = ? ";
//FIM FAZ CONTAGEM DE LANCAMENTO DE REVERSAO PARA VERIFICAR REDUNDANCIA

//INICIO CONSULTA QUE BUSCA AS INFORMACOES DO LANCAMENTO
	final String SQL_READ_LANCAMENTO = " SELECT * FROM SRES_LANCAMENTO WHERE SRESLANC_ID = ?";
//FIM CONSULTA QUE BUSCA AS INFORMACOES DO LANCAMENTO

//INICIO CONSULTA QUE BUSCA AS DIVIDAS HISTORICAS COM ESTADO 'EM ESPERA'
	final String SQL_REL_DIVHIST_ESPERA = "SELECT SRESPES_ID, SRESDIV_CGS, SRESPES_MATFIN, SRESPES_NOME, SRESPES_CPF, SRESPES_PT, SRESPES_SITUACAO, "
			+ "SRESDIV_ID, SRESDIV_CODIGO, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, "
			+ "SRESDIV_REF_INICIO_MES, SRESDIV_REF_INICIO_ANO, "
			+ "SRESDIV_REF_TERMINO_MES, SRESDIV_REF_TERMINO_ANO, SRESDIV_VAL, "
			+ "SRESDIV_DOC_ENVIO, SRESDIV_NUM_DOC_ENVIO, SRESDIV_DT_DOC_ENVIO, "
			+ "SRESLANC_ORIGEM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESLANC_NUM_DOC_AUT, SRESLANC_DT_DOC_AUT, "
			+ "SRESLANC_OBS, SRESLANC_ID, SRESLANC_CODIGO, SRESLANC_DIV_ID, "
			+ "SRESDIV_MOT_ID, SRESMOT_NOME, SRESDIV_DT_MOTIVO, "
			+ "SRESOM_NOME, SRESOM_OM_ID, SRESOM_OM_OC_COD, SRESBCO_COD, SRESDIV_TPDIV_ID "
			+ "FROM SRES_DIVIDA, SRES_PESSOA_RR, SRES_LANCAMENTO, SRES_MOTIVO, SRES_ORGANIZACAO_MILITAR, SRES_BANCO  "
			+ "WHERE SRESPES_ID = SRESDIV_PES_ID AND SRESDIV_ID = SRESLANC_DIV_ID AND SRESBCO_ID = SRESDIV_BCO_ID AND "
			+ "SRESDIV_MOT_ID = SRESMOT_ID AND SRESOM_OM_ID  = SRESDIV_OM_OC_ID AND "
			+ "SRESDIV_ESTADO = 'EM ESPERA' AND SRESDIV_TPDIV_ID = 2 AND SRESLANC_TPLANC_ID = 1 AND "
			+ "SRESDIV_DOC_ENVIO IS NOT NULL AND SRESDIV_NUM_DOC_ENVIO IS NOT NULL AND SRESDIV_DT_DOC_ENVIO IS NOT NULL";
//FIM CONSULTA QUE BUSCA AS DIVIDAS HISTORICAS COM ESTADO 'EM ESPERA'

//INICIO CONSULTA DIVIDAS MENSAIS COM STATUS 'EM ABERTO'    
	final String SQL_REL_DIVMES_ESPERA = " SELECT SRESDIV_CGS, SRESPES_ID, SRESPES_MATFIN, SRESPES_MATSIAP, SRESPES_NOME, SRESPES_CPF, SRESPES_PT, SRESPES_SITUACAO, "
			+ "SRESDIV_ID, SRESDIV_CODIGO, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, "
			+ "SRESDIV_REF_INICIO_MES, SRESDIV_REF_INICIO_ANO, "
			+ "SRESDIV_REF_TERMINO_MES, SRESDIV_REF_TERMINO_ANO, SRESDIV_VAL, "
			+ "SRESDIV_DOC_ENVIO, SRESDIV_NUM_DOC_ENVIO, SRESDIV_DT_DOC_ENVIO, "
			+ "SRESLANC_ORIGEM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESLANC_NUM_DOC_AUT, SRESLANC_DT_DOC_AUT, "
			+ "SRESLANC_OBS, SRESLANC_ID, SRESLANC_CODIGO, SRESLANC_DIV_ID, "
			+ "SRESDIV_MOT_ID, SRESMOT_NOME, SRESDIV_DT_MOTIVO, "
			+ " SRESOM_NOME, SRESOM_OM_ID, SRESOM_OM_OC_COD, SRESBCO_COD, SRESDIV_TPDIV_ID "
			+ "FROM SRES_DIVIDA, SRES_PESSOA_RR, SRES_LANCAMENTO, SRES_MOTIVO, SRES_ORGANIZACAO_MILITAR, SRES_BANCO "
			+ "WHERE SRESPES_ID = SRESDIV_PES_ID AND SRESDIV_ID = SRESLANC_DIV_ID AND SRESBCO_ID = SRESDIV_BCO_ID AND "
			+ "SRESDIV_MOT_ID = SRESMOT_ID AND SRESOM_OM_ID  = SRESDIV_OM_OC_ID "
			+ "AND SRESDIV_ESTADO = 'EM ESPERA' AND SRESDIV_TPDIV_ID = 1 AND SRESLANC_TPLANC_ID = 1  ";
//FIM CONSULTA DIVIDAS MENSAIS COM STATUS 'EM ABERTO'

//INICIO CONSULTA LANCAMENTOS DE REVERSAO NA DIVIDA
	final String SQL_LANCAMENTO_REVERSAO = "SELECT * FROM SRES_LANCAMENTO WHERE SRESLANC_TPLANC_ID = 2 AND SRESLANC_DIV_ID = ? ";
//INICIO CONSULTA LANCAMENTOS DE REVERSAO NA DIVIDA

//INICIO SQL CALCULA VALOR DA DIVIDA    
	final String SQL_VALOR_DIVIDA = " select sreslanc_div_id div_id, sum(decode(sreslanc_operador, 'D',sreslanc_valor, -sreslanc_valor)) Valor "
			+ " from sres_lancamento where sreslanc_div_id = ? " + " group by  sreslanc_div_id ";
//FIM SQL CALCULA VALOR DA DIVIDA    

//INICIO SQL CALCULA SALDO A REGULARIZAR (VALOR EM ABERTO DA DIVIDA)    
	final String SQL_SALDO_REGULARIZAR = "SELECT SUM(DECODE(SRESLANC_OPERADOR,'C',-SRESLANC_VALOR,SRESLANC_VALOR)) SALDOAREGULARIZAR FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID = ? ";

//FIM SQL CALCULA SALDO A REGULARIZAR (VALOR EM ABERTO DA DIVIDA)

	final String SQL_TIPO_DIVIDA = " SELECT * FROM SRES_TIPO_DIVIDA WHERE TRIM(SRESTPDIV_NOME) = TRIM( ? )";

	final String SQL_TIPO_BANCO = "SELECT * FROM SRES_BANCO WHERE TRIM(SRESBCO_COD) = TRIM( ? )";

	final String SQL_INSERIR_LANCAMENTO_DEVOLUCAO = " INSERT INTO SRES_LANCAMENTO (SRESLANC_ID, SRESLANC_CODIGO, "
			+ " SRESLANC_NUM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESLANC_DT_DOC_AUT, "
			+ " SRESLANC_ORIGEM_DOC_AUT, SRESLANC_OPERADOR, SRESLANC_VALOR, SRESLANC_OBS, "
			+ " SRESLANC_DIV_ID, SRESLANC_TPLANC_ID, SRESLANC_RESP_ALT, SRESLANC_RESP_DT, "
			+ " SRESLANC_NUM_0B, SRESLANC_DT_OB ) "
			+ " VALUES (SQ_SRES_LANCAMENTO_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";

	final String SQL_INSERIR_LANCAMENTO_PERDAO = " INSERT INTO SRES_LANCAMENTO (SRESLANC_ID, SRESLANC_CODIGO, "
			+ " SRESLANC_NUM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESLANC_DT_DOC_AUT, "
			+ " SRESLANC_ORIGEM_DOC_AUT, SRESLANC_OPERADOR, SRESLANC_VALOR, SRESLANC_OBS, "
			+ " SRESLANC_DIV_ID, SRESLANC_TPLANC_ID, SRESLANC_RESP_ALT, SRESLANC_RESP_DT) "
			+ " VALUES (SQ_SRES_LANCAMENTO_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
	/*
	 * //INICIO CONSULTA DO RELATORIO DE MOVIMENTACAO SISRES final String
	 * SQL_MOVIMENTACAO_SISRES =
	 * " SELECT SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) MESANOATUAL, "
	 * + " SRESLANC_ORIGEM_DOC_AUT OC,  SRESOM_NOME NOME_OC ," +
	 * " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,0)),0) INCLUSAO,"
	 * +
	 * " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'C',SRESLANC_VALOR,0)),0) EXCLUSAO"
	 * + " FROM SRES_LANCAMENTO, sres_divida, SRES_ORGANIZACAO_MILITAR " +
	 * " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
	 * + "  	AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD  " +
	 * "  AND sres_divida.sresdiv_id = sreslanc_div_id " +
	 * "  AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
	 * + "  AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) = ?  " +
	 * "  AND sres_divida.sresdiv_estado <> 'EM ESPERA' " +
	 * "  GROUP BY SRESLANC_ORIGEM_DOC_AUT, SRESOM_NOME, SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7)"
	 * + "  ORDER BY SRESLANC_ORIGEM_DOC_AUT ";
	 * 
	 * 
	 * final String SQL_MOVIMENTACAO_SISRES_MES_ANTERIOR =
	 * "select sum(SALDOMESANTECESSOR) SALDOMESANTECESSOR " + "from ( " +
	 * "SELECT SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,-SRESLANC_VALOR)) SALDOMESANTECESSOR "
	 * + " FROM SRES_LANCAMENTO, SRES_ORGANIZACAO_MILITAR, SRES_DIVIDA " +
	 * " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
	 * + "      AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD " +
	 * " AND sres_divida.sresdiv_id = sreslanc_div_id " +
	 * " AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
	 * + " AND sres_divida.sresdiv_estado <> 'EM ESPERA' " +
	 * "      AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) between ? and ? "
	 * + "      AND SRESLANC_ORIGEM_DOC_AUT = ? " +
	 * "      GROUP BY SRESLANC_DT_LANCAMENTO, SRESLANC_ORIGEM_DOC_AUT " +
	 * " ORDER BY SRESLANC_ORIGEM_DOC_AUT) ";
	 * 
	 * 
	 * final String SQL_COUNT_MOVIMENTACAO = "select COUNT(NOME) QTD from ( " +
	 * " SELECT SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) MESANOATUAL, "
	 * + " SRESLANC_ORIGEM_DOC_AUT OC, SRESOM_NOME NOME, " +
	 * " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,0)),0) INCLUSAO,"
	 * +
	 * " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'C',SRESLANC_VALOR,0)),0) EXCLUSAO"
	 * + " FROM SRES_LANCAMENTO, sres_divida, SRES_ORGANIZACAO_MILITAR " +
	 * " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
	 * + "  	AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD  " +
	 * "  AND sres_divida.sresdiv_id = sreslanc_div_id " +
	 * "  AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
	 * + "  AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) = ?  " +
	 * "  AND sres_divida.sresdiv_estado <> 'EM ESPERA' " +
	 * "  GROUP BY SRESLANC_ORIGEM_DOC_AUT, SRESOM_NOME, SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7)"
	 * + "  ORDER BY SRESLANC_ORIGEM_DOC_AUT) "; //FIM CONSULTA DO RELATORIO DE
	 * MOVIMENTACAO SISRES
	 */
	final String SQL_MOVIMENTACAO_SISRES = " SELECT SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) MESANOATUAL, "
			+ " SRESLANC_ORIGEM_DOC_AUT OC,  SRESOM_NOME NOME_OC ,"
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,0)),0) INCLUSAO,"
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'C',SRESLANC_VALOR,0)),0) EXCLUSAO"
			+ " FROM SRES_LANCAMENTO, sres_divida, SRES_ORGANIZACAO_MILITAR "
			+ " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
			+ "  	AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD  " + "  AND sres_divida.sresdiv_id = sreslanc_div_id "
			+ "  AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
			+ "  AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) = ?  "
			+ "  AND sres_divida.sresdiv_estado <> 'EM ESPERA' "
			+ "  GROUP BY SRESLANC_ORIGEM_DOC_AUT, SRESOM_NOME, SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7)"
			+ "  ORDER BY SRESLANC_ORIGEM_DOC_AUT ";

	final String SQL_MOVIMENTACAO_SISRES_MES_ANTERIOR = "select sum(SALDOMESANTECESSOR) SALDOMESANTECESSOR " + "from ( "
			+ "SELECT SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,-SRESLANC_VALOR)) SALDOMESANTECESSOR "
			+ " FROM SRES_LANCAMENTO, SRES_ORGANIZACAO_MILITAR, SRES_DIVIDA "
			+ " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
			+ "      AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD " + " AND sres_divida.sresdiv_id = sreslanc_div_id "
			+ " AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
			+ " AND sres_divida.sresdiv_estado <> 'EM ESPERA' "
			+ "      AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) between ? and ? "
			+ "      AND SRESLANC_ORIGEM_DOC_AUT = ? "
			+ "      GROUP BY SRESLANC_DT_LANCAMENTO, SRESLANC_ORIGEM_DOC_AUT " + " ORDER BY SRESLANC_ORIGEM_DOC_AUT) ";

	final String SQL_COUNT_MOVIMENTACAO = "select COUNT(NOME) QTD from ( "
			+ " SELECT SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) MESANOATUAL, "
			+ " SRESLANC_ORIGEM_DOC_AUT OC, SRESOM_NOME NOME, "
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,0)),0) INCLUSAO,"
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'C',SRESLANC_VALOR,0)),0) EXCLUSAO"
			+ " FROM SRES_LANCAMENTO, sres_divida, SRES_ORGANIZACAO_MILITAR "
			+ " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
			+ "  	AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD  " + "  AND sres_divida.sresdiv_id = sreslanc_div_id "
			+ "  AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
			+ "  AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) = ?  "
			+ "  AND sres_divida.sresdiv_estado <> 'EM ESPERA' "
			+ "  GROUP BY SRESLANC_ORIGEM_DOC_AUT, SRESOM_NOME, SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7)"
			+ "  ORDER BY SRESLANC_ORIGEM_DOC_AUT) ";

	final String SQL_CORRIGIR_DIVIDA = " INSERT INTO SRES_LANCAMENTO ( SRESLANC_ID, SRESLANC_CODIGO, SRESLANC_OPERADOR, "
			+ " SRESLANC_VALOR,SRESLANC_DIV_ID, SRESLANC_TPLANC_ID,"
			+ " SRESLANC_LANC_ID, SRESLANC_RESP_ALT, SRESLANC_RESP_DT) "
			+ " values (SQ_SRES_LANCAMENTO_ID.NEXTVAL, ?, ? , ?, ?, 5, ? , ? , SYSDATE)";
	final String SQL_LANCAMENTO_DIVIDA = " SELECT * FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID = ? AND SRESLANC_TPLANC_ID = ?";

	final String SQL_REVERSAO_POR_PEDIDO = " SELECT * FROM SRES_LANCAMENTO WHERE SRESLANC_TPLANC_ID = 2 AND "
			+ " SRESLANC_DIV_ID IN (SELECT SRESDIV_ID FROM SRES_DIVIDA WHERE SRESDIV_PEDIDO_REVERSAO = ? ) ";
	final String SQL_DELETE_PEDIDO = " DELETE SRES_PEDIDO WHERE SRESPED_ID = ? ";

	// TODO: Migrar SISPAG2 - OK
	final String SQL_BUSCA_ESTAGIO_CGS = " SELECT CGSM_ESTAG ESTAGIO, 20||CGSM_DT_PROC_A ANO, CGSM_DT_PROC_M MES FROM TAB_CGSM ";
	// TODO: Migrar SISPAG2 - OK
	final String SQL_BUSCA_RESPAREG_SISPAG = " SELECT nvl(sum(decode(cgsf_segfin,'3',1,'4',1,0)),0) qtdRespareg,    nvl(sum(decode(cgsf_segfin,'5',1,0)),0) qtdRegaresp  "
			+ " FROM  TAB_CGSF " + "	WHERE CGSF_VINC = '  ' AND  CGSF_STATUS = 'P' AND "
			+ "	SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') ";

	// TODO: Migrar SISPAG2 - OK
	/*
	 * final String SQL_BUSCA_RESPAREG_SISRES =
	 * "SELECT  COUNT(*) TOTAL FROM 	SRES_DIVIDA , 	" +
	 * "(	SELECT   SRESPES_ID, SRESOM_OM_ID,  CGSM_DT_PROC_M MES , " +
	 * "			 '20'||CGSM_DT_PROC_A ANO, SRESBCO_ID	" +
	 * "FROM TAB_CGSP, TAB_CGSF, TAB_CGSM  , SRES_ORGANIZACAO_MILITAR, " +
	 * "     SRES_BANCO, SRES_PESSOA_RR	" +
	 * " WHERE CGSP_MATFIN = CGSF_MATFIN AND CGSP_VINC = CGSF_VINC AND" +
	 * "       TRIM(SRESPES_MATFIN) = TRIM(CGSP_MATFIN) AND TRIM(SRESOM_OM_OM_COD) = TRIM(CGSP_LOTA_OC) "
	 * +
	 * "       AND	SRESBCO_COD =  CGSP_DEPBANC_BCO AND CGSP_VINC = ' '  AND   CGSP_STATUS = 'P'   AND"
	 * + "       CGSF_STATUS = 'P'         AND   " +
	 * " SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') AND CGSF_SEGFIN IN ('3','4')) RESPAREG "
	 * +
	 * " WHERE RESPAREG.SRESPES_ID = SRES_DIVIDA.SRESDIV_PES_ID AND SRES_DIVIDA.SRESDIV_OM_OC_ID = RESPAREG.SRESOM_OM_ID   AND "
	 * +
	 * " SRES_DIVIDA.SRESDIV_REF_INICIO_ANO = RESPAREG.ANO  AND SRES_DIVIDA.SRESDIV_REF_INICIO_MES = RESPAREG.MES  AND "
	 * + " SRES_DIVIDA.SRESDIV_BCO_ID =  RESPAREG.SRESBCO_ID";
	 */

	final String SQL_BUSCA_RESPAREG_SISRES = "SELECT  COUNT(*) TOTAL FROM 	SRES_DIVIDA , "
			+ "(	SELECT   SRESPES_ID, SRESOM_OM_ID,  CGSM_DT_PROC_M MES , "
			+ "			 '20'||CGSM_DT_PROC_A ANO, SRESBCO_ID	"
			+ " FROM CGS_HISTORICO.TAB_CGSP, CGS_HISTORICO.TAB_CGSF, CGS_HISTORICO.TAB_CGSM  , SRES_ORGANIZACAO_MILITAR,      SRES_BANCO, SRES_PESSOA_RR "
			+ " WHERE TRIM(CGSP_MATFIN) = TRIM(CGSF_MATFIN) AND CGSP_VINC = CGSF_VINC AND "
			+ "      TRIM(SRESPES_MATFIN) = TRIM(CGSP_MATFIN) AND TRIM(SRESOM_OM_OM_COD) = TRIM(CGSP_LOTA_OC) "
			+ "      AND	SRESBCO_COD =  CGSP_DEPBANC_BCO AND CGSP_VINC = '  '  AND   CGSP_STATUS = 'P'   AND "
			+ "      CGSF_STATUS = 'P'         AND    "
			+ " SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') AND CGSF_SEGFIN IN ('3','4') " + " ) RESPAREG  "
			+ " WHERE RESPAREG.SRESPES_ID = SRES_DIVIDA.SRESDIV_PES_ID AND SRES_DIVIDA.SRESDIV_OM_OC_ID = RESPAREG.SRESOM_OM_ID   AND "
			+ " SRES_DIVIDA.SRESDIV_REF_INICIO_ANO = RESPAREG.ANO  AND SRES_DIVIDA.SRESDIV_REF_INICIO_MES = RESPAREG.MES  AND "
			+ " SRES_DIVIDA.SRESDIV_BCO_ID =  RESPAREG.SRESBCO_ID ";

	// TODO: Migrar SISPAG2 - OK
	/*
	 * final String SQL_INSERIR_LANCAMENTO_RESPAREG =
	 * " INSERT INTO SRES_DIVIDA (SRESDIV_ID, SRESDIV_OM_OC_ID, SRESDIV_REF_INICIO_MES, "
	 * +
	 * "SRESDIV_REF_INICIO_ANO,                     SRESDIV_CODIGO, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, SRESDIV_VAL,    "
	 * +
	 * "                 SRESDIV_ESTADO, SRESDIV_CGS, SRESDIV_PES_ID, SRESDIV_MOT_ID, SRESDIV_TPDIV_ID, SRESDIV_resp_dt,"
	 * +
	 * "SRESDIV_RESP_ALT) (select  (SELECT MAX(SRESDIV_ID) FROM SRES_DIVIDA)+ ROWNUM,         SRESOM_OM_ID,  "
	 * +
	 * "CGSM_DT_PROC_M, '20'||CGSM_DT_PROC_A,         NVL((SELECT MAX(SRESDIV_CODIGO) "
	 * +
	 * "FROM SRES_DIVIDA WHERE SRESDIV_PES_ID =SRESPES_ID) +1,1)  SRESDIV_CODIGO,         CGSP_DEpBANC_AGE,   "
	 * +
	 * "CGSP_DEpBANC_CCR, SRESBCO_ID, CGSF_VAL,        'CONCLUIDO' SRESDIV_ESTADO, 'S' SRESDIV_CGS, "
	 * +
	 * "SRESPES_ID,         2 SRESDIV_MOT_ID, 3 SRESDIV_TPDIV_ID, SYSDATE, user      "
	 * +
	 * "from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsf, CGS_HISTORICO.tab_cgsm  , SRES_ORGANIZACAO_MILITAR, SRES_BANCO, SRES_PESSOA_RR "
	 * +
	 * "where cgsp_matfin = cgsf_matfin and       cgsp_vinc   = cgsf_vinc   and       "
	 * +
	 * "TRIM(SRESPES_MATFIN) = TRIM(CGSP_MATFIN) AND        TRIM(SRESOM_OM_Om_COD) = TRIM(CGSP_LOTA_OC) and       "
	 * +
	 * "SRESBCO_COD =  CGSP_DEpBANC_BCO AND        cgsp_vinc = ' '           and       cgsp_status = 'P'         and        "
	 * +
	 * "cgsf_status = 'P'         and       SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN IN ('3','4') )"
	 * ;
	 */

	String SQL_INSERIR_LANCAMENTO_RESPAREG = " INSERT INTO SRES_DIVIDA (SRESDIV_ID, SRESDIV_OM_OC_ID, SRESDIV_REF_INICIO_MES, "
			+ "SRESDIV_REF_INICIO_ANO,                     SRESDIV_CODIGO, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, SRESDIV_VAL,    "
			+ "                 SRESDIV_ESTADO, SRESDIV_CGS, SRESDIV_PES_ID, SRESDIV_MOT_ID, SRESDIV_TPDIV_ID, SRESDIV_resp_dt,"
			+ "SRESDIV_RESP_ALT) (select  (SELECT MAX(SRESDIV_ID) FROM SRES_DIVIDA)+ ROWNUM,         SRESOM_OM_ID,  "
			+ "CGSM_DT_PROC_M, '20'||CGSM_DT_PROC_A,         NVL((SELECT MAX(SRESDIV_CODIGO) "
			+ "FROM SRES_DIVIDA WHERE SRESDIV_PES_ID =SRESPES_ID) +1,1)  SRESDIV_CODIGO,         CGSP_DEpBANC_AGE,   "
			+ "CGSP_DEpBANC_CCR, SRESBCO_ID, CGSF_VAL,        'CONCLUIDO' SRESDIV_ESTADO, 'S' SRESDIV_CGS, "
			+ "SRESPES_ID,         2 SRESDIV_MOT_ID, 3 SRESDIV_TPDIV_ID, SYSDATE, user      "
			+ "from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsf, CGS_HISTORICO.tab_cgsm  , SRES_ORGANIZACAO_MILITAR, SRES_BANCO, SRES_PESSOA_RR "
			+ "where cgsp_matfin = cgsf_matfin and       cgsp_vinc   = cgsf_vinc   and       "
			+ "TRIM(SRESPES_MATFIN) = TRIM(CGSP_MATFIN) AND        TRIM(SRESOM_OM_Om_COD) = TRIM(CGSP_LOTA_OC) and       "
			+ "SRESBCO_COD =  CGSP_DEpBANC_BCO AND        cgsp_vinc = '  '           and       cgsp_status = 'P'         and        "
			+ "cgsf_status = 'P'         and       SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN IN ('3','4') )";

	// TODO: Migrar SISPAG2 - OK
	/*
	 * final String SQL_INSERIR_LANCAMENTO_REGARESP =
	 * " INSERT INTO SRES_LANCAMENTO ( " + "  SRESLANC_ID, " +
	 * "         SRESLANC_DIV_ID,  " + "         SRESLANC_CODIGO, " +
	 * "           SRESLANC_TPLANC_ID, " + "          SRESLANC_VALOR, " +
	 * "         SRESLANC_OPERADOR, " + "          SRESLANC_ORIGEM_DOC_AUT,  " +
	 * 
	 * "         SRESLANC_RESP_ALT, " + "         SRESLANC_RESP_DT)  " +
	 * 
	 * " select SQ_SRES_LANCAMENTO_ID.nextval, SRESDIV_ID, " +
	 * " NVL((SELECT MAX(SRESLANC_CODIGO) FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID =SRESDIV_ID),0) +1  "
	 * + ", 2, CGSF_VAL,'C','CGS',  " + "       USER,SYSDATE " +
	 * " from sres_divida, sres_pessoa_rr, tab_cgsf , (  SELECT substr( TO_DATE(01||'/'||CGSm_dt_PROC_M||'/'||CgSm_dt_PROC_A)-1, 4,2) Mes, substr( TO_DATE(01||'/'||CGSm_dt_PROC_M||'/'||CgSm_dt_PROC_A)-1, 7,2) Ano "
	 * + " FROM TAB_CGSm) tab_cgsm  " + " where  " +
	 * " SRESDIV_REF_INICIO_MES = mes and " +
	 * " SRESDIV_REF_INICIO_ANO = '20'||ano and " +
	 * 
	 * " srespes_id = sresdiv_pes_id and trim(srespes_matfin) = trim(cgsf_matfin) and "
	 * + " SRESDIV_TPDIV_ID = 3 and  " + " cgsF_vinc = ' '           and " +
	 * 
	 * " cgsf_status = 'P'         and " +
	 * " SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN ='5' ";
	 */

	final String SQL_INSERIR_LANCAMENTO_REGARESP = " INSERT INTO SRES_LANCAMENTO ( " + "  SRESLANC_ID, "
			+ "         SRESLANC_DIV_ID,  " + "         SRESLANC_CODIGO, " + "           SRESLANC_TPLANC_ID, "
			+ "          SRESLANC_VALOR, " + "         SRESLANC_OPERADOR, " + "          SRESLANC_ORIGEM_DOC_AUT,  " +

			"         SRESLANC_RESP_ALT, " + "         SRESLANC_RESP_DT)  " +

			" select SQ_SRES_LANCAMENTO_ID.nextval, SRESDIV_ID, "
			+ " NVL((SELECT MAX(SRESLANC_CODIGO) FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID =SRESDIV_ID),0) +1  "
			+ ", 2, CGSF_VAL,'C','CGS',  " + "       USER,SYSDATE "
			+ " from sres_divida, sres_pessoa_rr, CGS_HISTORICO.tab_cgsf , (  SELECT substr( TO_DATE(01||'/'||CGSm_dt_PROC_M||'/'||CgSm_dt_PROC_A)-1, 4,2) Mes, substr( TO_DATE(01||'/'||CGSm_dt_PROC_M||'/'||CgSm_dt_PROC_A)-1, 7,2) Ano "
			+ " FROM CGS_HISTORICO.TAB_CGSm) tab_cgsm  " + " where  " + " SRESDIV_REF_INICIO_MES = mes and "
			+ " SRESDIV_REF_INICIO_ANO = '20'||ano and " +

			" srespes_id = sresdiv_pes_id and trim(srespes_matfin) = trim(cgsf_matfin) and "
			+ " SRESDIV_TPDIV_ID = 3 and  " + " cgsF_vinc = '  '           and " +

			" cgsf_status = 'P'         and " + " SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN ='5' ";

	// TODO: Migrar SISPAG2 - OK
	/*
	 * final String SQL_INSERIR_PESSOA_RESPAREG =
	 * "insert into sres_pessoa_rr ( srespes_matfin, srespes_nome, srespes_cpf, srespes_pt, srespes_situacao) "
	 * +
	 * " (  SELECT   trim(CGSP_MATFIN) , cgsp_nom, cgsp_cpf, CGSp_PT, cgsp_sit  from tab_cgsp, tab_cgsf   "
	 * + " where cgsp_matfin = cgsf_matfin and        " +
	 * " cgsp_vinc   = cgsf_vinc   and       cgsp_vinc = ' '           and       cgsp_status = 'P'         and "
	 * +
	 * "  cgsf_status = 'P'         and       SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN IN ('3','4') minus "
	 * +
	 * "  select trim(srespes_matfin), srespes_nome, srespes_cpf, srespes_pt, srespes_situacao from sres_pessoa_rr )"
	 * ;
	 */

	final String SQL_INSERIR_PESSOA_RESPAREG = "insert into sres_pessoa_rr ( srespes_matfin, srespes_nome, srespes_cpf, srespes_pt, srespes_situacao) "
			+ " (  SELECT   trim(CGSP_MATFIN) , cgsp_nom, cgsp_cpf, CGSp_PT, cgsp_sit  from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsf   "
			+ " where cgsp_matfin = cgsf_matfin and        "
			+ " cgsp_vinc   = cgsf_vinc   and       cgsp_vinc = '  '           and       cgsp_status = 'P'         and "
			+ "  cgsf_status = 'P'         and       SUBSTR(CGSF_PARC,1,5) IN ('32502', '32504') and CGSF_SEGFIN IN ('3','4') minus "
			+ "  select trim(srespes_matfin), srespes_nome, srespes_cpf, srespes_pt, srespes_situacao from sres_pessoa_rr )";

	final String SQL_QTD_MOVIMENTACAO_SISRES = " SELECT  count(*) QTD from ( select "
			+ " SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) MESANOATUAL, "
			+ " SRESLANC_ORIGEM_DOC_AUT OC,  SRESOM_NOME NOME_OC , "
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'D',SRESLANC_VALOR,0)),0) INCLUSAO, "
			+ " DECODE(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7),? ,SUM(DECODE(SRESLANC_OPERADOR,'C',SRESLANC_VALOR,0)),0) EXCLUSAO "
			+ " FROM SRES_LANCAMENTO, sres_divida, SRES_ORGANIZACAO_MILITAR "
			+ " WHERE SRESLANC_ORIGEM_DOC_AUT = SRES_ORGANIZACAO_MILITAR.SRESOM_OM_OC_COD "
			+ "       AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD " + "  AND sres_divida.sresdiv_id = sreslanc_div_id "
			+ " AND sres_divida.sresdiv_tpdiv_id not in (Select srestpdiv_id from sres_tipo_divida WHERE srestpdiv_nome = 'RESPAREG') "
			+ " AND SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) = ? "
			+ " AND sres_divida.sresdiv_estado <> 'EM ESPERA' "
			+ " GROUP BY SRESLANC_ORIGEM_DOC_AUT, SRESOM_NOME, SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7) "
			+ " ORDER BY SRESLANC_ORIGEM_DOC_AUT )";

	// TODO: Migrar SISPAG2 - OK - Corrigdo
//INICIO CONSULTA PROCESSO ATUAL DO CGS	
	// final String SQL_Consulta_Processo_Atual = "Select distinct(CGSM_DT_PROC_M),
	// CGSM_DT_PROC_A from tab_cgsp, tab_cgsm";
	final String SQL_Consulta_Processo_Atual = "Select CGSM_DT_PROC_M, CGSM_DT_PROC_A  from tab_cgsm";
//FIM CONSULTA PROCESSO ATUAL DO CGS

	public DAODivida() {

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
			connexao = ds.getConnection();
		} catch (SQLException e) {
			throw new SispagException(e.getMessage());
		}
	}

	public void fecharConexao(String metodo) throws SQLException {
		this.connexao.close();
		System.out.println("*************** Fechou Conexão " + metodo + " *************");
	}

	public int geraCodigoDiv(int idPes, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		int novocod = 0;

		final String RETRIEVE_GERACODIGODIVIDA = "SELECT MAX(SRESDIV_CODIGO) FROM SRES_DIVIDA "
				+ " WHERE SRESDIV_PES_ID =? ";
		try {
			// Cria sql
			stmt = conexao.prepareStatement(RETRIEVE_GERACODIGODIVIDA);
			stmt.setInt(1, idPes);

			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {
				novocod = results.getInt("MAX(SRESDIV_CODIGO)") + 1;
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

	private int geraCodigoLanc(int idDivida, Connection conexao) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		int novocod = 0;

		final String RETRIEVE_GERACODIGOLANCAMENTO = "SELECT MAX(SRESLANC_CODIGO) FROM SRES_LANCAMENTO WHERE "
				+ " SRESLANC_DIV_ID =?";

		try {
			// Cria sql
			stmt = conexao.prepareStatement(RETRIEVE_GERACODIGOLANCAMENTO);
			stmt.setInt(1, idDivida);

			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				novocod = results.getInt("MAX(SRESLANC_CODIGO)") + 1;
			}
			return novocod;
		} catch (SQLException e) {
			// System.out.println(e);
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

	public void setDivida(Divida divida) {
		this.divida = divida;
	}

	public void insereDivida(Divida div, String tipoDivida, String estado, String codUsuario) throws SispagException {

		//UsuarioService us =new UsuarioService();
		
		
		boolean transaction = false;
		String matricula = null;
		// System.out.println("Entrei no DAO cadastrar Divida");
		Connection conn= null;
		
			
			/* INSERE OU ATUALIZA PESSOA */
			Pessoa pessoaDiv = null;

			int idDivida = -1;

			// getPessoa - Divida
			pessoaDiv = div.getPessoa();

			// System.out.println("Tipo Divida: "+ tipoDivida);

			transaction = atualizaPessoa(pessoaDiv, div.getCgs());
			// System.out.println("passei do atualiza");

			if (transaction) {
				transaction = false;

				if (div.getCgs().equals("S")) {
					matricula = pessoaDiv.getMatFin();
				} else if (div.getCgs().equals("N")) {
					matricula = pessoaDiv.getMatSIAPE();
				}

				// Método readPessoa foi novamente chamado porque precisamos pegar o
				// id da Pessoa
				div.setPessoa(readPessoa(div.getCgs(), matricula));
				// System.out.println("passei do readPessoa");

				/* INSERE EM DIVIDA */
				PreparedStatement stmt = null;
				ResultSet results = null;
				// System.out.println("cheguei no SEQUENCE");

				final String RETRIEVE_SEQUENCEDIVIDA = " SELECT SQ_SRES_DIVIDA_ID.NEXTVAL NEXTVAL FROM DUAL " ;

				// Gera a Sequence de Dívida
				try {
					conn = ds.getConnection();
					// Cria sql
					stmt = conn.prepareStatement(RETRIEVE_SEQUENCEDIVIDA);

					// Executa sql
					results = stmt.executeQuery();
					if (results.next()) {
						idDivida = results.getInt("NEXTVAL");
					}
				} catch (SQLException e) {
					// System.out.println(e);
					throw new SispagException("Erro inserindo em dívida");
				} finally {
					DButils.closeQuietly(results, stmt, conn);
				}

				String RETRIEVE_INSEREDIVIDA = null;

				RETRIEVE_INSEREDIVIDA = "INSERT INTO SRES_DIVIDA (SRESDIV_ID, SRESDIV_OM_OC_ID, "
						+ " SRESDIV_REF_INICIO_MES, SRESDIV_REF_INICIO_ANO, "
						+ " SRESDIV_CODIGO, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, SRESDIV_VAL, "
						+ " SRESDIV_ESTADO, SRESDIV_CGS, SRESDIV_PES_ID, SRESDIV_MOT_ID, SRESDIV_TPDIV_ID, SRESDIV_DT_MOTIVO, "
						+ " SRESDIV_RESP_ALT, SRESDIV_RESP_DT";

				if (tipoDivida.equals("2")) {

					RETRIEVE_INSEREDIVIDA += ",SRESDIV_REF_TERMINO_MES, SRESDIV_REF_TERMINO_ANO, SRESDIV_DOC_ENVIO, SRESDIV_NUM_DOC_ENVIO, SRESDIV_DT_DOC_ENVIO";

				}

				RETRIEVE_INSEREDIVIDA += ") " + " VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, "
						+ " ?, SYSDATE";

				if (tipoDivida.equals("2")) {

					RETRIEVE_INSEREDIVIDA += ",?,?,?,?,?";

				}
				RETRIEVE_INSEREDIVIDA += ") ";

				try {
					conn = ds.getConnection();
					// Cria sql
					stmt = conn.prepareStatement(RETRIEVE_INSEREDIVIDA);
					stmt.setInt(1, idDivida);
					// System.out.println("idDivida "+idDivida);
					stmt.setInt(2, div.getOc().getId()); // buscar id oc
					// System.out.println("getOC "+div.getOc().getId());
					stmt.setString(3, div.getMesProcessoGeracao());
					// System.out.println("div.getMesProcessoGeracao()
					// "+div.getMesProcessoGeracao());
					stmt.setString(4, div.getAnoProcessoGeracao());
					// System.out.println("div.getAnoProcessoGeracao()
					// "+div.getAnoProcessoGeracao());
					stmt.setInt(5, geraCodigoDiv(div.getPessoa().getId(), conn));
					// System.out.println("geraCodigoDiv(div.getPessoa().getId())
					// "+geraCodigoDiv(div.getPessoa().getId(),conn));
					stmt.setString(6, div.getAgencia());
					// System.out.println("div.getAgencia() "+div.getAgencia());
					stmt.setString(7, div.getContaCorrente());
					// System.out.println("div.getContaCorrente() "+div.getContaCorrente());
					stmt.setString(8, buscaIdBancoByCodigo(div.getBanco(), conn)); // insere id banco
					// System.out.println("buscaIdBancoByCodigo(div.getBanco())
					// "+buscaIdBancoByCodigo(div.getBanco(), conn));
					stmt.setDouble(9, div.getValor());
					// System.out.println("div.getValor() "+div.getValor());
					stmt.setString(10, estado);
					// System.out.println("estado "+estado);
					stmt.setString(11, div.getCgs());
					// System.out.println("Sistema Origem: ");
					// if(div.getCgs().equals("S"))//System.out.println("SISPAG"); else
					// //System.out.println("SIAPE");
					stmt.setInt(12, div.getPessoa().getId()); // buscar id pessoa
					// System.out.println("div.getPessoa().getId() "+ div.getPessoa().getId());
					stmt.setInt(13, buscaIdMotivoByNome(div.getMotivo(), conn)); // buscar
					// System.out.println("buscaIdMotivoByNome(div.getMotivo()) "+
					// buscaIdMotivoByNome(div.getMotivo(), conn));

					// id
					// motivo

					// Tipo da Divida - Mes=1 / Histï¿½rica=2 / Respareg=3
					stmt.setString(14, tipoDivida);
					// System.out.println("tipoDivida "+ tipoDivida);
					stmt.setDate(15, div.getDataMotivo());
					// System.out.println("div.getDataMotivo() "+ div.getDataMotivo());
					stmt.setString(16, codUsuario);
					// System.out.println("codUsuario "+ codUsuario);

					if (tipoDivida.equals("2")) {

						stmt.setString(17, div.getMesTermino());
						// System.out.println("div.getMesTermino() "+ div.getMesTermino());
						stmt.setString(18, div.getAnoTermino());
						// System.out.println("div.getAnoTermino() "+ div.getAnoTermino());

						stmt.setString(19, div.getDocEnvio());
						// System.out.println("div.getDocEnvio() "+ div.getDocEnvio());
						stmt.setString(20, div.getNumeroDocEnvio());
						// System.out.println("div.getNumeroDocEnvio() "+ div.getNumeroDocEnvio());
						stmt.setDate(21, div.getDataDocEnvio());
						// System.out.println("div.getDataDocEnvio() "+ div.getDataDocEnvio());

					}
					// Executa sql
					// System.out.println("sql " + RETRIEVE_INSEREDIVIDA);
					// System.out.println("cheguei no execute");
					stmt.executeUpdate();
					// System.out.println("passei do execute");

					transaction = true;
				} catch (SQLException e) {
					System.out.println(e);
					throw new SispagException("Erro inserindo em dívida");
				} finally {
					DButils.closeQuietly(results, stmt,conn);				
				}

				// Lï¿½ a dívida para poder pegar o id - melhorar !!!!
				// idDivida = readDivida(matFin, idOc, mes,
				// div.getAnoProcessoGeracao()).;

				/* INSERE EM LANCAMENTO */
				if (transaction) {
					transaction = false;

					Lancamento lanc = new Lancamento();
					lanc = div.getLancamentoInicial();
					lanc.setValor(div.getValor());

					final String RETRIEVE_INSERELANCAMENTO = "INSERT INTO SRES_LANCAMENTO (SRESLANC_ID, SRESLANC_CODIGO, "
							+ "SRESLANC_NUM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESLANC_DT_DOC_AUT, "
							+ "SRESLANC_ORIGEM_DOC_AUT, SRESLANC_OPERADOR, SRESLANC_VALOR, SRESLANC_OBS, "
							+ "SRESLANC_DIV_ID, SRESLANC_TPLANC_ID, SRESLANC_RESP_ALT, SRESLANC_RESP_DT) "
							+ "VALUES (SQ_SRES_LANCAMENTO_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ";

					try {
						// Cria sql
					conn = ds.getConnection();
						stmt = conn.prepareStatement(RETRIEVE_INSERELANCAMENTO);

						stmt.setInt(1, geraCodigoLanc(idDivida, conn)); // buscar novo
						// codigo
						stmt.setString(2, lanc.getNumeroDocAutorizacao());
						stmt.setString(3, lanc.getTipoDocAutorizacao());
						stmt.setDate(4, (Date) lanc.getDataDocAutorizacao());
						stmt.setString(5, lanc.getOrigemDocAutorizacao());
						stmt.setString(6, "D");
						stmt.setDouble(7, lanc.getValor());
						stmt.setString(8, lanc.getObservacao());
						stmt.setInt(9, idDivida);
						stmt.setString(10, "1"); // tipo 1 = divida inicial

						stmt.setString(11, codUsuario); // ALTERAR DEPOIS

						// Executa sql
						results = stmt.executeQuery();

						transaction = true;
					} catch (SQLException e) {
						// System.out.println(e);
						throw new SispagException("Erro inserindo em dívida");
					} finally {
						DButils.closeQuietly(results, stmt, conn);
					}
				}
			}

			processTransaction(transaction);		

	}

	public void getDividasPessoas() {

	}

	
	public boolean atualizaPessoa(Pessoa pessoaDiv, String sistemaOrigem) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection conexao =null;
	//	String idPessoa = null;
		boolean sucesso = false;
		String campoQueryMatriculaPessoa = null;
		String matricula = null;
		
		if (sistemaOrigem.equals("S")) // S --> Divida oriunda do SISPAG | N --> Dívida oriunda do SIAPE
		{
			campoQueryMatriculaPessoa = " SRESPES_MATFIN ";
			matricula = pessoaDiv.getMatFin();
		} else if (sistemaOrigem.equals("N")) {
			campoQueryMatriculaPessoa = " SRESPES_MATSIAP ";
			matricula = pessoaDiv.getMatSIAPE();
		}

		final String RETRIEVE_ATUALIZAPESSOA = "UPDATE SRES_PESSOA_RR SET SRESPES_NOME = ?, "
				+ " SRESPES_PT = ?, SRESPES_CPF= ?, SRESPES_SITUACAO = ? WHERE " + campoQueryMatriculaPessoa + " = ? ";
		final String RETRIEVE_INSEREPESSOA = "INSERT INTO SRES_PESSOA_RR (SRESPES_ID," + campoQueryMatriculaPessoa
				+ ", SRESPES_NOME, SRESPES_PT, " + " SRESPES_CPF, SRESPES_SITUACAO)"
				+ " VALUES (SQ_SRES_PESSOA_RR_ID.NEXTVAL, UPPER(?) ,UPPER(?) , ? , ? , ? ) ";

		// Retorna a pessoa que se encontra no SISRES
		Pessoa pessoaSISRES = readPessoa(sistemaOrigem, matricula);

		if (pessoaSISRES == null) { // Insere pessoa
			try {
				// Cria sql
				conexao = ds.getConnection();
				stmt = conexao.prepareStatement(RETRIEVE_INSEREPESSOA);

				if (sistemaOrigem.equals("S"))
					stmt.setString(1, pessoaDiv.getMatFin());
				else if (sistemaOrigem.equals("N"))
					stmt.setString(1, pessoaDiv.getMatSIAPE());
				stmt.setString(2, pessoaDiv.getNome());
				stmt.setString(3, pessoaDiv.getPosto());
				stmt.setString(4, pessoaDiv.getCpf());
				stmt.setString(5, pessoaDiv.getSituacao());
				// Executa sql
				// System.out.println("vai executar o sql RETRIEVE_INSEREPESSOA");
				stmt.executeUpdate();
				// System.out.println("passou do sql RETRIEVE_INSEREPESSOA");

				sucesso = true;
			} catch (SQLException e) {
				// System.out.println(e);
			} finally {
				DButils.closeQuietly(results, stmt, conexao);
			}
		} else { // Atualiza pessoa
			
			try {
				conexao = ds.getConnection();
				// Cria sql
				stmt = conexao.prepareStatement(RETRIEVE_ATUALIZAPESSOA);
				stmt.setString(1, pessoaDiv.getNome());
				// System.out.println("pessoaDiv.getNome() "+ pessoaDiv.getNome());
				stmt.setString(2, pessoaDiv.getPosto());
				// System.out.println("pessoaDiv.getPosto() "+ pessoaDiv.getPosto());
				stmt.setString(3, pessoaDiv.getCpf());
				// System.out.println("pessoaDiv.getCpf() "+ pessoaDiv.getCpf());
				stmt.setString(4, pessoaDiv.getSituacao());
				// System.out.println("pessoaDiv.getSituacao() "+ pessoaDiv.getSituacao());
				if (sistemaOrigem.equals("S")) {
					stmt.setString(5, pessoaDiv.getMatFin().trim());
					// System.out.println("pessoaDiv.getMatFin().trim() "+
					// pessoaDiv.getMatFin().trim());
				} else if (sistemaOrigem.equals("N")) {
					stmt.setString(5, pessoaDiv.getMatSIAPE().trim());
					// System.out.println("pessoaDiv.getMatSIAPE().trim() "+
					// pessoaDiv.getMatSIAPE().trim());
				}

				// Executa sql
				// System.out.println("vai executar o sql RETRIEVE_ATUALIZAPESSOA");
				// System.out.println("SQL "+ RETRIEVE_ATUALIZAPESSOA);
//				int n = stmt.executeUpdate();
				// System.out.println("passou do sql RETRIEVE_ATUALIZAPESSOA");

				// System.out.println(n);

				sucesso = true;
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				DButils.closeQuietly(results, stmt, conexao);				
			}			
		}
		return sucesso;
	}

	public OC readOC(String codOc) throws SispagException {
//		final String RETRIEVE_READOC = "SELECT SRESOM_OM_ID, SRESOM_OM_OC_COD, SRESOM_NOME FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_OC_COD = SRESOM_OM_OM_COD AND SRESOM_OM_OC_COD=? ";
		final String RETRIEVE_READOC = "SELECT SRESOM_OM_ID, SRESOM_OM_OC_COD, SRESOM_NOME FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_OM_COD=? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;

		try {

			conn = ds.getConnection();
			stmt = conn.prepareStatement(RETRIEVE_READOC);

			stmt.setString(1, codOc.trim());

			// Executa sql
			rs = stmt.executeQuery();

			if (rs.next()) {
				OC oc = new OC();
				oc.setId(rs.getInt("SRESOM_OM_ID"));
				oc.setOc(rs.getString("SRESOM_OM_OC_COD"));
				oc.setNome(rs.getString("SRESOM_NOME"));
				// System.out.println("Read oc certo");
				return oc;
			} else {
				// System.out.println("Read oc null");
				return null;
			}
		} catch (Exception e) {
			//// System.out.println(e);
			e.printStackTrace();
			// System.out.println("Read oc null 2");
			return null;
		} finally {
			DButils.closeQuietly(rs, stmt, conn);
		}
	}

	public Pessoa readPessoa(String sistemaOrigem, String matricula) {

		String campoQueryMatriculaPessoa = null;
		

		if (sistemaOrigem.equals("S")) // S --> Divida oriunda do SISPAG | N --> Dívida oriunda do SIAPE
		{
			campoQueryMatriculaPessoa = " SRESPES_MATFIN ";
		} else if (sistemaOrigem.equals("N")) {
			campoQueryMatriculaPessoa = " SRESPES_MATSIAP ";
		}

		final String RETRIEVE_READPESSOA = "SELECT * FROM SRES_PESSOA_RR WHERE " + campoQueryMatriculaPessoa + "=? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
//		boolean flag = false;

		Connection conexao = null;
		try {
			
			conexao = ds.getConnection();
			// Cria sql
			stmt = conexao.prepareStatement(RETRIEVE_READPESSOA);
			stmt.setString(1, matricula);

			// Executa sql
			rs = stmt.executeQuery();
			if (!rs.next()) {
				// //System.out.println("Matrícula não encontrada!");
				return null;
			} else {
				Pessoa pes = new Pessoa();
				pes.setId(rs.getInt("SRESPES_ID"));
				pes.setMatFin(rs.getString("SRESPES_MATFIN"));
				pes.setMatSIAPE(rs.getString("SRESPES_MATSIAP"));
				pes.setNome(rs.getString("SRESPES_NOME"));
				pes.setCpf(rs.getString("SRESPES_CPF"));
				pes.setPosto(rs.getString("SRESPES_PT"));
				pes.setSituacao(rs.getString("SRESPES_SITUACAO"));

				return pes;
			}
		} catch (Exception e) {
			// System.out.println(e);
			return null;
		} finally {
			DButils.closeQuietly(rs, stmt, conexao);
		}
	}

	public Divida readDivida(String sistemaOrigem, String matricula, OC idOc, String mes, String ano)
			throws SispagException {

		String campoQueryMatriculaPessoa = null;
		if (sistemaOrigem.equals("S")) // S --> Divida oriunda do SISPAG | N --> Dívida oriunda do SIAPE
		{
			campoQueryMatriculaPessoa = " SRESPES_MATFIN ";
		} else if (sistemaOrigem.equals("N")) {
			campoQueryMatriculaPessoa = " SRESPES_MATSIAP ";
		}

		final String RETRIEVE_READDIVIDA = "SELECT * FROM SRES_DIVIDA, SRES_PESSOA_RR "
				+ " WHERE SRESDIV_PES_ID = SRESPES_ID AND SRESDIV_PES_ID = (SELECT SRESPES_ID FROM SRES_PESSOA_RR WHERE "
				+ campoQueryMatriculaPessoa + "=?) " + " AND SRESDIV_OM_OC_ID = ? "
				+ " AND SRESDIV_REF_INICIO_MES =? AND SRESDIV_REF_INICIO_ANO =? ";

		PreparedStatement stmt = null;
		Divida div = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			conn = ds.getConnection();
			// Cria sql
			stmt = conn.prepareStatement(RETRIEVE_READDIVIDA);

			stmt.setString(1, matricula);
			stmt.setInt(2, idOc.getId());
			stmt.setString(3, mes);
			stmt.setString(4, ano);

			// System.out.println(RETRIEVE_READDIVIDA);
			// System.out.println(idOc.getId());
			// System.out.println(mes);
			// System.out.println(ano);

			// Executa sql
			rs = stmt.executeQuery();

			if (!rs.next()) {
				// System.out.println("Divida não encontrada!");
				return null;
			} else {
				// System.out.println("Dívida jï¿½ cadastrada!");
				div = new Divida();
				div.setId(rs.getInt("SRESDIV_ID"));
				// div.setOc(readOC(rs.getString("SRESDIV_OM_OC_ID")));
				div.setOc(idOc);
				div.setMesProcessoGeracao(rs.getString("SRESDIV_REF_INICIO_MES"));
				div.setAnoProcessoGeracao(rs.getString("SRESDIV_REF_INICIO_ANO"));
				div.setCodigo(rs.getInt("SRESDIV_CODIGO"));
				div.setAgencia(rs.getString("SRESDIV_AGE"));
				div.setContaCorrente(rs.getString("SRESDIV_CCR"));
				div.setBanco(buscaBancoById(rs.getString("SRESDIV_BCO_ID"), conn));
				div.setValor(rs.getDouble("SRESDIV_VAL"));
				div.setEstado(rs.getString("SRESDIV_ESTADO"));
				div.setDataMotivo(rs.getDate("SRESDIV_DT_MOTIVO"));
				div.setMotivo(buscaMotivoById(rs.getString("SRESDIV_MOT_ID"), conn));
				div.setTipo(rs.getString("SRESDIV_TPDIV_ID"));
				div.setDataMotivo(rs.getDate("SRESDIV_DT_MOTIVO"));
				if (sistemaOrigem.equals("S"))
					div.setPessoa(readPessoa(sistemaOrigem, rs.getString("SRESPES_MATFIN")));
				else if (sistemaOrigem.equals("N"))
					div.setPessoa(readPessoa(sistemaOrigem, rs.getString("SRESPES_MATSIAP")));

				return div;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DButils.closeQuietly(rs, stmt, conn);
		}

	}

	/*
	 * public String buscaIdByOC(String codoc){
	 * 
	 * PreparedStatement stmt = null; ResultSet results = null; String statment,
	 * id_oc;
	 * 
	 * try { //Cria sql statment =
	 * "SELECT SRESOM_OM_ID FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_OC_COD=? AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD"
	 * ; stmt = this.conn.prepareStatement(statment); stmt.setString(1,codoc);
	 * //Executa sql results = stmt.executeQuery(); if (results.next()){ id_oc =
	 * results.getString("SRESOM_OM_ID"); return id_oc; } else {
	 * //System.out.println("Oc não encontrada!"); return null; } } catch
	 * (SQLException e){ //System.out.println(e); return null; } }
	 */

	public String buscaCodByOC(String idoc, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment, cod_oc;

		try {
			// Cria sql
			statment = "SELECT SRESOM_OM_OC_COD FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_ID=? AND SRESOM_OM_OC_COD = SRESOM_OM_OM_COD";
			stmt = conexao.prepareStatement(statment);
			stmt.setString(1, idoc);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				cod_oc = results.getString("SRESOM_OM_OC_COD");
				return cod_oc;
			} else {
				// System.out.println("Oc não encontrada!");
				return null;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return null;
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

	public int buscaIdByNomeOc(String nomeOc, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment;
		int id_oc;
		try {
			// Cria sql
			statment = "SELECT SRESOM_OM_ID FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_NOME=? ";
			stmt = conexao.prepareStatement(statment);
			stmt.setString(1, nomeOc);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				id_oc = results.getInt("SRESOM_OM_ID");
				return id_oc;
			} else {
				// System.out.println("Oc não encontrada!");
				return 0;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return 0;
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

	public String buscaBancoById(String idbco, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment = "";
		String codbco = "";

		try {
			// Cria sql
			statment = "SELECT SRESBCO_COD FROM SRES_BANCO WHERE SRESBCO_ID=?";
			stmt = conexao.prepareStatement(statment);
			stmt.setString(1, idbco);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				// Banco banco = new Banco(results.getString("SRESBCO_ID"));
				codbco = results.getString("SRESBCO_COD");
				return codbco;
			} else {
				// System.out.println("Banco não encontrado!");
				return null;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return null;
		} /*
			 * finally { if (stmt != null) { try { stmt.close(); } catch (Exception e) {
			 * e.printStackTrace(); } } if (results != null) { try { results.close(); }
			 * catch (Exception e) { e.printStackTrace(); } } }
			 */
	}

	public String buscaIdBancoByCodigo(String codBco, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment = "";
		String idBco = "";

		try {
			// Cria sql
			statment = "SELECT SRESBCO_ID FROM SRES_BANCO WHERE SRESBCO_COD=?";
			stmt = conexao.prepareStatement(statment);
			stmt.setString(1, codBco);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				// Banco banco = new Banco(results.getString("SRESBCO_ID"));
				idBco = results.getString("SRESBCO_ID");
				return idBco;
			} else {
				// System.out.println("Banco não encontrado!");
				return null;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return null;
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

	public String buscaMotivoById(String idmot, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment = "";
		String nommot = "";

		try {
			// Cria sql
			statment = "SELECT SRESMOT_NOME FROM SRES_MOTIVO WHERE SRESMOT_ID=?";
			stmt = conexao.prepareStatement(statment);
			stmt.setString(1, idmot);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				nommot = results.getString("SRESMOT_NOME");
				return nommot;
			} else {
				// System.out.println("Motivo não encontrado!");
				return null;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return null;
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

	public int buscaIdMotivoByNome(String nomeMot, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment = "";
		int idMot = 0;

		try {
			// Cria sql
			statment = "SELECT SRESMOT_ID FROM SRES_MOTIVO WHERE SRESMOT_NOME = ? ";
			stmt = conexao.prepareStatement(statment);

			nomeMot = nomeMot.toUpperCase().trim();

			stmt.setString(1, nomeMot);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				idMot = results.getInt("SRESMOT_ID");
				return idMot;
			} else {
				// System.out.println("Motivo não encontrado!");
				return 0;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			return 0;
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

	// metodo processTransaction()
	//TODO: P32 remover esse metodo trantando as transacoes!
	public void processTransaction(boolean transaction) throws SispagException {
		Connection conn = null;
		try {
			conn =ds.getConnection();
			conn.setAutoCommit(false);
			if (transaction) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException se) {
				// TODO: handle exception
				//
			}
			e.printStackTrace();
			throw new SispagException("Transaï¿½ï¿½o não efetivada!");
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO: handle exception

			}
		
			DButils.closeQuietly(conn);
		}
	}

	public Divida getDivida(int dividaId) {

		return divida;
	}

	public void dividaList() {

	}

	public void insereDivida() {

	}

	// @SuppressWarnings("finally")
	private int contRegistro(String[] parametros, String sql) {
		int qtd = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement(sql);

			// System.out.println("Entrei no count registro");

			// System.out.println(sql);

			for (int i = 0; i < parametros.length; i++) {
				// System.out.println(parametros[i]);
			}

			for (int i = 0; i < parametros.length; i++) {
				ps.setString(i + 1, parametros[i]);
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				qtd = rs.getInt("QTD");
			} else {
				qtd = 0;
			}
		} catch (SQLException se) {
            logger.error(se.getMessage(),se);			
		} finally {
			DButils.closeQuietly(rs, ps, conn);
			
		}
		return qtd;
	}

	public String[][] obterListaDividas(String mesA, String anoA, String listaOC, String listaPessoa, String mesB,
			String anoB, String listaOC2, String listaPessoa2, int qtdDivida, String query)
			throws SQLException, SispagException {
		String listaRetorno[][] = new String[qtdDivida][8];
		PreparedStatement preparedStatementDivida = null;
		ResultSet resultSetDivida = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			preparedStatementDivida = conn.prepareStatement(query);
			preparedStatementDivida.setString(1, mesA);
			preparedStatementDivida.setString(2, anoA);
			preparedStatementDivida.setString(3, listaOC);
			preparedStatementDivida.setString(4, listaPessoa);
			preparedStatementDivida.setString(5, mesB);
			preparedStatementDivida.setString(6, anoB);
			preparedStatementDivida.setString(7, listaOC2);
			preparedStatementDivida.setString(8, listaPessoa2);

			resultSetDivida = preparedStatementDivida.executeQuery();

			int contador = 0;
			while (resultSetDivida.next()) {
				Double calculaValor = new Double(
						resultSetDivida.getDouble("INICIAL") - resultSetDivida.getDouble("REVERSAO")
								+ resultSetDivida.getDouble("DEVOLUCAO") - resultSetDivida.getDouble("PERDOADO"));

				listaRetorno[contador][0] = resultSetDivida.getString("SRESLANC_NUM_DOC_AUT");
				listaRetorno[contador][1] = resultSetDivida.getString("SRESLANC_DT_DOC_AUT");
				listaRetorno[contador][2] = resultSetDivida.getString("SRESMOT_NOME");
				listaRetorno[contador][3] = resultSetDivida.getString("INICIAL");
				listaRetorno[contador][4] = resultSetDivida.getString("REVERSAO");
				listaRetorno[contador][5] = resultSetDivida.getString("DEVOLUCAO");
				listaRetorno[contador][6] = resultSetDivida.getString("PERDOADO");
				listaRetorno[contador][7] = Double.toString(calculaValor);
				contador++;
			}
		} finally {
			DButils.closeQuietly(resultSetDivida, preparedStatementDivida, conn);
		}
		return listaRetorno;
	}

	/**
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @param qtdPessoa -- valor utilizado para instanciar a lista de retorno.
	 * @param listaOC
	 * @param query
	 * @return
	 * @throws SispagException
	 * @throws SQLException
	 */
	private String[][] obterListaPessoa(String mes, String ano, int qtdPessoa, String listaOC, String query)
			throws SispagException, SQLException {
		String listaRetorno[][] = null;
		ResultSet resultSetPessoa = null;
		PreparedStatement preparedStatementPessoa = null;
		try {
			obterConexao();
			listaRetorno = new String[qtdPessoa][4];
			preparedStatementPessoa = connexao.prepareStatement(query);
			preparedStatementPessoa.setString(1, mes);
			preparedStatementPessoa.setString(2, ano);
			preparedStatementPessoa.setString(3, listaOC);

			resultSetPessoa = preparedStatementPessoa.executeQuery();

			int contador = 0;
			while (resultSetPessoa.next()) {
				listaRetorno[contador][0] = resultSetPessoa.getString("SRESPES_ID");
				listaRetorno[contador][1] = resultSetPessoa.getString("matricula");
				listaRetorno[contador][2] = resultSetPessoa.getString("SRESPES_NOME");
				listaRetorno[contador][3] = resultSetPessoa.getString("SRESPES_CPF");

				contador++;
			}
			return listaRetorno;
		} finally {
			if (resultSetPessoa != null) {
				resultSetPessoa.close();
			}
			if (preparedStatementPessoa != null) {
				preparedStatementPessoa.close();
			}
			if (connexao != null) {
				// conn.close();
				fecharConexao("obterListaPessoa");
			}
		}

	}

	/**
	 * Método responsï¿½vel por obter a lista de OC que ppssuem dividas para o
	 * periodo informado
	 * 
	 * @param mes
	 * @param ano
	 * @param qtdOC
	 * @param query
	 * @return listaRetorno
	 * @throws SispagException
	 * @throws SQLException
	 */
	private String[][] obterListaOc(String mes, String ano, int qtdOC, String query)
			throws SispagException, SQLException {
		String[][] listaRetorno = new String[qtdOC][2];
		PreparedStatement preparedStatementOC = null;
		ResultSet resultSetOC = null;
		try {
			obterConexao();
			preparedStatementOC = connexao.prepareStatement(query);
			preparedStatementOC.setString(1, mes);
			preparedStatementOC.setString(2, ano);

			resultSetOC = preparedStatementOC.executeQuery();

			int contador = 0;
			while (resultSetOC.next()) {
				listaRetorno[contador][0] = resultSetOC.getString("SRESOM_OM_ID");
				listaRetorno[contador][1] = resultSetOC.getString("SRESOM_NOME");
				contador++;
			}
		} finally {
			if (resultSetOC != null) {
				resultSetOC.close();
			}
			if (preparedStatementOC != null) {
				preparedStatementOC.close();
			}
			if (connexao != null) {
				fecharConexao("obterListaOc");
			}
		}
		return listaRetorno;
	}

	/**
	 * Metodo responsavel por montar a query que irï¿½ calcular a quantidade de OC
	 * que possuem dívidas para o periodo informado.
	 * 
	 * @param listOC
	 * @return
	 */
	private String montarQueryQtdeOC(List<String> listOC) {
		String sqlOC = null;

		// Constroi SQL para obter as OC com dividas no universo informado pelo usuario
		if (!listOC.get(0).equals("Todas as OC")) {
			Iterator<String> itens = listOC.iterator();
			sqlOC = " AND SRESOM_OM_OC_COD in (";
			while (itens.hasNext()) {
				sqlOC = sqlOC + "'" + itens.next() + "'";
				if (itens.hasNext()) {
					sqlOC = sqlOC + ",";
				}
			}
			sqlOC = sqlOC + ") ";
		}
		sqlOC = sqlOC + " ORDER BY  SRESOM_NOME ";
		return sqlOC;
	}

	private int contaRegistroDividasOC(String[] parametros, String sql) throws SispagException {
		int qtd = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement(sql);

			// System.out.println("Entrei no count registro");

			// System.out.println(sql);

			for (int i = 0; i < parametros.length; i++) {
				// System.out.println(parametros[i]);
			}

			for (int i = 0; i < parametros.length; i++) {
				ps.setString(i + 1, parametros[i]);
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				qtd = rs.getInt("QTD");
			} else {
				qtd = 0;
			}
			rs.close();
			ps.close();
		} catch (SQLException se) {
			// System.out.println(se.getMessage());
			se.printStackTrace();
		} finally {
			if (connexao != null) {
				try {
					fecharConexao("contRegistro()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return qtd;
		}

	}

	public String[][][][] getDividasOC(String mes, String ano, List<String> listOC) throws SispagException {

		String[][][][] listRetornoDivida = null;

		try {
			// Obtï¿½m a quantidade de OC que possuem dividas no mes/ano informado
			String sqlOC = montarQueryQtdeOC(listOC);

			// Cï¿½lculo do tamanho da primeira dimensï¿½o o Array
			String[] parametroOC = new String[2];
			parametroOC[0] = mes;
			parametroOC[1] = ano;
			String sqlOCOM = SQL_QTDOCDIVIDA + sqlOC;

			int qtdOC = contaRegistroDividasOC(parametroOC, sqlOCOM);

			System.out.println(sqlOCOM);

			if (qtdOC > 0) {
				// System.out.println("\nQuantidade de OCs com dividas: " + qtdOC);
				String[][] listaOC = obterListaOc(mes, ano, qtdOC, SQL_OCDIVIDA + sqlOC);

				listRetornoDivida = new String[qtdOC][][][];

				// Para cada OC, obter a quantidade de pessoas que possuem dividas no periodo
				// informado
				for (int i = 0; i < qtdOC; i++) {
					String[] parametroPessoa = new String[3];
					parametroPessoa[0] = mes;
					parametroPessoa[1] = ano;
					parametroPessoa[2] = listaOC[i][0];
					int qtdPessoa = contaRegistroDividasOC(parametroPessoa, SQL_QTDPESSOADIVIDA);

					if (qtdPessoa > 0) {
						// System.out.println("\nQuantidade de pessoas para a ID_OC "+listaOC[i][0]+"
						// :"+qtdPessoa);
						String[][] listaPessoa = obterListaPessoa(mes, ano, qtdPessoa, listaOC[i][0],
								SQL_PESSOAOCDIVIDA);

						listRetornoDivida[i] = new String[qtdPessoa][][];

						// Para cada Pessoa, recuperar suas dívidas
						for (int j = 0; j < qtdPessoa; j++) {
							String[] parametroDivida = new String[4];
							parametroDivida[0] = mes;
							parametroDivida[1] = ano;
							parametroDivida[2] = listaOC[i][0];
							parametroDivida[3] = listaPessoa[j][0];

							int qtdDivida = contaRegistroDividasOC(parametroDivida, SQL_QTDDIVIDATOTAIS);

							if (qtdDivida > 0) {
								// System.out.println("\nQuantidade de dividas da ID_PESSOA
								// "+listaPessoa[j][0]+" :"+qtdDivida);
								String[][] listaDivida = obterListaDividas(mes, ano, listaOC[i][0], listaPessoa[j][0],
										mes, ano, listaOC[i][0], listaPessoa[j][0], qtdDivida, SQL_DIVIDATOTAIS);
								listRetornoDivida[i][j] = new String[qtdDivida][];

								for (int k = 0; k < qtdDivida; k++) {
									listRetornoDivida[i][j][k] = new String[12];

									listRetornoDivida[i][j][k][0] = listaOC[i][1]; // Nome da OM
									listRetornoDivida[i][j][k][1] = listaPessoa[j][1]; // matricula do militar
									listRetornoDivida[i][j][k][2] = listaPessoa[j][2]; // nome do militar
									listRetornoDivida[i][j][k][3] = listaPessoa[j][3]; // cpf do militar
									listRetornoDivida[i][j][k][4] = listaDivida[k][0]; // numero do documento de
																						// autorizacao
									listRetornoDivida[i][j][k][5] = listaDivida[k][1]; // data do documento de
																						// autorizacao
									listRetornoDivida[i][j][k][6] = listaDivida[k][2]; // nome do motivo
									listRetornoDivida[i][j][k][7] = listaDivida[k][3]; // valor inicial
									listRetornoDivida[i][j][k][8] = listaDivida[k][4]; // valor de reversao
									listRetornoDivida[i][j][k][9] = listaDivida[k][5]; // valor devolucao
									listRetornoDivida[i][j][k][10] = listaDivida[k][6]; // valor perdoado
									listRetornoDivida[i][j][k][11] = listaDivida[k][7]; // valor a regularizar
								}
							} else {
								return null;
							}
						}
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		} catch (SQLException e) {
			// System.out.println("Erro no BB ao obter relatorio de dividas por OC:
			// \n"+e.getMessage());
			throw new SispagException("Erro ao obter as Dividas por OC");
		}

		return listRetornoDivida;
	}

	// INICIO String GERADOR RELATORIO POR OC DATA INCLUSAO
	public String[][][][] getDividasOCDTInclusao(String mes, String ano, List<String> listOC) throws SispagException {

		String[][][][] listRetornoDivida = null;

		try {
			// Obtï¿½m a quantidade de OC que possuem dividas no mes/ano informado
			String sqlOC = montarQueryQtdeOC(listOC);

			// Cï¿½lculo do tamanho da primeira dimensï¿½o o Array
			String[] parametroOC = new String[2];
			parametroOC[0] = mes;
			parametroOC[1] = ano;
			String sqlOCOM = SQL_QTDOCDIVIDADTINCLUSAO + sqlOC; // mudar sql SQL_OCDIVIDADTINCLUSAO

			int qtdOC = contaRegistroDividasOC(parametroOC, sqlOCOM);

			System.out.println(sqlOCOM);

			if (qtdOC > 0) {
				// System.out.println("\nQuantidade de OCs com dividas: " + qtdOC);
				String[][] listaOC = obterListaOc(mes, ano, qtdOC, SQL_OCDIVIDADTINCLUSAO + sqlOC); // mudar sql

				listRetornoDivida = new String[qtdOC][][][];

				// Para cada OC, obter a quantidade de pessoas que possuem dividas no periodo
				// informado
				for (int i = 0; i < qtdOC; i++) {
					String[] parametroPessoa = new String[3];
					parametroPessoa[0] = mes;
					parametroPessoa[1] = ano;
					parametroPessoa[2] = listaOC[i][0];
					int qtdPessoa = contaRegistroDividasOC(parametroPessoa, SQL_QTDPESSOADIVIDADTINCLUSAO);// mudar sql

					if (qtdPessoa > 0) {
						// System.out.println("\nQuantidade de pessoas para a ID_OC "+listaOC[i][0]+"
						// :"+qtdPessoa);
						String[][] listaPessoa = obterListaPessoa(mes, ano, qtdPessoa, listaOC[i][0],
								SQL_PESSOAOCDIVIDADTINCLUSAO);

						listRetornoDivida[i] = new String[qtdPessoa][][];

						// Para cada Pessoa, recuperar suas dívidas
						for (int j = 0; j < qtdPessoa; j++) {
							String[] parametroDivida = new String[4];
							parametroDivida[0] = mes;
							parametroDivida[1] = ano;
							parametroDivida[2] = listaOC[i][0];
							parametroDivida[3] = listaPessoa[j][0];

							int qtdDivida = contaRegistroDividasOC(parametroDivida, SQL_QTDDIVIDATOTAISDTINCLUSAO);

							if (qtdDivida > 0) {
								// System.out.println("\nQuantidade de dividas da ID_PESSOA
								// "+listaPessoa[j][0]+" :"+qtdDivida);
								String[][] listaDivida = obterListaDividas(mes, ano, listaOC[i][0], listaPessoa[j][0],
										mes, ano, listaOC[i][0], listaPessoa[j][0], qtdDivida,
										SQL_DIVIDATOTAISDTINCLUSAO);
								listRetornoDivida[i][j] = new String[qtdDivida][];

								for (int k = 0; k < qtdDivida; k++) {
									listRetornoDivida[i][j][k] = new String[12];

									listRetornoDivida[i][j][k][0] = listaOC[i][1]; // Nome da OM
									listRetornoDivida[i][j][k][1] = listaPessoa[j][1]; // matricula do militar
									listRetornoDivida[i][j][k][2] = listaPessoa[j][2]; // nome do militar
									listRetornoDivida[i][j][k][3] = listaPessoa[j][3]; // cpf do militar
									listRetornoDivida[i][j][k][4] = listaDivida[k][0]; // numero do documento de
																						// autorizacao
									listRetornoDivida[i][j][k][5] = listaDivida[k][1]; // data do documento de
																						// autorizacao
									listRetornoDivida[i][j][k][6] = listaDivida[k][2]; // nome do motivo
									listRetornoDivida[i][j][k][7] = listaDivida[k][3]; // valor inicial
									listRetornoDivida[i][j][k][8] = listaDivida[k][4]; // valor de reversao
									listRetornoDivida[i][j][k][9] = listaDivida[k][5]; // valor devolucao
									listRetornoDivida[i][j][k][10] = listaDivida[k][6]; // valor perdoado
									listRetornoDivida[i][j][k][11] = listaDivida[k][7]; // valor a regularizar
								}
							} else {
								return null;
							}
						}
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		} catch (SQLException e) {
			// System.out.println("Erro no BB ao obter relatorio de dividas por OC:
			// \n"+e.getMessage());
			throw new SispagException("Erro ao obter as Dividas por OC");
		}

		return listRetornoDivida;
	}
	// FIM String GERADOR RELATORIO POR OC DATA INCLUSAO

	private boolean deleteLancamento(String id) throws SispagException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_DELETELANCAMENTO);
			ps.setString(1, id);
			resultado = ps.execute();

		} catch (SQLException se) {
			// System.out.println(se.getMessage());
			se.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("deleteLancamento()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}

			}
			return resultado;
		}
	}

	public boolean deleteDivida(Divida divida) throws SispagException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_DELETEDIVIDA);
			ps.setInt(1, divida.getId());
			int res = ps.executeUpdate();
			resultado = true;

		} catch (SQLException se) {
			// System.out.println(se.getMessage());

			se.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("deleteDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return resultado;
		}
	}

	public boolean updateDivida(Divida divida, String usuario) throws SispagException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean transaction = false;
		boolean resultado = false;
		int motivo, codDivida;

		String matricula = null;

		if (divida.getCgs().equals("S")) {
			matricula = divida.getPessoa().getMatFin().trim();
		} else if (divida.getCgs().equals("N")) {
			matricula = divida.getPessoa().getMatSIAPE().trim();
		}

		try {
			obterConexao();
			// Método readPessoa para pegar o id da Pessoa
			Pessoa pessoa = readPessoa(divida.getCgs(), matricula);
			motivo = buscaIdMotivoByNome(divida.getMotivo(), connexao);
			codDivida = geraCodigoDiv(divida.getPessoa().getId(), connexao);

			obterConexao();
			// conn.setAutoCommit(false);
			ps = connexao.prepareStatement(SQL_UPDATEDIVIDA);
			ps.setString(1, divida.getDocEnvio());
			ps.setString(2, divida.getNumeroDocEnvio());
			ps.setDate(3, divida.getDataDocEnvio());
			ps.setString(4, usuario);
			ps.setInt(5, motivo);
			ps.setDate(6, divida.getDataMotivo());
			ps.setInt(7, divida.getId());

			/*
			 * ps.setInt(1, divida.getOc().getId()); ps.setString(2, divida.getAgencia());
			 * ps.setString(3, divida.getContaCorrente()); ps.setString(4,
			 * divida.getBanco()); ps.setString(5, usuario);
			 * 
			 * /* Funï¿½ï¿½o que utilizei para formatar a data - não estou usando mais e
			 * deixei como exemplo para uso no futuro DateFormat formatador = new
			 * SimpleDateFormat("dd/mm/yyyy"); String data = formatador.format(new
			 * Date(System.currentTimeMillis()));
			 */
			/*
			 * divida.setPessoa(pessoa); ps.setInt(6, divida.getPessoa().getId());
			 * ps.setInt(7, motivo); ps.setDate(8, divida.getDataMotivo()); ps.setInt(9,
			 * codDivida); ps.setInt(10, divida.getId());
			 */

			ps.execute();
			resultado = true;
			// atualiza os dados do lanï¿½amento inicial (numeroDocAutorizacao,
			// OrigemDocAutorizacao, tipoDocAutorizacao e DataDocAutorizacao)
			// tem que ser uma transaï¿½ï¿½o atomica
			transaction = updateLancamento(divida.getLancamentoInicial(), usuario);
			// System.out.println("alterou o lanc incial");
			if (resultado && transaction) {
				processTransaction(transaction);
			}

		} catch (Exception se) {
			se.printStackTrace();
			throw new SispagException("Erro alterando dívida");
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new SispagException("Erro alterando dívida");
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SispagException("Erro alterando dívida");
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("updateDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

			return resultado;
		}

	}// end do updateDivida

	@SuppressWarnings("finally")
	public boolean updateLancamento(Lancamento lancamento, String usuario) throws SispagException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_UPDATELANCAMENTO);
			// System.out.println(SQL_UPDATELANCAMENTO);
			ps.setString(1, lancamento.getNumeroDocAutorizacao());
			// System.out.println(lancamento.getNumeroDocAutorizacao());

			ps.setString(2, lancamento.getTipoDocAutorizacao());
			// System.out.println(lancamento.getTipoDocAutorizacao());

			ps.setDate(3, new java.sql.Date(lancamento.getDataDocAutorizacao().getTime()));
			// System.out.println(lancamento.getDataDocAutorizacao());

			ps.setString(4, lancamento.getOrigemDocAutorizacao());
			// System.out.println(lancamento.getOrigemDocAutorizacao());

			ps.setString(5, lancamento.getObservacao());

			ps.setString(6, usuario);

			ps.setInt(7, lancamento.getIdLancamento());
			// System.out.println(lancamento.getCodigo());

			int res = 0;
			res = ps.executeUpdate();
			resultado = true;

		} catch (Exception se) {
			throw new SispagException("Erro alterando lancamento");

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new SispagException("Erro alterando dívida");
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SispagException("Erro alterando lancamento");
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("updateLancamento()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

			return resultado;
		}

	}// end do updateLancamento

	private ArrayList<Object> montaParametroFiltroDivida(String[] vParametros) {
		ArrayList<Object> list_retorno = new ArrayList<Object>();
		String parametroFiltro = "";
		ArrayList<Object> lista_stmt = new ArrayList<Object>();

		// Configuraï¿½ï¿½o do DAO e Montagem da Lista de dívidas
		for (int i = 0; i < vParametros.length; i++) {

			switch (i) {
			case 0: {
				// Passa o parametro cï¿½digo da divida
				if ((vParametros[0].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESDIV_CODIGO = ?";
					lista_stmt.add(vParametros[0]);

				}

			}

				break;
			case 1: {
				// Passa o parametro tipo da divida
				if ((vParametros[1].length() > 0) && (!vParametros[1].toUpperCase().equals("TODAS"))) {
					parametroFiltro = parametroFiltro + " AND  UPPER(TRIM(SRESTPDIV_NOME))LIKE ?";
					lista_stmt.add("%" + vParametros[1].trim().toUpperCase() + "%");

				}
			}

				break;

			case 2: {
				// Passa o parametro oc de origem da dívida - CGS ou SIAP
				if ((vParametros[2].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESLANC_ORIGEM_DOC_AUT = ?";
					lista_stmt.add(vParametros[2]);

				}

			}

				break;

			case 3: {
				// Passa o parametro Matrícula financeira da dívida
				if ((vParametros[3].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND (SRESPES_MATFIN = ? OR SRESPES_MATSIAP = ? ) ";
					lista_stmt.add(vParametros[3]);
					lista_stmt.add(vParametros[3]);

				}

			}

				break;

			case 4: {
				// Passa o parametro Nome
				if ((vParametros[4].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND UPPER(TRIM(SRESPES_NOME)) LIKE ?";
					vParametros[4] = "%" + vParametros[4].toUpperCase() + "%";
					lista_stmt.add(vParametros[4].trim());

				}

			}

				break;

			case 5: {
				// Campos de informações sobre LANÇAMENTOS
				// Passa o parametro Número de Documento de autorizaï¿½ï¿½o da
				// dívida
				if ((vParametros[5].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESLANC_NUM_DOC_AUT = ? ";
					lista_stmt.add(vParametros[5]);

				}

			}

				break;

			case 6: {
				// Passa o parametro Tipo de Documento de autorizaï¿½ï¿½o da
				// dívida
				if ((vParametros[6].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESLANC_TIPO_DOC_AUT = ? ";
					lista_stmt.add(vParametros[6]);

				}

			}

				break;

			case 7: {
				// Passa o parametro Data de Documento de autorizaï¿½ï¿½o da
				// dívida
				if ((vParametros[7].length() > 0)) {

					DateFormat dfD = new SimpleDateFormat("yyyy-MM-dd");

					Date data = null;
					try {
						data = new Date(dfD.parse(vParametros[7]).getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					parametroFiltro = parametroFiltro + " AND SRESLANC_DT_DOC_AUT = ? ";
					lista_stmt.add(data);

				}

			}

				break;

			case 8: {
				// Passa o parametro Pedido de Reversão da dívida
				if ((vParametros[8].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESPED_COD = ? ";
					lista_stmt.add(vParametros[8]);

				}

			}

				break;
			case 9: {
				// System.out.println("parametro 9 sispag ou siape:"+vParametros[9]);
				// Passa o parametro que especifica se a dívida vem do CGS
				if ((vParametros[9].length() > 0)) {
					if (vParametros[9].equals("S")) {
						parametroFiltro = parametroFiltro + " AND SRESDIV_CGS = ? ";
						vParametros[9] = "S";
						// ParametroContagem=ParametroContagem + SQL_CONTAGEM;

					} else {
						parametroFiltro = parametroFiltro + " AND SRESDIV_CGS = ? ";
						vParametros[9] = "N";

					}

					lista_stmt.add(vParametros[9]);
				}

			}

				break;

			case 10: {
				if (!vParametros[10].equals("")) {
					parametroFiltro = parametroFiltro + " AND SRESDIV_ESTADO = ? ";
					lista_stmt.add(vParametros[10]);

				}
			}

				break;

			case 11: {
				if (!vParametros[11].equals("")) {
					parametroFiltro = parametroFiltro + " AND Valor " + vParametros[11] + " ? ";
					lista_stmt.add(vParametros[12]);

				}
			}

				break;
			case 13: {
				if (vParametros[13].equals("0")) {
					parametroFiltro = parametroFiltro + " AND  sresdiv_ped_id is NULL ";

				}
			}

				break;

			case 18: {
				if (!vParametros[18].equals("")) {
					parametroFiltro = parametroFiltro + " AND SRESOM_OM_OC_COD = ? ";
					lista_stmt.add(vParametros[18]);

				}
			}

				break;
                                
                               
                                
                                

			case 21: {
				// System.out.println("Parametro 21:"+vParametros[21]);
				if ((vParametros[21].length() > 0) && (!vParametros[21].toUpperCase().equals("TODAS"))) {
					parametroFiltro = parametroFiltro + " AND  UPPER(TRIM(SRESMOT_NOME))LIKE ?";
					lista_stmt.add("%" + vParametros[21].trim().toUpperCase() + "%");

				}
			}

				break;
                                
                        /*CPF*/        
                        case 22: {
				 System.out.println("Parametro 22:"+vParametros[22]);
				if (!vParametros[22].equals("")) {
					parametroFiltro = parametroFiltro + " AND  UPPER(TRIM(SRESPES_CPF))LIKE ?";
					lista_stmt.add("%" + vParametros[22].trim().toUpperCase() + "%");

				}
			}

				break;       

			}// fim do switch )

		} // end do for
		list_retorno.add(parametroFiltro);
		list_retorno.add(lista_stmt);
		return list_retorno;
	}

	// ***********************************************************************

	private ArrayList<Object> montaParametroFiltroReversao(String[] vParametros) {
		ArrayList<Object> list_retorno = new ArrayList<Object>();
		String parametroFiltro = "";
		ArrayList<Object> lista_stmt = new ArrayList<Object>();

		// Configuraï¿½ï¿½o do DAO e Montagem da Lista de pedidos
		for (int i = 0; i < vParametros.length; i++) {

			switch (i) {

			case 0: {
				// Passa o parametro cï¿½digo da divida
				if ((vParametros[0].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESPED_COD = ? ";
					lista_stmt.add(vParametros[0]);

				}

			}

				break;

			case 1: {
				// Passa o parametro cï¿½digo da divida
				if ((vParametros[1].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESPED_ANO = ? ";
					lista_stmt.add(vParametros[1]);

				}

			}

				break;

			case 2: {
				// Passa o parametro Data de Documento de autorizaï¿½ï¿½o da dívida

				if ((vParametros[2].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND to_char(SRESPED_RESP_DT,'dd/mm/yyyy') = ? ";
					lista_stmt.add(vParametros[2]);
				}

			}
				break;

			case 3: {
				// Passa o parametro cï¿½digo da divida
				if ((vParametros[3].length() > 0)) {
					parametroFiltro = parametroFiltro + " AND SRESPED_BCO_ID = ?";
					lista_stmt.add(vParametros[3]);

				}

			}

				break;

			case 4: {
				// Passa o parametro tipo da divida
				// System.out.println("entrou no tipo");
				if ((vParametros[4].length() > 0) && (!vParametros[1].toUpperCase().equals("TODAS"))) {
					if (vParametros[4].equals("MÊS")) {
						parametroFiltro = parametroFiltro + " AND SRESPED_TIPO = 1 ";
					} else if (vParametros[4].equals("HISTÓRICA")) {
						parametroFiltro = parametroFiltro + " AND SRESPED_TIPO = 2 ";

					}

				}
			}

			}// fim do switch )

		} // end do for
		list_retorno.add(parametroFiltro);
		list_retorno.add(lista_stmt);
		return list_retorno;
	}

	// ***********************************************************************

	public int getQtdTotalDividas(String[] parametroContagem) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		Integer count = 0;

		try {
			obterConexao();
			
                        System.out.println("Obteve Conexão !");
                        
                        ArrayList<Object> objetosMontagem = montaParametroFiltroDivida(parametroContagem);
			String parametroConsulta = (String) objetosMontagem.get(0);
			ArrayList<Object> lista_stmt = (ArrayList<Object>) objetosMontagem.get(1);
			 System.out.println("entrou no total da paginação");
			if (parametroConsulta.equals("")) {
				stmt = connexao.prepareStatement(SQL_CONTAGEM);
			} else {
				String sql = SQL_CONTAGEM + parametroConsulta;
				stmt = connexao.prepareStatement(sql);
			}
			System.out.print("Sql paginacao:");
			System.out.println(SQL_CONTAGEM + parametroConsulta);

			// passagem de parametro

			Object parametro = null;
                        System.out.println("Passou obj parametro");
			// System.out.println("Parametros: ");

			// mudar o list_stmt para segundo item do arraylist
			for (int j = 0; j < lista_stmt.size(); j++) {
				parametro = lista_stmt.get(j);
				 //System.out.println(parametro);
				if (parametro instanceof Date) {
					stmt.setDate(j + 1, (Date) parametro);
                                       System.out.println("Passou IF parametro");
				} else {

					stmt.setString(j + 1, ((String) parametro).trim());
				}
			}

			 
			results = stmt.executeQuery();
                        System.out.println("Passou pelo result !");

			if (results.next()) {
				count = results.getInt("QUANTIDADE");
                                System.out.println("Entrou no if resulte !");
			}
			return count;

		} catch (Exception se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("getQtdTotalDividas()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		}

	} 

	// ***********************************************************************

	public int getQtdTotalReversao(String[] parametroContagem) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		Integer count = 0;

		try {
			obterConexao();
			ArrayList<Object> objetosMontagem = montaParametroFiltroReversao(parametroContagem);
			String parametroConsulta = (String) objetosMontagem.get(0);
			ArrayList<Object> lista_stmt = (ArrayList<Object>) objetosMontagem.get(1);

			if (parametroConsulta.equals("")) {
				stmt = connexao.prepareStatement(SQL_CONTAGEM_PEDIDO);
			} else {
				stmt = connexao.prepareStatement(SQL_CONTAGEM_PEDIDO + parametroConsulta);
			}
			System.out.print("Sql paginacao:");
			// System.out.println(SQL_CONTAGEM_PEDIDO + parametroConsulta);

			// passagem de parametro

			Object parametro = null;
			// System.out.println("Parametros: ");

			// mudar o list_stmt para segundo item do arraylist
			for (int j = 0; j < lista_stmt.size(); j++) {
				parametro = lista_stmt.get(j);
				// System.out.println(parametro);
				if (parametro instanceof Date) {
					stmt.setDate(j + 1, (Date) parametro);

				} else {

					stmt.setString(j + 1, (String) parametro);
				}
			}

			results = stmt.executeQuery();

			if (results.next()) {
				count = results.getInt("QUANTIDADE");
			}
			// System.out.println("saiu do count" + count);
			return count;

		} catch (Exception se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("getQtdTotalReversao()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		}

	}

	// *****************************************************************************************************************************
        
        public void addDividaEmAndamento(Divida divida) throws SispagException{
            
            PreparedStatement stmt = null;
	    ResultSet results = null;
            
//            String sql = "SELECT DA.SRESDIVAND_ID, DA.SRESDIVAND_DESCRICAO, DA.SRESDIVAND_ORIGEM_DOC_INC, DA.SRESDIVAND_TIPO_DOC_AUT, "
//                       + "DA.SRESDIVAND_DOC_DT_AUT, DA.SRESDIVAND_OBS, DA.SRESDIV_ID, DA.SRESDIVAND_DT_INS_BCODADOS FROM SRES_DIVIDA D "
//                       + "INNER JOIN SRES_DIVIDA_ANDAMENTO DA ON DA.SRESDIV_ID = D.SRESDIV_ID "
//                       + "where DA.SRESDIV_ID = ?";

            String sql = "SELECT DA.SRESDIVAND_ID, DA.SRESDIVAND_DESCRICAO, DA.SRESDIVAND_ORIGEM_DOC_INC, DA.SRESDIVAND_TIPO_DOC_AUT, "
                       + "DA.SRESDIVAND_DOC_DT_AUT, DA.SRESDIVAND_OBS, DA.SRESDIV_ID, DA.SRESDIVAND_DT_INS_BCODADOS, DA.SRESDIVAND_NUM_DOC_AUT "
                       + "from SRES_DIVIDA_ANDAMENTO DA "
                       + "INNER JOIN SRES_DIVIDA D ON DA.SRESDIV_ID = D.SRESDIV_ID "
                       + "INNER JOIN ( "
                       + "SELECT SRESDIV_ID, MAX(SRESDIVAND_DT_INS_BCODADOS) DATA_MAX "
                       + "FROM SRES_DIVIDA_ANDAMENTO "
                       + "GROUP BY SRESDIV_ID "
                       + ") RECENTE ON DA.SRESDIV_ID = RECENTE.SRESDIV_ID "
                       + "AND DA.SRESDIVAND_DT_INS_BCODADOS = recente.data_max "
                       + "AND  DA.SRESDIV_ID = ?";
            try{
                obterConexao();
                
                 stmt = connexao.prepareStatement(sql);
                 stmt.setInt(1, divida.getId());
                 results = stmt.executeQuery();
                 
                 
                while (results.next()) {

				DividaEmAndamento dividaAndamento = new DividaEmAndamento();
				//dividaAndamento.setId(results.getInt("SRESDIV_ID"));
                                dividaAndamento.setStatusDivida(results.getString("SRESDIVAND_ID"));
                                dividaAndamento.setDescricao(results.getString("SRESDIVAND_DESCRICAO"));
                                dividaAndamento.setOrigemDocInclusaoStatus(results.getString("SRESDIVAND_ORIGEM_DOC_INC"));
                                dividaAndamento.setTipoDocAutorizacaoInclusaoStatus(results.getString("SRESDIVAND_TIPO_DOC_AUT"));
                                dividaAndamento.setDataDocInclusaoStatus(results.getString("SRESDIVAND_DOC_DT_AUT"));
                                dividaAndamento.setObservacaoStatusDivida(results.getString("SRESDIVAND_OBS"));
                                dividaAndamento.setIdDivida(results.getInt("SRESDIV_ID"));
                                dividaAndamento.setDataInsercaoBancoDados(results.getString("SRESDIVAND_DT_INS_BCODADOS"));
                                dividaAndamento.setNumDoc(results.getString("SRESDIVAND_NUM_DOC_AUT"));
                                
                                divida.setDividaEmAndamento(dividaAndamento);
                                
                }
                 
                 
            } catch (SQLException se) {
			throw new RuntimeException("Ocorreu um erro no Banco de Dados." + se.getMessage());

		} catch (Exception se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());

		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("addDividaEmAndamento()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		} // fim do bloco try-catch-finally                  
        }
        
	public ArrayList<Divida> listDividas(String[] vParametros)

			throws SQLException, SispagException {// , NamingException {

		String ParametroContagem = "";
		PreparedStatement stmt = null;
		ResultSet results = null;
		int registros = 0;

		try {
			obterConexao();
			// Montagem da Query SQL de acordo com o num de parametros que vem da view
			/*
			 * String Query_Stmt = "SELECT   srespes_nome, srespes_cpf, sresom_om_oc_cod," +
			 * "srespes_matsiap, decode(SRESPES_MATSIAP ,Null, SRESPES_MATFIN , SRESPES_MATSIAP) srespes_matfin, srespes_pt, srespes_resp_alt, srespes_resp_dt,"
			 * + "srestpdiv_nome, sresmot_nome, sresdiv_id, sresdiv_age," +
			 * "sresdiv_estado, sresdiv_bco_id, sresdiv_ccr, sresdiv_ref_inicio_mes" +
			 * ", valor sresdiv_val, sresdiv_codigo, sresdiv_ref_inicio_ano" +
			 * ",sresdiv_om_oc_id, sresdiv_dt_motivo, sresom_nome, sresom_om_id" +
			 * ",sresdiv_doc_envio, sresdiv_num_doc_envio" +
			 * ",sresdiv_dt_doc_envio, sreslanc_id, sreslanc_codigo" +
			 * ",sreslanc_num_doc_aut, sreslanc_tipo_doc_aut, srespes_situacao" +
			 * ",sreslanc_dt_doc_aut, sreslanc_origem_doc_aut, sreslanc_operador" +
			 * ",sreslanc_valor, sresbco_cod, sreslanc_obs, sreslanc_resp_alt" +
			 * ",sreslanc_resp_dt, sreslanc_div_id, sreslanc_tplanc_id" +
			 * ",sresdiv_ref_termino_mes, sresdiv_ref_termino_ano, sresped_cod " +
			 * "FROM sres_divida, " + "sres_pessoa_rr, " + "sres_lancamento , " +
			 * "sres_tipo_divida, " + "sres_motivo, " + "sres_organizacao_militar, " +
			 * "sres_banco, sres_pedido, " +
			 * " (select sreslanc_div_id div_id, sum(decode(sreslanc_operador, 'D',sreslanc_valor, -sreslanc_valor)) Valor from sres_lancamento group by sreslanc_div_id) tb_valor "
			 * + "WHERE tb_valor.div_id = sreslanc_div_id  " +
			 * "AND sresdiv_pes_id = srespes_id " + "AND sreslanc_div_id = sresdiv_id " +
			 * "AND sresdiv_mot_id = sresmot_id " + "AND sresbco_id = sresdiv_bco_id " +
			 * "AND srestpdiv_id = sresdiv_tpdiv_id " +
			 * "AND sresdiv_om_oc_id = sresom_om_id " +
			 * "AND SRESPED_ID  (+)= SRESDIV_PED_ID " + "AND sreslanc_codigo = 1 ";
			 */

			String Query_Stmt = "SELECT  sresdiv_cgs, srespes_nome, srespes_cpf, sresom_om_oc_cod," +
			// "srespes_matsiap, " +
			// "decode(SRESPES_MATSIAP ,Null, SRESPES_MATFIN , SRESPES_MATSIAP)
			// srespes_matfin, " +
					"decode(SRESPES_MATSIAP ,Null, '0000000000' , SRESPES_MATSIAP) srespes_matsiap, "
					+ "decode(SRESPES_MATFIN ,Null, '0000000000' , SRESPES_MATFIN) srespes_matfin, "
					+ "srespes_pt, srespes_resp_alt, srespes_resp_dt,"
					+ "srestpdiv_nome, sresmot_nome, sresdiv_id, sresdiv_age,"
					+ "sresdiv_estado, sresdiv_bco_id, sresdiv_ccr, sresdiv_ref_inicio_mes"
					+ ", valor sresdiv_val, sresdiv_codigo, sresdiv_ref_inicio_ano"
					+ ",sresdiv_om_oc_id, sresdiv_dt_motivo, sresom_nome, sresom_om_id"
					+ ",sresdiv_doc_envio, sresdiv_num_doc_envio"
					+ ",sresdiv_dt_doc_envio, sreslanc_id, sreslanc_codigo"
					+ ",sreslanc_num_doc_aut, sreslanc_tipo_doc_aut, srespes_situacao"
					+ ",sreslanc_dt_doc_aut, sreslanc_origem_doc_aut, sreslanc_operador"
					+ ",sreslanc_valor, sresbco_cod, sreslanc_obs, sreslanc_resp_alt"
					+ ",sreslanc_resp_dt, sreslanc_div_id, sreslanc_tplanc_id"
					+ ",sresdiv_ref_termino_mes, sresdiv_ref_termino_ano, sresped_cod " + "FROM sres_divida, "
					+ "sres_pessoa_rr, " + "sres_lancamento , " + "sres_tipo_divida, " + "sres_motivo, "
					+ "sres_organizacao_militar, " + "sres_banco, sres_pedido, "
					+ " (select sreslanc_div_id div_id, sum(decode(sreslanc_operador, 'D',sreslanc_valor, -sreslanc_valor)) Valor from sres_lancamento group by sreslanc_div_id) tb_valor "
					+ "WHERE tb_valor.div_id = sreslanc_div_id  " + "AND sresdiv_pes_id = srespes_id "
					+ "AND sreslanc_div_id = sresdiv_id " + "AND sresdiv_mot_id = sresmot_id "
					+ "AND sresbco_id = sresdiv_bco_id " + "AND srestpdiv_id = sresdiv_tpdiv_id "
					+ "AND sresdiv_om_oc_id = sresom_om_id " + "AND SRESPED_ID  (+)= SRESDIV_PED_ID "
					+ "AND sreslanc_codigo = 1 ";

			ArrayList<Object> lista_stmt = null;
			ArrayList<Object> lista_retorno = null;

			lista_retorno = montaParametroFiltroDivida(vParametros); //Provavelmnente, será aqui: faxina dos filtros

			lista_retorno.add(" order by srespes_nome");

			lista_stmt = (ArrayList<Object>) lista_retorno.get(1);
			// monta a query adicionando os argumentos de filtro
			Query_Stmt = Query_Stmt + (String) lista_retorno.get(0); /*
																		 * + ") x " + ") " + "WHERE rn BETWEEN " +
																		 * vParametros[19] + " AND " + vParametros[20];
																		 */
			Query_Stmt = Query_Stmt + (String) lista_retorno.get(lista_retorno.size() - 1);

			// System.out.println("nova query do list divida: " +Query_Stmt);
			// Cria SQL e declara
			stmt = connexao.prepareStatement(Query_Stmt);

			Object parametro = null;

			for (int j = 0; j < lista_stmt.size(); j++) {
				parametro = lista_stmt.get(j);
				if (parametro instanceof Date) {
					stmt.setDate(j + 1, (Date) parametro);

				} else {

					stmt.setString(j + 1, ((String) parametro).trim());
				}
			}

			// System.out.println(Query_Stmt);

			// Configuraï¿½ï¿½o do DAO e Montagem da Lista de dívidas
			results = stmt.executeQuery();

			ArrayList<Divida> dividas = new ArrayList<Divida>();

			// Tratar recordset com objetos recuperados
			while (results.next()) {

				Divida div = new Divida();
				div.setId(results.getInt("SRESDIV_ID"));

				div.setAgencia(results.getString("SRESDIV_AGE"));

				div.setBanco(results.getString("SRESBCO_COD"));

				div.setContaCorrente(results.getString("SRESDIV_CCR"));

				div.setAnoProcessoGeracao(results.getString("SRESDIV_REF_INICIO_ANO"));

				div.setMesProcessoGeracao(results.getString("SRESDIV_REF_INICIO_MES"));

				div.setTipo(results.getString("SRESTPDIV_NOME"));

				div.setCodigo(results.getInt("SRESDIV_CODIGO"));

				div.setValor(results.getDouble("SRESDIV_VAL"));

				div.setMesTermino(results.getString("SRESDIV_REF_TERMINO_MES"));
				div.setAnoTermino(results.getString("SRESDIV_REF_TERMINO_ANO"));

				div.setValor(results.getDouble("SRESDIV_VAL"));

// queiroz				
				div.setDocEnvio(results.getString("SRESDIV_DOC_ENVIO"));
				div.setNumeroDocEnvio(results.getString("SRESDIV_NUM_DOC_ENVIO"));
				div.setDataDocEnvio(results.getDate("SRESDIV_DT_DOC_ENVIO"));

				div.setMotivo(results.getString("SRESMOT_NOME"));
				div.setDataMotivo(results.getDate("SRESDIV_DT_MOTIVO"));

				// valor utilizado como filtro para habilitar a opção de excluir divida
				div.setEstado(results.getString("SRESDIV_ESTADO"));

				/**
				 * Ten Luis Fernando Inclusao do indicador SIAPE / SISPAG - linha abaixo
				 */
				div.setCgs(results.getString("sresdiv_cgs"));

				// instanciar objeto OC e preencher
				OC oc = new OC();
				oc.setId(results.getInt("SRESDIV_OM_OC_ID"));
				oc.setNome(results.getString("SRESOM_NOME"));
				oc.setOc(results.getString("SRESOM_OM_OC_COD"));
				div.setOc(oc);

				// instanciar objeto Pessoa e preencher
				Pessoa pes = new Pessoa();
				pes.setNome(results.getString("SRESPES_NOME"));
				pes.setCpf(results.getString("SRESPES_CPF"));
				pes.setPosto(results.getString("SRESPES_PT"));
				pes.setSituacao(results.getString("SRESPES_SITUACAO"));
				pes.setMatSIAPE(results.getString("SRESPES_MATSIAP"));
				pes.setMatFin(results.getString("SRESPES_MATFIN"));
				div.setPessoa(pes);

				// instanciar objeto Pedido e preencher
				Pedido ped = new Pedido();
				ped.setCodigo(results.getString("SRESPED_COD"));
				div.setPedido(ped);

				// instanciar objeto Lançamento e preencher lanï¿½amento inicial
				// (cï¿½gigo = 1)
				ArrayList<Lancamento> lancamento = new ArrayList<Lancamento>();

				Lancamento lanc = new Lancamento();

				lanc.setIdLancamento(results.getInt("SRESLANC_ID"));

				lanc.setCodigo(results.getInt("SRESLANC_CODIGO"));

				lanc.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lanc.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));
				lanc.setDataDocAutorizacao(results.getDate("SRESLANC_DT_DOC_AUT"));
				lanc.setOrigemDocAutorizacao(results.getString("SRESLANC_ORIGEM_DOC_AUT"));
				lanc.setOperador(results.getString("SRESLANC_OPERADOR"));
				lanc.setValor(results.getDouble("SRESLANC_VALOR"));

				lanc.setObservacao(results.getString("SRESLANC_OBS"));

				lanc.setTipo(results.getString("SRESLANC_TPLANC_ID"));
				// lancamento.add(registros,lanc);

				div.setLancamento(lanc);
                                
                                //TODO: chamar método que faz busca no banco para as Dívidas em andamento
                                addDividaEmAndamento(div);
                                

				dividas.add(registros, div);

				registros++;

			} // fim do while que monta a coleï¿½ï¿½o de dívidas

			if (dividas != null) {
				return dividas;
			}

		} catch (SQLException se) {
			throw new RuntimeException("Ocorreu um erro no Banco de Dados." + se.getMessage());

			// Tratamento de erro SQL

		} catch (Exception se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());

			/*
			 * // Tratamento de erro JNDI }catch (NamingException ne){ throw new
			 * RuntimeException ("Ocorreu um erro no Banco de Dados." + ne.getMessage());
			 */
			// Lipeza dos recursos do JDBC
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("listDividas()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		} // fim do bloco try-catch-finally
		return null;

	}// fim do método

	// *****************************************************************************************************************************
	public ArrayList<Divida> listReversao(String[] vParametros)

			throws SQLException, NamingException, SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		int registros = 0;

		try {
			obterConexao();

			/*
			 * // Montagem da Query SQL de acordo com o num de parametros que vem da view
			 * String Query_Stmt =
			 * "SELECT   SRESPED_COD, srespes_nome, srespes_matfin, sresbco_cod, sresdiv_age, sresdiv_ccr, SRESPED_ANO, sresdiv_ref_inicio_mes, sresdiv_val  "
			 * +
			 * "FROM sres_divida, sres_pessoa_rr, SRES_PEDIDO, sres_banco, sres_tipo_divida "
			 * + "WHERE sresdiv_pes_id = srespes_id  AND " +
			 * "sresbco_id = sresdiv_bco_id AND " + "srestpdiv_id = sresdiv_tpdiv_id AND " +
			 * "SRESDIV_PED_ID = SRESPED_ID ";
			 */
			String Query_Stmt = "SELECT   SRESPED_COD, srespes_nome, srespes_matsiap, srespes_matfin, sresbco_cod, sresdiv_age, sresdiv_ccr, SRESPED_ANO, sresdiv_ref_inicio_mes, sresdiv_val  "
					+ "FROM sres_divida, sres_pessoa_rr, SRES_PEDIDO, sres_banco, sres_tipo_divida "
					+ "WHERE sresdiv_pes_id = srespes_id  AND " + "sresbco_id = sresdiv_bco_id AND "
					+ "srestpdiv_id = sresdiv_tpdiv_id AND " + "SRESDIV_PED_ID = SRESPED_ID ";

			/* verficar se vou usar essas */
			ArrayList<Object> lista_stmt = null;
			ArrayList<Object> lista_retorno = null;

			lista_retorno = montaParametroFiltroReversao(vParametros);

			lista_stmt = (ArrayList<Object>) lista_retorno.get(1);
			// monta a query adicionando os argumentos de filtro
			Query_Stmt = Query_Stmt + (String) lista_retorno.get(0); /*
																		 * + ") x " + ") " + "WHERE rn BETWEEN " +
																		 * vParametros[19] + " AND " + vParametros[20];
																		 */

			// System.out.println("SQL: " + Query_Stmt);
			// Cria SQL e declara
			stmt = connexao.prepareStatement(Query_Stmt);

			Object parametro = null;

			for (int j = 0; j < lista_stmt.size(); j++) {
				parametro = lista_stmt.get(j);
				if (parametro instanceof Date) {
					stmt.setDate(j + 1, (Date) parametro);

				} else {
					stmt.setString(j + 1, (String) parametro);

				}
			}

			// Configuraï¿½ï¿½o do DAO e Montagem da Lista de dívidas

			results = stmt.executeQuery();

			ArrayList<Divida> dividas = new ArrayList<Divida>();

			// Tratar recordset com objetos recuperados
			while (results.next()) {

				Divida div = new Divida();

				div.setAgencia(results.getString("SRESDIV_AGE"));
				div.setBanco(results.getString("sresbco_cod"));
				div.setContaCorrente(results.getString("SRESDIV_CCR"));
				div.setProcessoInicioStr(results.getString("sresdiv_ref_inicio_mes"));
				div.setValor(Double.parseDouble(results.getString("sresdiv_val")));

				// instanciar objeto Pessoa e preencher
				Pessoa pes = new Pessoa();
				pes.setNome(results.getString("SRESPES_NOME"));
				pes.setMatFin(results.getString("SRESPES_MATFIN"));
				pes.setMatSIAPE(results.getString("SRESPES_MATSIAP"));
				div.setPessoa(pes);

				// instanciar objeto Reversao e preencher
				Pedido rev = new Pedido();
				rev.setCodigo(results.getString("SRESPED_COD"));
				rev.setAno(results.getInt("SRESPED_ANO"));
				div.setPedido(rev);

				dividas.add(registros, div);

				registros++;

			} // fim do while que monta a coleï¿½ï¿½o de reversoes

			if (dividas != null) {

				return dividas;

			}

		} catch (SQLException se) {
			throw new RuntimeException("Ocorreu um erro no Banco de Dados." + se.getMessage());

			// Tratamento de erro SQL

		} catch (Exception se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());

			/*
			 * // Tratamento de erro JNDI }catch (NamingException ne){ throw new
			 * RuntimeException ("Ocorreu um erro no Banco de Dados." + ne.getMessage());
			 */
			// Lipeza dos recursos do JDBC
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("listReversao()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		} // fim do bloco try-catch-finally
		return null;

	}// fim do método

	// **************************************************************************

	public ArrayList<Lancamento> readLancamentosporDivida(String dividaID)
			throws SQLException, NamingException, SispagException {

		// Retorna um ArrayList de lanï¿½amentos para a dívida cujo DividaID ï¿½
		// passado como parametro
		// Caso não encontre nenhum lanï¿½amento retorna null

		// Variáveis JDBC
		// DataSource ds = null;
		// Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		ArrayList<Lancamento> lancamentos = new ArrayList<Lancamento>();
		int registros = 0;

		try {
			obterConexao();

			// Class.forName("oracle.jdbc.OracleDriver");

			// Montagem da Query SQL de acordo com o Cï¿½digo da dívida

			// Montagem da Query SQL de acordo com o Cï¿½digo da dívida

			String Query_Stmt = "SELECT a.*, b.srestplanc_nome FROM SRES_LANCAMENTO a, sres_tipo_lancamento b  WHERE b.srestplanc_id = a.sreslanc_tplanc_id and SRESLANC_DIV_ID = ?";

			stmt = connexao.prepareStatement(Query_Stmt);
			// System.out.println("Id da dívida recebida: " + dividaID);
			stmt.setString(1, dividaID);

			// Executa sql
			results = stmt.executeQuery();

			while (results.next()) {
				System.out.println("Existem lanï¿½amentos jï¿½ cadastrados para esta dívida!");

				// Cria e instancia um objeto Lancamento
				Lancamento lanc = new Lancamento();
				lanc.setIdLancamento(results.getInt("SRESLANC_ID"));
				System.out.println("Resultado da consulta do lanï¿½amento: rigaud" + results.getString("SRESLANC_ID"));
				lanc.setCodigo(results.getInt("SRESLANC_CODIGO"));
				lanc.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lanc.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));

				lanc.setCodigo(results.getInt("SRESLANC_CODIGO"));
				lanc.setResponsavel(results.getString("SRESLANC_RESP_ALT"));
				lanc.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lanc.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));

				lanc.setDataDocAutorizacao(results.getDate("SRESLANC_DT_DOC_AUT"));
				lanc.setDataLancamento(results.getDate("SRESLANC_DT_LANCAMENTO"));
				lanc.setOrigemDocAutorizacao(results.getString("SRESLANC_ORIGEM_DOC_AUT"));
				lanc.setOperador(results.getString("SRESLANC_OPERADOR"));
				lanc.setValor(results.getDouble("SRESLANC_VALOR"));
				lanc.setObservacao(results.getString("SRESLANC_OBS"));
				lanc.setTipo(results.getString("SRESLANC_TPLANC_ID"));
				lanc.setTipoLancNome(results.getString("SRESTPLANC_NOME"));

				// Vetor de lancamentos recebe mais um objeto lancamento
				lancamentos.add(registros, lanc);

				// Vetor de lancamentos recebe mais um objeto lancamento
				// lancamentos.add(registros, lanc);

				// System.out.println("Inseridos" + registros);
				registros++;
			} // Fim do while que cria coleï¿½ï¿½o de lancamentos

			return lancamentos;

			// }catch (Exception se){
			// throw new RuntimeException ("Ocorreu um erro no Banco de Dados."
			// + se.getMessage());

			// }catch (Exception se){
			// throw new RuntimeException ("Ocorreu um erro no Banco de Dados."
			// + se.getMessage());

			// Tratamento de erro SQL
		} catch (SQLException se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());

			/*
			 * // Tratamento de erro JNDI }catch (NamingException ne){ throw new
			 * RuntimeException ("Ocorreu um erro no Banco de Dados." + ne.getMessage());
			 */
			// Lipeza dos recursos do JDBC
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
					throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
					throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("readLancamentosporDivida()");
				} catch (SQLException se) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		} // fim do bloco try-catch-finally

	}// fim do método

	public Lancamento recuperaLancamento(int idLancamento) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// System.out.println("passei pelo recupera "+idLancamento );

		Lancamento lanc = new Lancamento();

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_READ_LANCAMENTO);

			stmt.setInt(1, idLancamento);
			rs = stmt.executeQuery();
			lanc.setIdLancamento(rs.getInt("SRESLANC_ID"));
			lanc.setCodigo(rs.getInt("SRESLANC_CODIGO"));
			lanc.setNumeroDocAutorizacao(rs.getString("SRESLANC_NUM_DOC_AUT"));
			lanc.setTipoDocAutorizacao(rs.getString("SRESLANC_TIPO_DOC_AUT"));
			lanc.setCodigo(rs.getInt("SRESLANC_CODIGO"));
			lanc.setResponsavel(rs.getString("SRESLANC_RESP_ALT"));
			lanc.setNumeroDocAutorizacao(rs.getString("SRESLANC_NUM_DOC_AUT"));
			lanc.setTipoDocAutorizacao(rs.getString("SRESLANC_TIPO_DOC_AUT"));
			lanc.setDataDocAutorizacao(rs.getDate("SRESLANC_DT_DOC_AUT"));
			lanc.setDataLancamento(rs.getDate("SRESLANC_DT_LANCAMENTO"));
			lanc.setOrigemDocAutorizacao(rs.getString("SRESLANC_ORIGEM_DOC_AUT"));
			lanc.setOperador(rs.getString("SRESLANC_OPERADOR"));
			lanc.setValor(rs.getDouble("SRESLANC_VALOR"));
			lanc.setObservacao(rs.getString("SRESLANC_OBS"));
			lanc.setTipo(rs.getString("SRESLANC_TPLANC_ID"));

		} catch (Exception se) {
			se.printStackTrace(System.err);
		} // Fim do bloco catch
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Bloco catch gerado automaticamente
					e.printStackTrace();
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("recuperaLancamento()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return lanc;
	}

	public String buscarProcessoDivida() throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		String mesProcessoGeracao = "";
		String anoProcessoGeracao = "";
		String processo = mesProcessoGeracao + "/" + anoProcessoGeracao;
		// int estagioCGS = 0;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_Consulta_Processo_Atual);

			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {

				mesProcessoGeracao = results.getString("CGSM_DT_PROC_M");
				anoProcessoGeracao = results.getString("GSM_DT_PROC_A");
			}

			// System.out.println("mes" +mesProcessoGeracao);
			// System.out.println("ano" +anoProcessoGeracao);

		} catch (SQLException e) {
			throw new SispagException("Erro ao buscar processo");

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("buscarProcessoDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return processo;

	}

	@SuppressWarnings("finally")
	public Divida buscaValorDivida(Divida divida) throws SispagException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_VALOR_DIVIDA);
			ps.setInt(1, divida.getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				divida.setValor(rs.getDouble("Valor"));
			}

		} catch (SQLException se) {
			se.printStackTrace(System.err);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}
			if (connexao != null) {
				try {
					fecharConexao("buscaValorDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return divida;
		}
	}

	@SuppressWarnings("finally")
	public Divida buscaProcessoMesAnoDivida() throws SispagException {
		Divida dividaRetorno = new Divida();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_Consulta_Processo_Atual);
			rs = ps.executeQuery();
			if (rs.next()) {

				dividaRetorno.setAnoProcessoGeracao(rs.getString("CGSM_DT_PROC_A"));
				dividaRetorno.setMesProcessoGeracao(rs.getString("CGSM_DT_PROC_M"));
				// return dividaRetorno;

			}

		} catch (SQLException se) {
			se.printStackTrace(System.err);
		} finally {
			try {
				if (ps != null)
					ps.close();

			} catch (SQLException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}
			if (connexao != null) {
				try {
					fecharConexao("buscaProcessoMesAnoDivida");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

		}
		return dividaRetorno;
	}

	public boolean incluirReversao(String numeroDocAutorizacao, String tipoDocAutorizacao, String dataDocAutorizacao,
			String origemDocAutorizacao, String operador, double valor, String observacao, int divID, int tipo,
			// int lancamentoRevertidoId,
			String codUsuario) throws SispagException {

		// Lancamento reversao = new Lancamento();
		boolean reversao = false;
		PreparedStatement psReversao = null;

		int execUp = 0;
		// String codUsuario = getUserPrincipal().getName();
		// boolean resultado = false;

		Date dataDAut;
		dataDAut = Date.valueOf(dataDocAutorizacao);

		// System.out.println("Transformei a data no metodo incluir reversão " +
		// dataDAut);

		try {
			obterConexao();

			psReversao = connexao.prepareStatement(SQL_ADD_REVERSAO);

			psReversao.setInt(1, geraCodigoLanc(divID, connexao)); // SRESLANC_CODIGO
			// psReversao.setInt(2, numeroDocAutorizacao); // SRESLANC_NUM_DOC_AUT
			psReversao.setString(2, numeroDocAutorizacao); // SRESLANC_NUM_DOC_AUT
			psReversao.setString(3, tipoDocAutorizacao); // SRESLANC_TIPO_DOC_AUT
			psReversao.setDate(4, dataDAut); // SRESLANC_DT_DOC_AUT
			psReversao.setString(5, origemDocAutorizacao); // SRESLANC_ORIGEM_DOC_AUT
			psReversao.setString(6, operador); // SRESLANC_OPERADOR
			psReversao.setDouble(7, valor); // SRESLANC_VALOR
			psReversao.setString(8, observacao); // SRESLANC_OBS
			psReversao.setString(9, codUsuario); // SRESLANC_RESP_ALT
			psReversao.setInt(10, divID); // SRESLANC_DIV_ID
			psReversao.setInt(11, tipo); // SRESLANC_TPLANC_ID
			// psReversao.setInt(12, lancamentoRevertidoId); // SRESLANC_LANC_ID

			// System.out.println(SQL_ADD_REVERSAO);

			execUp = psReversao.executeUpdate();
			// System.out.println("Executo o update! " + execUp);

			if (execUp != 0) {
				reversao = true;
			}

			// reversao =
			// recuperaLancamento(psReversao.getResultSet().getInt("SRESLANC_ID"));
			// System.out.println("Serï¿½ que funciona?? ");

			// resultado = true;
		} // Fim do bloco try
		catch (SQLException se) {
			se.printStackTrace(System.err);
		} // Fim do bloco catch
		finally {
			if (psReversao != null) {
				try {
					psReversao.close();
				} catch (SQLException e) {
					// TODO Bloco catch gerado automaticamente
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("incluirReversao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // Fim do blo finally
		return reversao;
	}

	/*
	 * public boolean verificarReversao(int numeroDocAutorizacao, String
	 * dataDocAutorizacao, String origemDocAutorizacao, int divID) {
	 */
	public boolean verificarReversao(String numeroDocAutorizacao, String dataDocAutorizacao,
			String origemDocAutorizacao, int divID) throws SispagException {

		// System.out.println("entrei no método verif reversão");

		PreparedStatement psVerificaRev = null;
		boolean resultado = false;
		ResultSet rs = null;

		Date dataDAut;
		dataDAut = Date.valueOf(dataDocAutorizacao);
		// System.out.println("Transformei a data");
		// System.out.println(dataDAut);

		// System.out.println("parï¿½metros do método verificaReversao: ");
		// System.out.println(numeroDocAutorizacao);
		// System.out.println(dataDAut);
		// System.out.println(origemDocAutorizacao);
		// System.out.println(divID);

		try {
			obterConexao();
			psVerificaRev = connexao.prepareStatement(SQL_VERIFICA_REVERSAO);
			// System.out.println("SQL na variï¿½vel psVerificaRev " +
			// SQL_VERIFICA_REVERSAO);

			// psVerificaRev.setInt(1, numeroDocAutorizacao); // SRESLANC_NUM_DOC_AUT
			psVerificaRev.setString(1, numeroDocAutorizacao);
			psVerificaRev.setDate(2, dataDAut); // SRESLANC_DT_DOC_AUT
			psVerificaRev.setString(3, origemDocAutorizacao); // SRESLANC_ORIGEM_DOC_AUT
			psVerificaRev.setInt(4, divID); // SRESLANC_DIV_ID

			rs = psVerificaRev.executeQuery();

			int qtd = 0;

			if (rs.next()) {
				qtd = rs.getInt("QTDE");
			} else {
				qtd = 0;
			}

			// System.out.println("Quant: "+ qtd);

			// Verifica se jï¿½ existe um lanï¿½amento de reversão igual cadastrado para
			// aquela
			// dívida
			if (qtd == 0) {
				resultado = true;
			}
		} // Fim do bloco try
		catch (SQLException se) {
			se.printStackTrace(System.err);
		} // Fim do bloco catch
		finally {
			if (psVerificaRev != null) {
				try {
					psVerificaRev.close();
				} catch (SQLException e) {
					// TODO Bloco catch gerado automaticamente
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("verificarReversao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // Fim do bloco finally
		return resultado;
	}

	public boolean confirmaAtualizaDividaHistorica(Divida div, Date dataDocEnvio, String numeroDocEnvio,
			String tipoDocEnvio, String codUsuario, Connection conexao) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		boolean sucesso = false;

		final String SQL_CONFIRMAATUALIZADIVIDA = "UPDATE SRES_DIVIDA SET " + " SRESDIV_ESTADO  = ?,  "
				+ " SRESDIV_RESP_ALT  = ?,  " + " SRESDIV_RESP_DT  = SYSDATE,  " + " SRESDIV_DOC_ENVIO = ?,  "
				+ " SRESDIV_NUM_DOC_ENVIO = ?, " + " SRESDIV_DT_DOC_ENVIO = ? " + " WHERE  "
				+ " (SRESDIV_CODIGO = ? )  AND  " + " (SRESDIV_PES_ID = ?)";

		try {
			// Cria sql
			stmt = conexao.prepareStatement(SQL_CONFIRMAATUALIZADIVIDA);
			stmt.setString(1, "CONCLUIDO");
			stmt.setString(2, codUsuario);

			stmt.setString(3, tipoDocEnvio);
			// System.out.println(tipoDocEnvio);

			stmt.setString(4, numeroDocEnvio);
			// System.out.println(numeroDocEnvio);

			stmt.setDate(5, dataDocEnvio);
			// System.out.println(dataDocEnvio);

			stmt.setInt(6, div.getCodigo());

			// System.out.println(div.getCodigo());

			// System.out.println("ID DO USUARIO PARA CONFIRMAR");
			// System.out.println(div.getPessoa().getId());

			stmt.setInt(7, div.getPessoa().getId());
			// System.out.println(div.getPessoa().getId());

			// Executa sql
			int n = stmt.executeUpdate();
			// System.out.println(n);
			sucesso = true;

		} catch (SQLException e) {
			// System.out.println("Erro no conf update TTTT " + e);
			sucesso = false;
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
		// System.out.println("Terminou Confirma Divida");
		return sucesso;
	}

	public boolean confirmaAtualizaDividaMensal(Divida div, String codUsuario, Connection conexao)
			throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		boolean sucesso = false;

		final String SQL_CONFIRMAATUALIZADIVIDA = "UPDATE sisres.SRES_DIVIDA SET " + " SRESDIV_ESTADO  = ?,  "
				+ " SRESDIV_RESP_ALT  = ?,  " + " SRESDIV_RESP_DT  = SYSDATE  " + " WHERE  "
				+ "  (SRESDIV_CODIGO = ? )  AND  " + " (SRESDIV_PES_ID = ?)";

		try {
			// Cria sql
			// System.out.println("SQL Confirmar: "+SQL_CONFIRMAATUALIZADIVIDA);

			stmt = conexao.prepareStatement(SQL_CONFIRMAATUALIZADIVIDA);
			stmt.setString(1, "CONCLUIDO");
			stmt.setString(2, codUsuario);

			stmt.setInt(3, div.getCodigo());

			// System.out.println(div.getCodigo());

			// System.out.println("ID DO USUARIO PARA CONFIRMAR");
			// System.out.println(div.getPessoa().getId());

			stmt.setInt(4, div.getPessoa().getId());
			// System.out.println(div.getPessoa().getId());
			// Executa sql
			int n = stmt.executeUpdate();
			// System.out.println(n);
			sucesso = true;

		} catch (SQLException e) {
			// System.out.println("Erro no conf update TTTT " + e);
			sucesso = false;
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
		// System.out.println("Terminou Confirma Divida");
		return sucesso;
	}

	public void confirmaAtualizaLancamentoHistorica(Divida div, Date dataDocAut, String numeroDocAut, String tipoDocAut,
			String origemDocAut, Date dataDocEnvio, String numeroDocEnvio, String tipoDocEnvio, String codUsuario)
			throws SispagException {

		boolean transaction = false;

		try {
			obterConexao();
			connexao.setAutoCommit(false);

			PreparedStatement stmt = null;
			ResultSet results = null;

			// Atualiza os valores de dívida
			div.setPessoa(readPessoa(div.getCgs(), div.getPessoa().getMatFin()));
			div.setDataDocEnvio(dataDocEnvio);
			div.setNumeroDocEnvio(numeroDocAut);
			div.setDocEnvio(tipoDocAut);
			div.getLancamentoInicial().setNumeroDocAutorizacao(numeroDocAut);
			div.getLancamentoInicial().setTipoDocAutorizacao(tipoDocAut);
			div.getLancamentoInicial().setDataDocAutorizacao(dataDocAut);
			div.getLancamentoInicial().setOrigemDocAutorizacao(origemDocAut);

			transaction = confirmaAtualizaDividaHistorica(div, dataDocEnvio, numeroDocEnvio, tipoDocEnvio, codUsuario,
					connexao);

			if (transaction) {

				final String SQL_CONFIRMAATUALIZALANCAMENTODIVIDA = "UPDATE SRES_LANCAMENTO SET "
						+ " SRESLANC_NUM_DOC_AUT  = ?,  " + " SRESLANC_TIPO_DOC_AUT  = ?,  "
						+ " SRESLANC_DT_DOC_AUT  = ?,  " + " SRESLANC_ORIGEM_DOC_AUT  = ?,  "
						+ " SRESLANC_RESP_ALT  = ?,  " + " SRESLANC_RESP_DT  = SYSDATE  " + " WHERE  "
						+ " (SRESLANC_DIV_ID = ? )  " + " AND  " + " (SRESLANC_CODIGO = ?)";

				try {
					// Cria sql

					/*
					 * int aa = numeroDocAut; String bb = tipoDocAut; Date cc = dataDocAut; String
					 * dd = origemDocAut; int divid = div.getLancamentoInicial().getDividaId(); int
					 * divcodigo = div.getLancamentoInicial().getCodigo();
					 */

					stmt = connexao.prepareStatement(SQL_CONFIRMAATUALIZALANCAMENTODIVIDA);
					stmt.setString(1, numeroDocAut);
					stmt.setString(2, tipoDocAut);
					stmt.setDate(3, dataDocAut);
					stmt.setString(4, origemDocAut);
					stmt.setString(5, "Teste");
					stmt.setInt(6, div.getLancamentoInicial().getDividaId());
					stmt.setInt(7, div.getLancamentoInicial().getCodigo());
					// System.out.println("ID DO LANCAMENTO PARA CONFIRMAR");
					// System.out.println(div.getLancamentoInicial().getDividaId());
					// System.out.println(div.getLancamentoInicial().getCodigo());
					// stmt.setInt(7, div.getLancamentoInicial().getDividaId());
					// stmt.setInt(8, div.getLancamentoInicial().getCodigo());

					// Executa sql

					int n = stmt.executeUpdate();
					// System.out.println(n);
					transaction = true;

				} catch (SQLException e) {
					// System.out.println("Erro no conf update TTTT " + e);
					transaction = false;
					throw new SispagException("Erro inserindo em Lancamento");
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
				// System.out.println("Terminou Confirma Divida");
				processTransaction(transaction);
			}
		} catch (SQLException e) {
			// System.out.println("Erro ao definir conn.autocommit(false)");
			throw new SispagException("Erro ao definir propriedade no banco de dados.");
		} finally {
			if (connexao != null)
				try {

					fecharConexao("confirmaAtualizaLancamentoHistorica()");
				} catch (SQLException e1) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
		}
	}

//**************************
	public void confirmaAtualizaLancamentoMensal(Divida div, Date dataDocAut, String numeroDocAut, String tipoDocAut,
			String origemDocAut, String codUsuario) throws SispagException {

		boolean transaction = false;

		try {
			obterConexao();
			connexao.setAutoCommit(false);

			PreparedStatement stmt = null;
			ResultSet results = null;

			// Atualiza os valores de dívida
			div.setPessoa(readPessoa(div.getCgs(), div.getPessoa().getMatFin()));
			div.getLancamentoInicial().setNumeroDocAutorizacao(numeroDocAut);
			div.getLancamentoInicial().setTipoDocAutorizacao(tipoDocAut);
			div.getLancamentoInicial().setDataDocAutorizacao(dataDocAut);
			div.getLancamentoInicial().setOrigemDocAutorizacao(origemDocAut);

			transaction = confirmaAtualizaDividaMensal(div, codUsuario, connexao);

			if (transaction) {

				final String SQL_CONFIRMAATUALIZALANCAMENTODIVIDA = "UPDATE SRES_LANCAMENTO SET "
						+ " SRESLANC_NUM_DOC_AUT  = ?,  " + " SRESLANC_TIPO_DOC_AUT  = ?,  "
						+ " SRESLANC_DT_DOC_AUT  = ?,  " + " SRESLANC_ORIGEM_DOC_AUT  = ?,  "
						+ " SRESLANC_RESP_ALT  = ?,  " + " SRESLANC_RESP_DT  = SYSDATE  " + " WHERE  "
						+ " (SRESLANC_DIV_ID = ? )  " + " AND  " + " (SRESLANC_CODIGO = ?)";

				try {
					// Cria sql

					/*
					 * int aa = numeroDocAut; String bb = tipoDocAut; Date cc = dataDocAut; String
					 * dd = origemDocAut; int divid = div.getLancamentoInicial().getDividaId(); int
					 * divcodigo = div.getLancamentoInicial().getCodigo();
					 */

					stmt = connexao.prepareStatement(SQL_CONFIRMAATUALIZALANCAMENTODIVIDA);
					stmt.setString(1, numeroDocAut);
					stmt.setString(2, tipoDocAut);
					stmt.setDate(3, dataDocAut);
					stmt.setString(4, origemDocAut);
					stmt.setString(5, "Teste");
					stmt.setInt(6, div.getLancamentoInicial().getDividaId());
					stmt.setInt(7, div.getLancamentoInicial().getCodigo());
					// System.out.println("ID DO LANCAMENTO PARA CONFIRMAR");
					// System.out.println(div.getLancamentoInicial().getDividaId());
					// System.out.println(div.getLancamentoInicial().getCodigo());
					// stmt.setInt(7, div.getLancamentoInicial().getDividaId());
					// stmt.setInt(8, div.getLancamentoInicial().getCodigo());

					// Executa sql

					int n = stmt.executeUpdate();
					// System.out.println(n);
					transaction = true;

				} catch (SQLException e) {
					// System.out.println("Erro no conf update TTTT " + e);
					transaction = false;
					throw new SispagException("Erro inserindo em Lancamento");
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
				// System.out.println("Terminou Confirma Divida");
				processTransaction(transaction);
			}
		} catch (SQLException e) {
			// System.out.println("Erro ao definir conn.autocommit(false)");
			throw new SispagException("Erro ao definir propriedade no banco de dados.");
		} finally {
			if (connexao != null)
				try {

					fecharConexao("confirmaAtualizaLancamentoMensal()");
				} catch (SQLException e1) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
		}
	}

//**************************

	public void incluirLancamentoEstornoReversao(Lancamento lancamento) throws SispagException {
		/* INSERE EM LANCAMENTO */
		PreparedStatement stmt = null;
		ResultSet results = null;

		final String ESTORNO_REVERSAO = "INSERT INTO SRES_LANCAMENTO (" + " SRESLANC_ID," + " SRESLANC_CODIGO,"
				+ " SRESLANC_NUM_DOC_AUT," + " SRESLANC_TIPO_DOC_AUT," + " SRESLANC_DT_DOC_AUT,"
				+ " SRESLANC_ORIGEM_DOC_AUT, " + " SRESLANC_OPERADOR," + " SRESLANC_VALOR," + " SRESLANC_OBS,"
				+ " SRESLANC_DIV_ID, " + " SRESLANC_LANC_ID, " + " SRESLANC_TPLANC_ID," + " SRESLANC_RESP_ALT,"
				+ " SRESLANC_RESP_DT) " + " VALUES(SQ_SRES_LANCAMENTO_ID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE)";

		// if (verificarEstorno(lancamento.getIdLancamento()) == true) {

		try {
			// Cria sql
			obterConexao();
			stmt = connexao.prepareStatement(ESTORNO_REVERSAO);
			stmt.setInt(1, geraCodigoLanc(lancamento.getDividaId(), connexao));
			// System.out.println("novo codigo:"
			// + geraCodigoLanc(lancamento.getDividaId(),conn));
			// buscar novo codigo

			stmt.setString(2, lancamento.getNumeroDocAutorizacao());
			// System.out.println("getNumeroDocAutorizacao 1: "
			// + lancamento.getNumeroDocAutorizacao());

			stmt.setString(3, lancamento.getTipoDocAutorizacao());
			// System.out.println("getIdLancamento 1: "
			// + lancamento.getTipoDocAutorizacao());

			stmt.setDate(4, (Date) lancamento.getDataDocAutorizacao());
			// System.out.println("getDataDocAutorizacao 1: "
			// + lancamento.getDataDocAutorizacao());

			stmt.setString(5, lancamento.getOrigemDocAutorizacao());
			// System.out.println("getOrigemDocAutorizacao 1: "
			// + lancamento.getOrigemDocAutorizacao());

			if (lancamento.getOperador().equals("D")) {
				stmt.setString(6, "C");
			} else {
				stmt.setString(6, "D");
			}

			stmt.setDouble(7, lancamento.getValor());
			// System.out.println("getValor 1: " + lancamento.getValor());

			stmt.setString(8, lancamento.getObservacao());

			stmt.setInt(9, lancamento.getDividaId());
			// System.out.println("divida 2: " + lancamento.getDividaId());

			stmt.setInt(10, lancamento.getIdLancamento());
			// System.out.println("getIdLancamento 1: "
			// + lancamento.getIdLancamento());

			stmt.setString(11, "5"); // tipo 5 = estorno

			stmt.setString(12, lancamento.getResponsavel());
			// System.out.println("getResponsavel 1: "
			// + lancamento.getResponsavel());

			// Executa sql
			results = stmt.executeQuery();

		} catch (SQLException e) {
			// System.out.println(e);
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
			if (connexao != null) {
				try {
					fecharConexao("incluirLancamentoEstornoReversao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
	}

	public boolean verificarEstorno(int id) throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		boolean estorno = false;

		final String ESTORNO = "SELECT COUNT(*) COUNT FROM SRES_LANCAMENTO " + "WHERE SRESLANC_LANC_ID = ? "
				+ " AND SRESLANC_TPLANC_ID = 5 ";

		// System.out.println("Id Lancamento:" + id);
		try {
			obterConexao();
			// Cria sql
			stmt = connexao.prepareStatement(ESTORNO);
			stmt.setInt(1, id);
			// Executa sql
			results = stmt.executeQuery();

			while (results.next()) {
				// System.out.println(results.getInt("COUNT"));
				if (results.getInt("COUNT") == 0) {
					estorno = true;
				}
			}
		} catch (SQLException e) {
			// System.out.println(e);

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("verificarEstorno()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return estorno;
	}
	// fim verificar estorno

	public List<Motivo> getAllMotivo() throws SispagException {
		List<Motivo> motivoList = new ArrayList<Motivo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement("SELECT * FROM SRES_MOTIVO ORDER BY SRESMOT_ID");
			rs = ps.executeQuery();
			while (rs.next()) {
				Motivo motivo = new Motivo(rs.getInt("SRESMOT_ID"), rs.getString("SRESMOT_NOME"));

				motivoList.add(motivo);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("getAllMotivo()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return motivoList;
	}

	public List<Divida> getAllDividaHistoricaEmEspera() throws SispagException {
		List<Divida> dividaHistoricaList = new ArrayList<Divida>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_REL_DIVHIST_ESPERA);
			rs = ps.executeQuery();

			while (rs.next()) {
				Divida dividaHistorica = new Divida(rs.getString("SRESDIV_CGS"), rs.getString("SRESDIV_CODIGO"),
						rs.getString("SRESPES_MATFIN"),
						rs.getString("SRESDIV_REF_INICIO_MES") + "/" + rs.getString("SRESDIV_REF_INICIO_ANO"),
						rs.getString("SRESDIV_REF_TERMINO_MES") + "/" + rs.getString("SRESDIV_REF_TERMINO_ANO"),
						rs.getString("SRESPES_CPF"), rs.getString("SRESPES_NOME"), rs.getString("SRESPES_PT"),
						rs.getString("SRESPES_SITUACAO"), rs.getInt("SRESOM_OM_ID"), rs.getString("SRESOM_OM_OC_COD"),
						rs.getString("SRESOM_NOME"), rs.getString("SRESBCO_COD"), rs.getString("SRESDIV_AGE"),
						rs.getString("SRESDIV_CCR"), rs.getDouble("SRESDIV_VAL"), rs.getString("SRESDIV_TPDIV_ID"),
						rs.getString("SRESLANC_ORIGEM_DOC_AUT"), rs.getString("SRESLANC_TIPO_DOC_AUT"),
						rs.getString("SRESLANC_NUM_DOC_AUT"), rs.getString("SRESLANC_DT_DOC_AUT"),
						rs.getString("SRESDIV_DT_MOTIVO"), rs.getString("SRESDIV_MOT_ID"),
						rs.getString("SRESDIV_DOC_ENVIO"), rs.getString("SRESDIV_NUM_DOC_ENVIO"),
						rs.getDate("SRESDIV_DT_DOC_ENVIO"), rs.getString("SRESMOT_NOME"), rs.getInt("SRESPES_ID"),
						rs.getInt("SRESLANC_DIV_ID"), rs.getInt("SRESLANC_CODIGO"));

				dividaHistoricaList.add(dividaHistorica);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("getAllDividaHistoricaEmEspera()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return dividaHistoricaList;
	}

	public List<Divida> getAllDividaMesEmEspera() throws SispagException {
		List<Divida> dividaMesList = new ArrayList<Divida>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_REL_DIVMES_ESPERA);
			rs = ps.executeQuery();

			while (rs.next()) {
				Divida dividaMes = new Divida(rs.getString("SRESDIV_CGS"), rs.getString("SRESDIV_CODIGO"),
						rs.getString("SRESPES_MATFIN"),
						rs.getString("SRESDIV_REF_INICIO_MES") + "/" + rs.getString("SRESDIV_REF_INICIO_ANO"),
						rs.getString("SRESDIV_REF_TERMINO_MES") + "/" + rs.getString("SRESDIV_REF_TERMINO_ANO"),
						rs.getString("SRESPES_CPF"), rs.getString("SRESPES_NOME"), rs.getString("SRESPES_PT"),
						rs.getString("SRESPES_SITUACAO"), rs.getInt("SRESOM_OM_ID"), rs.getString("SRESOM_OM_OC_COD"),
						rs.getString("SRESOM_NOME"), rs.getString("SRESBCO_COD"), rs.getString("SRESDIV_AGE"),
						rs.getString("SRESDIV_CCR"), rs.getDouble("SRESDIV_VAL"), rs.getString("SRESDIV_TPDIV_ID"),
						rs.getString("SRESLANC_ORIGEM_DOC_AUT"), rs.getString("SRESLANC_TIPO_DOC_AUT"),
						rs.getString("SRESLANC_NUM_DOC_AUT"), rs.getString("SRESLANC_DT_DOC_AUT"),
						rs.getString("SRESDIV_DT_MOTIVO"), rs.getString("SRESDIV_MOT_ID"),
						rs.getString("SRESDIV_DOC_ENVIO"), rs.getString("SRESDIV_NUM_DOC_ENVIO"),
						rs.getDate("SRESDIV_DT_DOC_ENVIO"), rs.getString("SRESMOT_NOME"), rs.getInt("SRESPES_ID"),
						rs.getInt("SRESLANC_DIV_ID"), rs.getInt("SRESLANC_CODIGO"));

				dividaMesList.add(dividaMes);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("getAllDividaMesEmEspera()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}
		return dividaMesList;
	}

	public boolean verificaLancReversao(int codDivida) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_LANCAMENTO_REVERSAO);
			stmt.setInt(1, codDivida);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro inserindo em dívida");
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
			if (connexao != null) {
				try {
					fecharConexao("verificaLancReversao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // end do finally

	}// end da funcao

	public Double verificaSaldoRegularizar(int codDivida) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection con = null;
		try {

			con = ds.getConnection();
			stmt = con.prepareStatement(SQL_SALDO_REGULARIZAR);
			stmt.setInt(1, codDivida);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return results.getDouble("SALDOAREGULARIZAR");
			} else {
				return null;
			}
		} catch (SQLException e) {
			logger.error("Erro inserindo em dívida: {} \n", codDivida, e);			
			throw new SispagException("Erro inserindo em dívida " + e.getMessage());
		} finally {			
			DButils.closeQuietly(results, stmt, con);			
		} 

	}// end da funcao

	public boolean verificaTipoDivida(String tipoDivida) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_TIPO_DIVIDA);
			stmt.setString(1, tipoDivida);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro inserindo em dívida");
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
			if (connexao != null) {
				try {
					fecharConexao("verificaTipoDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // end do finally

	}// end da funï¿½ï¿½o

	public boolean verificaTipoBanco(String tipoBanco) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_TIPO_BANCO);
			stmt.setString(1, tipoBanco);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro na verificaï¿½ï¿½o do Banco");
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
				if (connexao != null) {
					try {
						fecharConexao("verificaTipoBanco()");
					} catch (SQLException e) {
						// System.out.println("Erro ao fechar conexão com o BD");
						throw new SispagException("Erro ao conectar ao BD.");
					}
				}
			}
		} // end do finally
	}// end da funï¿½ï¿½o

	public boolean corrigirDivida(Lancamento lancamento) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_CORRIGIR_DIVIDA);
			stmt.setInt(1, lancamento.getCodigo());
			stmt.setString(2, lancamento.getOperador());
			stmt.setDouble(3, lancamento.getValor());
			stmt.setInt(4, lancamento.getDividaId());
			stmt.setInt(5, getLancamento(lancamento.getDividaId(), 1).getIdLancamento());
			stmt.setString(6, lancamento.getResponsavel());

			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro na Correï¿½ï¿½o da Dívida");
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
			if (connexao != null) {
				try {
					fecharConexao("corrigirDivida()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // end do finally

	}// end da função corrigirDivida

	public Lancamento getLancamento(int dividaId, int tipoLancamento) throws SispagException {
		Lancamento lancamento = new Lancamento();
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_LANCAMENTO_DIVIDA);
			stmt.setInt(1, dividaId);
			stmt.setInt(2, tipoLancamento);

			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				lancamento.setCodigo(results.getInt("SRESLANC_CODIGO"));
				lancamento.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lancamento.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));
				lancamento.setDataDocAutorizacao(results.getDate("SRESLANC_DT_DOC_AUT"));
				lancamento.setOrigemDocAutorizacao(results.getString("SRESLANC_ORIGEM_DOC_AUT"));
				lancamento.setOperador(results.getString("SRESLANC_OPERADOR"));
				lancamento.setValor(results.getDouble("SRESLANC_VALOR"));
				lancamento.setObservacao(results.getString("SRESLANC_OBS"));
				lancamento.setResponsavel(results.getString("SRESLANC_RESP_ALT"));
				lancamento.setDividaId(results.getInt("SRESLANC_DIV_ID"));
				lancamento.setTipo(results.getString("SRESLANC_TPLANC_ID"));
				lancamento.setIdLancamento(results.getInt("SRESLANC_ID"));
				lancamento.setDataLancamento(results.getDate("SRESLANC_DT_LANCAMENTO"));

				return lancamento;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro na Busca do Lançamento");
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
			if (connexao != null) {
				try {
					fecharConexao("getLancamento()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // end do finally
	} // end da funcao getLancamento

	public boolean verificaReversao(int idPedido) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_VERIFICA_REVERSAO);
			stmt.setInt(1, idPedido);
			// Executa sql
			results = stmt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// System.out.println(e);
			throw new SispagException("Erro na verificaï¿½ï¿½o se existe dívida que possui reversão");
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
			if (connexao != null) {
				try {
					fecharConexao("verificaReversao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		} // end do finally

	}// end da funï¿½ï¿½o

	public boolean deletePedido(int pedido) throws SispagException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_DELETE_PEDIDO);
			ps.setInt(1, pedido);
			resultado = ps.execute();
			return resultado;

		} catch (SQLException se) {
			throw new SispagException("Erro ao excluir Pedido de Reversão ");
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("deletePedido()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return resultado;
		} // end do finally

	}// end da funcao delete Pedido

	public boolean inserirLancamentoPerdao(Lancamento lancamento) throws SispagException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_INSERIR_LANCAMENTO_PERDAO);
			int id = geraCodigoLanc(lancamento.getDividaId(), connexao);
			ps.setInt(1, id);
			// System.out.println("id: "+id);
			ps.setString(2, lancamento.getNumeroDocAutorizacao());
			// System.out.println("n doc aut: "+lancamento.getNumeroDocAutorizacao());
			ps.setString(3, lancamento.getTipoDocAutorizacao());
			// System.out.println("tipo doc aut: "+lancamento.getTipoDocAutorizacao());
			ps.setDate(4, new java.sql.Date(lancamento.getDataDocAutorizacao().getTime()));
			// System.out.println("Data doc aut: " + new
			// java.sql.Date(lancamento.getDataDocAutorizacao().getTime()));
			ps.setString(5, lancamento.getOrigemDocAutorizacao());
			// System.out.println(" orig doc aut: " + lancamento.getOrigemDocAutorizacao());
			// System.out.println("operador: " +lancamento.getOperador());
			ps.setString(6, lancamento.getOperador());
			// System.out.println("valor: " +lancamento.getValor());
			ps.setDouble(7, lancamento.getValor());
			ps.setString(8, lancamento.getObservacao());
			// System.out.println("Codigo da divida: " +lancamento.getDividaId());
			ps.setInt(9, lancamento.getDividaId());
			// System.out.println("Tipo :" +lancamento.getTipo());
			ps.setString(10, lancamento.getTipo());
			// System.out.println(lancamento.getResponsavel());
			ps.setString(11, lancamento.getResponsavel());

			int res = 0;
			res = ps.executeUpdate();
			// System.out.println("Resultado do update: "+res);
			if (res > 0) {
				resultado = true;
			} else {
				resultado = false;
			}

		} catch (SQLException se) {

			throw new SispagException("Erro ao inserir Lançamento de Perdão ");
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new SispagException("Erro ao inserir Lançamento de Perdão ");
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SispagException("Erro ao inserir Lançamento de Perdão ");
				}
			}

			if (connexao != null) {
				try {
					fecharConexao("inserirLancamentoPerdao()");
				} catch (Exception e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

			// System.out.println("Valor de resultado: "+ resultado);
			return resultado;

		} // end do finally

	}// end da funcao inserir Lancamento PERDï¿½O

	public boolean inserirLancamentoDevolucao(Lancamento lancamento) throws SispagException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;
		try {
			obterConexao();
			ps = connexao.prepareStatement(SQL_INSERIR_LANCAMENTO_DEVOLUCAO);
			int id = geraCodigoLanc(lancamento.getDividaId(), connexao);
			ps.setInt(1, id);
			ps.setString(2, lancamento.getNumeroDocAutorizacao());
			ps.setString(3, lancamento.getTipoDocAutorizacao());
			ps.setDate(4, new java.sql.Date(lancamento.getDataDocAutorizacao().getTime()));
			ps.setString(5, lancamento.getOrigemDocAutorizacao());
			// System.out.println("Lançamento teste:" +
			// lancamento.getOrigemDocAutorizacao());
			// ps.setString(5,"PAPEM");
			// System.out.println("TipoDocAutorizacao:"+lancamento.getTipoDocAutorizacao());
			// System.out.println(lancamento.getOperador());
			ps.setString(6, lancamento.getOperador());
			// System.out.println(lancamento.getValor());
			ps.setDouble(7, lancamento.getValor());
			ps.setString(8, lancamento.getObservacao());
			// System.out.println(lancamento.getDividaId());
			ps.setInt(9, lancamento.getDividaId());
			// System.out.println(lancamento.getTipo());
			ps.setString(10, lancamento.getTipo());
			// System.out.println(lancamento.getResponsavel());
			ps.setString(11, lancamento.getResponsavel());
			// System.out.println(lancamento.getNumeroOB());
			ps.setString(12, lancamento.getNumeroOB());
			// System.out.println(new java.sql.Date(lancamento.getDataOB().getTime()));
			ps.setDate(13, new java.sql.Date(lancamento.getDataOB().getTime()));

			/**
			 * 
			 * Abaixo, reorg tenente luis
			 * 
			 */

			/*
			 * ps = conn.prepareStatement(SQL_INSERIR_LANCAMENTO_DEVOLUCAO); int id =
			 * geraCodigoLanc(lancamento.getDividaId()); ps.setInt(1, id); //SRESLANC_ID
			 * //System.out.println(id); ps.setInt(2, lancamento.getCodigo());
			 * //SRESLANC_CODIGO //System.out.println(lancamento.getCodigo());
			 * ps.setString(3, lancamento.getNumeroDocAutorizacao());//SRESLANC_NUM_DOC_AUT
			 * //System.out.println(lancamento.getNumeroDocAutorizacao()); ps.setString(4,
			 * lancamento.getTipoDocAutorizacao()); //SRESLANC_TIPO_DOC_AUT
			 * //System.out.println(lancamento.getTipoDocAutorizacao()); ps.setDate(5, new
			 * Date(lancamento.getDataDocAutorizacao().getTime()));//SRESLANC_DT_DOC_AUT
			 * //System.out.println(lancamento.getDataDocAutorizacao().getTime());
			 * ps.setString(6, lancamento.getOperador()); //SRESLANC_OPERADOR
			 * //System.out.println(lancamento.getOperador());
			 * ps.setDouble(7,lancamento.getValor());//SRESLANC_VALOR
			 * //System.out.println(lancamento.getValor()); ps.setString(8,
			 * lancamento.getObservacao());//SRESLANC_OBS
			 * //System.out.println(lancamento.getObservacao()); ps.setInt(9,
			 * lancamento.getDividaId());//SRESLANC_DIV_ID
			 * //System.out.println(lancamento.getDividaId()); ps.setString(10,
			 * lancamento.getTipo());//SRESLANC_TPLANC_ID
			 * //System.out.println(lancamento.getTipo()); ps.setString(11,
			 * lancamento.getResponsavel());//SRESLANC_RESP_ALT
			 * //System.out.println(lancamento.getResponsavel()); ps.setDate(12, new
			 * Date(lancamento.getDataLancamento().getTime())); //SRESLANC_RESP_DT
			 * //System.out.println(lancamento.getDataLancamento().getTime());
			 * ps.setString(13, lancamento.getNumeroOB());//SRESLANC_NUM_0B
			 * //System.out.println(lancamento.getNumeroOB()); ps.setDate(14, new
			 * Date(lancamento.getDataOB().getTime())); //SRESLANC_DT_OB
			 * //System.out.println(lancamento.getDataOB().getTime());
			 *//*****//*
						 * // //System.out.println(lancamento.getTipo()); // ps.setString(10,
						 * lancamento.getTipo()); // //System.out.println(lancamento.getResponsavel());
						 * // ps.setString(11,lancamento.getResponsavel()); //
						 * //System.out.println(lancamento.getNumeroOB()); //
						 * ps.setString(12,lancamento.getNumeroOB()); // //System.out.println(new
						 * java.sql.Date(lancamento.getDataOB().getTime())); // ps.setDate(13,new
						 * java.sql.Date(lancamento.getDataOB().getTime())); //
						 * //System.out.println(ps.);
						 */
			int res = 0;
			res = ps.executeUpdate();
			if (res > 0) {
				resultado = true;
			} else {
				resultado = false;
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SispagException("Erro ao inserir Lançamento de Devolução ");
		} finally {
			// System.out.println("Entrou no finally do inc lancamento");
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new SispagException("Erro ao inserir Lançamento de Devolução ");
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SispagException("Erro ao inserir Lançamento de Devolução ");
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("inserirLancamentoDevolucao()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");

				}
			}
			return resultado;
		} // end do finally

	}// end da funcao inserir Lancamento devolucao

	public String[][] getDividasMovimentacaoSisres(String mes, String ano) throws SispagException {
		ActionErrors errors = new ActionErrors();
		String[][] listMovimentacao = null;
		PreparedStatement psMovimentacao = null;
		ResultSet rsMovimentacao = null;
		PreparedStatement psSaldoAnterior = null;
		ResultSet rsSaldoAnterior = null;
		String[][] listaMovimentacoes = null;

		try {
			String dataReferenciaFormatada, dataAnteriorFormatada;
			String dia = "01";
			String mesAnoInicioConsultaSaldoAnterior = null;

			// Criando a data com o mï¿½s de Referencia passado na chamada do método
			dataAnteriorFormatada = Utilitaria.formatarData(mes + '/' + dia + '/' + ano, "dd/MM/yy");

			/*
			 * decrementar a data mesAnoAnterior -1 para acertar o mï¿½s pois o
			 * gregorianCalendar comeï¿½a o mï¿½s no 0 e -1 para pegar o mï¿½s anterior ao
			 * informardo como referencia
			 */
			Calendar novaData = new GregorianCalendar(Integer.parseInt(ano), (Integer.parseInt(mes) - 1),
					Integer.parseInt(dia));
			novaData.add(Calendar.MONTH, -1);

			dataAnteriorFormatada = Utilitaria.formatarData(novaData.getTime(), "dd/MM/yy");
			double saldoAtual = 0;

			String[] param = new String[3];
			for (int j = 0; j < 3; j++) {
				// param[j] = mes + '/' + ano.substring(2,4) ;
				param[j] = mes + '/' + ano;
			}
			int qtdMov = contaRegistroMovimentacaoOC(param, SQL_QTD_MOVIMENTACAO_SISRES);

			listaMovimentacoes = obterListaMovimentacao(mes, ano, qtdMov, SQL_MOVIMENTACAO_SISRES);
			String[] parametroMovimentacao = new String[3];
			for (int j = 0; j < 3; j++) {
//					parametroMovimentacao[j] =  mes +  '/' + ano.substring(2,4) ;
				parametroMovimentacao[j] = mes + '/' + ano;
			}
			int qtdLinhasMovimentacao = contaRegistroMovimentacaoOC(parametroMovimentacao, SQL_COUNT_MOVIMENTACAO);
			// contRegistro(parametroMovimentacao, SQL_COUNT_MOVIMENTACAO,conn);

			// System.out.println(qtdLinhasMovimentacao);

			listMovimentacao = new String[qtdLinhasMovimentacao][6];
			// Percorre cada linha do resultado para alimentar o relatorio
			// while (rsMovimentacao.next()) {
			for (int i = 0; i < listaMovimentacoes.length; i++) {
				// incluir na lista o codigo da OC, Inclusao, exclusao retornados pela query
				// SQL_MOVIMENTACAO_SISRES
				listMovimentacao[i][0] = listaMovimentacoes[i][1];// OC
				listMovimentacao[i][1] = listaMovimentacoes[i][2];// Nome da OC
				mesAnoInicioConsultaSaldoAnterior = getMesAnoInicio(listaMovimentacoes[i][1]);
				String[][] listaSaldoAnterior = obterSaldoAnterior(mesAnoInicioConsultaSaldoAnterior,
						dataAnteriorFormatada, listaMovimentacoes[i][1], qtdLinhasMovimentacao,
						SQL_MOVIMENTACAO_SISRES_MES_ANTERIOR);
				// Calculado o saldo Atual (se o saldo anterior for <> de Zero)
				// if (rsSaldoAnterior.next()) {
				// if(listaSaldoAnterior.length > 1 && listaSaldoAnterior[1] != null){
				if (listaSaldoAnterior.length > i) {
					// saldoAtual = (Math.abs(rsSaldoAnterior.getDouble("SALDOMESANTECESSOR")) -
					// (Math.abs(rsMovimentacao.getDouble("SALDOMES"))));
					saldoAtual = Math.abs(Double.parseDouble(listaSaldoAnterior[0][0])); // SALDOMESANTECESSOR
					listMovimentacao[i][2] = Utilitaria.formatarValor("######0.00",
							Math.abs(Double.parseDouble(listaSaldoAnterior[0][0])));
				} else { // se o saldo anterior for = zero
					// inclui na lista o valor 0 pois não hï¿½ saldo anterior para essa OC
					// saldoAtual = Math.abs(0 - (Math.abs(rsMovimentacao.getDouble("SALDOMES"))));
					saldoAtual = 0;
					listMovimentacao[i][2] = "0";
				} // fim do if que testa o valor do saldo anterior retornado
					// incluir os valores de inclusao e exclusao de cada OC

				listMovimentacao[i][3] = Utilitaria.formatarValor("######0.00",
						Math.abs(Double.parseDouble(listaMovimentacoes[i][3])));// inclusao
				listMovimentacao[i][4] = Utilitaria.formatarValor("######0.00",
						Math.abs(Double.parseDouble(listaMovimentacoes[i][4])));// exclusao
				// calcular o saldo atual (Saldo anterior + inclusï¿½es - exclusoes)
				saldoAtual = saldoAtual + Math.abs(Double.parseDouble(listaMovimentacoes[i][3]))
						- Math.abs(Double.parseDouble(listaMovimentacoes[i][4]));
				// incluir na lista o saldo Atual calculado acima
				listMovimentacao[i][5] = Utilitaria.formatarValor("######0.00", Math.abs(saldoAtual));

			} // final do While rsMovimentacao
		} catch (SQLException e) {
			// System.out.println("Erro no BB ao obter relatorio de dividas por OC: \n");
			e.printStackTrace();
			throw new SispagException("Erro ao obter as Dividas por OC");
		}
		return listMovimentacao;
	}

	private int contaRegistroMovimentacaoOC(String[] parametros, String sql) throws SispagException {
		int qtd = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement(sql);

			// System.out.println("Entrei no count registro");

			// System.out.println(sql);

			for (int i = 0; i < parametros.length; i++) {
				// System.out.println(parametros[i]);
			}

			for (int i = 0; i < parametros.length; i++) {
				ps.setString(i + 1, parametros[i]);
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				qtd = rs.getInt("QTD");
			} else {
				qtd = 0;
			}
			rs.close();
			ps.close();
		} catch (SQLException se) {
			// System.out.println(se.getMessage());
			se.printStackTrace();
		} finally {
			if (connexao != null) {
				try {
					fecharConexao("contRegistro()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return qtd;
		}

	}

	public String[][] obterListaMovimentacao(String mes, String ano, int qtde, String query)
			throws SispagException, SQLException {
		PreparedStatement psMovimentacao = null;
		ResultSet rsMovimentacao = null;
		String[][] listaRetorno = null;
		try {
			obterConexao();
			psMovimentacao = connexao.prepareStatement(query);

			// System.out.println(SQL_MOVIMENTACAO_SISRES);

			for (int i = 1; i < 4; i++) {
//				//System.out.println(mes +  '/' + ano.substring(2,4));	
				// System.out.println(mes + '/' + ano);

			}

			// passando o mes ano para os parï¿½metros da Query
			for (int i = 1; i < 4; i++) {
				// psMovimentacao.setString(i, mes + '/' + ano.substring(2,4));
				psMovimentacao.setString(i, mes + '/' + ano);
			}
			rsMovimentacao = psMovimentacao.executeQuery();

			// Calculo do tamanho da primeira dimensï¿½o o Array
			listaRetorno = new String[qtde][5];
			int contador = 0;
			// for (int j=0; j<3; j++){
			while (rsMovimentacao.next()) {
//				listaRetorno[contador][0] =  mes +  '/' + ano.substring(2,4) ;
				listaRetorno[contador][0] = mes + '/' + ano;
				listaRetorno[contador][1] = rsMovimentacao.getString("oc");
				listaRetorno[contador][2] = rsMovimentacao.getString("Nome_oc");
				listaRetorno[contador][3] = rsMovimentacao.getString("inclusao");
				listaRetorno[contador][4] = rsMovimentacao.getString("exclusao");
				contador++;
			}
			// }
		} finally {
			if (rsMovimentacao != null) {
				rsMovimentacao.close();
			}
			if (psMovimentacao != null) {
				psMovimentacao.close();
			}
			if (connexao != null) {
				fecharConexao("obterListaMovimentacao");
			}
		}
		return listaRetorno;
	}

	public String[][] obterSaldoAnterior(String mesAnoInicioConsultaSaldoAnterior, String dataAnteriorFormatada,
			String idOC, int qtdLinhasMovimentacao, String query) throws SispagException, SQLException {
//		String[][] listaRetorno = null;
		String[][] listaRetorno = new String[qtdLinhasMovimentacao][1];
		PreparedStatement psSaldoAnterior = null;
		ResultSet rsSaldoAnterior = null;
		// Verificar qual ï¿½ o menor mï¿½s de lancamento

		try {
			obterConexao();
			psSaldoAnterior = connexao.prepareStatement(query);
			psSaldoAnterior.setString(1, mesAnoInicioConsultaSaldoAnterior); // data inicio
			psSaldoAnterior.setString(2, dataAnteriorFormatada.substring(3, 8)); // data de referencia
			psSaldoAnterior.setString(3, idOC); // oc
			rsSaldoAnterior = psSaldoAnterior.executeQuery();
			int contador = 0;
			while (rsSaldoAnterior.next()) {
				listaRetorno[contador][0] = String.valueOf(rsSaldoAnterior.getDouble("SALDOMESANTECESSOR"));
				contador++;
			}
		} finally {
			if (rsSaldoAnterior != null) {
				rsSaldoAnterior.close();
			}
			if (psSaldoAnterior != null) {
				psSaldoAnterior.close();
			}
			if (connexao != null) {
				fecharConexao("obterSaldoAnterior()");
			}

		}
		return listaRetorno;
	}

	public String getMesAnoInicio(String oc) throws SispagException {
		String mesAnoInicio = null;
		final String SQL_MES_ANO_INICIO = "SELECT MIN(SUBSTR(to_char(SRESLANC_DT_LANCAMENTO,'dd/mm/yy'),4,7)) Inic  "
				+ " FROM SRES_LANCAMENTO " + "WHERE sreslanc_origem_doc_aut = ? ";
		PreparedStatement psMesAnoInicio = null;
		ResultSet rsMesAnoInicio = null;
		try {
			obterConexao();
			psMesAnoInicio = connexao.prepareStatement(SQL_MES_ANO_INICIO);
			psMesAnoInicio.setString(1, oc);
			rsMesAnoInicio = psMesAnoInicio.executeQuery();
			while (rsMesAnoInicio.next()) {
				mesAnoInicio = rsMesAnoInicio.getString("Inic");
			}
		} catch (SQLException se) {
			// System.out.println(se.getMessage());
			se.printStackTrace();
		} finally {
			if (rsMesAnoInicio != null) {
				try {
					rsMesAnoInicio.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psMesAnoInicio != null) {
				try {
					psMesAnoInicio.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("getMesAnoInicio()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
		}

		return mesAnoInicio;
	}

	public ArrayList<Divida> listDividasReversao(FiltrarPedidoReversaoForm myForm)
			throws SQLException, SispagException {

		
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection con = null;
		int registros = 0;
		int tipoDivida = Integer.parseInt(myForm.getTipoDivida());
		// int banco = Integer.parseInt(myForm.getBanco());

		
		
		
		try {
			con = ds.getConnection();

			// Montagem da Query SQL de acordo com o num de parametros que vier
			String Query_Stmt = "SELECT SRESPES_NOME, SRESPES_CPF, SRESOM_OM_OC_COD,SRESPES_MATFIN,	SRESPES_MATSIAP, SRESPES_PT, "
					+ " SRESPES_RESP_DT, SRESTPDIV_NOME, SRESMOT_NOME, SRESDIV_ID,SRESDIV_AGE, "
					+ " SRESDIV_BCO_ID, SRESDIV_CCR, SRESDIV_REF_INICIO_MES, SRESDIV_VAL, SRESDIV_CODIGO, "
					+ "	 SRESDIV_REF_INICIO_ANO, SRESDIV_OM_OC_ID, SRESDIV_DT_MOTIVO, SRESOM_NOME, SRESOM_OM_ID, SRESOM_OM_OC_COD, "
					+ "    SRESDIV_DOC_ENVIO, SRESDIV_NUM_DOC_ENVIO, SRESDIV_DT_DOC_ENVIO, "
					+ "	 SRESLANC_ID, SRESLANC_CODIGO, SRESLANC_NUM_DOC_AUT, SRESLANC_TIPO_DOC_AUT, SRESPES_SITUACAO, "
					+ "	 SRESLANC_DT_DOC_AUT, SRESLANC_ORIGEM_DOC_AUT, SRESLANC_OPERADOR, SRESLANC_VALOR, "
					+ "	 SRESLANC_OBS, SRESLANC_RESP_ALT, SRESLANC_RESP_DT, SRESLANC_DIV_ID, SRESLANC_TPLANC_ID ,SRESDIV_REF_TERMINO_MES,SRESDIV_REF_TERMINO_ANO "
					+ "	 FROM SRES_DIVIDA,SRES_PESSOA_RR,SRES_LANCAMENTO,SRES_TIPO_DIVIDA,SRES_MOTIVO,SRES_ORGANIZACAO_MILITAR "
					+ "	 WHERE SRESDIV_PES_ID = SRESPES_ID AND SRESLANC_DIV_ID = SRESDIV_ID AND SRESDIV_MOT_ID = SRESMOT_ID "
					+ "	 AND SRESTPDIV_ID = SRESDIV_TPDIV_ID AND SRESDIV_OM_OC_ID = SRESOM_OM_ID AND SRESLANC_CODIGO = 1 "
					+ "    AND SRESDIV_PED_ID IS NULL AND SRESDIV_PED_ID IS NULL AND SRESDIV_ESTADO <> 'EM ESPERA'"
					+ "    AND SRESTPDIV_COD = ? AND SRESDIV_BCO_ID = ?  order by SRESPES_NOME ";

			// System.out.println(Query_Stmt);
			// System.out.println(tipoDivida);
			// System.out.println(myForm.getBanco());

			// Configuraï¿½ï¿½o do DAO e Montagem da Lista de dívidas para reversao

			// Cria SQL e declara
			stmt = con.prepareStatement(Query_Stmt);
			stmt.setInt(1, tipoDivida);

			stmt.setInt(2, new Integer(myForm.getBanco()));

			// Configuraï¿½ï¿½o do DAO e Montagem da Lista de dívidas
			results = stmt.executeQuery();

			ArrayList<Divida> dividas = new ArrayList<Divida>();

			// System.out.println("Vou entrar no while do interger");
			// Tratar recordset com objetos recuperados
			while (results.next()) {

				// System.out.println("entrei no while");

				Divida div = new Divida();
				// System.out.println("criei a dívida");
				div.setId(results.getInt("SRESDIV_ID"));
				// System.out.println("DAO - SRESDIV_ID");
				div.setAgencia(results.getString("SRESDIV_AGE"));
				// System.out.println("DAO - SRESDIV_AGE");
				div.setBanco(results.getString("SRESDIV_BCO_ID"));
				// System.out.println("DAO - SRESDIV_BCO_ID");
				div.setContaCorrente(results.getString("SRESDIV_CCR"));
				// System.out.println("DAO - SRESDIV_CCR");
				div.setAnoProcessoGeracao(results.getString("SRESDIV_REF_INICIO_ANO"));
				// System.out.println("DAO - SRESDIV_REF_INICIO_ANO");
				div.setMesProcessoGeracao(results.getString("SRESDIV_REF_INICIO_MES"));
				// System.out.println("DAO - SRESDIV_REF_INICIO_MES");
				div.setTipo(results.getString("SRESTPDIV_NOME"));

				div.setCodigo(results.getInt("SRESDIV_CODIGO"));
				// System.out.println("DAO - SRESDIV_CODIGO");

				div.setMesTermino(results.getString("SRESDIV_REF_TERMINO_MES"));
				div.setAnoTermino(results.getString("SRESDIV_REF_TERMINO_ANO"));

				// coloco o valor a regularizar
				Double valor = (double) verificaSaldoRegularizar(results.getInt("SRESDIV_ID"));
				div.setValor(valor);
				// System.out.println("DAO - SRESDIV_valor");

				div.setMotivo(results.getString("SRESMOT_NOME"));
				// System.out.println("DAO - SRESDIV_MOTIVONOME");
				div.setDataMotivo(results.getDate("SRESDIV_DT_MOTIVO"));
				// System.out.println("DAO - SRESDIV_MOTIVODATA");

// queiroz
				div.setDocEnvio(results.getString("SRESDIV_DOC_ENVIO"));
				div.setNumeroDocEnvio(results.getString("SRESDIV_NUM_DOC_ENVIO"));
				div.setDataDocEnvio(results.getDate("SRESDIV_DT_DOC_ENVIO"));

				// instanciar objeto OC e preencher
				OC oc = new OC();
				oc.setId(results.getInt("SRESDIV_OM_OC_ID"));
				oc.setNome(results.getString("SRESOM_NOME"));
				oc.setOc(results.getString("SRESOM_OM_OC_COD"));
				div.setOc(oc);

				// instanciar objeto Pessoa e preencher
				Pessoa pes = new Pessoa();
				pes.setNome(results.getString("SRESPES_NOME"));
				pes.setCpf(results.getString("SRESPES_CPF"));
				pes.setPosto(results.getString("SRESPES_PT"));
				pes.setSituacao(results.getString("SRESPES_SITUACAO"));
				pes.setMatFin(results.getString("SRESPES_MATFIN"));
				pes.setMatSIAPE(results.getString("SRESPES_MATSIAP"));
				div.setPessoa(pes);
				// System.out.println("DAO - PASSEI NA PESSOA");

				// instanciar objeto Lançamento e preencher lanï¿½amento inicial
				// (cï¿½gigo = 1)
				ArrayList<Lancamento> lancamento = new ArrayList<Lancamento>();

				Lancamento lanc = new Lancamento();

				lanc.setIdLancamento(results.getInt("SRESLANC_ID"));

				// System.out.println(results.getInt("SRESLANC_ID"));
				lanc.setCodigo(results.getInt("SRESLANC_CODIGO"));
				// System.out.println("DAO - COMECEI A CONSTRUIR A LANÇAMENTO");
				lanc.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lanc.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));
				lanc.setDataDocAutorizacao(results.getDate("SRESLANC_DT_DOC_AUT"));
				lanc.setOrigemDocAutorizacao(results.getString("SRESLANC_ORIGEM_DOC_AUT"));
				lanc.setOperador(results.getString("SRESLANC_OPERADOR"));
				lanc.setValor(results.getDouble("SRESLANC_VALOR"));
				// System.out.println("DAO - VALOR DO LANÇAMENTO");
				lanc.setObservacao(results.getString("SRESLANC_OBS"));
				// System.out.println("DAO - obs DO LANÇAMENTO");
				lanc.setTipo(results.getString("SRESLANC_TPLANC_ID"));
				// lancamento.add(registros,lanc);
				// System.out.println("DAO - ADICIONEI ATRIBUTOS AO LANÇAMENTO");
				div.setLancamento(lanc);
				// System.out.println("DAO - ADICIONEI LANÇAMENTO ï¿½ Dï¿½VIDA");
				// System.out.println("Passou no lancamento!!!!");

				dividas.add(registros, div);

				// System.out.println("Inseridos" + registros);
				registros++;

			} // fim do while que monta a coleï¿½ï¿½o de dívidas

			// System.out.println("Sai do while");

				return dividas;
			
		/*} catch (Exception e) {
			logger.error(se.getMessage(), se);
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());
		*/} finally {
			DButils.closeQuietly(results,stmt, con);
		} // fim do bloco try-catch-finally
		
	}

	public Boolean gerarPedidoReversao(GerarPedidoReversaoForm myForm, String codUsuario) throws SispagException {
		// TODO Auto-generated method stub

		PreparedStatement ps = null, stmt = null;
		ResultSet rs = null, result = null;
		boolean transaction = false;
		boolean resultado = false;
		int idPedido = 0, codigo = 0;
		Connection conn = null;

		String ano = null;

		String codTipo = myForm.getCodTipo();
		String banco = myForm.getBanco();
		String[] listaDeDividas = myForm.getListaDeDividas();

		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			// pego a id do pedido atravï¿½s da sequence
			final String RETRIEVE_SEQUENCE_PED = "SELECT SQ_SRES_PED_REVERSAO.NEXTVAL NEXTVAL, "
					+ " TO_CHAR(SYSDATE,'YYYY') ANO, NVL((SELECT MAX(TO_NUMBER(SRESPED_COD))+1 COD "
					+ " FROM SRES_PEDIDO WHERE SRESPED_ANO =  TO_CHAR(SYSDATE,'YYYY')),0) SRESPED_COD,"
					+ " TO_CHAR(SYSDATE,'DD/MM/YYYY') DATA_PEDIDO FROM DUAL";

			// Gera a Sequence de Dívida

			// Cria sql
			stmt = conn.prepareStatement(RETRIEVE_SEQUENCE_PED);

			// Executa sql
			result = stmt.executeQuery();
			if (result.next()) {
				idPedido = result.getInt("NEXTVAL");
				ano = result.getString("ANO");
				codigo = result.getInt("SRESPED_COD");
			}

			// Crio query de inclusï¿½o no banco referente ao pedido de refersï¿½o
			final String RETRIEVE_GERA_PEDIDO = "INSERT INTO SRES_PEDIDO (SRESPED_ID, SRESPED_COD, SRESPED_ANO, SRESPED_TIPO, SRESPED_BCO_ID, SRESPED_RESP_ALT, SRESPED_RESP_DT ) "
					+ " VALUES (?,?,?,?,?,?,SYSDATE)";

			// Cria sql
			ps = conn.prepareStatement(RETRIEVE_GERA_PEDIDO);

			// System.out.println(RETRIEVE_GERA_PEDIDO);

			ps.setInt(1, idPedido);
			ps.setInt(2, codigo);
			// System.out.println(idPedido);
			ps.setString(3, ano);
			// System.out.println(ano);
			ps.setString(4, codTipo);
			// System.out.println(codTipo);
			ps.setString(5, banco);
			// System.out.println(banco);
			ps.setString(6, codUsuario);
			// System.out.println(codUsuario);

			ps.execute();
			resultado = true;

			// System.out.println("Resultado: "+resultado);

			transaction = updateDividaReversao(idPedido, ano, listaDeDividas, conn);
			// System.out.println(transaction);

			if (resultado && transaction) {
					conn.commit();								
			}else {
				conn.rollback();
			}
			conn.setAutoCommit(true);

		} catch (Exception se) {
			logger.error(se.getMessage(),se);
			throw new SispagException("Erro ao gerar pedido de reversão");
		} finally {			
			DButils.rollbackQuietly(conn);
			DButils.setAutoCommitQuietly(conn, true);
			DButils.closeQuietly(stmt);
			DButils.closeQuietly(result);
			DButils.closeQuietly(ps);
			DButils.closeQuietly(rs);
			DButils.closeQuietly(conn);
		}

		return resultado;
	}

	private boolean updateDividaReversao(int idPedido, String ano, String[] listaDeDividas, Connection conexao)
			throws SispagException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean resultado = false;

		// System.out.println("updateDividaReversao");

		// System.out.println(listaDeDividas.length);

		try {
			// obterConexao();
			for (int i = 0; i < listaDeDividas.length; i++) {

				// pego a id do pedido atravï¿½s da sequence
				final String RETRIEVE_UPDATE_DIVIDA_REVERSAO = " UPDATE SRES_DIVIDA SET SRESDIV_PED_ID = ? , SRESDIV_PED_ANO = ? "
						+ " WHERE SRESDIV_ID = ? ";

				// System.out.println();

				ps = conexao.prepareStatement(RETRIEVE_UPDATE_DIVIDA_REVERSAO);

				// System.out.println(RETRIEVE_UPDATE_DIVIDA_REVERSAO);

				ps.setInt(1, idPedido);
				// System.out.println(idPedido);
				ps.setString(2, ano);
				// System.out.println(ano);
				ps.setString(3, listaDeDividas[i]);
				// System.out.println(listaDeDividas[i]);

				ps.execute();
				resultado = true;
				// System.out.println("Resultado: "+resultado);
				
			}
			
		} catch (Exception se) {
			throw new SispagException("Erro alterando Divida");
		} finally {
			DButils.closeQuietly(ps);
			DButils.closeQuietly(rs);
		}
		return resultado;
	}

	@SuppressWarnings("finally")
	private int contRegistro(String[] parametros, String sql, Connection conexao) throws SispagException {
		int qtd = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(sql);

			for (int i = 0; i < parametros.length; i++) {
				ps.setString(i + 1, parametros[i]);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				qtd++;
			}

		} catch (SQLException se) {
			// System.out.println("Erro ao contar registros"+se.getMessage());
			throw new SispagException("Erro ao contar registros.");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * if(conn != null) { try { fecharConexao("contRegistro()"); } catch
			 * (SQLException e) { //System.out.println("Erro ao fechar conexão com o BD");
			 * throw new SispagException("Erro ao conectar ao BD."); } }
			 */
			return qtd;
		}

	}

	public String[][] getlistPedidoReversao() throws SispagException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		int idPedido = 0;
		String ano = null;
		String[][] listPedidoReversao = null;

		// pego a id do pedido atravï¿½s da sequence
		final String RETRIEVE_SEQUENCE_PED = "  SELECT SRESPED_ID, SRESPED_ANO " + "  FROM SRES_PEDIDO "
				+ "  WHERE SRESPED_COD = TO_CHAR((SELECT MAX(TO_NUMBER(SRESPED_COD)) "
				+ "                             FROM SRES_PEDIDO WHERE SRESPED_ANO = TO_CHAR(SYSDATE,'YYYY'))) "
				+ "      AND SRESPED_ANO = TO_CHAR(SYSDATE,'YYYY')";

		// Gera a Sequence de Dívida
		try {
			obterConexao();
			// Cria sql
			stmt = connexao.prepareStatement(RETRIEVE_SEQUENCE_PED);

			// Executa sql
			result = stmt.executeQuery();
			if (result.next()) {
				idPedido = result.getInt("SRESPED_ID");
				ano = result.getString("SRESPED_ANO");
			}

		} catch (Exception se) {
			se.printStackTrace();
			throw new SispagException("Erro alterando dívida");
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (result != null) {
				try {
					result.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				if (connexao != null) {
					fecharConexao("getlistPedidoReversao()");
				}
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar conexão com o BD");
				throw new SispagException("Erro ao conectar ao BD.");
			}
		}

		try {
			listPedidoReversao = recuperarListaDividasPedidoReversao(idPedido, ano);
		} catch (SQLException e) {
			// System.out.println("Erro ao recuperar a lista de dívidas.");
			e.getMessage();
			e.printStackTrace();
			throw new SispagException("Erro ao efetuar conexão com o banco de dados.");

		} catch (SispagException e) {
			// System.out.println("Erro ao recuperar a lista de dívidas.");
			throw new SispagException("Erro ao recuperar a lista de dívidas.");
		}

		return listPedidoReversao;
	}

	/**
	 * @author luis.fernando Metodo destinado a recuperar o ID do banco referente ao
	 *         cï¿½dido do pedido de reversao.
	 * @param codigoPedidoReversao
	 * @param ano
	 * @return Id do banco atinente ao cï¿½digo do pedido de reversão.
	 * @throws SispagException
	 */

	public Integer obterIdPedidoReversao(Integer codigoPedidoReversao, Integer ano) throws SispagException {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Integer retorno = null;
		try {
			obterConexao();
			pstm = connexao.prepareStatement(SQL_OBTER_ID_PEDIDO_REVERSAO);
			pstm.setString(1, codigoPedidoReversao + "");
			pstm.setInt(2, ano);

			rs = pstm.executeQuery();
			while (rs.next()) {
				retorno = rs.getInt("SRESPED_ID");
				System.out.println(retorno);
			}

			return retorno;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SispagException("Erro ao recuperar a id do cï¿½digo do pedido de reversão.");
		} catch (SispagException e) {
			// System.out.println("Erro ao recuperar a id do cï¿½digo do pedido de
			// reversão.");
			e.printStackTrace();
			throw new SispagException("Erro ao recuperar a id do cï¿½digo do pedido de reversão.");
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				if (connexao != null) {
					fecharConexao("obterIdPedidoReversao()");
				}
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar conexão com o BD");
				throw new SispagException("Erro ao conectar ao BD.");
			}
		}
	}

	/**
	 * @author luis.fernando Método responsï¿½vel por recuperar as dividas que
	 *         compï¿½em um pedido de reversão.
	 * @param idPedido - Identificador do pedido de reversão (não ï¿½ o cï¿½digo,
	 *                 mas sim o ID)
	 * @param ano      - Ano em que no pedido foi gerado.
	 * @throws SispagException - Exceï¿½ï¿½o que serï¿½ relanï¿½ada quando surgir
	 *                         uma exceï¿½ï¿½o oriunda do obterConexao
	 * @throws SQLException
	 */
	public String[][] recuperarListaDividasPedidoReversao(int idPedido, String ano)
			throws SispagException, SQLException {
		String[] param = new String[2];

		param[0] = Integer.toString(idPedido);
		param[1] = ano;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[][] listPedidoReversao = null;

		// Crio query de inclusï¿½o no banco referente ao pedido de reversão
		final String RETRIEVE_GERAR_REL_REVERSAO = " SELECT SRESDIV_ID, SRESDIV_AGE, SRESDIV_CCR, SRESDIV_BCO_ID, SRESBCO_NOME, SRESPED_COD, SRESPED_ANO, SRESPED_TIPO, "
				+ " SRESPED_RESP_ALT, TO_CHAR(SRESPED_RESP_DT,'DD/MM/YYYY') AS SRESPED_RESP_DT , "
				+ " SRESPES_MATFIN, SRESPES_MATSIAP, UPPER(SRESPES_NOME) as SRESPES_NOME, SRESPES_CPF, "
				+ " SRESDIV_REF_INICIO_MES, SRESDIV_REF_INICIO_ANO,"
				+ " SRESDIV_REF_TERMINO_MES, SRESDIV_REF_TERMINO_ANO "
				+ " FROM SRES_PEDIDO, SRES_DIVIDA, SRES_BANCO , SRES_PESSOA_RR "
				+ " WHERE SRESDIV_BCO_ID = SRESBCO_ID  "
				+ " AND SRESDIV_PED_ID = SRESPED_ID AND SRESDIV_PED_ANO = SRESDIV_PED_ANO "
				+ " AND SRESDIV_PES_ID = SRESPES_ID " + " AND SRESDIV_PED_ID = ? AND SRESDIV_PED_ANO = ? "
				+ "ORDER BY SRESPES_NOME";

		try {
			obterConexao();

			// Cria sql
			ps = connexao.prepareStatement(RETRIEVE_GERAR_REL_REVERSAO);

			// System.out.println(RETRIEVE_GERAR_REL_REVERSAO);

			ps.setInt(1, idPedido);
			// System.out.println(idPedido);
			ps.setString(2, ano);
			// System.out.println(ano);

			rs = ps.executeQuery();

			// int qtdLinhas = contRegistro(param, RETRIEVE_GERAR_REL_REVERSAO,"teste");
			int qtdLinhas = contRegistro(param, RETRIEVE_GERAR_REL_REVERSAO, connexao);
			listPedidoReversao = new String[qtdLinhas][19];
			// Percorre cada linha do resultado para alimentar o relatorio
			while (rs.next()) {

				String valor = Double.toString(verificaSaldoRegularizar(rs.getInt("SRESDIV_ID")));

				listPedidoReversao[rs.getRow() - 1][0] = rs.getString("SRESDIV_ID");
				listPedidoReversao[rs.getRow() - 1][1] = rs.getString("SRESDIV_AGE");
				listPedidoReversao[rs.getRow() - 1][2] = rs.getString("SRESDIV_CCR");
				listPedidoReversao[rs.getRow() - 1][3] = rs.getString("SRESDIV_BCO_ID");
				listPedidoReversao[rs.getRow() - 1][4] = rs.getString("SRESBCO_NOME");
				listPedidoReversao[rs.getRow() - 1][5] = rs.getString("SRESPED_COD");
				listPedidoReversao[rs.getRow() - 1][6] = rs.getString("SRESPED_ANO");
				listPedidoReversao[rs.getRow() - 1][7] = rs.getString("SRESPED_TIPO");
				listPedidoReversao[rs.getRow() - 1][8] = rs.getString("SRESPED_RESP_ALT");
				listPedidoReversao[rs.getRow() - 1][9] = rs.getString("SRESPED_RESP_DT");
				listPedidoReversao[rs.getRow() - 1][10] = valor;
				listPedidoReversao[rs.getRow() - 1][11] = rs.getString("SRESPES_MATFIN");
				listPedidoReversao[rs.getRow() - 1][12] = rs.getString("SRESPES_NOME");
				listPedidoReversao[rs.getRow() - 1][13] = rs.getString("SRESPES_CPF");

				listPedidoReversao[rs.getRow() - 1][14] = rs.getString("SRESDIV_REF_INICIO_MES");
				listPedidoReversao[rs.getRow() - 1][15] = rs.getString("SRESDIV_REF_INICIO_ANO");
				listPedidoReversao[rs.getRow() - 1][16] = rs.getString("SRESDIV_REF_TERMINO_MES");
				listPedidoReversao[rs.getRow() - 1][17] = rs.getString("SRESDIV_REF_TERMINO_ANO");
				listPedidoReversao[rs.getRow() - 1][18] = rs.getString("SRESPES_MATSIAP");

			}
		} catch (SispagException e) {
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				if (connexao != null) {
					fecharConexao("getlistPedidoReversao()");
				}
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar conexão com o BD");
				throw new SQLException("Erro ao fechar conexão com o BD");
			}

		}

		return listPedidoReversao;
	}

	public ArrayList<OC> getAllOc() throws SispagException {
		// TODO Auto-generated method stub
		List<OC> ocList = new ArrayList<OC>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			obterConexao();
			ps = connexao.prepareStatement(
					"SELECT DISTINCT(SRESOM_OM_OC_COD) OC, SRESOM_NOME AS NOME FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_OC_COD = SRESOM_OM_OM_COD ORDER BY SRESOM_OM_OC_COD");
			rs = ps.executeQuery();
			while (rs.next()) {
				OC oc = new OC(rs.getString("OC"), rs.getString("NOME"));
				ocList.add(oc);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DButils.closeQuietly(rs, ps, connexao);
		}
		return (ArrayList<OC>) ocList;
	}

	public ArrayList<Banco> getAllBanco() throws SispagException {
		// TODO Auto-generated method stub
		List<Banco> bancoList = new ArrayList<Banco>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		// System.out.println("entrou no getAllBanco");
		try {
			obterConexao();
			ps = connexao.prepareStatement(
					"SELECT SRESBCO_ID ID, SRESBCO_COD COD, SRESBCO_NOME NOME FROM SRES_BANCO ORDER BY SRESBCO_COD");
			// System.out.println("vai executar a query");
			rs = ps.executeQuery();
			// System.out.println("executou a query");
			while (rs.next()) {
				Banco banco = new Banco(rs.getInt("ID"), rs.getString("COD"), rs.getString("NOME"));
				bancoList.add(banco);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DButils.closeQuietly(rs, ps, connexao);
		}
		// System.out.println("retornou o listbanco");
		return (ArrayList<Banco>) bancoList;
	}

	public boolean readDividaHist(String matFin, int idOc, String mes, String ano) throws SispagException {
		// TODO Auto-generated method stub

		final String RETRIEVE_READDIVIDA = "SELECT COUNT(*) QTDE FROM SRES_DIVIDA, SRES_PESSOA_RR "
				+ " WHERE SRESDIV_PES_ID = SRESPES_ID AND SRESDIV_PES_ID = (SELECT SRESPES_ID FROM SRES_PESSOA_RR WHERE SRESPES_MATFIN=?) "
				+ " AND SRESDIV_OM_OC_ID = ? " + " AND SRESDIV_REF_INICIO_MES =? AND SRESDIV_REF_INICIO_ANO =? ";

		PreparedStatement stmt = null;
		Divida div = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			// Cria sql
			stmt = conn.prepareStatement(RETRIEVE_READDIVIDA);

			stmt.setString(1, matFin);
			stmt.setInt(2, idOc);
			stmt.setString(3, mes);
			stmt.setString(4, ano);

			// System.out.println(RETRIEVE_READDIVIDA);
			// System.out.println(idOc);
			// System.out.println(mes);
			// System.out.println(ano);

			// Executa sql
			rs = stmt.executeQuery();

			int qtde = 0;

			while (rs.next()) {
				// System.out.println("Qtde: "+rs.getInt("QTDE"));
				qtde = rs.getInt("QTDE");

			}

			if (qtde == 0) {
				// System.out.println("Divida não encontrada!");
				return false;
			} else {

				// System.out.println("Divida encontrada!");
				return true;

			}
		} catch (Exception e) {
			// System.out.println(e);
			return false;
		} finally {
			DButils.closeQuietly(rs, stmt, conn);
		}

	}

	public int buscarEstagioCGS() throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		int estagioCGS = 0;

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_BUSCA_ESTAGIO_CGS);

			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {
				estagioCGS = results.getInt("ESTAGIO");

			}
			// System.out.println("Estagio CGS" +estagioCGS);
		} catch (SQLException e) {
			throw new SispagException("Erro ao buscar estagio CGS");

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("buscarEstagioCGS()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

			return estagioCGS;
		} // end do finally
	}// end da procedure buscarEstagioCGS

	public int[] verificaExisteResparegSISPAG() throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		int[] resultado = new int[2];

		try {
			obterConexao();
			stmt = connexao.prepareStatement(SQL_BUSCA_RESPAREG_SISPAG);
			// System.out.println(SQL_BUSCA_RESPAREG_SISPAG);
			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {
				resultado[0] = results.getInt("qtdRespareg");
				resultado[1] = results.getInt("qtdRegaresp");

			}

		} catch (SQLException e) {
			// System.out.println(e);

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("verificaExisteResparegSISPAG()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}
			return resultado;
		} // end do finally

	}// end verificaExisteResparegSISPAG

	public boolean verificaExisteResparegSISRES() throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection conn=null;
		boolean respareg = false;

		try {
			conn = ds.getConnection();
			stmt = conn.prepareStatement(SQL_BUSCA_RESPAREG_SISRES);
			// System.out.println(SQL_BUSCA_RESPAREG_SISRES);
			// Executa sql
			results = stmt.executeQuery();

			if (results.next()) {

				if (results.getInt("TOTAL") > 0) {
					respareg = true;
				}

			}			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		} finally {
			DButils.closeQuietly(results, stmt, conn);			
		} // end do finally
		return respareg;
	}// end verificaExisteResparegSISRES

	public boolean insereRespareg(int qtdRespareg, int qtdRegaresp) throws SispagException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		boolean respareg = false;

		try {
			obterConexao();
			// INSERIR TRANSAï¿½ï¿½O
			connexao.setAutoCommit(false);
			stmt = connexao.prepareStatement(SQL_INSERIR_PESSOA_RESPAREG);
			// System.out.println(SQL_INSERIR_PESSOA_RESPAREG);
			int retornoUpdate = stmt.executeUpdate();

			/*
			 * Existe caso não necessitar incluir pessoa if(retornoUpdate == 0){
			 * 
			 * return false; }
			 */
			// System.out.println("pESSOA:" +retornoUpdate);
			stmt = connexao.prepareStatement(SQL_INSERIR_LANCAMENTO_RESPAREG);
			retornoUpdate = stmt.executeUpdate();
			// Verifica se a MESMA quantidade de registros retornadas do tipo RESPAREG do
			// SISPAG foi incluï¿½da no SISRES
			if ((qtdRespareg == retornoUpdate) || (retornoUpdate > 0)) {

				respareg = true;
			}
			// System.out.println("Respareg:" +retornoUpdate);
			// System.out.println(SQL_INSERIR_LANCAMENTO_REGARESP);
			stmt = connexao.prepareStatement(SQL_INSERIR_LANCAMENTO_REGARESP);
			retornoUpdate = stmt.executeUpdate();
			// System.out.println("REgaresp: " +retornoUpdate);
			// Verifica se a MESMA quantidade de registros retornadas do tipo REGARESP do
			// SISPAG foi incluï¿½da no SISRES
			if ((qtdRespareg == retornoUpdate) || (retornoUpdate > 0)) {

				respareg = true;
			}

		} catch (SQLException e) {
			// System.out.println(e);

		} finally {

			
			if (stmt != null) {
				try {
					if (respareg) {
						connexao.commit();
					} else {
						connexao.rollback();
					}
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connexao != null) {
				try {
					fecharConexao("insereRespareg()");
				} catch (SQLException e) {
					// System.out.println("Erro ao fechar conexão com o BD");
					throw new SispagException("Erro ao conectar ao BD.");
				}
			}

			return respareg;
		} // end do finally

	}// end verificaExisteRespareg

}// Fim do DAODivida

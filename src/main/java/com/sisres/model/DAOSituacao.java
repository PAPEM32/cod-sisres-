package com.sisres.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * Classe que objetiva retornar a situação da Pessoa no SISPAG
 * 
 * ID COD NOME_SITUACAO 1 A MILITAR ATIVA 2 I MILITAR INATIVO 3 N CIVIL
 * ESTATUTARIO 4 P CIVIL PENSIONISTA 5 Q CIVIL ESTATUTARIO - PAIS 6 T CIVIL
 * AUXILIAR LOCAL 7 C CIVIL AUXILIAR LOCAL - ESTATUTARIO - EXTERIOR
 *
 */
public class DAOSituacao {
	private DataSource ds;
	final String SQL_OBTER_SITUACOES = "SELECT * FROM SRES_SITUACAO_SPAG";

	public DAOSituacao() {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:/jdbc/sisres2"); //sispag2 //sisres2
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<Situacao> obterListaSituacoes() throws SispagException, SQLException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		List<Situacao> listaRetornoSituacoes = new ArrayList<Situacao>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			stmt = conn.prepareStatement(SQL_OBTER_SITUACOES);
			results = stmt.executeQuery();
			while (results.next()) {
				Situacao situacao = new Situacao();
				situacao.setId(results.getInt("SRESSIT_ID"));
				situacao.setCodigo(results.getString("SRESSIT_COD"));
				situacao.setNome(results.getString("SRESSIT_NOME"));
				listaRetornoSituacoes.add(situacao);
			}
		} finally {
			if (stmt != null)
				stmt.close();
			if (results != null) {
				results.close();
			}
			if (conn != null)
				conn.close();
		}
		return listaRetornoSituacoes;
	}

}

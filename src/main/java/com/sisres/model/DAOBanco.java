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
 * Classe com objetivo de retornar os dados dos bancos cadastrados no SISRES
 * 
 * ID COD NOME_BANCO 1 000 DFM - REGULARIZACAO 2 001 BRASIL 3 003 AMAZONIA 4 008
 * MERIDIONAL 5 020 PRODUBAN
 *
 */
public class DAOBanco {
	private DataSource ds;

	final String SQL_OBTER_BANCOS = "SELECT * FROM SRES_BANCO ORDER BY SRESBCO_ID";

	public DAOBanco() {
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:/jdbc/sisres2"); //sispag2 //sisres2
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<Banco> obterListaBancos() throws SispagException, SQLException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		List<Banco> listaRetornoBancos = new ArrayList<Banco>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			stmt = conn.prepareStatement(SQL_OBTER_BANCOS);
			results = stmt.executeQuery();
			while (results.next()) {
				Banco banco = new Banco();
				banco.setId(results.getInt("SRESBCO_ID"));
				banco.setCodigo(results.getString("SRESBCO_COD"));
				banco.setNome(results.getString("SRESBCO_NOME"));
				listaRetornoBancos.add(banco);
			}
		} finally {
			if (stmt != null)
				stmt.close();
			if (results != null)
				results.close();
			if (conn != null)
				conn.close();
		}

		return listaRetornoBancos;
	}

}

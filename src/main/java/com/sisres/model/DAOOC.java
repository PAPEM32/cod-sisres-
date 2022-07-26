

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
 * Classe que objetiva retornar as informações da OC no SISRES
 *
 * ID OC OM NOME_OM OM_BP 2 002 002 BASE NAVAL - RIO DE JANEIRO 91800 4 004 004
 * DIRETORIA DE ABASTECIMENTO DA MARINHA 71000 6 006 006 SERVICO DE ASSISTENCIA
 * SOCIAL DA MARINHA 67100 7 007 007 NAVIO AERODROMO - SAO PAULO 91603
 *
 */
public class DAOOC {
	// TODO p32 Copiar modelos do DAO Situacao para conections
	Connection conexao;
	DataSource ds;

	public DAOOC() throws SispagException {
		this.obterConexao();
	}

	public OC getOC(String codOC) throws SispagException {
		OC oc = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(
					"SELECT * FROM SRES_ORGANIZACAO_MILITAR WHERE SRESOM_OM_OC_COD = SRESOM_OM_OM_COD AND SRESOM_OM_OC_COD = '"
							+ codOC + "' ");
			rs = ps.executeQuery();
			while (rs.next()) {
				oc = new OC(rs.getInt("SRESOM_OM_ID"), rs.getString("SRESOM_OM_OC_COD"), rs.getString("SRESOM_NOME"),
						rs.getString("SRESOM_OM_BP"));
			}
		} catch (SQLException se) {
			se.printStackTrace();

		} finally {

			try {
				rs.close();
				ps.close();
				conexao.close();
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar as conexões com o banco de dados em
				// DAOOC.java "+e.getMessage());
				throw new SispagException("Erro ao fechar as conexões com o banco de dados");
				// e.printStackTrace();
			}
			/*
			 * if(rs != null) { try { rs.close(); } catch (Exception e) {
			 * e.printStackTrace(); } } if(ps != null) { try { ps.close(); } catch
			 * (Exception e) { e.printStackTrace(); } } if(conexao != null){ try
			 * {conexao.close(); } catch(Exception e) { e.printStackTrace(); } }
			 */
		}
		return oc;

	}

	private void obterConexao() throws SispagException {
		Context ctx = null;
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {

			// System.out.println("Erro ao localizar o recurso JNDI: java:jdbc/Sisres");
			throw new SispagException("Erro ao efetuar conexão com o banco de dados");
		}
		if (ctx == null) {
			throw new RuntimeException("JNDI não pode ser encontrado.");
		}
		try {
			// ds = (DataSource)ctx.lookup("java:jdbc/Sisres");
			ds = (DataSource) ctx.lookup("java:/jdbc/sisres2"); //sispag2 //sisres2
		} catch (NamingException e) {
			// System.out.println("Erro ao localizar o recurso JNDI: java:jdbc/Sisres");
			throw new SispagException("Erro ao efetuar conexão com o banco de dados");
		}
		if (ds == null) {
			throw new RuntimeException("DataSource não pode ser encontrado");
		}
		try {
			conexao = ds.getConnection();
		} catch (SQLException e) {
			// System.out.println("Erro ao localizar o recurso JNDI: java:jdbc/Sisres:
			// "+e.getMessage());
			throw new SispagException("Erro ao efetuar conexão com o banco de dados");
		}

	}

	public List<OC> getAllOC() throws SispagException {
		List<OC> ocList = new ArrayList<OC>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(
					"SELECT * FROM SRES_ORGANIZACAO_MILITAR where SRESOM_OM_OC_COD = SRESOM_OM_OM_COD ORDER BY SRESOM_OM_OC_COD ");
			rs = ps.executeQuery();
			while (rs.next()) {
				OC oc = new OC(rs.getInt("SRESOM_OM_ID"), rs.getString("SRESOM_OM_OC_COD"), rs.getString("SRESOM_NOME"),
						rs.getString("SRESOM_OM_BP"));

				ocList.add(oc);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				conexao.close();
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar as conexões com o banco de dados em
				// DAOOC.java "+e.getMessage());
				throw new SispagException("Erro ao fechar as conexões com o banco de dados");
				// e.printStackTrace();
			}
			/*
			 * if(rs != null) { try { rs.close(); } catch (Exception e) {
			 * e.printStackTrace(); } } if(ps != null) { try { ps.close(); } catch
			 * (Exception e) { e.printStackTrace(); } } if(conexao != null){ try
			 * {conexao.close(); } catch(Exception e) { e.printStackTrace(); } }
			 */
		}
		return ocList;

	}

}
package com.sisres.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * Classe que objetiva retornar os lancamentos realizados de acordo com o ID da
 * Divida no SISRES
 *
 */
public class DAOLancamento {

	public DAOLancamento() {

		this.obterConexao();
	}

	// TODO p32 Copiar modelos do DAO Situacao para conections
	Connection conexao;
	DataSource ds;

	private void obterConexao() {
		Context ctx = null;
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {

			e.printStackTrace();
		}
		if (ctx == null) {
			throw new RuntimeException("JNDI não pode ser encontrado.");
		}
		try {
			
			ds = (DataSource) ctx.lookup("java:/jdbc/sisres2"); //sispag2 //sisres2
		} catch (NamingException e) {

			e.printStackTrace();
		}
		if (ds == null) {
			throw new RuntimeException("DataSource não pode ser encontrado");
		}
		try {
			conexao = ds.getConnection();
		} catch (SQLException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}

	}

	public ArrayList<Lancamento> readLancamentosporDivida(String dividaID)
			throws SQLException, NamingException, SispagException {

		// Retorna um ArrayList de lançamentos para a dívida cujo DividaID é passado
		// como parametro
		// Caso não encontre nenhum lançamento retorna null

		// Variáveis JDBC

		PreparedStatement stmt = null;
		ResultSet results = null;

		ArrayList<Lancamento> lancamentos = new ArrayList<Lancamento>();
		int registros = 0;

		try {

			// Montagem da Query SQL de acordo com o Código da dívida
			String Query_Stmt = "SELECT * FROM SRES_LANCAMENTO WHERE SRESLANC_DIV_ID = ?";

			stmt = conexao.prepareStatement(Query_Stmt);
			stmt.setString(1, dividaID);

			// Executa sql
			results = stmt.executeQuery();

			while (results.next()) {
				// System.out.println("Existem lançamentos já cadastrados para esta dívida!");

				// Cria e instancia um objeto Lancamento
				Lancamento lanc = new Lancamento();
				lanc.setCodigo(results.getInt("SRESLANC_CODIGO"));
//	          lanc.setNumeroDocAutorizacao(results.getInt("SRESLANC_NUM_DOC_AUT"));
				lanc.setNumeroDocAutorizacao(results.getString("SRESLANC_NUM_DOC_AUT"));
				lanc.setTipoDocAutorizacao(results.getString("SRESLANC_TIPO_DOC_AUT"));
				lanc.setDataDocAutorizacao(results.getDate("SRESLANC_DT_DOC_AUT"));
				lanc.setOrigemDocAutorizacao(results.getString("SRESLANC_ORIGEM_DOC_AUT"));
				lanc.setOperador(results.getString("SRESLANC_OPERADOR"));
				lanc.setValor(results.getDouble("SRESLANC_VALOR"));
				lanc.setObservacao(results.getString("SRESLANC_OBS"));
				lanc.setTipo(results.getString("SRESLANC_TPLANC_ID"));
				// Vetor de lancamentos recebe mais um objeto lancamento
				lancamentos.add(registros, lanc);
				registros++;

			} // Fim do while que cria coleção de lancamentos

			return lancamentos;

			// Tratamento de erro SQL
		} catch (SQLException se) {
			throw new RuntimeException("Ocorreu SQL Exception erro no Banco de Dados." + se.getMessage());

			/*
			 * // Tratamento de erro JNDI }catch (NamingException ne){ throw new
			 * RuntimeException ("Ocorreu um erro no Banco de Dados." + ne.getMessage());
			 */
			// Lipeza dos recursos do JDBC

		} finally {
			try {
				results.close();
				stmt.close();
				conexao.close();
			} catch (SQLException e) {
				// System.out.println("Erro ao fechar as conexões com o banco de dados em
				// DAOOC.java "+e.getMessage());
				throw new SispagException("Erro ao fechar as conexões com o banco de dados");
				// e.printStackTrace();
			}
			/*
			 * if (results != null){ try {results.close();} catch (SQLException
			 * se){se.printStackTrace(System.err);} }
			 * 
			 * if (stmt != null){ try {stmt.close();} catch (SQLException
			 * se){se.printStackTrace(System.err);} }
			 */

		} // fim do bloco try-catch-finally

	}// fim do método
}

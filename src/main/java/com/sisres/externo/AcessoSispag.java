package com.sisres.externo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import com.acesso.verificacao.dasda;
import com.sisres.model.Divida;
import com.sisres.model.OC;
import com.sisres.model.Pessoa;
import com.sisres.model.SispagException;

import papem32.DButils;

public class AcessoSispag implements SispagInterface {

	DataSource ds = null;

	public AcessoSispag() {
		try {
			Context ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:/jdbc/sisres2");//jdbc/admseg
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public String getOc(String matfin) {
		// TODO: Migrado

		String ocCod = null;
		// final String QRYOCUSUARIO = "SELECT CCGSP_LOTA_OC oc_cod FROM TAB_CCGSP WHERE
		// trim(CCGSP_MATFIN) = ? and CCGSP_VINC=' ' AND (CCGSP_PROC_A||CCGSP_PROC_M) IN
		// (SELECT MAX (CCGSP_PROC_A||CCGSP_PROC_M) FROM TAB_CCGSP)";
		final String QRYOCUSUARIO = ""
				.concat("SELECT cgsp_oc oc_cod          ")
				.concat("FROM                           ")
				.concat("CGS_CORRENTE.rep_cgs_pessoal   ")
				.concat("WHERE  CGSP_NIP = ?  and CGSP_VINC1=' ' and CGSP_VINC2=' ' ") 
				.concat("and    CGSP_seq_rr = ' '  and cgsp_fol_tp='000' ")
				.concat("	    AND  CGSP_FOL_DT =      ")
				.concat("        (SELECT MAX(CGSP_FOL_DT) ")
				.concat("         FROM CGS_CORRENTE.rep_cgs_pessoal) ");
		try {
			Connection conexao = ds.getConnection();
			PreparedStatement smtOcUsuario = conexao.prepareStatement(QRYOCUSUARIO);
			// smtOcUsuario.setString(1, getIdOm(usuario));
			smtOcUsuario.setString(1, matfin);
			ResultSet rsOcUsuario = smtOcUsuario.executeQuery();
			while (rsOcUsuario.next()) {
				ocCod = rsOcUsuario.getString("oc_cod");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ocCod;
	}

	public String getOm(String matfin) {
		// TODO: Migrado
		// final String QRYOMUSUARIO = "select om_cod from spag_organizacao_militar
		// where om_id= ? ";
		String omCod = null;

		//final String QRYOMUSUARIO = "SELECT CCGSP_LOTA_OM as om_cod FROM TAB_CCGSP WHERE  trim(CCGSP_MATFIN) = ? AND CCGSP_VINC= '  ' AND  (CCGSP_PROC_A||CCGSP_PROC_M) IN (SELECT MAX (CCGSP_PROC_A||CCGSP_PROC_M) FROM TAB_CCGSP)";

		final String QRYOMUSUARIO = ""
				.concat("SELECT cgsp_om om_cod          ")
				.concat("FROM                           ")
				.concat("CGS_CORRENTE.rep_cgs_pessoal   ")
				.concat("WHERE  CGSP_NIP = ?  and CGSP_VINC1=' ' and CGSP_VINC2=' ' ")
				.concat("and    CGSP_seq_rr = ' '  and cgsp_fol_tp='000' ")
				.concat("	    AND  CGSP_FOL_DT =      ")
				.concat("        (SELECT MAX(CGSP_FOL_DT) ")
				.concat("         FROM CGS_CORRENTE.rep_cgs_pessoal) ");
		
		try {
			Connection conexao = ds.getConnection();
			PreparedStatement smtOmUsuario = conexao.prepareStatement(QRYOMUSUARIO);
			// smtOmUsuario.setString(1, getIdOm(matfin));
			smtOmUsuario.setString(1, matfin);
			ResultSet rsOmUsuario = smtOmUsuario.executeQuery();
			while (rsOmUsuario.next()) {
				omCod = rsOmUsuario.getString("om_cod");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return omCod;
	}

	// TODO: Migrar SISPAG2 - OK
	/*
	 * private static final String RETRIEVE_SQL_MATFIN =
	 * "select CGSP_MATFIN, CGSP_DEPBANC_AGE, CGSP_DEPBANC_BCO, CGSP_DEPBANC_CCR, "
	 * +
	 * "CGSP_LOTA_OC, CGSP_NOM, CGSP_CPF, CGSP_PT, CGSP_SIT, CGSM_DT_PROC_M, CGSM_DT_PROC_A  "
	 * + "from tab_cgsp, tab_cgsm where cgsp_matfin =?";
	 */
	/*
	 * private static final String RETRIEVE_SQL_MATFIN =
	 * "select CGSP_MATFIN, CGSP_DEPBANC_AGE, CGSP_DEPBANC_BCO, CGSP_DEPBANC_CCR, CGSP_LOTA_OC, CGSP_NOM, CGSP_CPF, CGSP_PT, CGSP_SIT, CGSP_PROC_M as m.CGSM_DT_PROC_M, m.CGSM_DT_PROC_A"
	 * +
	 * " from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsm m where trim(cgsp_matfin) = trim(?)"
	 * ;
	 */
	/*
	 * private static final String RETRIEVE_SQL_MATFIN =
	 * "select CGSP_MATFIN, CGSP_DEPBANC_AGE, CGSP_DEPBANC_BCO, CGSP_DEPBANC_CCR, CGSP_LOTA_OC, CGSP_NOM, CGSP_CPF, CGSP_PT, CGSP_SIT, m.CGSM_DT_PROC_M, m.CGSM_DT_PROC_A "
	 * +
	 * "from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsm m where trim(cgsp_matfin) = trim(?)"
	 * ;
	 */

	private static final String RETRIEVE_SQL_MATFIN = "select CGSP_MATFIN, CGSP_DEPBANC_AGE, CGSP_DEPBANC_BCO, CGSP_DEPBANC_CCR, CGSP_LOTA_OC, CGSP_NOM, CGSP_CPF, CGSP_PT, CGSP_SIT, m.CGSM_DT_PROC_M, m.CGSM_DT_PROC_A "
			+ "from CGS_HISTORICO.tab_cgsp, CGS_HISTORICO.tab_cgsm m where trim(cgsp_matfin) = trim(?) and cgsp_vinc='  '";

	// TODO: Migrar SISPAG2 - OK
	/*
	 * private static final String RETRIEVE_SQL_DIVIDA =
	 * "SELECT CGSF_MATFIN, round(sum(liquido),2) as totalliquido " +
	 * "from (SELECT CGSF_MATFIN, CGSF_SEGFIN, CGSF_PARC,  CGSF_VAL , (case CGSF_SEGFIN when '3' then CGSF_VAL "
	 * +
	 * "when '4' then CGSF_VAL else 0 end) as Credito, (case CGSF_SEGFIN when '5' then CGSF_VAL when '6' then CGSF_VAL "
	 * +
	 * "else 0 end) as Debito, (case CGSF_SEGFIN when '3' then CGSF_VAL when '4' then CGSF_VAL when '5' then CGSF_VAL  * (-1) "
	 * +
	 * "when '6' then CGSF_VAL  * (-1) else 0 end) as Liquido FROM TAB_CGSF WHERE CGSF_MATFIN =? "
	 * + "AND CGSF_STATUS = 'P' AND CGSF_VINC = ' ' ) group by CGSF_MATFIN ";
	 */

	private static final String RETRIEVE_SQL_DIVIDA = "SELECT CGSF_MATFIN, round(sum(liquido),2) as totalliquido "
			+ " from ( "
			+ " SELECT CGSF_MATFIN, CGSF_SEGFIN, CGSF_PARC,  CGSF_VAL , (case CGSF_SEGFIN when '3' then CGSF_VAL "
			+ "      when '4' then CGSF_VAL else 0 end) as Credito, (case CGSF_SEGFIN when '5' then CGSF_VAL when '6' then CGSF_VAL "
			+ "			                                          else 0 end) as Debito, (case CGSF_SEGFIN when '3' then CGSF_VAL when '4' then CGSF_VAL when '5' then CGSF_VAL  * (-1) "
			+ "			when '6' then CGSF_VAL  * (-1) else 0 end) as Liquido FROM CGS_HISTORICO.TAB_CGSF WHERE trim(CGSF_MATFIN) = trim(?) "
			+ "			AND CGSF_STATUS = 'P' AND CGSF_VINC = '  ' ) " + "            group by CGSF_MATFIN    ";

	// TODO: Migrar SISPAG2 - OK

	/*
	 * private static final String RETRIEVE_SQL_DIVHISTOC =
	 * " select ccgsp_lota_oc OC, om_indnav indnav, om_nome nome ,  (ccgsp_proc_a || ccgsp_proc_m)dt_inicio ,"
	 * +
	 * "  (ccgsp_proc_a || ccgsp_proc_m) dt_fim , CCGSP_DEPBANC_AGE Agencia, CCGSP_DEPBANC_CCR CC "
	 * + "  from tab_ccgsp, spag_organizacao_militar  " +
	 * "  where ccgsp_matfin= ? and  ccgsp_vinc= ' ' and " +
	 * "  ccgsp_proc_a||ccgsp_proc_m between ?  and   ?  and  trim(om_cod) = trim(ccgsp_lota_oc)"
	 * +
	 * "  group by CCGSP_DEPBANC_AGE, CCGSP_DEPBANC_CCR , ccgsp_proc_a || ccgsp_proc_m , ccgsp_lota_oc, om_indnav, om_nome "
	 * + "  order by dt_inicio, dt_fim, OC, CC";
	 */

	/*private static final String RETRIEVE_SQL_DIVHISTOC = " select ccgsp_lota_oc OC, om_indnav indnav, om_nome nome ,  (ccgsp_proc_a || ccgsp_proc_m)dt_inicio ,     (ccgsp_proc_a || ccgsp_proc_m) dt_fim , CCGSP_DEPBANC_AGE Agencia, CCGSP_DEPBANC_CCR CC "
			+ " from cgs_historico.tab_ccgsp, cgs_historico.spag_organizacao_militar   "
			+ " where trim(ccgsp_matfin)= ? and  ccgsp_vinc= '  ' and "
			+ " ccgsp_proc_a||ccgsp_proc_m between ?  and   ?  and  trim(om_cod) = trim(ccgsp_lota_oc) "
			+ " group by CCGSP_DEPBANC_AGE, CCGSP_DEPBANC_CCR , ccgsp_proc_a || ccgsp_proc_m , ccgsp_lota_oc, om_indnav, om_nome "
			+ " order by dt_inicio, dt_fim, OC, CC ";
*/
	
	public boolean verificarDisponibilidade() throws SispagException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		String statment = null;
		boolean estagio = false;
		Connection conn = null;

		try {
			conn = ds.getConnection();
			// Cria sql
			// TODO: Migrar SISPAG2 - OK
			// statment = "select * from tab_cgsm where cgsm_estag = '3'";
			statment = "select * from CGS_HISTORICO.tab_cgsm where cgsm_estag = '3'";
			stmt = conn.prepareStatement(statment);
			// Executa sql

			results = stmt.executeQuery();

			// Se tem um registro entï¿½o cgs no estagio 3
			if (results.next()) {
				// CGS no estágio 3
				estagio = true;
			}

			if (estagio) {
				return true;
			} else {
				// System.out.println("Sistema indisponível no momento.");
				return false;

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SispagException(e.getMessage());

		} finally {
			DButils.closeQuietly(results, stmt, conn);
		}
	}

	private String ajustaMatFin(String matFin) {
		if (matFin.trim().length() == 8) {
			matFin = matFin + " ";
		}
		return matFin;
	}

	public Divida getDivida(String matFin) throws SispagException {

		matFin = ajustaMatFin(matFin);

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection conn = null;

		try {
			conn = ds.getConnection();
			stmt = conn.prepareStatement(RETRIEVE_SQL_MATFIN);
			stmt.setString(1, matFin);

			// Executa sql
			rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Divida div = new Divida();
			div.setAgencia(rs.getString("CGSP_DEPBANC_AGE"));
			div.setBanco(rs.getString("CGSP_DEPBANC_BCO"));
			div.setContaCorrente(rs.getString("CGSP_DEPBANC_CCR"));

			// Concatena "20" ao ano
			div.setAnoProcessoGeracao("20" + rs.getString("CGSM_DT_PROC_A"));

			div.setMesProcessoGeracao(rs.getString("CGSM_DT_PROC_M"));

			/*
			 * instanciar objeto OC e preencher
			 */
			OC oc = new OC();
			oc.setOc(rs.getString("CGSP_LOTA_OC"));

			div.setOc(oc);

			/*
			 * instanciar objeto Pessoa e preencher
			 */
			Pessoa pes = new Pessoa();
			pes.setNome(rs.getString("CGSP_NOM"));
			pes.setCpf(rs.getString("CGSP_CPF"));
			pes.setPosto(rs.getString("CGSP_PT"));
			pes.setSituacao(rs.getString("CGSP_SIT"));

			div.setPessoa(pes);

			stmt2 = conn.prepareStatement(RETRIEVE_SQL_DIVIDA);
			stmt2.setString(1, matFin);
			rs1 = stmt2.executeQuery();

			rs1.next();

			div.setValor(rs1.getDouble("totalliquido"));

			// System.out.println("Passou getDiv");
			return div;
		} catch (Exception e) {
			// throw new SispagException("error.matfin.invalido");
			throw new SispagException("MATRÍCULA FINANCEIRA não encontrada");
		} finally {

			DButils.closeQuietly(rs);
			DButils.closeQuietly(stmt);
			DButils.closeQuietly(rs1, stmt2, conn);
		}
	}

	/**
	 * Metodo que retorna as dividas historicas em um array de trï¿½s dimensï¿½es
	 * Meu ARRAY 1ï¿½ OC 2ï¿½ Conta Corrente 3ï¿½ DADOS DA DIVIDA
	 */
	public String[][][] getDividaHist(String matFin, String processoInicioStr, String processoTerminoStr)
			throws SispagException {

		// Primeiro verifico as quebras de dados por oc neste periodo
		String[][][] divOc = getDivOC_CGS2(matFin, processoInicioStr, processoTerminoStr);

		return divOc;
	}

	private String[][][] getDivOC_CGS2(String matFin, String processoInicioStr, String processoTerminoStr)
			throws SispagException {

		matFin = ajustaMatFin(matFin);

		int pos = processoInicioStr.indexOf("/");
		String mesIni = processoInicioStr.substring(0, pos);
		String anoIni = processoInicioStr.substring(pos + 1);

		String anoMesIni = anoIni + mesIni; // 200801

		int poss = processoTerminoStr.indexOf("/");
		String mesTerm = processoTerminoStr.substring(0, poss);
		String anoTerm = processoTerminoStr.substring(poss + 1);

		String anoMesFim = anoTerm + mesTerm; // 200801

		String[][] listOC = listaDiferentes_OC_CGS2(1, matFin, anoMesIni, anoMesFim);// TODO: P32 remover o 1 nap
																						// precisa mais

		// se não houver cadastrado no CGS o militar no período solicitado listOC
		// retorna null
		String[][][] listDivOC;

		if (listOC == null) {
			listDivOC = null;
		} else {
			listDivOC = montaArray_OC_CC(listOC);
		}
		return listDivOC;
	}

	private String[][] listaDiferentes_OC_CGS2(int sit, String matFin, String anoMesIni, String anoMesFim)
			throws SispagException {

		if (sit == 1 || sit == 2 || sit == 3 || sit == 4) { // no corrente e no historico
			return obterDadosHistoricos_cgs2(matFin, anoMesIni, anoMesFim);
		}

		return null;

	}

	private String[][] obterDadosHistoricos_cgs2(String matFin, String anoMesIni, String anoMesFim)
			throws SispagException {

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			conn = ds.getConnection();
			String sql = "";
			/*sql += " select CGSP_OC OC, om_indnav indnav, om_nome nome , to_char(cgsp_fol_dt) dt_inicio, to_char(cgsp_fol_dt) dt_fim , CGSP_DEPBANC_AGE Agencia, CGSP_DEPBANC_CC CC ";
			sql += " from CGS_HISTORICO.REP_CGS_PESSOAL , CGS_HISTORICO.spag_organizacao_militar ";
			sql += " where CGSP_NIP||CGSP_SEQ_RR= ? and  CGSP_VINC1|| CGSP_VINC2= '  '";
			sql += "       and cgsp_fol_dt between to_number(?) and to_number(?)";
			sql += "       and  trim(om_cod) = trim(CGSP_OC)";
			sql += " group by CGSP_DEPBANC_AGE, CGSP_DEPBANC_CC , cgsp_fol_dt, CGSP_OC, om_indnav, om_nome";
			sql += " order by dt_inicio, dt_fim, OC, CC";
*/
			
			sql="select CGSP_OC OC, om_indnav indnav, om_nome nome , to_char(cgsp_fol_dt) dt_inicio, to_char(cgsp_fol_dt) dt_fim , CGSP_DEPBANC_AGE Agencia, CGSP_DEPBANC_CC CC \r\n"
					+ "from CGS_HISTORICO.REP_CGS_PESSOAL , CGS_HISTORICO.spag_organizacao_militar  \r\n"
					+ "where CGSP_NIP =? and CGSP_SEQ_RR= ? and  CGSP_VINC1= ' ' and CGSP_VINC2= ' ' \r\n"
					+ "      and  (om_cod) = (CGSP_OC) \r\n"
					+ "      and cgsp_fol_dt between to_number(?) and to_number(?) \r\n"
					+ "      group by CGSP_DEPBANC_AGE, CGSP_DEPBANC_CC , cgsp_fol_dt, CGSP_OC, om_indnav, om_nome\r\n"
					+ "      order by dt_inicio, dt_fim, OC, CC";
			
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, matFin.substring(0,8));
                        System.out.println("matFin.substring(0,8) " + matFin.substring(0,8));
			stmt.setString(2, matFin.substring(8));
                        System.out.println("matFin.substring(8) " + matFin.substring(8));
			stmt.setString(3, anoMesIni);
			stmt.setString(4, anoMesFim);

			rs = stmt.executeQuery();

			int qtd = 0;
			TreeMap<Integer, String[]> array_rs = new TreeMap<>();
			while (rs.next()) {

				String[] vec = new String[7];
				vec[0] = rs.getString("OC");
				vec[1] = rs.getString("INDNAV");
				vec[2] = rs.getString("NOME");
				vec[3] = rs.getString("DT_INICIO");
				vec[4] = rs.getString("DT_FIM");
				String rsAgencia = rs.getString("AGENCIA");
				if (rsAgencia == null) {
					vec[5] = "";
					/*
					 * Ten Luis Fernando O trecho acima refere-se ao conjunto de registros oriundos
					 * do SISPAG entre o período de 1994 e 1999 que não possuem a informação de
					 * Agência e conta Corrente nas tabelas históricas do pagamento.
					 */
				} else {
					vec[5] = rs.getString("AGENCIA");
				}
				String rsContaCorrente = rs.getString("CC");
				if (rsContaCorrente == null) {
					vec[6] = "";
				} else {
					vec[6] = rs.getString("CC");
				}
				array_rs.put(qtd, vec);
				qtd++;
			}

			if (qtd == 0)
				return null;

			String[][] ret = new String[qtd][7];

			for (Map.Entry<Integer, String[]> e : array_rs.entrySet()) {
				for (int i = 0; i < 7; i++)
					ret[e.getKey()][i] = e.getValue()[i];
			}

			return ret;

		} catch (SQLException e) {
			throw new SispagException("Erro ao conectar ao BD: " + e.getMessage());
		} finally {
			DButils.closeQuietly(rs, stmt, conn);
		}

	}

	private String[][][] montaArray_OC_CC(String[][] array_rs) {

		/*
		 * verifico quantas ocs existentes para esta divida para poder instanciar a
		 * primeira parte do array
		 */
		int quant = 1;

		String oc1, oc2 = null;
		String cc1, cc2 = null;

		oc1 = array_rs[0][0];
		oc2 = array_rs[0][0];

		// verifico quantas oc tem
		for (int i = 0; i < array_rs.length; i++) {

			if (!oc1.equals(oc2)) {

				quant++;

				oc2 = oc1;
			}

			oc1 = array_rs[i][0];

		}

		// instancio a primeira parte da oc

		String[][][] array_OC_CC = new String[quant][][];

		/*
		 * verifico quantas Contas Correntes em cada oc para poder instanciar a primeira
		 * parte do array
		 */

		int quantCC = 0;
		int quantOC = 0;
		int count = 0;
		int total_count = 0;

		oc1 = array_rs[0][0];
		oc2 = array_rs[0][0];

		// varro o meu array de oc
		for (int i = 0; i < array_rs.length; i++) {
			quantCC = 1;
			count = i;
			if (!oc1.equals(oc2)) {

				cc1 = array_rs[total_count][6];
				cc2 = array_rs[total_count][6];

				for (int a = 0; a < count - total_count; a++) {

					if (!cc1.equals(cc2)) {

						quantCC++;

						cc2 = cc1;

					}

					cc1 = array_rs[total_count + a][6];

				}

				total_count = count;

				// instancio segunda e terceira possiï¿½ï¿½o do array posição do array
				array_OC_CC[quantOC] = new String[quantCC][7];

				quantOC++;
				oc2 = oc1;
			}

			oc1 = array_rs[i][0];

		}

//TOD0 - Ten Luis Fernando - Nao deveria zerar a quantidade de CC para a ultima OC?
		quantCC = 1;
		// faï¿½o para ultima oc
		cc1 = array_rs[total_count][6];
		cc2 = array_rs[total_count][6];

		for (int a = 0; a < count - total_count; a++) {

			if (!cc1.equals(cc2)) {

				quantCC++;

				cc2 = cc1;

			}

			cc1 = array_rs[total_count + a][6];

		}

		// instancio segunda e terceira possição do array posição do array
		array_OC_CC[quantOC] = new String[quantCC][7];

		/*
		 * Monto o Array da seguinte forma
		 *
		 * 1 é quebra por oc 2 é quebra por cc 3 é Dados da informação da seguinte forma
		 * 
		 * posição 0 do array = Numero da oc posição 1 do array = indnav posição 2 do
		 * array = nome da oc posição 3 do array = dt_inicio posição 4 do array = dt_fim
		 * posição 5 do array = Agencia posição 6 do array = Conta Corrente
		 * 
		 */

		oc1 = array_rs[0][0];
		oc2 = array_rs[0][0];
		total_count = 0;

		int numoc = 0;
		int numcc = 0;

		// Pego a primeira posição do array
		array_OC_CC[numoc][numcc][0] = array_rs[0][0];
		array_OC_CC[numoc][numcc][1] = array_rs[0][1];
		array_OC_CC[numoc][numcc][2] = array_rs[0][2];
		array_OC_CC[numoc][numcc][3] = array_rs[0][3];
		array_OC_CC[numoc][numcc][5] = array_rs[0][5];

		// varro o meu array de oc
		for (int i = 0; i < array_rs.length; i++) {

			count = i;
			if (!oc1.equals(oc2)) {

				// System.out.println("Ocs diferentes!!!");

				// zero o contador das cc
				numcc = 0;

				// pego o num da conta corrente para dpoder conferir
				// System.out.println("Total Count: " + total_count);
				cc1 = array_rs[total_count][6];
				cc2 = array_rs[total_count][6];

				// Faï¿½o o loop para verificar a quebra por conta corrente
				for (int a = 0; a < count - total_count; a++) {

					// System.out.println("CC1 " + cc1 );
					// System.out.println("CC2 " + cc2 );

					if (!cc1.equals(cc2)) {

						// System.out.println("Ccs diferentes!!!");

						++numcc;

						// System.out.println("NumOC" + numoc);
						// System.out.println("NumCC" + numcc);
						// System.out.println("posição array" + (total_count +a -1));

						// Pego a primeira posição do array
						array_OC_CC[numoc][numcc][0] = array_rs[total_count + a - 1][0];
						array_OC_CC[numoc][numcc][1] = array_rs[total_count + a - 1][1];
						array_OC_CC[numoc][numcc][2] = array_rs[total_count + a - 1][2];
						array_OC_CC[numoc][numcc][3] = array_rs[total_count + a - 1][3];
						array_OC_CC[numoc][numcc][5] = array_rs[total_count + a - 1][5];

						// pego a ultima posição para colocar os dados da interaï¿½ï¿½o anterior
						if (numcc > 0) {
							// coloco a data final e a conta corrente do anterior
							array_OC_CC[numoc][numcc - 1][4] = array_rs[total_count + a - 2][4];
							array_OC_CC[numoc][numcc - 1][6] = array_rs[total_count + a - 2][6];
						}

						cc2 = cc1;

					}

					if (a == (count - total_count - 1)) {

						// System.out.println("Entrei na ultima iteraï¿½ï¿½o da CC");
						// System.out.println("NumOC: " + numoc);
						// System.out.println("NumCC: " + numcc);
						// System.out.println("Total Count: " + total_count);
						// System.out.println("A: " + a);
						// System.out.println("posição array: " + (total_count + a -1));

						// Coloco a data final da oc anterior

						// coloco a data final e a conta corrente do anterior
						array_OC_CC[numoc][numcc][4] = array_rs[total_count + a - 1][4];
						array_OC_CC[numoc][numcc][6] = array_rs[total_count + a - 1][6];

						// quando ocorre a ultima iteraï¿½ï¿½o da quebra por conta corrente
						array_OC_CC[numoc + 1][0][0] = array_rs[total_count + a][0];
						array_OC_CC[numoc + 1][0][1] = array_rs[total_count + a][1];
						array_OC_CC[numoc + 1][0][2] = array_rs[total_count + a][2];
						array_OC_CC[numoc + 1][0][3] = array_rs[total_count + a][3];
						array_OC_CC[numoc + 1][0][5] = array_rs[total_count + a][5];

					}

					cc1 = array_rs[total_count + a][6];

				} // final do for da cc

				total_count = count;

				oc2 = oc1;
				++numoc;

			} // final do if da diferenï¿½a das ocs

			oc1 = array_rs[i][0];

		} // final do for da oc

		// a ultima iteraï¿½ï¿½o da ultima oc
		// zero o contador das cc
		numcc = 0;

		// pego o num da conta corrente para dpoder conferir
		// System.out.println("Ultima oc");
		// System.out.println("Total Count: " + total_count);
		cc1 = array_rs[total_count][6];
		cc2 = array_rs[total_count][6];

		// Faï¿½o o loop para verificar a quebra por conta corrente
		for (int a = 0; a < count - total_count; a++) {

			// System.out.println("CC1 " + cc1 );
			// System.out.println("CC2 " + cc1 );

			if (!cc1.equals(cc2)) {

				// System.out.println("Ccs diferentes!!!");

				++numcc;

				// System.out.println("NumOC" + numoc);
				// System.out.println("NumCC" + numcc);
				// System.out.println("posição array" + (total_count +a -1));

				// Pego a primeira posição do array
				array_OC_CC[numoc][numcc][0] = array_rs[total_count + a - 1][0];
				array_OC_CC[numoc][numcc][1] = array_rs[total_count + a - 1][1];
				array_OC_CC[numoc][numcc][2] = array_rs[total_count + a - 1][2];
				array_OC_CC[numoc][numcc][3] = array_rs[total_count + a - 1][3];
				array_OC_CC[numoc][numcc][5] = array_rs[total_count + a - 1][5];

				// pego a ultima posição para colocar os dados da interaï¿½ï¿½o anterior
				if (numcc > 0) {
					// coloco a data final e a conta corrente do anterior
					array_OC_CC[numoc][numcc - 1][4] = array_rs[total_count + a - 2][4];
					array_OC_CC[numoc][numcc - 1][6] = array_rs[total_count + a - 2][6];
				}

				cc2 = cc1;

			}

			if (a == (count - total_count - 1)) {

				// System.out.println("Entrei na ultima iteraï¿½ï¿½o da CC");
				// System.out.println("NumOC: " + numoc);
				// System.out.println("NumCC: " + numcc);
				// System.out.println("Total Count: " + total_count);
				// System.out.println("A: " + a);
				// System.out.println("posição array: " + (total_count + a -1));

				// Coloco a data final da oc anterior

				// coloco a data final e a conta corrente do anterior
				array_OC_CC[numoc][numcc][4] = array_rs[count][4];
				array_OC_CC[numoc][numcc][6] = array_rs[count][6];

			}

			cc1 = array_rs[total_count + a][6];

		} // final do for da cc

		total_count = count;
		// coloco a data final e a conta corrente do anterior
		array_OC_CC[numoc][numcc][4] = array_rs[count][4];
		array_OC_CC[numoc][numcc][6] = array_rs[count][6];

		return array_OC_CC;
	}

	public Divida getDadosDivHistorica_cgs2(String matFin, String dataIni, String dataFim) throws SispagException {

		matFin = ajustaMatFin(matFin);

		Divida DividaItem = null;

		try {
			// DividaItem = getQuerySitu(1, matFin, dataIni, dataFim);//TODO: Remover
			// situacado nao precisa mais
			DividaItem = getQuerySitu_cgs2(matFin, dataIni, dataFim);
		} catch (Exception e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}

		return DividaItem;
	}

	private Divida getQuerySitu_cgs2(String matFin, String dataIni, String dataFim) throws SispagException {

		Connection conn = null;
		PreparedStatement stmtPes = null;
		ResultSet rsPes = null;
		PreparedStatement stmtFin = null;
		ResultSet rsFin = null;

		try {

			conn = ds.getConnection();

			String sqlp = "SELECT CGSP_NIP||CGSP_SEQ_RR MATFIN,          \r\n"
					+ "CGSP_DEPBANC_AGE AGENCIA,                     \r\n"
					+ "	       CGSP_DEPBANC_BCO BANCO,              \r\n"
					+ "	       CGSP_DEPBANC_CC CCR,                 \r\n"
					+ "	       CGSP_OC OC,                          \r\n"
					+ "	       CGSP_NOME NOME,                      \r\n"
					+ "	       CGSP_CPF_CNPJ CPF,                   \r\n"
					+ "	       CGSP_PT POSTO,                       \r\n"
					+ "	       CGSP_SITFUNC SITU                    \r\n"
					+ "	FROM  CGS_HISTORICO.REP_CGS_pessoal         \r\n"
					+ "	WHERE CGSP_SEQ_RR = ? 	      \r\n"
					+ "        AND CGSP_VINC1 = ' ' and CGSP_VINC2= ' '  \r\n"
					+ "        and CGSP_NIP = ? \r\n"
					+ "        AND CGSP_fol_dt BETWEEN ?  AND  ?     ";
/*
			sqlp += " SELECT CGSP_NIP||CGSP_SEQ_RR MATFIN,          ";
			sqlp += " CGSP_DEPBANC_AGE AGENCIA,                     ";
			sqlp += " 	       CGSP_DEPBANC_BCO BANCO,              ";
			sqlp += " 	       CGSP_DEPBANC_CC CCR,                 ";
			sqlp += " 	       CGSP_OC OC,                          ";
			sqlp += " 	       CGSP_NOME NOME,                      ";
			sqlp += " 	       CGSP_CPF_CNPJ CPF,                   ";
			sqlp += " 	       CGSP_PT POSTO,                       ";
			sqlp += " 	       CGSP_SITFUNC SITU                    ";
			sqlp += " 	FROM  CGS_HISTORICO.REP_CGS_pessoal         ";
			sqlp += " 	WHERE trim(CGSP_NIP||CGSP_SEQ_RR) = trim(?) ";
			sqlp += " 	      AND CGSP_fol_dt BETWEEN ?  AND  ?     ";
			sqlp += "         AND CGSP_VINC1|| CGSP_VINC2= '  '     ";
*/
			// conn = ds.getConnection(usuario, senhaCripto);

			// Cria sql
			stmtPes = conn.prepareStatement(sqlp);
			stmtPes.setString(1, matFin.substring(8));			
			stmtPes.setString(2, matFin.substring(0, 8));
			stmtPes.setString(3, dataIni);
			stmtPes.setString(4, dataFim);

			rsPes = stmtPes.executeQuery();

			if (!rsPes.next()) {
				// System.out.println("Matrícula não encontrada!");
				return null;
			}

			// System.out.println("Instancio Divida");
			Divida div = new Divida();
			div.setAgencia(rsPes.getString("AGENCIA"));
			div.setBanco(rsPes.getString("BANCO"));
			div.setContaCorrente(rsPes.getString("CCR"));

			/*
			 * instanciar objeto OC e preencher
			 */
			OC oc = new OC();
			oc.setOc(rsPes.getString("OC"));

			div.setOc(oc);

			/*
			 * instanciar objeto Pessoa e preencher
			 */
			Pessoa pes = new Pessoa();
			pes.setNome(rsPes.getString("NOME"));
			pes.setCpf(rsPes.getString("CPF"));
			pes.setPosto(rsPes.getString("POSTO"));
			pes.setSituacao(rsPes.getString("SITU"));

			div.setPessoa(pes);

			// RETRIEVE_SQL_CALCDIVIDA
			String sql = " SELECT CGSF_MATFIN, round(sum(liquido),2) as totalliquido\r\n"
					+ " from (\r\n"
					+ " 			       SELECT CGSF_NIP||CGSf_SEQ_RR as CGSF_MATFIN, CGSF_SEGFIN, CGSF_RUBR_COD, CGSF_VAL_CALC,\r\n"
					+ " 			              (case CgSF_SEGFIN \r\n"
					+ " 			                 when '3' then CGSF_VAL_CALC\r\n"
					+ " 			                 when '4' then CGSF_VAL_CALC \r\n"
					+ " 			                 else 0 \r\n"
					+ " 			               end\r\n"
					+ " 			               ) as Credito,\r\n"
					+ " 			               (case CGSF_SEGFIN\r\n"
					+ " 			                  when '5' then CGSF_VAL_CALC\r\n"
					+ " 			                  when '6' then CGSF_VAL_CALC \r\n"
					+ " 			                  else 0 \r\n"
					+ " 			                end) as Debito,\r\n"
					+ " 			              (case CGSF_SEGFIN \r\n"
					+ " 			                 when '3' then CGSF_VAL_CALC\r\n"
					+ " 			                 when '4' then CGSF_VAL_CALC \r\n"
					+ " 			                 when '5' then CGSF_VAL_CALC  * (-1)\r\n"
					+ " 			                 when '6' then CGSF_VAL_CALC  * (-1)\r\n"
					+ " 			                 else 0 \r\n"
					+ " 			               end) as Liquido\r\n"
					+ " 			        FROM  CGS_HISTORICO.REP_CGS_financeiro\r\n"
					+ " 			        WHERE CGSF_rubr_STATUS = 'EP' AND CGSF_VINC1 = ' ' and CGSF_VINC2 =' ' \r\n"
					+ "                         and CGSf_SEQ_RR = ? \r\n"
					+ " 			             AND CGSF_NIP = ? \r\n"
					+ " 			             AND CGSF_fol_dt BETWEEN ? AND ? \r\n"
					+ " 			) group by CGSF_MATFIN ";
/*
			sql += " SELECT CGSF_MATFIN, round(sum(liquido),2) as totalliquido";
			sql += " from (";
			sql += " 			       SELECT CGSF_NIP||CGSf_SEQ_RR as CGSF_MATFIN, CGSF_SEGFIN, CGSF_RUBR_COD, CGSF_VAL_CALC,";
			sql += " 			              (case CgSF_SEGFIN ";
			sql += " 			                 when '3' then CGSF_VAL_CALC";
			sql += " 			                 when '4' then CGSF_VAL_CALC ";
			sql += " 			                 else 0 ";
			sql += " 			               end";
			sql += " 			               ) as Credito,";
			sql += " 			               (case CGSF_SEGFIN";
			sql += " 			                  when '5' then CGSF_VAL_CALC";
			sql += " 			                  when '6' then CGSF_VAL_CALC ";
			sql += " 			                  else 0 ";
			sql += " 			                end) as Debito,";
			sql += " 			              (case CGSF_SEGFIN ";
			sql += " 			                 when '3' then CGSF_VAL_CALC";
			sql += " 			                 when '4' then CGSF_VAL_CALC ";
			sql += " 			                 when '5' then CGSF_VAL_CALC  * (-1)";
			sql += " 			                 when '6' then CGSF_VAL_CALC  * (-1)";
			sql += " 			                 else 0 ";
			sql += " 			               end) as Liquido";
			sql += " 			        FROM  CGS_HISTORICO.REP_CGS_financeiro";
			sql += " 			        WHERE trim(CGSF_NIP||CGSf_SEQ_RR) = trim(?) ";
			sql += " 			             AND CGSF_rubr_STATUS = 'EP' AND CGSF_VINC1 = ' ' and CGSF_VINC2 =' ' ";
			sql += " 			             AND CGSF_fol_dt BETWEEN ? AND ?";
			sql += " 			) group by CGSF_MATFIN ";*/

			stmtFin = conn.prepareStatement(sql);
			stmtFin.setString(1, matFin.substring(8));
			stmtFin.setString(2, matFin.substring(0,8));
			stmtFin.setString(3, dataIni);
			stmtFin.setString(4, dataFim);
			rsFin = stmtFin.executeQuery();
			rsFin.next();

			/*
			 * IMPLEMENTAR? BigDecimal bd = new BigDecimal(somaValor).setScale(2,
			 * RoundingMode.HALF_EVEN); div.setValor(bd.doubleValue());
			 */

			div.setValor(rsFin.getDouble("totalliquido"));

			return div;
		} catch (Exception e) {
			throw new SispagException(e.getMessage());
		} finally {
			DButils.closeQuietly(rsPes);
			DButils.closeQuietly(stmtPes);
			DButils.closeQuietly(rsFin, stmtFin, conn);
		}
	}

}
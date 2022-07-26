package com.acesso.verificacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sisres.externo.AcessoSispag;

public class VerificarPerfil /* implements IVerificarPerfil */ {
	private String idUsuario, idOm, nomeUsuario;
	private String idGrupo, descricaoGrupo;
//	private String ocCod, omCod;
	private boolean flagAplicacao;
	private Collection menuUsuario = new ArrayList<HashMap>();
	private Privilegios privilegios = new Privilegios();

	DataSource dataSource = null;
	
	public double bufferSenha = 0;

	/*
	 * ResultSet rsID, rsIDOm, rsVerificarGrupo, rsAcessoGrupoAplicativo,
	 * rsOcUsuario, rsOmUsuario, rsMenuUsuario, rsDescricaoGrupo, rsNome;
	 * PreparedStatement smtId, smtIdOm, smtVerificarGrupo,
	 * smtAcessoGrupoAplicativo, smtOcUsuario, smtOmUsuario, smtMenuUsuario,
	 * smtDescricaoGrupo, smtNome;
	 */
	public VerificarPerfil() {

		try {
			Context ic = new InitialContext();
			dataSource = (DataSource) ic.lookup("java:/jdbc/admseg");//jdbc/admseg jdbc/sisres2
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	// @Override
	public String getIdUsuario(String codigoUsuario) {
		try {
			Connection conexao = dataSource.getConnection();

			final String QRYBUSCARID = "SELECT ID FROM sissec.USUARIO WHERE CODIGO = ? ";

                        //final String QRYBUSCARID = "SELECT ID FROM USUARIO WHERE CODIGO = ? "; comentadp por vitor
                        
			PreparedStatement smtId = conexao.prepareStatement(QRYBUSCARID);
			smtId.setString(1, codigoUsuario);
			ResultSet rsID = smtId.executeQuery();
			while (rsID.next()) {
				idUsuario = rsID.getString("ID");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return idUsuario;
	}

	// @Override
	public String getIdOm(String codigoUsuario) {
		final String QRYBUSCARIDOM = "SELECT OM_ID FROM sissec.USUARIO WHERE CODIGO = ? ";
                
               // final String QRYBUSCARIDOM = "SELECT OM_ID FROM USUARIO WHERE CODIGO = ? "; comentatdo por vitor
                
		try {
			Connection conexao = dataSource.getConnection();

			PreparedStatement smtIdOm = conexao.prepareStatement(QRYBUSCARIDOM);
			smtIdOm.setString(1, codigoUsuario);
			ResultSet rsIDOm = smtIdOm.executeQuery();
			while (rsIDOm.next()) {
				idOm = rsIDOm.getString("OM_ID");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idOm;
	}

	// @Override
	public String getIdGrupoUsuario(String codigoUsuario) {

	//	final String QRYVERIFICARGRUPO = " SELECT GRUPO_USUARIO_USUARIO.GRUPO_USR_ID ID_GRUPO, CONTROLE_ACESSO.estado_dte DTE, GRUPO_USUARIO.descricao DESCRICAO_GRUPO "
	//			+ "  FROM GRUPO_USUARIO_USUARIO, aplicacao_GRUPO_USR, CONTROLE_ACESSO, GRUPO_USUARIO  "
	//			+ "  WHERE USUARIO_ID                    =   ?  AND " + // passar o id do usuario
	//			"  CONTROLE_ACESSO.grupo_usr_id        =  GRUPO_USUARIO_USUARIO.grupo_usr_id AND "
	//			+ "  GRUPO_USUARIO_USUARIO.grupo_usr_id  =  APLICACAO_GRUPO_USR.GRUPO_USR_ID   AND "
	//			+ "  GRUPO_USUARIO.id                    = GRUPO_USUARIO_USUARIO.grupo_usr_id  AND "
	//			+ "  aplicacao_GRUPO_USR.APLICACAO_ID    = (SELECT ID FROM APLICACAO WHERE SIGLA = 'SISRES') AND "
	//			+ "  aplicacao_GRUPO_USR.APLICACAO_ID    = CONTROLE_ACESSO.APLICACAO_ID ";
	//comentado por vitor 	
        
        //SISSEC
        
	    final String QRYVERIFICARGRUPO = "SELECT gu.GRUPO_USR_ID ID_GRUPO, "+ 
	    "c.estado_dte DTE, "+
	    "g.descricao DESCRICAO_GRUPO "+ 
            "FROM SISSEC.GRUPO_USUARIO_USUARIO gu, SISSEC.aplicacao_GRUPO_USR ag, SISSEC.CONTROLE_ACESSO c, SISSEC.GRUPO_USUARIO  g " +
	    "WHERE USUARIO_ID                    =   ? AND " +
	    "c.grupo_usr_id        =  gu.grupo_usr_id AND " +
	    "gu.grupo_usr_id  =  ag.GRUPO_USR_ID   AND "+
	    "g.id                    = gu.grupo_usr_id  AND "+
	    "ag.APLICACAO_ID    = (SELECT ID FROM SISSEC.APLICACAO WHERE SIGLA = 'SISRES') AND "+ 
	    "ag.APLICACAO_ID    = c.APLICACAO_ID"  ;
       
		
		
		
		try {
			Connection conexao = dataSource.getConnection();

			PreparedStatement smtVerificarGrupo = conexao.prepareStatement(QRYVERIFICARGRUPO);
			smtVerificarGrupo.setString(1, getIdUsuario(codigoUsuario));
			ResultSet rsVerificarGrupo = smtVerificarGrupo.executeQuery();
			while (rsVerificarGrupo.next()) {
				idGrupo = rsVerificarGrupo.getString("ID_GRUPO");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idGrupo;
	}

	public boolean getGrupoAcessaAplicacao(String codigoUsuario) {

		final String QRYACESSOGRUPOAPLICATIVO = " SELECT DECODE (count(*),0,'true','false') retorno FROM SISSEC.APLICACAO_GRUPO_USR  WHERE APLICACAO_ID = (SELECT ID FROM SISSEC.APLICACAO WHERE SIGLA = 'SISRES') "
				+ " AND GRUPO_USR_ID = ? ";
                
                //final String QRYACESSOGRUPOAPLICATIVO = " SELECT DECODE (count(*),0,'true','false') retorno FROM APLICACAO_GRUPO_USR  WHERE APLICACAO_ID = (SELECT ID FROM APLICACAO WHERE SIGLA = 'SISRES') "
		//		+ " AND GRUPO_USR_ID = ? ";
                //comentado por vitor
                
		try {

			Connection conexao = dataSource.getConnection();
			PreparedStatement smtAcessoGrupoAplicativo = conexao.prepareStatement(QRYACESSOGRUPOAPLICATIVO);
			smtAcessoGrupoAplicativo.setString(1, getIdGrupoUsuario(codigoUsuario)); //
			ResultSet rsAcessoGrupoAplicativo = smtAcessoGrupoAplicativo.executeQuery();
			while (rsAcessoGrupoAplicativo.next()) {
				flagAplicacao = rsAcessoGrupoAplicativo.getBoolean("retorno");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flagAplicacao;
	}

	/*
	 * //TODO: Migrar SISPAG2 - OK
	 * 
	 * @Override public String getOc(String codigoUsuario) { //CONSULTA ALTERADAD
	 * PARA VER A CONDICAO DO FINANCEIRO final String QRYOCUSUARIO =
	 * "select om_cod from spag_organizacao_militar where om_id=(select om_oc_id from  spag_organizacao_militar where om_ID= ? ) "
	 * ; try { Connection conexao = dataSource.getConnection(); PreparedStatement
	 * smtOcUsuario = conexao.prepareStatement(QRYOCUSUARIO);
	 * smtOcUsuario.setString(1, getIdOm(codigoUsuario)); ResultSet rsOcUsuario =
	 * smtOcUsuario.executeQuery(); while (rsOcUsuario.next()) { ocCod =
	 * rsOcUsuario.getString("om_cod"); } conexao.close(); } catch (SQLException e)
	 * { e.printStackTrace(); } return ocCod; }
	 * 
	 * //TODO: Migrar SISPAG2 - OK //@Override public String getOm(String
	 * codigoUsuario) { //CONSULTA ALTERADAD PARA VER A CONDICAO DO FINANCEIRO final
	 * String QRYOMUSUARIO =
	 * "select om_cod from spag_organizacao_militar where om_id= ? "; try {
	 * Connection conexao = dataSource.getConnection(); PreparedStatement
	 * smtOmUsuario = conexao.prepareStatement(QRYOMUSUARIO);
	 * smtOmUsuario.setString(1, getIdOm(codigoUsuario)); ResultSet rsOmUsuario =
	 * smtOmUsuario.executeQuery(); while (rsOmUsuario.next()) { omCod =
	 * rsOmUsuario.getString("om_cod"); } conexao.close(); } catch (SQLException e)
	 * { e.printStackTrace(); } return omCod; }
	 */
	
	/*P32 Movido para acessar a oc pelo CGS2 SISPAG2*/

	public String getOc(String usuario) {
		// TODO: Migrado
		String matfin = usuario.substring(usuario.length() - 8);
		return new AcessoSispag().getOc(matfin);
		/*String matfin = usuario.substring(usuario.length() - 8);
		final String QRYOCUSUARIO = "SELECT CCGSP_LOTA_OC oc_cod FROM TAB_CCGSP WHERE trim(CCGSP_MATFIN) = ? and CCGSP_VINC='  ' AND  (CCGSP_PROC_A||CCGSP_PROC_M) IN (SELECT MAX (CCGSP_PROC_A||CCGSP_PROC_M) FROM TAB_CCGSP)";
		try {dasda
			Connection conexao = dataSource.getConnection();
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
		return ocCod;*/
	}

//@Override
	public String getOm(String usuario) {
		
		String matfin = usuario.substring(usuario.length() - 8);
		
		return new AcessoSispag().getOm(matfin);
		
		// TODO: Migrado
		// final String QRYOMUSUARIO = "select om_cod from spag_organizacao_militar
		// where om_id= ? ";
		/*String matfin = usuario.substring(usuario.length() - 8);
		final String QRYOMUSUARIO = "SELECT CCGSP_LOTA_OM as om_cod FROM TAB_CCGSP WHERE  trim(CCGSP_MATFIN) = ? AND CCGSP_VINC= '  ' AND  (CCGSP_PROC_A||CCGSP_PROC_M) IN (SELECT MAX (CCGSP_PROC_A||CCGSP_PROC_M) FROM TAB_CCGSP)";

		try {
			Connection conexao = dataSource.getConnection();
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
		*/
	}

	// @Override
	public Privilegios getRetornaPrivilegioUsuario(String codigoUsuario) {
		//final String QRYMENUUSUARIO = " select ITEM_MENU.nome NomeItem , CONTROLE_ACESSO_MENU.privilegio Privilegio, ITEM_MENU.CAMINHO CAMINHO, "
		//		+ " CONTROLE_ACESSO_MENU.modo_operacao Operacao "
		//		+ " from   FORMULARIO, CONTROLE_ACESSO_MENU, GRUPO_USUARIO_USUARIO, ITEM_MENU, GRUPO_USUARIO, "
		//		+ "        CONTROLE_ACESSO, APLICACAO_GRUPO_USR "
		//		+ "   where  FORMULARIO.aplicacao_id            = (SELECT ID FROM APLICACAO WHERE SIGLA = 'SISRES') "
		//		+ "   and FORMULARIO.id                      = ITEM_MENU.formulario_id   "
		//		+ "   and FORMULARIO.id                      = CONTROLE_ACESSO_MENU.formulario_id"
		//		+ "   and FORMULARIO.id                      = CONTROLE_ACESSO.formulario_id"
		//		+ "   and ITEM_MENU.id                       = CONTROLE_ACESSO_MENU.item_menu_id"
		//		+ "   and CONTROLE_ACESSO_MENU.grupo_usr_id  = GRUPO_USUARIO.id"
		//		+ "   and GRUPO_USUARIO.id                   = GRUPO_USUARIO_USUARIO.grupo_usr_id"
		//		+ "   and GRUPO_USUARIO.id                   = CONTROLE_ACESSO.grupo_usr_id "
		//		+ "   and CONTROLE_ACESSO.formulario_id      = FORMULARIO.id"
		//		+ "   and CONTROLE_ACESSO.aplicacao_id       = FORMULARIO.aplicacao_id"
		//		+ "   and APLICACAO_GRUPO_USR.grupo_usr_id   = GRUPO_USUARIO.id"
		//		+ "   and APLICACAO_GRUPO_USR.aplicacao_id   = FORMULARIO.aplicacao_id"
		//		+ "   and CONTROLE_ACESSO_MENU.privilegio <> '2'" + "   and CONTROLE_ACESSO_MENU.MODO_OPERACAO <> '0'"
		//		+ "   and GRUPO_USUARIO_USUARIO.grupo_usr_id = ? " + "   order by to_char(operacao) ";
		//comentado por vitor 		
                
                //SISSEC
                
		String QRYMENUUSUARIO= "SELECT "
				+ "    m.nome              nomeitem,   "
				+ "    cm.privilegio       privilegio,   "
				+ "    m.caminho           caminho,   "
				+ "    cm.modo_operacao    operacao   "
				+ "FROM   "
				+ "    sissec.formulario             f,   "
				+ "    sissec.controle_acesso_menu   cm,   "
				+ "    sissec.grupo_usuario_usuario  gu,   "
				+ "    sissec.item_menu              m,   "
				+ "    sissec.grupo_usuario          g,   "
				+ "    sissec.controle_acesso        c,   "
				+ "    sissec.aplicacao_grupo_usr    a   "
				+ "WHERE   "
				+ "        f.id = m.formulario_id   "
				+ "    AND f.id = cm.formulario_id   "
				+ "    AND f.id = c.formulario_id   "
				+ "    AND m.id = cm.item_menu_id   "
				+ "    AND cm.grupo_usr_id = g.id   "
				+ "    AND g.id = gu.grupo_usr_id   "
				+ "    AND g.id = c.grupo_usr_id   "
				+ "    AND c.formulario_id = f.id   "
				+ "    AND c.aplicacao_id = f.aplicacao_id   "
				+ "    AND a.grupo_usr_id = g.id   "
				+ "    AND a.aplicacao_id = f.aplicacao_id   "
				+ "    AND cm.privilegio <> '2'   "
				+ "    AND f.aplicacao_id = (   "
				+ "        SELECT   "
				+ "            id   "
				+ "        FROM   "
				+ "            sissec.aplicacao   "
				+ "        WHERE   "
				+ "            sigla = 'SISRES'   "
				+ "    )   "
				+ "    AND cm.modo_operacao <> '0'   "
				+ "    AND gu.grupo_usr_id = ?   "
				+ "ORDER BY   "
				+ "    to_char(operacao)";
             
		try {
			Connection conexao = dataSource.getConnection();
			PreparedStatement smtMenuUsuario = conexao.prepareStatement(QRYMENUUSUARIO);
			smtMenuUsuario.setString(1, getIdGrupoUsuario(codigoUsuario));
			ResultSet rsMenuUsuario = smtMenuUsuario.executeQuery();
			if (rsMenuUsuario.next()) {
				ResultSetMetaData rsmd = rsMenuUsuario.getMetaData();
				int iCol = rsmd.getColumnCount();
				do {
					HashMap<String, Object> tmp = new HashMap<String, Object>();
					for (int i = 1; i <= iCol; i++) {
						tmp.put(rsmd.getColumnName(i).toUpperCase(), rsMenuUsuario.getObject(i));
					}
					menuUsuario.add(tmp);
				} while (rsMenuUsuario.next());
			}
			conexao.close();
		} catch (SQLException e) {
			System.out.println("Erro no sql");
		}
		privilegios.setMenuUsuario((ArrayList<HashMap>) menuUsuario);

		return privilegios;
	}

	// @Override
	public String getPerfil(String codigoUsuario) {
		
            //SISSEC
            final String QRYBUSCARPERFIL = "SELECT DESCRICAO FROM sissec.GRUPO_USUARIO WHERE ID = ? ";
            
            //final String QRYBUSCARPERFIL = "SELECT DESCRICAO FROM GRUPO_USUARIO WHERE ID = ? ";
            //comentado por vitor
		try {
			Connection conexao = dataSource.getConnection();
			PreparedStatement smtDescricaoGrupo = conexao.prepareStatement(QRYBUSCARPERFIL);
			smtDescricaoGrupo.setString(1, getIdGrupoUsuario(codigoUsuario));
			ResultSet rsDescricaoGrupo = smtDescricaoGrupo.executeQuery();
			while (rsDescricaoGrupo.next()) {
				descricaoGrupo = rsDescricaoGrupo.getString("DESCRICAO");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return descricaoGrupo;
	}

	// @Override
	public String getNome(String codigoUsuario) {
	    //SISSEC
            final String QRYBUSCARNOME = "SELECT NOME_GUERRA FROM sissec.USUARIO WHERE  CODIGO = ?";
            
           // final String QRYBUSCARNOME = "SELECT NOME_GUERRA FROM USUARIO WHERE  CODIGO = ?";
           //comentado por vitor
            
		try {
			Connection conexao = dataSource.getConnection();
			PreparedStatement smtNome = conexao.prepareStatement(QRYBUSCARNOME);
			smtNome.setString(1, codigoUsuario);
			ResultSet rsNome = smtNome.executeQuery();
			while (rsNome.next()) {
				nomeUsuario = rsNome.getString("NOME_GUERRA");
			}
			conexao.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nomeUsuario;
	}
	/*
	 * public void fecharJDBCVariaveis(ServletContext context) { if (rsID != null) {
	 * try { rsID.close(); } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (smtId != null) { try { smtId.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * if (rsIDOm != null) { try { rsIDOm.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * if (smtIdOm != null) { try { smtIdOm.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * if (rsVerificarGrupo != null) { try { rsVerificarGrupo.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (smtVerificarGrupo != null) { try { smtVerificarGrupo.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (rsAcessoGrupoAplicativo != null) { try { rsAcessoGrupoAplicativo.close();
	 * } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (smtAcessoGrupoAplicativo != null) { try {
	 * smtAcessoGrupoAplicativo.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * if (rsOcUsuario != null) { try { rsOcUsuario.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } }
	 * 
	 * if (smtOcUsuario != null) { try { smtOcUsuario.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } }
	 * 
	 * if (rsOmUsuario != null) { try { rsOmUsuario.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } }
	 * 
	 * if (smtOmUsuario != null) { try { smtOmUsuario.close(); } catch (SQLException
	 * e) { e.printStackTrace(); } }
	 * 
	 * if (rsMenuUsuario != null) { try { rsMenuUsuario.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (smtMenuUsuario != null) { try { smtMenuUsuario.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (rsDescricaoGrupo != null) { try { rsDescricaoGrupo.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (smtDescricaoGrupo != null) { try { smtDescricaoGrupo.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (rsNome != null) { try { rsNome.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * if (smtNome != null) { try { smtNome.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } } } // end da classe
	 */
//TODO: P32 @Override nao precisa mais aqui passar para usuarioService
	/*
	 * public String codificaSenha(String senha) { bufferSenha = 0; for (int i = 0;
	 * i < senha.length(); i++) { bufferSenha = bufferSenha + (Math.floor((int)
	 * Math.pow(((int) senha.charAt(i)), 2) / (i + 1))); } return "S" + (int)
	 * bufferSenha; }
	 */
}

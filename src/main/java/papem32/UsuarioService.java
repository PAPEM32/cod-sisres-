package papem32;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sisres.sec.AdmSegPwHash;

//TODO: Servicos de usuariso como nome, oc, perfil, ...
public class UsuarioService {

	DataSource ds;
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	public UsuarioService() {
		try {
			Context ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:/jdbc/admseg"); //jdbc/admseg jdbc/sisres2
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public boolean verificaSenha(String ususario, String senha) {

		boolean ok = false;
                //SISSEC
                String sql = "select 1 from sissec.usuario where codigo=? and senha_aplicacao = ?";
		//String sql = "select 1 from usuario where codigo=? and senha_aplicacao = ?";
                //comentado por vitor

		Connection c = null;
		PreparedStatement s = null;
		try {
			c = ds.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, ususario);
			s.setString(2, new AdmSegPwHash().codificaSenha(senha));

			ResultSet rs = s.executeQuery();

			ok = rs.next();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			DButils.closeQuietly(s);
			DButils.closeQuietly(c);
		}
		return ok;

	}

	public void alteraSenha(String usu, String novaSenha) {
		String sql = "";
                //SISSEC
                sql += " update sissec.USUARIO set senha_aplicacao = ?   ";
		//sql += " update USUARIO set senha_aplicacao = ?   ";
                //comentado por vitor
		sql += " where codigo = ?                         ";

		Connection c = null;
		PreparedStatement s = null;
		try {
			c = ds.getConnection();
			s = c.prepareStatement(sql);
			s.setString(1, new AdmSegPwHash().codificaSenha(novaSenha));
			s.setString(2, usu);
			s.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			DButils.closeQuietly(s);
			DButils.closeQuietly(c);
		}
	}

	public String getRole(String usuario) {

		String role = "";
		PreparedStatement s = null;
		ResultSet r = null;
		Connection c = null;
		try {

			c = ds.getConnection();

                        // SISSEC
                       
			String sql = //"select GRUPO_USUARIO.descricao ROLE_USUARIO FROM GRUPO_USUARIO_USUARIO, aplicacao_GRUPO_USR, CONTROLE_ACESSO, GRUPO_USUARIO WHERE USUARIO_ID = (select id from usuario where codigo= ?)  AND CONTROLE_ACESSO.grupo_usr_id = GRUPO_USUARIO_USUARIO.grupo_usr_id AND GRUPO_USUARIO_USUARIO.grupo_usr_id = APLICACAO_GRUPO_USR.GRUPO_USR_ID AND GRUPO_USUARIO.id = GRUPO_USUARIO_USUARIO.grupo_usr_id AND aplicacao_GRUPO_USR.APLICACAO_ID = (SELECT ID FROM APLICACAO WHERE SIGLA = 'SISRES')AND aplicacao_GRUPO_USR.APLICACAO_ID = CONTROLE_ACESSO.APLICACAO_ID";
					"SELECT\n"
					+ "    g.descricao role_usuario\n"
					+ "FROM\n"
					+ "    sissec.grupo_usuario_usuario  gu,\n"
					+ "    sissec.aplicacao_grupo_usr    ag,\n"
					+ "    sissec.controle_acesso        c,\n"
					+ "    sissec.grupo_usuario          g\n"
					+ "WHERE\n"
					+ "        c.grupo_usr_id = gu.grupo_usr_id\n"
					+ "    AND gu.grupo_usr_id = ag.grupo_usr_id\n"
					+ "    AND g.id = gu.grupo_usr_id\n"
					+ "    AND ag.aplicacao_id = c.aplicacao_id\n"
					+ "    AND usuario_id = (\n"
					+ "        SELECT\n"
					+ "            id\n"
					+ "        FROM\n"
					+ "            sissec.usuario u\n"
					+ "        WHERE\n"
					+ "            codigo = ?\n"
					+ "    )\n"
					+ "    AND ag.aplicacao_id = (\n"
					+ "        SELECT\n"
					+ "            id\n"
					+ "        FROM\n"
					+ "            sissec.aplicacao\n"
					+ "        WHERE\n"
					+ "            sigla = 'SISRES'\n"
					+ "    )";

             /*           String sql = //"select GRUPO_USUARIO.descricao ROLE_USUARIO FROM GRUPO_USUARIO_USUARIO, aplicacao_GRUPO_USR, CONTROLE_ACESSO, GRUPO_USUARIO WHERE USUARIO_ID = (select id from usuario where codigo= ?)  AND CONTROLE_ACESSO.grupo_usr_id = GRUPO_USUARIO_USUARIO.grupo_usr_id AND GRUPO_USUARIO_USUARIO.grupo_usr_id = APLICACAO_GRUPO_USR.GRUPO_USR_ID AND GRUPO_USUARIO.id = GRUPO_USUARIO_USUARIO.grupo_usr_id AND aplicacao_GRUPO_USR.APLICACAO_ID = (SELECT ID FROM APLICACAO WHERE SIGLA = 'SISRES')AND aplicacao_GRUPO_USR.APLICACAO_ID = CONTROLE_ACESSO.APLICACAO_ID";
					"SELECT\n"
					+ "    g.descricao role_usuario\n"
					+ "FROM\n"
					+ "    grupo_usuario_usuario  gu,\n"
					+ "    aplicacao_grupo_usr    ag,\n"
					+ "    controle_acesso        c,\n"
					+ "    grupo_usuario          g\n"
					+ "WHERE\n"
					+ "        c.grupo_usr_id = gu.grupo_usr_id\n"
					+ "    AND gu.grupo_usr_id = ag.grupo_usr_id\n"
					+ "    AND g.id = gu.grupo_usr_id\n"
					+ "    AND ag.aplicacao_id = c.aplicacao_id\n"
					+ "    AND usuario_id = (\n"
					+ "        SELECT\n"
					+ "            id\n"
					+ "        FROM\n"
					+ "            usuario u\n"
					+ "        WHERE\n"
					+ "            codigo = ?\n"
					+ "    )\n"
					+ "    AND ag.aplicacao_id = (\n"
					+ "        SELECT\n"
					+ "            id\n"
					+ "        FROM\n"
					+ "            aplicacao\n"
					+ "        WHERE\n"
					+ "            sigla = 'SISRES'\n"
					+ "    )"; */ //comentado por Vitor
			s = c.prepareStatement(sql);
			s.setString(1, usuario);
			r = s.executeQuery();

			while (r.next()) {
				role = r.getString(1);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			DButils.closeQuietly(r, s, c);
		}
		return role;
	}

}
